package com.cspaying.shanfu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.entit.PasswordEntity;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.CustomProgressDialog;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class PasSafeActivity extends BaseActivity{

	private RelativeLayout layTitle;
	private TextView tvTitle,tvBack;
	private Button btnReg;
	private EditText et_usrPasSafeOldPas;
	private EditText et_usrRegInfoPas;
	private EditText et_usrRegInfoPasOk;
	private CustomProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pas_safe_main);
		MyApplication.getInstance().addActivity(this);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);
		
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("密码安全");
		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);
		
		et_usrPasSafeOldPas = (EditText) findViewById(R.id.et_usrPasSafeOldPas);
		et_usrRegInfoPas = (EditText) findViewById(R.id.et_usrRegInfoPas);
		et_usrRegInfoPasOk = (EditText) findViewById(R.id.et_usrRegInfoPas);
		
		btnReg = (Button) findViewById(R.id.btn_commit);
		btnReg.setOnClickListener(this);
	}
	
	public boolean check(String oldpass,String newpass,String surepass){
		boolean flag = false;
		if (oldpass != null && newpass != null && surepass != null) {
			if (!oldpass.equals("") && !newpass.equals("") && !surepass.equals("")) {
				if (newpass.equals(surepass)) {
					flag = true;
				}else {
					Toast.makeText(getApplicationContext(),MyApplication.getContext()
							.getString(R.string.new_old),Toast.LENGTH_SHORT)
							.show();
				}
				
			}
		}
		
		return flag;
	}
	
	public void postpass(String oldPass,String newPass){
		String token = LoginUtils.getLoginToken(PasSafeActivity.this);
		String logininfo = LoginUtils.getLoginName(PasSafeActivity.this);
		String jsondata = InitJson.getInstance(PasSafeActivity.this)
				.initChangePass("cs.mert.pass.modify", "1.0", token, logininfo
						, oldPass, newPass);
		//startProgressDialog();
		HttpUtil.getInstance(PasSafeActivity.this).reques(jsondata, HttpUtil.baseUrl
				, new OnRequestListener() {
					
					@Override
					public void onResult(int statusCode, String str) {
						// TODO Auto-generated method stub
						Message message = new Message();
						if (statusCode ==0) {
							message.what = 1;
							message.obj = str;
						}else {
							message.what = 2;
						}
						UIhandle.sendMessage(message);
						
					}
				});
	}
	
	
	private Handler UIhandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				String json = msg.obj.toString();
				PasswordEntity entity = ParaseJson.getInstance(PasSafeActivity.this)
						.Parasechangpass(json);
				if (entity != null) {
					String returnCode = entity.getReturnCode();
					if (returnCode.equals("0")) {
						Toast.makeText(getApplicationContext(),MyApplication.getContext()
								.getString(R.string.modify_pass_sucess),Toast.LENGTH_SHORT)
								.show();
						finish();
					}else {
						Toast.makeText(getApplicationContext(),MyApplication.getContext()
								.getString(R.string.modify_pass_fail),Toast.LENGTH_SHORT)
								.show();
					}
				}else {
					Toast.makeText(getApplicationContext(),MyApplication.getContext()
							.getString(R.string.modify_pass_fail),Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 2:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.modify_pass_fail),Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
			stopProgressDialog();
		}
		
	};
	
	/**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(PasSafeActivity.this);
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_commit:
			String oldpass = et_usrPasSafeOldPas.getText().toString();
			String newpas = et_usrRegInfoPas.getText().toString();
			String surepass = et_usrRegInfoPasOk.getText().toString();
			if (check(oldpass, newpas, surepass)) {
				postpass(oldpass, newpas);
			}else {
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.pass_fail),Toast.LENGTH_SHORT)
						.show();
			}

			
			break;
		case R.id.tv_left_back:
			finish();
			break;
		default:
			break;
		}
	}
}
