/*
 *    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
 *    Qualcomm Technologies Proprietary and Confidential.
 *
 */

package com.mct;

import android.service.notification.INotificationListener;
import android.test.InstrumentationTestCase;
import android.webkit.FindActionModeCallback;

/**
 * VehicleInterfaceProperties provides definitions to be used by application
 * developer to set/get vehicle data. Please note that this class is work in
 * progress, and there might be few changes going forward.
 */
public final class VehicleInterfaceProperties
{
	/**
	 * Definition for Qualcomm property Ids. These IDs are unique and used to identify
	 * specific property.
	 */

	public static final int VIM_ODOMETER_PROPERTY = 1000;									//里程(总里程)

	public static final int VIM_TRANSMISSION_OIL_LIFE_LVL_PROPERTY = 1001;		//传动装置润滑油余量
	
	public static final int VIM_TRANSMISSION_OIL_TEMP_PROPERTY = 1002;			//传动装置润滑油温度

	public static final int VIM_BRAKE_FLUID_LVL_PROPERTY = 1003;						//刹车液等级

	public static final int VIM_WASHER_FLUID_LVL_PROPERTY = 1004;						//雨刷水等级

	public static final int VIM_MALFUNCTION_INDICATOR_PROPERTY = 1005;			//故障码属性

	public static final int VIM_BATTERY_VOLTAGE_PROPERTY = 1006;						//电池电压

	public static final int VIM_BATTERY_CURRENT_PROPERTY = 1007;						//电池电流

	public static final int VIM_TIRE_PRESSURE_FRONT_LEFT_PROPERTY = 1008;		//前左胎压

	public static final int VIM_TIRE_PRESSURE_FRONT_RIGHT_PROPERTY = 1009;	//前右胎压

	public static final int VIM_TIRE_PRESSURE_REAR_LEFT_PROPERTY = 1010;		//后左胎压

	public static final int VIM_TIRE_PRESSURE_REAR_RIGHT_PROPERTY = 1011;		//后右胎压

	public static final int VIM_SECURITY_ALERT_PROPERTY = 1012;							//安全警告

	public static final int VIM_PARKING_LIGHTS_PROPERTY = 1013;							//驻车灯

	public static final int VIM_PARKING_BRAKES_PROPERTY = 1014;							//驻车制动(手刹)

	public static final int VIM_INTERIOR_TEMP_PROPERTY = 1015;							//车内温度

	public static final int VIM_EXTERIOR_TEMP_PROPERTY = 1016;							//车外温度

	public static final int VIM_EXTERIOR_BRGHTNESS_PROPERTY = 1017;					//外部亮度

	public static final int VIM_RAIN_SENSOR_PROPERTY = 1018;								//雨量传感器

	public static final int VIM_MAIN_WSHIELD_WIPER_PROPERTY = 1019;				//前主雨刷

	public static final int VIM_REAR_WSHIELD_WIPER_PROPERTY = 1020;				//后雨刷

	public static final int VIM_HVAC_FAN_DIRECTION_PROPERTY = 1021;					//送风模式(可能包括左送风与右送风)

	public static final int VIM_HVAC_FAN_SPEED_PROPERTY = 1022;							//风速(整形数组,元素范围为0-max)

	public static final int VIM_HVAC_FAN_TARGET_TEMP_PROPERTY = 1023;			//吹出温度（根据值数组大小，如大小为2表示左边与右边温度）

	public static final int VIM_AIR_CONDITIONING_PROPERTY = 1024;						//空调开关

	public static final int VIM_AIR_CIRCULATION_PROPERTY = 1025;						//换气开关

	public static final int VIM_HEATER_PROPERTY = 1026;										//加热开关

	public static final int VIM_HEATER_STRNG_WHEEL_PROPERTY = 1027;				//加热方向盘

	public static final int VIM_HEATER_DRVNG_SEAT_PROPERTY = 1028;					//驾驶座加热等级(0表示关,范围0-3)

	public static final int VIM_HEATER_PSNGR_SEAT_PROPERTY = 1029;					//副驾驶座加热等级(0表示关,范围0-3)

	public static final int VIM_DEFROST_WSHIELD_PROPERTY = 1030;						//挡风玻璃除霜

	public static final int VIM_DEFROST_FRONT_WINDOW_PROPERTY = 1031;			//前窗除霜

	public static final int VIM_DEFROST_REAR_WINDOW_PROPERTY = 1032;				//后窗除霜

	public static final int VIM_DEFROST_SIDE_MIRRORS_PROPERTY = 1033;				//后视镜除霜
	
	public static final int VIM_DRVR_WINDOW_PROPERTY = 1034;							//驾驶座车窗

	public static final int VIM_PSNGR_WINDOW_PROPERTY = 1035;							//副驾驶座车窗

	public static final int VIM_REAR_LEFT_WINDOW_PROPERTY = 1036;					//后左车窗

	public static final int VIM_REAR_RIGHT_WINDOW_PROPERTY = 1037;					//后右车窗		

	public static final int VIM_SUN_ROOF_OPEN_PROPERTY = 1038;							//天窗打开

	public static final int VIM_SUN_ROOF_TILT_PROPERTY = 1039;							//天窗倾斜

	public static final int VIM_CONV_ROOF_PROPERTY = 1040;									//天窗关闭

	public static final int VIM_TRANSMISSION_GEAR_STATUS_PROPERTY = 1041;		//档位

	public static final int VIM_VEHICLE_POWER_MODE_PROPERTY = 1042;				//电源模式

	public static final int VIM_RMNG_FUEL_LVL_PROPERTY = 1043;							//燃油余量

	public static final int VIM_RMNG_DRVNG_RANGE_PROPERTY = 1044;					//剩余里程

	public static final int VIM_ENGN_OIL_RMNG_PROPERTY = 1045;							//机油余量			
	
	public static final int VIM_ENGN_OIL_CHNG_PROPERTY = 1046;							//???

	public static final int VIM_ENGN_OIL_TEMP_PROPERTY = 1047;							//机油温度						

	public static final int VIM_COOLANT_LVL_PROPERTY = 1048;								//冷却液级别

	public static final int VIM_COOLANT_TEMP_PROPERTY = 1049;							//冷却液温度

	public static final int VIM_STRNG_WHEEL_ANGLE_PROPERTY = 1050;					//方形盘角度

	public static final int VIM_VEHICLE_WMI_PROPERTY = 1051;								//车辆WMI码

	public static final int VIM_VEHICLE_VIN_PROPERTY = 1052;								//车辆VIN码

	public static final int VIM_VEHICLE_TYPE_PROPERTY = 1053;								//车辆类型

	public static final int VIM_VEHICLE_DOOR_TYPE_1ST_ROW_PROPERTY = 1054;	//第一排车门

	public static final int VIM_VEHICLE_DOOR_TYPE_2ND_ROW_PROPERTY = 1055;	//第二排车门

	public static final int VIM_VEHICLE_DOOR_TYPE_3RD_ROW_PROPERTY = 1056;	//第三排车门

	public static final int VIM_VEHICLE_FUEL_TYPE_PROPERTY = 1057;						//车辆燃油类型

	public static final int VIM_VEHICLE_TRANS_GEAR_TYPE_PROPERTY = 1058;		//变速器类型

	public static final int VIM_VEHICLE_WHEEL_INFO_RADIUS_PROPERTY = 1059;	//车轮半径

	public static final int VIM_VEHICLE_WHEEL_INFO_TRACK_PROPERTY = 1060;		//车轮?

	public static final int VIM_SPEEDO_METER_PROPERTY = 1061;							//车速表(当前车速)

	public static final int VIM_ENGINE_SPEED_PROPERTY = 1062;								//引擎转速

	public static final int VIM_TRIP_METER_1_MILEAGE_PROPERTY = 1063;				//行程表1的里程(本次里程)

	public static final int VIM_TRIP_METER_2_MILEAGE_PROPERTY = 1064;				//行程表2的里程(累计里程)

	public static final int VIM_TRIP_METER_1_AVG_SPEED_PROPERTY = 1065;			//行程表1的平均速度

	public static final int VIM_TRIP_METER_2_AVG_SPEED_PROPERTY = 1066;			//行程表2的平均速度

	public static final int VIM_TRIP_METER_1_FUEL_CONSUMPTION_PROPERTY = 1067;//行程表1记录的耗油量(本次耗油量)

	public static final int VIM_TRIP_METER_2_FUEL_CONSUMPTION_PROPERTY = 1068;//行程表2记录的耗油量(累计耗油量)

	public static final int VIM_CRUISE_CONTROL_STATUS_PROPERTY = 1069;			//自动巡航状态

	public static final int VIM_CRUISE_CONTROL_SPEED_PROPERTY = 1070;				//自动巡航速度

	public static final int VIM_ANTI_LOCK_BRK_SYSTEM_PROPERTY = 1071;				//ABS系统状态(防抱死制动系统)

	public static final int VIM_TRACTION_CONTROL_SYSTEM_PROPERTY = 1072;		//TCS系统状态(牵引力控制系统)

	public static final int VIM_ELECTRONIC_STABILITY_CONTROL_PROPERTY = 1073;//ESC系统状态(电子稳定控制系统)

	public static final int VIM_VEHICLE_TOP_SPEED_LIMIT_PROPERTY = 1074;			//汽车最高速度限制

	public static final int VIM_AIR_BAG_STATUS_DRIVER_PROPERTY = 1075;			//驾驶员位安全气囊状态

	public static final int VIM_AIR_BAG_STATUS_PSNGR_PROPERTY = 1076;				//副驾驶位安全气囊状态

	public static final int VIM_AIR_BAG_STATUS_SIDE_PROPERTY = 1077;				//两边安全气囊状态

	public static final int VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY = 1078;		//驾驶员位车门状态

	public static final int VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY = 1079;		//副驾驶位车门状态

	public static final int VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY = 1080;//后左车门状态

	public static final int VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY = 1081;//后右车门状态

	public static final int VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY = 1082;		//行李箱车门状态

	public static final int VIM_FUEL_FILTER_CAP_STATUS_PROPERTY = 1083;			//燃油过滤器状态

	public static final int VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY = 1084;			//引擎盖状态

	public static final int VIM_DOOR_LOCK_STATUS_DRIVER_PROPERTY = 1085;		//驾驶员位车门锁状态

	public static final int VIM_DOOR_LOCK_STATUS_PSNGR_PROPERTY = 1086;		//副驾驶位车门锁状态

	public static final int VIM_DOOR_LOCK_STATUS_REAR_LEFT_PROPERTY = 1087;//后左位车门锁状态

	public static final int VIM_DOOR_LOCK_STATUS_REAR_RIGHT_PROPERTY = 1088;//后右位车门锁状态

	public static final int VIM_CHILD_SAFETY_LOCK_REAR_LEFT_PROPERTY = 1089;//后左儿童安全锁状态

	public static final int VIM_CHILD_SAFETY_LOCK_REAR_RIGHT_PROPERTY = 1090;//后右儿童安全锁状态

	public static final int VIM_OCCUPANT_STATUS_DRIVER_PROPERTY = 1091;			//驾驶位使用状态

	public static final int VIM_OCCUPANT_STATUS_PSNGR_PROPERTY = 1092;			//副驾驶位使用状态

	public static final int VIM_OCCUPANT_STATUS_REAR_LEFT_PROPERTY = 1093;	//后左位使用状态

	public static final int VIM_OCCUPANT_STATUS_REAR_RIGHT_PROPERTY = 1094;//后右位使用状态

	public static final int VIM_OCCUPANT_STATUS_REAR_CENTER_PROPERTY = 1095;//后中间位使用状态

	public static final int VIM_SEAT_BELT_STATUS_DRIVER_PROPERTY = 1096;			//驾驶位安全带状态

	public static final int VIM_SEAT_BELT_STATUS_PSNGR_PROPERTY = 1097;				//副驾驶位安全带状态

	public static final int VIM_SEAT_BELT_STATUS_REAR_LEFT_PROPERTY = 1098;		//后左位安全带状态

	public static final int VIM_SEAT_BELT_STATUS_REAR_RIGHT_PROPERTY = 1099;	//后右位安全带状态

	public static final int VIM_SEAT_BELT_STATUS_REAR_CENTER_PROPERTY = 1100;	//后中间位安全带状态

	public static final int VIM_WINDOW_LOCK_STATUS_DRIVER_PROPERTY = 1101;		//驾驶位车窗锁	

