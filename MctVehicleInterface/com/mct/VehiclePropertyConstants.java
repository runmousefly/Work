/*
*    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
*    Qualcomm Technologies Proprietary and Confidential.
*
*/
package com.mct;

import java.nio.channels.ScatteringByteChannel;

/**
 * VehiclePropertyConstants provides constant definations for various vehicle
 * properties. Please note that this API is work in progress, and there might be
 * few changes going forward.
 *
 */
public final class VehiclePropertyConstants
{

	// Data types associated with properties.
	// Please refer to VehicleInterfaceData.getPropertiesDataType.
	public static final int DATA_TYPE_UNKNOWN 		= 0;
	public static final int DATA_TYPE_INTEGER 		= 1;
	public static final int DATA_TYPE_FLOAT 			= 2;
	public static final int DATA_TYPE_STRING 			= 3;
	
	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= 0; 	// NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= 1; 	// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= 2; 	// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= 3;	// GET_SET
	
	//onDataStreamUpdate,streamType
	public static final int DATA_STREAM_TYPE_CAN_RT_STREAM 			= 0;	//实时数据流类型
	public static final int DATA_STREAM_TYPE_CAN_DRIVING_HABITS = 1;	//驾驶习惯数据流类型
	public static final int DATA_STREAM_TYPE_CAN_THIS_TRIP 			= 2;	//本次行程数据流类型
	

	// Constant definations for properties related to Transmission
	public static final short VEHICLE_POWERMODE_OFF 				= 1;
	public static final short VEHICLE_POWERMODE_ACC 				= 2;
	public static final short VEHICLE_POWERMODE_RUN 				= 3;
	public static final short VEHICLE_POWERMODE_IGNITION 		= 4;
	
	public static final short TRANSMISSION_GEARSTATUS_NEUTRAL 	= 0;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL1 	= 1;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL2 	= 2;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL3 	= 3;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL4 	= 4;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL5 	= 5;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL6 	= 6;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL7 	= 7;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL8 	= 8;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL9 	= 9;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL10 	= 10;
	
	public static final short TRANSMISSION_GEARSTATUS_AUTO1 		= 11;
	public static final short TRANSMISSION_GEARSTATUS_AUTO2 		= 12;
	public static final short TRANSMISSION_GEARSTATUS_AUTO3 		= 13;
	public static final short TRANSMISSION_GEARSTATUS_AUTO4 		= 14;
	public static final short TRANSMISSION_GEARSTATUS_AUTO5 		= 15;
	public static final short TRANSMISSION_GEARSTATUS_AUTO6 		= 16;
	public static final short TRANSMISSION_GEARSTATUS_AUTO7 		= 17;
	public static final short TRANSMISSION_GEARSTATUS_AUTO8 		= 18;
	public static final short TRANSMISSION_GEARSTATUS_AUTO9 		= 19;
	public static final short TRANSMISSION_GEARSTATUS_AUTO10 		= 20;
	
	public static final short TRANSMISSION_GEARSTATUS_DRIVE 			= 32;
	public static final short TRANSMISSION_GEARSTATUS_PARKING 	= 64;
	public static final short TRANSMISSION_GEARSTATUS_REVERSE 		= 128;
	
	public static final short ENGINE_COOLANT_LEVEL_NORMAL 			= 0;
	public static final short ENGINE_COOLANT_LEVEL_LOW 					= 1;

	// Constant definations for properties related to Safety
	public static final String ABS_AVAILABLE 				= "ABS_AVAILABLE";
	public static final String ABS_IDLE 						= "ABS_IDLE";
	public static final String ABS_ACTIVE 					= "ABS_ACTIVE";
	public static final String TCS_AVAILABLE 				= "TCS_AVAILABLE";
	public static final String TCS_IDLE 						= "TCS_IDLE";
	public static final String TCS_ACTIVE 					= "TCS_ACTIVE";
	public static final String ESC_AVAILABLE 				= "ECS_AVAILABLE";
	public static final String ESC_IDLE 						= "ECS_IDLE";
	public static final String ESC_ACTIVE 					= "ECS_ACTIVE";
	public static final String AIRBAG_ACTIVATE 		= "AIRBAG_ACTIVATE";
	public static final String AIRBAG_DEACTIVATE 	= "AIRBAG_DEACTIVATE";
	public static final String AIRBAG_DEPLOYMENT 	= "AIRBAG_DEPLOYMENT";
	public static final String DOOR_STATUS_OPEN 	= "DOOR_OPEN";
	public static final String DOOR_STATUS_AJAR 		= "DOOR_AJAR";
	public static final String DOOR_STATUS_CLOSE 	= "DOOR_CLOSE";
	public static final String OCCUPANT_ADULT 			= "ADULT_OCCUPANT";
	public static final String OCCUPANT_CHILD 			= "CHILD_OCCUPANT";
	public static final String OCCUPANT_VACANT 		= "VACANT";

