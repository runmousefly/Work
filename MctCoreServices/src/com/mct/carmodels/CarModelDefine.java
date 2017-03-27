package com.mct.carmodels;

import com.mct.VehiclePropertyConstants;

public class CarModelDefine
{
	/*
	 * Can盒类型
	 */
	public static final short CAN_BOX_NONE    									= -1;//无CAN盒(通用机)
	public static final short CAN_BOX_RZC										= 0;//睿智诚
	public static final short CAN_BOX_SS											= 1;//尚摄
	public static final short CAN_BOX_XP											= 2;//欣普
		
	
	/*
	 * 车型定义
	 */
	
	//通用机
	public static final short CAR_MODEL_NONE    								= -1;//无车型(通用机)
	
	//大众系列(0-49)
	public static final short CAR_MODEL_VOLKSWAGEN_MAGOTAN    = 0;//大众迈腾
	public static final short CAR_MODEL_VOLKSWAGEN_SAGITAR    	= 1;//大众速腾
	public static final short CAR_MODEL_VOLKSWAGEN_GOLF7   		= 2;//大众高尔夫7
	public static final short CAR_MODEL_VOLKSWAGEN_POLO    		= 3;//大众波罗
	public static final short CAR_MODEL_VOLKSWAGEN_OCTAVIA    	= 4;//大众明锐
	public static final short CAR_MODEL_VOLKSWAGEN_17MAGOTAN= 5;//17款迈腾
	
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
	public static final short CAR_MODEL_HONDA_12CRV					= 61;//12CRV
	
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
	public static final short CAR_MODEL_NISSAN_VENUCIA_T90		= 114;//启辰T90
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
	public static final short CAR_MODEL_GM_GL8								= 204;//GL8
	public static final short CAR_MODEL_GM_AVEO							= 205;//爱唯欧
	public static final short CAR_MODEL_GM_REGAL							= 206;//君威
	public static final short CAR_MODEL_GM_ENCORE						= 207;//昂科拉
	
	//标志雪铁龙系列(250-299)
	public static final short CAR_MODEL_PEUGEOT_16_308				= 250;//16款308
	public static final short CAR_MODEL_PEUGEOT_14_408				= 251;//14款408
	public static final short CAR_MODEL_PEUGEOT_ELYSEE				= 252;//爱丽舍
	public static final short CAR_MODEL_PEUGEOT_301					= 253;//301
	public static final short CAR_MODEL_PEUGEOT_2008					= 254;//2008
	public static final short CAR_MODEL_PEUGEOT_3008					= 255;//3008
	public static final short CAR_MODEL_PEUGEOT_C4L					= 256;//C4L
	public static final short CAR_MODEL_PEUGEOT_DS5LS					= 257;//DS5LS
	public static final short CAR_MODEL_PEUGEOT_C3XL					= 258;//C3XL
	public static final short CAR_MODEL_PEUGEOT_SEGA					= 259;//世嘉
	public static final short CAR_MODEL_PEUGEOT_4008					= 260;//4008

	//韩系(300-349)
	public static final short CAR_MODEL_KOREA_IX35						= 300;//IX35
	public static final short CAR_MODEL_KOREA_IX45						= 301;//IX45
	public static final short CAR_MODEL_KOREA_SONATA8				= 302;//索纳塔8
	public static final short CAR_MODEL_KOREA_SONATA9				= 303;//索纳塔9
	public static final short CAR_MODEL_KOREA_SORRENTO				= 304;//索兰托
	public static final short CAR_MODEL_KOREA_KIA_K5					= 305;//起亚K5
	public static final short CAR_MODEL_KOREA_KIA_KX5					= 306;//起亚KX5
	public static final short CAR_MODEL_KOREA_16MISTRA				= 307;//16名图
	public static final short CAR_MODEL_KOREA_KIA_16ELANTRA		= 308;//16领动
	public static final short CAR_MODEL_KOREA_KIA_16K3				= 309;//16K3
	
	//福特车系系列(350-399)
	public static final short CAR_MODEL_FORD_12_FUCOS				= 350;//2012款福克斯
	public static final short CAR_MODEL_FORD_13_KUGA					= 351;//2013款翼虎
	public static final short CAR_MODEL_FORD_13_ECO_SPORT		= 352;//2013款翼博
	public static final short CAR_MODEL_FORD_13_FIESTA				= 353;//2013款嘉年华
	public static final short CAR_MODEL_FORD_15_FUCOS				= 354;//2015款福克斯
	public static final short CAR_MODEL_FORD_17_KUGA					= 355;//2017款翼虎
	

