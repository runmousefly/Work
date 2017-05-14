package com.mct.coreservices;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.android.internal.util.ArrayUtils;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.serial.headunit.McuSerial;
import com.mct.tpms.TPMSManager;
import com.mct.utils.ServiceHelper;
import com.mct.utils.ThreadManager;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView.Validator;

public class HeadUnitMcuManager extends MctVehicleManager implements McuSerial.Listener
{
	public static String TAG = "HeadUnitMcuManager";
	public static String TAG1 = "AudioTest";
		
	private CarService mService = null;
	private DVRManager mDVRManager = null;
	private TPMSManager mTpmsManager = null;
	private McuSerial mSerial = null;
	private boolean mbWaitReturn = false;
	private ContentObserver mBrightnessObserver = null;

	private LinkedBlockingQueue<int[]> mlsReturn = new LinkedBlockingQueue<int[]>(); // 阻塞型队列，用于等待数据
	private boolean mSerialConnect = false;
	private final int MCU_UPDATE_SUCCESS 	= 1000;
	private final int MCU_UPDATE_FAILED 		= 1001;
	public static final int MAX_RETRY_COUNT = 5;
	private int mMcuUpgradeState = VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN;
	
	private String mMcuUpgradePath = null;
	private int mMcuUpgradeFailedCount = 0;
	
	private static final int  HDR_SIZE = 128;
	private static final int  PRODUCT_NAME_LEN = 32;
	private static final int  HDR_VERSION = 2;
	private static final int  HDR_MARK = 0x55AA55AA;
	private final int MCU_UPGRADE_FRAME_SIZE = 1024;
	private final String PRODUCT_NAME = "D1002A";
	private final String MCU_UPGRADE_THREAD_NAME = "HeadUnitMcuUpgradeThread";
	
	private int mCurRadioBand = VehiclePropertyConstants.RADIO_BAND_FM1;
	private int mCurRadioRegion = VehiclePropertyConstants.RADIO_REGION_CHINA;
	private int mCurRadioFreq = 0;
	private int mPressedKey = VehiclePropertyConstants.USER_KEY_UNKNOWN;
	private long mSWCKeyPressedTime = 0;	
	private int mUIMode = -1;
	private int mMediaMode = -1;
	private long mSetSourceTime = 0;
	private Timer mSourceSyncTimer = null;
	private boolean mEnableSyncBackLight = false;
	private int mBrightnessBeforeILLOn = 0;	//大灯开之前的亮度值
	
	private KeyEvent mCurrentKeyEvent = null;
	private long mCurrentKeyPressedTime = 0;
	private int mCurrentKeyPressedCount = 0;
	
	private boolean mReverseStatus = false;
	
	private AudioManager mAudioManager = null;
	private int mMixEnable = 1;
	private int mMixLevel = 7;
	private int mMediaStreamVolume = -1;
	
	
	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitMcuManager");
		mService = service;
		mSerial = new McuSerial();
		if(mDVRManager == null)
		{
			mDVRManager = new DVRManager();
		}
		if(mTpmsManager == null)
		{
			mTpmsManager = new TPMSManager();
		}
		
