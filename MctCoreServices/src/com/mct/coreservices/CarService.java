package com.mct.coreservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.auth.NTCredentials;

import com.android.internal.util.ArrayUtils;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;
import com.mct.common.MctCoreServicesProvider;
import com.mct.common.MctCoreServicesProviderMetaData;
import com.mct.common.MctSetPropertyFilter;
import com.mct.utils.ServiceHelper;
import com.mct.utils.ThreadManager;
import com.qualcomm.qti.ivi.aidl.IVehicleService;

import android.R.array;
import android.R.integer;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHandsfreeClient;
import android.bluetooth.BluetoothHandsfreeClientCall;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.AutoAcc;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;

public class CarService extends Service 
{
	public 	static final String SERVICE_NAME 	= "com.mct.carservice";
	private 	static final String TAG 					= SERVICE_NAME;	
	
	public static final int		PLATFORM_MCU_TYPE 					= 1;//0 后视镜 1 车机
	public static final int 		VEHICLE_PROPERTY_ID_MIN 		= 0;
	public static final int 		VEHICLE_PROPERTY_ID_MAX 		= 19999;
	public static final int 		MCU_PROPERTY_ID_MIN 				= 20000;
	public static final int 		MCU_PROPERTY_ID_MAX 				= 99999;
	
	
	public static final int 		MSG_RECEIVE_MCU_DATA 		= 	0x01;
	public static final int 		MSG_RECEIVE_VEHICLE_DATA 	= 	0x02;
	public static final int 		MSG_PLATFORM_SLEEP				= 	0x03;
	public static final int 		MSG_PLATFORM_WAKEUP 		= 	0x04;
	public static final int 		MSG_PLAY_BEEP						= 0x09;
	public static final String 	ACTION_PLATFORM_SLEEP		= "com.android.suyoung.sleep";
	public static final String 	ACTION_PLATFORM_WAKEUP 	= "com.android.suyoung.wakeup";
	public static final String 	KEY_DATA 								= 	"Data";
	
	public static final int 		MSG_MCU_UPGRADE_FAILED    = 0xA0;
	public static final String   KEY_UPGRADE_FAILED_COUNT = "upgrade_failed_count";
	
	//按键上报
	private final static String OEM_KEY_CODE 					= "keycode";
	private final static String OEM_KEY_ACTION 				= "keystatus";
	private final static String OEM_KEY_REPEAT_COUNT 	= "keyrepeatcount";
	private final static String ACTION_OEM_KEYEVENT 		= "com.mct.action.oem.keyevent";
		
	//协议ACC广播
	private final static String ACTION_PROTOCOL_ACC_CHANGE  = "com.mct.protocol.acc.change";
	private final static String KEY_ACC_STATE 						  		= "acc_status";	
		
	//警告信息上报
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
	
	//摄像头视频预览
	private final static String ACTION_CAMERA_PREVIEW				= "com.mct.camera_preview";
	private final static String KEY_CAMERA_PREVIEW_ENABLE 		= "camera_preview_enable";//0 预览关闭;1 预览打开;2 由应用决定(当前不在预览界面则预览打开;否则则退出预览界面)

	//右视摄像头视频预览
	private final static String ACTION_RIGHT_CAMERA_PREVIEW				= "com.mct.right_camera_preview";
	private final static String KEY_RIGHT_CAMERA_PREVIEW_ENABLE 		= "right_camera_preview_enable";//0 预览关闭;1 预览打开;2 由应用决定(当前不在预览界面则预览打开;否则则退出预览界面)

		
	//驻车雷达预览
	private final static String ACTION_PARKING_RADAR_PREVIEW			= "com.mct.parking_radar_preview";
	private final static String KEY_PARKING_RADAR_PREVIEW_ENABLE 	= "parking_radar_preview_enable";//0 预览关闭;1 预览打开
	
	//系统按键音广播
	private final static String ACTION_MCT_PLAY_BEEP								= "com.mct.play_beep";
	private final static String KEY_BEEP_STATUS 										= "beep_status";//0结束;1 开始
	 
		
	
	// share prefrence key
	public static final String SHARED_PREFERENCES_NAME 					= "CarService";
	public static final String SHARED_PREFERENCES_KEY_CAR_MODEL 	= "CarModel";
	public static final String SHARED_PREFERENCES_KEY_DVR_TYPE 	= "DvrType";
			
