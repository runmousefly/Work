/*
 *    Copyright (c) 2016. Mct Technologies, Inc. All Rights Reserved.
 */

package com.mct;

import com.android.internal.statusbar.StatusBarIcon;

import android.service.notification.INotificationListener;

public final class DeviceInterfaceProperties
{
	//对讲机模块属性功能
	
	public static final int DIM_INTERPHONE_CHANNEL_PROPERTY 								= 1;	//信号切换(1-16)
	
	public static final int DIM_INTERPHONE_RECEIVE_VOLUME_PROPERTY					= 2;	//模块接收音量(1-9)
		
	public static final int DIM_INTERPHONE_SCAN_FREQ_STATUS_PROPERTY				= 3;	//扫频开关
			
	public static final int DIM_INTERPHONE_DEVICE_STATUS_PROPERTY						= 4;	//模块收发状态应答
	
	public static final int DIM_INTERPHONE_SIGNAL_STRENGTH_PROPERTY					= 5;	//信号强度
	
	public static final int DIM_INTERPHONE_CALLOUT_PROPERTY									= 10;	//开始/关闭主呼{开关，呼叫类型，号码}
		
	public static final int DIM_INTERPHONE_CALL_STATUS_PROPERTY							= 11;	//呼叫状态上报
	
	public static final int DIM_INTERPHONE_CALLING_CONTACTS_INFO_PROPERTY		= 12;	//当前呼叫的联系人信息{呼叫类型，号码}
		
	public static final int DIM_INTERPHONE_SEND_MESSAGE_PROPERTY						= 13;	//发送短信
		
	public static final int DIM_INTERPHONE_NEW_MESSAGE_PROPERTY						= 14;	//收到新短信
	
	public static final int DIM_INTERPHONE_ALARM_STATUS_PROPERTY						= 20;	//紧急报警
		
	public static final int DIM_INTERPHONE_EXTERN_REQ_CHECK_PROPERTY				= 21;//增强功能-对讲机检测
	
	public static final int DIM_INTERPHONE_EXTERN_CALL_PROMPT_PROPERTY			= 22;//增强功能-呼叫提示
	
	public static final int DIM_INTERPHONE_EXTERN_REMOTE_MONITOR_PROPERTY	= 23;//增强功能-远程监听
	
	public static final int DIM_INTERPHONE_EXTERN_REMOTE_KILL_PROPERTY			= 24;//增强功能-远程遥毙
	
	public static final int DIM_INTERPHONE_EXTERN_ACTIVE_PROPERTY						= 25;//增强功能-对讲机激活
		
	public static final int DIM_INTERPHONE_MIC_GAIN_PROPERTY								= 30;//MIC增益(0-16)
		
	public static final int DIM_INTERPHONE_POWER_MODE_PROPERTY						= 31;//省电模式
		
	public static final int DIM_INTERPHONE_COMN_FREQ_PROPERTY							= 32;//设置模块收发频率
		
	public static final int DIM_INTERPHONE_RELAY_MODE_PROPERTY							= 33;//中继/脱网模式
		
	public static final int DIM_INTERPHONE_SQUELCH_LEVEL_PROPERTY						= 34;//静噪等级
		
	public static final int DIM_INTERPHONE_TONE_TYPE_PROPERTY							= 40;//亚音类型{接收亚音类型,发射亚音类型}
		
	//模拟亚音，频率范围(0-50);数字亚音，频率范围(0-83)
	public static final int DIM_INTERPHONE_TONE_FREQ_PROPERTY							= 41;//亚音频率{接收亚音频率,发射亚音频率}
		
	//只有在模拟通道下有效
	public static final int DIM_INTERPHONE_MONITOR_STATUS_PROPERTY					= 42;//监听开关
		
	public static final int DIM_INTERPHONE_SENDING_POWER_PROPERTY					= 43;//发射功率
		
	public static final int DIM_INTERPHONE_CALL_CONTACT_PROPERTY						= 50;//设置联系人{CallType,CallNumber}
		
	public static final int DIM_INTERPHONE_ENCRYPTION_STATUS_PROPERTY				= 60;//加密开关
		
	public static final int DIM_INTERPHONE_INIT_STATUS_PROPERTY							= 61;//初始化状态
		
	public static final int DIM_INTERPHONE_CONTACTS_INFO_PROPERTY						= 62;//查询返回的联系人信息
	
	public static final int DIM_INTERPHONE_DEVICE_NUMBER_PROPERTY						= 63;//本机号
	
	public static final int DIM_INTERPHONE_VERSION_PROPERTY									= 64;//模块软件版本
		
	public static final int DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY					= 99;//错误代码返回{PropID,errID}
	
	public static final int DIM_INTERPHONE_REQ_CMD_PROPERTY								= 100;//对讲机请求命令
	
	public static final int DIM_INTERPHONE_SERVICE_STATUS_PROPERTY					= 999;//对讲机服务状态

	//测距模块属性功能
	public static final int DIM_RANGING_LASER_STATUS_PROPERTY								= 1000;//测距模块状态(暂不支持)
	
	public static final int DIM_RANGING_DISTANCE_PROPERTY										= 1001;//测距上报的距离属性
	
	public static final int DIM_RANGING_TEMP_PROPERTY											= 1002;//测距上报的温度属性
	
	public static final int DIM_RANGING_VOLTAGE_PROPERTY										= 1003;//测距上报的电压属性
	
	public static final int DIM_RANGING_REQ_CMD_PROPERTY										= 1004;//测距模块请求命令
	
	public static final int DIM_RANGING_RET_ERROR_CODE_PROPERTY						= 1005;//测距错误码
	
	public static final int DIM_LASER_SERVICE_STATUS_PROPERTY								= 1999;//激光测距服务状态

	public static final int DIM_HARDWARE_FUNCTON_SWITCH_PROPERTY					= 2000;//硬切接口
	
	public static final int DIM_MODULE_CONNECT_STATUS_PROPERTY							= 2001;//模块连接状态

}
