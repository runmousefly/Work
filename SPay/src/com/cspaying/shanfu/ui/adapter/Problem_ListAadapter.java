package com.cspaying.shanfu.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.entit.problemEntity;

public class Problem_ListAadapter extends BaseAdapter {
    
	private ArrayList<problemEntity> listData;
	private Context context;
	private ViewHodler viewHodler;  
	private int currentPosition;
	public Problem_ListAadapter(Context context,ArrayList<problemEntity> listData) {
		// TODO Auto-generated constructor stub
		this.listData = listData;
		this.context = context;
	}
	
	public void update(ArrayList<problemEntity> listData){
		this.listData = listData;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData==null?null:listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		problemEntity entity = listData.get(position);
		
		if (convertView == null) {
		viewHodler = new ViewHodler();
		convertView = View.inflate(context, R.layout.problemlist_adapter,null);
		viewHodler.title = (TextView) convertView.findViewById(R.id.title);
		viewHodler.title_re = (RelativeLayout) convertView.findViewById(R.id.title_re);
		viewHodler.value = (TextView) convertView.findViewById(R.id.value);
		convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
			
		}
		
		
		if(currentPosition == position){
			viewHodler.value.setVisibility(View.VISIBLE);
		}else{
			viewHodler.value.setVisibility(View.GONE);
		}
		viewHodler.title_re.setOnClickListener(new myonclic(position));
		
		
		viewHodler.title.setText(""+entity.getTitle());
		viewHodler.value.setText(""+entity.getContent());
		
		return convertView;
	}
	
	class myonclic implements OnClickListener{
		private int position;
		public myonclic(int position){
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(currentPosition == position ){
				currentPosition = -1;
			}else{
				currentPosition = position;
			}
			notifyDataSetChanged();
		}
		
	}
	
	
	
	
	static class ViewHodler {
		TextView title;
		TextView value;
		RelativeLayout title_re;
	}

}
