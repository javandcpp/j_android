package com.yzk.clibapp;

import android.app.Application;

/**
 * Created by android on 9/26/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("JniSO");
    }
}
