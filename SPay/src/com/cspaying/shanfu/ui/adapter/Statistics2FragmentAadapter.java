package com.cspaying.shanfu.ui.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.nutz.json.Json;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.entit.Statistics1Entity;
import com.cspaying.shanfu.ui.entit.Statistics2Entity;
import com.cspaying.shanfu.ui.view.XListView;

public class Statistics2FragmentAadapter extends BaseAdapter {
	private Context context;
	private ArrayList<Statistics2Entity> mList;

	public Statistics2FragmentAadapter(Context context,
			ArrayList<Statistics2Entity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mList = list;
	}

	public void refresh(ArrayList<Statistics2Entity> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Statistics2Entity entity = mList.get(position);
		ViewHodler viewHodler = null;
		// ViewHodler1 viewHodler1 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fragment_statistics2_adapter, null);
			viewHodler.item_name = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHodler.pay_percent = (TextView) convertView
					.findViewById(R.id.pay_percent);
			viewHodler.pay_total_amount = (TextView) convertView
					.findViewById(R.id.pay_total_amount);
			viewHodler.pay_total_count = (TextView) convertView
					.findViewById(R.id.pay_total_count);
			convertView.setTag(viewHodler);
			viewHodler.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}

		String itemName = entity.getItemName();
		int totalCount = entity.getTotalNum();
		double totalAmount = entity.getTotalAmount();
		double percent = entity.getPercent();

		DecimalFormat df = new DecimalFormat(".00");
		viewHodler.item_name.setText(itemName);
		viewHodler.pay_percent.setText(df.format(100 * percent) + "%");
		viewHodler.pay_total_amount.setText("￥" + totalAmount);
		viewHodler.pay_total_count.setText(totalCount + "笔");
		viewHodler.progressBar.setProgress((int) (percent * 100));

		return convertView;
	}

	static class ViewHodler {
		private TextView item_name;
		private TextView pay_percent;
		private TextView pay_total_count;
		private TextView pay_total_amount;
		private ProgressBar progressBar;
	}
}
