package com.vimalsagarji.vimalsagarjiapp;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }




}
