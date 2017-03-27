package com.mct.carmodels;

import java.util.ArrayList;
import java.util.List;

import com.android.internal.app.LocalePicker.LocaleSelectionListener;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.app.Service;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-丰田车系车型协议管理
public class RZC_ToyotaSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC-Toyota";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_TOYOTA_RAV4;
	private int mPressedKeyValue = 0;
	private int mRadarEnable = 1;
	private boolean mSupportPanoramic = true;//是否支持全景
	private boolean mPanoramicStatus = false;//全景状态
	
	public RZC_ToyotaSeriesManager(int carModel)
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
		return true;
	}

	private void initVehicleConfig()
	{
		// 初始化雷达距离范围
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "4");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4}), false);
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE, "1");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MIN, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MAX, "63");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MIN, "-5");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MAX, "5");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MIN, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MAX, "14");						
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
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
		if(mSupportPanoramic)
		{
			return ServiceHelper.combineArray(new int[]{VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_PATH_VIEW_MODE_PROPERTY,
					VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY}, RZC_ToyotaSeriesProtocol.VEHICLE_CAN_PROPERITIES);
		}
		return RZC_ToyotaSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_ToyotaSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_ToyotaSeriesProtocol.getProperityPermission(RZC_ToyotaSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_ToyotaSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_ToyotaSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_ToyotaSeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_SWITCH, new int[]{0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_SWITCH,new int[]{0x00}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_AIR_CONDITION_INFO}));
							//仅皇冠
							if(mCarModel == CarModelDefine.CAR_MODEL_TOYOTA_CROWN)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_AIR_CONDITION_INFO2}));
							}
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_TRIP_PER_MINUTE_INFO}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_INSTANT_TRIP_INFO}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_HISTORY_TRIP_INFO}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_OIL_ELEC_MIXED_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							//请求前后雷达信息
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_FRONT_RADAR_INFO}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_REAR_RADAR_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_TPMS_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_BASE_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_OIL_ELEC_MIX_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_OIL_ELEC_MIXED_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_SETTING:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_VEHICLE_SETTING}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{RZC_ToyotaSeriesProtocol.CH_CMD_VEHICLE_SET_STATUS_INFO}));
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new int[]{Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SPEED_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x00,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SHIFT_FROM_P_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x01,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SHIFT_TO_P_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x02,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_REMOTE_DOUBLE_PRESS_LOCK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x03,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_DAYTIME_RUNNING_LIGHT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x04,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_LOCK_UNLOCK_FEEDBACK_TONE_LOCKPROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x05,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_HEADLAMPS_ON_SENSITIVITY_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x06,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x07,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_INTERNEL_LIGHT_AUTO_OFF_TIME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x0C,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_EXTERNEL_LIGHT_AUTO_OFF_TIMER_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x0C,Integer.valueOf(value)+4}));
					break;
				case VehicleInterfaceProperties.VIM_DOUBLE_PRESS_KEY_LOCK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x0D,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_DRIVER_DOOR_SYNC_UNLOCK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x0E,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_SMART_DOOR_UNLOCK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x0F,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_SMART_LOCK_AND_ONE_KEY_STARTUP_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x10,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_LOCK_UNLOCK_FLASH_ALERT_LIGHTS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x11,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_SYNC_WITH_AUTO_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x12,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VENTILATE_SYNC_WITH_AUTO_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x13,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_RELOCK_TIMER_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x14,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x15,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DISPLAY_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x16,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_DETECT_DISTANCE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x17,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_DETECT_DISTANCE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x17,Integer.valueOf(value)+2}));
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_SWITCH_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x20}));
					break;
				case VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY:
				case VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_PATH_VIEW_MODE_PROPERTY:
					int[] point = ServiceHelper.stringToIntArray(value);
					if(point != null && point.length == 3 && point[2] == 1)
					{
						//丰田RAV4荣放车型上车时没测到有左上角与右上角按钮
/*
						//左上角
						if(point[0] >= 0 && point[0] <= 100 &&
								point[1] >= 0 && point[1] <= 100)
						{
							//全景模式下
							if(mPanoramicStatus)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x21,0x01}));
							}
							
						}
						//右上角
						else if(point[0] >= 900 && point[0] <= 1024 &&
								point[1] >= 0 && point[1] <= 100)
						{
							//倒车模式下
							if(mMcuManager.isInReverseStatus())
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x21,0x06}));
							}
						}
						*/
						//左下角
						if(point[0] >= 55 && point[0] <= 200 &&
								point[1] >= 512 && point[1] <= 590)
						{
							//倒挡模式下
							if(mMcuManager.isInReverseStatus())
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x21,0x04}));
							}
							else if(mPanoramicStatus)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x21,0x02}));
							}
						}
						//右下角
						else if(point[0] >= 855 && point[0] <= 1000 &&
								point[1] >= 512 && point[1] <= 590)
						{
							//仅倒挡模式下有效
							if(mMcuManager.isInReverseStatus())
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x21,0x03}));
							}
						}
						//右中下角
						else if(point[0] >= 560 && point[0] <= 708 &&
								point[1] >= 512 && point[1] <= 590)
						{
							//仅全景模式下有效
							if(mPanoramicStatus)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x21,0x07}));
							}
						}
						//mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x21,Integer.valueOf(value)}));
					}
					break;
				case VehicleInterfaceProperties.VIM_CAMERA_PATH_TYPE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x22,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AUTO_REAR_DOOR_OPEN_LEVEL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x23,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_COLOR_THEME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_VEHICLE_SET,new int[]{0x23,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x07,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x02,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x01,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x04,7+Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x05,7+Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x06,7+Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_POWER_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x08,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_BOSE_CENTERPOINT_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_ToyotaSeriesProtocol.HC_CMD_COMMON_SET,new int[]{0x09,Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_STATUS_PROPERTY:
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
	
	private float getFuelConsumValue(int param1,int param2)
	{
		if(param1 == 0xFF && param2 == 0xFF)
		{
			return 0;
		}
		return 0.1f*(param1*256+param2);
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
			case RZC_ToyotaSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:"+param[0]+",status:"+param[1]);
					//旋钮按键
					if(param[0] >= RZC_ToyotaSeriesProtocol.SCROLL_KEY_VOLUME_UP && param[0] <= RZC_ToyotaSeriesProtocol.SCROLL_KEY_TUNE_TRACK_DOWN)
					{
						if(param[1] > 0)
						{
							for(int i=0;i<param[1];i++)
							{
								mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(RZC_ToyotaSeriesProtocol.swcKeyToUserKey(param[0])), true);
							}
						}
						mPressedKeyValue = RZC_ToyotaSeriesProtocol.SWC_KEY_NONE;
						return;
					}
					//按键操作
					//0 按键释放
					if(param[1] == 0 && mPressedKeyValue != RZC_ToyotaSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_ToyotaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);		
						mPressedKeyValue = RZC_ToyotaSeriesProtocol.SWC_KEY_NONE;
					}
					//1 按键按下
					else if(param[1] == 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_ToyotaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					//2 长按,只针对PWR键处理
//					else if(param[1] == 2 && mPressedKeyValue == RZC_ToyotaSeriesProtocol.SWC_KEY_POWER)
//					{
//						mPressedKeyValue = RZC_ToyotaSeriesProtocol.SWC_KEY_NONE;
//						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_LONG_PRESS_POWER_OFF), true);
//					}
				}
				break;
			//皇冠车型才支持
			case RZC_ToyotaSeriesProtocol.CH_CMD_AC_PANEL_KEY_INFO:
				if(param != null && param.length == 2 && mCarModel == CarModelDefine.CAR_MODEL_TOYOTA_CROWN)
				{
					Log.i(TAG, "receive ac panel key,Key:"+param[0]+",status:"+param[1]);
					//旋钮按键
					if(param[0] >= RZC_ToyotaSeriesProtocol.SCROLL_KEY_VOLUME_UP && param[0] <= RZC_ToyotaSeriesProtocol.SCROLL_KEY_TUNE_TRACK_DOWN)
					{
						if(param[1] > 0)
						{
							for(int i=0;i<param[1];i++)
							{
								mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(RZC_ToyotaSeriesProtocol.swcKeyToUserKey(param[0])), true);
							}
						}
						mPressedKeyValue = RZC_ToyotaSeriesProtocol.SWC_KEY_NONE;
						return;
					}
					//按键操作
					//0 按键释放
					if(param[1] == 0 && mPressedKeyValue != RZC_ToyotaSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_ToyotaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);		
						mPressedKeyValue = RZC_ToyotaSeriesProtocol.SWC_KEY_NONE;
					}
					//1 按键按下
					else if(param[1] == 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_ToyotaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					//2 长按,只针对PWR键处理
//					else if(param[1] == 2 && mPressedKeyValue == RZC_ToyotaSeriesProtocol.SWC_KEY_POWER)
//					{
//						mPressedKeyValue = RZC_ToyotaSeriesProtocol.SWC_KEY_NONE;
//						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_LONG_PRESS_POWER_OFF), true);
//					}
				}
				break;
			//每分钟油耗
			case RZC_ToyotaSeriesProtocol.CH_CMD_TRIP_PER_MINUTE_INFO:
				if(param != null && param.length == 7)
				{
					Log.i(TAG, "receive trip info per minute");
					
					//当前行驶时间(分钟)
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_CUR_PROPERTY, String.valueOf(param[2]*256+param[3]), false);
					float avgVehicleSpeed = (param[0]*256+param[1])*0.1f;
					float rmngDrivingRange = param[4]*256+param[5]; 
					int odometerUnit = param[6];
