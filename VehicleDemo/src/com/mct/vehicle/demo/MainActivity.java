package com.mct.vehicle.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.VehicleManager;
import com.mct.vehicle.demo.R;
import com.suyoung.systemset.SystemSetting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.content.pm.ResolveInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemService;
import android.os.PowerManager.WakeLock;
import android.os.RecoverySystem;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.NtpTrustedTime;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener
{
	public final static String TAG = "MctVehicleDemo";
	public MainActivity gInstance = null;
	Button mcuTestBtn = null;
	Button radioTestBtn = null;
	Button obdTestBtn = null;
	Button canTestBtn = null;
	Button dvrTestBtn = null;
	Button tpmsTestBtn = null;
	Context mContext = null;
	TextView mLogText = null;
	public MyReceiver mReceiver = new MyReceiver();
	PowerManager mPowerManagerer = null;
	WakeLock mWakeLock = null;
	Timer mTimer = null;
	//按键上报
	private final static String OEM_KEY_CODE 					= "keycode";
	private final static String OEM_KEY_ACTION 				= "keystatus";
	private final static String OEM_KEY_REPEAT_COUNT 	= "keyrepeatcount";
	private final static String ACTION_OEM_KEYEVENT 		= "com.mct.action.oem.keyevent";
	
	//原车警告信息
	private final static String ACTION_VEHICLE_ALERT_INFO 	= "com.mct.action.vehicle_alert_info";
	private final static String KEY_VEHICLE_ALERT_ID_ARRAY 	= "alertIds";
	
	//手刹信息
	private final static String ACTION_VEHICLE_BRAKE_INFO 	= "com.mct.action.vehicle_brake_info";
	private final static String KEY_VEHICLE_BRAKE_STATUS 		= "brake_status";
		
	//大灯信息
	private final static String ACTION_VEHICLE_ILL_INFO 			= "com.mct.action.vehicle_ill_info";
	private final static String KEY_VEHICLE_ILL_STATUS 			= "ill_status";
			
	//倒车信息
	private final static String ACTION_VEHICLE_REVERSE_INFO = "com.mct.action.vehicle_reverse_info";
	private final static String KEY_VEHICLE_REVERSE_STATUS 	 = "reverse_status";
	
	//车门状态
	private final static String ACTION_VEHICLE_DOOR_INFO		 	= "com.mct.action.vehicle_door_info";
	private final static String KEY_FRONT_LEFT_DOOR_STATUS 	= "front_left_door_status";//前左车门状态
	private final static String KEY_FRONT_RIGHT_DOOR_STATUS = "front_right_door_status";//前右车门状态
	private final static String KEY_REAR_LEFT_DOOR_STATUS 	 	= "rear_left_door_status";//后左车门状态
	private final static String KEY_REAR_RIGHT_DOOR_STATUS 	= "rear_right_door_status";//后右车门状态
	private final static String KEY_TRUNK_STATUS	 					= "trunk_status";//后备箱状态
	private final static String KEY_HOOD_STATUS 							= "hood_status";//引擎盖状态
	
	//空调Bar显示与隐藏
	private final static String ACTION_AIR_CONDITION_INFO		= "com.mct.air_condition_info";
	private final static String KEY_DISPLAY_AC_BAR 						= "display_ac_bar";//是否显示空调bar,0 隐藏;1显示
	
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
					if(gInstance == null)
					{
						return;
					}
					String strLog = new String();
					// TODO Auto-generated method stub
					if(intent.getAction().equals(ACTION_OEM_KEYEVENT))
					{
						strLog = "keycode:"+intent.getIntExtra(OEM_KEY_CODE, 0)+
								",down:"+intent.getBooleanExtra(OEM_KEY_ACTION, false);
						gInstance.printUILog(strLog);
					}
					else if(intent.getAction().equals(ACTION_VEHICLE_ALERT_INFO))
					{
						strLog = "AlertIDs:"+VehicleManager.intArrayToString(intent.getIntArrayExtra(KEY_VEHICLE_ALERT_ID_ARRAY));
						gInstance.printUILog(strLog);
					}
					else if(intent.getAction().equals(ACTION_VEHICLE_REVERSE_INFO))
					{
						strLog = "reverse status:"+intent.getIntExtra(KEY_VEHICLE_REVERSE_STATUS, -1);
						gInstance.printUILog(strLog);
					}
					else if(intent.getAction().equals(ACTION_VEHICLE_ILL_INFO))
					{
						strLog = "ILL status:"+intent.getIntExtra(KEY_VEHICLE_ILL_STATUS, -1);
						gInstance.printUILog(strLog);
					}
					else if(intent.getAction().equals(ACTION_VEHICLE_BRAKE_INFO))
					{
						strLog = "brake status:"+intent.getIntExtra(KEY_VEHICLE_BRAKE_STATUS, -1);
						gInstance.printUILog(strLog);
					}
					else if(intent.getAction().equals(ACTION_VEHICLE_DOOR_INFO))
					{
						int flStatus = intent.getIntExtra(KEY_FRONT_LEFT_DOOR_STATUS, -1);
						int frStatus = intent.getIntExtra(KEY_FRONT_RIGHT_DOOR_STATUS, -1);
						int rlStatus = intent.getIntExtra(KEY_REAR_LEFT_DOOR_STATUS, -1);
						int rrStatus = intent.getIntExtra(KEY_REAR_RIGHT_DOOR_STATUS, -1);
						int trunkStatus = intent.getIntExtra(KEY_TRUNK_STATUS, -1);
						int hoodStatus = intent.getIntExtra(KEY_HOOD_STATUS, -1);
						strLog = "Door Status,FL:"+flStatus+",FR:"+frStatus+",RL:"+rlStatus+",RR:"+rrStatus+",TRUNK:"+trunkStatus+",HOOD:"+hoodStatus;
						gInstance.printUILog(strLog);
					}	
					else if(intent.getAction().equals(ACTION_AIR_CONDITION_INFO))
					{
						int acBarShow = intent.getIntExtra(KEY_DISPLAY_AC_BAR,-1);
						strLog = "ACBar show:"+acBarShow;
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
			String strHistory = mLogText.getText().toString();
			mLogText.setText(strHistory+"[UILog]  "+logText+"\r\n");
			mLogText.setVisibility(View.VISIBLE);
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
		
		mcuTestBtn = (Button)this.findViewById(R.id.mcuTest);
		radioTestBtn = (Button)this.findViewById(R.id.radioTest);
		obdTestBtn = (Button)this.findViewById(R.id.obdTest);
		canTestBtn = (Button)this.findViewById(R.id.canTest);
		dvrTestBtn = (Button)this.findViewById(R.id.dvrTest);
		tpmsTestBtn = (Button)this.findViewById(R.id.tpmsTest);
		mcuTestBtn.setOnClickListener(this);
		radioTestBtn.setOnClickListener(this);
		obdTestBtn.setOnClickListener(this);
		canTestBtn.setOnClickListener(this);
		dvrTestBtn.setOnClickListener(this);
		tpmsTestBtn.setOnClickListener(this);
		
		mLogText = (TextView)this.findViewById(R.id.LogText);
		mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());
		
//		int a = ((-2)&0xFF);
//		int b = ((~((-2)&0xFF)) & 0x80+1);
		
//		int sum = 4;
//		int test = ((sum^0xFF) & 0xFF)+1;
//		Log.i(TAG, "test:"+test);
		
		
		IntentFilter myIntentFilter = new IntentFilter(); 
		myIntentFilter.addAction(ACTION_OEM_KEYEVENT);
		myIntentFilter.addAction(ACTION_VEHICLE_ALERT_INFO);
		myIntentFilter.addAction(ACTION_VEHICLE_REVERSE_INFO);
		myIntentFilter.addAction(ACTION_VEHICLE_ILL_INFO);
		myIntentFilter.addAction(ACTION_VEHICLE_BRAKE_INFO);
		myIntentFilter.addAction(ACTION_VEHICLE_DOOR_INFO);
		myIntentFilter.addAction(ACTION_AIR_CONDITION_INFO);
	   // myIntentFilter.addAction(Intent.ACTION_SCREEN_ON);     
	   // myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);  
	   // myIntentFilter.addAction("com.mct.wakeup.mode");
		//myIntentFilter.addAction("com.mct.gensor_wakeup");     
	    registerReceiver(mReceiver, myIntentFilter);     
//	    
//	    mPowerManagerer = (PowerManager) getSystemService(Context.POWER_SERVICE);
//	    
	   /* mTimer = new Timer(); 
	    mTimer.schedule(new TimerTask()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				//Log.i(TAG, "isBegingKeptAwake:"+mPowerManagerer.isBeingKeptAwake());
				AudioManager audioManager = new AudioManager(MainActivity.this);
				if(audioManager != null)
				{
					String strStreamActive = new String();
					String strStreamActiveRemotely = new String();
					for(int i = 0;i<10;i++)
					{
						if(AudioSystem.isStreamActive(i, 500))
						{
							strStreamActive += "["+i+"]:true";
						}
						if(AudioSystem.isStreamActiveRemotely(i, 500))
						{
							strStreamActiveRemotely += "["+i+"]:true";
						}
					}
					final String str1 = strStreamActive;
					final String str2 = strStreamActiveRemotely;
					
//					final boolean mute = audioManager.isMasterMute();
//					audioManager.setMasterMute(!mute);
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							printUILog("StreamActive:"+str1);
							printUILog("StreamActiveRemote:"+str2);
						}
						
					});
				}
			}
		}, 2000,500);*/
	    
	  //  calculateAudioBalanceFromPoint(7,7);
	 //   calculatePointFromAudioBalance(1,7,0,2);

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
		if(mTimer != null)
		{
			mTimer.purge();
			mTimer.cancel();
			mTimer = null;
		}
		
		if(mWakeLock != null)
		{
			Log.i(TAG, "release lock");
			mWakeLock.release();
			mWakeLock = null;
		}
		if(mReceiver != null)
		{
			unregisterReceiver(mReceiver);
		}
		super.onDestroy();
	}

	//public static String TEST_PACKAGE_NAME = "com.ijidou.factorydetect";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.canbus";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.iche";
	//public static String TEST_PACKAGE_NAME = "com.android.settings";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.telephone";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.mqb";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.personal";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.toyotasetting";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.update";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.userdetect";
	//public static String TEST_PACKAGE_NAME = "com.qti.factory";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.core";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.canbus.service";
	//public static String TEST_PACKAGE_NAME = "com.ijidou.ihelp";
	public static String TEST_PACKAGE_NAME = "com.ijidou.apkversion";
	@Override
	public void onClick(View v)
	{
		if(v == mcuTestBtn)
		{
			//SystemSetting setting = new SystemSetting(mContext);
			//setting.wipeAppUserData();
			//openApp(TEST_PACKAGE_NAME);
			startActivity(new Intent(MainActivity.this,McuDemoActivity.class));
//			if(mWakeLock == null)
//			{
//				Log.i(TAG, "acquire lock");
//				//SCREEN_BRIGHT_WAKE_LOCK
//				//FULL_WAKE_LOCK
//				//SCREEN_DIM_WAKE_LOCK
//				//PARTIAL_WAKE_LOCK
//				mWakeLock = mPowerManagerer.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "VehiceDemo");
//				mWakeLock.acquire();
//			}
			
    	}
		else if(v == radioTestBtn)
		{
			//SystemSetting setting = new SystemSetting(mContext);
			//setting.wipeAppData();
			//Intent intent = new Intent("com.mct.action.MASTER_CLEAR_APP");
			//sendBroadcast(intent);
			//mI2sManager.stopI2s();
			startActivity(new Intent(MainActivity.this,RadioActivity.class));
			//scanApps(TEST_PACKAGE_NAME);
		}
		else if(v == obdTestBtn)
		{
			//mI2sManager.startI2s();
			startActivity(new Intent(MainActivity.this,OBDDemoActivity.class));
//			if(mWakeLock != null)
//			{
//				Log.i(TAG, "release lock");
//				mWakeLock.release();
//				mWakeLock = null;
//			}
		}
		else if(v == canTestBtn)
		{
			
			startActivity(new Intent(MainActivity.this,CanActivity.class));
		}
		else if(v == dvrTestBtn)
		{
			//startActivity(new Intent(MainActivity.this,DVRActivity.class));
			TelephonyManager telephonyManager = new TelephonyManager(MainActivity.this);
			String subscriberId = telephonyManager.getSubscriberId();
			printUILog("getSubscriberId:"+subscriberId);
			/*AudioManager audioManager = new AudioManager(MainActivity.this);
			if(audioManager != null)
			{
				audioManager.setMasterMute(!audioManager.isMasterMute());
			}*/
		}
		else if(v == tpmsTestBtn)
		{
			startActivity(new Intent(MainActivity.this,TPMSActivity.class));
		}
	}
	
	public String getSerialNumber1() 
	{
		String getSerialNumber1 = android.os.Build.SERIAL;
		
		Log.i(TAG, "getSerialNumber1:"+getSerialNumber1);
	    return getSerialNumber1;
	}

	public String getSerialNumber()
	{
		String serial = "";
		try
		{
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class);
			serial = (String) get.invoke(c, "ro.serialno");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		Log.i(TAG, "getSerialNumber:" + serial);
		return serial;
	}
	
	
	public void writeOemData()
	{
		ByteBuffer buffer = ByteBuffer.allocate(255);
		buffer.put((byte) 0xFF);
		buffer.put((byte) 0x01);
		byte[] snCodeArray = new byte[]{0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		byte[] reserved = new byte[220];
		for(int i=0;i<220;i++)
		{
			reserved[i] = 0x00;
		}
		buffer.put(snCodeArray);
		buffer.put(reserved);
		byte checksum = 0;
		for(int i=0;i<254;i++)
		{
			checksum += buffer.get(i);
		}
		checksum = (byte)((checksum & 0xFF)-1);
		buffer.put(checksum);
		int pos = buffer.position();
		int capacity = buffer.capacity();
		int limit = buffer.limit();
		Log.i(TAG, "pos:"+pos+",capacity:"+capacity+",limit:"+limit+",checksum:"+checksum);
		try{   
		       FileOutputStream fout = new FileOutputStream("/dev/block/bootdevice/by-name/oem");   
		       fout.write(buffer.array());   
		       fout.close();   
		     }  
		      catch(Exception e){   
		        e.printStackTrace();   
		       }
	}
	
	public  void getDeviceId()
	{
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		if(tm != null)
		{
			Log.i(TAG,"DeviceID:"+tm.getDeviceId());
		}
	}
	
	public  void showNtpServer()
	{
		NtpTrustedTime.getInstance(mContext);
	}
	
	//根据坐标计算声场平衡增益值
	public void calculateAudioBalanceFromPoint(int x,int y)
	{
		//第一象限
		int fl = 7,fr = 7,rl = 7,rr = 7;
		if(x < 0 && y > 0)
		{
			fl = 7;
			fr = 7+x;
			rl = 7 - y;
			rr = 0;
		}
		//第二象限
		else if(x > 0 && y > 0)
		{
			fl = 7 - x;
			fr = 7;
			rl = 0;
			rr = 7 - y;
		}
		//第三象限
		else if(x < 0 && y < 0)
		{
			fl = 7 + y;
			fr = 0;
			rl = 7;
			rr = 7 + x;
		}
		//第四象限
		else if(x > 0 && y < 0)
		{
			fl = 0;
			fr = 7 + y;
			rl = 7 - x;
			rr = 7;
		}
		else if(x == 0 && y != 0)
		{
			if(y < 0)
			{
				fl = 7 + y;
				fr = 7 + y;
				rl = 7;
				rr = 7;
			}
			else
			{
				fl = 7;
				fr = 7;
				rl = 7 - y;
				rr = 7 - y;
			}
		}
		else if(x != 0 && y == 0)
		{
			if(x < 0)
			{
				fl = 7;
				fr = 7 + x;
				rl = 7;
				rr = 7 + x;
			}
			else
			{
				fl = 7 - x;
				fr = 7;
				rl = 7 - x;
				rr = 7;
			}
		}
		Log.i(TAG, "AudioBalanceResult,FL:"+fl+",FR:"+fr+",RL:"+rl+"RR:"+rr);
	}
	
	//根据声场平衡增益值计算坐标值
	public void calculatePointFromAudioBalance(int fl,int fr,int rl,int rr)
	{
		int x = 0,y = 0;
		if(fl == 7 && fr == 7 && rl == 7 && rr == 7)
		{
			x = 0;
			y = 0;
			return;
		}
		else
		{
			int xMin = -7;
			int xMax = 7;
			int yMin = -7;
			int yMax = 7;
			
			if(fl == 7)
			{
				xMin = Math.max(-7,xMin);
				xMax = Math.min(0,xMax);
				yMin = Math.max(0,yMin);
				yMax = Math.min(7,yMax);
			}
			else if(fl == 0)
			{
				xMin = Math.max(1,xMin);
				xMax = Math.min(7,xMax);
				yMin = Math.max(-7,yMin);
				yMax = Math.min(-1,yMax);
			}
			
			if(fr == 7)
			{
				xMin = Math.max(0,xMin);
				xMax = Math.min(7,xMax);
				yMin = Math.max(0,yMin);
				yMax = Math.min(7,yMax);
			}
			else if(fr == 0)
			{
				xMin = Math.max(-7,xMin);
				xMax = Math.min(-1,xMax);
				yMin = Math.max(-7,yMin);
				yMax = Math.min(-1,yMax);
			}
			
			if(rl == 7)
			{
				xMin = Math.max(-7,xMin);
				xMax = Math.min(0,xMax);
				yMin = Math.max(-7,yMin);
				yMax = Math.min(0,yMax);
			}
			else if(rl == 0)
			{
				xMin = Math.max(1,xMin);
				xMax = Math.min(7,xMax);
				yMin = Math.max(1,yMin);
				yMax = Math.min(7,yMax);
			}
			
			if(rr == 7)
			{
				xMin 	= Math.max(0,xMin);
				xMax = Math.min(7,xMax);
				yMin 	= Math.max(-7,yMin);
				yMax 	= Math.min(0,yMax);
			}
			else if(rr == 0)
			{
				xMin = Math.max(-7,xMin);
				xMax = Math.min(-1,xMax);
				yMin = Math.max(1,yMin);
				yMax = Math.min(7,yMax);
			}
			
			if(fl != 0 && fl != 7)
			{
				int x1 = 7 - fl;
				int y1 = fl - 7;
				if(x1 >= xMin && x1 <= xMax)
				{
					x = x1;
				}
				if(y1 >= yMin && y1 <= yMax)
				{
					y = y1;
				}
			}
			
			if(fr != 0 && fr != 7)
			{
				int x2 = fr - 7;
				int y2 = fr - 7;
				if(x2 >= xMin && x2 <= xMax)
				{
					x = x2;
				}
				if(y2 >= yMin && y2 <= yMax)
				{
					y = y2;
				}
			}
			
			if(rl != 0 && rl != 7)
			{
				int x3 = 7 - rl;
				int y3 = 7 - rl;
				if(x3 >= xMin && x3 <= xMax)
				{
					x = x3;
				}
				if(y3 >= yMin && y3 <= yMax)
				{
					y = y3;
				}
			}
			
			if(rr != 0 && rr != 7)
			{
				int x4 = rr - 7;
				int y4 = 7 - rr;
				if(x4 >= xMin && x4 <= xMax)
				{
					x = x4;
				}
				if(y4 >= yMin && y4 <= yMax)
				{
					y = y4;
				}
			}
		}
		Log.i(TAG, "Point Result,X:"+x+",Y:"+y);
	}
	
	
	private void openApp(String packageName)
	{
		PackageManager pm = getPackageManager();
		if (pm == null) { return; }
		PackageInfo pi = null;
		try
		{
			pi = pm.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pi == null) { return; }

		Intent resolveIntent = new Intent(Intent.ACTION_DEFAULT, null);
		//resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null)
		{
			// String packageName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_DEFAULT);
			//intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			startActivity(intent);
		}
	}
	
	private void scanApps(String packageName)
	{
		PackageManager pm = getPackageManager();
		if (pm == null) { return; }
		PackageInfo pi = null;
		try
		{
			pi = pm.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pi == null) { return; }

		
		Intent resolveIntent = new Intent(Intent.ACTION_DEFAULT, null);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
		for(int i=0;i<apps.size();i++)
		{
			String className = apps.get(i).activityInfo.name;
			Log.i(TAG, "Activity:"+className);
//			if("com.android.setting.activity.SettingIjidouActivity".equals(className))
//			{
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//				ComponentName cn = new ComponentName(packageName, className);
//
//				intent.setComponent(cn);
//				startActivity(intent);
//				return;
//			}
		}
	}
	
	private  void execShellCmd(String strShellCmd)
	{
		Runtime runtime = Runtime.getRuntime();    
	    try
		{
			Process proc = runtime.exec(strShellCmd);
			InputStream inputstream = proc.getInputStream();  
	        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
	        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
	        // read the ls output  
	        String line = "";  
	        StringBuilder sb = new StringBuilder(line);  
	        while ((line = bufferedreader.readLine()) != null) {   
	                sb.append(line);  
	                sb.append('\n');  
	        }  
	        Log.i(TAG, "exec shell:"+sb.toString());
	        printUILog(sb.toString());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
//	Runtime runtime = Runtime.getRuntime();    
//    try
//	{
//		Process proc = runtime.exec("mkdir -p /data/data/test1");
//		InputStream inputstream = proc.getInputStream();  
//        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
//        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
//        // read the ls output  
//        String line = "";  
//        StringBuilder sb = new StringBuilder(line);  
//        while ((line = bufferedreader.readLine()) != null) {  
//            //System.out.println(line);  
//                sb.append(line);  
//                sb.append('\n');  
//        }  
//        Log.i(TAG, "exec shell:"+sb.toString());
//	} catch (IOException e1)
//	{
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}        //这句话就是shell与高级语言间的调用  
//    
//	File file = new File("//data//data//test2");
//	boolean ret = false;
//	try
//	{
//		boolean bMkDir = file.mkdirs();
//		Log.i(TAG, "create dir:"+bMkDir);
//		ret = file.createNewFile();
//	} catch (IOException e)
//	{
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	Log.i(TAG, "create file:"+ret);
}
