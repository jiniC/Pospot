package dongkyul.pospot.view.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Properties;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class TourApiActivity extends BaseActivity {
    LocationManager locationManager;
    TextView tourapi;
    double mapX;
    double mapY;
    int tourItemContenttypeid;
    double tourItemMapx;
    double tourItemMapy;
    String tourItemTitle;
    String result = "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_api);
        initView();
        mapX = 126.981611;
        mapY = 37.568477;
        getTourData(mapX,mapY);
    }

    private void initView(){
        tourapi = (TextView) findViewById(R.id.tourapi);
    }

    private void getTourData(double mapX, double mapY) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASEURL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getTour(mapX, mapY,1000, 500,'Y', 'A', "AND", "pospot","json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject body = response.body();
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    ResponseContainer tourList = gson.fromJson(body, new TypeToken<ResponseContainer>() {}.getType());

                    for (int i=0; i < tourList.response.body.items.item.size(); i++) {
                        tourItemContenttypeid = tourList.response.body.items.item.get(i).contenttypeid;
                        tourItemMapx = tourList.response.body.items.item.get(i).mapx;
                        tourItemMapy = tourList.response.body.items.item.get(i).mapy;
                        tourItemTitle = tourList.response.body.items.item.get(i).title;
                        result += tourItemTitle + "\n" + tourItemMapx + "\n" + tourItemMapy + "\n" + tourItemContenttypeid+"\n\n";
                    }
                    tourapi.setText(result);
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }
}