package com.jufan.ijkplayer.inter;

/**
 * Created by android on 12/20/16.
 */

public interface IActivityLiftCycle {

    void onActivityCreate();
    void onActivityStart();
    void onActivityResume();
    void onActivityPause();
    void onActivityDestroy();
}
