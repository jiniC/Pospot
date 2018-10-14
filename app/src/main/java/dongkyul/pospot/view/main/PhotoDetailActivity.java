package dongkyul.pospot.view.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import dongkyul.pospot.R;

public class PhotoDetailActivity extends AppCompatActivity {

    ImageView mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mPlace = findViewById(R.id.imageView);
        Bundle mBundle = getIntent().getExtras();
        if(mBundle != null){
            mPlace.setImageResource(mBundle.getInt("Image"));
        }
    }
}