package com.yzk.rxjavaretrofitdagger.application;

import android.app.Application;

import com.yzk.rxjavaretrofitdagger.component.AppComponent;
import com.yzk.rxjavaretrofitdagger.component.DaggerAppComponent;
import com.yzk.rxjavaretrofitdagger.module.ApiModule;
import com.yzk.rxjavaretrofitdagger.module.AppModule;

/**
 * Created by android on 9/22/16.
 */
public class AppApplication extends Application {

    private static AppApplication sInstance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.sInstance = this;
        initCompoent();

    }

    private void initCompoent() {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).apiModule(new ApiModule()).build();
    }

    public static AppApplication getsInstance() {
        return sInstance;
    }

    public AppComponent getAppComponet() {
        return appComponent;

    }
}
