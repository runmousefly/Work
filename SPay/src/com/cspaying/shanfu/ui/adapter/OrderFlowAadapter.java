package com.cspaying.shanfu.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.entit.OrderDetail;
import com.cspaying.shanfu.ui.entit.OrderDetail;


public class OrderFlowAadapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderDetail> mcOrderList;
    
    
    public OrderFlowAadapter(Context context,ArrayList<OrderDetail> mcOrderList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mcOrderList = mcOrderList;
	}
    
    
    
    public void updateOrderFlow(ArrayList<OrderDetail> mcOrderList){
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
		return mcOrderList.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		OrderDetail entity = mcOrderList.get(position);
		ViewHodler viewHodler = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			
				viewHodler = new ViewHodler();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_items, null);
				viewHodler.content = (TextView) convertView.findViewById(R.id.content);
				viewHodler.pay_tyype = (TextView) convertView.findViewById(R.id.pay_tyype);
				viewHodler.pay_value = (TextView) convertView.findViewById(R.id.pay_value);
				viewHodler.title = (TextView) convertView.findViewById(R.id.title);
				viewHodler.mImage = (ImageView) convertView.findViewById(R.id.mImage);
				
				convertView.setTag(viewHodler);
		} else {
			
				viewHodler = (ViewHodler) convertView.getTag();
		}
		
		   if (entity != null) {
			   String title = entity.getOutRradeNo();
			   String time = entity.getTransTime();
			   float payaoumont = entity.getAmount();
			   String payType = entity.getStatus();
			   String channel = entity.getChannel();
			   viewHodler.title.setText(title);
			   viewHodler.content.setText(time);
			   viewHodler.pay_value.setText(""+payaoumont);
			   if (payType == null) {
				   Log.e("payTypepayTypepayType",""+ payType);
			     }
			   if (payType != null) {
				   if (payType.equals("01")) {
					   viewHodler.pay_tyype.setText("未支付");
				    }else if (payType.equals("02")) {
				    	viewHodler.pay_tyype.setText("已支付");
					}else if (payType.equals("03")) {
				    	viewHodler.pay_tyype.setText("已冲正");
					}else if (payType.equals("04")) {
				    	viewHodler.pay_tyype.setText("已关闭");
					}else if (payType.equals("05")) {
				    	viewHodler.pay_tyype.setText("转入退款");
					}
			    }
			   
			   if (channel != null) {
				 if (channel.equals("wxPub") || channel.equals("wxPubQR")||
					 channel.equals("wxApp") || channel.equals("wxMicro")) {
					 viewHodler.mImage.setImageResource(R.drawable.icon_list_wechat);
				 }else if (channel.equals("jdPay") || channel.equals("jdPayGate")||
						 channel.equals("jdMicro") || channel.equals("jdQR")) {
						 viewHodler.mImage.setImageResource(R.drawable.jd_san_logo);
				 }else if (channel.equals("alipayQR") || channel.equals("alipayApp")||
						 channel.equals("alipayMicro")) {
						 viewHodler.mImage.setImageResource(R.drawable.zfb_scan_logo);
				 }
				 
			}
			   
		}
		   return convertView;
	}
	
	
	static class ViewHodler {
		
		private ImageView mImage;
		private TextView title;
		private TextView content;
		private TextView pay_value;
		private TextView pay_tyype;
		
	}
	
	
	
}
