package com.gfcommunity.course.gfcommunity.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Check Network connection
 */
public class NetworkConnectedUtil {
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
