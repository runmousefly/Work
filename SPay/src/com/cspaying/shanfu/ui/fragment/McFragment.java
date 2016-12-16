package com.cspaying.shanfu.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.adapter.McFragmentAadapter;
import com.cspaying.shanfu.ui.entit.McOrderEntity;
import com.cspaying.shanfu.ui.view.XListView;
import com.cspaying.shanfu.ui.view.XListView.IXListViewListener;

@SuppressLint("ValidFragment")
public class McFragment extends Fragment implements IXListViewListener{
	private XListView mListView;
	private SimpleAdapter mAdapter1;
	private Handler mHandler;
	private ArrayList<HashMap<String, Object>> dlist;
	private int currrentType = 0;
	private ArrayList<McOrderEntity> mcOrderList = new ArrayList<McOrderEntity>();
	private McFragmentAadapter adapter;
	
	public McFragment(){
		
	} 
	
   public McFragment(int type){
	   currrentType = type;
	} 
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mc, container, false);
		 //下拉刷新，上拉加载 
		/*dlist = new ArrayList<HashMap<String, Object>>();
		mListView = (XListView) rootView.findViewById(R.id.techan_xListView);// 你这个listview是在这个layout里面
		mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		mAdapter1 = new SimpleAdapter(getActivity(), getData(),
				R.layout.list_items, new String[] { "name", "img", "content" },
				new int[] { R.id.title, R.id.mImage, R.id.content });
		mListView.setAdapter(mAdapter1);
		mListView.setXListViewListener(this);
		mHandler = new Handler();*/
		initListViewData();
		initListView(rootView);
		return rootView;
	}
	
	public void initListViewData(){
		
		for (int i = 0; i < 15; i++) {
			McOrderEntity entity = new McOrderEntity();
			entity.setLogOnInfo("7551000001");
			entity.setOutChannelNo((i%3)+"");
			entity.setOutTradeNo("40832101216283"+i);
			entity.setPostscript("test");
			entity.setStatus("1");
			entity.setTotalAmount("0.39");
			entity.setTotalNum("39");
			entity.setTransTime("9999999");
			mcOrderList.add(entity);
			
		}
		
	}
	
	public void initListView(View rootView){
		mListView = (XListView) rootView.findViewById(R.id.techan_xListView);// 你这个listview是在这个layout里面
		adapter = new McFragmentAadapter(getActivity(), mcOrderList);
		mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		mListView.setAdapter(adapter);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
	}
	


	
	/** 初始化本地数据 */
	String data[] = new String[] { "408321012162838", "408321012162839", "408321012162840",
			"408321012162841", "408321012162842" };
	String data1[] = new String[] { "2016-06-25  19:19:30", "2016-06-25  19:19:30", "2016-06-25  19:19:30",
			"2016-06-25  19:19:30", "2016-06-25  19:19:30" };

	private ArrayList<HashMap<String, Object>> getData() {
		for (int i = 0; i < data.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", data[i]);
			map.put("content", data1[i]);
			map.put("img", R.drawable.icon_list_wechat);
			dlist.add(map);
		}
		return dlist;
	}
	
	/** 停止刷新， */
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				//getData();
				//mListView.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				initListViewData();//getData();
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
		}
		return false;
	}*/
}
