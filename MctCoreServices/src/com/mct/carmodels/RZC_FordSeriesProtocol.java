package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_FordSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_PANEL_KEY 							= 0x01;
	public static final int CH_CMD_BACKLIGHT_INFO 				= 0x14;
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x20;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x21;
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x22;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x23;
	public static final int CH_CMD_BASE_INFO 							= 0x24;
	public static final int CH_CMD_PARK_ASSIST_INFO 				= 0x25;
	public static final int CH_CMD_DATE_TIME_INFO 				= 0x26;
	public static final int CH_CMD_LANGUAGE_INFO					= 0x27;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x30;
	
	public static final int CH_CMD_SYNC_VERSION					= 0x40;
	public static final int CH_CMD_SYNC_MENU_INFO				= 0x50;
	public static final int CH_CMD_SYNC_MENU_ITEM_INFO		= 0x51;
	public static final int CH_CMD_SYNC_PLAY_TIME_INFO		= 0x52;
	public static final int CH_CMD_SYNC_CALL_TIME_INFO		= 0x53;
	
	public static final int CH_CMD_SYNC_SRT_UP 						= 0x70;
	public static final int CH_CMD_SYNC_SRT_DOWN 				= 0x71;
	public static final int CH_CMD_SYNC_SRT_SHORT				= 0x72;
	public static final int CH_CMD_SYNC_STATUS_INFO				= 0x78;
	public static final int CH_CMD_SYNC_REQ_SWITCH				= 0x79;
	
	//HU->CanBox
	public static final int HC_CMD_CONTROL          					= 0xC6;
	public static final int HC_CMD_SWITCH								= 0x81;
	public static final int HC_CMD_TIME_SET								= 0x82;
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0x90;
	public static final int HC_CMD_CAR_MODEL_SET					= 0x91;
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量键
	public static final int SWC_KEY_FAST_FORWARD					= 0x03;//快进
	public static final int SWC_KEY_FAST_REWIND					= 0x04;//快退
	public static final int SWC_KEY_SRC									= 0x07;//源切换
	public static final int SWC_KEY_UP										= 0x0E;
	public static final int SWC_KEY_DOWN								= 0x0F;
	public static final int SWC_KEY_LEFT									= 0x10;
	public static final int SWC_KEY_RIGHT								= 0x11;
	public static final int SWC_KEY_OK										= 0x12;
	
	public static final int SWC_KEY_DIALPAD_STAR					= 0x2A;//*
	public static final int SWC_KEY_DIALPAD_POUND				= 0x2B;//#
	public static final int SWC_KEY_SIRIUS								= 0x33;
	public static final int SWC_KEY_RADIO								= 0x34;
	public static final int SWC_KEY_CD										= 0x35;
	public static final int SWC_KEY_AUX									= 0x36;
	public static final int SWC_KEY_MENU									= 0x37;
	public static final int SWC_KEY_SOUND								= 0x38;
	public static final int SWC_KEY_PHONE								= 0x39;
	public static final int SWC_KEY_CLOCK								= 0x3D;
	public static final int SWC_KEY_POWER								= 0x3F;
	public static final int SWC_KEY_OK2									= 0x48;
	public static final int SWC_KEY_LEFT2									= 0x49;
	public static final int SWC_KEY_RIGHT2								= 0x4A;
	public static final int SWC_KEY_UP2									= 0x4B;
	public static final int SWC_KEY_DOWN2								= 0x4C;
	public static final int SWC_KEY_PICKUP								= 0x52;
	public static final int SWC_KEY_HANGUP								= 0x53;
	public static final int SWC_KEY_EJECT									= 0x54;
	public static final int SWC_KEY_TA										= 0x56;
	public static final int SWC_KEY_INFO									= 0x57;
	public static final int SWC_KEY_DSP									= 0x59;
	public static final int SWC_KEY_MUTE									= 0x5A;
	public static final int SWC_KEY_DISPLAY								= 0x5B;
	public static final int SWC_KEY_K1										= 0x5C;
	public static final int SWC_KEY_K2										= 0x5D;
	public static final int SWC_KEY_K3										= 0x5E;
	public static final int SWC_KEY_K4										= 0x5F;
	public static final int SWC_KEY_PLAY_PAUSE						= 0x66;
	public static final int SWC_KEY_MEDIA								= 0x67;
	public static final int SWC_KEY_TUNE_UP							= 0x70;
	public static final int SWC_KEY_TUNE_DOWN						= 0x71;
	public static final int SWC_KEY_VOLUME_UP2						= 0xF0;
	public static final int SWC_KEY_VOLUME_DOWN2				= 0xF1;
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP,VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN,VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_FAST_FORWARD,VehiclePropertyConstants.USER_KEY_FAST_FORWARD);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_FAST_REWIND,VehiclePropertyConstants.USER_KEY_FAST_REWIND);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC,VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP,VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN	,VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_LEFT,VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RIGHT,VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_OK,VehiclePropertyConstants.USER_KEY_OK);
		
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DIALPAD_STAR,VehiclePropertyConstants.USER_KEY_DIALPAD_STAR);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DIALPAD_POUND	,VehiclePropertyConstants.USER_KEY_DIALPAD_POUND);
		//SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SIRIUS,VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RADIO,VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_CD,VehiclePropertyConstants.USER_KEY_DVD);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_AUX,VehiclePropertyConstants.USER_KEY_AUX1);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU,VehiclePropertyConstants.USER_KEY_MENU);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SOUND,VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE,VehiclePropertyConstants.USER_KEY_BLUETOOTH);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_CLOCK,VehiclePropertyConstants.USER_KEY_CLOCK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_POWER,VehiclePropertyConstants.USER_KEY_POWER);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_OK2,VehiclePropertyConstants.USER_KEY_OK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_LEFT2,VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RIGHT2,VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP2	,VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN2,VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PICKUP,VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_HANGUP,VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_EJECT,VehiclePropertyConstants.USER_KEY_EJECT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TA,VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_INFO,VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DSP	,VehiclePropertyConstants.USER_KEY_AUDIO_BALANCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE,VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DISPLAY,VehiclePropertyConstants.USER_KEY_STAND_BY);
		//SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_K1,VehiclePropertyConstants.USER_KEY_);
		//SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_K2,VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		//SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_K3,VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		//SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_K4,VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PLAY_PAUSE,VehiclePropertyConstants.USER_KEY_PLAY_PAUSE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MEDIA,VehiclePropertyConstants.USER_KEY_MUSIC);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TUNE_UP	,VehiclePropertyConstants.USER_KEY_RADIO_STEP_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TUNE_DOWN,VehiclePropertyConstants.USER_KEY_RADIO_STEP_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP2,VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN2,VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
	}
	
	
	public static final float AC_LOWEST_TEMP 		= 15.5f;//最低温度
	public static final float AC_HIGHEST_TEMP 		= 29.5f;//最高温度
	
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
