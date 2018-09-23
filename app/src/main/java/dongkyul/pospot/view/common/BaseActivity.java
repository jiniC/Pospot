package dongkyul.pospot.view.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import dongkyul.pospot.utils.SessionUtils;
import io.realm.Realm;

public class BaseActivity extends Activity implements View.OnClickListener {
    protected String token;
    protected static Realm realm;

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = SessionUtils.getString(this, SessionUtils.TOKEN,SessionUtils.TEST_TOKEN);
//        ContextUtils.setStatusColor(this, getWindow(), R.color.dark);
        /*
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("pospotDB")
                .deleteRealmIfMigrationNeeded()
                .build();
                */
        //realm = Realm.getInstance(config);
    }

    public void init(){

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
