package com.yzk.rxjavaretrofitdagger.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.yzk.rxjavaretrofitdagger.R;
import com.yzk.rxjavaretrofitdagger.application.AppApplication;
import com.yzk.rxjavaretrofitdagger.component.AppComponent;
import com.yzk.rxjavaretrofitdagger.utils.StatusBarCompat;

import butterknife.ButterKnife;

/**
 * Created by android on 9/19/16.
 */
public abstract class BaseActivity extends AppCompatActivity {


    private int statusBarColor;
    protected Toolbar mCommonToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
//        if (statusBarColor > 0) {
            StatusBarCompat.compat(this, statusBarColor);
//        } else if(statusBarColor == 0){
//            StatusBarCompat.compat(this);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ButterKnife.bind(this);
        mCommonToolBar = ButterKnife.findById(this, R.id.common_tool);
        mCommonToolBar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setupActivityComponet(AppApplication.getsInstance().getAppComponet());
        if (mCommonToolBar != null) {
            initToolBar();
            setSupportActionBar(mCommonToolBar);
        }
//        mCommonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(BaseActivity.this, "click", Toast.LENGTH_SHORT).show();
//            }
//        });
        initDatas();
        configViews();
    }

    public abstract int getLayoutId();
    public abstract void initToolBar();
    public abstract void initDatas();
    public abstract void setupActivityComponet(AppComponent appComponent);

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
