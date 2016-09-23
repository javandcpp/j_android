package com.yzk.rxjavaretrofitdagger.module;

import android.util.Log;

import com.yzk.rxjavaretrofitdagger.api.NetApi;
import com.yzk.rxjavaretrofitdagger.api.support.HeaderInterceptor;
import com.yzk.rxjavaretrofitdagger.api.support.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by android on 9/22/16.
 */

@Module
public class ApiModule {

    /**
     * 自定义日志输出
     */
    public static class MyLog implements LoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Log.e("OKHTTP", "oklog: " + message);
        }
    }

    public ApiModule() {

    }

    private OkHttpClient.Builder getOkHttpClient(){
        LoggingInterceptor logging = new LoggingInterceptor(new MyLog());
        logging.setLevel(LoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(logging);
    }


    @Provides
    public NetApi getApiService(){
        return NetApi.getInstance(getOkHttpClient());
    }

}
