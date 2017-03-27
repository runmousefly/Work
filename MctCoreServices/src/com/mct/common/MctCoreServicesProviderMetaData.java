package com.mct.common;
import android.net.Uri;

public class MctCoreServicesProviderMetaData
{
	/** Database filename */
	public static final String DB_NAME = "mct_coreservices_data.db";


	/** Name of product table in the database */
	public static final String TABLE_CAN_VEHICLE_INFO 		= "CanVehicleInfo";
	public static final String TABLE_APP_CONFIG 					= "AppConfig";
	public static final String TABLE_CAN_VEHICLE_SETTING 	= "CanVehicleSetting";
	public static final String TABLE_VEHICLE_FUNCTION 		= "VehicleFunction";
	
	/**
	 * The content:// URI to access cached products
	 */
	public static final Uri CAN_VEHICLE_INFO_CONTENT_URI 		= Uri.parse("content://com.mct.coreservices/CanVehicleInfo");
	public static final Uri APP_CONFIG_CONTENT_URI					= Uri.parse("content://com.mct.coreservices/AppConfig");//暂保留不用
	public static final Uri CAN_VEHICLE_SETTING_CONTENT_URI	= Uri.parse("content://com.mct.coreservices/CanVehicleSetting");
	public static final Uri CAN_VEHICLE_FUNCTION_URI				= Uri.parse("content://com.mct.coreservices/VehicleFunction");
	
	public static final String 	COLUMN_ID											="CID";//计数
	/** CanVehicleInfo表 */
	public static final String 	COLUMN_VEHICLE_MODEL_ID 				= "CarModelId";//车型ID
	public static final String 	COLUMN_VEHICLE_SERIES_ID				= "CarSeriesId";//车系ID
	public static final String 	COLUMN_VEHICLE_PLATFORM_ID 		= "CarPlatformlId";//车辆平台ID
	public static final String 	COLUMN_VEHICLE_CAN_BOX_IDS 			= "CanBoxId";//CAN盒ID
	public static final String 	COLUMN_VEHICLE_MODEL_CH_NAME	= "CarModelCHName";//车型中文名
	public static final String 	COLUMN_VEHICLE_MODEL_EN_NAME	= "CarModelENName";//车型英文名
	public static final String 	COLUMN_VEHICLE_SERIES_CH_NAME	= "CarSeriesCHName";//车系中文名
	public static final String 	COLUMN_VEHICLE_SERIES_EN_NAME	= "CarSeriesENName";//车系英文名
	public static final String 	COLUMN_VEHICLE_CAN_BOX_CH_NAME= "CanBoxCHName";//车系中文名
	public static final String 	COLUMN_VEHICLE_CAN_BOX_EN_NAME= "CanBoxENName";//车系英文名
	
	/** AppConfig表*/
	public static final String 	COLUMN_PROP_ID 							= "PropId";//PropID
	public static final String 	COLUMN_PROP_VALUE 						= "Value";//PropValue
	
	/** CanVehicleSetting表 */
//	public static final String 	COLUMN_VEHICLE_PLATFORM_ID 	= "CarPlatformlId";//平台ID
	public static final String 	COLUMN_VEHICLE_PROP_GROUP_ID 		= "CarPropGroupId";//设置项的属性分类
	public static final String 	COLUMN_VEHICLE_PROP_GROUP_NAME 	= "CarPropGroupName";//设置项的属性分类中文名称
	public static final String 	COLUMN_VEHICLE_PROP_GROUP_NAME2 	= "CarPropGroupName2";//设置项的属性分类英文名称，暂保留
	public static final String 	COLUMN_VEHICLE_PROP_ID 					= "CarPropId";//设置项的属性ID
	public static final String 	COLUMN_VEHICLE_PROP_NAME 				= "CarPropName";	//设置项的属性中文名称
	public static final String 	COLUMN_VEHICLE_PROP_NAME2 				= "CarPropName2";	//设置项的属性英文文名称,暂保留
	public static final String 	COLUMN_VEHICLE_PROP_TYPE 				= "CarPropType";//设置项的属性类型
	public static final String 	COLUMN_VEHICLE_PROP_VALUE 				= "CarPropValue";//设置项的属性值内容
	public static final String 	COLUMN_VEHICLE_PROP_VALUE2 			= "CarPropValue2";//设置项的属性值内容，暂保留
	public static final String 	COLUMN_VEHICLE_PROP_CMD 					= "CarPropCmd";//设置项的属性值内容对应的命令
	public static final String	COLUMN_VEHICLE_PROP_CONTENT 			= "CarPropContent";//弹出框内容-中文
	public static final String	COLUMN_VEHICLE_PROP_CONTENT2		= "CarPropContent2";//弹出框内容-英文
	public static final String 	COLUMN_DEPEND_BY_CAR_PROP_ID		= "DependByCarPropId";//依赖项属性ID
	public static final String 	COLUMN_DEPEND_BY_CAR_PROP_CMD		= "DependByCarPropCmd";//依赖项属性命令值
	public static final String 	COLUMN_PROGRESS_STEP_VALUE			= "ProgressStepValue";//进度条类型时的步进值,默认为1
	
	
	/** VehicleFunction表 */
	//	public static final String 	COLUMN_VEHICLE_MODEL_ID 				= "CarModelId";//车型ID
	public static final String 	COLUMN_SUPPORT_VEHICLE_INFO 		= "VehicleInfo";	//支持原车信息
	public static final String 	COLUMN_VEHICLE_INFO_VIEW_ID 		= "VehicleInfoViewId"; //原车信息界面ID
	public static final String 	COLUMN_SUPPORT_VEHICLE_SETTING = "VehicleSetting";	//支持原车设置
	public static final String 	COLUMN_SUPPORT_AIR_CONDITION	 = "AirCondition";	//支持原车空调
	public static final String 	COLUMN_SUPPORT_AIR_CONDITION_CONTROL = "AirConditionControl";	//支持原车空调控制
	public static final String 	COLUMN_SUPPORT_AMPLIFIER  			= "Amplifier";//支持功放
	public static final String 	COLUMN_SUPPORT_PARKING_RADAR 	= "ParkingRadar";//支持倒车雷达
	public static final String 	COLUMN_SUPPORT_TPMS 					= "TPMS";//支持胎压
	public static final String 	COLUMN_SUPPORT_AVM 						= "AVM";//支持全景
	public static final String 	COLUMN_SUPPORT_REVERSE_TRACE	= "ReverseTrace";//支持倒车轨迹线
	public static final String 	COLUMN_SUPPORT_COMPASS				= "Compass";//支持指南针
	public static final String 	COLUMN_SUPPORT_VEHICLE_MEDIA_PLAYER	= "VehicleMediaPlayer";//支持原车多媒体
	public static final String 	COLUMN_SUPPORT_ONSTART				= "OnStar";//支持安吉星
	public static final String 	COLUMN_SUPPORT_SYNC						= "SYNC";//福特SYNC
}
