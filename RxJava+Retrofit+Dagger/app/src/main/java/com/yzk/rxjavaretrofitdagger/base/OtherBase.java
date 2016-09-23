package com.yzk.rxjavaretrofitdagger.base;

import com.yzk.rxjavaretrofitdagger.bean.BannerData;

/**
 * Created by android on 9/22/16.
 */
public interface OtherBase {

    interface View extends RxBasePresenter.BaseView {
//        void onSuccess();
        void showBanner(BannerData bannerData);
    }

    interface Presenter extends RxBasePresenter.Presenter<View>{
        String getResult();
    }

}
