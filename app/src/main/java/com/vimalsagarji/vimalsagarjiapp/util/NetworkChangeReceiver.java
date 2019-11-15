package com.vimalsagarji.vimalsagarjiapp.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;

/**
 * Created by Grapes-Pradip on 2/8/2017.
 */

@SuppressWarnings("ALL")
public class NetworkChangeReceiver extends BroadcastReceiver {

    public NetworkChangeReceiver() {

    }

    public interface NetworkChange {
        void onNetworkChange(String status);
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, final Intent intent) {

        String status = NetworkClass.getConnectivityStatusString(context);
        InformationCategory csActivity = new InformationCategory();
        NetworkChange csNetworkChange = csActivity instanceof NetworkChange ? csActivity : null;
        csNetworkChange.onNetworkChange(status);
    }
}
