package com.cspaying.shanfu.ui.activity;

import java.util.ArrayList;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;



import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.adapter.Problem_ListAadapter;
import com.cspaying.shanfu.ui.entit.ProblemListEntity;
import com.cspaying.shanfu.ui.entit.problemEntity;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommonProblem extends BaseActivity{

	private CustomProgressDialog progressDialog;
	private TextView tvTitle,tvBack;
	private ListView problem_list;
	private Problem_ListAadapter adapter;
	private ArrayList<problemEntity> listdata = new ArrayList<problemEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_problem);
		MyApplication.getInstance().addActivity(this);
		initView();
		initList();
		initListData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		initTitle();
		problem_list = (ListView) findViewById(R.id.problem_list);
	}
	
	public void initList(){
		adapter = new  Problem_ListAadapter(this, listdata);
		problem_list.setAdapter(adapter);
	}
	
	public void initListData(){
		
		String version = MyApplication.getStrVersion();
		String jsondata = InitJson.getInstance(CommonProblem.this)
				.initProblem("cs.common.question",version);
		startProgressDialog();
		HttpUtil.getInstance(CommonProblem.this).reques(jsondata, HttpUtil.baseUrl
				, new OnRequestListener() {
					
					@Override
					public void onResult(int statusCode, String str) {
						// TODO Auto-generated method stub
						Message message = new Message();
						if (statusCode == 0) {
							message.what =1;
							message.obj = str;
						}else {
							message.what =2;
						}
						uIHandler.sendMessage(message);
					}
				});
	}
	
	public void initTitle(){
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("常见问题");
		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);
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
	
	private Handler uIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				String jsondata = msg.obj.toString();
				ProblemListEntity entity = ParaseJson.getInstance(CommonProblem.this)
						.ParaseProblemListEntity(jsondata);
				if (entity != null && entity.getDetail() != null) {
					ArrayList<problemEntity> detail = entity.getDetail();
					if (detail.size()>0) {
						listdata = detail;
						adapter.update(listdata);
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
			stopProgressDialog();
		}
		
	};
	
	
	 /**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(CommonProblem.this);
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
