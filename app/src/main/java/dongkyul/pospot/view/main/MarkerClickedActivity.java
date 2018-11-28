package dongkyul.pospot.view.main;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    static List<Bitmap> mPhotoList_img;
    int urlIndex=0;
    public ProgressDialog mProgressDialog;


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
        LinearLayout filterMenu = (LinearLayout)findViewById(R.id.filterMenu);
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

    public void requestImages(){ //API로부터 이미지 링크 받아오기
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
                if(container.imgurls==null) {
                    TextView noResult = (TextView)findViewById(R.id.noResultTab);
                    noResult.setVisibility(View.VISIBLE);
                    noResult.setText("\""+markerName+"\" 태그의 Instagram 검색 결과가 존재하지 않습니다");
                    return;
                }
                String toConcat=container.imgurls.substring(1,container.imgurls.length()-1).replace("\"","");
                String[] rst=toConcat.split("\\,");

                if(rst!=null) {
                    for (String url : rst) {
                        urls.add(url);
                    }
                    showProgressDialog();
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
        showProgressDialog();
        final String url = urls.get(urlIndex);
        urlIndex++;
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(15, TimeUnit.SECONDS);
        client.readTimeout(15,TimeUnit.SECONDS);
        client.writeTimeout(15,TimeUnit.SECONDS);
        OkHttpClient client1 = client.build();
        client1.newCall(request).enqueue(new okhttp3.Callback() {
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
                        if((urls.size()<5&urlIndex==0)||urlIndex==4){
                            hideProgressDialog();
                        }
                    }
                });
            }
        });
    }

    public static Bitmap getBitmapFilter(int position){
        return mPhotoList_img.get(position);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("로딩중...");
            mProgressDialog.setIndeterminate(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.show();
                }
            });
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
