package com.cspaying.shanfu.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

/**
 * ScrollView反弹效果的实现
 */
public class BounceScrollView extends HorizontalScrollView {
	// 孩子View
	private View inner;
    // 点击时x坐标
	private float x;
	// 矩形(这里只是个形式，只是用于判断是否需要动画
	private Rect normal = new Rect();
    // 是否开始计算
	private boolean isCount = false;
	private RotatImageView  mRotatImageView;
	
	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}
	
	//手动需要设置滚动位置
	public void setScrolledTo(int position, float positionOffset) {
		this.scrollTo(position,(int) positionOffset);
	}

	 //监听touch
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}

		return super.onTouchEvent(ev);
	}

	//触摸事件
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			// 手指松开.
			if (isNeedAnimation()) {
				animation();
				isCount = false;
			}
			break;
		/***
		 * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
		 * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
		 * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
		 */
		case MotionEvent.ACTION_MOVE:
			final float preX = x;// 按下时的y坐标
			float nowX = ev.getX();// 时时y坐标
			int deltaX = (int) (preX - nowX);// 滑动距离
			if (!isCount) {
				deltaX = 0; // 在这里要归0.
			}

			x = nowX;
			// 当滚动到最上或者最下时就不会再滚动，这时移动布局
			if (isNeedMove()) {
				// 初始化头部矩形
				if (normal.isEmpty()) {
					// 保存正常的布局位置
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
				}
				// 移动布局
				inner.layout(inner.getLeft() - deltaX / 4, inner.getTop(),
						inner.getRight()  - deltaX / 4, inner.getBottom());
				
				//图片加号旋转，如果不需要这个直接删了就行
				if (mRotatImageView != null) {
					mRotatImageView.setRotationLeft();
				}
			}
			isCount = true;
			break;

		default:
			break;
		}
	}

	//回缩动画
	public void animation() {
		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(200);
		inner.startAnimation(ta);
		// 设置回到正常的布局位置
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();

	}
	
	//设置图片加号旋转
	public void setRotatImageView(RotatImageView rotatImageView){
		this.mRotatImageView = rotatImageView;
	}

	// 是否需要开启动画
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	/***
	 * 是否需要移动布局 inner.getMeasuredHeight():获取的是控件的总高度
	 * getHeight()：获取的是屏幕的高度
	 */
	public boolean isNeedMove() {
		int offset = inner.getMeasuredWidth() - getWidth();
		int scrollX = getScrollX();
		
		// 0是顶部反弹
		//是底部反弹加上    
		if (scrollX == 0  || scrollX == offset) {
			return true;
		}
		return false;
	}

}
