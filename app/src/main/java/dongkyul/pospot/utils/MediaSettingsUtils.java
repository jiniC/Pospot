package dongkyul.pospot.utils;

import android.content.Context;
import android.media.AudioManager;

public class MediaSettingsUtils {
    public static void changeVolume(Context context,int vol){
        ((AudioManager)context.getSystemService(Context.AUDIO_SERVICE)).setStreamVolume(AudioManager.STREAM_MUSIC,vol,AudioManager.FLAG_PLAY_SOUND);
    }
    public static int getVolume(Context context){
        return ((AudioManager)context.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(AudioManager.STREAM_MUSIC);
    }

}
