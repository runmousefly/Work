package com.cspaying.shanfu.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.BaseActivity;
import com.cspaying.shanfu.ui.MyApplication;
import com.cspaying.shanfu.ui.entit.PostOrderDetails;
import com.cspaying.shanfu.ui.jsondata.InitJson;
import com.cspaying.shanfu.ui.jsondata.ParaseJson;
import com.cspaying.shanfu.ui.service.HttpUtil;
import com.cspaying.shanfu.ui.service.HttpUtil.OnRequestListener;
import com.cspaying.shanfu.ui.utils.DialogUtil;

public class OrderDetailsActivity extends BaseActivity
{

	private RelativeLayout layTitle;
	private TextView tvTitle;
	private Button btnBack;
	private TextView tvBack;

	public static final int TARDE_STATUS_PAY_NO = 1;
	public static final int TARDE_STATUS_PAY_SUCCESS = 2;
	public static final int TARDE_STATUS_PAY_REV = 3;
	public static final int TARDE_STATUS_PAY_CLOSE = 4;
	public static final int TARDE_STATUS_PAY_REFUND = 5;
	public static final int TARDE_STATUS_PAY_FAILED = 9;
	public static final int TARDE_STATUS_PAY_TIMEOUT = 10;

	private TextView pay_amount_value;// 交易金额
	private TextView merchant_num_value;
	private TextView transaction_num_value;// 交易单号
	private TextView transaction_num;// 商户单号
	private TextView order_time_value;// 下单时间
	private TextView pay_type_value;// 支付方式

	public static String mchIdKey = "mchIdkey";
	public static String outTradeNoKey = "outTradeNokey";
	public static String queryTypeKey = "queryTypekey";
	public static String queryStatusKey = "queryStatuskey";
	private String mchId;
	private String outTradeNo;
	private String queryType;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetails_main);
		MyApplication.getInstance().addActivity(this);
		initInten();
		initView();
		initPostData();
	}

	public void initInten()
	{
		Intent intent = getIntent();
		mchId = intent.getStringExtra(mchIdKey);
		outTradeNo = intent.getStringExtra(outTradeNoKey);
		queryType = intent.getStringExtra(queryTypeKey);
	}

	private void initView()
	{
		// TODO Auto-generated method stub
		layTitle = (RelativeLayout) findViewById(R.id.lay_title);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("订单详情");

		tvBack = (TextView) findViewById(R.id.tv_left_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setOnClickListener(this);

		pay_amount_value = (TextView) findViewById(R.id.pay_amount_value);

		merchant_num_value = (TextView) findViewById(R.id.merchant_num_value);
		transaction_num_value = (TextView) findViewById(R.id.transaction_num_value);
		transaction_num_value = (TextView) findViewById(R.id.transaction_num_value);
		order_time_value = (TextView) findViewById(R.id.order_time_value);
		pay_type_value = (TextView) findViewById(R.id.pay_type_value);

	}

	public void initPostData()
	{
		DialogUtil.getInstance(OrderDetailsActivity.this).startProgressDialog(OrderDetailsActivity.this, "");
		String jsonData = InitJson.getInstance(OrderDetailsActivity.this).initDetailOrder("cs.trade.single.query", "1.0", mchId, outTradeNo, queryType);
		HttpUtil.getInstance(OrderDetailsActivity.this).reques(jsonData, HttpUtil.baseUrl, new OnRequestListener()
		{

			@Override
			public void onResult(int statusCode, String str)
			{
				// TODO Auto-generated method stub
				Message message = new Message();
				if (statusCode == 0)
				{
					message.what = 1;
					message.obj = str;
				}
				else
				{
					message.what = 2;
				}
				UIhandle.sendMessage(message);
			}
		});

	}

	public void initData(PostOrderDetails entity)
	{

		pay_amount_value.setText("" + entity.getAmount());
		merchant_num_value.setText(mchId);
		// merchant_name.setText("");
		transaction_num_value.setText("" + entity.getOutTradeNo());
		order_time_value.setText("" + changeDate(entity.getTransTime()));

		String channel = entity.getChannel();
		if (channel.equals("wxPub") || channel.equals("wxPubQR") || channel.equals("wxApp") || channel.equals("wxMicro"))
		{
			pay_type_value.setText("微信支付");
		}
		else if (channel.equals("jdPay") || channel.equals("jdPayGate") || channel.equals("jdMicro") || channel.equals("jdQR"))
		{
			pay_type_value.setText("京东支付");
		}
		else if (channel.equals("alipayQR") || channel.equals("alipayApp") || channel.equals("alipayMicro"))
		{
			pay_type_value.setText("支付宝支付");
		}
		else
		{
			pay_type_value.setText("unknown");
		}

	}

	public String changeDate(String datestring)
	{
		String fatString = null;
		try
		{
			if (datestring != null)
			{
				String yearString = datestring.substring(0, 4);
				String monString = datestring.substring(4, 6);
				String dayString = datestring.substring(6, 8);
				String hourString = datestring.substring(8, 10);
				String fentring = datestring.substring(10, 12);
				String miaoString = datestring.substring(12, 14);
				fatString = yearString + "-" + monString + "-" + dayString + " " + hourString + ":" + fentring + ":" + miaoString;
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fatString;
	}

	private Handler UIhandle = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what)
			{
				case 1:
					String jsondata = msg.obj.toString();
					PostOrderDetails entity = ParaseJson.getInstance(OrderDetailsActivity.this).ParasepostDetail(jsondata);
					initData(entity);
					break;
				case 2:

					break;

				default:
					break;
			}
			DialogUtil.getInstance(OrderDetailsActivity.this).stopProgressDialog();
		}

	};

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId())
		{

			case R.id.tv_left_back:
				finish();
				break;
			default:
				break;
		}
	}
}