	public static final int VIM_WINDOW_LOCK_STATUS_PSNGR_PROPERTY = 1102;		//副驾驶位车窗锁

	public static final int VIM_WINDOW_LOCK_STATUS_REAR_LEFT_PROPERTY = 1103;

	public static final int VIM_WINDOW_LOCK_STATUS_REAR_RIGHT_PROPERTY = 1104;

	public static final int VIM_OBSTACLE_DISTANCE_SENSOR_STATUS_PROPERTY = 1105;	//障碍物距离传感器状态

	public static final int VIM_OBSTACLE_DISTANCE_FRONT_CENTER_PROPERTY = 1106;		//障碍物正前方距离

	public static final int VIM_OBSTACLE_DISTANCE_FRONT_LEFT_PROPERTY = 1107;			//障碍物前左方距离

	public static final int VIM_OBSTACLE_DISTANCE_FRONT_RIGHT_PROPERTY = 1108;		//障碍物前右方距离

	public static final int VIM_OBSTACLE_DISTANCE_REAR_CENTER_PROPERTY = 1109;

	public static final int VIM_OBSTACLE_DISTANCE_REAR_LEFT_PROPERTY = 1110;

	public static final int VIM_OBSTACLE_DISTANCE_REAR_RIGHT_PROPERTY = 1111;

	public static final int VIM_OBSTACLE_DISTANCE_MIDDLE_LEFT_PROPERTY = 1112;

	public static final int VIM_OBSTACLE_DISTANCE_MIDDLE_RIGHT_PROPERTY = 1113;

	public static final int VIM_FRONT_COLLISION_DETECTION_STATUS_PROPERTY = 1114;		//前方碰撞检测状态

	public static final int VIM_FRONT_COLLISION_DETECTION_DISTANCE_PROPERTY = 1115;	//前方碰撞检测距离

	public static final int VIM_FRONT_COLLISION_DETECTION_TIME_PROPERTY = 1116;			//前方碰到检测时间

	public static final int VIM_HEAD_LIGHTS_PROPERTY = 1117;												//前大灯

	public static final int VIM_AUTOMATIC_HEAD_LIGHTS_PROPERTY = 1118;							//自动前大灯

	public static final int VIM_DYNAMIC_HIGH_BEAM_PROPERTY = 1119;									//动态远光灯

	public static final int VIM_HEAD_LIGHTS_HIGH_BEAM_PROPERTY = 1120;							//前大灯远光灯				

	public static final int VIM_LEFT_TURN_LIGHT_PROPERTY = 1121;										//左转向灯

	public static final int VIM_RIGHT_TURN_LIGHT_PROPERTY = 1122;										//右转向灯

	public static final int VIM_BRAKE_LIGHT_PROPERTY = 1123;												//刹车灯

	public static final int VIM_LIGHT_STATUS_FOG_FRONT_PROPERTY = 1124;							//前雾灯

	public static final int VIM_LIGHT_STATUS_FOG_REAR_PROPERTY = 1125;							//后雾灯

	public static final int VIM_HAZARD_LIGHT_STATUS_PROPERTY = 1126;								//警告灯

	public static final int VIM_PARKING_LIGHT_PROPERTY = 1127;											//停车灯

	public static final int VIM_INTR_LIGHT_DRIVER_PROPERTY = 1128;									//驾驶员位内部灯

	public static final int VIM_INTR_LIGHT_PSNGR_PROPERTY = 1129;										//副驾驶员位内部灯

	public static final int VIM_INTR_LIGHT_CENTER_PROPERTY = 1130;									//中间内部灯

	public static final int VIM_INTR_LIGHT_3RD_ROW_PROPERTY = 1131;								//第三排内部灯

	public static final int VIM_VOLUME_LVL_PROPERTY = 1132;													//音量大小

	public static final int VIM_FADE_LVL_PROPERTY = 1133;														//声音前后均衡

	public static final int VIM_TREBLE_LVL_PROPERTY = 1134;													//高音大小

	public static final int VIM_BASS_LVL_PROPERTY = 1135;														//低音大小

	public static final int VIM_BRIGHTNESS_LVL_PROPERTY = 1136;											//亮度级别

	public static final int VIM_REAR_LEFT_HEAD_PHONE_STATUS_PROPERTY = 1137;				//后左喇叭状态

	public static final int VIM_REAR_LEFT_HEAD_PHONE_VOLUME_PROPERTY = 1138;				//后左喇叭音量

	public static final int VIM_REAR_RIGHT_HEAD_PHONE_STATUS_PROPERTY = 1139;				//后右喇叭状态
		
	public static final int VIM_REAR_RIGHT_HEAD_PHONE_VOLUME_PROPERTY = 1140;			//后右喇叭音量

	public static final int VIM_RADIO_STATUS_PROPERTY = 1141;												//收音机状态

	public static final int VIM_RADIO_STATION_PROPERTY = 1142;											//收音机电台

	public static final int VIM_CD_STATUS_PROPERTY = 1143;													//CD状态

	public static final int VIM_DYNAMIC_ADAPT_SOUND_PROPERTY = 1144;								//自动声音调节

	public static final int VIM_DYNAMIC_ADAPT_DISPLAY_PROPERTY = 1145;							//自动光线调节

	public static final int VIM_BT_STATUS_PROPERTY = 1146;													//蓝牙状态

	public static final int VIM_USB_MEDIA_STATUS_PROPERTY = 1147;										//USB媒体状态

	public static final int VIM_AUDIO_SOURCE_STATUS_PROPERTY = 1148;								//音频源状态

	public static final int VIM_KEY_PROPERTY = 1149;																//键值

	public static final int VIM_LANGUAGE_PROPERTY = 1150;													//操作语言
	
	public static final int VIM_MEASUREMENT_SYSTEM_PROPERTY = 1151;								//系统检测

	public static final int VIM_MEASUREMENT_FUEL_PROPERTY = 1152;									//燃油检测

	public static final int VIM_MEASUREMENT_DISTANCE_PROPERTY = 1153;							//里程检测

	public static final int VIM_MEASUREMENT_SPEED_PROPERTY = 1154;									//速度检测
	
	public static final int VIM_MEASUREMENT_CONSUMPTION_PROPERTY = 1155;						//耗油量检测

	public static final int VIM_MIRROR_DRIVER_PROPERTY = 1156;											//驾驶位反射镜

	public static final int VIM_MIRROR_PSNGR_PROPERTY = 1157;											//副驾驶位反射镜

	public static final int VIM_MIRROR_INTERIOR_PROPERTY = 1158;										//内部反射镜

	public static final int VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY = 1159;					//方向盘角度(正为向右，负为向左)

	public static final int VIM_STEERING_WHEEL_POSN_TILT_PROPERTY = 1160;						//方向盘倾斜位置

	public static final int VIM_DRIVING_MODE_PROPERTY = 1161;											//行车模式

	public static final int VIM_DRIVER_SEAT_POSN_RECL_SEAT_BACK_PROPERTY = 1162;

	public static final int VIM_DRIVER_SEAT_POSN_SLIDE_PROPERTY = 1163;

	public static final int VIM_DRIVER_SEAT_POSN_CUSHION_HEIGHT_PROPERTY = 1164;

	public static final int VIM_DRIVER_SEAT_POSN_HEAD_REST_PROPERTY = 1165;

	public static final int VIM_DRIVER_SEAT_POSN_BACK_CUSHION_PROPERTY = 1166;

	public static final int VIM_DRIVER_SEAT_POSN_SIDE_CUSHION_PROPERTY = 1167;

	public static final int VIM_PSNGR_SEAT_POSN_RECL_SEAT_BACK_PROPERTY = 1168;		//实际靠背位置

	public static final int VIM_PSNGR_SEAT_POSN_SLIDE_PROPERTY = 1169;						//实际扶手位置

	public static final int VIM_PSNGR_SEAT_POSN_CUSHION_HEIGHT_PROPERTY = 1170;		//座位高度

	public static final int VIM_PSNGR_SEAT_POSN_HEAD_REST_PROPERTY = 1171;				//头靠

	public static final int VIM_PSNGR_SEAT_POSN_BACK_CUSHION_PROPERTY = 1172;			//靠垫

	public static final int VIM_PSNGR_SEAT_POSN_SIDE_CUSHION_PROPERTY = 1173;			//扶手垫	

	public static final int VIM_DASH_BOARD_ILLUMINATION_PROPERTY = 1174;					//仪表盘照明

	public static final int VIM_VEHICLE_SOUND_MODE_PROPERTY = 1175;							//车辆声音模式
	
	
	/* 
	 * Extern Vehicle Property
	 */
	public static final int VIM_VEHICLE_MODEL_PROPERTY 												= 5000;//车型(参数形式为{CanBoxType,CarModel})
	
	public static final int VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY 						= 5001;//系统支持的车型
	
	public static final int VIM_VEHICLE_NAME_PROPERTY 												= 5002;//车型名(通过ID值获取具体的车型名，暂保留)
	
	public static final int VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY 						= 5003;//系统支持的CAN盒
	
	public static final int VIM_NEW_MESSAGE_NOTIFICATION_PROPERTY 						= 5004;//新消息提醒
	
	public static final int VIM_VEHICLE_FACTORY_SETTING_CMD_PROPERTY					= 5039;//工厂设置命令
	
	public static final int VIM_VEHICLE_COLOR_THEME_PROPERTY									= 5040;//原车颜色主题
	
	public static final int VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY 						= 5050;//空调温度单位(默认为摄氏度)
	
	public static final int VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY 					= 5051;//空调循环模式
	
	public static final int VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY 					= 5052;//空调制冷模式(数组类型)
	
	public static final int VIM_AIR_CONDITIONING_REAR_FLAG_PROPERTY 						= 5053;//后座空调锁定灯状态
	
	public static final int VIM_DRIVER_SEAT_FAN_LEVEL_PROPERTY 								= 5054;//主驾驶席座椅吹风等级
	
	public static final int VIM_PSNGR_SEAT_FAN_LEVEL_PROPERTY 									= 5055;//副驾驶席座椅吹风等级
	
	public static final int VIM_EXTERIOR_TEMP_UNIT_PROPERTY										= 5056;//外部温度单位
	
	public static final int VIM_HVAC_FAN_STRENGTH															= 5057;//风量强度
	
	public static final int VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY					= 5060;//泊车辅助系统开关
	
	public static final int VIM_VEHICLE_FRONT_RADAR_PROPERTY                                   = 5061;//原车前雷达(前左、前左中、前右中、前右)
	
	public static final int VIM_VEHICLE_REAR_RADAR_PROPERTY                                   	= 5062;//原车后雷达(后左、后左中、后右中、后右)
	
	public static final int VIM_VEHICLE_LEFT_RADAR_PROPERTY                                   	= 5063;//原车左雷达(左前、左中前、左中后、左后)
	
	public static final int VIM_VEHICLE_RIGHT_RADAR_PROPERTY                                   = 5064;//原车右雷达(右前、右中前、右中后、右后)
	
	public static final int VIM_VEHICLE_RADAR_TONE_PROPERTY										= 5065;//雷达提示音状态
	
	public static final int VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY						= 5066;//雷达距离范围(0-max)
	
	public static final int VIM_VEHICLE_PARKING_MODE_PROPERTY									= 5067;//驻车模式
	
	public static final int VIM_VEHICLE_RADAR_DISPLAY_STATUS_PROPERTY					= 5068;//雷达显示开关
	
	public static final int VIM_VEHICLE_REAR_RADAR_DETECT_DISTANCE_PROPERTY		= 5069;//倒车后雷达探测距离设置
	
	public static final int VIM_VEHICLE_FRONT_RADAR_DETECT_DISTANCE_PROPERTY	= 5070;//前雷达探测距离设置
	
	public static final int VIM_VEHICLE_RADAR_SWITCH_PROPERTY									= 5071;//雷达开关
	
	public static final int VIM_VEHICLE_RADAR_TONE_VOLUME_PROPERTY						= 5072;//雷达提示音音量
	
	public static final int VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY		= 5073;//前雷达开机状态
	
	public static final int VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY			= 5074;//后雷达开机状态
	
	public static final int VIM_VEHICLE_LEFT_RADAR_POWER_STATUS_PROPERTY			= 5075;//左雷达开机状态
	
	public static final int VIM_VEHICLE_RIGHT_RADAR_POWER_STATUS_PROPERTY			= 5076;//右雷达开机状态
	
