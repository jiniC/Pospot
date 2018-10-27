package dongkyul.pospot.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class PhotoRealmDBCheck extends BaseActivity {

    String MarkerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_db);
        init();
    }


    @Override
    public void init() {
        super.init();
        MarkerTitle = getIntent().getStringExtra("MarkerTitle");
        TextView tvMarkerTitle = (TextView)findViewById(R.id.tvMarkerTitle);
        tvMarkerTitle.setText(MarkerTitle);
    }
}