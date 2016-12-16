package com.cspaying.shanfu.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.activity.LoginActivity;
import com.cspaying.shanfu.ui.adapter.Statistics1FragmentAadapter;
import com.cspaying.shanfu.ui.entit.McOrderEntity;
import com.cspaying.shanfu.ui.entit.Orderinquiry;
import com.cspaying.shanfu.ui.entit.Statistics1Entity;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.InitSignString;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.DateUtil;
import com.cspaying.shanfu.ui.utils.LoginUtils;
import com.cspaying.shanfu.ui.view.XListView;
import com.cspaying.shanfu.ui.view.XListView.IXListViewListener;

@SuppressLint("ValidFragment")
public class Statistics1Fragment extends Fragment implements OnClickListener,
		IXListViewListener {
	private int currrentType = 7;
	private ArrayList<Statistics1Entity> mList = new ArrayList<Statistics1Entity>();
	private Statistics1FragmentAadapter adapter;
	private XListView mListView;
	private Handler mHandler;
	Button btnWeek;
	Button btnMonth;
	WebView mWebView;
	
	TextView rightTitle;

	public Statistics1Fragment() {

	}

	public Statistics1Fragment(Handler handler) {
		mHandler = handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_statistics1, container,
				false);
		
		rightTitle = (TextView) view.findViewById(R.id.right_title);
		btnWeek = (Button) view.findViewById(R.id.btn_week);
		btnMonth = (Button) view.findViewById(R.id.btn_month);
		btnWeek.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		rightTitle.setOnClickListener(this);

		initListView(view);
		initWebView(view);
		return view;
	}

	public void initListViewData() {
		getData("7");
		// for (int i = 0; i < 15; i++) {
		// Statistics1Entity entity = new Statistics1Entity();
		// entity.setTotalAmount(0.39);
		// entity.setTotalNum(39);
		// mList.add(entity);
		// }
	}

	public void initListView(View rootView) {
		mListView = (XListView) rootView.findViewById(R.id.techan_xListView);// 你这个listview是在这个layout里面
		adapter = new Statistics1FragmentAadapter(getActivity(), mList);
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
		getData(String.valueOf(currrentType));
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Runnable() {
			@Override
			public void run() {
				// initListViewData();// getData();
				getData(String.valueOf(currrentType));
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
			getData("7");
			break;
		}
		case R.id.btn_month: {
			btnWeek.setBackgroundColor(getResources().getColor(
					R.color.grass_green));
			btnMonth.setBackgroundColor(getResources().getColor(R.color.orange));
			currrentType = 30;
			getData("30");
			break;
		}
		default: {
			break;
		}

		}
	}

	/**
	 * 定期交易统计查询
	 */
	public void getData(String days) {// 7 30
		String logOnInfo = LoginUtils.getLoginName(getActivity());
		String token = LoginUtils.getLoginToken(getActivity());
		String signKey = LoginUtils.getSignKey(getActivity());

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tradeType", "cs.trade.regular.sta.query");
			jsonObject.put("version", "1.0");
			jsonObject.put("logOnInfo", logOnInfo);
			jsonObject.put("token", token);
			// jsonObject.put("groupType", "01");
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
							message.what = 1;
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
		List<Statistics1Entity> list = null;
		ArrayList<Statistics1Entity> alist = null;
		if (jsonData != null) {
			 list = ParaseJson.getInstance(getActivity())
					.ParasepostStatistics(jsonData);
		}
		if (list != null) {
			 alist = new ArrayList<Statistics1Entity>(
					list);
		}
		
		// Log.d("paraseData list size :", list.size() + "");
		if (list != null) {
			Message message = new Message();
			message.obj = alist;
			message.what = 3;
			mHandler.sendMessage(message);
		} else {
			mHandler.sendEmptyMessage(4);
		}
	}

	@SuppressLint("NewApi") public void refresh(ArrayList<Statistics1Entity> list) {
		Log.d("refresh adapter", " " + mList.size());
		mWebView.evaluateJavascript("javascript:initData(" + mkJavaScriptStr(list) + ")", null);
		adapter.refresh(list);
		// adapter.notifyDataSetChanged();
	}

	public Handler getmHandler() {
		return mHandler;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public ArrayList<Statistics1Entity> getmList() {
		return mList;
	}

	public void setmList(ArrayList<Statistics1Entity> mList) {
		this.mList = mList;
	}

	@SuppressLint({ "SetJavaScriptEnabled", "AddJavascriptInterface" })
	void initWebView(View view) {
		mWebView = (WebView) view.findViewById(R.id.webView);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				initListViewData();
			}
		});

//		mWebView.setWebChromeClient(new WebChromeClient() {
//			@Override
//			public void onProgressChanged(WebView view, int newProgress) {
//				if (newProgress == 100) {// 网页加载完成
//					// progressDialog.cancel();
//				}
//			}
//			public void onPageFinished(WebView view, String url) {
//				// TODO Auto-generated method stub
//				//super.on(view, url);
//				//finish();
//			}
//		});

		// 启用支持javascript
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);// JS
		settings.setDefaultTextEncodingName("utf-8");
		// 在js中调用本地java方法
		mWebView.addJavascriptInterface(new JsInterface(this.getContext()),
				"Android");
		// settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//Cache
		// LOAD_CACHE_ELSE_NETWORK
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// mWebView.loadUrl("http://110.80.36.234:28081/memberw/jsp/assets/index.html");
		mWebView.loadUrl("file:///android_asset/echarts/line.html");
	}

	// mWebView.evaluateJavascript("javascript:$(window.parent.document).contents().find('#J_Iframe')[0].contentWindow.showMessage(0)",
	// null);
	private class JsInterface {
		private Context mContext;

		public JsInterface(Context context) {
			this.mContext = context;
		}

		@JavascriptInterface
		public void Power() {
			// nfcPlugin.mAudioJackReader.reset();
		}

		@JavascriptInterface
		public int GetMessage() {
			return 1;
		}
	}
	
	private String mkJavaScriptStr(ArrayList<Statistics1Entity> list){
		/** 初始化图表数据 */
//		String[] xData = DateUtil.getSpanDate("2016-08-22 00:00:00", "2016-08-26 00:00:00");
//		double[] yData1 = new double[] { 0.12, 0.01, 10.12, 5.02,
//				8.22};
//		int[] yData2 = new int[] { 3, 1, 2, 5, 1};
		
		List<String> list_xData = new ArrayList<String>();
		List<Double> list_yData1 = new ArrayList<Double>();
		List<Integer> list_yData2 = new ArrayList<Integer>();
		
		if(list != null){
			for (Statistics1Entity statistics1Entity : list) {
				list_xData.add(statistics1Entity.getType());
				list_yData1.add(statistics1Entity.getTotalAmount());
				list_yData2.add(statistics1Entity.getTotalNum());
			}
		}
		
		String xData_json = Json.toJson(list_xData);
		String yData1_json = Json.toJson(list_yData1);
		String yData2_json = Json.toJson(list_yData2);
		String result = xData_json +","+ yData1_json +","+ yData2_json;
		//Log.d("xData", result);
		return result;
	}
	
}