	//private static final int		HISTORY_PROPERTY_CACHE_MAX	= 20;
	
	//蓝牙定义的广播状态
	public static final String ACTION_BLUETOOTH_CALL_CHANGED = "org.codeaurora.handsfreeclient.profile.action.AG_CALL_CHANGED";
	public static final int CALL_STATE_COMMUNICATING = 0; 	
	public static final int CALL_STATE_READY = -1; //when some number have input
	public static final int CALL_STATE_IDLE = -2;
 	public static final int CALL_STATE_HOLD = 1;
 	public static final int CALL_STATE_DIALING = 2;
 	public static final int CALL_STATE_DIAL = 3; //some phone may return this value when dialing
 	public static final int CALL_STATE_INCOMING = 4;
 	public static final int CALL_STATE_WAITING = 5;
 	public static final int CALL_STATE_HELD_BY_RESPONSE_AND_HOLD = 6;
 	public static final int CALL_STATE_TERMINATED = 7;
 	
 	private String mAppVersion = null;
	private Map<Integer, String> 	mPropertiesHistoryCache = new HashMap<Integer, String>();				//属性值缓存
	private SparseArray<Vector<Messenger>> mListeners = new SparseArray<Vector<Messenger>>();		//监听器缓存器
	private PowerStatusReceiver mPowerStatusReceiver = new PowerStatusReceiver();
	private BtCallStatusReceiver mBtCallStatusReceiver = new BtCallStatusReceiver();
	
	private AutoAcc					mAccManager 				= null;
	private static	Handler 			mMainHandler 			= null;
	private static CarService  	mCarService				= null;
	private boolean 				mIsWakeupInit			= false;
	private boolean 				mIsServiceInit				= false;
	private boolean 				mIsServiceStartup       = false;
	private MctVehicleManager 	mMcuManager 			= null;
	private MctVehicleManager 	mMctVehicleManager 	= null;
	private boolean mIsExistQuacommVehicleService 	= false;
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// 接口对象
	private StubBinder mBinder = null;
    
	
    public class BtCallStatusReceiver extends BroadcastReceiver
	{
		public void onReceive(final Context context, final Intent intent)
		{
			Log.i(TAG, "receive broadcast message:"+intent.getAction());
			if(!mIsServiceStartup)
			{
				Log.w(TAG, "coreservice has not startup!");
				return;
			}
			if(intent.getAction().equals(ACTION_BLUETOOTH_CALL_CHANGED))
			{
				if(mMcuManager == null)
				{
					Log.e(TAG, "mcu manager is destroy!");
					return;
				}
				BluetoothHandsfreeClientCall hfpCall = (BluetoothHandsfreeClientCall) intent.getParcelableExtra(BluetoothHandsfreeClient.EXTRA_CALL);
				if(hfpCall == null)
				{
					return;
				}
				int state = hfpCall.getState();
				Log.i(TAG, "receive call state:"+state);
				switch (state) 
				{
					case CALL_STATE_COMMUNICATING:
						mMcuManager.setPropValue(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY, "4");
						break;
					case CALL_STATE_DIAL:
						mMcuManager.setPropValue(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY, "3");
						break;
					case CALL_STATE_DIALING:
						mMcuManager.setPropValue(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY, "3");
						break;
					case CALL_STATE_INCOMING:
						mMcuManager.setPropValue(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY, "2");
						break;
					case CALL_STATE_TERMINATED:
						mMcuManager.setPropValue(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY, "5");
						break;
				}
				if(mMctVehicleManager != null)
				{
					String btPhoneParam = VehiclePropertyConstants.formatBtPhoneInfoString(1, state, hfpCall.getNumber());
					mMctVehicleManager.setPropValue(VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY, btPhoneParam);
				}
			}
			else if(intent.getAction().equals(BluetoothHandsfreeClient.ACTION_CONNECTION_STATE_CHANGED)
					&& mMctVehicleManager != null)
			{
				int hfpConnStatus = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
				String btPhoneParam = VehiclePropertyConstants.formatBtPhoneInfoString(hfpConnStatus == BluetoothHandsfreeClient.STATE_CONNECTED?1:0,CALL_STATE_IDLE, "");
				mMctVehicleManager.setPropValue(VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY, btPhoneParam);
			}
		}
	}
    
