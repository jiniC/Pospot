package dongkyul.pospot.network;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import dongkyul.pospot.utils.ImproveDateTypeAdapter;
import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zuby on 2018. 5. 9..
 */

public class RestApi {

    public final static String token = "AAAAB3NzaC1yc2EAAAADAQABAAABAQCzvMKnZEdWDYyzWNRN6D8xcIRbmg9gIpH7LxFQf5X3S/NTpPjlOPDAXOUkGIs3ZXvvG0LAsqDxUqa8dAsnMBqqmzvYLpTLXaDxmK";

    @Getter
    private Context context;
    private static RestApi instance ;
    @Getter
    private Retrofit retrofit;


    public RestApi(Context mContext) {
        this.context = mContext;
        GsonBuilder builder = new GsonBuilder();

// Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new ImproveDateTypeAdapter());

        Gson gson = builder.create();
        retrofit = new Retrofit.Builder()
                .baseUrl(dongkyul.pospot.network.NetworkConstant.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RestApi getInstance(Context context) {
        if(instance ==null) {
            instance = new RestApi(context);
        }
        return instance;
    }
    public static void checkNetworkPermission(final Context context){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("네트연결에 문제가 생겼습니다.\n 잠시후 다시 시도해주세요.");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(-1);
            }
        });

        alert.show();
    }

}
