package com.cspaying.shanfu.ui.fragment;

import java.security.PublicKey;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.activity.FlowingWaterActivity;
import com.cspaying.shanfu.ui.activity.LoginActivity;
import com.cspaying.shanfu.ui.activity.OrderDetailsActivity;
import com.cspaying.shanfu.ui.adapter.OrderFlowAadapter;
import com.cspaying.shanfu.ui.entit.OrderDetail;
import com.cspaying.shanfu.ui.entit.Orderinquiry;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.DialogUtil;
import com.cspaying.shanfu.ui.utils.LoginUtils;
import com.cspaying.shanfu.ui.view.XListView;
import com.cspaying.shanfu.ui.view.XListView.IXListViewListener;

@SuppressLint("ValidFragment")
public class FragmentStatistics extends Fragment implements IXListViewListener{
	private TextView no_record;
	private XListView mListView;
	private Handler mHandler;
	private ArrayList<HashMap<String, Object>> dlist;
	private int currrentType = 0;
	
	private OrderFlowAadapter orderAdapter;
    private OrderFlowAadapter refoundAdapter;
    
    private ArrayList<OrderDetail> orderDtailList = new ArrayList<OrderDetail>();
    private ArrayList<OrderDetail> refoundList = new ArrayList<OrderDetail>();
	
    private FlowingWaterActivity activity; 
	public FragmentStatistics(){
		
	} 
	
   public FragmentStatistics(int type,FlowingWaterActivity ac){
	   currrentType = type;
	   activity = ac;
	}   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
		 //下拉刷新，上拉加载 
		no_record = (TextView) rootView.findViewById(R.id.no_record);
		
		dlist = new ArrayList<HashMap<String, Object>>();
		mListView = (XListView) rootView.findViewById(R.id.techan_xListView);// 你这个listview是在这个layout里面
		
		 initOrderlistView();
		 initData();
		return rootView;
	}
	
	public void initData(){
		Format format = new SimpleDateFormat("yyyyMMdd");
		 String dateString = format.format(new Date());
		if (currrentType == 0) {
			//initOrderListData();
			postJiaoyiliushui(dateString,"00");
			
		}else {
			postTuikuangliushui(dateString,"00");
			
		}
	}
	
	public void initOrderlistView(){
		
		
		mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		orderAdapter = new OrderFlowAadapter(getActivity(),orderDtailList);
		
		mListView.setAdapter(orderAdapter);
		mListView.setXListViewListener(this);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				OrderDetail entity = (OrderDetail) orderAdapter.getItem(arg2);
				String mchId = LoginUtils.getLoginMcid(getActivity());
				String outTradeNo = entity.getOutRradeNo();
				String type;
				if (currrentType ==0) {
					 type = "1";
				}else  {
					type = "3";
				}
				Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
				intent.putExtra(OrderDetailsActivity.mchIdKey, mchId);
				intent.putExtra(OrderDetailsActivity.outTradeNoKey, outTradeNo);
				intent.putExtra(OrderDetailsActivity.queryTypeKey, type);
				startActivity(intent);
			}
		});
		mHandler = new Handler();
	}
	
	
	
	/** 停止刷新， */
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		//mListView.setRefreshTime("刚刚");
	}
	
	private void onLoad1() {
		//mListView.stopRefresh();
		mListView.stopLoadMore();
		//mListView.setRefreshTime("刚刚");
	}
	@Override
	
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				try {
					getData();
					onLoad();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}, 100);
	}
	
	
	public void getData(){
		try {
			if (getActivity() != null) {
				String ateString =null;
				String num =activity.getmapState();
				String data = activity.getdate();
			    if (data != null) {
			    	ateString = data;
			   }else {
				   Date now = new Date();
					SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyyMMdd");
					 ateString = outDateFormat.format(now);
			    }
				
				
				if (currrentType ==0) {
					postJiaoyiliushui(ateString, num);
				}else {
					postTuikuangliushui(ateString, num);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 查询交易流水
	 */
	public void postJiaoyiliushui(String dateString,String transStatus){
		DialogUtil.getInstance(getActivity()).startProgressDialog(
				getActivity(), "");
		String loginInfo = LoginUtils.getLoginName(getActivity());
		String token = LoginUtils.getLoginToken(getActivity());
        String date = dateString.replaceAll("-", "");
		 
		 String jsonData = InitJson.getInstance(getActivity()).initTransactionFlow
				(loginInfo,token, "1", "cs.trade.dtl.query",date,
				"", "", transStatus, "1.0");
		HttpUtil.getInstance(getActivity()).reques(jsonData, HttpUtil.baseUrl, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				Message message = new Message();
				  if (statusCode == 0) {
					  message.what = 3;
					  message.obj = str;
				    }else {
				    	 message.what = 2;
					}
				  handler.sendMessage(message);
			}
		});
	}
	
	
	/**
	 * 查询退款流水
	 */
	public void postTuikuangliushui(String dateString,String status){
		String loginInfo = LoginUtils.getLoginName(getActivity());
		String token = LoginUtils.getLoginToken(getActivity());
        String date = dateString.replaceAll("-", "");
		 
		 String jsonData = InitJson.getInstance(getActivity()).initRefundFlow
				(loginInfo,token, "3", "cs.trade.dtl.query",date,
				"", "",status, "1.0");
		  
		
		HttpUtil.getInstance(getActivity()).reques(jsonData, HttpUtil.baseUrl, new OnRequestListener() {
			
			@Override
			public void onResult(int statusCode, String str) {
				// TODO Auto-generated method stub
				Message message = new Message();
				  if (statusCode == 0) {
					  message.what = 3;
					  message.obj = str;
				    }else {
				    	 message.what = 2;
					}
				  handler.sendMessage(message);
			}
		});
	}
	
	
	       
	public void paraseOrderList(String jsonData){
		Orderinquiry orderList = ParaseJson.getInstance(getActivity())
				    .ParaseOrderInquiry(jsonData);
		if (orderList != null) {
			Message message = new Message();
			if(orderList.getResultCode().equals("0")){
				message.obj = orderList;
				message.what = 4;
			}else {
				message.what = 5;
				
			}
			handler.sendMessage(message);
			
		}else {
			handler.sendEmptyMessage(6);
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				Toast.makeText(getActivity().getApplicationContext(),MyApplication.getContext()
						.getString(R.string.load_data_error)
						,Toast.LENGTH_SHORT).show();
				DialogUtil.getInstance(getActivity()).stopProgressDialog();
				break;
			case 3:
				String orderListStr = (String) msg.obj;
				paraseOrderList(orderListStr);
				DialogUtil.getInstance(getActivity()).stopProgressDialog();
			break;
			
			case 4:
				Orderinquiry orderentity = (Orderinquiry) msg.obj;
				float payeValue = orderentity.getTotalAmount();
				int num = orderentity.getTotalNum();
				activity.updateTitle(payeValue,num);
				
				orderDtailList.clear();
				if (orderentity.getDetail() != null) {
					orderDtailList = orderentity.getDetail();
					if (orderDtailList.size()>0) {
						
						no_record.setVisibility(View.GONE);
					}else {
						no_record.setVisibility(View.VISIBLE);
					}
				}else {
					Toast.makeText(getActivity().getApplicationContext(),MyApplication.getContext()
							.getString(R.string.load_data_error)
							,Toast.LENGTH_SHORT).show();
				}
				
				orderAdapter.updateOrderFlow(orderDtailList);
				
			break;

			default:
				break;
			}
		}
		
	};
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		onLoad1();
	}
}
