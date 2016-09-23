package com.yzk.rxjavaretrofitdagger.presenter;

import android.util.Log;

import com.yzk.rxjavaretrofitdagger.api.NetApi;
import com.yzk.rxjavaretrofitdagger.base.OtherBase;
import com.yzk.rxjavaretrofitdagger.base.RxPresenter;
import com.yzk.rxjavaretrofitdagger.bean.BannerData;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by android on 9/22/16.
 */
public class OtherPresenter extends RxPresenter<OtherBase.View> implements OtherBase.Presenter {

    private final NetApi netapi;

    @Override
    public String getResult() {
        return "other prester";
    }

    @Inject
    public OtherPresenter(NetApi netApi) {
        this.netapi = netApi;
    }

    public void requestBanner() {
        Observable<BannerData> observable = netapi.getApiService().getBanner();

        Subscription subscribe = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BannerData>() {
            @Override
            public void onCompleted() {
                Log.e("TAG", "complete");
                mView.complete();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "error");
                mView.error();
            }

            @Override
            public void onNext(BannerData bannerData) {
                Log.e("TAG", bannerData.toString() + "");
                mView.showBanner(bannerData);

            }
        });
        addSubscrebe(subscribe);
    }
}
