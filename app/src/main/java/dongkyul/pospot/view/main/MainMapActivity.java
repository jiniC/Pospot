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
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainMapActivity extends BaseActivity {
    public TMapView tMapView;
    public ToggleButton btnSet;
    public Button myLocationButton;

    private static int mMarkerID;
    private static int mPhotoMarkerID;
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<String> mPhotoArrayMarkerID = new ArrayList<String>();
    private ArrayList<TMapMarkerItem> m_mapMarkerItem = new ArrayList<TMapMarkerItem>();
    private ArrayList<TMapMarkerItem> m_mapPhotoMarkerItem = new ArrayList<TMapMarkerItem>();

    String tourItemContenttypeid;
    float tourItemMapLat;
    float tourItemMapLon;
    String tourItemTitle;
    Button recommendButton;

    //지도 가운데 중심점
    double centerLat;
    double centerLon;

    private GpsInfo gps;

    // activity cycle
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
        btnSet = (ToggleButton) findViewById(R.id.btnSet);
        myLocationButton = (Button) findViewById(R.id.btnMyLocation);
        myLocationButton.setOnClickListener(this);
        recommendButton = (Button)findViewById(R.id.btnRecommend);
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
        } else {
            loadPosition();
        }

        btnSet.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if(isChecked) {
                    showPhotoMarker();
                } else {

                    removePhotoMarker();
                }
            }
        });

        setPhotoMarker();
    }

    public void loadPosition(){
        try {
            gps = new GpsInfo(MainMapActivity.this);

            if (gps.isGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                if(latitude!=0.0&&longitude!=0.0) {
                    tMapView.setLocationPoint(longitude, latitude);
                    tMapView.setCenterPoint(longitude, latitude);
                    if (m_mapMarkerItem.size() == 0) {
                        getTourData(longitude, latitude);
                    }
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

                    if (MarkerID.contains("mymarker")) {
                        Intent intent = new Intent(MainMapActivity.this, PhotoMarkerViewActivity.class);
                        intent.putExtra("lat",markeritem.latitude);
                        intent.putExtra("lon",markeritem.longitude);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainMapActivity.this, MarkerClickedActivity.class);
                        intent.putExtra("markerName",MarkerName);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList markerlist,ArrayList poilist, TMapPoint point) {
                if(markerlist.isEmpty()) {
                    addPhotoMarker(point.getLatitude(), point.getLongitude());
                }
            }
        });
    }

    public void showMarkerPoint() { // attraction 배열에 관광지목록만 따로 추가하는 역할도 함
        for(int i=0; i < m_mapMarkerItem.size(); i++) {
            TMapPoint point = m_mapMarkerItem.get(i).getTMapPoint();
            TMapMarkerItem item1 = new TMapMarkerItem();

            int contenttypeid = Integer.parseInt(m_mapMarkerItem.get(i).getCalloutSubTitle());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
            switch (contenttypeid) {
                case 12:
                    // 관광지
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker12);
                    break;
                case 14:
                    // 문화시설
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker14);
                    break;
                case 15:
                    // 축제/공연/행사
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker15);
                    break;
                case 25:
                    // 여행코스
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker25);
                    break;
                case 28:
                    // 레포츠
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker28);
                    break;
                case 32:
                    // 숙박
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker32);
                    break;
                case 38:
                    // 쇼핑
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker38);
                    break;
                case 39:
                    // 음식
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker39);
                    break;
                default:
                    // 기본
                    break;
            }
            item1.setIcon(bitmap); // android.graphics.Bitmap@9405e51
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
        Call<JsonObject> call = apiService.getTour(mapLon, mapLat,1000000, 1000,'Y', 'A', "AND", "pospot","json");
//        Call<JsonObject> call = apiService.getTour(mapLon, mapLat,1000, 1000,'Y', 'A', "AND", "pospot","json");
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

                    // 인스타 태그 수 표시창으로 이동
                    recommendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =  new Intent(MainMapActivity.this,RecommendedPlacesActivity.class);
                            TMapPoint centerPoint=tMapView.getCenterPoint();
                            centerLat=centerPoint.getLatitude();
                            centerLon=centerPoint.getLongitude(); //중심점 위치
                            intent.putExtra("lat",centerLat);
                            intent.putExtra("lon",centerLon);
                            startActivity(intent);
                        }
                    });
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

    public void setPhotoMarker() {
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

    public void showPhotoMarker() {
        // m_mapPhotoMarkerItem.size(), visible, hide. . .
        Log.e("showPhotoMarker :: ", String.valueOf(m_mapPhotoMarkerItem));
        Log.e("showPhotoMarker :: ", String.valueOf(m_mapPhotoMarkerItem.size()));
//        for (int i = 0; m_mapPhotoMarkerItem.size(); i++) {
//        }
        m_mapPhotoMarkerItem.get(0).setVisible(m_mapPhotoMarkerItem.get(0).VISIBLE);
        m_mapPhotoMarkerItem.get(1).setVisible(m_mapPhotoMarkerItem.get(1).VISIBLE);
    }

    public void removePhotoMarker() {
        m_mapPhotoMarkerItem.get(0).setVisible(m_mapPhotoMarkerItem.get(0).HIDDEN);
        m_mapPhotoMarkerItem.get(1).setVisible(m_mapPhotoMarkerItem.get(1).HIDDEN);
        // m_mapPhotoMarkerItem 보이지 않게
        Log.e("removePhotoMarker :: ", String.valueOf(m_mapPhotoMarkerItem));
        for(TMapMarkerItem PhotoMarkerItem:m_mapPhotoMarkerItem) {
            PhotoMarkerItem.setVisible(PhotoMarkerItem.HIDDEN);
            Log.e("remove for :: ",PhotoMarkerItem.getID());
        }
    }
}