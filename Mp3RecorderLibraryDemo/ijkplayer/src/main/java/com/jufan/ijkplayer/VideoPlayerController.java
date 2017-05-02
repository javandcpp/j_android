package com.jufan.ijkplayer;

import android.content.Context;

import com.jufan.ijkplayer.inter.IActivityLiftCycle;

import java.io.IOException;


/**
 * Created by android on 12/20/16.
 */

public class VideoPlayerController<T> implements IActivityLiftCycle {


    private final AbstractPlayer mPlayer;

    public VideoPlayerController(Context context, T surfaceView) {
        mPlayer = new IjkPlayWrapper(context, surfaceView);
    }

    public void play() throws IOException {
        mPlayer.openVideo();
    }

    public int getCurrentState(){
        return mPlayer.getCurrentState();
    }

    @Override
    public void onActivityCreate() {
        if (null != mPlayer)
            mPlayer.onActivityCreate();
    }

    @Override
    public void onActivityStart() {
        if (null != mPlayer)
            mPlayer.onActivityStart();
    }

    @Override
    public void onActivityResume() {
        if (null != mPlayer)
            mPlayer.onActivityResume();
    }

    @Override
    public void onActivityPause() {
        if (null != mPlayer)
            mPlayer.onActivityPause();
    }

    @Override
    public void onActivityDestroy() {
        if (null != mPlayer)
            mPlayer.onActivityDestroy();
    }


}
