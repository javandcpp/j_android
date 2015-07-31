package com.wujay.fund.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class MyImageView extends ImageView {
	private Context mContext;
	private int id;
	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext=context;
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext=context;
	}

	public MyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext=context;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = windowManager.getDefaultDisplay();
		
		int width = defaultDisplay.getWidth();
		int height = defaultDisplay.getHeight();
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
		int bitmapW = bitmap.getWidth();
		int bitmapH = bitmap.getHeight();
		canvas.drawBitmap(bitmap, (width-bitmapW)/2, (height-bitmapH)/2, null);
	}

}
