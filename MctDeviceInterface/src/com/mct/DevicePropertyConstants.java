/*
*    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
*    Qualcomm Technologies Proprietary and Confidential.
*
*/
package com.mct;

import java.nio.channels.ScatteringByteChannel;
import java.security.KeyStore.PrivateKeyEntry;

/**
 * VehiclePropertyConstants provides constant definations for various vehicle
 * properties. Please note that this API is work in progress, and there might be
 * few changes going forward.
 *
 */
public final class DevicePropertyConstants
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
		
		

	//DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY
	public static final int RET_CODE_SUCCESS 									= 0x00;// 设置成功
	public static final int RET_CODE_BUSY 										= 0x01;// 模块繁忙或者设置失败
	public static final int RET_CODE_CHANNEL_ERROR 						= 0x02;// 无此信道或信道错误
	public static final int RET_CODE_MODULE_DIE 							= 0x07;// 模块被毙
	public static final int RET_CODE_CHECKSUM_ERROR 					= 0x09;// 校验错误
	public static final int RET_CODE_CALL_ACTIVE_OVER 					= 0x62;// 主呼结束
	public static final int RET_CODE_BS_ACTIVE_TIMEOUT 				= 0x6C;// BS激活超时
	public static final int RET_CODE_REJECT_SENDING 						= 0x6D;// 拒绝发送
	public static final int RET_CODE_CALL_ACTIVE_TIMEOUT 			= 0x6E;// 主呼超时
	public static final int RET_CODE_BE_CALLED_OVER 					= 0x6F;// 被呼结束
	public static final int RET_CODE_SEND_MESSAGE_SUCCESS 		= 0x70;// 短信发送成功
	public static final int RET_CODE_RECV_PACKAGE_TIMEOUT 		= 0x7E;// 接收反馈包超时
	public static final int RET_CODE_INTERPHONE_CHECK_SUCCESS = 0xA1;// 对讲机检测成功
	public static final int RET_CODE_CALL_PROPMT_SUCCESS 			= 0xA2;// 呼叫提示成功
	public static final int RET_CODE_REMOTE_MONITOR_SUCCESS 	= 0xA3;// 远程监听成功
	public static final int RET_CODE_REMOTE_KILL_SUCCESS 			= 0xA4;// 对讲机遥毙成功
	public static final int RET_CODE_REMOTE_ACTIVE_SUCCESS 		= 0xA5;// 对讲机激活成功
	public static final int RET_CODE_DECODE_FAILED 						= 0xAF;// 解码失败	
	

	// Call Type
	public static final int CALL_TYPE_PERSONAL 						= 0x01;// 个呼
	public static final int CALL_TYPE_GROUP 							= 0x02;// 组呼
	public static final int CALL_TYPE_NO_ADDRESS 					= 0x03;// 无地址呼
	public static final int CALL_TYPE_BROADCAST 					= 0x04;// 全呼和广播
	
	//所有开关状态的值定义，如扫频开关等
	public static final int STATUS_OFF 										= 0;	
	public static final int STATUS_ON 										= 1;				
	
	//发射功率
	public static final int SEND_POWER_LOWER						= 0;
	public static final int SEND_POWER_HIGH							= 1;
	
	//省电模式
	public static final int SAVE_POWER_OFF								= 0;
	public static final int SAVE_POWER_ON								= 1;
	
	//静噪等级
	public static final int SQUELCH_LEVEL_NORMAL					= 0;//正常
	public static final int SQUELCH_LEVEL_ALWAYS					= 0;//常开
	public static final int SQUELCH_LEVEL_STEADY					= 0;//加强
	
	//对讲机模块收发状态
	public static final int MODULE_STATUS_SENDING					= 0x01;// 正在接收
	public static final int MODULE_STATUS_RECEIVING 			= 0x02;// 正在发射
	public static final int MODULE_STATUS_STANDBY 				= 0x03;// 待机
		
	//DIM_INTERPHONE_SET_TONE_TYPE_PROPERTY
	public static final int TONE_TYPE_NONE 								= 1;//无亚音
	public static final int TONE_TYPE_ANALOG 							= 2;//模拟亚音
	public static final int TONE_TYPE_DIGIT 								= 3;//数字亚音
	public static final int TONE_TYPE_REVERSE_DIGIT 				= 4;//反向数字亚音
	
	
	//DIM_INTERPHONE_REQ_CMD_PROPERTY
	public static final int CMD_QUERY_SCAN_FREQ_STATUS 		= 	0;	//查询扫频状态
	public static final int CMD_QUERY_MODULE_STATUS 			= 	1;	//查询模块收发状态
	public static final int CMD_QUERY_SIGNAL_STRENGTH 		= 	2;	//查询信号强度
	public static final int CMD_QUERY_INIT_STATUS 				= 	3;	//查询初始化状态
	public static final int CMD_QUERY_LOCAL_NUMBER 			= 	4;	//查询本机号码
	public static final int CMD_QUERY_VERSION 						= 	5;	//查询版本号
	public static final int CMD_QUERY_ENCRYPTION_STATUS	= 	6;	//查询加密状态
	public static final int CMD_QUERY_CONTACTS						= 	7;//查询联系人信息
	public static final int CMD_ENABLE_POWER_ON					= 	30;//打开对讲机模块供电
	public static final int CMD_ENABLE_POWER_OFF					= 	31;//关闭对讲机模块供电
	
	//DIM_RANGING_STATUS_PROPERTY
	public static final int RANGING_LASER_OFF					=	0; //激光关闭
	public static final int RANGING_LASER_ON					=  1; //激光打开
	
	//DIM_RANGING_RET_ERROR_CODE_PROPERTY
	public static final int RANGING_ERR_CODE_LOW_BATTERY 		= 1;//电池电压过低
	public static final int RANGING_ERR_CODE_HIGH_BATTERY 		= 2;//电池电压过高
	public static final int RANGING_ERR_CODE_LOW_TEMP 				= 3;//温度过低
	public static final int RANGING_ERR_CODE_HIGH_TEMP 				= 4;//温度过高
	public static final int RANGING_ERR_CODE_DATA_OVERFLOW 	= 5;//数据溢出
	public static final int RANGING_ERR_CODE_DATA_ERROR 			= 6;//数据错误
	public static final int RANGING_ERR_CODE_HIGHT_BRIGHTNESS = 7;//环境光线过强
	public static final int RANGING_ERR_CODE_LOW_SIGNAL			= 8;//信号太弱
	public static final int RANGING_ERR_CODE_HIGHT_SIGNAL 		= 9;//信号太强
	public static final int RANGING_ERR_CODE_HARDWARE_ERROR 	= 10;//硬件错误
	public static final int RANGING_ERR_CODE_INNER_ERROR_L 		= 11;//内部错误1
	public static final int RANGING_ERR_CODE_INNER_ERROR_H 		= 12;//内部错误2
	public static final int RANGING_ERR_CODE_INNER_ERROR_D		= 13;//内部错误3
	public static final int RANGING_ERR_CODE_FLASH_ERROR			= 14;//Flash存储错误
	public static final int RANGING_ERR_CODE_HIGH_ROCK				= 15;//Rock晃动过大
	
	//DIM_RANGING_REQ_CMD_PROPERTY
	public static final int CMD_OPEN_LASER						= 0; //请求打开激光
	public static final int CMD_CLOSE_LASER						= 1; //请求关闭激光
	public static final int CMD_RANGING							= 2; //请求测距
	public static final int CMD_REQ_MODULE_INFO			= 3; //请求模块温度以及供电情况
	public static final int CMD_ENABLE_LASER_POWER		= 10; //打开激光供电
	public static final int CMD_DISABLE_LASER_POWER		= 11; //关闭激光供电
	
	//DIM_HARDWARE_FUNCTON_SWITCH_PROPERTY
	public static final int FUN_ID_ALL_OFF							= 0;	//对讲机与激光均关闭
	public static final int FUN_ID_INTERPHONE						= 1;	//切换到对讲机
	public static final int FUN_ID_RANGING							= 2;	//切换到测距
	public static final int FUN_ID_FILLLIGHT_ON					= 9;	//补光灯开
	public static final int FUN_ID_FILLLIGHT_OFF					= 10;	//补光灯关
	
	//DIM_RANGING_LASER_SERVICE_STATUS_PROPERTY
	public static final int LASER_SERVICE_STATE_OFF						= 0;//激光服务已关闭
	public static final int LASER_SERVICE_STATE_TURNING_ON		= 1;//激光服务正在启动
	public static final int LASER_SERVICE_STATE_TURNING_OFF		= 2;//激光服务正在退出
	public static final int LASER_SERVICE_STATE_ON						= 3;//激光服务正常工作
	
	//DIM_INTERPHONE_SERVICE_STATUS_PROPERTY
	public static final int INTERPHONE_SERVICE_STATE_OFF						= 4;//对讲机服务已关闭
	public static final int INTERPHONE_SERVICE_STATE_TURNING_ON		= 5;//对讲机服务正在启动
	public static final int INTERPHONE_SERVICE_STATE_TURNING_OFF		= 6;//对讲机服务正在退出
	public static final int INTERPHONE_SERVICE_STATE_ON						= 7;//对讲机服务正常工作

	//DIM_MODULE_CONNECT_STATUS_PROPERTY
	public static final int MODULE_STATUS_DISCONNECTED				= 0;//模块未连接
	public static final int MODULE_STATUS_CONNECTED					= 1;//模块已连接
	
}
