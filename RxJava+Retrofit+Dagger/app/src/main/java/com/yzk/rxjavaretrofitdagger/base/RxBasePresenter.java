package com.yzk.rxjavaretrofitdagger.base;

/**
 * Created by android on 9/22/16.
 */
public interface RxBasePresenter{

     interface BaseView {

         void error();
         void complete();
    }

     interface Presenter<T> {
         void attachView(T view);
         void detachView();
    }
}