	//奔腾车系
	public static final short CAR_MODEL_BESTURN_16_B50				= 400;//2016款奔腾B50
	public static final short CAR_MODEL_BESTURN_X80					= 405;//奔腾X80
	
	//广汽传祺车系
	public static final short CAR_MODEL_TRUMPCH_GA3					= 450;
	public static final short CAR_MODEL_TRUMPCH_GA3S					= 451;
	public static final short CAR_MODEL_TRUMPCH_GA6					= 452;
	public static final short CAR_MODEL_TRUMPCH_GS4					= 453;
	public static final short CAR_MODEL_TRUMPCH_GS5					= 454;
	public static final short CAR_MODEL_TRUMPCH_XINGLANG			= 455;//广汽吉奥星朗
	
	//吉利车系
	public static final short CAR_MODEL_GEELY_EMGRAND				= 500;//吉利帝豪
	public static final short CAR_MODEL_GEELY_EMGRAND_GS			= 501;//吉利帝豪GS
	public static final short CAR_MODEL_GEELY_NL_3						= 502;//吉利博越
	
	//中华车系
	public static final short CAR_MODEL_CHINA_MOTOR_V3				= 550;//中华V3
	public static final short CAR_MODEL_CHINA_MOTOR_H3				= 551;//中华H3
	
	//宝骏车系
	public static final short CAR_MODEL_BAO_JUN_560_2015			= 600;//2015宝骏560
	public static final short CAR_MODEL_BAO_JUN_730_2014			= 601;//2014宝骏730
	public static final short CAR_MODEL_BAO_JUN_730_2017			= 602;//2017宝骏730
	
	//吉普车系
	public static final short CAR_MODEL_JEEP_FREELIGHT_2015		= 650;//2015吉普自由光
	public static final short CAR_MODEL_JEEP_LIBERTY_2016			= 651;//2016吉普自由侠
	public static final short CAR_MODEL_JEEP_COMPASS_2017			= 652;//2017吉普指南者
	
	//长城车系
	public static final short CAR_MODEL_GREAT_WALL_H2				= 700;//长城H2
	
	
	
	
	public static int[] SUPPORTED_CAN_BOX = new int[]
			{
					CAN_BOX_NONE,
					CAN_BOX_RZC//,
					//CAN_BOX_SS,
					//CAN_BOX_XP
			};
	
