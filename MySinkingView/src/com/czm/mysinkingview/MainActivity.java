
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

        percent = 0.56f;
        mSinkingView.setPercent(percent);
    }


    private void test() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                percent = 0;
                while (percent <= 1) {
                    mSinkingView.setPercent(percent);
                    percent += 0.01f;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                percent = 0.56f;
                mSinkingView.setPercent(percent);
                // mSinkingView.clear();
            }
        });
        thread.start();
    }

}
