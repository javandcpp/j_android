package com.guagua.mp3recorderlibrarydemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by android on 3/31/17.
 */

public class ThumbView extends LinearLayout {
    private Context mContent;
    private View contentView;
    private int lastX;
    private int lastY;
    private ImageView thumb;
    private SeekBar seekBar;
    private LinearLayout thumbParent;

    public ThumbView(Context context) {
        super(context);
        this.mContent=context;
        initView();
    }

    public ThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContent=context;
        initView();
    }

    public ThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContent=context;
        initView();
    }

    private void initView() {
        contentView = LayoutInflater.from(mContent).inflate(R.layout.thumb_layout,this,true);
        thumb = (ImageView) findViewById(R.id.thumb);
        seekBar = ((SeekBar) findViewById(R.id.seekBar));
        thumbParent = (LinearLayout) findViewById(R.id.thumbParent);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int thumeTrasn = thumbParent.getMeasuredWidth();
                int del=(seekBar.getMeasuredWidth()-thumeTrasn);
                int width = seekBar.getMeasuredWidth()-del;
                int delX=width/100*progress;
                float x = thumb.getX();
                Log.d("Tag","disX:"+delX+",thumb:"+x+",width:"+width+",progress:"+progress+",del:"+del);
                if (delX>=0&&delX<=seekBar.getMeasuredWidth()-del) {
                    ViewHelper.setTranslationX(thumb, delX);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offX = x - lastX;
                int offY = y - lastY;
//                offsetLeftAndRight(offX);

//                offsetTopAndBottom(offY);
                break;
        }
        return true;
    }

    public float getDescendantCoordRelativeToSelf(View descendant, int[] coord) {
        float scale = 1.0f;
        float[] pt = {coord[0], coord[1]};
        //坐标值进行当前窗口的矩阵映射，比如View进行了旋转之类，它的坐标系会发生改变。map之后，会把点转换为改变之前的坐标。
        descendant.getMatrix().mapPoints(pt);
        //转换为直接父窗口的坐标
        scale *= descendant.getScaleX();
        pt[0] += descendant.getLeft();
        pt[1] += descendant.getTop();
        ViewParent viewParent = descendant.getParent();
        //循环获得父窗口的父窗口，并且依次计算在每个父窗口中的坐标
        while (viewParent instanceof View && viewParent != this) {
            final View view = (View) viewParent;
            view.getMatrix().mapPoints(pt);
            scale *= view.getScaleX();//这个是计算X的缩放值。此处可以不管
            //转换为相当于可视区左上角的坐标，scrollX，scollY是去掉滚动的影响
            pt[0] += view.getLeft() - view.getScrollX();
            pt[1] += view.getTop() - view.getScrollY();
            viewParent = view.getParent();
        }
        coord[0] = (int) Math.round(pt[0]);
        coord[1] = (int) Math.round(pt[1]);
        return scale;
    }
}