	public static final int VIM_VEHICLE_PARKING_MODE_ALL_KINDS_PROPERTY				= 5098;//驻车模式的所有类别
	
	public static final int VIM_RIGHT_TURN_CAMERA_PROPERTY										= 5099;//右转向摄像头状态
	
	
	public static final int VIM_HISTORY_FUEL_CONSUMPTION_PROPERTY							= 5100;//历史油耗{当前行程油耗,行程1油耗,行程2油耗,行程3油耗,行程4油耗,行程5油耗}
	
	public static final int VIM_HISTORY_FUEL_CONSUMPTION_UNIT_PROPERTY				= 5101;//历史油耗单位
	
	public static final int VIM_AVG_FUEL_CONSUMPTION_PROPERTY								= 5102;//平均油耗
	
	public static final int VIM_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY						= 5103;//平均油耗单位
	
	public static final int VIM_INSTANT_FUEL_CONSUMPTION_UNIT_PROPERTY				= 5104;//瞬时油耗单位
	
	public static final int VIM_ODOMETER_UNIT_PROPERTY												= 5105;//里程单位
	
	public static final int VIM_AVG_VECHILE_SPEED_UNIT_PROPERTY								= 5106;//平均车速单位
	
	public static final int VIM_FUEL_CONSUMPTION_UNIT_15_MINUES_PROPERTY			= 5107;//过去十五分钟每分钟油耗单位
	
	public static final int VIM_FUEL_CONSUMPTION_15_MINUES_PROPERTY						= 5108;//过去十五分钟的每分钟油耗
	
	public static final int VIM_COMMON_FUEL_CONSUMPTION_UNIT_PROPERTY				= 5109;//通用油耗单位
	
	public static final int VIM_SPEED_DISTANCE_UNIT_PROPERTY									= 5110;//速度距离单位
	
	public static final int VIM_TACHOMETER_DISPLAY_PROPERTY									 	= 5111;//转速计显示
	
	public static final int VIM_TACHOMETER_SETTING_PROPERTY									= 5112;//转速表设置
	
	public static final int VIM_COMPASS_REGION_PROPERTY											= 5113;//指南针区域
	
	public static final int VIM_CURRENT_AVG_FUEL_CONSUMPTION_PROPERTY 				= 5114;//当前平均油耗
	
	public static final int VIM_HISTORY_AVG_FUEL_CONSUMPTION_PROPERTY 				= 5115;//历史平均油耗
	
	public static final int VIM_CURRENT_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY 		= 5116;//当前平均油耗单位
	
	public static final int VIM_HISTORY_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY 		= 5117;//历史平均油耗单位
	
	public static final int VIM_TRIP_A_ODOMETER_UNIT_PROPERTY 								= 5118;//TRIPA单位
	
	public static final int VIM_FUEL_CONSUMPTION_RANGE_PROPERTY 							= 5119;//油耗量程
	
	public static final int VIM_CACHE_RECORD_TRIPA_ODOMETER_UNIT_PROPERTY 		= 5120;//TRIPA记录中的里程单位
	
	public static final int VIM_CACHE_RECORD_AVG_FUEL_CONSUMPTION_UNIT_PROPERTY = 5121;//TRIPA记录中的平均油耗单位

	public static final int VIM_CACHE_RECORD_FUEL_CONSUMPTION_RANGE_PROPERTY 		= 5122;//TRIPA记录中的油耗量程
	
	public static final int VIM_TRIP_RECORD_IN_CACHE_PROPERTY 								= 5123;//TRIPA记录{第1条TRIPA记录,第1条平均油耗,第2条TRIPA记录,第2条平均油耗,第3条TRIPA记录,第3条平均油耗}
	
	public static final int VIM_RMNG_DRVNG_RANGE_UNIT_PROPERTY								= 5124;//剩余里程单位
	
	public static final int VIM_COMPASS_CALIBRATE_STATUS_PROPERTY							= 5125;//指南针校准
	
	public static final int VIM_COMPASS_ANGLE_PROPERTY												= 5126;//指南针角度{正北为0,正东90,正南为180,正西270}
	
	public static final int VIM_VEHICLE_SPEED_UNIT_PROPERTY										= 5127;//车速单位
	
	public static final int VIM_DISTANCE_SINCE_START_PROPERTY									= 5128;//自启动后行驶里程{单位,里程}
	
	public static final int VIM_DISTANCE_SINCE_REFUELING_PROPERTY							= 5129;//自加油起行驶里程{单位,里程}
	
	public static final int VIM_DISTANCE_LONG_TERM_PROPERTY										= 5130;//长时间行驶里程{单位,里程}
	
	public static final int VIM_AVG_CONSUMPTION_SINCE_START_PROPERTY					= 5131;//自启动以后平均油耗{单位,油耗}
	
	public static final int VIM_AVG_CONSUMPTION_SINCE_REFUELING_PROPERTY			= 5132;//自加油起平均油耗{单位,油耗}
	
	public static final int VIM_AVG_CONSUMPTION_LONG_TERM_PROPERTY						= 5133;//长时间平均油耗{单位,油耗}
	
	public static final int VIM_AVG_SPEED_SINCE_START_PROPERTY								= 5134;//自启动后平均车速{单位,车速}
	
	public static final int VIM_AVG_SPEED_SINCE_REFUELING_PROPERTY							= 5135;//自加油起平均车速{单位,车速}

	public static final int VIM_AVG_SPEED_LONG_TERM_PROPERTY									= 5136;//长时间平均车速{单位,车速}
	
	public static final int VIM_TRAVELLING_TIME_SINCE_START_PROPERTY						= 5137;//自启动后行驶时间,单位为分钟
	
	public static final int VIM_TRAVELLING_TIME_SINCE_REFUELING_PROPERTY				= 5138;//自加油起行驶时间,单位为分钟

	public static final int VIM_TRAVELLING_TIME_LONG_TERM_PROPERTY						= 5139;//长时间行驶时间,单位为分钟
	
	public static final int VIM_CONV_CONSUMERS_VALUE_PROPERTY								= 5140;//舒适性用电器单位{单位,值}
	
	public static final int VIM_VEHICLE_SPEED_WARNING_STATUS_PROPERTY					= 5141;//车速警告开关状态
	
	public static final int VIM_VEHICLE_SPEED_WARNING_VALUE_PROPERTY					= 5142;//车速警告阈值
	
	public static final int VIM_VEHICLE_DESTINATION_ODOMETER_PROPERTY					= 5143;//目的地里程
	
	public static final int VIM_COMPASS_OFFSET_VALUE_PROPERTY									= 5144;//指南针偏移值(吉普指南者2017范围0-15)
	
	
	public static final int VIM_DOOR_OPEN_STATUS_REAR_PROPERTY								= 5201;//后门状态
	
	public static final int VIM_DOOR_REAR_DIRECTION_PROPERTY									= 5202;//电动后门运行方向
	
	public static final int VIM_ELECT_REAR_DOOR_ALARM_PROPERTY								= 5203;//电动尾门报警
	
	public static final int VIM_VEHICLE_DOOR_KEY_STATUS_PROPERTY							= 5280;//车钥匙状态
	
	public static final int VIM_VEHICLE_AMPLIFIER_TYPE_PROPERTY 								= 5300;	//原车功放类型
	
	public static final int VIM_BALANCE_LVL_PROPERTY 													= 5301;	//原车功放左右均衡
	
	public static final int VIM_ALTO_LVL_PROPERTY 														= 5302;//原车攻放中音
	
	public static final int VIM_SYNC_SPEED_VOLUME_STATUS_PROPERTY 						= 5303;//车速联动音量
	
	public static final int VIM_SYNC_SPEED_VOLUME_PROPERTY 										= 5304;//车速联动音量
	
	public static final int VIM_BOSE_CENTERPOINT_STATUS_PROPERTY							= 5305;//环绕声音状态
	
	public static final int VIM_SURROUND_VOLUME_PROPERTY											= 5306;//环绕音量
	
	public static final int VIM_DRIVER_SEAT_AUDIO_STATUS_PROPERTY							= 5307;//驾驶座音场开关
	
	public static final int VIM_VEHICLE_AMPLIFIER_DEVICE_NODE_PROPERTY					= 5308;//原车功放节点
	
	public static final int VIM_VEHICLE_AMPLIFIER_POWER_STATUS_PROPERTY				= 5309;//功放开关机状态
	
	public static final int VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE									= 5310;//控制类型
	
	public static final int VIM_VEHICLE_AMPLIFIER_VOLUME_MIN										= 5311;//功放音量最小值
	
	public static final int VIM_VEHICLE_AMPLIFIER_VOLUME_MAX										= 5312;//功放音量最大值
	
	public static final int VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MIN							= 5313;//音效调节最小值(高中低音范围)
	
	public static final int VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MAX						= 5314;//音效调节最大值(高中低音范围)
	
	public static final int VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MIN							= 5315;//均衡调节最小值(前后左右均衡调节范围)
	
	public static final int VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MAX						= 5316;//均衡调节最大值(前后左右均衡调节范围)
	
	public static final int VIM_VEHICLE_AMPLIFIER_SURROUND_VOLUME_MIN					= 5317;//环绕音最小值
	
	public static final int VIM_VEHICLE_AMPLIFIER_SURROUND_VOLUME_MAX					= 5318;//环绕音最大值
	
	public static final int VIM_SYNC_SPEED_VOLUME_MIN_PROPERTY 								= 5319;//车速联动音量最小值
	
	public static final int VIM_SYNC_SPEED_VOLUME_MAX_PROPERTY								= 5320;//车速联动音量最大值
	
	public static final int VIM_VEHICLE_AUDIO_EFFECTS_MODE_PROPERTY						= 5321;//原车音效模式
	
	public static final int VIM_VEHICLE_REAR_DEVICE_NODE_PROPERTY							= 5399;//原车后座节点
	
	
	public static final int VIM_NAVI_INFO_DISPLAY_ENABLE_PROPERTY							= 5400;//导航信息显示开关
	
	public static final int VIM_NAVI_INFO_DISPLAY_TYPE_PROPERTY								= 5401;//导航信息显示类型(0 路向;1,指南针)
	
	public static final int VIM_NAVI_INFO_FLAG_POINT_UNIT_PROPERTY							= 5401;//信息点距离单位/进度条显示开关
	
	public static final int VIM_NAVI_INFO_DESTION_DIS_UNIT_PROPERTY						= 5402;//目的地距离单位
	
	public static final int VIM_NAVI_INFO_COMPASS_DIR_PROPERTY								= 5403;//指南针方向
	
	public static final int VIM_NAVI_INFO_FLAG_POINT_DIS_PROPERTY							= 5404;//信息点距离或者进度
	
	public static final int VIM_NAVI_INFO_DESTION_DIS_PROPERTY									= 5405;//目的地距离
	
	public static final int VIM_NAVI_INFO_ROAD_DIR_TYPE_PROPERTY							= 5406;//路向信息(数据格式{type,value})
	
	public static final int VIM_NAVI_INFO_COLLECT_PROPERTY										= 5407;//导航信息集(map字符串)
	
	public static final int VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY							= 5480;//同步到原车媒体信息
	
	public static final int VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY						= 5481;//同步到原车蓝牙电话信息
	
	public static final int VIM_AUDIO_CONTROL_MUTE_PROPERTY									= 5490;//音量控制-静音
	
	public static final int VIM_AUDIO_CONTROL_VOLUME_PROPERTY								= 5491;//音量控制-音量大小
	
	public static final int VIM_ALARM_VOLUME_PROPERTY 												= 5492;//警告音量
	
	public static final int VIM_ACC_FRONT_DETECT_ALARM_TONE_PROPERTY  				= 5493;//ACC前车探知警告音
	
	public static final int VIM_PAUSE_LKAS_TONE_PROPERTY 										 	= 5494;//暂停LKAS提示音
	
	public static final int VIM_SPEECH_ALARM_SYS_VOLUME_PROPERTY  							= 5495;//语音报警系统音量
	

	
	public static final int VIM_AVM_AUTO_PARK_UI_MODE_PROPERTY								= 5500;//自动泊车设置UI显示
	
	public static final int VIM_AVM_VERTICAL_PARK_UI_COLOR_PROPERTY						= 5501;//垂直车位调节UI颜色
	
	public static final int VIM_AVM_AUTO_PARK_HINT_PROPERTY									= 5502;//自动泊车提示语
	
