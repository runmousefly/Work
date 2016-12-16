package com.cspaying.shanfu.ui.activity;

import java.util.Calendar;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.entit.Re_Pay_Sweixin_ntity;
import com.cspaying.shanfu.ui.entit.Re_zf_pay;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.InitSignString;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.scan.ResultActivity;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class PayWebActivity extends BaseActivity {

	private RelativeLayout layTitle;
	private TextView tvTitle;
	private Button btnBack;
	private TextView tvBack;
	private WebView paywebView;
	private String url;
	private int type;
    private String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_web);
		MyApplication.getInstance().addActivity(this);
		initIntent();
		initView();
		initPost(type,title);
	}
	
	/**
	 * 获取页面跳转的INTENT值
	 */
	public void initIntent(){
		Intent  intent = getIntent();
		 type = intent.getIntExtra(ReceivablesActivity.payTypeValue, 0);
		 title = intent.getStringExtra(ReceivablesActivity.WebTitle);
	}
    
	
	/**
	 * 支付请求
	 * @param type 0=支付宝支付，1=微信支付，2=qq支付
	 * @param url
	 */
	public void initPost(int type,String url){
		switch (type) {
		 case 0:
			 postJingDongPay();
			break;
		case 1:
			
		  break;
		  
		case 3:
			
		  break;
			

		default:
			break;
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(title);

		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);
		paywebView = (WebView) findViewById(R.id.paywebview);
	}
	
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 定义一个Handler，用于处理下载线程与UI间通讯
			switch (msg.what) {
			case 1 :
				String jsonData = (String) msg.obj;
				ParseReSWeixinPay(jsonData);
				break;
			case 2:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				break;
			case 3 :
				 url = (String) msg.obj;
				 initHtml5Web();
			   break;
			case 4:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				break;
				
			case 5:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				
				break;
			case 6:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	
	/**
	 * 京东支付接口
	 */
	public void postJingDongPay(){
		    String outTradeNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
			String loginInfo = LoginUtils.getLoginName(PayWebActivity.this);
			String token = LoginUtils.getLoginToken(PayWebActivity.this);
			String mchId = loginInfo;//.split("@")[1];
			String jsonData = InitJson.getInstance(PayWebActivity.this)
				.JDpay("0.01", "京东支付", "jdPay", "京东支付", "2", "000050001000001", outTradeNo
						, "cs.pay.submit", "CNY", "1.0");
			
			HttpUtil.getInstance(PayWebActivity.this).reques(jsonData,HttpUtil.baseUrl, 
					new OnRequestListener() {
				
				@Override
				public void onResult(int statusCode, String str) {
					// TODO Auto-generated method stub
					Message message = new Message();
					if (statusCode == 0) {
						message.what = 9;
						message.obj = str;
					}else{
						message.what = 2;
					}
					handler.sendMessage(message);
				}
			});
	}
	
	
	/**
	 * 微信支付接口
	 */
	public void postWeixinPay(){
		    String outTradeNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
			String loginInfo = LoginUtils.getLoginName(PayWebActivity.this);
			String token = LoginUtils.getLoginToken(PayWebActivity.this);
			String mchId = loginInfo;//.split("@")[1];
			String jsonData = InitJson.getInstance(PayWebActivity.this)
				.JDpay("0.01", "微信支付", "wxApp", "微信支付", "2", mchId, outTradeNo
						, "cs.pay.submit", "CNY", "1.0");
			
			HttpUtil.getInstance(PayWebActivity.this).reques(jsonData,HttpUtil.baseUrl, 
					new OnRequestListener() {
				
				@Override
				public void onResult(int statusCode, String str) {
					// TODO Auto-generated method stub
					Message message = new Message();
					if (statusCode == 0) {
						message.what = 1;
						message.obj = str;
					}else{
						message.what = 2;
					}
					handler.sendMessage(message);
				}
			});
	}
	
	
	/**
	 * 解析订单支付返回JSON字符串
	 * @param jsonData
	 */
	public void ParseReSWeixinPay(String jsonData){
		Re_zf_pay reSweixinpayEntity = ParaseJson.getInstance(PayWebActivity.this)
				    .Parasezhifubao(jsonData);
		if (reSweixinpayEntity != null) {
			Message message = new Message();
			if(reSweixinpayEntity.getResultCode().equals("0")){
				HashMap<String, String> hashMap = new HashMap<String, String>();
				
				  
				//hashMap.put("outTradeNo", reSweixinpayEntity.getOutTradeNo());
				hashMap.put("outChannelNo", reSweixinpayEntity.getOutChannelNo());
				//hashMap.put("payCode", reSweixinpayEntity.getPayCode());
				hashMap.put("resultCode", reSweixinpayEntity.getResultCode());
				hashMap.put("returnCode", reSweixinpayEntity.getReturnCode());
				String Loginkey = LoginUtils.getSignKey(MyApplication.getContext());
				String sign = InitSignString.getSign(hashMap,Loginkey);
				Log.e("++++++++++++++++++++++++++sign", sign);
				if (sign.equals(reSweixinpayEntity.getSign())) {
					message.what = 3;
				//	message.obj = reSweixinpayEntity.getPayCode();
				}else {
					message.what = 5;
				}
			}else {
				message.what = 4;  
				
			}
			handler.sendMessage(message);
			
		}else {
			handler.sendEmptyMessage(6);
		}
	}
	
	/**
	 * 加载Html5网页
	 */
	public void initHtml5Web() {
		WebSettings webSettings = paywebView.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
		paywebView.loadUrl(url);
		Log.e("+++++++++++++++++++++loadUrl"
				, "loadUrl:"+url);
		// 设置Web视图
		paywebView.setWebViewClient(new webViewClient());

	}
	
	
	//Web视图    
		private class webViewClient extends WebViewClient {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
		    }

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
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
		default:
			break;
		}
	}
}
