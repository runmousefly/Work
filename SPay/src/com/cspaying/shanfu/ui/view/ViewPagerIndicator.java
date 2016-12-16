package com.cspaying.shanfu.ui.view;


import java.util.List;



import com.cspaying.shanfu.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout
{
	//绘制三角形的画笔
	private Paint mPaint;
	 //path构成一个三角形
	private Path mPath;
	//三角形的宽度
	private int mTriangleWidth;
	// 三角形的高度
	private int mTriangleHeight;
	 //三角形的宽度为单个Tab的1/6
	private static final float RADIO_TRIANGEL = 1.0f / 6;
	// 三角形的最大宽度
	private final int DIMENSION_TRIANGEL_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGEL);
	//初始时，三角形指示器的偏移量
	private int mInitTranslationX;
	 // 手指滑动时的偏移量
	private float mTranslationX;
	 // tab数量
	private int mTabVisibleCount;
	 // tab上的内容
	private List<String> mTabTitles;
	 // 与之绑定的ViewPager
	public ViewPager mViewPager;
	 // 标题正常时的颜色
	private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
	 //标题选中时的颜色
	private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFFFFFFFF;
	
	public ViewPagerIndicator(Context context)
	{
		this(context, null);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		// 获得自定义属性，tab的数量
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ViewPagerIndicator);
		mTabVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_item_count,4);
		if (mTabVisibleCount < 0)
			mTabVisibleCount = 4;
		a.recycle();

		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ffffffff"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

	}

	//绘制指示器
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		canvas.save();
		// 画笔平移到正确的位置
		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
		canvas.drawPath(mPath, mPaint);
		canvas.restore();

		super.dispatchDraw(canvas);
	}

	 //初始化三角形的宽度
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGEL);// 1/6 of
																		// width
		mTriangleWidth = Math.min(DIMENSION_TRIANGEL_WIDTH, mTriangleWidth);

		// 初始化三角形
		initTriangle();

		// 初始时的偏移量
		mInitTranslationX = getScreenWidth() / mTabVisibleCount / 2 - mTriangleWidth / 2;
	}

	 //设置可见的tab的数量
	public void setVisibleTabCount(int count)
	{
		this.mTabVisibleCount = count;
	}

    //设置tab的标题内容 可选，生成textview加入布局，灵活处理
	public void setTabItemTitles(List<String> datas)
	{
		// 如果传入的list有值，则移除布局文件中设置的view
		if (datas != null && datas.size() > 0)
		{
			this.removeAllViews();
			this.mTabTitles = datas;

			for (String title : mTabTitles)
			{
				// 添加view
				addView(generateTextView(title));
			}
			// 设置item的click事件
			setItemClickEvent();
		}

	}
	
	//根据标题生成我们的TextView
	private TextView generateTextView(String text)
	{
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setLayoutParams(lp);
		return tv;
	}

	//对外的ViewPager的回调接口
	public interface PageChangeListener
	{
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels);

		public void onPageSelected(int position);

		public void onPageScrollStateChanged(int state);
	}

	// 对外的ViewPager的回调接口
	private PageChangeListener onPageChangeListener;

	// 对外的ViewPager的回调接口的设置
	public void setOnPageChangeListener(PageChangeListener pageChangeListener)
	{
		this.onPageChangeListener = pageChangeListener;
	}

	// 设置关联的ViewPager,以及传入 BounceScrollView，进行设置滚动
	public void setViewPager(ViewPager mViewPager, final BounceScrollView scrollView, int pos)
	{
		this.mViewPager = mViewPager;
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				// 设置字体颜色高亮
				resetTextViewColor();
				highLightTextView(position);

				// 回调
				if (onPageChangeListener != null)
				{
					onPageChangeListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{
				// 滚动
				scroll(scrollView,position, positionOffset);
				// 回调
				if (onPageChangeListener != null)
				{
					onPageChangeListener.onPageScrolled(position,
							positionOffset, positionOffsetPixels);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
				// 回调
				if (onPageChangeListener != null)
				{
					onPageChangeListener.onPageScrollStateChanged(state);
				}

			}
		});
		// 设置当前页
		mViewPager.setCurrentItem(pos);
		// 高亮
		highLightTextView(pos);
	}

	//高亮文本
	protected void highLightTextView(int position)
	{
		View view = getChildAt(position);
		if (view instanceof TextView)
		{
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
		}

	}

	//重置文本颜色
	private void resetTextViewColor()
	{
		for (int i = 0; i < getChildCount(); i++)
		{
			View view = getChildAt(i);
			if (view instanceof TextView)
			{
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}

	// 设置点击事件
	public void setItemClickEvent()
	{
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++)
		{
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

	// 初始化三角形指示器
	private void initTriangle()
	{
		mPath = new Path();

		mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
		mPath.close();
	}
	
	//指示器跟随手指滚动，以及容器滚动
	public void scroll(BounceScrollView scrollView,int position, float offset)
	{
		// 不断改变偏移量，invalidate
		mTranslationX = getScreenWidth() / mTabVisibleCount * (position + offset);

		int tabWidth = getScreenWidth() / mTabVisibleCount;

		// 容器滚动，当移动到倒数最后一个的时候，开始滚动
		if (offset > 0 && position >= (mTabVisibleCount - 1) && getChildCount() > mTabVisibleCount)
		{
			
			if (mTabVisibleCount != 1)
			{
				//下面注释掉，是滚动ViewPagerIndicator这个LinearLayout
//				this.scrollTo((position - (mTabVisibleCount - 1)) * tabWidth + (int) (tabWidth * offset), 0);
				//只滚动滚动条，禁止滚动lineayout
				scrollView.setScrolledTo((position - (mTabVisibleCount - 1)) * tabWidth + (int) (tabWidth * offset), 0);
			} else
			// 为count为1时 的特殊处理
			{
				this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
//				scrollView.setScrolledTo(position * tabWidth + (int) (tabWidth * offset), 0);
			}
		}
		//处理特殊情况
		else if (offset > 0 && position <= mTabVisibleCount - 1) {
			scrollView.setScrolledTo(0, 0);
		}

		invalidate();
	}

	//设置布局中view的一些必要属性；如果设置了setTabTitles，布局中view则无效
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();

		int cCount = getChildCount();

		if (cCount == 0)
			return;

		for (int i = 0; i < cCount; i++)
		{
			View view = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) view
					.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		// 设置点击事件
		setItemClickEvent();

	}

	//获得屏幕的宽度
	public int getScreenWidth()
	{
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		//获取是整个屏幕的宽度，我试过自定义宽度测量宽度，不行。因为本身还没内容，是后面添加的。所以需要后面加东西
		//需要这里减去宽度就行。其中这里减去120就是60dp的宽度
		return outMetrics.widthPixels -100;
	}

}
