package com.vimalsagarji.vimalsagarjiapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Grapes-Pradip on 2/8/2017.
 */

@SuppressWarnings("ALL")
public class NetworkClass {
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;


    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkClass.getConnectivityStatus(context);

        String status = null;
        if (conn == NetworkClass.TYPE_WIFI) {
            status = "TRUE";
        } else if (conn == NetworkClass.TYPE_MOBILE) {
            status = "TRUE";
        } else if (conn == NetworkClass.TYPE_NOT_CONNECTED) {
            status = "FALSE";
        }
        return status;
    }
}
