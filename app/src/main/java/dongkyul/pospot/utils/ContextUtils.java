package dongkyul.pospot.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

public class ContextUtils {

    public static void setStatusColor(Context context, Window window, int color) {

        if (Build.VERSION.SDK_INT>=21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setStatusBarColor(ContextCompat.getColor(context, color));

        }
    }
}
