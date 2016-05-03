package client.project.sumana.androidremoteclient.constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sumana on 24-04-2016.
 */
public class Constants {
    public static String DISCOVERY_MESSAGE = "discover_android_remote";
    public static final int DISCOVERY_PORT = 8081;

    public static void putServerAddress(Context context, String address) {
        SharedPreferences pref = context.getSharedPreferences("android_remote", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("server_address", address);
        editor.apply();
    }

    public static String getServerAddress(Context context) {
        SharedPreferences pref = context.getSharedPreferences("android_remote", Context.MODE_PRIVATE);
        return pref.getString("server_address", null);
    }

    public static String getAbsoluteUrl(String relativeURL, Context context) {
        return "http://"+Constants.getServerAddress(context)+ ":8080"+ relativeURL;
    }
}
