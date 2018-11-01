package dongkyul.pospot.view.common;

import android.app.Application;

import io.realm.Realm;
import lombok.Data;

@Data
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
