package com.cspaying.shanfu.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cspaying.shanfu.R;

public class RotatImageView extends View {
	private Paint paint = null; // 画笔
	private Bitmap mbitmap = null; // 图片位图
	private Bitmap bitmapDisplay = null;
	private Matrix matrix = null;
	private int mWidth = 0; // 图片的宽度
	private int mHeight = 0; // 图片的高度
	private float fAngle = 180.0f; // 图片旋转
	private PaintFlagsDrawFilter mDrawFilter; 

	public RotatImageView(Context context) {
		super(context);
	}
	
	public RotatImageView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	/**
	 * 初始化一些自定义的参数
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RotatImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.RotatImageView, defStyle, 0);

		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			// 原始图片，在布局里面获取
			case R.styleable.RotatImageView_src:
				mbitmap = BitmapFactory.decodeResource(getResources(),a.getResourceId(attr, 0));
				bitmapDisplay = mbitmap;
				break;
			}
		}
		a.recycle();

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		matrix = new Matrix();
	}
	
	/**
	 * 计算控件的高度和宽度
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// 设置宽度
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		//match_parent或者设置的精确值获取
		//MeasureSpec.EXACTLY
		if (specMode == MeasureSpec.EXACTLY)
		{
			mWidth = specSize;
		} 
		else
		{
			// 由图片决定的宽
			//getPaddingLeft(),getPaddingRight()这两个值是控件属性的向内偏移的距离值，所以的一起计算
			//区别于layout_marginLeft,两个控件的左间距值设置
			if(null != mbitmap){
			int desireByImg = getPaddingLeft() + getPaddingRight()
					+ mbitmap.getWidth();
			
			// wrap_content
			if (specMode == MeasureSpec.AT_MOST)
			{
				//所以最小的值，宽度的话是左右内偏移距离之和
				mWidth = Math.min(desireByImg, specSize);
			} else

				mWidth = desireByImg;
			
			}
		}

		// 设置高度，部分解释同上
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		
		//match_parent或者设置的精确值获取
		//MeasureSpec.EXACTLY
		if (specMode == MeasureSpec.EXACTLY)
		{
			mHeight = specSize;
		} else
		{
			if(null != mbitmap){
			int desire = getPaddingTop() + getPaddingBottom()
					+ mbitmap.getHeight();

			// wrap_content
			if (specMode == MeasureSpec.AT_MOST)
			{
				mHeight = Math.min(desire, specSize);
			} else
				mHeight = desire;
			}
		}

		//计算好的宽度以及高度是值，设置进去
		setMeasuredDimension(mWidth, mHeight);
	}

	// 向左旋转
	public void setRotationLeft() {
		fAngle = fAngle - 20;
		setAngle();
	}

	// 向右旋转
	public void setRotationRight() {
		fAngle = fAngle + 20;
		setAngle();
	}
	
	private boolean isRoate = false;
	// 设置旋转比例
	private void setAngle() {
		Log.i("Show", String.valueOf(fAngle));
		isRoate = true;
		//设置图片的旋转中心，即绕（X,Y）这点进行中心旋转 要旋转的角度
		matrix.preRotate(fAngle, (float)mbitmap.getWidth()/2, (float)mbitmap.getHeight()/2); 
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//消除锯齿, 图片旋转后的锯齿消除不成功，实在不行图片边缘加一些白色像素点
		canvas.setDrawFilter(mDrawFilter); 
		if (isRoate) {
			canvas.drawBitmap(bitmapDisplay, matrix, paint);
			isRoate = false;
		}else {
			canvas.drawBitmap(bitmapDisplay, 0, 0, paint);	
		}
	}
}
