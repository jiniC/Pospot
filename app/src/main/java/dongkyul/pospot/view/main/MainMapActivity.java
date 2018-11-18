package dongkyul.pospot.view.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import dongkyul.pospot.R;
import dongkyul.pospot.utils.PermissionUtils;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainMapActivity extends BaseActivity {
    public TMapView tMapView;
    public Button btnSet;
    public Button myLocationButton;

    private static int mMarkerID;
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<TMapMarkerItem> m_mapMarkerItem = new ArrayList<TMapMarkerItem>();

    String tourItemContenttypeid;
    float tourItemMapLat;
    float tourItemMapLon;
    String tourItemTitle;

    private GpsInfo gps;

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
        btnSet = (Button) findViewById(R.id.btnSet);
        myLocationButton = (Button) findViewById(R.id.btnMyLocation);
        myLocationButton.setOnClickListener(this);
        addMapView();
        PermissionListener locationPermissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                loadPosition();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(),"위치 정보 로드 권한이 없습니다", Toast.LENGTH_LONG);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.checkLocationPermission(this, locationPermissionListener);
        }else{
            loadPosition();
        }

        // getZoomLevel
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int previousZoomlevel=tMapView.getZoomLevel();
                int nowZoomlevel;
                double previousPosition = tMapView.getCenterPointX();
                double nowPosition = tMapView.getCenterPointX();
                while(true){
                    try {
                        Thread.sleep(1000);
                        nowZoomlevel = tMapView.getZoomLevel();
                        if(previousZoomlevel!=nowZoomlevel||previousPosition!=nowPosition){
                            Thread.sleep(1000);
                            if(previousZoomlevel==nowZoomlevel&&previousPosition==nowPosition) {
                                // to-be
                                // 줌 레벨에 따라 세 단계 정도로 나눠서 데이터 받아오기 & 위치 변경에 따라서만 데이터 받아오기
                            }
                        }
                    }

                    catch(Exception e){

                    }
                }
            }
        });

        showPhotoMarker();
    }

    public void loadPosition(){
        try {
            gps = new GpsInfo(MainMapActivity.this);

            if (gps.isGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                if(latitude!=0.0&&longitude!=0.0) {
                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();
                    tMapView.setLocationPoint(longitude, latitude);
                    tMapView.setCenterPoint(longitude, latitude);

                    getTourData(longitude, latitude);
                } else {
                    Toast.makeText(this,"위치를 찾을 수 없습니다.",Toast.LENGTH_LONG);
                }
            } else {
                gps.showSettingsAlert();
            }
        } catch(Exception e){
            Toast.makeText(this,"위치 찾기 에러",Toast.LENGTH_LONG);
            Log.e("S","Error 받기");
        }

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

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayMapMarkerItem, ArrayList<TMapPOIItem> arrayMapPOIItem, TMapPoint mapPoint, PointF pointF) {
                if(!arrayMapMarkerItem.isEmpty()) {
                    String MarkerID = arrayMapMarkerItem.get(0).getID();
                    TMapMarkerItem markeritem = tMapView.getMarkerItemFromID(String.valueOf(MarkerID));
                    String MarkerName = markeritem.getName();
                    Toast.makeText(
                            getApplicationContext(),
                            "마커가 클릭됬습니다!\n"+MarkerName,
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainMapActivity.this, CrawlingApiTestActivity.class);
                    intent.putExtra("markerName",MarkerName);
                    startActivity(intent);
                }
                return false;
            }
        });

        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList markerlist,ArrayList poilist, TMapPoint point) {
                if(markerlist.isEmpty()) {
                    Toast.makeText(
                            getApplicationContext(),
                            "앨범 등록을 위한 위치 선택 완료!\n"+point.getLatitude()+"\n"+point.getLongitude()+"\n",
                            Toast.LENGTH_LONG).show();
                    addPhotoMarker(point.getLatitude(), point.getLongitude());
                }
            }
        });
    }

    public void showMarkerPoint() {
        for(int i=0; i < m_mapMarkerItem.size(); i++) {
            TMapPoint point = m_mapMarkerItem.get(i).getTMapPoint();
            TMapMarkerItem item1 = new TMapMarkerItem();

            int contenttypeid = Integer.parseInt(m_mapMarkerItem.get(i).getCalloutSubTitle());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon);
            switch (contenttypeid) {
                case 12:
                    // 관광지
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_12);
                    break;
                case 14:
                    // 문화시설
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_14);
                    break;
                case 15:
                    // 축제/공연/행사
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_15);
                    break;
                case 25:
                    // 여행코스
                    break;
                case 28:
                    // 레포츠
                    break;
                case 32:
                    // 숙박
                    break;
                case 38:
                    // 쇼핑
                    break;
                case 39:
                    // 음식
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapicon_39);
                    break;
                default:
                    // 기본
                    break;
            }
            item1.setIcon(bitmap);
            item1.setPosition(0.5f, 1.0f);
            item1.setTMapPoint(point);
            item1.setName(m_mapMarkerItem.get(i).getName());
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
        // Call<JsonObject> call = apiService.getTour(mapLon, mapLat,1000000, 10000,'Y', 'A', "AND", "pospot","json");
        Call<JsonObject> call = apiService.getTour(mapLon, mapLat,1000, 1000,'Y', 'A', "AND", "pospot","json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try{
                        JsonObject body = response.body();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        ResponseContainer tourList = gson.fromJson(body, new TypeToken<ResponseContainer>() {
                        }.getType());

                        for (int i = 0; i < tourList.response.body.items.item.size(); i++) {
                            tourItemContenttypeid = String.valueOf(tourList.response.body.items.item.get(i).contenttypeid);
                            tourItemMapLat = (float) tourList.response.body.items.item.get(i).mapy; // latitude
                            tourItemMapLon = (float) tourList.response.body.items.item.get(i).mapx; // longitude
                            tourItemTitle = tourList.response.body.items.item.get(i).title;
                            m_mapMarkerItem.add(new TMapMarkerItem());
                            m_mapMarkerItem.get(i).setTMapPoint(new TMapPoint(tourItemMapLat, tourItemMapLon));
                            m_mapMarkerItem.get(i).setName(tourItemTitle);
                            m_mapMarkerItem.get(i).setCalloutSubTitle(String.valueOf(tourItemContenttypeid));
                        }
                    }
                    catch(Exception e){
                        Log.e("e",e.toString());
                    }
                    showMarkerPoint();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }

        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnMyLocation:
                // to-be
                // 관광 데이터 있는데 한번 더 누르면 뜨는 오류 해결하기
                loadPosition();
                break;
        }
    }


    public void addPhotoMarker(final double pointLat, final double pointLon) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMapActivity.this);

        alertDialog.setTitle("포토 마커 등록");
        alertDialog.setMessage("해당 위치에 포토마커를 생성할까요?");

        alertDialog.setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(MainMapActivity.this, PhotoMainActivity.class);
                        intent.putExtra("pointLat",pointLat);
                        intent.putExtra("pointLon",pointLon);
                        startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void showPhotoMarker() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("PhotoToAdd").build();
        Realm realm = Realm.getInstance(config);
        RealmResults<PhotoMarkerDB> results = realm.where(PhotoMarkerDB.class).findAll();
        String output="";
        for(PhotoMarkerDB result:results) {
            output+=result.toString();
            output+="\n";
        }

        Log.e("output ::: ", output);
        realm.close();

        // to-be
        // 대표이미지들로 포토마커 찍기
    }


}