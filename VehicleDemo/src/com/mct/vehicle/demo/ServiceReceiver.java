package com.mct.vehicle.demo;

import java.io.File;
import java.io.IOException;

import com.mct.VehicleManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RecoverySystem;
import android.util.Log;

public class ServiceReceiver extends BroadcastReceiver
{
	public static final String LOG_TAG = ServiceReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		Log.i(LOG_TAG, "receive msg:"+action);
		if(action.equals("mct.action.SERVICE_START"))
		{
			
		}
		else if(action.equals("mct.action.SERVICE_RESUME"))
		{
			VehicleManager vehicleManager = VehicleManager.getInstance();
			if(vehicleManager == null)
			{
				Log.e(LOG_TAG, "receive vehicle manager instance error!");
			}
			Log.e(LOG_TAG, "receive vehicle manager instance success!");
			Intent testIntent = new Intent(context,MainActivity.class);
			testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			context.startActivity(testIntent);
		}
		else if(action.equals("mct.action.SERVICE_PAUSE"))
		{
			
		}
		else if(action.equals("mct.action.SERVICE_STOP"))
		{
			
		}
		else if(action.equals(Intent.ACTION_BOOT_COMPLETED))
		{
			//Intent testIntent = new Intent(context,McuDemoActivity.class);
			//Intent testIntent = new Intent(context,MainActivity.class);
			//testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			//context.startActivity(testIntent);
//			try
//			{
//				RecoverySystem.installPackage(context, new File("/cache/update.zip"));
//			} catch (IOException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
