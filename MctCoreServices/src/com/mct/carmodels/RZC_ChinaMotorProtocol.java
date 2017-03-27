package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_ChinaMotorProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x02;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x03;//空调信息
	public static final int CH_CMD_BASE_INFO 							= 0x06;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x0A;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x7F;
	
	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_SOURCE								= 0x82;
	public static final int HC_CMD_TIME_SET								= 0x85;
	public static final int HC_CMD_BT_PHONE_INFO					= 0x88;
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0xFF;
	
	//方控
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量键
	public static final int SWC_KEY_DOWN								= 0x03;//下一曲
	public static final int SWC_KEY_UP										= 0x04;//上一曲
	public static final int SWC_KEY_MODE									= 0x05;//SOURCE
	public static final int SWC_KEY_MUTE									= 0x06;//静音
	public static final int SWC_KEY_PHONE_UP							= 0x07;
	public static final int SWC_KEY_PHONE_DOWN					= 0x08;
	public static final int PANEL_KEY_HOME								= 0x10;
	public static final int PANEL_KEY_MEDIA								= 0x11;
	public static final int PANEL_KEY_POWER							= 0x12;
	public static final int PANEL_KEY_RADIO								= 0x13;
	public static final int PANEL_KEY_TEL									= 0x14;
	public static final int PANEL_KEY_VOLUME_UP						= 0x15;
	public static final int PANEL_KEY_VOLUME_DOWN				= 0x16;

	
	public static final int AC_LOWEST_TEMP 		= 17;//最低温度
	public static final int AC_HIGHEST_TEMP 	= 29;//最高温度
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MODE, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_UP, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_DOWN, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_HOME, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MEDIA, VehiclePropertyConstants.USER_KEY_MUSIC);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_RADIO, VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_TEL, VehiclePropertyConstants.USER_KEY_BLUETOOTH);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
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
		
}
