package dongkyul.pospot.view.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MyTourActivity extends BaseActivity {
    public TMapView tMapView;
    public ToggleButton btnSet;
    public Button btnSave;
    public Button btnSend;

    private static int mMarkerID;
//    private static int mPhotoMarkerID;
    private ArrayList<String> mPhotoArrayMarkerID = new ArrayList<String>();
    private ArrayList<TMapMarkerItem> m_mapPhotoMarkerItem = new ArrayList<TMapMarkerItem>();

    int zoomLevel;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytour);
        init();
    }

    @Override
    public void init() {
        super.init();
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);
        tMapView = (TMapView)findViewById(R.id.tmapView);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSend = (Button)findViewById(R.id.btnSend);

        addMapView();
        setPhotoMarker();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void addMapView() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        zoomLevel = extras.getInt("zoomLevel");
        lat = extras.getDouble("lat");
        lon = extras.getDouble("lon");
        tMapView.setSKTMapApiKey( "fac21bdf-e297-4eaa-b2a0-fc02db2f6f1f");
        tMapView.setLocationPoint(lon,lat);
        tMapView.setCenterPoint(lon,lat);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(zoomLevel);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });
    }

    public void setPhotoMarker() {
        Log.e("setPhotoMarker :: ", "setPhotoMarker");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PhotoMarkerDB> results = realm.where(PhotoMarkerDB.class).findAll();

//        더미 데이터 날림
//        realm.beginTransaction();
//        results.deleteAllFromRealm();
//        realm.commitTransaction();
//        realm.close();

        int i=0;
        if(results != null) {
            for(PhotoMarkerDB result:results) {
                RealmList<byte[]> photoList = result.getPhotoList();
                int titleIndex = result.getTitleIndex();

                m_mapPhotoMarkerItem.add(new TMapMarkerItem());
                m_mapPhotoMarkerItem.get(i).setTMapPoint(new TMapPoint(result.getLat(), result.getLon()));
                m_mapPhotoMarkerItem.get(i).setName(result.getTitle());

                TMapPoint point = m_mapPhotoMarkerItem.get(i).getTMapPoint();
                TMapMarkerItem item1 = new TMapMarkerItem();
                // byte -> bitmap
                if(titleIndex < 0) {
                    titleIndex = 0;
                }
                byte[] titleByte = photoList.get(titleIndex); // error

                Bitmap titleBitmap = BitmapFactory.decodeByteArray(titleByte, 0, titleByte.length);
                item1.setIcon(titleBitmap);
                item1.setPosition(0.5f, 1.0f);
                item1.setTMapPoint(point);
                item1.setName(m_mapPhotoMarkerItem.get(i).getName());
                item1.setVisible(item1.VISIBLE);
                String strID = String.format("mymarker%d",mMarkerID++);
                tMapView.addMarkerItem(strID, item1);
                mPhotoArrayMarkerID.add(strID);

                i = i + 1;
            }
        }

        realm.close();
    }

}
