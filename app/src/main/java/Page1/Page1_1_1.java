package Page1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hansol.spot_200510_hs.R;

import java.util.ArrayList;
import java.util.List;

import DB.DbOpenHelper;

public class Page1_1_1 extends AppCompatActivity {
    Page1_1_1_Adapter adapter;
    ArrayList<String> name = new ArrayList<>();
    private List<Recycler_item> items = new ArrayList<Recycler_item>();

    private ArrayList<String > mySpot = new ArrayList<String >();   // 타이틀 저장
    private ArrayList<String> myContentID = new ArrayList<String >();  // contentId저장
    private ArrayList<String> myImage = new ArrayList<String >();  // 이미지저장
    private ArrayList<String> myType = new ArrayList<String >();  // 타입 저장

    private DbOpenHelper mDbOpenHelper;
    String sort = "userid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1_1_1);

        // 리사이클러뷰 설정
        RecyclerView recyclerView = findViewById(R.id.page1_1_1_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Page1_1_1_Adapter(name, items);
        recyclerView.setAdapter(adapter);

        // DB열기
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        showDatabase(sort);

        // 리사이클러뷰 헤더
        name.add("첫번째");
        name.add("두번째");
        name.add("세번째");
        name.add("네번째");

        for (int i = 0 ; i < mySpot.size() ; i++) {
            items.add(new Recycler_item(myImage.get(i), mySpot.get(i), myContentID.get(i), myType.get(i)));
        }

        adapter.notifyDataSetChanged();

//                //리사이클러뷰 안 리서이클러뷰에 들어갈 데이터
//        items.add(new Recycler_item("", "경복궁", "1234", "역사"));
//        items.add(new Recycler_item("", "창덕궁", "2345", "역사"));
//        items.add(new Recycler_item("", "덕수궁", "3456", "역사"));
//        items.add(new Recycler_item("", "창경궁", "4567", "역사"));
    }

    //리사이클러뷰 안 리사이클러뷰 데이터 구조
    public class Recycler_item {
        String image;
        String title;
        String contentviewID;
        String type;

        String getImage() {
            return this.image;
        }

        String getTitle() {
            return this.title;
        }

        String getContentviewID() {
            return this.contentviewID;
        }

        String getType() {
            return this.type;
        }

        Recycler_item(String image, String title, String contentviewID, String type) {
            this.image = image;
            this.title = title;
            this.contentviewID = contentviewID;
            this.type = type;
        }
    }

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.selectColumns();
        //iCursor.moveToFirst();
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        mySpot.clear();
        myContentID.clear();
        myImage.clear();
        myType.clear();

        while(iCursor.moveToNext()){
            String tempID = iCursor.getString(iCursor.getColumnIndex("userid"));
            tempID = setTextLength(tempID,10);
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);
            String tempImage = iCursor.getString(iCursor.getColumnIndex("image"));
            tempImage = setTextLength(tempImage, 100);
            String tempType = iCursor.getString(iCursor.getColumnIndex("type"));
            tempType = setTextLength(tempType, 10);

            mySpot.add(tempName);
            myContentID.add(tempID);
            myImage.add(tempImage);
            myType.add(tempType);
        }
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
