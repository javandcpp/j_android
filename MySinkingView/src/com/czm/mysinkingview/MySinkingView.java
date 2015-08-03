package com.czm.mysinkingview;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 水波浪球形进度View
 * 
 * @author caizhiming
 * 
 */
public class MySinkingView extends FrameLayout {
	private static final int DEFAULT_TEXTCOLOT = 0xFFFFFFFF;

	private static final int DEFAULT_TEXTSIZE = 250;

	private float mPercent;

	private Paint mPaint = new Paint();

	private Bitmap mBitmap;

	private Bitmap mScaledBitmap;

	private float mLeft;

	private int mSpeed = 15;

	private int mRepeatCount = 0;

	private Status mFlag = Status.NONE;

	private int mTextColor = DEFAULT_TEXTCOLOT;

	private int mTextSize = DEFAULT_TEXTSIZE;

	private ExecutorService threadPool;

	private float currentPercent;
	private String str;

	public MySinkingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		threadPool = Executors.newSingleThreadExecutor();
	}

	public void setTextColor(int color) {
		mTextColor = color;
	}

	public void setTextSize(int size) {
		mTextSize = size;
	}

	public void setPercent(final float percent) {

		mFlag = Status.RUNNING;
		mPercent = percent;
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (percent <= 1) {
					if (currentPercent > percent) {
						break;
					}
					currentPercent += 0.01f;
					postInvalidate();
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				// clear();
			}
		});

	}

	public void setStatus(Status status) {
		mFlag = status;
	}

	public void clear() {
		mFlag = Status.NONE;
		if (mScaledBitmap != null) {
			mScaledBitmap.recycle();
			mScaledBitmap = null;
		}

		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		int width = getWidth();
		int height = getHeight();

		// 裁剪成圆区域
		Path path = new Path();
		canvas.save();
		path.reset();
		canvas.clipPath(path);
		path.addCircle(width / 2, height / 2, width / 2, Direction.CCW);
		canvas.clipPath(path, Op.REPLACE);
		if (mFlag == Status.RUNNING) {
			if (mScaledBitmap == null) {
				mBitmap = BitmapFactory.decodeResource(getContext()
						.getResources(), R.drawable.wave2);
				mScaledBitmap = Bitmap.createScaledBitmap(mBitmap,
						mBitmap.getWidth(), getHeight(), false);
				mBitmap.recycle();
				mBitmap = null;
				mRepeatCount = (int) Math.ceil(getWidth()
						/ mScaledBitmap.getWidth() + 0.5) + 1;
			}
			for (int idx = 0; idx < mRepeatCount; idx++) {
				canvas.drawBitmap(mScaledBitmap, mLeft + (idx - 1)
						* mScaledBitmap.getWidth(), (1 - currentPercent)
						* getHeight(), null);
			}
		
			if (0 != mPercent) {
				str = (int) (currentPercent * 100) + "%";
			} else {
				str = 0 + "%";
			}
			mPaint.setColor(mTextColor);
			mPaint.setTextSize(mTextSize);
			mPaint.setStyle(Style.FILL);
			canvas.drawText(str, (getWidth() - mPaint.measureText(str)) / 2,
					getHeight() / 2 + mTextSize / 2, mPaint);

			mLeft += mSpeed;
			if (mLeft >= mScaledBitmap.getWidth())
				mLeft = 0;
			// 绘制外圆环
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(4);
			mPaint.setAntiAlias(true);
			mPaint.setColor(Color.rgb(33, 211, 39));
			canvas.drawCircle(width / 2, height / 2, width / 2 - 2, mPaint);

			postInvalidateDelayed(20);
		}
		canvas.restore();

	}

	public enum Status {
		RUNNING, NONE
	}

}
