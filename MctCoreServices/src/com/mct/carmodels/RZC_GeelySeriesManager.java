package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.media.MediaRouter.VolumeCallback;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-吉利车系车型协议管理
public class RZC_GeelySeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC-Geely";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_GEELY_EMGRAND;
	private int mPressedKeyValue = 0;
	private boolean mShowVehicleDoorInfo = true;
	private int[] mAirConditionParam = null;
	
	public RZC_GeelySeriesManager(int carModel)
	{
		mCarModel = carModel;
	}

	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager,current carmodel:"+mCarModel);
		mService = service;
		mShowVehicleDoorInfo = true;
		mMcuManager = (HeadUnitMcuManager) service.getMcuManager();
		mCanMangaer = (CanManager) service.getVehicleManager();
		initVehicleConfig();
		return true;
	}

	private void initVehicleConfig()
	{
		// 初始化雷达距离范围
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "10");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
	//	mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
	//				ServiceHelper.intArrayToString(new int[]{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10}), false);

		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF), false);
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		mShowVehicleDoorInfo = false;
		mMcuManager = null;
		mCanMangaer = null;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		if(mCarModel == CarModelDefine.CAR_MODEL_GEELY_NL_3)
		{
			return ServiceHelper.combineArray(new int[]{VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY}, RZC_GeelySeriesProtocol.VEHICLE_CAN_PROPERITIES);
		}
		return RZC_GeelySeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_GeelySeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_GeelySeriesProtocol.getProperityPermission(RZC_GeelySeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_GeelySeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_GeelySeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_GeelySeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GeelySeriesProtocol.CH_CMD_BASE_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GeelySeriesProtocol.CH_CMD_BASE_INFO }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GeelySeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO }));
							break;
						default:
							break;
					}
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY:
					if(mCarModel != CarModelDefine.CAR_MODEL_GEELY_NL_3)
					{
						break;
					}
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
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x01,band,freq&0xFF,(freq>>8)&0xFF,0x00,0x00,0x00}));
						}
						// 更新媒体格式信息
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB)
						{
							Log.i(TAG, "sync usb type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							Log.i(TAG, "sync other type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							Log.i(TAG, "sync aux type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));

							//bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] { sourceType, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{
							Log.i(TAG, "sync off type");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							Log.i(TAG, "sync bt music type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
					}
					break;
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					if(mCarModel != CarModelDefine.CAR_MODEL_GEELY_NL_3)
					{
						break;
					}
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
						int connStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY));
						String number = btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_NUMBER_PROPERTY);
						int callByte = 0x04;
						if(connStatus == 1)
						{
							if(callStatus == CarService.CALL_STATE_INCOMING)
							{
								callByte = 0x01;
							}
							//呼出
							else if(callStatus == CarService.CALL_STATE_DIAL || callStatus == CarService.CALL_STATE_DIALING)
							{
								callByte = 0x02;
							}
							//通话中
							else if (callStatus == CarService.CALL_STATE_COMMUNICATING)
							{
								callByte = 0x03;
							}
							else if(callStatus == CarService.CALL_STATE_WAITING || callStatus == CarService.CALL_STATE_HOLD || callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
							{
								callByte = 0x03;
							}
							//通话结束
							else if(callStatus == CarService.CALL_STATE_TERMINATED)
							{
								callByte = 0x04;
							}
							if(callByte != 0x04)
							{
								int[] numberBytes = ServiceHelper.combineArray(new int[]{callByte,0x01}, ServiceHelper.byteArrayToIntArray(number.getBytes()));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_PHONE_INFO,numberBytes));
							}
							else if(callByte == 0x00)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_PHONE_INFO,new int[]{callByte,0x01}));
							}
						}
						else
						{
							Log.i(TAG, "bt disconnected!");
						}
					}
					break;
				case VehicleInterfaceProperties.VIM_LANGUAGE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x00,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CLOSE_WINDOW_WITH_LOCK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x01,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_POSITION_LIGHT_OFF_WITH_LOCK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x02,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_UNLOCK_WITH_ACC_OFF_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x03,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REMOTE_LOCK_FEEDBACK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x04,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ELECTRONIC_STABILITY_CONTROL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x05,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_STATIC_PATH_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x06,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_DYNAMIC_PATH_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x07,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_FISHEYE_CALIBRATION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x08,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_EXIT_DELAY_TIME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x09,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_VIDEO_SIGNAL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x0A,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ASSIST_ACTION_FOLLOW_TURN_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x0B,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_SWITCH_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x0C,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AVM_CAMERA_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VEHICLE_CONTROL,new int[]{0x0D,Integer.valueOf(value)}));
					break;
				//全景切换
				case VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY:
					int[] point = ServiceHelper.stringToIntArray(value);
					//按下有效
					if(point != null && point.length == 3 && point[2] == 1)
					{
						//前
						if(point[0] >= 0 && point[0] < 512 &&
								point[1] >= 0 && point[1] < 300)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VIDEO_SWITCH,new int[]{0x01}));
						}
						//后
						else if(point[0] >= 512 && point[0] <= 1024 &&
								point[1] >= 0 && point[1] < 300)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VIDEO_SWITCH,new int[]{0x02}));
						}
						//左
						else if(point[0] >= 0 && point[0] < 512 &&
								point[1] >= 300 && point[1] <= 600)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VIDEO_SWITCH,new int[]{0x03}));
						}
						//右
						else if(point[0] >= 512 && point[0] <= 1024 &&
								point[1] >= 300 && point[1] <= 600)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_VIDEO_SWITCH,new int[]{0x04}));
						}
					}
					break;
				//空调控制
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_PROPERTY:
					final int subCmd = Integer.valueOf(value);
					switch (subCmd)
					{
						case VehiclePropertyConstants.AIR_CONDITION_CMD_POWER_ON:
						case VehiclePropertyConstants.AIR_CONDITION_CMD_POWER_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x00,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_ON:
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x01,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AUTO_ON:
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AUTO_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x02,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_DUAL_ON:
						case VehiclePropertyConstants.AIR_CONDITION_CMD_DUAL_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x03,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_INTERIOR_CYCLE_MODE_ON:
						case VehiclePropertyConstants.AIR_CONDITION_CMD_INTERIOR_CYCLE_MODE_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x04,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_EXTERNAL_CYCLE_MODE_ON:
						case VehiclePropertyConstants.AIR_CONDITION_CMD_EXTERNAL_CYCLE_MODE_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x05,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_SPEED_UP:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x0B,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_SPEED_DOWN:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x0C,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_LEFT_TEMP_UP:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x0D,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_LEFT_TEMP_DOWN:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x0E,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_RIGHT_TEMP_UP:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x0F,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_RIGHT_TEMP_DOWN:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x10,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_MODE_SELECT:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x11,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_REAR_DEFOGGING_ON:
						case VehiclePropertyConstants.AIR_CONDITION_CMD_REAR_DEFOGGING_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x12,0x01}));
							break;
						default:
							break;
					}
				//风向
				case VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY:
					int []fanDirs = ServiceHelper.stringToIntArray(value);
					if(fanDirs != null && fanDirs.length == 3)
					{
						//前除霜
						if(fanDirs[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON
								&& fanDirs[1] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF
								&& fanDirs[2] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x06,0x01}));
						}
						//吹头
						else if(fanDirs[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF
								&& fanDirs[1] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON
								&& fanDirs[2] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x07,0x01}));
						}
						//吹头吹脚
						else if(fanDirs[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF
								&& fanDirs[1] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON
								&& fanDirs[2] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x08,0x01}));
						}
						//吹脚
						else if(fanDirs[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF
								&& fanDirs[1] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF
								&& fanDirs[2] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x09,0x01}));
						}
						//吹脚前除霜
						else if(fanDirs[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON
								&& fanDirs[1] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF
								&& fanDirs[2] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GeelySeriesProtocol.HC_CMD_AIR_CONDITON_CONTROL,new int[]{0x0A,0x01}));
						}
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
			case RZC_GeelySeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_GeelySeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_GeelySeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_GeelySeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_GeelySeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_GeelySeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 6)
				{
					Log.i(TAG, "receive air condition info");
					// 空调开关
					int fanSpeed =  param[2];
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), true);
					if (acStatus == 0 || fanSpeed == 0)
					{
						Log.i(TAG, "AC is off");
						mAirConditionParam = param;
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param,6))
					{
						Log.i(TAG, "filter no change air condition data");
						break;
					}
					mAirConditionParam = param;
					
					// 制冷模式
					int[] acMode = new int[4];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					acMode[3] = ServiceHelper.getBit(param[5], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 循环模式
					int[] cycleMode = new int[1];
					cycleMode[0] = ServiceHelper.getBit(param[0], 5) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);

					// 风向
					int[] windDirInfo = new int[3];
					switch (param[1])
					{
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
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[] { fanSpeed }), false);

					// 前窗除雾和后窗除雾
					if(fanSpeed == 7 && (ServiceHelper.getBit(param[5], 7) == 1 || windDirInfo[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON))
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf("1"), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf("0"), false);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 6)), false);
				
					// 空调温度信息
					float[] interiorTemp = new float[2];
					int tempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_C;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, String.valueOf(tempUnit), false);
					// 驾驶员位温度(18-31)
					if (param[3] == 0x00)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[3] == 0x1F)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						interiorTemp[0] = (float) Math.max(RZC_GeelySeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_GeelySeriesProtocol.AC_HIGHEST_TEMP, RZC_GeelySeriesProtocol.AC_LOWEST_TEMP + (param[3] - 1) ));
					}

					// 副驾驶员位温度(18-26)
					if (param[4] == 0x00)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[4] == 0x1F)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						interiorTemp[1] = (float) Math.max(RZC_GeelySeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_GeelySeriesProtocol.AC_HIGHEST_TEMP, RZC_GeelySeriesProtocol.AC_LOWEST_TEMP + (param[4] - 1) ));
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);

					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_GeelySeriesProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 1)
				{
					Log.i(TAG, "receive vehicle door info");
					if(mCarModel != CarModelDefine.CAR_MODEL_GEELY_NL_3)
					{
						int flStatus = ServiceHelper.getBit(param[0], 6);
						int frStatus = ServiceHelper.getBit(param[0], 7);
						int rlStatus = ServiceHelper.getBit(param[0], 4);
						int rrStatus = ServiceHelper.getBit(param[0], 5);
						int trunkStatus = ServiceHelper.getBit(param[0], 3);
						int hoodStatus = ServiceHelper.getBit(param[0], 2);

						String strHisrotyFlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY);
						String strHisrotyFrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY);
						String strHisrotyRlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY);
						String strHisrotyRrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY);
						String strHisrotyTrunkStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY);
						String strHoodStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY);

						//有变化才上报
						if (mShowVehicleDoorInfo || strHisrotyFlStatus == null || strHisrotyFrStatus == null || strHisrotyRlStatus == null || strHisrotyRrStatus == null || 
								strHisrotyTrunkStatus == null || strHoodStatus == null || Integer.valueOf(strHisrotyFlStatus) != flStatus || Integer.valueOf(strHisrotyFrStatus) != frStatus || 
								Integer.valueOf(strHisrotyRlStatus) != rlStatus || Integer.valueOf(strHisrotyRrStatus) != rrStatus || 
								Integer.valueOf(strHisrotyTrunkStatus) != trunkStatus || Integer.valueOf(strHoodStatus) != hoodStatus)
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
					}
				}
				break;
			// 方向盘角度
			case RZC_GeelySeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				//解码盒软件版本
				if(mCarModel == CarModelDefine.CAR_MODEL_GEELY_NL_3)
				{
					if (param != null && param.length > 0)
					{
						String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
						Log.i(TAG, "receive protocol version info:" + version);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
					}
					break;
				}
				if (param != null && param.length >= 2)
				{
					int dir = param[0];
					float angle = param[1];
					// 向左
					if (dir <= 0x15)
					{
						angle = -(param[1] + (0xFF + 1) * dir);
					}
					// 向右
					else if(dir >= 0xEA)
					{
						angle = (0xFF + 1) - param[1] + (0xFF - dir) * (0xFF + 1);
					}
					else 
					{
						Log.i(TAG, "owerflow angle range!!!");
						break;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			// 协议版本号
			case RZC_GeelySeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if(mCarModel == CarModelDefine.CAR_MODEL_GEELY_NL_3)
				{
					break;
				}
				if (param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					Log.i(TAG, "receive protocol version info:" + version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
				}
				break;
			case RZC_GeelySeriesProtocol.CH_CMD_VEHICLE_STAUS_INFO:
				if (param != null && param.length >= 5)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LANGUAGE_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CLOSE_WINDOW_WITH_LOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_POSITION_LIGHT_OFF_WITH_LOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_UNLOCK_WITH_ACC_OFF_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_LOCK_FEEDBACK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ELECTRONIC_STABILITY_CONTROL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0,4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REVERSE_STATIC_PATH_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 0)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REVERSE_DYNAMIC_PATH_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 1)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FISHEYE_CALIBRATION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REVERSE_EXIT_DELAY_TIME_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REVERSE_VIDEO_SIGNAL_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ASSIST_ACTION_FOLLOW_TURN_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_SWITCH_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 6)), false);					
				}
				break;
			default:
				break;
		}
	}
}
