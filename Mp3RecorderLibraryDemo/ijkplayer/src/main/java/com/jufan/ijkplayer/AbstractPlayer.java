package com.jufan.ijkplayer;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.jufan.ijkplayer.inter.IActivityLiftCycle;

import java.io.File;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by android on 12/20/16.
 */

public abstract class AbstractPlayer<T> implements IActivityLiftCycle {

    protected IjkMediaPlayer ijkMediaPlayer;

    protected IjkPlayWrapper.DefaultListener defaultListener;
    protected IjkPlayWrapper.SurfaceCallBack callBack;
    protected int mCurrentState=STATE_IDLE;

    protected static final int STATE_ERROR = -1;
    protected static final int STATE_IDLE = 0;
    protected static final int STATE_PREPARING = 1;
    protected static final int STATE_PREPARED = 2;
    protected static final int STATE_PLAYING = 3;
    protected static final int STATE_PAUSED = 4;
    protected static final int STATE_PLAYBACK_COMPLETED = 5;

    protected final Context mContext;
    protected T mSurfaceView;
    protected SurfaceHolder mSurfaceHolder;

    public AbstractPlayer(Context context, T surfaceView){
        this.mContext = context;
        this.mSurfaceView = surfaceView;
        if (surfaceView instanceof SurfaceView) {
            this.mSurfaceHolder = ((SurfaceView)surfaceView).getHolder();
        }else if (surfaceView instanceof TextureView){

        }
    }

    public AbstractPlayer(Context context) {
        this.mContext=context;
    }

    public void openAudioWithFile(File file){};
    public void openAudioWithURI(Uri uri){};
    public void openVideo(File file){}
    public void openVideo(){}
    public abstract int getCurrentState();

}
