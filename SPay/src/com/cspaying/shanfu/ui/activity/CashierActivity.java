package com.cspaying.shanfu.ui.activity;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;



import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.CustomProgressDialog;
import com.cspaying.shanfu.ui.utils.LoginUtils;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CashierActivity extends BaseActivity{

	private RelativeLayout layTitle;
	private TextView tvTitle,tvBack;
	private Button btnSave;
	private CheckBox  cbRefund,cbFlowingWater,cbOpenAndCloseManager,cb_tongji;
	private TextView tvRefundState,tvFlowingWaterState,tvOpenAndCloseManagerState,cb_tongji_state;
	private RelativeLayout  layRefund,layFlowingWater,layOpenAndCloseManager;
	private EditText et_usrCashierPhone;
	private EditText et_usrCashierName;
	private EditText et_usrCashierShop;
	private CustomProgressDialog progressDialog;
	private String refund = "2";
	private String flow = "2";
	private String dongjie = "2";
	private String tongji = "2";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_main);
		MyApplication.getInstance().addActivity(this);
		initView();
		addListener();
	}
	private void addListener() {
		// TODO Auto-generated method stub
		tvBack.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		cbRefund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	refund = "1";
                	tvRefundState.setText("已开启"); 
                }else{ 
                	refund = "2";
                	tvRefundState.setText("未开启"); 
                } 
            } 
        });
		
		cbFlowingWater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	flow = "1";
                	tvFlowingWaterState.setText("已开启"); 
                }else{ 
                	flow = "2";
                	tvFlowingWaterState.setText("未开启"); 
                } 
            } 
        });
		cbOpenAndCloseManager.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	dongjie = "1";
                	tvOpenAndCloseManagerState.setText("已开启"); 
                }else{ 
                	dongjie = "2";
                	tvOpenAndCloseManagerState.setText("未开启"); 
                } 
            } 
        });
		
		cb_tongji.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					tongji = "1";
					cb_tongji_state.setText("已开启");
				}else {
					tongji = "2";
					cb_tongji_state.setText("未开启");
				}
			}
		});
		
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		
		et_usrCashierPhone = (EditText) findViewById(R.id.et_usrCashierPhone);
		et_usrCashierName = (EditText) findViewById(R.id.et_usrCashierName);
		et_usrCashierShop = (EditText) findViewById(R.id.et_usrCashierShop);
		
		
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);
		
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("收银员添加");
		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
	
		
		cbRefund = (CheckBox) findViewById(R.id.cb_refund);
		cbFlowingWater = (CheckBox) findViewById(R.id.cb_flowing_water);
		cbOpenAndCloseManager = (CheckBox) findViewById(R.id.cb_open_and_close_manager);
		cb_tongji = (CheckBox) findViewById(R.id.cb_tongji);
		
		tvRefundState = (TextView) findViewById(R.id.tv_refund_state);
		tvFlowingWaterState = (TextView) findViewById(R.id.tv_flowing_water_state);
		tvOpenAndCloseManagerState = (TextView) findViewById(R.id.tv_open_and_close_manager_state);
		cb_tongji_state = (TextView) findViewById(R.id.cb_tongji_state);
		
		btnSave = (Button) findViewById(R.id.btn_commit);
		btnSave.setOnClickListener(this);
		
	}
	
	/**
	 * 增加收银员
	 */
	public void PostAdd_cashier(String phone,String name,String shop){
		String token = LoginUtils.getLoginToken(CashierActivity.this);
		String jsonData = InitJson.getInstance(CashierActivity.this)
			.initAddCashier("cs.mert.cashier.add", "1.0", token,name,phone
					,shop,refund,flow,tongji,dongjie);
		startProgressDialog();
		HttpUtil.getInstance(CashierActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				Message message = new Message();
				if (statusCode == 0) {
					message.what=1;
					message.obj = str;
				}else {
					message.what=2;
				}
				uIHandler.sendMessage(message);
				Log.e("+++++++++++++++str:", ""+str);
			}
		});
		
		
	}
	
	public boolean check(String phone,String name,String shop){
		boolean flag = false;
		if (phone != null && name != null && shop != null) {
			if (!phone.equals("") && !name.equals("") && !shop.equals("")) {
				flag = true;
			}
		}
		
		return flag;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_commit:
			String phone = et_usrCashierPhone.getText().toString();
			String name = et_usrCashierName.getText().toString();
			String shop = et_usrCashierShop.getText().toString();
			if (check(phone, name, shop)) {
				PostAdd_cashier(phone, name, shop);
			}else {
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.cashier_null),Toast.LENGTH_SHORT)
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
	
	private Handler uIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				String jsondata = msg.obj.toString();
				ReAddCashier entity = ParaseJson.getInstance(CashierActivity.this)
						.ParaseReAddCashier(jsondata);
				if (entity != null) {
					String returnCode = entity.getReturnCode();
					if (returnCode.equals("0")) {
						Toast.makeText(getApplicationContext(),MyApplication.getContext()
								.getString(R.string.add_cashier_sucess),Toast.LENGTH_SHORT)
								.show();
						stopProgressDialog();
						finish();
					}else {
						stopProgressDialog();
						Toast.makeText(getApplicationContext(),MyApplication.getContext()
								.getString(R.string.add_cashier_fail),Toast.LENGTH_SHORT)
						.show();
					}
				}else {
					stopProgressDialog();
					Toast.makeText(getApplicationContext(),MyApplication.getContext()
							.getString(R.string.add_cashier_fail),Toast.LENGTH_SHORT)
					.show();
				}
				break;
			case 2:
				stopProgressDialog();
				  Toast.makeText(getApplicationContext(),MyApplication.getContext()
							.getString(R.string.add_cashier_fail),Toast.LENGTH_SHORT)
					.show();
				break;

			default:
				break;
			}
		}
		
	};
	
	 /**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(CashierActivity.this);
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
