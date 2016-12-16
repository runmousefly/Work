package com.cspaying.shanfu.ui.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.entit.Re_LoginEntity;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.InitSignString;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.DialogUtil;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class LoginActivity extends BaseActivity {

	//public final static String Loginkey = "91be991a7491481ab43a89657a780b69";
	//public final static String Loginkey = "6a450ed47196457ea03b58b46edfc442";
	private Button btnLogin, btnForgetPas;
	private ImageView tv_remenberPas_img;
	private TextView tv_remenberPas;
	private TextView tvForgetPas;
	private Intent intent;
	private EditText et_usrePhone;
	private EditText et_usrePas;
	private String userPhone;
	private String userPass;
	private Re_LoginEntity reLoginEntity;
	private boolean remenberPass = true;

	private boolean DEBUG = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		MyApplication.getInstance().addActivity(this);
		if (isLogin()) {
			intent = new Intent(LoginActivity.this, MainTabActivity.class);
			startActivity(intent);
			finish();
		}
		initView();
	}

	/**
	 * 判断是否已登录
	 */
	public boolean isLogin() {
		return LoginUtils.getLoginFlag(LoginActivity.this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_remenberPas_img = (ImageView) findViewById(R.id.tv_remenberPas_img);
		tv_remenberPas_img.setOnClickListener(this);

		tv_remenberPas = (TextView) findViewById(R.id.tv_remenberPas);
		tv_remenberPas.setOnClickListener(this);

		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);

		tvForgetPas = (TextView) findViewById(R.id.tv_usrForgetPas);
		tvForgetPas.setOnClickListener(this);

		et_usrePhone = (EditText) findViewById(R.id.et_usrePhone);
		et_usrePhone.setOnClickListener(this);
		et_usrePas = (EditText) findViewById(R.id.et_usrePas);
		et_usrePas.setOnClickListener(this);
		if(DEBUG)
		{
			et_usrePhone.setText("000140000000000003");
			et_usrePas.setText("777777");
		}
		
		int rememberPassFlag = LoginUtils.getRemenberpass(LoginActivity.this);
		if(rememberPassFlag == 1)
		{
			tv_remenberPas_img
			.setImageResource(R.drawable.box_select_choose_01);
		}
		else if(rememberPassFlag == 0)
		{
			tv_remenberPas_img
			.setImageResource(R.drawable.box_select_default_01);
		}
		else 
		{
			tv_remenberPas_img
			.setImageResource(R.drawable.box_select_choose_01);
			LoginUtils.setRemenberPass(LoginActivity.this, 1);
		}
		//et_usrePas.setText("6a50cd7a190047dcb15cfcc8b5eefcf8");
		//et_usrePhone.setText("000010000000001");
		
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		initAccount();
	}

	public void initAccount(){
		String account = LoginUtils.getAccount(LoginActivity.this);
		if (account != null && !account.equals("")) {
			et_usrePhone.setText(account); 
		}
		if (LoginUtils.getRemenberpass(LoginActivity.this) == 1) {
			 String passwordString = LoginUtils.getpass(LoginActivity.this);
			 if (passwordString != null && !passwordString.equals("")) {
				 et_usrePas.setText(passwordString);
			}
			
		}
		
	}

	Handler UIhandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int what = msg.what;
			super.handleMessage(msg);
			switch (what) {
			case 1:
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.user_account_null), Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.user_password_null),
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.send_message_error),
						Toast.LENGTH_SHORT).show();
				DialogUtil.getInstance(LoginActivity.this).stopProgressDialog();
				break;
			case 4:
				String errorMessage = msg.obj.toString();
				Toast.makeText(getApplicationContext(), errorMessage,
						Toast.LENGTH_SHORT).show();
				DialogUtil.getInstance(LoginActivity.this).stopProgressDialog();
				break;
			case 5:// 登陆成功
				Re_LoginEntity entity = (Re_LoginEntity) msg.obj;
				String token = entity.getToken();// 登陆返回的Token
				String mcid = entity.getMchId();
				String signKey = entity.getSignKey();
				LoginUtils.setLoginToken(LoginActivity.this, token);// 保存登陆Token
				LoginUtils.setMcId(LoginActivity.this, mcid);
				String logininfo = entity.getLogOnInfo();// 登陆账号
				LoginUtils.setLoginName(LoginActivity.this, logininfo);// 保存登陆账号
				LoginUtils.setLoginFlag(LoginActivity.this, 1);// 保存登陆账号
				LoginUtils.setSignKey(LoginActivity.this, signKey);
				setisRemenberPass(userPass,userPhone);
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.login_sucess), Toast.LENGTH_SHORT)
						.show();
				DialogUtil.getInstance(LoginActivity.this).stopProgressDialog();
				intent = new Intent(LoginActivity.this, MainTabActivity.class);
				startActivity(intent);
				MyApplication.getInstance().onLoginSuccess();
				finish();
				break;
			case 6:
				Toast.makeText(
						getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.packet_inspection_error),
						Toast.LENGTH_SHORT).show();
				DialogUtil.getInstance(LoginActivity.this).stopProgressDialog();
				break;
			case 7:
				String jsonStr = msg.obj.toString();
				ParseReLogin(jsonStr);
				break;

			default:

				break;
			}

		}

	};

	
	public void setisRemenberPass(String pass,String account){
		int rememberPass = LoginUtils.getRemenberpass(LoginActivity.this);
		LoginUtils.setAccount(LoginActivity.this, account);
		if (rememberPass == 1) {
			LoginUtils.setPass(LoginActivity.this, pass);
		}
		else{
			LoginUtils.setPass(LoginActivity.this, "");
		}
			
	}
	/**
	 * 检查登陆输入信息
	 */
	public boolean checkLoginMessage() {
		boolean flag = false;
		Message mess = new Message();
		if (TextUtils.isEmpty(userPhone)) {
			mess.what = 1;
		} else if (TextUtils.isEmpty(userPass)) {
			mess.what = 2;
		} else {
			flag = true;
		}

		return flag;
	}

	/**
	 * 解析登陆请求返回的JSON字符串
	 * 
	 * @param jsonData
	 */
	public void ParseReLogin(String jsonData) {
		reLoginEntity = ParaseJson.getInstance(LoginActivity.this)
				.ParaseLoginGetData(jsonData);
		if (reLoginEntity != null) {
			Message message = new Message();
			if (reLoginEntity.getResultCode().equals("0")) {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("token", reLoginEntity.getToken());
				hashMap.put("returnCode", reLoginEntity.getReturnCode());
				hashMap.put("resultCode", reLoginEntity.getResultCode());
				hashMap.put("logOnInfo", reLoginEntity.getLogOnInfo());
				hashMap.put("mchId", reLoginEntity.getMchId());
				hashMap.put("signKey", reLoginEntity.getSignKey());

				Log.e("==========Map", hashMap.toString());

				// String sign = InitSignString.getSign(hashMap,
				// LoginActivity.Loginkey);
				LoginUtils.setSignKey(LoginActivity.this,
						reLoginEntity.getSignKey());
				 message.what = 5;
				 message.obj = reLoginEntity;
				// Log.e("++sign equals", sign+" : "+reLoginEntity.getSign());
				// if (!sign.equals(reLoginEntity.getSign())) {
				// message.what = 5;
				// message.obj = reLoginEntity;
				// }else {
				// message.what = 6;
				// }
			} else {
				String errorStr = reLoginEntity.getErrCodeDes();
				message.what = 4;
				message.obj = errorStr;

			}
			UIhandle.sendMessage(message);

		} else {
			UIhandle.sendEmptyMessage(3);
		}
	}

	/**
	 * 登陆网络请求
	 * 
	 * @param jsonData
	 */
	public void PostLogin(String jsonData) {
		DialogUtil.getInstance(LoginActivity.this).startProgressDialog(
				LoginActivity.this, "");
		HttpUtil.getInstance(LoginActivity.this).reques(jsonData,
				HttpUtil.baseUrl, new OnRequestListener() {
					@Override
					public void onResult(int statusCode, String str) {
						// TODO Auto-generated method stub
						Message message = new Message();
						if (statusCode == 0) {
							message.what = 7;
							message.obj = str;
							Log.e("++++++++++++++++++++=OnRequestListener", str);
						} else {
							message.what = 3;
						}
						UIhandle.sendMessage(message);
					}
				});
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_login:
			userPhone = et_usrePhone.getText().toString();
			userPass = et_usrePas.getText().toString();
			if (checkLoginMessage()) {
				String jsondataString = InitJson.getInstance(this).initLogin(
						"cs.mert.info.verify", "1.0", userPhone, userPass, "");
				PostLogin(jsondataString);
				Log.e("+++++++++++++++jsondataString:", jsondataString);

			}

			break;  

		case R.id.tv_usrForgetPas:
			intent = new Intent(LoginActivity.this, ForgetPasActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_remenberPas_img:
			updateRemembePassStatus(!remenberPass);
			break;
		case R.id.tv_remenberPas:
			updateRemembePassStatus(!remenberPass);
			break;
		case R.id.et_usrePhone:

			break;

		case R.id.et_usrePas:

			break;

		default:
			break;
		}
	}

	private void updateRemembePassStatus(boolean bRememberPass)
	{
		if(bRememberPass != remenberPass)
		{
			remenberPass = bRememberPass;
			if (remenberPass) {
				tv_remenberPas_img
				.setImageResource(R.drawable.box_select_choose_01);
				LoginUtils.setRemenberPass(LoginActivity.this, 1);
			} else {
				tv_remenberPas_img
				.setImageResource(R.drawable.box_select_default_01);
				LoginUtils.setRemenberPass(LoginActivity.this, 0);
			}
		}
	}
}
