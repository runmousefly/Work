package com.mct.carmodels;

import java.nio.channels.ScatteringByteChannel;

import org.apache.http.auth.NTCredentials;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.service.notification.INotificationListener;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class RZC_PeugeotSeriesProtocol
{
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//CanBox->HU
	public static final int CH_CMD_BACKLIGHT_INFO 				= 0x01;
	public static final int CH_CMD_STEERING_WHEEL_KEY 		= 0x02;
	public static final int CH_CMD_AIR_CONDITIONING_INFO 	= 0x21;
	public static final int CH_CMD_STEERING_WHEEL_ANGLE 	= 0x29;
	public static final int CH_CMD_FULL_RADAR_INFO 				= 0x30;
	public static final int CH_CMD_REVERSE_RADAR_INFO 		= 0x32;
	public static final int CH_CMD_COMPUTE_INFO_PAGE0		= 0x33;
	public static final int CH_CMD_COMPUTE_INFO_PAGE1		= 0x34;
	public static final int CH_CMD_COMPUTE_INFO_PAGE2		= 0x35;
	public static final int CH_CMD_EXTERN_TEMP						= 0x36;
	public static final int CH_CMD_ALARM_RECORD_INFO			= 0x37;
	public static final int CH_CMD_VEHICLE_STATUS					= 0x38;
	public static final int CH_CMD_VEHICLE_FUNC_INFO			= 0x39;
	public static final int CH_CMD_VEHICLE_DIAGNOSTIC_INFO	= 0x3A;
	public static final int CH_CMD_RECORD_SPEED_VALUE		= 0x3B;
	public static final int CH_CMD_SPEED_INFO							= 0x3D;
	public static final int CH_CMD_SPEED_ALARM_DIALOG			= 0x3F;
	public static final int CH_CMD_PROTOCOL_VERSION_INFO 	= 0x7F;//协议版本信息
	
	//HU->CanBox
	public static final int HC_CMD_VEHICLE_PARAM_SETTING   = 0x80;//车辆参数设置
	public static final int HC_CMD_COMPUTE_PARAM_SETTING	= 0x82;//行车电脑参数设置
	public static final int HC_CMD_REQ_VEHICLE_ALARM_INFO	= 0x85;//请求汽车报警记录信息
	public static final int HC_CMD_REQ_FUNCTION_STATUS_INFO	= 0x86;//请求汽车功能状态信息
	public static final int HC_CMD_VEHICLE_DISGNOSTIC_INFO	= 0x87;//请求汽车诊断信息
	public static final int HC_CMD_SET_RECORD_SPEED			= 0x88;//设置记忆速度值
	public static final int HC_CMD_SET_SPEED_INFO					= 0x89;//设置巡航速度、速度限值
	public static final int HC_CMD_AC_CONTROL						= 0x8A;//空调控制
	public static final int HC_CMD_SCREEN_DISPLAY					= 0x8C;//液晶仪表盘显示模式
	public static final int HC_CMD_REQ_CONTROL_INFO			= 0x8F;//请求显示信息命令
	public static final int HC_CMD_SET_SPEEDOMETER				= 0x99;//巡航速度和速度限制仪表设置
	public static final int HC_CMD_SET_TIME								= 0xA6;//时间设置信息
	
	public static final int SWC_KEY_NONE									= 0x00;//无按键或者按键释放
	public static final int SWC_KEY_MENU									= 0x02;//MENU
	public static final int SWC_KEY_MENU_UP							= 0x03;//方向键-上
	public static final int SWC_KEY_MENU_DOWN						= 0x04;//方向键-下
	public static final int SWC_KEY_OK										= 0x07;//OK
	public static final int SWC_KEY_ESC									= 0x08;//ESC
	public static final int SWC_KEY_MODE									= 0x10;//Mode
	public static final int SWC_KEY_SRC									= 0x11;//Source
	public static final int SWC_KEY_SEEK_DOWN						= 0x12;//Seek+
	public static final int SWC_KEY_SEEK_UP								= 0x13;//Seek-
	public static final int SWC_KEY_VOLUME_UP						= 0x14;//Volume+
	public static final int SWC_KEY_VOLUME_DOWN					= 0x15;//Volume-
	public static final int SWC_KEY_MUTE									= 0x16;//Mute
	public static final int SWC_KEY_MEMO_UP							= 0x17;//上一曲
	public static final int SWC_KEY_MEMO_DOWN						= 0x18;//下一曲
	public static final int SWC_KEY_PAGE_SW							= 0x20;//电脑信息页切换
	public static final int SWC_KEY_MENU4								= 0x21;//菜单
	public static final int SWC_KEY_MEMO									= 0x22;//记忆速度界面
	public static final int SWC_KEY_BT										= 0x23;//蓝牙按键
	public static final int SWC_KEY_PUSH_TO_TALK					= 0x29;//
	public static final int SWC_KEY_VEHICLE_SETTING				= 0x2A;//车身设置
	public static final int SWC_KEY_VEHICLE_NAVI					= 0x2B;
	public static final int SWC_KEY_MUSIC								= 0x2C;
	public static final int SWC_KEY_BLUETOOTH						= 0x2D;
	public static final int SWC_KEY_APPS									= 0x2E;
	public static final int SWC_KEY_AIR_CONDTION_CONTROL	= 0x2F;
	public static final int SWC_KEY_PHONE_ACCEPT					= 0x30;//电话接听	
	public static final int SWC_KEY_PHONE_HANGUP					= 0x31;//电话挂断
	public static final int SWC_KEY_NAVI									= 0x32;//Navi
	public static final int SWC_KEY_RADIO								= 0x33;//收音
	public static final int SWC_KEY_SETUP								= 0x34;//设置
	public static final int SWC_KEY_ADDR									= 0x35;//Addr
	public static final int SWC_KEY_MEDIA								= 0x36;//Media
	public static final int SWC_KEY_TRAF									= 0x37;//Traf
	public static final int SWC_KEY_UP										= 0x38;//Up
	public static final int SWC_KEY_DOWN								= 0x39;//Down
	public static final int SWC_KEY_LEFT									= 0x40;//Left
	public static final int SWC_KEY_RIGHT								= 0x41;//Right
	public static final int SWC_KEY_SCROLL_UP						= 0x42;//ScrollUp
	public static final int SWC_KEY_SCROLL_DOWN					= 0x43;//ScrollDown
	public static final int SWC_KEY_NUM1									= 0x44;//1
	public static final int SWC_KEY_NUM2									= 0x45;//2
	public static final int SWC_KEY_NUM3									= 0x46;//3
	public static final int SWC_KEY_NUM4									= 0x47;//4
	public static final int SWC_KEY_NUM5									= 0x48;//5
	public static final int SWC_KEY_NUM6									= 0x49;//6
	public static final int SWC_KEY_SRC2									= 0x4A;//SRC
	public static final int SWC_KEY_BAND									= 0x50;//Band
	public static final int SWC_KEY_LIST									= 0x51;//List
	public static final int SWC_KEY_SOUND								= 0x52;//Sound
	public static final int SWC_KEY_TA										= 0x53;//TA
	public static final int SWC_KEY_DARK									= 0x54;//Dark
	public static final int SWC_KEY_EJECT									= 0x55;//出碟
	public static final int SWC_KEY_RIGHT2								= 0x56;//右
	public static final int SWC_KEY_LEFT2									= 0x57;//左
	public static final int SWC_KEY_UP2									= 0x58;//上
	public static final int SWC_KEY_DOWN2								= 0x59;//下
	public static final int SWC_KEY_MENU2								= 0x5A;//菜单	
	public static final int SWC_KEY_MENU3								= 0x5B;//菜单	
	public static final int SWC_KEY_OK2									= 0x5C;//
	public static final int SWC_KEY_MUTE2								= 0x5D;
	public static final int SWC_KEY_BACK									= 0x5E;//返回
	public static final int SWC_KEY_CHECK								= 0x60;//Check
	public static final int SWC_KEY_POWER								= 0x80;//Power

		
	public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
	static
	{

		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU_DOWN, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_OK, VehiclePropertyConstants.USER_KEY_OK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_ESC	, VehiclePropertyConstants.USER_KEY_BACK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MODE, VehiclePropertyConstants.USER_KEY_MODE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC	, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_DOWN, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SEEK_UP, VehiclePropertyConstants.USER_KEY_RADIO_SEARCH_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MEMO_UP, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MEMO_DOWN, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PAGE_SW, VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BT, VehiclePropertyConstants.USER_KEY_BLUETOOTH);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PUSH_TO_TALK, VehiclePropertyConstants.USER_KEY_VOICE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VEHICLE_SETTING, VehiclePropertyConstants.USER_KEY_VEHICLE_SETTING);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_VEHICLE_NAVI, VehiclePropertyConstants.USER_KEY_NAVI);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUSIC, VehiclePropertyConstants.USER_KEY_MUSIC);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BLUETOOTH, VehiclePropertyConstants.USER_KEY_BLUETOOTH);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_APPS, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_AIR_CONDTION_CONTROL, VehiclePropertyConstants.USER_KEY_AIR_CONDITON_CONTROL);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_ACCEPT, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_PHONE_HANGUP, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_NAVI, VehiclePropertyConstants.USER_KEY_NAVI);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RADIO	, VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SETUP, VehiclePropertyConstants.USER_KEY_SYS_SETTING);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_ADDR, VehiclePropertyConstants.USER_KEY_NAVI_ADDR);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MEDIA, VehiclePropertyConstants.USER_KEY_MUSIC);
		//SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TRAF, VehiclePropertyConstants.USER_KEY_TRAF);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN	, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_LEFT, VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RIGHT	, VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SCROLL_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SCROLL_DOWN	, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_NUM1, VehiclePropertyConstants.USER_KEY_NUM1);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_NUM2, VehiclePropertyConstants.USER_KEY_NUM2);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_NUM3, VehiclePropertyConstants.USER_KEY_NUM3);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_NUM4, VehiclePropertyConstants.USER_KEY_NUM4);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_NUM5, VehiclePropertyConstants.USER_KEY_NUM5);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_NUM6, VehiclePropertyConstants.USER_KEY_NUM6);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SRC2, VehiclePropertyConstants.USER_KEY_SOURCE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BAND, VehiclePropertyConstants.USER_KEY_RADIO);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_LIST, VehiclePropertyConstants.USER_KEY_LIST);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_SOUND, VehiclePropertyConstants.USER_KEY_AUDIO_SET);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_TA, VehiclePropertyConstants.USER_KEY_RADIO);
		//SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DARK, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_EJECT, VehiclePropertyConstants.USER_KEY_EJECT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_RIGHT2, VehiclePropertyConstants.USER_KEY_RIGHT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_LEFT2, VehiclePropertyConstants.USER_KEY_LEFT);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_UP2	, VehiclePropertyConstants.USER_KEY_UP);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_DOWN2, VehiclePropertyConstants.USER_KEY_DOWN);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_CHECK, VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);

		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MUTE2, VehiclePropertyConstants.USER_KEY_MUTE);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MEMO, VehiclePropertyConstants.USER_KEY_VEHICLE_SETTING);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU2, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU3, VehiclePropertyConstants.USER_KEY_HOME);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_BACK, VehiclePropertyConstants.USER_KEY_BACK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_OK2, VehiclePropertyConstants.USER_KEY_OK);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_POWER, VehiclePropertyConstants.USER_KEY_POWER);
		SWC_KEY_TO_USER_KEY_TABLE.put(SWC_KEY_MENU4, VehiclePropertyConstants.USER_KEY_HOME);
	}
	
	
	public static final float AC_LOWEST_TEMP_C 	= 0.5f;//最低温度
	public static final float AC_HIGHEST_TEMP_C	= 127.0f;//最高温度
	
	public static final float AC_LOWEST_TEMP_F 	= 1.0f;//最低温度
	public static final float AC_HIGHEST_TEMP_F	= 254.0f;//最高温度
	
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
		
	/*
	//标志车系，报警记录信息、功能状态信息、诊断信息编号以及对应的信息描述
	public static final SparseArray<String> PEUGEOT_VEHICLE_INFO = new SparseArray<String>();
	static
	{
		PEUGEOT_VEHICLE_INFO.put(0x00, "无功能状态信息、无报警记录、诊断正常");
		PEUGEOT_VEHICLE_INFO.put(0x01, "驻车辅助启用");
		PEUGEOT_VEHICLE_INFO.put(0x02, "驻车辅助停止");
		PEUGEOT_VEHICLE_INFO.put(0x03, "乘客正面气囊启用");
		PEUGEOT_VEHICLE_INFO.put(0x04, "乘客正面气囊停用");
		PEUGEOT_VEHICLE_INFO.put(0x05, "车门自锁启用");
		PEUGEOT_VEHICLE_INFO.put(0x06, "车门自锁停用");
		PEUGEOT_VEHICLE_INFO.put(0x07, "车门已锁");
		PEUGEOT_VEHICLE_INFO.put(0x08, "车门已解锁");
		PEUGEOT_VEHICLE_INFO.put(0x09, "倒车时后雨刷器启动");
		PEUGEOT_VEHICLE_INFO.put(0x0A, "倒车时后雨刷器停用");
		PEUGEOT_VEHICLE_INFO.put(0x0B, "引擎温度故障,请停车");
		PEUGEOT_VEHICLE_INFO.put(0x0C, "请加注冷却液");
		PEUGEOT_VEHICLE_INFO.put(0x0D, "请加引擎机油");
		PEUGEOT_VEHICLE_INFO.put(0x0E, "引擎机油压力故障，请停车");
		PEUGEOT_VEHICLE_INFO.put(0x0F, "制动系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x10, "不可操作");
		PEUGEOT_VEHICLE_INFO.put(0x11, "车门、行李箱、引擎盖、后风窗或油箱未关");
		PEUGEOT_VEHICLE_INFO.put(0x12, "多个轮胎爆裂");
		PEUGEOT_VEHICLE_INFO.put(0x13, "微粒过滤器有堵塞风险：请参考说明书");
		PEUGEOT_VEHICLE_INFO.put(0x14, "悬架故障 限速：90km/h");
		PEUGEOT_VEHICLE_INFO.put(0x15, "悬架系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x16, "助力转向故障");
		PEUGEOT_VEHICLE_INFO.put(0x17, "10km/h");
		PEUGEOT_VEHICLE_INFO.put(0x18, "驻车制动启用");
		PEUGEOT_VEHICLE_INFO.put(0x19, "驻车制动解除");
		PEUGEOT_VEHICLE_INFO.put(0x1A, "制动操控故障驻车制动自动激活");
		PEUGEOT_VEHICLE_INFO.put(0x1B, "制动片磨碎");
		PEUGEOT_VEHICLE_INFO.put(0x1C, "驻车制动故障");
		PEUGEOT_VEHICLE_INFO.put(0x1D, "可变尾翼功能缺失限速，请参考说明书");
		PEUGEOT_VEHICLE_INFO.put(0x1E, "ABS 故障");
		PEUGEOT_VEHICLE_INFO.put(0x1F, "ESP/ASR 系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x20, "变速箱系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x21, "变速箱故障：请维修车辆");
		PEUGEOT_VEHICLE_INFO.put(0x22, "巡航系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x23, "微粒过滤器添加剂不足：请维修车辆");
		PEUGEOT_VEHICLE_INFO.put(0x24, "电子防盗系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x25, "驻车辅助系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x26, "车位测量系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x27, "蓄电池充电或供电系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x28, "轮胎气压不足");
		PEUGEOT_VEHICLE_INFO.put(0x29, "近光灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x2A, "远光灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x2B, "制动灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x2C, "雾灯灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x2D, "转向灯灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x2E, "倒车灯炮坏");
		PEUGEOT_VEHICLE_INFO.put(0x2F, "位置灯灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x30, "换挡杠换到\"P\"档");
		PEUGEOT_VEHICLE_INFO.put(0x31, "当心路面结冰");
		PEUGEOT_VEHICLE_INFO.put(0x32, "当心手刹");
		PEUGEOT_VEHICLE_INFO.put(0x33, "风窗洗涤液不足");
		PEUGEOT_VEHICLE_INFO.put(0x34, "燃油不足");
		PEUGEOT_VEHICLE_INFO.put(0x35, "供油切断");
		PEUGEOT_VEHICLE_INFO.put(0x36, "遥控器电量不足");
		PEUGEOT_VEHICLE_INFO.put(0x37, "轮胎气压未监控");
		PEUGEOT_VEHICLE_INFO.put(0x38, "高速、检测胎压是否合适");
		PEUGEOT_VEHICLE_INFO.put(0x39, "轮胎气压不足");
		PEUGEOT_VEHICLE_INFO.put(0x3A, "诊断中");
		PEUGEOT_VEHICLE_INFO.put(0x3B, "诊断完成");
		PEUGEOT_VEHICLE_INFO.put(0x3C, "左右安全带未系");
		PEUGEOT_VEHICLE_INFO.put(0x3D, "后座中间安全带未系");
		PEUGEOT_VEHICLE_INFO.put(0x3E, "左后安全带未系");
		PEUGEOT_VEHICLE_INFO.put(0x3F, "右后安全带未系");
		PEUGEOT_VEHICLE_INFO.put(0x40, "自动雨刮器启用");
		PEUGEOT_VEHICLE_INFO.put(0x41, "自动雨刮器停用");
		PEUGEOT_VEHICLE_INFO.put(0x42, "大灯自动点亮启用");
		PEUGEOT_VEHICLE_INFO.put(0x43, "大灯自动点亮停用");
		PEUGEOT_VEHICLE_INFO.put(0x44, "无法开启硬顶：隔板未放");
		PEUGEOT_VEHICLE_INFO.put(0x45, "硬顶操作完成");
		PEUGEOT_VEHICLE_INFO.put(0x46, "立即结束硬顶操作");
		PEUGEOT_VEHICLE_INFO.put(0x47, "操作失败：硬顶已锁");
		PEUGEOT_VEHICLE_INFO.put(0x48, "硬顶折叠机械故障");
		PEUGEOT_VEHICLE_INFO.put(0x49, "操作失败：后风窗未关");
		PEUGEOT_VEHICLE_INFO.put(0x4A, "防倒滑系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x4B, "驾驶模式：正常模式");
		PEUGEOT_VEHICLE_INFO.put(0x4C, "驾驶模式：泥地模式");
		PEUGEOT_VEHICLE_INFO.put(0x4D, "驾驶模式：雪地模式");
		PEUGEOT_VEHICLE_INFO.put(0x4E, "驾驶模式：沙地模式");
		PEUGEOT_VEHICLE_INFO.put(0x4F, "ESP 系统停用");
		PEUGEOT_VEHICLE_INFO.put(0x50, "行车间距不可测量：能见度过低");
		PEUGEOT_VEHICLE_INFO.put(0x51, "行车间距不可测量：正在初始化");
		PEUGEOT_VEHICLE_INFO.put(0x52, "活动顶棚操作失败：膨体未到位");
		PEUGEOT_VEHICLE_INFO.put(0x53, "行李箱盖未关");
		PEUGEOT_VEHICLE_INFO.put(0x54, "油箱口盖未锁好");
		PEUGEOT_VEHICLE_INFO.put(0x55, "右后车门未关");
		PEUGEOT_VEHICLE_INFO.put(0x56, "左后车门未关");
		PEUGEOT_VEHICLE_INFO.put(0x57, "右前车门未关");
		PEUGEOT_VEHICLE_INFO.put(0x58, "左前车门未关");
		PEUGEOT_VEHICLE_INFO.put(0x59, "当心手刹");
		PEUGEOT_VEHICLE_INFO.put(0x5A, "安全气囊或安全带预张紧故障");
		PEUGEOT_VEHICLE_INFO.put(0x5B, "柴油滤清器进水：请维修车辆");
		PEUGEOT_VEHICLE_INFO.put(0x5C, "胎压监测系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x5D, "无法开启硬顶：系统温度过高(左后轮胎气压不足：请充气，然后重新初始化)");
		PEUGEOT_VEHICLE_INFO.put(0x5E, "无法开启硬顶：系统温度过高(左后轮胎气压不足：请充气，然后重新初始化)");
		PEUGEOT_VEHICLE_INFO.put(0x5F, "电子防起动故障");
		PEUGEOT_VEHICLE_INFO.put(0x60, "方向盘锁故障：请维修车辆");
		PEUGEOT_VEHICLE_INFO.put(0x61, "检测中间制动灯");
		PEUGEOT_VEHICLE_INFO.put(0x62, "预热/预通风停用:油量不足(巡航系统故障)");
		PEUGEOT_VEHICLE_INFO.put(0x63, "预热/预通风停用:电量不足");
		PEUGEOT_VEHICLE_INFO.put(0x64, "混合动力系统故障:请限速,维修车辆");
		PEUGEOT_VEHICLE_INFO.put(0x65, "混合动力系统故障:请维修车辆");
		PEUGEOT_VEHICLE_INFO.put(0x66, "大灯自动调节系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x67, "混合动力系统故障:请停车并参考说明书");
		PEUGEOT_VEHICLE_INFO.put(0x68, "无法开启硬顶：系统温度过高(左前轮胎气压不足：请充气，然后重新初始化)");
		PEUGEOT_VEHICLE_INFO.put(0x69, "无法开启硬顶：系统温度过高(右前轮胎气压不足：请充气，然后重新初始化)");
		PEUGEOT_VEHICLE_INFO.put(0x6A, "胎压检测系统故障(轮胎气压未监控)");
		PEUGEOT_VEHICLE_INFO.put(0x6B, "拖车连接故障:请维修车辆(光敏传感器故障)");
		PEUGEOT_VEHICLE_INFO.put(0x6C, "预热/预通风停用:调节时钟");
		PEUGEOT_VEHICLE_INFO.put(0x6D, "碰撞风险探测故障（发动机罩未关）");
		PEUGEOT_VEHICLE_INFO.put(0x6E, "随动转向前大灯故障");
		PEUGEOT_VEHICLE_INFO.put(0x6F, "自动手刹系统故障（发动机罩未关）");
		PEUGEOT_VEHICLE_INFO.put(0x70, "车道偏离报警故障");
		PEUGEOT_VEHICLE_INFO.put(0x71, "传感器故障:左后轮胎气压未监控");
		PEUGEOT_VEHICLE_INFO.put(0x72, "传感器故障:右后轮胎气压未监控");
		PEUGEOT_VEHICLE_INFO.put(0x73, "传感器故障:右前轮胎气压未监控");
		PEUGEOT_VEHICLE_INFO.put(0x74, "传感器故障:左前轮胎气压未监控");
		PEUGEOT_VEHICLE_INFO.put(0x75, "胎压检测系统故障（填充防污染添加剂）");
		PEUGEOT_VEHICLE_INFO.put(0x76, "发动机（引擎）故障:请停车");
		PEUGEOT_VEHICLE_INFO.put(0x77, "发动机（引擎）故障:请维修车辆");
		PEUGEOT_VEHICLE_INFO.put(0x78, "行车间距测量故障");
		PEUGEOT_VEHICLE_INFO.put(0x79, "填充防污染添加剂:禁止启动");
		PEUGEOT_VEHICLE_INFO.put(0x7A, "填充防污染添加剂");
		PEUGEOT_VEHICLE_INFO.put(0x7B, "左前轮胎气压不足:请充气,然后重新初始化(防污染系统故障：禁止启动)");
		PEUGEOT_VEHICLE_INFO.put(0x7C, "右前轮胎气压不足:请充气,然后重新初始化(防污染系统故障：禁止启动)");
		PEUGEOT_VEHICLE_INFO.put(0x7D, "左后轮胎气压不足:请充气,然后重新初始化(防污染系统故障：禁止启动)");
		PEUGEOT_VEHICLE_INFO.put(0x7E, "右后轮胎气压不足:请充气,然后重新初始化(防污染系统故障：禁止启动)");
		PEUGEOT_VEHICLE_INFO.put(0x7F, "左后轮胎破裂:更换或修理轮胎");
		PEUGEOT_VEHICLE_INFO.put(0x80, "右后轮胎破裂:更换或修理轮胎");
		PEUGEOT_VEHICLE_INFO.put(0x81, "右前轮胎破裂:更换或修理轮胎");
		PEUGEOT_VEHICLE_INFO.put(0x82, "左前轮胎破裂:更换或修理轮胎");
		PEUGEOT_VEHICLE_INFO.put(0x83, "右后制动灯灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x84, "左后制动灯灯泡坏");
		PEUGEOT_VEHICLE_INFO.put(0x85, "防污染系统故障");
		PEUGEOT_VEHICLE_INFO.put(0x86, "防污染系统故障:禁止启动");
		PEUGEOT_VEHICLE_INFO.put(0x87, "关闭大灯");
		PEUGEOT_VEHICLE_INFO.put(0xF0, "无信息");
		PEUGEOT_VEHICLE_INFO.put(0xFF, "未知信息");
	}
	
	public static String getVehicleInfoByNo(int infoNo)
	{
		return (PEUGEOT_VEHICLE_INFO.indexOfKey(infoNo) >= 0) ? PEUGEOT_VEHICLE_INFO.get(infoNo):null;
	}*/
}
