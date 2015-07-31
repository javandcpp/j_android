package com.wujay.fund.entity;

import java.util.ArrayList;

import com.nineoldandroids.animation.ObjectAnimator;
import com.wujay.fund.R;
import com.wujay.fund.common.Constants;
import com.wujay.fund.widget.GestureContentView;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class GesturePoint {
	/**
	 * ���x��ֵ
	 */
	private int leftX;
	/**
	 * �ұ�x��ֵ
	 */
	private int rightX;
	/**
	 * �ϱ�y��ֵ
	 */
	private int topY;
	/**
	 * �±�y��ֵ
	 */
	private int bottomY;
	/**
	 * ������Ӧ��ImageView�ؼ�
	 */
	public ImageView image;

	/**
	 * ����xֵ
	 */
	private int centerX;

	/**
	 * ����yֵ
	 */
	private int centerY;

	/**
	 * ״ֵ̬
	 */
	private int pointState;

	/**
	 * �������Point�����������֣���1��ʼ(ֱ�Ӹо���1��ʼ)
	 */
	private int num;

	private GestureContentView gestureContentView;

	public GesturePoint(int leftX, int rightX, int topY, int bottomY,
			ImageView image, int num) {
		super();
		this.leftX = leftX;
		this.rightX = rightX;
		this.topY = topY;
		this.bottomY = bottomY;
		this.image = image;

		this.centerX = (leftX + rightX) / 2;
		this.centerY = (topY + bottomY) / 2;

		this.num = num;
	}

	public int getLeftX() {
		return leftX;
	}

	public void setLeftX(int leftX) {
		this.leftX = leftX;
	}

	public int getRightX() {
		return rightX;
	}

	public void setRightX(int rightX) {
		this.rightX = rightX;
	}

	public int getTopY() {
		return topY;
	}

	public void setTopY(int topY) {
		this.topY = topY;
	}

	public int getBottomY() {
		return bottomY;
	}

	public void setBottomY(int bottomY) {
		this.bottomY = bottomY;
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public int getPointState() {
		return pointState;
	}

//	public GestureContentView getGestureContentView() {
//		return gestureContentView;
//	}
//
//	public void setGestureContentView(GestureContentView gestureContentView) {
//		this.gestureContentView = gestureContentView;
//	}

	public void setPointState(int state) {
		pointState = state;
		switch (state) {
		case Constants.POINT_STATE_NORMAL:
			this.image.setBackgroundResource(R.drawable.gesture_node_normal);
			System.out.println("normal");
			

			break;
		case Constants.POINT_STATE_SELECTED:
			
			final ScaleAnimation animation = new ScaleAnimation(1f, 1.3f,
					1f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(500);// ���ö�������ʱ��
			animation.setRepeatCount(0);
			animation.setFillAfter(true);// ����ִ������Ƿ�ͣ����ִ�����״̬
			this.image.setAnimation(animation);
			
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			animation.startNow();
			this.image.setBackgroundResource(R.drawable.gesture_node_pressed);

			break;
		case Constants.POINT_STATE_WRONG:
			this.image.setImageResource(R.drawable.gesture_node_wrong);

			break;
		default:
			break;
		}
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottomY;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + leftX;
		result = prime * result + rightX;
		result = prime * result + topY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GesturePoint other = (GesturePoint) obj;
		if (bottomY != other.bottomY)
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (leftX != other.leftX)
			return false;
		if (rightX != other.rightX)
			return false;
		if (topY != other.topY)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [leftX=" + leftX + ", rightX=" + rightX + ", topY="
				+ topY + ", bottomY=" + bottomY + "]";
	}
}
