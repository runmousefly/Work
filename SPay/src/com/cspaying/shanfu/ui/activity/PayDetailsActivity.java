package com.cspaying.shanfu.ui.activity;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.scan.ResultActivity;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class PayDetailsActivity extends BaseActivity {

	private RelativeLayout layTitle;
	private TextView tvTitle;
	private Button btnBack;
	private TextView tvBack;
	private TextView pay_status_text;
	private TextView pay_amount_value;//交易金额
	private TextView pay_tag_value;//收款终端
	private TextView merchant_num_value;//商户号
	private TextView merchant_name;//商户名称
	private TextView commodity_name;//商品名称
	private TextView cashier_value;//收银员
	private TextView transaction_num_value;//交易单号
	private TextView merchant_document;//商户单号
	private TextView pay_document;//支付方式单号
	private TextView pay_document_value;//支付方式单号
	private TextView order_time_value;//下单时间
	private TextView pay_type_value;//支付方式
	
	public static String payType="payType";
	public static String outChannelNo="outChannelNo";
	public static String outTradeNo="outTradeNo";
	
	int payTypeValue;
	String outChannelNoValue;
	String outTradeNoValue;
	String paydocument;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paydetails_main);
		MyApplication.getInstance().addActivity(this);
		getIntenData();
		initView();
		initData();
	}
	
	public void getIntenData(){
		Intent intent = getIntent();
		 payTypeValue = intent.getIntExtra(payType,0);
		 outChannelNoValue = intent.getStringExtra(outChannelNo);
		 outTradeNoValue = intent.getStringExtra(outTradeNo);
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("订单支付");

		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);
		
		
		pay_status_text = (TextView) findViewById(R.id.pay_status_text);
		pay_amount_value = (TextView) findViewById(R.id.pay_amount_value);
		
		pay_tag_value = (TextView) findViewById(R.id.pay_tag_value);
		merchant_num_value = (TextView) findViewById(R.id.merchant_num_value);
		merchant_name = (TextView) findViewById(R.id.merchant_name);
		commodity_name = (TextView) findViewById(R.id.commodity_name);
		cashier_value = (TextView) findViewById(R.id.cashier_value);
		transaction_num_value = (TextView) findViewById(R.id.transaction_num_value);
		merchant_document = (TextView) findViewById(R.id.merchant_document);
		pay_document = (TextView) findViewById(R.id.pay_document);
		pay_document_value = (TextView) findViewById(R.id.pay_document_value);
		order_time_value = (TextView) findViewById(R.id.order_time_value);
		pay_type_value = (TextView) findViewById(R.id.pay_type_value);
		
	}
	
	
	public void initData(){
		
		 Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String timePaid = format.format(new Date());
		 String pay_type_TEXT = null;
		 switch (payTypeValue) {
		case 0:
			pay_type_TEXT = "支付宝 - 扫描支付";
			paydocument = "支付宝单号";
			break;
		case 1:
			pay_type_TEXT = "微信 - 扫描支付";
			paydocument = "微信单号";
			break;
		case 2:
			pay_type_TEXT = "京东- 扫描支付";
			paydocument = "京东单号";
			break;

		default:
			break;
		}
		 String loginInfo = LoginUtils.getLoginName(PayDetailsActivity.this);
		 String mchId = null;
		 if (loginInfo.indexOf("@") != -1) {
			  mchId = loginInfo.split("@")[1];
		}else{
			mchId = loginInfo;
		}
			
		//pay_tag_value.setText("");
		merchant_num_value.setText(mchId);
		//cashier_value.setText("");
		transaction_num_value.setText(""+outTradeNoValue);
		//merchant_document.setText("");
		pay_document.setText(paydocument);
		pay_document_value.setText(outChannelNoValue);
		order_time_value.setText(timePaid);
		pay_type_value.setText(pay_type_TEXT);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub  
		super.onClick(v);
		switch (v.getId()) {

		case R.id.tv_left_back:
			finish();
			break;
		default:
			break;
		}
	}
}
