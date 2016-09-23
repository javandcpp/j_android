package com.yzk.rxjavaretrofitdagger.component;

import android.content.Context;

import com.yzk.rxjavaretrofitdagger.api.NetApi;
import com.yzk.rxjavaretrofitdagger.module.ApiModule;
import com.yzk.rxjavaretrofitdagger.module.AppModule;

import dagger.Component;

/**
 * Created by android on 9/22/16.
 */

@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    Context getContent();

    NetApi getApiService();
}
