package com.yzk.rxjavaretrofitdagger.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzk.rxjavaretrofitdagger.R;
import com.yzk.rxjavaretrofitdagger.base.BaseActivity;
import com.yzk.rxjavaretrofitdagger.base.OtherBase;
import com.yzk.rxjavaretrofitdagger.bean.BannerData;
import com.yzk.rxjavaretrofitdagger.component.AppComponent;
import com.yzk.rxjavaretrofitdagger.component.AppMainComponent;
import com.yzk.rxjavaretrofitdagger.component.DaggerAppMainComponent;
import com.yzk.rxjavaretrofitdagger.module.Property;
import com.yzk.rxjavaretrofitdagger.presenter.OtherPresenter;

import javax.inject.Inject;

import butterknife.Bind;

public class OtherActivity extends BaseActivity implements OtherBase.View {


    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.btn1)
    Button btn;

    @Inject
    Property property;


    @Inject
    OtherPresenter otherPresenter;

    private AppMainComponent build;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {
        mCommonToolBar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        btn.setVisibility(View.GONE);
    }

    @Override
    public void setupActivityComponet(AppComponent appComponent) {
        build = DaggerAppMainComponent.builder().appComponent(appComponent).build();
        build.inject(this);

    }

    @Override
    public void configViews() {

        otherPresenter.attachView(this);

        otherPresenter.requestBanner();

//        tv1.setText(property + ":" + build);

    }

    @Override
    public void error() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showBanner(BannerData bannerData) {
        tv1.setText(bannerData.toString());
    }
}