		mAudioManager = new AudioManager(service.getApplicationContext());
		//mMixEnable = mService.loadSettingsToInt(CarService.SHARED_PREFERENCES_KEY_MIX_ENABLE);
		//mMixLevel = mService.loadSettingsToInt(CarService.SHARED_PREFERENCES_KEY_MIX_LEVEL);
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_MCU_GPS_MIX_ENABLE_PROPERTY, String.valueOf(mMixEnable));
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_MCU_GPS_MIX_LEVEL_PROPERTY, String.valueOf(mMixLevel));
		
		mSerial.init(ServiceHelper.DEFAULT_PACKAGE_LEN, ServiceHelper.WAIT_MINIMUM, ServiceHelper.WAIT_MINIMUM, 1);
		mSerial.requestListener(this);
		
		Log.i(TAG, "begin to open serial");
		// 初始化串口数据收发配置
		for (int i = 0; i < ServiceHelper.REPEAT_MAXIMUM; ++i)
		{
			try
			{
				if (mSerial.open() != 0)
				{
					Log.i(TAG, "open serial success,and retry count:" + i);
					mSerialConnect = true;
					postData(HeadUnitMcuProtocol.AM_CMD_SYS_INIT_OK, new int[]{0});
					if(mDVRManager != null)
					{
						mDVRManager.onInitManager(service);
					}
					if(mTpmsManager != null)
					{
						mTpmsManager.onInitManager(service);
					}
					//恢复关机亮度
					//recoveryBacklightBrightness();
					//倒车状态/大灯状态
					reqMcuInitStatus();
					//启动源同步计时器
					startSourceSyncTimer();
					break;
				}
				else
				{
					mSerial.close();
				}
				Thread.sleep(ServiceHelper.WAIT_ELAPSE);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		Log.i(TAG, "finish to open serial");
		//监测系统背光亮度值
		if(mBrightnessObserver == null)
		{
			mBrightnessObserver = new ContentObserver(mService.getMainHandler()) 
			{
		        @Override
		        public void onChange(boolean selfChange) 
		        {
		        	int brightness = getSystemBacklightBrightness();
		        	int sysBrightness = (int)((brightness/255.000)*100);
		        	//+1误差，防止误差积累
		        	if(sysBrightness % 51 != 0)
		        	{
		        		sysBrightness += 1; 
		        	}
		        	if(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY).equals("1"))
		        	{
		        		sysBrightness *= 0.4;
		        	}
	        		Log.i(TAG, "android system backlight brightness value change:"+brightness+",and set mcu brightness:"+sysBrightness);
					setPropValue(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY,String.valueOf(sysBrightness));
		        }
			};
			mService.getContentResolver().registerContentObserver(
					Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), false, mBrightnessObserver);
		}
		return true;
	}

	@Override
	public boolean onDeinitManager()
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onDeinitManager");
		mAudioManager = null;
		stopSourceSyncTimer();
		if(mService != null && mBrightnessObserver != null)
		{
			mService.getContentResolver().unregisterContentObserver(mBrightnessObserver);
		}
		if(mDVRManager != null)
		{
			mDVRManager.onDeinitManager();
		}
		if(mTpmsManager != null)
		{
			mTpmsManager.onDeinitManager();
		}
		if (mSerial != null && mSerialConnect)
		{
			if(mSerial.close() > 0)
			{
				mSerialConnect = false;
			}
			mSerial = null;
			//mService = null;
		}
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return HeadUnitMcuProtocol.HEADUNIT_MCU_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < HeadUnitMcuProtocol.HEADUNIT_MCU_PROPERITIES.length; i++)
		{
			if (HeadUnitMcuProtocol.getProperityPermission(HeadUnitMcuProtocol.HEADUNIT_MCU_PROPERITIES[i]) >= HeadUnitMcuProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(HeadUnitMcuProtocol.HEADUNIT_MCU_PROPERITIES[i]);
			}
		}
		int[] retArray = new int[writableProps.size()];
		for (int i = 0; i < retArray.length; i++)
		{
			retArray[i] = writableProps.get(i);
		}
		return retArray;
	}

	@Override
	public int getPropertyDataType(int propId)
	{
		// TODO Auto-generated method stub
		return HeadUnitMcuProtocol.getProperityDataType(propId);
	}

	//恢复亮度
	private void recoveryBacklightBrightness()
	{
		int brightness = getSystemBacklightBrightness();
		Log.i(TAG, "recoveryBacklightBrightness:"+brightness);
		setPropValue(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY,String.valueOf((int)((brightness/255.000)*100)));
	}
	
	//获取系统屏幕亮度值
	private int getSystemBacklightBrightness()
	{
		int brightness = -1;
		try
		{
			brightness = Settings.System.getInt(mService.getContentResolver(),
			        Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return brightness;
	}
	
	//同步系统亮度值(mcu->sys,-1误差)
	private void syncSystemBacklightBrightness(int brightness)
	{
		int sysBrightness = getSystemBacklightBrightness();
		int mcuBrightness = (int)((brightness/100.000)*255);
		if(sysBrightness != mcuBrightness)
		{
			Log.i(TAG,"syncSystemBacklightBrightness,cur:"+sysBrightness+",set:"+mcuBrightness);
			Settings.System.putInt(mService.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, mcuBrightness);
//			PowerManager powerManager = (PowerManager)mServiceo.getSystemService(Context.POWER_SERVICE);
//			if(powerManager != null)
//			{
//				powerManager.setBacklightBrightness(mcuBrightness);
//			}
		}
	}
  
	private void reqMcuInitStatus()
	{
		//mEnableSyncBackLight = true;
		//请求背光亮度
		//postData(HeadUnitMcuProtocol.AM_CMD_REQ_BACKLIGHT_BRIGHTNESS, new int[]{0x01});
		//请求倒车状态
		//postData(HeadUnitMcuProtocol.AM_CMD_REQ_REVERSE_STATUS, new int[]{0x00});
		//请求大灯状态
		//postData(HeadUnitMcuProtocol.AM_CMD_REQ_ILL_STATUS, new int[]{0x00});
		//请求背光开关状态
		postData(HeadUnitMcuProtocol.AM_CMD_REQ_BACKLIGHT_STATUS, new int[]{0x01});
		//请求ACC
		postData(HeadUnitMcuProtocol.AM_CMD_REQ_ACC_STATUS, new int[]{0x00});
		//请求媒体音量
		postData(HeadUnitMcuProtocol.AM_CMD_REQ_VOLUME,new int[]{0x00});
	}
	
	//启动源同步定时器
	//协议要求1s定时发送，暂改为3s,避免浪费资源
	private void startSourceSyncTimer()
	{
		if(mSourceSyncTimer != null)
		{
			Log.w(TAG, "startSourceSyncTimer,timer is exist!");
			return;
		}
		Log.i(TAG, "startSourceSyncTimer");
		//3S定时发送
		mSourceSyncTimer = new Timer();
		mSourceSyncTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				long curTime = SystemClock.uptimeMillis();
				if(mMediaMode != -1 && mUIMode != -1 && (curTime - mSetSourceTime) > 2000)
				{
					Log.i(TAG, "Retry to set source,uiMode:"+mUIMode+",mediaMode:"+mMediaMode+",time:"+SystemClock.uptimeMillis());
					postData(HeadUnitMcuProtocol.AM_CMD_UI_MODE, new int[]{Integer.valueOf(mUIMode)});
					postData(HeadUnitMcuProtocol.AM_CMD_MEDIA_MODE, new int[]{Integer.valueOf(mMediaMode)});
				}
			}
		}, 5,3000);
	}
	
	private void syncVehicleMediaInfo()
	{
		/*
		int band = 0;
		int freq = 0;
		if(mMediaMode == VehiclePropertyConstants.MEDIA_MODE_TUNER)
		{
			band = ServiceHelper.stringToIntSafe(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY));
			freq = ServiceHelper.stringToIntSafe(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));
		}
		String mediaInfoParam = VehiclePropertyConstants.formatSourceInfoString(mMediaMode, -1, -1,-1,-1,0,"",band,freq);
		mService.getVehicleManager().setPropValue(VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY, mediaInfoParam);
		*/
	}
	
	private void syncVehicleMediaVolume(int mediaVolume)
	{
		MctVehicleManager vehicleManager = mService.getVehicleManager();
		if(vehicleManager != null)
		{
			vehicleManager.setPropValue(VehicleInterfaceProperties.VIM_AUDIO_CONTROL_VOLUME_PROPERTY, String.valueOf(mediaVolume));
			vehicleManager.setPropValue(VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY, String.valueOf(mediaVolume));
		}
	}
	
	
	//停止源同步定时器
	private void stopSourceSyncTimer()
	{
		if(mSourceSyncTimer != null)
		{
			Log.i(TAG, "stopSourceSyncTimer");
			mSourceSyncTimer.cancel();
			mSourceSyncTimer.purge();
			mSourceSyncTimer = null;
		}
	}
	
	//大灯状态亮度自动更新
	private void onHandleIllStatusChange(boolean bLightOn)
	{
		mBrightnessBeforeILLOn = getSystemBacklightBrightness();
		Log.i(TAG, "onHandleIllStatusChange:"+bLightOn+",current:"+mBrightnessBeforeILLOn);
		int brightness = getSystemBacklightBrightness();
    	int sysBrightness = (int)((brightness/255.000)*100);
    	//+1误差，防止误差积累
    	if(sysBrightness % 51 != 0)
    	{
    		sysBrightness += 1; 
    	}
    	if(bLightOn)
    	{
    		sysBrightness *= 0.4;
    	}
		setPropValue(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY,String.valueOf(sysBrightness));
	}
	
	public boolean isInReverseStatus()
	{
		return mReverseStatus;
	}
	
	public int getCurrentMediaMode()
	{
		return mMediaMode;
	}
	
	//初始化CAN解析协议
	public boolean onInitCanProtocol(CanBoxProtocol protocol)
	{
		Log.i(TAG, "onInitCanProtocol");
		int []param = new int[16];
		param[0] = protocol.getLengthFlag();
		int[] canHead = protocol.getHeadFlag();
		param[1] = canHead[0];
		param[2] = canHead[1];
		param[3] = canHead[2];
		int[] baudRate = protocol.getBaudRateFlag();
		param[4] = baudRate[0];
		param[5] = baudRate[1];
		param[6] = baudRate[2];
		param[7] = baudRate[3];
		param[8] = protocol.getChecksumAddLengthFlag();
		param[9] = protocol.getChecksumAddCmdFlag();
		param[10] = protocol.getChecksumModeFlag();
		param[11] = protocol.getChecksumExternFlag();
		param[12] = protocol.getNeedAckFlag();
		param[13] = protocol.getOkAckFlag();
		param[14] = protocol.getFaliedAckFlag();
		param[15] = 0x00;
		param[15] |= (protocol.getCheckSumLength() & 0x03);
		param[15] |= ((protocol.getDataLengthMode() << 2) & 0x0C);
 		return postData(HeadUnitMcuProtocol.AM_CMD_CAN_CONFIG, param);
	}

	private void onHandleKey(int key,int action)
	{
		Log.i(TAG, "onHandleKey,key:"+key+",actoion:"+action);
		//直接上报弹起或者旋钮动作
		if(mCurrentKeyEvent == null && 
				(action == HeadUnitMcuProtocol.KEY_ACTION_UP ||
				action == HeadUnitMcuProtocol.KEY_ACTION_UNKNOWN))
		{
			//上报按下与弹起
			if(mService != null)
			{
				mService.broadcastKeyEvent(key,KeyEvent.ACTION_DOWN,0);
				mService.broadcastKeyEvent(key,KeyEvent.ACTION_UP,0);
			}
			
		}
		//首次按下
		else if(mCurrentKeyEvent == null && action == HeadUnitMcuProtocol.KEY_ACTION_DOWN)
		{
			//上报按下
			mCurrentKeyPressedTime = SystemClock.uptimeMillis();
			mCurrentKeyEvent = new KeyEvent(mCurrentKeyPressedTime, mCurrentKeyPressedTime, KeyEvent.ACTION_DOWN, key, 0);
			if(mService != null)
			{
				mService.broadcastKeyEvent(key,KeyEvent.ACTION_DOWN,0);
			}
			
		}
		//重复按下(500ms上报一次)
		else if(mCurrentKeyEvent != null && mCurrentKeyEvent.getKeyCode() == key &&
				mCurrentKeyEvent.getAction() == KeyEvent.ACTION_DOWN &&
				action == HeadUnitMcuProtocol.KEY_ACTION_DOWN)
		{
			//上报重复按下
			mCurrentKeyPressedCount ++;
			mCurrentKeyEvent = new KeyEvent(mCurrentKeyPressedTime, SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, key, mCurrentKeyPressedCount);
			//暂时保留，待虚拟键支持了再加上
			//broadcastKeyEvent(key,KeyEvent.ACTION_DOWN,mCurrentKeyPressedCount);
		}
		//按下后弹起
		else if(mCurrentKeyEvent != null && mCurrentKeyEvent.getKeyCode() == key &&
			    mCurrentKeyEvent.getAction() == KeyEvent.ACTION_DOWN &&
				action == HeadUnitMcuProtocol.KEY_ACTION_UP)
		{
			//上报弹起
			mCurrentKeyEvent = new KeyEvent(mCurrentKeyPressedTime, SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, key, 0);
			mCurrentKeyPressedCount = 0;
			mCurrentKeyPressedTime = 0;
			mCurrentKeyEvent = null;
			if(mService != null)
			{
				mService.broadcastKeyEvent(key,KeyEvent.ACTION_UP,0);
			}
			
		}
		else 
		{
			Log.e(TAG, "unvalid key status!!!");
			mCurrentKeyEvent = null;
			mCurrentKeyPressedCount = 0;
		}
	}
	

	
	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		if(value == null || value.length() == 0)
		{
			Log.e(TAG, "value is invalid!");
			return false;
		}
		if(mMcuUpgradeState != VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN
				|| ThreadManager.getSinglePool(MCU_UPGRADE_THREAD_NAME).getActiveCount() > 0)
		{
			Log.e(TAG, "not allow communication in mcu upgrade state");
			return false;
		}
		//截取TPMS指令
		if(propId >= VehicleInterfaceProperties.VIM_MCU_TPMS_DEVICE_STATE_PROPERTY &&
				propId <= VehicleInterfaceProperties.VIM_MCU_TPMS_REQ_COMMAND_PROPERTY &&
				mTpmsManager != null)
		{
			return mTpmsManager.setPropValue(propId, value); 
		}
		try
		{
			switch(propId)
			{
				//设置系统时间
				case VehicleInterfaceProperties.VIM_MCU_SYSTEM_TIME_PROPERTY:
					int[] times = VehicleManager.stringToIntArray(value);
					if(times != null && times.length == 6)
					{
						postData(HeadUnitMcuProtocol.AM_CMD_SYS_TIME, new int[]{times[0] >> 8,times[0],times[1],times[2],times[3],times[4],times[5]});
					}
					break;
//				//设置倒车检测模式
//				case VehicleInterfaceProperties.VIM_MCU_REVERSE_DETECT_MODE_PROPERTY:
//					postData(HeadUnitMcuProtocol.AM_CMD_REVERSE_DETECT, new int[]{Integer.valueOf(value)});
//					break;
//				//设置手刹检测模式
//				case VehicleInterfaceProperties.VIM_MCU_BRAKE_DETECT_MODE_PROPERTY:
//					postData(HeadUnitMcuProtocol.AM_CMD_BRAKE_DETECT, new int[]{Integer.valueOf(value)});
//					break;
//				//设置大灯检测模式
//				case VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_DETECT_MODE_PROPERTY:
//					postData(HeadUnitMcuProtocol.AM_CMD_ILL_DETECT, new int[]{Integer.valueOf(value)});
//					break;
				
				//发送键值
				case VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_SEND_VIRTUAL_KEY, new int[]{Integer.valueOf(value)-VehiclePropertyConstants.USER_KEY_OFFSET});
					break;
				//设置Mute
				case VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_MUTE, new int[]{Integer.valueOf(value)});
					break;
				//按键音开关
				case VehicleInterfaceProperties.VIM_MCU_KEY_BEEP_SWITCH_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_SET_OTHER_INFO,new int[]{0x01,Integer.valueOf(value)});
					break;
				//开机媒体默认音量
				case VehicleInterfaceProperties.VIM_MCU_MEDIA_DEFAULT_VOLUME_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_INIT_DEFAULT_VOLUME, new int[]{HeadUnitMcuProtocol.VOLUME_TYPE_MEDIA,Integer.valueOf(value)});
					break;
				//开机蓝牙电话默认音量
				case VehicleInterfaceProperties.VIM_MCU_BT_PHONE_DEFAULT_VOLUME_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_INIT_DEFAULT_VOLUME, new int[]{HeadUnitMcuProtocol.VOLUME_TYPE_BT_PHONE,Integer.valueOf(value)});
					break;
				//开机导航默认音量
				case VehicleInterfaceProperties.VIM_MCU_NAVIGATION_DEFAULT_VOLUME_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_INIT_DEFAULT_VOLUME, new int[]{HeadUnitMcuProtocol.VOLUME_TYPE_NAVIGATION,Integer.valueOf(value)});
					break;
				//设置媒体音量
				case VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY:
					Log.i(TAG1,"set media volume:"+value);
					postData(HeadUnitMcuProtocol.AM_CMD_SET_VOLUME, new int[]{HeadUnitMcuProtocol.VOLUME_TYPE_MEDIA,Integer.valueOf(value)});
					break;
				//设置蓝牙电话音量
				case VehicleInterfaceProperties.VIM_MCU_BT_PHONE_VOLUME_PROPERTY:
					Log.i(TAG1,"set phone volume:"+value);
					postData(HeadUnitMcuProtocol.AM_CMD_SET_VOLUME, new int[]{HeadUnitMcuProtocol.VOLUME_TYPE_BT_PHONE,Integer.valueOf(value)});
					break;
				//设置导航音量
				case VehicleInterfaceProperties.VIM_MCU_NAVIGATION_VOLUME_PROPERTY:
					Log.i(TAG1,"set navi volume:"+value);
					postData(HeadUnitMcuProtocol.AM_CMD_SET_VOLUME, new int[]{HeadUnitMcuProtocol.VOLUME_TYPE_NAVIGATION,Integer.valueOf(value)});
					break;
				case VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_BT_PHONE_INFO, new int[]{0x0D,Integer.valueOf(value)});
					break;
				case VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_BT_PHONE_INFO, new int[]{0x0D,Integer.valueOf(value)});
					break;
				//设置GPS混音开关
				case VehicleInterfaceProperties.VIM_MCU_GPS_MIX_ENABLE_PROPERTY:
					Log.i(TAG1,"set mix enable:"+value);
					mMixEnable = Integer.valueOf(value);
					/*
					mService.getMainHandler().removeMessages(CarService.MSG_UPDATE_MIX_ENABLE);
					Message msgMixEnable = mService.getMainHandler().obtainMessage();
					msgMixEnable.arg1 = Integer.valueOf(value);
					
					msgMixEnable.what = CarService.MSG_UPDATE_MIX_ENABLE;
					mService.getMainHandler().sendMessageDelayed(msgMixEnable, 900);*/
					//在混音播报过程中关闭混音,恢复导航音量
					if(mMixEnable == 0 && mMediaStreamVolume >= 0)
					{
						Log.i(TAG1,"recovery media volume when disable mix in gps playing:"+mMediaStreamVolume);
						AudioSystem.setMcStreamVolume(AudioManager.STREAM_MUSIC, mMediaStreamVolume, 0);
						mMediaStreamVolume = -1;
					}
					postData(HeadUnitMcuProtocol.AM_CMD_SET_GPS_INFO, new int[]{0x01,Integer.valueOf(value)});
					break;
				case VehicleInterfaceProperties.VIM_MCU_GPS_MONITOR_ENABLE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_SET_GPS_INFO, new int[]{0x00,Integer.valueOf(value)});
					break;
				//设置GPS混音比例
				case VehicleInterfaceProperties.VIM_MCU_GPS_MIX_LEVEL_PROPERTY:
					Log.i(TAG1,"set mix level:"+value);
					mMixLevel = Integer.valueOf(value);
					/*mService.getMainHandler().removeMessages(CarService.MSG_UPDATE_MIX_LEVEL);
					Message msgMixLevel = mService.getMainHandler().obtainMessage();
					msgMixLevel.arg1 = Integer.valueOf(value);
					msgMixLevel.what = CarService.MSG_UPDATE_MIX_LEVEL;
					mService.getMainHandler().sendMessageDelayed(msgMixLevel, 900);*/
					postData(HeadUnitMcuProtocol.AM_CMD_SET_GPS_INFO, new int[]{0x02,Integer.valueOf(value)});
					break;
				//设置EQ-低音值
				case VehicleInterfaceProperties.VIM_MCU_EQ_BASS_VALUE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0,HeadUnitMcuProtocol.EQ_BASS,Integer.valueOf(value)});
					break;
				//设置EQ-中音值
				case VehicleInterfaceProperties.VIM_MCU_EQ_ALTO_VALUE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0,HeadUnitMcuProtocol.EQ_ALTO,Integer.valueOf(value)});
					break;
				//设置EQ-高音值
				case VehicleInterfaceProperties.VIM_MCU_EQ_TREBLE_VALUE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0,HeadUnitMcuProtocol.EQ_TREBLE,Integer.valueOf(value)});
					break;
				//设置EQ-重低音
				case VehicleInterfaceProperties.VIM_MCU_EQ_SUBWOOFER_VALUE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0,HeadUnitMcuProtocol.EQ_SUBWOOFER,Integer.valueOf(value)});
					break;
				//中心频点信息
				case VehicleInterfaceProperties.VIM_MCU_EQ_CENTER_FREQ_INFO_PROPERTY:
					int[] centerFreqInfo = VehicleManager.stringToIntArray(value);
					if(centerFreqInfo != null && centerFreqInfo.length == 3)
					{
						int lowStrength = centerFreqInfo[2] & 0xFF;
						int highStrength = (centerFreqInfo[2] >> 8) & 0xFF;
						postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0,HeadUnitMcuProtocol.EQ_CENTER_FREQ_INFO,centerFreqInfo[0],centerFreqInfo[1],highStrength,lowStrength});
					}
					else 
					{
						Log.w(TAG, "VIM_MCU_EQ_CENTER_FREQ_INFO_PROPERTY param is invalid,value:"+value);
					}
					break;
				//等响度开关
				case VehicleInterfaceProperties.VIM_MCU_EQ_LOUDNESS_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0,HeadUnitMcuProtocol.EQ_LOUDNESS_SWITCH,Integer.valueOf(value)});
					break;
				//预设模式
				case VehicleInterfaceProperties.VIM_MCU_EQ_PRESET_MODE_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0,HeadUnitMcuProtocol.EQ_PRESET_MODE,Integer.valueOf(value)});
					break;

				//声场平衡信息
				case VehicleInterfaceProperties.VIM_MCU_AUDIO_BALANCE_INFO_PROPERTY:
					int[] audioBalanceInfo = VehicleManager.stringToIntArray(value);
					if(audioBalanceInfo != null && audioBalanceInfo.length == 4)
					{
						postData(HeadUnitMcuProtocol.AM_CMD_AUDIO_BALANCE_INFO, new int[]{0,audioBalanceInfo[0],audioBalanceInfo[1],audioBalanceInfo[2],audioBalanceInfo[3]});
					}
					else 
					{
						Log.w(TAG, "VIM_MCU_EQ_LOUDNESS_PROPERTY param is invalid,value:"+value);
					}
					break;
				//收音状态请求
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY:
					int state =Integer.valueOf(value);
					switch (state)
					{
						case VehiclePropertyConstants.RADIO_STATUS_NORMAL:
							//postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STOP_SEARCH,0,0}); 
							break;
						case VehiclePropertyConstants.RADIO_STATUS_SEARCH:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STOP_SEARCH,0,0}); 
							break;
						case VehiclePropertyConstants.RADIO_STATUS_SCAN:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SCAN,0,0}); 
							break;
						default:
							break;
					}
				//设置指定波段
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY:
					int band = Integer.valueOf(value);
					switch(band)
					{
						case VehiclePropertyConstants.RADIO_BAND_FM1:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_FM1}); 
							break;
						case VehiclePropertyConstants.RADIO_BAND_FM2:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_FM2}); 
							break;
						case VehiclePropertyConstants.RADIO_BAND_FM3:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_FM3}); 
							break;
						case VehiclePropertyConstants.RADIO_BAND_AM1:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_AM1}); 
							break;
						case VehiclePropertyConstants.RADIO_BAND_AM2:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_AM2}); 
							break;
						default:
							break;
					}
					
					break;
				//设置指定频率
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY:
					int []param = VehicleManager.stringToIntArray(value);
					if(param.length == 2)
					{
						if(param[0] < 0 || param[0] > 4 || isSameBand(param[0]))
						{
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SET_FREQ,ServiceHelper.HIBYTE(param[1]),ServiceHelper.LOBYTE(param[1])}); 
							if(mCurRadioBand != param[0])
							{
								setPropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY, String.valueOf(param[0]));
							}
						}
						else
						{
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SET_BAND_AND_FREQ,param[0],ServiceHelper.HIBYTE(param[1]),ServiceHelper.LOBYTE(param[1])}); 
						}
						
					}
					
					break;
				//设置收音区域
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_RADIO_SET_REGION, new int[]{Integer.valueOf(value)}); 
					break;
				//设置ST(此调用需要MCU支持)
				case VehicleInterfaceProperties.VIM_MCU_RADIO_ST_KEY_STATUS_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STEREO,Integer.valueOf(value)});
					break;
				//设置LOC(此调用需要MCU支持)
				case VehicleInterfaceProperties.VIM_MCU_RADIO_LOC_STATUS_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LOC,Integer.valueOf(value)});
					break;
				//设置TP
				case VehicleInterfaceProperties.VIM_MCU_RADIO_TP_STATUS_PROPERTY:
					Log.w(TAG,"not support set VIM_MCU_RADIO_TP_STATUS_PROPERTY");
					break;	
				//设置TA(此调用需要MCU支持)
				case VehicleInterfaceProperties.VIM_MCU_RADIO_TA_STATUS_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_TA,Integer.valueOf(value)});
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_AF_STATUS_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_AF,Integer.valueOf(value)}); 
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_STOP_SENS_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STOP_SENSITIVITY,Integer.valueOf(value)}); 
					break;
				//收藏电台到指定索引项
				case VehicleInterfaceProperties.VIM_MCU_RADIO_FAVOR_FREQ_INDEX_PROPERTY:
					int saveIndex = Integer.valueOf(value);
					if(saveIndex < 0 || saveIndex > 5)
					{
						Log.e(TAG,"VIM_MCU_RADIO_FAVOR_FREQ_INDEX_PROPERTY param is error(0-5)");
						return false;
					}
					switch (saveIndex)
					{
						case 0:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SP1}); 
							break;
						case 1:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SP2}); 
							break;
						case 2:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SP3}); 
							break;
						case 3:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SP4}); 
							break;
						case 4:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SP5}); 
							break;
						case 5:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SP6}); 
							break;
						default:
							break;
					}
					break;
				//预存频率选中
				case VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY:
					int preFreqindex = Integer.valueOf(value);
					if(preFreqindex < 0 || preFreqindex > 5)
					{
						Log.e(TAG,"VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY param is error(0-5)");
						return false;
					}
					switch (preFreqindex)
					{
						case 0:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LP1}); 
							break;
						case 1:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LP2}); 
							break;
						case 2:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LP3}); 
							break;
						case 3:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LP4}); 
							break;
						case 4:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LP5}); 
							break;
						case 5:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LP6}); 
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY:
					int radioAudioStatus = Integer.valueOf(value);
					postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_POWER,radioAudioStatus});
					break;
				//收音相关的请求操作
				case VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY:
					int radioHandleCmd = Integer.valueOf(value);
					switch (radioHandleCmd)
					{
						case VehiclePropertyConstants.RADIO_CMD_INIT:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_SWITCH, new int[]{0x01}); 
							//postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_POWER,0x01}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_EXIT:
							//postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STOP}); 
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_POWER,0x00});
							break;
						case VehiclePropertyConstants.RADIO_CMD_REQ_REGION:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_REQ_REGIION, new int[]{0x01}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_FM_AM:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_FM_AM_SWITCH}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_FM:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_BAND_FM}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_AM:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_BAND_AM}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_BAND:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_BAND}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_NEXT_PRE_CHANNEL:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_NEXT_PRE_STATION}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_PRIO_PRE_CHANNEL:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_PRIO_PRE_STATION}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_STEP_UP:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STEP_UP}); 
							break;
						case VehiclePropertyConstants.RADIO_CMD_STEP_DOWN:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STEP_DOWN});
							break;
						case VehiclePropertyConstants.RADIO_CMD_SEARCH_UP:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SEARCH_UP});
							break;
						case VehiclePropertyConstants.RADIO_CMD_SEARCH_DOWN:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SEARCH_DOWN});
							break;
						case VehiclePropertyConstants.RADIO_CMD_SCAN:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_SCAN});
							break;
						case VehiclePropertyConstants.RADIO_CMD_AUTO_SEARCH:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_AUTO_SERACH});
							break;
						case VehiclePropertyConstants.RADIO_CMD_STOP_SEARCH:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STOP_SEARCH});
							break;
						case VehiclePropertyConstants.RADIO_CMD_SWITCH_STERIO:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_STEREO});
							break;
						case VehiclePropertyConstants.RADIO_CMD_SWITCH_LOC:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_LOC});
							break;
						case VehiclePropertyConstants.RADIO_CMD_SWITCH_TA:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_TA});
							break;
						case VehiclePropertyConstants.RADIO_CMD_SWITCH_AF:
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_AF});
							break;
						case VehiclePropertyConstants.RADIO_CMD_ENABLE_AUDIO:
							Log.i(TAG, "enable radio audio,time:"+SystemClock.uptimeMillis());
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_POWER,0x01});
							break;
						case VehiclePropertyConstants.RADIO_CMD_DISABLE_AUDIO:
							Log.i(TAG, "disable radio audio,time:"+SystemClock.uptimeMillis());
							postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACTION, new int[]{HeadUnitMcuProtocol.RADIO_ACTION_POWER,0x00});
							break;
						default:
							Log.w(TAG, "VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY,unvalid param:"+value);
							break;
					}
					break;
				//每隔2S发送一次
				case VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY:
					mUIMode = -1;
					Log.i(TAG, "Set VIM_MCU_UI_MODE_PROPERTY:"+value+",time:"+SystemClock.uptimeMillis()+",pid:"+android.os.Process.myPid());
					postData(HeadUnitMcuProtocol.AM_CMD_UI_MODE, new int[]{Integer.valueOf(value)});
					mSetSourceTime = SystemClock.uptimeMillis();
					mUIMode = Integer.valueOf(value);
					mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, value);
					break;
				//每隔2S发送一次
				case VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY:
					mMediaMode = -1;
					Log.i(TAG, "Set VIM_MCU_MEDIA_MODE_PROPERTY:"+value+",time:"+SystemClock.uptimeMillis()+",pid:"+android.os.Process.myPid());
					postData(HeadUnitMcuProtocol.AM_CMD_MEDIA_MODE, new int[]{Integer.valueOf(value)});
					mSetSourceTime = SystemClock.uptimeMillis();
					mMediaMode = Integer.valueOf(value);
					mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, value);
					break;
				//模拟通道
				case VehicleInterfaceProperties.VIM_MCU_NAV_AUDIO_CHANNEL_PROPERTY:
					{
						Log.i(TAG1, "Navi Play:"+value);
						int ttsOn = Integer.valueOf(value);
						//播报开始
						if(ttsOn == 1)
						{
							int curNaviVolume = AudioSystem.getMcStreamVolume(AudioManager.STREAM_DTMF, 2);//mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);
							int mixMediaVolume = Math.round(curNaviVolume*mMixLevel*1.0f/10.0f);
							mMediaStreamVolume = AudioSystem.getMcStreamVolume(AudioManager.STREAM_MUSIC, 2);//mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
							Log.i(TAG1,"Navi Play:set mix volume,media volume from "+mMediaStreamVolume+" to "+mixMediaVolume+",mixLevel:"+mMixLevel+",navi volume:"+curNaviVolume);
							if(mMediaStreamVolume > mixMediaVolume)
							{
								AudioSystem.setMcStreamVolume(AudioManager.STREAM_MUSIC,mixMediaVolume, 0);
							}
							else
							{
								Log.i(TAG1,"Navi Play:media volume is too low ,and not need to set");
							}
						}
						//播报结束
						else if(ttsOn == 0 && mMediaStreamVolume >= 0)
						{
							Log.i(TAG1,"Navi Play:recovery media volume:"+mMediaStreamVolume);
							AudioSystem.setMcStreamVolume(AudioManager.STREAM_MUSIC, mMediaStreamVolume, 0);
							mMediaStreamVolume = -1;
						}
					}
					//postData(HeadUnitMcuProtocol.AM_CMD_PLAY_GPS_SOUND, new int[]{Integer.valueOf(value)});
					break;
				//I2S通道开关
				case VehicleInterfaceProperties.VIM_MCU_ARM_AUDIO_CHANNEL_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_PLAY_MEDIA_SOUND, new int[]{Integer.valueOf(value)});
					break;
				//升级文件路径
				case VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY:
					mMcuUpgradePath = value;
					break;
				//方向盘学习功能ID
				case VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_KEY_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_STEER_KEY_FUN_ID, new int[]{Integer.valueOf(value)});
					break;
				//方向盘学习ActionID
				case VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY:
					postData(HeadUnitMcuProtocol.AM_CMD_STEER_STUDY_ACTION, new int[]{Integer.valueOf(value)});
					break;
				//设置背光亮度
				case VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY:
					int brightness = Integer.valueOf(value);
					brightness = Math.min(100, Math.max(1, brightness));
					postData(HeadUnitMcuProtocol.AM_CMD_SET_BACKLIGHT_BRIGHTNESS, new int[]{brightness});
					break;
				//开关屏
				case VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY:
					int brightnessEnable = Integer.valueOf(value);
					postData(HeadUnitMcuProtocol.AM_CMD_BACKLIGHT_BRIGHTNESS_SWITCH, new int[]{brightnessEnable});
					break;
				//DVR显示区域
				case VehicleInterfaceProperties.VIM_MCU_DVR_DISPLAY_AREA_PROPERTYE:
				//DVR触摸事件
				case VehicleInterfaceProperties.VIM_MCU_DVR_TOUCH_EVENT_PROPERTYE:
				//DVR按键事件
				case VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE:
				//DVR类型
				case VehicleInterfaceProperties.VIM_MCU_DVR_TYPE_PROPERTYE:
					if(mDVRManager != null)
					{
						return mDVRManager.setPropValue(propId, value);
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY:
					int[] gain = ServiceHelper.stringToIntArray(value);
					if(gain != null && gain.length == 2)
					{
						postData(HeadUnitMcuProtocol.AM_CMD_SET_SOURCE_GAIN, gain); 
					};
					break;
				//请求命令
				case VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY:
					int cmd = Integer.valueOf(value);
					switch(cmd)
					{
						//请求版本号
						case VehiclePropertyConstants.MCU_CMD_REQ_VERSION:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_VERSION, new int[]{1});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_RESET:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_RESET, new int[]{0x00});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_UPGRADE:
							if (mMcuUpgradePath == null || mMcuUpgradePath.length() == 0)
							{
								Log.e(TAG, "mcu upgrade path is empty");
								break;
							}
							if (mMcuUpgradeState != VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN )
							{
								Log.e(TAG, "mcu is in update");
								break;
							}
							// 开始MCU升级
							if (!mcuUpdate(mMcuUpgradePath))
							{
								mMcuUpgradeFailedCount = 0;
								Log.e(TAG, "mcu upgrade failed!");
								return false;
							}
							break;
						//请求倒车状态
						case VehiclePropertyConstants.MCU_CMD_REQ_REVERSE_STATUS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_REVERSE_STATUS, new int[]{0});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_BRAKE_STATUS:
							Log.w(TAG, "not support this req cmd");
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_STATE:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_ACC_STATUS, new int[]{0});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_ILL_STATUS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_ILL_STATUS, new int[]{0});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_MEDIA_VOLUME_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_VOLUME,new int[]{0x00});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_BT_VOLUME_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_VOLUME,new int[]{0x01});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_NAVI_VOLUME_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_VOLUME,new int[]{0x02});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_GPS_MONITOR_STATUS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_GPS_SET_INFO,new int[]{0x00});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_GPS_MIX_STATUS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_GPS_SET_INFO,new int[]{0x01});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_GPS_MIX_LEVEL:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_GPS_SET_INFO,new int[]{0x02});
							break;
						case VehiclePropertyConstants.MCU_CMD_PLAY_KEY_SPEAKER:
							postData(HeadUnitMcuProtocol.AM_CMD_SEND_KEY_SPEAKER, new int[]{1});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_REVERSE_BEPP_STATUS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_OTHER_SET_INFO, new int[]{0x00});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_KEY_BEPP_STATUS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_OTHER_SET_INFO, new int[]{0x01});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_AUDIO_BALANCE_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_AUDIO_BALANCE_INFO,new int[]{0x01});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EQ_BASS_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0x01,0x00});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EQ_ALTO_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0x01,0x01});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EQ_TREBLE_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0x01,0x02});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EQ_SUBWOOFER_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0x01,0x03});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EQ_CENTER_FREQ_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0x01,0x04});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EQ_LOUDNESS_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0x01,0x05});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EQ_PRESET_MODE_INFO:
							postData(HeadUnitMcuProtocol.AM_CMD_EQ_INFO, new int[]{0x01,0x06});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_OPEN_SERIAL:
							if(!mSerialConnect)
							{
								if (mSerial.open() != 0)
								{
									mSerialConnect = true;
									break;
								}
							}
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_CLOSE_SERIAL:
							if(mSerialConnect)
							{
								if(mSerial.close() != 0)
								{
									mSerialConnect = false;
								}
							}
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_ENTER_STEER_STUDY:
							postData(HeadUnitMcuProtocol.AM_CMD_STEER_STUDY_STATUS, new int[]{0x01});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_EXIT_STEER_STUDY:
							postData(HeadUnitMcuProtocol.AM_CMD_STEER_STUDY_STATUS, new int[]{0x00});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_BACKLIGHT_BRIGHTNESS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_BACKLIGHT_BRIGHTNESS, new int[]{0x01});
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_BACKLIGHT_STATUS:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_BACKLIGHT_STATUS, new int[]{0x01});
							break;
						//DVR时间同步
						case VehiclePropertyConstants.MCU_CMD_REQ_DVR_SYNC_TIME:
							if(mDVRManager != null)
							{
								return mDVRManager.syncDVRTime();
							}
							break;
						case VehiclePropertyConstants.MCU_CMD_REQ_SOURCE_GAIN:
							postData(HeadUnitMcuProtocol.AM_CMD_REQ_SOURCE_GAIN, new int[]{0xFF});
							break;
					}
					break;
				default:
					break;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "setPropValue Exception,propId:"+propId+",value:"+value);
			return false;
		}
		return true;
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		return super.getPropValue(propId);
	}

	// 此时获取的数据时Cmd+Data
	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		if (data == null) 
		{
			Log.e(TAG, "onLocalData is null");
			return; 
		}
		int[] buffer = data.getIntArray(CarService.KEY_DATA);
