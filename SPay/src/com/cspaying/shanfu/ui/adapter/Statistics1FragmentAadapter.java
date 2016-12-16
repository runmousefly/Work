package com.cspaying.shanfu.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.entit.Statistics1Entity;
import com.cspaying.shanfu.ui.entit.SubStatisticsEntity;
import com.cspaying.shanfu.ui.view.XListView;

public class Statistics1FragmentAadapter extends BaseAdapter {
	private Context context;
	private int type_flag = 0;
	private ArrayList<Statistics1Entity> mList;
	private ArrayList<SubStatisticsEntity> mSubList = new ArrayList<>();

	public Statistics1FragmentAadapter(Context context,
			ArrayList<Statistics1Entity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mList = list;
	}

	public void refresh(ArrayList<Statistics1Entity> list) {
		this.mList = list;
		mSubList = null;
		mSubList = new ArrayList<SubStatisticsEntity>();
		for (Statistics1Entity entity : this.mList) {
			if(entity.getSubDetail() != null){
				for (SubStatisticsEntity subEntity : entity.getSubDetail()) {
					mSubList.add(subEntity);
				}
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSubList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mSubList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SubStatisticsEntity entity = mSubList.get(position);
		ViewHodler viewHodler = null;
		// ViewHodler1 viewHodler1 = null;
		int type = getItemViewType(position);

		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fragment_statistics1_adapter, null);
			viewHodler.pay_img = (ImageView) convertView
					.findViewById(R.id.pay_img);
			viewHodler.pay_text = (TextView) convertView
					.findViewById(R.id.pay_text);
			viewHodler.pay_total_amount = (TextView) convertView
					.findViewById(R.id.pay_total_amount);
			viewHodler.pay_total_count = (TextView) convertView
					.findViewById(R.id.pay_total_count);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}

		String paytext = entity.getItemCode();
		int totalCount = entity.getTotalNumByItem();
		double totalAmount = entity.getTotalAmountByItem();

		viewHodler.pay_text.setText("" + entity.getItemName());
		viewHodler.pay_total_amount.setText("￥" + totalAmount);
		viewHodler.pay_total_count.setText(totalCount + "笔");
		
		switch (paytext) {
		case "1":
		case "2":
		case "3":
		case "4":
			viewHodler.pay_img.setImageResource(R.drawable.icon_list_wechat);
			break;
		case "5":
		case "6":
		case "7":
		case "8":
			viewHodler.pay_img.setImageResource(R.drawable.icon_list_jd);
			break;
		case "9":
			viewHodler.pay_img.setImageResource(R.drawable.icon_list_qq);
			break;
		case "10":
		case "11":
		case "12":
			viewHodler.pay_img.setImageResource(R.drawable.icon_list_pay);
			break;
		default:
			break;
		}

		return convertView;
	}

	static class ViewHodler {

		private ImageView pay_img;
		private TextView pay_text;
		private TextView pay_total_count;
		private TextView pay_total_amount;

	}
}
