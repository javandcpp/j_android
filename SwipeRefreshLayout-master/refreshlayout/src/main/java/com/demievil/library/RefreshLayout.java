package com.demievil.library;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * 使用说明
 *
 mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_container);
 initAdapter();
 mRefreshLayout.setAdapter(mAdapter);
 onRefreshComplete
 */
public class RefreshLayout extends SwipeRefreshLayout {




    private final int mTouchSlop;
    private ListView mListView;
    private OnListRefreshListener mOnListRefreshListener;

    private float firstTouchY;
    private float lastTouchY;

    private boolean isLoading = false;
    private View footerLayout;
    //    private TextView textMore;
    private ProgressBar progressBar;

    public RefreshLayout(Context context) {
        this(context, null);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView();
    }

    public ListView getListView() {
        return mListView;
    }

    public void setAdapter(BaseAdapter adapter){
        mListView.setAdapter(adapter);
    }

    private void initView() {
        mListView = (ListView) View.inflate(getContext(), R.layout.listview, null);
        footerLayout = View.inflate(getContext(), R.layout.listview_footer, null);
//        textMore = (TextView) footerLayout.findViewById(R.id.text_more);
        progressBar = (ProgressBar) footerLayout.findViewById(R.id.load_progress_bar);

        this.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow);

        //这里可以替换为自定义的footer布局
        //you can custom FooterView
        mListView.addFooterView(footerLayout);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (canLoadMore()) {
                    loadData();
                }
            }
        });

        this.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null != mOnListRefreshListener) {
                    mOnListRefreshListener.refresh();
                }
            }
        });

        this.addView(mListView);
    }

    //set the child view of RefreshLayout,ListView
//    public void setChildView(ListView mListView) {
//
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        final int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                firstTouchY = event.getRawY();
//                break;
//
//            case MotionEvent.ACTION_UP:
//                lastTouchY = event.getRawY();
//                if (canLoadMore()) {
//                    loadData();
//                }
//                break;
//            default:
//                break;
//        }
//
//        return super.dispatchTouchEvent(event);
//    }

    private boolean canLoadMore() {
        return isBottom() && !isLoading;
//                && isPullingUp();
    }

    private boolean isBottom() {
        if (mListView.getCount() > 0) {
            if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 &&
                    mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPullingUp() {
        return (firstTouchY - lastTouchY) >= mTouchSlop;
    }

    private void loadData() {
        if (mOnListRefreshListener != null) {
            setLoading(true);
        }
    }

    public void onRefreshComplete() {

        Log.e("TAG", "isRefreshing:" + isRefreshing() + ",isLoading:" + isLoading);
        if (isLoading) {
            footerLayout.setVisibility(View.GONE);
        }
        if (isRefreshing()) {
            setRefreshing(false);
        }
        isLoading = false;
    }

    public void setLoading(boolean loading) {
        if (mListView == null) return;
        isLoading = loading;
        Log.e("TAG", isLoading + "");
        if (loading) {
            if (isRefreshing()) {
                setRefreshing(false);
            }
            mListView.setSelection(mListView.getAdapter().getCount() - 1);
            mOnListRefreshListener.loadMore();
            footerLayout.setVisibility(View.VISIBLE);
        } else {
            firstTouchY = 0;
            lastTouchY = 0;
        }
    }

    public void setOnListRefreshListener(OnListRefreshListener onListRefreshListener) {
        mOnListRefreshListener = onListRefreshListener;

    }

    public interface OnListRefreshListener {
        public void loadMore();

        public void refresh();
    }


}