package com.mct.deviceservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.mct.DeviceInterfaceProperties;
import com.mct.DevicePropertyConstants;
import com.mct.utils.ServiceHelper;
import com.mct.utils.ThreadManager;
import com.mct.device.aidl.IDeviceManager;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.service.notification.INotificationListener;
import android.telephony.ServiceState;
import android.util.Log;
import android.util.SparseArray;

public class DeviceService extends Service 
{
	public 	static final String SERVICE_NAME 	= "com.mct.deviceservice";
	private 	static final String TAG 					= SERVICE_NAME;
	private  static final String SERVICE_VERSION = "MctDeviceServices_V2.4_2017_02_14";//便于确认问题的版本号
	private	static final String TARGET_DEVICE_INT_NODE_PATH 		= "/sys/devices/soc.0/vsp-device.21/connection";
	private	static final String TARGET_DEVICE_STATE_NODE_PATH 	= "/sys/devices/soc.0/vsp-device.21/state";
	private static final String SHARED_PREFERENCES_NAME = "luqiao_config";
	private static final String KEY_LAST_WORK_MODE 		   = "last_work_mode";

	
	private static final int 	INTERPHONE_PROPERTY_ID_MIN 		= 0;
	private static final int 	INTERPHONE_PROPERTY_ID_MAX 		= 999;
	private static final int 	RANGING_PROPERTY_ID_MIN 				= 1000;
	private static final int 	RANGING_PROPERTY_ID_MAX 				= 1999;
	
	
	public static final int 		MSG_RECEIVE_INTERPHONE_DATA 		= 	0x01;
	public static final int 		MSG_RECEIVE_RANGING_DATA 			= 	0x02;
	public static final int 		MSG_PLATFORM_SLEEP							= 	0x03;
	public static final int 		MSG_PLATFORM_WAKEUP 					= 	0x04;
	public static final int 		MSG_MODULE_CONNECTED 					= 0x05;
	public static final int 		MSG_MODULE_DISCONNECTED 				= 0x06;
	
	public static final String 	ACTION_MODULE_CONNECT_CHANGE	= "com.vesmart.module.status";
	public static final String 	KEY_CONNECT_STATUS							= "connect_status";
	
	public static final String 	ACTION_PLATFORM_SLEEP					= "com.android.suyoung.sleep";
	public static final String 	ACTION_PLATFORM_WAKEUP 				= "com.android.suyoung.wakeup";
	public static final String 	KEY_DATA 											= 	"Data";
	
	public static final String   PROP_LASTER_POWER_ENABLE	    	= "persist.mct.laser.enable";
	public static final String   PROP_INTERPHONE_POWER_ENABLE	= "persist.mct.interphone.enable";
	public static final String   PROP_FILL_LIGHT_ENABLE	    			= "persist.mct.filllight.enable";
	public static final String   PROP_SERIAL_IC_SWITCH					= "persist.mct.serial.switch";
	public static final String	PROP_SERIAL_CLOCK 							= "persist.mct.serial.clock";

	//private static final int		HISTORY_PROPERTY_CACHE_MAX	= 20;
	public static final int 		WORK_MODE_OFF									= 	0x00;
	public static final int 		WORK_MODE_INTERPHONE					= 	0x01;
	public static final int 		WORK_MODE_LASER 							= 	0x02;
	
	public static final int 		SERVICE_STATE_OFF							= 	0x00;
	public static final int 		SERVICE_STATE_INITING						= 	0x01;
	public static final int 		SERVICE_STATE_DEINITING 					= 	0x02;
	public static final int 		SERVICE_STATE_ON 								= 	0x03;
	
	private Map<Integer, String> 	mPropertiesHistoryCache = new HashMap<Integer, String>();				//属�?值缓�?
	private SparseArray<Vector<Messenger>> mListeners = new SparseArray<Vector<Messenger>>();		//监听器缓存器
	//private PowerStatusReceiver mPowerStatusReceiver = new PowerStatusReceiver();
	