//		// 返回get的数据
//		Log.w(TAG, "wait data:"+mbWaitReturn);
//		if (mbWaitReturn)
//		{
//			try
//			{
//				mlsReturn.put(buffer);
//			} catch (Exception e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		int[] param = new int[buffer.length - 1];
		System.arraycopy(buffer, 1, param, 0, param.length);
		onReceiveData(buffer[0], param);
	}

	@Override
	public  void onReceive(ByteBuffer buffer)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "MCU onReceive: " + ServiceHelper.toHexString(buffer)+",position:"+buffer.position());
		if (buffer == null) 
		{ 
			return; 
		}
		buffer.position(0);
		ByteBuffer data = ByteBuffer.allocate(buffer.capacity());
		data.put(buffer);

		while (data.position() >= 6)
		{
			int[] frameBuffer = unPack(data);
			if (frameBuffer == null || frameBuffer.length < 1)
			{
				Log.i(TAG, "data is null or too short,content:"+ServiceHelper.toString(buffer));
				continue;
			}
			
			// 返回get的数据
			//Log.w(TAG, "wait data:"+mbWaitReturn);
			if (mbWaitReturn)
			{
				try
				{
					mlsReturn.put(frameBuffer);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Message msg = mService.getMainHandler().obtainMessage();
			
			int cmd = frameBuffer[0];
			Bundle bundle = new Bundle();
			if(cmd == HeadUnitMcuProtocol.MA_CMD_CAN_INFO)
			{
				int[] canBuffer = new int[frameBuffer.length-1];
				System.arraycopy(frameBuffer,1,canBuffer,0,canBuffer.length);
				msg.arg1 = canBuffer.length;
				msg.what = CarService.MSG_RECEIVE_VEHICLE_DATA;
				bundle.putIntArray(CarService.KEY_DATA, canBuffer);
			}
			else
			{
				msg.arg1 = frameBuffer.length;
				msg.what = CarService.MSG_RECEIVE_MCU_DATA;
				bundle.putIntArray(CarService.KEY_DATA, frameBuffer);
			}
			msg.setData(bundle);
			mService.getMainHandler().sendMessage(msg);
		}
	}

	@Override
	public void onSend(ByteBuffer data)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "MCU onSend: " + ServiceHelper.toHexString(data));
	}

	private void onMcuUpgradeStateChange(int state,int errCode)
	{
		if (mMcuUpgradeState != state)
		{
			mMcuUpgradeState = state;
			if (state == VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED)
			{
				//协议通讯失败导致升级失败
				if(errCode > 1)
				{
					int retryCount = 0;
					while(sendIAPCmd(HeadUnitMcuProtocol.AM_CMD_UPGRADE_START, new int[]{0x02}, HeadUnitMcuProtocol.MA_CMD_UPGRADE_STATUS,new int[]{0x00}) < 0)
					{
						if(retryCount >= MAX_RETRY_COUNT)
						{
							Log.e(TAG, "req exit mcu upgrade failed,and please contact mcu engineer!");
							break;
						}
						retryCount ++;
						Log.e(TAG, "req exit mcu upgrade failed,and retry count:"+retryCount);					
					}
					mMcuUpgradeState = VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN;
					//重复整个升级流程
					mMcuUpgradeFailedCount ++;
					if(mMcuUpgradeFailedCount <= MAX_RETRY_COUNT)
					{
						Message msg = mService.getMainHandler().obtainMessage();
						msg.what = CarService.MSG_MCU_UPGRADE_FAILED;
						msg.arg1 = mMcuUpgradeFailedCount ;
						mService.getMainHandler().sendMessageDelayed(msg, 500);
						return;
					}
					Log.e(TAG, "mcu upgrade failed too many times!");
				}
				//文件校验失败导致升级失败
				else
				{
					startSourceSyncTimer();
				}
				mMcuUpgradeState = VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN;
				mMcuUpgradeFailedCount = 0;
			}
			if(mService != null)
			{
				mService.dispatchData(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY, String.valueOf(state));
			}
		}
	}
	
	//校验升级文件
	private boolean checkHdr(byte[] array,int pos)
	{
		// TODO Auto-generated constructor stub
		if(array.length < HDR_SIZE)
		{
			return false;
		}
		int offset = pos;
		byte[] hdr_version_bytes = new byte[4];
		byte[] hdr_mark_bytes = new byte[4];
		byte[] img_Size_bytes = new byte[4];
		byte[] product_name_bytes = new byte[32];
		byte[] img_type_bytes = new byte[4];
		byte[] img_entry_bytes = new byte[4];
		byte[] img_checksum_bytes = new byte[4];
		byte[] hdr_type_bytes = new byte[4];
		byte[] hdr_checksum_bytes = new byte[4];
		
		hdr_version_bytes = Arrays.copyOfRange(array, offset, offset+4);
		offset+=4;
		hdr_mark_bytes = Arrays.copyOfRange(array, offset, offset+4);
		offset+=4;
		img_Size_bytes = Arrays.copyOfRange(array, offset, offset+4);
		offset+=4;
		product_name_bytes = Arrays.copyOfRange(array, offset, offset+32);
		offset+=32;
		img_type_bytes = Arrays.copyOfRange(array, offset, offset+4);
		offset+=4;
		img_entry_bytes = Arrays.copyOfRange(array, offset, offset+4);
		offset+=4+64;
		img_checksum_bytes = Arrays.copyOfRange(array, offset, offset+4);
		offset+=4;
		hdr_type_bytes = Arrays.copyOfRange(array, offset,offset+4);
		offset+=4;
		hdr_checksum_bytes = Arrays.copyOfRange(array, offset, offset+4);
		offset+=4;
		
		int hdr_version = ServiceHelper.getIntFromBytes(hdr_version_bytes);
		int hdr_mark = ServiceHelper.getIntFromBytes(hdr_mark_bytes);
		int img_size = ServiceHelper.getIntFromBytes(img_Size_bytes);
		String product_name = ServiceHelper.toString(product_name_bytes, 0, product_name_bytes.length, "UTF-8");
		int img_type = ServiceHelper.getIntFromBytes(img_type_bytes);
		int frame_entry = ServiceHelper.getIntFromBytes(img_entry_bytes);
		int img_check_sum = ServiceHelper.getIntFromBytes(img_checksum_bytes);
		int hdr_type = ServiceHelper.getIntFromBytes(hdr_type_bytes);
		int hdr_check_sum = ServiceHelper.getIntFromBytes(hdr_checksum_bytes);
		
		// check version
//	    if (hdr_version < HDR_VERSION)
//	    {
//	    	Log.e(TAG, "mce version"+hdr_version+" less than required version "+HDR_VERSION);
//	    	return false;
//	    }

	    // check header
	    if (hdr_mark != HDR_MARK)
	    {
	    	Log.e(TAG, "mark not match");
	        return false;
	    }
	    
	    if(img_size != (array.length-HDR_SIZE))
	    {
	    	Log.e(TAG, "update data size is error!");
	    	return false;
	    }
	    
	    //check product name
	    if(!product_name.equals(PRODUCT_NAME))
	    {
	    	Log.e(TAG, "product name is not right,update file product name:"+product_name+",expected product name:"+PRODUCT_NAME);
	    	return false;
	    }
	    
	    // hdr check sum
	    int head_check_sum = 0;
	    for (int index = 0; index < (HDR_SIZE - 4); index++)
	    {
	    	head_check_sum += (array[pos+index] & 0xFF);
	    }
	    if (hdr_check_sum != head_check_sum)
	    {
	        Log.e(TAG,"hdr checksum not match, orignal "+hdr_check_sum+", actual "+head_check_sum);
	        return false;
	    }
	    
	    //img check sum
	    int frame_check_sum = 0;
	    for (int index = HDR_SIZE; index < array.length; index++)
	    {
	    	frame_check_sum += (array[index] & 0xFF);
	    }
	    if(frame_check_sum != img_check_sum)
	    {
	    	Log.e(TAG,"img checksum not match, orignal "+img_check_sum+", actual "+frame_check_sum);
	        return false;
	    }
		return true;
	}
	
	private byte[] checkUpdateFile(String path)
	{
		// 升级文件校验
		byte[] byFile = null;
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(path);
			byFile = new byte[is.available()];	
			if (is.read(byFile, 0, byFile.length) < byFile.length)
			{
				Log.w(TAG, "update mcu read file failed: " + path);
				is.close();
				return null;
			}
			if(is != null)
			{
				is.close();
				is = null;
			}
			
			//方便在优联科主板上升级对应的mcu文件，后期关闭
			if(mMcuUpgradePath.contains("jidou"))
			{
				Log.e(TAG, "jidou update file for inner debug");
				return byFile; 
			}
			
			if(byFile.length > HDR_SIZE && checkHdr(byFile,0))
			{
				//去掉前128个byte
				byte []tmp =  new byte[byFile.length-HDR_SIZE];
				System.arraycopy(byFile, 128, tmp, 0, tmp.length);
				byFile = tmp;
				return byFile;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				if (is != null)
					is.close();
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}		
		return null;
	}
	
	private int sendIAPCmd(int cmd, int[] data,int expectedCmd,int[] expectedData)
	{
		int[] ret = sendData(cmd, data, expectedCmd, expectedData,ServiceHelper.WAIT_LONG);
		if(ret == null || ret.length == 0)
		{
			return -1;
		}
		if(ret[0] == HeadUnitMcuProtocol.MA_CMD_UPGRADE_COMPLETE && ret.length > 1)
		{
			return ret[1] == 0 ? MCU_UPDATE_FAILED:MCU_UPDATE_SUCCESS;
		}
		//处理上报升级结果
		if(cmd == HeadUnitMcuProtocol.AM_CMD_UPGRADE_FRAME)
		{
			if(ret[0] == HeadUnitMcuProtocol.MA_CMD_REQ_UPGRADE_FRAME && ret.length > 2)
			{
				//返回请求的帧索引值
				return ret[2];
			}
			else 
			{
				Log.e(TAG, "unexpected updata data!");
				return data[0];
			}
		}
		else if(cmd == HeadUnitMcuProtocol.AM_CMD_UPGRADE_START && expectedCmd == HeadUnitMcuProtocol.MA_CMD_UPGRADE_STATUS)
		{
			if(ret[0] == expectedCmd && ret.length >= (1+expectedData.length) &&
					ServiceHelper.compareIntArray(Arrays.copyOfRange(ret, 1,1+expectedData.length), expectedData))
			{
				return 0;
			}
			else 
			{
				return -1;
			}
		}
		return 0;
	}
	
	private boolean mcuUpdate(final String path)
	{
		if(mMcuUpgradeState != VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN
				|| ThreadManager.getSinglePool(MCU_UPGRADE_THREAD_NAME).getActiveCount() > 0)
		{
			Log.e(TAG, "already in mcu upgrade state");
			return false;
		}
		stopSourceSyncTimer();
		ThreadManager.getSinglePool(MCU_UPGRADE_THREAD_NAME).execute(new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				try
				{
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_INIT,0);
					byte[] updateData = checkUpdateFile(path);
					if(updateData == null || updateData.length == 0)
					{
						Log.i(TAG, "check update file failed!");
						onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED,1);
						return;
					}
					Log.i(TAG, "check update file success!");
					
	                int totalFrames = updateData.length / MCU_UPGRADE_FRAME_SIZE;
	                if ((updateData.length % MCU_UPGRADE_FRAME_SIZE) != 0) 
	                {
	                	totalFrames++;
	                }
	                int ret = -1;
	                int retryCount  = 0;
					
	                //请求进入升级模式
	                onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_START,0);
	                do
					{
	                	ret = sendIAPCmd(HeadUnitMcuProtocol.AM_CMD_UPGRADE_START, new int[]{0x01}, HeadUnitMcuProtocol.MA_CMD_UPGRADE_STATUS,new int[]{0x01});
	                	if(ret == MCU_UPDATE_FAILED || (ret < 0 && retryCount >= (MAX_RETRY_COUNT-1)))
	 					{
	 						Log.i(TAG, "request to iap mode failed!");
	 						onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED,2);
	 						return;
	 					}
	                	else if(ret == 0)
	                	{
	                		break;
	                	}
	                	retryCount ++;
	                	Log.i(TAG, "request to iap mode failed,count:"+retryCount);
	                	Thread.sleep(200);
					}
					while (ret < 0);

					//发送总帧数
	                retryCount = 0;
	                ret = -1;
	                do
					{
	                	ret = sendIAPCmd(HeadUnitMcuProtocol.AM_CMD_UPGRADE_INIT, new int[]{0x01,totalFrames}, HeadUnitMcuProtocol.MA_CMD_REQ_UPGRADE_FRAME,new int[]{0x01,0x00});
	                	if(ret == MCU_UPDATE_FAILED || (ret < 0 && retryCount >= (MAX_RETRY_COUNT-1)))
	 					{
	 						Log.i(TAG, "send total frames failed!");
	 						onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED,3);
	 						return;
	 					}
	                	else if(ret == 0)
	                	{
	                		break;
	                	}
	                	retryCount ++;
	                	Log.i(TAG, "send total frames failed,count:"+retryCount);
	                	Thread.sleep(200);
					}
					while (ret < 0);
	               
					float updateProgress = 0.0f;
					int reqFrame = 0;
					//发送数据帧
				    onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_WRITE,0);
					retryCount  = 0;
				    while(reqFrame >= 0)
					{
						int []data = new int[MCU_UPGRADE_FRAME_SIZE+1];
						data[0] = reqFrame;
						for(int i=0;i<MCU_UPGRADE_FRAME_SIZE;i++)
						{
							//最后空位补0xFF
							if( (reqFrame*MCU_UPGRADE_FRAME_SIZE+i) >= updateData.length )
							{
								data[i+1] = 0xFF;
							}
							else 
							{
								data[i+1] = updateData[reqFrame*1024+i];
							}
						}
						Log.i(TAG, "send update frame index:"+reqFrame);
						ret = sendIAPCmd(HeadUnitMcuProtocol.AM_CMD_UPGRADE_FRAME,data, HeadUnitMcuProtocol.MA_CMD_REQ_UPGRADE_FRAME,new int[]{0x01});
						if(ret < 0)
						{
							retryCount++;
							//2minute
							if(retryCount >= MAX_RETRY_COUNT)
							{
								retryCount = 0;
								reqFrame = -1;
								break;
							}
							Log.e(TAG, "wait mcu answer timeout,and retry wait:"+retryCount);
							continue;
						}
						else if(ret == MCU_UPDATE_FAILED)
						{
							retryCount = 0;
							reqFrame = -1;
							break;
						}
						else if(ret == MCU_UPDATE_SUCCESS)
						{
							retryCount = 0;
							reqFrame = 0;
							break;
						}
						else 
						{
							reqFrame = ret;
							float curProgress = (float)(Math.round((reqFrame+1)*1.0f/totalFrames*1000))/1000;
							if(updateProgress != curProgress)
							{
								updateProgress = curProgress;
								mService.dispatchData(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY, String.valueOf(updateProgress));
							}
							if(reqFrame >= totalFrames)
							{
								Log.e(TAG, "request frame index is error,reqFrameIndex:"+reqFrame+",totalFrame:"+totalFrames);
								retryCount = 0;
								reqFrame = -1;
								break;
							}
						}

					}
					if(reqFrame < 0)
					{
						Log.i(TAG, "send upgrade data failed");
						onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED,4);
						return;
					}
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_SUCCESS,0);
			
					Thread.sleep(1000);
					//重启MCU
					for(int i=0;i<3;i++)
					{
						postData(HeadUnitMcuProtocol.AM_CMD_REQ_RESET, new int[]{0x00});
						Thread.sleep(50);
					}
				} catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED,5);
				}
				
			}
		});
		return true;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// 封包
	public ByteBuffer pack(int cmd, int[] param)
	{
		int paramLen = (param == null) ? 0 : param.length;
		byte[] data = new byte[paramLen + 6];

		data[0] = ServiceHelper.getByteFromInt((0xAA & 0xFF));
		data[1] = ServiceHelper.getByteFromInt((0x55 & 0xFF));
		data[2] = ServiceHelper.getByteFromInt(((paramLen + 2) >> 8) & 0xFF);
		data[3] = ServiceHelper.getByteFromInt((paramLen + 2) & 0xFF);
		data[4] = ServiceHelper.getByteFromInt((cmd & 0xFF));
		int i = 0;
		if (param != null)
		{
			for (i = 0; i < paramLen; ++i)
			{
				data[5 + i] = ServiceHelper.getByteFromInt((param[i] & 0xFF));
			}
		}
		
		// checksum
		byte checksum = 0;
        for (i = 2; i < (data.length-1); i++) {
            checksum += data[i];
        }
        data[i] = (byte) (~checksum + 1);
		
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.position(data.length);
		return buffer;
	}

	// 解包
	public int[] unPack(ByteBuffer buffer)
	{
		int[] byReturn = null;
		int pos = 0;
		int length = buffer.position();

		// sync
		while (length >= 2)
		{
			if (ServiceHelper.getIntFromByte(buffer.get(pos + 0)) == 0xAA && ServiceHelper.getIntFromByte(buffer.get(pos + 1)) == 0x55)
			{
				pos += 2;
				length -= 2;
				break;
			}
			++pos;
			--length;
		}

		if (length >= 4)
		{
			// len
			int len_h = ServiceHelper.getIntFromByte(buffer.get(pos));
			int len_l = ServiceHelper.getIntFromByte(buffer.get(pos+1));
			pos += 2;
			length -= 2;
			int len = ServiceHelper.MAKEWORD(len_l, len_h);
			if (len <= length)
			{
				// checksum
				int sum = (len_h + len_l) & 0xFF;
				byReturn = new int[len - 1];
				for (int i = 0; i < (len - 1); ++i)
				{
					int data = ServiceHelper.getIntFromByte(buffer.get(pos+i));
					sum += data;
					sum = (sum & 0xFF);
					byReturn[i] = data;
				}
				pos += (len-1);
				length -= (len-1);
				sum = ((sum^0xFF)+1) & 0xFF;
				if (sum != ServiceHelper.getIntFromByte(buffer.get(pos)))
				{
					Log.e(TAG, "MCU onRecieve checksum error!");
					byReturn = null;
				}
				pos ++;
				length --;
			}
			else
			{
				// need more bytes
				Log.w(TAG, "receive data is not compelte!");
			}
		}
		else
		{
			// need more bytes
			Log.w(TAG, "receive data(head) is not compelte!");
		}
		if(pos > 0)
		{
			if(length == 0)
			{
				buffer.clear();
			}
			else
			{
				byte[] tmp = new byte[length];
				buffer.position(pos);
				buffer.get(tmp, 0, length);
				buffer.clear();
				buffer.put(tmp);
			}
		}
		return byReturn;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean postCanData(int[] param)
	{
		return postData(HeadUnitMcuProtocol.AM_CMD_CAN_INFO, param);
	}
	
	public boolean postCanData(int[] param,int postType)
	{
		return postData(HeadUnitMcuProtocol.AM_CMD_CAN_INFO, param,postType);
	}
	
	public boolean postDVRData(int[] param)
	{
		return postData(HeadUnitMcuProtocol.AM_CMD_DVR_INFO, param);
	}
	
	public boolean postDVRType(int type)
	{
		if(type >= 0 && type <= 1)
		{
			return postData(HeadUnitMcuProtocol.AM_CMD_DVR_CONFIG, new int[]{type});
		}
		return false;
	}
	
	public boolean postTPMSData(int[] param)
	{
		return postData(HeadUnitMcuProtocol.AM_CMD_TPMS_INFO, param);
	}
	
	public boolean postTPMSType(int type,int id)
	{
		if(type >= 0 && type <= 1)
		{
			return postData(HeadUnitMcuProtocol.AM_CMD_TPMS_CONFIG, new int[]{type,id});
		}
		return false;
	}
	
	public synchronized void returnCanAck(int resCmd)
	{
		returnAck(HeadUnitMcuProtocol.AM_CMD_CAN_ACK, resCmd);
	}
	
	private boolean postData(int cmd, int[] param)
	{
		try
		{
			if (!mSerialConnect)
			{
				Log.w(TAG, "PostData failed,Serial is close");
				return false;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		ByteBuffer buffer = pack(cmd,param);
		return mSerial.post(buffer);
	}
	
	private boolean postData(int cmd, int[] param,int postType)
	{
		try
		{
			if (!mSerialConnect)
			{
				Log.w(TAG, "PostData failed,Serial is close");
				return false;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		ByteBuffer buffer = pack(cmd,param);
		switch (postType)
		{
			case ServiceHelper.POST_TYPE_NORMAL:
				return mSerial.post(buffer);
			case ServiceHelper.POST_TYPE_KEEP_QUEUE:
				return mSerial.postInNoQueue(buffer);
			case ServiceHelper.POST_TYPE_REPLACE_QUEUE:
				return mSerial.postAndRemoveQueue(buffer);
			default:
				break;
		}
		return mSerial.post(buffer);
	}

	private synchronized int[] sendData(int cmd, int[] param, int expectedCmd,int[] expectedData, int timeout)
	{
		int[] byReturn = null;
		try
		{
			if (!mSerialConnect)
			{
				Log.w(TAG, "SendData failed,Serial is close");
				return null;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (mSerial == null)
		{
			Log.e(TAG, "MCU not connected, can't send request to it");
			return byReturn;
		}

		// 只需要封装Data区域即可
		ByteBuffer buffer = pack(cmd,param);

		// send data, repeat 3 times if failed
		for (int i = 0; i < ServiceHelper.REPEAT_NORMAL; ++i)
		{
			if(timeout > 0)
			{
				mbWaitReturn = true;
			}
			if (mSerial.send(buffer))
			{
				break;
			}
//			else
//			{
//				mbWaitReturn = false;
//			}
		}

		if (timeout > 0)
		{
			byReturn = waitData(expectedCmd, expectedData, timeout);
			if (byReturn == null)
			{
				Log.e(TAG, "writeData wait return timeout! " + "cmd=0x" + Integer.toHexString(expectedCmd) + ", subcmd=0x" +ServiceHelper.toString(expectedData) + ", timeout=" + timeout);
			}
		}
		return byReturn;
	}

	private synchronized int[] waitData(int cmd, int[] data, int timeout)
	{
		if (mSerial == null || !mSerialConnect)
		{
			Log.e(TAG, "MCU not connected, can't wait request to it");
			mbWaitReturn = false;
			return null;
		}
		if (cmd < 0)
		{
			mbWaitReturn = false;
			return null;
		}

		int[] byReturn = null;
		try
		{
			int waitTime = timeout;
			while (!Thread.currentThread().isInterrupted() && (waitTime >= 0 || timeout == ServiceHelper.WAIT_INFINITE))
			{
				long tTick = SystemClock.uptimeMillis();
				int[] retArray = mlsReturn.poll(waitTime, TimeUnit.MILLISECONDS);
				//针对升级的特殊处理,Cmd+Data
				if(cmd >= HeadUnitMcuProtocol.MA_CMD_UPGRADE_STATUS && 
						cmd <= HeadUnitMcuProtocol.MA_CMD_UPGRADE_COMPLETE &&
						retArray != null && retArray.length > 0 &&
						(cmd == retArray[0] || retArray[0] == HeadUnitMcuProtocol.MA_CMD_UPGRADE_COMPLETE))
				{
					byReturn = new int[retArray.length];
					System.arraycopy(retArray, 0, byReturn, 0, byReturn.length);
					break;
				}
				//匹配后只返回Data部分
				else if (retArray != null && retArray.length >= (1+data.length) && retArray[0] == cmd && 
						(data == null || ServiceHelper.compareIntArray(Arrays.copyOfRange(retArray, 1,1+data.length), data)))
				{
					byReturn = new int[retArray.length - 1];
					System.arraycopy(retArray, 1, byReturn, 0, byReturn.length);
					break;
				}
				
				else
				{
					byReturn = null;
					Log.w(TAG, "not expected result,expected cmd:" + cmd + " and data:" + ServiceHelper.toString(data));
				}
				waitTime -= (SystemClock.uptimeMillis() - tTick);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		mbWaitReturn = false;
		mlsReturn.clear();

		return byReturn;
	}

	//回复ACK
	private synchronized void returnAck(int type,int resCmd)
	{
		/*
		switch(type)
		{
			case HeadUnitMcuProtocol.ACK_TYPE_SYS:
				sendData(HeadUnitMcuProtocol.AM_CMD_SYS_ACK, new int[]{resCmd},0,null,0);
				//postData(HeadUnitMcuProtocol.AM_CMD_SYS_ACK, new int[]{resCmd});
				break;
			case HeadUnitMcuProtocol.ACK_TYPE_SET:
				sendData(HeadUnitMcuProtocol.AM_CMD_SET_ACK, new int[]{resCmd},0,null,0);
				//postData(HeadUnitMcuProtocol.AM_CMD_SET_ACK, new int[]{resCmd});
				break;
			case HeadUnitMcuProtocol.ACK_TYPE_RADIO:
				sendData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACK, new int[]{resCmd},0,null,0);
				//postData(HeadUnitMcuProtocol.AM_CMD_RADIO_ACK, new int[]{resCmd});
				break;
			case HeadUnitMcuProtocol.ACK_TYPE_STEER_STUDY:
				sendData(HeadUnitMcuProtocol.AM_CMD_STEER_STUDY_ACK, new int[]{resCmd},0,null,0);
				//postData(HeadUnitMcuProtocol.AM_CMD_STEER_STUDY_ACK, new int[]{resCmd});
				break;
			case HeadUnitMcuProtocol.ACK_TYPE_CAN:
				sendData(HeadUnitMcuProtocol.AM_CMD_CAN_ACK, new int[]{resCmd},0,null,0);
				//postData(HeadUnitMcuProtocol.AM_CMD_CAN_ACK, new int[]{resCmd});
				break;
			case HeadUnitMcuProtocol.ACK_TYPE_UPGRADE:
				sendData(HeadUnitMcuProtocol.AM_CMD_UPGRADE_ACK, new int[]{resCmd},0,null,0);
				//postData(HeadUnitMcuProtocol.AM_CMD_UPGRADE_ACK, new int[]{resCmd});
				break;
			default:
				break;
			
		}*/
	}
	
	private void onReceiveData(int cmd, int[] param)
	{
		if(mService == null)
		{
			Log.e(TAG, "car service instance has been destroyed!");
			return;
		}
		switch (cmd)
		{
			case HeadUnitMcuProtocol.MA_CMD_INIT_DATA:
				if (param != null && param.length >= 21)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_STARTUP_MODE_PROPERTYE, String.valueOf(param[0]), false);
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_LAST_POWER_OFF_SOURCE_PROPERTY, String.valueOf(param[1]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf(param[1]), true);
					int []times = new int[6];
					times[0] = ServiceHelper.MAKEWORD(param[3], param[2]);//year
					times[1] = param[4];//month
					times[2] = param[5];//day
					times[3] = param[6];//hour
					times[4] = param[7];//minute
					times[5] = param[8];//second
					mReverseStatus = (param[9] == 0 ? false:true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY, String.valueOf(param[9]), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY, String.valueOf(param[10]), true);
					String strCacheIllStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, String.valueOf(param[11]), true);
					String version = String.format("%s_%03d_%03d_20%02d.%02d.%02d.%02d.%02d",Build.PRODUCT.toUpperCase(),param[12],param[15],param[16],param[17],param[18],param[19],param[20]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, version, true);
					//根据大灯状态,调节背光亮度
					if(strCacheIllStatus == null || Integer.valueOf(strCacheIllStatus) != param[11])
					{
						onHandleIllStatusChange(param[11] == VehiclePropertyConstants.CAR_LIGHT_ON ? true : false);
					}
					reqMcuInitStatus();
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_MCU_VERSION:
				Log.i(TAG, "receive mcu version");
				if (param != null && param.length == 9)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					//String version = "D1002-C" + param[0] +"-R"+ServiceHelper.MAKEWORD(param[2], param[1]) +"-CAN" + param[3] +"--"+ param[4] +"-"+ param[5] +"-"+ param[6] + " "+param[7]+":"+param[8];
					String version = String.format("%s_%03d_%03d_20%02d.%02d.%02d.%02d.%02d",Build.PRODUCT.toUpperCase(),param[0],param[3],param[4],param[5],param[6],param[7],param[8]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, version, true);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_MUTE_STATUS:
				if(param != null && param.length > 0)
				{
					String strMuteStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY);
					String strMediaVolume = mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY,String.valueOf(param[0]), true);
					if(strMuteStatus == null || Integer.valueOf(strMuteStatus) != param[0])
					{
						//mute
						if(param[0] == 1)
						{
							syncVehicleMediaVolume(0);
						}
						//unmute
						else if(param[0] == 0 && strMediaVolume != null)
						{
							syncVehicleMediaVolume(Integer.valueOf(strMediaVolume));
						}
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_VOLUME_INFO:
				if(param != null && param.length == 2)
				{
					switch (param[0])
					{
						case 0:
							String strMediaVolume = mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY,String.valueOf(param[1]), false);
							if(strMediaVolume == null || param[1] != Integer.valueOf(strMediaVolume))
							{
								syncVehicleMediaVolume(param[1]);
							}
							break;
						case 1:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BT_PHONE_VOLUME_PROPERTY,String.valueOf(param[1]), false);
							break;
						case 2:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_NAVIGATION_VOLUME_PROPERTY,String.valueOf(param[1]), false);
							break;
						default:
							break;
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_CAR_ACC:
				if(param != null && param.length > 0)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_ACC_STAT_PROPERTYE, String.valueOf(param[0]), true);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_CAR_REVERSE:
				if(param != null  && param.length > 0)
				{
					Log.i(TAG, "receive reverse status:"+param[0]);
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					mReverseStatus = (param[0] == 0 ? false:true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY, String.valueOf(param[0]), true);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_CAR_BRAKE:
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive brake status:"+param[0]);
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY, String.valueOf(param[0]), true);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_CAR_ILL:
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive ill status:"+param[0]);
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					int illStatus = ServiceHelper.stringToIntSafe(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY));
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, String.valueOf(param[0]), true);
					//背光开才能响应大灯状态调节亮度,逻辑由MCU处理
					if(illStatus != param[0])
					{
						onHandleIllStatusChange(param[0] == VehiclePropertyConstants.CAR_LIGHT_ON ? true : false);
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_TURN_LIGHT:
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive turn light info:"+param[0]);
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					switch(param[0])
					{
						case 0:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TURN_LEFT_LIGHT_STATUS_PROPERTY, "0", true);
							break;
						case 1:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TURN_LEFT_LIGHT_STATUS_PROPERTY, "1", true);
							break;
						case 2:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY, "0", true);
							break;
						case 3:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY, "1", true);
							break;
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_SYS_ACK:
				if(param != null)
				{
					Log.i(TAG, "receive sys ack");
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_GPS_SET_INFO:
				if(param != null && param.length >= 2)
				{
					Log.i(TAG, "receive GPS info");
					switch (param[0])
					{
						case HeadUnitMcuProtocol.GPS_SET_MONITOR_STATUS:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_GPS_MONITOR_ENABLE_PROPERTY, String.valueOf(param[1]), true);
							break;
						case HeadUnitMcuProtocol.GPS_SET_MIX_STATUS:
							mMixEnable = param[1];
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_GPS_MIX_ENABLE_PROPERTY, String.valueOf(param[1]), true);
							break;
						case HeadUnitMcuProtocol.GPS_SET_MIX_LEVEL:
							mMixLevel = param[1];
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_GPS_MIX_LEVEL_PROPERTY, String.valueOf(param[1]), true);
							break;
						default:
							break;
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_OTHER_SET_INFO:
				if(param != null && param.length >= 2)
				{
					Log.i(TAG, "receive other set info");
					switch (param[0])
					{
						//此处协议待确定，请求按键音返回的type值为0
						case 0:
							//Log.i(TAG, "Reverse Beep Status:"+param[1]);
							//break;
						case 1:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_KEY_BEEP_SWITCH_PROPERTY, String.valueOf(param[1]), true);
							break;
						default:
							break;
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_EQ_SET_INFO:
				if(param != null && param.length >= 2)
				{
					Log.i(TAG, "receive EQ info");
					switch (param[0])
					{
						case HeadUnitMcuProtocol.EQ_BASS:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_EQ_BASS_VALUE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case HeadUnitMcuProtocol.EQ_ALTO:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_EQ_ALTO_VALUE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case HeadUnitMcuProtocol.EQ_TREBLE:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_EQ_TREBLE_VALUE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case HeadUnitMcuProtocol.EQ_SUBWOOFER:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_EQ_SUBWOOFER_VALUE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case HeadUnitMcuProtocol.EQ_CENTER_FREQ_INFO:
							if(param.length < 5)
							{
								break;
							}
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_EQ_CENTER_FREQ_INFO_PROPERTY,VehicleManager.intArrayToString(new int[]{param[1],param[2],ServiceHelper.MAKEWORD(param[4],param[3])}), false);
							break;
						case HeadUnitMcuProtocol.EQ_LOUDNESS_SWITCH:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_EQ_LOUDNESS_PROPERTY,VehicleManager.intArrayToString(new int[]{param[1]}), false);
							break;
						case HeadUnitMcuProtocol.EQ_PRESET_MODE:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_EQ_PRESET_MODE_PROPERTY,VehicleManager.intArrayToString(new int[]{param[1]}), false);
							break;
						default:
							break;
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_AUDIO_BALANCE_INFO:
				if(param != null && param.length == 4)
				{
					Log.i(TAG, "receive audio balance info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_AUDIO_BALANCE_INFO_PROPERTY,VehicleManager.intArrayToString(new int[]{param[0],param[1],param[2],param[3]}), false);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_SET_ACK:
				if(param != null)
				{
					Log.i(TAG, "receive set ack");
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_RADIO_INIT_DATA:
				if(param != null && param.length == 65)
				{
					Log.i(TAG, "receive radio init data");
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					mCurRadioBand = param[1];
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY, String.valueOf(param[1]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[3],param[2])), false);
					/*mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY, String.valueOf(--param[4]), true);//协议值范围1-6，实际App使用0-5
					//当前波段的预存频率列表
					int[] presetFreqList = new int[30];
					//FM1
					presetFreqList[0] = ServiceHelper.MAKEWORD(param[6], param[5]);
					presetFreqList[1] = ServiceHelper.MAKEWORD(param[8], param[7]);
					presetFreqList[2] = ServiceHelper.MAKEWORD(param[10], param[9]);
					presetFreqList[3] = ServiceHelper.MAKEWORD(param[12], param[11]);
					presetFreqList[4] = ServiceHelper.MAKEWORD(param[14], param[13]);
					presetFreqList[5] = ServiceHelper.MAKEWORD(param[16], param[15]);
					//FM2
					presetFreqList[6] = ServiceHelper.MAKEWORD(param[18], param[17]);
					presetFreqList[7] = ServiceHelper.MAKEWORD(param[20], param[19]);
					presetFreqList[8] = ServiceHelper.MAKEWORD(param[22], param[21]);
					presetFreqList[9] = ServiceHelper.MAKEWORD(param[24], param[23]);
					presetFreqList[10] = ServiceHelper.MAKEWORD(param[26], param[25]);
					presetFreqList[11] = ServiceHelper.MAKEWORD(param[28], param[27]);
					//FM3
					presetFreqList[12] = ServiceHelper.MAKEWORD(param[30], param[29]);
					presetFreqList[13] = ServiceHelper.MAKEWORD(param[32], param[31]);
					presetFreqList[14] = ServiceHelper.MAKEWORD(param[34], param[33]);
					presetFreqList[15] = ServiceHelper.MAKEWORD(param[36], param[35]);
					presetFreqList[16] = ServiceHelper.MAKEWORD(param[38], param[37]);
					presetFreqList[17] = ServiceHelper.MAKEWORD(param[40], param[39]);
					//AM1
					presetFreqList[18] = ServiceHelper.MAKEWORD(param[42], param[41]);
					presetFreqList[19] = ServiceHelper.MAKEWORD(param[44], param[43]);
					presetFreqList[20] = ServiceHelper.MAKEWORD(param[46], param[45]);
					presetFreqList[21] = ServiceHelper.MAKEWORD(param[48], param[47]);
					presetFreqList[22] = ServiceHelper.MAKEWORD(param[50], param[49]);
					presetFreqList[23] = ServiceHelper.MAKEWORD(param[52], param[51]);
					//AM2
					presetFreqList[24] = ServiceHelper.MAKEWORD(param[54], param[53]);
					presetFreqList[25] = ServiceHelper.MAKEWORD(param[56], param[55]);
					presetFreqList[26] = ServiceHelper.MAKEWORD(param[58], param[57]);
					presetFreqList[27] = ServiceHelper.MAKEWORD(param[60], param[59]);
					presetFreqList[28] = ServiceHelper.MAKEWORD(param[62], param[61]);
					presetFreqList[29] = ServiceHelper.MAKEWORD(param[64], param[63]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_FREQ_LIST_PROPERTY, VehicleManager.intArrayToString(presetFreqList), true);
					*/
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_RADIO_MAIN_DATA:
				
				if(param != null && param.length == 5)
				{
					Log.i(TAG, "receive radio main data");
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY, String.valueOf(param[1]), false);
					int freqValue = ServiceHelper.MAKEWORD(param[3],param[2]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY, String.valueOf(freqValue), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_SIGNAL_LEVEL_PROPERTY, String.valueOf(param[4]), false);
					//信号质量
					Log.i(TAG, "Radio Signal level:"+param[4]+",Freq:"+freqValue);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_RADIO_PRESET_DATA:
				Log.i(TAG, "receive radio preset data");
				/*if(param != null && param.length == 16 && param[0] == 0x03)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY, String.valueOf(param[1]), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY, String.valueOf(param[2]), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_PRESET_FREQ_PROPERTY, String.valueOf(param[3]), true);
					//当前波段的预存频率列表
					int[] presetFreqList = new int[6];
					presetFreqList[0] = OBDSerial.MAKEWORD(param[5], param[4]);
					presetFreqList[1] = OBDSerial.MAKEWORD(param[7], param[6]);
					presetFreqList[2] = OBDSerial.MAKEWORD(param[9], param[8]);
					presetFreqList[3] = OBDSerial.MAKEWORD(param[11], param[10]);
					presetFreqList[4] = OBDSerial.MAKEWORD(param[13], param[12]);
					presetFreqList[5] = OBDSerial.MAKEWORD(param[15], param[14]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_PRESET_FREQ_LIST_PROPERTY, VehicleManager.intArrayToString(presetFreqList), true);
				}*/
				break;
			case HeadUnitMcuProtocol.MA_CMD_RADIO_FLAG:
				Log.i(TAG, "receive radio flag");
				if(param != null && param.length ==5)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_ST_STATUS_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_LOC_STATUS_PROPERTY, String.valueOf(param[1]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY, String.valueOf(param[2]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_ST_KEY_STATUS_PROPERTY, String.valueOf(param[3]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_STOP_SENS_PROPERTY, String.valueOf(param[4]), false);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_RADIO_REGION:
				if(param != null && param.length == 11)
				{
					Log.i(TAG, "receive radio region");
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					mCurRadioRegion = param[0];
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_FM_MAX_FREQ_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[2],param[1])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_FM_MIN_FREQ_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[4],param[3])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_FM_STEP_VALUE_PROPERTY, String.valueOf(param[5]), false);
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_AM_MAX_FREQ_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[7],param[6])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_AM_MIN_FREQ_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[9],param[8])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_AM_STEP_VALUE_PROPERTY, String.valueOf(param[10]), false);	
				}
			case HeadUnitMcuProtocol.MA_CMD_RDS_INFO:
				if(param != null && param.length == 4)
				{
					Log.i(TAG, "receive radio RDS info");
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					/*mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_TP_STATUS_PROPERTY, String.valueOf(param[0]), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_TA_STATUS_PROPERTY, String.valueOf(param[1]), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_AF_STATUS_PROPERTY, String.valueOf(param[2]), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_TYPE_PROPERTY, String.valueOf(param[3]), true);*/
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_PS_INFO:
				if(param != null && param.length == 8)
				{
					Log.i(TAG, "receive PS info:");
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					/*String psInfo = ServiceHelper.toString(param, 0, 8, "UTF-8");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_PS_INFO_PROPERTY, psInfo, true);
					*/
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_SWITCH_TO_RADIO:
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive radio switch to radio");
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(param[0]), true);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_RADIO_AUDIO_STATUS:
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive radio audio status:"+param[0]);
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_RADIO,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY, String.valueOf(param[0]), true);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_RADIO_ACK:
				if(param != null)
				{
					Log.i(TAG, "receive radio ack");
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_STEER_STUDY_STATUS:
				if(param != null && param.length > 0)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_STEER_STUDY,cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_STATE_PROPERTY, String.valueOf(param[0]), false);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_STEER_KEY_STATUS:
				if(param != null && param.length ==3 && param[0] == 0x05)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_STEER_STUDY,cmd);
					Log.i(TAG, "receive steer study key status,KEY0:"+param[1]+",KEY1:"+param[2]);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_STEER_KEY_INFO:
				if(param != null && param.length ==7 && param[0] == 0x06)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_STEER_STUDY,cmd);
					if(param[2] == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_FUNC_ID_PROPERTY, String.valueOf(param[1]), true);
					}
				}
				break;
			//已学习的键按下返回的值
			//1S内的重复按键抛弃
			case HeadUnitMcuProtocol.MA_CMD_STEER_KEY2:
				if(param != null && param.length >=2 && param[0] == 0x08)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_STEER_STUDY,cmd);
					onHandleKey(HeadUnitMcuProtocol.swcKeyToUserKey(param[1]), param[2]);
