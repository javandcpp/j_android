package com.justwayward.reader.ui.contract;

import com.justwayward.reader.base.BaseContract;
import com.justwayward.reader.bean.RankingList;

/**
 * @author yuyh.
 * @date 16/9/1.
 */
public interface TopRankContract {

    interface View extends BaseContract.BaseView {
        void showRankList(RankingList rankingList);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getRankList();
    }

}
