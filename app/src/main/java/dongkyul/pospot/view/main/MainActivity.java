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

        Button btnMyTour = (Button) findViewById(R.id.btnMyTour);
        Button btnMain = (Button) findViewById(R.id.btnMain);

//        btnMyTour.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PhotoMainActivity.class);
//                startActivity(intent);
//            }
//        });

        btnMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainMapActivity.class);
                startActivity(intent);
            }
        });
    }
}