	//睿智诚支持的车型
	public static int[] RZC_SUPPORTED_CAR_MODEL = new int[]
			{
				//大众系列
				CAR_MODEL_VOLKSWAGEN_MAGOTAN,
				CAR_MODEL_VOLKSWAGEN_SAGITAR,
				CAR_MODEL_VOLKSWAGEN_GOLF7,
				CAR_MODEL_VOLKSWAGEN_POLO,
				CAR_MODEL_VOLKSWAGEN_OCTAVIA,
				CAR_MODEL_VOLKSWAGEN_17MAGOTAN,
				
				//本田系列
				CAR_MODEL_HONDA_15CRV_L,
				CAR_MODEL_HONDA_15CRV_H,
				CAR_MODEL_HONDA_CROSSTOUR,
				CAR_MODEL_HONDA_ACCORD_9L,
				CAR_MODEL_HONDA_ACCORD_9H,
				CAR_MODEL_HONDA_CRIDER,
				CAR_MODEL_HONDA_JADE,
				CAR_MODEL_HONDA_FIDO,
				CAR_MODEL_HONDA_WISDOM,
				CAR_MODEL_HONDA_CITY,
				CAR_MODEL_HONDA_12CRV,
				
				//日产系列
				CAR_MODEL_NISSAN_X_TRAIL,
				CAR_MODEL_NISSAN_QASHQAI,
				CAR_MODEL_NISSAN_NEW_TEANA,
				CAR_MODEL_NISSAN_TEANA,
				CAR_MODEL_NISSAN_TIIDA,
				CAR_MODEL_NISSAN_SUN,
				CAR_MODEL_NISSAN_LI_WEI,
				CAR_MODEL_NISSAN_BLUEBIRD,
				CAR_MODEL_NISSAN_INIFINITI_QX50_L,
				CAR_MODEL_NISSAN_INIFINITI_QX50_H,
				CAR_MODEL_NISSAN_MORNAO,
				CAR_MODEL_NISSAN_CIMA_L,
				CAR_MODEL_NISSAN_CIMA_H,
				CAR_MODEL_NISSAN_VENUCIA_T70,
				CAR_MODEL_NISSAN_VENUCIA_T90,
				
				//丰田系列
				CAR_MODEL_TOYOTA_RAV4,
				CAR_MODEL_TOYOTA_VIOS,
				CAR_MODEL_TOYOTA_REIZ,
				CAR_MODEL_TOYOTA_OVERBEARING,
				CAR_MODEL_TOYOTA_COROLLA,
				CAR_MODEL_TOYOTA_HIGHLANDER,
				CAR_MODEL_TOYOTA_CAMRY,
				CAR_MODEL_TOYOTA_CROWN,
				
				//通用车系
				CAR_MODEL_GM_HIDEN,
				CAR_MODEL_GM_CRUZE,
				CAR_MODEL_GM_MALIBU,
				CAR_MODEL_GM_16MALIBU_XL,
				CAR_MODEL_GM_GL8,
				CAR_MODEL_GM_AVEO	,
				CAR_MODEL_GM_REGAL,
				CAR_MODEL_GM_ENCORE,
				
				//标志雪铁龙系列
				CAR_MODEL_PEUGEOT_16_308,
				CAR_MODEL_PEUGEOT_14_408,
				CAR_MODEL_PEUGEOT_ELYSEE,
				CAR_MODEL_PEUGEOT_301,
				CAR_MODEL_PEUGEOT_2008,
				CAR_MODEL_PEUGEOT_3008,
				CAR_MODEL_PEUGEOT_C4L,
				CAR_MODEL_PEUGEOT_DS5LS,
				CAR_MODEL_PEUGEOT_C3XL,
				CAR_MODEL_PEUGEOT_SEGA,
				CAR_MODEL_PEUGEOT_4008,
				
				//韩系
				CAR_MODEL_KOREA_IX35,
				CAR_MODEL_KOREA_IX45,
				CAR_MODEL_KOREA_SONATA8,
				CAR_MODEL_KOREA_SONATA9,
				CAR_MODEL_KOREA_SORRENTO,
				CAR_MODEL_KOREA_KIA_K5,
				CAR_MODEL_KOREA_KIA_KX5,
				CAR_MODEL_KOREA_16MISTRA,
				CAR_MODEL_KOREA_KIA_16ELANTRA,
				CAR_MODEL_KOREA_KIA_16K3,
				
				//福特车系
				CAR_MODEL_FORD_12_FUCOS,
				CAR_MODEL_FORD_13_KUGA,
				CAR_MODEL_FORD_13_ECO_SPORT,
				CAR_MODEL_FORD_13_FIESTA,
				CAR_MODEL_FORD_15_FUCOS,
				CAR_MODEL_FORD_17_KUGA,
				
				//奔腾车系
				CAR_MODEL_BESTURN_16_B50,
				
				//广汽传祺车系
				CAR_MODEL_TRUMPCH_GA3,
				CAR_MODEL_TRUMPCH_GA3S,
				CAR_MODEL_TRUMPCH_GS4,
				CAR_MODEL_TRUMPCH_GS5,
				CAR_MODEL_TRUMPCH_GA6,
				CAR_MODEL_TRUMPCH_XINGLANG,
				
				//吉利帝豪
				CAR_MODEL_GEELY_EMGRAND,
				CAR_MODEL_GEELY_EMGRAND_GS,
				CAR_MODEL_GEELY_NL_3,
				
				//中华车系
				CAR_MODEL_CHINA_MOTOR_V3,
				CAR_MODEL_CHINA_MOTOR_H3,
				
				//宝骏车系
				CAR_MODEL_BAO_JUN_560_2015,
				CAR_MODEL_BAO_JUN_730_2014,
				CAR_MODEL_BAO_JUN_730_2017,
				
				//吉普车系
				CAR_MODEL_JEEP_FREELIGHT_2015,
				CAR_MODEL_JEEP_LIBERTY_2016,
				CAR_MODEL_JEEP_COMPASS_2017,
				
				//长城车系
				CAR_MODEL_GREAT_WALL_H2
			};
	
	//尚摄支持的车型
	public static int[] SS_SUPPORTED_CAR_MODEL = new int[]
			{
			};
	
	
	//欣普支持的车型
	public static int[] XP_SUPPORTED_CAR_MODEL = new int[]
			{
			};
}
