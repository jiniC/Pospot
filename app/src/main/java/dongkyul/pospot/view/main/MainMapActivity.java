package dongkyul.pospot.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class MainMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        LinearLayout viewMap = (LinearLayout)findViewById(R.id.viewMap);
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey( "27c0a585-2760-4f43-afb6-cba605ab31b3" );
        tMapView.setLocationPoint(126.970325,37.556152);
        tMapView.setCenterPoint(126.970325,37.556152);
        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setLanguage(TMapView.MAPTYPE_STANDARD);
        tMapView.setMapType(TMapView.LANGUAGE_KOREAN);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
        viewMap.addView( tMapView );

    }
}