	public class StubBinder extends IVehicleService.Stub
	{

		@Override
		public int[] getSupportedSignalIds() throws RemoteException
		{
			// TODO Auto-generated method stub
			int []mcuSupportedPropertyIs = null;
			int []obdSupportedPropertyIs = null;
			if(mMcuManager != null)
			{
				mcuSupportedPropertyIs = mMcuManager.getSupportedPropertyIds();
			}
			if(mMctVehicleManager != null)
			{
				obdSupportedPropertyIs = mMctVehicleManager.getSupportedPropertyIds();
			}
			return ServiceHelper.combineArray(mcuSupportedPropertyIs,obdSupportedPropertyIs);
		}
		
		@Override
		public int[] getWritableSignalIds() throws RemoteException
		{
			// TODO Auto-generated method stub
			int []mcuWritablePropertyIds = null;
			int []obdWritablePropertyIds = null;
			if(mMcuManager != null)
			{
				mcuWritablePropertyIds = mMcuManager.getWritablePropertyIds();
			}
			if(mMctVehicleManager != null)
			{
				obdWritablePropertyIds = mMctVehicleManager.getWritablePropertyIds();
			}
			return ServiceHelper.combineArray(mcuWritablePropertyIds,obdWritablePropertyIds);
		}

		@Override
		public int[] getSignalsDataType(int[] propIds) throws RemoteException
		{
			// TODO Auto-generated method stub
			if (propIds == null || propIds.length == 0) { return null; }
			int[] properiesDataType = new int[propIds.length];
			for (int i = 0; i < propIds.length; i++)
			{
				//OBD Properity ID Range
				if(propIds[i] >= VEHICLE_PROPERTY_ID_MIN && propIds[i] <= VEHICLE_PROPERTY_ID_MAX )
				{
					if(mMctVehicleManager != null)
					{
						properiesDataType[i] = mMctVehicleManager.getPropertyDataType(propIds[i]);
					}
				}
				//MCU Properity ID Range
				else if(propIds[i] >= MCU_PROPERTY_ID_MIN && propIds[i] <= MCU_PROPERTY_ID_MAX )
				{
					if(mMcuManager != null)
					{
						properiesDataType[i] = mMcuManager.getPropertyDataType(propIds[i]);
					}
				}
			}
			return properiesDataType;
		}
		
