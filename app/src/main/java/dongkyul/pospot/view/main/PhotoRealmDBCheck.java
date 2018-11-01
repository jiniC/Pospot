package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.widget.TextView;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PhotoRealmDBCheck extends BaseActivity {

    TextView tvMarkerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_db);
        tvMarkerTitle = (TextView)findViewById(R.id.tvMarkerTitle);
        RealmConfiguration config = new RealmConfiguration.Builder().name("PhotoToAdd").build();
        Realm realm = Realm.getInstance(config);
        PhotoMarkerDB result = realm.where(PhotoMarkerDB.class).findFirst();
        String output="";
        for(byte[] bytes:result.getPhotoList()){
            output+=toString(result,bytes);
        }
        tvMarkerTitle.setText(output);
        realm.close();
    }

    public String toString(PhotoMarkerDB result, byte[] bytes) {
        return "PhotoMarker {" +
                "title='" + result.getTitle() + '\'' +
                ", lat='" + result.getLat() + '\'' +
                ", lon=" + result.getLon() + '\'' +
                ", photoList=" + bytes +
                '}';
    }
}