package com.cspaying.shanfu.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.json.Json;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.adapter.Statistics2FragmentAadapter;
import com.cspaying.shanfu.ui.entit.NameValue;
import com.cspaying.shanfu.ui.entit.Statistics1Entity;
import com.cspaying.shanfu.ui.entit.Statistics2Entity;
import com.cspaying.shanfu.ui.jsondata.InitSignString;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.LoginUtils;
import com.cspaying.shanfu.ui.view.XListView;
import com.cspaying.shanfu.ui.view.XListView.IXListViewListener;

@SuppressLint("ValidFragment")
public class Statistics2Fragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, IXListViewListener {
	// private SimpleAdapter mAdapter1;
	// private ArrayList<HashMap<String, Object>> dlist;
	private int currrentType = 7;
	private String currrentGroupType = "01"; // 终端类型 支付类型 收银员
	private ArrayList<Statistics2Entity> mList = new ArrayList<Statistics2Entity>();
	private Statistics2FragmentAadapter adapter;

	// WebView mWebView;

	private XListView mListView;
	private Handler mHandler;
	Button btnWeek;
	Button btnMonth;
	Spinner spinner;

	public Statistics2Fragment() {

	}

	public Statistics2Fragment(Handler handler) {
		mHandler = handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_statistics2, container,
				false);

		btnWeek = (Button) view.findViewById(R.id.btn_week);
		btnMonth = (Button) view.findViewById(R.id.btn_month);
		btnWeek.setOnClickListener(this);
		btnMonth.setOnClickListener(this);

		spinner = (Spinner) view.findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);

		initListViewData();
		initListView(view);
		return view;
	}

	public void initListViewData() {
		getData(String.valueOf(currrentType), currrentGroupType);
		// for (int i = 0; i < 15; i++) {
		// Statistics1Entity entity = new Statistics1Entity();
		// entity.setTotalAmount(0.39);
		// entity.setTotalNum(39);
		// mList.add(entity);
		// }
	}

	public void initListView(View rootView) {
		mListView = (XListView) rootView.findViewById(R.id.techan_xListView);// 你这个listview是在这个layout里面
		adapter = new Statistics2FragmentAadapter(getActivity(), mList);
		mListView.setPullLoadEnable(false);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		mListView.setAdapter(adapter);
		mListView.setXListViewListener(this);
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
		getData(String.valueOf(currrentType), currrentGroupType);
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Runnable() {
			@Override
			public void run() {
				// initListViewData();// getData();
				getData(String.valueOf(currrentType), currrentGroupType);
				adapter.notifyDataSetChanged();
				onLoad();
			}
		};
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_week: {

			btnWeek.setBackgroundColor(getResources().getColor(R.color.orange));
			btnMonth.setBackgroundColor(getResources().getColor(
					R.color.grass_green));
			currrentType = 7;
			getData(String.valueOf(currrentType), currrentGroupType);
			break;
		}
		case R.id.btn_month: {
			btnWeek.setBackgroundColor(getResources().getColor(
					R.color.grass_green));
			btnMonth.setBackgroundColor(getResources().getColor(R.color.orange));
			currrentType = 30;
			getData(String.valueOf(currrentType), currrentGroupType);
			break;
		}
		default: {
			break;
		}

		}
	}

	/**
	 * 定期交易分布查询
	 */
	public void getData(String days, String type) {// 7 30
		String logOnInfo = LoginUtils.getLoginName(getActivity());
		String token = LoginUtils.getLoginToken(getActivity());
		String signKey = LoginUtils.getSignKey(getActivity());

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tradeType", "cs.trade.regular.dis.query");
			jsonObject.put("version", "1.0");
			jsonObject.put("logOnInfo", logOnInfo);
			jsonObject.put("token", token);
			jsonObject.put("groupType", type);
			jsonObject.put("queryRegularType", days);
			String sign = InitSignString.getSign(jsonObject, signKey);
			jsonObject.put("sign", sign);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String jsonData = jsonObject.toString();
		Log.d("postData", jsonData);
		HttpUtil.getInstance(getActivity()).reques(jsonData, HttpUtil.baseUrl,
				new OnRequestListener() {
					@Override
					public void onResult(int statusCode, String result) {
						Log.d("getData", result);
						// TODO Auto-generated method stub
						Message message = new Message();
						if (statusCode == 0) {
							message.what = 4;
							message.obj = result;
							// paraseData(result);
						} else {
							message.what = 2;
						}
						mHandler.sendMessage(message);
					}
				});
	}

	public void paraseData(String jsonData) {
		List<Statistics2Entity> list = ParaseJson.getInstance(getActivity())
				.ParasepostStatistics2(jsonData);
		ArrayList<Statistics2Entity> alist = null;
		if (list != null) {
			 alist = new ArrayList<Statistics2Entity>(list);
		}
		
		// Log.d("paraseData list size :", list.size() + "");
		if (list != null) {
			Message message = new Message();
			message.obj = alist;
			message.what = 5;
			mHandler.sendMessage(message);
		} else {
			mHandler.sendEmptyMessage(4);
		}
	}

	public void refresh(ArrayList<Statistics2Entity> list) {
		// Log.d("refresh adapter", " " + mList.size());
		adapter.refresh(list);
		// adapter.notifyDataSetChanged();
	}

	public Handler getmHandler() {
		return mHandler;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public ArrayList<Statistics2Entity> getmList() {
		return mList;
	}

	public void setmList(ArrayList<Statistics2Entity> mList) {
		this.mList = mList;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int post, long id) {
		String[] items = getResources().getStringArray(
				R.array.select_type_items);
		// TODO Auto-generated method stub
		switch (post) {
		case 0:
			currrentGroupType = "01";
			break;
		case 1:
			currrentGroupType = "02";
			break;
		case 2:
			currrentGroupType = "03";
			break;
		default:
			currrentGroupType = "01";
			break;
		}
		getData(String.valueOf(currrentType),currrentGroupType);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