		@Override
		public int getSignalDataType(int propId) throws RemoteException
		{
			// TODO Auto-generated method stub
			//OBD Properity ID Range
			if(propId >= VEHICLE_PROPERTY_ID_MIN && propId <= VEHICLE_PROPERTY_ID_MAX )
			{
				if(mMctVehicleManager != null)
				{
					return mMctVehicleManager.getPropertyDataType(propId);
				}
			}
			//MCU Properity ID Range
			else if(propId >= MCU_PROPERTY_ID_MIN && propId <= MCU_PROPERTY_ID_MAX )
			{
				if(mMcuManager != null)
				{
					return mMcuManager.getPropertyDataType(propId);
				}
			}
			return VehiclePropertyConstants.DATA_TYPE_UNKNOWN;
		}

		
		@Override
		public boolean setSignal(int propId, String value) throws RemoteException
		{
			// TODO Auto-generated method stub
			//过滤频繁调用
			if(!MctSetPropertyFilter.getInstance().canAccess(propId, value))
			{
				Log.i(TAG, "filter set action,propId:"+propId+",value:"+value);
				return false;
			}
			if(propId >= VEHICLE_PROPERTY_ID_MIN && propId <= VEHICLE_PROPERTY_ID_MAX )
			{
				if(mMctVehicleManager != null)
				{
					return mMctVehicleManager.setPropValue(propId, value);
				}
			}
			//MCU Properity ID Range
			else if(propId >= MCU_PROPERTY_ID_MIN && propId <= MCU_PROPERTY_ID_MAX )
			{
				if(mMcuManager != null)
				{
					return mMcuManager.setPropValue(propId, value);
				}
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
				//过滤频繁调用
				if(!MctSetPropertyFilter.getInstance().canAccess(propIds[i], propValues[i]))
				{
					Log.i(TAG, "filter set action,propId:"+propIds[i]+",value:"+propValues[i]);
					return false;
				}
				//OBD Properity ID Range
				if(propIds[i] >= VEHICLE_PROPERTY_ID_MIN && propIds[i] <= VEHICLE_PROPERTY_ID_MAX )
				{
					if(mMctVehicleManager != null)
					{
						mMctVehicleManager.setPropValue(propIds[i], propValues[i]);
					}
				}
				//MCU Properity ID Range
				else if(propIds[i] >= MCU_PROPERTY_ID_MIN && propIds[i] <= MCU_PROPERTY_ID_MAX )
				{
					if(mMcuManager != null)
					{
						mMcuManager.setPropValue(propIds[i], propValues[i]);
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
		Log.i(TAG, SERVICE_NAME + " OnCreate:"+SystemClock.uptimeMillis());
		mBinder = new StubBinder();
		JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_START);
		
		mCarService = this;
		mIsWakeupInit = false;
		mIsServiceInit = false;
		mIsServiceStartup = false;
		mAppVersion = ServiceHelper.getAppVersionName(getApplicationContext());
		mIsExistQuacommVehicleService = ServiceHelper.isServiceExist(getApplicationContext(), QualcommVehicleManager.QUALCOMM_SERVICE_NAME);
		
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(ACTION_PLATFORM_SLEEP);
		iFilter.addAction(ACTION_PLATFORM_WAKEUP);
		iFilter.addAction(ACTION_MCT_PLAY_BEEP);
		registerReceiver(mPowerStatusReceiver, iFilter);

		IntentFilter intentFilter = new IntentFilter(); 
		intentFilter.addAction(ACTION_BLUETOOTH_CALL_CHANGED);
		intentFilter.addAction(BluetoothHandsfreeClient.ACTION_CONNECTION_STATE_CHANGED);
		this.registerReceiver(mBtCallStatusReceiver, intentFilter);
		
		mMainHandler = new Handler(this.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				switch (msg.what)
				{
					case MSG_RECEIVE_MCU_DATA:
					{
						if(mMcuManager != null)
						{
							mMcuManager.onLocalReceive(msg.getData());
						}
						break;
					}
					case MSG_RECEIVE_VEHICLE_DATA:
					{
						if(mMctVehicleManager != null)
						{
							mMctVehicleManager.onLocalReceive(msg.getData());
						}
						break;
					}
					case MSG_PLATFORM_SLEEP:
						mIsWakeupInit = false;
						deinitService();
						break;
					case MSG_PLATFORM_WAKEUP:
						mIsWakeupInit = true;
						initService();
						break;
					case MSG_MCU_UPGRADE_FAILED:
						int failedCount = msg.arg1;
						if(failedCount <= HeadUnitMcuManager.MAX_RETRY_COUNT && mMcuManager != null)
						{
							Log.w(TAG, "retry to mcu update count:"+failedCount);
							if(!mMcuManager.setPropValue(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_UPGRADE)))
							{
								Log.e(TAG, "retry mcu update failed!");
								dispatchData(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED));
							}
						}
						break;
					case MSG_PLAY_BEEP:
						
						break;
					default:
						break;
				}
			}
		};
		
		try
		{
			//WakeUp状态
			mAccManager 	= new AutoAcc();
			if(mAccManager != null && mAccManager.getState() == 1)
			{
				initService();
			}
			else
			{
				Log.w(TAG, "platform is in sleep status");
				if(PLATFORM_MCU_TYPE == 0)
				{
					//加强关屏
					Log.i(TAG, "Close Screen BackLight");
					ServiceHelper.enableMirrorScreen(false);
				}
				JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_RESUME);
				mIsServiceStartup = true;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			//处理在不支持autoacc的环境中的异常,默认是在acc on状态启动
			e.printStackTrace();
			initService();
		}
		
	}

