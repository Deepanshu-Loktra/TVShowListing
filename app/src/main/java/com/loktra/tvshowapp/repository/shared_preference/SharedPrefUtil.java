package com.loktra.tvshowapp.repository.shared_preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.loktra.tvshowapp.utils.GsonHelper;

public class SharedPrefUtil {
    private static final String TAG = SharedPrefUtil.class.getSimpleName();
    public static SharedPreferences globalPrefs;

    public static void init(@NonNull Context context) {
        SharedPrefUtil.globalPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void saveStringToPreferences(String key,
                                               String value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getString(key, null);
    }

    public static void saveFloatToPreferences(String key, float value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloatFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getFloat(key, 0f);
    }

    public static void saveIntToPreferences(String key,
                                            int value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getInt(key, 0);
    }

    public static void saveLongToPreferences(String key,
                                             long value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getLong(key, 0);
    }

    public static void saveBooleanToPreferences(String key, boolean value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getBoolean(key, false);
    }

    public static void saveObjectToPrefs(@NonNull String key, Object value) {
        String toJson = GsonHelper.getInstance().toJson(value);
        SharedPrefUtil.saveStringToPreferences(key, toJson);
    }
}
