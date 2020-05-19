package Page2_1_X;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hansol.spot_200510_hs.R;

public class Page2_1_X_ImageSelected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_1_x_image_selected);
    }

    // 이전으로 돌아가기
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
