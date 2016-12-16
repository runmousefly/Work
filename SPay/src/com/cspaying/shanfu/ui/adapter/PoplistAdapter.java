package com.cspaying.shanfu.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cspaying.shanfu.R;

public class PoplistAdapter extends BaseAdapter {
    
	private ArrayList<String> mActionItems;
	private Context context;
	public  PoplistAdapter(ArrayList<String> mActionItems,Context context) {
		this.mActionItems = mActionItems;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mActionItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mActionItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
       ViewHodler viewHodler;  
		
		if(arg1 == null){
			viewHodler = new ViewHodler();
			arg1 = View.inflate(context, R.layout.pop_list_adapter,null);
			viewHodler.item = (TextView) arg1.findViewById(R.id.item);
			
			arg1.setTag(viewHodler);
		}else{
			viewHodler = (ViewHodler) arg1.getTag();
		}
		
		viewHodler.item.setText(" "+mActionItems.get(arg0));
		return arg1;
	}
	
     static class ViewHodler{
		TextView item;
	}

}
