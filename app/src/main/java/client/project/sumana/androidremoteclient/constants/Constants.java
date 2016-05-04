package client.project.sumana.androidremoteclient.constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

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

    public static void putServerName(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences("android_remote", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("server_name", name);
        editor.apply();
    }

    public static String getServerName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("android_remote", Context.MODE_PRIVATE);
        return pref.getString("server_name", null);
    }

    public static String getAbsoluteUrl(String relativeURL, Context context) {
        String server = Constants.getServerAddress(context);
        if(server.equals("")) {
            return null;
        }
        return "http://"+server+ ":8080"+ relativeURL;
    }

    public static boolean isServerConnected(Context context) {
        String serverAddress = Constants.getServerAddress(context);
        String serverName = Constants.getServerName(context);
        if(serverAddress == null || serverName == null || serverAddress.equals("") || serverName.equals("")) {
            return false;
        }
        return true;
    }

    public static void showErrorDialog(String title, String desc, Activity a) {
        new AlertDialog.Builder(a).setMessage(desc).setCancelable(false).setTitle(title).setPositiveButton("OK", null).create().show();
    }
}