	public static final int VIM_AVM_CAMERA_UI_FALG_PROPERTY										= 5503;//前后摄像头指示UI
	
	public static final int VIM_AVM_CAMERA_STATUS_PROPERTY										= 5504;//全景倒车预览状态
	
	public static final int VIM_AVM_AUTO_PARK_TOUCH_CMD_PROPERTY							= 5505;//自动泊车UI触控命令
	
	public static final int VIM_STATUS_AUTO_PARK_PROPERTY										= 5506;//自动泊车状态
		
	public static final int VIM_CAMERA_STARTUP_BY_LEFT_TURN_LIGHT_PROPERTY		= 5516;//摄像头设置-左转向灯触发
	
	public static final int VIM_CAMERA_STARTUP_BY_RIGHT_TURN_LIGHT_PROPERTY		= 5517;//摄像头设置-右转向灯触发
	
	public static final int VIM_CAMERA_STARTUP_BY_STEER_WHELL_PROPERTY				= 5518;//摄像头设置-方向盘触发
	
	public static final int VIM_CAMERA_STARTUP_BY_FIRST_FGEAR_PROPERTY				= 5519;//摄像头设置-首次前进挡自动启动
	
	public static final int VIM_CAMERA_SMART_ENTER_PROPERTY									= 5520;//摄像头设置-智能进入开关
	
	public static final int VIM_CAMERA_STARTUP_BY_FGEAR_PROPERTY							= 5521;//摄像头设置-前进挡触发
	
	public static final int VIM_AVM_STARTUP_ANIMATE_PROPERTY									= 5522;//摄像头设置-AVM开机动画开关
	
	public static final int VIM_CAMERA_CONFIG_RESET_PROPERTY									= 5523;//摄像头设置-恢复默认开关
	
	public static final int VIM_CAMERA_PATH_TYPE_PROPERTY										= 5524;//摄像头轨迹类型
	
	public static final int VIM_CAMERA_PANORAMIC_SWITCH_PROPERTY							= 5525;//全景切入
	
	public static final int VIM_CAMERA_PANORAMIC_PATH_VIEW_MODE_PROPERTY			= 5526;//全景轨迹视图
	
	public static final int VIM_REVERSE_VIDEO_MODE_PROPERTY 									= 5527;//倒车视频模式设置
	
	public static final int VIM_REVERSE_DYNAMIC_SETTING_PROPERTY 							= 5528;//倒车动态设置
	
	public static final int VIM_CAMERA_PANORAMIC_DEVICE_NODE_PROPERTY				= 5529;//原车全景摄像节点
	
	public static final int VIM_REVERSE_CAMERA_BRIGHTNESS_PROPERTY 						= 5530;//倒车视频亮度调节
	
	public static final int VIM_REVERSE_CAMERA_CONTRAST_PROPERTY 							= 5531;//倒车视频对比度调节
	
	public static final int VIM_REVERSE_CAMERA_SATURATION_PROPERTY 						= 5532;//倒车视频饱和度调节
	
	public static final int VIM_PARKING_AND_MANOEUVRING_AUTO_ACTIVE_PROPERTY	= 5533;//驻车和调车自动激活
	
	public static final int VIM_PARKING_AND_MANOEUVRING_ACTIVE_STATUS_PROPERTY= 5534;//驻车和调车激活状态

	public static final int VIM_REVERSE_STATIC_PATH_STATUS_PROPERTY						= 5535;//静态轨迹线状态
	
	public static final int VIM_REVERSE_DYNAMIC_PATH_STATUS_PROPERTY					= 5536;//动态轨迹线状态
	
	public static final int VIM_FISHEYE_CALIBRATION_PROPERTY										= 5537;//鱼眼校正
	
	public static final int VIM_REVERSE_EXIT_DELAY_TIME_PROPERTY								= 5538;//R档退出延迟

	public static final int VIM_REVERSE_VIDEO_SIGNAL_PROPERTY									= 5539;//单路倒车影像

	public static final int VIM_ASSIST_ACTION_FOLLOW_TURN_PROPERTY						= 5540;//辅助随动
	
	public static final int VIM_REVERSE_CAMERA_TOUCH_POINT_PROPERTY 					= 5590;//倒车视频界面触摸坐标位置{x,y,action},0弹起,1按下


	public static final int VIM_ADAS_LANE_DEPARTURE_DETECTION_PROPERTY				= 5600;//ADAS-车道偏移检测开关
	
	public static final int VIM_ADAS_BLIND_SPOT_DETECTION_PROPERTY						= 5601;//ADAS-盲点检测开关
	
	public static final int VIM_ADAS_MOVING_OBJECT_DETECTION_PROPERTY					= 5602;//ADAS-移动对象检测开关
	
	public static final int VIM_ADAS_LANE_DEPARTURE_SETING_PROPERTY 						= 5603;//车道偏离辅修系统设定
	
	public static final int VIM_ADAS_LANE_ASSIST_PROPERTY											= 5604;//自适应车道导向
	
	public static final int VIM_ADAS_DRIVER_ALERT_SYSTEM_PROPERTY							= 5605;//驾驶员安全警告系统(驾驶疲劳检测系统)
	
	public static final int VIM_ADAS_LAST_DISTANCE_SELECTED 										= 5606;//上次选择的车距
	
	public static final int VIM_ADAS_FRONT_ASSIST_ACTIVE_PROPERTY 							= 5607;//前部辅助系统激活状态
	
	public static final int VIM_ADAS_FRONT_ASSIST_ADVANCE_WARNING_PROPERTY 		= 5608;//前部辅助系统预警
	
	public static final int VIM_ADAS_FRONT_ASSIST_DISPLAY_DISTANCE_WARNING_PROPERTY = 5609;//前部辅助系统显示距离报警
	
	public static final int VIM_ADAS_LANE_KEEP_ASSIST_ACTIVE_PROPERTY 					= 5610;//车道保持辅助系统激活
	
	public static final int VIM_ADAS_AUTO_CRUISE_DRIVE_PROGRAM_PROPERTY 			= 5611;//自适应巡航行驶程序
	
	public static final int VIM_ADAS_AUTO_CRUISE_DRIVE_DISTANCE_PROPERTY 			= 5612;//自适应巡航车距
	
	public static final int VIM_ADAS_FLANK_BLIND_WARNING_PROPERTY 						= 5613;//侧翼盲区报警
	
	public static final int VIM_ADAS_BLIND_DETECTION_PROPERTY									= 5614;//盲区探测
	
	public static final int VIM_ADAS_BLIND_ALARM_TYPE_PROPERTY								= 5615;//盲区警告类型

	public static final int VIM_ADAS_LANE_DEPARTURE_ALARM_DELAY_PROPERTY 			= 5616;//车道偏离警告延迟时间

	
	
	public static final int VIM_RAMP_STARTUP_ASSIST_PROPERTY 									= 5650;//坡道启动辅助
	
	public static final int VIM_AUTO_PREVENT_BUMP_PROPERTY										= 5651;//自动防撞准备
	
	public static final int VIM_VEHICLE_STATUS_NOTIFICATION_PROPERTY						= 5652;//汽车状态通知
	
	public static final int VIM_RESET_DRIVE_COMPUTER_INFO_PROPERTY						= 5653;//清除行车电脑信息
	
	
	public static final int VIM_VEHICLE_TPMS_DEVICE_EXIST_PROPERTY							= 5700;//TPMS设备是否存在
	
	public static final int VIM_VEHICLE_TPMS_UI_STATUS_PROPERTY								= 5701;//TPMS显示界面状态(正常/报警状态)
	
	public static final int VIM_VEHICLE_SHOW_BACK_TIRE_PRESSURE_PROPERTY			= 5702;//是否显示备胎
	
	public static final int VIM_VEHICLE_TPMS_DISPLAY_MODE_PROPERTY   						= 5703;//轮胎显示方式
	
	public static final int VIM_TIRE_PRESSURE_UNIT_PROPERTY 										= 5704;//胎压单位
	
	public static final int VIM_TIRE_PRESSURE_BACK_PROPERTY 										= 5705;//备胎胎压
	
	public static final int VIM_TIRE_PRESSURE_ALERT_INFO_PROPERTY							= 5706;//胎压异常信息{前左,前右,后左,后右,备胎(可能没有)}
	
	
	public static final int VIM_PARKSENSE_RADAR_PARKING_PROPERTY							= 5750;//Parksense雷达泊车
	
	public static final int VIM_PARKSENSE_FRONT_VOLUME_PROPERTY							= 5751;//前Parksense音量

	public static final int VIM_PARKSENSE_REAR_VOLUME_PROPERTY								= 5752;//后Parksense音量

	public static final int VIM_PARKVIEW_DYNAMIC_PATH_PROPERTY								= 5753;//parkview影像泊车动态引导线

	public static final int VIM_PARKVIEW_STATIC_PATH_PROPERTY									= 5754;//parkview影像泊车静态引导线

	public static final int VIM_PARKVIEW_REVERSE_VIDEO_DELAY_PROPERTY					= 5755;//parkview影像泊车延时
	
	public static final int VIM_AUTO_ASSIST_REAR_RADAR_PARKING_PROPERTY				= 5756;//后雷达泊车自动辅助
	
	public static final int VIM_ENABLE_AUTO_BRAKE_SERVICE_PROPERTY						= 5757;//允许自动制动服务
	
	public static final int VIM_ENABLE_AUTO_PARKING_BRAKE_PROPERTY						= 5758;//自动泊车制动
	
	public static final int VIM_ENABLE_AUTO_STOP_BRAKE_PROPERTY								= 5759;//自动驻车制动
	
	public static final int VIM_ENABLE_AUTO_BRAKE_FRONT_COLLISION_PROPERTY		= 5760;//前方碰撞警报自动制动
	
	public static final int VIM_SERVICE_MODE_PROPERTY													= 5761;//服务模式
	
	public static final int VIM_FRONT_COLLISION_ALARM_PROPERTY								= 5762;//前方碰撞警报

	public static final int VIM_DOOR_OPEN_ALARM_PROPERTY											= 5763;//车门报警

	public static final int VIM_VEHICLE_BEEP_PROPERTY													= 5764;//汽车蜂鸣器
	
	public static final int VIM_SUSPENSION_AUTO_ADJUST_PROPERTY								= 5765;//悬架自动调整

	public static final int VIM_SUSPENSION_INFO_DISPLAY_PROPERTY								= 5766;//显示悬架信息

	public static final int VIM_VEHICLE_TIRE_JACK_PROPERTY											= 5767;//轮胎千斤顶

	public static final int VIM_TRANSPORT_MODE_PROPERTY											= 5768;//运输模式

	public static final int VIM_TIRE_CALIBRATE_MODE_PROPERTY									= 5769;//车轮校准模式
				
	
	public static final int VIM_DAYTIME_RUNNING_LIGHT_PROPERTY								= 5800;//日间行车灯开关
	
	public static final int VIM_HEADLAMPS_ON_SENSITIVITY_PROPERTY							= 5801;//自动头灯灵敏度
	
	public static final int VIM_INTERNEL_LIGHT_AUTO_OFF_TIME_PROPERTY					= 5802;//车内灯关闭时间
	
	public static final int VIM_EXTERNEL_LIGHT_AUTO_OFF_TIMER_PROPERTY				= 5803;//车外灯自动关闭时间

	public static final int VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY				= 5804;//头灯自动关闭时间
	
	public static final int VIM_FUEL_EFFICIENTY_BACKLIGHT_PROPERTY 							= 5805;//节能模式的自动照明
	
	public static final int VIM_AUTO_INTERIOR_ILL_SENSITIVITY_PROPERTY 					= 5806;//自动车内照明灵敏度
	
	public static final int VIM_AUTO_HEAD_LIGHT_ON_WITH_WIPER_ON_PROPERTY 		= 5807;//雨刷和自动大灯联动个性化设定
	
	public static final int VIM_AUTO_LIGHT_SENSITIVITY_PROPERTY								= 5808;//自动点灯灵敏度								
	
	public static final int VIM_VEHICLE_LIGHT_AUTO_SWITCH_ON_TIME_PROPERTY		= 5809;//自动接通时间
	
	public static final int VIM_LANE_CHANGE_TRUN_LIGHT_PROPERTY								= 5810;//变道转向灯
	
