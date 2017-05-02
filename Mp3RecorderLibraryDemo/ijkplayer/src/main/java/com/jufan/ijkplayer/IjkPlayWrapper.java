package com.jufan.ijkplayer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by android on 12/20/16.
 */

public class IjkPlayWrapper<T> extends AbstractPlayer {




    private Surface mTextureSurface;
    static {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }
    public IjkPlayWrapper(Context context, T surfaceView) {
        super(context, surfaceView);
        ijkMediaPlayer = new IjkMediaPlayer();
        initListener();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);//设定log级别
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);//硬编码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);//RGBX8888
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);

    }

    @Override
    public void openVideo(File file) {
        if (null == ijkMediaPlayer) {
            throw new RuntimeException("ijkplayer NullPointException");
        }
////        Uri uri = Uri.parse("http://175.25.168.25/v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4?wsiphost=local");
////        Uri uri = Uri.parse("http://42.96.249.166/live/24035.m3u8");
//
////        ijkMediaPlayer.setDataSource(mContext, Uri.parse(file.getPath()), null);
//
////        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        ijkMediaPlayer.setDataSource(file.getAbsolutePath());
//////        ijkMediaPlayer.setDataSource(FileDescriptor);
////        Log.d(TAG,file.getAbsolutePath().toString()+":"+file.exists());
////        ijkMediaPlayer.setDataSource(new File(getsaveDirectory()+"mediaCodec.mp4").getAbsolutePath());
////        if (ijkMediaPlayer.isPlaying()) {
////            ijkMediaPlayer.reset();
////        }
//        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        ijkMediaPlayer.setScreenOnWhilePlaying(true);
//        ijkMediaPlayer.prepareAsync();
////        ijkMediaPlayer.start();
//        mCurrentState = STATE_PLAYING;
    }

    @Override
    public void openVideo() {

    }

    @Override
    public int getCurrentState() {
        return mCurrentState;
    }


    /**
     * 初始化监听
     */
    private void initListener() {
        defaultListener = new DefaultListener();
        ijkMediaPlayer.setOnPreparedListener(defaultListener);
//        ijkMediaPlayer.setOnVideoSizeChangedListener(defaultListener);
        ijkMediaPlayer.setOnCompletionListener(defaultListener);
        ijkMediaPlayer.setOnErrorListener(defaultListener);
        ijkMediaPlayer.setOnInfoListener(defaultListener);
        ijkMediaPlayer.setOnBufferingUpdateListener(defaultListener);
        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ijkMediaPlayer.setScreenOnWhilePlaying(true);

        if (mSurfaceView instanceof SurfaceView) {
            callBack = new SurfaceCallBack();
            mSurfaceHolder.addCallback(callBack);
            ijkMediaPlayer.setDisplay(mSurfaceHolder);
        } else if (mSurfaceView instanceof TextureView) {
            ((TextureView) mSurfaceView).setSurfaceTextureListener(new SurfaceTextureListener());
        }
    }


    private void releaseWithoutStop() {
        if (null != ijkMediaPlayer) {
            ijkMediaPlayer.setDisplay(null);
        }
        mCurrentState = STATE_IDLE;
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
        if (null != ijkMediaPlayer) {
            release();
            ijkMediaPlayer = null;
        }
    }


    public class SurfaceCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (ijkMediaPlayer != null) {
                mSurfaceHolder = surfaceHolder;
                ijkMediaPlayer.setDisplay(surfaceHolder);
                ijkMediaPlayer.setScreenOnWhilePlaying(true);
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = null;
            releaseWithoutStop();
        }
    }

    public void release() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.reset();
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
            mCurrentState = STATE_IDLE;
        }
    }


    public class DefaultListener implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener {

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            ijkMediaPlayer.start();
            mCurrentState = STATE_PREPARED;
        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

        }

        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            iMediaPlayer.stop();
            iMediaPlayer.reset();
            mCurrentState = STATE_IDLE;
        }

        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            mCurrentState = STATE_ERROR;
//            openVideo();
            return false;
        }

        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            mCurrentState = STATE_PLAYING;
            return false;
        }

        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

        }
    }


    public class SurfaceTextureListener implements TextureView.SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            mTextureSurface = new Surface(surfaceTexture);
            ijkMediaPlayer.setSurface(mTextureSurface);
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            surfaceTexture = null;
            releaseWithoutStop();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }

    }

    public String getsaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            Toast.makeText(mContext, rootDir, Toast.LENGTH_SHORT).show();

            return rootDir;
        } else {
            return null;
        }
    }

}
