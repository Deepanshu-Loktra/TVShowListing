package com.loktra.tvshowapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppUtils {

    private static String TAG = AppUtils.class.getSimpleName();


    // Toasting Message
    public static void showToast(@Nullable Context activity, @NonNull String text) {
        if (activity != null) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Not making toast as activity is null");
        }
    }
}
