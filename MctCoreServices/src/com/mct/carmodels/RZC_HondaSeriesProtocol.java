package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_HondaSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_TUNER_CUR_FREQ				= 0x03;//原车收音当前频点
	public static final int CH_CMD_TUNER_PRESET_INFO			= 0x04;//原车收音当前频点
	public static final int CH_CMD_TUNER_STORE_LIST				= 0x07;//原车收音存储列表
	public static final int CH_CMD_BACKLIGHT_INFO 				= 0x14;
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x20;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x21;//空调信息(15CRV_L/12CRV为原车多媒体信息)
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x22;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x23;
	public static final int CH_CMD_BASE_INFO 							= 0x24;
	public static final int CH_CMD_PARKING_STATUS_INFO 		= 0x25;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x29;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x30;
	public static final int CH_CMD_VEHICLE_SET_INFO				= 0x32;
	public static final int CH_CMD_TRIP_ODOMETER_INFO		= 0x33;
	public static final int CH_CMD_RET_DRIVE_SET_STATUS		= 0xD0;
	public static final int CH_CMD_VEHICLE_CAMERA					= 0xD1;//原车摄像头(15CRV_L/12CRV为时间设置)
	public static final int CH_CMD_COMPASS_STATUS				= 0xD2;//指南针状态
	public static final int CH_CMD_VEHICLE_BLUTOOTH_CONTROL = 0xD3;//原车从机蓝牙指令

	
	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_CUR_SOURCE						= 0x82;//主机当前源信息
	public static final int HC_CMD_RADIO_CONTROL   				= 0x83;//收音机操作命令
	public static final int HC_CMD_VEHICLE_SET						= 0xC6;//车辆设置命令
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0x90;
	public static final int HC_CMD_SOURCE								= 0xC0;
	public static final int HC_CMD_RADIO_INFO							= 0xC2;
	public static final int HC_CMD_MEDIA_INFO							= 0xC3;
	public static final int HC_CMD_VOLUME								= 0xC4;
	public static final int HC_CMD_VEHICLE_MEDIA					= 0xC5;//原车多媒体控制(15CRV_L/12CRV支持)
	public static final int HC_CMD_SYNC_VEHICLE_TIME			= 0xC6;//同步原车时间(15CRV_L/12CRV支持)
	public static final int HC_CMD_COMPLASS_HANDLE				= 0xC9;//指南针操作(15CRV_L/12CRV支持)
	public static final int HC_CMD_BLUETOOTH_HANDLE			= 0xCA;//蓝牙操作指令(15CRV_L/12CRV支持)
	public static final int HC_CMD_PHONE_AND_MESSAGE			= 0xCB;//电话及信息指令
	public static final int HC_CMD_BLUETOOTH_KEY					= 0xCC;//主机蓝牙按键指令(15CRV_L/12CRV支持)
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量键
	public static final int SWC_KEY_MENU_RIGHT						= 0x03;//方向键-右
	public static final int SWC_KEY_MENU_LEFT						= 0x04;//方向键-左
	public static final int SWC_KEY_SRC									= 0x07;//SOURCE
	public static final int SWC_KEY_PICKUP								= 0x09;//PICKUP
	public static final int SWC_KEY_HANGUP								= 0x0A;//HANGUP
	public static final int SWC_KEY_SPEECH								= 0x08;//语音
	public static final int SWC_KEY_MENU_UP							= 0x13;//方向键-上
	public static final int SWC_KEY_MENU_DOWN						= 0x14;//方向键-下
	public static final int SWC_KEY_MENU_ENTER						= 0x16;//方向键-OK
	public static final int SWC_KEY_MENU									= 0x17;//MENU 15款锋范和16款凌派当作mute键处理
	public static final int SWC_KEY_DISP									= 0x18;//DISP
	public static final int SWC_KEY_CAMERA								= 0x29;//右视摄像头切换,歌诗图车型才有
	public static final int SWC_KEY_MUTE									= 0x30;
	public static final int SWC_TOUCH_KEY_VOLUME_UP			= 0x81;//16款思域
	public static final int SWC_TOUCH_KEY_VOLUME_DOWN		= 0x82;//16款思域
	public static final int SWC_TOUCH_KEY_MUTE						= 0x86;//16款思域
	public static final int SWC_KEY_TURN_RIGHT_CAMERA2		= 0xAA;//右视摄像头切换,歌诗图车型才有
	
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_RIGHT, VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_LEFT, VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PICKUP, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_HANGUP, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SPEECH, VehiclePropertyConstants.USER_KEY_VOICE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DISP, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_CAMERA, VehiclePropertyConstants.USER_KEY_CAMERA);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_TOUCH_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_TOUCH_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_TOUCH_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TURN_RIGHT_CAMERA2, VehiclePropertyConstants.USER_KEY_TURN_RIGHT_CAMERA);
	}
	
	
	public static final float AC_LOWEST_TEMP 	= 0.5f;//最低温度
	public static final float AC_HIGHEST_TEMP 	= 127;//最高温度
	
	//所有支持的属性集
	public static final int[] VEHICLE_CAN_PROPERITIES = new int[] { 
			VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY,
			VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY,
			VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY,
			VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,
			VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY,
			VehicleInterfaceProperties.VIM_KEY_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,
		
			VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,
			
			VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY,
			VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY,
			VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY,
			VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY,
			VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY,
			VehicleInterfaceProperties.VIM_INTERIOR_TEMP_PROPERTY,
			VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY,
			
			VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,
			VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,
			
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY,
			
			VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY,
			VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,
	};	
		
	//CRV低配
	public static final int[] VEHICLE_CAN_PROPERITIES_CRV = new int[] { 
			VehicleInterfaceProperties.VIM_COMPASS_ANGLE_PROPERTY,
			VehicleInterfaceProperties.VIM_COMPASS_CALIBRATE_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_COMPASS_REGION_PROPERTY
	};	
		
	// 属性读写权限表
	public static final SparseIntArray VEHICLE_CAN_PROPERITY_PERMISSION_TABLE = new SparseIntArray();
	static
	{
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY, PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY, PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY,PROPERITY_PERMISSON_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_KEY_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,PROPERITY_PERMISSON_NO);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_INTERIOR_TEMP_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY,PROPERITY_PERMISSON_NO);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY, PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_COMPASS_ANGLE_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_COMPASS_CALIBRATE_STATUS_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_COMPASS_REGION_PROPERTY,PROPERITY_PERMISSON_GET);
	}	

	// 属性值类型表
	public static final SparseIntArray VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE = new SparseIntArray();
	static
	{
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_KEY_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_INTERIOR_TEMP_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY,VehiclePropertyConstants.DATA_TYPE_FLOAT);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_COMPASS_ANGLE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_COMPASS_CALIBRATE_STATUS_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_COMPASS_REGION_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
	}	
	
	public static int getProperityDataType(int prop)
	{
		return (VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.indexOfKey(prop) >= 0) ? VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.get(prop) : -1;
	}

	public static int getProperityPermission(int prop)
	{
		return (VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.indexOfKey(prop) >= 0) ? VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.get(prop) : -1;
	}
	
	public static int swcKeyToUserKey(int swcKey)
	{
		return (SWC_KEY_TO_USER_KEY_TABLE.indexOfKey(swcKey) >= 0) ? SWC_KEY_TO_USER_KEY_TABLE.get(swcKey):-1;
	}
		
}
