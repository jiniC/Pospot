package dongkyul.pospot.view.common;

import android.app.Application;

import dongkyul.pospot.view.main.PhotoMarkerDBMigration;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import lombok.Data;

@Data
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().name("PhotoToAdd").migration(new PhotoMarkerDBMigration()).build();
        Realm.setDefaultConfiguration(config);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
