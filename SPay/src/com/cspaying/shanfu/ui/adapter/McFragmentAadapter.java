package com.cspaying.shanfu.ui.adapter;

import java.util.ArrayList;

import org.achartengine.GraphicalView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.entit.McOrderEntity;
import com.cspaying.shanfu.ui.view.McView;


public class McFragmentAadapter extends BaseAdapter {
    private Context context;
    private final int type1 =0;
    private final int type2 = 1;
    private ArrayList<McOrderEntity> mcOrderList;
    
    
   
		
	
    
    public McFragmentAadapter(Context context,ArrayList<McOrderEntity> mcOrderList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mcOrderList = mcOrderList;
	}
    
    
    
    public void updateRecommendRing(ArrayList<McOrderEntity> mcOrderList){
    	this.mcOrderList = mcOrderList;
    	notifyDataSetChanged();
    }
   
   

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mcOrderList.size();
	}

	
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mcOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		
		return position;
	}
	
	

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(position ==0){
			return type1;
		}else{
			return type2;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		McOrderEntity entity = mcOrderList.get(position);
		ViewHodler viewHodler = null;
		ViewHodler1 viewHodler1 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case type1:
				viewHodler1 = new ViewHodler1();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.fragment_mc_adapter_title, null);
				viewHodler1.a_month = (TextView) convertView.findViewById(R.id.a_month);
				viewHodler1.sevent_days = (TextView) convertView.findViewById(R.id.sevent_days);
				viewHodler1.mc_view = (LinearLayout) convertView.findViewById(R.id.mc_view_line);
				GraphicalView mLineChartView = McView.getInstance(context).getCurveIntent(context);
				viewHodler1.mc_view.addView(mLineChartView, new LayoutParams(LayoutParams.FILL_PARENT
							, LayoutParams.FILL_PARENT));
				break;

			case type2:
				viewHodler = new ViewHodler();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.fragment_statistics1_adapter, null);
				viewHodler.pay_img = (ImageView) convertView.findViewById(R.id.pay_img);
				viewHodler.pay_text = (TextView) convertView.findViewById(R.id.pay_text);
				viewHodler.pay_total_amount = (TextView) convertView.findViewById(R.id.pay_total_amount);
				viewHodler.pay_total_count = (TextView) convertView.findViewById(R.id.pay_total_count);
				convertView.setTag(viewHodler);
				break;
			}

		} else {
			switch (type) {
			case type1:
				viewHodler1 = (ViewHodler1) convertView.getTag();
				break;

			case type2:
				viewHodler = (ViewHodler) convertView.getTag();
				break;
			}

		}


		switch (type) {
		case type1:
			
			     
			break;
		case type2:
			
			String paytext = entity.getOutTradeNo();
			String totalCount = entity.getTotalNum();
			String totalAmount = entity.getTotalAmount();
			String paystaus = entity.getOutChannelNo();
			
			
			viewHodler.pay_text.setText(""+paytext);
			viewHodler.pay_total_amount.setText("￥"+totalAmount);
			viewHodler.pay_total_count.setText(totalCount+"笔");
			if (paystaus.equals("0")) {
				viewHodler.pay_img.setImageResource(R.drawable.icon_list_wechat);
			}else if (paystaus.equals("1")) {
				viewHodler.pay_img.setImageResource(R.drawable.icon_list_pay);

			}else if (paystaus.equals("2")) {
				viewHodler.pay_img.setImageResource(R.drawable.icon_list_qq);

			} 
				

		}

		return convertView;
	}
	
	
	static class ViewHodler {
		
		private ImageView pay_img;
		private TextView pay_text;
		private TextView pay_total_count;
		private TextView pay_total_amount;
		
	}
	static class ViewHodler1 {
		private TextView sevent_days;
		private TextView a_month;
		private LinearLayout mc_view;
	}
	
	
}
