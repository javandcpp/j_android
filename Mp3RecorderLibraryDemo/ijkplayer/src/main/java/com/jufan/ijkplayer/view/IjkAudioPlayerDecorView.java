package com.jufan.ijkplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jufan.ijkplayer.BaseIjkPlayerAdapter;
import com.jufan.ijkplayer.BasicIjkPlayer;
import com.jufan.ijkplayer.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by android on 3/30/17.
 */

public class IjkAudioPlayerDecorView extends FrameLayout {
    public static final String TAG = "Ijk";
    private Context mContext;
    private BaseIjkPlayerAdapter ijkPlayer;
    private View mContentView;
    private Button btnPlay;
    private TextView tvTime;
    private SeekBar seekBar;
    private long mPrepareEndTime;
    private int mSeekWhenPrepared;
    private long mSeekEndTime;

    public IjkAudioPlayerDecorView(Context context, BaseIjkPlayerAdapter abstractPlayer) {
        super(context);
        this.mContext = context;
        this.ijkPlayer = abstractPlayer;
        initView();
    }

    public IjkAudioPlayerDecorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IjkAudioPlayerDecorView);
//        String className = typedArray.getString(R.styleable.IjkAudioPlayerDecorView_player_class);
        newObject();
//        typedArray.recycle();
        initView();
    }

    public IjkAudioPlayerDecorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IjkAudioPlayerDecorView);
//        String className = typedArray.getString(R.styleable.IjkAudioPlayerDecorView_player_class);
        newObject();
        initView();
    }

    private void newObject() {
        this.ijkPlayer= BasicIjkPlayer.getInstance(mContext);
//        try {
//            Class<?> aClass = Class.forName(className);
//
//            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(Context.class);
//            Object o = declaredConstructor.newInstance(mContext);
//            this.ijkPlayer = ((BaseIjkPlayerAdapter) o);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void initView() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.voice_play_item, this, true);
        btnPlay = ((Button) findViewById(R.id.play));
        tvTime = (TextView) findViewById(R.id.tvTime);
        seekBar = ((SeekBar) findViewById(R.id.seekBar));
        IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
                new IMediaPlayer.OnVideoSizeChangedListener() {
                    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                        requestLayout();
                    }
                };

        IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer mp) {
                mPrepareEndTime = System.currentTimeMillis();
                int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
                if (seekToPosition != 0) {
                    ijkPlayer.seekTo(seekToPosition);
                }
                ijkPlayer.start();
            }
        };

        IMediaPlayer.OnCompletionListener mCompletionListener =
                new IMediaPlayer.OnCompletionListener() {
                    public void onCompletion(IMediaPlayer mp) {

                    }
                };

        IMediaPlayer.OnInfoListener mInfoListener =
                new IMediaPlayer.OnInfoListener() {
                    public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                        switch (arg1) {
                            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                                Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                                Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                                Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                                Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                                Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                                break;
                            case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                                Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                                Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                                Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                                Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                                Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                                break;
                            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                                Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                                break;
                            case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                                Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                                break;
                        }
                        return true;
                    }
                };

        IMediaPlayer.OnErrorListener mErrorListener =
                new IMediaPlayer.OnErrorListener() {
                    public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                        Log.d(TAG, "Error: " + framework_err + "," + impl_err);
                                return true;
                    }
                };
        IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
                new IMediaPlayer.OnBufferingUpdateListener() {
                    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                        Log.d(TAG, "buffer: ");
                    }
                };
        IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {

            @Override
            public void onSeekComplete(IMediaPlayer mp) {
                mSeekEndTime = System.currentTimeMillis();
            }
        };

        IMediaPlayer.OnTimedTextListener mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(IMediaPlayer mp, IjkTimedText text) {
                if (text != null) {
                }
            }
        };

        ijkPlayer.setOnPreparedListener(mPreparedListener);
        ijkPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
        ijkPlayer.setOnInfoListener(mInfoListener);
        ijkPlayer.setOnCompletionListener(mCompletionListener);
        ijkPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
    }

    public BaseIjkPlayerAdapter getPlayer() {
        return ijkPlayer;
    }

}
