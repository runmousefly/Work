package com.terris.mobileguard.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

public class UIUtils
{
	public static void showToast(final Activity context,final String msgInfo)
	{
		if(context == null || TextUtils.isEmpty(msgInfo))
		{
			return;
		}
		//是否在主线程
		if("main".equals(Thread.currentThread().getName()))
		{
			Toast.makeText(context, msgInfo, Toast.LENGTH_LONG).show();
		}
		else
		{
			context.runOnUiThread(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					Toast.makeText(context, msgInfo, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
