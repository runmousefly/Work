/*
*    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
*    Qualcomm Technologies Proprietary and Confidential.
*
*/
package com.mct;

import java.nio.channels.ScatteringByteChannel;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.HashMap;

import android.service.notification.INotificationListener;
import android.util.SparseArray;
import android.util.SparseIntArray;

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
	//VIM_VEHICLE_POWER_MODE_PROPERTY
	public static final short VEHICLE_POWERMODE_OFF 				= 1;//断电
	public static final short VEHICLE_POWERMODE_ACC 				= 2;//ACC
	public static final short VEHICLE_POWERMODE_RUN 				= 3;//运行
	public static final short VEHICLE_POWERMODE_IGNITION 		= 4;//点火
	
	//VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY
	//VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY
	//VIM_VEHICLE_LEFT_RADAR_POWER_STATUS_PROPERTY
	//VIM_VEHICLE_RIGHT_RADAR_POWER_STATUS_PROPERTY
	//此四个属性，默认状态应该是1,开机且允许显示
	public static final short RADAR_POWER_STATUS_POWER_OFF					= 0;
	public static final short RADAR_POWER_STATUS_POWER_ON_AND_SHOW	= 1;
	public static final short RADAR_POWER_STATUS_POWER_ON_NOT_SHOW	= 2;
	
	//VIM_TRANSMISSION_GEAR_STATUS_PROPERTY
	public static final short TRANSMISSION_GEARSTATUS_NEUTRAL 	= 0;//空挡
	public static final short TRANSMISSION_GEARSTATUS_MANUAL1 	= 1;//手动挡1
	public static final short TRANSMISSION_GEARSTATUS_MANUAL2 	= 2;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL3 	= 3;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL4 	= 4;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL5 	= 5;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL6 	= 6;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL7 	= 7;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL8 	= 8;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL9 	= 9;
	public static final short TRANSMISSION_GEARSTATUS_MANUAL10 	= 10;
	public static final short TRANSMISSION_GEARSTATUS_AUTO1 		= 11;//自动挡1
	public static final short TRANSMISSION_GEARSTATUS_AUTO2 		= 12;
	public static final short TRANSMISSION_GEARSTATUS_AUTO3 		= 13;
	public static final short TRANSMISSION_GEARSTATUS_AUTO4 		= 14;
	public static final short TRANSMISSION_GEARSTATUS_AUTO5 		= 15;
	public static final short TRANSMISSION_GEARSTATUS_AUTO6 		= 16;
	public static final short TRANSMISSION_GEARSTATUS_AUTO7 		= 17;
	public static final short TRANSMISSION_GEARSTATUS_AUTO8 		= 18;
	public static final short TRANSMISSION_GEARSTATUS_AUTO9 		= 19;
	public static final short TRANSMISSION_GEARSTATUS_AUTO10 		= 20;
	public static final short TRANSMISSION_GEARSTATUS_DRIVE 			= 32;	//D挡
	public static final short TRANSMISSION_GEARSTATUS_PARKING 	= 64;//P挡
	public static final short TRANSMISSION_GEARSTATUS_REVERSE 		= 128;//R挡
	
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

	// Constant definations for properties related to Parking(Reserved for qualcomm)
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
	//VIM_TIRE_PRESSURE_REAR_RIGHT_PROPERTY
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
	
	//VIM_CAN_DEVICE_CONNECT_STATUS
	//30s内没收到串口应答数据认为连接断开
	public static final short CAN_DEVICE_CONNECTING 	= 0;	//尝试连接中...
	public static final short CAN_DEVICE_DISCONNECTED = 1;	//断开连接
	public static final short CAN_DEVICE_CONNECTED 		= 2;	//连接上
	
	//CAN Box Type(Can盒类型)
	//VIM_VEHICLE_CAN_BOX_MODEL_PROPERTY
	public static final short CAN_BOX_NONE    								= -1;//无CAN盒(通用机)
	public static final short CAN_BOX_RZC									= 0;//睿智诚
	public static final short CAN_BOX_SS										= 1;//尚摄
	public static final short CAN_BOX_XP										= 2;//欣普
	
	
	//车系
	public static final short CAR_SERIES_NONE 				= -1;//通用机
	public static final short CAR_SERIES_VOLKSWAGEN 	= 0;//大众车系
	public static final short CAR_SERIES_HONDA 				= 1;//本田车系
	public static final short CAR_SERIES_NISSAN 				= 2;//日产车系
	public static final short CAR_SERIES_TOYOTA 			= 3;//丰田车系
	
	
	//Car Model(车型)
	//VIM_VEHICLE_MODEL_PROPERTY
	
	public static final short CAR_MODEL_NONE    								= -1;//无车型(通用机)
	
	//大众系列(0-49)
	public static final short CAR_MODEL_VOLKSWAGEN_MAGOTAN    = 0;//大众迈腾
	public static final short CAR_MODEL_VOLKSWAGEN_SAGITAR    	= 1;//大众速腾
	public static final short CAR_MODEL_VOLKSWAGEN_GOLF7   		= 2;//大众高尔夫7
	public static final short CAR_MODEL_VOLKSWAGEN_POLO    		= 3;//大众波罗
	
	//本田系列(50-99)
	public static final short CAR_MODEL_HONDA_15CRV_L				= 50;//15CRV低配
	public static final short CAR_MODEL_HONDA_15CRV_H				= 51;//15CRV高配
	public static final short CAR_MODEL_HONDA_CROSSTOUR			= 52;//歌诗图
	public static final short CAR_MODEL_HONDA_ACCORD_9L			= 53;//雅阁9代低配
	public static final short CAR_MODEL_HONDA_ACCORD_9H			= 54;//雅阁9代高配
	public static final short CAR_MODEL_HONDA_CRIDER					= 55;//凌派2016
	public static final short CAR_MODEL_HONDA_JADE						= 56;//杰德
	public static final short CAR_MODEL_HONDA_ODYSSEY				= 57;//奥德赛
	public static final short CAR_MODEL_HONDA_FIDO						= 58;//飞度
	public static final short CAR_MODEL_HONDA_WISDOM				= 59;//缤智
	public static final short CAR_MODEL_HONDA_CITY						= 60;//锋范旗舰版2015
	public static final short CAR_MODEL_HONDA_12CRV					=61;//12CRV
	//日产系列(100-149)
	public static final short CAR_MODEL_NISSAN_X_TRAIL				=	100;//奇骏
	public static final short CAR_MODEL_NISSAN_QASHQAI				=	101;//逍客
	public static final short CAR_MODEL_NISSAN_NEW_TEANA			=	102;//新天籁
	public static final short CAR_MODEL_NISSAN_TEANA					=	103;//天籁
	public static final short CAR_MODEL_NISSAN_TIIDA					=	104;//骐达
	public static final short CAR_MODEL_NISSAN_SUN						=	105;//阳光
	public static final short CAR_MODEL_NISSAN_LI_WEI					=	106;//骊威
	public static final short CAR_MODEL_NISSAN_BLUEBIRD				=	107;//蓝鸟
	public static final short CAR_MODEL_NISSAN_INIFINITI_QX50_L	=	108;//英菲尼迪QX50低配
	public static final short CAR_MODEL_NISSAN_INIFINITI_QX50_H=	109;//英菲尼迪QX50高配
	public static final short CAR_MODEL_NISSAN_MORNAO				=	110;//楼兰
	public static final short CAR_MODEL_NISSAN_CIMA_L					=	111;//西玛低配
	public static final short CAR_MODEL_NISSAN_CIMA_H					=	112;//西玛高配
	public static final short CAR_MODEL_NISSAN_VENUCIA_T70		= 113;//启辰T70
	
	//丰田系列(150-199)
	public static final short CAR_MODEL_TOYOTA_RAV4					= 150;//RAV4
	public static final short CAR_MODEL_TOYOTA_VIOS					= 151;//威驰
	public static final short CAR_MODEL_TOYOTA_REIZ					= 152;//锐志	
	public static final short CAR_MODEL_TOYOTA_OVERBEARING		= 153;//霸道
	public static final short CAR_MODEL_TOYOTA_COROLLA				= 154;//卡罗拉
	public static final short CAR_MODEL_TOYOTA_HIGHLANDER		= 155;//汉兰达
	public static final short CAR_MODEL_TOYOTA_CAMRY					= 156;//凯美瑞
	public static final short CAR_MODEL_TOYOTA_CROWN				= 157;//皇冠
	
	//通用系列(200-249)
	public static final short CAR_MODEL_GM_HIDEN							= 200;//英朗
	public static final short CAR_MODEL_GM_CRUZE							= 201;//科鲁兹
	public static final short CAR_MODEL_GM_MALIBU						= 202;//迈锐宝
	public static final short CAR_MODEL_GM_16MALIBU_XL				= 203;//2016迈锐宝XL
	
	//标志系列(250-300)
	public static final short CAR_MODEL_PEUGEOT_308					= 250;//标志308
	public static final short CAR_MODEL_PEUGEOT_408					= 251;//标志408
	
	
	
	//VIM_SECURITY_ALERT_PROPERTY(数组形式)
	public static final short SECURITY_ALERT_LOW_FUEL_ON    					= 0;//低油量警告开
	public static final short SECURITY_ALERT_LOW_FUEL_OFF    					= 1;//低油量警告关
	public static final short SECURITY_ALERT_LOW_VOLTAGE_ON    				= 2;//低电压警告开
	public static final short SECURITY_ALERT_LOW_VOLTAGE_OFF   			= 3;//低电压警告关
	public static final short SECURITY_ALERT_LOW_CLEAN_FLUID_ON			= 4;//清洗液过低开
	public static final short SECURITY_ALERT_LOW_CLEAN_FLUID_OFF			= 5;//清洗液过低关
	public static final short SECURITY_ALERT_SEAT_BELT_ON						= 6;//安全带异常开
	public static final short SECURITY_ALERT_SEAT_BELT_OFF						= 7;//安全带异常关
	public static final short SECURITY_ALERT_PARKING_BRAKE_ON				= 8;//驻车制动警告开
	public static final short SECURITY_ALERT_PARKING_BRAKE_OFF				= 9;//驻车制动警告关
	//针对有的车型直接上报胎压值，而有的车型是上报胎压状态
	public static final short SECURITY_ALERT_FL_TIRE_PRESSURE_LOW		= 50;//前左胎压过低报警开
	public static final short SECURITY_ALERT_FL_TIRE_PRESSURE_HIGH		= 51;//前左胎压过高报警开
	public static final short SECURITY_ALERT_FL_TIRE_PRESSURE_NORMAL	= 52;//前左胎压正常
	public static final short SECURITY_ALERT_FR_TIRE_PRESSURE_LOW		= 53;//前右胎压过低报警开
	public static final short SECURITY_ALERT_FR_TIRE_PRESSURE_HIGH		= 54;//前右胎压过高报警开
	public static final short SECURITY_ALERT_FR_TIRE_PRESSURE_NORMAL= 55;//前右胎压正常
	public static final short SECURITY_ALERT_RL_TIRE_PRESSURE_LOW		= 56;//后左胎压过低报警开
	public static final short SECURITY_ALERT_RL_TIRE_PRESSURE_HIGH		= 57;//后左胎压过高报警开
	public static final short SECURITY_ALERT_RL_TIRE_PRESSURE_NORMAL= 58;//后左胎压正常
	public static final short SECURITY_ALERT_RR_TIRE_PRESSURE_LOW		= 59;//后右胎压过低报警开
	public static final short SECURITY_ALERT_RR_TIRE_PRESSURE_HIGH		= 60;//后右胎压过高报警开
	public static final short SECURITY_ALERT_RR_TIRE_PRESSURE_NORMAL= 61;//后右胎压正常
	public static final short SECURITY_ALERT_BACK_TIRE_PRESSURE_LOW	= 62;	//备胎胎压过低报警开
	public static final short SECURITY_ALERT_BACK_TIRE_PRESSURE_HIGH	= 63;	//备胎胎压过高报警开
	public static final short SECURITY_ALERT_BACK_TIRE_PRESSURE_NORMAL= 64;	//备胎胎压正常
	
	//空调信息
	//VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY
	public static final short AC_TEMP_UNIT_NO = -1;//不显示温度单位(表示温度等级)
	public static final short AC_TEMP_UNIT_C    = 0;//摄氏温度单位
	public static final short AC_TEMP_UNIT_F    = 1;//华氏温度单位
	
	
	//VIM_TIRE_PRESSURE_UNIT_PROPERTY(默认使用BAR)
	public static final short TIRE_PRESSURE_UNIT_BAR   = 0;//BAR
	public static final short TIRE_PRESSURE_UNIT_PSI    = 1;//PSI
	public static final short TIRE_PRESSURE_UNIT_KPA   = 2;//KPA
	
	//VIM_HISTORY_FUEL_CONSUMPTION_UNIT_PROPERTY
	//VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY
	//VIM_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY
	//油耗单位(默认为L/100KM)
	public static final short FUEL_CONSUMPTION_UNIT_L_100KM = 0;//L/100KM
	public static final short FUEL_CONSUMPTION_UNIT_KM_L 		= 1;//KM/L
	public static final short FUEL_CONSUMPTION_UNIT_MPG 		= 2;//MPG
	
	
	//VIM_ODOMETER_UNIT_PROPERTY
	//里程单位(默认为KM)
	public static final short ODOMETER_UNIT_NONE 	= 0;//不显示
	public static final short ODOMETER_UNIT_MILE 		= 1;//MILE
	public static final short ODOMETER_UNIT_KM			= 2;//KM

	//VIM_INTERIOR_TEMP_PROPERTY
	public static final short AC_TEMP_LO 			= 0;		//空调温度最低档(显示LO,不同车型最低档温度不一样)
	public static final short AC_TEMP_HI 			= 1000;	//空调温度最高档(显示HI,不同车型最高档温度不一样)
	
	//VIM_DOOR_REAR_DIRECTION_PROPERTY
	public static final short REAR_DOOR_OPEING 		= 0;//向外，正在打开
	public static final short REAR_DOOR_CLOSING 	= 0;//向内，正在关闭
	
	//VIM_HVAC_FAN_DIRECTION_PROPERTY(数组形式)
	public static final short HVAC_FAN_DIRECTION_UP_ON           			= 0;//单区控制-向上送风开
	public static final short HVAC_FAN_DIRECTION_UP_OFF           			= 1;//单区控制-向上送风关
	public static final short HVAC_FAN_DIRECTION_CENTER_ON         		= 2;//单区控制-平行送风开
	public static final short HVAC_FAN_DIRECTION_CENTER_OFF         		= 3;//单区控制-平行送风关
	public static final short HVAC_FAN_DIRECTION_DOWN_ON           		= 4;//单区控制-向下吹风开
	public static final short HVAC_FAN_DIRECTION_DOWN_OFF           		= 5;//单区控制-向下吹风关
	public static final short HVAC_FAN_DIRECTION_LEFT_UP_ON           	= 6;//双区控制-左向上送风开
	public static final short HVAC_FAN_DIRECTION_LEFT_UP_OFF           	= 7;//双区控制-左向上送风关
	public static final short HVAC_FAN_DIRECTION_LEFT_CENTER_ON		= 8;//双区控制-左平行送风开
	public static final short HVAC_FAN_DIRECTION_LEFT_CENTER_OFF		= 9;//双区控制-左平行送风关
	public static final short HVAC_FAN_DIRECTION_LEFT_DOWN_ON       	= 10;//双区控制-左向下吹风开
	public static final short HVAC_FAN_DIRECTION_LEFT_DOWN_OFF       = 11;//双区控制-左向下吹风关
	public static final short HVAC_FAN_DIRECTION_RIGHT_UP_ON         	= 12;//双区控制-右向上送风开
	public static final short HVAC_FAN_DIRECTION_RIGHT_UP_OFF         	= 13;//双区控制-右向上送风关
	public static final short HVAC_FAN_DIRECTION_RIGHT_CENTER_ON	= 14;//双区控制-右平行送风开
	public static final short HVAC_FAN_DIRECTION_RIGHT_CENTER_OFF	= 15;//双区控制-右平行送风关
	public static final short HVAC_FAN_DIRECTION_RIGHT_DOWN_ON    	= 16;//双区控制-右向下吹风开
	public static final short HVAC_FAN_DIRECTION_RIGHT_DOWN_OFF   	= 17;//双区控制-右向下吹风关
	public static final short HVAC_FAN_DIRECTION_REAR_UP_ON         	= 18;//后座向上送风开
	public static final short HVAC_FAN_DIRECTION_REAR_UP_OFF         	= 19;//后座向上送风关
	public static final short HVAC_FAN_DIRECTION_REAR_CENTER_ON		= 20;//后座平行送风开
	public static final short HVAC_FAN_DIRECTION_REAR_CENTER_OFF	= 21;//后座平行送风关
	public static final short HVAC_FAN_DIRECTION_REAR_DOWN_ON      	= 22;//后座向下吹风开
	public static final short HVAC_FAN_DIRECTION_REAR_DOWN_OFF      = 23;//后座右向下吹风关
	
	
	//VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY
	public static final short  AC_INTERIOR_CYCLE_MODE 			= 0x01;//内循环
	public static final short  AC_EXTERIOR_CYCLE_MODE 			= 0x02;//外循环
	public static final short  AC_AUTO_IN_OUT_CYCLE_MODE	= 0x03;//自动内外循环
	
	//VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY
	public static final short  AC_COOL_MODE_AC_OFF			= 0x00;//A/C关
	public static final short  AC_COOL_MODE_AC_ON			= 0x01;//A/C开
	public static final short  AC_COOL_MODE_AC_MAX_OFF	= 0x02;//A/C-MAX关(可能不支持)
	public static final short  AC_COOL_MODE_AC_MAX_ON	= 0x03;//A/C-AMX开(可能不支持)
	public static final short  AC_COOL_MODE_AUTO_OFF		= 0x04;//AUTO关
	public static final short  AC_COOL_MODE_AUTO_ON		= 0x05;//AUTO开
	public static final short  AC_COOL_MODE_AUTO2_OFF		= 0x06;//AUTO2关(可能不支持)
	public static final short  AC_COOL_MODE_AUTO2_ON		= 0x07;//AUTO2开(可能不支持)
	public static final short  AC_COOL_MODE_DUAL_OFF		= 0x08;//DUAL关(可能不支持)
	public static final short  AC_COOL_MODE_DUAL_ON		= 0x09;//DUAL开(可能不支持)
	public static final short  AC_COOL_MODE_SYNC_OFF		= 0x0A;//SYNC关(可能不支持)
	public static final short  AC_COOL_MODE_SYNC_ON		= 0x0B;//SYNC开(可能不支持)
	public static final short  AC_COOL_MODE_REAR_AUTO_OFF	= 0x0C;//后座Auto关(可能不支持)
	public static final short  AC_COOL_MODE_REAR_AUTO_ON	= 0x0D;//后座Auto开(可能不支持)
	public static final short AC_COOL_MODE_AC_AUTO			= 0xA0;//A/C自动
	
	
	//VIM_HVAC_FAN_DIRECTION_PROPERTY(仅适用于通用车系空调风向值定义)
	public static final short AC_FAN_DIR_AUTO 						= 1;//自动送风
	public static final short AC_FAN_DIR_FRONT_WINDOW 		= 2;//前挡风玻璃窗送风
	public static final short AC_FAN_DIR_DOWN 						= 3;//向下送风
	public static final short AC_FAN_DIR_DOWN_AND_CENTER 	= 4;//向下与平行送风
	public static final short AC_FAN_DIR_CENTER 					= 5;//平行送风
	public static final short AC_FAN_DIR_CENTER_AND_UP 		= 6;//平行与向上送风
	public static final short AC_FAN_DIR_UP 							= 7;//向上送风
	public static final short AC_FAN_DIR_UP_AND_DOWN 			= 8;//向上与向下送风
	public static final short AC_FAN_DIR_UP_DOWN_CENTER 	= 9;//向上与向下与平行送风
	
	//VIM_HVAC_FAN_STRENGTH
	public static final short HVAC_FAN_STRENGTH_LOW			= 0x00;//低
	public static final short HVAC_FAN_STRENGTH_MID				= 0x01;//中
	public static final short HVAC_FAN_STRENGTH_HIGH			= 0x02;//高
	
	//RADAR Distance Level(0-5)
	//针对不同的车型雷达距离级别定义不同，底层统一转为以下5个等级
	public static final short RADAR_DISTANCE_NONE			= 0x00;//无障碍物
	public static final short RADAR_DISTANCE_NEAREST		= 0x01;//最近的
	public static final short RADAR_DISTANCE_NEAR				= 0x02;//稍近
	public static final short RADAR_DISTANCE_FAR				= 0x03;//稍远
	public static final short RADAR_DISTANCE_FARTHEST		= 0x04;//最远的

	//VIM_CAMERA_PANORAMIC_PATH_VIEW_MODE_PROPERTY(针对丰田车系定义)
	public static final short CAMERA_PANORAMIC_TRACK_PATH_SETTING 	= 0x01;//全景轨迹线设置
	public static final short CAMERA_PANORAMIC_SETTING_AUTO 				= 0x02;//全景自动非自动设置
	public static final short REVERSE_GEAR_MODE_SETTING 						= 0x03;//倒挡模式设置
	public static final short REVERSE_GEAR_TRACK_PATH_SETTING 				= 0x04;//倒挡轨迹设置
	public static final short REVERSE_NARROW_MODE_AGREEN 					= 0x05;//纵列倒车打开狭窄模式确认“同意”
	public static final short REVERSE_NARROW_MODE_CANCEL 					= 0x06;//纵列倒车打开狭窄模式确认“返回”
	public static final short TRACK_PATH_MODE_SWITCH 							= 0x07;//轨迹模式切换
	
	//VIM_COMPASS_CALIBRATE_STATUS_PROPERTY
	public static final short COMPASS_CALIBRATE_FINISH								= 0x00;//校准完成
	public static final short COMPASS_CALIBRATE_WORKING						= 0x01;//正在校准
	public static final short COMPASS_CALIBRATE_FAILED								= 0x02;//校准失败

	
	//VIM_CAN_REQ_COMMAND_PROPERTY
	public static final short CAN_CMD_REQ_ENGINE_LOAD							= 0;	//请求发动机负荷
	public static final short CAN_CMD_REQ_COOLANT_TEMP							= 1;	//请求冷却液温度
	public static final short CAN_CMD_REQ_FUEL_PRESSURE							= 2;	//请求燃油压力
	public static final short CAN_CMD_REQ_INPIPE_PRESSURE						= 3;	//请求进气管压力
	public static final short CAN_CMD_REQ_ENGINE_SPEED							= 4;	//请求引擎转速
	public static final short CAN_CMD_REQ_DRIVING_SPEED							= 5;	//请求车速
	public static final short CAN_CMD_REQ_IGNITION_ANGLE						= 6;	//请求点火提前角
	public static final short CAN_CMD_REQ_INTAKE_TEMP							= 7;	//请求进气温度
	public static final short CAN_CMD_REQ_INTAKE_RATE								= 8;	//请求进气流量
	public static final short CAN_CMD_REQ_THROTTLE_POSN						= 9;	//节气门位置
	public static final short CAN_CMD_REQ_ENGINE_RUN_TIME					= 10;	//请求引擎运行时间
	public static final short CAN_CMD_REQ_VACUUM_OIL_RAIL_PRESSURE	= 11;	//请求真空油轨运行压力
	public static final short CAN_CMD_REQ_EGR_DEGREE								= 12;	//请求EGR开度
	public static final short CAN_CMD_REQ_EVA_DEGREE								= 13;	//请求蒸汽清除开度
	public static final short CAN_CMD_REQ_FUEL_RMNG								= 14;	//请求剩余油量
	public static final short CAN_CMD_REQ_VEHICLE_VIN								= 15;	//请求VIN
	public static final short CAN_CMD_REQ_BATTERY_VOLTAGE					= 16;	//请求电瓶电压
	public static final short CAN_CMD_REQ_FUEL_CONSUM_INFO					= 17;	//请求油耗信息
	public static final short CAN_CMD_REQ_ODOMETER_INFO						= 18;	//请求里程信息
	public static final short CAN_CMD_REQ_DRVING_TIME_INFO 					= 30;	//请求驾驶时间信息，包括本次以及累计的怠速时间与行驶时间
	public static final short CAN_CMD_REQ_MINUS_ODOMETER	 					= 31;	//扣减累计里程，用于里程校准
	public static final short CAN_CMD_REQ_CLEAR_DTC 								= 32;	//清除故障码
	public static final short CAN_CMD_REQ_DEVICE_INFO 							= 33;	//请求设备信息
	public static final short CAN_CMD_REQ_CLEAR_DATA 								= 34;	//请求清除保存信息
	public static final short CAN_CMD_REQ_DRIV_HABIT_DATA						= 35;	//请求驾驶习惯数据
	public static final short CAN_CMD_REQ_HOT_RESTART							= 46;	//热重启
	public static final short CAN_CMD_REQ_SLEEP											= 47;	//进入休眠

	public static final short CANBOX_CMD_REQ_START									= 100;//请求开始连接
	public static final short CANBOX_CMD_REQ_END										= 101;//请求结束连接
	public static final short CANBOX_CMD_REQ_VEHICLE_INFO						= 102;//请求车身基本信息(包括车速、油耗、里程等)
	public static final short CANBOX_CMD_REQ_AIR_CONDITION_INFO			= 103;//请求空调信息
	public static final short CANBOX_CMD_REQ_RADAR_INFO						= 104;//请求雷达信息
	public static final short CANBOX_CMD_REQ_TPMS_INFO							= 105;//请求胎压信息
	public static final short CANBOX_CMD_REQ_DOOR_INFO							= 106;//请求车门状态信息
	public static final short CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE	= 107;//请求方向盘转角信息
	public static final short CANBOX_CMD_REQ_AMPLIFIER_INFO					= 108;//请求功放信息
	public static final short CANBOX_CMD_REQ_VEHICLE_SETTING				= 109;//请求原车设置信息
	public static final short CANBOX_CMD_REQ_SYNC_SYS_TIME					= 129;//请求同步系统时间
	public static final short CANBOX_CMD_REQ_VERSION_INFO					= 130;//请求CAN盒软件版本信息
	public static final short CANBOX_CMd_REQ_SERVICE_INFO						= 150;//请求保养服务信息
	public static final short CANBOX_CMD_REQ_CAMERA_STATUS					= 300;//请求摄像头状态(启辰T70)
	public static final short CANBOX_CMD_REQ_CAMERA_SETTING_INFO		= 301;//请求摄像头设置信息(启辰T70)
	public static final short CANBOX_CMD_REQ_SWITCH_CAMERA					= 302;//请求摄像头切换(启辰T70)
	public static final short CANBOX_CMD_REQ_OIL_ELEC_MIX_INFO			= 350;//请求油电混合信息
	public static final short CANBOX_CMD_REQ_COMPASS_INFO					= 351;//请求指南针信息
	public static final short CANBOX_CMD_REQ_VEHICLE_DIAGNOSTIC_INFO= 352;//请求原车诊断信息
	public static final short CANBOX_CMD_REQ_CLEAR_TRIP_RECORD			= 400;//请求删除TRIP记录
	public static final short CANBOX_CMD_REQ_TPMS_SET							= 401;//请求校准胎压
	public static final short CANBOX_CMD_RESET_DRIVE_DATA_SINCE_START		= 402;//重置自启动以来的行驶数据
	public static final short CANBOX_CMD_RESET_DRIVE_DATA_LONG_TERM		= 403;//重置长时间的行驶数据
	public static final short CANBOX_CMD_RESET_DRIVER_ASSIST_SYSTEM			= 404;//重置驾驶员辅助系统
	public static final short CANBOX_CMD_RESET_PARKING_AND_MANOEUVRING	= 405;//重置驻车和调车
	public static final short CANBOX_CMD_RESET_LIGHT_SETTING						= 406;//重置车灯设置
	public static final short CANBOX_CMD_RESET_MIRRORS_AND_WIPERS			= 407;//重置后视镜和雨刷
	public static final short CANBOX_CMD_RESET_OPENING_AND_CLOSING			= 408;//重置开关设置
	public static final short CANBOX_CMD_RESET_MULTIFUNCTION_DISPLAY		= 409;//重置多功能显示设置
	public static final short CANBOX_CMD_RESET_ALL_SETTING							= 410;//重置所有设置

	
	//以下只会在调试用使用
	public static final short CAN_CMD_ENABLE_DATA_EMULATE						= 220;// 打开CAN数据模拟
	public static final short CAN_CMD_DISABLE_DATA_EMULATE					= 221;// 关闭CAN数据模拟
	
	//VIM_MCU_UI_MODE_PROPERTY
	public static final short UI_MODE_TUNER												= 0x00; // 收音界面
	public static final short UI_MODE_DISC													= 0x01; // 碟片界面
	public static final short UI_MODE_MUSIC													= 0x02; // 音乐界面
	public static final short UI_MODE_VIDEO													= 0x03; // 视频界面
	public static final short UI_MODE_IPOD													= 0x04; // IPOD界面
	public static final short UI_MODE_BLUETOOTH										= 0x05; // 蓝牙电话界面
	public static final short UI_MODE_NAVIGATION										= 0x06; // 导航界面
	public static final short UI_MODE_VCDC													= 0x07; // 虚拟碟界面
	public static final short UI_MODE_TV														= 0x08; // TV界面
	public static final short UI_MODE_AUX1													= 0x09; // AUX1界面
	public static final short UI_MODE_A2DP													= 0x0A; // 蓝牙音乐界面
	public static final short UI_MODE_AVR													= 0x0B; // 行车记录仪界面
	public static final short UI_MODE_BACKCAR											= 0x0C; // 倒车界面
	public static final short UI_MODE_SETTING												= 0x0D; // 设置界面
	public static final short UI_MODE_MAINMENU											= 0x0E; // 主页界面
	public static final short UI_MODE_OBD													= 0x0F; // OBD界面
	public static final short UI_MODE_LOCK_SCREEN										= 0x10; // 屏保界面
	public static final short UI_MODE_AUX2													= 0x11; // AUX2界面
	public static final short UI_MODE_SPEECH												= 0x12;// 语音界面
	
	
	//VIM_MCU_MEDIA_MODE_PROPERTY
	public static final short MEDIA_MODE_UNKNOWN									= -1; 	// 未知
	public static final short MEDIA_MODE_TUNER											= 0x00; // 收音
	public static final short MEDIA_MODE_MUSIC											= 0x02; // 音乐
	public static final short MEDIA_MODE_VIDEO											= 0x03; // 视频
	public static final short MEDIA_MODE_IPOD											= 0x04; // IPOD
	public static final short MEDIA_MODE_BLUETOOTH									= 0x05; // 蓝牙电话
	public static final short MEDIA_MODE_NAVIGATION									= 0x06; // 导航
	public static final short MEDIA_MODE_VCDC											= 0x07; // 虚拟碟
	public static final short MEDIA_MODE_TV												= 0x08; // TV
	public static final short MEDIA_MODE_AUX1											= 0x09; // AUX1模式
	public static final short MEDIA_MODE_A2DP											= 0x0A; // 蓝牙音乐
	public static final short MEDIA_MODE_AVR												= 0x0B; // 行车记录仪
	public static final short MEDIA_MODE_OBD												= 0x0C; // OBD
	public static final short MEDIA_MODE_LOCK_SCREEN								= 0x0D; // 屏保
	public static final short MEDIA_MODE_AUX2											= 0x11; // AUX2模式
	public static final short MEDIA_MODE_SPEECH										= 0x12; // 语音模式
	public static final short MEDIA_MODE_MUTE											= 0xFD; // 无声音模式
	
	public static final short MEDIA_MODE_FIRST_POWERON							= 0xFF; // 第一次开机模式
	
	//Vehicle Media Type
	public static final short VEHICLE_MEDIA_TYPE_OFF 								= 0x00;	//OFF 显示音响关
	public static final short VEHICLE_MEDIA_TYPE_TUNER 							= 0x01;	//收音
	public static final short VEHICLE_MEDIA_TYPE_DISC 								= 0x02;	//碟片
	public static final short VEHICLE_MEDIA_TYPE_TV 									= 0x03;	//模拟电视
	public static final short VEHICLE_MEDIA_TYPE_NAVI 								= 0x04;	//导航
	public static final short VEHICLE_MEDIA_TYPE_PHONE 							= 0x05;	//电话(蓝牙电话/SIM电话)
	public static final short VEHICLE_MEDIA_TYPE_IPOD 								= 0x06; //IPOD
	public static final short VEHICLE_MEDIA_TYPE_AUX 								= 0x07; //AUX
	public static final short VEHICLE_MEDIA_TYPE_USB 								= 0x08;	//USB(本地音乐)
	public static final short VEHICLE_MEDIA_TYPE_SD			 						= 0x09; //SD
	public static final short VEHICLE_MEDIA_TYPE_DVB 								= 0x0A; //数字电视
	public static final short VEHICLE_MEDIA_TYPE_BT_MUSIC 						= 0x0B; //蓝牙音乐
	public static final short VEHICLE_MEDIA_TYPE_OTHER 							= 0x0C; //其它
	public static final short VEHICLE_MEDIA_TYPE_CDC 								= 0x0D; //虚拟碟
	public static final short VEHICLE_MEDIA_TYPE_CD 									= 0x10; 
	public static final short VEHICLE_MEDIA_TYPE_DVD 								= 0x11; 
	public static final short VEHICLE_MEDIA_TYPE_SELF_DEFINE 					= 0xFF; //自定义显示类型，目前日产指定车型有支持
	
	//VIM_MCU_MUTE_STATUS_PROPERTY
	public static final short STATUS_UNMUTE													= 0; // 静音关
	public static final short STATUS_MUTE														= 1; // 静音开

	
	//Bt Status
	public static final short BT_STATUS_DISCONNECT  										= 0; // 断开连接
	public static final short BT_STATUS_CONNECTED  										= 1; // 已连接
	public static final short BT_STATUS_INCOMING  											= 2; // 来电中
	public static final short BT_STATUS_CALL_OUT  											= 3; // 去电中
	public static final short BT_STATUS_CALLING  											= 4; // 通话中
	
	
	//Commom status value define
	public static final short COMMON_STATUS_OFF								= 0;//关闭
	public static final short COMMON_STATUS_ON									= 1;//打开
	
	//DOOR_OPEN_STATUS
	public static final short VEHICLE_DOOR_STATUS_CLOSE					= 0;//车门关闭
	public static final short VEHICLE_DOOR_STATUS_OPEN					= 1;//车门打开
	
	//VIM_SEAT_BELT_STATUS_DRIVER_PROPERTY
	//VIM_SEAT_BELT_STATUS_PSNGR_PROPERTY
	//VIM_SEAT_BELT_STATUS_REAR_LEFT_PROPERTY
	//VIM_SEAT_BELT_STATUS_REAR_RIGHT_PROPERTY
	//VIM_SEAT_BELT_STATUS_REAR_CENTER_PROPERTY
	public static final short SEAT_BELT_STATUS_NOT_TIED					= 0;//安全带未系
	public static final short SEAT_BELT_STATUS_TIED							= 1;//安全带已系
		
	//VIM_MCU_ACC_STAT_PROPERTYE
	public static final short ACC_STATUS_OFF										= 0;
	public static final short ACC_STATUS_ON										= 1;
	
	//VIM_MCU_REVERSE_STATUS_PROPERTY
	public static final short CAR_IN_NO_REVERSE									= 0;	//非倒车状态
	public static final short CAR_IN_REVERSE										= 1;	//倒车状态
	
	//VIM_MCU_BRAKE_STATUS_PROPERTY
	//VIM_PARKING_BRAKES_PROPERTY
	public static final short CAR_BRAKE_OFF											= 0;	//手刹放下
	public static final short CAR_BRAKE_ON											= 1;	//手刹拉起
	
	//VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY
	public static final short CAR_LIGHT_OFF											= 0;	//大灯/转向灯关闭
	public static final short CAR_LIGHT_ON											= 1;	//大灯/转向灯开启
	
	//VIM_MCU_RADIO_CUR_STATE_PROPERTY
	public static final short RADIO_STATUS_NORMAL							= 0; //正常模式
	public static final short RADIO_STATUS_SEARCH								= 1; //搜索模式
	public static final short RADIO_STATUS_SCAN									= 2; //扫描播放模式
	
	//VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY
	public static final short RADIO_AUDIO_OFF										= 0; //收音声音关
	public static final short RADIO_AUDIO_ON										= 1; //收音声音开

	//VIM_VEHICLE_PARKING_MODE_PROPERTY
	public static final short PARKING_MODE_NORMAL							= 0;//普通模式
	public static final short PARKING_MODE_SIDE									= 1;//侧方模式(路边驻车)
	public static final short PARKING_MODE_VERTICAL							= 2;//垂直模式
	public static final short PARKING_MODE_NALLOW_ALLEY				= 2;//窄巷模式
	
	//VIM_NAVI_INFO_COLLECT_PROPERTY
	public static String formatNaviInfoString(int naviDisSwitch,int naviInfoType,int flagPointUnit,int destDisUnit,int compassDir,int flagPointDis,int destDis,int[] roadInfo)
	{
		HashMap<Integer, String> naviInfoMap = new HashMap<Integer, String>();
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_DISPLAY_ENABLE_PROPERTY, String.valueOf(naviDisSwitch));
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_DISPLAY_TYPE_PROPERTY, String.valueOf(naviInfoType));
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_FLAG_POINT_UNIT_PROPERTY, String.valueOf(flagPointUnit));
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_DESTION_DIS_UNIT_PROPERTY, String.valueOf(destDisUnit));
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_COMPASS_DIR_PROPERTY, String.valueOf(compassDir));
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_FLAG_POINT_DIS_PROPERTY, String.valueOf(flagPointDis));
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_DESTION_DIS_PROPERTY, String.valueOf(destDis));
		naviInfoMap.put(VehicleInterfaceProperties.VIM_NAVI_INFO_ROAD_DIR_TYPE_PROPERTY, VehicleManager.intArrayToString(roadInfo));
		return VehicleManager.hashMapToString(naviInfoMap);
	}
	
	//VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY
	public static String formatSourceInfoString(int sourceType,int curTrack,int totalTrack,int curPlayTime,int totalPlayTime,int playState,String strID3,int radioBand,int radioFreq)
	{
		HashMap<Integer, String> sourceInfoMap = new HashMap<Integer, String>();
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf(sourceType));
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY, String.valueOf(curTrack));
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY, String.valueOf(totalTrack));
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY, String.valueOf(curPlayTime));
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_PLAY_TIME_PROPERTY, String.valueOf(totalPlayTime));
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_PLAY_STATE_PROPERTY, String.valueOf(playState));
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY, strID3);
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY, String.valueOf(radioBand));
		sourceInfoMap.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY, String.valueOf(radioFreq));
		return VehicleManager.hashMapToString(sourceInfoMap);
	}
	
	//VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY
	public static String formatBtPhoneInfoString(int btConnectStatus,int callStatus,String phoneNumber)
	{
		HashMap<Integer, String> btPhoneInfoMap = new HashMap<Integer, String>();
		btPhoneInfoMap.put(VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY, String.valueOf(btConnectStatus));
		btPhoneInfoMap.put(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY, String.valueOf(callStatus));
		btPhoneInfoMap.put(VehicleInterfaceProperties.VIM_MCU_BT_CALL_NUMBER_PROPERTY, String.valueOf(phoneNumber));
		return VehicleManager.hashMapToString(btPhoneInfoMap);
	}

	//VIM_VEHICLE_STATUS_PROMPT_MESSAGE_PROPERTY
	public static final short MESSAGE_TRAILER_MODE 											= 0x00;// Trailer mode 拖车模式
	public static final short MESSAGE_WINDSCREEN_COULD_BTCOME_FOGGED		= 0x01;// Windscreen could become fogged 小心档风玻璃起雾
	public static final short MESSAGE_OPERATING_TEMP_NOT_MAINTAINED 		= 0x02;// Operating temperature not maintained 不能维持工作温度
	public static final short MESSAGE_NO_DRIVER_DETECTED 								= 0x03;// No driver detected 未发现驾驶员
	public static final short MESSAGE_GEARBOX_REQUIRE_ENGINE_RUNNING 		= 0x04;// Gearbox requires engine to be running 变速箱需要发动机运行
	public static final short MESSAGE_POWER_CONSUM_IS_HIGH 							= 0x05;// Power consumption is high 功耗高
	public static final short MESSAGE_STARTING_START_STOP_SYSTEM 				= 0x06;// Starting start-stop system 启用启-停系统
	public static final short MESSAGE_ENGINE_MUST_BE_RUNNING 						= 0x07;// Engine must be running 引擎必须运行
	public static final short MESSAGE_GRADIENT_TOO_STEEP 								= 0x08;// Gradient too steep 坡度太陡
	public static final short MESSAGE_START_STOP_SYSTEM_NOT_AVAILABLE 		= 0x09;// Start -stop system is currently not available 启-停系统当前不可用
	public static final short MESSAGE_EMERGENCY_BRAKE_FUNCTION_INTERVENTION = 0x0A;// Emergency brake function intervention 紧急制动功能干预
	public static final short MESSAGE_BRAKE_REQUIRE_ENGINE_RUNNING 			= 0x0B;// Brake requires engine to be running 制动器需要发机运行
	public static final short MESSAGE_POWER_CONSUM_IS_HIGH_2						= 0x0C;// Power consumption is high 功耗高
	public static final short MESSAGE_AIR_CONDITION_REQUIRE_ENGINE_RUNNING = 0x0D;// Airconditioning requires engine to be running 空调开启需要发动机运行
	public static final short MESSAGE_STEER_ANGLE_TOO_GREAT 						= 0x0E;// Steering angle too great 转向角太大
	public static final short MESSAGE_MANOEUVERING_MODE								= 0x0F;// Manoeuvering mode 操纵模式
	public static final short MESSAGE_DEFROST_FUNCTION_ON 							= 0x10;// Defrost function is switched on 除霜功能开启
	public static final short MESSAGE_DRIVER_SEATBELT_NOT_FASTENED			= 0x11;// Driver seatbelt not fastened 驾驶员未系安全带
	public static final short MESSAGE_DRIVER_DOOR_OPEN 									= 0x12;// Driver door open 驾驶室门打开
	public static final short MESSAGE_ACCELERATOR_IS_DEPRESSED 					= 0x13;// Accelerator is depressed 加速踏板被踩下
	public static final short MESSAGE_WINDSCREEN_HEATING_ON 						= 0x14;// Windscreen heating is switched on 前窗加热开启
	public static final short MESSAGE_MAXIMUM_AC_REQUIRE_ENGINE_RUNNING= 0x15;// Maximum air conditioning requires engine to be running 空调最大制冷需要发动机运行
	public static final short MESSAGE_BONNET_OPEN 											= 0x16;// Bonnet has been opened 引擎盖未关好
	public static final short MESSAGE_ENGINE_STOP_NOT_POSSIBLE 					= 0x17;// Engine stop currently not possible 发动机当前无法停止
	public static final short MESSAGE_OFF_ROAD_MODE_ACTIVED 						= 0x18;// Off-road mode is activated 越野模式已启用
	public static final short MESSAGE_BUTTON_OR_LEVEL_ACTIVE_TOO_OFTEN 	= 0x19;// System protection intervention Button/lever activated too often 手刹按钮 /杆使用太频繁
	public static final short MESSAGE_GEAR_IS_ENGAGED 									= 0x1A;//Gear is engaged 齿轮啮合
	public static final short MESSAGE_START_SOP_BUTTON_PRESSED 					= 0x1B;// Start -stop button pressed 启-停按钮已下
	public static final short MESSAGE_AIR_CONDITION_REQUIRE_ENGINE_RUNNING_2 = 0x1C;// Air conditioning requires engine to be running 空调开启需要发动机运行
	public static final short MESSAGE_ACC_INTERVENTION_ACTIVED 					= 0x1D;// ACC intervention ACC状态
	public static final short MESSAGE_PARK_ASSIST_IS_ACTIVED 							= 0x1E;// Park Assist is activated 驻车辅助已启动
	public static final short MESSAGE_ESC_OFF 													= 0x1F;// ESC is switched off 关闭
	public static final short MESSAGE_POWER_CONSUM_IS_HIGH_3 						= 0x20;// Power consumption is high 功耗高
	public static final short MESSAGE_START_STOP_SYSTEM_REQUIRE_ENGINE_RUNNING = 0x21;// Start -stop system requires engine to be running 启-停系统需要发动机运行
	public static final short MESSAGE_START_STOP_SYSTEM_DEACTIVATED			= 0x30;// Start-stop system deactivated
	public static final short MESSAGE_START_STOP_SYSTEM_ACTIVATED				= 0x31;// Start-stop system activated
	
	
	//VIM_CONV_CONSUMERS_PROMPT_MESSAGE_PROPERTY
	public static final short PROMPT_INFO_AIR_CONDITION 						= 0x01;// Air conditioning 空调
	public static final short PROMPT_INFO_AUXILIARY_HEATER 					= 0x02;//Auxiliary heater 辅助加热器
	public static final short PROMPT_INFO_REAR_WINDOW_HEATING 			= 0x03;//Rear window heating 后窗加热
	public static final short PROMPT_INFO_LEFT_SEAT_HEATING 					= 0x04;//Left seat heating 左边座椅加热
	public static final short PROMPT_INFO_RIGHT_SEAT_HEATING 				= 0x05;//Right seat heating 右边座椅加热
	public static final short PROMPT_INFO_LEFT_SEAT_VENTILATION 			= 0x06;//Left seat ventilation 左座椅通风
	public static final short PROMPT_INFO_RIGHT_HEAD_AREA_HEATING 	= 0x07;//Right head area heating 右前区域加热
	public static final short PROMPT_INFO_LEFT_HEAD_AREA_HEATING 		= 0x08;//Left head area heating 左前区域加热
	public static final short PROMPT_INFO_RIGHT_HEAD_AREA_HEATING_2 = 0x09;//Right head area heating 右前区域加热
	public static final short PROMPT_INFO_FRONT_FOG_LIGHT 					= 0x0A;//Front fog light 前雾灯
	public static final short PROMPT_INFO_REAR_FOG_LIGHT 						= 0x0B;//Rear fog light 后雾灯
	public static final short PROMPT_INFO_WINDSCREEN_HEATING 				= 0x0C;//Windscreen heating 前窗加热
	public static final short PROMPT_INFO_STEER_WHEEL_HEADING 			= 0x0D;//Steering wheel heating 方向盘加热
	public static final short PROMPT_INFO_MIRROR_HEATING 						= 0x0E;//Mirror heating 后视镜加热
	public static final short PROMPT_INFO_REAR_SEAT_HEATING 				= 0x0F;//Rear seat heating 后座椅加热
	public static final short PROMPT_INFO_REAR_SEAT_VENTILATION 			= 0x10;//Rear seat ventilation 后座椅通风
	public static final short PROMPT_INFO_SOCKET 										= 0x11;//Socket 插座
	public static final short PROMPT_INFO_DRINK_HOLDER 							= 0x12;//Drink holder 饮料架

	//VIM_VEHICLE_MEDIA_DEVICE_TYPE_PROPERTY
	public static final short VEHICLE_MEDIA_DEVICE_TYPE_NO 		= 0x00;//无媒体信息
	public static final short VEHICLE_MEDIA_DEVICE_TYPE_IPOD 		= 0x01;//IPOD
	public static final short VEHICLE_MEDIA_DEVICE_TYPE_USB 		= 0x02;//USB

	
	//VIM_VEHICLE_MEDIA_DEVICE_STATUS_PROPERTY
	public static final short VEHICLE_MEDIA_DEVICE_STOP 				= 0x00;//关闭/停止状态
	public static final short VEHICLE_MEDIA_DEVICE_LOADING 		= 0x01;//加载中
	public static final short VEHICLE_MEDIA_DEVICE_NOT_EXIST 		= 0x02;//没有连接USB设备
	public static final short VEHICLE_MEDIA_DEVICE_CONNECTED		= 0x03;//设备已连接
	public static final short VEHICLE_MEDIA_DEVICE_PLAYING 			= 0x04;//播放中
	public static final short VEHICLE_MEDIA_DEVICE_PAUSE 			= 0x05;//暂停
	public static final short VEHICLE_MEDIA_DEVICE_OTHER 			= 0x06;//其它状态
	
	//VIM_VEHICLE_MEDIA_PLAY_CMD_PROPERTY
	public static final short VEHICLE_MEDIA_CMD_NO 						= 0x00;//无操作
	public static final short VEHICLE_MEDIA_CMD_PLAY 					= 0x01;//开机/播放
	public static final short VEHICLE_MEDIA_CMD_STOP 					= 0x02;//关机/停止
	public static final short VEHICLE_MEDIA_CMD_TRACK_UP			= 0x03;//上一曲
	public static final short VEHICLE_MEDIA_CMD_TRACK_DOWN 		= 0x04;//下一曲
	public static final short VEHICLE_MEDIA_CMD_FOLDER_UP 			= 0x05;//上一个文件夹
	public static final short VEHICLE_MEDIA_CMD_FOLDER_DOWN 	= 0x06;//下一个文件夹
	
	//VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE
	public static final short AMPLIFIER_CONTROL_TYPE_STEP				= 0;//步进调节
	public static final short AMPLIFIER_CONTROL_TYPE_DIRECT			= 1;//直接设值调节
	
	//对于高中低音和平衡音调节，步进调节方式下的参数定义
	public static final short AMPLIFIER_ADJUST_STEP_DOWN				= 0;//步进减
	public static final short AMPLIFIER_ADJUST_STEP_UP						= 1;//步进加

	//VIM_ONSTAR_SYSTEM_STATUS_PROPERTY
	public static final short ONSTAR_STATUS_OFF								= 0x00;//系统关闭
	public static final short ONSTAR_STATUS_ON								= 0x01;//系统开启
	public static final short ONSTAR_STATUS_INCOMING					= 0x02;//有电话输入
	public static final short ONSTAR_STATUS_DIALING						= 0x03;//正在拨号
	public static final short ONSTAR_STATUS_CALL_ACTIVE				= 0x04;//电话已接通
	
	//VIM_ONSTAR_REQ_CMD_PROPERTY
	public static final short ONSTAR_CMD_REQ_EXIT						= 0x00;//请求关闭OnStar系统
	public static final short ONSTAR_CMD_REQ_STARTUP					= 0x01;//请求启动OnStar系统
	public static final short ONSTAR_CMD_REQ_PHONE_ACCEPT		= 0x02;//电话接听命令
	public static final short ONSTAR_CMD_REQ_PHONE_HANGUP		= 0x03;//电话挂断命令
	//以下拨号盘指令只能在电话接通时有效
	public static final short ONSTAR_CMD_NUM0								= 0x80;//拨号盘数字0
	public static final short ONSTAR_CMD_NUM1								= 0x81;//拨号盘数字1
	public static final short ONSTAR_CMD_NUM2								= 0x82;//拨号盘数字2
	public static final short ONSTAR_CMD_NUM3								= 0x83;//拨号盘数字3
	public static final short ONSTAR_CMD_NUM4								= 0x84;//拨号盘数字4
	public static final short ONSTAR_CMD_NUM5								= 0x85;//拨号盘数字5
	public static final short ONSTAR_CMD_NUM6								= 0x86;//拨号盘数字6
	public static final short ONSTAR_CMD_NUM7								= 0x87;//拨号盘数字7
	public static final short ONSTAR_CMD_NUM8								= 0x88;//拨号盘数字8
	public static final short ONSTAR_CMD_NUM9								= 0x89;//拨号盘数字9
	public static final short ONSTAR_CMD_NUM_STAR						= 0x8A;//拨号盘数字*
	public static final short ONSTAR_CMD_NUM_ASTERISK				= 0x8B;//拨号盘数字#

	
	//VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_DOWN_PROPERTY
	//VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_UP_PROPERTY
	public static final short AIR_CONDITION_CMD_POWER									= 0x00;//Power键
	public static final short AIR_CONDITION_CMD_AC											= 0x01;//AC
	public static final short AIR_CONDITION_CMD_AUTO										= 0x02;//AUTO
	public static final short AIR_CONDITION_CMD_IO_CIRCULATION					= 0x03;//内外循环
	public static final short AIR_CONDITION_CMD_TEMP_ADD								= 0x04;//温度+(双区控制时为左温度+)
	public static final short AIR_CONDITION_CMD_TEMP_MINUS							= 0x05;//温度-(双区控制时为左温度-)
	public static final short AIR_CONDITION_CMD_FAN_SPEED_ADD						= 0x06;//风速+
	public static final short AIR_CONDITION_CMD_FAN_SPEED_MINUS					= 0x07;//风速-
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_CENTER		= 0x08;//平行吹风
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_CENTER_DOWN	 = 0x09;//平行向下吹风
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_DOWN_UP	= 0x0A;//向下向上吹风
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_DOWN			= 0x0B;//向下吹风
	public static final short AIR_CONDITION_CMD_DEFROST_FRONT						= 0x0C;//前除霜
	public static final short AIR_CONDITION_CMD_DEFROST_SYNC						= 0x0D;//左右温度同步
	public static final short AIR_CONDITION_CMD_RIGHT_TEMP_ADD					= 0x14;//双区空调右温度+
	public static final short AIR_CONDITION_CMD_RIGHT_TEMP_MINUS				= 0x15;//双区空调右温度-
	public static final short AIR_CONDITION_CMD_INTERIOR_CYCLE					= 0x16;//内循环
	public static final short AIR_CONDITION_CMD_EXTERNAL_CYCLE					= 0x17;//外循环
	public static final short AIR_CONDITION_CMD_MODE_CONTROL						= 0x18;//模式控制

	
	//VIM_AIR_CONDITION_CONTROL_CMD_PROPERTY
	public static final short AIR_CONDITION_CMD_AUTO_ON						= 0xA0;//AUTO开
	public static final short AIR_CONDITION_CMD_AUTO_OFF						= 0xA1;//AUTO关
	public static final short AIR_CONDITION_CMD_AC_ON								= 0xA2;//AC开
	public static final short AIR_CONDITION_CMD_AC_OFF							= 0xA3;//AC关
	public static final short AIR_CONDITION_CMD_AC_MAX_ON					= 0xA4;//AC-MAX开
	public static final short AIR_CONDITION_CMD_AC_MAX_OFF					= 0xA5;//AC-MAX关
	public static final short AIR_CONDITION_CMD_DUAL_ON							= 0xA6;//DUAL开
	public static final short AIR_CONDITION_CMD_DUAL_OFF						= 0xA7;//DUAL关
	public static final short AIR_CONDITION_CMD_LEFT_TEMP_UP				= 0xA8;//左温度+
	public static final short AIR_CONDITION_CMD_LEFT_TEMP_DOWN			= 0xA9;//左温度-
	public static final short AIR_CONDITION_CMD_RIGHT_TEMP_UP				= 0xAA;//右温度+
	public static final short AIR_CONDITION_CMD_RIGHT_TEMP_DOWN		= 0xAB;//右温度-
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_CENTER_ON	= 0xAC;//平行送风开
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_CENTER_OFF	= 0xAD;//平行送风关
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_UP_ON			= 0xAE;//向上送风开
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_UP_OFF		= 0xAF;//向上送风关
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_DOWN_ON	= 0xB0;//向下送风开
	public static final short AIR_CONDITION_CMD_FAN_DIRECTION_DOWN_OFF	= 0xB1;//向下送风关
	public static final short AIR_CONDITION_CMD_FAN_SPEED_UP 						= 0xB2;//风速+
	public static final short AIR_CONDITION_CMD_FAN_SPEED_DOWN 					= 0xB3;//风速-
	public static final short AIR_CONDITION_CMD_AQS_ON 									= 0xB4;//AQS开
	public static final short AIR_CONDITION_CMD_AQS_OFF 								= 0xB5;//AQS关
	public static final short AIR_CONDITION_CMD_INTERIOR_CYCLE_MODE_ON 	= 0xB6;//内循环开
	public static final short AIR_CONDITION_CMD_INTERIOR_CYCLE_MODE_OFF 	= 0xB7;//内循环关
	public static final short AIR_CONDITION_CMD_SYNC_ON 								= 0xC0;//SYNC开
	public static final short AIR_CONDITION_CMD_SYNC_OFF 								= 0xC1;//SYNC关
	public static final short AIR_CONDITION_CMD_REAR_TEMP_UP						= 0xC2;//后区温度+
	public static final short AIR_CONDITION_CMD_REAR_TEMP_DOWN					= 0xC3;//后区温度-
	public static final short AIR_CONDITION_CMD_REAR_LOCK								= 0xC4;//后区锁定
	public static final short AIR_CONDITION_CMD_REAR_UNLOCK							= 0xC5;//后区解锁					
	public static final short AIR_CONDITION_CMD_POWER_ON								= 0xC6;//空调开
	public static final short AIR_CONDITION_CMD_POWER_OFF							= 0xC7;//空调关
	public static final short AIR_CONDITION_CMD_REAR_DEFOGGING_ON				= 0xD0;//后挡风玻璃加热开
	public static final short AIR_CONDITION_CMD_REAR_DEFOGGING_OFF			= 0xD1;//后挡风玻璃加热关
	public static final short AIR_CONDITION_CMD_MODE_SELECT							= 0xD2;//模式选择
	public static final short AIR_CONDITION_CMD_EXTERNAL_CYCLE_MODE_ON 	= 0xD3;//外循环开
	public static final short AIR_CONDITION_CMD_EXTERNAL_CYCLE_MODE_OFF 	= 0xD4;//外循环关

	
	//VIM_AIR_CONDITION_MODE_SETTING_PROPERTY(空调调节模式选择,17款迈腾)
	public static final short AIR_CONDITION_MODE_MANUAL 								= 0x00;//手动
	public static final short AIR_CONDITION_MODE_AUTO 									= 0x01;//Auto
	public static final short AIR_CONDITION_MODE_AC_MAX 								= 0x02;//AC-MAX
	public static final short AIR_CONDITION_MODE_FRONT_DEFOGGING 				= 0x03;//前窗除雾
	
	//VIM_AIR_CONDITION_FAN_SPEED_AUTO_MODE_PROPERTY
	public static final short AIR_CONDITION_FAN_SPEED_LOW								= 0x00;//低(弱)
	public static final short AIR_CONDITION_FAN_SPEED_NORMAL						= 0x01;//中
	public static final short AIR_CONDITION_FAN_SPEED_HIGH							= 0x02;//高(强)
	
	//VIM_MCU_EQ_PRESET_MODE_PROPERTY
	public static final short EQ_PRESET_MODE_NO								= 0;//特效关
	public static final short EQ_PRESET_MODE_CLASSICAL					= 1;//古典
	public static final short EQ_PRESET_MODE_POPULAR						= 2;//流行
	public static final short EQ_PRESET_MODE_CLUB							= 3;//俱乐部
	public static final short EQ_PRESET_MODE_SCENE							= 4;//现场
	public static final short EQ_PRESET_MODE_USER							= 5;//用户
	public static final short EQ_PRESET_MODE_JAZZ								= 6;//爵士
	public static final short EQ_PRESET_MODE_GENTLE							= 7;//轻柔
	public static final short EQ_PRESET_MODE_ROCK							= 8;//摇滚
	public static final short EQ_PRESET_MODE_DISCO							= 9;//迪斯科
	
	/*
	 * 福特SYNC属性值定义
	 */
	//VIM_FORD_SYNC_VERSION_PROPERTY
	public static final short SYNC_VERSION_NONE				 						= 0x00;//无SYNC
	public static final short SYNC_VERSION_V1				 							= 0x01;
	public static final short SYNC_VERSION_V2				 							= 0x02;
	public static final short SYNC_VERSION_V3				 							= 0x03;

	
	//VIM_FORD_SYNC_RUN_MODE_PROPERTY
	public static final short SYNC_RUN_MODE_OFF				 					= 0x00;
	public static final short SYNC_RUN_MODE_ON				 						= 0x01;
	public static final short SYNC_RUN_MODE_MEDIA				 				= 0x02;
	public static final short SYNC_RUN_MODE_PHONE				 				= 0x03;

	//VIM_FORD_SYNC_DEVICE_STATUS_PROPERTY
	public static final short SYNC_DEVICE_NONE										= 0x00;//无SYNC模块
	public static final short SYNC_DEVICE_OK											= 0x01;//有SYNC模块

	//VIM_FORD_SYNC_BLUETOOTH_CONNECT_STATUS_PROPERTY
	public static final short SYNC_BLUETOOTH_DEVICE_DISCONNECT		= 0x00;//SYNC的蓝牙未连接
	public static final short SYNC_BLUETOOTH_DEVICE_CONNECTED			= 0x01;//SYNC的蓝牙已连接

	//VIM_FORD_SYNC_MESSAGE_FLAG_STATUS_PROPERTY
	public static final short SYNC_MESSAGE_ICON_HIDE							= 0x00;//不显示短信图标
	public static final short SYNC_MESSAGE_ICON_SHOW							= 0x01;//显示短信图标

	
	//VIM_FORD_SYNC_SPEECH_STATUS_PROPERTY
	public static final short SYNC_SPEECH_OFF											= 0x00;//非语音状态
	public static final short SYNC_SPEECH_ON											= 0x01;//语音状态

	//VIM_FORD_SYNC_CALL_STATUS_PROPERTY
	public static final short SYNC_CALL_IDLE											= 0x00;//非通话状态
	public static final short SYNC_CALL_ACTIVE										= 0x01;//通话状态

	//VIM_FORD_SYNC_MEDIA_ENABLE_PROPERTY
	public static final short SYNC_MEDIA_INFO_DISABLE							= 0x00;//SYNC的MEDIA下的INFO键不可用
	public static final short SYNC_MEDIA_INFO_ENABLE							= 0x01;//SYNC的MEDIA下的INFO键可用

	//VIM_FORD_SYNC_AUDIO_REQUEST_PROPERTY
	public static final short SYNC_AUDIO_REQUEST_OFF							= 0x00;//请求切换到OFF
	public static final short SYNC_AUDIO_REQUEST_PHONE_ON				= 0x01;//请求切换到电话(有来电)
	public static final short SYNC_AUDIO_REQUEST_PHONE_OFF				= 0x02;//请求退出电话(挂机)
	public static final short SYNC_AUDIO_REQUEST_MEDIA_ON					= 0x03;//请求切换到媒体
	public static final short SYNC_AUDIO_REQUEST_SPEECH_ON				= 0x04;//请求切换到语音
	public static final short SYNC_AUDIO_REQUEST_SPEECH_OFF				= 0x05;//请求退出语音

	//VIM_FORD_SYNC_CUR_MENU_TYPE_PROPERTY
	public static final short SYNC_MENU_TYPE_NONE								=0x00;//无效（不需要显示菜单时为此值）
	public static final short SYNC_MENU_TYPE_BROWSER							=0x02;//USB 浏览/通话历史
	public static final short SYNC_MENU_TYPE_SRC									= 0x03;//当前源
	public static final short SYNC_MENU_TYPE_SETTING_MENU 				= 0x04;//一般的设定菜单
	public static final short SYNC_MENU_TYPE_PHONE_BOOK 					= 0x05;//电话本
	public static final short SYNC_MENU_TYPE_SPEED_DIAL 						= 0x06;//快速拨号
	public static final short SYNC_MENU_TYPE_TALKING 							= 0x07;//通话中
			
	//VIM_FORD_SYNC_PERCENT_BAR_TYPE_IN_MENU_PROPERTY
	public static final short PERCENT_BAR_ATT_NONE 								= 0x00;
	public static final short PERCENT_BAR_ATT_UP_DOWN 						= 0x05;//显示可以向上及可以向下操作（方向图标被点亮）
	public static final short PERCENT_BAR_ATT_UP									= 0x06;//上向方向图标被点亮
	public static final short PERCENT_BAR_ATT_DOWN								= 0x09;//向下方向图标被点亮
	//其它值无效，如果遇到其它值可以考虑显示进度条，也显示方向(此项做不做问题不大)
	
	//VIM_FORD_SYNC_CUR_MENU_ICON_PROPERTY
	public static final short SYNC_MENU_ICON_NONE 								= 0x00;
	public static final short SYNC_MENU_ICON_PHONE								= 0x02;
	public static final short SYNC_MENU_ICON_LINE_IN							= 0x05;
	public static final short SYNC_MENU_ICON_A2DP								= 0x08;
	public static final short SYNC_MENU_ICON_USB									= 0x0A;
	public static final short SYNC_MENU_ICON_IPOD									= 0x0C;
	
	//VIM_FORD_SYNC_CUR_DIALOG_TYPE_PROPERTY
	public static final short SYNC_DIALOG_MESSAGE_TYPE_NONE							= 0x00;//无效 （不需要显示对话框时为此值）
	public static final short SYNC_DIALOG_MESSAGE_TYPE_1_LINE_NO_BUTTON	= 0x01;//1 行文本 没有按键
	public static final short SYNC_DIALOG_MESSAGE_TYPE_1_LINE_4_BUTTON		= 0x02;//1 行文本 +下面 4 个 SOFT KEY
	public static final short SYNC_DIALOG_MESSAGE_TYPE_2_LINE_NO_BUTTON	= 0x03;//....
	public static final short SYNC_DIALOG_MESSAGE_TYPE_2_LINE_4_BUTTON		= 0x04;//....
	public static final short SYNC_DIALOG_MESSAGE_TYPE_3_LINE_NO_BUTTON	= 0x05;//....
	public static final short SYNC_DIALOG_MESSAGE_TYPE_3_LINE_4_BUTTON		= 0x06;//....
	public static final short SYNC_DIALOG_MESSAGE_TYPE_DIAL_REDIAL				= 0x07;//拨号/重拨/来电 V1.14.001
	public static final short SYNC_DIALOG_MESSAGE_TYPE_SPEECH						= 0x08;//SYNC 语音
	public static final short SYNC_DIALOG_MESSAGE_TYPE_DIAL_A_NUMBER		= 0x0B;//拨打一个电话号码
			
	//VIM_FORD_SYNC_MENU_ITEM_LINE_TYPE_PROPERTY(文本显示时,行号1-10)
	public static final short SYNC_LINE_TEXT_ATT_NOR_TEXT 								= 0x00;//普通高亮文本/透明背景
	public static final short SYNC_LINE_TEXT_ATT_NOR_TEXT_GRAY_BKG			= 0x01;//普通高亮文本/灰色背景
	public static final short SYNC_LINE_TEXT_ATT_GRAY_TEXT							= 0x02;//灰色文本/透明背景
	public static final short SYNC_LINE_TEXT_ATT_GRAY_TEXT_GRAY_BKG			= 0x03;//灰色文本/深灰色背景
	public static final short SYNC_LINE_TEXT_ATT_DEEP_GRAY_TEXT					= 0x04;//深灰色文本/透明背景
	public static final short SYNC_LINE_TEXT_ATT_HIDDEN									= 0x05;//不显示的行
	
	//VIM_FORD_SYNC_MENU_ITEM_LINE_TYPE_PROPERTY(SOFT_KEY显示时,行号11-18)
	public static final short SOFT_KEY_STATE_NONE												=0x00;//未知状态
	public static final short SOFT_KEY_STATE_ICON												=0x02;//显示图标
	public static final short SOFT_KEY_STATE_HIGHLIGHT_ICON							=0x03;//显示被选中（灰底)的图标
	public static final short SOFT_KEY_STATE_TEXT												=0x0a; //显示文本
	public static final short SOFT_KEY_STATE_HIGHTLIGHT_TEXT							=0x0b; //显示被选中（灰底)的文本
	
	//VIM_FORD_SYNC_MENU_INFO_COLLECT_PROPERTY
	public static String formatSyncMenuInfoString(int curMenuType,int selectItemInMenu,int percentInMenu,int percentBarType,
			int curMenuIcon,int curDialogType,int selectItemInDialog)
	{
		HashMap<Integer, String> hashMapMenuInfoMap = new HashMap<Integer, String>();
		hashMapMenuInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_CUR_MENU_TYPE_PROPERTY, String.valueOf(curMenuType));
		hashMapMenuInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_SELECTED_ITEM_IN_MENU_PROPERTY, String.valueOf(selectItemInMenu));
		hashMapMenuInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_PERCENT_IN_MENU_PROPERTY, String.valueOf(percentInMenu));
		hashMapMenuInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_PERCENT_BAR_TYPE_IN_MENU_PROPERTY, String.valueOf(percentBarType));
		hashMapMenuInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_CUR_MENU_ICON_PROPERTY, String.valueOf(curMenuIcon));
		hashMapMenuInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_CUR_DIALOG_TYPE_PROPERTY, String.valueOf(curDialogType));
		hashMapMenuInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_SELECTED_ITEM_IN_DIALOG_PROPERTY, String.valueOf(selectItemInDialog));
		return VehicleManager.hashMapToString(hashMapMenuInfoMap);
	}	
		
	//VIM_FORD_SYNC_MENU_ITEM_INFO_COLLECT_PROPERTY
	public static String formatSyncMenuItemInfoString(int menuItemLineId,String menuItemLineContext,int menuItemLineType,int menuItemLineEnable,
			int leftTextIcon,int rightTextIcon)
	{
		HashMap<Integer, String> hashMapMenuItemInfoMap = new HashMap<Integer, String>();
		hashMapMenuItemInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_ID_PROPERTY, String.valueOf(menuItemLineId));
		hashMapMenuItemInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_CONTEXT_PROPERTY, menuItemLineContext);
		hashMapMenuItemInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_TYPE_PROPERTY, String.valueOf(menuItemLineType));
		hashMapMenuItemInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_ENABLE_PROPERTY, String.valueOf(menuItemLineEnable));
		hashMapMenuItemInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_ICON_LEFT_TEXT_PROPERTY, String.valueOf(leftTextIcon));
		hashMapMenuItemInfoMap.put(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_ICON_RIGHT_TEXT_PROPERTY, String.valueOf(rightTextIcon));
		return VehicleManager.hashMapToString(hashMapMenuItemInfoMap);
	}	
		
	//VIM_FORD_SYNC_REQ_CMD_PROPERTY
	public static final short SYNC_REQ_CMD_SPEECH   			= 0x01;//SYNC语音按键
	public static final short SYNC_REQ_CMD_MENU   			= 0x02;//进入SYNC菜单
	public static final short SYNC_REQ_CMD_PHONE   			= 0x03;//切换到PHONE
	public static final short SYNC_REQ_CMD_HANGUP   		= 0x04;//挂电话(或退出电话)
	public static final short SYNC_REQ_CMD_CALL   				= 0x05;//拨打电话
	public static final short SYNC_REQ_CMD_INFO   				= 0x06;//显示SYNC MEDIA INFO信息
	public static final short SYNC_REQ_CMD_SHUFF   			= 0x07;//SYNC MEDIA随机播放按键
	public static final short SYNC_REQ_CMD_PREV   				= 0x08;//SYNC MEDIA上一曲
	public static final short SYNC_REQ_CMD_NEXT   				= 0x09;//SYNC MEDIA下一曲
	public static final short SYNC_REQ_CMD_UP   					= 0x0A;//SYNC 向上按键
	public static final short SYNC_REQ_CMD_DOWN   			= 0x0B;//SYNC 向下按键
	public static final short SYNC_REQ_CMD_OK   					= 0x0C;//SYNC OK按键
	public static final short SYNC_REQ_CMD_NUM0   			= 0x0D;//SYNC 数字按键0
	public static final short SYNC_REQ_CMD_NUM1   			= 0x0E;//SYNC 数字按键1
	public static final short SYNC_REQ_CMD_NUM2   			= 0x0F;//SYNC 数字按键2
	public static final short SYNC_REQ_CMD_NUM3   			= 0x10;//SYNC 数字按键3
	public static final short SYNC_REQ_CMD_NUM4   			= 0x11;//SYNC 数字按键4
	public static final short SYNC_REQ_CMD_NUM5   			= 0x12;//SYNC 数字按键5
	public static final short SYNC_REQ_CMD_NUM6   			= 0x13;//SYNC 数字按键6
	public static final short SYNC_REQ_CMD_NUM7   			= 0x14;//SYNC 数字按键7
	public static final short SYNC_REQ_CMD_NUM8   			= 0x15;//SYNC 数字按键8
	public static final short SYNC_REQ_CMD_NUM9   			= 0x16;//SYNC 数字按键9
	public static final short SYNC_REQ_CMD_STAR   				= 0x17;//SYNC 数字按键*
	public static final short SYNC_REQ_CMD_POUND   			= 0x18;//SYNC 数字按键#
	public static final short SYNC_REQ_CMD_LEFT   				= 0x19;//SYNC 向左按键
	public static final short SYNC_REQ_CMD_RIGHT   			= 0x1A;//SYNC 向右按键
	public static final short SYNC_REQ_CMD_AUX   				= 0x1B;//SYNC AUX按键用于SYNC MEDIA切换
	public static final short SYNC_REQ_CMD_S1   					= 0x1C;//SYNC按键 最左
	public static final short SYNC_REQ_CMD_S2   					= 0x1D;//SYNC按键 左中
	public static final short SYNC_REQ_CMD_S3   					= 0x1E;//SYNC按键 右中
	public static final short SYNC_REQ_CMD_S4   					= 0x1F;//SYNC按键 最右
	public static final short SYNC_REQ_CMD_APP   				= 0x20;//SYNC APPLICATIONS(预留)
	public static final short SYNC_REQ_CMD_MEDIA   			= 0x81;//切换到SYNC MEDIA
	public static final short SYNC_REQ_CMD_OTHERS   			= 0x82;//从SYNC语音/PHONE/MEDIA退出
	public static final short SYNC_REQ_CMD_READY   			= 0x83;//当主机准备好切换音频后发送此命令
	public static final short SYNC_REQ_CMD_ESC   				= 0x84;//退出当前 SYNC 菜单
	public static final short SYNC_REQ_CMD_MENU_ITEM1   	= 0x91;//选择菜单行1
	public static final short SYNC_REQ_CMD_MENU_ITEM2   	= 0x92;//选择菜单行2
	public static final short SYNC_REQ_CMD_MENU_ITEM3   	= 0x93;//选择菜单行3
	public static final short SYNC_REQ_CMD_MENU_ITEM4   	= 0x94;//选择菜单行4
	public static final short SYNC_REQ_CMD_MENU_ITEM5   	= 0x95;//选择菜单行5

	

	
	//VIM_MCU_TTS_PROPERTY
	public static final short TTS_OFF														= 0;
	public static final short TTS_ON														= 1;
	
	//VIM_MCU_BACKLIGHT_STATUS_PROPERTY
	public static final short SCREEN_BACKLIGHT_OFF							= 0;//背光关闭
	public static final short SCREEN_BACKLIGHT_ON								= 1;//背光打开
	public static final short SCREEN_BACKLIGHT_AUTO							= 2;//mcu根据当前状态执行切换，当前关即执行开屏，当前开即执行关屏
	
	//VIM_RIGHT_TURN_CAMERA_PROPERTY
	public static final short CAMERA_OFF												= 0;//摄像头开
	public static final short CAMERA_ON												= 1;//摄像头关
	
	
	//VIM_MCU_RADIO_CUR_BAND_PROPERTY
	public static final short RADIO_BAND_FM1										= 0; //FM1
	public static final short RADIO_BAND_FM2										= 1; //FM2
	public static final short RADIO_BAND_FM3										= 2; //FM3
	public static final short RADIO_BAND_AM1										= 3; //AM1
	public static final short RADIO_BAND_AM2										= 4; //AM2
	
	//VIM_MCU_RADIO_ST_STATUS_PROPERTY
	//VIM_MCU_RADIO_TP_STATUS_PROPERTY
	//VIM_MCU_RADIO_TA_STATUS_PROPERTY
	//VIM_MCU_RADIO_AF_STATUS_PROPERTY
	public static final short RADIO_FLAG_OFF										= 0;	//收音相关属性关闭
	public static final short RADIO_FLAG_ON											= 1;	//收音相关属性开启
	
	//VIM_MCU_RADIO_ST_KEY_STATUS_PROPERTY
	public static final short RADIO_ST_KEY_ON										= 0; //收音立体声模式开
	public static final short RADIO_ST_KEY_OFF									= 1; //收音立体声模式关
	
	//VIM_MCU_RADIO_LOC_STATUS_PROPERTY
	public static final short RADIO_LOC_FAR											= 0; //远程台
	public static final short RADIO_LOC_NEAR										= 1; //进程台
	
	//VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY
	public static final short RADIO_CMD_INIT										= 0;	//收音初始化，保留不用
	public static final short RADIO_CMD_EXIT										= 1;	//退出收音，保留不用
	public static final short RADIO_CMD_REQ_REGION							= 2;	//请求收音区域
	public static final short RADIO_CMD_FM_AM									= 3;	//FM-AM变化
	public static final short RADIO_CMD_FM											= 4;	//FM
	public static final short RADIO_CMD_AM											= 5;	//AM
	public static final short RADIO_CMD_BAND										= 6;	//BAND顺序切换,保留不用
	public static final short RADIO_CMD_NEXT_PRE_CHANNEL				= 10;	//下一个预存台
	public static final short RADIO_CMD_PRIO_PRE_CHANNEL				= 11;	//上一个预存台
	public static final short RADIO_CMD_STEP_UP								= 12;	//向上微调
	public static final short RADIO_CMD_STEP_DOWN							= 13;	//向下微调
	public static final short RADIO_CMD_SEARCH_UP							= 14;	//向上搜台
	public static final short RADIO_CMD_SEARCH_DOWN						= 15;	//向下搜台
	public static final short RADIO_CMD_SCAN										= 16;	//扫描播放电台
	public static final short RADIO_CMD_AUTO_SEARCH						= 17;	//自动搜台
	public static final short RADIO_CMD_STOP_SEARCH						= 18;	//停止搜台
	public static final short RADIO_CMD_SWITCH_STERIO					= 25;//收音立体声开关
	public static final short RADIO_CMD_SWITCH_LOC							= 26;//LOC开关
	public static final short RADIO_CMD_SWITCH_AF							= 27;//AF开关
	public static final short RADIO_CMD_SWITCH_TA							= 28;//TA开关
	public static final short RADIO_CMD_ENABLE_AUDIO						= 29;//收音声音开
	public static final short RADIO_CMD_DISABLE_AUDIO						= 30;//收音声音关
	
	//VIM_MCU_RADIO_CUR_REGION_PROPERTY
	public static final short RADIO_REGION_NORTH_AMERICA				= 0; //北美洲
	public static final short RADIO_REGION_LATIN_AMERICA				= 1; //南美洲(拉丁美洲)
	public static final short RADIO_REGION_EUROPE							= 2; //欧洲
	public static final short RADIO_REGION_OIRT									= 3; //东欧(OIRT)
	public static final short RADIO_REGION_JAPAN								= 4; //日本
	public static final short RADIO_REGION_CHINA								= 5; //中国
	
	//VIM_MCU_EQ_CENTER_FREQ_INFO_PROPERTY
	public static final short EQ_CENTER_FREQ_UNIT_HZ						= 0; //中心频点单位-HZ
	public static final short EQ_CENTER_FREQ_UNIT_KHZ						= 1; //中心频点单位-KHZ
	
	//
	// VIM_VEHICLE_MCU_LAST_SLEEP_REASON
	public static final short MCU_SLEEP_REASON_ACC_OFF 					= 1; 	// ACC断开进入休眠
	public static final short MCU_SLEEP_REASON_HIGHT_VOLTAGE 		= 2; 	// 高压保护进入休眠
	public static final short MCU_SLEEP_REASON_LOW_VOLTAGE 			= 4; 	// 低压保护进入休眠
	
	//VIM_MCU_STATUS_PROPERTY
	public static final short MCU_STATE_UNKNOWN 					= -1; 	// 未知状态,未收到MCU返回数据时的状态
	public static final short MCU_STATE_BOOT 							= 0; 		// BOOT状态
	public static final short MCU_STATE_WAKEUP_ACCON 		= 1; 		// ACC唤醒状态
	public static final short MCU_STATE_WAKEUP_GSENSOR		= 2; 		// GSENSOR唤醒状态
	public static final short MCU_STATE_WAKEUP_MODEM 		= 4; 		// Modem唤醒状态	

	// VIM_VEHICLE_MCU_UPGRADE_STATE
	public static final short MCU_UPGRADE_STATE_UNKNOWN = -1; 	// 未知状态
	public static final short MCU_UPGRADE_STATE_INIT 			= 0; 	// 升级初始化
	public static final short MCU_UPGRADE_STATE_START 		= 1; 	// 升级开始
	public static final short MCU_UPGRADE_STATE_ERASE 		= 2; 	// 擦除数据
	public static final short MCU_UPGRADE_STATE_WRITE 		= 3; 	// 数据写入中
	public static final short MCU_UPGRADE_STATE_VERTIFY 	= 4; 	// 数据确认中
	public static final short MCU_UPGRADE_STATE_SUCCESS 	= 5; 	// 升级成功
	public static final short MCU_UPGRADE_STATE_FAILED 		= 6; 	// 升级失败
	
	//VIM_MCU_STEER_STUDY_STATE_PROPERTY
	public static final short STEER_STUDY_STATE_SELECT_KEY 		= 0x01;//请选定你要学习的按健
	public static final short STEER_STUDY_STATE_STUDYING			= 0x02;//请长按方控按健3s，等学习成功后释放
	public static final short STEER_STUDY_STATE_SUCCESS 				= 0x03;//此方控按健学习成功，请选定下一个，或按学习完成
	public static final short STEER_STUDY_STATE_FAILED 				= 0x04;//方控学习失败,请重学，或与厂家联系
	public static final short STEER_STUDY_STATE_SAVE_SUCCESS 	= 0x05;//恭喜您，方控学习成功，试试效果吧
	public static final short STEER_STUDY_STATE_STUDIED				= 0x06;//方控已学习成功，若要再学，请按重置
	
	//VIM_MCU_STEER_STUDY_KEY_PROPERTY(最多支持20个按键)
	//VIM_MCU_STEER_STUDIED_KEY_DOWN_PROPERTY
	public static final short STEER_STUDY_FUNC_ID_SOURCE							= 0x01;// SRC
	public static final short STEER_STUDY_FUNC_ID_SPEECH							= 0x02;// SPEECH
	public static final short STEER_STUDY_FUNC_ID_NAVIGATION					= 0x03;// Navigation
	public static final short STEER_STUDY_FUNC_ID_TUNER								= 0x04;// Tuner
	public static final short STEER_STUDY_FUNC_ID_MUSIC								= 0x05;// Media Music
	public static final short STEER_STUDY_FUNC_ID_VIDEO								= 0x06;// Media Video
	public static final short STEER_STUDY_FUNC_ID_BT_PHONE						= 0x07;// BT Phone
	public static final short STEER_STUDY_FUNC_ID_BT_MUSIC						= 0x08;// Bt Music
	public static final short STEER_STUDY_FUNC_ID_PLAY_PAUSE					= 0x09;// PLAY/PAUSE
	public static final short STEER_STUDY_FUNC_ID_PLAY_PREV						= 0x0A;// Play Prev
	public static final short STEER_STUDY_FUNC_ID_PLAY_NEXT						= 0x0B;// Play Next
	public static final short STEER_STUDY_FUNC_ID_PHONE_ACCEPT				= 0x0C;// Phone accept
	public static final short STEER_STUDY_FUNC_ID_PHONE_HANGUP				= 0x0D;// Phone hangup
	public static final short STEER_STUDY_FUNC_ID_MUTE								= 0x0E;// Mute
	public static final short STEER_STUDY_FUNC_ID_VOLUME_DOWN				= 0x0F;// Volume down
	public static final short STEER_STUDY_FUNC_ID_VOLUME_UP						= 0x10;// Volume up
	public static final short STEER_STUDY_FUNC_ID_HOME								= 0x11;// Home
	public static final short STEER_STUDY_FUNC_ID_BACK								= 0x12;// Back
	public static final short STEER_STUDY_FUNC_ID_POWER							= 0x13;// Power
	public static final short STEER_STUDY_FUNC_ID_VEHICLE_CONDITION		= 0x14;// Vehicle Condition
	
	
	
	
	//VIM_MCU_STEER_STUDY_ACTION_PROPERTY
	public static final short STEER_STUDY_ACTION_SAVE 		= 0x0;	// 保存
	public static final short STEER_STUDY_ACTION_RESET 	= 0x1;	// 复位
	public static final short STEER_STUDY_ACTION_CLEAR 	= 0x2;	// 清除
	public static final short STEER_STUDY_ACTION_INFO 		= 0x3;	// 信息
	public static final short STEER_STUDY_ACTION_STUDY 	= 0x4;	// 学习
	
	//VIM_MCU_SOURCE_GAIN_PROPERTY
	public static final short SOURCE_TYPE_ARM_ANALOG		= 0x00; // ARM模拟通道
	public static final short SOURCE_TYPE_ARM_MEDIA		= 0x01; // ARM多媒体通道
	public static final short SOURCE_TYPE_TUNER				= 0x02; // 收音通道
	public static final short SOURCE_TYPE_AUX1					= 0x03; // AUX1通道
	public static final short SOURCE_TYPE_AUX2					= 0x04; // AUX2通道
	public static final short SOURCE_TYPE_RESERVED			= 0x05; // 保留通道
	
	//VIM_MCU_USER_KEY_PROPERTY
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final short USER_KEY_VALUE_MIN 							= 2000;
	public static final short USER_KEY_VALYE_MAX							= 3000;
	public static final short USER_KEY_OFFSET									= 2000;
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final short USER_KEY_UNKNOWN								= 0;
	public static final short USER_KEY_NUM0 									= USER_KEY_OFFSET+0x00;
	public static final short USER_KEY_NUM1 									= USER_KEY_OFFSET+0x01;
	public static final short USER_KEY_NUM2 									= USER_KEY_OFFSET+0x02;
	public static final short USER_KEY_NUM3 									= USER_KEY_OFFSET+0x03;
	public static final short USER_KEY_NUM4 									= USER_KEY_OFFSET+0x04;
	public static final short USER_KEY_NUM5 									= USER_KEY_OFFSET+0x05;
	public static final short USER_KEY_NUM6 									= USER_KEY_OFFSET+0x06;
	public static final short USER_KEY_NUM7 									= USER_KEY_OFFSET+0x07;
	public static final short USER_KEY_NUM8 									= USER_KEY_OFFSET+0x08;
	public static final short USER_KEY_NUM9 									= USER_KEY_OFFSET+0x09;
	public static final short USER_KEY_ADD 										= USER_KEY_OFFSET+0x0A;//Add,保留
	public static final short USER_KEY_UP 										= USER_KEY_OFFSET+0x0B;
	public static final short USER_KEY_DOWN 									= USER_KEY_OFFSET+0x0C;
	public static final short USER_KEY_LEFT 										= USER_KEY_OFFSET+0x0D;
	public static final short USER_KEY_RIGHT 									= USER_KEY_OFFSET+0x0E;
	public static final short USER_KEY_OK 										= USER_KEY_OFFSET+0x0F;
	public static final short USER_KEY_PLAY_PAUSE 							= USER_KEY_OFFSET+0x10;// 播放暂停
	public static final short USER_KEY_STOP 									= USER_KEY_OFFSET+0x11;// 停止
	public static final short USER_KEY_PLAY_NEXT 							= USER_KEY_OFFSET+0x12;// 下一曲
	public static final short USER_KEY_PLAY_PREV 							= USER_KEY_OFFSET+0x13;// 上一曲
	public static final short USER_KEY_PLAY_REPEAT 						= USER_KEY_OFFSET+0x14;// 重复模式
	public static final short USER_KEY_PLAY_RANDOM	 					= USER_KEY_OFFSET+0x15;// 随机模式
	public static final short USER_KEY_FAST_FORWARD 					= USER_KEY_OFFSET+0x16;// 快进
	public static final short USER_KEY_FAST_REWIND 						= USER_KEY_OFFSET+0x17;// 快退
	public static final short USER_KEY_FRAME_FORWARD 				= USER_KEY_OFFSET+0x18;// 慢进
	public static final short USER_KEY_FRAME_REWIND 					= USER_KEY_OFFSET+0x19;// 慢退
	public static final short USER_KEY_MENU 									= USER_KEY_OFFSET+0x1A;// menu
	public static final short USER_KEY_ROOT_MENU 							= USER_KEY_OFFSET+0x1B;// DVD的Root菜单
	public static final short USER_KEY_AUDIO_SET 							= USER_KEY_OFFSET+0x1C;// 音频调节
	public static final short USER_KEY_SUB_TITLE 							= USER_KEY_OFFSET+0x1D;// 子标题
	public static final short USER_KEY_ANGLE 									= USER_KEY_OFFSET+0x1E;// 角度
	public static final short USER_KEY_INFO 									= USER_KEY_OFFSET+0x1F;// 信息显示
	public static final short USER_KEY_PBC										= USER_KEY_OFFSET+0x20;// PBC
	public static final short USER_KEY_PROGRAM 								= USER_KEY_OFFSET+0x21;// Program
	public static final short USER_KEY_GOTO									= USER_KEY_OFFSET+0x22;// GOTO
	public static final short USER_KEY_VIDEO_FORMAT 					= USER_KEY_OFFSET+0x23;//制式
	public static final short USER_KEY_ZOOM 									= USER_KEY_OFFSET+0x24;// ZOOM
	public static final short USER_KEY_A_B 										= USER_KEY_OFFSET+0x25;// AB面切换
	public static final short USER_KEY_PLAY 									= USER_KEY_OFFSET+0x26;// 播放
	public static final short USER_KEY_PAUSE 									= USER_KEY_OFFSET+0x27;// 暂停
	public static final short USER_KEY_PLAY_SCAN 							= USER_KEY_OFFSET+0x28;// 扫描播放
	public static final short USER_KEY_RADIO_STEP_UP	 				= USER_KEY_OFFSET+0x29;// 向上微调
	public static final short USER_KEY_RADIO_STEP_DOWN 				= USER_KEY_OFFSET+0x2A;// 向下微调
	public static final short USER_KEY_LIST 										= USER_KEY_OFFSET+0x2B;//列表
	public static final short USER_KEY_BACK 									= USER_KEY_OFFSET+0x2C;//返回
	public static final short USER_KEY_HOME 									= USER_KEY_OFFSET+0x30;//主页
	public static final short USER_KEY_MUTE 									= USER_KEY_OFFSET+0x31;//静音
	public static final short USER_KEY_VOLUME_UP 							= USER_KEY_OFFSET+0x32;//音量加
	public static final short USER_KEY_VOLUME_DOWN 					= USER_KEY_OFFSET+0x33;//音量键
	public static final short USER_KEY_EJECT 									= USER_KEY_OFFSET+0x34;//出仓
	public static final short USER_KEY_EQ 										= USER_KEY_OFFSET+0x35;//EQ调节
	public static final short USER_KEY_VIDEO 									= USER_KEY_OFFSET+0x36;//视频
	public static final short USER_KEY_NAVI 									= USER_KEY_OFFSET+0x37;//导航
	public static final short USER_KEY_DVD 										= USER_KEY_OFFSET+0x38;//DVD
	public static final short USER_KEY_RADIO 									= USER_KEY_OFFSET+0x39;//收音
	public static final short USER_KEY_PHONE_ACCEPT 					= USER_KEY_OFFSET+0x3A;//来电接听
	public static final short USER_KEY_PHONE_HANGUP 					= USER_KEY_OFFSET+0x3B;//来电拒听
	public static final short USER_KEY_MODE 									= USER_KEY_OFFSET+0x3C;//模式
	public static final short USER_KEY_USB 										= USER_KEY_OFFSET+0x3D;//USB
	public static final short USER_KEY_AUX1 									= USER_KEY_OFFSET+0x3E;//AUX1
	public static final short USER_KEY_AUX2 									= USER_KEY_OFFSET+0x3F;//AUX2
	public static final short USER_KEY_BLUETOOTH 							= USER_KEY_OFFSET+0x40;//蓝牙(蓝牙复合按健)
	public static final short USER_KEY_DAY_NIGHT 							= USER_KEY_OFFSET+0x41;//日夜模式
	public static final short USER_KEY_MUSIC 									= USER_KEY_OFFSET+0x42;//音乐
	public static final short USER_KEY_TV 										= USER_KEY_OFFSET+0x43;//电视
	public static final short USER_KEY_VEHICLE_CONDITION 			= USER_KEY_OFFSET+0x44;//车况
	public static final short USER_KEY_LOUDNESS 							= USER_KEY_OFFSET+0x45;//响度
	public static final short USER_KEY_TOUCH_CALIBRATE 				= USER_KEY_OFFSET+0x46;//触摸校准
	public static final short USER_KEY_SYS_SETTING 						= USER_KEY_OFFSET+0x47;//系统设置
	public static final short USER_KEY_STAND_BY 							= USER_KEY_OFFSET+0x48;//待机界面
	public static final short USER_KEY_CLOCK 									= USER_KEY_OFFSET+0x49;//时钟界面
	public static final short USER_KEY_AUDIO_BALANCE 					= USER_KEY_OFFSET+0x50;//音效平衡
	public static final short USER_KEY_CALL           							= USER_KEY_OFFSET+0x53;//拨打电话
	public static final short USER_KEY_RECENT_TASK    					= USER_KEY_OFFSET+0x54;//最近任务
	public static final short USER_KEY_POWER 									= USER_KEY_OFFSET+0x55;//电源键
	public static final short USER_KEY_DVR            							= USER_KEY_OFFSET+0x56;//行车记录仪
	public static final short USER_KEY_BT_MUSIC 							= USER_KEY_OFFSET+0x57;//蓝牙音乐
	public static final short USER_KEY_SOURCE 								= USER_KEY_OFFSET+0x58;//源切换
	public static final short USER_KEY_VOICE 									= USER_KEY_OFFSET+0x59;//语音助手
	public static final short USER_KEY_RADIO_AUTO_SEARCH 			= USER_KEY_OFFSET+0x5A;//收音自动搜索
	public static final short USER_KEY_RADIO_PLAY_SCAN				= USER_KEY_OFFSET+0x5B;//媒体扫描播放
	public static final short USER_KEY_RADIO_FM								= USER_KEY_OFFSET+0x5C;//收音切换到FM波段
	public static final short USER_KEY_RADIO_AM								= USER_KEY_OFFSET+0x5D;//收音切换到AM波段
	public static final short USER_KEY_CAMERA								= USER_KEY_OFFSET+0x5E;//CAMERA(倒车摄像头界面预览)
	public static final short USER_KEY_AIR_CONDITION					= USER_KEY_OFFSET+0x5F;//空调显示界面
	public static final short USER_KEY_RADAR									= USER_KEY_OFFSET+0x61;//雷达
	public static final short USER_KEY_TURN_RIGHT_CAMERA			= USER_KEY_OFFSET+0x62;//右视摄像头预览
	public static final short USER_KEY_RADIO_SEARCH_UP				= USER_KEY_OFFSET+0x63;//收音向上搜索
	public static final short USER_KEY_RADIO_SEARCH_DOWN			= USER_KEY_OFFSET+0x64;//收音向下搜索
	public static final short USER_KEY_SCREEN_OFF							= USER_KEY_OFFSET+0x65;//关屏
	public static final short USER_KEY_EXIT										= USER_KEY_OFFSET+0x70;//退出
	public static final short USER_KEY_SCREENSHOT                      	= USER_KEY_OFFSET+0x71;//系统截图
	public static final short USER_KEY_PLAY_FAST_RELEASE				= USER_KEY_OFFSET+0x80;//快进快退释放
	public static final short USER_KEY_PHONE_LGNORE					= USER_KEY_OFFSET+0x81;//表示不使用主机的而是使用手机的铃声
	public static final short USER_KEY_PHONE_HOLD						= USER_KEY_OFFSET+0x82;//Hold电话
	public static final short USER_KEY_PHONE_CONTINUE					= USER_KEY_OFFSET+0x83;//继续Hold的电话
	public static final short USER_KEY_PHONE_MIC_ON					= USER_KEY_OFFSET+0x84;//电话时Mic开
	public static final short USER_KEY_PHONE_MIC_OFF					= USER_KEY_OFFSET+0x85;//电话时Mic关
	public static final short USER_KEY_PHONE_PRIVATE					= USER_KEY_OFFSET+0x86;//私人电话模式
	public static final short USER_KEY_PHONE_HANDFREE					= USER_KEY_OFFSET+0x87;//免提电话模式
	public static final short USER_KEY_NAVI_ADDR							= USER_KEY_OFFSET+0x88;//导航目的地界面
	public static final short USER_KEY_FAVOR									= USER_KEY_OFFSET+0x89;//收藏
	public static final short USER_KEY_PHONE_LINK							= USER_KEY_OFFSET+0x90;//手机互联
	public static final short USER_KEY_DIALPAD_STAR						= USER_KEY_OFFSET+0x91;//拨号盘*
	public static final short USER_KEY_DIALPAD_POUND					= USER_KEY_OFFSET+0x92;//拨号盘#
	public static final short USER_KEY_VEHICLE_SETTING					= USER_KEY_OFFSET+0x93;//原车设置
	public static final short USER_KEY_AIR_CONDITON_CONTROL		= USER_KEY_OFFSET+0x94;//空调控制界面

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//VIM_MCU_TPMS_DEVICE_TYPE_PROPERTY,设备类型
	public static final short TPMS_DEVICE_TYPE_USB 		= 0;//基于USB连接的胎压模块
	public static final short TPMS_DEVICE_TYPE_UART 	= 1;//基于UART连接的胎压模块
	
	//VIM_MCU_TPMS_DEVICE_TYPE_PROPERTY,设备型号ID
	public static final short TPMS_DEVICE_ID_YAT 			= 0;//永奥图
	
	
	
	
	//VIM_MCU_TPMS_DEVICE_STATE_PROPERTY
	public static final short TPMS_DEVICE_STATE_NO				=	0;//不存在或者设备未准备好
	public static final short TPMS_DEVICE_STATE_READY			=	1;//胎压设备连接正常
	public static final short TPMS_DEVICE_STATE_CHECKING	=	2;//设备正在检测
	
	//VIM_MCU_TPMS_PRESSURE_UNIT_PROPERTY
	public static final short TPMS_PRESSURE_UNIT_BAR			=	0;//胎压单位为BAR
	public static final short TPMS_PRESSURE_UNIT_PSI			=	1;//胎压单位为PSI
	public static final short TPMS_PRESSURE_UNIT_KPA			=	2;//胎压单位为KPA
	
	//VIM_MCU_TPMS_TEMP_UNIT_PROPERTY
	public static final short TPMS_TEMP_UNIT_C						=	0;//温度单位为摄氏度
	public static final short TPMS_TEMP_UNIT_F						=	1;//温度单位为华氏度
	
	//VIM_MCU_TPMS_TIRE_DATA_PROPERTY 轮胎位置
	//VIM_MCU_TPMS_DELTE_TIRE_ID_PROPERTY
	public static final short TIRE_ALL										= 0;
	public static final short TIRE_FRONT_RIGHT						= 1;
	public static final short TIRE_FRONT_LEFT							= 2;
	public static final short TIRE_REAR_RIGHT							= 3;
	public static final short TIRE_REAR_LEFT							= 4;
	public static final short TIRE_REAR_SPARE							= 5;//备胎位置

	//VIM_MCU_TPMS_INIT_DATA_STATE_PROPERTY
	public static final short TPMS_INIT_DATA_STATE_START			= 1;//开始接收读码器数据
	public static final short TPMS_INIT_DATA_STATE_END				= 2;//接收读码器数据结束

	
	//VIM_MCU_TPMS_AUTO_MATCH_CODE_STATE_PROPERTY
	public static final short TPMS_MATCH_CODE_STATE_START		= 1;//开始充气对码
	public static final short TPMS_MATCH_CODE_STATE_END			= 2;//对码结束
	
	//传感器电压状态
	public static final short TPMS_SENSOR_BATTERY_STATUS_NORMAL 	= 0;//胎压传感器电压正常
	public static final short TPMS_SENSOR_BATTERY_STATUS_LOW 			= 1;//胎压传感器电压过低
	
	//胎压状态
	public static final short TPMS_PRESSURE_STATUS_NORMAL 	= 0;//胎压正常
	public static final short TPMS_PRESSURE_STATUS_HIGH 		= 1;//胎压过高
	public static final short TPMS_PRESSURE_STATUS_LOW 			= 2;//胎压过低

	//温度状态
	public static final short TPMS_TEMP_STATUS_NORMAL 			= 0;//轮胎温度正常
	public static final short TPMS_TEMP_STATUS_HIGH 				= 1;//轮胎温度过高

	//气阀状态
	public static final short TPMS_TIRE_VALVE_STATUS_NORMAL 				= 0;//气门阀正常
	public static final short TPMS_TIRE_VALVE_STATUS_DEFLATION_FAST 	= 1;//急漏气	
	public static final short TPMS_TIRE_VALVE_STATUS_DEFLATION 			= 2;//慢漏气	
	public static final short TPMS_TIRE_VALVE_STATUS_INFLATION 			= 3;//加气	
	
	//VIM_MCU_TPMS_REQ_COMMAND_PROPERTY
	public static final short TPMS_CMD_REQ_DEVICE_STATE						= 0;//请求设备状态
	public static final short TPMS_CMD_REQ_TIRE_DATA_ALL					= 1;//请求所有轮胎数据
	public static final short TPMS_CMD_REQ_TIRE_DATA_FRONT_RIGHT	= 2;
	public static final short TPMS_CMD_REQ_TIRE_DATA_FRONT_LEFT		= 3;
	public static final short TPMS_CMD_REQ_TIRE_DATA_REAR_RIGHT		= 4;
	public static final short TPMS_CMD_REQ_TIRE_DATA_REAR_LEFT		= 5;
	public static final short TPMS_CMD_REQ_TIRE_DATA_REAR_SPARE		= 6;//备胎位置
	public static final short TPMS_CMD_REQ_ALARM_PARAM_ALL				= 10;//请求所有报警参数
	public static final short TPMS_CMD_REQ_ALARM_PARAM_AXIS_1		= 11;//请求第一轴胎压报警参数
	public static final short TPMS_CMD_REQ_ALARM_PARAM_AXIS_2		= 12;//请求第二轴胎压报警参数
	public static final short TPMS_CMD_REQ_ALARM_PARAM_AXIS_3		= 13;//请求第三轴胎压报警参数
	public static final short TPMS_CMD_REQ_ALARM_PARAM_AXIS_4		= 14;//请求第四轴胎压报警参数
	public static final short TPMS_CMD_REQ_ALARM_PARAM_AXIS_DRAG	= 15;//请求托卡胎压报警参数
	public static final short TPMS_CMD_REQ_ALARM_PARAM_TEMP			= 16;//请求温度报警参数
	public static final short TPMS_CMD_REQ_RECEIVE_INIT_DATA			= 20;//请求接收读码器数据

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//VIM_MCU_REQ_COMMAND_PROPERTY
	public static final short MCU_CMD_REQ_VERSION 						= 0; 	// 请求MCU版本号
	public static final short MCU_CMD_REQ_VOLTAGE 						= 1;	// 请求MCU当前电压值
	public static final short MCU_CMD_REQ_LAST_SLEEP_REASON 	= 2;	// 请求MCU上次休眠原因
	public static final short MCU_CMD_REQ_UPGRADE 						= 3;	// 请求MCU升级
	public static final short MCU_CMD_REQ_RESET 							= 4;	// 请求MCU重启
	public static final short MCU_CMD_REQ_STATE 							= 5;	// 请求MCU状态
	public static final short MCU_CMD_POWER_OFF_READY        		= 6;// ARM准备好进入休眠
	public static final short MCU_CMD_REQ_BACKLIGHT_BRIGHTNESS = 7;// 请求背光亮度
	public static final short MCU_CMD_REQ_BACKLIGHT_STATUS		= 8;//请求背光状态
	
	public static final short MCU_CMD_REQ_REVERSE_STATUS         	= 10;// 请求倒车状态
	public static final short MCU_CMD_REQ_BRAKE_STATUS         		= 11;// 请求手刹状态
	public static final short MCU_CMD_REQ_ILL_STATUS               	= 12;// 请求大灯状态
	
	public static final short MCU_CMD_REQ_MEDIA_VOLUME_INFO   = 20;// 请求媒体音量信息
	public static final short MCU_CMD_REQ_BT_VOLUME_INFO   		= 21;// 请求蓝牙通话音量信息
	public static final short MCU_CMD_REQ_NAVI_VOLUME_INFO   	= 22;// 请求导航音量信息
	public static final short MCU_CMD_REQ_GPS_MONITOR_STATUS	= 23;// 请求GPS监听开关
	public static final short MCU_CMD_REQ_GPS_MIX_STATUS 			= 24;// 请求GPS混音开关
	public static final short MCU_CMD_REQ_GPS_MIX_LEVEL 			= 25;// 请求GPS混音比例
	public static final short MCU_CMD_PLAY_KEY_SPEAKER				= 26;// 请求播放按键音
	public static final short MCU_CMD_REQ_KEY_BEPP_STATUS		= 27;// 请求按键音开关状态
	public static final short MCU_CMD_REQ_REVERSE_BEPP_STATUS= 28;// 请求倒车声音开关状态

	public static final short MCU_CMD_REQ_AUDIO_BALANCE_INFO	= 40;//请求声场平衡信息
	public static final short MCU_CMD_REQ_EQ_BASS_INFO				= 41;// 请求EQ-低音信息
	public static final short MCU_CMD_REQ_EQ_ALTO_INFO				= 42;// 请求EQ-中音信息
	public static final short MCU_CMD_REQ_EQ_TREBLE_INFO			= 43;// 请求EQ-高音信息
	public static final short MCU_CMD_REQ_EQ_SUBWOOFER_INFO	= 44;// 请求EQ-重低音信息
	public static final short MCU_CMD_REQ_EQ_CENTER_FREQ_INFO= 45;// 请求中心频点信息
	public static final short MCU_CMD_REQ_EQ_LOUDNESS_INFO		= 46;// 请求EQ-等响度信息
	public static final short MCU_CMD_REQ_EQ_PRESET_MODE_INFO= 47;// 请求EQ-预设模式信息
	
	public static final short MCU_CMD_REQ_DVR_SYNC_TIME			= 50;//请求DVR同步系统时间
	
	public static final short MCU_CMD_REQ_ENTER_STEER_STUDY	= 60;//进入方向盘学习
	public static final short MCU_CMD_REQ_EXIT_STEER_STUDY		= 61;//退出方向盘学习
	
	//以下属性仅为调试时使用
	
	public static final short MCU_CMD_REQ_OPEN_SERIAL 				= 110; // 请求串口打开
	public static final short MCU_CMD_REQ_CLOSE_SERIAL 				= 111; // 请求串口关闭
	public static final short MCU_CMD_REQ_SOURCE_GAIN				= 112; // 请求源增益值

	//VIM_MCU_DVR_TYPE_PROPERTYE
	public static final int DVR_TYPE_MOCAR_NO_TOUCH_PANEL_VIA_IR 		= 0;//摩卡通过IR连接的无触控DVR,仅支持发送键值
	public static final int DVR_TYPE_MOCAR_NO_TOUCH_PANEL_VIA_UART 	= 1;//摩卡通过UART连接的无触控DVR,仅支持发送键值
	public static final int DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_IR 			= 2;//摩卡通过IR连接的有触控DVR,支持发送触摸和键值
	public static final int DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_UART 		= 3;//摩卡通过UART连接的有触控DVR,支持发送触摸和键值
	public static final int DVR_TYPE_CXJ_TOUCH_PANEL_VIA_IR 					= 30;//车行健通过IR连接的有触控DVR,支持发送触摸和键值	
}
