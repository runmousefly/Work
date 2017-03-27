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
import android.media.MediaRouter.VolumeCallback;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-中华车系车型协议管理
public class RZC_ChinaMotorManager extends MctVehicleManager
{
	private static String TAG = "RZC-ChinaMotor";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_CHINA_MOTOR_H3;
	private int mPressedKeyValue = 0;
	private boolean mShowVehicleDoorInfo = true;
	private int[] mAirConditionParam = null;
	
	private boolean mSupportSyncTime = true;

	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();

	
	public RZC_ChinaMotorManager(int carModel)
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
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "10");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		//mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
		//			ServiceHelper.intArrayToString(new int[]{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10}), false);
		
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF), false);
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
		mShowVehicleDoorInfo = false;
		mMcuManager = null;
		mCanMangaer = null;
		
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_ChinaMotorProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_ChinaMotorProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_ChinaMotorProtocol.getProperityPermission(RZC_ChinaMotorProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_ChinaMotorProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_ChinaMotorProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_ChinaMotorProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_ChinaMotorProtocol.CH_CMD_BASE_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_ChinaMotorProtocol.CH_CMD_AIR_CONDITIONING_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_ChinaMotorProtocol.CH_CMD_BASE_INFO }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_ChinaMotorProtocol.CH_CMD_STEERING_WHEEL_ANGLE}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_ChinaMotorProtocol.CH_CMD_PROTOCOL_VERSION_INFO }));
							break;
						default:
							break;
					}
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY:
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
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x01,band,freq&0xFF,(freq>>8)&0xFF,0x00,0x00,0x00}));
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
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x30,0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							Log.i(TAG, "sync other type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x30, 0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							Log.i(TAG, "sync aux type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x30, 0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));

							//bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] { sourceType, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{
							Log.i(TAG, "sync off type");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType,0x00, 0x00, 0x00,0x00, 0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							Log.i(TAG, "sync bt music type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_SOURCE, new int[] {sourceType, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
						}
					}
					break;
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
						//来电
						if(callStatus == CarService.CALL_STATE_INCOMING)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_BT_PHONE_INFO, new int[] { 0x00, 0x01 }));
						}
						//呼出
						else if(callStatus == CarService.CALL_STATE_DIAL || callStatus == CarService.CALL_STATE_DIALING)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_BT_PHONE_INFO, new int[] { 0x00, 0x02 }));
						}
						//通话中
						else if (callStatus == CarService.CALL_STATE_COMMUNICATING)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_BT_PHONE_INFO, new int[] { 0x00, 0x04 }));
						}
						else if(callStatus == CarService.CALL_STATE_WAITING || callStatus == CarService.CALL_STATE_HOLD || callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_BT_PHONE_INFO, new int[] { 0x00, 0x03 }));
						}
						//通话结束
						else if(callStatus == CarService.CALL_STATE_TERMINATED)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_BT_PHONE_INFO, new int[] {0x00,0x05}));
						}
						else 
						{
							Log.i(TAG, "other call status:"+callStatus);
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_BT_PHONE_INFO, new int[] {0x00,0x00}));
						}
					}
					break;
				case VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME:
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
					}
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ChinaMotorProtocol.HC_CMD_TIME_SET, new int[] {Math.max(0, time.year-2000),time.month+1,time.monthDay, time.hour,time.minute, 0x00,0x00 }));
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
			case RZC_ChinaMotorProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_ChinaMotorProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_ChinaMotorProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_ChinaMotorProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_ChinaMotorProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_ChinaMotorProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 5)
				{
					Log.i(TAG, "receive air condition info");
					// 空调开关
					int fanSpeed =  ServiceHelper.getBits(param[1], 0, 4);
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), true);
					if (acStatus == 0 || fanSpeed == 0)
					{
						Log.i(TAG, "AC is off");
						System.arraycopy(param,0,mAirConditionParam, 0, 4);
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param,5))
					{
						Log.i(TAG, "filter no change air condition data");
						break;
					}
					mAirConditionParam = param;
					
					// 制冷模式
					int[] acMode = new int[1];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 循环模式
					int[] cycleMode = new int[1];
					cycleMode[0] = ServiceHelper.getBit(param[0], 5) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);

					// 风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = ServiceHelper.getBit(param[1], 7) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[1], 5) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					// 风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[] { fanSpeed }), false);

					// 前窗除雾和后窗除雾
					if(fanSpeed == 7 && ServiceHelper.getBit(param[1], 7) == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf("1"), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf("0"), false);
					}
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
					//后区标志
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_REAR_FLAG_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
					
					// 空调温度信息
					float[] interiorTemp = new float[2];
					int tempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_C;
					if(mCarModel == CarModelDefine.CAR_MODEL_CHINA_MOTOR_V3)
					{
						tempUnit = ServiceHelper.getBit(param[4], 0);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, String.valueOf(tempUnit), false);
					// 驾驶员位温度(18-26)
					if (param[2] == 0x00)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[2] == 0x0E)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else if(param[2] > 0x00 && param[2] < 0x0E )
					{
						interiorTemp[0] = (float) Math.max(RZC_ChinaMotorProtocol.AC_LOWEST_TEMP, Math.min(RZC_ChinaMotorProtocol.AC_HIGHEST_TEMP, RZC_ChinaMotorProtocol.AC_LOWEST_TEMP + (param[2] - 1)));
					}

					// 副驾驶员位温度(18-26)
					if (param[3] == 0x00)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[3] == 0x0E)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else if(param[2] > 0x00 && param[2] < 0x0E )
					{
						interiorTemp[1] = (float) Math.max(RZC_ChinaMotorProtocol.AC_LOWEST_TEMP, Math.min(RZC_ChinaMotorProtocol.AC_HIGHEST_TEMP, RZC_ChinaMotorProtocol.AC_LOWEST_TEMP + (param[3] - 1) ));
					}
					
					//转华氏温度
					if(tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_F)
					{
						interiorTemp[0] = interiorTemp[0]*1.8f + 32;
						interiorTemp[1] = interiorTemp[1]*1.8f + 32;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);

					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_ChinaMotorProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 1)
				{
					Log.i(TAG, "receive vehicle door info");
					int flStatus = ServiceHelper.getBit(param[0], 6);
					int frStatus = ServiceHelper.getBit(param[0], 7);
					int rlStatus = ServiceHelper.getBit(param[0], 4);
					int rrStatus = ServiceHelper.getBit(param[0], 5);
					int trunkStatus = ServiceHelper.getBit(param[0], 3);
					int hoodStatus = 0;

					String strHisrotyFlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY);
					String strHisrotyFrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY);
					String strHisrotyRlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY);
					String strHisrotyRrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY);
					String strHisrotyTrunkStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY);

					//有变化才上报
					if (mShowVehicleDoorInfo || strHisrotyFlStatus == null || strHisrotyFrStatus == null || strHisrotyRlStatus == null || strHisrotyRrStatus == null || 
							strHisrotyTrunkStatus == null || Integer.valueOf(strHisrotyFlStatus) != flStatus || Integer.valueOf(strHisrotyFrStatus) != frStatus || 
							Integer.valueOf(strHisrotyRlStatus) != rlStatus || Integer.valueOf(strHisrotyRrStatus) != rrStatus || 
							Integer.valueOf(strHisrotyTrunkStatus) != trunkStatus)
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
					//安全带警告
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SEAT_BELT_STATUS_DRIVER_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
				}
				break;
			// 方向盘角度
			case RZC_ChinaMotorProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length >= 3)
				{
					int dir = ServiceHelper.getBit(param[0],0);
					float angle = ServiceHelper.MAKEWORD(param[1],param[2]);
					// 向左
					if (dir == 1)
					{
						angle = -angle;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			// 协议版本号
			case RZC_ChinaMotorProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
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