//					if(odometerUnit == 1)
//					{
//						//mile->km
//						avgVehicleSpeed *= ServiceHelper.KM_PER_MILE;
//						rmngDrivingRange *= ServiceHelper.KM_PER_MILE;
//					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ODOMETER_UNIT_PROPERTY, String.valueOf(odometerUnit), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_VECHILE_SPEED, String.format("%.2f", avgVehicleSpeed), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY,String.format("%.2f", rmngDrivingRange),false);
				}
				break;
			//过去15分钟油耗
			case RZC_ToyotaSeriesProtocol.CH_CMD_TRIP_BEFORE_15_MINUTE:
				if(param != null && param.length == 0x1F)
				{
					Log.i(TAG, "receive before 15 minute fuel consumption info");
					int unit = param[0];
					if(unit == 0)
					{
						unit = 2;
					}
					else if(unit == 2)
					{
						unit = 0;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FUEL_CONSUMPTION_UNIT_15_MINUES_PROPERTY, String.valueOf(unit), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FUEL_CONSUMPTION_15_MINUES_PROPERTY, ServiceHelper.floatArrayToString(
							new float[]{getFuelConsumValue(param[1],param[2]),
									getFuelConsumValue(param[3],param[4]),getFuelConsumValue(param[5],param[6]),
									getFuelConsumValue(param[7],param[8]),getFuelConsumValue(param[9],param[10]),
									getFuelConsumValue(param[11],param[12]),getFuelConsumValue(param[13],param[14]),
									getFuelConsumValue(param[15],param[16]),getFuelConsumValue(param[17],param[18]),
									getFuelConsumValue(param[19],param[20]),getFuelConsumValue(param[21],param[22]),
									getFuelConsumValue(param[23],param[24]),getFuelConsumValue(param[25],param[26]),
									getFuelConsumValue(param[27],param[28]),getFuelConsumValue(param[29],param[30])}), false);
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_INSTANT_TRIP_INFO:
				if(param != null && param.length == 3)
				{
					Log.i(TAG, "receive instant trip info");
					//瞬时油耗计量单位
					int instUnit = param[0];
					if(instUnit == 0)
					{
						instUnit = 2;
					}
					else if(instUnit == 2)
					{
						instUnit = 0;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(instUnit), false);
					//瞬时油耗
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY, String.valueOf((param[1]*256+param[2])*0.1f), false);
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_HISTORY_TRIP_INFO:
				if(param != null && param.length == 0x0D)
				{
					Log.i(TAG, "receive history trip info");
					//历史油耗计量单位
					int historyUnit = param[0];
					if(historyUnit == 0)
					{
						historyUnit = 2;
					}
					else if(historyUnit == 2)
					{
						historyUnit = 0;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HISTORY_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(historyUnit), true);				
					
					//当前行程油耗
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HISTORY_FUEL_CONSUMPTION_PROPERTY, ServiceHelper.floatArrayToString(new float[]{
							getFuelConsumValue(param[1], param[2]),getFuelConsumValue(param[3], param[4]),
							getFuelConsumValue(param[5], param[6]),getFuelConsumValue(param[7], param[8]),
							getFuelConsumValue(param[9], param[10]),getFuelConsumValue(param[11], param[12])}), false);				
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_BASE_INFO:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive base info");
					int flStatus = ServiceHelper.getBit(param[0],6);
					int frStatus = ServiceHelper.getBit(param[0],7);
					int rlStatus = ServiceHelper.getBit(param[0],4);
					int rrStatus = ServiceHelper.getBit(param[0],5);
					int trunkStatus = ServiceHelper.getBit(param[0],3);
					int hoodStatus = 0;
					mService.broadcastVehicleDoorInfo(flStatus, frStatus, rlStatus, rrStatus, trunkStatus, hoodStatus);
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY, String.valueOf(flStatus), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY, String.valueOf(frStatus), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY, String.valueOf(rlStatus), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY, String.valueOf(rrStatus), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY, String.valueOf(trunkStatus), false);
					//电动后门
					int rearDoorStatus = ServiceHelper.getBit(param[0], 2);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_PROPERTY, String.valueOf(rearDoorStatus), false);
					if(rearDoorStatus == 1)
					{
						//电动后门运行方向
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_REAR_DIRECTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					}
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_TPMS_INFO:
				if(param != null && param.length == 6)
				{
					Log.i(TAG, "receive tpms info");
					int tpmsDevice = ServiceHelper.getBit(param[0], 7);	
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_TPMS_DEVICE_EXIST_PROPERTY, String.valueOf(tpmsDevice), false);
					//有胎压设备
					if(tpmsDevice == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_TPMS_UI_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 6)), false);
						int isShowBackTair = ServiceHelper.getBit(param[0], 5);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_SHOW_BACK_TIRE_PRESSURE_PROPERTY, String.valueOf(isShowBackTair), false);
						int showWay = ServiceHelper.getBit(param[0], 2);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_TPMS_DISPLAY_MODE_PROPERTY, String.valueOf(showWay), false);
						
						//显示为一辆车时的形式时才有各轮胎数据
						//if(showWay == 1)
						{
							float deltaValue = 0.0f;
							int unit = ServiceHelper.getBits(param[0], 0,2);
							switch(unit)
							{
								//0.1bar
								case 0:
									deltaValue = 0.1f;
									break;
								//PSI
								case 1:
									deltaValue = 1.0f;
									break;
								//2.5KPA
								case 2:
									deltaValue = 2.5f;
									break;
								default:
									return;
							}
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_UNIT_PROPERTY, String.valueOf(unit), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_FRONT_LEFT_PROPERTY, String.format("%.1f",param[1]*deltaValue),false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_FRONT_RIGHT_PROPERTY, String.format("%.1f",param[2]*deltaValue), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_REAR_LEFT_PROPERTY, String.format("%.1f",param[3]*deltaValue), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_REAR_RIGHT_PROPERTY, String.format("%.1f",param[4]*deltaValue), false);
							if(isShowBackTair == 1)
							{
								mService.cachePropValue(VehicleInterfaceProperties.VIM_TIRE_PRESSURE_BACK_PROPERTY, String.format("%.1f",param[5]*deltaValue), false);
							}
						}
					}
					else 
					{
						Log.w(TAG, "no tpms devices");
					}
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_AIR_CONDITION_INFO:
				if(param != null && param.length == 5)
				{
					Log.i(TAG, "receive air condition info");
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(acStatus), true);
					if(acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						//mService.broadcastAirConditionInfo(0);
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
					int[] acMode = new int[4];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON:VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON:VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 4) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO2_ON:VehiclePropertyConstants.AC_COOL_MODE_AUTO2_OFF;
					acMode[3] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON:VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);
					//循环模式
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(new int[]{ServiceHelper.getBit(param[0], 5)}), false);
					//风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = ServiceHelper.getBit(param[1], 7) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[1], 5) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					//风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[]{ServiceHelper.getBits(param[1], 0, 4)}), false);
					//左右温度
					int tempUnit = ServiceHelper.getBit(param[4], 0) == 0 ? VehiclePropertyConstants.AC_TEMP_UNIT_C:VehiclePropertyConstants.AC_TEMP_UNIT_F;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{tempUnit,tempUnit}), false);
					float []interiorTemp = new float[2];
					if(tempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_C)
					{
						//左温度
						if(param[2] == 0x00)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if(param[2] == 0x1F)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else if(param[2] >= 0x20 && param[2] <= 0x23)
						{
							interiorTemp[0] = 16+(param[2]-0x20);
						}
						else 
						{
							interiorTemp[0] = (float)Math.max(RZC_ToyotaSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_ToyotaSeriesProtocol.AC_HIGHEST_TEMP, RZC_ToyotaSeriesProtocol.AC_LOWEST_TEMP+(param[2]-1)*0.5));
						}
						//右温度
						if(param[3] == 0x00)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if(param[3] == 0x1F)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else if(param[3] >= 0x20 && param[3] <= 0x23)
						{
							interiorTemp[1] = 16+(param[3]-0x20);
						}
						else 
						{
							interiorTemp[1] = (float)Math.max(RZC_ToyotaSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_ToyotaSeriesProtocol.AC_HIGHEST_TEMP, RZC_ToyotaSeriesProtocol.AC_LOWEST_TEMP+(param[3]-1)*0.5));
						}
					}
					else
					{
						//左温度
						if(param[2] == 0x00)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if(param[2] == 0xFF)
						{
							interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else 
						{
							interiorTemp[0] = param[2];
						}
						//右温度
						if(param[3] == 0x00)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
						}
						else if(param[3] == 0xFF)
						{
							interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
						}
						else 
						{
							interiorTemp[1] = param[3];
						}
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
					//前后窗除雾
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 6)), false);
					if(showAcBar == 1)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						mCanMangaer.showAirConditionEvent(true);
					}
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_AMPLIFIER_STATUS_INFO:
				if(param != null && param.length  == 5)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY, String.valueOf(param[3]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 0, 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 4, 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 4, 4)-7), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 4, 4)-7), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 0, 4)-7), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_STATUS_PROPERTY, String.valueOf(param[4]), false);
				}
				break;
				//方向盘角度
			case RZC_ToyotaSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive steering-wheel info");
					int dir = ServiceHelper.getBit(param[1], 3);
					int angle = 0;
					//向左
					if(dir == 0)
					{
						angle = -(param[0]+(0xFF+1) * ServiceHelper.getBits(param[1], 0,4));
					}
					//向右
					else 
					{
						angle = (0xFF+1)-param[0]+(0xF - ServiceHelper.getBits(param[1], 0,4))*(0xFF+1);
					}
					angle = Math.max(-380, Math.min(380, angle));
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			//2014霸道车型才支持
			case RZC_ToyotaSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if(param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");
					if(mRadarEnable == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
					}
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if(param != null && param.length == 5)
				{
					Log.i(TAG, "receive rear radar info");
					mRadarEnable = ServiceHelper.getBit(param[4], 5);
					if(mRadarEnable == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(new int[]{param[0],param[1],param[2],param[3]}), false);
					}
					int radarVolume = ServiceHelper.getBits(param[4], 0,3);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY, radarVolume == 0 ? "0":"1", false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_VOLUME_PROPERTY, String.valueOf(radarVolume), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_SWITCH_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_DETECT_DISTANCE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DISPLAY_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 7)), false);
				}
				break;
			//皇冠车型-空调信息
			case RZC_ToyotaSeriesProtocol.CH_CMD_AIR_CONDITION_INFO2:
				if(param != null && param.length == 5 && mCarModel == CarModelDefine.CAR_MODEL_TOYOTA_CROWN)
				{
					Log.i(TAG, "receive CROWN air condition info");
					//座椅加热
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 4, 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 0, 4)), false);
					//风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = ServiceHelper.getBit(param[1], 7) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[1], 5) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON:VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					//请求显示空调信息
					if( ServiceHelper.getBit(param[1], 4) == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, String.valueOf(VehiclePropertyConstants.USER_KEY_AIR_CONDITION), false);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					//风量大小
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[]{ServiceHelper.getBits(param[1], 0, 4)}), false);
					//座椅吹风
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DRIVER_SEAT_FAN_LEVEL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 4, 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_PSNGR_SEAT_FAN_LEVEL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0, 4)), false);
				}
				break;
			//油电混合信息
			case RZC_ToyotaSeriesProtocol.CH_CMD_OIL_ELEC_MIXED_INFO:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive oil-elec mixed vehicle info");
					int batteryLevel = ServiceHelper.getBits(param[0], 0, 3);
					int fuelElecVehicle = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_OIL_ELECT_MIX_SUPPORT_PROPERTY, String.valueOf(fuelElecVehicle), false);
					//油电混合车
					if(fuelElecVehicle == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_BATTERY_POWER_LEVEL_PROPERTY, String.valueOf(batteryLevel), false);
						int []mode = new int[6];
						//马达驱动电池
						mode[0] = ServiceHelper.getBit(param[1], 0);
						//马达驱动车轮
						mode[1] = ServiceHelper.getBit(param[1], 1);
						//发动机驱动马达
						mode[2] = ServiceHelper.getBit(param[1], 2);
						//发动机驱动车轮
						mode[3] = ServiceHelper.getBit(param[1], 3);
						//电池驱动马达
						mode[4] = ServiceHelper.getBit(param[1], 4);
						//车轮驱动马达
						mode[5]= ServiceHelper.getBit(param[1], 5);
						Log.i(TAG, "OIL_ELEC_MODE:"+VehicleManager.intArrayToString(mode));
						mService.cachePropValue(VehicleInterfaceProperties.VIM_OIL_ELECT_MIX_DRIVE_MODE_PROPERTY, ServiceHelper.intArrayToString(mode), false);
					}
					
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_VEHICLE_SETTING:
				if(param != null && param.length == 4)
				{
					Log.i(TAG, "receive vehicle setting info");
					//灯光设置
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DAYTIME_RUNNING_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEADLAMPS_ON_SENSITIVITY_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 4,3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_INTERNEL_LIGHT_AUTO_OFF_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 2,2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 0,2)), false);
					//车锁设置
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SPEED_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SHIFT_FROM_P_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SHIFT_TO_P_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_DOUBLE_PRESS_LOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LOCK_UNLOCK_FEEDBACK_TONE_LOCKPROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 0,3)), false);
					//车锁设置2
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOUBLE_PRESS_KEY_LOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2],7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DRIVER_DOOR_SYNC_UNLOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2],6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SMART_DOOR_UNLOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2],5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SMART_LOCK_AND_ONE_KEY_STARTUP_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2],4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LOCK_UNLOCK_FLASH_ALERT_LIGHTS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2],3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_REAR_DOOR_OPEN_LEVEL_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2],0,3)), false);
					//空调设置
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_SYNC_WITH_AUTO_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3],7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VENTILATE_SYNC_WITH_AUTO_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3],6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_PATH_TYPE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[3],2,2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_RELOCK_TIMER_PROPERTY, String.valueOf(ServiceHelper.getBits(param[3],0,2)), false);
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_VEHICLE_SET_STATUS_INFO:
				if(param != null && param.length == 2)
				{
					Log.i(TAG, "receive vehicle set status info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERNEL_LIGHT_AUTO_OFF_TIMER_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 0,2)), false);
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_VECHILE_SET_RETURN_INFO:
				if(param != null && param.length == 3)
				{
					Log.i(TAG, "receive vehicle set return info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_DETECT_DISTANCE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_FUEL_CONSUMPTION_UNIT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_COLOR_THEME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 0,3)), false);
				}
				break;
			case RZC_ToyotaSeriesProtocol.CH_CMD_SYSTEM_INFO:
				if(param != null && param.length == 1)
				{
					Log.i(TAG, "receive system info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_DEVICE_NODE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_DEVICE_NODE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					int supportCameraPanoramic = ServiceHelper.getBit(param[0], 2);
					//默认支持,兼容旧版本
					//mSupportPanoramic = (supportCameraPanoramic == 0 ? false : true);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_DEVICE_NODE_PROPERTY, String.valueOf(supportCameraPanoramic), false);
					
					int enableCameraPreview = ServiceHelper.getBit(param[0],3);
					mPanoramicStatus = (enableCameraPreview == 0 ? false:true);
					String strCameraPreview = mService.getCacheValue(VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_SWITCH_PROPERTY);
					Log.i("Terris-Debug", "Camera old:"+strCameraPreview+",current:"+enableCameraPreview);
					
					if(strCameraPreview == null || Integer.valueOf(strCameraPreview) != enableCameraPreview)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_CAMERA_PANORAMIC_SWITCH_PROPERTY, String.valueOf(enableCameraPreview), false);
						mService.broadcastCameraPreview(enableCameraPreview);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_POWER_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUDIO_CONTROL_MUTE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
				}
				
				break;
				//协议版本
			case RZC_ToyotaSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
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
}

