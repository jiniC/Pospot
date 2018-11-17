package dongkyul.pospot.view.main;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.graphics.BitmapFactory.decodeByteArray;

public class PhotoMarkerActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    PhotoMarkerAdapter myAdapter;
    List<Bitmap> mPhotoList_img;
    RealmList<byte[]> mPhotoList_byte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_marker);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Double photoMarkerLat = extras.getDouble("photoMarkerLat");
        Double photoMarkerLon = extras.getDouble("photoMarkerLon");

        RealmConfiguration config = new RealmConfiguration.Builder().name("PhotoToAdd").build();
        Realm realm = Realm.getInstance(config);


        PhotoMarkerDB clickPhotoMarker;
        clickPhotoMarker = realm.where(PhotoMarkerDB.class)
                                                   .equalTo("lat", photoMarkerLat)
                                                   .and()
                                                   .equalTo("lon", photoMarkerLon)
                                                   .findFirst();
        // Log.e("results ::: ", String.valueOf(clickPhotoMarker));

        mPhotoList_img = new ArrayList<Bitmap>();
        mPhotoList_byte = new RealmList<byte[]>();
        mPhotoList_byte = clickPhotoMarker.getPhotoList();
        // Log.e("mPhotoList_byte", String.valueOf(mPhotoList_byte)); // RealmList<byte[]>@[byte[57800]]
        // byte -> bitmap
        for(byte[] mPhoto_byte : mPhotoList_byte) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(mPhoto_byte, 0, mPhoto_byte.length);
            mPhotoList_img.add(bitmap);
        }

        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PhotoMarkerActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        myAdapter = new PhotoMarkerAdapter(PhotoMarkerActivity.this, mPhotoList_img);
        mRecyclerView.setAdapter(myAdapter);
    }
}