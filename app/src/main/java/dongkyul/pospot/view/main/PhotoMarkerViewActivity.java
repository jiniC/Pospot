package dongkyul.pospot.view.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class PhotoMarkerViewActivity extends BaseActivity {

    double photoMarkerLat;
    double photoMarkerLon;
    RecyclerView mRecyclerView;
    PhotoMarkerViewAdapter myAdapter;
    List<Bitmap> mPhotoList_img;
    RealmList<byte[]> mPhotoList_byte;
    TextView nameTab;
    Button btnDelete;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_marker_view);
        init();
    }

    @Override
    public void init() {
        super.init();
        nameTab = (TextView)findViewById(R.id.markerNameTab);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        photoMarkerLat = extras.getDouble("lat");
        photoMarkerLon = extras.getDouble("lon");

        realm = Realm.getDefaultInstance();
        PhotoMarkerDB clickPhotoMarker;
        clickPhotoMarker = realm.where(PhotoMarkerDB.class)
                .equalTo("lat", photoMarkerLat)
                .and()
                .equalTo("lon", photoMarkerLon)
                .findFirst();

        nameTab.setText(clickPhotoMarker.getTitle());

        mPhotoList_img = new ArrayList<Bitmap>();
        mPhotoList_byte = new RealmList<byte[]>();
        mPhotoList_byte = clickPhotoMarker.getPhotoList();
        Log.e("mPhotoList_byte ::: ", String.valueOf(mPhotoList_byte));
        // byte -> bitmap
        for(byte[] mPhoto_byte : mPhotoList_byte) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(mPhoto_byte, 0, mPhoto_byte.length);
            mPhotoList_img.add(bitmap);
        }

        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PhotoMarkerViewActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        myAdapter = new PhotoMarkerViewAdapter(PhotoMarkerViewActivity.this, mPhotoList_img);
        mRecyclerView.setAdapter(myAdapter);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhotoMarkerViewActivity.this);

                alertDialog.setTitle("포토 마커 삭제");
                alertDialog.setMessage("해당 포토마커를 삭제할까요?");

                alertDialog.setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                RealmResults<PhotoMarkerDB> item = realm.where(PhotoMarkerDB.class)
                                        .equalTo("lat", photoMarkerLat)
                                        .and()
                                        .equalTo("lon", photoMarkerLon)
                                        .findAll();
                                realm.beginTransaction();
                                item.deleteAllFromRealm();
                                realm.commitTransaction();
                                realm.close();
                                Intent intent = new Intent(PhotoMarkerViewActivity.this, MainMapActivity.class);
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
        });
    }
}
