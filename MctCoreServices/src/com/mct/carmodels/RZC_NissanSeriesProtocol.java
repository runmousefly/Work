package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_NissanSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x20;
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x22;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x23;
	public static final int CH_CMD_BASE_INFO 							= 0x28;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x29;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x30;
	public static final int CH_CMD_SRC_SWITCH						= 0x40;
	public static final int CH_CMD_CALL_OPERATION					= 0x50;
	public static final int CH_CMD_SOS 										= 0x91;
	public static final int CH_CMD_AMPLIFIER							= 0x93;
	public static final int CH_CMD_AVM										= 0x94;
	public static final int CH_CMD_ADAS									= 0x95;
	//启辰T70
	public static final int CH_CMD_CAMERA_STATUS					= 0x60;
	public static final int CH_CMD_CAMERA_SET_INFO				= 0x61;
	//启辰T90
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x21;//空调信息
	
	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_MEDIA_SOURCE_INFO			= 0xC0;
	public static final int HC_CMD_MEDIA_INFO1						= 0x70;
	public static final int HC_CMD_MEDIA_INFO2						= 0x71;
	public static final int HC_CMD_VOLUME								= 0xC4;
	public static final int HC_CMD_CALL_STATUS						= 0xC5;
	public static final int HC_CMD_AVM_STATUS						= 0xC7;
	public static final int HC_CMD_TIME_INFO							= 0xC8;
	public static final int HC_CMD_VEHICLE_SET						= 0x83;
	//启辰T70
	public static final int HC_CMD_CAMERA_SWITCH					= 0x82;//摄像头视频切换
	public static final int HC_CMD_CAMERA_SET						= 0x83;//摄像头视频切换
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0x90;//请求指定信息
	//启辰T90
	public static final int HC_CMD_AIR_CONDITIONING_CONTROL 	= 0xA8;//空调信息
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量键
	public static final int SWC_KEY_MENU_UP							= 0x03;//方向键-上
	public static final int SWC_KEY_MENU_DOWN						= 0x04;//方向键-下
	public static final int SWC_KEY_SRC									= 0x07;//源切换
	public static final int SWC_KEY_SPEECH_T90						= 0x08;
	public static final int SWC_KEY_PICK_UP								= 0x09;//接电话
	public static final int SWC_KEY_HANG_UP							= 0x0A;//挂电话(T90,倒车视频预览)
	public static final int SWC_KEY_POWER_T90						= 0x12;
	public static final int SWC_KEY_BACK									= 0x15;//Back
	public static final int SWC_KEY_ENTER								= 0x16;//Enter
	public static final int SWC_KEY_VOL_UP_T90						= 0x20;
	public static final int SWC_KEY_VOL_DOWN_T90					= 0x21;
	public static final int SWC_KEY_BACK_T90							= 0x22;
	public static final int SWC_KEY_MENU_T90							= 0x23;
	public static final int SWC_KEY_MEDIA_T90							= 0x25;
	public static final int SWC_KEY_HOME_T90							= 0x26;
	public static final int SWC_KEY_POWER								= 0x87;//Power
	public static final int SWC_KEY2_UP									= 0x60;//上
	public static final int SWC_KEY2_RIGHT_UP						= 0x61;//右上
	public static final int SWC_KEY2_RIGHT								= 0x62;//右
	public static final int SWC_KEY2_RIGHT_DOWN					= 0x63;//右下
	public static final int SWC_KEY2_DOWN								= 0x64;//下
	public static final int SWC_KEY2_LEFT_DOWN						= 0x65;//左下
	public static final int SWC_KEY2_LEFT									= 0x66;//左
	public static final int SWC_KEY2_LEFT_UP							= 0x67;//左上
	public static final int SWC_KEY2_ENTER								= 0x70;//OK
	public static final int SWC_KEY2_BACK								= 0x71;//BACK
	public static final int SWC_KEY2_MAP									= 0x72;//MAP
	public static final int SWC_KEY2_MENU								= 0x73;//MENU
	public static final int SWC_KEY2_SCROLL_RIGHT					= 0x74;//左旋
	public static final int SWC_KEY2_SCROLL_LEFT						= 0x75;//右旋
	
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
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_RIGHT_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_RIGHT, VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_RIGHT_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_LEFT_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_LEFT, VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_LEFT_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_MAP, VehiclePropertyConstants.USER_KEY_NAVI);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_MENU, VehiclePropertyConstants.USER_KEY_ROOT_MENU);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_SCROLL_RIGHT, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY2_SCROLL_LEFT, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SPEECH_T90, VehiclePropertyConstants.USER_KEY_VOICE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_POWER_T90, VehiclePropertyConstants.USER_KEY_POWER);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOL_UP_T90, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOL_DOWN_T90, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BACK_T90, VehiclePropertyConstants.USER_KEY_BACK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_T90, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MEDIA_T90, VehiclePropertyConstants.USER_KEY_MUSIC);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_HOME_T90, VehiclePropertyConstants.USER_KEY_HOME);
	}
	
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
			VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY,
			VehicleInterfaceProperties.VIM_KEY_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,
	};		
		
	//西玛低配
	public static final int[] VEHICLE_CAN_PROPERITIES_CIMA_L = new int[] { 
			VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,
			VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,
			VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,
	};
	
	//西玛中高配
	public static final int[] VEHICLE_CAN_PROPERITIES_CIMA_H = new int[] { 
			VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_PROPERTY,
			VehicleInterfaceProperties.VIM_SURROUND_VOLUME_PROPERTY,
			VehicleInterfaceProperties.VIM_BOSE_CENTERPOINT_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_DRIVER_SEAT_AUDIO_STATUS_PROPERTY
	};
		
	//奇骏中低配
	public static final int[] VEHICLE_CAN_PROPERITIES_XTRAIL = new int[] {
			VehicleInterfaceProperties.VIM_AVM_CAMERA_STATUS_PROPERTY
	};
	
	//国外奇骏高配
	public static final int[] VEHICLE_CAN_PROPERITIES_XTRAIL_H = new int[] { 
			VehicleInterfaceProperties.VIM_AVM_AUTO_PARK_UI_MODE_PROPERTY,
			VehicleInterfaceProperties.VIM_AVM_AUTO_PARK_TOUCH_CMD_PROPERTY,
			VehicleInterfaceProperties.VIM_AVM_AUTO_PARK_HINT_PROPERTY
	};
	
	//国外JUKE
	public static final int[] VEHICLE_CAN_PROPERITIES_JUKE = new int[] { 
			VehicleInterfaceProperties.VIM_ADAS_BLIND_SPOT_DETECTION_PROPERTY,
			VehicleInterfaceProperties.VIM_ADAS_LANE_DEPARTURE_DETECTION_PROPERTY,
			VehicleInterfaceProperties.VIM_ADAS_MOVING_OBJECT_DETECTION_PROPERTY
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

		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,PROPERITY_PERMISSON_GET);
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,PROPERITY_PERMISSON_GET);
		
		VEHICLE_CAN_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY,	PROPERITY_PERMISSON_SET);

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
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY,VehiclePropertyConstants.DATA_TYPE_STRING);
		
		VEHICLE_CAN_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY,	VehiclePropertyConstants.DATA_TYPE_STRING);

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
