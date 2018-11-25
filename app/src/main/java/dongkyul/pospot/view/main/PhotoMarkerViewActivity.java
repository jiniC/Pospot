package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.util.Log;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class PhotoMarkerViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_marker_view);
        init();
    }
}
