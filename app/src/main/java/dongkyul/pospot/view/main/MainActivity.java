package dongkyul.pospot.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGridPhoto = (Button) findViewById(R.id.btnGridPhoto);
        Button btnMain = (Button) findViewById(R.id.btnMain);
        //Button btnTourAPI = (Button) findViewById(R.id.btnTourAPI);

        btnGridPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotoMainActivity.class);
                startActivity(intent);
            }
        });
        btnMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainMapActivity.class);
                startActivity(intent);
            }
        });
//        btnTourAPI.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TourApiActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}