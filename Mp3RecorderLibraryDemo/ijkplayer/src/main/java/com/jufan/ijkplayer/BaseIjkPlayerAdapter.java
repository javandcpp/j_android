package com.jufan.ijkplayer;

import android.content.Context;

import com.jufan.ijkplayer.inter.IjkPlayerInter;

/**
 * Created by android on 3/30/17.
 */

public abstract class BaseIjkPlayerAdapter extends AbstractPlayer implements IjkPlayerInter {
    public BaseIjkPlayerAdapter(Context context, Object surfaceView) {
        super(context, surfaceView);
    }

    public BaseIjkPlayerAdapter(Context context) {
        super(context);
    }


    public abstract void destroy();
}
