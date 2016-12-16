package com.cspaying.shanfu.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.entit.CashierDetailEntity;


public class CashierListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CashierDetailEntity> mcOrderList;
    
    
    public CashierListAdapter(Context context,ArrayList<CashierDetailEntity> mcOrderList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mcOrderList = mcOrderList;
	}
    
    
    
    public void updateOrderFlow(ArrayList<CashierDetailEntity> mcOrderList){
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CashierDetailEntity entity = mcOrderList.get(position);
		ViewHodler viewHodler = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			
				viewHodler = new ViewHodler();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.cashierlist_adpter, null);
				viewHodler.name_value = (TextView) convertView.findViewById(R.id.name_value);
				viewHodler.number_value = (TextView) convertView.findViewById(R.id.number_value);
				viewHodler.phone_value = (TextView) convertView.findViewById(R.id.phone_value);
				viewHodler.store_value = (TextView) convertView.findViewById(R.id.store_value);
				
				convertView.setTag(viewHodler);
		} else {
			
				viewHodler = (ViewHodler) convertView.getTag();
		}
		 if (entity != null) {
			 viewHodler.name_value.setText(""+entity.getCashierName());
			 viewHodler.number_value.setText(""+entity.getCashierNo());
			 viewHodler.phone_value.setText(""+entity.getCashierPhone());
			 viewHodler.store_value.setText(""+entity.getStore());
		}
		
		
		
		   return convertView;
	}
	
	
	static class ViewHodler {
		
		private TextView number_value;
		private TextView name_value;
		private TextView phone_value;
		private TextView store_value;
		
	}
	
	
	
}
