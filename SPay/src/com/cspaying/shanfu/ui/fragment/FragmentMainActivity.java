package com.cspaying.shanfu.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.cspaying.shanfu.R;

@SuppressLint("NewApi")
public class FragmentMainActivity extends FragmentActivity implements
		OnClickListener {
	private FragmentReceivables fragmentReceivables;
	private FragmentFlowingWater fragmentFlowingWater;
	private FragmentStatistics fragmentStatistics;
	private FragmentMore fragmentMore;

	private FrameLayout atFl, authFl, spaceFl, moreFl;

	private ImageView atIv, authIv, spaceIv, moreIv;

	private ImageView toggleImageView, plusImageView;

	private PopupWindow popWindow;

	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		initView();

		initData();

		clickReceivablesBtn();
	}

	private void initView() {
		// TODO Auto-generated method stub
		atFl = (FrameLayout) findViewById(R.id.layout_Receivables);
		authFl = (FrameLayout) findViewById(R.id.layout_FlowingWater);
		spaceFl = (FrameLayout) findViewById(R.id.layout_Statistics);
		moreFl = (FrameLayout) findViewById(R.id.layout_more);

		// ʵ����ͼƬ�������
		atIv = (ImageView) findViewById(R.id.image_at);
		authIv = (ImageView) findViewById(R.id.image_space);
		spaceIv = (ImageView) findViewById(R.id.image_space);
		moreIv = (ImageView) findViewById(R.id.image_more);

		// ʵ������ťͼƬ���
		toggleImageView = (ImageView) findViewById(R.id.toggle_btn);
		plusImageView = (ImageView) findViewById(R.id.plus_btn);
	}

	private void initData() {
		// TODO Auto-generated method stub
		atFl.setOnClickListener(this);
		authFl.setOnClickListener(this);
		spaceFl.setOnClickListener(this);
		moreFl.setOnClickListener(this);

		// ����ťͼƬ���ü���
		toggleImageView.setOnClickListener(this);
	}

	/**
	 * 收款按扭
	 */
	private void clickReceivablesBtn() {
		// TODO Auto-generated method stub
		fragmentReceivables = new FragmentReceivables();
		// �õ�Fragment���������
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		// �滻��ǰ��ҳ��
		fragmentTransaction.replace(R.id.frame_content, fragmentReceivables);
		// ��������ύ
		fragmentTransaction.commit();
		// �ı�ѡ��״̬
		atFl.setSelected(true);
		atIv.setSelected(true);

		authFl.setSelected(false);
		authIv.setSelected(false);

		spaceFl.setSelected(false);
		spaceIv.setSelected(false);

		moreFl.setSelected(false);
		moreIv.setSelected(false);
	}

	@Override
	// TODO Auto-generated method stub
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击收款按钮
		case R.id.layout_Receivables:
			clickReceivablesBtn();
			break;
		// 点击流水按钮
		case R.id.layout_FlowingWater:
			clickFlowingWaterBtn();
			break;
		// 点击统计按钮
		case R.id.layout_Statistics:
			clickStatisticsBtn();
			break;
		// 点击更多按钮
		case R.id.layout_more:
			clickMoreBtn();
			break;
		// 点击中间按钮
		case R.id.toggle_btn:
			clickToggleBtn();
			break;
		}
	}
	
	/**
	 * 点击了“流水”按钮
	 */
	private void clickFlowingWaterBtn() {
		// 实例化Fragment页面
		fragmentFlowingWater = new FragmentFlowingWater();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentFlowingWater);
		// 事务管理提交
		fragmentTransaction.commit();

		atFl.setSelected(false);
		atIv.setSelected(false);

		authFl.setSelected(true);
		authIv.setSelected(true);

		spaceFl.setSelected(false);
		spaceIv.setSelected(false);

		moreFl.setSelected(false);
		moreIv.setSelected(false);
	}

	/**
	 * 点击了“统计”按钮
	 */
	private void clickStatisticsBtn() {
		// 实例化Fragment页面
		fragmentStatistics = new FragmentStatistics();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentStatistics);
		// 事务管理提交
		fragmentTransaction.commit();

		atFl.setSelected(false);
		atIv.setSelected(false);

		authFl.setSelected(false);
		authIv.setSelected(false);

		spaceFl.setSelected(true);
		spaceIv.setSelected(true);

		moreFl.setSelected(false);
		moreIv.setSelected(false);
	}

	
	/**
	 * 点击了“更多”按钮
	 */
	private void clickMoreBtn() {
		// 实例化Fragment页面
		fragmentMore = new FragmentMore();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentMore);
		// 事务管理提交
		fragmentTransaction.commit();

		atFl.setSelected(false);
		atIv.setSelected(false);

		authFl.setSelected(false);
		authIv.setSelected(false);

		spaceFl.setSelected(false);
		spaceIv.setSelected(false);

		moreFl.setSelected(true);
		moreIv.setSelected(true);
	}

	/**
	 * 点击了中间按钮
	 */
	private void clickToggleBtn() {
		showPopupWindow(toggleImageView);
		// 改变按钮显示的图片为按下时的状态
		plusImageView.setSelected(true);
	}

	/**
	 * 改变显示的按钮图片为正常状态
	 */
	private void changeButtonImage() {
		plusImageView.setSelected(false);
	}

	/**
	 * 显示PopupWindow弹出菜单
	 */
	private void showPopupWindow(View parent) {
		if (popWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
			dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			// 创建一个PopuWidow对象
			popWindow = new PopupWindow(view, dm.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT); 
		}
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		popWindow.setFocusable(true);
		// 设置允许在外点击消失
		popWindow.setOutsideTouchable(true);
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		// PopupWindow的显示及位置设置
		// popWindow.showAtLocation(parent, Gravity.FILL, 0, 0);
		popWindow.showAsDropDown(parent, 0,0);

		popWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// 改变显示的按钮图片为正常状态
				changeButtonImage();
			}
		});

		// 监听触屏事件
		popWindow.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				// 改变显示的按钮图片为正常状态
				changeButtonImage();
				popWindow.dismiss();
				return false;
			}
		});
	}
}