//					long currentTime = SystemClock.uptimeMillis();
//					if(currentTime - mSWCKeyPressedTime > 1000)
//					{
//						mSWCKeyPressedTime = currentTime;
//						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(HeadUnitMcuProtocol.swcKeyToUserKey(param[1])), true);
//					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_STEER_STUDY_ACK:
				if(param != null)
				{
					Log.i(TAG, "receive steer study ack");
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_USER_KEY2:
				if(param != null && param.length >= 2)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS, cmd);
					onHandleKey(VehiclePropertyConstants.USER_KEY_OFFSET+param[0], param[1]);
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_OFFSET+param[0]), true);
				}
				break;
			//此处应该不会到达
			case HeadUnitMcuProtocol.MA_CMD_CAN_INFO:
				if(param != null && param.length > 0)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_CAN,cmd);
					Log.i(TAG, "receive can info:"+ServiceHelper.toString(param));
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_DVR_INFO:
				if(param != null && param.length > 0)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					Log.i(TAG, "receive dvr info:"+ServiceHelper.toString(param));
					if(mDVRManager != null)
					{
						mDVRManager.onReceiveData(param);
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_TPMS_INFO:
				if(param != null && param.length >= 6)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS,cmd);
					Log.i(TAG, "receive tpms info:"+ServiceHelper.toString(param));
					if(mTpmsManager != null)
					{
						mTpmsManager.onReceiveData(param);
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_BACKLIGHT_BRIGHTNESS:
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive backlight brightness info:"+param[0]);
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS, cmd);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY,String.valueOf(param[0]),false);
					if(mEnableSyncBackLight)
					{
						syncSystemBacklightBrightness(param[0]);
						mEnableSyncBackLight = false;
					}
					
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_BACKLIGHT_STATUS:
				if(param != null && param.length == 1)
				{
					Log.i(TAG, "receive backlight status info:"+param[0]);
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_SYS, cmd);
					int hisBacklightStatus = ServiceHelper.stringToIntSafe(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY));
					int illStatus = ServiceHelper.stringToIntSafe(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY));
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY,String.valueOf(param[0]),true);
					if(param[0] == 1 && hisBacklightStatus != param[0] && illStatus >= 0)
					{
						onHandleIllStatusChange(illStatus == 1 ? true:false);
					}
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_CAN_ACK:
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive can info ack");
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_UPGRADE_STATUS:
				if(param != null && param.length > 0)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_UPGRADE,cmd);
					Log.i(TAG, "receive mcu update status:"+param[0]);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_REQ_UPGRADE_FRAME:
				if(param != null && param.length == 2)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_UPGRADE,cmd);
					if(param[0]  == 1)
					{
						Log.i(TAG, "receive mcu request upgrade frame data:"+param[1]);
					}
					else
					{
						Log.i(TAG, "receive mcu no request upgrade frame data");
					}
					
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_UPGRADE_COMPLETE:
				if(param != null && param.length > 0)
				{
					returnAck(HeadUnitMcuProtocol.ACK_TYPE_UPGRADE,cmd);
					Log.i(TAG, "mcu update finish,and result:"+param[0]);
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_UPGRADE_ACK:
				if(param != null)
				{
					Log.i(TAG, "receive mcu upgrade ack");
				}
				break;
			case HeadUnitMcuProtocol.MA_CMD_SOURCE_GAIN_INFO:
				if(param != null && param.length >= 2)
				{
					if(param[0] == 0xFF && param.length >= 7)
					{
						Log.i(TAG, "receive all source gain info");
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.SOURCE_TYPE_ARM_ANALOG,param[1]}), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.SOURCE_TYPE_ARM_MEDIA,param[2]}), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.SOURCE_TYPE_TUNER,param[3]}), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.SOURCE_TYPE_AUX1,param[4]}), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.SOURCE_TYPE_AUX2,param[5]}), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.SOURCE_TYPE_RESERVED,param[6]}), false);
					}
					else
					{
						Log.i(TAG, "receive source gain info,source:"+param[0]+",gain:"+param[1]);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_SOURCE_GAIN_PROPERTY, ServiceHelper.intArrayToString(new int[]{param[0],param[1]}), false);
					}
				}
				break;
			default:
				break;
		}
	}
	
	//解决目前同波段(FM1-FM2)时同时设置波段与频点无效的问题
	private boolean isSameBand(int band)
	{
		if(band >= VehiclePropertyConstants.RADIO_BAND_FM1 && band <= VehiclePropertyConstants.RADIO_BAND_FM3
				&& mCurRadioBand >= VehiclePropertyConstants.RADIO_BAND_FM1 && mCurRadioBand <= VehiclePropertyConstants.RADIO_BAND_FM3)
		{
			return true;
		}
		else if(band >= VehiclePropertyConstants.RADIO_BAND_AM1 && band <= VehiclePropertyConstants.RADIO_BAND_AM2
				&& mCurRadioBand >= VehiclePropertyConstants.RADIO_BAND_AM1 && mCurRadioBand <= VehiclePropertyConstants.RADIO_BAND_AM2)
		{
			return true;
		}
		return false;
	}
}
