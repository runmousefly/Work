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

//睿智诚-长城车系车型协议管理
public class RZC_GreatWallManager extends MctVehicleManager
{
	private static String TAG = "RZC-GreatWall";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_GREAT_WALL_H2;
	private int mPressedKeyValue = 0;
	private boolean mShowVehicleDoorInfo = true;
	private int[] mAirConditionParam = null;
	
	private boolean mSupportSyncTime = true;
	private Timer mSyncVehicleInfoTimer = null;
	private long mSyncVehicleTimeTick = 0;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();

	public RZC_GreatWallManager(int carModel)
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
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{0,0,0,0,4,4,4,4,0,0,0,0,0,0,0,0}), false);
		
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_ALL_KINDS_PROPERTY, "3");

	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		mShowVehicleDoorInfo = false;
		mMcuManager = null;
		mCanMangaer = null;
		stopSyncVehicleInfoTimer();
		if(mSystemTimeSetReceiver != null && mService != null)
		{
			mService.unregisterReceiver(mSystemTimeSetReceiver);
		}
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_GreatWallProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_GreatWallProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_GreatWallProtocol.getProperityPermission(RZC_GreatWallProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_GreatWallProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_GreatWallProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_GreatWallProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GreatWallProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GreatWallProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GreatWallProtocol.HC_CMD_VEHICLE_SET, new int[] {0x03,time.hour,time.minute }));
							break;
						default:
							break;
					}
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GreatWallProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x02,Integer.valueOf(value)+1,0xFF }));
					break;
				/*倒车视频开关(预览)
				case VehicleInterfaceProperties.
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GreatWallProtocol.HC_CMD_VEHICLE_SET, new int[] { 0x01,Integer.valueOf(value),0xFF }));
					break;*/
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
			case RZC_GreatWallProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_GreatWallProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_GreatWallProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_GreatWallProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_GreatWallProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_GreatWallProtocol.CH_CMD_PANEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive panel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_GreatWallProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_GreatWallProtocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_GreatWallProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_GreatWallProtocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_GreatWallProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 5)
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
					
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param,5))
					{
						Log.i(TAG, "filter no change air condition data");
						if(param[5] != 0xFF)
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.format("%.1f", param[5]*0.5f-40),false);
						}
						break;
					}
					mAirConditionParam = param;
					
					// 制冷模式
					int[] acMode = new int[3];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
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
						case 6:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
							break;
						case 7:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							break;
						default:
							break;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					
					// 风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[] { fanSpeed }), false);

					// 前窗除雾和后窗除雾
					if(fanSpeed == 8 && (ServiceHelper.getBit(param[0], 0) == 1 || windDirInfo[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON))
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf("1"), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf("0"), false);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
				
					// 空调温度信息
					float[] interiorTemp = new float[2];
					int tempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_NO;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, String.valueOf(tempUnit), false);
					interiorTemp[0] = param[3];
					interiorTemp[1] = param[4];
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);

					//环境温度
					if(param[5] != 0xFF)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.format("%.1f", param[5]*0.5f-40),false);
					}
					
					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_GreatWallProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
			case RZC_GreatWallProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 1)
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
				break;
			// 方向盘角度
			case RZC_GreatWallProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
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
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.format("%.1f",angle*0.1f), false);
				}
				break;
			//右视
			case RZC_GreatWallProtocol.CH_CMD_RIGHT_CAMERA:
				if(param != null && param.length >= 1)
				{
					Log.i(TAG, "receive right camera:"+param[0]);
					mService.broadcastRightCameraPreview(param[0]);
				}
				break;
			// 协议版本号
			case RZC_GreatWallProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
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
		// 定时发送
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
