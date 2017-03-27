package com.mct.carmodels;

import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.app.LocalePicker.LocaleSelectionListener;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.carmodels.RZC_HondaSeriesManager.SystemTimeSetReceiver;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.R.string;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.FindActionModeCallback;

//睿智诚-大众高尔夫7车型协议管理
public class RZC_Volkswagen_Golf7_Manager extends MctVehicleManager
{
	private static String TAG = "RZC-Volkswagen-Golf7";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_VOLKSWAGEN_GOLF7;
	private int mPressedKeyValue = 0;
	private String mSyncMediaInfo = null;
	private long mSyncMediaTick = 0;
	private boolean mShowVehicleDoorInfo = true;
	private boolean mSupportSyncTime = true;
	private Timer mSyncVehicleInfoTimer = null;
	private long mSyncVehicleTimeTick = 0;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();
	
	public RZC_Volkswagen_Golf7_Manager(int carModel)
	{
		mCarModel = carModel;
	}
	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager,current carmodel:"+mCarModel);
		mService = service;
		mMcuManager = (HeadUnitMcuManager)service.getMcuManager();
		mCanMangaer = (CanManager)service.getVehicleManager();
		initVehicleConfig();
		if(mSupportSyncTime)
		{
			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction("android.intent.action.TIME_SET");
			mService.registerReceiver(mSystemTimeSetReceiver, iFilter);
			startSyncVehicleInfoTimer();
		}
		return true;
	}

	private void initVehicleConfig()
	{
		// 初始化雷达距离范围
		//mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "11", false);		
		
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
				ServiceHelper.intArrayToString(new int[]{4,8,8,4,4,11,11,4,6,6,6,6,6,6,6,6}), false);		
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_LEFT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RIGHT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF), false);

		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_ALL_KINDS_PROPERTY, "4");

	}
	
	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		mMcuManager = null;
		mCanMangaer = null;
		mShowVehicleDoorInfo = false;
		stopSyncVehicleInfoTimer();
		if(mSystemTimeSetReceiver != null && mService != null)
		{
			mService.unregisterReceiver(mSystemTimeSetReceiver);
		}
		
		return true;
	}

	public class SystemTimeSetReceiver extends BroadcastReceiver
	{
		public void onReceive(final Context context, final Intent intent)
		{
			Log.i(TAG, "receive broadcast message:"+intent.getAction());
			if(intent.getAction().equals("android.intent.action.TIME_SET") && mSupportSyncTime)
			{
				stopSyncVehicleInfoTimer();
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
				startSyncVehicleInfoTimer();
			}
		}
	}
	
	private void startSyncVehicleInfoTimer()
	{
		if (mSyncVehicleInfoTimer != null)
		{
			Log.w(TAG, "startSyncVehicleInfoTimer,timer is exist!");
			return;
		}
		Log.i(TAG, "startSyncVehicleInfoTimer");
		// 3S定时发送
		mSyncVehicleInfoTimer = new Timer();
		mSyncVehicleInfoTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				if(SystemClock.uptimeMillis() - mSyncVehicleTimeTick > 60*1000)
				{
					setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
				}
				//请求舒适性数据
				if(mMcuManager != null)
				{
					mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x60}));
					//mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO,0x10}));
					//mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_TPMS_INFO,0xFF}));
				}
			}
		}, 10*1000, 25*1000);
	}

	// 停止源同步定时器
	private void stopSyncVehicleInfoTimer()
	{
		if (mSyncVehicleInfoTimer != null)
		{
			Log.i(TAG, "stopSyncVehicleInfoTimer");
			mSyncVehicleInfoTimer.cancel();
			mSyncVehicleInfoTimer.purge();
			mSyncVehicleInfoTimer = null;
		}
	}

	
	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		if(mCarModel == CarModelDefine.CAR_MODEL_VOLKSWAGEN_17MAGOTAN)
		{
			return ServiceHelper.combineArray(RZC_Volkswagen_Golf7_Protocol.VEHICLE_CAN_PROPERITIES, 
					new int[]{VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY});
		}
		return RZC_Volkswagen_Golf7_Protocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_Volkswagen_Golf7_Protocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_Volkswagen_Golf7_Protocol.getProperityPermission(RZC_Volkswagen_Golf7_Protocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_Volkswagen_Golf7_Protocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_Volkswagen_Golf7_Protocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_Volkswagen_Golf7_Protocol.getProperityDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		boolean bRet = false;
		if(mMcuManager == null || mCanMangaer == null)
		{
			Log.e(TAG, "McuManager is not ready");
			return bRet;
		}
		try
		{
			switch(propId)
			{
				case VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY:
					int cmd = Integer.valueOf(value);
					switch(cmd)
					{
						case VehiclePropertyConstants.CANBOX_CMD_REQ_START:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SWITCH, new int[]{0x01}));
							reqVehicleInitInfo(0);
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SWITCH,new int[]{0x00}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_ENV_TEMP,0xFF}));
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_AIR_CONDITIONING_INFO,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_PARKING_STATUS_INFO,0xFF}));;
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_FRONT_RADAR_INFO,0xFF}));
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_REAR_RADAR_INFO,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_TPMS_INFO,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_BASE_INFO,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_STEERING_WHEEL_ANGLE,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_PROTOCOL_VERSION_INFO,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_SETTING:
							reqVehicleInitInfo(RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS);
							bRet = true;
							break;
						case VehiclePropertyConstants.CANBOX_CMd_REQ_SERVICE_INFO:
							reqVehicleInitInfo(RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO);
							bRet = true;
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME:
							mSyncVehicleTimeTick = SystemClock.uptimeMillis();
							Time time = new Time();
							time.setToNow();
							String strTimeFormat = android.provider.Settings.System.getString(mService.getContentResolver(),
									android.provider.Settings.System.TIME_12_24);
							if(strTimeFormat != null && strTimeFormat.equals("12"))
							{
								if(time.hour > 12)
								{
									time.hour = time.hour-12;
								}
								else if(time.hour == 0)
								{
									time.hour = 12;
								}
								time.hour |= 0x80;
							}
							time.year = time.year-2000;
							if(time.year < 0)
							{
								time.year = 0;
							}
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SET_TIME, new int[] { time.year,time.month,time.monthDay+1,time.hour,time.minute,time.second,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_SET:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x22,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_DRIVE_DATA_SINCE_START:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x8A,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_DRIVE_DATA_LONG_TERM:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x8B,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_DRIVER_ASSIST_SYSTEM:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xC1,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_PARKING_AND_MANOEUVRING:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xC2,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_LIGHT_SETTING:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xC3,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_MIRRORS_AND_WIPERS:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xC4,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_OPENING_AND_CLOSING:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xC5,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_MULTIFUNCTION_DISPLAY:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xC6,0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_ALL_SETTING:
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xC7,0x01}));
							break;
						default:
							break;
					}
					return bRet;
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{Integer.valueOf(value),0xFF}));
				case VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY:
					if (mSyncMediaInfo != null && mSyncMediaInfo.equals(value) && (SystemClock.uptimeMillis() - mSyncMediaTick) < 1000)
					{
						// filter post too fast
						Log.i(TAG, "media info sync too fast!");
						break;
					}
					mSyncMediaInfo = value;
					mSyncMediaTick = SystemClock.uptimeMillis();
					HashMap<Integer, String> sourceInfoMap = VehicleManager.stringToHashMap(value);
					if (sourceInfoMap != null && sourceInfoMap.size() > 0)
					{
						int sourceType = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY));
						if (sourceType == -1)
						{
							Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
							break;
						}
						Log.i(TAG, "sync source media type:" + sourceType);
						// 更新收音信息
						if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_TUNER)
						{
							Log.i(TAG, "sync tuner type info");
							int band = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY));
							if (band >= VehiclePropertyConstants.RADIO_BAND_FM1 && band <= VehiclePropertyConstants.RADIO_BAND_FM3)
							{
								band = 0x00;
							}
							else if (band >= VehiclePropertyConstants.RADIO_BAND_AM1 && band <= VehiclePropertyConstants.RADIO_BAND_AM2)
							{
								band = 0x10;
							}
							else
							{
								Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
								break;
							}
							int freq = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] {sourceType,0x01,band,freq&0xFF,(freq>>8)&0xFF,0x00,0x00,0x00}));
						}
						// 更新媒体格式信息
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB)
						{
							Log.i(TAG, "sync usb type info");
							int currentPlayTime = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY));

							int curTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY));
							int totalTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY));
							currentPlayTime = currentPlayTime < 0 ? 0 : currentPlayTime;
							curTrack = curTrack < 0 ? 0 : curTrack;
							totalTrack = totalTrack < 0 ? 0 : totalTrack;
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] {sourceType,0x11, curTrack & 0xFF, (curTrack >> 8) & 0xFF,totalTrack & 0xFF, (totalTrack >> 8) & 0xFF, currentPlayTime / 60, currentPlayTime % 60 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							Log.i(TAG, "sync other type info");
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] {sourceType,0x11, 0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							Log.i(TAG, "sync aux type info");
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] {sourceType,0x11, 0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));

							//bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] { sourceType, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{
							Log.i(TAG, "sync off type");
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] {sourceType,0x11, 0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
							//bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] { 0x0C, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE)
						{
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] { VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE, 0x40, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							Log.i(TAG, "sync bt music type info");
							bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] {sourceType,0x11, 0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));

							//bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] { 0x0B, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
					}
					return bRet;
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int connStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY));
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
						String number = btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_NUMBER_PROPERTY);
						String strCallStatus = null;
						if(connStatus == 1)
						{
							//来电
							if(callStatus == CarService.CALL_STATE_INCOMING)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] { VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE, 0x40, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00}));
								strCallStatus = "来电";
								int []incomingArray = ServiceHelper.combineArray(new int[]{0x03,0x01},ServiceHelper.byteArrayToIntArray(strCallStatus.getBytes()));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO,incomingArray));
								if(number != null && number.length() > 0)
								{
									int []phoneNumberArray = ServiceHelper.combineArray(new int[]{0x01,0x01},ServiceHelper.byteArrayToIntArray(number.getBytes()));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO,phoneNumberArray));
								}
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_STATUS, new int[] {0x00,0x11}));
							}
							//呼出
							else if(callStatus == CarService.CALL_STATE_DIAL || callStatus == CarService.CALL_STATE_DIALING)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_SOURCE, new int[] { VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE, 0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
								strCallStatus = "呼出";
								int []outgoingArray = ServiceHelper.combineArray(new int[]{0x03,0x01},ServiceHelper.byteArrayToIntArray(strCallStatus.getBytes()));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO,outgoingArray));
								if(number != null && number.length() > 0)
								{
									int []phoneNumberArray = ServiceHelper.combineArray(new int[]{0x01,0x01},ServiceHelper.byteArrayToIntArray(number.getBytes()));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO,phoneNumberArray));
								}
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_STATUS, new int[] {0x54,0x12}));
							}
							//通话中
							else if (callStatus == CarService.CALL_STATE_COMMUNICATING || callStatus == CarService.CALL_STATE_WAITING || callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
							{
								strCallStatus = "通话中";
								int []incomingArray = ServiceHelper.combineArray(new int[]{0x03,0x01},ServiceHelper.byteArrayToIntArray(strCallStatus.getBytes()));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO,incomingArray));
							
								if(number != null && number.length() > 0)
								{
									int []phoneNumberArray = ServiceHelper.combineArray(new int[]{0x01,0x01},ServiceHelper.byteArrayToIntArray(number.getBytes()));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO,phoneNumberArray));
								}
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO, new int[] { }));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_STATUS, new int[] {0x54,0x14}));
							}
							//Hold状态
							else if(callStatus == CarService.CALL_STATE_HOLD)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO, new int[] { }));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_STATUS, new int[] {0x54,0x13}));
							}
							//通话结束
							else if(callStatus == CarService.CALL_STATE_TERMINATED)
							{
								strCallStatus = "结束";
								int []incomingArray = ServiceHelper.combineArray(new int[]{0x03,0x01},ServiceHelper.byteArrayToIntArray(strCallStatus.getBytes()));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_INFO,incomingArray));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_STATUS, new int[] {0x54,0x15}));
							}
							else 
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_PHONE_STATUS, new int[] {0x54,0x10}));
							}
						}
						bRet = true;
					}
					return bRet;
				case VehicleInterfaceProperties.VIM_ELECTRONIC_STABILITY_CONTROL_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x10,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_SPEED_WARNING_STATUS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x20,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_SPEED_WARNING_VALUE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x21,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_LANE_ASSIST_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x30,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_DRIVER_ALERT_SYSTEM_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x31,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_LAST_DISTANCE_SELECTED:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x32,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_FRONT_ASSIST_ACTIVE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x33,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_FRONT_ASSIST_ADVANCE_WARNING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x34,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_FRONT_ASSIST_DISPLAY_DISTANCE_WARNING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x35,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_LANE_KEEP_ASSIST_ACTIVE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x36,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_AUTO_CRUISE_DRIVE_PROGRAM_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x37,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_AUTO_CRUISE_DRIVE_DISTANCE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x38,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_PARKING_AND_MANOEUVRING_AUTO_ACTIVE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x40,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_PARKING_AND_MANOEUVRING_ACTIVE_STATUS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x45,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_FRONT_VOLUME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x41,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_FRONT_TONE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x42,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_REAR_VOLUME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x43,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_REAR_TONE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x44,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY:
					int reverseVideoMode = Integer.valueOf(value);
					reverseVideoMode = Math.max(0, Math.min(3, reverseVideoMode));
					if(reverseVideoMode == 1)
					{
						reverseVideoMode = 3;
					}
					else if(reverseVideoMode == 3)
					{
						reverseVideoMode = 1;
					}
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x46,reverseVideoMode}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_CAMERA_BRIGHTNESS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x47,Integer.valueOf(value)}));
					break;	
				case VehicleInterfaceProperties.VIM_REVERSE_CAMERA_CONTRAST_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x48,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_CAMERA_SATURATION_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x49,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_LIGHT_AUTO_SWITCH_ON_TIME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x50,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_HEADLIGHT_IN_RAIN_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x51,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_LANE_CHANGE_TRUN_LIGHT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x52,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_METER_LIGHT_BRIGHTNESS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x53,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_BACK_HOME_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x54,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_LEAVE_HOME_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x55,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_TRAVEL_MODEL_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x56,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_DOOR_AMBIENT_LIGHT_BRIGHTNESS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x57,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_FOOTWELL_LIGHT_BRIGHTNESS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x58,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MOTORWAY_LIGHT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x59,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_DYNAMIC_LIGHT_ASSIST_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x5A,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ROOF_LIGHT_BRIGHTNESS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x5B,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_FRONT_LIGHT_BRIGHTNESS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x5C,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ALL_LIGHT_BRIGHTNESS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x5D,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AMBIENT_LIGHT_COLOR_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x5E,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MW_SYNC_ADJUSTMENT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x60,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MW_LOWER_WHILE_REVERSING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x61,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MW_AUTOMATIC_WIPING_IN_RAIN_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x62,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MW_REAR_WINDOW_WIPEING_IN_REVERSE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x63,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MW_FOLD_AWAY_AFTER_PARKING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x64,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_WINDOW_CONV_OPEN_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x70,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_DOOR_UNLOCK_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x71,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AUTOMATIC_LOCKING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x72,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_CURRENT_FUEL_CONSUMPTION_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x80,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_AVG_FUEL_CONSUMPTION_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x81,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_CONVENIENCE_CONSUMERS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x82,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_ECONOMY_RUNNING_PROMPT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x83,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_DRIVE_TIME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x84,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_DRIVE_ODOMETER_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x85,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_AVG_SPEED_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x86,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_DIGIT_SPEED_DISPLAY_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x87,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_SPEED_ALERT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x88,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MFD_OIL_TEMP_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x89,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_UNIT_DISTANCE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x90,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_UNIT_SPEED_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x91,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_UNIT_TEMP_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x92,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_UNIT_VOLUME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x93,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_UNIT_FUEL_CONSUMPTION_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x94,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_UNIT_TPMS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0x95,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xAB,Integer.valueOf(value)}));
					break;
				//原车空调控制
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_PROPERTY:
					int acCmd = ServiceHelper.stringToIntSafe(value);
					switch (acCmd)
					{
						case VehiclePropertyConstants.AIR_CONDITION_CMD_POWER_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB2,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_POWER_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB2,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AQS_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB0,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AQS_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB0,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_SYNC_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB3,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_SYNC_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB3,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_CENTER_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB4,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_CENTER_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB4,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_DOWN_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB5,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_DOWN_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB5,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_UP_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB6,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_UP_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB6,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_LEFT_TEMP_UP:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB8,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_LEFT_TEMP_DOWN:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB8,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_RIGHT_TEMP_UP:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB9,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_RIGHT_TEMP_DOWN:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB9,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_REAR_TEMP_UP:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBA,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_REAR_TEMP_DOWN:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBA,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_REAR_LOCK:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBC,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_REAR_UNLOCK:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBC,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBD,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBD,0x00}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_INTERIOR_CYCLE_MODE_ON:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBE,0x01}));
						case VehiclePropertyConstants.AIR_CONDITION_CMD_INTERIOR_CYCLE_MODE_OFF:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBE,0x00}));
						default:
							break;
					}
					break;
				//空调模式
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_MODE_SETTING_PROPERTY:
					Log.i(TAG, "set fan speed auto mode:"+value); 
					return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xBB,Integer.valueOf(value)}));
				//自动模式吹风风速
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_AUTO_MODE_PROPERTY:
					Log.i(TAG, "set fan speed auto mode:"+value); 
					return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB1,Integer.valueOf(value)}));
				//风速
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_PROPERTY:
				case VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY:
					Log.i(TAG, "set fan speed:"+value);
					int[] fanSpeeds = ServiceHelper.stringToIntArray(value);
					if(fanSpeeds != null && fanSpeeds.length >= 1)
					{
						return mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_VEHICLE_SETTING, new int[] {0xB7,Math.min(7, Math.max(0, Integer.valueOf(value)))}));
					}
			   //驾驶模式设置	
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "setPropValue Exception,propId:"+propId+",value:"+value);
			return false;
		}
		//针对设置后模拟器不返回状态的情况
		if(bRet)
		{
			mService.cachePropValue(propId, value, false);
		}
		return bRet;
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		if (data == null || mCanMangaer == null) { return; }
		int[] tmp = data.getIntArray(CarService.KEY_DATA);
		int[] buffer = mCanMangaer.unPack(tmp);
		if(buffer == null  || buffer.length == 0)
		{
			return;
		}
		int[] param = new int[buffer.length - 1];
		System.arraycopy(buffer, 1, param, 0, param.length);
		onReceiveData(buffer[0], param);
	}
	
	private void onReceiveData(int cmd, int[] param)
	{
		if(mMcuManager == null)
		{
			Log.e(TAG, "Mcu Manager is not ready");
			return;
		}
		mMcuManager.returnCanAck(cmd);
		switch (cmd)
		{
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_BACKLIGHT_INFO:
				if(param != null && param.length == 2)
				{
					//原车屏背光亮度
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_BRIGHTNESS_LVL_PROPERTY, String.valueOf(param[0]), false);
					//原车按键板背光亮度
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_BRIGHTNESS_LVL_PROPERTY, String.valueOf(param[1]), false);
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_SPEED_INFO:
				if(param != null && param.length == 3)
				{
					/*int unit = ServiceHelper.getBit(param[2], 0);
					float speed = (param[1]*256+param[0])/16.0f;
					//mph->km/h
					if(unit == 1)
					{
						speed = speed*ServiceHelper.KM_PER_MILE;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY, String.format("%.1f", speed), true);*/
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_STEERING_WHEEL_KEY:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:"+param[0]+",status:"+param[1]);
					//0 按键释放
					if(param[1] == 0 && mPressedKeyValue != RZC_Volkswagen_Golf7_Protocol.SWC_KEY_NONE)
					{
						//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(RZC_Volkswagen_Golf7_Protocol.swcKeyToUserKey(mPressedKeyValue)), true);
						mService.broadcastKeyEvent(RZC_Volkswagen_Golf7_Protocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_Volkswagen_Golf7_Protocol.SWC_KEY_NONE;
					}
					//1 按键按下
					else if(param[1] == 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_Volkswagen_Golf7_Protocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					//2 按键持续按下
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_STEERING_WHEEL_CMD:
				if(param != null && param.length == 2)
				{
					int userKey = RZC_Volkswagen_Golf7_Protocol.swcCmdToUserKey(param[0]);
					if(userKey >= 0)
					{
						Log.i(TAG, "receive steering sheel cmd:"+userKey);
						mService.broadcastKeyEvent(userKey, KeyEvent.ACTION_DOWN, 0);
						mService.broadcastKeyEvent(userKey, KeyEvent.ACTION_UP, 0);
					}
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_AIR_CONDITIONING_INFO:
				if(param != null && param.length >= 7)
				{
					Log.i(TAG, "receive air condition info");
					//空调开关
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), true);
					if(acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						//清空空调数据状态
						clearACStatus();
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					//请求显示空调信息
					int showAcBar = ServiceHelper.getBit(param[1], 4);
					if(showAcBar == 0)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						break;
					}
					//制冷模式
					int[] acMode = new int[6];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON:VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON:VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 4) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO2_ON:VehiclePropertyConstants.AC_COOL_MODE_AUTO2_OFF;
					acMode[3] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_SYNC_ON:VehiclePropertyConstants.AC_COOL_MODE_SYNC_OFF;
					acMode[4] = ServiceHelper.getBit(param[4], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON:VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					acMode[5] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON:VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);
					
					//循环模式
					int[] cycleMode = new int[1];
					if(ServiceHelper.getBit(param[4], 5) == 1)
					{
						cycleMode[0] = VehiclePropertyConstants.AC_AUTO_IN_OUT_CYCLE_MODE;
					}
					else
					{
						cycleMode[0] = ServiceHelper.getBit(param[0], 5) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE:VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;	
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);
					//后座空调锁定灯状态
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_REAR_FLAG_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
					
					//风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = ServiceHelper.getBit(param[1], 7) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[1], 5) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					//风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[]{ServiceHelper.getBits(param[1], 0, 4)}), false);
					
					//空调温度信息
					int tempUnit = ServiceHelper.getBit(param[4], 0) == 0 ? VehiclePropertyConstants.AC_TEMP_UNIT_C:VehiclePropertyConstants.AC_TEMP_UNIT_F;
					float []interiorTemp = null;
					//支持后座温度
					if(param.length > 7)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{tempUnit,tempUnit,tempUnit}), false);
						interiorTemp = new float[3];
					}
					else 
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{tempUnit,tempUnit}), false);
						interiorTemp = new float[2];
					}
					//驾驶员位温度(18-26)
					if(param[2] == 0x00)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if(param[2] == 0x1F)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						if(tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_C)
						{
							interiorTemp[0] = (float)Math.max(RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_C, Math.min(RZC_Volkswagen_Golf7_Protocol.AC_HIGHEST_TEMP_C, RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_C+(param[2]-1)*0.5));
						}
						else 
						{
							interiorTemp[0] = (float)Math.max(RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_F, Math.min(RZC_Volkswagen_Golf7_Protocol.AC_HIGHEST_TEMP_F, RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_F+(param[2]-1)));
						}
						
					}
					
					//副驾驶员位温度
					if(param[3] == 0x00)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if(param[3] == 0x1F)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else 
					{
						if(tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_C)
						{
							interiorTemp[1] = (float)Math.max(RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_C, Math.min(RZC_Volkswagen_Golf7_Protocol.AC_HIGHEST_TEMP_C, RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_C+(param[3]-1)*0.5));
						}
						else
						{
							interiorTemp[1] = (float)Math.max(RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_F, Math.min(RZC_Volkswagen_Golf7_Protocol.AC_HIGHEST_TEMP_F, RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_F+(param[3]-1)));
						}
					}
					if(param.length > 7)
					{
						//后座温度
						if(param[7] == 0x00)
						{
							interiorTemp[2] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if(param[7] == 0x1F)
						{
							interiorTemp[2] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else 
						{
							if(tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_C)
							{
								interiorTemp[2] = (float)Math.max(RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_C, Math.min(RZC_Volkswagen_Golf7_Protocol.AC_HIGHEST_TEMP_C, RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_C+(param[7]-1)*0.5));
							}
							else
							{
								interiorTemp[2] = (float)Math.max(RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_F, Math.min(RZC_Volkswagen_Golf7_Protocol.AC_HIGHEST_TEMP_F, RZC_Volkswagen_Golf7_Protocol.AC_LOWEST_TEMP_F+(param[7]-1)));
							}
						}
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
					//前后窗除雾
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 6)), false);
					
					//座椅加热信息
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[5], 4, 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_PSNGR_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[5], 0, 3)), false);
					
					//自动模式吹风风速
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_AUTO_MODE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[6], 0, 2)), false);
					
					if(showAcBar == 1)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						mCanMangaer.showAirConditionEvent(true);
					}
				}
				break;
				/*
				 * 此数据在17迈腾车上，睿智诚CAN盒,会定时上报距离为0xFF的数据，如果处理为等级0，则会导致雷达等级线闪烁;
				 * 如果作为无效数据不处理，则会导致障碍物距离由近及远后雷达等级线不消失
				 */
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_REAR_RADAR_INFO:
				if(param != null && param.length == 6)
				{
					Log.i(TAG, "receive rear radar info,dislevel:"+ServiceHelper.intArrayToString(param));
					int[] cacheRearRadarArray = new int[]{0,0,0,0};
					String cacheRearRadarValue = mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY);
					if(cacheRearRadarValue != null)
					{
						cacheRearRadarArray = ServiceHelper.stringToIntArray(cacheRearRadarValue);
					}
					//转化为1-12等级
					int []radarDisLevel = new int[4];
					radarDisLevel[0] = tramslateRadarDistance(param[0],4,cacheRearRadarArray[0]);
					radarDisLevel[1] = tramslateRadarDistance(param[1],11,cacheRearRadarArray[1]);
					radarDisLevel[2] = tramslateRadarDistance(param[2],11,cacheRearRadarArray[2]);
					radarDisLevel[3] = tramslateRadarDistance(param[3],4,cacheRearRadarArray[3]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,VehicleManager.intArrayToString(radarDisLevel), false);
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_FRONT_RADAR_INFO:
				if(param != null && param.length == 6)
				{
					Log.i(TAG, "receive front radar info,dislevel:"+ServiceHelper.intArrayToString(param));
					int[] cacheFrontRadarArray = new int[]{0,0,0,0};
					String cacheFrontRadarValue = mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY);
					if(cacheFrontRadarValue != null)
					{
						cacheFrontRadarArray = ServiceHelper.stringToIntArray(cacheFrontRadarValue);
					}
					
					int []radarDisLevel = new int[4];
					radarDisLevel[0] = tramslateRadarDistance(param[0],4,cacheFrontRadarArray[0]);
					radarDisLevel[1] = tramslateRadarDistance(param[1],8,cacheFrontRadarArray[1]);
					radarDisLevel[2] = tramslateRadarDistance(param[2],8,cacheFrontRadarArray[2]);
					radarDisLevel[3] = tramslateRadarDistance(param[3],4,cacheFrontRadarArray[3]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(radarDisLevel), false);
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_LEFT_RADAR:
				if(param != null && param.length == 6)
				{
					Log.i(TAG, "receive left radar info");
					int []radarDisLevel = new int[4];
					radarDisLevel[0] = param[0];
					radarDisLevel[1] = param[1];
					radarDisLevel[2] = param[2];
					radarDisLevel[3] = param[3];
					for(int i=0;i<4;i++)
					{
						if(radarDisLevel[i] < 0 || radarDisLevel[i] > 6)
						{
							radarDisLevel[i] = 0;
						}
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_LEFT_RADAR_PROPERTY, VehicleManager.intArrayToString(radarDisLevel), false);
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_RIGHT_RADAR:
				if(param != null && param.length == 6)
				{
					Log.i(TAG, "receive right radar info");
					int []radarDisLevel = new int[4];
					radarDisLevel[0] = param[0];
					radarDisLevel[1] = param[1];
					radarDisLevel[2] = param[2];
					radarDisLevel[3] = param[3];
					for(int i=0;i<4;i++)
					{
						if(radarDisLevel[i] < 0 || radarDisLevel[i] > 6)
						{
							radarDisLevel[i] = 0;
						}
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RIGHT_RADAR_PROPERTY, VehicleManager.intArrayToString(radarDisLevel), false);
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_BASE_INFO:
				if(param != null && param.length == 2)
				{
					//带车门信息
					if(ServiceHelper.getBit(param[0], 0) == 1)
					{
						int flStatus = ServiceHelper.getBit(param[0],6);
						int frStatus = ServiceHelper.getBit(param[0],7);
						int rlStatus = ServiceHelper.getBit(param[0],4);
						int rrStatus = ServiceHelper.getBit(param[0],5);
						int trunkStatus = ServiceHelper.getBit(param[0],3);
						int hoodStatus = ServiceHelper.getBit(param[0],2);
						
						String strHisrotyFlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY);
						String strHisrotyFrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY);
						String strHisrotyRlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY);
						String strHisrotyRrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY);
						String strHisrotyTrunkStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY);
						String strHisrotyHoodStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY);
						
						//有变化才上报
						if (mShowVehicleDoorInfo || strHisrotyFlStatus == null || strHisrotyFrStatus == null || strHisrotyRlStatus == null || strHisrotyRrStatus == null || 
								strHisrotyTrunkStatus == null || strHisrotyHoodStatus == null ||Integer.valueOf(strHisrotyFlStatus) != flStatus || Integer.valueOf(strHisrotyFrStatus) != frStatus || 
								Integer.valueOf(strHisrotyRlStatus) != rlStatus || Integer.valueOf(strHisrotyRrStatus) != rrStatus || 
								Integer.valueOf(strHisrotyTrunkStatus) != trunkStatus || Integer.valueOf(strHisrotyHoodStatus) != hoodStatus)
						{
							mShowVehicleDoorInfo = false;
							
							mService.broadcastVehicleDoorInfo(flStatus, frStatus, rlStatus, rrStatus, trunkStatus, hoodStatus);
							
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY, String.valueOf(flStatus), true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY, String.valueOf(frStatus), true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY, String.valueOf(rlStatus), true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY, String.valueOf(rrStatus), true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY, String.valueOf(trunkStatus), true);
							//引擎盖
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY, String.valueOf(hoodStatus), true);
						}

					}
					
					int reverseState = ServiceHelper.getBit(param[1], 0);
					//0 P档,手刹；1 非P档
					int brakeState = (ServiceHelper.getBit(param[1], 1) == 0 ? 1 : 0);
					int lightState = ServiceHelper.getBit(param[1], 2);
					Log.i(TAG, "receive base info(unused),reverseState:"+reverseState+",brakeState:"+brakeState+",headlightState:"+lightState);
					/*
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY, String.valueOf(reverseState), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY, String.valueOf(brakeState), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, String.valueOf(lightState), true);
					*/
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_PARKING_STATUS_INFO:
				if(param != null && param.length >= 1)
				{
					int opsStatus = ServiceHelper.getBit(param[0], 1);
					int radarTone = ServiceHelper.getBit(param[0], 0);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY, String.valueOf(radarTone), true);
					// 倒车辅助状态
					String strCurParkAssistStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY);
					if (strCurParkAssistStatus == null || Integer.valueOf(strCurParkAssistStatus) != opsStatus)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY, String.valueOf(opsStatus), false);
						mService.broadcastRadarPreview(opsStatus);
					}
				}
				break;
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_ENV_TEMP:
				if(param != null && param.length == 3)
				{
					Log.i(TAG, "receive environment temp info");
					int tempUnit = param[0];
					float temp = 0.0f;
					//value = 补码/10
					//负数
					if(ServiceHelper.getBit(param[2],7) == 1)
					{
						temp = -ServiceHelper.MAKEWORD((~((param[1]-1)&0xFF)&0xFF),(~(param[2]&0xFF))&0xFF)/10.0f;
					}
					//正数
					else
					{
						temp = ServiceHelper.MAKEWORD(param[1], param[2])/10.0f;
					}
					//华氏温度转换成摄氏温度
					/*if(tempUnit == 1)
					{
						temp = (temp-32)*5.0f/9.0f;
					}*/
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_UNIT_PROPERTY, String.valueOf(tempUnit), false);
					temp = (float)Math.min(RZC_Volkswagen_Golf7_Protocol.EXTERN_HIGHEST_TEMP, Math.max(RZC_Volkswagen_Golf7_Protocol.EXTERN_LOWEST_TEMP, temp));
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.format("%.1f",temp), true);
				}
				break;
			//车身状态信息
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS:
				if(param != null && param.length == 5)
				{
					Log.i(TAG, "receive vehicle status info");
					switch(param[0])
					{
						//语言
						case 0x00:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_LANGUAGE_PROPERTY, String.valueOf(param[1]), false);
							break;
						//ESC系统
						case 0x10:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ELECTRONIC_STABILITY_CONTROL_PROPERTY, String.valueOf(param[1]), false);
							break;
						//轮胎设置
						case 0x20:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_SPEED_WARNING_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_SPEED_UNIT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_SPEED_WARNING_VALUE_PROPERTY, String.valueOf(param[2]), false);
							break;
						//行车辅助系统
						case 0x30:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_LANE_ASSIST_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_DRIVER_ALERT_SYSTEM_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_LANE_KEEP_ASSIST_ACTIVE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 0)), false);
							break;
						//行车辅助系统2
						case 0x31:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_LAST_DISTANCE_SELECTED, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_FRONT_ASSIST_ACTIVE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_FRONT_ASSIST_ADVANCE_WARNING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 2)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_FRONT_ASSIST_DISPLAY_DISTANCE_WARNING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);

							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_AUTO_CRUISE_DRIVE_PROGRAM_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0,4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_AUTO_CRUISE_DRIVE_DISTANCE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 4,4)), false);
							break;
						//驻车和调车
						case 0x40:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_PARKING_AND_MANOEUVRING_AUTO_ACTIVE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_PARKING_AND_MANOEUVRING_ACTIVE_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_VOLUME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0, 4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_TONE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 4, 4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_VOLUME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[3], 0, 4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_TONE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[3], 4, 4)), false);
							//倒车模式
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 0, 4)), false);
							break;
						//灯光设置
						case 0x50:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_LIGHT_AUTO_SWITCH_ON_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 0,4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_HEADLIGHT_IN_RAIN_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_LANE_CHANGE_TRUN_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DAYTIME_RUNNING_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 6)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_METER_LIGHT_BRIGHTNESS_PROPERTY, String.valueOf(param[2]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_BACK_HOME_MODE_PROPERTY, String.valueOf(param[3]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_LEAVE_HOME_MODE_PROPERTY, String.valueOf(param[4]), false);
							break;
						//灯光设置2
						case 0x51:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TRAVEL_MODEL_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_AMBIENT_LIGHT_BRIGHTNESS_PROPERTY, String.valueOf(param[2]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_FOOTWELL_LIGHT_BRIGHTNESS_PROPERTY, String.valueOf(param[3]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DYNAMIC_LIGHT_ASSIST_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MOTORWAY_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 0)), false);
							break;
						//灯光设置3
						case 0x52:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ROOF_LIGHT_BRIGHTNESS_PROPERTY, String.valueOf(param[1]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_FRONT_LIGHT_BRIGHTNESS_PROPERTY, String.valueOf(param[2]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AMBIENT_LIGHT_COLOR_PROPERTY, String.valueOf(param[3]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ALL_LIGHT_BRIGHTNESS_PROPERTY, String.valueOf(param[4]), false);
							break;
						//后视镜和雨刷
						case 0x60:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_SYNC_ADJUSTMENT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_LOWER_WHILE_REVERSING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_AUTOMATIC_WIPING_IN_RAIN_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 2)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_REAR_WINDOW_WIPEING_IN_REVERSE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_FOLD_AWAY_AFTER_PARKING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 0)), false);
							break;
						//开关状态
						case 0x70:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_WINDOW_CONV_OPEN_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 0,4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_UNLOCK_MODE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 4,4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTOMATIC_LOCKING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_INDUCTION_TRUNK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 1)), false);
							break;
						//多功能显示器
						case 0x80:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_CURRENT_FUEL_CONSUMPTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_AVG_FUEL_CONSUMPTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_CONVENIENCE_CONSUMERS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 2)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_ECONOMY_RUNNING_PROMPT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_DRIVE_TIME_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_DRIVE_ODOMETER_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_AVG_SPEED_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 6)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_DIGIT_SPEED_DISPLAY_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 7)), false);
							
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_SPEED_ALERT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MFD_OIL_TEMP_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 1)), false);
							break;
						//单位
						case 0x90:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_DISTANCE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_SPEED_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_TEMP_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 2)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_VOLUME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 4,4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_FUEL_CONSUMPTION_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0,4)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_TPMS_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 4,4)), false);
							break;
						case 0xA0:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_KEY_ACTIVED_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_KEY_MEMORY_MATCH_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
							break;
						case 0xB0:
							break;
					}
				}
				break;
			//方向盘角度
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "Receive ESP SterringWheel angle info");
					float angle 	= 0.0f;
					//向左
					if(param[1] > 128)
					{
						angle = -(256-param[0]+(255-param[1])*256);
					}
					else
					{
						angle = param[0] + param[1]*256;
					}

					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), true);
				}
				break;
			//行驶数据
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA:
				if(param != null && param.length == 6)
				{
					Log.i(TAG,"receive driving data");
					switch (param[0])
					{
						//续航里程
						case 0x10:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_UNIT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[2], param[3])), false);
							break;
						//自启动后行驶里程
						case 0x20:
							int distanceSinceStartUnit = ServiceHelper.getBit(param[1], 0);
							float distanceSinceStart = (param[2]+param[3]*256+param[4]*256*256+param[5]*256*256*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DISTANCE_SINCE_START_PROPERTY, ServiceHelper.floatArrayToString(new float[]{distanceSinceStartUnit,distanceSinceStart}), false);
							break;
						//自加油起行驶里程
						case 0x21:
							int distanceSinceRefuelingUnit = ServiceHelper.getBit(param[1], 0);
							float distanceSinceRefueling = (param[2]+param[3]*256+param[4]*256*256+param[5]*256*256*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DISTANCE_SINCE_REFUELING_PROPERTY, ServiceHelper.floatArrayToString(new float[]{distanceSinceRefuelingUnit,distanceSinceRefueling}), false);
							break;
						//长时间行驶里程
						case 0x22:
							int distanceLongTermUnit = ServiceHelper.getBit(param[1], 0);
							float distanceLongTerm = (param[2]+param[3]*256+param[4]*256*256+param[5]*256*256*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DISTANCE_LONG_TERM_PROPERTY, ServiceHelper.floatArrayToString(new float[]{distanceLongTermUnit,distanceLongTerm}), false);
							break;
						//自启动后平均油耗
						case 0x30:
							int avgConsumSinceStartUnit = ServiceHelper.getBits(param[1], 0,2);
							float avgConsumSinceStart = (param[2]+param[3]*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_CONSUMPTION_SINCE_START_PROPERTY, ServiceHelper.floatArrayToString(new float[]{avgConsumSinceStartUnit,avgConsumSinceStart}), false);
							break;
						//自加油起平均油耗
						case 0x31:
							int avgConsumSinceRefuelingUnit = ServiceHelper.getBits(param[1], 0,2);
							float avgConsumSinceRefueling = (param[2]+param[3]*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_CONSUMPTION_SINCE_REFUELING_PROPERTY, ServiceHelper.floatArrayToString(new float[]{avgConsumSinceRefuelingUnit,avgConsumSinceRefueling}), false);
							break;
						//长时间平均油耗
						case 0x32:
							int avgConsumLongTermUnit = ServiceHelper.getBits(param[1], 0, 2);
							switch (avgConsumLongTermUnit)
							{
								case 0:
									avgConsumLongTermUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_L_100KM;
									break;
								case 1:
									avgConsumLongTermUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_KM_L;
									break;
								case 2:
									avgConsumLongTermUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_MPG;
									break;
								case 3:
									avgConsumLongTermUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_MPG;
									break;
								default:
									avgConsumLongTermUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_MPG;
									break;
							}
							float avgConsumLongTerm = (param[2]+param[3]*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_CONSUMPTION_LONG_TERM_PROPERTY,  ServiceHelper.floatArrayToString(new float[]{avgConsumLongTermUnit,avgConsumLongTerm}), false);
							break;
						//自启动后平均车速
						case 0x40:
							int avgSpeedSinceStartUnit = ServiceHelper.getBit(param[1], 0);
							float avgSpeedSinceStart = (param[2]+param[3]*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_SPEED_SINCE_START_PROPERTY, ServiceHelper.floatArrayToString(new float[]{avgSpeedSinceStartUnit,avgSpeedSinceStart}), false);
							break;
						//自加油起平均车速
						case 0x41:
							int avgSpeedSinceRefuelingUnit = ServiceHelper.getBit(param[1], 0);
							float avgSpeedSinceRefueling = (param[2]+param[3]*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_SPEED_SINCE_REFUELING_PROPERTY, ServiceHelper.floatArrayToString(new float[]{avgSpeedSinceRefuelingUnit,avgSpeedSinceRefueling}), false);
							break;
						//长时间平均车速
						case 0x42:
							int avgSpeedLongTermUnit = ServiceHelper.getBit(param[1], 0);
							float avgSpeedLongTerm = (param[2]+param[3]*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_SPEED_LONG_TERM_PROPERTY, ServiceHelper.floatArrayToString(new float[]{avgSpeedLongTermUnit,avgSpeedLongTerm}), false);
							break;
						//自启动后行驶时间
						case 0x50:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TRAVELLING_TIME_SINCE_START_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[1], param[2])), false);
							break;
						//自加油起行驶时间
						case 0x51:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TRAVELLING_TIME_SINCE_REFUELING_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[1], param[2])), false);
							break;
						//长时间行驶时间
						case 0x52:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TRAVELLING_TIME_LONG_TERM_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[1], param[2])), false);
							break;
						//舒适性用电器
						case 0x60:
							int convConsumersUnit = ServiceHelper.getBit(param[1], 0);
							int convConsumers = param[2]+param[3]*256;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_CONV_CONSUMERS_VALUE_PROPERTY, ServiceHelper.intArrayToString(new int[]{convConsumersUnit,convConsumers}), false);
							break;
						//瞬时油耗
						case 0x61:
							int instantFuelConsumUnit = ServiceHelper.getBits(param[1], 0, 2);
							switch (instantFuelConsumUnit)
							{
								case 0:
									instantFuelConsumUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_L_100KM;
									break;
								case 1:
									instantFuelConsumUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_KM_L;
									break;
								case 2:
									instantFuelConsumUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_MPG;
									break;
								case 3:
									instantFuelConsumUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_MPG;
									break;
								default:
									break;
							}
							float instantFuelConsum = (param[2]+param[3]*256)/10.0f;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(instantFuelConsumUnit), true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY, String.format("%.1f", instantFuelConsum), true);
							break;
						default:
							break;
					}
				}
				break;
			//车辆提示信息
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_PROMPT_INFO:
				if(param != null && param.length == 0x08)
				{
					Log.i(TAG, "receive vehicle prompt info");
					int []vehicleStatusPromptInfo = null;
					if(param[0] > 0)
					{
						vehicleStatusPromptInfo = new int[param[0]];
						for(int i=0;i<param[0] && i < 6;i++)
						{
							vehicleStatusPromptInfo[i] = param[1+i];
						}
					}
					else
					{
						vehicleStatusPromptInfo = new int[1];
						vehicleStatusPromptInfo[0] = param[7] == 0 ? VehiclePropertyConstants.MESSAGE_START_STOP_SYSTEM_DEACTIVATED:VehiclePropertyConstants.MESSAGE_START_STOP_SYSTEM_ACTIVATED;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_STATUS_PROMPT_MESSAGE_PROPERTY, ServiceHelper.intArrayToString(vehicleStatusPromptInfo), false);
				}
				break;
			//舒适性用电器
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_CONV_CONSUME:
				if(param != null && param.length == 0x04)
				{
					Log.i(TAG, "Receive conv.consume info");
					if(param[0] > 0)
					{
						int []convConsumeNumInfo = new int[param[0]];
						for(int i=0;i<param[0] && i < 3;i++)
						{
							convConsumeNumInfo[i] = param[1+i];
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CONV_CONSUMERS_PROMPT_MESSAGE_PROPERTY, ServiceHelper.intArrayToString(convConsumeNumInfo), false);
					}
				}
				break;
			//保养服务信息
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO:
				if(param != null && param.length > 0)
				{
					//Vehicle No
					if(param[0] == 0x00)
					{
						String strVIN = ServiceHelper.toString(param, 1, param.length-1, "UTF-8");
						Log.i(TAG, "receive vehicle No:"+strVIN);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_VIN_PROPERTY, strVIN, true);
					}
					else if(param.length == 0x04)
					{
						switch (param[0])
						{
							//车况检查天数
							case 0x10:
								mService.cachePropValue(VehicleInterfaceProperties.VIM_CARE_INSPECTION_LEFT_DAYS_PROPERTY, ServiceHelper.intArrayToString(new int[]{ServiceHelper.getBits(param[1], 0, 4),param[2]+param[3]*256}), false);
								break;
							//车况检查里程
							case 0x11:
								mService.cachePropValue(VehicleInterfaceProperties.VIM_CARE_INSPECTION_LEFT_ODOMETER_PROPERTY, ServiceHelper.intArrayToString(new int[]{ServiceHelper.getBits(param[1], 4, 4),ServiceHelper.getBits(param[1], 0, 4),(param[2]+param[3]*256)*100}), false);
								break;
							//更换机油保养天数
							case 0x20:
								mService.cachePropValue(VehicleInterfaceProperties.VIM_CARE_REFUEL_OIL_DAYS_PROPERTY, ServiceHelper.intArrayToString(new int[]{ServiceHelper.getBits(param[1], 0, 4),param[2]+param[3]*256}), false);
								break;
							//更换机油保养里程
							case 0x21:
								mService.cachePropValue(VehicleInterfaceProperties.VIM_CARE_REFUEL_OIL_ODOMETER_PROPERTY, ServiceHelper.intArrayToString(new int[]{ServiceHelper.getBits(param[1], 4, 4),ServiceHelper.getBits(param[1], 0, 4),(param[2]+param[3]*256)*100}), false);
								break;
							default:
								break;
						}
					}
				}
				break;
			//胎压信息
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_TPMS_INFO:
				if(param != null && param.length == 0x04)
				{
					Log.i(TAG, "Receive TPMS info");
					for(int i=0;i<param.length;i++)
					{
						param[i] = tramslateTPMSStatus(param[i]);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_ALERT_INFO_PROPERTY, ServiceHelper.intArrayToString(param), true);
				}
				break;
			//协议版本信息
			case RZC_Volkswagen_Golf7_Protocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if(param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param,0,param.length,"UTF-8");
					Log.i(TAG, "receive protocol version info:"+version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,version, true);
				}
				break;
			default:
				break;
		}
	}
	
	private int tramslateTPMSStatus(int tpmsStatus)
	{
		switch (tpmsStatus)
		{
			case 0:
				return VehiclePropertyConstants.SECURITY_ALERT_FL_TIRE_PRESSURE_NORMAL;
			case 1:
				return VehiclePropertyConstants.SECURITY_ALERT_FL_TIRE_PRESSURE_LOW;
			case 2:
				return VehiclePropertyConstants.SECURITY_ALERT_FL_TIRE_PRESSURE_HIGH;
			default:
				return VehiclePropertyConstants.SECURITY_ALERT_FL_TIRE_PRESSURE_NORMAL;
		}
	}
	
	//
	private int tramslateRadarDistance(int originDistance,int maxLevel,int lastCacheValue)
	{
		int ret = 0;
		if(originDistance >= 0x00 && originDistance <= 0x0F )
		{
			ret = 1;
		}
		else if(originDistance >= 0x10 && originDistance <= 0x1E )
		{
			ret = 2;
		}
		else if(originDistance >= 0x1F && originDistance <= 0x2D )
		{
			ret = 3;
		}
		else if(originDistance >= 0x2E && originDistance <= 0x3C )
		{
			ret = 4;
		}
		else if(originDistance >= 0x3D && originDistance <= 0x4B )
		{
			ret = 5;
		}
		else if(originDistance >= 0x4C && originDistance <= 0x5A )
		{
			ret = 6;
		}
		else if(originDistance >= 0x5B && originDistance <= 0x69 )
		{
			ret = 7;
		}
		else if(originDistance >= 0x6A && originDistance <= 0x78 )
		{
			ret = 8;
		}
		else if(originDistance >= 0x79 && originDistance <= 0x87 )
		{
			ret = 9;
		}
		else if(originDistance >= 0x88 && originDistance <= 0x96 )
		{
			ret = 10;
		}
		else if(originDistance >= 0x97 && originDistance <= 0xA5 )
		{
			ret = 11;
		}
		else if(originDistance == 0xFF)
		{
			//离开有效范围后的无效数据
			if(maxLevel == lastCacheValue || lastCacheValue == 0)
			{
				ret = 0;
			}
			//无效不处理，使用上一次值
			else
			{
				ret = lastCacheValue;
			}
		}
		ret = Math.min(maxLevel, Math.max(0, ret));
		return ret;
	}
	
	//请求原车初始信息
	private void reqVehicleInitInfo(final int reqCode)
	{
		Log.i(TAG, "reqVehicleInitInfo");
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				try
				{					
					//泊车辅助状态
					if(reqCode == 0 || reqCode == RZC_Volkswagen_Golf7_Protocol.CH_CMD_PARKING_STATUS_INFO
							&& mService.getCacheValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY) == null)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_PARKING_STATUS_INFO,0xFF}));
					}
					
					//行车数据
					if(reqCode == 0 || reqCode == RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA)
					{
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_UNIT_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x10}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_DISTANCE_SINCE_START_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x20}));
						}
						Thread.sleep(200);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_DISTANCE_SINCE_REFUELING_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x21}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_DISTANCE_LONG_TERM_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x22}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_AVG_CONSUMPTION_SINCE_START_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x30}));
						}
						Thread.sleep(200);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_AVG_CONSUMPTION_SINCE_REFUELING_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x31}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_AVG_CONSUMPTION_LONG_TERM_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x32}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_AVG_SPEED_SINCE_START_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x40}));
						}
						Thread.sleep(200);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_AVG_SPEED_SINCE_REFUELING_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x41}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_AVG_SPEED_LONG_TERM_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x42}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_TRAVELLING_TIME_SINCE_START_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x50}));
						}
						Thread.sleep(200);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_TRAVELLING_TIME_SINCE_REFUELING_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x51}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_TRAVELLING_TIME_LONG_TERM_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x52}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_CONV_CONSUMERS_VALUE_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x60}));
						}
						Thread.sleep(200);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_DRIVING_DATA,0x61}));
						}
					}
					
					//车辆提示
					if(reqCode == 0 || reqCode == RZC_Volkswagen_Golf7_Protocol.CH_CMD_PROMPT_INFO
							&& mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_STATUS_PROMPT_MESSAGE_PROPERTY) == null)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_PROMPT_INFO,0xFF}));
					}
			
					//服务
					if(reqCode == 0 || reqCode == RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO)
					{
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_VIN_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO,0x00}));
						}
						Thread.sleep(100);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_CARE_INSPECTION_LEFT_DAYS_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO,0x10}));
						}
						Thread.sleep(100);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_CARE_INSPECTION_LEFT_ODOMETER_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO,0x11}));
						}
						Thread.sleep(100);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_CARE_REFUEL_OIL_DAYS_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO,0x20}));
						}
						Thread.sleep(100);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_CARE_REFUEL_OIL_ODOMETER_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_SERVICE_INFO,0x21}));
						}
					}
					
					//胎压信息
					if(reqCode == 0 || reqCode == RZC_Volkswagen_Golf7_Protocol.CH_CMD_TPMS_INFO
							&& mService.getCacheValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_ALERT_INFO_PROPERTY) == null)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_TPMS_INFO,0xFF}));
					}

					//舒适性用电器
					if(reqCode == 0 || reqCode == RZC_Volkswagen_Golf7_Protocol.CH_CMD_CONV_CONSUME
							&&  mService.getCacheValue(VehicleInterfaceProperties.VIM_CONV_CONSUMERS_PROMPT_MESSAGE_PROPERTY) == null)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_CONV_CONSUME,0xFF}));
					}
					
					//车身状态
					if(reqCode == 0 || reqCode == RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS)
					{
						//mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_HU_ENABLE,0xFF}));
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_ELECTRONIC_STABILITY_CONTROL_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x10}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_SPEED_WARNING_STATUS_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x20}));
						}
						Thread.sleep(300);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_ADAS_LANE_ASSIST_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x30}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_ADAS_LAST_DISTANCE_SELECTED) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x31}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_PARKING_AND_MANOEUVRING_AUTO_ACTIVE_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x40}));
						}
						Thread.sleep(300);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_LIGHT_AUTO_SWITCH_ON_TIME_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x50}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_TRAVEL_MODEL_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x51}));
						}
						Thread.sleep(300);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_ROOF_LIGHT_BRIGHTNESS_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x52}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_MW_SYNC_ADJUSTMENT_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x60}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_WINDOW_CONV_OPEN_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x70}));
						}
						Thread.sleep(300);
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_MFD_CURRENT_FUEL_CONSUMPTION_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x80}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_DISTANCE_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0x90}));
						}
						if(mService.getCacheValue(VehicleInterfaceProperties.VIM_VEHICLE_KEY_ACTIVED_PROPERTY) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0xA0}));
						}
						/*
						if(mService.getCacheValue(VehicleInterfaceProperties.) == null)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Volkswagen_Golf7_Protocol.HC_CMD_REQ_CONTROL_INFO, new int[]{RZC_Volkswagen_Golf7_Protocol.CH_CMD_VEHICLE_STAUS,0xB0}));
						}*/
					}
				} catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void clearACStatus()
	{
		int[] acMode = new int[5];
		acMode[0] = VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
		acMode[1] = VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
		acMode[2] = VehiclePropertyConstants.AC_COOL_MODE_AUTO2_OFF;
		acMode[3] = VehiclePropertyConstants.AC_COOL_MODE_SYNC_OFF;
		acMode[4] = VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
		mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);
		
		//循环模式
		int[] cycleMode = new int[1];
		cycleMode[0] = VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
		mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);
		
		//后座空调锁定灯状态
		mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_REAR_FLAG_PROPERTY, "0", false);
		
		//风向
		int[] windDirInfo = new int[3];
		windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
		windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
		windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
		mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
		//风量大小
		mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[]{0}), false);
		
		//空调温度信息
		int tempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_C;
		mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{tempUnit,tempUnit,tempUnit}), false);

		
		
		float []interiorTemp = new float[]{VehiclePropertyConstants.AC_TEMP_LO,VehiclePropertyConstants.AC_TEMP_LO,VehiclePropertyConstants.AC_TEMP_LO};
		mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
		
		//前后窗除雾
		mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "0", false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, "0", false);
		
		//座椅加热信息
		mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY, "0", false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_PSNGR_SEAT_PROPERTY, "0", false);
		
		//自动模式吹风风速
		mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_AUTO_MODE_PROPERTY, "1", false);
		
	}
}
