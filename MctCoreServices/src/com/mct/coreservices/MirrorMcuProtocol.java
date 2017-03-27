package com.mct.coreservices;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class MirrorMcuProtocol
{
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MCU CMD
	public static final int CMD_RESET = 0x00; 		// 请求重启MCU与ARM
	public static final int CMD_LINK = 0x01; 			// ARM没有任何命令时每隔一秒发送一次
	public static final int CMD_READY = 0x02; 		// ARM启动完成
	public static final int CMD_VERSION = 0x03; 	// MCU或者Boot版本以及MCU所处的模式
	public static final int CMD_MCU_INFO = 0x04;
	public static final int CMD_SYS_STATUS = 0x05;
	public static final int CMD_IAP = 0x0F;
	public static final int CMD_UNUSE = 0x10; 		// 不使用
	public static final int CMD_VOLTAGE = 0x11;
	public static final int CMD_AUDIO_PARAM = 0x12;
	public static final int CMD_DATE_TIME = 0x13;
	public static final int CMD_RDS = 0x14;
	public static final int CMD_TEL = 0x15;
	public static final int CMD_MEDIA = 0x16;
	public static final int CMD_MEDIA_CONTROL = 0x17;
	public static final int CMD_MEDIA_INFO = 0x18;
	public static final int CMD_AUDIO_SWITCH = 0x19;
	public static final int CMD_VIDEO_SWITCH = 0x1A;
	public static final int CMD_ADCKEY = 0x1B;
	public static final int CMD_ADCKEY_SET = 0x1C;
	public static final int CMD_ACC = 0x80;
	public static final int CMD_GEAR = 0x81;
	public static final int CMD_BRAKE = 0x82;
	public static final int CMD_LIGHT = 0x83;
	public static final int CMD_CAR_DOOR = 0x84;

	public static final int CMD_CAR_KEY = 0x85;
	public static final int CMD_WHEEL_ANGLE = 0x86;
	public static final int CMD_CAR_VIN = 0x87;
	public static final int CMD_CLIMATE = 0x88;
	public static final int CMD_CAR_METER = 0x89;
	public static final int CMD_VEHICLE_SETTING = 0x8A;
	public static final int CMD_FAULT_CODE = 0x8B;
	public static final int CMD_FUEL_REALTIME = 0x8C;

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//MCU SUBCMD
	//SYS_STATUS
	public static final int  SUB_CMD_MCU_LAST_SLEEP_REASON = 0x00;
	
	//IAP Sub Cmd
	public static final int SUB_CMD_IAP_START	 	= 0x00;
	public static final int SUB_CMD_IAP_EXIT	 	= 0x01;
	public static final int SUB_CMD_IAP_ERASE 	= 0x02;
	public static final int SUB_CMD_IAP_WRITE	 	= 0x03;
	public static final int SUB_CMD_IAP_READ	 	= 0x04;
	public static final int SUB_CMD_IAP_BLANK	 	= 0x05;
	public static final int SUB_CMD_IAP_VERTIFY 	= 0x06;
	public static final int SUB_CMD_IAP_INIT		= 0x07;
	
	// IAP ACK
	public static final int MCU_IAP_ACK_START 	= 0x80;
	public static final int MCU_IAP_ACK_EXIT 		= 0x81;
	public static final int MCU_IAP_ACK_ERASE 	= 0x82;
	public static final int MCU_IAP_ACK_WRITE 	= 0x83;
	public static final int MCU_IAP_ACK_READ 		= 0x84;
	public static final int MCU_IAP_ACK_BLANK 	= 0x85;
	public static final int MCU_IAP_ACK_VERTIFY 	= 0x86;
	public static final int MCU_IAP_ACK_INIT 		= 0x87;

	// IAP Error Code
	public static final int MCU_IAP_NO_ERROR 				= 0x00;
	public static final int MCU_IAP_NOT_SUPPORT 		= 0x01;
	public static final int MCU_IAP_ERASE_ERROR 		= 0x02;
	public static final int MCU_IAP_ADDRESS_ERROR 	= 0x03;
	public static final int MCU_IAP_WRITE_ERROR 		= 0x04;
	public static final int MCU_IAP_READ_ERROR 			= 0x05;
	public static final int MCU_IAP_NOT_BLANK 			= 0x06;

	//MCU协议类型
	public static final int MCU_CMD_TYPE_SET 			= 0x01;	//需要回ACK
	public static final int MCU_CMD_TYPE_GET 			= 0x02;	//需要回ACK
	public static final int MCU_CMD_TYPE_MSG 		= 0x03;	//不需要回ACK
	public static final int MCU_CMD_TYPE_ACK 			= 0x04;	
	public static final int MCU_CMD_TYPE_NOTIFY 	= 0x05; //MCU主动上报

	//权限定义
	public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
	public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
	public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
	public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

	//所有支持的属性集
	public static final int[] MIRROR_MCU_PROPERITIES = new int[] { 
			VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, 
			VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY, 
			VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY, 
			VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY};
	
	// 属性读写权限表
	public static final SparseIntArray MIRROR_MCU_PROPERITY_PERMISSION_TABLE = new SparseIntArray();
	static
	{
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY, PROPERITY_PERMISSON_GET);
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, PROPERITY_PERMISSON_GET);
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY, PROPERITY_PERMISSON_GET);
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY, PROPERITY_PERMISSON_GET);
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY, PROPERITY_PERMISSON_SET);
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY, PROPERITY_PERMISSON_GET);
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY, PROPERITY_PERMISSON_GET);
		MIRROR_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, PROPERITY_PERMISSON_SET);
	}

	// 属性值类型表
	public static final SparseIntArray MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE = new SparseIntArray();
	static
	{
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_FLOAT);
		MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
	}

	public static int getProperityDataType(int prop)
	{
		return (MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.indexOfKey(prop) >= 0) ? MIRROR_MCU_PROPERITY_DATA_TYPE_TABLE.get(prop) : -1;
	}

	public static int getProperityPermission(int prop)
	{
		return (MIRROR_MCU_PROPERITY_PERMISSION_TABLE.indexOfKey(prop) >= 0) ? MIRROR_MCU_PROPERITY_PERMISSION_TABLE.get(prop) : -1;
	}
}
