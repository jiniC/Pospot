package dongkyul.pospot.view.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class MainMapActivity extends BaseActivity {
    private Context context = null;
    public TMapView tMapView;

    private static int mMarkerID;
    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

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
        tMapView = (TMapView)findViewById(R.id.tmapView);
        addMapView();
        addPoint();
        showMarkerPoint();
    }

    private void addMapView() {
        tMapView.setSKTMapApiKey( "fac21bdf-e297-4eaa-b2a0-fc02db2f6f1f");
        tMapView.setLocationPoint(126.970325,37.556152);
        tMapView.setCenterPoint(126.970325,37.556152);
        //tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
    }

    public void addPoint() {
        m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
        m_mapPoint.add(new MapPoint("고려대학교", 37.590799, 127.02777730000003));
        m_mapPoint.add(new MapPoint("서울시청", 37.566535, 126.97796919999996));
    }

    public void showMarkerPoint() {
        for(int i=0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(), m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(i).getName());
            item1.setVisible(item1.VISIBLE);
            String strID = String.format("pmarker%d",mMarkerID++);
            tMapView.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
        }
    }

    /*
    1. TMapMarkerItem 객체를 생성한다.
    2. TMapPoint 객체를 생성하고 좌표를 등록한다. ( point를 생성할때 (y,x) 순으로 매개변수를 받는다. 주의)
    3. 마커에 point를 지정한다.
    4. 마커의 VISIBLE을 설정한다 (TMapMarkerItem.VISIBLE은 int값 1과 같음)
    5. 마커의 이미지를 설정한다.
    6. 맵의 중심이 마커로 이동
    7. 맵에 마커표시
    * */

}