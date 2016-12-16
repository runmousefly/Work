package com.cspaying.shanfu.ui.scan;

import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.activity.LoginActivity;
import com.cspaying.shanfu.ui.activity.PayActivity;
import com.cspaying.shanfu.ui.activity.PayDetailsActivity;
import com.cspaying.shanfu.ui.activity.PostTestActivity;
import com.cspaying.shanfu.ui.entit.ExcraEntity;
import com.cspaying.shanfu.ui.entit.Re_Pay_Sweixin_ntity;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.InitSignString;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.CustomProgressDialog;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class ResultActivity extends Activity implements OnClickListener {

	private ImageView mResultImage;
	private TextView mResultText;
	private String ScanId;
	private TextView test;
	private ProgressBar progress;
	private Button tuikuang;
	private String outTradeNo;
	
	private String  strAmount;
	private int currentType = 0;
	private CustomProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		initView();
		initData();
	}

	public void initView() {
		mResultImage = (ImageView) findViewById(R.id.result_image);  
		mResultText = (TextView) findViewById(R.id.result_text);
		test = (TextView) findViewById(R.id.test);
		progress = (ProgressBar) findViewById(R.id.progress);
		tuikuang = (Button) findViewById(R.id.tuikan);
		tuikuang.setOnClickListener(this);
	}

	public void initData() {
		//progress.setVisibility(View.VISIBLE);
		Bundle extras = getIntent().getExtras();

		if (null != extras) {
			

			String result = extras.getString("result");
			ScanId = result;// 扫描得到字符串
			currentType = extras.getInt(PayActivity.Table_Type, 0);
			strAmount = extras.getString(PayActivity.Amount);
			//Log.e("++++++++++++++currentType", ""+currentType);
		//	Log.e("++++++++++++++strAmount", ""+strAmount);
			startProgressDialog();
			switch (currentType) {
			case 0:
				String zjsondata = initzhifubaoJson();
				HtttPost(zjsondata);
				break;
			case 1:
				String wjsondata = initScanJson();
				HtttPost(wjsondata);
				break;
			case 2:
				String jjsondata = initjingdongJson();
				HtttPost(jjsondata);
				break;

			default:
				break;
			}
			
			
		}
	}
	
	/**
	 * 订单支付
	 */
	public void HtttPost(String jsondata){
		
		HttpUtil.getInstance(ResultActivity.this).reques(jsondata,HttpUtil.baseUrl
				, new OnRequestListener() {
			
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
				uIhHandler.sendMessage(message);
			}
		});
	}
	
	/**
	 * 微信刷卡生成订单支付请求json字符窜
	 * @return
	 */
	public String initScanJson(){  
		 Format format = new SimpleDateFormat("yyyyMMddHHmmss");
		 String timePaid = format.format(new Date());
		 String loginInfo = LoginUtils.getLoginName(this);
		 String mchId = "";
		 if (loginInfo != null) {
			 mchId = loginInfo;//.split("@")[1];
		}
		
		 ExcraEntity entity = new ExcraEntity();
		 entity.setAuthCode(ScanId);
		 
		 String scanPayJso = InitJson.getInstance(this).InitOrder(
					"cs.pay.submit.inner", "1.0",mchId, "wxMicro"
					,strAmount,strAmount, "CNY", "subject", "TESTbody", entity, timePaid
					, "100", "testdescription",loginInfo,"CSP_AND","authCode",ScanId);
	
		return scanPayJso;
		
	}
	
	
	/**
	 * 支付宝生成订单支付请求json字符窜
	 * @return
	 */
	public String initzhifubaoJson(){  
		 Format format = new SimpleDateFormat("yyyyMMddHHmmss");
		 String timePaid = format.format(new Date());
		 String loginInfo = LoginUtils.getLoginName(this);
		 String mchId = "";
		 if (loginInfo != null) {
			 mchId = loginInfo;//.split("@")[1];
		}
		
		 ExcraEntity entity = new ExcraEntity();
		 entity.setAuthCode(ScanId);
		 
		 String scanPayJso = InitJson.getInstance(this).InitOrder(
					"cs.pay.submit.inner", "1.0",mchId, "alipayMicro"
					,strAmount,strAmount, "CNY", "subject", "支付宝支付", entity, timePaid
					, "100", "test description",loginInfo,"CSP_AND","authCode",ScanId);
	
		return scanPayJso;
	}
	
	/**
	 * 京东生成订单支付请求json字符窜
	 * @return
	 */
	public String initjingdongJson(){  
		 Format format = new SimpleDateFormat("yyyyMMddHHmmss");
		 String timePaid = format.format(new Date());
		 String loginInfo = LoginUtils.getLoginName(this);
		 String mchId = "";
		 if (loginInfo != null) {
			 mchId = loginInfo;//.split("@")[1];
		}
		
		 ExcraEntity entity = new ExcraEntity();
		 entity.setAuthCode(ScanId);
		 
		String scanPayJso = InitJson.getInstance(this).InitOrder(
		"cs.pay.submit.inner", "1.0",mchId, "jdMicro"
		,strAmount,strAmount, "CNY", "subject", "京东支付", entity, timePaid
		, "100", "testdescription",loginInfo,"CSP_AND","authCode",ScanId);
	
		return scanPayJso;
	}
	
	
	/**
	 * 解析订单支付返回JSON字符串
	 * @param jsonData
	 */
	public void ParseReSWeixinPay(String jsonData){
		Re_Pay_Sweixin_ntity reSweixinpayEntity = ParaseJson.getInstance(ResultActivity.this)
				    .ParaseWeixinPay(jsonData);
		if (reSweixinpayEntity != null) {
			Message message = new Message();
			if(reSweixinpayEntity.getResultCode().equals("0")){
				HashMap<String, String> hashMap = new HashMap<String, String>();
				 
				hashMap.put("returnCode", reSweixinpayEntity.getReturnCode());
				hashMap.put("resultCode", reSweixinpayEntity.getResultCode());
				hashMap.put("outTradeNo", reSweixinpayEntity.getOutTradeNo());
				hashMap.put("outChannelNo", reSweixinpayEntity.getOutChannelNo());
				if (reSweixinpayEntity.getReturnMsg() != null) {
					hashMap.put("returnMsg", reSweixinpayEntity.getReturnMsg());
				}
				String Loginkey = LoginUtils.getSignKey(MyApplication.getContext());
				String sign = InitSignString.getSign(hashMap,Loginkey);
				Log.e("++++++++++++++++++++++++++sign", sign);
				if (true) {//sign.equals(reSweixinpayEntity.getSign())
					message.what = 3;
					outTradeNo = reSweixinpayEntity.getOutTradeNo();
					message.obj = reSweixinpayEntity;
				}else {
					message.what = 5;  
				}
			}else {
				String errorStr = reSweixinpayEntity.getReturnMsg();
				message.what = 4;
				message.obj = errorStr;
				
			}
			uIhHandler.sendMessage(message);
			
		}else {
			uIhHandler.sendEmptyMessage(6);
		}
	}
	
	
	/**
	 * 退款申请
	 */
	public void tuikuanqingqiu(){
		String loginInfo = LoginUtils.getLoginName(ResultActivity.this);
		String token = LoginUtils.getLoginToken(ResultActivity.this);
		String mchId = loginInfo;//.split("@")[1];
		String jsonData = InitJson.getInstance(ResultActivity.this)
			.initRefund("cs.refund.apply", "1.0", token
					, mchId, "weixin", outTradeNo, "", "0.01", "TestRefund");
		HttpUtil.getInstance(ResultActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}  
		});
	}
	
	private Handler uIhHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				String data = (String) msg.obj;
				ParseReSWeixinPay(data);
				break;
			case 2:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 3:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_sucess),Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(ResultActivity.this,PayDetailsActivity.class);
				intent.putExtra(PayDetailsActivity.payType, currentType);
				Re_Pay_Sweixin_ntity entity = (Re_Pay_Sweixin_ntity) msg.obj;
				intent.putExtra(PayDetailsActivity.outChannelNo, entity.getOutChannelNo());
				intent.putExtra(PayDetailsActivity.outTradeNo, entity.getOutTradeNo());
				startActivity(intent);   
				finish();
				break;
				
			case 4:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 5:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.packet_inspection_error),Toast.LENGTH_SHORT)
						.show();
				finish();
				break;
			case 6:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pay_error)
						,Toast.LENGTH_SHORT).show();
				finish();
				break;
			

			default:
				break;
			}
			
			if (what != 1) {
			//	finish();
				//progress.setVisibility(View.GONE);
				stopProgressDialog();
			}  
			
		}
		
		
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.tuikan:
			tuikuanqingqiu();
			break;

		default:
			break;
		}
	}
	
	 /**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(this);
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
