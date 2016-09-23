package com.justwayward.reader.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.ListPopupWindow;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justwayward.reader.R;
import com.justwayward.reader.base.BaseActivity;
import com.justwayward.reader.base.Constant;
import com.justwayward.reader.bean.BookSource;
import com.justwayward.reader.bean.BookToc;
import com.justwayward.reader.bean.ChapterRead;
import com.justwayward.reader.bean.support.DownloadComplete;
import com.justwayward.reader.bean.support.DownloadProgress;
import com.justwayward.reader.bean.support.DownloadQueue;
import com.justwayward.reader.component.AppComponent;
import com.justwayward.reader.component.DaggerBookComponent;
import com.justwayward.reader.service.DownloadBookService;
import com.justwayward.reader.ui.adapter.BookReadPageAdapter;
import com.justwayward.reader.ui.adapter.TocListAdapter;
import com.justwayward.reader.ui.contract.BookReadContract;
import com.justwayward.reader.ui.presenter.BookReadPresenter;
import com.justwayward.reader.utils.BookPageFactory;
import com.justwayward.reader.utils.LogUtils;
import com.justwayward.reader.utils.SharedPreferencesUtil;
import com.justwayward.reader.utils.TTSPlayerUtils;
import com.justwayward.reader.utils.ToastUtils;
import com.justwayward.reader.view.BookReadFrameLayout;
import com.sinovoice.hcicloudsdk.android.tts.player.TTSPlayer;
import com.sinovoice.hcicloudsdk.common.tts.TtsConfig;
import com.sinovoice.hcicloudsdk.player.TTSCommonPlayer;
import com.yuyh.library.bookflip.FlipViewController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lfh on 2016/8/7.
 */
