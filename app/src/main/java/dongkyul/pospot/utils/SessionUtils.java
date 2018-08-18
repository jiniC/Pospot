package dongkyul.pospot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionUtils {

    public final static String TOKEN = "TOKEN";
    public final static String TEST_TOKEN = "AAAAB3NzaC1yc2EAAAADAQABAAABAQCzvMKnZEdWDYyzWNRN6D8xcIRbmg9gIpH7LxFQf5X3S/NTpPjlOPDAXOUkGIs3ZXvvG0LAsqDxUqa8dAsnMBqqmzvYLpTLXaDxmK";


    public static void putString(Context context, String key , String value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void putBoolean(Context context, String key , boolean value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public static void putLong(Context context, String key , long value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(key,value);
        editor.commit();
    }
    public static void putInt(Context context, String key , int value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(key,value);
        editor.commit();
    }
    public static void remove(Context context, String key){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(key);
        editor.commit();
    }
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key,defValue);
    }
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(key,defValue);
    }
    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getLong(key,defValue);
    }
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(key,defValue);
    }
}
