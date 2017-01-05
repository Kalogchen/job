package com.intermediary.job.utlis;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kalogchen on 2016/12/21.
 */

public class SharedPreferencesUtils {
    public static String sharePreName = "config";

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(sharePreName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(sharePreName, Context.MODE_PRIVATE);
        boolean spBoolean = sp.getBoolean(key, defaultValue);
        return spBoolean;

    }

    public static void setString(Context context, String key, String text) {
        SharedPreferences sp = context.getSharedPreferences(sharePreName, Context.MODE_PRIVATE);
        sp.edit().putString(key, text).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(sharePreName, Context.MODE_PRIVATE);
        String spString = sp.getString(key, defaultValue);
        return spString;
    }

    public static void setInt(Context context, String key, int record) {
        SharedPreferences sp = context.getSharedPreferences(sharePreName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, record).commit();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(sharePreName, Context.MODE_PRIVATE);
        int spInt = sp.getInt(key, defaultValue);
        return spInt;
    }

}
