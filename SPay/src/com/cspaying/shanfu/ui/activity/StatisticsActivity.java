package com.cspaying.shanfu.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.fragment.VpSimpleFragment;
import com.cspaying.shanfu.ui.view.BounceScrollView;
import com.cspaying.shanfu.ui.view.RotatImageView;
import com.cspaying.shanfu.ui.view.ViewPagerIndicator;

@SuppressLint("NewApi")
public class StatisticsActivity extends FragmentActivity {
	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private List<String> mDatas = Arrays.asList("本周", "上周", "本月", "上月",
			"区间");

	private ViewPagerIndicator mIndicator;
	private BounceScrollView mScrollView;
	private RotatImageView mRotatImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vp_indicator);
		MyApplication.getInstance().addActivity(this);
		initView();
		initDatas();
		//设置Tab上的标题
		mIndicator.setTabItemTitles(mDatas);
		mViewPager.setAdapter(mAdapter);
		//设置关联的ViewPager
		mIndicator.setViewPager(mViewPager,mScrollView,0);
		//设置关联的图片旋转，根据需要设置，效果不是很好
		mScrollView.setRotatImageView(mRotatImageView);
	}
	private void initDatas()
	{

		for (String data : mDatas)
		{
			VpSimpleFragment fragment = VpSimpleFragment.newInstance(data);
			mTabContents.add(fragment);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mTabContents.size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return mTabContents.get(position);
			}
		};
	}

	private void initView()
	{
		mScrollView = (BounceScrollView) findViewById(R.id.id_scrollview);
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
		mRotatImageView = (RotatImageView)findViewById(R.id.id_rotat_imageView);
		
	}

}
