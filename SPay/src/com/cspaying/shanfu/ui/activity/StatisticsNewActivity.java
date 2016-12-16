package com.cspaying.shanfu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.adapter.FragmentAdapter;
import com.cspaying.shanfu.ui.entit.Statistics1Entity;
import com.cspaying.shanfu.ui.entit.Statistics2Entity;
import com.cspaying.shanfu.ui.fragment.Statistics1Fragment;
import com.cspaying.shanfu.ui.fragment.Statistics2Fragment;
import com.cspaying.shanfu.ui.utils.CustomProgressDialog;

public class StatisticsNewActivity extends FragmentActivity implements
		OnClickListener, OnPageChangeListener {
	//TextView tvSta;// 统计
	//TextView tvDis;// 交易分布

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;
	private ViewPager mViewPager;
	private int currentIndex;

	private Statistics1Fragment mContactsFg1;
	private Statistics2Fragment mContactsFg2;
	
	private CustomProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		MyApplication.getInstance().addActivity(this);
		initView();
		init();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) this.findViewById(R.id.viewpager);
		mViewPager.addOnPageChangeListener(this);

//		tvSta = (TextView) findViewById(R.id.center_title);
//		tvDis = (TextView) findViewById(R.id.right_title);
//		tvSta.setOnClickListener(this);
//		tvDis.setOnClickListener(this);
	}

	private void init() {
		mContactsFg1 = new Statistics1Fragment(mHandler);
		mContactsFg2 = new Statistics2Fragment(mHandler);

		mFragmentList.add(mContactsFg1);
		mFragmentList.add(mContactsFg2);
		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mViewPager.setAdapter(mFragmentAdapter);
		mViewPager.setCurrentItem(0);
	}

	Handler mHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int what = msg.what;
			super.handleMessage(msg);
			switch (what) {
			case 1:
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.user_account_null), Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}

		}
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.center_title: {
			if(currentIndex == 0){
				currentIndex = 1;
			}else if(currentIndex == 1){
				currentIndex = 0;
			}
			mViewPager.setCurrentItem(currentIndex);
			//updateView();
			break;
		}
		case R.id.right_title: {
			if(currentIndex == 0){
				currentIndex = 1;
			}else if(currentIndex == 1){
				currentIndex = 0;
			}
			mViewPager.setCurrentItem(currentIndex);
			//updateView();
			break;
		}
		default: {
			break;
		}

		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float offset, int offsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		currentIndex = position;
		//updateView();
		// TODO Auto-generated method stub
	}

//	public void updateView() {
//
//		if (currentIndex == 0) {
//			tvSta.setText(R.string.tongji);
//			tvDis.setText(R.string.fenbu);
//
//		} else if (currentIndex == 1) {
//			tvSta.setText(R.string.fenbu);
//			tvDis.setText(R.string.tongji);
//		}
//	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				Log.d("paraseData", "----------------------------------");
				String jsonStr1 = (String) msg.obj;
				mContactsFg1.paraseData(jsonStr1);
				
				break;
			case 2:
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.load_data_error), Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				//mContactsFg0.setmList((ArrayList<Statistics1Entity>)msg.obj);
				mContactsFg1.refresh((ArrayList<Statistics1Entity>)msg.obj);
				//stopProgressDialog();
				break;
			case 4:
				Log.d("paraseData", "----------------------------------");
				String jsonStr2 = (String) msg.obj;
				mContactsFg2.paraseData(jsonStr2);
				break;
			case 5:
				//mContactsFg0.setmList((ArrayList<Statistics1Entity>)msg.obj);
				mContactsFg2.refresh((ArrayList<Statistics2Entity>)msg.obj);
				//stopProgressDialog();
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}

	};

	public Handler getmHandler() {
		return mHandler;
	}
	
	/**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(StatisticsNewActivity.this);
    	   progressDialog.setMessage(MyApplication.getContext()
    			   .getResources().getString(R.string.intalling));
    	}
    	progressDialog.show();
    }
    
    /**
     * 结束ProgressDialog()
     */
    private void stopProgressDialog(){
	  if (progressDialog != null && progressDialog.isShowing()){
    	progressDialog.dismiss();
    	progressDialog = null;
      }
    }
}
