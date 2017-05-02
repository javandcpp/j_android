package com.guagua.mp3recorderlibrarydemo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.guagua.mp3recorder.MP3Recorder;
import com.guagua.mp3recorder.util.LameUtil;
import com.jufan.ijkplayer.AbstractPlayer;
import com.jufan.ijkplayer.view.IjkAudioPlayerDecorView;

import java.io.File;

//

public class MainActivity extends Activity implements LameUtil.LameWriteFinishCall, MP3Recorder.RecordDecibelListener {

    private MP3Recorder mRecorder;
    private AbstractPlayer ijkPlayWrapper;
    private IjkAudioPlayerDecorView ijkAudioPlayerDecorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final File destFile = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+".mp3");
//        if (!destFile.exists()) {
//            try {
//                destFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        mRecorder = new MP3Recorder(destFile,this);

        Button startButton = (Button) findViewById(R.id.StartButton);
        ijkAudioPlayerDecorView = (IjkAudioPlayerDecorView) findViewById(R.id.ijkDecorView);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mRecorder.start();
//                    Utils.cleanDirectory(getExternalCacheDir());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button stopButton = (Button) findViewById(R.id.StopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder.stop();
//                ijkAudioPlayerDecorView.getPlayer().stop();
            }
        });


        Button playButton = (Button) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    ijkPlayWrapper = new IjkPlayWrapper(MainActivity.this, new SurfaceView(MainActivity.this));
//                    ijkPlayWrapper.openVideo(destFile);
                    ijkAudioPlayerDecorView.getPlayer().openAudioWithURI(Uri.parse("http://link.hhtjim.com/sina/2850351.mp3"));
//                    ijkAudioPlayerDecorView.getPlayer().openAudioWithFile(destFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//

        LameUtil.setLameCallback(this);
        LameUtil.setDebug(true);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void lameWriteCallBack(boolean b) {
        Log.d("tag", b + "");
    }


    @Override
    public void decibelValueCallback(double v) {
        Log.d("tag", ((int) v)+"");
    }
}