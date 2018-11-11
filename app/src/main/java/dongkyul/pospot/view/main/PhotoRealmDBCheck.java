package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.widget.TextView;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PhotoRealmDBCheck extends BaseActivity {

    TextView tvMarkerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_db);
        tvMarkerTitle = (TextView)findViewById(R.id.tvMarkerTitle);
        RealmConfiguration config = new RealmConfiguration.Builder().name("PhotoToAdd").build();
        Realm realm = Realm.getInstance(config);
        RealmResults<PhotoMarkerDB> results = realm.where(PhotoMarkerDB.class).findAll(); //모든 포토마커 정보를 가져옴
        String output="";
        for(PhotoMarkerDB result:results) { //photomarker별로
            output+=result.toString(); //결과를 붙여주기
            output+="\n"; //개행
        }
        tvMarkerTitle.setText(output);
        realm.close();
    }
}