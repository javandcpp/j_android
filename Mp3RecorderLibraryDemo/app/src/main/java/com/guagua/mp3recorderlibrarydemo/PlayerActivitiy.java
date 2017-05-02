package com.guagua.mp3recorderlibrarydemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;




public class PlayerActivitiy extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private Button play;
    private Button pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_activitiy);

        initView();

    }

    private void initView() {
        seekBar = ((SeekBar) findViewById(R.id.seekBar));
        play = ((Button) findViewById(R.id.play));
        pause = ((Button) findViewById(R.id.pause));

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pause:

                break;
            case R.id.play:

                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("SEEK",seekBar.getProgress()+"");
    }
}
