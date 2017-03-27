package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_KoreaSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_EXTERN_TEMP_INFO 			= 0x01;
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x02;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x03;
	public static final int CH_CMD_RADAR_INFO 						= 0x04;
	public static final int CH_CMD_VEHICLE_DOOR_INFO 			= 0x05;
	public static final int CH_CMD_AC_CLIMATE_STATUS 			= 0x06;
	public static final int CH_CMD_BACKLIGHT_INFO 				= 0x07;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x7F;
	
	//HU->CanBox
	public static final int HC_CMD_SETTING          						= 0x03;
	public static final int HC_CMD_POWER									= 0x04;
	public static final int HC_CMD_VOLUME_ADJUST					= 0x05;//音量调节
	public static final int HC_CMD_TIME_INFO							= 0x06;
	public static final int HC_CMD_AUDIO_BALANCE					= 0x07;//平衡音调节
	public static final int HC_CMD_SOUND_EFFECTS					= 0x08;//音效调节
	public static final int HC_CMD_HOST_STATUS_INFO				= 0x09;//主机状态信息
	
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_MUTE									= 0x10;//静音
	public static final int SWC_KEY_MODE									= 0x11;//模式
	public static final int SWC_KEY_SEEK_UP								= 0x12;//Seek+
	public static final int SWC_KEY_SEEK_DOWN						= 0x13;//Seek-
	public static final int SWC_KEY_VOLUME_UP						= 0x14;//音量+
	public static final int SWC_KEY_VOLUME_DOWN					= 0x15;//音量-
	public static final int SWC_KEY_SCROLL_UP						= 0x84;//
	public static final int SWC_KEY_SCROLL_DOWN					= 0x85;
	public static final int SWC_KEY_PHONE_ACCEPT					= 0x16;//接电话
	public static final int SWC_KEY_PHONE_HANGUP					= 0x17;//挂电话
	
	public static final int PANEL_KEY_POWER							= 0x18;
	public static final int PANEL_KEY_VOLUME_UP						= 0x19;
	public static final int PANEL_KEY_VOLUME_DOWN				= 0x1A;
	public static final int PANEL_KEY_FM_AM								= 0x1B;
	public static final int PANEL_KEY_MEDIA								= 0x1C;
	public static final int PANEL_KEY_PHONE								= 0x1D;
	public static final int PANEL_KEY_DISP								= 0x1E;
	public static final int PANEL_KEY_SEEK_UP							= 0x1F;//Seek+
	public static final int PANEL_KEY_SEEK_DOWN					= 0x20;//Seek-
	public static final int PANEL_KEY_MAP									= 0x21;//
	public static final int PANEL_KEY_DEST								= 0x22;//
	public static final int PANEL_KEY_ROUTE								= 0x23;//
	public static final int PANEL_KEY_SETUP								= 0x24;//
	public static final int PANEL_KEY_ENTER								= 0x25;//
	public static final int PANEL_KEY_TUNER_UP						= 0x26;//
	public static final int PANEL_KEY_TUNER_DOWN					= 0x27;//
	public static final int PANEL_KEY_UVO									= 0x28;//
	public static final int PANEL_KEY_HOME								= 0x29;//

	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MODE, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_UP,VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_DOWN,VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SCROLL_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SCROLL_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_ACCEPT, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_HANGUP,VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_POWER,VehiclePropertyConstants.USER_KEY_POWER);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOLUME_UP,VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_FM_AM, VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MEDIA, VehiclePropertyConstants.USER_KEY_MUSIC);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_PHONE, VehiclePropertyConstants.USER_KEY_BLUETOOTH);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_DISP, VehiclePropertyConstants.USER_KEY_STAND_BY);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SEEK_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_MAP, VehiclePropertyConstants.USER_KEY_NAVI);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_DEST, VehiclePropertyConstants.USER_KEY_NAVI);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_ROUTE, VehiclePropertyConstants.USER_KEY_NAVI);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_SETUP, VehiclePropertyConstants.USER_KEY_SYS_SETTING);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_TUNER_UP, VehiclePropertyConstants.USER_KEY_RADIO_STEP_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_TUNER_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_STEP_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_UVO, VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		SWC_KEY_TO_USER_KEY_TABLE.put(PANEL_KEY_HOME, VehiclePropertyConstants.USER_KEY_HOME);
		
	}
	
	
	public static final float AC_LOWEST_TEMP_C 	= 17.5f;//最低温度
	public static final float AC_HIGHEST_TEMP_C	= 31.5f;//最高温度
	
	public static final float AC_LOWEST_TEMP_F 	= 62.0f;//最低温度
	public static final float AC_HIGHEST_TEMP_F	= 90.0f;//最高温度
	
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