	@Override
	public void onDestroy()
	{
		JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_PAUSE);
		ThreadManager.getSinglePool().stop();
		if(mPowerStatusReceiver != null)
		{
			unregisterReceiver(mPowerStatusReceiver);
		}
		if(mBtCallStatusReceiver != null)
		{
			unregisterReceiver(mBtCallStatusReceiver);
		}
		if(mMcuManager != null)
		{
			mMcuManager.onDeinitManager();
			mMcuManager = null;
		}
		if(mMctVehicleManager != null)
		{
			mMctVehicleManager.onDeinitManager();
			mMctVehicleManager = null;
		}
		mIsServiceStartup = false;
		JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_STOP);
		Log.i(TAG, SERVICE_NAME + " onDestroy");
		super.onDestroy();
	}
	
	public Handler getMainHandler()
	{
		return mMainHandler;
	}
	
	public boolean isWakeupInit()
	{
		return mIsWakeupInit;
	}
	
	public String getCacheValue(int propId)
	{
		return mPropertiesHistoryCache.get(propId);
	}
	
	private synchronized void initService()
	{
		ThreadManager.getSinglePool().execute(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i(TAG,"initService");
				/*ContentValues values = new ContentValues();
				values.put(MctCoreServicesProviderMetaData.COLUMN_PROP_ID, 0);
				values.put(MctCoreServicesProviderMetaData.COLUMN_PROP_VALUE, String.valueOf(MctCoreServicesProvider.DB_VERSION));
				getContentResolver().insert(MctCoreServicesProviderMetaData.APP_CONFIG_CONTENT_URI, values);
				*/
				if(mIsServiceInit && mMcuManager != null && mMctVehicleManager != null)
				{
					Log.w(TAG, "mcu service has been init!");
					return;
				}
				Log.i(TAG, "CarService Version:"+mAppVersion);
				mMcuManager = null;
				mMctVehicleManager = null;
				if(PLATFORM_MCU_TYPE == 0)
				{
					mMcuManager = new MirrorMcuManager();
				}
				else if(PLATFORM_MCU_TYPE == 1)
				{
					mMcuManager = new HeadUnitMcuManager();
				}
				
				//根据高通service是否存在来初始化具体哪个原车manager
				if(mIsExistQuacommVehicleService)
				{
					Log.i(TAG,"create quacomm vehicle manager instance");
					mMctVehicleManager = new QualcommVehicleManager();
				}
				else
				{
					//后视镜使用OBD获取原车信息
					if(PLATFORM_MCU_TYPE == 0)
					{
						Log.i(TAG,"create obd manager instance");
						mMctVehicleManager = new OBDManager();
					}
					//无高通原车信息支持的车机项目，使用CAN协议获取原车数据
					else if(PLATFORM_MCU_TYPE == 1)
					{
						Log.i(TAG,"create can manager instance");
						mMctVehicleManager = new CanManager();
					}
				}
				
				if(mMcuManager != null)
				{
					if(mMcuManager.onInitManager(mCarService))
					{
						Log.i(TAG, "mcu manager init success");
					}
					else
					{
						Log.e(TAG, "mcu manager init failed");
						mMcuManager = null;
					}
				}
				
				if(mMctVehicleManager != null)
				{
					if(mMctVehicleManager.onInitManager(mCarService))
					{
						Log.i(TAG, "vehicle manager init success");
					}
					else
					{
						Log.e(TAG, "vehicle manager init failed");
						mMctVehicleManager = null;
					}
				}
				if(!mIsWakeupInit)
				{
					JinneeApplication.setCarService(SERVICE_NAME, mBinder, JinneeApplication.ACTION_SERVICE_RESUME);
				}
				mIsServiceInit =  true;
				mIsServiceStartup = true;
			}
		});
	}
	
	private synchronized void deinitService()
	{
		ThreadManager.getSinglePool().execute(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i(TAG,"deinitService");
				
				if(mMctVehicleManager != null)
				{
					mMctVehicleManager.onDeinitManager();
					mMctVehicleManager = null;
				}
				
				if(mMcuManager != null)
				{
					mMcuManager.onDeinitManager();
					mMcuManager = null;
				}
				
				mIsServiceInit = false;
			}
		});
	}
	
	
	// 缓存属性值
	public synchronized void cachePropValue(int propId, String value,boolean bDispatch)
	{
		Log.i(TAG, "cachePropValue,PropId:" + propId + ",Value:" + value);
		String cacheValue = mPropertiesHistoryCache.put(propId, value);
		if(propId == VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY)
		{
			try
			{
				int keycode = Integer.valueOf(value);
				broadcastKeyEvent(keycode, KeyEvent.ACTION_DOWN, 0);
				broadcastKeyEvent(keycode, KeyEvent.ACTION_UP, 0);
			} catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		//警告信息
		else if(propId == VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY)
		{
			broadcastVehicleAlertInfo(ServiceHelper.stringToIntArray(value));
		}
		else if(propId == VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY ||
				propId == VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY ||
				propId == VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY )
		{
			broadcastVehicleBaseInfo(propId, Integer.valueOf(value));
		}
		else if(propId == VehicleInterfaceProperties.VIM_MCU_ACC_STAT_PROPERTYE)
		{
			broadcastAccStatus(Integer.valueOf(value));
		}
		if(bDispatch || cacheValue == null || !cacheValue.equals(value))
		{
			dispatchData(propId, value);
		}
	}	
		
	public synchronized void cachePropValueOnly(int propId, String value)
	{
		Log.i(TAG, "cachePropValueOnly,PropId:" + propId + ",Value:" + value);
		mPropertiesHistoryCache.put(propId, value);
	}	
	
	public MctVehicleManager getMcuManager()
	{
		return mMcuManager;
	}
	
	public MctVehicleManager getVehicleManager()
	{
		return mMctVehicleManager;
	}
	
	public String loadSettings(String key)
	{
		return getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(key, null);
	}
	
	public boolean saveSettings(String key,String value)
	{
		Log.i(TAG, "save settings,key:"+key+",value:"+value);
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public void broadcastKeyEvent(int key,int action,int pressCount)
	{
		Log.i(TAG, "broadcastKeyEvent,key:"+key+",action:"+action);
		final Intent keycodesIntent = new Intent(ACTION_OEM_KEYEVENT);
        keycodesIntent.putExtra(OEM_KEY_CODE, key);
        keycodesIntent.putExtra(OEM_KEY_ACTION, action == KeyEvent.ACTION_DOWN?true:false);
        keycodesIntent.putExtra(OEM_KEY_REPEAT_COUNT, pressCount);
        sendBroadcast(keycodesIntent);
	}
	
	public void broadcastVehicleAlertInfo(int[] alertIds)
	{
		if(alertIds == null || alertIds.length == 0)
		{
			return;
		}
		final Intent alertInfoIntent = new Intent(ACTION_VEHICLE_ALERT_INFO);
		alertInfoIntent.putExtra(KEY_VEHICLE_ALERT_ID_ARRAY, alertIds);
        sendBroadcast(alertInfoIntent);
	}
	
	public void broadcastVehicleBaseInfo(int propId,int value)
	{
		Log.i(TAG, "broadcastVehicleBaseInfo,propId:"+propId+",value:"+value);
		Intent vehicleIntent = null;
		switch(propId)
		{
			case VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY:
				vehicleIntent = new Intent(ACTION_VEHICLE_REVERSE_INFO);
				vehicleIntent.putExtra(KEY_VEHICLE_REVERSE_STATUS, value);
				break;
			case VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY:
				vehicleIntent = new Intent(ACTION_VEHICLE_ILL_INFO);
				vehicleIntent.putExtra(KEY_VEHICLE_ILL_STATUS, value);
				break;
			case VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY:
				vehicleIntent = new Intent(ACTION_VEHICLE_BRAKE_INFO);
				vehicleIntent.putExtra(KEY_VEHICLE_BRAKE_STATUS, value);
				break;
		}
		if(vehicleIntent != null)
		{
			sendBroadcast(vehicleIntent);
		}
	}
	
	public void broadcastVehicleDoorInfo(int flStatus,int frStatus,int rlStatus,int rrStatus,int trunkStatus,int hoodStatus)
	{
		Log.i(TAG, "broadcastVehicleDoorInfo,FL:"+flStatus+",FR:"+frStatus+",RL:"+rlStatus+",RR:"+rrStatus+",TRUNK:"+trunkStatus+",HOOD:"+hoodStatus);
		Intent vehicleDoorIntent = new Intent(ACTION_VEHICLE_DOOR_INFO);
		vehicleDoorIntent.putExtra(KEY_FRONT_LEFT_DOOR_STATUS, flStatus);
		vehicleDoorIntent.putExtra(KEY_FRONT_RIGHT_DOOR_STATUS, frStatus);
		vehicleDoorIntent.putExtra(KEY_REAR_LEFT_DOOR_STATUS, rlStatus);
		vehicleDoorIntent.putExtra(KEY_REAR_RIGHT_DOOR_STATUS, rrStatus);
		vehicleDoorIntent.putExtra(KEY_TRUNK_STATUS, trunkStatus);
		vehicleDoorIntent.putExtra(KEY_HOOD_STATUS, hoodStatus);
        sendBroadcast(vehicleDoorIntent);
	}
	
	public void broadcastRadarPreview(int enable)
	{
		Log.i(TAG, "broadcastRadarPreview,enable:"+enable);
		Intent radarPreviewIntent = new Intent(ACTION_PARKING_RADAR_PREVIEW);
		radarPreviewIntent.putExtra(KEY_PARKING_RADAR_PREVIEW_ENABLE, enable);
        sendBroadcast(radarPreviewIntent);
	}
	
	public void broadcastCameraPreview(int param)
	{
		Log.i(TAG, "broadcastCameraPreview,enable:"+param);
		Intent cameraPreviewIntent = new Intent(ACTION_CAMERA_PREVIEW);
		cameraPreviewIntent.putExtra(KEY_CAMERA_PREVIEW_ENABLE, param);
        sendBroadcast(cameraPreviewIntent);
	}
	
	public void broadcastRightCameraPreview(int param)
	{
		Log.i(TAG, "broadcastRightCameraPreview,enable:"+param);
		Intent cameraPreviewIntent = new Intent(ACTION_RIGHT_CAMERA_PREVIEW);
		cameraPreviewIntent.putExtra(KEY_RIGHT_CAMERA_PREVIEW_ENABLE, param);
        sendBroadcast(cameraPreviewIntent);
	}
	
	public void broadcastAccStatus(int accStatus)
	{
		Log.i(TAG, "broadcastAccStatus,accStatus:"+accStatus);
		Intent vehicleDoorIntent = new Intent(ACTION_PROTOCOL_ACC_CHANGE);
		vehicleDoorIntent.putExtra(KEY_ACC_STATE, accStatus);
        sendBroadcast(vehicleDoorIntent);
	}
	
	public void broadcastAirConditionInfo(int showAcBar)
	{
		Log.i(TAG, "broadcastAirConditionInfo,showAcBar:"+showAcBar);
		Intent acIntent = new Intent(ACTION_AIR_CONDITION_INFO);
		acIntent.putExtra(KEY_DISPLAY_AC_BAR, showAcBar);
        sendBroadcast(acIntent);
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
	
	public void clearHistoryCache(int startPropId,int endPropId,int[] excludePropIds)
	{
		Log.i(TAG, "clearVehicleCache begin:"+SystemClock.uptimeMillis());
		for(int i=startPropId;i<=endPropId;i++)
		{
			if(excludePropIds == null || !ArrayUtils.contains(excludePropIds, i))
			{
				if(mPropertiesHistoryCache.containsKey(i) && mPropertiesHistoryCache.get(i) != null)
				{
					mPropertiesHistoryCache.put(i, null);
				}
			}
		}
		Log.i(TAG, "clearVehicleCache end:"+SystemClock.uptimeMillis());
	}
	
	public class PowerStatusReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(!mIsServiceStartup)
			{
				Log.w(TAG, "coreservice has not startup!");
				return;
			}
			if(intent.getAction().equals(ACTION_PLATFORM_SLEEP))
			{
				Log.v(TAG, "onReceive MSG_PLATFORM_SLEEP_ACTION");
				if(mMainHandler != null)
				{
					mMainHandler.sendEmptyMessage(MSG_PLATFORM_SLEEP);
				}
			}
			else if(intent.getAction().equals(ACTION_PLATFORM_WAKEUP))
			{
				Log.v(TAG, "onReceive MSG_PLATFORM_WAKEUP_ACTION");
				if(mMainHandler != null)
				{
					mMainHandler.sendEmptyMessage(MSG_PLATFORM_WAKEUP);
				}
			}
			else if(intent.getAction().equals(ACTION_MCT_PLAY_BEEP))
			{
				int beepStatus = intent.getIntExtra(KEY_BEEP_STATUS, 0);
				Log.v(TAG, "onReceive ACTION_MCT_PLAY_BEEP,param:"+beepStatus);
				if(mMcuManager != null && beepStatus == 1)
				{
					mMcuManager.setPropValue(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_PLAY_KEY_SPEAKER));
				}
			}
		}
	}
}
