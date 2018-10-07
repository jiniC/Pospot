package dongkyul.pospot.view.main;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {
    // 베이스 Url
    static final String BASEURL = "http://api.visitkorea.or.kr/";
    static final String APIKEY ="tIdYKCsxdgAhM0wG9UJDpooJzQOCYOUkR0AhRKPuF7wxYLmQ82Rf7C7oDm3PreMahRMtJURA5JHX5XB8SqFTRQ%3D%3D";

    /*
    * http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?
    * ServiceKey=tIdYKCsxdgAhM0wG9UJDpooJzQOCYOUkR0AhRKPuF7wxYLmQ82Rf7C7oDm3PreMahRMtJURA5JHX5XB8SqFTRQ%3D%3D
    * &mapX=126.981611
    * &mapY=37.568477
    * &radius=1000
    * &numOfRows=500
    * &arrange=A
    * &MobileOS=ETC
    * &MobileApp=AppTest
    * &_type=json
    * */

    // http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=tIdYKCsxdgAhM0wG9UJDpooJzQOCYOUkR0AhRKPuF7wxYLmQ82Rf7C7oDm3PreMahRMtJURA5JHX5XB8SqFTRQ%3D%3D&mapX=126.981611&mapY=37.56847&radius=1000&numOfRows=500&arrange=A&MobileOS=ETC&MobileApp=AppTest&_type=json

    // get 메소드를 통한 http rest api 통신
    @GET("openapi/service/rest/KorService/locationBasedList?ServiceKey=tIdYKCsxdgAhM0wG9UJDpooJzQOCYOUkR0AhRKPuF7wxYLmQ82Rf7C7oDm3PreMahRMtJURA5JHX5XB8SqFTRQ%3D%3D")
    Call<JsonObject> getTour (
                              @Query("mapX") double mapX,
                              @Query("mapY") double mapY,
                              @Query("radius") int radius,
                              @Query("numOfRows") int numOfRows,
                              @Query("listYN") char listYN,
                              @Query("arrange") char arrange,
                              @Query("MobileOS") String MobileOS,
                              @Query("MobileApp") String MobileApp,
                              @Query("_type") String type);
}
