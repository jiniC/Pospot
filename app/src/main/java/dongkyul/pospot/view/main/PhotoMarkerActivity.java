package dongkyul.pospot.view.main;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class PhotoMarkerActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    PhotoMarkerAdapter myAdapter;
    List<Bitmap> mPhotoList_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 넘어온 lat, lon 값으로 MarkerDB 객체 찾고
        // -> 거기에 사진리스트들 찾고
        // -> 그걸 리사이클 어댑터로 넘겨줌

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_marker);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Double photoMarkerLat = extras.getDouble("photoMarkerLat");
        Double photoMarkerLon = extras.getDouble("photoMarkerLon");

        Log.e("photoMarkerLat :: ", String.valueOf(photoMarkerLat));
        Log.e("photoMarkerLon :: ", String.valueOf(photoMarkerLon));

//        mPhotoList_img = new ArrayList<Bitmap>();
//        mRecyclerView = findViewById(R.id.recyclerview);
//        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PhotoMarkerActivity.this, 2);
//        mRecyclerView.setLayoutManager(mGridLayoutManager);
//        myAdapter = new PhotoMarkerAdapter(PhotoMarkerActivity.this, mPhotoList_img);
//        mRecyclerView.setAdapter(myAdapter);
    }
}