	public static final int VIM_AUTO_HEADLIGHT_IN_RAIN_PROPERTY 								= 5811;//自动行车灯(雨天)
	
	public static final int VIM_METER_LIGHT_BRIGHTNESS_PROPERTY								= 5812;//仪表开关照明亮度
	
	public static final int VIM_DOOR_AMBIENT_LIGHT_BRIGHTNESS_PROPERTY				= 5813;//车内环境照明灯

	public static final int VIM_FOOTWELL_LIGHT_BRIGHTNESS_PROPERTY						= 5814;//脚部空间环境照明灯

	public static final int VIM_ROOF_LIGHT_BRIGHTNESS_PROPERTY								= 5815;//车顶环境环境照明灯

	public static final int VIM_FRONT_LIGHT_BRIGHTNESS_PROPERTY								= 5816;//前部环境环境照明灯

	public static final int VIM_ALL_LIGHT_BRIGHTNESS_PROPERTY									= 5817;//所有区域照明灯

	public static final int VIM_DYNAMIC_LIGHT_ASSIST_PROPERTY									= 5818;//动态灯光辅助

	public static final int VIM_MOTORWAY_LIGHT_PROPERTY											= 5819;//动态大灯随动

	public static final int VIM_AMBIENT_LIGHT_COLOR_PROPERTY									= 5820;//环境灯颜色

	public static final int VIM_TRAVEL_MODEL_PROPERTY 												= 5821;//旅行模式
	
	public static final int VIM_BACK_HOME_MODE_PROPERTY 											= 5822;//回家模式
	
	public static final int VIM_LEAVE_HOME_MODE_PROPERTY 											= 5823;//离家模式

	public static final int VIM_FIND_VEHICLE_LIGHT_PROPERTY 										= 5824;//寻车灯功能
	
	public static final int VIM_WELCOME_LIGHT_DELAY_TIME_PROPERTY							= 5825;//迎宾照明
	
	public static final int VIM_AMBIENT_LIGHT_PROPERTY												= 5826;//氛围照明
	
	public static final int VIM_WELCOME_LIGHT_STATUS_PROPERTY								= 5827;//迎宾功能
	
	public static final int VIM_LOCK_ACTIVE_TRUN_LIGHT_PROPERTY								= 5828;//锁车转向灯闪烁

	public static final int VIM_CORNER_ASSIST_LIGHT_PROPERTY											= 5829;//转角辅助灯
	
	
	public static final int VIM_AUTO_LOCK_BY_SPEED_PROPERTY										= 5900;//车门感应车速自动锁定

	public static final int VIM_AUTO_LOCK_BY_SHIFT_FROM_P_PROPERTY						= 5901;//换挡联动车门自动锁定
	
	public static final int VIM_AUTO_LOCK_BY_SHIFT_TO_P_PROPERTY							= 5902;//P档联动解锁
	
	public static final int VIM_REMOTE_DOUBLE_PRESS_LOCK_PROPERTY						= 5903;//操作旋钮两次解锁
	
	public static final int VIM_LOCK_UNLOCK_FEEDBACK_TONE_LOCKPROPERTY				= 5904;//开锁/解锁提示音反馈
	
	public static final int VIM_DOUBLE_PRESS_KEY_LOCK_PROPERTY								= 5905;//操作钥匙两次解锁
	
	public static final int VIM_DRIVER_DOOR_SYNC_UNLOCK_PROPERTY							= 5906;//驾驶席开门联动解锁
	
	public static final int VIM_SMART_DOOR_UNLOCK_PROPERTY										= 5907;//智能车门解锁
	
	public static final int VIM_SMART_LOCK_AND_ONE_KEY_STARTUP_PROPERTY			= 5908;//智能车锁和一键启动
	
	public static final int VIM_LOCK_UNLOCK_FLASH_ALERT_LIGHTS_PROPERTY				= 5909;//上锁开锁闪紧急响应灯
	
	public static final int VIM_AUTO_REAR_DOOR_OPEN_LEVEL_PROPERTY  					= 5910;//电动后门开度调节
	
	public static final int VIM_AUTO_RELOCK_TIMER_PROPERTY  									= 5911;//自动重新锁定时间
	
	public static final int VIM_AUTO_DOOR_LOCK_WITH_PROPERTY									= 5912;//车门自动上锁联动
	
	public static final int VIM_AUTO_DOOR_UNLOCK_WITH_PROPERTY								= 5913;//车门自动开锁联动
	
	public static final int VIM_KEYLESS_LOCK_ANSWER_BACK_PROPERTY 						= 5914;//遥控落锁提示
	
	public static final int VIM_KEYLESS_ACCESS_BEEP_PROPERTY 									= 5915;//遥控门锁蜂鸣声
	
	public static final int VIM_WALK_AWAY_AUTO_LOCK_PROPERTY									= 5916;//离开锁止个性设定
	
	public static final int VIM_DOOR_UNLOCK_MODE_PROPERTY 										= 5917;//车门解锁模式
	
	public static final int VIM_KEY_AND_REMOTE_UNLOCK_MODE_PROPERTY					= 5918;//远程解锁模式
	
	public static final int VIM_PREVENT_AUTO_LOCK_DOOR_OPEN_PROPERTY					= 5919;//防止开门时自动落锁

	public static final int VIM_DELAY_LOCK_PROPERTY														= 5920;//延时落锁
	
	public static final int VIM_UNLOCK_LIGHT_PROPERTY													= 5921;//解锁灯光
	
	public static final int VIM_REMOTE_LOCK_FEEDBACK_PROPERTY									= 5922;//遥控锁门反馈

	public static final int VIM_REMOTE_LOCK_AGAIN_PROPERTY										= 5923;//遥控再锁门
	
	public static final int VIM_VEHICLE_REMOTE_STARTUP_PROPERTY								= 5924;//遥控启动车辆
	
	public static final int VIM_LOCK_DOOR_MODE_NEAR_PROPERTY									= 5925;//近车解锁设置
	
	public static final int VIM_KEY_FORGOTTEN_PROMPT_PROPERTY								= 5926;//钥匙遗忘提醒
	
	public static final int VIM_PERSONAL_DRIVE_MODE_PROPERTY									= 5927;//个性化驾驶

	public static final int VIM_AUTO_RELOCK_DOORS_PROPERTY										= 5928;//重新锁门
	
	public static final int VIM_REMOTE_CONTROL_WINDOW_PROPERTY							= 5929;//遥控车窗控制
	
	public static final int VIM_UNLOCK_TRUNK_ONLY_PROPERTY										= 5930;//仅解锁后备箱
	
	public static final int VIM_CLOSE_WINDOW_WITH_LOCK_PROPERTY							= 5931;//闭锁车门自动关窗
	
	public static final int VIM_POSITION_LIGHT_OFF_WITH_LOCK_PROPERTY					= 5932;//闭锁车门自动熄灭位置灯状态

	public static final int VIM_UNLOCK_WITH_ACC_OFF_PROPERTY									= 5933;//熄火自动解锁
	
	public static final int VIM_KEY_UNLOCK_FOR_FIRST_PROPERTY									= 5934;//首次按车钥匙解锁
	
	public static final int VIM_NO_KEY_ENTERY_PROPERTY												 = 5935;//无钥匙进入
	
	
	public static final int VIM_AIR_CONDITION_SYNC_WITH_AUTO_PROPERTY  				= 6000;//空调与AUTO键联动
	
	public static final int VIM_VENTILATE_SYNC_WITH_AUTO_PROPERTY  						= 6001;//内外换气与AUTO键联动
	
	public static final int VIM_AIR_CONDITION_MODE_SETTING_PROPERTY 					= 6002;//空调模式设置
	
	public static final int VIM_AIR_CONDITION_FAN_SPEED_PROPERTY 							= 6003;//空调风速
	
	public static final int VIM_AIR_CONDITION_FAN_SPEED_AUTO_MODE_PROPERTY		= 6004;//空调自动模式风量

	public static final int VIM_AIR_CONDITION_TEMP_REGION_PROPERTY						= 6005;//空调分区温度
	
	public static final int VIM_AIR_CONDITION_REAR_AUTO_DEFOGGING_PROPERTY		= 6006;//后窗自动除雾
	
	public static final int VIM_AIR_CONDITION_FRONT_AUTO_DEFOGGING_PROPERTY		= 6007;//前窗自动除雾
	
	public static final int VIM_AIR_CONDITION_SEAT_AUTO_HEATING_PROPERTY			= 6008;//遥控启动座椅自动加热

	public static final int VIM_AIR_CONDITION_STARTUP_MODE_PROPERTY						= 6009;//空调启动模式
	
	public static final int VIM_AIR_CONDITION_AIR_QUALITY_SENSOR_PROPERTY			= 6010;//空气质量传感器设置
	
	public static final int VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_DOWN_PROPERTY= 6011;//空调控制命令触摸按下
	
	public static final int VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_UP_PROPERTY	= 6012;//空调控制命令触摸释放
		
	public static final int VIM_AIR_CONDITION_CONTROL_CMD_PROPERTY						= 6013;//空调控制命令(区别于上面两个属性需要触摸状态)
	
	public static final int VIM_DRIVER_SEAT_AUTO_HEATING_STARTUP_PROPERTY			= 6014;//车辆启动后自动驾驶位座椅加热
	
	
	public static final int VIM_AC_COMPRESSOR_STATUS_IN_AUTO_PROPERTY				= 6050;//Auto时压缩机状态
	
	public static final int VIM_AC_CYCLE_WAY_IN_AUTO_PROPERTY									= 6051;//Auto时内外循环控制方式

	public static final int VIM_AC_COMFORT_CURVE_PROPERTY										= 6052;//空调舒适曲线设置
	
	public static final int VIM_AC_ANION_MODE_PROPERTY												= 6053;//负离子模式
	
	public static final int VIM_DRIVER_SEAT_AUTO_HEATING_PROPERTY							= 6054;//驾驶座自动加热
	
	public static final int VIM_PSNGR_SEAT_AUTO_HEATING_PROPERTY							= 6055;//副驾驶座自动加热

	public static final int VIM_KEY_AUTO_IDENTIFY_SEAT_PROPERTY								= 6056;//钥匙自动识别座椅位置

	public static final int VIM_DRIVER_SEAT_AUTO_ADJUST_PROPERTY							= 6057;//座椅自动调整便利进出
	
	public static final int VIM_OIL_ELECT_MIX_SUPPORT_PROPERTY								= 6100;//油电混合车型
	
	public static final int VIM_BATTERY_POWER_LEVEL_PROPERTY									= 6101;//电池电量
	
	public static final int VIM_OIL_ELECT_MIX_DRIVE_MODE_PROPERTY							= 6102;//驱动方式,{是否马达驱动电池,是否马达驱动车轮,是否发动机驱动马达,是否发动机驱动车轮,是否电池驱动马达,是否车轮驱动马达}
	
	public static final int VIM_REMOTE_POWER_ON_TIME_PROPERTY								= 6180;//远程上电时间
	
	public static final int VIM_REMOTE_STARTUP_TIME_PROPERTY									= 6181;//远程启动时间

	public static final int VIM_STEERING_MODE_PROPERTY												= 6182;//转向模式
	
	public static final int VIM_COUSTOM_LEFT_SCREEN_DISPLAY_MODE_PROPERTY			= 6183;//自定义仪表盘左边显示模式						
	
	public static final int VIM_COUSTOM_RIGHT_SCREEN_DISPLAY_MODE_PROPERTY		= 6184;//自定义仪表盘左边显示模式						

	public static final int VIM_REMOTE_STARTUP_TONE_PROPERTY											= 6185;//远程启动提示音
	
	
	public static final int VIM_OUTSIDE_TEMP_ADJUST_PROPERTY									= 6200;//外部气温调节
	
	public static final int VIM_TRIP_A_RESET_TIMING_PROPERTY									= 6201;//里程A重设条件切换
	
	public static final int VIM_TRIP_B_RESET_TIMING_PROPERTY									= 6202;//里程B重设条件切换
	
	public static final int VIM_REMOTE_START_SYSTEM_ON_OFF_PROPERTY 					= 6203;//遥控启动系统开启与关闭

	public static final int VIM_ENGINE_AUTO_SYSTEM_ON_OFF_DISPLAY_PROPERTY 		= 6204;//发动机节能自动启停显示
	
	public static final int VIM_FRONT_ALARM_DISTANCE_PROPERTY  								= 6205;//前方危险警告距离
	
