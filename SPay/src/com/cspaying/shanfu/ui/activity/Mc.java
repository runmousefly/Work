package com.cspaying.shanfu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.WindowManager;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.adapter.FragmentAdapter;
import com.cspaying.shanfu.ui.fragment.McFragment;

@SuppressLint("NewApi")
public class Mc extends FragmentActivity {

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;

	private ViewPager mPageVp;
	/**
	 * Tab显示内容TextView
	 */
	//private TextView mTabThisWeek, mTabLastWeek, mTabThisMonth, mTabLastMonth,
	//		mTabSection;
	/**
	 * Tab的那个引导线
	 */
	//private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	
	private McFragment mContactsFg0;
	private McFragment mContactsFg1;
//	private FragmentStatistics mContactsFg2;
//	private FragmentStatistics mContactsFg3;
//	private FragmentStatistics mContactsFg4;
	
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		findById();
		init();
		//initTabLineWidth();

	}

	private void findById() {
		/*mTabThisWeek = (TextView) this.findViewById(R.id.id_thisWeek);
		mTabLastWeek = (TextView) this.findViewById(R.id.id_lastWeek);
		mTabThisMonth = (TextView) this.findViewById(R.id.id_thisMonth);
		mTabLastMonth = (TextView) this.findViewById(R.id.id_lastMonth);
		mTabSection = (TextView) this.findViewById(R.id.id_section);*/

		//mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);

		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
	}

	@SuppressWarnings("deprecation")
	private void init() {
		mContactsFg0 = new McFragment();
		mContactsFg1 = new McFragment();
//		mContactsFg2 = new FragmentStatistics();
//		mContactsFg3 = new FragmentStatistics();
//		mContactsFg4 = new FragmentStatistics();
		
		
		mFragmentList.add(mContactsFg0);
		mFragmentList.add(mContactsFg1);
		//mFragmentList.add(mContactsFg2);
		//mFragmentList.add(mContactsFg3);
		//mFragmentList.add(mContactsFg4);
		
		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(0);

		mPageVp.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				/*LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				Log.e("offset:", offset + "");
				*//**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1;
				 * 1->0
				 *//*

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
							* (screenWidth / 5));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 5) + currentIndex
							* (screenWidth / 5));

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
							* (screenWidth / 5));

				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 5) + currentIndex
							* (screenWidth / 5));

				} else if (currentIndex == 3 && position == 3) {// 2->3
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
							* (screenWidth / 5));
				} else if (currentIndex == 3 && position == 2) // 3->2
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 2 / 5) + currentIndex
							* (screenWidth / 5));

				} else if (currentIndex == 4 && position == 4) {// 3->4
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
							* (screenWidth / 5));
				} else if (currentIndex == 4 && position == 3) // 4->3
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 5) + currentIndex
							* (screenWidth / 5));

				}

				mTabLineIv.setLayoutParams(lp);*/
			}

			@Override
			public void onPageSelected(int position) {
				//resetTextView();
				/*switch (position) {
				case 0:
					mTabThisWeek.setTextColor(Color.BLUE);
					break;
				case 1:
					mTabLastWeek.setTextColor(Color.BLUE);
					break;
				case 2:
					mTabThisMonth.setTextColor(Color.BLUE);
					break;
				case 3:
					mTabLastMonth.setTextColor(Color.BLUE);
					break;
				case 4:
					mTabSection.setTextColor(Color.BLUE);
					break;
				}*/
				currentIndex = position;
			}
		});

	}

	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 *//*
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 5;
		mTabLineIv.setLayoutParams(lp);
	}*/

	/**
	 * 重置颜色
	 *//*
	private void resetTextView() {
		mTabThisWeek.setTextColor(Color.BLACK);
		mTabLastWeek.setTextColor(Color.BLACK);
		mTabThisMonth.setTextColor(Color.BLACK);
		mTabLastMonth.setTextColor(Color.BLACK);
		mTabSection.setTextColor(Color.BLACK);
	}*/

}
