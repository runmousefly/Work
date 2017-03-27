package com.mct.carmodels;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class RZC_GMSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x01;
	public static final int CH_CMD_PANEL_KEY 							= 0x02;//面板按键
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x03;//空调信息
	public static final int CH_CMD_ILL_INFO								= 0x04;//小灯信息
	public static final int CH_CMD_AIR_CONDITON_CONTROL_INFO		= 0x05;//车身控制信息
	public static final int CH_CMD_VEHICLE_SETTING_INFO		= 0x06;//车身中控设定信息
	public static final int CH_CMD_PARKING_RADAR_SWITCH_INFO		= 0x07;//倒车雷达开关信息
	public static final int CH_CMD_ONSTAR_PHONE_INFO			= 0x08;//OnStar电话信息
	public static final int CH_CMD_ONSTAR_STATUS_INFO		= 0x09;//OnStar状态信息
	public static final int CH_CMD_VEHICLE_SETTING_INFO2		= 0x0A;//中控设置信息2
	public static final int CH_CMD_VEHICLE_SPPED_SIGNAL		= 0x0B;//车速信号
	public static final int CH_CMD_VEHICLE_LANGUAGE				= 0x0C;//语言状态
	public static final int CH_CMD_ALARM_VOLUME					= 0x0D;//警示音音量
	public static final int CH_CMD_REAR_RADAR_INFO 				= 0x22;
	public static final int CH_CMD_FRONT_RADAR_INFO 			= 0x23;
	public static final int CH_CMD_BASE_INFO 							= 0x24;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x26;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x30;
	public static final int CH_CMD_ONSTAR_WIRELESS_ACCESS_POINT = 0x41;//OnStar无线接入点
	public static final int CH_CMD_ONSTAR_WIRELESS_PASSWORD 	= 0x42;//OnStSar无线连接密码
	public static final int CH_CMD_EXTERN_VHICLE_SETTING_INFO	= 0x43;//车身设置 
	
	
	//HU->CanBox
	public static final int HC_CMD_SWITCH          						= 0x81;
	public static final int HC_CMD_AIR_CONDITION_CONTROL  = 0x82;//空调控制
	public static final int HC_CMD_VEHICLE_CONTROL  				= 0x83;//车身中控命令
	public static final int HC_CMD_PARKING_RADAR_SET			= 0x84;//倒车雷达控制
	public static final int HC_CMD_ONSTAR_SET						= 0x85;//OnStar指令
	public static final int HC_CMD_ONSTAR_CALL						= 0x86;//OnStar拨号
	public static final int HC_CMD_LANGUAGE_SET					= 0x87;//语言设置
	public static final int HC_CMD_ALARM_VOLUME					= 0x88;//警示音量设置
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0x90;
	
	//风向
	public static final short AC_FAN_DIR_AUTO 						= 1;//自动送风
	public static final short AC_FAN_DIR_FRONT_WINDOW 		= 2;//前挡风玻璃窗送风
	public static final short AC_FAN_DIR_DOWN 						= 3;//向下送风
	public static final short AC_FAN_DIR_DOWN_AND_CENTER 	= 4;//向下与平行送风
	public static final short AC_FAN_DIR_CENTER 					= 5;//平行送风
	public static final short AC_FAN_DIR_CENTER_AND_UP 		= 6;//平行与向上送风
	public static final short AC_FAN_DIR_UP 							= 7;//向上送风
	public static final short AC_FAN_DIR_UP_AND_DOWN 			= 8;//向上与向下送风
	public static final short AC_FAN_DIR_UP_DOWN_CENTER 	= 9;//向上与向下与平行送风
	
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_VOLUME_UP						= 0x01;//音量加
	public static final int SWC_KEY_VOLUME_DOWN					= 0x02;//音量键
	public static final int SWC_KEY_UP										= 0x03;//方向键-上
	public static final int SWC_KEY_DOWN								= 0x04;//方向键-下
	public static final int SWC_KEY_SRC									= 0x05;//SOURCE
	public static final int SWC_KEY_SPEECH								= 0x06;//语音
	public static final int SWC_KEY_MUTE									= 0x07;
	
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SPEECH, VehiclePropertyConstants.USER_KEY_VOICE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
	}
	
	public static int swcKeyToUserKey(int swcKey)
	{
		return (SWC_KEY_TO_USER_KEY_TABLE.indexOfKey(swcKey) >= 0) ? SWC_KEY_TO_USER_KEY_TABLE.get(swcKey):-1;
	}
	
	
	//通用面板按键
	public static final int COMMON_PANEL_KEY_POWER 				= 0x01;//Power
	public static final int COMMON_PANEL_KEY_PREV 					= 0x02;//Prev
	public static final int COMMON_PANEL_KEY_NEXT 					= 0x03;//Next
	public static final int COMMON_PANEL_KEY_CONFIG 				= 0x04;//Config
	public static final int COMMON_PANEL_KEY_TONE 					= 0x05;//Tone
	public static final int COMMON_PANEL_KEY_BACK					= 0x06;//Back
	public static final int COMMON_PANEL_KEY_RADIO					= 0x07;//Radio
	public static final int COMMON_PANEL_KEY_AUX1					= 0x08;//CD/Aux
	public static final int COMMON_PANEL_KEY_MUTE					= 0x09;//Mute
	public static final int COMMON_PANEL_KEY_NUM1					= 0x0A;//1
	public static final int COMMON_PANEL_KEY_NUM2					= 0x0B;//2
	public static final int COMMON_PANEL_KEY_NUM3					= 0x0C;//3
	public static final int COMMON_PANEL_KEY_NUM4					= 0x0D;//4
	public static final int COMMON_PANEL_KEY_NUM5					= 0x0E;//5
	public static final int COMMON_PANEL_KEY_NUM6					= 0x0F;//6
	public static final int COMMON_PANEL_KEY_LOAD					= 0x10;
	public static final int COMMON_PANEL_KEY_EJECT					= 0x11;
	public static final int COMMON_PANEL_KEY_INFO					= 0x12;
	public static final int COMMON_PANEL_KEY_TIME					= 0x13;
	public static final int COMMON_PANEL_KEY_FAV						= 0x14;
	public static final int COMMON_PANEL_KEY_AS						= 0x15;
	public static final int COMMON_PANEL_KEY_ENTER					= 0x16;
	public static final int COMMON_PANEL_KEY_VOLUME_UP			= 0x17;
	public static final int COMMON_PANEL_KEY_VOLUME_DOWN	= 0x18;
	public static final int COMMON_PANEL_KEY_SEL_UP				= 0x19;
	public static final int COMMON_PANEL_KEY_SEL_DOWN			= 0x1A;
	public static final int COMMON_PANEL_KEY_PLAY_PAUSE		= 0x1B;
	public static final int COMMON_PANEL_KEY_UP						= 0x1C;
	public static final int COMMON_PANEL_KEY_DOWN					= 0x1D;
	public static final int COMMON_PANEL_KEY_AUX2					= 0x40;
	public static final int COMMON_PANEL_KEY_TUNE_UP				= 0x34;
	public static final int COMMON_PANEL_KEY_TUNE_DOWN		= 0x35;
	public static final int COMMON_PANEL_KEY_HOME					= 0x50;
	public static final int COMMON_PANEL_KEY_SOURCE				= 0x51;
	public static final int COMMON_PANEL_KEY_MENU					= 0x53;
	public static final int COMMON_PANEL_KEY_MEDIA					= 0x54;

	
	public static final SparseIntArray COMMON_PANEL_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_PREV, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_NEXT, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_CONFIG, VehiclePropertyConstants.USER_KEY_SYS_SETTING);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_TONE, VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_RADIO, VehiclePropertyConstants.USER_KEY_RADIO);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_AUX1, VehiclePropertyConstants.USER_KEY_AUX1);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_NUM1, VehiclePropertyConstants.USER_KEY_NUM1);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_NUM2, VehiclePropertyConstants.USER_KEY_NUM2);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_NUM3, VehiclePropertyConstants.USER_KEY_NUM3);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_NUM4, VehiclePropertyConstants.USER_KEY_NUM4);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_NUM5, VehiclePropertyConstants.USER_KEY_NUM5);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_NUM6, VehiclePropertyConstants.USER_KEY_NUM6);
		//COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_LOAD, VehiclePropertyConstants.USER_KEY_LOAD);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_EJECT, VehiclePropertyConstants.USER_KEY_EJECT);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_INFO, VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_TIME, VehiclePropertyConstants.USER_KEY_CLOCK);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_FAV, VehiclePropertyConstants.USER_KEY_FAVOR);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_AS, VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_SEL_UP, VehiclePropertyConstants.USER_KEY_UP);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_SEL_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_PLAY_PAUSE, VehiclePropertyConstants.USER_KEY_PLAY_PAUSE);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_UP, VehiclePropertyConstants.USER_KEY_UP);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_AUX2, VehiclePropertyConstants.USER_KEY_AUX2);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_TUNE_UP, VehiclePropertyConstants.USER_KEY_RADIO_STEP_UP);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_TUNE_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_STEP_DOWN);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_HOME, VehiclePropertyConstants.USER_KEY_HOME);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_SOURCE, VehiclePropertyConstants.USER_KEY_SOURCE);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_MENU, VehiclePropertyConstants.USER_KEY_MENU);
		COMMON_PANEL_KEY_TO_USER_KEY_TABLE.put(COMMON_PANEL_KEY_MEDIA, VehiclePropertyConstants.USER_KEY_MUSIC);

	}
	
	public static int commonPanelKeyToUserKey(int panelKey)
	{
		return (COMMON_PANEL_KEY_TO_USER_KEY_TABLE.indexOfKey(panelKey) >= 0) ? COMMON_PANEL_KEY_TO_USER_KEY_TABLE.get(panelKey):-1;
	}
	
	
	/*
	 * GL8(商务版)面板
	 */
	public static final int GL8_PANEL_KEY_PREV 				= 0x01;
	public static final int GL8_PANEL_KEY_NUM5 				= 0x02;
	public static final int GL8_PANEL_KEY_NUM4 				= 0x03;
	public static final int GL8_PANEL_KEY_NUM3 				= 0x04;
	public static final int GL8_PANEL_KEY_POWER 			= 0x05;
	public static final int GL8_PANEL_KEY_INFO				= 0x06;
	public static final int GL8_PANEL_KEY_FAV					= 0x07;
	public static final int GL8_PANEL_KEY_NUM1				= 0x08;
	public static final int GL8_PANEL_KEY_NUM2				= 0x09;
	public static final int GL8_PANEL_KEY_NEXT				= 0x0A;
	public static final int GL8_PANEL_KEY_ENTER				= 0x0B;
	public static final int GL8_PANEL_KEY_BACK				= 0x0C;
	public static final int GL8_PANEL_KEY_TONE				= 0x0D;
	public static final int GL8_PANEL_KEY_EJECT				= 0x0E;
	public static final int GL8_PANEL_KEY_CONFIG			= 0x11;
	public static final int GL8_PANEL_KEY_MEDIA				= 0x12;
	public static final int GL8_PANEL_KEY_PLAY_PAUSE	= 0x13;
	public static final int GL8_PANEL_KEY_RADIO				= 0x14;
	public static final int GL8_PANEL_KEY_MUTE				= 0x15;
	public static final int GL8_PANEL_KEY_NUM6				= 0x16;
	public static final int GL8_PANEL_KEY_VOLUME_UP		= 0x17;
	public static final int GL8_PANEL_KEY_VOLUME_DOWN= 0x18;
	public static final int GL8_PANEL_KEY_SEL_UP			= 0x19;
	public static final int GL8_PANEL_KEY_SEL_DOWN		= 0x1A;
	public static final int GL8_PANEL_KEY_TUNE_UP			= 0x34;
	public static final int GL8_PANEL_KEY_TUNE_DOWN	= 0x35;

	
	public static final SparseIntArray GL8_PANEL_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_PREV, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_NEXT, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_CONFIG, VehiclePropertyConstants.USER_KEY_SYS_SETTING);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_TONE, VehiclePropertyConstants.USER_KEY_AUDIO_BALANCE);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_NUM1, VehiclePropertyConstants.USER_KEY_NUM1);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_NUM2, VehiclePropertyConstants.USER_KEY_NUM2);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_NUM3, VehiclePropertyConstants.USER_KEY_NUM3);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_NUM4, VehiclePropertyConstants.USER_KEY_NUM4);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_NUM5, VehiclePropertyConstants.USER_KEY_NUM5);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_NUM6, VehiclePropertyConstants.USER_KEY_NUM6);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_EJECT, VehiclePropertyConstants.USER_KEY_EJECT);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_INFO, VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_FAV, VehiclePropertyConstants.USER_KEY_FAVOR);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_SEL_UP, VehiclePropertyConstants.USER_KEY_UP);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_SEL_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_PLAY_PAUSE, VehiclePropertyConstants.USER_KEY_PLAY_PAUSE);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_TUNE_UP, VehiclePropertyConstants.USER_KEY_RADIO_STEP_UP);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_TUNE_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_STEP_DOWN);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_MEDIA, VehiclePropertyConstants.USER_KEY_MUSIC);
		GL8_PANEL_KEY_TO_USER_KEY_TABLE.put(GL8_PANEL_KEY_RADIO, VehiclePropertyConstants.USER_KEY_RADIO);
	}
	
	public static int gl8PanelKeyToUserKey(int panelKey)
	{
		return (GL8_PANEL_KEY_TO_USER_KEY_TABLE.indexOfKey(panelKey) >= 0) ? GL8_PANEL_KEY_TO_USER_KEY_TABLE.get(panelKey):-1;
	}
	
	
	
	/*
	 * 昂科威低配面板按键配置
	 */
	public static final int ENCORE_PANEL_KEY_NUM2				= 0x01;
	public static final int ENCORE_PANEL_KEY_MUTE					= 0x02;
	public static final int ENCORE_PANEL_KEY_BACK					= 0x03;
	public static final int ENCORE_PANEL_KEY_FAV					= 0x04;
	public static final int ENCORE_PANEL_KEY_RADIO				= 0x05;
	public static final int ENCORE_PANEL_KEY_PREV 				= 0x06;
	public static final int ENCORE_PANEL_KEY_POWER 				= 0x07;
	public static final int ENCORE_PANEL_KEY_ENTER				= 0x08;
	public static final int ENCORE_PANEL_KEY_INFO					= 0x09;
	public static final int ENCORE_PANEL_KEY_NUM3 				= 0x0A;
	public static final int ENCORE_PANEL_KEY_NUM4 				= 0x0B;
	public static final int ENCORE_PANEL_KEY_NUM5 				= 0x0C;
	public static final int ENCORE_PANEL_KEY_NUM6				= 0x0D;
	public static final int ENCORE_PANEL_KEY_AS						= 0x10;
	public static final int ENCORE_PANEL_KEY_EJECT				= 0x11;
	public static final int ENCORE_PANEL_KEY_CONFIG				= 0x12;
	public static final int ENCORE_PANEL_KEY_TIME					= 0x13;
	public static final int ENCORE_PANEL_KEY_TONE					= 0x14;
	public static final int ENCORE_PANEL_KEY_NUM1				= 0x15;
	public static final int ENCORE_PANEL_KEY_NEXT					= 0x16;
	public static final int ENCORE_PANEL_KEY_VOLUME_UP		= 0x17;
	public static final int ENCORE_PANEL_KEY_VOLUME_DOWN	= 0x18;
	public static final int ENCORE_PANEL_KEY_SEL_UP				= 0x19;
	public static final int ENCORE_PANEL_KEY_SEL_DOWN		= 0x1A;
	public static final int ENCORE_PANEL_KEY_PLAY_PAUSE		= 0x1B;
	public static final int ENCORE_PANEL_KEY_CD						= 0x36;
	public static final int ENCORE_PANEL_KEY_AUX					= 0x40;

	public static final SparseIntArray ENCORE_PANEL_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_PREV, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_NEXT, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_CONFIG, VehiclePropertyConstants.USER_KEY_SYS_SETTING);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_TONE, VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_NUM1, VehiclePropertyConstants.USER_KEY_NUM1);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_NUM2, VehiclePropertyConstants.USER_KEY_NUM2);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_NUM3, VehiclePropertyConstants.USER_KEY_NUM3);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_NUM4, VehiclePropertyConstants.USER_KEY_NUM4);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_NUM5, VehiclePropertyConstants.USER_KEY_NUM5);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_NUM6, VehiclePropertyConstants.USER_KEY_NUM6);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_EJECT, VehiclePropertyConstants.USER_KEY_EJECT);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_INFO, VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_FAV, VehiclePropertyConstants.USER_KEY_FAVOR);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_ENTER, VehiclePropertyConstants.USER_KEY_OK);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_SEL_UP, VehiclePropertyConstants.USER_KEY_UP);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_SEL_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_PLAY_PAUSE, VehiclePropertyConstants.USER_KEY_PLAY_PAUSE);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_AS, VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_AUX, VehiclePropertyConstants.USER_KEY_AUX1);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_CD, VehiclePropertyConstants.USER_KEY_DVD);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_RADIO, VehiclePropertyConstants.USER_KEY_RADIO);
		ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.put(ENCORE_PANEL_KEY_TIME,VehiclePropertyConstants.USER_KEY_CLOCK);
	}
	
	public static int encorePanelKeyToUserKey(int panelKey)
	{
		return (ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.indexOfKey(panelKey) >= 0) ? ENCORE_PANEL_KEY_TO_USER_KEY_TABLE.get(panelKey):-1;
	}
	
	
	
	public static final float AC_LOWEST_TEMP 	= 17.0f;//最低温度
	public static final float AC_HIGHEST_TEMP 	= 30.5f;//最高温度
	
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
	
	
	
	//原车设置属性ID映射协议命令表
	public static final SparseIntArray VEHICLE_SETTING_PROPID_TO_CMD = new SparseIntArray();
	static
	{
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_FIND_VEHICLE_LIGHT_PROPERTY, 0x00);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY, 0x01);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_PREVENT_AUTO_LOCK_DOOR_OPEN_PROPERTY, 0x02);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SPEED_PROPERTY, 0x03);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SHIFT_TO_P_PROPERTY, 0x04);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_DELAY_LOCK_PROPERTY, 0x05);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_UNLOCK_LIGHT_PROPERTY, 0x06);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_REMOTE_LOCK_FEEDBACK_PROPERTY, 0x07);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_KEY_AND_REMOTE_UNLOCK_MODE_PROPERTY, 0x08);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_MW_REAR_WINDOW_WIPEING_IN_REVERSE_PROPERTY, 0x09);

		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_REMOTE_LOCK_AGAIN_PROPERTY, 0x0A);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_VEHICLE_REMOTE_STARTUP_PROPERTY, 0x0B);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_LOCK_DOOR_MODE_NEAR_PROPERTY, 0x0C);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_KEY_FORGOTTEN_PROMPT_PROPERTY, 0x0D);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_PERSONAL_DRIVE_MODE_PROPERTY, 0x0E);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_AUTO_RELOCK_DOORS_PROPERTY, 0x0F);
		
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_ADAS_FLANK_BLIND_WARNING_PROPERTY, 0x16);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_AUTO_DOOR_LOCK_WITH_PROPERTY, 0x17);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_AUTO_PREVENT_BUMP_PROPERTY, 0x18);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_VEHICLE_STATUS_NOTIFICATION_PROPERTY, 0x19);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_MW_AUTOMATIC_WIPING_IN_RAIN_PROPERTY, 0x1A);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_REMOTE_CONTROL_WINDOW_PROPERTY, 0x1B);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_RAMP_STARTUP_ASSIST_PROPERTY, 0x51);
		VEHICLE_SETTING_PROPID_TO_CMD.put(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, 0x80);

	}
	
	public static int vehicleSettingPropIdToCmd(int propId)
	{
		return (VEHICLE_SETTING_PROPID_TO_CMD.indexOfKey(propId) >= 0) ? VEHICLE_SETTING_PROPID_TO_CMD.get(propId):-1;
	}
		
}
