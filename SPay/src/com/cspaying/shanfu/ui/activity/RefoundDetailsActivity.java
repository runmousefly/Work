package com.cspaying.shanfu.ui.activity;

import java.text.DecimalFormat;
import java.util.Map;

import org.nutz.json.Json;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.entit.PostOrderDetails;
import com.cspaying.shanfu.ui.entit.TransStatus;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.DialogUtil;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class RefoundDetailsActivity extends BaseActivity {

	private RelativeLayout layTitle;
	private TextView tvTitle;
	private Button btnBack;
	private TextView tvBack;
	private Button btnRefund;
	private String channel;
	private String status;
	private double amount;
	private TextView pay_amount_value;// 交易金额
	private TextView pay_tag_value;// 收款终端
	private TextView merchant_num_value;// 商户号
	private TextView merchant_name;// 商户名称
	private TextView commodity_name;// 商品名称
	private TextView cashier_value;// 收银员
	private TextView transaction_num_value;// 交易单号
	private TextView merchant_document;// 商户单号
	private TextView pay_document;// 支付方式单号
	private TextView pay_document_value;// 支付方式单号
	private TextView order_time_value;// 下单时间
	private TextView pay_type_value;// 支付方式
	private TextView pay_statu_value;// 交易状态
	private TextView finish_time_value;// 完成时间

	// public static String mchIdKey = "mchIdkey";
	public static String outTradeNoKey = "outTradeNokey";
	public static String queryTypeKey = "queryTypekey";
	private String mchId;
	private String outTradeNo;
	private String queryType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refound_details);
		MyApplication.getInstance().addActivity(this);

		mchId = LoginUtils.getLoginMcid(getApplicationContext());
		initInten();
		initView();
		initPostData();
	}

	public void initInten() {
		Log.d("initInten", "++++++++++++++++++++");
		Intent intent = getIntent();
		if (intent.getFlags() == 200) {
			outTradeNo = intent.getStringExtra(outTradeNoKey);
			queryType = intent.getStringExtra(queryTypeKey);

			Log.d("退款  2 outTradeNoKey", outTradeNo);
			Log.d("退款  2 queryTypeKey", queryType);
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);
		btnRefund = (Button) findViewById(R.id.btn_refund);
		btnRefund.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("订单详情");

		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);

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
		pay_statu_value = (TextView) findViewById(R.id.pay_statu_value);
		finish_time_value = (TextView) findViewById(R.id.finish_time_value);
	}

	public void initPostData() {
		String jsonData = InitJson.getInstance(RefoundDetailsActivity.this)
				.initDetailOrder("cs.trade.single.query", "1.0", mchId,
						outTradeNo, queryType);

		HttpUtil.getInstance(RefoundDetailsActivity.this).reques(jsonData,
				HttpUtil.baseUrl, new OnRequestListener() {
					@Override
					public void onResult(int statusCode, String str) {
						// TODO Auto-generated method stub
						Message message = new Message();
						if (statusCode == 0) {
							message.what = 1;
							message.obj = str;
						} else {
							message.what = 2;
						}
						UIhandle.sendMessage(message);
					}
				});

	}

	public void initData(PostOrderDetails entity) {
		// amount
		if (entity != null) {
			channel = entity.getChannel();
			status = entity.getStatus();
			DecimalFormat df = new DecimalFormat(".00");
			amount = Double.valueOf(df.format(entity.getAmount()));
			pay_amount_value.setText("" + entity.getAmount());
			//pay_tag_value.setText("" + entity.getPostscript());
			merchant_num_value.setText(mchId);
			// merchant_name.setText("");
			commodity_name.setText("");
			//cashier_value.setText("" + entity.getCashierName());
			transaction_num_value.setText("" + entity.getOutTradeNo());
			merchant_document.setText("" + entity.getOutTradeNo());
			pay_document.setText("");
			pay_document_value.setText("");
			order_time_value.setText("" + entity.getTransTime());
			//pay_type_value.setText("" + entity.getPostscript());
			pay_statu_value.setText("" + entity.getStatus());
			finish_time_value.setText("" + entity.getTransTime());
		}

	}

	private Handler UIhandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				String jsondata = msg.obj.toString();
				PostOrderDetails entity = ParaseJson.getInstance(
						RefoundDetailsActivity.this).ParasepostDetail(jsondata);
				initData(entity);
				break;
			case 2:
				DialogUtil.getInstance(RefoundDetailsActivity.this).stopProgressDialog();
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.refund_sucess), Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				DialogUtil.getInstance(RefoundDetailsActivity.this).stopProgressDialog();
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.refund_fail), Toast.LENGTH_SHORT)
						.show();
				break;
			case 4:
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.load_data_error), Toast.LENGTH_SHORT)
						.show();  
				break;
			default:
				break;
			}
		}

	};
	
	public String getChannel(){
		String chanel = null;
		if (channel != null) {
			if (channel.indexOf("wx") != -1) {
				chanel = "weixin";
			}else if (channel.indexOf("jd") != -1) {
				chanel = "jd";
			}else if (channel.indexOf("alipay") != -1) {
				chanel = "alipay";
			}
		}else {
			chanel = "weixin";
		}
		
		return chanel;
	}

	public void postReFund() {
		TransStatus trans = new TransStatus();
		trans.setStatus(status);
		String cannel = getChannel();
		String token = LoginUtils.getLoginToken(RefoundDetailsActivity.this);
		String jsonData = InitJson.getInstance(RefoundDetailsActivity.this)
				.Refund("cs.refund.apply.inner", "1.0", token, mchId, cannel, outTradeNo
						, outTradeNo, ""+amount, "tuikuang",trans);
		Log.d("postData", jsonData);
		if (jsonData != null) {
			DialogUtil.getInstance(RefoundDetailsActivity.this)
			     .startProgressDialog(RefoundDetailsActivity.this, "");
			HttpUtil.getInstance(this.getApplicationContext()).reques(jsonData,
					HttpUtil.baseUrl, new OnRequestListener() {
						@Override  
						public void onResult(int statusCode, String result) {
							Log.d("getData", result);
							// TODO Auto-generated method stub
							Message message = new Message();
							if (statusCode == 0) {
								Map<String, String> map = Json.fromJson(Map.class,
										result);
								if (map.get("resultCode").equals("0")) {
									message.what = 2;
									 UIhandle.sendMessage(message);
									finish();
								} else {
									message.what = 3;
									 UIhandle.sendMessage(message);
								}
								// paraseData(result);
							} else {
								message.what = 3;
								 UIhandle.sendMessage(message);
							} 
						}
					});
		}else {
			Message message = new Message();
			message.what = 3;
		    UIhandle.sendMessage(message);
		}
		
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_left_back:
			finish();
			break;
		case R.id.btn_refund:
			postReFund();
			break;
		default:
			break;
		}
	}

}
