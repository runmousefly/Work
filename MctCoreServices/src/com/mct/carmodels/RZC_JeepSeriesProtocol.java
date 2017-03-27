package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_JeepSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x01;
	public static final int CH_CMD_ILL_INFO 								= 0x02;
	public static final int CH_CMD_VEHICLE_SPEED_INFO 			= 0x03;
	public static final int CH_CMD_PANEL_KEY 							= 0x04;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x05;//空调信息
	public static final int CH_CMD_VEHICLE_CONTROL_INFO1 	= 0x07;
	public static final int CH_CMD_VEHICLE_CONTROL_INFO2 	= 0x17;
	public static final int CH_CMD_RADAR_INFO 						= 0x08;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x09;
	public static final int CH_CMD_VEHICLE_STATUS_INFO 		= 0x0A;
	public static final int CH_CMD_COMPASS_INFO 					= 0x0B;
	public static final int CH_CMD_CD_STATUS_INFO 				= 0x10;
	public static final int CH_CMD_CD_TEXT_INFO 					= 0x11;
	public static final int CH_CMD_EXTERNAL_TEMP_INFO			= 0x15;
	public static final int CH_CMD_AMPLIFIER_STATUS_INFO 	= 0x31;
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x22;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x23;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x30;
	
	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_DISPLAY_INFO						= 0x90;//中控台显示信息
	public static final int HC_CMD_AMPLIFIER_CONTROL			= 0x93;//攻放控制
	public static final int HC_CMD_NAVI_INFO							= 0x94;//导航信息
	public static final int HC_CMD_AC_CONTROL						= 0x95;//空调控制
	public static final int HC_CMD_VEHICLE_CONTROL1				= 0x97;//车辆控制1
	public static final int HC_CMD_VEHICLE_CONTROL2				= 0xA7;//车辆控制2
	public static final int HC_CMD_CD_CONTROL						= 0xA0;//车辆控制2
	public static final int HC_CMD_TIME_SET								= 0xC6;
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0xF1;
	
	//方控
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x11;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x12;//音量键
	public static final int SWC_KEY_UP										= 0x13;
	public static final int SWC_KEY_DOWN								= 0x14;
	public static final int SWC_KEY_MODE									= 0x15;//SOURCE
	public static final int SWC_KEY_CH										= 0x16;
	public static final int SWC_KEY_VOICE									= 0x17;
	public static final int SWC_KEY_TEL_ON								= 0x18;
	public static final int SWC_KEY_TEL_OFF								= 0x19;
	public static final int SWC_KEY_SRC									= 0x1A;
	public static final int SWC_KEY_BAND									= 0x1B;
	public static final int SWC_KEY_SEEK_UP								= 0x1C;
	public static final int SWC_KEY_SEEK_DOWN						= 0x1D;
	public static final int SWC_KEY_LEFT									= 0x1E;
	public static final int SWC_KEY_RIGHT								= 0x1F;
	public static final int SWC_KEY_OK										= 0x20;
	
	//面板按键
	public static final int PANEL_KEY_SCREEN_OFF					= 0x01;
	public static final int PANEL_KEY_BACK								= 0x02;
	public static final int PANEL_KEY_MUTE								= 0x03;
	public static final int PANEL_KEY_BROWSE_ENTER				= 0x04;
	public static final int PANEL_KEY_VOL_DOWN						= 0x05;
	public static final int PANEL_KEY_VOL_UP							= 0x06;
	public static final int PANEL_KEY_SEEK_DOWN					= 0x07;
	public static final int PANEL_KEY_SEEK_UP							= 0x08;
	public static final int PANEL_KEY_POWER_OFF					= 0x09;
	
	
	
	public static final float AC_LOWEST_TEMP_C 		= 13;//最低温度
	public static final float AC_HIGHEST_TEMP_C	 	= 30;//最高温度
	public static final float AC_LOWEST_TEMP_F 		= 60;//最低温度
	public static final float AC_HIGHEST_TEMP_F	 	= 84;//最高温度
	
	public static final float COMPASS_AC_LOWEST_TEMP_C 		= 16;//最低温度
	public static final float COMPASS_AC_HIGHEST_TEMP_C	 	= 28;//最高温度
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MODE, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_CH, VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOICE, VehiclePropertyConstants.USER_KEY_VOICE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TEL_ON, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TEL_OFF, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BAND, VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_UP, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_LEFT, VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RIGHT, VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_OK, VehiclePropertyConstants.USER_KEY_OK);
	}
	
	public static final SparseIntArray PANEL_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCREEN_OFF, VehiclePropertyConstants.USER_KEY_SCREEN_OFF);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_BROWSE_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOL_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOL_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_UP, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_POWER_OFF, VehiclePropertyConstants.USER_KEY_STAND_BY);
	}
	
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
			
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,
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
	
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,PROPERITY_PERMISSON_NO);
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
	
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
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
