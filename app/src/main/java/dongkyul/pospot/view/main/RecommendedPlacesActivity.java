package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetTagNumInterface.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        List<String> receivedTags = getIntent().getStringArrayListExtra("attractions");



        for(int i=0;i<5; i++)
            tagNames.add(receivedTags.get(i));

        GetTagNumInterface service = retrofit.create(GetTagNumInterface.class);
        Call<JsonObject> request = service.getTagNum(tagNames.get(0),tagNames.get(1),tagNames.get(2),tagNames.get(3),tagNames.get(4)); //아직 거리순으로 정렬된게 아님 - 더 만들어야...
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                TagNumResponseContainer container = gson.fromJson(response.body(),TagNumResponseContainer.class);
                for(TagNumResponseContainer.TagBox t:container.getResult()) {
                    tagNums.add(t.getTagNum());
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
