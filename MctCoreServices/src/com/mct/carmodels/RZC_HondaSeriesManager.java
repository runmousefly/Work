package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.carmodels.RZC_NissanSeriesManager.SystemTimeSetReceiver;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-本田车系车型协议管理
public class RZC_HondaSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC-Honda";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_HONDA_12CRV;
	private boolean mShowVehicleDoorInfo = true;
	private int mPressedKeyValue = 0;
	private boolean mSupportSyncTime = true;
	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();

	private Runnable mMediaSyncInfoRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			if (mService != null && mMcuManager != null)
			{
				if (mMcuManager.getCurrentMediaMode() == VehiclePropertyConstants.MEDIA_MODE_TUNER)
				{
					int sourceType = VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_TUNER;
					int curTrack = -1;
					int totalTrack = -1;
					int currentPlayTime = -1;
					int totalPlayTime = -1;
					int playState = -1;
					String strID3 = null;
					int band = Integer.valueOf(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY));
					int freq = Integer.valueOf(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));;
					String mediaInfoParam = VehiclePropertyConstants.formatSourceInfoString(sourceType, curTrack, totalTrack, currentPlayTime, totalPlayTime, playState, strID3, band, freq);
					mCanMangaer.setPropValue(VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY, mediaInfoParam);
				}
			}
		}
	};
	
	public RZC_HondaSeriesManager(int carModel)
	{
		mCarModel = carModel;
	}

	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager,current carmodel:"+mCarModel);
		mService = service;
		mMcuManager = (HeadUnitMcuManager) service.getMcuManager();
		mCanMangaer = (CanManager) service.getVehicleManager();
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
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "4");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4}), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		mShowVehicleDoorInfo = false;
		stopSyncVehicleInfoTimer();
		if(mSystemTimeSetReceiver != null && mService != null)
		{
			mService.unregisterReceiver(mSystemTimeSetReceiver);
		}
		if(mMediaSyncInfoRunnable != null && mService != null)
		{
			mService.getMainHandler().removeCallbacks(mMediaSyncInfoRunnable);
		}
		mMcuManager = null;
		mCanMangaer = null;
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
		// 60S定时发送
		mSyncVehicleInfoTimer = new Timer();
		mSyncVehicleInfoTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
			}
		}, 10*1000, 60*1000);
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
		if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L
				|| mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_H
				|| mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
		{
			return ServiceHelper.combineArray(RZC_HondaSeriesProtocol.VEHICLE_CAN_PROPERITIES, 
					RZC_HondaSeriesProtocol.VEHICLE_CAN_PROPERITIES_CRV);
		}
		return RZC_HondaSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_HondaSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_HondaSeriesProtocol.getProperityPermission(RZC_HondaSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_HondaSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_HondaSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_HondaSeriesProtocol.getProperityDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		if (mMcuManager == null || mCanMangaer == null)
		{
			Log.e(TAG, "McuManager is not ready");
			return false;
		}
		try
		{
			switch (propId)
			{
				case VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY:
					int cmd = Integer.valueOf(value);
					switch (cmd)
					{
						case VehiclePropertyConstants.CANBOX_CMD_REQ_START:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_TRIP_ODOMETER_INFO, 0x01 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_TRIP_ODOMETER_INFO, 0x02 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_COMPASS_STATUS, 0xFF }));

							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_BASE_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_FRONT_RADAR_INFO,0xFF }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_REAR_RADAR_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_BASE_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_SETTING:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_VEHICLE_SET_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CAN_CMD_REQ_ODOMETER_INFO:
						case VehiclePropertyConstants.CAN_CMD_REQ_FUEL_CONSUM_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_TRIP_ODOMETER_INFO,0x01 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_TRIP_ODOMETER_INFO,0x02 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME:
							Time time = new Time();
							time.setToNow();
							String strTimeFormat = android.provider.Settings.System.getString(mService.getContentResolver(),
									android.provider.Settings.System.TIME_12_24);
							int timeType = 0;//0 24H; 1 12H
							if(strTimeFormat != null && strTimeFormat.equals("12"))
							{
								timeType = 1;
								if(time.hour == 0)
								{
									time.hour = 12;
								}
							}
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
									mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SYNC_VEHICLE_TIME, new int[] { 0x00,timeType,time.hour,time.minute}));	
							}
							else 
							{
								if(timeType == 1)
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
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x50,time.hour,time.minute}));	
							}
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_CLEAR_TRIP_RECORD:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_TRIP_ODOMETER_INFO, 0x03}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_COMPASS_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_HondaSeriesProtocol.CH_CMD_COMPASS_STATUS, 0xFF}));
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_OUTSIDE_TEMP_ADJUST_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x00,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_TRIP_A_RESET_TIMING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x02,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_TRIP_B_RESET_TIMING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x03,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_INTERNEL_LIGHT_AUTO_OFF_TIME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x04,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x05,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_LIGHT_SENSITIVITY_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x06,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_DOOR_LOCK_WITH_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x07,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_DOOR_UNLOCK_WITH_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x08,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_KEY_AND_REMOTE_UNLOCK_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x09,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_KEYLESS_LOCK_ANSWER_BACK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0A,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_RELOCK_TIMER_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0B,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_KEYLESS_ACCESS_BEEP_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0D,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ALARM_VOLUME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x12,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_FUEL_EFFICIENTY_BACKLIGHT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x13,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_NEW_MESSAGE_NOTIFICATION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x14,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_SPEED_DISTANCE_UNIT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x15,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_TACHOMETER_DISPLAY_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x16,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_WALK_AWAY_AUTO_LOCK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x17,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_REMOTE_START_SYSTEM_ON_OFF_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x18,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_DOOR_UNLOCK_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x19,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_LOCK_UNLOCK_FLASH_ALERT_LIGHTS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1A,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_INTERIOR_ILL_SENSITIVITY_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1B,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_HEAD_LIGHT_ON_WITH_WIPER_ON_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1C,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ENGINE_AUTO_SYSTEM_ON_OFF_DISPLAY_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1D,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_SPEECH_ALARM_SYS_VOLUME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1E,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_FRONT_ALARM_DISTANCE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1F,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ACC_FRONT_DETECT_ALARM_TONE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x20,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_PAUSE_LKAS_TONE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x21,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_LANE_DEPARTURE_SETING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x22,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_TACHOMETER_SETTING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x23,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_COMPASS_CALIBRATE_STATUS_PROPERTY:
					if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
					mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_COMPLASS_HANDLE, new int[] { 0x01,0x01 }));
					}
					else 
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xC0,Integer.valueOf(value) }));
					}
					break;
				case VehicleInterfaceProperties.VIM_COMPASS_REGION_PROPERTY:
					if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
					mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_COMPLASS_HANDLE, new int[] { 0x02,Integer.valueOf(value) }));
					}
					else 
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xC1,Integer.valueOf(value) }));
					}
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x40,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_LANGUAGE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x55,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_DYNAMIC_SETTING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x60,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_PROPERTY:
				case VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xAD,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_DRVNG_RANGE_ICON_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xF0,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_AVG_SPEED__ICON_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xF1,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_SHOW_ICON_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xF2,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_TURBO_ICON_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xF3,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_ICON_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xF4,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_PHONE_ICON_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0xF5,Integer.valueOf(value) }));
					break;
				//媒体信息同步
				case VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> sourceInfoMap = VehicleManager.stringToHashMap(value);
					if(sourceInfoMap != null && sourceInfoMap.size() > 0)
					{
						int sourceType = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY));
						if(sourceType == -1)
						{
							Log.e(TAG, "setPropValue Exception,propId:"+propId+",value:"+value);
							break;
						}
						//更新收音信息
						if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_TUNER)
						{
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x01}));
							}
							int band = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY));
							if(band >= VehiclePropertyConstants.RADIO_BAND_FM1 && band <= VehiclePropertyConstants.RADIO_BAND_FM3)
							{
								band = 0x00;
							}
							else if(band >= VehiclePropertyConstants.RADIO_BAND_AM1 && band <= VehiclePropertyConstants.RADIO_BAND_AM2)
							{
								band = 0x10;
							}
							else 
							{
								Log.e(TAG, "setPropValue Exception,propId:"+propId+",value:"+value);
								break;
							}
							int freq = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_RADIO_INFO,new int[]{band,freq & 0xFF,(freq >> 8) & 0xFF,0}));
						}
						//更新媒体格式信息
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB)
						{
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x04}));
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x08,0x11}));
							int currentPlayTime = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY));

							int curTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY));
							int totalTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY));
							currentPlayTime = currentPlayTime < 0 ? 0: currentPlayTime;
							curTrack = curTrack < 0 ? 0: curTrack;
							totalTrack = totalTrack < 0 ? 0: totalTrack;
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_MEDIA_INFO,new int[]{curTrack&0xFF,(curTrack>>8)&0xFF,totalTrack&0xFF,(totalTrack>>8)&0xFF,currentPlayTime/60,currentPlayTime%60}));
							String strId3 = sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY);
							if(strId3 != null && strId3.length() > 0)
							{
								String[] id3Array = ServiceHelper.stringToStringArray(strId3);
								if (id3Array != null && id3Array.length == 3)
								{
									int []title = ServiceHelper.combineArray(new int[]{0x02}, ServiceHelper.stringToUnicodeBytes(id3Array[0], true));
									int []artist = ServiceHelper.combineArray(new int[]{0x04}, ServiceHelper.stringToUnicodeBytes(id3Array[1], true));
									int []album = ServiceHelper.combineArray(new int[]{0x03}, ServiceHelper.stringToUnicodeBytes(id3Array[2], true));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_PHONE_AND_MESSAGE, title));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_PHONE_AND_MESSAGE, artist));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_PHONE_AND_MESSAGE, album));
								}
							}
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							/*if(mCarModel == VehiclePropertyConstants.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x04}));
							}*/						
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x0C,0x30}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_MEDIA_INFO,new int[]{0,0,0,0,0,0}));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x03}));
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x07,0x30}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_MEDIA_INFO,new int[]{0,0,0,0,0,0}));
						//	mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_ICON,new int[]{0x00}));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{ 
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x00}));
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x00,0x30}));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE)
						{
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x05}));
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x05,0x40}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_MEDIA_INFO,new int[]{0,0,0,0,0,0}));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x04,0x30}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_MEDIA_INFO,new int[]{0,0,0,0,0,0}));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x06}));
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x0B,0x30}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_MEDIA_INFO,new int[]{0,0,0,0,0,0}));
							String strId3 = sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY);
							if(strId3 != null && strId3.length() > 0)
							{
								String[] id3Array = VehicleManager.stringToStringArray(strId3);
								if (id3Array != null && id3Array.length == 3)
								{
									int []title = ServiceHelper.combineArray(new int[]{0x02}, ServiceHelper.stringToUnicodeBytes(id3Array[0], true));
									int []artist = ServiceHelper.combineArray(new int[]{0x04}, ServiceHelper.stringToUnicodeBytes(id3Array[1], true));
									int []album = ServiceHelper.combineArray(new int[]{0x03}, ServiceHelper.stringToUnicodeBytes(id3Array[2], true));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_PHONE_AND_MESSAGE, title));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_PHONE_AND_MESSAGE, artist));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_PHONE_AND_MESSAGE, album));
								}
							}
						}
					}
					break;
				//电话信息同步
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
						if(callStatus == CarService.CALL_STATE_INCOMING ||
								callStatus == CarService.CALL_STATE_DIAL ||
								callStatus == CarService.CALL_STATE_DIALING ||
								callStatus == CarService.CALL_STATE_COMMUNICATING ||
								callStatus == CarService.CALL_STATE_WAITING ||
								callStatus == CarService.CALL_STATE_HOLD ||
								callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
						{
							if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_CUR_SOURCE,new int[]{0x05}));
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_SOURCE,new int[]{0x05,0x40}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_MEDIA_INFO,new int[]{0,0,0,0,0,0}));
							//电话号码
							String strNumber = btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_NUMBER_PROPERTY);
							if(strNumber != null && strNumber.length() > 0)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_PHONE_AND_MESSAGE,ServiceHelper.combineArray(new int[]{0x01}, ServiceHelper.byteArrayToIntArray(strNumber.getBytes()))));
							}
						}
					}
					break;
				case VehicleInterfaceProperties.VIM_AUDIO_CONTROL_VOLUME_PROPERTY:
					int controlVolume = Integer.valueOf(value);
					if (controlVolume == 0)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VOLUME, new int[] { 0x80 }));
					}
					else
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VOLUME, new int[] { controlVolume }));
					}
					if (mService != null && mMcuManager.getCurrentMediaMode() == VehiclePropertyConstants.MEDIA_MODE_TUNER)
					{
						mService.getMainHandler().removeCallbacks(mMediaSyncInfoRunnable);
						mService.getMainHandler().postDelayed(mMediaSyncInfoRunnable, 1000);
					}
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_PLAY_CMD_PROPERTY:
					if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
					mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_HondaSeriesProtocol.HC_CMD_VEHICLE_MEDIA, new int[] { Integer.valueOf(value),0xFF }));
					}
					break;
				default:
					break;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
			return false;
		}
		return true;
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
		if (buffer == null || buffer.length == 0) { return; }
		int[] param = new int[buffer.length - 1];
		System.arraycopy(buffer, 1, param, 0, param.length);
		onReceiveData(buffer[0], param);
	}

	private void onReceiveData(int cmd, int[] param)
	{
		if (mMcuManager == null)
		{
			Log.e(TAG, "Mcu Manager is not ready");
			return;
		}
		mMcuManager.returnCanAck(cmd);
		switch (cmd)
		{
			case RZC_HondaSeriesProtocol.CH_CMD_TUNER_CUR_FREQ:

				break;
			case RZC_HondaSeriesProtocol.CH_CMD_TUNER_PRESET_INFO:

				break;

			case RZC_HondaSeriesProtocol.CH_CMD_TUNER_STORE_LIST:

				break;
			case RZC_HondaSeriesProtocol.CH_CMD_BACKLIGHT_INFO:
				if(param != null && param.length == 1)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BRIGHTNESS_LVL_PROPERTY, String.valueOf(param[0]), false);
				}
				break;
			case RZC_HondaSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_HondaSeriesProtocol.SWC_KEY_NONE)
					{
						// mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,
						// String.valueOf(RZC_HondaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue)),
						// true);
						mService.broadcastKeyEvent(RZC_HondaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_HondaSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] == 1)
					{
						mPressedKeyValue = param[0];
						if (mPressedKeyValue == RZC_HondaSeriesProtocol.SWC_KEY_MENU && (mCarModel == CarModelDefine.CAR_MODEL_HONDA_CRIDER || mCarModel == CarModelDefine.CAR_MODEL_HONDA_CITY))
						{
							mPressedKeyValue = RZC_HondaSeriesProtocol.SWC_KEY_MUTE;
						}
						//只有歌诗图支持Camera转右视
//						else if(mPressedKeyValue == RZC_HondaSeriesProtocol.SWC_KEY_CAMERA
//								&& mCarModel == VehiclePropertyConstants.CAR_MODEL_HONDA_CROSSTOUR)
//						{
//							mPressedKeyValue = RZC_HondaSeriesProtocol.SWC_KEY_TURN_RIGHT_CAMERA2;
//						}
						mService.broadcastKeyEvent(RZC_HondaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			//15CRV_L与12CRV是原车多媒体播放状态，其它为空调信息
			case RZC_HondaSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if(param != null && param.length == 0x0B &&
						(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
				mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV))
				{
					Log.i(TAG, "receive vehicle media info");
					int deviceType = ServiceHelper.getBits(param[0], 4, 2);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_DEVICE_TYPE_PROPERTY, String.valueOf(deviceType), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_DEVICE_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 0, 4)), false);
					if(deviceType == VehiclePropertyConstants.VEHICLE_MEDIA_DEVICE_TYPE_NO)
					{
						break;
					}
					long playTime = param[2]*60+param[3];
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_CUR_PLAY_TIME_PROPERTY, String.valueOf(playTime), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_CUR_PLAY_TRACK_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[5], param[4])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_TOTAL_TRACK_PROPERTY, String.valueOf(ServiceHelper.MAKEWORD(param[7], param[6])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_FOLDER_NUM_PROPERTY, String.valueOf(param[8]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MEDIA_PLAY_PROGRESS_PROPERTY, String.valueOf(param[9]), false);
				}
				else if (param != null && param.length >= 6)
				{
					Log.i(TAG, "receive air condition info");
					boolean bRearACSupport = false;
					/*if (param.length == 9)
					{
						Log.i(TAG, "support rear ac info");
						bRearACSupport = true;
					}*/
					// 空调开关
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), true);
					if (acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						//mService.broadcastAirConditionInfo(0);
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					// 请求显示空调信息
					int showAcBar = ServiceHelper.getBit(param[1], 4);
					if (showAcBar == 0)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						break;
					}
					// 制冷模式
					int[] acMode = new int[5];
					int[] cycleMode = new int[1];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 4) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_SYNC_ON : VehiclePropertyConstants.AC_COOL_MODE_SYNC_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[3] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					acMode[4] = ServiceHelper.getBit(param[0], 0) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_REAR_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_REAR_AUTO_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 循环模式
					cycleMode[0] = ServiceHelper.getBit(param[0], 5) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);

					// 风向
					int[] windDirInfo = null;
					if (bRearACSupport)
					{
						windDirInfo = new int[6];
					}
					else
					{
						windDirInfo = new int[3];
					}
					windDirInfo[0] = ServiceHelper.getBit(param[1], 7) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[1], 5) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					if (bRearACSupport)
					{
						windDirInfo[3] = ServiceHelper.getBit(param[7], 7) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_REAR_UP_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_REAR_UP_OFF;
						windDirInfo[4] = ServiceHelper.getBit(param[7], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_REAR_CENTER_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_REAR_CENTER_OFF;
						windDirInfo[5] = ServiceHelper.getBit(param[7], 5) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_REAR_DOWN_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_REAR_DOWN_OFF;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);

					// 风量大小
					if (bRearACSupport)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, VehicleManager.intArrayToString(new int[] { ServiceHelper.getBits(param[1], 0, 4), -1, ServiceHelper.getBits(param[7], 0, 4) }), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, VehicleManager.intArrayToString(new int[] { ServiceHelper.getBits(param[1], 0, 4) }), false);
					}

					// 空调温度信息
					// 左右温度
					int tempUnit = ServiceHelper.getBit(param[4], 0) == 0 ? VehiclePropertyConstants.AC_TEMP_UNIT_C : VehiclePropertyConstants.AC_TEMP_UNIT_F;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{tempUnit,tempUnit}), false);
					float[] interiorTemp = null;
					if (bRearACSupport)
					{
						interiorTemp = new float[3];
					}
					else
					{
						interiorTemp = new float[2];
					}
					if (tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_C)
					{
						// 左温度
						if (param[2] == 0x00)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if (param[2] == 0xFF)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else
						{
							interiorTemp[0] = (float) Math.max(RZC_HondaSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_HondaSeriesProtocol.AC_HIGHEST_TEMP, RZC_HondaSeriesProtocol.AC_LOWEST_TEMP + param[2] * 0.5));
						}
						// 右温度
						if (param[3] == 0x00)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if (param[3] == 0xFF)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else
						{
							interiorTemp[1] = (float) Math.max(RZC_HondaSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_HondaSeriesProtocol.AC_HIGHEST_TEMP, RZC_HondaSeriesProtocol.AC_LOWEST_TEMP + param[3] * 0.5));
						}
						// 后座温度
						if (bRearACSupport)
						{
							if (param[6] == 0x00)
							{
								interiorTemp[2] = VehiclePropertyConstants.AC_TEMP_LO;
							}
							else if (param[6] == 0xFF)
							{
								interiorTemp[2] = VehiclePropertyConstants.AC_TEMP_HI;
							}
							else
							{
								interiorTemp[2] = (float) Math.max(RZC_HondaSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_HondaSeriesProtocol.AC_HIGHEST_TEMP, RZC_HondaSeriesProtocol.AC_LOWEST_TEMP + param[6] * 0.5));
							}
						}
					}
					else
					{
						// 左温度
						if (param[2] == 0x00)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if (param[2] == 0xFF)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else
						{
							interiorTemp[0] = param[2];
						}
						// 右温度
						if (param[3] == 0x00)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if (param[3] == 0xFF)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else
						{
							interiorTemp[1] = param[3];
						}
						// 后座温度
						if (bRearACSupport)
						{
							if (param[6] == 0x00)
							{
								interiorTemp[2] = VehiclePropertyConstants.AC_TEMP_LO;
							}
							else if (param[6] == 0xFF)
							{
								interiorTemp[2] = VehiclePropertyConstants.AC_TEMP_HI;
							}
							else
							{
								interiorTemp[2] = param[6];
							}
						}
					}
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_REAR_FLAG_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 2)),false);
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
					// 前窗除雾
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 7)), false);
					if (showAcBar == 1)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						mCanMangaer.showAirConditionEvent(true);
					}
				}
				break;
			case RZC_HondaSeriesProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive base info");
					int flStatus = ServiceHelper.getBit(param[0], 6);
					int frStatus = ServiceHelper.getBit(param[0], 7);
					int rlStatus = ServiceHelper.getBit(param[0], 4);
					int rrStatus = ServiceHelper.getBit(param[0], 5);
					int trunkStatus = ServiceHelper.getBit(param[0], 3);
					int hoodStatus = 0;
					if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
							mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
					{
						hoodStatus = ServiceHelper.getBit(param[1], 7);
					}
					else
					{
						hoodStatus = ServiceHelper.getBit(param[0], 2);
					}
					
					
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

						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY, String.valueOf(flStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY, String.valueOf(frStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY, String.valueOf(rlStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY, String.valueOf(rrStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY, String.valueOf(trunkStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY, String.valueOf(hoodStatus), false);

					}
					
					if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
							mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
					{
						break;
					}
					int reverseState = ServiceHelper.getBit(param[1], 0);
					// 1 非P档(手刹状态)；0 P档 == >与其它车型协议定义是反的
					int brakeState = (ServiceHelper.getBit(param[1], 1) == 0 ? 1 : 0);
					int lightState = ServiceHelper.getBit(param[1], 2);
					Log.i(TAG, "receive base info(unused),reverse:"+reverseState+",brakeState:"+brakeState+",lightState:"+lightState);
					/*mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY, String.valueOf(ReverseState), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY, String.valueOf(brakeState), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, String.valueOf(lightState), false);
					*/
					// 仅歌诗图车型支持
					// 转向灯
					if (mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR)
					{
						int turnLight = ServiceHelper.getBits(param[1], 6, 2);
						if (turnLight == 0)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_LEFT_TURN_LIGHT_PROPERTY, "0", true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RIGHT_TURN_LIGHT_PROPERTY, "0", true);
						}
						// 右转灯
						else if (turnLight == 1)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_LEFT_TURN_LIGHT_PROPERTY, "0", true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RIGHT_TURN_LIGHT_PROPERTY, "1", true);
						}
						// 左转灯
						else if (turnLight == 2)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_LEFT_TURN_LIGHT_PROPERTY, "1", true);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RIGHT_TURN_LIGHT_PROPERTY, "0", true);
						}
					}
				}
				break;
			// 油耗及里程信息(500ms定时发送)
			case RZC_HondaSeriesProtocol.CH_CMD_TRIP_ODOMETER_INFO:
				if (param != null && param.length > 0)
				{
					if (param[0] == 1 && param.length == 15)
					{
						Log.i(TAG, "receive trip and odometer info");
						
						// 油耗量程
						mService.cachePropValue(VehicleInterfaceProperties.VIM_FUEL_CONSUMPTION_RANGE_PROPERTY, String.valueOf(param[14]), false);

						// 瞬时油耗
						int instantFuelConsumUnit = ServiceHelper.getBits(param[13], 0, 2);
						if (instantFuelConsumUnit == 0)
						{
							instantFuelConsumUnit = 2;
						}
						else if (instantFuelConsumUnit == 2)
						{
							instantFuelConsumUnit = 0;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(instantFuelConsumUnit), false);
						
						mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY, String.valueOf(param[1]), false);

						// 当前平均油耗
						int curAvgFuelConsumUnit = ServiceHelper.getBits(param[13], 2, 2);
						if (curAvgFuelConsumUnit == 0)
						{
							curAvgFuelConsumUnit = 2;
						}
						else if (curAvgFuelConsumUnit == 2)
						{
							curAvgFuelConsumUnit = 0;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CURRENT_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(curAvgFuelConsumUnit), false);
						if(param[2] != 0xFF && param[3] != 0xFF)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_CURRENT_AVG_FUEL_CONSUMPTION_PROPERTY, String.format("%.1f", (param[2] * 256 + param[3]) * 0.1f), false);
						}
						else
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_CURRENT_AVG_FUEL_CONSUMPTION_PROPERTY, "-1", false);
						}

						// 历史平均油耗
						int historyAvgFuelConsumUnit = ServiceHelper.getBits(param[13], 2, 2);
						if (historyAvgFuelConsumUnit == 0)
						{
							historyAvgFuelConsumUnit = 2;
						}
						else if (historyAvgFuelConsumUnit == 2)
						{
							historyAvgFuelConsumUnit = 0;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HISTORY_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(historyAvgFuelConsumUnit), false);
						if(param[4] != 0xFF && param[5] != 0xFF)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HISTORY_AVG_FUEL_CONSUMPTION_PROPERTY, String.format("%.1f", (param[4] * 256 + param[5]) * 0.1f), false);
						}
						else
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HISTORY_AVG_FUEL_CONSUMPTION_PROPERTY, "-1", false);
						}

						// 平均油耗
						int avgFuelConsumUnit = ServiceHelper.getBits(param[13], 4, 2);
						if (avgFuelConsumUnit == 0)
						{
							avgFuelConsumUnit = 2;
						}
						else if (avgFuelConsumUnit == 2)
						{
							avgFuelConsumUnit = 0;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(avgFuelConsumUnit), false);
						if(param[6] != 0xFF && param[7] != 0xFF)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_FUEL_CONSUMPTION_PROPERTY, String.format("%.1f", (param[6] * 256 + param[7]) * 0.1f), false);
						}
						else
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_FUEL_CONSUMPTION_PROPERTY, "-1", false);
						}

						// TRIPA
						int odometerUnint = ServiceHelper.getBit(param[13], 6);
						// km
						if (odometerUnint == 0)
						{
							odometerUnint = VehiclePropertyConstants.ODOMETER_UNIT_KM;
						}
						else if (odometerUnint == 1)
						{
							odometerUnint = VehiclePropertyConstants.ODOMETER_UNIT_MILE;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_A_ODOMETER_UNIT_PROPERTY, String.valueOf(odometerUnint), false);
						if(param[8] != 0xFF && param[9] != 0xFF && param[10] != 0xFF)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY, String.format("%.1f", (param[8] * 256 * 256 + param[9] * 256 + param[10]) * 0.1f), false);
						}
						else
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY, "-1", false);
						}

						// 续航里程
						int rmngOdometerUnint = ServiceHelper.getBit(param[13], 7);
						// km
						if (rmngOdometerUnint == 0)
						{
							rmngOdometerUnint = VehiclePropertyConstants.ODOMETER_UNIT_KM;
						}
						else if (rmngOdometerUnint == 1)
						{
							rmngOdometerUnint = VehiclePropertyConstants.ODOMETER_UNIT_MILE;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_UNIT_PROPERTY, String.valueOf(rmngOdometerUnint), false);
						if(param[11] != 0xFF && param[12] != 0xFF)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY, String.valueOf(param[11] * 256 + param[12]), false);
						}
						else
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY, "-1", false);
						}
					}
					else if (param[0] == 2 && param.length == 18)
					{
						Log.i(TAG, "Receive tripa record info");
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CACHE_RECORD_FUEL_CONSUMPTION_RANGE_PROPERTY, String.valueOf(param[17]), false);
						// 平均油耗
						int avgFuelConsumUnit = ServiceHelper.getBits(param[16], 4, 2);
						if (avgFuelConsumUnit == 0)
						{
							avgFuelConsumUnit = 2;
						}
						else if (avgFuelConsumUnit == 2)
						{
							avgFuelConsumUnit = 0;
						}
						// TRIPA
						int odometerUnint = ServiceHelper.getBit(param[16], 6);
						// km
						if (odometerUnint == 0)
						{
							odometerUnint = VehiclePropertyConstants.ODOMETER_UNIT_KM;
						}

						else if (odometerUnint == 1)
						{
							odometerUnint = VehiclePropertyConstants.ODOMETER_UNIT_MILE;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CACHE_RECORD_TRIPA_ODOMETER_UNIT_PROPERTY, String.valueOf(odometerUnint), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CACHE_RECORD_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(avgFuelConsumUnit), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_RECORD_IN_CACHE_PROPERTY, ServiceHelper.floatArrayToString(new float[]{
						(param[1] == 0xFF && param[2] == 0xFF && param[3] == 0xFF) ? -1 : (param[1]*256*256+param[2]*256+param[3])*0.1f,
						(param[4] == 0xFF && param[5] == 0xFF) ? -1 : (param[4]*256+param[5])*0.1f,
						(param[6] == 0xFF && param[7] == 0xFF && param[8] == 0xFF) ? -1 : (param[6]*256*256+param[7]*256+param[8])*0.1f,
						(param[9] == 0xFF && param[10] == 0xFF) ? -1 : (param[9]*256+param[10])*0.1f,
						(param[11] == 0xFF && param[12] == 0xFF && param[13] == 0xFF) ? -1 : (param[11]*256*256+param[12]*256+param[13])*0.1f,
						(param[14] == 0xFF && param[15] == 0xFF) ? -1 : (param[14]*256+param[15])*0.1f,}), false);
					}
				}
				break;
			case RZC_HondaSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}

				break;
			case RZC_HondaSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
			//泊车辅助状态
			case RZC_HondaSeriesProtocol.CH_CMD_PARKING_STATUS_INFO:
				if(param != null && param.length == 2)
				{
					//前雷达工作状态
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 2)), false);
					//后雷达工作状态
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 3)), false);
					
					int opsStatus = ServiceHelper.getBit(param[0], 1);
					int radarTone = ServiceHelper.getBit(param[0], 0);
					//倒车辅助状态
					String strCurParkAssistStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY);
					if(strCurParkAssistStatus == null || Integer.valueOf(strCurParkAssistStatus) != opsStatus)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY, String.valueOf(opsStatus), false);
						mService.broadcastRadarPreview(opsStatus);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY, String.valueOf(radarTone), false);
				}
				break;
			// 方向盘角度
			case RZC_HondaSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length == 2)
				{
					float angle = 0;
					// 向左
					if (param[1] >= 128)
					{
						angle = -(256 * (255 - param[1]) + (256 - param[0]));
					}
					// 向右
					else
					{
						angle = 256 * param[1] + param[0];
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			// 协议版本号
			case RZC_HondaSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if (param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					Log.i(TAG, "receive protocol version info:" + version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
				}
				break;
			// 右视摄像头
			case RZC_HondaSeriesProtocol.CH_CMD_VEHICLE_CAMERA:
				if(mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_L ||
				mCarModel == CarModelDefine.CAR_MODEL_HONDA_12CRV)
				{
					Log.i(TAG, "receive time setting action");
					break;
				}
				else if (param != null && param.length == 2)
				{
					// 右转向灯
					int turnRightSwitch = ServiceHelper.getBit(param[1], 7);
					String strHistoryTurnRightSwitch = mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY, String.valueOf(turnRightSwitch), true);
					
					// 仅歌诗图车型支持右视
					if (mCarModel == CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR
						|| mCarModel == CarModelDefine.CAR_MODEL_HONDA_15CRV_H 
						|| mCarModel == CarModelDefine.CAR_MODEL_HONDA_CRIDER)
					{
						//mService.cachePropValue(VehicleInterfaceProperties.VIM_RIGHT_TURN_CAMERA_PROPERTY, String.valueOf(turnRightSwitch), true);
						// 右视按键状态
						int turnRightCameraBtnStatus = ServiceHelper.getBit(param[1], 6);
						if (turnRightCameraBtnStatus == 1)
						{
							mPressedKeyValue = RZC_HondaSeriesProtocol.SWC_KEY_TURN_RIGHT_CAMERA2;
							mService.broadcastKeyEvent(RZC_HondaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
						}
						else if (turnRightCameraBtnStatus == 0 && mPressedKeyValue == RZC_HondaSeriesProtocol.SWC_KEY_TURN_RIGHT_CAMERA2)
						{
							mService.broadcastKeyEvent(RZC_HondaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
							mPressedKeyValue = RZC_HondaSeriesProtocol.SWC_KEY_NONE;
						}
						else 
						{
							if(strHistoryTurnRightSwitch == null || Integer.valueOf(strHistoryTurnRightSwitch) != turnRightSwitch)
							{
								mService.broadcastKeyEvent(VehiclePropertyConstants.USER_KEY_TURN_RIGHT_CAMERA, KeyEvent.ACTION_DOWN, 0);
								mService.broadcastKeyEvent(VehiclePropertyConstants.USER_KEY_TURN_RIGHT_CAMERA, KeyEvent.ACTION_UP, 0);
							}
						}
					}

				}
				break;
			case RZC_HondaSeriesProtocol.CH_CMD_VEHICLE_SET_INFO:
				// 长度可变
				if (param != null)
				{
					Log.i(TAG, "receive vehicle setting info");
					if (param.length >= 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_B_RESET_TIMING_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 6, 2)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_A_RESET_TIMING_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 4, 2)), false);
						// 外部气温调节
						mService.cachePropValue(VehicleInterfaceProperties.VIM_OUTSIDE_TEMP_ADJUST_PROPERTY,String.valueOf(ServiceHelper.getBits(param[0], 0,4)), false);
					}
					if (param.length >= 2)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LIGHT_SENSITIVITY_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 4, 3)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 2, 2)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_INTERNEL_LIGHT_AUTO_OFF_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 0, 2)), false);
					}
					if (param.length >= 3)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_KEYLESS_LOCK_ANSWER_BACK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 7)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_KEY_AND_REMOTE_UNLOCK_MODE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 6)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_RELOCK_TIMER_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 4, 2)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_DOOR_UNLOCK_WITH_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 2, 2)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_DOOR_LOCK_WITH_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0, 2)), false);
					}
					if (param.length >= 4)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_KEYLESS_ACCESS_BEEP_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 6)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_START_SYSTEM_ON_OFF_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 5)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_UNLOCK_MODE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 4)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_LOCK_UNLOCK_FLASH_ALERT_LIGHTS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 3)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_INTERIOR_ILL_SENSITIVITY_PROPERTY, String.valueOf(ServiceHelper.getBits(param[3], 0, 3)), false);
					}
					if (param.length >= 5)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_ALARM_VOLUME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 6, 2)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_FUEL_EFFICIENTY_BACKLIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 5)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_NEW_MESSAGE_NOTIFICATION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 4)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEED_DISTANCE_UNIT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 3)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_TACHOMETER_DISPLAY_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 2)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_WALK_AWAY_AUTO_LOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 1)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_HEAD_LIGHT_ON_WITH_WIPER_ON_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 0)), false);
					}
					if (param.length >= 6)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEECH_ALARM_SYS_VOLUME_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 7)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGINE_AUTO_SYSTEM_ON_OFF_DISPLAY_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 6)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_ACC_FRONT_DETECT_ALARM_TONE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 5)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_PAUSE_LKAS_TONE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 4)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_FRONT_ALARM_DISTANCE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[5], 2, 2)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_LANE_DEPARTURE_SETING_PROPERTY, String.valueOf(ServiceHelper.getBits(param[5], 0, 2)), false);

					}
					if (param.length >= 7)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_TACHOMETER_SETTING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[6], 7)), false);
					}
				}
				break;
			//指南针状态
			case RZC_HondaSeriesProtocol.CH_CMD_COMPASS_STATUS:
				if(param != null && param.length == 2)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_COMPASS_CALIBRATE_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_COMPASS_REGION_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 0,4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_COMPASS_ANGLE_PROPERTY, String.valueOf(param[1]*3.0f/2.0f), false);
				}
				break;
			default:
				break;
		}
	}
}