	// Constant definations for properties related to Parking
	public static final short SECURITYALERT_AVAILABLE 				= 1;
	public static final short SECURITYALERT_IDLE 						= 2;
	public static final short SECURITYALERT_ACTIVATED 				= 3;
	public static final short SECURITYALERT_ALARM_DETECTED 	= 4;

	// Constant definations for properties related to Vehicle overview
	public static final String VEHICLETYPE_SEDAN 				= "SEDAN";
	public static final String VEHICLETYPE_COUPE 			= "COUPE";
	public static final String VEHICLETYPE_CABRIOLET 		= "CABRIOLET";
	public static final String VEHICLETYPE_ROADSTER 		= "ROADSTER";
	public static final String VEHICLETYPE_SUV 					= "SUV";
	public static final String VEHICLETYPE_TRUCK 			= "TRUCK";
	public static final String VEHICLETYPE_MINIVAN 			= "MINI-VAN";
	public static final String VEHICLETYPE_VAN 				= "VAN";
	
	public static final short FUELTYPE_GASOLINE = 0x01;
	public static final short FUELTYPE_METHANOL = 0x02;
	public static final short FUELTYPE_ETHANOL = 0x03;
	public static final short FUELTYPE_DIESEL = 0x04;
	public static final short FUELTYPE_LPG = 0x05;
	public static final short FUELTYPE_CNG = 0x06;
	public static final short FUELTYPE_PROPANE = 0x07;
	public static final short FUELTYPE_ELECTRIC = 0x08;
	public static final short FUELTYPE_BIFUELRUNNINGGASOLINE = 0x09;
	public static final short FUELTYPE_BIFUELRUNNINGMETHANOL = 0x0A;
	public static final short FUELTYPE_BIFUELRUNNINGETHANOL = 0x0B;
	public static final short FUELTYPE_BIFUELRUNNINGLPG = 0x0C;
	public static final short FUELTYPE_BIFUELRUNNINGCNG = 0x0D;
	public static final short FUELTYPE_BIFUELRUNNINGPROP = 0x0E;
	public static final short FUELTYPE_BIFUELRUNNINGELECTRICITY = 0x0F;
	public static final short FUELTYPE_BIFUELMIXEDGASELECTRIC = 0x10;
	public static final short FUELTYPE_HYBRIDGASOLINE = 0x11;
	public static final short FUELTYPE_HYBRIDETHANOL = 0x12;
	public static final short FUELTYPE_HYBRIDDIESEL = 0x13;
	public static final short FUELTYPE_HYBRIDELECTRIC = 0x14;
	public static final short FUELTYPE_HYBRIDMIXEDFUEL = 0x15;
	public static final short FUELTYPE_HYBRIDREGENERATIVE = 0x16;
	public static final String TRANSMISSIONGEARTYPE_AUTO = "AUTO-TRANSMISSION";
	public static final String TRANSMISSIONGEARTYPE_MANUAL = "MANUAL-TRANSMISSION";
	public static final String TRANSMISSIONGEARTYPE_CVT = "CVT-TRANSMISSION";

	// Constant definations for properties related to Media
	public static final short MEDIA_AUDIO_SOURCE_USB = 0;
	public static final short MEDIA_AUDIO_SOURCE_BT = 1;
	public static final short MEDIA_AUDIO_SOURCE_RADIO = 2;
	public static final short MEDIA_AUDIO_SOURCE_CD = 3;
	public static final short MEDIA_AUDIO_SOURCE_IPOD = 4;

	// Constant definations for properties related to Maintenance
	public static final short TIREPRESSURESTATUS_NORMAL = 0;
	public static final short TIREPRESSURESTATUS_LOW = 1;
	public static final short TIREPRESSURESTATUS_HIGH = 2;

	// Constant definations for properties related to driver specific
	// personalization
	public static final short ENGLISH = 1;
	public static final short SPANISH = 2;
	public static final short FRENCH = 3;
	public static final short DRIVING_MODE_COMFORT = 1;
	public static final short DRIVING_MODE_AUTO = 2;
	public static final short DRIVING_MODE_SPORT = 3;
	public static final short DRIVING_MODE_ECO = 4;
	public static final short DRIVING_MODE_MANUAL = 5;
	public static final short GENERATED_VEHICLE_SOUNDMODE_NORMAL = 1;
	public static final short GENERATED_VEHICLE_SOUNDMODE_QUIET = 2;
	public static final short GENERATED_VEHICLE_SOUNDMODE_SPORTIVE = 3;

