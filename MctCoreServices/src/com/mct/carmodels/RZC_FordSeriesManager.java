package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.util.ArrayUtils;
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
import android.util.SparseIntArray;
import android.view.KeyEvent;

//睿智诚-福特车系车型协议管理
public class RZC_FordSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC_FordSeries";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_FORD_12_FUCOS;
	private int mPressedKeyValue = 0;
	private String mSyncMediaInfo = null;
	private long mSyncMediaTick = 0;
	private boolean mShowVehicleDoorInfo = true;

	private boolean mSupportSyncTime = true;

	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();
	
	
	//福特SYNC缓存
	private int mCurMenuType = 0;
	private int mCurDialogType = 0;
	private HashMap<Integer, String> mMenuCacheInfo = new HashMap<Integer, String>();
	private HashMap<Integer,String> mDialogCacheInfo = new HashMap<Integer, String>();
	
	public RZC_FordSeriesManager(int carModel)
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
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "31");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{7,13,13,7,7,31,31,7,0,0,0,0,0,0,0,0}), false);
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
		
		mMenuCacheInfo.clear();
		mDialogCacheInfo.clear();
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_CONTEXT_CACHE_PROPERTY, null);
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_FORD_SYNC_DIALOG_CONTEXT_CACHE_PROPERTY, null);
		
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
		return RZC_FordSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_FordSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_FordSeriesProtocol.getProperityPermission(RZC_FordSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_FordSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_FordSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_FordSeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							//设置车型
							if(mCarModel == CarModelDefine.CAR_MODEL_FORD_17_KUGA)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_CAR_MODEL_SET, new int[] { 0x02 }));
							}
							else 
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_CAR_MODEL_SET, new int[] { 0x01 }));
							}
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_SYNC_STATUS_INFO}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_SYNC_SRT_DOWN}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_SYNC_SRT_UP}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_SYNC_SRT_SHORT}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_FRONT_RADAR_INFO }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_REAR_RADAR_INFO }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_BASE_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_FordSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO }));
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_TIME_SET, new int[] {Math.max(0, time.year-2000),time.month+1,time.monthDay, time.hour,time.minute, time.second }));
							break;
						default:
							break;
					}
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_FORD_SYNC_REQ_CMD_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_FordSeriesProtocol.HC_CMD_CONTROL, new int[]{0xA1,Integer.valueOf(value)}));
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
			case RZC_FordSeriesProtocol.CH_CMD_BACKLIGHT_INFO:
				if (param != null && param.length >= 1)
				{
					Log.i(TAG, "receive backlight info:" + param[0]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BRIGHTNESS_LVL_PROPERTY, String.valueOf(param[0]), false);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length >= 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_FordSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_FordSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_FordSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_FordSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 6)
				{
					Log.i(TAG, "receive air condition info");
					// 空调开关
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), true);
					if (acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					// 请求显示空调信息
					int showAcBar = ServiceHelper.getBit(param[1], 4);
					if (showAcBar == 0)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						//车外温度变化会导致空调信息请求显示为0
						mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[5])), false);
						break;
					}
					// 制冷模式
					int[] acMode = new int[4];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					acMode[3] = ServiceHelper.getBit(param[4], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 前窗除雾和后窗除雾
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);

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
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[] { ServiceHelper.getBits(param[1], 0, 4) }), false);

					// 空调温度信息
					int tempUnit = ServiceHelper.getBit(param[4], 6) == 0 ? VehiclePropertyConstants.AC_TEMP_UNIT_C:VehiclePropertyConstants.AC_TEMP_UNIT_F;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{tempUnit,tempUnit}), false);
					float[] interiorTemp = new float[2];
					// 驾驶员位温度(18-26)
					if(param[2] == 0x00)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if(param[2] == 0x7F)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else if(param[2] >= 0x1F && param[2] <= 0x3B )
					{
						interiorTemp[0] = (float)Math.max(RZC_FordSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_FordSeriesProtocol.AC_HIGHEST_TEMP, RZC_FordSeriesProtocol.AC_LOWEST_TEMP+(param[2]-0x1F)*0.5));
						if(tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_F)
						{
							interiorTemp[0] = 9.0f/5.0f*interiorTemp[0]+32.0f;
						}
					}
					
					//副驾驶员位温度
					if(param[3] == 0x00)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if(param[3] == 0x7F)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else if(param[3] >= 0x1F && param[3] <= 0x3B )
					{
						interiorTemp[1] = (float)Math.max(RZC_FordSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_FordSeriesProtocol.AC_HIGHEST_TEMP, RZC_FordSeriesProtocol.AC_LOWEST_TEMP+(param[3]-0x1F)*0.5));
						if(tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_F)
						{
							interiorTemp[1] = 9.0f/5.0f*interiorTemp[1]+32.0f;
						}
						
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
					
					//车外温度
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[5])), false);
					
					if (showAcBar == 1)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						mCanMangaer.showAirConditionEvent(true);
					}
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}

				break;
			case RZC_FordSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length >= 4)
				{
					Log.i(TAG, "receive front radar info");
					int []frontRadarInfo = new int[4];
					System.arraycopy(param, 0, frontRadarInfo, 0, 4);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(frontRadarInfo), false);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 2)
				{
					Log.i(TAG, "receive vehicle base info");
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
					}
					
					//安全气囊
					//MY KEY音量限制
					//MY KEY MUTE
					
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_PARK_ASSIST_INFO:
				if(param != null && param.length > 1)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 3)), false);
				}
				break;
			// 方向盘角度
			case RZC_FordSeriesProtocol.CH_CMD_DATE_TIME_INFO:
				if (param != null && param.length >= 6)
				{
					Log.i(TAG, "receive time info:"+(2000+param[0])+"-"+param[1]+"-"+param[2]+" "+param[3]+":"+param[4]+":"+param[5]);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_LANGUAGE_INFO:
				if(param != null)
				{
					Log.i(TAG, "receive language info");
				}
				break;
			// 协议版本号
			case RZC_FordSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if (param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					Log.i(TAG, "receive protocol version info:" + version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
				}
				break;
			//SYNC版本
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_VERSION:
				if(param != null && param.length > 1)
				{
					Log.i(TAG, "receive sync version:"+param[0]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_VERSION_PROPERTY, String.valueOf(param[0]), true);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_MENU_INFO:
				
				if(param != null && param.length >= 8)
				{
					//判断菜单和对话框的类型有更新即清理缓存
					if(mCurMenuType != param[0])
					{
						mMenuCacheInfo.clear();
						mCurMenuType = param[0];
						mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_CONTEXT_CACHE_PROPERTY, null);
					}
					if(mCurDialogType != param[3])
					{
						mDialogCacheInfo.clear();
						mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_FORD_SYNC_DIALOG_CONTEXT_CACHE_PROPERTY, null);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_INFO_COLLECT_PROPERTY, VehiclePropertyConstants.formatSyncMenuInfoString(
							param[0], param[1], param[2], param[6], param[5], param[3], param[4]), false);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_MENU_ITEM_INFO:
				if(param != null && param.length >= 4)
				{
					String itemInfo = null;
					if(param.length > 4)
					{
						itemInfo = ServiceHelper.toString(param, 4, param.length-4, "UNICODE");
					}
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_INFO_COLLECT_PROPERTY, VehiclePropertyConstants.formatSyncMenuItemInfoString(
					//		param[0], itemInfo,ServiceHelper.getBits(param[1], 0, 4), ServiceHelper.getBit(param[1], 4), param[2], param[3]),false);
					Random random=new Random();
					String strItemInfo = VehiclePropertyConstants.formatSyncMenuItemInfoString(param[0], itemInfo,ServiceHelper.getBits(param[1], 0, 4), ServiceHelper.getBit(param[1], 4),random.nextInt(48) ,random.nextInt(48));
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_INFO_COLLECT_PROPERTY, strItemInfo ,false);
					String[] itemCacheArray = new String[1];
					//更新菜单缓存内容
					if((param[0] >= 1 && param[0] <= 5) || (param[0] >= 11 && param[0] <= 14))
					{
						mMenuCacheInfo.put(param[0], strItemInfo);
						itemCacheArray = mMenuCacheInfo.values().toArray(itemCacheArray);
						mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_CONTEXT_CACHE_PROPERTY, ServiceHelper.stringArrayToString(itemCacheArray));
					}
					//更新对话框缓存内容
					else if((param[0] >= 6 && param[0] <= 10) || (param[0] >= 15 && param[0] <= 18))
					{
						mDialogCacheInfo.put(param[0], strItemInfo);
						itemCacheArray = mDialogCacheInfo.values().toArray(itemCacheArray);
						mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_FORD_SYNC_DIALOG_CONTEXT_CACHE_PROPERTY, ServiceHelper.stringArrayToString(itemCacheArray));
					}
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_PLAY_TIME_INFO:
				if(param != null && param.length >= 3)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_MEDIA_PLAY_TIME_INFO_PROPERTY, ServiceHelper.intArrayToString(new int[]{param[0],param[1],param[2]}), true);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_CALL_TIME_INFO:
				if(param != null && param.length >= 3)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_CALL_TIME_INFO_PROPERTY, ServiceHelper.intArrayToString(new int[]{param[0],param[1],param[2]}), true);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_SRT_UP:
				if(param != null && param.length >= 1)
				{
					String syncUpInfo = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_SRT_UP_INFO_PROPERTY, syncUpInfo, false);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_SRT_DOWN:
				if(param != null && param.length >= 1)
				{
					String syncDownInfo = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_SRT_UP_INFO_PROPERTY, syncDownInfo, false);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_SRT_SHORT:
				if(param != null && param.length >= 1)
				{
					String syncShortInfo = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_SRT_UP_INFO_PROPERTY, syncShortInfo, false);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_STATUS_INFO:
				if(param != null && param.length >= 3)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_RUN_MODE_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_DEVICE_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 0)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_BLUETOOTH_CONNECT_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_MESSAGE_FLAG_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_SPEECH_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_CALL_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_MEDIA_INFO_ENABLE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 6)), false);

					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_PHONE_SIGNAL_LEVEL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0, 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_PHONE_BATTERY_LEVEL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2],4, 4)), false);
				}
				break;
			case RZC_FordSeriesProtocol.CH_CMD_SYNC_REQ_SWITCH:
				if(param != null && param.length >= 1)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FORD_SYNC_AUDIO_REQUEST_PROPERTY, String.valueOf(param[0]), false);
				}
				break;
			default:
				break;
		}
	}
	
	private int tramslateRadarDistance(int originDistance)
	{
		//R
		if(originDistance <= 0x01)
		{
			return originDistance;
		}
		//Y
		else if(originDistance >= 0x02 && originDistance <= 0x07)
		{
			return 0x02;
		}
		//G4
		else if(originDistance >= 0x08 && originDistance <= 0x0D)
		{
			return 0x03;
		}
		//G3
		else if(originDistance >= 0x0E && originDistance <= 0x13)
		{
			return 0x04;
		}
		////G2
		else if(originDistance >= 0x14 && originDistance <= 0x19)
		{
			return 0x05;
		}
		//G1
		else if(originDistance >= 0x1A && originDistance <= 0x1F)
		{
			return 0x06;
		}
		return 0;
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
