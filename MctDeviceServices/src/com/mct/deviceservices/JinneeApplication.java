package com.mct.deviceservices;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;


public class JinneeApplication extends Application
{
	public static final String LOG_TAG = JinneeApplication.class.getSimpleName();
	public static final String ACTION_SERVICE_START = "mct.action.SERVICE_START";
	public static final String ACTION_SERVICE_RESUME = "mct.action.SERVICE_RESUME";
	public static final String ACTION_SERVICE_PAUSE = "mct.action.SERVICE_PAUSE";
	public static final String ACTION_SERVICE_STOP = "mct.action.SERVICE_STOP";
	public static final String EXTRA_SERVICE_NAME = "coagent.extra.SERVICE_NAME";
	
	private static JinneeApplication mInstance = null;
    //private PowerManager.WakeLock mWakeLock;

	public static JinneeApplication getInstance()
	{	return mInstance;	}

	@Override
	public void onCreate()
	{
		Log.v(LOG_TAG, "JinneeApplication onCreate");
		super.onCreate();
		mInstance = this;

		//禁止设备关屏或休眠
//		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
//		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
//		mWakeLock.acquire();		
				
		ComponentName componentName = startServiceAsUser(new Intent(this, DeviceService.class), UserHandle.CURRENT);
		//ComponentName componentName = startService(new Intent(this, CarService.class));
		Log.v(LOG_TAG, "startServiceAsUser:"+componentName);
	}

	@Override
	public void onTerminate()
	{
		Log.d(LOG_TAG, "JinneeApplication onTerminate");
		super.onTerminate();
		mInstance = null;
	//	mWakeLock.release();
	}

	public static boolean setCarService(String name, IBinder binder, String action)
	{
		Log.i("CarServices", "setCarService: " + name + ", " + action);

		if(action.equals(ACTION_SERVICE_START))
		{
			ServiceManager.addService(name, binder);
		}
		else if(action.equals(ACTION_SERVICE_RESUME))
		{
			
		}
		else if(action.equals(ACTION_SERVICE_PAUSE))
		{
			
		}
		else if(action.equals(ACTION_SERVICE_STOP))
		{
		}
		Intent intent = new Intent(action);
		intent.putExtra(EXTRA_SERVICE_NAME, name);
		intent.setData(Uri.parse(name + ":" + action));
		getInstance().sendBroadcastAsUser(intent, UserHandle.CURRENT);
		return true;
	}
}
