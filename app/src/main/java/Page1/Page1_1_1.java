package Page1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

    private ArrayList<String > mySpot = new ArrayList<String >();

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

        // 리사이클러뷰 헤더
        name.add("첫번째");
        name.add("두번째");
        name.add("세번째");
        name.add("네번째");

                //리사이클러뷰 안 리서이클러뷰에 들어갈 데이터
        items.add(new Recycler_item("", "경복궁", "1234", "역사"));
        items.add(new Recycler_item("", "창덕궁", "2345", "역사"));
        items.add(new Recycler_item("", "덕수궁", "3456", "역사"));
        items.add(new Recycler_item("", "창경궁", "4567", "역사"));
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
}
