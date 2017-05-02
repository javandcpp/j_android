/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jufan.ijkplayer;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.jufan.ijkplayer.inter.IActivityLiftCycle;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class BasicIjkPlayer extends BaseIjkPlayerAdapter implements IActivityLiftCycle, CacheListener {
    private String TAG = "Ijk";
    // settable by the client
    protected Uri mUri;
    protected Map<String, String> mHeaders;

    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;

    protected static final int STATE_ERROR = -1;
    protected static final int STATE_IDLE = 0;
    protected static final int STATE_PREPARING = 1;
    protected static final int STATE_PREPARED = 2;
    protected static final int STATE_PLAYING = 3;
    protected static final int STATE_PAUSED = 4;
    protected static final int STATE_PLAYBACK_COMPLETED = 5;

    protected int mCurrentState = STATE_IDLE;
    protected int mTargetState = STATE_IDLE;

    protected IMediaPlayer mMediaPlayer = null;
    protected int mCurrentBufferPercentage;
    private Context mAppContext;
    private static volatile BasicIjkPlayer instance = null;

    /*********缓冲下载***********/
    private HttpProxyCacheServer proxy;

    public HttpProxyCacheServer getProxy(Context context) {
        return proxy == null ? (proxy = newProxy(context)) :proxy;
    }

    private HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer(context);
    }

    /**
     * 检查缓存的状态
     */
    private void checkCachedState(String url) {
        HttpProxyCacheServer proxy = getProxy(mContext);
        boolean fullyCached = proxy.isCached(url);
        Log.d(TAG,"fullyCached:"+fullyCached);
//        if (fullyCached && onCacheListener != null) {
//            onCacheListener.getCacheProgress(100);
//        }
    }
    /********************/

    public static BaseIjkPlayerAdapter getInstance(Context cxt) {
        if (null == instance) {
            synchronized (BasicIjkPlayer.class) {
                if (null == instance) {
                    instance = new BasicIjkPlayer(cxt);
                }
            }
        }
        return instance;
    }


    private BasicIjkPlayer(Context context, SurfaceView view) {
        super(context, view);
        initPlayer(context);
    }

    private BasicIjkPlayer(Context context) {
        super(context);
        initPlayer(context);
    }


    private void initPlayer(Context context) {
        mAppContext = context.getApplicationContext();
        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
        mMediaPlayer = createPlayer();
    }


    @Override
    public void setOnPreparedListener(OnPreparedListener l) {
        mMediaPlayer.setOnPreparedListener(l);
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener l) {
        mMediaPlayer.setOnCompletionListener(l);
    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        mMediaPlayer.setOnBufferingUpdateListener(listener);
    }

    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        mMediaPlayer.setOnSeekCompleteListener(listener);
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        mMediaPlayer.setOnVideoSizeChangedListener(listener);
    }

    @Override
    public void setOnErrorListener(OnErrorListener l) {
        mMediaPlayer.setOnErrorListener(l);
    }

    @Override
    public void setOnInfoListener(OnInfoListener l) {
        mMediaPlayer.setOnInfoListener(l);
    }

    @Override
    public void setOnTimedTextListener(OnTimedTextListener listener) {
        mMediaPlayer.setOnTimedTextListener(listener);
    }

    @Override
    public void setAudioStreamType(int streamtype) {
        mMediaPlayer.setAudioStreamType(streamtype);
    }

    @Override
    public void setKeepInBackground(boolean keepInBackground) {
        mMediaPlayer.setKeepInBackground(keepInBackground);
    }

    @Override
    public int getVideoSarNum() {
        return mMediaPlayer.getVideoSarNum();
    }

    @Override
    public int getVideoSarDen() {
        return mMediaPlayer.getVideoSarDen();
    }

    @Override
    public void setWakeMode(Context context, int mode) {
        mMediaPlayer.setWakeMode(context, mode);
    }

    @Override
    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    @Override
    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        return mMediaPlayer.getTrackInfo();
    }

    @Override
    public void setSurface(Surface surface) {
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDataSource(IMediaDataSource mediaDataSource) {
        mMediaPlayer.setDataSource(mediaDataSource);
    }

    @Override
    public void openAudioWithURI(Uri uri) {
        super.openAudioWithURI(uri);
        openAudioWithURI(uri, null);
    }

    private void openAudioWithURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        if (null != mMediaPlayer) {
            open();
        }
    }

    @Override
    public void openAudioWithFile(File file) {
        super.openAudioWithFile(file);
        if (null != file && file.exists()) {
            openAudioWithURI(Uri.parse(file.getPath()));
        }
    }

    private void open() {
        if (mUri == null) {
            return;
        }
        release(false);
        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        try {
            HttpProxyCacheServer proxy = getProxy(mContext);
            proxy.registerCacheListener(this, mUri.toString());
            String proxyUrl = proxy.getProxyUrl(mUri.toString());
            Uri destUri=Uri.parse(proxyUrl);
            Log.d(TAG,proxyUrl.toString());
            mCurrentBufferPercentage = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mAppContext, destUri, mHeaders);
            } else {
                mMediaPlayer.setDataSource(destUri.toString());
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
        } finally {
        }
    }

    /**
     * 播放器状态获取
     *
     * @return
     */
    @Override
    public int getCurrentState() {
        return mCurrentState;
    }

    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            // REMOVED: mPendingSubtitleTracks.clear();
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setDisplay(sh);
        }
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (null != mMediaPlayer) {
            mMediaPlayer.setDataSource(context, uri);
        }
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {

    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public String getDataSource() {
        return null;
    }

    @Override
    public void prepareAsync() throws IllegalStateException {

    }

    /**
     * 开始播放
     */
    public void start() {
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void stop() throws IllegalStateException {
        if (isInPlaybackState()){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
                mCurrentState=STATE_IDLE;
            }
        }
        mTargetState=STATE_IDLE;
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
        mTargetState = STATE_PAUSED;
    }

    @Override
    public void setScreenOnWhilePlaying(boolean screenOn) {

    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    public void suspend() {
        release(false);
    }

    /**
     * 获取播放时长
     *
     * @return
     */
    @Override
    public long getDuration() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override
    public void release() {
        releasePlayer();
    }

    @Override
    public void reset() {

    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public int getAudioSessionId() {
        return null != mMediaPlayer ? mMediaPlayer.getAudioSessionId() : -1;
    }

    @Override
    public MediaInfo getMediaInfo() {
        return null != mMediaPlayer ? mMediaPlayer.getMediaInfo() : null;
    }

    @Override
    public void setLogEnabled(boolean enable) {
        mMediaPlayer.setLogEnabled(enable);
    }

    @Override
    public boolean isPlayable() {
        return null != mMediaPlayer ? mMediaPlayer.isPlayable() : false;
    }

    /**
     * 当前播放位置
     *
     * @return
     */
    @Override
    public long getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 拖拽播放
     *
     * @param msec
     */
    private void seek(long msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
        }
    }

    /**
     * 是否播放中
     *
     * @return
     */
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        this.seek(msec);
    }


    /**
     * 状态判断
     *
     * @return
     */
    public boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE);
    }


    /**
     * 播放器释放
     */
    public void releasePlayer() {
        release(false);
    }

    /**
     * 播放器创建
     *
     * @return
     */
    private IMediaPlayer createPlayer() {
        IMediaPlayer mediaPlayer = null;
        IjkMediaPlayer ijkMediaPlayer = null;
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        mediaPlayer = ijkMediaPlayer;
        return mediaPlayer;
    }


    @Override
    public void onActivityCreate() {

    }

    @Override
    public void onActivityStart() {

    }

    @Override
    public void onActivityResume() {

    }

    @Override
    public void onActivityPause() {

    }

    @Override
    public void onActivityDestroy() {

    }

    /**
     *
     */
    @Override
    public void destroy() {
        getProxy(mContext).unregisterCacheListener(this);
        release();
    }
    /**
     * 缓存下载
     * @param cacheFile
     * @param url
     * @param progress
     */
    @Override
    public void onCacheAvailable(File cacheFile, String url, int progress) {
        Log.d(TAG,"url:"+url+",progress:"+progress+",cacheFile:"+cacheFile.getAbsolutePath());
    }
}
