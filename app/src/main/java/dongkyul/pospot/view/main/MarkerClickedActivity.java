package dongkyul.pospot.view.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarkerClickedActivity extends BaseActivity {

    String markerName;
    RecyclerView mRecyclerView;
    FilterListRecyclerAdapter myAdapter;
    List<String> urls;
    List<Bitmap> mPhotoList_img;
    int urlIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_insta_image);
        urls = new ArrayList<>();
        mPhotoList_img = new ArrayList<>();
        markerName=getIntent().getStringExtra("markerName"); //마커 이름
        TextView nameTab = (TextView)findViewById(R.id.markerNameTab);
        nameTab.setText(markerName);
        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MarkerClickedActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        myAdapter = new FilterListRecyclerAdapter(MarkerClickedActivity.this, mPhotoList_img);
        mRecyclerView.setAdapter(myAdapter);
        requestImages();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

        }
    }

    public void requestImages(){ //API로부터 이미지 받아오기
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getPhotosInterface.BaseURL)
                .build();
        getPhotosInterface apiService = retrofit.create(getPhotosInterface.class);
        Call<JsonObject> request = apiService.getPhotos(markerName);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                PhotoResponseContainer container = gson.fromJson(response.body(),PhotoResponseContainer.class);
                String toConcat=container.imgurls.substring(1,container.imgurls.length()-1).replace("\"","");
                String[] rst=toConcat.split("\\,");

                if(rst!=null) {
                    for (String url : rst) {
                        urls.add(url);
                    }
                    getImages();

                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Error",t.toString());
            }
        });
    }

    public void getImages(){ //onResponse에 따라 재귀적으로 url로부터 이미지 호출
        String url = urls.get(urlIndex);
        urlIndex++;
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("error",e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                InputStream bytes = response.body().byteStream();
                Bitmap bmp = BitmapFactory.decodeStream(bytes);
                mPhotoList_img.add(bmp);

                if(urlIndex<urls.size()){
                    getImages();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
