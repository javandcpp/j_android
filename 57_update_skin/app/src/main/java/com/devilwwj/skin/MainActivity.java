package com.devilwwj.skin;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 功能：切换皮肤
 * @author devilwwj
 *
 */
public class MainActivity extends BaseActivity{
	private static final String APK_NAME = "skin.apk";
	private static final String DEX_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/skin.apk";
//	private Button dayButton;
	private Button nightButton;
	private TextView textView;
//	private boolean nightModel = false;
	private TextView txView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);

		txView = (TextView) findViewById(R.id.tvText);


	}

	public void onClick(View view){
		Intent intent =new Intent (this,ModeActivity.class);
		startActivity(intent);
	}

	    @Override
    protected void onResume() {
        super.onResume();
        if (SkinPackageManager.getInstance(this).mResources != null) {
            updateTheme();
        }
    }

	@Override
	public void updateTheme() {
//		super.updateTheme();

		Log.e("MainActivity","update");

		Resources mResource = SkinPackageManager.getInstance(this).mResources;
		if (nightModel) {
			// 如果是黑夜的模式，则加载黑夜的主题
			int id1 = mResource.getIdentifier("night_btn_color", "color",
					"com.devilwwj.res");
			txView.setBackgroundColor(mResource.getColor(id1));
			int id2 = mResource.getIdentifier("night_background", "color",
					"com.devilwwj.res");
//			txView.setTextColor(mResource.getColor(id2));
			txView.setTextColor(mResource.getColor(id2));

			txView.setText("night");
//			nightModel=false;

		} else {
			// 如果是白天模式，则加载白天的主题
			int id1 = mResource.getIdentifier("day_btn_color", "color",
					"com.devilwwj.res");
			txView.setBackgroundColor(mResource.getColor(id1));
			int id2 = mResource.getIdentifier("day_background", "color",
					"com.devilwwj.res");
			txView.setTextColor(mResource.getColor(id2));
//			textView.setTextColor(mResource.getColor(id2));
			txView.setText("day");
		}
	}
}
