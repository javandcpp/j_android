package com.justwayward.reader.ui.activity;

import com.justwayward.reader.R;
import com.justwayward.reader.base.BaseActivity;
import com.justwayward.reader.component.AppComponent;

/**
 * 女生区
 */
public class BookMaleActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_community_book_male;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("女生区");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }
}
