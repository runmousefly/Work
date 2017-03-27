package com.mct.coreservices;

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
	
	public static final String ACTION_SERVICE_START 		= "mct.action.SERVICE_START";	//开始启动
	public static final String ACTION_SERVICE_RESUME 	= "mct.action.SERVICE_RESUME";	//启动完成
	public static final String ACTION_SERVICE_PAUSE 		= "mct.action.SERVICE_PAUSE";	//开始退出
	public static final String ACTION_SERVICE_STOP 		= "mct.action.SERVICE_STOP";		//退出完成
	
	public static final String EXTRA_SERVICE_NAME 		= "mct.extra.SERVICE_NAME";
	
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
				
		ComponentName componentName = startServiceAsUser(new Intent(this, CarService.class), UserHandle.CURRENT);
		Log.v(LOG_TAG, "startServiceAsUser:"+componentName);
	}

	@Override
	public void onTerminate()
	{
		Log.d(LOG_TAG, "JinneeApplication onTerminate");
		super.onTerminate();
		mInstance = null;
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
