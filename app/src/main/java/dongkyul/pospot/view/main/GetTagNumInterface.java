package dongkyul.pospot.view.main;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetTagNumInterface {
    String BaseURL = "http://13.67.42.177:8000/";
    @GET("crawling/tagnum")
    Call<JsonArray> getTagNum (
            @Query("name1") String name1,
            @Query("name2") String name2,
            @Query("name3") String name3,
            @Query("name4") String name4,
            @Query("name5") String name5);
}
