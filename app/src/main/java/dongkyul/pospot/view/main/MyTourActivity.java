package dongkyul.pospot.view.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    Bitmap bm;

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
                screenshot();
                Toast.makeText(MyTourActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyTourActivity.this, "추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void screenshot() {
        // View -> bitmap
        Bitmap bitmap = null;
        bitmap = tMapView.getCaptureImage();

        // bitmap을 byte array로 변환
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] currentData = stream.toByteArray();

        // 앨범에 저장
        new SaveImageTask().execute(currentData);
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/pospot");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d("SS", "onPictureTaken - wrote bytes: " + data.length + " to "
                        + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
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
