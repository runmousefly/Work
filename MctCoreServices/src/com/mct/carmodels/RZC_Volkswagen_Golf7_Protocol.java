package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_Volkswagen_Golf7_Protocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_BACKLIGHT_INFO 				= 0x14;
	public static final int CH_CMD_VEHICLE_SPEED_INFO 			= 0x16;
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x20;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x21;
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x22;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x23;
	public static final int CH_CMD_BASE_INFO 							= 0x24;
	public static final int CH_CMD_PARKING_STATUS_INFO 		= 0x25;
	public static final int CH_CMD_VEHICLE_TIME_INFO 			= 0x26;
	public static final int CH_CMD_ENV_TEMP 							= 0x27;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x29;
	public static final int CH_CMD_STEERING_WHEEL_CMD		= 0x2F;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x30;
	public static final int CH_CMD_LEFT_RADAR 						= 0x32;
	public static final int CH_CMD_RIGHT_RADAR 						= 0x33;	
	public static final int CH_CMD_VEHICLE_STAUS					= 0x40;
	public static final int CH_CMD_HU_ENABLE 							= 0x41;
	public static final int CH_CMD_DRIVING_DATA 					= 0x50;
	public static final int CH_CMD_PROMPT_INFO						= 0x60;
	public static final int CH_CMD_CONV_CONSUME					= 0x62;
	public static final int CH_CMD_SERVICE_INFO						= 0x63;
	public static final int CH_CMD_TPMS_INFO							= 0x65;		
	
	//HU->CanBox
	public static final int HC_CMD_MEDIA_INFO_1						= 0x70;
	public static final int HC_CMD_MEDIA_INFO_2						= 0x71;
	public static final int HC_CMD_MEDIA_INFO_3						= 0x72;
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0x90;
	public static final int HC_CMD_SET_TIME								= 0xA6;
	public static final int HC_CMD_SOURCE								= 0xC0;
	public static final int HC_CMD_ICON									= 0xC1;
	public static final int HC_CMD_VOLUME								= 0xC4;
	public static final int HC_CMD_VEHICLE_SETTING				= 0xC6;
	public static final int HC_CMD_PHONE_STATUS					= 0xC5;
	public static final int HC_CMD_PHONE_INFO						= 0xCA;
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量减
	public static final int SWC_KEY_PLAY_NEXT							= 0x03;//Next
	public static final int SWC_KEY_PLAY_PREV							= 0x04;//Prev
	public static final int SWC_KEY_TEL										= 0x05;//电话
	public static final int SWC_KEY_MUTE									= 0x06;//静音
	public static final int SWC_KEY_SRC									= 0x07;//源切换
	public static final int SWC_KEY_MIC									= 0x08;//Mic
	
	
	public static final int SWC_CMD_PREV									= 0x01;//上一曲
	public static final int SWC_CMD_NEXT									= 0x02;//下一曲
	public static final int SWC_CMD_FAST_FORWARD				= 0x03;//快进
	public static final int SWC_CMD_FAST_REWIND					= 0x04;//快退
	public static final int SWC_CMD_FAST_RELEASE					= 0x05;//快进快退释放
	public static final int SWC_CMD_MODE								= 0x10;//MODE
	public static final int SWC_CMD_ANSWER							= 0x11;//回答
	public static final int SWC_CMD_REJECT								= 0x12;//Reject
	public static final int SWC_CMD_LGNORE								= 0x13;//LGNORE,使用手机铃声
	public static final int SWC_CMD_HOLD									= 0x14;//Hold
	public static final int SWC_CMD_CONTINUE							= 0x15;//Continue
	public static final int SWC_CMD_MIC_OFF							= 0x16;//Mic Off
	public static final int SWC_CMD_MIC_ON								= 0x17;//Mic On
	public static final int SWC_CMD_PRIVATE							= 0x18;//私人
	public static final int SWC_CMD_HANDFREE							= 0x19;//免提


	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PLAY_NEXT, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PLAY_PREV, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TEL, VehiclePropertyConstants.USER_KEY_BLUETOOTH);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MIC, VehiclePropertyConstants.USER_KEY_VOICE);	
	}
	
	public static final SparseIntArray SWC_CMD_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_PREV, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_NEXT, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_FAST_FORWARD, VehiclePropertyConstants.USER_KEY_FAST_FORWARD);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_FAST_REWIND, VehiclePropertyConstants.USER_KEY_FAST_REWIND);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_FAST_RELEASE, VehiclePropertyConstants.USER_KEY_PLAY);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_MODE, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_ANSWER, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_REJECT, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_LGNORE, VehiclePropertyConstants.USER_KEY_PHONE_LGNORE);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_HOLD, VehiclePropertyConstants.USER_KEY_PHONE_HOLD);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_CONTINUE, VehiclePropertyConstants.USER_KEY_PHONE_CONTINUE);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_MIC_OFF, VehiclePropertyConstants.USER_KEY_PHONE_MIC_OFF);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_MIC_ON, VehiclePropertyConstants.USER_KEY_PHONE_MIC_ON);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_PRIVATE, VehiclePropertyConstants.USER_KEY_PHONE_PRIVATE);
		SWC_CMD_TO_USER_KEY_TABLE.put(SWC_CMD_HANDFREE, VehiclePropertyConstants.USER_KEY_PHONE_HANDFREE);
	}
	
	
	public static final float AC_LOWEST_TEMP_C 	= 16;		//最低温度
	public static final float AC_HIGHEST_TEMP_C	= 29.5f;	//最高温度
	
	public static final float AC_LOWEST_TEMP_F 	= 60;		//最低温度
	public static final float AC_HIGHEST_TEMP_F	= 87;		//最高温度
	
	public static final float EXTERN_LOWEST_TEMP  = -58.0f;
	public static final float EXTERN_HIGHEST_TEMP = 171.0f;
	
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
				
				//VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY,//仅17款迈腾有
				VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY,
				VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY,
				
				VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,
				VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY
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
		
			VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY, PROPERITY_PERMISSON_GET_SET);
			VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY,PROPERITY_PERMISSON_NO);
			VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY,PROPERITY_PERMISSON_NO);
		
			VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,PROPERITY_PERMISSON_GET);
			VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,PROPERITY_PERMISSON_GET_SET);
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
			
			VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_VIDEO_MODE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
			VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
			
			VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
			VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
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
	
	public static int swcCmdToUserKey(int swcCmd)
	{
		return (SWC_CMD_TO_USER_KEY_TABLE.indexOfKey(swcCmd) >= 0) ? SWC_CMD_TO_USER_KEY_TABLE.get(swcCmd):-1;

	}
		
}
