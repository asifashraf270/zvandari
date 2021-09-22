package com.glowingsoft.zvandiri;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Tausif ur Rehman on 2/8/2019.
 */

public class SharedPreferencesClass {
    public static SharedPreferences sharedPreference;
    public static SharedPreferences.Editor editor;
    public static String fileName = "TextFile";
    public static SharedPreferencesClass sharedPreferencesClass;

    private SharedPreferencesClass() {
    }

    public static SharedPreferencesClass getInstance() {
        if (sharedPreferencesClass == null) {
            return new SharedPreferencesClass();
        }
        return sharedPreferencesClass;
    }


    public void putUserId(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public NetworkInfo checkInternetConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return netInfo;

    }


}
