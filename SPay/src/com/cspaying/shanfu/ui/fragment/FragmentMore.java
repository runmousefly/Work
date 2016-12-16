package com.cspaying.shanfu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.activity.CashierActivity;
import com.cspaying.shanfu.ui.activity.CommodityNameActivity;
import com.cspaying.shanfu.ui.activity.PasSafeActivity;
import com.cspaying.shanfu.ui.activity.RefundActivity;

public class FragmentMore extends Fragment implements OnClickListener {

	private RelativeLayout layPasSafe, layCashier, layCommodityName, layRefund,
			layTestVersion;
	private Intent intent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_more, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		layPasSafe = (RelativeLayout) view.findViewById(R.id.lay_pasSafe);
		layPasSafe.setOnClickListener(this);
		layCashier = (RelativeLayout) view
				.findViewById(R.id.lay_cashier_manager);

		layCommodityName = (RelativeLayout) view
				.findViewById(R.id.lay_CommodityName);

		layRefund = (RelativeLayout) view.findViewById(R.id.lay_refund);
		layTestVersion = (RelativeLayout) view
				.findViewById(R.id.lay_testVersion);

		layTestVersion.setOnClickListener(this);
		layRefund.setOnClickListener(this);
		layCashier.setOnClickListener(this);
		layCommodityName.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lay_pasSafe:
			intent = new Intent(getActivity(), PasSafeActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_cashier_manager:
			intent = new Intent(getActivity(), CashierActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_CommodityName:
			intent = new Intent(getActivity(), CommodityNameActivity.class);
			startActivity(intent);
			break;

		case R.id.lay_refund:
			intent = new Intent(getActivity(), RefundActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_testVersion:

			break;
		default:
			break;
		}
	}

}
