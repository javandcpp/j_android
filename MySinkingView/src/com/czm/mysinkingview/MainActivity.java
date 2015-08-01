
package com.czm.mysinkingview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 测试用例页
 * 
 * @author caizhiming
 */
public class MainActivity extends Activity {
    private MySinkingView mSinkingView;

    private float percent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSinkingView = (MySinkingView) findViewById(R.id.sinking);

        findViewById(R.id.btn_test).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                test();
            }
        });

        
        mSinkingView.setPercent(0.00f);
//        mSinkingView.clear();
    }


    private void test() {
    	 mSinkingView.setPercent(0.8f);
//       
    }

}
