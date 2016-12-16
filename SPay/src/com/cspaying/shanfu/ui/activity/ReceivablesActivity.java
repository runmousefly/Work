package com.cspaying.shanfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.adapter.ImageAdapter;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.scan.CaptureActivity;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class ReceivablesActivity extends BaseActivity {

	private ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9, ibo,
			ibDel, ibNum, ibDot;
	// private TextView tvTitle, tvScanCode;
	private TextView tvNum;
	private ImageView layScaCode;
	private TextView layScaCode_tx;
	private StringBuffer sb;
	private Message msg;
	private String strTmp;
	private Intent intent;
	private ImageView ivLeft, ivRight;
	private RelativeLayout layCenter;

	private Gallery gallery_pay_table;// GalleryFlow
	private ImageView galler_left;
	private ImageView galler_right;

	private ImageAdapter adapter;
	private RelativeLayout gallery_re;

	private boolean measure = false;
	private int quite_width;
	private int quite_height;

	public static int[] images = { R.drawable.icon_ashing_pay,
			R.drawable.icon_ashing_wechat, R.drawable.qq_pay_icon };// icon_ashing_qq

	public static int[] selectedimages = { R.drawable.zhufubao_pay_icon,
			R.drawable.weixin_pay_icon, R.drawable.jd_pay_icon };

	private int currentType = 1;// 0=支付宝支付，1=微信支付，2=qq支付
	public static String payTypeValue = "payTypeValue";
	public static String WebUrl = "WebUrl";
	public static String WebTitle = "WebTitle";

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String num = tvNum.getText().toString().trim();
			switch (msg.what) {
			case 1:
				strTmp = msg.getData().getString("num");
				if (null != strTmp) {
					if (strTmp.equals(".")) {
						if ("".equals(num)) {
							tvNum.setText("0" + strTmp);
						} else if (num.indexOf(".") > 0) {
							break;
						} else {
							tvNum.setText(num + strTmp);
						}
					} else {
						String[] art_num = num.split("\\.");
						Log.d("art_num.length", art_num.length + "");
						if (art_num.length > 1) {
							Log.d("art_num[1].length()", art_num[1]);
							if (art_num[1].length() < 2) {
								tvNum.setText(num + strTmp);
							}
						} else {
							tvNum.setText(num + strTmp);
						}
					}
				}
				break;
			case 2:
				if (num != null && num.length() > 0) {
					tvNum.setText(num.substring(0, num.length() - 1));
				}
				break;
			case 3:
				int type = msg.arg1;
				if (type == 0) {
					galler_left.setImageResource(R.drawable.jd_scan_logo_);
					galler_right
							.setImageResource(R.drawable.icon_ashing_wechat);
				} else if (type == 1) {
					galler_left.setImageResource(R.drawable.icon_ashing_pay);
					galler_right.setImageResource(R.drawable.jd_scan_logo_);
				} else if (type == 2) {
					galler_left.setImageResource(R.drawable.icon_ashing_wechat);
					galler_right.setImageResource(R.drawable.icon_ashing_pay);
				}
				break;
			case 4:
				tvNum.setText(null);
				break;
			case 5:// 支付宝支付
				Intent initen = new Intent(ReceivablesActivity.this,
						PayWebActivity.class);
				startActivity(initen);
				break;
			case 6:
				break;
			default:
				break;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_at);
		MyApplication.getInstance().addActivity(this);
		initView();
		addListener();
	}

	/**
	 * 加载控件
	 */
	private void initView() {
		// TODO Auto-generated method stub
		layScaCode = (ImageView) findViewById(R.id.lay_scanCode);
		layScaCode_tx = (TextView) findViewById(R.id.layScaCode_tx);

		tvNum = (TextView) findViewById(R.id.tv_num);
		// tvxiaoshu1 = (TextView) findViewById(R.id.tv_xiaoshu1);
		// tvxiaoshu2 = (TextView) findViewById(R.id.tv_xiaoshu2);

		ib1 = (ImageButton) findViewById(R.id.ib_one);
		ib2 = (ImageButton) findViewById(R.id.ib_two);
		ib3 = (ImageButton) findViewById(R.id.ib_third);
		ib4 = (ImageButton) findViewById(R.id.ib_four);
		ib5 = (ImageButton) findViewById(R.id.ib_five);
		ib6 = (ImageButton) findViewById(R.id.ib_six);
		ib7 = (ImageButton) findViewById(R.id.ib_seven);
		ib8 = (ImageButton) findViewById(R.id.ib_eight);
		ib9 = (ImageButton) findViewById(R.id.ib_nine);
		ibo = (ImageButton) findViewById(R.id.ib_zero);
		ibDel = (ImageButton) findViewById(R.id.ib_del);
		ibNum = (ImageButton) findViewById(R.id.ib_x);
		ibDot = (ImageButton) findViewById(R.id.ib_dot);

		gallery_pay_table = (Gallery) findViewById(R.id.gallery_pay_table);
		gallery_re = (RelativeLayout) findViewById(R.id.gallery_re);
		galler_left = (ImageView) findViewById(R.id.galler_left);
		galler_right = (ImageView) findViewById(R.id.galler_right);

	}

	private void addListener() {
		// TODO Auto-generated method stub
		layScaCode.setOnClickListener(this);
		layScaCode_tx.setOnClickListener(this);
		ib1.setOnClickListener(this);
		ib2.setOnClickListener(this);
		ib3.setOnClickListener(this);
		ib4.setOnClickListener(this);
		ib5.setOnClickListener(this);
		ib6.setOnClickListener(this);
		ib7.setOnClickListener(this);
		ib8.setOnClickListener(this);
		ib9.setOnClickListener(this);
		ibo.setOnClickListener(this);
		ibDel.setOnClickListener(this);
		ibNum.setOnClickListener(this);
		ibDot.setOnClickListener(this);
		
		galler_left.setOnClickListener(this);
		galler_right.setOnClickListener(this);
	};

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && !measure) {
			measure = true;
			measure();
		}
	}

	private void measure() {
		quite_width = gallery_pay_table.getWidth();
		quite_height = gallery_pay_table.getHeight();
		initGalleryView();
	}

	/**
	 * 加载galleryView
	 */
	public void initGalleryView() {

		int width = quite_width;

		adapter = new ImageAdapter(this, images, selectedimages, quite_width);
		gallery_pay_table.setAdapter(adapter);
		gallery_pay_table.setSelection(currentType);

		gallery_pay_table.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int type = position % selectedimages.length;

				initCode(type);
			}
		});

		gallery_pay_table
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Log.e("+++++++++++onItemSelected", "arg2:" + arg2
								% selectedimages.length);
						/*
						 * adapter.setSelltion(arg2); adapter.changeImage();
						 */
						currentType = arg2 % selectedimages.length;
						Message message = new Message();
						message.what = 3;
						message.arg1 = currentType;
						handler.sendMessage(message);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 根据金额生成二维码
	 */
	public void initCode(int currentType) {
		if (verificationAmount()) {
			String amount_value = tvNum.getText().toString().trim();
			intent = new Intent(ReceivablesActivity.this, PayActivity.class);
			intent.putExtra(PayActivity.Amount, amount_value);
			intent.putExtra(PayActivity.Table_Type, currentType);
			startActivity(intent);
		} else {
			Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 根据跳转扫描页面
	 */
	public void initScan() {
		if (verificationAmount()) {
			String amount_value = tvNum.getText().toString().trim();
			Intent intent = new Intent(ReceivablesActivity.this,
					CaptureActivity.class);
			intent.putExtra(PayActivity.Amount, amount_value);
			intent.putExtra(PayActivity.Table_Type, currentType);
			startActivity(intent);
		} else {
			Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		msg = new Message();
		switch (v.getId()) {
		case R.id.lay_scanCode:
			initScan();
			// initCode();
			break;
		case R.id.ib_zero:
			msg.what = 1;
			msg.getData().putString("num", "0");
			handler.sendMessage(msg);
			break;
		case R.id.ib_one:
			msg.what = 1;
			msg.getData().putString("num", "1");
			handler.sendMessage(msg);
			break;
		case R.id.ib_two:
			msg.what = 1;
			msg.getData().putString("num", "2");
			handler.sendMessage(msg);
			break;
		case R.id.ib_third:
			msg.what = 1;
			msg.getData().putString("num", "3");
			handler.sendMessage(msg);
			break;
		case R.id.ib_four:
			msg.what = 1;
			msg.getData().putString("num", "4");
			handler.sendMessage(msg);
			break;
		case R.id.ib_five:
			msg.what = 1;
			msg.getData().putString("num", "5");
			handler.sendMessage(msg);
			break;
		case R.id.ib_six:
			msg.what = 1;
			msg.getData().putString("num", "6");
			handler.sendMessage(msg);
			break;
		case R.id.ib_seven:
			msg.what = 1;
			msg.getData().putString("num", "7");
			handler.sendMessage(msg);
			break;
		case R.id.ib_eight:
			msg.what = 1;
			msg.getData().putString("num", "8");
			handler.sendMessage(msg);
			break;
		case R.id.ib_nine:
			msg.what = 1;
			msg.getData().putString("num", "9");
			handler.sendMessage(msg);
			break;
		case R.id.ib_dot:
			msg.what = 1;
			msg.getData().putString("num", ".");
			handler.sendMessage(msg);
			break;
		case R.id.ib_del:
			msg.what = 2;
			msg.getData().putString("del", "");
			handler.sendMessage(msg);
			break;
		case R.id.ib_x:
			msg.what = 4;
			msg.getData().putString("c", "");
			handler.sendMessage(msg);
			break;
		case R.id.galler_left:
			currentType --;
			if(currentType < 0)
			{
				currentType = 2;
			}
			else if(currentType > 2)
			{
				currentType = 0;
			}
			gallery_pay_table.setSelection(currentType+1);
			break;
		case R.id.galler_right:
			currentType ++;
			if(currentType < 0)
			{
				currentType = 2;
			}
			else if(currentType > 2)
			{
				currentType = 0;
			}
			gallery_pay_table.setSelection(currentType+1);
			break;
		default:
			break;
		}
	}

	public Boolean verificationAmount() {
		CharSequence cs = tvNum.getText();
		if (cs == null) {
			return false;
		}
		String num = cs.toString().trim();
		if (num == null || "".equals(num)) {
			return false;
		}
		// if (num.length() > 7) {
		// return false;
		// }
		Double d_num = Double.parseDouble(num);
		if (d_num <= 0.00d) {
			return false;
		}
		// Log.d("Double d_num:", d_num + "");
		return true;
	}
}
