package com.mct.carmodels;

import java.util.ArrayList;
import java.util.List;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.net.UrlQuerySanitizer.ParameterValuePair;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-日产英菲尼迪QX50车型协议管理
public class RZC_Nissan_Infiniti_QX50_Manager extends MctVehicleManager
{
	private static String TAG = "RZC-Infiniti-QX50";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_NISSAN_INIFINITI_QX50_L;
	private int mPressedKeyValue = 0;
	
	public RZC_Nissan_Infiniti_QX50_Manager(int carModel)
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
		return true;
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		mMcuManager = null;
		mCanMangaer = null;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_Nissan_Infiniti_QX50_Protocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_Nissan_Infiniti_QX50_Protocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_Nissan_Infiniti_QX50_Protocol.getProperityPermission(RZC_Nissan_Infiniti_QX50_Protocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_Nissan_Infiniti_QX50_Protocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_Nissan_Infiniti_QX50_Protocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_Nissan_Infiniti_QX50_Protocol.getProperityDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		if(mMcuManager == null || mCanMangaer == null)
		{
			Log.e(TAG, "McuManager is not ready");
			return false;
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Nissan_Infiniti_QX50_Protocol.HC_CMD_SWITCH, new int[]{0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Nissan_Infiniti_QX50_Protocol.HC_CMD_SWITCH,new int[]{0x00}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_Nissan_Infiniti_QX50_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{0x00,0x01}));
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_Nissan_Infiniti_QX50_Protocol.HC_CMD_REQ_CONTROL_INFO,new int[]{Integer.valueOf(value)}));
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
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_STEERING_WHEEL_KEY:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:"+param[0]+",status:"+param[1]);
					//按键操作
					//0 按键释放
					if(param[1] == 0 && mPressedKeyValue != RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE)
					{
						//低配特殊按键处理
						if(mCarModel == CarModelDefine.CAR_MODEL_NISSAN_INIFINITI_QX50_L)
						{
							if(mPressedKeyValue == RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_PICK_UP)
							{
								//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_PHONE_HANGUP), true);
								mService.broadcastKeyEvent(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(VehiclePropertyConstants.USER_KEY_PHONE_HANGUP), KeyEvent.ACTION_UP, 0);
								mPressedKeyValue = RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE;
								break;
							}
							else if(mPressedKeyValue == RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_SPEECH)
							{
								//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT), true);
								mService.broadcastKeyEvent(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT), KeyEvent.ACTION_UP, 0);
								mPressedKeyValue = RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE;
								break;
							}
						}
						mService.broadcastKeyEvent(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(RZC_Nissan_Infiniti_QX50_Protocol.swcKeyToUserKey(mPressedKeyValue)), true);
						mPressedKeyValue = RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE;
					}
					//1 按键按下
					else if(param[1] == 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					//2 长按,只针对PWR键处理
//					else if(param[1] == 2 && mPressedKeyValue == RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_POWER)
//					{
//						mPressedKeyValue = RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE;
//						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_LONG_PRESS_POWER_OFF), true);
//					}
				}
				break;
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_PANEL_KEY_INFO:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive panel key,Key:"+param[0]+",status:"+param[1]);
					//旋钮按键
					if(param[0] ==RZC_Nissan_Infiniti_QX50_Protocol.PANEL_KEY_SCROLL_VOLUME_UP ||
							param[0] == RZC_Nissan_Infiniti_QX50_Protocol.PANEL_KEY_SCROLL_VOLUME_DOWN ||
							param[0] == RZC_Nissan_Infiniti_QX50_Protocol.PANEL_KEY_SCROLL_TUNE_UP ||
							param[0] == RZC_Nissan_Infiniti_QX50_Protocol.PANEL_KEY_SCROLL_TUNE_DOWN)
					{
						if(param[1] > 0)
						{
							for(int i=0;i<param[1];i++)
							{
								mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(param[0])), true);
							}
						}
						mPressedKeyValue = RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE;
						return;
					}
					//按键操作
					//0 按键释放
					if(param[1] == 0 && mPressedKeyValue != RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE)
					{
						//mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(mPressedKeyValue)), true);
						mService.broadcastKeyEvent(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE;
					}
					//1 按键按下
					else if(param[1] == 1)
					{
						mService.broadcastKeyEvent(RZC_Nissan_Infiniti_QX50_Protocol.panelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
						mPressedKeyValue = param[0];
					}
					//2 长按,只针对PWR键处理
//					else if(param[1] == 2 && mPressedKeyValue == RZC_Nissan_Infiniti_QX50_Protocol.PANEL_KEY_POWER)
//					{
//						mPressedKeyValue = RZC_Nissan_Infiniti_QX50_Protocol.SWC_KEY_NONE;
//						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_LONG_PRESS_POWER_OFF), true);
//					}
				}
				break;
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_AIR_CONDITION_INFO:
				if(param != null && param.length == 5)
				{
					Log.i(TAG, "receive air condition info");
					if(param[1] == 0x00 || param[2] == 0x00)
					{
						Log.i(TAG, "AC is off");
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(VehiclePropertyConstants.COMMON_STATUS_OFF), true);
						//mService.broadcastAirConditionInfo(0);
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(VehiclePropertyConstants.COMMON_STATUS_ON), true);
					//制冷模式
					int[] acMode = new int[3];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON:VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON:VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON:VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), true);
					//循环模式
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(new int[]{ServiceHelper.getBits(param[0], 4, 2)}), true);
					//风向
					int[] windDirInfo = new int[]{
							VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF,
							VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF,
							VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF} ;
					int front_defrost_win = 0;
					switch (param[1])
					{
						case 1:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
							front_defrost_win = 0;
							break;
						case 2:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							front_defrost_win = 0;
							break;
						case 3:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							front_defrost_win = 0;
							break;
						case 4:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON;
							front_defrost_win = 1;
							break;
						case 5:
							windDirInfo[0] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
							windDirInfo[1] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
							windDirInfo[2] = VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
							front_defrost_win = 1;
							break;
						default:
							break;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), true);
					//风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[]{param[2]}), true);
					//左右温度
					float []interiorTemp = new float[]{param[3]/2.0f,param[4]/2.0f};
					interiorTemp[0] = interiorTemp[0] < RZC_Nissan_Infiniti_QX50_Protocol.AC_LOWEST_TEMP ? VehiclePropertyConstants.AC_TEMP_LO:interiorTemp[0] ;
					interiorTemp[0] = interiorTemp[0] > RZC_Nissan_Infiniti_QX50_Protocol.AC_HIGHEST_TEMP ? VehiclePropertyConstants.AC_TEMP_HI:interiorTemp[0] ;
					interiorTemp[1] = interiorTemp[1] < RZC_Nissan_Infiniti_QX50_Protocol.AC_LOWEST_TEMP ? VehiclePropertyConstants.AC_TEMP_LO:interiorTemp[1] ;
					interiorTemp[1] = interiorTemp[1] > RZC_Nissan_Infiniti_QX50_Protocol.AC_HIGHEST_TEMP ? VehiclePropertyConstants.AC_TEMP_HI:interiorTemp[1] ;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), true);
					//前后窗除雾
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(front_defrost_win), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), true);
					
					//请求显示空调信息
					//mService.broadcastAirConditionInfo(1);
					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_FRONT_RADAR_INFO:
				if(param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), true);
				}
				break;
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_REAR_RADAR_INFO:
				if(param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), true);
				}
				break;
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_ODOMETER_INFO:
				if(param != null && param.length == 7)
				{
					Log.i(TAG, "receive odometer info");
					float totalOdometer = (param[4]*256*256*256+param[3]*256*256+param[2]*256+param[1])*0.1f;
					float rmngOdometer = (param[6]*256+param[5])*0.1f;
					//MILE->KM
//					if(ServiceHelper.getBit(param[0], 0) == 1)
//					{
//						totalOdometer *= ServiceHelper.KM_PER_MILE ;
//						rmngOdometer *= ServiceHelper.KM_PER_MILE ;
//					}
					//里程单位
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ODOMETER_UNIT_PROPERTY, String.valueOf(
							ServiceHelper.getBit(param[0], 0) == 0 ? VehiclePropertyConstants.ODOMETER_UNIT_KM:VehiclePropertyConstants.ODOMETER_UNIT_MILE), true);
					//总里程
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY,String.format("%.1f", totalOdometer) , true);
					//剩余续航里程
					mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY,String.format("%.1f", rmngOdometer) , true);
				}
				break;
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_BASE_INFO:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive base info");
					int flStatus = ServiceHelper.getBit(param[0],6);
					int frStatus = ServiceHelper.getBit(param[0],7);
					int rlStatus = ServiceHelper.getBit(param[0],4);
					int rrStatus = ServiceHelper.getBit(param[0],5);
					int trunkStatus = ServiceHelper.getBit(param[0],3);
					int hoodStatus = ServiceHelper.getBit(param[0],2);
					mService.broadcastVehicleDoorInfo(flStatus, frStatus, rlStatus, rrStatus, trunkStatus, hoodStatus);
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY, String.valueOf(flStatus), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY, String.valueOf(frStatus), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY, String.valueOf(rlStatus), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY, String.valueOf(rrStatus), true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY, String.valueOf(trunkStatus), true);
					//引擎盖
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY, String.valueOf(hoodStatus), true);
				}
				break;
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_TRIP_INFO:
				if(param != null && param.length == 5)
				{
					Log.i(TAG, "receive trip info");
					int instantFuelConsumeUnit = ServiceHelper.getBits(param[0], 0,2);
					if(instantFuelConsumeUnit == 0)
					{
						instantFuelConsumeUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_MPG;
					}
					else if(instantFuelConsumeUnit == 2)
					{
						instantFuelConsumeUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_L_100KM;
					}
					else if(instantFuelConsumeUnit == 1)
					{
						instantFuelConsumeUnit = VehiclePropertyConstants.FUEL_CONSUMPTION_UNIT_KM_L;
					}
					else {
						instantFuelConsumeUnit = -1;
					}
					if(instantFuelConsumeUnit > 0)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(instantFuelConsumeUnit), true);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_DRIVING_FUEL_CONSUMPTION_PROPERTY, String.valueOf((param[4]*256+param[3])*0.1f), true);
					}
					
				}
				break;
			//方向盘角度
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive steering-wheel info");
					int 	dir 	= ServiceHelper.getBit(param[0], 7);
					float angle	= ServiceHelper.MAKEWORD( param[1],ServiceHelper.getBits(param[0], 0, 7));
					//向左
					if(dir == 0)
					{
						angle = -angle;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), true);
				}
				break;
			//协议版本号，验证暂不支持
			case RZC_Nissan_Infiniti_QX50_Protocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if(param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param,0,param.length,"UTF-8");
					Log.i(TAG, "receive protocol version info:"+version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,version , true);
				}
				break;
			default:
				break;
		}
	}
}
