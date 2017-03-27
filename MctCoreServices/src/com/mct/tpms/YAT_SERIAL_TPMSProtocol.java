package com.mct.tpms;

public class YAT_SERIAL_TPMSProtocol
{
	public static final int YAT_TPMS_CMD_READY 							= 0x11; 	// 握手信号
	public static final int YAT_TPMS_CMD_ALARM_PARAM				= 0x62; 	// 报警参数
	public static final int YAT_TPMS_CMD_TIRE_DATA 					= 0x63; 	// 轮胎数据传输
	public static final int YAT_TPMS_CMD_DELETE_TIRE_DATA 	= 0x65; 	// 删除轮胎ID
	public static final int YAT_TPMS_CMD_STUDY_TIRE_DATA 		= 0x66; 	// 学习轮胎数据

}
