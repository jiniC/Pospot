package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import com.skt.Tmap.TMapView;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class MainMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        init();
    }

    @Override
    public void init() {
        super.init();
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);
        TMapView tMapView = (TMapView)findViewById(R.id.tmapView);
        tMapView.setSKTMapApiKey( "fac21bdf-e297-4eaa-b2a0-fc02db2f6f1f");
        tMapView.setLocationPoint(126.970325,37.556152);
        tMapView.setCenterPoint(126.970325,37.556152);
        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
    }
}