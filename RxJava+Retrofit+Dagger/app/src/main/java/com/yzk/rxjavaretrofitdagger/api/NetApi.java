package com.yzk.rxjavaretrofitdagger.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by android on 9/19/16.
 */

public class NetApi {
    public static NetApi instance;


    private NetApiService apiService;

    private NetApi(OkHttpClient.Builder okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ityf.dzwsyl.com:8080/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient.build())
                .build();

        apiService = retrofit.create(NetApiService.class);
    }

    public static NetApi getInstance(OkHttpClient.Builder okHttpClient) {
        if (instance == null)
            instance = new NetApi(okHttpClient);
        return instance;
    }


    public NetApiService getApiService() {
        return apiService;
    }
}
