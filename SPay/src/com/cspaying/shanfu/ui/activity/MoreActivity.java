package com.cspaying.shanfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class MoreActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout layPasSafe, layCashier, layCommodityName, layRefund,
			layTestVersion, layCommonProblem, layMerchantData,rece_code;
	private TextView userName;
	private Intent intent;
	private String version;
	private TextView tvVersion;
	private TextView login_quit;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_more);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		layPasSafe = (RelativeLayout) findViewById(R.id.lay_pasSafe);
		layPasSafe.setOnClickListener(this);
		layCashier = (RelativeLayout) findViewById(R.id.lay_cashier_manager);

		layMerchantData = (RelativeLayout) findViewById(R.id.lay_merchantData);

		layCommodityName = (RelativeLayout) findViewById(R.id.lay_CommodityName);
		layCommonProblem = (RelativeLayout) findViewById(R.id.lay_more_common_problem);
		layRefund = (RelativeLayout) findViewById(R.id.lay_refund);
		layTestVersion = (RelativeLayout) findViewById(R.id.lay_testVersion);
		rece_code = (RelativeLayout) findViewById(R.id.rece_code);
		
		
		tvVersion = (TextView) findViewById(R.id.tv_more_version);
		userName = (TextView) findViewById(R.id.userName);  

		layMerchantData.setOnClickListener(this);
		layTestVersion.setOnClickListener(this);  
		layRefund.setOnClickListener(this);
		layCashier.setOnClickListener(this);
		layCommodityName.setOnClickListener(this);
		layCommonProblem.setOnClickListener(this);
		rece_code.setOnClickListener(this);
		
		login_quit = (TextView) findViewById(R.id.login_quit);
		login_quit.setOnClickListener(this);

		initViewData();
	}
	
	public void initViewData(){
		
		String user_name =getResources().getString(R.string.welcome)
				+"！"+  LoginUtils.getLoginName(MoreActivity.this);//.split("@")[1];
				
		userName.setText(user_name);
		version = MyApplication.getStrVersion();
		tvVersion.setText(version);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lay_merchantData://商户资料
			intent = new Intent(this, MerchantDataActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_pasSafe://密码安全
			intent = new Intent(this, PasSafeActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_cashier_manager://收银员管理
			intent = new Intent(this, CashierMangerActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_CommodityName://商品名称
			intent = new Intent(this, CommodityNameActivity.class);
			startActivity(intent);
			break;

		case R.id.lay_refund://退款管理
			intent = new Intent(this, RefundActivity.class);
			startActivity(intent);
			break;
		case R.id.rece_code :
			intent = new Intent(this, RececodeActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_testVersion:

			Toast.makeText(this, "当前是最新版本", Toast.LENGTH_SHORT).show();
			break;

		case R.id.lay_more_common_problem:
			Intent intent = new Intent(MoreActivity.this,CommonProblem.class);
			startActivity(intent);
			break;
		case R.id.login_quit:
			LoginUtils.setLoginFlag(MoreActivity.this, 2);
			MyApplication.getInstance().exit();
			break;
		default:
			break;
		}
	}

}