	public static final int VIM_VEHICLE_DRVNG_RANGE_ICON_PROPERTY 						= 6206;//原车可续航距离仪表图标
	
	public static final int VIM_VEHICLE_AVG_SPEED__ICON_PROPERTY 							= 6207;//原车平均车速时间仪表图标
	
	public static final int VIM_VEHICLE_SHOW_ICON_PROPERTY 										= 6208;//原车显示关闭仪表图标
	
	public static final int VIM_VEHICLE_TURBO_ICON_PROPERTY 									= 6209;//原车涡轮仪表仪表图标
	
	public static final int VIM_VEHICLE_AMPLIFIER_ICON_PROPERTY 								= 6210;//原车音响仪表图标
	
	public static final int VIM_VEHICLE_PHONE_ICON_PROPERTY 										= 6211;//原车电话图标
	
	public static final int VIM_START_STOP_FUNCTION_TIMER_PROPERTY						= 6212;//启停系统定时(单位为秒)
	
	public static final int VIM_AUTO_START_STOP_FUNCTION_PROPERTY						= 6213;//自动启停功能
	
	
	public static final int VIM_VEHICLE_FRONT_VOLUME_PROPERTY									= 6301;//前部音量
	
	public static final int VIM_VEHICLE_FRONT_TONE_PROPERTY										= 6302;//前部音调

	public static final int VIM_VEHICLE_REAR_VOLUME_PROPERTY									= 6303;//后部音量
	
	public static final int VIM_VEHICLE_REAR_TONE_PROPERTY										= 6304;//后部音调
	
	public static final int VIM_VEHICLE_WINDOW_CONV_OPEN_PROPERTY						= 6305;//车窗便捷开启
	
	public static final int VIM_AUTOMATIC_LOCKING_PROPERTY										= 6306;//自动锁止
	
	public static final int VIM_INDUCTION_TRUNK_PROPERTY											= 6307;//感应式行李箱盖
	
	public static final int VIM_REMOTE_KEY_MEMORY_MATCH_PROPERTY						= 6308;//遥控钥匙记忆匹配
	
	public static final int VIM_VEHICLE_KEY_ACTIVED_PROPERTY										= 6309;//汽车钥匙已激活

	public static final int VIM_MW_SYNC_ADJUSTMENT_PROPERTY									= 6350;//后视镜和雨刷同步调节
	
	public static final int VIM_MW_LOWER_WHILE_REVERSING_PROPERTY						= 6351;//倒挡时后视镜降低

	public static final int VIM_MW_AUTOMATIC_WIPING_IN_RAIN_PROPERTY					= 6352;//雨天自动刮水

	public static final int VIM_MW_REAR_WINDOW_WIPEING_IN_REVERSE_PROPERTY		= 6353;//倒车档时后窗玻璃刮水

	public static final int VIM_MW_FOLD_AWAY_AFTER_PARKING_PROPERTY					= 6354;//驻车时内折
	
	public static final int VIM_FRONT_WIPER_REPAIR_MODE_PROPERTY							= 6355;//前雨刮维护功能

	public static final int VIM_MW_ADJUST_BRIGHTNESS_PROPERTY								= 6356;//后视镜调光镜
	
	
	public static final int VIM_MFD_CURRENT_FUEL_CONSUMPTION_PROPERTY				= 6400;//多功能显示器-当前油耗
	
	public static final int VIM_MFD_AVG_FUEL_CONSUMPTION_PROPERTY						= 6401;//多功能显示器-当前油耗
	
	public static final int VIM_MFD_CONVENIENCE_CONSUMERS_PROPERTY						= 6402;//多功能显示器-舒适性用电器
	
	public static final int VIM_MFD_ECONOMY_RUNNING_PROMPT_PROPERTY					= 6403;//多功能显示器-经济运行提示
	
	public static final int VIM_MFD_DRIVE_TIME_PROPERTY												= 6404;//多功能显示器-行驶时间
	
	public static final int VIM_MFD_DRIVE_ODOMETER_PROPERTY									= 6405;//多功能显示器-行驶里程
	
	public static final int VIM_MFD_DIGIT_SPEED_DISPLAY_PROPERTY								= 6406;//多功能显示器-数字式车速显示
	
	public static final int VIM_MFD_AVG_SPEED_PROPERTY												= 6407;//多功能显示器-平均车速
	
	public static final int VIM_MFD_SPEED_ALERT_PROPERTY											= 6408;//多功能显示器-车速报警
	
	public static final int VIM_MFD_OIL_TEMP_PROPERTY													= 6409;//多功能显示器-机油温度
	
	public static final int VIM_COMMON_UNIT_DISTANCE_PROPERTY	 							= 6500;//距离单位
	
	public static final int VIM_COMMON_UNIT_SPEED_PROPERTY	 									= 6501;//速度单位
	
	public static final int VIM_COMMON_UNIT_TEMP_PROPERTY	 									= 6502;//温度单位
	
	public static final int VIM_COMMON_UNIT_VOLUME_PROPERTY	 								= 6503;//容积单位
	
	public static final int VIM_COMMON_UNIT_FUEL_CONSUMPTION_PROPERTY	 			= 6504;//油耗单位
	
	public static final int VIM_COMMON_UNIT_TPMS_PROPERTY	 									= 6505;//胎压单位
	
	
	public static final int VIM_CARE_INSPECTION_LEFT_DAYS_PROPERTY							= 6600;//车况检查天数{显示模式,天数}
		
	public static final int VIM_CARE_INSPECTION_LEFT_ODOMETER_PROPERTY				= 6601;//车况检查里程{单位,显示模式,里程数}

	public static final int VIM_CARE_REFUEL_OIL_DAYS_PROPERTY	 								= 6602;//更换机油保养天数{显示模式,天数}
	
	public static final int VIM_CARE_REFUEL_OIL_ODOMETER_PROPERTY	 						= 6603;//更换机油保养里程{单位,显示模式,里程数}	
	
	public static final int VIM_VEHICLE_STATUS_PROMPT_MESSAGE_PROPERTY				= 6604;//车辆状态提示信息,可变数组，最大为6
	
	public static final int VIM_CONV_CONSUMERS_PROMPT_MESSAGE_PROPERTY				= 6605;//舒适性用电器提示信息,可变数组，最大为3
	
	
	public static final int VIM_VEHICLE_MEDIA_DEVICE_TYPE_PROPERTY							= 6700;//原车多媒体设备类型
	
	public static final int VIM_VEHICLE_MEDIA_DEVICE_STATUS_PROPERTY						= 6701;//原车多媒体设备状态

	public static final int VIM_VEHICLE_MEDIA_CUR_PLAY_TIME_PROPERTY						= 6702;//原车多媒体当前播放时间
	
	public static final int VIM_VEHICLE_MEDIA_CUR_PLAY_TRACK_PROPERTY					= 6703;//原车多媒体当前播放时间

	public static final int VIM_VEHICLE_MEDIA_TOTAL_TRACK_PROPERTY						= 6704;//原车多媒体当前播放时间

	public static final int VIM_VEHICLE_MEDIA_FOLDER_NUM_PROPERTY							= 6705;//原车多媒体文件夹

	public static final int VIM_VEHICLE_MEDIA_PLAY_PROGRESS_PROPERTY					= 6706;//原车多媒体播放进度(0-100)
	
	public static final int VIM_VEHICLE_MEDIA_PLAY_CMD_PROPERTY								= 6707;//原车多媒体播放控制
	
	
	public static final int VIM_ONSTAR_PHONE_NUMBER_PROPERTY									= 6800;//OnStar电话号码
	
	public static final int VIM_ONSTAR_SYSTEM_STATUS_PROPERTY								= 6801;//OnStar状态信息
	
	public static final int VIM_ONSTAR_WIRELESS_ACCESS_POINT_PROPERTY					= 6802;//OnStar无线接入点

	public static final int VIM_ONSTAR_WIRELESS_PASSWORD_PROPERTY						= 6803;//OnStar无线密码

	public static final int VIM_ONSTAR_REQ_CMD_PROPERTY											= 6809;//OnStar指令
	
	
	public static final int VIM_CRUISE_SPEED1_PROPERTY												= 6880;	//巡航速度1
	
	public static final int VIM_CRUISE_SPEED2_PROPERTY												= 6881;	//巡航速度2
	
	public static final int VIM_CRUISE_SPEED3_PROPERTY												= 6882;	//巡航速度3
	
	public static final int VIM_CRUISE_SPEED4_PROPERTY												= 6883;	//巡航速度4
	
	public static final int VIM_CRUISE_SPEED5_PROPERTY												= 6884;	//巡航速度5
	
	public static final int VIM_CRUISE_SPEED6_PROPERTY												= 6885;	//巡航速度6
	
	public static final int VIM_LIMIT_SPEED1_PROPERTY													= 6890;	//速度限值1
	
	public static final int VIM_LIMIT_SPEED2_PROPERTY													= 6891;	//速度限值2
	
	public static final int VIM_LIMIT_SPEED3_PROPERTY													= 6892;	//速度限值3
	
	public static final int VIM_LIMIT_SPEED4_PROPERTY													= 6893;	//速度限值4
	
	public static final int VIM_LIMIT_SPEED5_PROPERTY													= 6894;	//速度限值5
	
	public static final int VIM_LIMIT_SPEED6_PROPERTY													= 6895;	//速度限值6
	
	public static final int VIM_VEHICLE_ALARM_RECORD_NO_PROPERTY							= 6896;//报警记录编号
	
	public static final int VIM_VEHICLE_CONDTION_NO_PROPERTY									= 6897;//车身功能状态信息编号

	public static final int VIM_VEHICLE_DIAGNOSTIC_INFO_NO_PROPERTY						= 6898;//诊断信息编号
	
	public static final int VIM_RESET_SPEED_CONFIG_PROPERTY										= 6899;//恢复速度出厂配置(0,巡航速度;1,速度限值)
	
	
	public static final int VIM_FORD_SYNC_VERSION_PROPERTY										= 6900;//SYNC版本信息
	
	public static final int VIM_FORD_SYNC_RUN_MODE_PROPERTY									= 6901;//SYNC工作模式
	
	public static final int VIM_FORD_SYNC_DEVICE_STATUS_PROPERTY							= 6902;//SYNC设备状态

	public static final int VIM_FORD_SYNC_BLUETOOTH_CONNECT_STATUS_PROPERTY	= 6903;//SYNC蓝牙连接状态

	public static final int VIM_FORD_SYNC_MESSAGE_FLAG_STATUS_PROPERTY				= 6904;//SYNC短信显示标识

	public static final int VIM_FORD_SYNC_SPEECH_STATUS_PROPERTY							= 6905;//SYNC语音状态

	public static final int VIM_FORD_SYNC_CALL_STATUS_PROPERTY								= 6906;//SYNC电话通话状态

	public static final int VIM_FORD_SYNC_MEDIA_INFO_ENABLE_PROPERTY					= 6907;//SYNC Media菜单下的INFO键状态

	public static final int VIM_FORD_SYNC_PHONE_SIGNAL_LEVEL_PROPERTY					= 6908;//SYNC连接的手机信号

	public static final int VIM_FORD_SYNC_PHONE_BATTERY_LEVEL_PROPERTY				= 6909;//SYNC连接的手机电池电量

	public static final int VIM_FORD_SYNC_SRT_UP_INFO_PROPERTY								= 6910;//SYNC同步显示信息(上一行)

	public static final int VIM_FORD_SYNC_SRT_DOWN_INFO_PROPERTY							= 6911;//SYNC同步显示信息(下一行)

	public static final int VIM_FORD_SYNC_SRT_SHORT_INFO_PROPERTY							= 6912;//SYNC同步显示信息(通话时间/USB曲目播放时间)
	
	public static final int VIM_FORD_SYNC_AUDIO_REQUEST_PROPERTY							= 6913;//SYNC声音请求
	
	public static final int VIM_FORD_SYNC_MEDIA_PLAY_TIME_INFO_PROPERTY				= 6914;//SYNC媒体播放时间信息
	
	public static final int VIM_FORD_SYNC_CALL_TIME_INFO_PROPERTY							= 6915;//SYNC通话时间信息
	
	
	public static final int VIM_FORD_SYNC_CUR_MENU_TYPE_PROPERTY							= 6920;//SYNC当前菜单类型
	
	public static final int VIM_FORD_SYNC_SELECTED_ITEM_IN_MENU_PROPERTY			= 6921;//SYNC当前菜单选中的项目

