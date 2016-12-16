package com.cspaying.shanfu.ui.activity;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class PostTestActivity extends Activity implements OnClickListener {
	
	private Button shanghu_xinxi;//商户详情
	private Button jiayi_liushuo;//交易流水
	private Button tuikuang_shengqing;//退款申请
	private Button add_cashier;//增加收银员
	private Button qurey_cashier;//查询收银员信息
	private Button update_cashier;//修改收银员信息
	private Button delete_cashier;//删除收银员信息
	private Button cancellation_login;//注销登陆
	private Button modify_password;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_test);
		initView();
	}
	
	public void initView(){
		shanghu_xinxi = (Button) findViewById(R.id.shanghu_xinxi);
		shanghu_xinxi.setOnClickListener(this);
		jiayi_liushuo = (Button) findViewById(R.id.jiayi_liushuo);
		jiayi_liushuo.setOnClickListener(this);
		tuikuang_shengqing = (Button) findViewById(R.id.tuikuang_shengqing);
		tuikuang_shengqing.setOnClickListener(this);
		add_cashier = (Button) findViewById(R.id.add_cashier);
		add_cashier.setOnClickListener(this);
		qurey_cashier = (Button) findViewById(R.id.qurey_cashier);
		qurey_cashier.setOnClickListener(this);
		delete_cashier = (Button) findViewById(R.id.delete_cashier);
		delete_cashier.setOnClickListener(this);
		update_cashier = (Button) findViewById(R.id.update_cashier);
		update_cashier.setOnClickListener(this);
		cancellation_login = (Button) findViewById(R.id.cancellation_login);
		cancellation_login.setOnClickListener(this);
		modify_password = (Button) findViewById(R.id.modify_password);
		modify_password.setOnClickListener(this);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.shanghu_xinxi:
			postShanghuxinxi();
			break;
        case R.id.jiayi_liushuo:
        	 postJiaoyiliushui();
			break;
        case R.id.tuikuang_shengqing:
        	//tuikuanqingqiu();
        case R.id.add_cashier :
        	PostAdd_cashier();
        	break;
        case R.id.qurey_cashier :
        	PostQuret_cashier();
        	break;
        case R.id.delete_cashier :
        	PostDelete_cashier();
        	break;
        case R.id.update_cashier :
        	PostUpdate_cashier();
        	break;
        case R.id.cancellation_login :
        	PostCancel_login();
        	break;
        case R.id.modify_password :
        	//PostModify_password();
        	postyufu();
        	break;
		default:
			break;
		}
	}
	
	
	/**
	 * 请求商户信息
	 */
	public void postyufu(){
		
		
		String jsonData = InitJson.getInstance(PostTestActivity.this)
				.yufu();
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData, HttpUtil.baseUrl
				, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				  
			}
		});
		
	}
	
	/**
	 * 请求商户信息
	 */
	public void postShanghuxinxi(){
		
		String loginInfo = LoginUtils.getLoginName(PostTestActivity.this);
		String token = LoginUtils.getLoginToken(PostTestActivity.this);
		String jsonData = InitJson.getInstance(PostTestActivity.this)
				.initMerchantDetail(loginInfo,token, "cs.mert.info.query", "1.0");
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData, HttpUtil.baseUrl
				, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	/**
	 * 查询交易流水
	 */
	public void postJiaoyiliushui(){
		String loginInfo = LoginUtils.getLoginName(PostTestActivity.this);
		String token = LoginUtils.getLoginToken(PostTestActivity.this);

		 Format format = new SimpleDateFormat("yyyyMMddHHmmss");
		 String transEndTime = format.format(new Date());
		String jsonData = InitJson.getInstance(PostTestActivity.this).initTransactionFlow
				(loginInfo,token, "1", "cs.trade.dtl.query","20160706",
				transEndTime, "", "01", "1.0");
		
		
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData, HttpUtil.baseUrl, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				  
			}
		});
	}
	
	
	/**
	 * 退款申请
	 */
	/*public void tuikuanqingqiu(){
		String loginInfo = LoginUtils.getLoginName(PostTestActivity.this);
		String token = LoginUtils.getLoginToken(PostTestActivity.this);

		String jsonData = InitJson.getInstance(PostTestActivity.this)
				.initRefund("0.1", "weixin", "退款申请", "000030001000001"
						, "01", "01", "cs.refund.apply", "1.2");
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}
		});
	}*/
	
	/**
	 * 增加收银员
	 */
	public void PostAdd_cashier(){
		String token = LoginUtils.getLoginToken(PostTestActivity.this);
		String jsonData = InitJson.getInstance(PostTestActivity.this)
			.initAddCashier("cs.mert.cashier.add", "1.0", token, "ZZH2","77642"
					, "ZZH-TEST2", "1","1","1","1");
		
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
			}
		});
		
		
	}
	
	/**
	 * 查询收银员信息
	 */
	public void PostQuret_cashier(){
		String token = LoginUtils.getLoginToken(PostTestActivity.this);
		String jsonData = InitJson.getInstance(PostTestActivity.this)
			.initQureyCashier("cs.mert.cashier.query", "1.0", token);
		
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	/**
	 * 删除收银员信息
	 */
	public void PostDelete_cashier(){
		String token = LoginUtils.getLoginToken(PostTestActivity.this);
		String jsonData = InitJson.getInstance(PostTestActivity.this)
			.initDeleteCashier("cs.mert.cashier.del", "1.0", token,"77642@000050000000002");
		
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	/**
	 * 修改收银员信息
	 */
	public void PostUpdate_cashier(){
		String token = LoginUtils.getLoginToken(PostTestActivity.this);
		String jsonData = InitJson.getInstance(PostTestActivity.this)
			.initUpdateCashier("cs.mert.cashier.modify", "1.0", token,"13428777642@000050000000002"
			,"XIUGAI","13428777642","XIUGAI-TEST","1","1","1","1");
		
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	/**
	 *注销登陆
	 */
	public void PostCancel_login(){
		String loginInfo = LoginUtils.getLoginName(PostTestActivity.this);
		String token = LoginUtils.getLoginToken(PostTestActivity.this);
		String jsonData = InitJson.getInstance(PostTestActivity.this)
			.Cancellation_login("cs.mert.logout", "1.0",loginInfo
			,token);
		
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 *修改密码
	 */
	public void PostModify_password(){
		String loginInfo = LoginUtils.getLoginName(PostTestActivity.this);
		String token = LoginUtils.getLoginToken(PostTestActivity.this);
		String jsonData = InitJson.getInstance(PostTestActivity.this)
			.Modify_Password("cs.mert.pass.modify", "1.0", token, loginInfo
					, "777777", "666666");
		
		HttpUtil.getInstance(PostTestActivity.this).reques(jsonData,HttpUtil.baseUrl, 
				new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	

}
