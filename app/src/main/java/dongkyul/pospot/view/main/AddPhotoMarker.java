package dongkyul.pospot.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;


public class AddPhotoMarker extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photomarker);
        init();

        // 클릭 마커 위치
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        double pointLat = extras.getDouble("pointLat");
        double pointLon = extras.getDouble("pointLon");
        Toast.makeText(
                getApplicationContext(),
                "앨범이 저장될 위치 - \n위도: " + pointLat + "\n경도: " + pointLon,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void init() {
        super.init();

        Button btnHome = (Button)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);
        Button btnViewFilter = (Button)findViewById(R.id.btnViewFilter);
        btnViewFilter.setOnClickListener(this);
        Button btnAddPhoto = (Button)findViewById(R.id.btnAddPhoto);
        btnViewFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnHome:
                Intent intent = new Intent(AddPhotoMarker.this,MainMapActivity.class);
                startActivity(intent);
                Toast.makeText(
                        getApplicationContext(),
                        "홈버튼클릭!",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.btnViewFilter:
                // 필터 사진 목록보기로 이동
                break;
            case R.id.btnAddPhoto:
                // 사진 추가하기 화면으로 이동
                break;
        }
    }
}