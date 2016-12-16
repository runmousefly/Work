package com.cspaying.shanfu.ui.activity;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;

public class MainTabActivity extends TabActivity implements
		OnCheckedChangeListener {

	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
	private Intent mEIntent;
	private RadioButton rbFlowingWater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintabs);
		MyApplication.getInstance().addActivity(this);
		MyApplication.getInstance().addActivity(this);
		this.mAIntent = new Intent(this, ReceivablesActivity.class);
		this.mBIntent = new Intent(this, FlowingWaterActivity.class);
		this.mCIntent = new Intent(this, StatisticsNewActivity.class);
		this.mDIntent = new Intent(this, MoreActivity.class);
		// this.mEIntent = new Intent(this, EActivity.class);

		((RadioButton) findViewById(R.id.radio_button0))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button1))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button2))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button3))
				.setOnCheckedChangeListener(this);
		// ((RadioButton) findViewById(R.id.radio_button4))
		// .setOnCheckedChangeListener(this);
		// //
		setupIntent();
	}

	@SuppressLint("NewApi")
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.radio_button1:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.radio_button2:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.radio_button3:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			// case R.id.radio_button4:
			// this.mTabHost.setCurrentTabByTag("MORE_TAB");
			// break;
			}
		}

	}

	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("A_TAB", "收款",
				R.drawable.skin_tabbar_icon_auth_select, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", "流水",
				R.drawable.skin_tabbar_icon_at_select, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB", "统计",
				R.drawable.skin_tabbar_icon_space_select, this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB", "更多",
				R.drawable.skin_tabbar_icon_more_select, this.mDIntent));

		// localTabHost.addTab(buildTabSpec("MORE_TAB", R.string.more,
		// R.drawable.icon_5_n, this.mEIntent));

	}

	private TabHost.TabSpec buildTabSpec(String tag, String resLabel,
			int resIcon, final Intent content) {
		return this.mTabHost.newTabSpec(tag)
				.setIndicator(resLabel, getResources().getDrawable(resIcon))
				.setContent(content);
	}

}
