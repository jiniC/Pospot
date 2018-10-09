package dongkyul.pospot.view.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainMapActivity extends BaseActivity {
    private Context context = null;
    public TMapView tMapView;
    public Button btnSet;

    private static int mMarkerID;
    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    TextView tourapi;
    double mapLon;
    double mapLat;
    int tourItemContenttypeid;
    double tourItemMapLat;
    double tourItemMapLon;
    String tourItemTitle;
    String result = "\n";

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private GpsInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        init();

        btnSet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!isPermission) {
                    callPermission();
                    return;
                }

                gps = new GpsInfo(MainMapActivity.this);
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();

                    getTourData(longitude,latitude);
                } else {
                    gps.showSettingsAlert();
                }
            }
        });

        callPermission();
    }

    @Override
    public void init() {
        super.init();
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);
        tMapView = (TMapView)findViewById(R.id.tmapView);
        btnSet = (Button) findViewById(R.id.btnSet);
        tourapi = (TextView) findViewById(R.id.tourapi);
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

    private void getTourData(double mapLon, double mapLat) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASEURL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getTour(mapLon, mapLat,1000, 500,'Y', 'A', "AND", "pospot","json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject body = response.body();
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    ResponseContainer tourList = gson.fromJson(body, new TypeToken<ResponseContainer>() {}.getType());

                    for (int i=0; i < tourList.response.body.items.item.size(); i++) {
                        tourItemContenttypeid = tourList.response.body.items.item.get(i).contenttypeid;
                        tourItemMapLat = tourList.response.body.items.item.get(i).mapy; // latitude
                        tourItemMapLon = tourList.response.body.items.item.get(i).mapx; // longitude
                        tourItemTitle = tourList.response.body.items.item.get(i).title;
                        result += tourItemTitle + "\n" + tourItemMapLat + "\n" + tourItemMapLon + "\n" + tourItemContenttypeid+"\n\n";
                    }
                    tourapi.setText(result);
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    private void callPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
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