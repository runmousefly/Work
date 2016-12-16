package com.cspaying.shanfu.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.adapter.OrderFlowAadapter;
import com.cspaying.shanfu.ui.entit.OrderDetail;
import com.cspaying.shanfu.ui.entit.Orderinquiry;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.CustomProgressDialog;
import com.cspaying.shanfu.ui.utils.LoginUtils;

public class RefundActivity extends BaseActivity {

	private RelativeLayout layTitle;
	private TextView tvTitle, tvBack, tvRefundRecord;
	private Button btnReg;
	private Intent intent;
	private ListView refund_listview;
	private OrderFlowAadapter refoundAdapter;
	private ArrayList<OrderDetail> refoundList = new ArrayList<OrderDetail>();
	private CustomProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refund_main);
		MyApplication.getInstance().addActivity(this);
		initView();
		initListView();
		try {
			postJiaoyiliushui();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub

		layTitle = (RelativeLayout) findViewById(R.id.lay_title);

		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);

		tvRefundRecord = (TextView) findViewById(R.id.tv_RefundRecord);
		tvRefundRecord.setOnClickListener(this);

	}

	public void initListView() {

		refund_listview = (ListView) findViewById(R.id.refund_listview);
		refoundAdapter = new OrderFlowAadapter(this, refoundList);
		refund_listview.setAdapter(refoundAdapter);
		refund_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int post,
					long id) {
				Intent intent = new Intent(getBaseContext(),
						RefoundDetailsActivity.class);
				OrderDetail od = refoundList.get(post);
				//intent.putExtra("mchIdkey", "");
				intent.putExtra(RefoundDetailsActivity.outTradeNoKey, od.getOutRradeNo());
				//Log.d("退款 1  outTradeNoKey",od.getOutRradeNo());
				if (od.getStatus().equals("02")) {
					intent.putExtra(RefoundDetailsActivity.queryTypeKey, "1");
					//Log.d("退款 1  queryTypeKey","1");
				} else if (od.getStatus().equals("05")) {
					intent.putExtra(RefoundDetailsActivity.queryTypeKey, "3");
					//Log.d("退款 1  queryTypeKey","3");
				}
				intent.addFlags(200);
				startActivity(intent);
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 查询交易流水
	 * 
	 * @throws ParseException
	 */
	public void postJiaoyiliushui() throws ParseException {
		String loginInfo = LoginUtils.getLoginName(RefundActivity.this);
		String token = LoginUtils.getLoginToken(RefundActivity.this);

		Date currentTime = new Date();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date currentTime = sdf.parse("2016-09-04 22:52:34");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String date = formatter.format(currentTime);

		String jsonData = InitJson.getInstance(RefundActivity.this)
				.initTransactionFlow(loginInfo, token, "1",
						"cs.trade.dtl.query", date, "", "", "02", "1.0");
		startProgressDialog();
		HttpUtil.getInstance(RefundActivity.this).reques(jsonData,
				HttpUtil.baseUrl, new OnRequestListener() {

					@Override
					public void onResult(int statusCode, String str) {
						// TODO Auto-generated method stub
						Message message = new Message();
						if (statusCode == 0) {
							message.what = 1;
							message.obj = str;
						} else {
							message.what = 2;
						}
						UIhandle.sendMessage(message);
					}
				});
	}

	public void paraseOrderList(String jsonData) {
		Orderinquiry orderList = ParaseJson.getInstance(RefundActivity.this)
				.ParaseOrderInquiry(jsonData);
		if (orderList != null) {
			Message message = new Message();
			if (orderList.getResultCode().equals("0")) {
				message.obj = orderList;
				message.what = 4;
			} else {
				message.what = 5;

			}
			UIhandle.sendMessage(message);

		} else {
			UIhandle.sendEmptyMessage(6);
		}
	}

	private Handler UIhandle = new Handler() {

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
				// ArrayList<OrderDetail> details = new
				// ArrayList<OrderDetail>();
				// OrderDetail d = new OrderDetail();
				// d.setStatus("1");
				// d.setAmount(100.00f);
				// d.setOutRradeNo("20160917224508");
				// d.setTransTime("22:47:24");
				// details.add(d);
				// orderList.setDetail(details);

				if (orderList != null) {
					if (orderList.getDetail() != null) {

						refoundList = orderList.getDetail();
						refoundAdapter.updateOrderFlow(refoundList);
					} else {
						Toast.makeText(
								RefundActivity.this.getApplicationContext(),
								MyApplication.getContext().getString(
										R.string.load_data_error),
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(
							RefundActivity.this.getApplicationContext(),
							MyApplication.getContext().getString(
									R.string.load_data_error),
							Toast.LENGTH_SHORT).show();
				}
				stopProgressDialog();
				break;
			case 5:
				Toast.makeText(
						RefundActivity.this.getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.load_data_error), Toast.LENGTH_SHORT)
						.show();
				stopProgressDialog();
				break;
			case 6:
				Toast.makeText(
						RefundActivity.this.getApplicationContext(),
						MyApplication.getContext().getString(
								R.string.load_data_error), Toast.LENGTH_SHORT)
						.show();
				stopProgressDialog();
				break;

			default:
				break;
			}

		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_left_back:
			finish();
			break;
		case R.id.tv_RefundRecord:
			intent = new Intent(RefundActivity.this, DetailsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 开始ProgressDialog()
	 */
	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog
					.createDialog(RefundActivity.this);
			progressDialog.setMessage(MyApplication.getContext().getResources()
					.getString(R.string.intalling));
		}
		progressDialog.show();
	}

	/**
	 * 结束ProgressDialog()
	 */
	private void stopProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