	// Constant definations for properties related to Climate information
	public static final short RAIN_SENSOR_NORAIN = 0;
	public static final short RAIN_SENSOR_LEVEL1 = 1;
	public static final short RAIN_SENSOR_LEVEL2 = 2;
	public static final short RAIN_SENSOR_LEVEL3 = 3;
	public static final short RAIN_SENSOR_LEVEL4 = 4;
	public static final short RAIN_SENSOR_LEVEL5 = 5;
	public static final short RAIN_SENSOR_LEVEL6 = 6;
	public static final short RAIN_SENSOR_LEVEL7 = 7;
	public static final short RAIN_SENSOR_LEVEL8 = 8;
	public static final short RAIN_SENSOR_LEVEL9 = 9;
	public static final short RAIN_SENSOR_HEAVIESTRAIN = 10;
	public static final short WIPER_OFF = 0;
	public static final short WIPER_ONCE = 1;
	public static final short WIPER_SLOWEST = 2;
	public static final short WIPER_SLOW = 3;
	public static final short WIPER_FAST = 4;
	public static final short WIPER_FASTEST = 5;
	public static final short WIPER_AUTO = 10;
	public static final short HVACFAN_DIRECTION_FRONTPANEL = 1;
	public static final short HVACFAN_DIRECTION_FLOORDUCT = 2;
	public static final short HVACFAN_DIRECTION_FRONTFLOOR = 3;
	public static final short HVACFAN_DIRECTION_DEFROSTERFLOOR = 4;
	
	//VIM_CAN_REQ_COMMAND_PROPERTY
	public static final short CAN_CMD_REQ_ENGINE_LOAD							= 0;	//请求发动机负荷
	public static final short CAN_CMD_REQ_COOLANT_TEMP							= 1;	//请求冷却液温度
	public static final short CAN_CMD_REQ_FUEL_PRESSURE						= 2;	//请求燃油压力
	public static final short CAN_CMD_REQ_INPIPE_PRESSURE						= 3;	//请求进气管压力
	public static final short CAN_CMD_REQ_ENGINE_SPEED							= 4;	//请求引擎转速
	public static final short CAN_CMD_REQ_DRIVING_SPEED							= 5;	//请求车速
	public static final short CAN_CMD_REQ_IGNITION_ANGLE						= 6;	//请求点火提前角
	public static final short CAN_CMD_REQ_INTAKE_TEMP							= 7;	//请求进气温度
	public static final short CAN_CMD_REQ_INTAKE_RATE							= 8;	//请求进气流量
	public static final short CAN_CMD_REQ_THROTTLE_POSN						= 9;	//节气门位置
	public static final short CAN_CMD_REQ_ENGINE_RUN_TIME					= 10;	//请求引擎运行时间
	public static final short CAN_CMD_REQ_VACUUM_OIL_RAIL_PRESSURE	= 11;	//请求真空油轨运行压力
	public static final short CAN_CMD_REQ_EGR_DEGREE								= 12;	//请求EGR开度
	public static final short CAN_CMD_REQ_EVA_DEGREE								= 13;	//请求蒸汽清除开度
	public static final short CAN_CMD_REQ_FUEL_RMNG								= 14;	//请求剩余油量
	public static final short CAN_CMD_REQ_VEHICLE_VIN								= 15;	//请求VIN
	public static final short CAN_CMD_REQ_BATTERY_VOLTAGE					= 16;	//请求电瓶电压
	public static final short CAN_CMD_REQ_FUEL_CONSUM_INFO					= 17;	//请求油耗信息
	public static final short CAN_CMD_REQ_ODOMETER_INFO						= 18;	//里程信息
	public static final short CAN_CMD_REQ_DRVING_TIME_INFO 					= 30;	//请求驾驶时间信息，包括本次以及累计的怠速时间与行驶时间
	public static final short CAN_CMD_REQ_MINUS_ODOMETER	 				= 31;	//扣减累计里程，用于里程校准
	public static final short CAN_CMD_REQ_CLEAR_DTC 								= 32;	//清除故障码
	public static final short CAN_CMD_REQ_DEVICE_INFO 							= 33;	//请求设备信息
	public static final short CAN_CMD_REQ_CLEAR_DATA 								= 34;	//请求清除保存信息
	public static final short CAN_CMD_REQ_DRIV_HABIT_DATA					= 35;	//请求驾驶习惯数据
	public static final short CAN_CMD_REQ_HOT_RESTART							= 46;	//热重启
	public static final short CAN_CMD_REQ_SLEEP										= 47;	//进入休眠
	
	
	
