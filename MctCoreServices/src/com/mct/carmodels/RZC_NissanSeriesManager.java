package com.mct.carmodels;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.VehicleInterfaceDataHandler;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.HeadUnitMcuProtocol;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.R.string;
import android.bluetooth.BluetoothHandsfreeClient;
import android.bluetooth.BluetoothHandsfreeClientCall;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-日产车系车型协议管理,包括启辰T70
public class RZC_NissanSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC-Nissan";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_NISSAN_X_TRAIL;
	private int mPressedKeyValue = 0;
	private int[] mAirConditionParam = null;
	private boolean mSupportSyncTime = true;

	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();

	public RZC_NissanSeriesManager(int carModel)
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
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MIN, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MAX, "40");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MIN, "-5");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MAX, "5");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MIN, "-5");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MAX, "5");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SURROUND_VOLUME_MIN, "-5");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SURROUND_VOLUME_MAX, "5");				
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_MIN_PROPERTY, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_MAX_PROPERTY, "5");				
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		if(mSystemTimeSetReceiver != null)
		{
			mService.unregisterReceiver(mSystemTimeSetReceiver);
		}
		stopSyncVehicleInfoTimer();
		mMcuManager = null;
		mCanMangaer = null;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		if(mCarModel == CarModelDefine.CAR_MODEL_NISSAN_X_TRAIL)
		{
			return ServiceHelper.combineArray(RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES, 
					RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES_XTRAIL);
		}
		if(mCarModel == CarModelDefine.CAR_MODEL_NISSAN_CIMA_L)
		{
			return ServiceHelper.combineArray(RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES, 
					RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES_CIMA_L);
		}
		else if(mCarModel == CarModelDefine.CAR_MODEL_NISSAN_CIMA_H)
		{
			return ServiceHelper.combineArray(RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES, 
					RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES_CIMA_H);
		}
		else if(mCarModel == CarModelDefine.CAR_MODEL_NISSAN_VENUCIA_T70)
		{
			return ServiceHelper.combineArray(RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES, 
					new int[]{VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY});
		}
		return RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_NissanSeriesProtocol.getProperityPermission(RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_NissanSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_NissanSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_NissanSeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME:
							int hourMode = 0x01;
							Time time = new Time();
							time.setToNow();
							String strTimeFormat = android.provider.Settings.System.getString(mService.getContentResolver(),
									android.provider.Settings.System.TIME_12_24);
							if(strTimeFormat != null && strTimeFormat.equals("12"))
							{
								hourMode = 0x00;
								if(time.hour > 12)
								{
									time.hour = time.hour-12;
								}
								else if(time.hour == 0)
								{
									time.hour = 12;
								}
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_TIME_INFO, new int[] { time.minute, time.hour, 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_CAMERA_STATUS:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] {RZC_NissanSeriesProtocol.CH_CMD_CAMERA_STATUS}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_SETTING:
						case VehiclePropertyConstants.CANBOX_CMD_REQ_CAMERA_SETTING_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] {RZC_NissanSeriesProtocol.CH_CMD_CAMERA_SET_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_SWITCH_CAMERA:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SWITCH, new int[] {0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] {RZC_NissanSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AMPLIFIER_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] {RZC_NissanSeriesProtocol.CH_CMD_AMPLIFIER}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] {RZC_NissanSeriesProtocol.CH_CMD_REAR_RADAR_INFO}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] {RZC_NissanSeriesProtocol.CH_CMD_FRONT_RADAR_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] {RZC_NissanSeriesProtocol.CH_CMD_BASE_INFO}));
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY:
					int[] point = ServiceHelper.stringToIntArray(value);
					if(point != null && point.length == 3 && point[2] == 1)
					{
						if(point[1] > 300 && point[1] <= 600)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SWITCH, new int[] {0x01}));
						}
					}
					break;
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_SMART_ENTER_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[]{0x01,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_FIRST_FGEAR_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[] {0x02,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AVM_STARTUP_ANIMATE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[] {0x03,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_CONFIG_RESET_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[] {0x04,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_LEFT_TURN_LIGHT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[] {0x05,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_RIGHT_TURN_LIGHT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[] {0x06,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_STEER_WHELL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[] {0x07,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_FGEAR_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CAMERA_SET, new int[] {0x08,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AVM_CAMERA_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AVM_STATUS, new int[] { Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_DOWN)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x21,0x31 }));
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_UP)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x21, 0x21 }));
					}
					break;
				case VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_DOWN)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x22, 0x31 }));
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_UP)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x22, 0x21 }));
					}
					
					break;
				case VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_DOWN)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x23, 0x31 }));
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_UP)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x23, 0x21 }));
					}
					
					break;
				case VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_DOWN)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x24, 0x31 }));
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_UP)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x24, 0x21 }));
					}
					break;
				case VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_DOWN)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x25, 0x31 }));
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_UP)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x25, 0x21 }));
					}
					
					break;
				case VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_DOWN)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x26, 0x31 }));
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_UP)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x26, 0x21 }));
					}
					
					break;
				case VehicleInterfaceProperties.VIM_BOSE_CENTERPOINT_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x27, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_SURROUND_VOLUME_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_DOWN)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x28, 0x31 }));
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.AMPLIFIER_ADJUST_STEP_UP)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x28, 0x21 }));
					}
					break;
				case VehicleInterfaceProperties.VIM_DRIVER_SEAT_AUDIO_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x29, Integer.valueOf(value) }));
					break;
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] { sourceType, 0x01, band, freq & 0xFF, (freq >> 8) & 0xFF, 0, 0, 0 }));
						}
						//更新媒体格式信息
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB)
						{
							int currentPlayTime = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_PLAY_TIME_PROPERTY));
							int curTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY));
							int totalTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY));
							currentPlayTime = currentPlayTime < 0 ? 0: currentPlayTime;
							curTrack = curTrack < 0 ? 0: curTrack;
							totalTrack = totalTrack < 0 ? 0: totalTrack;
							int hour = currentPlayTime / 3600;
							int minute = (currentPlayTime - hour*3600) / 60;
							int second = currentPlayTime % 60;
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] { sourceType, 0x10,0, curTrack, totalTrack, hour,minute,second}));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] { sourceType, 0x30, 0, 0, 0, 0, 0, 0 }));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] { sourceType, 0x30, 0, 0, 0, 0, 0, 0 }));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] { sourceType, 0x30, 0, 0, 0, 0, 0, 0 }));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE)
						{
							//mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] { sourceType, 0x30, 0, 0, 0, 0, 0, 0 }));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] {sourceType, 0x30, 0, 0, 0, 0, 0, 0 }));
						}
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_SOURCE_INFO, new int[] { sourceType, 0x30, 0, 0, 0, 0, 0, 0 }));
						}
						//只有在未知媒体模式下才可显示自定义字符
						else if(sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_SELF_DEFINE)
						{
							String strID3 = sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY);
							if (strID3 != null && strID3.length() > 0)
							{
								String[] id3Array = VehicleManager.stringToStringArray(strID3);
								if (id3Array != null && id3Array.length == 3)
								{
									int []title = ServiceHelper.combineArray(new int[]{0x11}, ServiceHelper.stringToUnicodeBytes(id3Array[0], true));
									int []artist = ServiceHelper.combineArray(new int[]{0x11}, ServiceHelper.stringToUnicodeBytes(id3Array[1], true));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_INFO1, title));
									mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_MEDIA_INFO2, artist));
								}
							}
						}
					}
					break;
				// 电话状态
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
//						if (callStatus == -1)
//						{
//							Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
//							break;
//						}
						String callNumber = btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_NUMBER_PROPERTY);
						if(callStatus == CarService.CALL_STATE_INCOMING)
						{
							callStatus = 1;
						}
						else if(callStatus == CarService.CALL_STATE_DIAL ||
								callStatus == CarService.CALL_STATE_DIALING ||
								callStatus == CarService.CALL_STATE_COMMUNICATING ||
								callStatus == CarService.CALL_STATE_WAITING ||
								callStatus == CarService.CALL_STATE_HOLD ||
								callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
						{
							callStatus = 2;
						}
						else 
						{
							callStatus = 0;
							callNumber = "";
						}
						int[] btPhoneParam1 = new int[] { callStatus, 0x01 };
						int[] btPhoneParam2 = null;
						if (callNumber != null && callNumber.length() > 0)
						{
							btPhoneParam2 = ServiceHelper.byteArrayToIntArray(callNumber.getBytes());
						}
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_CALL_STATUS, ServiceHelper.combineArray(btPhoneParam1, btPhoneParam2)));
					}
					break;
				case VehicleInterfaceProperties.VIM_AVM_AUTO_PARK_TOUCH_CMD_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { Integer.valueOf(value), 0x01 }));
					break;
				case VehicleInterfaceProperties.VIM_LANGUAGE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x31, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_BLIND_SPOT_DETECTION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x53, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_LANE_DEPARTURE_DETECTION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x52, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_MOVING_OBJECT_DETECTION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x51, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_PROPERTY:
				case VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY:
					int[] fanSpeeds = ServiceHelper.stringToIntArray(value);
					if(fanSpeeds != null && fanSpeeds.length >= 1)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x12,fanSpeeds[0] }));
					}
					break;
				case VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY:
					float[] temp = ServiceHelper.stringToFloatArray(value);
					if(temp != null && temp.length >= 2)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x13,(int)temp[0] }));
						mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x14, (int)temp[1] }));
					}
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_DOWN_PROPERTY:
					final int subCmdDown = Integer.valueOf(value);
					switch (subCmdDown)
					{
						case VehiclePropertyConstants.AIR_CONDITION_CMD_POWER:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x00,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x01,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_INTERIOR_CYCLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x04,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_EXTERNAL_CYCLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x05,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_MODE_CONTROL:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x11,0x01}));
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_UP_PROPERTY:
					final int subCmdUp = Integer.valueOf(value);
					switch (subCmdUp)
					{
						case VehiclePropertyConstants.AIR_CONDITION_CMD_POWER:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x00,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x01,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_INTERIOR_CYCLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x04,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_EXTERNAL_CYCLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x05,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_MODE_CONTROL:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_NissanSeriesProtocol.HC_CMD_AIR_CONDITIONING_CONTROL, new int[] { 0x11,0x00}));
							break;
						default:
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
			case RZC_NissanSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					if (param != null && param.length == 2)
					{
						Log.i(TAG, "receive wheel key,Key:" + RZC_NissanSeriesProtocol.swcKeyToUserKey(param[0]) + ",status:" + param[1]);
						// 旋钮按键
						if (param[0] >= RZC_NissanSeriesProtocol.SWC_KEY2_SCROLL_LEFT && param[0] <= RZC_NissanSeriesProtocol.SWC_KEY2_SCROLL_RIGHT)
						{
							if (param[1] > 0)
							{
								for (int i = 0; i < param[1]; i++)
								{
									mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(RZC_NissanSeriesProtocol.swcKeyToUserKey(param[0])), true);
								}
							}
							mPressedKeyValue = RZC_NissanSeriesProtocol.SWC_KEY_NONE;
							return;
						}
						// 按键操作
						// 0 按键释放
						if (param[1] == 0 && mPressedKeyValue != RZC_NissanSeriesProtocol.SWC_KEY_NONE)
						{
							mService.broadcastKeyEvent(RZC_NissanSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
							mPressedKeyValue = RZC_NissanSeriesProtocol.SWC_KEY_NONE;
						}
						// 1 按键按下
						else if (param[1] >= 1)
						{
							if(param[0] == RZC_NissanSeriesProtocol.SWC_KEY_HANG_UP && mService != null &&
									mCarModel == CarModelDefine.CAR_MODEL_NISSAN_VENUCIA_T90)
							{
								mService.broadcastCameraPreview(2);
								mPressedKeyValue = RZC_NissanSeriesProtocol.SWC_KEY_NONE;
								break;
							}
							mPressedKeyValue = param[0];
							mService.broadcastKeyEvent(RZC_NissanSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
						}
					}
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 0x05)
				{
					Log.i(TAG, "receive air condition info");
					// 空调开关
					int fanSpeed = param[2];
					if (fanSpeed == 0)
					{
						Log.i(TAG, "AC is off");
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, "0", false);
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, "1", false);
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param))
					{
						Log.i(TAG, "filter no change air condition data");
						break;
					}
					mAirConditionParam = param;
					// 制冷模式
					int[] acMode = new int[3];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 7) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 循环模式
					int[] cycleMode = new int[1];
					cycleMode[0] = ServiceHelper.getBits(param[0], 4,2) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);
					
					// 风向
					int[] windDirInfo = new int[3];
					switch (param[1])
					{
						case 0:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
							break;
						case 1:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
							break;
						case 2:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							break;
						case 3:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							break;
						case 4:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							break;
						case 5:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
							break;
						default:
							break;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					
					// 风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, VehicleManager.intArrayToString(new int[]{fanSpeed}), false);
					
					if(fanSpeed == 7 && windDirInfo[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "1", false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "0", false);
					}
					//后座除霜
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					
					// 空调温度信息
					// 左右温度
					float[] interiorTemp = new float[2];
					int tempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_NO;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, String.valueOf(tempUnit), false);
					// 驾驶员位温度
					interiorTemp[0] = Math.min(15, Math.max(1, param[3]));
					interiorTemp[1] = Math.min(15, Math.max(1, param[4]));
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
					
					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			// 西玛车型才支持
			case RZC_NissanSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");

					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
			// 方向盘角度
			case RZC_NissanSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive steering-wheel info");
					int dir = ServiceHelper.getBit(param[0], 7);
					float angle = ServiceHelper.MAKEWORD(ServiceHelper.getBits(param[0], 0, 7), param[1]);
					// 向左
					if (dir == 0)
					{
						angle = -angle;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_SRC_SWITCH:
				if (param != null && param.length == 1)
				{
					Log.i(TAG, "receive source switch command");
					switch (param[0])
					{
						case RZC_NissanSeriesProtocol.SOURCE_AM:
							//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_RADIO_AM), true);
							//break;
						case RZC_NissanSeriesProtocol.SOURCE_FM1:
						case RZC_NissanSeriesProtocol.SOURCE_FM2:
						case RZC_NissanSeriesProtocol.SOURCE_FM:
						//	mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_RADIO_FM), true);
						//	break;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_RADIO), true);
							break;
						case RZC_NissanSeriesProtocol.SOURCE_CD:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_DVD), true);
							break;
						case RZC_NissanSeriesProtocol.SOURCE_USB:
						case RZC_NissanSeriesProtocol.SOURCE_USB1:
						case RZC_NissanSeriesProtocol.SOURCE_USB2:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_MUSIC), true);
							break;
						case RZC_NissanSeriesProtocol.SOURCE_BT_MUSIC:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_BT_MUSIC), true);
							break;
						case RZC_NissanSeriesProtocol.SOURCE_AUX:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_AUX1), true);
							break;
						default:
							break;
					}
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_CALL_OPERATION:
				if (param != null && param.length == 1)
				{
					Log.i(TAG, "receive call operate command");
					// 接听
					if (param[0] == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT), true);
					}
					// 拒接
					else if (param[0] == 2)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_PHONE_HANGUP), true);
					}
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_AMPLIFIER:
				if (param != null && param.length == 9)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[4])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[3])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[1])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[6])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_PROPERTY, String.valueOf(param[5]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BOSE_CENTERPOINT_STATUS_PROPERTY, String.valueOf(param[6]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SURROUND_VOLUME_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[7])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DRIVER_SEAT_AUDIO_STATUS_PROPERTY, String.valueOf(param[8]), false);
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_AVM:
				if (param != null && param.length == 4)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AVM_AUTO_PARK_UI_MODE_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AVM_VERTICAL_PARK_UI_COLOR_PROPERTY, String.valueOf(param[1]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AVM_AUTO_PARK_HINT_PROPERTY, String.valueOf(param[2]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AVM_CAMERA_UI_FALG_PROPERTY, String.valueOf(param[3]), false);
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_ADAS:
				if (param != null && param.length == 1)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_LANE_DEPARTURE_DETECTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_BLIND_SPOT_DETECTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_MOVING_OBJECT_DETECTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 5)), false);
				}
				break;
			case RZC_NissanSeriesProtocol.CH_CMD_CAMERA_SET_INFO:
				if (param != null && param.length >= 0x01)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_SMART_ENTER_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_FIRST_FGEAR_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AVM_STARTUP_ANIMATE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_LEFT_TURN_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_RIGHT_TURN_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_STEER_WHELL_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_STARTUP_BY_FGEAR_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
				}
				break;
			// 协议版本号，验证暂不支持
			case RZC_NissanSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if (param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					Log.i(TAG, "receive protocol version info:" + version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
				}
				break;
			default:
				break;
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
		// 1分钟定时发送
		mSyncVehicleInfoTimer = new Timer();
		mSyncVehicleInfoTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
			}
		}, 3000, 60 * 1000);
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
}
