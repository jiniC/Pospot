package dongkyul.pospot.utils;

import android.Manifest;
import android.content.Context;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class PermissionUtils {
    public static void checkCameraPermission(final Context context, PermissionListener permissionlistener){
        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    public static void checkWritePermission(final Context context, PermissionListener permissionlistener){
        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    public static void checkLocationPermission(final Context context, PermissionListener permissionlistener){
        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }
}