	public static final int VIM_FORD_SYNC_PERCENT_IN_MENU_PROPERTY						= 6922;//菜单项目显示百分比

	public static final int VIM_FORD_SYNC_PERCENT_BAR_TYPE_IN_MENU_PROPERTY		= 6923;//菜单进度条显示属性

	public static final int VIM_FORD_SYNC_CUR_MENU_ICON_PROPERTY							= 6924;//SYNC当前菜单ICON

	public static final int VIM_FORD_SYNC_CUR_DIALOG_TYPE_PROPERTY						= 6925;//当前对话框类型
	
	public static final int VIM_FORD_SYNC_SELECTED_ITEM_IN_DIALOG_PROPERTY			= 6926;//SYNC当前对话框选中的项目

	public static final int VIM_FORD_SYNC_MENU_INFO_COLLECT_PROPERTY					= 6927;//SYNC菜单信息集

	
	public static final int VIM_FORD_SYNC_MENU_ITEM_LINE_ID_PROPERTY					= 6930;//SYNC菜单项行ID
	
	public static final int VIM_FORD_SYNC_MENU_ITEM_LINE_CONTEXT_PROPERTY			= 6931;//SYNC菜单项行内容
	
	public static final int VIM_FORD_SYNC_MENU_ITEM_LINE_TYPE_PROPERTY				= 6932;//SYNC菜单项行状态

	public static final int VIM_FORD_SYNC_MENU_ITEM_LINE_ENABLE_PROPERTY			= 6933;//SYNC菜单项行是否可选

	public static final int VIM_FORD_SYNC_MENU_ITEM_LINE_ICON_LEFT_TEXT_PROPERTY		= 6934;//SYNC菜单项行文本左边ICON或者SOFTKEY ICON

	public static final int VIM_FORD_SYNC_MENU_ITEM_LINE_ICON_RIGHT_TEXT_PROPERTY	= 6935;//SYNC菜单项行文本右边ICON

	public static final int VIM_FORD_SYNC_MENU_ITEM_INFO_COLLECT_PROPERTY					= 6936;//SYNC菜单项信息集

	public static final int VIM_FORD_SYNC_MENU_CONTEXT_CACHE_PROPERTY						= 6937;//SYNC菜单内容缓存
	
	public static final int VIM_FORD_SYNC_DIALOG_CONTEXT_CACHE_PROPERTY						= 6938;//SYNC对话框内容缓存
	
	
	public static final int VIM_FORD_SYNC_REQ_CMD_PROPERTY												= 6999;//SYNC请求命令
	
	
	
	/*
	 *	Definiton of CAN Vehicle Properity 
	 */
	
	public static final int VIM_ENGN_LOAD_PROPERTY 									= 10000;							//发动机负荷
	
	public static final int VIM_FUEL_PRESSURE_PROPERTY							= 10001;							//燃油压力
	
	public static final int VIM_IN_PIPE_PRESSURE_PROPERTY 						= 10002;							//进气管压强
	
	public static final int VIM_IGNITION_ANGLE_PROPERTY 							= 10003;							//点火提前角	
	
	public static final int VIM_INTAKE_TEMP_PROPERTY								= 10004;							//进气温度
	
	public static final int VIM_INTAKE_FLOW_RATE_PROPERTY						= 10005;							//进气流量
	
	public static final int VIM_THROTTLE_POSN_PROPERTY							= 10006;							//节气门位置
	
	public static final int VIM_OXYGEN_SENSOR_POSN_PROPERTY				= 10007;							//氧传感器位置
	
	public static final int VIM_ENGN_RUN_TIME_PROPERTY 							= 10008;							//引擎运行时间
	
	public static final int VIM_VACUUM_OIL_RAIL_PRESSURE_PROPERTY		= 10010;							//真空油轨压力
	
	public static final int VIM_OXYGEN_SENSOR_B1S1_PROPERTY					=  10011;							//氧传感器B1S1
	
	public static final int VIM_OXYGEN_SENSOR_B1S2_PROPERTY					=  10012;							//氧传感器B1S2
	
	public static final int VIM_OXYGEN_SENSOR_B2S1_PROPERTY					=  10013;							//氧传感器B2S2
	
	public static final int VIM_OXYGEN_SENSOR_B2S2_PROPERTY					=  10014;							//氧传感器B2S2
	
	public static final int VIM_OXYGEN_SENSOR_B3S1_PROPERTY					=  10015;							//氧传感器B2S2
	
	public static final int VIM_OXYGEN_SENSOR_B3S2_PROPERTY					=  10016;							//氧传感器B2S2
	
	public static final int VIM_OXYGEN_SENSOR_B4S1_PROPERTY					=  10017;							//氧传感器B2S2
	
	public static final int VIM_OXYGEN_SENSOR_B4S2_PROPERTY					=  10018;							//氧传感器B2S2
	
	public static final int VIM_EGR_OPEN_DEGREE_PROPERTY						=  10019;							//EGR指令开度
	
	public static final int VIM_EVA_CLEAR_OPEN_DEGREE_PROPERTY			=  10020;							//蒸发清除开度
	
	public static final int VIM_THREE_WAY_CATALYST_TEMP_B1S1_PROPERTY				=  10021;			//三元催化剂温度B1S1
	
	public static final int VIM_THREE_WAY_CATALYST_TEMP_B1S2_PROPERTY				=  10022;			//三元催化剂温度B1S2
	
	public static final int VIM_THREE_WAY_CATALYST_TEMP_B2S1_PROPERTY				=  10023;			//三元催化剂温度B2S1
	
	public static final int VIM_THREE_WAY_CATALYST_TEMP_B2S2_PROPERTY				=  10024;			//三元催化剂温度B2S2
	
	public static final int VIM_EVA_SYSTEM_PRESSURE_PROPERTY								=  10025;			//蒸发系统蒸汽压力
	
	public static final int VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY			=  10026;			//瞬时油耗(怠速状态或行驶状态，具体根据车速判断)
	
	public static final int VIM_CUR_TRIP_IDLING_FUEL_CONSUMPTION_PROPERTY		=  10027;			//本次怠速耗油量
	
	public static final int VIM_CUR_TRIP_DRIVING_FUEL_CONSUMPTION_PROPERTY	=  10028;			//本次行驶耗油量
	
	public static final int VIM_HUNDRED_KILOMETERS_AVG_FUEL_CONSUMPTION_PROPERTY	=  10029;	//百公里平均油耗
	
	public static final int VIM_VEHICLE_IDLE_TIME_CUR_PROPERTY										= 10030;		//本次怠速时间
	
	public static final int VIM_VEHICLE_DRIVING_TIME_CUR_PROPERTY								= 10031;		//本次行驶时间
	
	public static final int VIM_VEHICLE_IDLE_TIME_TOTAL_PROPERTY									= 10032;		//累计怠速时间
	
	public static final int VIM_VEHICLE_DRIVING_TIME_TOTAL_PROPERTY							= 10033;		//累计行驶时间
	
	public static final int VIM_TOTAL_IGNITION_TIMES															= 10034;		//总点火次数

	public static final int VIM_AVG_WARM_UP_TIME																= 10035;		//平均热车时间
	
	public static final int VIM_AVG_VECHILE_SPEED																= 10036;		//平均车速
	
	public static final int VIM_HISTORY_HIGHEST_VEHICLE_SPEED										= 10037;		//历史最高车速
	
	public static final int VIM_HISTORY_HIGHEST_ENGINE_SPEED											= 10038;		//历史最高转速
	
	public static final int VIM_CUR_TRIP_QUICK_SPEED_UP_TIMES										= 10039;		//本次急加速次数
	
	public static final int VIM_CUR_TRIP_QUICK_SPEED_DOWN_TIMES									= 10040;		//本次急减速次数
	
	public static final int VIM_TOTAL_TRIP_QUICK_SPEED_UP_TIMES									= 10041;		//累计急加速次数
	
	public static final int VIM_TOTAL_TRIP_QUICK_SPEED_DOWN_TIMES								= 10042;		//累计急减速次数
	
	public static final int VIM_CUR_TRIP_WARM_UP_TIME													= 10043;		//本次热车时长
	
	public static final int VIM_CUR_TRIP_HIGEST_ENGINE_SPEED											= 10044;		//本次最高转速
	
	public static final int VIM_CUR_TRIP_HIGEST_VECHILE_SPEED										= 10045;		//本次最高车速
	
	public static final int VIM_VEHICLE_PROTOCOL_NAME_PROPERTY									= 10100;		//汽车协议名称
	
	public static final int VIM_VEHICLE_MODULE_SN_PROPERTY											= 10101;		//模块SN码
	
	public static final int VIM_CAN_HW_VERSION_PROPERTY												= 10102;		//硬件版本
	
	public static final int VIM_CAN_SW_VERSION_PROPERTY												= 10103;		//软件版本
	
	public static final int VIM_CAN_BAUD_RATE_PROPERTY													= 10104;		//设置波特率
	
	public static final int VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY							= 10105;		//实时数据流状态 0关,1开
	
	public static final int VIM_CAN_STARTUP_MODE_PROPERTY											= 10106;		//随车启动模式 0关,1开
	
	public static final int VIM_CAN_DELAY_CONNECT_STATUS_PROPERTY								= 10107;		//延时连接 0关,1开
	
	public static final int VIM_CAN_DEVICE_CONNECT_STATUS												= 10108;		//CAN连接状态
	
	public static final int VIM_CAN_REQ_COMMAND_PROPERTY												= 10112;		//CAN请求命令
	
	public static final int VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY							= 10113;		//CAN请求自定义命令
	
	public static final int VIM_CAN_DATA_STREAM_UPDATE_PROPERTY								= 19998;		//流数据更新，仅用于回调，无法Set与Get
	
	
	/*
	 * MCU相关属性
	 */
	
	//Geneal Setting 20000 - 20099
	public static final int VIM_MCU_VERSION_PROPERTY 										= 20000; //MCU版本号
	
	public static final int VIM_MCU_SYSTEM_TIME_PROPERTY								= 20001; //系统时间
	
	public static final int VIM_MCU_REVERSE_DETECT_MODE_PROPERTY				= 20002 ; //倒车检测模式,保留
	
	public static final int VIM_MCU_REVERSE_STATUS_PROPERTY							= 20003 ; //倒车状态
	
	public static final int VIM_MCU_BRAKE_DETECT_MODE_PROPERTY					= 20004 ; //手刹检测模式,保留
	
	public static final int VIM_MCU_BRAKE_STATUS_PROPERTY							= 20005 ; //手刹状态
	
	public static final int VIM_MCU_HEAD_LIGHT_DETECT_MODE_PROPERTY		= 20006 ; //大灯检测模式,保留
	
	public static final int VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY					= 20007 ; //大灯状态
	
	public static final int VIM_MCU_TURN_LEFT_LIGHT_STATUS_PROPERTY			= 20008 ; //左转向灯状态
	
	public static final int VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY		= 20009 ; //右转向灯状态
	
	public static final int VIM_MCU_USER_KEY_PROPERTY									= 20010 ; //按键(面板或者方控)
	
	public static final int VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY 			= 20011; //背光亮度
	
	public static final int VIM_MCU_BACKLIGHT_STATUS_PROPERTY 					= 20012; //背光状态
	
	public static final int VIM_MCU_STATUS_PROPERTY 										= 20099; //MCU状态
	
	//Audio 20100 - 20199
	public static final int VIM_MCU_MUTE_STATUS_PROPERTY								= 20100 ; //静音状态
	
	public static final int VIM_MCU_KEY_BEEP_SWITCH_PROPERTY						= 20101 ; //按键音开关
	
	public static final int VIM_MCU_MEDIA_DEFAULT_VOLUME_PROPERTY			= 20110 ; //开机默认媒体音量(0-40)
	
	public static final int VIM_MCU_BT_PHONE_DEFAULT_VOLUME_PROPERTY		= 20111 ; //开机默认媒体音量(0-40)
	
	public static final int VIM_MCU_NAVIGATION_DEFAULT_VOLUME_PROPERTY	= 20112 ; //开机默认媒体音量(0-40)
	
	public static final int VIM_MCU_MEDIA_VOLUME_PROPERTY							= 20113 ; //媒体音量(0-40)
	
	public static final int VIM_MCU_BT_PHONE_VOLUME_PROPERTY						= 20114 ; //蓝牙电话音量(0-40)
	
