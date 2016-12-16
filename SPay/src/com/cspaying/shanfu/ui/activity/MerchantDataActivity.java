package com.cspaying.shanfu.ui.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.entit.MerchantInformation;
import com.cspaying.shanfu.ui.entit.Re_zf_pay;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.InitSignString;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class MerchantDataActivity extends BaseActivity{

	private RelativeLayout layTitle;
	private TextView tvTitle;
	private Button btnBack;
	private TextView tvBack;
	private Spinner hangye_sp;
	private TextView MerchantName;
	private TextView industryName;
	private TextView privance;
	private TextView city;
	private TextView email;
	private TextView blankname;
	private TextView blank_web;
	private TextView blank_user;
	
	private ArrayAdapter<String> adapter;
	private static final String[] hangye = {"实物","餐饮",};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_data_main);
		MyApplication.getInstance().addActivity(this);
		initView();
		initSppiner();
		MerchantInformation mMerchData = MyApplication.getInstance().getMerchantInformation();
		if(mMerchData != null)
		{
			initInfomation(mMerchData);
		}
		else
		{
			postShanghuxinxi();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("商户资料");

		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);
		
		hangye_sp = (Spinner) findViewById(R.id.hangye_sp);
		MerchantName = (TextView) findViewById(R.id.MerchantName);
		industryName = (TextView) findViewById(R.id.industryName);
		privance = (TextView) findViewById(R.id.privance);
		city = (TextView) findViewById(R.id.city);
		email = (TextView) findViewById(R.id.email);
		blankname = (TextView) findViewById(R.id.blankname);
		blank_web = (TextView) findViewById(R.id.blank_web);
		blank_user = (TextView) findViewById(R.id.blank_user);
	}
	
	/**
	 * 请求用户信息
	 */
     public void postShanghuxinxi(){
		
		String loginInfo = LoginUtils.getLoginName(MerchantDataActivity.this);
		String token = LoginUtils.getLoginToken(MerchantDataActivity.this);
		String jsonData = InitJson.getInstance(MerchantDataActivity.this)
				.initMerchantDetail(loginInfo,token, "cs.mert.info.query", "1.0");
		HttpUtil.getInstance(MerchantDataActivity.this).reques(jsonData, HttpUtil.baseUrl
				, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				Message message = new Message();
				if (statusCode ==0) {
					LoginUtils.setMerchantData(MyApplication.getContext(), str, null);
					message.what = 1;
					message.obj = str;
				}else {
					message.what = 2;
				}
				handler.sendMessage(message);
			}
		});
		
	}
     
     /**
 	 * 解析返回JSON字符串
 	 * @param jsonData
 	 */
 	public void ParseMerchantInfomation(String jsonData){
 		MerchantInformation Entity = ParaseJson.getInstance(MerchantDataActivity.this)
 				    .ParaseMerchantInfomation(jsonData);
 		if (Entity != null) {
 			Message message = new Message();
 			if(Entity.getResultCode().equals("0")){
 				message.what = 3;
 				message.obj = Entity;
 			}else {
 				message.what = 4;
 				
 			}
 			handler.sendMessage(message);
 			
 		}else {
 			handler.sendEmptyMessage(5);
 		}
 	}
 	
 	/**
 	 * 跟新UI数据
 	 * @param Entity
 	 */
 	public void initInfomation(MerchantInformation Entity){
 		MerchantName.setText(Entity.getMchName());
 		industryName.setText(Entity.getIndustry());
 		//privance.setText(Entity.getPro());
 		city.setText(Entity.getPro()+Entity.getCity());
 		email.setText(Entity.getEmail());
        blankname.setText(Entity.getBankType());
 	    blank_web.setText(Entity.getBankName());
 		blank_user.setText(Entity.getBankAccName());
 		
 	}
 	
	
	public void initSppiner(){
		adapter = new ArrayAdapter<String>(this, R.layout.drop_list_ys, hangye);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		adapter.setDropDownViewResource(R.layout.drop_list_hover);
		hangye_sp.setAdapter(adapter);     
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 定义一个Handler，用于处理下载线程与UI间通讯
			switch (msg.what) {
			case 1 :
				String jsonData = (String) msg.obj;
				ParseMerchantInfomation(jsonData);
				break;
			case 2:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.getImfomation)
						,Toast.LENGTH_SHORT).show();
				break;
			case 3 :
				MerchantInformation entity = (MerchantInformation) msg.obj;
				initInfomation(entity);
			   break;
			case 4:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.getImfomation)
						,Toast.LENGTH_SHORT).show();
				break;
				
			case 5:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.getImfomation)
						,Toast.LENGTH_SHORT).show();
				
				break;
			
			default:
				break;
			}
		}
	};

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
