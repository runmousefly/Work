package com.mct.deviceservices;

import com.mct.DeviceInterfaceProperties;
import com.mct.DevicePropertyConstants;

import android.util.SparseIntArray;

public class RangingProtocol
{
		// Data types associated with properties.
		public static final int DATA_TYPE_UNKNOWN 						= 0;
		public static final int DATA_TYPE_INTEGER 						= 1;
		public static final int DATA_TYPE_FLOAT 							= 2;
		public static final int DATA_TYPE_STRING 							= 3;	
		public static final int DATA_TYPE_INT_ARRAY 					= 4;	
		public static final int DATA_TYPE_FLOAT_ARRAY 				= 5;	
		public static final int DATA_TYPE_STRING_ARRAY 				= 6;		
				
		//权限定义
		public static final int PROPERITY_PERMISSON_NO				= 0; 	// NOTIFY ONLY
		public static final int PROPERITY_PERMISSON_GET				= 1; 	// GET ONLY
		public static final int PROPERITY_PERMISSON_SET				= 2; 	// SET ONLY
		public static final int PROPERITY_PERMISSON_GET_SET 	= 3;	// GET_SET		
			
		// Cmd定义
		public static final int CMD_OPEN_LASER 								= 'O';// 信号切换
		public static final int CMD_CLOSE_LASER 							= 'C';// 音量设置
		public static final int CMD_RANGING_DISTANCE 					= 'D';// 扫频开关
		public static final int CMD_QUERY_MODULE_INFO 				= 'S';// 查询模块收发状态

		//所有支持的属性集
		public static final int[] RANGING_DEVICE_PROPERITIES = new int[] { 
				DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY,
				DeviceInterfaceProperties.DIM_RANGING_DISTANCE_PROPERTY,
				DeviceInterfaceProperties.DIM_RANGING_TEMP_PROPERTY,
				DeviceInterfaceProperties.DIM_RANGING_VOLTAGE_PROPERTY,
				DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY
		};
		
		// 属性读写权限表
		public static final SparseIntArray RANGING_DEVICE_PROPERITY_PERMISSION_TABLE = new SparseIntArray();
		static
		{
			RANGING_DEVICE_PROPERITY_PERMISSION_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY,PROPERITY_PERMISSON_GET);
			RANGING_DEVICE_PROPERITY_PERMISSION_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_DISTANCE_PROPERTY,PROPERITY_PERMISSON_GET);
			RANGING_DEVICE_PROPERITY_PERMISSION_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_TEMP_PROPERTY,PROPERITY_PERMISSON_GET);
			RANGING_DEVICE_PROPERITY_PERMISSION_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_VOLTAGE_PROPERTY,PROPERITY_PERMISSON_GET);
			RANGING_DEVICE_PROPERITY_PERMISSION_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY,PROPERITY_PERMISSON_SET);
			
		}

		// 属性值类型表
		public static final SparseIntArray RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE = new SparseIntArray();
		static
		{
			RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY,DATA_TYPE_INTEGER);
			RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_DISTANCE_PROPERTY,DATA_TYPE_FLOAT);
			RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_TEMP_PROPERTY,DATA_TYPE_FLOAT);
			RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_VOLTAGE_PROPERTY,DATA_TYPE_FLOAT);
			RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE.put(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY,DATA_TYPE_INTEGER);
		}

		public static int getProperityDataType(int prop)
		{
			return (RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE.indexOfKey(prop) >= 0) ? RANGING_DEVICE_PROPERITY_DATA_TYPE_TABLE.get(prop) : -1;
		}

		public static int getProperityPermission(int prop)
		{
			return (RANGING_DEVICE_PROPERITY_PERMISSION_TABLE.indexOfKey(prop) >= 0) ? RANGING_DEVICE_PROPERITY_PERMISSION_TABLE.get(prop) : -1;
		}
	
}