	public static final int VIM_MCU_NAVIGATION_VOLUME_PROPERTY					= 20115 ; //导航音量(0-40)
	
	public static final int VIM_MCU_GPS_MIX_ENABLE_PROPERTY							= 20016 ; //GPS混音开关
	
	public static final int VIM_MCU_GPS_MONITOR_ENABLE_PROPERTY				= 20017 ; //GPS监听开关
	
	public static final int VIM_MCU_GPS_MIX_LEVEL_PROPERTY							= 20118 ; //GPS混音比例(0-10,默认为7)
	
	public static final int VIM_MCU_EQ_BASS_VALUE_PROPERTY							= 20120 ; //EQ-低音值(0-14)
	
	public static final int VIM_MCU_EQ_ALTO_VALUE_PROPERTY							= 20121 ; //EQ-中音值(0-14)
	
	public static final int VIM_MCU_EQ_TREBLE_VALUE_PROPERTY						= 20122 ; //EQ-高音值(0-14)
	
	public static final int VIM_MCU_EQ_SUBWOOFER_VALUE_PROPERTY				= 20123 ; //EQ-重低音
	
	public static final int VIM_MCU_EQ_CENTER_FREQ_INFO_PROPERTY				= 20124 ; //EQ-中心频点信息{中心频点单位、中心频点值、中心频率}
	
	public static final int VIM_MCU_EQ_LOUDNESS_PROPERTY								= 20125 ; //EQ-等响度
	
	public static final int VIM_MCU_EQ_PRESET_MODE_PROPERTY						= 20126 ; //EQ-预设模式
	
	public static final int VIM_MCU_AUDIO_BALANCE_INFO_PROPERTY					= 20130 ; //声场平衡
	
	//Radio 20200 - 20249 
	public static final int VIM_MCU_RADIO_CUR_STATE_PROPERTY						= 20200 ; //收音机当前状态
	
	public static final int VIM_MCU_RADIO_CUR_BAND_PROPERTY						= 20201; // 收音机当前波段
	
	public static final int VIM_MCU_RADIO_CUR_FREQ_PROPERTY						= 20202 ; //收音机当前频点
	
	public static final int VIM_MCU_RADIO_CUR_REGION_PROPERTY					= 20203 ; //当前收音区域
	
	public static final int VIM_MCU_RADIO_FREQ_LIST_PROPERTY						= 20204 ; //频率列表
	
	public static final int VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY			= 20205 ; //频率列表选中项
	
	public static final int VIM_MCU_RADIO_FAVOR_FREQ_INDEX_PROPERTY			= 20206 ; //收藏当前频率到当前Band的频率列表的索引值(0-5)
	
	public static final int VIM_MCU_RADIO_SIGNAL_LEVEL_PROPERTY					= 20207 ; //收音信号质量，0表示无效台,1表示有效台
	
	public static final int VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY				= 20208; //收音机声音开关状态
	
	// 0:ST(立体声模式开) ，表示电台如果有立体声效果，就按立体声效果播放；如果没有立体声，就按单声道效果播放。 
	// 1:MONO(立体声模式关)，表示不管电台有没有立体声，都按单声道效果播放。
	public static final int VIM_MCU_RADIO_ST_KEY_STATUS_PROPERTY 				= 20210;  //收音立体声key状态
	
	public static final int VIM_MCU_RADIO_ST_STATUS_PROPERTY						= 20211 ; //收音立体声状态(用于ST图标显示)
	
	public static final int VIM_MCU_RADIO_LOC_STATUS_PROPERTY					= 20212 ; //收音LOC状态,0表示远程台，1表示近程台
	
	public static final int VIM_MCU_RADIO_TP_STATUS_PROPERTY						= 20213 ; //收音TP状态
	
	public static final int VIM_MCU_RADIO_TA_STATUS_PROPERTY						= 20214 ; //收音TA状态
	
	public static final int VIM_MCU_RADIO_AF_STATUS_PROPERTY						= 20215 ; //收音AF状态
	
	public static final int VIM_MCU_RADIO_PS_INFO_PROPERTY							= 20216 ; //收音PS信息
	
	public static final int VIM_MCU_RADIO_PTY_TYPE_PROPERTY						= 20217 ; //PTY类型
	
	public static final int VIM_MCU_RADIO_PTY_LIST_PROPERTY							= 20218 ; //PTY列表
		
	public static final int VIM_MCU_RADIO_STOP_SENS_PROPERTY						= 20221;//收音停台灵敏度
	
	public static final int VIM_MCU_RADIO_FM_MAX_FREQ_PROPERTY					= 20222;//当前区域下FM最大频率
	
	public static final int VIM_MCU_RADIO_FM_MIN_FREQ_PROPERTY					= 20223;//当前区域下FM最小频率
	
	public static final int VIM_MCU_RADIO_FM_STEP_VALUE_PROPERTY				= 20224;//当前区域下FM步进值
	
	public static final int VIM_MCU_RADIO_AM_MAX_FREQ_PROPERTY					= 20225;//当前区域下AM最大频率
	
	public static final int VIM_MCU_RADIO_AM_MIN_FREQ_PROPERTY					= 20226;//当前区域下AM最小频率
	
	public static final int VIM_MCU_RADIO_AM_STEP_VALUE_PROPERTY				= 20227;//当前区域下AM步进值
	
	public static final int VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY				= 20249 ;//收音机相关的动作
	
	
	//Media Info 20250 - 20259
	public static final int VIM_MCU_MEDIA_INFO_SOURCE_PROPERTY					= 20250 ; //媒体信息-媒体源
	
	public static final int VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY			= 20251 ; //媒体信息-当前章节(DVD)或当前索引
	
	public static final int VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY		= 20252 ; //媒体信息-总章节数(DVD)或总歌曲数
	
	public static final int VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY		= 20253 ; //媒体信息-当前播放的时间
	
	public static final int VIM_MCU_MEDIA_INFO_TOTAL_PLAY_TIME_PROPERTY	= 20254 ; //媒体信息-总播放的时间
	
	public static final int VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY			= 20255 ; //媒体信息-ID3信息{Title;Artist;Album}
	
	public static final int VIM_MCU_MEDIA_INFO_PLAY_STATE_PROPERTY			= 20256 ; //媒体信息-播放状态
	
	public static final int VIM_MCU_BT_CON_STATE_PROPERTY							= 20257 ; //蓝牙信息-连接信息
	
	public static final int VIM_MCU_BT_CALL_NUMBER_PROPERTY						= 20258 ; //蓝牙信息-通话号码
	
	public static final int VIM_MCU_BT_CALL_STATE_PROPERTY							= 20259 ; //蓝牙信息-通话状态
	
	//DVR 20260-20269
	public static final int VIM_MCU_DVR_DISPLAY_AREA_PROPERTYE					= 20260;//DVR视频显示区域{w,h}
	
    public static final int VIM_MCU_DVR_TOUCH_EVENT_PROPERTYE					= 20261;//DVR触摸事件{x,y,pressed}(x,y是相对于显示区域左上角的坐标)
    
    public static final int VIM_MCU_DVR_KEY_EVENT_PROPERTYE						= 20262;//DVR按键事件
    
    public static final int VIM_MCU_DVR_TYPE_PROPERTYE									= 20263;//DVR类型
    		
	//Power 20270 - 20299
	public static final int VIM_MCU_VOLTAGE_PROPERTY 										= 20270; // MCU电压
	
	public static final int VIM_MCU_LAST_SLEEP_REASON_PROPERTY 					= 20271; // MCU上次休眠原因
	
	public static final int VIM_MCU_STARTUP_MODE_PROPERTYE							= 20272; // MCU启动方式	
	
	public static final int VIM_MCU_ACC_STAT_PROPERTYE									= 20273 ; // ACC状态
	
	//Source 20300 - 20349
	public static final int VIM_MCU_LAST_POWER_OFF_SOURCE_PROPERTY			= 20300; // 上一次关机源
	
	public static final int VIM_MCU_UI_MODE_PROPERTY										= 20301 ; // 当前界面
	
	public static final int VIM_MCU_MEDIA_MODE_PROPERTY								= 20302 ; // 当前源
	
	public static final int VIM_MCU_NAV_AUDIO_CHANNEL_PROPERTY					= 20303 ; // GPS播报，模拟通道
	
	public static final int VIM_MCU_ARM_AUDIO_CHANNEL_PROPERTY					= 20304 ; // 媒体播放，I2S通道
	
	//Steer Study 20350 - 20359
	public static final int VIM_MCU_STEER_STUDY_STATE_PROPERTY					= 20350 ; // 方向盘学习状态
	
	public static final int VIM_MCU_STEER_STUDY_KEY_PROPERTY						= 20351 ; // 方向盘学习Key的功能ID
	
	public static final int VIM_MCU_STEER_STUDY_ACTION_PROPERTY					= 20352 ; // 方向盘学习操作
	
	public static final int VIM_MCU_STEER_STUDIED_KEY_FUNC_ID_PROPERTY	= 20353; // 已学习的按键ID
	
	public static final int VIM_MCU_STEER_STUDIED_KEY_DOWN_PROPERTY		= 20354;//	已学习的键按下
	
	//Mcu Upgrade 20360 - 20369
	public static final int VIM_MCU_UPGRADE_PATH_PROPERTY							= 20360; // MCU升级文件路径

	public static final int VIM_MCU_UPGRADE_STATE_PROPERTY 							= 20361; // MCU升级状态

	public static final int VIM_MCU_UPGRADE_PROGRESS_PROPERTY 					= 20362; // MCU升级进度，只有在WRITE状态才有意义
	
	
	//TPMS 20400 - 20450
	public static final int VIM_MCU_TPMS_DEVICE_STATE_PROPERTY						= 20400; // TPMS设备状态
	
	public static final int VIM_MCU_TPMS_PRESSURE_UNIT_PROPERTY					= 20401; // TPMS胎压值单位

	public static final int VIM_MCU_TPMS_TEMP_UNIT_PROPERTY							= 20402; // TPMS温度值单位
	
	public static final int VIM_MCU_TPMS_PRESSURE_ALARM_RANGE_PROPERTY		= 20403; // TPMS胎压报警调节范围{最小值*10，最大值*10,胎压单位}
	
	public static final int VIM_MCU_TPMS_TEMP_ALARM_RANGE_PROPERTY				= 20404; // TPMS温度报警调节范围{最小值,最大值,温度单位}
	
	public static final int VIM_MCU_TPMS_PRESSURE_ALARM_PARAM_PROPERTY		= 20405; // TPMS设备胎压报警参数参数,{轮胎轴,高压阈值*10,低压阈值*10}
	
	public static final int VIM_MCU_TPMS_TEMP_ALARM_PARAM_PROPERTY				= 20406; // TPMS设备温度报警参数参数,设置最高温
	
	public static final int VIM_MCU_TPMS_TIRE_DATA_PROPERTY							= 20407; // 轮胎数据,{轮胎位置,传感器ID,胎压*10,温度,传感器电压状态,胎压状态,温度状态,气门阀状态}

	public static final int VIM_MCU_TPMS_DELTE_TIRE_ID_PROPERTY						= 20408;// 删除轮胎ID,参数为轮胎位置
	
	public static final int VIM_MCU_TPMS_INIT_DATA_STATE_PROPERTY					= 20409;// 接收手持读码器设备数据状态
	
	public static final int VIM_MCU_TPMS_AUTO_MATCH_CODE_STATE_PROPERTY	= 20410;// 自动对码状态

	public static final int VIM_MCU_TPMS_UPDATE_TIRE_CODE_PROPERTY				= 20411;// 手动输码{轮胎位置,轮胎ID}

	public static final int VIM_MCU_TPMS_DEVICE_TYPE_PROPERTY							= 20447;//TPMS设备型号{设备类型,设备型号ID}

	public static final int VIM_MCU_TPMS_REQ_AUTO_MATCH_CODE_PROPERTY		= 20448;//请求自动对码,参数为轮胎位置

	public static final int VIM_MCU_TPMS_REQ_COMMAND_PROPERTY						= 20449; // 请求TPMS命令
	
	public static final int VIM_MCU_SOURCE_GAIN_PROPERTY 								= 99998;// MCU源增益,整形数组{SourceType,GainValue}
	
	public static final int VIM_MCU_REQ_COMMAND_PROPERTY							= 99999; // 请求MCU命令

		
	
	
}
