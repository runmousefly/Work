package com.terris.mobileguard.ui;

import java.lang.annotation.Annotation;

import com.lidroid.xutils.view.annotation.event.OnClick;
import com.terris.mobileguard.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class HomeActivity extends Activity
{
	private GridView gv_home;
	private Dialog mEnterPwdDialog;
	
	private String[] home_item_names = new String[]{
		"手机防盗","通讯卫士","软件管家",
		"进程管理","流量统计","手机杀毒",
		"缓存清理","高级工具","设置中心"
	};
	
	private int[] home_item_icons = new int[]{
			R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app_selector,
			R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
			R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
	}
	
	private void initView()
	{
		gv_home = (GridView)findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
	}

	
	private class HomeAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return home_item_names.length;
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			View itemView = null;
			if(convertView == null)
			{
				itemView = View.inflate(getApplicationContext(), R.layout.home_item, null);
			}
			else
			{
				itemView = convertView;
			}
			ImageView itemIcon = (ImageView)itemView.findViewById(R.id.iv_home_icon);
			TextView itemName = (TextView)itemView.findViewById(R.id.tv_home_name);
			itemIcon.setImageResource(home_item_icons[position]);
			itemName.setText(home_item_names[position]);
			return itemView;
		}
		
	}
	
	private void showEnterPwdDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		View view = View.inflate(this, R.layout.dialog_login, null);
		EditText enterPwd = (EditText)view.findViewById(R.id.et_login_password);
		Button okBtn = (Button)view.findViewById(R.id.btn_login_ok);
		Button cancelBtn= (Button)view.findViewById(R.id.btn_login_cancel);
		cancelBtn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mEnterPwdDialog.dismiss();
			}
		});
		okBtn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mEnterPwdDialog.dismiss();
			}
		});
		builder.setView(view);
		mEnterPwdDialog = builder.show();
	}
}
