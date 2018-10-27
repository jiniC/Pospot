package dongkyul.pospot.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmResults;

public class PhotoRealmDBCheck extends BaseActivity {

    TextView tvMarkerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_db);
        tvMarkerTitle = (TextView)findViewById(R.id.tvMarkerTitle);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PhotoMarkerDB> results = realm.where(PhotoMarkerDB.class).findAllAsync();
        results.load();
        String output="";
        for(PhotoMarkerDB photoMarkerDB:results){
            output += photoMarkerDB.toString();
        }
        tvMarkerTitle.setText(output);
        realm.close();
    }

}