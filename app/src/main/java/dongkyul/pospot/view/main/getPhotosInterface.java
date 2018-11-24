package dongkyul.pospot.view.main;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface getPhotosInterface {
    String BaseURL = "http://13.67.42.177:8000/";
    @GET("crawling/pictures")
    Call<JsonObject> getPhotos (
            @Query("name") String name
    );
}
