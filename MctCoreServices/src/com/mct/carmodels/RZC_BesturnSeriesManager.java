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
import android.net.UrlQuerySanitizer.ParameterValuePair;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup.MarginLayoutParams;

//睿智诚-奔腾车系车型协议管理
public class RZC_BesturnSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC_BesturnSeries";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_BESTURN_16_B50;
	private int mPressedKeyValue = 0;
	private int[] mAirConditionParam = null;
	private boolean mShowVehicleDoorInfo = true;

	public RZC_BesturnSeriesManager(int carModel)
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
		mMcuManager = null;
		mCanMangaer = null;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_BesturnSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_BesturnSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_BesturnSeriesProtocol.getProperityPermission(RZC_BesturnSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_BesturnSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_BesturnSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_BesturnSeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_BesturnSeriesProtocol.CH_CMD_BASE_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_BesturnSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO }));
							// 显示车外温度
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_BesturnSeriesProtocol.CH_CMD_BASE_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_BesturnSeriesProtocol.CH_CMD_BASE_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_BesturnSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO }));
							break;
						default:
							break;
					}
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_BesturnSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
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
			case RZC_BesturnSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_BesturnSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_BesturnSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_BesturnSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] == 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_BesturnSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_BesturnSeriesProtocol.CH_CMD_PANEL_KEY:
				if (param != null && param.length >= 2)
				{
					Log.i(TAG, "receive panel key,Key:" + param[0] + ",status:" + param[1]);
					//旋钮操作
					if(param[0] == RZC_BesturnSeriesProtocol.PANEL_SCROLL_KEY_VOLUME_DOWN ||
							param[0] == RZC_BesturnSeriesProtocol.PANEL_SCROLL_KEY_VOLUME_UP ||
							param[0] == RZC_BesturnSeriesProtocol.PANEL_SCROLL_KEY_TUNER_DOWN ||
							param[0] == RZC_BesturnSeriesProtocol.PANEL_SCROLL_KEY_TUNER_UP)
					{
						//for(int i=0;i<param[1];i++)
						{
							mService.broadcastKeyEvent(RZC_BesturnSeriesProtocol.panelKeyToUserKeyForB50(param[0]), KeyEvent.ACTION_DOWN, param[1]);
							mService.broadcastKeyEvent(RZC_BesturnSeriesProtocol.panelKeyToUserKeyForB50(param[0]), KeyEvent.ACTION_UP,0);
							
						}
						mPressedKeyValue =  RZC_BesturnSeriesProtocol.SWC_KEY_NONE;
					}
					// 0 按键释放
					else if (param[1] == 0 && mPressedKeyValue != RZC_BesturnSeriesProtocol.SWC_KEY_NONE)
					{
						if(mCarModel == CarModelDefine.CAR_MODEL_BESTURN_16_B50)
						{
							mService.broadcastKeyEvent(RZC_BesturnSeriesProtocol.panelKeyToUserKeyForB50(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						}
						mPressedKeyValue = RZC_BesturnSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] == 1)
					{
						mPressedKeyValue = param[0];
						if(mCarModel == CarModelDefine.CAR_MODEL_BESTURN_16_B50)
						{
							mService.broadcastKeyEvent(RZC_BesturnSeriesProtocol.panelKeyToUserKeyForB50(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
						}
					}
					// 2 按键持续按下
				}
				break;
			case RZC_BesturnSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length == 5)
				{
					Log.i(TAG, "receive air condition info");
					int acStatus = (param[2] == 0) ? 0 : 1;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					if (acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						mAirConditionParam = param;
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param))
					{
						Log.i(TAG, "filter no change air condition data");
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
					cycleMode[0] = ServiceHelper.getBits(param[0], 4,2) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);

					// 风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					switch (param[1])
					{
						//吹头
						case 1:
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON;
							break;
						//吹头吹脚
						case 2:
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							break;
						//吹脚
						case 3:
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							break;
						//吹脚前除霜
						case 4:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							break;
						//前除霜
						case 5:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							break;
						default:
							break;
					}
					
					// 前窗除雾和后窗除雾
					//MaxFront
					if(param[2] == 7 && windDirInfo[0] == VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "1", false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "0", false);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					
					// 风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[]{param[2]}), false);

					// 空调温度信息
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.AC_TEMP_UNIT_NO,VehiclePropertyConstants.AC_TEMP_UNIT_NO}), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, ServiceHelper.floatArrayToString(new float[]{param[3],param[4]}), false);

					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_BesturnSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					 for(int i=0;i<param.length;i++)
					 {
						 param[i] = translateRadarDistance(param[i]);
					 }
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}

				break;
			case RZC_BesturnSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");
					for(int i=0;i<param.length;i++)
					 {
						 param[i] = translateRadarDistance(param[i]);
					 }
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
			case RZC_BesturnSeriesProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 1)
				{
					Log.i(TAG, "receive base info");
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
					
					//室外温度
					if(param[1] == 0xFE || param[1] == 0xFF)
					{
						param[1] = 0xFF;
					}
					param[1] = param[1] - 40;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_UNIT_PROPERTY, String.valueOf(VehiclePropertyConstants.AC_TEMP_UNIT_C), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.format("%.1f",param[1]*1.0f), false);
				}
				break;
			// 方向盘角度
			case RZC_BesturnSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length == 2)
				{
					int dir = ServiceHelper.getBit(param[0], 7);
					float angle = ((ServiceHelper.getBits(param[0], 0, 7) << 8) | param[1])*0.1f;
					if(dir == 0)
					{
						angle = -angle;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.format("%.1f", angle), false);
				}
				break;
			// 协议版本号
			case RZC_BesturnSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
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
	
	private int translateRadarDistance(int originDistance)
	{
		switch (originDistance)
		{
			case 4:
				return 0x01;
			case 3:
				return 0x02;
			case 2:
				return 0x03;
			case 1:
				return 0x04;
			case 0:
				return 0x00;
			default:
				break;
		}
		return 0;
	}
}
