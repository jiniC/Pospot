package dongkyul.pospot.view.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmList;

public class PhotoMarkerViewActivity extends BaseActivity {

    double photoMarkerLat;
    double photoMarkerLon;
    RecyclerView mRecyclerView;
    PhotoMarkerViewAdapter myAdapter;
    List<Bitmap> mPhotoList_img;
    RealmList<byte[]> mPhotoList_byte;
    TextView nameTab;

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Double photoMarkerLat = extras.getDouble("lat");
        Double photoMarkerLon = extras.getDouble("lon");

        Realm realm = Realm.getDefaultInstance();
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
    }
}
