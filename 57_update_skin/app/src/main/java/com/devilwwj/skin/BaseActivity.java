package com.devilwwj.skin;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

public class BaseActivity extends Activity implements ISkinUpdate, SkinPackageManager.loadSkinCallBack {

    protected static final String APK_NAME = "skin.apk";
    protected static final String DEX_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/skin.apk";
    //	private Button dayButton;
    private Button nightButton;
    private TextView textView;
    protected static boolean nightModel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);



//		dayButton = (Button) findViewById(R.id.btn_day);
//        nightButton = (Button) findViewById(R.id.btn_night);
//        textView = (TextView) findViewById(R.id.text);



    }


    public void update(){
        updateTheme();
    }









    @Override
    public void updateTheme() {

//			nightModel=true;
    }

    @Override
    public void startloadSkin() {

    }

    @Override
    public void loadSkinSuccess() {
        update();
    }

    @Override
    public void loadSkinFail() {

    }
}
