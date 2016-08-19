package com.devilwwj.skin;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ModeActivity extends BaseActivity implements View.OnClickListener {

    private Button nightButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nightButton = (Button) findViewById(R.id.btn_night);
        textView = (TextView) findViewById(R.id.text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_night:
                nightModel = !nightModel;
                update();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SkinPackageManager.getInstance(this).mResources != null) {
            updateTheme();
        }
    }

    /**
     * 加载皮肤
     */
    private void loadSkin() {
//        SkinPackageManager.getInstance(this).loadSkinAsync(DEX_PATH,
//                new SkinPackageManager.loadSkinCallBack() {
//
//                    @Override
//                    public void startloadSkin() {
//                        Log.d("xiaowu", "startloadSkin");
//                    }
//
//                    @Override
//                    public void loadSkinSuccess() {
//                        Log.d("xiaowu", "loadSkinSuccess");
//                        // 然后这里更新主题
//                        update();
//                    }
//
//                    @Override
//                    public void loadSkinFail() {
//                        Log.d("xiaowu", "loadSkinFail");
//                    }
//                });
    }

    @Override
    public void updateTheme() {
        Log.e("ModeActivity","update");
        Resources mResource = SkinPackageManager.getInstance(this).mResources;
        if (nightModel) {
            // 如果是黑夜的模式，则加载黑夜的主题
            int id1 = mResource.getIdentifier("night_btn_color", "color",
                    "com.devilwwj.res");
            textView.setBackgroundColor(mResource.getColor(id1));
            int id2 = mResource.getIdentifier("night_background", "color",
                    "com.devilwwj.res");
//			txView.setTextColor(mResource.getColor(id2));
            textView.setTextColor(mResource.getColor(id2));

            textView.setText("night");
//			nightModel=false;

        } else {
            // 如果是白天模式，则加载白天的主题
            int id1 = mResource.getIdentifier("day_btn_color", "color",
                    "com.devilwwj.res");
            textView.setBackgroundColor(mResource.getColor(id1));
            int id2 = mResource.getIdentifier("day_background", "color",
                    "com.devilwwj.res");
            textView.setTextColor(mResource.getColor(id2));
//			textView.setTextColor(mResource.getColor(id2));
            textView.setText("day");
        }

    }


}