	private static	Handler 			mMainHandler 					= null;
	//private boolean 				mIsWakeupInit					= false;
	private boolean 				mServiceReady					= false;
	private int 						mWorkMode 						= WORK_MODE_OFF;
	private int 						mModuleConnectStatus		= DevicePropertyConstants.MODULE_STATUS_DISCONNECTED;
	private int 						mServiceState						= SERVICE_STATE_OFF;
	private static DeviceService  		mCarService				= null;
	private MctDeviceDataManager 	mInterPhoneManager 	= null;
	private MctDeviceDataManager 	mRangingManager 		= null;
	
	
	private Timer mDeviceCheckTimer = null;
	//启动设备检测定时器
	private void startDeviceCheckTimer()
	{
		if (mDeviceCheckTimer != null)
		{
			Log.w(TAG, "startDeviceCheckTimer,timer is exist!");
			return;
		}
		Log.i(TAG, "startDeviceCheckTimer");
		// 1S定时发送
		mDeviceCheckTimer = new Timer();
		mDeviceCheckTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				if(mServiceState == SERVICE_STATE_INITING || mServiceState == SERVICE_STATE_DEINITING)
				{
					Log.i(TAG,"service is busy,wait...");
					return;
				}
				Log.i(TAG, "begin to read device node");
				int connectStatus = ServiceHelper.stringToIntSafe(ServiceHelper.readDeviceNode(TARGET_DEVICE_INT_NODE_PATH));
				connectStatus = Math.min(1, Math.max(0, connectStatus));
				Log.i(TAG, "device node value:"+connectStatus+",PrevDeviceStatus:"+mModuleConnectStatus);
				if(mModuleConnectStatus != connectStatus)
				{
					mModuleConnectStatus = connectStatus;
					if(mModuleConnectStatus == DevicePropertyConstants.MODULE_STATUS_CONNECTED)
					{
						mMainHandler.sendEmptyMessage(MSG_MODULE_CONNECTED);
					}
					else if(mModuleConnectStatus == DevicePropertyConstants.MODULE_STATUS_DISCONNECTED)
					{
						mMainHandler.sendEmptyMessage(MSG_MODULE_DISCONNECTED);
					}
				}
				Log.i(TAG, "check device end");
			}
		}, 1000, 1000);//延迟10s,开机直接读取会导致Crash
	}

	// 停止设备检测定时器
	private void stopDeviceCheckTimer()
	{
		if (mDeviceCheckTimer != null)
		{
			Log.i(TAG, "stopDeviceCheckTimer");
			mDeviceCheckTimer.cancel();
			mDeviceCheckTimer.purge();
			mDeviceCheckTimer = null;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// 接口对象
	private StubBinder mBinder = null;

	public class StubBinder extends IDeviceManager.Stub
	{

		@Override
		public int[] getSupportedSignalIds() throws RemoteException
		{
			// TODO Auto-generated method stub
			int []interPhoneSupportedPropertyIds = null;
			int []rangingSupportedPropertyIds = null;
			if(mInterPhoneManager != null && mServiceReady)
			{
				rangingSupportedPropertyIds = mInterPhoneManager.getSupportedPropertyIds();
			}
			if(mRangingManager != null && mServiceReady)
			{
				interPhoneSupportedPropertyIds = mRangingManager.getSupportedPropertyIds();
			}
			return ServiceHelper.combineArray(interPhoneSupportedPropertyIds,rangingSupportedPropertyIds);
		}

		@Override
		public int[] getWritableSignalIds() throws RemoteException
		{
			// TODO Auto-generated method stub
			int []interPhoneWritablePropertyIds = null;
			int []RangingWritablePropertyIds = null;
			if(mRangingManager != null && mServiceReady)
			{
				interPhoneWritablePropertyIds = mRangingManager.getWritablePropertyIds();
			}
			if(mInterPhoneManager != null && mServiceReady)
			{
				RangingWritablePropertyIds = mInterPhoneManager.getWritablePropertyIds();
			}
			return ServiceHelper.combineArray(interPhoneWritablePropertyIds,RangingWritablePropertyIds);
		}

		@Override
		public int[] getSignalsDataType(int[] propIds) throws RemoteException
		{
			// TODO Auto-generated method stub
			if (propIds == null || propIds.length == 0) { return null; }
			int[] properiesDataType = new int[propIds.length];
			for (int i = 0; i < propIds.length; i++)
			{
				//InterPhone Properity ID Range
				if(propIds[i] >= INTERPHONE_PROPERTY_ID_MIN && propIds[i] <= INTERPHONE_PROPERTY_ID_MAX )
				{
					if(mInterPhoneManager != null && mServiceReady)
					{
						properiesDataType[i] = mInterPhoneManager.getPropertyDataType(propIds[i]);
					}
				}
				//Ranging Properity ID Range
				else if(propIds[i] >= RANGING_PROPERTY_ID_MIN && propIds[i] <= RANGING_PROPERTY_ID_MAX )
				{
					if(mRangingManager != null && mServiceReady)
					{
						properiesDataType[i] = mRangingManager.getPropertyDataType(propIds[i]);
					}
				}
			}
			return properiesDataType;
		}
		
		@Override
		public int getSignalDataType(int propId) throws RemoteException
		{
			// TODO Auto-generated method stub
			//InterPhone Properity ID Range
			if(propId >= INTERPHONE_PROPERTY_ID_MIN && propId <= INTERPHONE_PROPERTY_ID_MAX )
			{
				if(mInterPhoneManager != null && mServiceReady)
				{
					return mInterPhoneManager.getPropertyDataType(propId);
				}
			}
			//Ranging Properity ID Range
			else if(propId >= RANGING_PROPERTY_ID_MIN && propId <= RANGING_PROPERTY_ID_MAX )
			{
				if(mRangingManager != null && mServiceReady)
				{
					return mRangingManager.getPropertyDataType(propId);
				}
			}
			return DevicePropertyConstants.DATA_TYPE_UNKNOWN;
		}

		
		@Override
		public boolean setSignal(int propId, String value) throws RemoteException
		{
			// TODO Auto-generated method stub
			//InterPhone Properity ID Range
			if(propId >= INTERPHONE_PROPERTY_ID_MIN && propId <= INTERPHONE_PROPERTY_ID_MAX )
			{
				if(mInterPhoneManager != null && mServiceReady)
				{
					return mInterPhoneManager.setPropValue(propId, value);
				}
			}
			//Ranging Properity ID Range
			else if(propId >= RANGING_PROPERTY_ID_MIN && propId <= RANGING_PROPERTY_ID_MAX )
			{
				if(mRangingManager != null && mServiceReady)
				{
					return mRangingManager.setPropValue(propId, value);
				}
			}
			//硬切功能
			else if(propId == DeviceInterfaceProperties.DIM_HARDWARE_FUNCTON_SWITCH_PROPERTY)
			{
				int funId = ServiceHelper.stringToIntSafe(value);
				handleFuncId(funId);
			}
			return true;
		}
		
		@Override
		public boolean setSignals(int[] propIds, String[] propValues) throws RemoteException
		{
			// TODO Auto-generated method stub
			if(propIds == null || propValues == null || propIds.length != propValues.length || 
					propIds.length == 0 || propValues.length == 0)
			{
				return false;
			}
			for(int i=0;i<propIds.length;i++)
			{
				//InterPhone Properity ID Range
				if(propIds[i] >= INTERPHONE_PROPERTY_ID_MIN && propIds[i] <= INTERPHONE_PROPERTY_ID_MAX )
				{
					if(mInterPhoneManager != null && mServiceReady)
					{
						mInterPhoneManager.setPropValue(propIds[i], propValues[i]);
					}
				}
				//Ranging Properity ID Range
				else if(propIds[i] >= RANGING_PROPERTY_ID_MIN && propIds[i] <= RANGING_PROPERTY_ID_MAX )
				{
					if(mRangingManager != null && mServiceReady)
					{
						mRangingManager.setPropValue(propIds[i], propValues[i]);
					}
				}
			}
			return true;
		}

		@Override
		public String getSignal(int propId) throws RemoteException
		{
			// TODO Auto-generated method stub
			return mPropertiesHistoryCache.get(propId);
		}
		
		@Override
		public String[] getSignals(int[] propIds) throws RemoteException
		{
			// TODO Auto-generated method stub
			String[] strValues = new String[propIds.length];
			for (int i = 0; i < propIds.length; i++)
			{
				strValues[i] = mPropertiesHistoryCache.get(propIds[i]);
			}
			return strValues;
		}
		
		@Override
		public boolean registerMessenger(int[] propIds, Messenger msngr, int[] notifyTypes, int[] rateMs) throws RemoteException
		{
			// TODO Auto-generated method stub
			for (int i = 0; i < propIds.length; i++)
			{
				ServiceHelper.addInterface(mListeners, msngr, propIds[i]);
			}
			return true;
		}

		@Override
		public boolean unregisterMessenger(int[] propIds, Messenger msngr) throws RemoteException
		{
			// TODO Auto-generated method stub
			for (int i = 0; i < propIds.length; i++)
			{
				ServiceHelper.removeInterface(mListeners, msngr, propIds[i]);
			}
			return true;
		}

		public boolean removeListener(int propId, Messenger msngr)
		{
			return ServiceHelper.removeInterface(mListeners, msngr, propId);
		}

		@Override
		public String getUserValue(int param) throws RemoteException
		{
			// TODO Auto-generated method stub
			return null;
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IBinder onBind(Intent intent)
	{
		Log.i(TAG, SERVICE_NAME + " onBind");
		return mBinder;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "OnCreate:"+SystemClock.uptimeMillis());
		mBinder = new StubBinder();
		JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_START);
		
		mCarService = this;
		cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_SERVICE_STATUS_PROPERTY, String.valueOf(DevicePropertyConstants.INTERPHONE_SERVICE_STATE_OFF), false);
		cachePropValue(DeviceInterfaceProperties.DIM_LASER_SERVICE_STATUS_PROPERTY, String.valueOf(DevicePropertyConstants.LASER_SERVICE_STATE_OFF), false);

		mModuleConnectStatus = ServiceHelper.stringToIntSafe(ServiceHelper.readDeviceNode(TARGET_DEVICE_INT_NODE_PATH));
		mModuleConnectStatus = Math.min(1, Math.max(0, mModuleConnectStatus));
		cachePropValue(DeviceInterfaceProperties.DIM_MODULE_CONNECT_STATUS_PROPERTY, String.valueOf(mModuleConnectStatus), false);

		mMainHandler = new Handler(this.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				switch (msg.what)
				{
					case MSG_RECEIVE_INTERPHONE_DATA:
					{
						if(mInterPhoneManager != null)
						{
							mInterPhoneManager.onLocalReceive(msg.getData());
						}
						break;
					}
					case MSG_RECEIVE_RANGING_DATA:
					{
						if(mRangingManager != null)
						{
							mRangingManager.onLocalReceive(msg.getData());
						}
						break;
					}
					case MSG_MODULE_CONNECTED:
						handleModuleConnectStatusChange(DevicePropertyConstants.MODULE_STATUS_CONNECTED);
						break;
						
					case MSG_MODULE_DISCONNECTED:
						handleModuleConnectStatusChange(DevicePropertyConstants.MODULE_STATUS_DISCONNECTED);
						break;
					/*case MSG_PLATFORM_SLEEP:
						mIsWakeupInit = false;
						deinitService();
						break;
					case MSG_PLATFORM_WAKEUP:
						mIsWakeupInit = true;
						initService(WORK_MODE_NONE);
						break;*/
					default:
						break;
				}
			}
		};
		mWorkMode = loadSettings(KEY_LAST_WORK_MODE, -1);
		mWorkMode = Math.min(WORK_MODE_LASER, Math.max(WORK_MODE_OFF, mWorkMode));
		if(mModuleConnectStatus == DevicePropertyConstants.MODULE_STATUS_CONNECTED)
		{
			initService(mWorkMode);
		}
		startDeviceCheckTimer();
		//上报启动完成
		JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_RESUME);
		Log.i(TAG, "OnCreate end:"+SystemClock.uptimeMillis());
	}

	@Override
	public void onDestroy()
	{
		JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_PAUSE);
		super.onDestroy();
		stopDeviceCheckTimer();
		mModuleConnectStatus = DevicePropertyConstants.MODULE_STATUS_DISCONNECTED;
		
		ThreadManager.getSinglePool().stop();
		/*if(mPowerStatusReceiver != null)
		{
			unregisterReceiver(mPowerStatusReceiver);
		}*/
		if(mInterPhoneManager != null)
		{
			mInterPhoneManager.onDeinitManager();
			mInterPhoneManager = null;
		}
		if(mRangingManager != null)
		{
			mRangingManager.onDeinitManager();
			mRangingManager = null;
		}
		JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_STOP);
		mServiceState = SERVICE_STATE_OFF;
		Log.i(TAG, SERVICE_NAME + " onDestroy");
	}
	
	public Handler getMainHandler()
	{
		return mMainHandler;
	}
	
	
	private synchronized void initService(final int initMode)
	{
		Log.i(TAG, "initService begin:"+SystemClock.uptimeMillis());
		ThreadManager.getSinglePool().execute(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i(TAG, "CarService Version:"+SERVICE_VERSION);
				if(initMode != WORK_MODE_OFF)
				{
					enableSerialClock(true);
				}
				switchModuleSerial(initMode);
				mServiceState = SERVICE_STATE_INITING;
				mServiceReady = false;
				if(loadSettings(KEY_LAST_WORK_MODE, -1) != mWorkMode)
				{
					saveSettings(KEY_LAST_WORK_MODE, mWorkMode);
				}
				//初始化对讲机
				if(mWorkMode == WORK_MODE_INTERPHONE)
				{
					enableInterPhoneModule(true);
					if(mRangingManager != null && mRangingManager.getServiceState() != DevicePropertyConstants.LASER_SERVICE_STATE_OFF)
					{
						mRangingManager.onDeinitManager();
					}
					if(mInterPhoneManager == null)
					{
						mInterPhoneManager = new InterPhoneManager();
					}
					if(mInterPhoneManager.getServiceState() != DevicePropertyConstants.INTERPHONE_SERVICE_STATE_ON)
					{
						mInterPhoneManager.onInitManager(mCarService);
					}
					mServiceReady = true;
				}
				//初始化激光
				else if(mWorkMode == WORK_MODE_LASER)
				{
					enableRangingModule(true);
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(mInterPhoneManager != null && mInterPhoneManager.getServiceState() != DevicePropertyConstants.INTERPHONE_SERVICE_STATE_OFF)
					{
						mInterPhoneManager.onDeinitManager();
					}
					if(mRangingManager == null)
					{
						mRangingManager = new RangingManager();
					}
					if(mRangingManager.getServiceState() != DevicePropertyConstants.LASER_SERVICE_STATE_ON)
					{
						mRangingManager.onInitManager(mCarService);
					}
					mServiceReady = true;
				}
				else 
				{
					if(mInterPhoneManager != null && mInterPhoneManager.getServiceState() != DevicePropertyConstants.INTERPHONE_SERVICE_STATE_OFF)
					{
						mInterPhoneManager.onDeinitManager();
					}
					if(mRangingManager != null && mRangingManager.getServiceState() != DevicePropertyConstants.LASER_SERVICE_STATE_OFF)
					{
						mRangingManager.onDeinitManager();
					}
					mServiceReady = false;
				}
				mServiceState = SERVICE_STATE_ON;
				Log.i(TAG, "initService end:"+SystemClock.uptimeMillis());
			}
		});
	}
	
	private synchronized void deinitService()
	{
		Log.i(TAG, "deinitService begin");
		ThreadManager.getSinglePool().execute(new Runnable()
		{
			@Override
			public void run()
			{
				mServiceState = SERVICE_STATE_DEINITING;
				mServiceReady = false;
				if(mRangingManager != null && mRangingManager.getServiceState() != DevicePropertyConstants.LASER_SERVICE_STATE_OFF)
				{
					Log.i(TAG, "ranging manager deinit");
					if(mRangingManager.getServiceState() == DevicePropertyConstants.LASER_SERVICE_STATE_ON)
					{
						mRangingManager.onDeinitManager();
					}
					else 
					{
						Log.i(TAG,"RangingManager is busy!");
					}

				}
				
				if(mInterPhoneManager != null && mInterPhoneManager.getServiceState() != DevicePropertyConstants.INTERPHONE_SERVICE_STATE_OFF)
				{
					Log.i(TAG, "interphone manager deinit");
					if(mInterPhoneManager.getServiceState() == DevicePropertyConstants.INTERPHONE_SERVICE_STATE_ON)
					{
						mInterPhoneManager.onDeinitManager();
					}
					else 
					{
						Log.i(TAG,"InterPhoneManager is busy!");
					}
					
				}
				enableSerialClock(false);
				Log.i(TAG, "deinitService end");
				mServiceState = SERVICE_STATE_OFF;
			}
		});
	}
	
	// 缓存属性值
	public synchronized void cachePropValue(int propId, String value,boolean bDispatch)
	{
		Log.i(TAG, "cachePropValue,PropId:" + propId + ",Value:" + value);
		String cacheValue = mPropertiesHistoryCache.put(propId, value);
		if(cacheValue == null || value == null || bDispatch || !cacheValue.equals(value))
		{
			dispatchData(propId, value);
		}
	}	
		
	public MctDeviceDataManager getInterPhoneManager()
	{
		return mInterPhoneManager;
	}
	
	public MctDeviceDataManager getRangingManager()
	{
		return mRangingManager;
	}
	
	public String loadSettings(String key,String defaultValue)
	{
		return getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(key, defaultValue);
	}
	
	public int loadSettings(String key,int defaultValue)
	{
		return getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(key, defaultValue);
	}
	
	public boolean saveSettings(String key,String value)
	{
		Log.i(TAG, "save settings,key:"+key+",value:"+value);
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public boolean saveSettings(String key,int value)
	{
		Log.i(TAG, "save settings,key:"+key+",value:"+value);
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	
	// 分包
	public synchronized boolean dispatchData(int what, String value)
	{
		Vector<Messenger> vecListener = mListeners.get(what);
		if (vecListener == null)
			return false;
		List<Messenger> removeMessengers = new ArrayList<Messenger>();
		for (int i = vecListener.size() - 1; i >= 0; --i)
		{
			Messenger listener = vecListener.get(i);
			try
			{
				Message msg = Message.obtain();
				msg.what = what;
				Bundle bundle = new Bundle();
				bundle.putString("Value", value);
				msg.setData(bundle);
				listener.send(msg);
			} 
			catch (DeadObjectException e)
			{
				Log.e(TAG, "DeadObjectException " + listener.toString());
				removeMessengers.add(listener);
			} 
			catch (RemoteException e)
			{
				e.printStackTrace();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if(removeMessengers.size() > 0)
		{
			for(int i=0;i<removeMessengers.size();i++)
			{
				ServiceHelper.removeInterface(mListeners, removeMessengers.get(i), what);
			}
		}
		return true;
	}
	
	public int getWorkMode()
	{
		return mWorkMode;
	}
	
	public void enableRangingModule(boolean bEnable)
	{
		if(bEnable)
		{
			SystemProperties.set(PROP_LASTER_POWER_ENABLE, "1");
		}
		else
		{
			SystemProperties.set(PROP_LASTER_POWER_ENABLE, "0");
		}
	}
	
	public void enableInterPhoneModule(boolean bEnable)
	{
		if(bEnable)
		{
			SystemProperties.set(PROP_INTERPHONE_POWER_ENABLE, "1");
		}
		else
		{
			SystemProperties.set(PROP_INTERPHONE_POWER_ENABLE, "0");
		}
	}
	
	public void enableFillLightDevice(boolean bEnable)
	{
		if(bEnable)
		{
			SystemProperties.set(PROP_FILL_LIGHT_ENABLE, "1");
		}
		else
		{
			SystemProperties.set(PROP_FILL_LIGHT_ENABLE, "0");
		}
	}
	
	//	切换串口IC
	public void switchModuleSerial(int workMode)
	{
		if(workMode == WORK_MODE_INTERPHONE)
		{
			Log.i(TAG,"switchModuleSerial:0");
			SystemProperties.set(PROP_SERIAL_IC_SWITCH, "0");
		}
		else if(workMode == WORK_MODE_LASER)
		{
			Log.i(TAG,"switchModuleSerial:1");
			SystemProperties.set(PROP_SERIAL_IC_SWITCH, "1");
		}
	}
	
	public void enableSerialClock(boolean bEnable)
	{
		Log.i(TAG,"enableSerialClock:"+bEnable);
		if(bEnable)
		{
			SystemProperties.set(PROP_SERIAL_CLOCK, "1");
		}
		else
		{
			SystemProperties.set(PROP_SERIAL_CLOCK, "0");
		}
	}
	
	//处理功能硬切模式
	private void handleFuncId(int funId)
	{
		if(DevicePropertyConstants.FUN_ID_INTERPHONE == funId)
		{
			if(mModuleConnectStatus == DevicePropertyConstants.MODULE_STATUS_DISCONNECTED)
			{
				Log.i(TAG, "module is disconnected!");
				return;
			}
			if(mWorkMode == WORK_MODE_INTERPHONE && 
					mServiceState !=  SERVICE_STATE_DEINITING && mServiceState != SERVICE_STATE_OFF)
			{
				Log.i(TAG,"current mode has been in interphone mode");
			}
			else
			{
				Log.i(TAG,"switch to interphone mode");
				mWorkMode = WORK_MODE_INTERPHONE;
				mServiceReady = false;
				if(ThreadManager.getSinglePool().getActiveCount() > 0)
				{
					if(mServiceState == SERVICE_STATE_INITING || mServiceState == SERVICE_STATE_DEINITING)
					{
						Log.i(TAG,"threadpoll is working");
					}
					Log.i(TAG,"force exit thread poll");
					ThreadManager.getSinglePool().stop();
				}
				initService(mWorkMode);
			}
		}
		else if(DevicePropertyConstants.FUN_ID_RANGING == funId)
		{
			if(mModuleConnectStatus == DevicePropertyConstants.MODULE_STATUS_DISCONNECTED)
			{
				Log.i(TAG, "module is disconnected!");
				return;
			}
			if(mWorkMode == WORK_MODE_LASER && 
					mServiceState !=  SERVICE_STATE_DEINITING && mServiceState != SERVICE_STATE_OFF)
			{
				Log.i(TAG,"current mode has been in laser ranging mode");
			}
			else
			{
				Log.i(TAG,"switch to ranging mode");
				mWorkMode = WORK_MODE_LASER;
				mServiceReady = false;
				if(ThreadManager.getSinglePool().getActiveCount() > 0)
				{
					if(mServiceState == SERVICE_STATE_INITING || mServiceState == SERVICE_STATE_DEINITING)
					{
						Log.i(TAG,"threadpoll is working");
					}
					Log.i(TAG,"force exit thread poll");
					ThreadManager.getSinglePool().stop();
				}
				initService(mWorkMode);
			}
		}
		else if(mWorkMode == DevicePropertyConstants.FUN_ID_ALL_OFF)
		{
			deinitService();
		}
		else if(DevicePropertyConstants.FUN_ID_FILLLIGHT_ON == funId)
		{
			enableFillLightDevice(true);
		}
		else if(DevicePropertyConstants.FUN_ID_FILLLIGHT_OFF == funId)
		{
			enableFillLightDevice(false);
		}
	}
	
	private void handleModuleConnectStatusChange(int connStatus)
	{
		Intent cameraPreviewIntent = new Intent(ACTION_MODULE_CONNECT_CHANGE);
		cameraPreviewIntent.putExtra(KEY_CONNECT_STATUS, connStatus);
		cachePropValue(DeviceInterfaceProperties.DIM_MODULE_CONNECT_STATUS_PROPERTY, String.valueOf(mModuleConnectStatus), false);
		if(connStatus == DevicePropertyConstants.MODULE_STATUS_DISCONNECTED)
		{
			sendBroadcast(cameraPreviewIntent);
			mServiceReady = false;
			deinitService();
		}
		else if(connStatus == DevicePropertyConstants.MODULE_STATUS_CONNECTED)
		{
			mServiceReady = false;
			initService(mWorkMode);
			sendBroadcast(cameraPreviewIntent);
		}
	}
	
	private int getModuleConnectState()
	{
		int status = ServiceHelper.stringToIntSafe(ServiceHelper.readDeviceNode(TARGET_DEVICE_INT_NODE_PATH));
		status = Math.min(1, Math.max(0, status));
		return status;
	}
	
	public static class PowerStatusReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals(ACTION_PLATFORM_SLEEP))
			{
				Log.v(TAG, "onReceive MSG_PLATFORM_SLEEP_ACTION");
				mMainHandler.sendEmptyMessage(MSG_PLATFORM_SLEEP);
			}
			else if(intent.getAction().equals(ACTION_PLATFORM_WAKEUP))
			{
				Log.v(TAG, "onReceive MSG_PLATFORM_WAKEUP_ACTION");
				mMainHandler.sendEmptyMessage(MSG_PLATFORM_WAKEUP);
			}
		}
	}
}
