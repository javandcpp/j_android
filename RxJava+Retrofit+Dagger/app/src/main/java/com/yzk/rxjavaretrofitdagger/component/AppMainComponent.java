package com.yzk.rxjavaretrofitdagger.component;

import com.yzk.rxjavaretrofitdagger.api.NetApi;
import com.yzk.rxjavaretrofitdagger.module.PropertyModule;
import com.yzk.rxjavaretrofitdagger.ui.activity.MainActivity;
import com.yzk.rxjavaretrofitdagger.ui.activity.OtherActivity;

import dagger.Component;

/**
 * Created by android on 9/22/16.
 */

//@Singleton
@Component(dependencies = AppComponent.class, modules = PropertyModule.class)
public abstract class AppMainComponent {

    public abstract MainActivity inject(MainActivity mainActivity);

    public abstract OtherActivity inject(OtherActivity mainActivity);

    public abstract NetApi getApiService();

    private static AppMainComponent sComponent;

    public static AppMainComponent getInstance() {
        if (sComponent == null) {
            sComponent = DaggerAppMainComponent.builder().build();

        }

        return sComponent;
    }

}
