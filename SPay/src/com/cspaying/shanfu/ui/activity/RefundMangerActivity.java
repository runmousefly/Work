package com.cspaying.shanfu.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.adapter.OrderFlowAadapter;
import com.cspaying.shanfu.ui.entit.OrderDetail;
import com.cspaying.shanfu.ui.entit.Orderinquiry;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.LoginUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class RefundMangerActivity extends Activity {
    private ListView refund_listview;
    private OrderFlowAadapter refoundAdapter;
    private ArrayList<OrderDetail> refoundList = new ArrayList<OrderDetail>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refund);
		
		initListView();
		postJiaoyiliushui();
	}
	
	public void initListView(){
		refund_listview = (ListView) findViewById(R.id.refund_listview);
		refoundAdapter = new OrderFlowAadapter(this,refoundList);
		refund_listview.setAdapter(refoundAdapter);
	}
	
	/**
	 * 查询交易流水
	 */
	public void postJiaoyiliushui(){
		String loginInfo = LoginUtils.getLoginName(RefundMangerActivity.this);
		String token = LoginUtils.getLoginToken(RefundMangerActivity.this);
		
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String date = formatter.format(currentTime);
		 
		 String jsonData = InitJson.getInstance(RefundMangerActivity.this).initTransactionFlow
				(loginInfo,token, "1", "cs.trade.dtl.query",date,
				"", "", "2", "1.0");
		HttpUtil.getInstance(RefundMangerActivity.this)
		                 .reques(jsonData, HttpUtil.baseUrl, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				Message message = new Message();
				  if (statusCode == 0) {
					  message.what = 1;
					  message.obj = str;
				    }else {
				    	 message.what = 2;
					}
				  UIhandle.sendMessage(message);
			}
		});
	}
	
	
	public void paraseOrderList(String jsonData){
		Orderinquiry orderList = ParaseJson.getInstance(RefundMangerActivity.this)
				    .ParaseOrderInquiry(jsonData);
		if (orderList != null) {
			Message message = new Message();
			if(orderList.getResultCode().equals("0")){
				message.obj = orderList;
				message.what = 4;
			}else {
				message.what = 5;
				
			}
			UIhandle.sendMessage(message);
			
		}else {
			UIhandle.sendEmptyMessage(6);
		}
	}
	
	private Handler UIhandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				String jsondata = msg.obj.toString();
				paraseOrderList(jsondata);
				break;
			case 2:
				
				break;
			case 4:
				Orderinquiry orderList = (Orderinquiry) msg.obj;
				if (orderList != null) {
					refoundList =  orderList.getDetail();
					refoundAdapter.updateOrderFlow(refoundList);
				}
				break;
			case 5:
				
				break;
			case 6:
				
				break;

			default:
				break;
			}
			
			
		}
		
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	

}
