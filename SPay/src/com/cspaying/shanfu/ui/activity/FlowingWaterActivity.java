package com.cspaying.shanfu.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.adapter.FlowingWaterFragmentAdapter;
import com.cspaying.shanfu.ui.fragment.FragmentStatistics;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.utils.PxUtil;
import com.cspaying.shanfu.ui.utils.TitlePopup;
import com.cspaying.shanfu.ui.utils.TitlePopup.OnItemOnClickListener;

@SuppressLint("NewApi")
public class FlowingWaterActivity extends FragmentActivity implements
		OnClickListener {
	private TextView tvFlowingWater;
	private TextView tvQuery;
	private TextView have_finish;
	private TextView had_pay;
	private TextView title_tv;
	private int mYear;
	private int mMonth;
	private int mDay;

	private Intent intent;

	private FragmentStatistics mContactsFg;
	private FragmentStatistics mContactsFg1;

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private FlowingWaterFragmentAdapter mFragmentAdapter;

	private ViewPager mPageVp;
	/**
	 * Tab显示内容TextView
	 */
	private TextView mdingdan, mtuikuan;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;

	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	private TitlePopup titlePopup;

	private String DateString;
	private String payState;
	private int payStateNum = 2;
	private Calendar mCalendar = Calendar.getInstance();
	private Map<String, String> mapState = new HashMap<String, String>();
    
	private float payValue =0;
	private int paynum =0;
	private float tuiValue = 0;
	private int tuinum = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_auth);
		MyApplication.getInstance().addActivity(this);
		initView();
		init();
		initTabLineWidth();
		initPopView();
	}

	private void initView() {

		mapState.put("全部", "00");
		mapState.put("未支付", "01");
		mapState.put("已支付", "02");
		mapState.put("已冲正", "03");
		mapState.put("已关闭", "04");
		mapState.put("转入退款", "05");
		mapState.put("支付失败", "09");
		mapState.put("订单超时", "10");

		// TODO Auto-generated method stub
		tvFlowingWater = (TextView) findViewById(R.id.tv_todayFlowingWater);
		tvFlowingWater.setOnClickListener(this);
		tvQuery = (TextView) findViewById(R.id.tv_query);
		tvQuery.setOnClickListener(this);

		title_tv = (TextView) findViewById(R.id.title_tv);
		have_finish = (TextView) findViewById(R.id.have_finish);

		had_pay = (TextView) findViewById(R.id.had_pay);
		had_pay.setOnClickListener(this);

		mdingdan = (TextView) this.findViewById(R.id.id_thisWeek);
		mdingdan.setOnClickListener(this);

		mtuikuan = (TextView) this.findViewById(R.id.id_thisMonth);
		mtuikuan.setOnClickListener(this);

		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);

		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

	}

	public void initTableText(int position) {
		if (position == 0) {
			tvFlowingWater.setText(DateString);
			if (payValue ==0) {
				title_tv.setText("￥" + 0+".00");
			}else {
				title_tv.setText("￥" + payValue);
			}
			
			have_finish.setText("已支付" + paynum + "笔");
			
			have_finish.setVisibility(View.VISIBLE);
			had_pay.setText("已支付");
			had_pay.setVisibility(View.VISIBLE);
		} else if (position == 1) {
			tvFlowingWater.setText(DateString);
			if (tuiValue == 0) {
				title_tv.setText("￥" + "0.00");
			}else {
				title_tv.setText("￥" + tuiValue);
			}
			
			have_finish.setText("已退款" + tuinum + "笔");
			have_finish.setVisibility(View.VISIBLE);
			had_pay.setText(payState);
			had_pay.setVisibility(View.GONE);
		}

	}

	public void updateTitle(float payValue, int num) {
		
		if (currentIndex == 0) {
			this.paynum = num;
			this.payValue = payValue;
			title_tv.setText("￥" + payValue);
			have_finish.setText("已支付" + num + "笔");
		} else if (currentIndex == 1) {
			this.tuinum = num;
			this.tuiValue= payValue;
			title_tv.setText("￥" + payValue);
			have_finish.setText("已退款" + num + "笔");
		}
	}

	/**
	 * 更新订单查询日期
	 */
	private void updateDisplay() {
		String monstr = null;
		String daystr = null;
		Log.e("mMonth", mMonth+","+mDay);
		if (mMonth < 9) {
			monstr = "0" + (mMonth + 1);
		} else {
			monstr = "" + (mMonth + 1);
		}
		if (mDay < 10) {
			daystr = "0" + mDay;
		} else {
			daystr = "" + mDay;
		}
		DateString = new StringBuilder().append(mYear).append("-")
				.append(monstr).append("-").append(daystr).append("")
				.toString();
		tvFlowingWater.setText(DateString);
	}

	/**
	 * 日期选择返回接口
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			Message message = new Message();
			message.what = 1;
			UIhandle.sendMessage(message);
		}
	};

	private void init() {
		Date now = new Date();
		SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyyMMdd");
		DateString = outDateFormat.format(now);
		payState = getResources().getString(R.string.payStateAll);
		mContactsFg = new FragmentStatistics(0, this);
		mContactsFg1 = new FragmentStatistics(1, this);
		mFragmentList.add(mContactsFg);
		mFragmentList.add(mContactsFg1);
		mFragmentAdapter = new FlowingWaterFragmentAdapter(
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
				scrollSelectTab(offset, position);
			}

			@Override
			public void onPageSelected(int position) {
				// resetTextView();
				currentIndex = position;
				initTableText(position);
			}
		});

	}

	public void scrollSelectTab(float offset, int position) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();

		Log.e("offset:", offset + "");

		/**
		 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来 设置mTabLineIv的左边距
		 * 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1; 1->0
		 */

		if (currentIndex == 0 && position == 0)// 0->1
		{
			lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
					* (screenWidth / 2));

		} else if (currentIndex == 1 && position == 0) // 1->0
		{
			lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 2) + currentIndex
					* (screenWidth / 2));

		} else if (currentIndex == 1 && position == 1) // 1->2
		{
			lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
					* (screenWidth / 2));

		} /*
		 * else if (currentIndex == 2 && position == 1) // 2->1 { lp.leftMargin
		 * = (int) (-(1 - offset) (screenWidth * 1.0 / 2) + currentIndex
		 * (screenWidth / 2));
		 * 
		 * }
		 */

		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 设置滑动条的宽度为屏幕的1/2(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels; 
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 2;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 加载支付状态POPview
	 */
	public void initPopView() {
		titlePopup = new TitlePopup(this, PxUtil.dip2px(this, 100),
				PxUtil.dip2px(this, 200));
		ArrayList<String> items = new ArrayList<String>();
		items.add("全部");
		items.add("未支付");
		items.add("已支付");
		items.add("已冲正");
		items.add("已关闭");
		items.add("转入退款");
		items.add("支付失败");
		items.add("订单超时");
		titlePopup.addAction(items);
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {

			@Override
			public void onItemClick(String item, int position) {
				// TODO Auto-generated method stub
				payState = item;
				payStateNum = position;

				UIhandle.sendEmptyMessage(2);
				titlePopup.dismiss();
				// Log.e("++++titlePopup:",
				// "item:"+item+","+"position:"+position);
			}
		});

	}

	/**
	 * 支付状态POPview
	 * 
	 * @param view
	 */
	public void showPopView(View view) {
		titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
		titlePopup.show(view);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_todayFlowingWater:
			new DatePickerDialog(FlowingWaterActivity.this, mDateSetListener,
					mYear, mMonth, mDay).show();
			break;
		case R.id.tv_query:
			intent = new Intent(this, QueryOrderActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.id_thisWeek:
			mPageVp.setCurrentItem(0);
			break;
		case R.id.id_thisMonth:
			mPageVp.setCurrentItem(1);
			break;
		case R.id.had_pay:
			showPopView(v);
			break;
		default:
			break;
		}
	}
	
	public String getmapState(){
		String numb = null;
		if (mapState != null) {
			numb = (String) mapState.get(payState);
		}
		return numb;
	}
	
	public String getdate(){
		if (DateString != null) {
			return DateString;
		}else {
			return null;
		}
	}

	/**
	 * Ui更新操作Handle
	 */
	private Handler UIhandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String numb = (String) mapState.get(payState);
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:// 更新订单查询日期
				updateDisplay();
				if (currentIndex == 0) {
					mContactsFg.postJiaoyiliushui(DateString, numb);
				} else if (currentIndex == 1) {
					mContactsFg1.postJiaoyiliushui(DateString, numb);
				}
				break;
			case 2:// 跟新支付状态
				had_pay.setText(payState);
				if (currentIndex == 0) {
					mContactsFg.postJiaoyiliushui(DateString, numb);
				} else if (currentIndex == 1) {
					mContactsFg1.postJiaoyiliushui(DateString, numb);
				}
				break;
			default:
				break;
			}
		}

	};
}
