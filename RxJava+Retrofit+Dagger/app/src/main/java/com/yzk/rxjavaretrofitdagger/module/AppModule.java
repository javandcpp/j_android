package com.yzk.rxjavaretrofitdagger.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by android on 9/22/16.
 */
@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context context) {
        this.mContext=context;
    }

    @Provides
    public Context provideContext(){
        return mContext;
    }


}
