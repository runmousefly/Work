package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_Nissan_Infiniti_QX50_Protocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x20;
	public static final int CH_CMD_AIR_CONDITION_INFO 		= 0x21;//空调信息
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x22;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x23;
	public static final int CH_CMD_PANEL_KEY_INFO 				= 0x24;//面板按键信息
	public static final int CH_CMD_LIGHT_WSHIELD_INFO 		= 0x24;//灯光和雨刷信息
	public static final int CH_CMD_SPEED_INFO 						= 0x26;//车速信息
	public static final int CH_CMD_ODOMETER_INFO 				= 0x27;//里程信息
	public static final int CH_CMD_BASE_INFO 							= 0x28;//基本信息
	public static final int CH_CMD_TRIP_INFO 							= 0x29;//油耗信息
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x30;
	public static final int CH_CMD_VEHICLE_SET1_INFO			= 0x71;
	public static final int CH_CMD_VEHICLE_SET2_INFO			= 0x72;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x7F;

	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0x83;
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量键
	public static final int SWC_KEY_MENU_UP							= 0x03;//方向键-上
	public static final int SWC_KEY_MENU_DOWN						= 0x04;//方向键-下
	public static final int SWC_KEY_SRC									= 0x07;//源切换
	public static final int SWC_KEY_PICK_UP								= 0x09;//接电话
	public static final int SWC_KEY_HANG_UP							= 0x0A;//挂电话
	public static final int SWC_KEY_SPEECH								= 0x12;//Speech
	public static final int SWC_KEY_BACK									= 0x15;//Back
	public static final int SWC_KEY_ENTER								= 0x16;//Enter
	public static final int SWC_KEY_EJECT									= 0x20;//出碟
	
	public static final int PANEL_KEY_POWER							= 0x01;
	public static final int PANEL_KEY_SCROLL_VOLUME_UP		= 0x02;
	public static final int PANEL_KEY_SCROLL_VOLUME_DOWN	= 0x03;
	public static final int PANEL_KEY_AUTO_SEARCH					= 0x04;
	public static final int PANEL_KEY_SCAN_PLAY						= 0x05;
	public static final int PANEL_KEY_SEEK_UP							= 0x06;
	public static final int PANEL_KEY_SEEK_DOWN					= 0x07;
	public static final int PANEL_KEY_AUDIO								= 0x08;
	public static final int PANEL_KEY_SCROLL_TUNE_UP			= 0x09;
	public static final int PANEL_KEY_SCROLL_TUNE_DOWN		= 0x0A;
	public static final int PANEL_KEY_NUMBER_1						= 0x0B;
	public static final int PANEL_KEY_NUMBER_2						= 0x0C;
	public static final int PANEL_KEY_NUMBER_3						= 0x0D;
	public static final int PANEL_KEY_NUMBER_4						= 0x0E;
	public static final int PANEL_KEY_NUMBER_5						= 0x0F;
	public static final int PANEL_KEY_NUMBER_6						= 0x10;
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PICK_UP, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_HANG_UP, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SPEECH, VehiclePropertyConstants.USER_KEY_VOICE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_EJECT, VehiclePropertyConstants.USER_KEY_EJECT);
	}
	
	public static final SparseIntArray PANEL_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_AUTO_SEARCH, VehiclePropertyConstants.USER_KEY_RADIO_AUTO_SEARCH);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCAN_PLAY, VehiclePropertyConstants.USER_KEY_PLAY_SCAN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_UP, VehiclePropertyConstants.USER_KEY_RADIO_STEP_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_STEP_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_AUDIO, VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_TUNE_UP, VehiclePropertyConstants.USER_KEY_RADIO_STEP_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_TUNE_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_STEP_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUMBER_1, VehiclePropertyConstants.USER_KEY_NUM1);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUMBER_2, VehiclePropertyConstants.USER_KEY_NUM2);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUMBER_3, VehiclePropertyConstants.USER_KEY_NUM3);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUMBER_4, VehiclePropertyConstants.USER_KEY_NUM4);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUMBER_5, VehiclePropertyConstants.USER_KEY_NUM5);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUMBER_6, VehiclePropertyConstants.USER_KEY_NUM6);
	}
	
	public static final int AC_LOWEST_TEMP 		= 18;//最低温度
	public static final int AC_HIGHEST_TEMP 	= 32;//最高温度
	
	//souce定义
	public static final int SOURCE_NONE 		= 0x00;
	public static final int SOURCE_AM 			= 0x01;
	public static final int SOURCE_FM1 			= 0x02;
	public static final int SOURCE_FM2 			= 0x03;
	public static final int SOURCE_CD 			= 0x04;
	public static final int SOURCE_USB1 		= 0x05;
	public static final int SOURCE_USB2 		= 0x06;
	public static final int SOURCE_BT_MUSIC= 0x07;
	public static final int SOURCE_AUX 			= 0x08;
	public static final int SOURCE_FM 			= 0x09;
	public static final int SOURCE_USB 			= 0x0A;
	
	//所有支持的属性集
	public static final int[] VEHICLE_CAN_PROPERITIES = new int[] { 
			VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY,
			VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY,
			VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY,
			VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,
			VehicleInterfaceProperties.VIM_KEY_PROPERTY,
			
			VehicleInterfaceProperties.VIM_ODOMETER_UNIT_PROPERTY,
			VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY,
			VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY,
			
			VehicleInterfaceProperties.VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY,
			VehicleInterfaceProperties.VIM_CUR_TRIP_DRIVING_FUEL_CONSUMPTION_PROPERTY,
			VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,
			
			VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY,
			VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY,
			VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY,
			VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY,
			VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY,
			VehicleInterfaceProperties.VIM_INTERIOR_TEMP_PROPERTY,
			VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY,
			VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY,

			VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,
			VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,
			
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY
	};	
		
	// 属性读写权限表
	public static final SparseIntArray VEHICLE_CAN_PROPERITY_PERMISSION_TABLE = new SparseIntArray();
	static
	{
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY, PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY, PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_KEY_PROPERTY,PROPERITY_PERMISSON_NO);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_ODOMETER_UNIT_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY, PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_CUR_TRIP_DRIVING_FUEL_CONSUMPTION_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_INTERIOR_TEMP_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY,PROPERITY_PERMISSON_GET);
		
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY,PROPERITY_PERMISSON_NO);
	}	

	// 属性值类型表
	public static final SparseIntArray VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE = new SparseIntArray();
	static
	{
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_KEY_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_ODOMETER_UNIT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY, VehiclePropertyConstants.DATA_TYPE_FLOAT);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_FLOAT);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_CUR_TRIP_DRIVING_FUEL_CONSUMPTION_PROPERTY,VehiclePropertyConstants.DATA_TYPE_FLOAT);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_INTERIOR_TEMP_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
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
	
	public static int panelKeyToUserKey(int panelKey)
	{
		return (PANEL_KEY_TO_USER_KEY_TABLE.indexOfKey(panelKey) >= 0) ? PANEL_KEY_TO_USER_KEY_TABLE.get(panelKey):-1;
	}
		
}
