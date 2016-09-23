package com.yzk.rxjavaretrofitdagger.ui.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.yzk.rxjavaretrofitdagger.R;
import com.yzk.rxjavaretrofitdagger.api.NetApi;
import com.yzk.rxjavaretrofitdagger.base.BaseActivity;
import com.yzk.rxjavaretrofitdagger.component.AppComponent;
import com.yzk.rxjavaretrofitdagger.component.AppMainComponent;
import com.yzk.rxjavaretrofitdagger.component.DaggerAppMainComponent;
import com.yzk.rxjavaretrofitdagger.module.Property;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Bind(R.id.tv1)
    TextView tv1;
    @Inject
    Property property;


    @Bind(R.id.btn1)
    Button btn;


    private AppMainComponent build;
    private AppMainComponent build1;
    private NetApi apiService;
    private AppMainComponent build2;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void setupActivityComponet(AppComponent appComponent) {
        build2 = DaggerAppMainComponent.builder().appComponent(appComponent).build();
        build2.inject(this);
    }

    @Override
    public void configViews() {
        tv1.setText(property+"");
    }

    @OnClick(R.id.btn1)
    public void clickBtn() {
        startActivity(new Intent(this, OtherActivity.class));
    }
}
