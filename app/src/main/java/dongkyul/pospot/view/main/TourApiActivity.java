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
    double mapX;
    double mapY;
    String result;
    TextView tourapi;

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
        //뷰세팅
        tourapi = (TextView) findViewById(R.id.tourapi);
    }

    private void getTourData(double mapX, double mapY) {
        Log.e("seojin-api","getTourData");
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASEURL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getTour(mapX, mapY,1000,'Y', 'A', "AND", "pospot","json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()){
                    // 데이터를 받아옴
                    Log.e("seojin-api", "onResponse");
                    JsonObject body = response.body();
                    Log.e("seojin-api", String.valueOf(body));
                    /*
                    {"response":{"header":{"resultCode":"0000","resultMsg":"OK"},"body":{"items":{"item":[{"addr1":"서울특별시 중구 명동8가길 32","addr2":"(충무로2가)","areacode":1,"cat1":"A04","cat2":"A0401","cat3":"A04010600","contentid":984586,"contenttypeid":38,"createdtime":20100324124214,"dist":884,"firstimage":"http://tong.visitkorea.or.kr/cms/resource/36/1009936_image2_1.jpg","firstimage2":"http://tong.visitkorea.or.kr/cms/resource/36/1009936_image3_1.jpg","mapx":126.9868259553,"mapy":37.5616910698,"mlevel":6,"modifiedtime":20180302170438,"readcount":11106,"sigungucode":24,"tel":"02-753-6372","title":"가나 안경원 (명동2호점)"},{"addr1":"서울특별시 종로구 인사동길 56","addr2":"(관훈동)","areacode":1,"booktour":0,"cat1":"A02","cat2":"A0206","cat3":"A02060500","contentid":130402,"contenttypeid":14,"createdtime":20071106015541,"dist":752,"firstimage":"http://tong.visitkorea.or.kr/cms/resource/19/1550819_image2_1.jpg","firstimage2":"http://tong.visitkorea.or.kr/cms/resource/19/1550819_image3_1.jpg","mapx":126.9839369528,"mapy":37.5749922502,"mlevel":6,"modifiedtime":20170818174633,"readcount":21835,"sigungucode":23,"title":"가나아트스페이스"},{"addr1":"서울특별시 종로구 인사동10길 11","addr2":"(관훈동)","areacode":1,"booktour":0,"cat1":"A02","cat2":"A0206","cat3":"A02060500","contentid":129898,"contenttypeid":14,"createdtime":20071106013446,"dist":735,"firstimage":"http://tong.visitkorea.or.kr/cms/resource/14/1394214_image2_1.jpg","firstimage2":"http://tong.visitkorea.or.kr/cms/resource/14/1394214_image3_1.jpg","mapx":126.9856034879,"mapy":37.5742756967,"mlevel":6,"modifiedtime":20170828182345,"readcount":18618,"sigungucode":23,"title":"가람화랑"},{"addr1":"서울특별시 종로구 종로8길 16","addr2":"(관철동)","areacode":1,"cat1":"A05","cat2":"A0502","cat3":"A05020100","contentid":1953272,"contenttypeid":39,"createdtime":20141001104426,"dist":221,"firstimage":"http://tong.visitkorea.or.kr/cms/resource/46/1953246_image2_1.JPG","firstimage2":"http://tong.visitkorea.or.kr/cms/resource/46/1953246_image2_1.JPG","mapx":"126.9838371210","mapy":37.5693679015,"mlevel":6,"modifiedtime":20170717094332,"readcount":4274,"sigungucode":23,"tel":"02-737-4987","title":"가마목"},{"addr1":"서울특별시 중구 명동10길 8","addr2":"(명동2가)","areacode":1,"cat1":"A04","cat2":"A0401","cat3":"A04010600","contentid":2498168,"contenttypeid":38,"createdtime":20170628153134,"dist":637,"firstimage":"http://tong.visitkorea.or.kr/cms/resource/57/2498157_image2_1.JPG","firstimage2":"http://tong.visitkorea.or.kr/cms/resource/57/2498157_image2_1.JPG","mapx":"126.9850038500","mapy":37.5634213914,"mlevel":6,"modifiedtime":20180829180118,"readcount":29,"sigungucode":24,"title":"갈라 프리스비 명동점 [한국관광품질인증/Korea Quality]"},{"addr1":"서울특별시 중구 세종대로11길 35","addr2":"(서소문동)","areacode":1,"cat1":"A05","cat2":"A0502","cat3":"A05020100","contentid":232229,"contenttypeid":39,"createdtime":20071002000000,"dist":994,"mapx":126.9739104574,"mapy":37.5619824706,"mlevel":6,"modifiedtime":20170202143803,"readcount":20996,"sigungucode":24,"tel":"02-752-1945","title":"강서면옥"},{"addr1":"서울특별시 종로구 인사동10길 11-3","addr2":"(관훈동)","areacode":1,"cat1":"A05","cat2":"A0502","cat3":"A05020100","contentid":398373,"contenttypeid":39,"createdtime":20080111201505,"dist":720,"firstimage":"http://tong.visitkorea.or.kr/cms/resource/44/1885344_image2_1.jpg","firstimage2":"http://tong.visitkorea.or.kr/cms/resource/44/1885344_image3_1.jpg","mapx":126.9853535117,"mapy":"37.5742312530","mlevel":6,"modifiedtime":20170125095616,"readcount":23963,"sigungucode":23,"tel":"02-733-9240","title":"개성만두 궁"},{"addr1":"서울특별시 중구 남대문로 52-5","addr2":"(명동2가)","areacode":1,"cat1":"A05","cat2":"A0502","cat3":"A05020400","contentid":134746,"contenttypeid":39,"createdtime":20041203000000,"dist":706,"firstimage":"http://tong.visitkorea.or.kr/cms/resource/11/1291311_image2_1.jp
                     */
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    ResponseContainer tourList = gson.fromJson(body, new TypeToken<ResponseContainer>() {}.getType());
                    //tourList.response.body.items.item.get(0); // item
                    //Log.e("seojin-api", String.valueOf(tourList.response.body.items.item.get(0)));

                    for (int i=0; i < tourList.response.body.items.item.size(); i++) {
                        result += tourList.response.body.items.item.get(i);
                       // System.out.println(tourList.response.body.items.item.get(i));
                    }
                    tourapi.setText(result);
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                //Log.e("S",t.getMessage());
            }
        });
    }
}