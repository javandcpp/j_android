package com.wsine.west.exp5;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * 描述：
 * 作者：chezi008 on 2016/11/30 10:48
 * 邮箱：chezi008@163.com
 */
public class App extends Application {

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(5 * 1024 * 1024)       // 1 mb for cache
                .build();
    }
}
