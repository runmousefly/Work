/*
 *    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
 *    Qualcomm Technologies Proprietary and Confidential.
 *
 */

package com.mct;

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

	public static final int VIM_PARKING_BRAKES_PROPERTY = 1014;							//驻车制动

	public static final int VIM_INTERIOR_TEMP_PROPERTY = 1015;							//车内温度

	public static final int VIM_EXTERIOR_TEMP_PROPERTY = 1016;							//车外温度

	public static final int VIM_EXTERIOR_BRGHTNESS_PROPERTY = 1017;					//外部亮度

	public static final int VIM_RAIN_SENSOR_PROPERTY = 1018;								//雨量传感器

	public static final int VIM_MAIN_WSHIELD_WIPER_PROPERTY = 1019;				//前主雨刷

	public static final int VIM_REAR_WSHIELD_WIPER_PROPERTY = 1020;				//后雨刷

	public static final int VIM_HVAC_FAN_DIRECTION_PROPERTY = 1021;					//暖风机方向

	public static final int VIM_HVAC_FAN_SPEED_PROPERTY = 1022;							//暖风机风速

	public static final int VIM_HVAC_FAN_TARGET_TEMP_PROPERTY = 1023;			//暖风机温度

	public static final int VIM_AIR_CONDITIONING_PROPERTY = 1024;						//空调

	public static final int VIM_AIR_CIRCULATION_PROPERTY = 1025;						//换气开关

	public static final int VIM_HEATER_PROPERTY = 1026;										//加热器

	public static final int VIM_HEATER_STRNG_WHEEL_PROPERTY = 1027;				//加热方向盘

	public static final int VIM_HEATER_DRVNG_SEAT_PROPERTY = 1028;					//加热驾驶员座椅

	public static final int VIM_HEATER_PSNGR_SEAT_PROPERTY = 1029;					//加热副驾驶座

	public static final int VIM_DEFROST_WSHIELD_PROPERTY = 1030;						//挡风玻璃除霜

	public static final int VIM_DEFROST_FRONT_WINDOW_PROPERTY = 1031;			//前窗除霜

	public static final int VIM_DEFROST_REAR_WINDOW_PROPERTY = 1032;				//后窗除霜

	public static final int VIM_DEFROST_SIDE_MIRRORS_PROPERTY = 1033;				//后视镜除霜
	
	public static final int VIM_DRVR_WINDOW_PROPERTY = 1034;							//驾驶座车窗

	public static final int VIM_PSNGR_WINDOW_PROPERTY = 1035;							//副驾驶座车窗

	public static final int VIM_REAR_LEFT_WINDOW_PROPERTY = 1036;					//后左车窗

	public static final int VIM_REAR_RIGHT_WINDOW_PROPERTY = 1037;					//后右车窗		

	public static final int VIM_SUN_ROOF_OPEN_PROPERTY = 1038;							//天窗打开

	public static final int VIM_SUN_ROOF_TILT_PROPERTY = 1039;							//天窗收起

	public static final int VIM_CONV_ROOF_PROPERTY = 1040;									//天窗关闭

	public static final int VIM_TRANSMISSION_GEAR_STATUS_PROPERTY = 1041;		//档位

	public static final int VIM_VEHICLE_POWER_MODE_PROPERTY = 1042;				//车载电源模式

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

	public static final int VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY = 1084;			//顶部敞篷车门状态

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

	public static final int VIM_FADE_LVL_PROPERTY = 1133;														//声音平衡

	public static final int VIM_TREBLE_LVL_PROPERTY = 1134;													//高音

	public static final int VIM_BASS_LVL_PROPERTY = 1135;														//低音大小

	public static final int VIM_BRIGHTNESS_LVL_PROPERTY = 1136;											//亮度级别

	public static final int VIM_REAR_LEFT_HEAD_PHONE_STATUS_PROPERTY = 1137;				//后左喇叭状态

	public static final int VIM_REAR_LEFT_HEAD_PHONE_VOLUME_PROPERTY = 1138;				//后左喇叭音量

	public static final int VIM_REAR_RIGHT_HEAD_PHONE_STATUS_PROPERTY = 1139;				//后右喇叭音量
		
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

	public static final int VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY = 1159;					//????????

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
	
	public static final int VIM_CAN_REQ_COMMAND_PROPERTY												= 10112;		//CAN请求命令
	
	public static final int VIM_CAN_DATA_STREAM_UPDATE_PROPERTY								= 19998;		//流数据更新，仅用于回调，无法Set与Get
	
	
	/*
	 * MCU相关属性
	 */
	
	//Geneal Setting 20000 - 20099
	public static final int VIM_MCU_VERSION_PROPERTY 										= 20000; //MCU版本号
	
	public static final int VIM_MCU_SYSTEM_TIME_PROPERTY								= 20001; //系统时间
	
	public static final int VIM_MCU_REVERSE_DETECT_MODE_PROPERTY				= 20002 ; //倒车检测模式
	
	public static final int VIM_MCU_REVERSE_STATUS_PROPERTY							= 20003 ; //倒车状态
	
	public static final int VIM_MCU_BRAKE_DETECT_MODE_PROPERTY					= 20004 ; //手刹检测模式
	
	public static final int VIM_MCU_BRAKE_STATUS_PROPERTY							= 20005 ; //手刹状态
	
	public static final int VIM_MCU_HEAD_LIGHT_DETECT_MODE_PROPERTY		= 20006 ; //大灯检测模式
	
	public static final int VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY					= 20007 ; //大灯状态
	
	public static final int VIM_MCU_TURN_LEFT_LIGHT_STATUS_PROPERTY			= 20008 ; //左转向灯状态
	
	public static final int VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY		= 20009 ; //右转向灯状态
	
	public static final int VIM_MCU_USER_KEY_PROPERTY									= 20010 ; //按键(面板或者方控)
	
	public static final int VIM_MCU_STATUS_PROPERTY 										= 20099; //MCU状态，Boot模式与Normal模式
	
	//Audio 20100 - 20199
	public static final int VIM_MCU_MUTE_STATUS_PROPERTY								= 20100 ; //静音状态
	
	//Radio 20200 - 20249 
	public static final int VIM_MCU_RADIO_CUR_STATE_PROPERTY						= 20200 ; //收音机当前状态
	
	public static final int VIM_MCU_RADIO_CUR_BAND_PROPERTY						= 20201; // 收音机当前波段
	
	public static final int VIM_MCU_RADIO_CUR_FREQ_PROPERTY						= 20202 ; //收音机当前频点
	
	public static final int VIM_MCU_RADIO_CUR_REGION_PROPERTY					= 20203 ; //当前收音区域
	
	public static final int VIM_MCU_RADIO_ST_STATUS_PROPERTY						= 20204 ; //收音ST状态
	
	public static final int VIM_MCU_RADIO_LOC_STATUS_PROPERTY					= 20205 ; //收音LOC状态
	
	public static final int VIM_MCU_RADIO_TP_STATUS_PROPERTY						= 20206 ; //收音TP状态
	
	public static final int VIM_MCU_RADIO_TA_STATUS_PROPERTY						= 20207 ; //收音TA状态
	
	public static final int VIM_MCU_RADIO_AF_STATUS_PROPERTY						= 20208 ; //收音AF状态
	
	public static final int VIM_MCU_RADIO_PS_INFO_PROPERTY							= 20209 ; //收音PS信息
	
	public static final int VIM_MCU_RADIO_PTY_TYPE_PROPERTY						= 20210 ; //PTY类型
	
	public static final int VIM_MCU_RADIO_PTY_LIST_PROPERTY							= 20211 ; //PTY列表
	
	public static final int VIM_MCU_RADIO_PRESET_FREQ_LIST_PROPERTY			= 20212 ; //预存频率列表
	
	public static final int VIM_MCU_RADIO_FOCUS_PRESET_FREQ_PROPERTY		= 20213 ; //预存频率列表选中项
	
	public static final int VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY				= 20249 ; //收音机相关的动作	
	
	//Media Info 20250 - 20269
	public static final int VIM_MCU_MEDIA_INFO_SOURCE_PROPERTY					= 20250 ; //媒体信息-媒体源
	
	public static final int VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY			= 20251 ; //媒体信息-当前章节(DVD)或当前索引
	
	public static final int VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY		= 20252 ; //媒体信息-总章节数(DVD)或总歌曲数
	
	public static final int VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY		= 20253 ; //媒体信息-当前播放的时间
	
	public static final int VIM_MCU_MEDIA_INFO_TOTAL_PLAY_TIME_PROPERTY	= 20254 ; //媒体信息-总播放的时间
	
	public static final int VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY			= 20255 ; //媒体信息-ID3信息
	
	public static final int VIM_MCU_BT_CON_STATE_PROPERTY							= 20256 ; //蓝牙信息-连接信息
	
	public static final int VIM_MCU_BT_CALL_STATE_PROPERTY							= 20257 ; //蓝牙信息-通话状态
	
	//Power 20270 - 20299
	public static final int VIM_MCU_VOLTAGE_PROPERTY 										= 20270; // MCU电压
	
	public static final int VIM_MCU_LAST_SLEEP_REASON_PROPERTY 					= 20271; // MCU上次休眠原因
	
	public static final int VIM_MCU_STARTUP_MODE_PROPERTYE							= 20272; // MCU启动方式	
	
	public static final int VIM_MCU_ACC_STAT_PROPERTYE									= 20273 ; // ACC状态
	
	//Source 20300 - 20349
	public static final int VIM_MCU_LAST_POWER_OFF_SOURCE_PROPERTY			= 20300; //上一次关机源
	
	public static final int VIM_MCU_CURRENT_APP_PROPERTY								= 20301 ; //当前界面
	
	public static final int VIM_MCU_CURRENT_SOURCE_PROPERTY						= 20302 ; //当前源
	
	//Steer Study 20350 - 20359
	public static final int VIM_MCU_STEER_STUDY_STATE_PROPERTY					= 20350 ; //方向盘学习状态
	
	public static final int VIM_MCU_STEER_STUDY_KEY_FUN_ID_PROPERTY			= 20351 ; //方向盘学习Key的功能ID
	
	public static final int VIM_MCU_STEER_STUDY_ACTION_PROPERTY					= 20352 ; //方向盘学习操作
	
	//Mcu Upgrade 20360 - 20369
	public static final int VIM_MCU_UPGRADE_PATH_PROPERTY							= 20360; // MCU升级文件路径

	public static final int VIM_MCU_UPGRADE_STATE_PROPERTY 							= 20361; // MCU升级状态

	public static final int VIM_MCU_UPGRADE_PROGRESS_PROPERTY 					= 20362; // MCU升级进度，只有在WRITE状态才有意义
	
	public static final int VIM_MCU_REQ_COMMAND_PROPERTY							= 99999; // 调试预留使用，串口打开与关闭

		
	
	
}
