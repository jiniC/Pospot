package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import dongkyul.pospot.R;

public class PhotoMainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    int[] mPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_main);

        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PhotoMainActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mPlaceList = new int[]{R.drawable.image_1, R.drawable.image_2, R.drawable.image_2};

        PhotoMyAdapter myAdapter = new PhotoMyAdapter(PhotoMainActivity.this, mPlaceList);
        mRecyclerView.setAdapter(myAdapter);
    }
}