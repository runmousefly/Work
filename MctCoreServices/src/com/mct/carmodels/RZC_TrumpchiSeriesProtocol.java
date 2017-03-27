package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_TrumpchiSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x10;
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x12;
	public static final int CH_CMD_BACKLIGHT_INFO 				= 0x14;
	public static final int CH_CMD_BASE_INFO 							= 0x24;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x30;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x31;
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x32;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x33;
	public static final int CH_CMD_AVM_STATUS_INFO				= 0x40;
	public static final int CH_CMD_PANEL_KEY_INFO					= 0x50;
	public static final int CH_CMD_LINK_SOS_INFO					= 0x51;
	public static final int CH_CMD_VEHICLE_SETTING_INFO 		= 0x52;	
	
	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_REVERSE_VIDEO_SWITCH		= 0x82;
	public static final int HC_CMD_VEHICLE_SET						= 0x83;
	public static final int HC_CMD_AVM_SWITCH						= 0x84;
	public static final int HC_CMD_AVM_SET								= 0xC7;
	public static final int HC_CMD_TIME_SET								= 0xC8;
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_MODE									= 0x11;
	public static final int SWC_KEY_SEEK_UP								= 0x12;
	public static final int SWC_KEY_SEEK_DOWN						= 0x13;
	public static final int SWC_KEY_VOLUME_UP						= 0x14;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x15;//音量键
	public static final int SWC_KEY_MUTE									= 0x16;//静音
	public static final int SWC_KEY_PICK_UP								= 0x30;//电话接听
	public static final int SWC_KEY_HANG_UP							= 0x31;//电话挂断
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MODE, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_UP, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PICK_UP, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_HANG_UP, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
	}
	
	/*
	 * 面板按键
	 */
	public static final int PANEL_KEY_POWER							= 0x01;
	public static final int PANEL_KEY_EJECT								= 0x02;//出碟
	public static final int PANEL_KEY_MENU								= 0x03;
	public static final int PANEL_KEY_MODE								= 0x04;
	public static final int PANEL_KEY_FM_AM								= 0x05;
	public static final int PANEL_KEY_LEFT								= 0x06;
	public static final int PANEL_KEY_RIGHT								= 0x07;
	public static final int PANEL_KEY_SET									= 0x08;
	public static final int PANEL_KEY_MUTE								= 0x09;
	public static final int PANEL_KEY_NAVI								= 0x0A;
	public static final int PANEL_KEY_SEL									= 0x0B;
	public static final int PANEL_KEY_SCROLL_VOLUME_UP		= 0x10;
	public static final int PANEL_KEY_SCROLL_VOLUME_DOWN	= 0x11;
	public static final int PANEL_KEY_SCROLL_TUNER_UP			= 0x12;
	public static final int PANEL_KEY_SCROLL_TUNER_DOWN	= 0x13;
	public static final int PANEL_KEY_NUM1								= 0x21;
	public static final int PANEL_KEY_NUM2								= 0x22;
	public static final int PANEL_KEY_NUM3								= 0x23;
	public static final int PANEL_KEY_NUM4								= 0x24;
	public static final int PANEL_KEY_NUM5								= 0x25;
	public static final int PANEL_KEY_NUM6								= 0x26;
	public static final int PANEL_KEY_APS									= 0x27;
	public static final int PANEL_KEY_SCAN								= 0x28;

	public static final SparseIntArray PANEL_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_POWER,VehiclePropertyConstants.USER_KEY_POWER);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_EJECT,VehiclePropertyConstants.USER_KEY_EJECT);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MENU, VehiclePropertyConstants.USER_KEY_HOME);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MODE, VehiclePropertyConstants.USER_KEY_SOURCE);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_FM_AM, VehiclePropertyConstants.USER_KEY_RADIO);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_LEFT, VehiclePropertyConstants.USER_KEY_LEFT);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_RIGHT, VehiclePropertyConstants.USER_KEY_RIGHT);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SET, VehiclePropertyConstants.USER_KEY_SYS_SETTING);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NAVI, VehiclePropertyConstants.USER_KEY_NAVI);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEL, VehiclePropertyConstants.USER_KEY_OK);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_TUNER_UP, VehiclePropertyConstants.USER_KEY_RADIO_STEP_UP);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCROLL_TUNER_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_STEP_DOWN);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUM1, VehiclePropertyConstants.USER_KEY_NUM1);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUM2, VehiclePropertyConstants.USER_KEY_NUM2);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUM3, VehiclePropertyConstants.USER_KEY_NUM3);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUM4, VehiclePropertyConstants.USER_KEY_NUM4);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUM5, VehiclePropertyConstants.USER_KEY_NUM5);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_NUM6, VehiclePropertyConstants.USER_KEY_NUM6);
		//PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_APS, VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		PANEL_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SCAN, VehiclePropertyConstants.USER_KEY_RADIO_PLAY_SCAN);
	}
	
	public static int panelKeyToUserKey(int panelKey)
	{
		return (PANEL_KEY_TO_USER_KEY_TABLE.indexOfKey(panelKey) >= 0) ? PANEL_KEY_TO_USER_KEY_TABLE.get(panelKey):-1;
	}
	
	public static final float AC_LOWEST_TEMP 		= 18.5f;//最低温度
	public static final float AC_HIGHEST_TEMP 		= 31.5f;//最高温度
	
	public static final float EXTERN_LOWEST_TEMP  = -50.0f;
	public static final float EXTERN_HIGHEST_TEMP = 77.5f;
	
	//所有支持的属性集
	public static final int[] VEHICLE_CAN_PROPERITIES = new int[] { 
			VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY,
			VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY,
			VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY,
			VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,
			VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY,
			VehicleInterfaceProperties.VIM_KEY_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,
			
			VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE,
			VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_STATUS_PROPERTY,
			
			VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY,
			VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY,
			VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY,
			VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY,
			VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY,
			
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
			
			VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY,
			VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY,
			
			VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,
			VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY
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
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_STATUS_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY,PROPERITY_PERMISSON_GET);
		
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
	
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY,PROPERITY_PERMISSON_NO);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY,PROPERITY_PERMISSON_NO);
	
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,PROPERITY_PERMISSON_GET_SET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY,PROPERITY_PERMISSON_GET_SET);
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
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_STATUS_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_FLOAT);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY,VehiclePropertyConstants.DATA_TYPE_FLOAT);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY,VehiclePropertyConstants.DATA_TYPE_FLOAT);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
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
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);

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