public class BookReadActivity extends BaseActivity implements BookReadContract.View,
        BookReadFrameLayout.OnScreenClickListener, FlipViewController.ViewFlipListener {

    @Bind(R.id.ivBack)
    ImageView mIvBack;
    @Bind(R.id.tvBookReadReading)
    TextView mTvBookReadReading;
    @Bind(R.id.tvBookReadCommunity)
    TextView mTvBookReadCommunity;
    @Bind(R.id.tvBookReadChangeSource)
    TextView mTvBookReadChangeSource;
    @Bind(R.id.ivBookReadMore)
    ImageView mIvBookReadMore;
    @Bind(R.id.llBookReadTop)
    LinearLayout mLlBookReadTop;
    @Bind(R.id.tvBookReadTocTitle)
    TextView mTvBookReadTocTitle;
    @Bind(R.id.tvBookReadMode)
    TextView mTvBookReadMode;
    @Bind(R.id.tvBookReadFeedBack)
    TextView mTvBookReadFeedBack;
    @Bind(R.id.tvBookReadSettings)
    TextView mTvBookReadSettings;
    @Bind(R.id.tvBookReadDownload)
    TextView mTvBookReadDownload;
    @Bind(R.id.tvBookReadToc)
    TextView mTvBookReadToc;
    @Bind(R.id.llBookReadBottom)
    LinearLayout mLlBookReadBottom;
    @Bind(R.id.rlBookReadRoot)
    RelativeLayout mRlBookReadRoot;
    @Bind(R.id.brflRoot)
    BookReadFrameLayout mBookReadFrameLayout;
    @Bind(R.id.tvDownloadProgress)
    TextView mTvDownloadProgress;

    @Bind(R.id.flipView)
    FlipViewController flipView;
    int lineHeight = 0;

    @Inject
    BookReadPresenter mPresenter;

    private List<String> mContentList = new ArrayList<>();
    private BookReadPageAdapter readPageAdapter;

    private List<BookToc.mixToc.Chapters> mChapterList = new ArrayList<>();
    private ListPopupWindow mTocListPopupWindow;
    private TocListAdapter mTocListAdapter;

    private String bookId;
    private int currentChapter = 1;
    private BookPageFactory factory;

    /**
     * 是否开始阅读章节
     **/
    boolean startRead = false;
    /**
     * 当前是否处于最后一页
     **/
    boolean endPage = false;
    /**
     * 当前是否处于第一页
     **/
    boolean startPage = false;
    /**
     * 是否是跳转到上一章
     **/
    private boolean isPre = false;

    /**
     * 朗读 播放器
     */
    private TTSPlayer mTtsPlayer;
    private TtsConfig ttsConfig;

    private IntentFilter intentFilter = new IntentFilter();
    private BatteryReceiver batteryReceiver = new BatteryReceiver();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    @Override
    public int getLayoutId() {
        statusBarColor = -1;
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        return R.layout.activity_book_read;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        showDialog();
    }

    @Override
    public void initDatas() {
        EventBus.getDefault().register(this);
        bookId = getIntent().getStringExtra("bookId");
        mTvBookReadTocTitle.setText(getIntent().getStringExtra("bookName"));

        mTtsPlayer = TTSPlayerUtils.getTTSPlayer();
        ttsConfig = TTSPlayerUtils.getTtsConfig();

        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
    }

    @Override
    public void configViews() {
        View view = getLayoutInflater().inflate(R.layout.item_book_read_page, null);
        final TextView tv = (TextView) view.findViewById(R.id.tvBookReadContent);
        lineHeight = tv.getLineHeight();
        LogUtils.i("line height:" + lineHeight + "  getLineHeight:");
        factory = new BookPageFactory(bookId, lineHeight);

        mTocListAdapter = new TocListAdapter(this, mChapterList);
        mTocListPopupWindow = new ListPopupWindow(this);
        mTocListPopupWindow.setAdapter(mTocListAdapter);
        mTocListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mTocListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mTocListPopupWindow.setAnchorView(mLlBookReadTop);
        mTocListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTocListPopupWindow.dismiss();
                currentChapter = position + 1;
                startRead = false;
                isPre = false;
                readCurrentChapter();
                hideReadBar();
            }
        });

        mPresenter.attachView(this);
        mPresenter.getBookToc(bookId, "chapters");
        mBookReadFrameLayout.setOnScreenClickListener(this);

        flipView.setOnViewFlipListener(this);

    }

    /**
     * 读取currentChapter章节。章节文件存在则直接阅读，不存在就请求加载
     */
    public void readCurrentChapter() {
        if (factory.getBookFile(currentChapter).length() > 50)
            showChapterRead(null, currentChapter);
        else
            mPresenter.getChapterRead(mChapterList.get(currentChapter - 1).link, currentChapter);
    }

    @Override
    public void showBookToc(List<BookToc.mixToc.Chapters> list) { // 加载章节列表
        mChapterList.clear();
        mChapterList.addAll(list);

        readCurrentChapter();
    }

    @Override
    public synchronized void showChapterRead(ChapterRead.Chapter data, int chapter) { // 加载章节内容
        if (data != null)
            factory.append(data, chapter); // 缓存章节保存到文件

        // 阅读currentChapter章节
        if (factory.getBookFile(currentChapter).length() > 50 && !startRead && currentChapter < mChapterList.size()) {
            startRead = true;
            new BookPageTask().execute();
        }

        if (chapter == currentChapter) {
            // 每次都往后继续缓存三个章节
            for (int j = currentChapter + 1; j <= currentChapter + 3 && j <= mChapterList.size(); j++) {
                if (factory.getBookFile(j).length() < 50) { // 认为章节文件不存在
                    // 获取对应章节
                    mPresenter.getChapterRead(mChapterList.get(j - 1).link, j);
                } else {
                    new ChapterCacheTask().execute(j); // 文章存在，则读取，放到LRUMap中
                }
            }
        } else if (factory.getBookFile(chapter).length() > 50 && !factory.hasCache(chapter)) { // 新获取的章节，还未缓存在LruMap
            new ChapterCacheTask().execute(chapter);
        }
    }

    @Override
    public void showBookSource(List<BookSource> list) {

    }

    @Override
    public void netError() {
        hideDialog();//防止因为网络问题而出现dialog不消失
        ToastUtils.showToast(R.string.net_error);
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        if (mTocListPopupWindow.isShowing()) {
            mTocListPopupWindow.dismiss();
        } else {
            finish();
        }
    }

    @OnClick(R.id.tvBookReadReading)
    public void readBook() {
        if (mTtsPlayer.getPlayerState() == TTSCommonPlayer.PLAYER_STATE_PLAYING) {
            mTtsPlayer.pause();
        } else if (mTtsPlayer.getPlayerState() == TTSCommonPlayer.PLAYER_STATE_PAUSE) {
            mTtsPlayer.resume();
        } else if (mTtsPlayer.getPlayerState() == TTSCommonPlayer.PLAYER_STATE_IDLE) {
            mTtsPlayer.play(mContentList.get(flipView.getSelectedItemPosition()), ttsConfig.getStringConfig());
        } else {
            ToastUtils.showSingleToast("播放器内部错误");
        }
    }

    @OnClick(R.id.tvBookReadChangeSource)
    public void onClickChangeSource() {

    }

    @OnClick(R.id.tvBookReadMode)
    public void onClickChangeMode() {
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
            SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }

    @OnClick(R.id.tvBookReadToc)
    public void onClickToc() {
        if (!mTocListPopupWindow.isShowing()) {
            visible(mTvBookReadTocTitle);
            gone(mTvBookReadReading, mTvBookReadCommunity, mTvBookReadChangeSource, mIvBookReadMore);
            mTocListPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mTocListPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mTocListPopupWindow.show();
        }
        mTocListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tvBookReadDownload)
    public void downloadBook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("缓存多少章？")
                .setItems(new String[]{"后面五十章", "后面全部", "全部"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, currentChapter + 1, currentChapter + 50));
                                break;
                            case 1:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, currentChapter + 1, mChapterList.size()));
                                break;
                            case 2:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, 1, mChapterList.size()));
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDownProgress(DownloadProgress progress) {
        if (bookId.equals(progress.bookId)) {
            LogUtils.e(progress.bookId + " " + progress.progress + "/" + mChapterList.size());
            if (isVisible(mLlBookReadBottom)) { // 如果工具栏显示，则进度条也显示
                visible(mTvDownloadProgress);
                mTvDownloadProgress.setText(String.format(getString(R.string.book_read_download_progress), mChapterList.get(progress.progress - 1).title, progress.progress, mChapterList.size()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadComplete(DownloadComplete complete) {
        if (bookId.equals(complete.bookId)) {
            mTvDownloadProgress.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTvDownloadProgress.setText("缓存完成");
                }
            }, 500);
            mTvDownloadProgress.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gone(mTvDownloadProgress);
                }
            }, 2500);
        }
    }

    private void hideReadBar() { // 隐藏工具栏
        gone(mLlBookReadBottom, mLlBookReadTop, mTvDownloadProgress);
    }

    private void showReadBar() { // 显示工具栏
        visible(mLlBookReadBottom, mLlBookReadTop);
    }

    private void toggleReadBar() { // 切换工具栏 隐藏/显示 状态
        if (isVisible(mLlBookReadBottom)) {
            hideReadBar();
        } else {
            showReadBar();
        }
    }

    @Override
    public void onSideClick(boolean isLeft) {
        if (isVisible(mLlBookReadBottom)) { //如果导航栏显示则隐藏
            hideReadBar();
            return;
        }

        if (isLeft) {
            if (flipView.getSelectedItemPosition() == 0) {
                startPage = true;
            }
            endPage = false;
            flipView.setSelection(flipView.getSelectedItemPosition() - 1);
        } else {
            flipView.setSelection(flipView.getSelectedItemPosition() + 1);
            if (flipView.getSelectedItemPosition() == mContentList.size() - 1) {
                endPage = true;
            }
            startPage = false;
        }
    }

    @Override
    public void onCenterClick() {
        toggleReadBar();
    }

    @Override
    public void onViewFlipped(View view, int position) { // 页面滑动切换
        hideReadBar();
        LogUtils.i("onViewFlipped--" + position);
        if (position == mContentList.size() - 1) { // 切换到最后一页
            if (!endPage) {
                endPage = true;// 标记。继续切换时就切换到下一章节
                return;
            }
            endPage = false;
            onNextChapter();
        } else if (position == 0) { // 切换到第一页
            if (!startPage) {
                startPage = true; // 标记。继续切换时就切换到上一章节
                return;
            }
            startPage = false;
            onPreChapter();
        } else {
            startPage = false;
            endPage = false;
        }
    }

    @Override
    public void onPreChapter() { // 加载上一章
        if (currentChapter > 1) {
            currentChapter -= 1;
            startRead = false;
            isPre = true; // 标记。加载完成之后显示最后一页
            readCurrentChapter();
        }
    }

    @Override
    public void onNextChapter() { // 加载下一章
        if (currentChapter < mChapterList.size()) {
            currentChapter += 1;
            startRead = false;
            startPage = true;
            readCurrentChapter();
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    /**
     * 读取章节内容，并进行分页处理
     */
    class BookPageTask extends AsyncTask<Integer, Integer, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!getDialog().isShowing())
                showDialog();
            LogUtils.i("分页前" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(System.currentTimeMillis())));
        }

        @Override
        protected List<String> doInBackground(Integer... params) {
            List<String> list = factory.readPage(currentChapter);
            return list;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            LogUtils.i("分页后" + new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(System.currentTimeMillis())));
            mContentList.clear();
            mContentList.addAll(list);

            if (readPageAdapter == null) {
                readPageAdapter = new BookReadPageAdapter(mContext, mContentList, mChapterList.get(currentChapter - 1).title);
                registerReceiver(batteryReceiver, intentFilter);
            } else {
                readPageAdapter.title = mChapterList.get(currentChapter - 1).title;
            }
            readPageAdapter.setTime(sdf.format(new Date()));
            flipView.setAdapter(readPageAdapter);

            if (isPre) { // 如果是加载上一章，则跳转到最后一页
                flipView.setSelection(mContentList.size() - 1);
                endPage = true;
                isPre = false;
            } else {
                startPage = true;
            }
            hideDialog();
        }
    }

    /**
     * 缓存章节分页结果（预处理）
     */
    class ChapterCacheTask extends AsyncTask<Integer, Integer, List<String>> {

        @Override
        protected List<String> doInBackground(Integer... params) {
            int chapter = params[0];
            factory.readPage(chapter);
            LogUtils.i("缓存章节分页结果:" + chapter);
            return null;
        }
    }

    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                readPageAdapter.setBattery(((level * 100) / scale) + "%");
            } else if(Intent.ACTION_TIME_TICK.equals(intent.getAction())){
                readPageAdapter.setTime(sdf.format(new Date()));
            }
            readPageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mTocListPopupWindow.isShowing()) {
                mTocListPopupWindow.dismiss();
                mTvBookReadTocTitle.setVisibility(View.GONE);
                mTvBookReadReading.setVisibility(View.VISIBLE);
                mTvBookReadCommunity.setVisibility(View.VISIBLE);
                mTvBookReadChangeSource.setVisibility(View.VISIBLE);
                mIvBookReadMore.setVisibility(View.VISIBLE);
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        flipView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flipView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTtsPlayer.getPlayerState() == TTSCommonPlayer.PLAYER_STATE_PLAYING)
            mTtsPlayer.stop();
        EventBus.getDefault().unregister(this);
        mPresenter.cancelDownload();
        unregisterReceiver(batteryReceiver);
    }
}
