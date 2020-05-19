package Page2_1_X;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.hansol.spot_200510_hs.R;

import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Page2_1_X extends AppCompatActivity {
    int station_code = 9999;
    String[] arr_line = null;
    String[] _name = new String[station_code];           //txt에서 받은 역이름
    String[] _areaCode = new String[station_code];       //txt에서 받은 지역코드
    String[] _sigunguCode = new String[station_code];    //txt에서 받은 시군구코드
    String[] _x = new String[station_code];              //txt에서 받은 x좌표
    String[] _y = new String[station_code];              //txt에서 받은 y좌표
    String[] _benefitURL = new String[station_code];     //txt에서 받은 혜택url
    String st_name, areaCode, sigunguCode, benefitURL;            //전달받은 역의 지역코드, 시군구코드, 혜택URL
    Double x, y;                                         //전달받은 역의 x,y 좌표

    //returnResult를 줄바꿈 단위로 쪼개서 넣은 배열
    String name_1[];

    //name_1를 "  " 단위로 쪼개서 넣은 배열
    String name_2[] = new String[5];

    //api 관련
    int page = 1;     //api 페이지 수
    boolean isLoadData = true;
    String returnResult, url;
    String Url_front = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=";
    String contentTypeId = "12", cat1 = "", cat2 = "";
    ProgressDialog asyncDialog;
    ProgressBar progressBar;
    Button  gift;

    boolean buttonState = false;
    Button add_btn;
    TextView title;
    String spot_title;
    String contentID;

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_1_x);

        title = (TextView)findViewById(R.id.page2_1_x_title);

        scrollView = (ScrollView)findViewById(R.id.page2_1_x_scrollView);
        scrollView.smoothScrollBy(0,0);

        // 프레그먼트뷰로 넘길 ImageView
        ArrayList<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.page1_1_mint_box);
        imageList.add(R.drawable.box_round3);
        imageList.add(R.drawable.box_round2);

        // 뷰페이져와 뷰페이져 어댑터 연결
        ViewPager viewPager = findViewById(R.id.page2_1_x_viewpager);
        Page2_1_X_FragmentAdapter page2_1_1_fragmentAdapter = new Page2_1_X_FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(page2_1_1_fragmentAdapter);

        // 뷰페이저 미리보기 만들기 -> 마진값주기
        viewPager.setClipToPadding(false);
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (30 * d);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 2);

        // 프레그먼트 어댑터에 프레그먼트 추가, Image갯수만큼
        for (int i = 0 ; i < imageList.size() ; i++) {
            Page2_1_X_ImageFragment page2_1_x_imageFragment = new Page2_1_X_ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", imageList.get(i));
            page2_1_x_imageFragment.setArguments(bundle);
            page2_1_1_fragmentAdapter.addItem(page2_1_x_imageFragment);
        }
        page2_1_1_fragmentAdapter.notifyDataSetChanged();

        // 앞에서 값 받아오기
        Intent intent = getIntent();
        spot_title = intent.getStringExtra("title");
        contentID = intent.getStringExtra("contentID");

        title.setText(spot_title);

        // 맵뷰 구현
        MapView mapView = new MapView(this);
        RelativeLayout mapViewContainer = (RelativeLayout)findViewById(R.id.page2_1_1_map);
        mapViewContainer.addView(mapView);

        // 버튼 눌림효과
        add_btn = (Button)findViewById(R.id.page2_1_1_like);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼 이미 눌려있을 때
                if (buttonState) {
                    buttonState = false;
                    add_btn.setBackgroundResource(R.drawable.ic_icon_add_float_2);
                } else {
                    // 버튼 처음 누를 때
                    buttonState = true;
                    add_btn.setBackgroundResource(R.drawable.ic_icon_add_float_1);
                }
            }
        });

        settingAPI_Data();
    }

    //txt 돌려 역 비교할 배열 만들기(이름 지역코드 동네코드)<-로 구성
    private void settingList(){

        String readStr = "";
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open("station_code.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            while (((str = reader.readLine()) != null)){ readStr += str + "\n";}
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] arr_all = readStr.split("\n"); //txt 내용을 줄바꿈 기준으로 나눈다.

        //한 줄의 값을 띄어쓰기 기준으로 나눠서, 역명/지역코드/시군구코드 배열에 넣는다.
        for(int i=0; i <arr_all.length; i++) {
            arr_line = arr_all[i].split(" ");

            _name[i] = arr_line[0];         //서울
            _areaCode[i] = arr_line[1];     //1
            _sigunguCode[i] = arr_line[2];  //0
            _y[i] = arr_line[3];            //y좌표
            _x[i] = arr_line[4];            //x좌표
            _benefitURL[i] = arr_line[5];
        }
    }


    //앞 액티비티에서 선택된 역과 같은 역을 찾는다.
    private void compareStation(){
        for(int i=0; i<_name.length; i++){
            if(st_name.equals(_name[i])){
                areaCode = _areaCode[i];
                sigunguCode = _sigunguCode[i];
                y = Double.parseDouble(_y[i]);
                x = Double.parseDouble(_x[i]);
                benefitURL = _benefitURL[i];
            }
        }
    }

    // api 연결 후 값 정제
    private void settingAPI_Data() {
        SearchTask task = new SearchTask();
        try {
            String RESULT = task.execute().get();
            Log.i("전달 받은 값", RESULT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // api 연결
    class SearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d("시작", "시작");

            url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=" +
                    "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D" +
                    "&MobileApp=AppTest&MobileOS=ETC" +
                    "&contentId=" + contentID +
                    "&defaultYN=Y&addrinfoYN=Y&mapinfoYN=Y";

            URL xmlUrl;
            returnResult = "";

            try {
                boolean item = false;
                boolean title = false;
                boolean tel = false;
                boolean addr1 = false;
                boolean mapx =false;
                boolean mapy = false;

                xmlUrl = new URL(url);
                Log.d("url", url);
                xmlUrl.openConnection().getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG: {
                            if (parser.getName().equals("item")) {
                                item = true;
                            }
                            if (parser.getName().equals("title")) {
                                title = true;
                            }
                            if (parser.getName().equals("tel")) {
                                tel = true;
                            }
                            if (parser.getName().equals("addr1")) {
                                addr1 = true;
                            }
                            if (parser.getName().equals("mapx")) {
                                mapx = true;
                            }
                            if (parser.getName().equals("mapy")) {
                                mapy = true;
                            }
                            break;
                        }

                        case XmlPullParser.TEXT: {
                            if (title) {
                                returnResult += parser.getText() + "  ";
                                title = false;
                            }
                            if (tel) {
                                returnResult += parser.getText() + "  ";
                                tel = false;
                            }
                            if (addr1) {
                                returnResult += parser.getText() + "  ";
                                addr1 = false;
                            }
                            if (mapx) {
                                returnResult += parser.getText() + "  ";
                                mapx = false;
                            }
                            if (mapy) {
                                returnResult += parser.getText() + "  ";
                                mapy = false;
                            }
                            break;
                        }

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                break;
                            }
                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnResult;
        }
    }
}
