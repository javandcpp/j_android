package com.yzk.rxjavaretrofitdagger.api;

import com.yzk.rxjavaretrofitdagger.bean.BannerData;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by android on 9/19/16.
 */
public interface NetApiService {

    @POST("home/advert_banner.htm")
    Observable<BannerData> getBanner();

}
