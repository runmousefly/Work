package com.mct.devicedemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.DeviceInterfaceDataHandler;
import com.mct.DeviceInterfaceProperties;
import com.mct.DevicePropertyConstants;
import com.mct.MctDeviceManager;
import com.mct.devicedemo.R;
import com.mct.devicedemo.RangingDemo.VehicleDataNotification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener
{
	public final static String TAG = "MctDeviceDemo";
	public MctDeviceManager mDeviceManager = null;
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();
	public MainActivity gInstance = null;
	public Handler mMainHandler = null;
	Button mInterPhoneTestBtn = null;
	Button mRangingTestBtn = null;
	Context mContext = null;
	TextView mLogText = null;
	int mStartMode = DevicePropertyConstants.FUN_ID_INTERPHONE;
	int[] mMcuProperties = new int[]{
			DeviceInterfaceProperties.DIM_INTERPHONE_SERVICE_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_LASER_SERVICE_STATUS_PROPERTY};
	public MyReceiver mReceiver = new MyReceiver();
	public class MyReceiver extends BroadcastReceiver
	{
		public void onReceive(final Context context, final Intent intent)
		{
			Log.i(TAG, "receive broadcast message:"+intent.getAction());
			
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					if(gInstance != null)
					{
						String strLog = "receive broadcast message:"+intent.getAction();
						if(intent.getAction().equals("com.mct.wakeup.mode"))
						{
							strLog += ",keyValue:"+intent.getIntExtra("wakeup_mode", -1);
						}
						gInstance.printUILog(strLog);
					}
				}
			});
		}
	}
		
	private void printUILog(String logText)
	{
		if(mLogText != null)
		{			
			mLogText.append("["+mLogText.getLineCount()+"] "+logText+"\n");
			int offset=mLogText.getLineCount()*mLogText.getLineHeight();
            if(offset>mLogText.getHeight())
            {
            	mLogText.scrollTo(0,offset-mLogText.getHeight());
            }
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gInstance = this;
		mContext = getApplicationContext();
		
		mInterPhoneTestBtn = (Button)this.findViewById(R.id.InterPhoneTest);
		mRangingTestBtn = (Button)this.findViewById(R.id.RangingTest);
		mInterPhoneTestBtn.setOnClickListener(this);
		mRangingTestBtn.setOnClickListener(this);
		
		mLogText = (TextView)this.findViewById(R.id.logView);
		mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		IntentFilter myIntentFilter = new IntentFilter(); 
	   // myIntentFilter.addAction(Intent.ACTION_SCREEN_ON);     
	   // myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);  
	   // myIntentFilter.addAction("com.mct.wakeup.mode");
		//myIntentFilter.addAction("com.mct.gensor_wakeup");     
	    registerReceiver(mReceiver, myIntentFilter);    
	    
	    mDeviceManager = MctDeviceManager.getInstance();
		if(mDeviceManager == null)
		{
			Log.e(TAG, "get Device Instace failed");
			return;
		}
		mDeviceManager.registerHandler(mMcuProperties, mDataNotification);

		mMainHandler = new Handler(this.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				try
				{
					switch (msg.what)
					{
						case 0:
						{
							int interphoneStatus = Integer.valueOf(mDeviceManager.getProperty(DeviceInterfaceProperties.DIM_INTERPHONE_SERVICE_STATUS_PROPERTY));
							printUILog("InterPhone Service Status:"+interphoneStatus);
							if(interphoneStatus == DevicePropertyConstants.INTERPHONE_SERVICE_STATE_ON)
							{
								startActivity(new Intent(MainActivity.this,InterPhoneDemo.class));
							}
							break;
						}
						case 1:
						{
							int laserStatus = Integer.valueOf(mDeviceManager.getProperty(DeviceInterfaceProperties.DIM_LASER_SERVICE_STATUS_PROPERTY));
							printUILog("Laser Service Status:"+laserStatus);
							if(laserStatus == DevicePropertyConstants.LASER_SERVICE_STATE_ON)
							{
								startActivity(new Intent(MainActivity.this,RangingDemo.class));
							}
							break;
						}
						default:
							break;
					}
				} catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) { return true; }
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		if(mReceiver != null)
		{
			unregisterReceiver(mReceiver);
		}
	}

	@Override
	public void onClick(View v)
	{
		if(v == mInterPhoneTestBtn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_HARDWARE_FUNCTON_SWITCH_PROPERTY, String.valueOf(DevicePropertyConstants.FUN_ID_INTERPHONE));
			mMainHandler.sendEmptyMessageDelayed(0, 2000);
    	}
		else if(v == mRangingTestBtn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_HARDWARE_FUNCTON_SWITCH_PROPERTY, String.valueOf(DevicePropertyConstants.FUN_ID_RANGING));
			mMainHandler.sendEmptyMessageDelayed(1, 2000);
		}
	}
	
	public class VehicleDataNotification implements DeviceInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(final int propId,final String value)
		{
			// TODO Auto-generated method stub
			printUILog("ServiceStatusChange,PropId:"+propId+",Value:"+value);
		}
		
		@Override
		public void onError(boolean bCleanUpAndRestart)
		{
			// TODO Auto-generated method stub
			Log.e(TAG, "onError,bCleanUpAndRestart:" + bCleanUpAndRestart);
		}


	}
}
