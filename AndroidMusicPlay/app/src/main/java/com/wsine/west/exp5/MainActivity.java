package com.wsine.west.exp5;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MusicService musicService;
    private SeekBar seekBar;
    private TextView musicStatus, musicTime;
    private Button btnPlayOrPause, btnStop, btnQuit, btn_clear;
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }

    public class Mp3ProgressHandler extends Handler {
        public void start() {
            sendEmptyMessage(0);
        }

        public void stop() {
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateMp3Progress();
            sendEmptyMessageDelayed(0, 500);
        }
    }

    public Mp3ProgressHandler handler = new Mp3ProgressHandler();

    private void updateMp3Progress() {
        if (musicService.mp.isPlaying()) {
            musicStatus.setText(getResources().getString(R.string.playing));
            btnPlayOrPause.setText(getResources().getString(R.string.pause).toUpperCase());
        } else {
            musicStatus.setText(getResources().getString(R.string.pause));
            btnPlayOrPause.setText(getResources().getString(R.string.play).toUpperCase());
        }
        musicTime.setText(time.format(musicService.mp.getCurrentPosition()) + "/"
                + time.format(musicService.mp.getDuration()));
        int currentPosition = musicService.mp.getCurrentPosition();
        int duration = musicService.mp.getDuration();
        Log.d("MainActivity", "currentPosition: " + currentPosition + "____duration:" + duration + "__getSeekBarMax:" + seekBar.getMax());
        seekBar.setMax(musicService.mp.getDuration());
        seekBar.setProgress(musicService.mp.getCurrentPosition());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) this.findViewById(R.id.MusicSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicService.mp.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Log.d("hint", "ready to new MusicService");
        musicService = new MusicService(this, new MusicService.OnCacheListener() {
            @Override
            public void getCacheProgress(int progress) {
                seekBar.setSecondaryProgress(progress * musicService.mp.getDuration() / 100);
                Log.d("MainActivitySecond", "getCacheProgress: " + progress);
            }
        });
        Log.d("hint", "finish to new MusicService");
        bindServiceConnection();

        musicStatus = (TextView) this.findViewById(R.id.MusicStatus);
        musicTime = (TextView) this.findViewById(R.id.MusicTime);

        btnPlayOrPause = (Button) this.findViewById(R.id.BtnPlayorPause);
        btn_clear = (Button) this.findViewById(R.id.btn_clear);

        Log.d("hint", Environment.getExternalStorageDirectory().getAbsolutePath() + "/You.mp3");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (musicService.mp.isPlaying()) {
            musicService.playOrPause();
        }
    }

    @Override
    protected void onResume() {
        if (musicService.mp.isPlaying()) {
            musicStatus.setText(getResources().getString(R.string.playing));
        } else {
            musicStatus.setText(getResources().getString(R.string.pause));
        }
        seekBar.setProgress(musicService.mp.getCurrentPosition());
        seekBar.setMax(musicService.mp.getDuration());
        handler.start();
        super.onResume();
        Log.d("hint", "handler post runnable");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BtnPlayorPause:
                musicService.playOrPause();
                if (musicService.mp.isPlaying()) {
                    handler.start();
                } else {
                    handler.stop();
                }

                break;
            case R.id.BtnStop:
                musicService.stop();
                seekBar.setProgress(0);
                break;
            case R.id.BtnQuit:
                handler.stop();
                unbindService(sc);
                try {
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnPre:
                musicService.preMusic();
                break;
            case R.id.btnNext:
                musicService.nextMusic();
                break;
            case R.id.btn_clear:
                try {
//                    Utils.cleanDirectory(getExternalCacheDir());
                    Toast.makeText(this, getExternalCacheDir().getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
