package com.cspaying.shanfu.ui.activity;

import java.security.PublicKey;
import java.util.ArrayList;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;



import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.adapter.CashierListAdapter;
import com.cspaying.shanfu.ui.entit.CashierDetailEntity;
import com.cspaying.shanfu.ui.entit.CashierEntity;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CashierMangerActivity extends BaseActivity{

	private RelativeLayout layTitle;
	private TextView tvTitle,tvBack;
	private ImageView right_img;
	private ListView cashier_list;
	private CashierListAdapter listAdapter;
	private ArrayList<CashierDetailEntity> listData = new ArrayList<CashierDetailEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_manger);
		MyApplication.getInstance().addActivity(this);
		initView();
		initList();
		initListData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);
		
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("收银员管理");
		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);
		right_img = (ImageView) findViewById(R.id.right_img);
		right_img.setVisibility(View.VISIBLE);
		right_img.setImageResource(R.drawable.icon_add);
		right_img.setOnClickListener(this);
		
		cashier_list = (ListView) findViewById(R.id.cashier_list);
		
	}
	
	public void initList(){
		listAdapter = new CashierListAdapter(this,listData);
		cashier_list.setAdapter(listAdapter);
	}
	
	public void initListData(){
		
		String version = MyApplication.getStrVersion();
		String token = LoginUtils.getLoginToken(this);
		String jsonData = InitJson.getInstance(this).queryCashier("cs.mert.cashier.query"
				, version, token);
		HttpUtil.getInstance(this).reques(jsonData, HttpUtil.baseUrl, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				Message message = new Message();
               if (statusCode == 0) {
            	    message.obj = str;
            	    message.what = 1;
					Log.e("++++++++++++++++++++=OnRequestListener", str);
				}else {
					message.what = 2;
				}
				UIHandler.sendMessage(message);
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_img:
			Intent intent = new Intent(CashierMangerActivity.this,CashierActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_left_back:
			CashierMangerActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	private Handler UIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				String str = msg.obj.toString();
				CashierEntity entity = ParaseJson.getInstance(CashierMangerActivity.this)
						.ParaseCashier(str);
				ArrayList<CashierDetailEntity> detail = entity.getDetail();
				if (detail != null) {
					if (detail.size()>0) {
						listData = detail;
						listAdapter.updateOrderFlow(listData);
					}else {
						Toast.makeText(getApplicationContext(),MyApplication.getContext()
								.getString(R.string.no_more_data),Toast.LENGTH_SHORT)
								.show();
					}
				}else {
					Toast.makeText(getApplicationContext(),MyApplication.getContext()
							.getString(R.string.no_more_data),Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 2:
				Toast.makeText(getApplicationContext(),MyApplication.getContext()
						.getString(R.string.no_more_data),Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
			
		}
		
	};
}
