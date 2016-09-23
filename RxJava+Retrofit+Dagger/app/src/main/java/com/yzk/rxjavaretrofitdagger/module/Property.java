package com.yzk.rxjavaretrofitdagger.module;

import javax.inject.Inject;

/**
 * Created by android on 9/22/16.
 */

public class Property {
    private String mPemo;

    // 用Inject标记构造函数,表示用它来注入到目标对象中去
    @Inject
    public Property(String string) {
        mPemo = "生活就像海洋";
    }

    public String getPemo() {
        return mPemo;
    }
}
