package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendedPlacesActivity extends BaseActivity {

    List<String> tagNames;
    List<Integer> tagNums;
    AttractionsRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    public double centerLat;
    public double centerLon;
    GetTagNumInterface service;
    int tagNumIndex;
    List<String> attractionNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_places);
        init();
    }

    @Override
    public void init() {
        super.init();

        tagNames = new ArrayList<>();
        tagNums = new ArrayList<>();
        recyclerAdapter = new AttractionsRecyclerAdapter(tagNames,tagNums);
        recyclerView = (RecyclerView)findViewById(R.id.attractions_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        centerLat = getIntent().getDoubleExtra("lat",0);
        centerLon = getIntent().getDoubleExtra("lon",0);

        getTourData(centerLon,centerLat);

    }

    private void getTourData(double mapLon, double mapLat) { //MainMap에서 받아온 중심점 위치 주변의 관광지들 받아오기
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASEURL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getTour(mapLon, mapLat,5000, 500,'Y', 'A', "AND", "pospot","json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                List<Attraction> attractions = new ArrayList<>();
                if (response.isSuccessful()){
                    try{
                        JsonObject body = response.body();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        ResponseContainer tourList = gson.fromJson(body, new TypeToken<ResponseContainer>() {
                        }.getType());

                        for (int i = 0; i < tourList.response.body.items.item.size(); i++) {
                            int tourItemContenttypeid = tourList.response.body.items.item.get(i).contenttypeid;
                            double lat = tourList.response.body.items.item.get(i).mapy; // latitude
                            double lon = tourList.response.body.items.item.get(i).mapx; // longitude
                            if(tourItemContenttypeid==12) { //관광지이면
                                attractions.add(new Attraction(tourList.response.body.items.item.get(i).title,lat,lon));
                            }
                        }
                    }
                    catch(Exception e){
                        Log.e("e",e.toString());
                    }
                }

                sortAttractions(attractions); //거리순 정렬
                getTagNumFromApi(); //태그 수 받아오기
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }

    private void getTagNumFromApi(){ //service 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetTagNumInterface.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GetTagNumInterface.class);
        tagNumIndex=0;
        requestApi();
    }

    private void sortAttractions(List<Attraction> attractions){

        Collections.sort(attractions);

        attractionNames = new ArrayList<>();
        for(Attraction a:attractions){
            attractionNames.add(a.name);
        }

    }

    public void requestApi(){
        Call<JsonObject> request = service.getTagNum(attractionNames.get(tagNumIndex),attractionNames.get(tagNumIndex+1),attractionNames.get(tagNumIndex+2),attractionNames.get(tagNumIndex+3),attractionNames.get(tagNumIndex+4));
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("S",response.body().toString());
                Gson gson = new GsonBuilder().serializeNulls().create();
                TagNumResponseContainer container = gson.fromJson(response.body(),TagNumResponseContainer.class);
                int k=0;
                for(TagNumResponseContainer.TagBox t:container.getResult()) {
                    if(t.getTagNum()!=0 && tagNums.size()<5) {
                        tagNames.add(attractionNames.get(tagNumIndex+k));
                        tagNums.add(t.getTagNum());
                    }
                    k++;
                }
                tagNumIndex+=5;
                if(tagNums.size()<5 && attractionNames.size()>0)
                    requestApi(); //5개가 채워질 때까지 재귀적으로 호출
                else{
                    Log.e("S",Integer.toString(tagNums.size()));
                    recyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Error",t.toString());
            }
        });
    }

    class Attraction implements Comparable<Attraction>{

        String name;
        double distance;

        Attraction(String name, double lat, double lon){
            this.name=name;
            this.distance = L2distance(lon,lat,centerLon,centerLat);
        }

        private double L2distance(double lon1, double lat1, double lon2, double lat2){
            return Math.sqrt(Math.pow((lon1-lon2),2)+Math.pow((lat1-lat2),2));
        }

        @Override
        public int compareTo(@NonNull Attraction o) {
            if(distance > o.distance){
                return 1;
            }
            else if(distance < o.distance){
                return -1;
            }
            return 0;
        }
    }
}
