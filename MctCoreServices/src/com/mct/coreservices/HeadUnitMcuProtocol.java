package com.mct.coreservices;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class HeadUnitMcuProtocol
{
		//APU->MCU
		public static final int AM_CMD_SYS_INIT_OK 					= 0x01; 	// ARM Ready
		public static final int AM_CMD_REQ_VERSION					= 0x02; // 请求初始化数据
		public static final int AM_CMD_UI_MODE 						= 0x03; // 报告UI状态
		public static final int AM_CMD_MEDIA_MODE					= 0x04; // 报告当前媒体源
		public static final int AM_CMD_MUTE								= 0x05; // 静音
		public static final int AM_CMD_REQ_VOLUME					= 0x06; // 请求音量大小
		public static final int AM_CMD_SET_VOLUME					= 0x07; // 设置音量大小
		public static final int AM_CMD_SYS_POWER_OFF				= 0x08; // 进入休眠流程
		public static final int AM_CMD_MEDIA_INFO					= 0x09; // 报告媒体信息
		public static final int AM_CMD_BT_PHONE_INFO				= 0x0A; // 报告蓝牙电话信息
		public static final int AM_CMD_EQ_INFO							= 0x0B; // 设置EQ信息
		public static final int AM_CMD_AUDIO_BALANCE_INFO		= 0x0C; // 设置喇叭平衡信息
		public static final int AM_CMD_INIT_DEFAULT_VOLUME	= 0x0D;// 设置默认开机音量
		public static final int AM_CMD_SYS_ACK							= 0x0F; // 同步ACK
		
		public static final int AM_CMD_SYS_TIME						= 0x10; // 系统时间
		public static final int AM_CMD_REQ_REVERSE_STATUS	= 0x11; // 请求倒车状态
		public static final int AM_CMD_REQ_ACC_STATUS			= 0x12; // 请求ACC状态
		public static final int AM_CMD_REQ_ILL_STATUS				= 0x13; // 请求大灯状态
		public static final int AM_CMD_PLAY_GPS_SOUND			= 0x14; // GPS播报,模拟通道出声音
		public static final int AM_CMD_MEDIA_PLAY_STATUS		= 0x15; // 报告播放状态
		public static final int AM_CMD_SEND_VIRTUAL_KEY			= 0x16; // 虚拟按键键值
		public static final int AM_CMD_SEND_KEY_SPEAKER		= 0x17; // 按键音
		public static final int AM_CMD_PLAY_MEDIA_SOUND		= 0x18; // ARM播放声音
		public static final int AM_CMD_REQ_GPS_SET_INFO			= 0x19; // 请求GPS设置信息
		public static final int AM_CMD_SET_GPS_INFO					= 0x1A; // 请求设置GPS信息
		public static final int AM_CMD_SET_OTHER_INFO			= 0x1B; // 报告其他设置项信息
		public static final int AM_CMD_REQ_OTHER_SET_INFO	= 0x1C; // 请求其他设置项信息
		public static final int AM_CMD_REQ_ENTER_RECOVERY	= 0x1D; // 通知MCU进入恢复模式		
		public static final int AM_CMD_SET_ACK							= 0x1F; // 设置ACK
		
		public static final int AM_CMD_RADIO_SWITCH				= 0x20; // 收音机开关
		public static final int AM_CMD_RADIO_ACTION				= 0x21; // 收音机功能操作
		//public static final int AM_CMD_RADIO_SET_FREQ			= 0x22; // 设置频率
		public static final int AM_CMD_RADIO_SET_REGION		= 0x24; // 设置收音区域
		public static final int AM_CMD_RADIO_REQ_REGIION		= 0x25; // 请求收音区域
		public static final int AM_CMD_RADIO_ACK						= 0x2F; // 收音ACK
		
		public static final int AM_CMD_STEER_STUDY_STATUS	= 0x30; // 方控学习状态
		public static final int AM_CMD_STEER_KEY_FUN_ID			= 0x31; // 方控学习键值对应的功能ID
		public static final int AM_CMD_STEER_STUDY_ACTION	= 0x32; // 方控学习操作
		public static final int AM_CMD_STEER_STUDY_ACK			= 0x3F; // 方控学习ACK
	
		public static final int AM_CMD_CAN_INFO						= 0x40; // CAN数据
		public static final int AM_CMD_CAN_CONFIG					= 0x41; // CAN盒兼容协议设置
		public static final int AM_CMD_DVR_INFO						= 0x4A; // DVR数据
		public static final int AM_CMD_DVR_CONFIG					= 0x4B; // DVR类型
		public static final int AM_CMD_TPMS_INFO						= 0x4D; // TPMS数据
		public static final int AM_CMD_TPMS_CONFIG					= 0x4E; // TPMS类型
		public static final int AM_CMD_CAN_ACK							= 0x4F; // CAN应答ACK
		
		public static final int AM_CMD_UPGRADE_START				= 0x50; // 开始升级
		public static final int AM_CMD_UPGRADE_INIT				= 0x51;	// 升级初始化
		public static final int AM_CMD_UPGRADE_FRAME				= 0x52;	// 发送升级数据帧
		public static final int AM_CMD_UPGRADE_ACK					= 0x5F;	// 更新ACK
		
		public static final int AM_CMD_REQ_BACKLIGHT_BRIGHTNESS 		= 0x60;//请求背光亮度
		public static final int AM_CMD_SET_BACKLIGHT_BRIGHTNESS 		= 0x61;//设置背光亮度
		public static final int AM_CMD_BACKLIGHT_BRIGHTNESS_SWITCH 	= 0x62;//背光开关
		public static final int AM_CMD_REQ_BACKLIGHT_STATUS 				= 0x63;//请求背光状态
		public static final int AM_CMD_SET_SOURCE_GAIN							= 0x6c;//设置源增益
		public static final int AM_CMD_REQ_SOURCE_GAIN							= 0x6d;//请求源增益
		public static final int AM_CMD_REQ_RESET 										= 0x6e; //请求重启
		
		//MCU-APU
		public static final int MA_CMD_INIT_DATA 						= 0x81; // 返回初始化数据
		public static final int MA_CMD_MCU_VERSION 				= 0x82; // 返回版本号
		public static final int MA_CMD_MUTE_STATUS					= 0x85; // 返回mute状态
		public static final int MA_CMD_VOLUME_INFO					= 0x86; // 返回音量信息
		public static final int MA_CMD_CAR_ACC 							= 0x8A; // ACC状态
		public static final int MA_CMD_CAR_REVERSE 					= 0x8B; // 倒车状态
		public static final int MA_CMD_CAR_BRAKE 						= 0x8C; // 手刹状态
		public static final int MA_CMD_CAR_ILL 							= 0x8D; // 大灯状态
		public static final int MA_CMD_TURN_LIGHT 					= 0x8E; // 转向灯状态
		public static final int MA_CMD_SYS_ACK 							= 0x8F; // 同步ACK
		
		public static final int MA_CMD_GPS_SET_INFO					= 0x9A; // GPS设置信息 
		public static final int MA_CMD_OTHER_SET_INFO			= 0x9B; // 其它设置信息 
		public static final int MA_CMD_EQ_SET_INFO					= 0x9C; // EQ设置信息
		public static final int MA_CMD_AUDIO_BALANCE_INFO		= 0x9D; // 声场平衡信息
		public static final int MA_CMD_SET_ACK 							= 0x9F; // 设置ACK
		
		public static final int MA_CMD_RADIO_INIT_DATA 			= 0xA0; // 收音机初始化数据
		public static final int MA_CMD_RADIO_MAIN_DATA			= 0xA1; // 收音机主频
		public static final int MA_CMD_RADIO_PRESET_DATA		= 0xA2; // 收音机预存频率列表
		public static final int MA_CMD_RADIO_FLAG					= 0xA3; // 收音机标志位
		public static final int MA_CMD_RADIO_REGION				= 0xA4; // 收音机区域
		public static final int MA_CMD_RDS_INFO						= 0xA5; // RDS标志信息
		public static final int MA_CMD_PS_INFO							= 0xA6; // RDS-PS信息
		public static final int MA_CMD_SWITCH_TO_RADIO			= 0xA7; // 请求切换到收音
		public static final int MA_CMD_RADIO_AUDIO_STATUS	= 0xA8; // 收音声音开关
		public static final int MA_CMD_RADIO_ACK						= 0xAF; // 收音ACK
		
		public static final int MA_CMD_STEER_STUDY_STATUS	= 0xB0; // 方控学习状态
		public static final int MA_CMD_STEER_KEY_STATUS			= 0xB1; // 学习后的KEY状态
		public static final int MA_CMD_STEER_KEY_INFO				= 0xB2; // 学习后的KEY信息
		public static final int MA_CMD_STEER_KEY2					= 0xB4;	// 方控键上报
		public static final int MA_CMD_STEER_STUDY_ACK			= 0xBF; // 方控学习ACK
		
		public static final int MA_CMD_USER_KEY2						= 0xC1; // 上报键值
		
		public static final int MA_CMD_CAN_INFO						= 0xD0; // 上报Can数据
		public static final int MA_CMD_DVR_INFO						= 0xDA; // 上报DVR数据
		public static final int MA_CMD_TPMS_INFO						= 0xDB; // 上报TPMS数据
		public static final int MA_CMD_CAN_ACK							= 0xDF; // Can数据ACK
		
		public static final int MA_CMD_UPGRADE_STATUS			= 0xE0; // MCU 升级状态，处于升级状态还是非升级状态
		public static final int MA_CMD_REQ_UPGRADE_FRAME 	= 0xE1; // MCU请求升级帧数据
		public static final int MA_CMD_UPGRADE_COMPLETE 		= 0xE2; // MCU升级完成
		public static final int MA_CMD_UPGRADE_ACK 				= 0xEF; // 升级ACK
		
		public static final int MA_CMD_BACKLIGHT_BRIGHTNESS = 0xF0;//上报亮度值
		public static final int MA_CMD_BACKLIGHT_STATUS 		 = 0xF1;//上报背光状态
		public static final int MA_CMD_SOURCE_GAIN_INFO		= 0xFE;//源增益信息

		//应答ACK类型
		public static final int ACK_TYPE_SYS								= 0; // SYS_ACK
		public static final int ACK_TYPE_SET								= 1; // SET_ACK
		public static final int ACK_TYPE_RADIO							= 2; // RADIO_ACK
		public static final int ACK_TYPE_STEER_STUDY				= 3; // STEER_STUDY_ACK
		public static final int ACK_TYPE_CAN								= 4; // CAN_ACK
		public static final int ACK_TYPE_UPGRADE						= 5; // UPGRADE_ACK
		
		//GPS SET Info
		public static final int GPS_SET_MONITOR_STATUS			= 0; // GPS监听状态
		public static final int GPS_SET_MIX_STATUS					= 1; // GPS混音状态
		public static final int GPS_SET_MIX_LEVEL						= 2; // GPS混音比例
		
		//Volume Type
		public static final int VOLUME_TYPE_MEDIA						= 0; // 媒体音量
		public static final int VOLUME_TYPE_BT_PHONE				= 1; // 蓝牙电话音量
		public static final int VOLUME_TYPE_NAVIGATION			= 2; // 导航音量
		
		//EQ Sub Type
		public static final int EQ_BASS											= 0; // 低音
		public static final int EQ_ALTO										= 1; // 中音
		public static final int EQ_TREBLE										= 2; // 高音
		public static final int EQ_SUBWOOFER								= 3; // 重低音
		public static final int EQ_CENTER_FREQ_INFO					= 4; // 中心频点
		public static final int EQ_LOUDNESS_SWITCH					= 5; // 等响度开关
		public static final int EQ_PRESET_MODE							= 6; // 预设模式
		
		
		public static final int KEY_ACTION_UP								= 0x00;
		public static final int KEY_ACTION_DOWN						= 0x01;
		public static final int KEY_ACTION_SCROLL						= 0x02;
		public static final int KEY_ACTION_UNKNOWN					= 0xFF;
		
		//Radio Action
		public static final int RADIO_ACTION_INIT						= 0x01; // 请求收音初始化数据
		public static final int RADIO_ACTION_STOP						= 0x02; // 退出收音命令
		public static final int RADIO_ACTION_FM_AM_SWITCH	= 0x03; // FM与AM直接切换
		public static final int RADIO_ACTION_FM1						= 0x04; // 切换到FM1
		public static final int RADIO_ACTION_FM2						= 0x05; // 切换到FM2
		public static final int RADIO_ACTION_FM3						= 0x06; // 切换到FM3
		public static final int RADIO_ACTION_AM1						= 0x07; // 切换到AM1
		public static final int RADIO_ACTION_AM2						= 0x08; // 切换到AM2
		public static final int RADIO_ACTION_BAND_FM				= 0x09; // 切换FM
		public static final int RADIO_ACTION_BAND_AM				= 0x0A; // 切换AM
		public static final int RADIO_ACTION_BAND						= 0x0B; // 切换Band
		public static final int RADIO_ACTION_LP1						= 0x0C; // 选台1
		public static final int RADIO_ACTION_LP2						= 0x0D; // 选台2
		public static final int RADIO_ACTION_LP3						= 0x0E; // 选台3
		public static final int RADIO_ACTION_LP4						= 0x0F; // 选台4
		public static final int RADIO_ACTION_LP5						= 0x10; // 选台5
		public static final int RADIO_ACTION_LP6						= 0x11; // 选台6
		public static final int RADIO_ACTION_SP1						= 0x12; // 保存索引台1
		public static final int RADIO_ACTION_SP2						= 0x13; // 保存索引台2
		public static final int RADIO_ACTION_SP3						= 0x14; // 保存索引台3
		public static final int RADIO_ACTION_SP4						= 0x15; // 保存索引台4
		public static final int RADIO_ACTION_SP5						= 0x16; // 保存索引台5
		public static final int RADIO_ACTION_SP6						= 0x17; // 保存索引台6
		public static final int RADIO_ACTION_NEXT_PRE_STATION	= 0x18; // 切换下一个预存台
		public static final int RADIO_ACTION_PRIO_PRE_STATION	= 0x19; // 切换上一个预存台
		public static final int RADIO_ACTION_STEP_UP				= 0x1A; // 向上微调
		public static final int RADIO_ACTION_STEP_DOWN			= 0x1B; // 向下微调
		public static final int RADIO_ACTION_SEARCH_UP			= 0x1C; // 向上搜索
		public static final int RADIO_ACTION_SEARCH_DOWN		= 0x1D; // 向下搜索
		public static final int RADIO_ACTION_SCAN						= 0x1E; // 电台扫描播放
		public static final int RADIO_ACTION_AUTO_SERACH		= 0x1F; // 自动搜索
		public static final int RADIO_ACTION_STEREO					= 0x20; // 设置立体音
		public static final int RADIO_ACTION_LOC						= 0x21; // 打开LOC
		public static final int RADIO_ACTION_SET_FREQ				= 0x22; // 设置频率
		public static final int RADIO_ACTION_AF							= 0x24; // 打开AF
		public static final int RADIO_ACTION_TA							= 0x25; // 打开TA
		public static final int RADIO_ACTION_PTY						= 0x26; // 请求PTY列表，保留
		public static final int RADIO_ACTION_PTY_SEL				= 0x27; // 选中PTY索引项
		public static final int RADIO_ACTION_CLEAR_PRE			= 0x28; // 清除预存电台
		public static final int RADIO_ACTION_NEW_NPRE			= 0x29; // 新增下一个预存台
		public static final int RADIO_ACTION_NEW_PPRE			= 0x2A; // 新增上一个预存台
		public static final int RADIO_ACTION_CHANGE_BAND		= 0x2B; // 设置Band
		public static final int RADIO_ACTION_POWER					= 0x2C; // 开关收音机声音
		public static final int RADIO_ACTION_STOP_SEARCH		= 0x2D; // 停止搜索
		public static final int RADIO_ACTION_STOP_SENSITIVITY= 0x2E; // 发送停台灵敏度
		public static final int RADIO_ACTION_SET_BAND_AND_FREQ = 0x2F; // 设置频点与波段
		
		//权限定义
		public static final int PROPERITY_PERMISSON_NO				= VehiclePropertyConstants.PROPERITY_PERMISSON_NO;			//NOTIFY ONLY
		public static final int PROPERITY_PERMISSON_GET				= VehiclePropertyConstants.PROPERITY_PERMISSON_GET; 			// GET ONLY
		public static final int PROPERITY_PERMISSON_SET				= VehiclePropertyConstants.PROPERITY_PERMISSON_SET; 			// SET ONLY
		public static final int PROPERITY_PERMISSON_GET_SET 	= VehiclePropertyConstants.PROPERITY_PERMISSON_GET_SET;	// GET_SET

		//所有支持的属性集
		public static final int[] HEADUNIT_MCU_PROPERITIES = new int[] { 
				VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_SYSTEM_TIME_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_REVERSE_DETECT_MODE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY,
				VehicleInterfaceProperties.VIM_MCU_BRAKE_DETECT_MODE_PROPERTY,
				VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY,
				VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_DETECT_MODE_PROPERTY,
				VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY,
				VehicleInterfaceProperties.VIM_MCU_TURN_LEFT_LIGHT_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY, 
				
				
				VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY,
				VehicleInterfaceProperties.VIM_MCU_RADIO_ST_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_LOC_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_TP_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_TA_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_AF_STATUS_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_PS_INFO_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_TYPE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_LIST_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_FREQ_LIST_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_SOURCE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_PLAY_TIME_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_STARTUP_MODE_PROPERTYE, 
				VehicleInterfaceProperties.VIM_MCU_ACC_STAT_PROPERTYE, 
				VehicleInterfaceProperties.VIM_MCU_LAST_POWER_OFF_SOURCE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_NAV_AUDIO_CHANNEL_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_STATE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_KEY_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_FUNC_ID_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_DOWN_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY, 
				VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY,
				VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY
		};
		
		// 属性读写权限表
		public static final SparseIntArray HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE = new SparseIntArray();
		static
		{
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, PROPERITY_PERMISSON_GET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_SYSTEM_TIME_PROPERTY, PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REVERSE_DETECT_MODE_PROPERTY, PROPERITY_PERMISSON_GET_SET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY, PROPERITY_PERMISSON_GET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BRAKE_DETECT_MODE_PROPERTY,PROPERITY_PERMISSON_GET_SET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY, PROPERITY_PERMISSON_GET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_DETECT_MODE_PROPERTY, PROPERITY_PERMISSON_GET_SET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, PROPERITY_PERMISSON_GET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_TURN_LEFT_LIGHT_STATUS_PROPERTY,  PROPERITY_PERMISSON_GET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY, PROPERITY_PERMISSON_GET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,  PROPERITY_PERMISSON_GET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY, PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY , PROPERITY_PERMISSON_SET);
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY, PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY,  PROPERITY_PERMISSON_GET_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_ST_STATUS_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_LOC_STATUS_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_TP_STATUS_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_TA_STATUS_PROPERTY, PROPERITY_PERMISSON_GET);  
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_AF_STATUS_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_PS_INFO_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_TYPE_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_LIST_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_FREQ_LIST_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_SOURCE_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY, PROPERITY_PERMISSON_SET);  
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_PLAY_TIME_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STARTUP_MODE_PROPERTYE,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_ACC_STAT_PROPERTYE,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_LAST_POWER_OFF_SOURCE_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_NAV_AUDIO_CHANNEL_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_STATE_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_KEY_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_FUNC_ID_PROPERTY,  PROPERITY_PERMISSON_NO); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_DOWN_PROPERTY,  PROPERITY_PERMISSON_NO); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY,  PROPERITY_PERMISSON_SET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY,  PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY, PROPERITY_PERMISSON_GET); 
			HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, PROPERITY_PERMISSON_SET); 
		}

		// 属性值类型表
		public static final SparseIntArray HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE = new SparseIntArray();
		static
		{
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_FLOAT);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
		
			
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_SYSTEM_TIME_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REVERSE_DETECT_MODE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BRAKE_DETECT_MODE_PROPERTY,VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_DETECT_MODE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_TURN_LEFT_LIGHT_STATUS_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_TURN_RIGHT_LIGHT_STATUS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY , VehiclePropertyConstants.DATA_TYPE_INTEGER);
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_ST_STATUS_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_LOC_STATUS_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_TP_STATUS_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_TA_STATUS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER);  
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_AF_STATUS_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_PS_INFO_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_TYPE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_LIST_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_FREQ_LIST_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_SOURCE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY, VehiclePropertyConstants.DATA_TYPE_STRING);  
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_PLAY_TIME_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_ID3_STRING_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STARTUP_MODE_PROPERTYE,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_ACC_STAT_PROPERTYE,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_LAST_POWER_OFF_SOURCE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_NAV_AUDIO_CHANNEL_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_STATE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_KEY_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_FUNC_ID_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_DOWN_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_STRING); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY,  VehiclePropertyConstants.DATA_TYPE_INTEGER); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY, VehiclePropertyConstants.DATA_TYPE_FLOAT); 
			HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.put(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, VehiclePropertyConstants.DATA_TYPE_INTEGER); 
		}
		
		public static final SparseIntArray SWC_KEY_TO_USER_KEY_TABLE = new SparseIntArray();
		static
		{
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_SOURCE, VehiclePropertyConstants.USER_KEY_SOURCE);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_SPEECH, VehiclePropertyConstants.USER_KEY_VOICE);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_NAVIGATION, VehiclePropertyConstants.USER_KEY_NAVI);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_TUNER, VehiclePropertyConstants.USER_KEY_RADIO);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_MUSIC, VehiclePropertyConstants.USER_KEY_MUSIC);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_VIDEO, VehiclePropertyConstants.USER_KEY_VIDEO);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_BT_PHONE, VehiclePropertyConstants.USER_KEY_BLUETOOTH);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_BT_MUSIC, VehiclePropertyConstants.USER_KEY_BT_MUSIC);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_PLAY_PAUSE, VehiclePropertyConstants.USER_KEY_PLAY_PAUSE);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_PLAY_PREV, VehiclePropertyConstants.USER_KEY_PLAY_PREV);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_PLAY_NEXT, VehiclePropertyConstants.USER_KEY_PLAY_NEXT);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_PHONE_ACCEPT, VehiclePropertyConstants.USER_KEY_PHONE_ACCEPT);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_PHONE_HANGUP, VehiclePropertyConstants.USER_KEY_PHONE_HANGUP);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_MUTE, VehiclePropertyConstants.USER_KEY_MUTE);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_VOLUME_DOWN, VehiclePropertyConstants.USER_KEY_VOLUME_DOWN);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_VOLUME_UP, VehiclePropertyConstants.USER_KEY_VOLUME_UP);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_HOME, VehiclePropertyConstants.USER_KEY_HOME);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_BACK, VehiclePropertyConstants.USER_KEY_BACK);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_POWER, VehiclePropertyConstants.USER_KEY_POWER);
			SWC_KEY_TO_USER_KEY_TABLE.put(VehiclePropertyConstants.STEER_STUDY_FUNC_ID_VEHICLE_CONDITION, VehiclePropertyConstants.USER_KEY_VEHICLE_CONDITION);
		}

		public static int getProperityDataType(int prop)
		{
			return (HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.indexOfKey(prop) >= 0) ? HEADUNIT_MCU_PROPERITY_DATA_TYPE_TABLE.get(prop) : -1;
		}

		public static int getProperityPermission(int prop)
		{
			return (HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.indexOfKey(prop) >= 0) ? HEADUNIT_MCU_PROPERITY_PERMISSION_TABLE.get(prop) : -1;
		}
		
		public static int swcKeyToUserKey(int swcKey)
		{
			return (SWC_KEY_TO_USER_KEY_TABLE.indexOfKey(swcKey) >= 0) ? SWC_KEY_TO_USER_KEY_TABLE.get(swcKey):-1;
		}
}
