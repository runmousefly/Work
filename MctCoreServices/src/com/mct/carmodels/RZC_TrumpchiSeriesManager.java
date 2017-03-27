package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.carmodels.RZC_FordSeriesManager.SystemTimeSetReceiver;
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
import android.provider.ContactsContract.PinnedPositions;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-广汽传祺车系协议管理
public class RZC_TrumpchiSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC-Trumpchi";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_TRUMPCH_GS5;
	private int mPressedKeyValue = 0;
	private String mSyncMediaInfo = null;
	private long mSyncMediaTick = 0;
	private int[] mAirConditionParam = null;
	private int mFrontRadarLevelCount = 0;
	private int mRearRadarLevelCount = 0;
	private boolean mShowVehicleDoorInfo = true;

	private boolean mSupportSyncTime = true;

	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();
	
	public RZC_TrumpchiSeriesManager(int carModel)
	{
		mCarModel = carModel;
		if(mCarModel == CarModelDefine.CAR_MODEL_TRUMPCH_GA6)
		{
			mSupportSyncTime = true;
		}
		else
		{
			mSupportSyncTime = false;
		}
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
		mShowVehicleDoorInfo = false;
		mMcuManager = null;
		mCanMangaer = null;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_TrumpchiSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_TrumpchiSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_TrumpchiSeriesProtocol.getProperityPermission(RZC_TrumpchiSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_TrumpchiSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_TrumpchiSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_TrumpchiSeriesProtocol.getProperityDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		boolean bRet = false;
		if (mMcuManager == null || mCanMangaer == null)
		{
			Log.e(TAG, "McuManager is not ready");
			return bRet;
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
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							break;
						// 雷达提示音
						case VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_ALL_SETTING:
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x00,0xFF }));			
						case VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME:
							int hourMode_24 = 0x01;
							int amMode = 0;
							Time time = new Time();
							time.setToNow();
							String strTimeFormat = android.provider.Settings.System.getString(mService.getContentResolver(),
									android.provider.Settings.System.TIME_12_24);
							if(strTimeFormat != null && strTimeFormat.equals("12"))
							{
								hourMode_24 = 0x00;
								if(time.hour > 12)
								{
									amMode = 1;
									time.hour = time.hour-12;
								}
								else if(time.hour == 0)
								{
									time.hour = 12;
								}
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_TIME_SET, new int[] { time.minute, time.hour, hourMode_24, amMode}));
							break;
						default:
							break;
					}
				//全景影像开关
				case VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_SWITCH_PROPERTY:
					return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_AVM_SWITCH, new int[] { Integer.valueOf(value) }));
				case VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY:
					int[] point = ServiceHelper.stringToIntArray(value);
					if(point != null && point.length == 3 && point[2] == 1
							&& point[1] >= 503 && point[1] <= 571)
					{
						//Icon Size 80*68(1024*600),63*55(800*480)
						if(point[0] >= 7 && point[0] <= 87)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_AVM_SET, new int[] { 0x01 }));
						}
						else if(point[0] >= 126 && point[0] <= 206)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_AVM_SET, new int[] { 0x02 }));
						}
						else if(point[0] >= 245 && point[0] <= 325)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_AVM_SET, new int[] { 0x03 }));
						}
						else if(point[0] >= 367 && point[0] <= 447)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_AVM_SET, new int[] { 0x04 }));
						}
						else if(point[0] >= 486 && point[0] <= 566)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_AVM_SET, new int[] { 0x05 }));
						}
					}
					break;
				//全景影像设置
				case VehicleInterfaceProperties.VIM_AC_COMPRESSOR_STATUS_IN_AUTO_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x02,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AC_CYCLE_WAY_IN_AUTO_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x03,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AC_COMFORT_CURVE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x04,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_DRIVER_SEAT_AUTO_HEATING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x05,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_PSNGR_SEAT_AUTO_HEATING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x06,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_TOP_SPEED_LIMIT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x07,Integer.valueOf(value)/10 }));
					break;
				case VehicleInterfaceProperties.VIM_ALARM_VOLUME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x08,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_REMOTE_POWER_ON_TIME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x09,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_REMOTE_STARTUP_TIME_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0A,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_STEERING_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0B,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_KEY_AND_REMOTE_UNLOCK_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0C,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SPEED_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0D,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_SMART_DOOR_UNLOCK_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0E,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_REMOTE_CONTROL_WINDOW_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x0F,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_FRONT_WIPER_REPAIR_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x10,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_MW_AUTOMATIC_WIPING_IN_RAIN_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x11,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_BACK_HOME_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x12,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_LANE_CHANGE_TRUN_LIGHT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x13,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_DAYTIME_RUNNING_LIGHT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x14,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_LIGHT_SENSITIVITY_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x15,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AC_ANION_MODE_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x18,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_WELCOME_LIGHT_STATUS_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x19,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_KEY_AUTO_IDENTIFY_SEAT_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1A,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_MW_FOLD_AWAY_AFTER_PARKING_PROPERTY:
					bRet = mMcuManager.postCanData(mCanMangaer.pack(RZC_TrumpchiSeriesProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x1B,Integer.valueOf(value) }));
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
		if(bRet)
		{
			//mService.cachePropValue(propId, value, false);
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
			case RZC_TrumpchiSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				//GA6、GS4 车型方控格式
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_TrumpchiSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_TrumpchiSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				else if(param != null && param.length == 1)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0]);
					//按下
					if(param[0] > 0)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_TrumpchiSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					//弹起
					else if(param[0] == RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE && mPressedKeyValue != RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_TrumpchiSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE;
					}
				}
				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_PANEL_KEY_INFO:
				if (param != null && param.length >= 2)
				{
					Log.i(TAG, "receive panel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_TrumpchiSeriesProtocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						//进入/退出全景
						if(param[0] == RZC_TrumpchiSeriesProtocol.PANEL_KEY_APS)
						{
							mService.broadcastCameraPreview(2);
							mPressedKeyValue = RZC_TrumpchiSeriesProtocol.SWC_KEY_NONE;
						}
						else 
						{
							mPressedKeyValue = param[0];
							mService.broadcastKeyEvent(RZC_TrumpchiSeriesProtocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
						}
					}
					// 2 按键持续按下
				}
				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 5)
				{
					Log.i(TAG, "receive air condition info");
					//空调开关
					int fanSpeed = ServiceHelper.getBits(param[2],0,4);
					if(fanSpeed == 0)
					{
						Log.i(TAG, "AC is off");
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(0), true);
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param))
					{
						Log.i(TAG, "filter no change air condition data");
						break;
					}
					mAirConditionParam = param;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(1), true);

					// 制冷模式
					int[] acMode = new int[4];
					acMode[0] = ServiceHelper.getBit(param[1], 0) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[1], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[2], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					acMode[3] = ServiceHelper.getBit(param[2], 7) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 前窗除雾和后窗除雾
					if(fanSpeed == 7 && ServiceHelper.getBit(param[1],6) == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "1", false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "0", false);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);

					// 循环模式
					int[] cycleMode = new int[1];
					cycleMode[0] = ServiceHelper.getBit(param[1], 7) == 0 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);

					// 风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[1], 3) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[1], 4) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					// 风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[] { fanSpeed }), false);

					// 空调温度信息
					float[] interiorTemp = new float[2];
					
					if(param[0] == 0)
					{
						// 驾驶员位温度
						if (param[3] == 0x01)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if (param[3] == 0x39)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else
						{
							interiorTemp[0] = (float) Math.max(RZC_TrumpchiSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_TrumpchiSeriesProtocol.AC_HIGHEST_TEMP, RZC_TrumpchiSeriesProtocol.AC_LOWEST_TEMP + (param[3] - 3)/2 * 0.5));
						}
						
						// 副驾驶员位温度
						if (param[4] == 0x01)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if (param[4] == 0x39)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else
						{
							interiorTemp[1] = (float) Math.max(RZC_TrumpchiSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_TrumpchiSeriesProtocol.AC_HIGHEST_TEMP, RZC_TrumpchiSeriesProtocol.AC_LOWEST_TEMP + (param[4] - 3)/2 * 0.5));
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.AC_TEMP_UNIT_C,VehiclePropertyConstants.AC_TEMP_UNIT_C}), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.AC_TEMP_UNIT_NO,VehiclePropertyConstants.AC_TEMP_UNIT_NO}), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(new float[]{param[0],param[0]}), false);
					}
					
					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_BACKLIGHT_INFO:
				if (param != null && param.length >= 1)
				{
					Log.i(TAG, "receive backlight info:"+param[0]);
				}
				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 2)
				{
					Log.i(TAG, "receive vehicle door info");
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
					String strHisrotyHoodStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY);

					//有变化才上报
					if (mShowVehicleDoorInfo || strHisrotyFlStatus == null || strHisrotyFrStatus == null || strHisrotyRlStatus == null || strHisrotyRrStatus == null || 
							strHisrotyTrunkStatus == null || strHisrotyHoodStatus == null || Integer.valueOf(strHisrotyFlStatus) != flStatus || Integer.valueOf(strHisrotyFrStatus) != frStatus || 
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
				}
				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length >= 6)
				{
					Log.i(TAG, "receive rear radar info");
					if(param[0] != 0)
					{
						Log.i(TAG, "not handle distance value");
						break;
					}
					mRearRadarLevelCount = param[1];
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
							ServiceHelper.intArrayToString(new int[]{mFrontRadarLevelCount,mFrontRadarLevelCount,mFrontRadarLevelCount,mFrontRadarLevelCount,
									mRearRadarLevelCount,mRearRadarLevelCount,mRearRadarLevelCount,mRearRadarLevelCount,
									0,0,0,0,0,0,0,0}), false);
					int[] rearRadarLevel = new int[4];
					System.arraycopy(param, 2, rearRadarLevel, 0, 4);
					for(int i=0;i<4;i++)
					{
						if(rearRadarLevel[i] >= 0 && rearRadarLevel[i] < mRearRadarLevelCount)
						{
							rearRadarLevel[i] = rearRadarLevel[i] + 1;
						}
						else
						{
							rearRadarLevel[i] = 0;
						}
						
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(rearRadarLevel), false);
				}

				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length >= 6)
				{
					Log.i(TAG, "receive front radar info");
					if(param[0] != 0)
					{
						Log.i(TAG, "not handle distance value");
						break;
					}
					mFrontRadarLevelCount = param[1];
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
							ServiceHelper.intArrayToString(new int[]{mFrontRadarLevelCount,mFrontRadarLevelCount,mFrontRadarLevelCount,mFrontRadarLevelCount,
									mRearRadarLevelCount,mRearRadarLevelCount,mRearRadarLevelCount,mRearRadarLevelCount,
									0,0,0,0,0,0,0,0}), false);
					int[] frontRadarLevel = new int[4];
					System.arraycopy(param, 2, frontRadarLevel, 0, 4);
					for(int i=0;i<4;i++)
					{
						if(frontRadarLevel[i] >= 0 && frontRadarLevel[i] < mFrontRadarLevelCount)
						{
							frontRadarLevel[i] = frontRadarLevel[i] + 1;
						}
						else
						{
							frontRadarLevel[i] = 0;
						}	
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(frontRadarLevel), false);
				}
				break;
			
			// 方向盘角度
			case RZC_TrumpchiSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length == 2)
				{
					float angle = param[0];
					int dir = param[1];
					// 向左
					if (dir < 0x1E)
					{
						angle = -((0x1E-param[0])*256-param[1]);
					}
					// 向右
					else
					{
						angle = (param[0]-0x1E)*256+param[1];
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_AVM_STATUS_INFO:
				if(param != null && param.length >= 1)
				{
					Log.i(TAG, "receive avm video view info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AVM_CAMERA_STATUS_PROPERTY, String.valueOf(param[0]), false);
				}
				break;
			//仅 GS4 高配车型,方案选做
			case RZC_TrumpchiSeriesProtocol.CH_CMD_LINK_SOS_INFO:
				if(param != null && param.length >= 1)
				{
					Log.i(TAG, "receive link/sos info");
					break;
				}
				break;
			// 协议版本号
			case RZC_TrumpchiSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if (param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					Log.i(TAG, "receive protocol version info:" + version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
				}
				break;
			case RZC_TrumpchiSeriesProtocol.CH_CMD_VEHICLE_SETTING_INFO:
				if (param != null && param.length >= 2)
				{
					Log.i(TAG, "receive vehicle setting info");
					switch (param[0])
					{
						//语音
						case 0x01:
							
							break;
						case 0x02:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AC_COMPRESSOR_STATUS_IN_AUTO_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x03:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AC_CYCLE_WAY_IN_AUTO_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x04:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AC_COMFORT_CURVE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x05:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DRIVER_SEAT_AUTO_HEATING_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x06:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_PSNGR_SEAT_AUTO_HEATING_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x07:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_TOP_SPEED_LIMIT_PROPERTY, String.valueOf(param[1]*10), false);
							break;
						case 0x08:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ALARM_VOLUME_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x09:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_POWER_ON_TIME_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x0A:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_STARTUP_TIME_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x0B:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_MODE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x0C:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_KEY_AND_REMOTE_UNLOCK_MODE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x0D:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SPEED_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x0E:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_SMART_DOOR_UNLOCK_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x0F:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_CONTROL_WINDOW_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x10:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_FRONT_WIPER_REPAIR_MODE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x11:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_AUTOMATIC_WIPING_IN_RAIN_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x12:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_BACK_HOME_MODE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x13:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_LANE_CHANGE_TRUN_LIGHT_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x14:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_DAYTIME_RUNNING_LIGHT_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x15:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LIGHT_SENSITIVITY_PROPERTY, String.valueOf(param[1]), false);
							break;
						//驾驶位置座椅加热
						case 0x16:
							String strDriverSeatHeaterLevel = mService.getCacheValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY, String.valueOf(param[1]), false);
							if(strDriverSeatHeaterLevel == null || Integer.valueOf(strDriverSeatHeaterLevel) != param[1])
							{
								mCanMangaer.showAirConditionEvent(true);
							}
							break;
						//副驾驶位置座椅加热
						case 0x17:
							String strPsngrSeatHeaterLevel = mService.getCacheValue(VehicleInterfaceProperties.VIM_HEATER_PSNGR_SEAT_PROPERTY);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_PSNGR_SEAT_PROPERTY, String.valueOf(param[1]), false);
							if(strPsngrSeatHeaterLevel == null || Integer.valueOf(strPsngrSeatHeaterLevel) != param[1])
							{
								mCanMangaer.showAirConditionEvent(true);
							}
							break;
						case 0x18:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_AC_ANION_MODE_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x19:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_WELCOME_LIGHT_STATUS_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x1A:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_KEY_AUTO_IDENTIFY_SEAT_PROPERTY, String.valueOf(param[1]), false);
							break;
						case 0x1B:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_FOLD_AWAY_AFTER_PARKING_PROPERTY, String.valueOf(param[1]), false);
							break;
						default:
							break;
					}
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
