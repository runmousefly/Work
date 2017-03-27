package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_GreatWallProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x21;
	public static final int CH_CMD_PANEL_KEY							= 0x22;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x23;//空调信息
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x26;
	public static final int CH_CMD_BASE_INFO 							= 0x28;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x30;
	public static final int CH_CMD_RIGHT_CAMERA 					= 0x40;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x7F;
	
	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_VEHICLE_SET						= 0x83;
	
	//方控
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量键
	public static final int SWC_KEY_MUTE									= 0x06;
	public static final int SWC_KEY_MODE									= 0x07;//SOURCE
	public static final int SWC_KEY_PHONE_UP							= 0x09;
	public static final int SWC_KEY_PHONE_DOWN					= 0x0A;
	public static final int SWC_KEY_UP										= 0x0B;//上一曲
	public static final int SWC_KEY_DOWN								= 0x0C;//下一曲
	public static final int SWC_KEY_RIGHT								= 0x0D;
	public static final int SWC_KEY_LEFT									= 0x0E;
	
	public static final int PANEL_KEY_POWER							= 0x01;
	public static final int PANEL_KEY_BAND								= 0x07;
	public static final int PANEL_KEY_MUTE								= 0x09;
	public static final int PANEL_KEY_VOLUME_UP						= 0x21;
	public static final int PANEL_KEY_VOLUME_DOWN				= 0x22;
	public static final int PANEL_KEY_SEEK_UP							= 0x29;
	public static final int PANEL_KEY_SEEK_DOWN					= 0x30;
	public static final int PANEL_KEY_SRC									= 0x31;
	public static final int PANEL_KEY_MENU								= 0x32;
	public static final int PANEL_KEY_PHONE_ON						= 0x33;
	public static final int PANEL_KEY_PHONE_OFF						= 0x34;
	public static final int PANEL_KEY_SET									= 0x35;
	public static final int PANEL_KEY_NAVI								= 0x36;
	

	
	public static final int AC_LOWEST_TEMP 		= 17;//最低温度
	public static final int AC_HIGHEST_TEMP 	= 29;//最高温度
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RIGHT, VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_LEFT, VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MODE, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_UP, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_DOWN, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
	}
	
	public static final SparseIntArray PANEL_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_BAND, VehiclePropertyConstants.USER_KEY_RADIO);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_UP, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SRC, VehiclePropertyConstants.USER_KEY_SOURCE);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MENU, VehiclePropertyConstants.USER_KEY_HOME);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_PHONE_ON, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_PHONE_OFF, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SET, VehiclePropertyConstants.USER_KEY_SYS_SETTING);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NAVI, VehiclePropertyConstants.USER_KEY_NAVI);
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
			
			VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_PROPERTY
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
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_PROPERTY, PROPERITY_PERMISSON_GET_SET);

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
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
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
