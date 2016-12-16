package com.cspaying.shanfu.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.view.XListView;
import com.cspaying.shanfu.ui.view.XListView.IXListViewListener;

public class QueryOrderActivity extends BaseActivity implements IXListViewListener {
	private TextView tvBack;
	private XListView mListView;
	private SimpleAdapter mAdapter1;
	private Handler mHandler;
	private ArrayList<HashMap<String, Object>> dlist;
	
	/** 初始化本地数据 */
	String data[] = new String[] {};
	String data1[] = new String[] {};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_main);
		MyApplication.getInstance().addActivity(this);
		initView();
		initlistView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setOnClickListener(this);
		
		dlist = new ArrayList<HashMap<String, Object>>();
		mListView = (XListView)findViewById(R.id.techan_xListView);
	}
	
	public void initlistView(){
		
		
		mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		mAdapter1 = new SimpleAdapter(QueryOrderActivity.this, getData(),
				R.layout.list_items, new String[] { "name", "img", "content" },
				new int[] { R.id.title, R.id.mImage, R.id.content });
		mListView.setAdapter(mAdapter1);
		mListView.setXListViewListener(this);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(QueryOrderActivity.this,OrderDetailsActivity.class);
				startActivity(intent);
			}
		});
		mHandler = new Handler();
	}
	
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
				getData();
				mListView.setAdapter(mAdapter1);
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
				getData();
				mAdapter1.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
}
