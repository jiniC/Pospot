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
   // private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
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

                    // to-be
                    // 위치 못 찾았을 때 (0.0, 0.0) 임시로 서울 시청 위치 넣어두고
                    // 토스트창으로 위치 확인이 안되서 임시 데이터로 했다는 토스트 노출하기

                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();

                    tMapView.setLocationPoint(longitude,latitude);
                    tMapView.setCenterPoint(longitude,latitude);

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
    }

    private void addMapView() {
        tMapView.setSKTMapApiKey( "fac21bdf-e297-4eaa-b2a0-fc02db2f6f1f");
        tMapView.setLocationPoint(126.970325,37.556152);
        tMapView.setCenterPoint(126.970325,37.556152);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
    }

    public void showMarkerPoint() {
        for(int i=0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(), m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();

            int contenttypeid = m_mapPoint.get(i).getType();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon);
            switch (contenttypeid) {
                case 12:
                    System.out.println("관광지");
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_12);
                    break;
                case 14:
                    System.out.println("문화시설");
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_14);
                    break;
                case 15:
                    System.out.println("축제/공연/행사");
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_15);
                    break;
                case 25:
                    System.out.println("여행코스");
                    break;
                case 28:
                    System.out.println("레포츠");
                    break;
                case 32:
                    System.out.println("숙박");
                    break;
                case 38:
                    System.out.println("쇼핑");
                    break;
                case 39:
                    System.out.println("음식");
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_39);
                    break;
                default:
                    System.out.println("기본");
                    break;
            }
            item1.setIcon(bitmap); // 마커 아이콘 지정
            item1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
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
//        Call<JsonObject> call = apiService.getTour(mapLon, mapLat,1000000, 10000,'Y', 'A', "AND", "pospot","json");
        Call<JsonObject> call = apiService.getTour(mapLon, mapLat,5000, 1000,'Y', 'A', "AND", "pospot","json");
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

                        m_mapPoint.add(new MapPoint(tourItemTitle, tourItemContenttypeid, tourItemMapLat, tourItemMapLon));
                    }
                    showMarkerPoint();
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
}