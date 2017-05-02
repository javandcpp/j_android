package com.wsine.west.exp5;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;

/**
 * Created by West on 2015/11/10.
 */
public class MusicService extends Service implements CacheListener {

    private String[] musicDir = new String[]{Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/仙剑奇侠传六-主题曲-《誓言成晖》.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/仙剑奇侠传六-主题曲-《剑客不能说》.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/仙剑奇侠传六-主题曲-《镜中人》.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/仙剑奇侠传六-主题曲-《浪花》.mp3"};

//    private String url = "http://www.ejoooo.com/audio/mp3/201611290520201435.mp3";
    private String url = "http://link.hhtjim.com/baidu/368589.mp3";
//    private String url = "http://link.hhtjim.com/qq/001faIUs4M2zna.mp3";
//    private String url = "http://link.hhtjim.com/xiami/1770409076.mp3";
//    private String url = "http://link.hhtjim.com/sina/2850351.mp3";
    private int musicIndex = 1;

    private Context mContext;
    private OnCacheListener onCacheListener;

    public final IBinder binder = new MyBinder();

    public interface OnCacheListener {
        void getCacheProgress(int progress);
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.d("tag",percentsAvailable+"------>"+cacheFile.getAbsolutePath());

        if (onCacheListener != null) {
            onCacheListener.getCacheProgress(percentsAvailable);
        }
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public static MediaPlayer mp = new MediaPlayer();

    public MusicService() {

    }

    public MusicService(Context context,OnCacheListener onCacheListener) {
        mContext = context;
        this.onCacheListener = onCacheListener;
        try {
            checkCachedState();

            HttpProxyCacheServer proxy = App.getProxy(mContext);
            proxy.registerCacheListener(this, url);
            String proxyUrl = proxy.getProxyUrl(url);

            mp.setDataSource(proxyUrl);
            //mp.setDataSource(Environment.getDataDirectory().getAbsolutePath()+"/You.mp3");
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            musicIndex = 1;
        } catch (Exception e) {
            Log.d("hint", "can't get to the song");
            e.printStackTrace();
        }
    }

    /**
     * 检查缓存的状态
     */
    private void checkCachedState() {
        HttpProxyCacheServer proxy = App.getProxy(mContext);
        boolean fullyCached = proxy.isCached(url);
        if (fullyCached && onCacheListener != null) {
            onCacheListener.getCacheProgress(100);
        }
    }

    public void playOrPause() {
        if (mp.isPlaying()) {
            mp.pause();
        } else {
            mp.start();
        }
    }

    public void stop() {
        if (mp != null) {
            mp.stop();
            try {
//                mp.prepareAsync();
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void nextMusic() {
        if (mp != null && musicIndex < 3) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicDir[musicIndex + 1]);
                musicIndex++;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        }
    }

    public void preMusic() {
        if (mp != null && musicIndex > 0) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicDir[musicIndex - 1]);
                musicIndex--;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        App.getProxy(getApplication()).unregisterCacheListener(this);
        super.onDestroy();
    }

    /**
     * onBind 是 Service 的虚方法，因此我们不得不实现它。
     * 返回 null，表示客服端不能建立到此服务的连接。
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