	//VIM_MCU_ACC_STAT_PROPERTYE
	public static final short ACC_STATUS_OFF										= 0;
	public static final short ACC_STATUS_ON										= 1;
	
	//MA_CMD_CAR_REVERSE
	public static final short CAR_IN_NO_REVERSE									= 0;	//非倒车状态
	public static final short CAR_IN_REVERSE										= 1;	//倒车状态
	
	//MA_CMD_CAR_BRAKE
	public static final short CAR_BRAKE_OFF											= 0;	//手刹放下
	public static final short CAR_BRAKE_ON											= 1;	//手刹拉起
	
	//VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY
	public static final short CAR_LIGHT_OFF											= 0;	//大灯/转向灯关闭
	public static final short CAR_LIGHT_ON											= 1;	//大灯/转向灯开启
	
	//VIM_MCU_RADIO_CUR_STATE_PROPERTY
	public static final short RADIO_STATUS_NORMAL							= 0; //Normal
	public static final short RADIO_STATUS_SEARCH								= 1; //Search
	public static final short RADIO_STATUS_SCAN									= 2; //Scanl
	
	//VIM_MCU_RADIO_CUR_BAND_PROPERTY
	public static final short RADIO_BAND_FM1										= 0; //FM1
	public static final short RADIO_BAND_FM2										= 1; //FM2
	public static final short RADIO_BAND_FM3										= 2; //FM3
	public static final short RADIO_BAND_AM1										= 3; //AM1
	public static final short RADIO_BAND_AM2										= 4; //AM2
	
	//VIM_MCU_RADIO_CUR_REGION_PROPERTY
	public static final short RADIO_REGION_AMERICA							= 0; //美洲
	public static final short RADIO_REGION_LATIN_AMERICA				= 1; //拉丁美洲
	public static final short RADIO_REGION_EUROPE							= 2; //欧洲
	public static final short RADIO_REGION_OIRT									= 3; //OIRT
	public static final short RADIO_REGION_JAPAN								= 4; //日本
	public static final short RADIO_REGION_SOUTH_AMERICA				= 5; //南美洲
	public static final short RADIO_REGION_EAST_EUROPE					= 6; //东欧
	
	// VIM_VEHICLE_MCU_LAST_SLEEP_REASON
	public static final short MCU_SLEEP_REASON_ACC_OFF 					= 0; 		// ACC断开
	public static final short MCU_SLEEP_REASON_HIGHT_VOLTAGE 		= 1; 		// 高压
	public static final short MCU_SLEEP_REASON_LOW_VOLTAGE 			= 2; 		// 低压

	// VIM_VEHICLE_MCU_UPGRADE_STATE
	public static final short MCU_UPGRADE_STATE_UNKNOWN = -1; // 未知状态
	public static final short MCU_UPGRADE_STATE_INIT = 0; // 升级初始化
	public static final short MCU_UPGRADE_STATE_START = 1; // 升级开始
	public static final short MCU_UPGRADE_STATE_ERASE = 2; // 擦除数据
	public static final short MCU_UPGRADE_STATE_WRITE = 3; // 数据写入中
	public static final short MCU_UPGRADE_STATE_VERTIFY = 4; // 数据确认中
	public static final short MCU_UPGRADE_STATE_SUCCESS = 5; // 升级成功
	public static final short MCU_UPGRADE_STATE_FAILED = 6; // 升级失败

	//VIM_MCU_STATUS_PROPERTY
	public static final short MCU_STATE_UNKNOWN 	= -1; 	// 未知状态,未收到MCU返回数据时的状态
	public static final short MCU_STATE_BOOT 			= 0; 		// Normal状态
	public static final short MCU_STATE_APP 		= 1; 			// 正常状态

	//VIM_MCU_REQ_COMMAND_PROPERTY
	public static final short MCU_CMD_REQ_VERSION 						= 0; 	// 请求MCU版本号
	public static final short MCU_CMD_REQ_VOLTAGE 						= 1;	// 请求MCU当前电压值
	public static final short MCU_CMD_REQ_LAST_SLEEP_REASON 	= 2;	// 请求MCU上次休眠原因
	public static final short MCU_CMD_REQ_UPGRADE 						= 3;	// 请求MCU升级
	public static final short MCU_CMD_REQ_RESET 							= 4;	// 请求MCU重启
	//仅为调试时使用，正常情况下service启动后串口一直处于打开状态
	public static final short MCU_CMD_REQ_OPEN_SERIAL 				= 110;	// 请求串口打开
	public static final short MCU_CMD_REQ_CLOSE_SERIAL 				= 111;	// 请求串口关闭

}
