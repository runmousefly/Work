package com.mct.deviceservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartupReceiver extends BroadcastReceiver
{
	public static final String LOG_TAG = StartupReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			Log.v(LOG_TAG, "onReceive BOOT_COMPLETED");
			//ComponentName componentName = context.startService(new Intent(context, CarService.class));
		}
	}
}
