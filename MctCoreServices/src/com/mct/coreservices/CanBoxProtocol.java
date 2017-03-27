package com.mct.coreservices;

import com.mct.carmodels.CarModelDefine;
import com.mct.carmodels.DefaultCarManager;
import com.mct.carmodels.RZC_BaoJunManager;
import com.mct.carmodels.RZC_BesturnSeriesManager;
import com.mct.carmodels.RZC_ChinaMotorManager;
import com.mct.carmodels.RZC_FordSeriesManager;
import com.mct.carmodels.RZC_GMSeriesManager;
import com.mct.carmodels.RZC_GeelySeriesManager;
import com.mct.carmodels.RZC_GreatWallManager;
import com.mct.carmodels.RZC_HondaSeriesManager;
import com.mct.carmodels.RZC_JeepSeriesManager;
import com.mct.carmodels.RZC_KoreaSeriesManager;
import com.mct.carmodels.RZC_NissanSeriesManager;
import com.mct.carmodels.RZC_Nissan_Infiniti_QX50_Manager;
import com.mct.carmodels.RZC_PeugeotSeriesManager;
import com.mct.carmodels.RZC_ToyotaSeriesManager;
import com.mct.carmodels.RZC_TrumpchiSeriesManager;
import com.mct.carmodels.RZC_VolkswagenSeriesManager;
import com.mct.carmodels.RZC_Volkswagen_Golf7_Manager;
import com.mct.utils.ServiceHelper;

import android.util.Log;

public class CanBoxProtocol
{
	private final static String TAG 		= "CanBoxProtocol";
	private boolean mIsValidProtocol = false;
	private int mLengthFlag 				= 0x1;//0 = 长度在前，1 = 长度在后
	private int mHeadLength 				= 0x00;//Head长度
	private int[] mHeadTag 				= new int[]{0xFF,0xFF,0xFF};//协议头，不够补0xFF
	private int[] mBaudRate 				= new int[]{0x00,0x00,0x00,0x00};//波特率
	private int mChecksumAddLength 	= 0x1;//0 checksum计算不需要包含length;1 需要包含length
	private int mChecksumAddCmd 	= 0x1;//0 checksum计算不需要包含cmd;1 需要包含cmd
	private int mChecksumMode 		= 0x1;//0 &0xFF; 1 ^0xFF
	private int mChecksumExtern 		= 0x0;//0 checksum不需要-1;1 不需要
	private int mNeedAck 					= 0x1;//是否需要ACK
	private int mOkAck 						= 0xFF;//成功ACK
	private int mFailedAck 				= 0xF0;//失败ACK
	private int mCheckSumLength		= 0x01;//CheckSum长度
	private int mDataLengthMode		= 0x00;//0 Data部分的长度;1 除协议头以外所有的数据长度
	
	
	public CanBoxProtocol(int canBoxType,int carModel)
	{
		updateCanBoxType(canBoxType,carModel);
	}
	
	public void updateCanBoxType(int canBoxType,int carModel)
	{
		Log.i(TAG, "updateCanBoxType:"+canBoxType+",carModel:"+carModel);
		switch(canBoxType)
		{
			case CarModelDefine.CAN_BOX_RZC:
			{
				//睿智诚标志雪铁龙车系协议格式要单独处理
				if(carModel >= 250 && carModel <= 299)
				{
					mLengthFlag = 0x00;
					mHeadTag[0] = 0xFD;
					mHeadTag[1] = 0xFF;
					mHeadTag[2] = 0xFF;
					mHeadLength = 0x01;
					mBaudRate[0] = 0x00;
					mBaudRate[1] = 0x00;
					mBaudRate[2] = 0x4B;
					mBaudRate[3] = 0x00;
					mChecksumAddLength = 0x01;
					mChecksumAddCmd = 0x01;
					mChecksumMode = 0x00;
					mChecksumExtern = 0x00;
					mNeedAck = 0x00;
					mOkAck = 0xFF;
					mFailedAck = 0xFF;
					mIsValidProtocol = true;
					mCheckSumLength = 0x01;
					mDataLengthMode = 0x01;
				}
				else if(carModel >= 300 && carModel <= 349)
				{
					mLengthFlag = 0x00;
					mHeadTag[0] = 0xFD;
					mHeadTag[1] = 0xFF;
					mHeadTag[2] = 0xFF;
					mHeadLength = 0x01;
					mBaudRate[0] = 0x00;
					mBaudRate[1] = 0x00;
					mBaudRate[2] = 0x4B;
					mBaudRate[3] = 0x00;
					mChecksumAddLength = 0x01;
					mChecksumAddCmd = 0x01;
					mChecksumMode = 0x00;
					mChecksumExtern = 0x00;
					mNeedAck = 0x00;
					mOkAck = 0xFF;
					mFailedAck = 0xFF;
					mIsValidProtocol = true;
					mCheckSumLength = 0x02;
					mDataLengthMode = 0x01;
				}
				else 
				{
					mLengthFlag = 0x01;
					mHeadTag[0] = 0x2E;
					mHeadTag[1] = 0xFF;
					mHeadTag[2] = 0xFF;
					mHeadLength = 0x01;
					mBaudRate[0] = 0x00;
					mBaudRate[1] = 0x00;
					mBaudRate[2] = 0x96;
					mBaudRate[3] = 0x00;
					mChecksumAddLength = 0x01;
					mChecksumAddCmd = 0x01;
					mChecksumMode = 0x01;
					mChecksumExtern = 0x00;
					mNeedAck = 0x01;
					mOkAck = 0xFF;
					mFailedAck = 0xF0;
					mIsValidProtocol = true;
					mCheckSumLength = 0x01;
					mDataLengthMode = 0x00;
				}
			}
				break;
			case CarModelDefine.CAN_BOX_SS:
				mLengthFlag = 0x00;
				mHeadTag[0] = 0xAA;
				mHeadTag[1] = 0x55;
				mHeadTag[2] = 0xFF;
				mHeadLength = 0x02;
				mBaudRate[0] = 0x00;
				mBaudRate[1] = 0x00;
				mBaudRate[2] = 0x96;
				mBaudRate[3] = 0x00;
				mChecksumAddLength = 0x01;
				mChecksumAddCmd = 0x01;
				mChecksumMode = 0x00;
				mChecksumExtern = 0x01;
				mNeedAck = 0x00;
				mOkAck = 0xFF;
				mFailedAck = 0xFF;
				mIsValidProtocol = true;
				mCheckSumLength = 0x01;
				mDataLengthMode = 0x00;
				break;
			case CarModelDefine.CAN_BOX_XP:
				mLengthFlag = 0x01;
				mHeadTag[0] = 0x2E;
				mHeadTag[1] = 0xFF;
				mHeadTag[2] = 0xFF;
				mHeadLength = 0x01;
				mBaudRate[0] = 0x00;
				mBaudRate[1] = 0x00;
				mBaudRate[2] = 0x96;
				mBaudRate[3] = 0x00;
				mChecksumAddLength = 0x01;
				mChecksumAddCmd = 0x01;
				mChecksumMode = 0x01;
				mChecksumExtern = 0x00;
				mNeedAck = 0x01;
				mOkAck = 0xFF;
				mFailedAck = 0xF0;
				mIsValidProtocol = true;
				mCheckSumLength = 0x01;
				mDataLengthMode = 0x00;
				break;
			default:
				Log.e(TAG, "not support this can box type");
				break;
		}
	}
	
	public boolean isValidCanBoxProtocol()
	{
		return mIsValidProtocol;
	}
	
	public int getLengthFlag()
	{
		return mLengthFlag;
	}
	
	public int getHeadLength()
	{
		return mHeadLength;
	}
	
	public int[] getHeadFlag()
	{
		return mHeadTag;
	}
	
	public int[] getBaudRateFlag()
	{
		return mBaudRate;
	}
	
	public int getChecksumAddLengthFlag()
	{
		return mChecksumAddLength;
	}
	
	public int getChecksumAddCmdFlag()
	{
		return mChecksumAddCmd;
	}
	
	public int getChecksumModeFlag()
	{
		return mChecksumMode;
	}
	
	public int getChecksumExternFlag()
	{
		return mChecksumExtern;
	}
	
	public int getNeedAckFlag()
	{
		return mNeedAck;
	}
	
	public int getOkAckFlag()
	{
		return mOkAck;
	}
	
	public int getFaliedAckFlag()
	{
		return mFailedAck;
	}
	
	public int getCheckSumLength()
	{
		return mCheckSumLength;
	}
	
	public int getDataLengthMode()
	{
		return mDataLengthMode;
	}
	
	
	public static int[] getSupportedCanBoxs()
	{
		return CarModelDefine.SUPPORTED_CAN_BOX;
	}
	
	public static int[] getSupportedCarModels(int canBoxType)
	{
		switch(canBoxType)
		{
			case CarModelDefine.CAN_BOX_NONE:
				return new int[]{CarModelDefine.CAR_MODEL_NONE};
			case CarModelDefine.CAN_BOX_RZC:
				return CarModelDefine.RZC_SUPPORTED_CAR_MODEL;
			case CarModelDefine.CAN_BOX_SS:
				return CarModelDefine.SS_SUPPORTED_CAR_MODEL;
			case CarModelDefine.CAN_BOX_XP:
				return CarModelDefine.XP_SUPPORTED_CAR_MODEL;
			default:
				break;
		}
		return null;
	}
	
	public static String[] getAllSupportedCarModels()
	{
		String []allSupportedCarModels = new String[CarModelDefine.SUPPORTED_CAN_BOX.length];
		for(int i=0;i<CarModelDefine.SUPPORTED_CAN_BOX.length;i++)
		{
			allSupportedCarModels[i] = ServiceHelper.intArrayToString(getSupportedCarModels(CarModelDefine.SUPPORTED_CAN_BOX[i]));
		}
		return allSupportedCarModels;
	}
	
	public static boolean isSupportedCanBoxType(int canBoxType)
	{
		for(int i=0;i<CarModelDefine.SUPPORTED_CAN_BOX.length;i++)
		{
			if(CarModelDefine.SUPPORTED_CAN_BOX[i] == canBoxType)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSupportedCarModel(int canBoxType,int carModel)
	{
		if(!isSupportedCanBoxType(canBoxType))
		{
			return false;
		}
		int[] supportedCarModels = getSupportedCarModels(canBoxType);
		for(int i=0;i<supportedCarModels.length;i++)
		{
			if(supportedCarModels[i] == carModel)
			{
				return true;
			}
		}
		return false;
	}
	
	public MctVehicleManager createVehicleManager(int canBoxType,int carModel)
	{
		switch (carModel)
		{
			//大众系列
			case CarModelDefine.CAR_MODEL_VOLKSWAGEN_MAGOTAN:
			case CarModelDefine.CAR_MODEL_VOLKSWAGEN_SAGITAR:
			case CarModelDefine.CAR_MODEL_VOLKSWAGEN_POLO:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_VolkswagenSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//大众高尔夫7、明锐、17款迈腾
			case CarModelDefine.CAR_MODEL_VOLKSWAGEN_GOLF7:
			case CarModelDefine.CAR_MODEL_VOLKSWAGEN_OCTAVIA:
			case CarModelDefine.CAR_MODEL_VOLKSWAGEN_17MAGOTAN:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_Volkswagen_Golf7_Manager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//日产系列
			case CarModelDefine.CAR_MODEL_NISSAN_X_TRAIL:
			case CarModelDefine.CAR_MODEL_NISSAN_QASHQAI:
			case CarModelDefine.CAR_MODEL_NISSAN_NEW_TEANA:
			case CarModelDefine.CAR_MODEL_NISSAN_TEANA:
			case CarModelDefine.CAR_MODEL_NISSAN_TIIDA:
			case CarModelDefine.CAR_MODEL_NISSAN_SUN:
			case CarModelDefine.CAR_MODEL_NISSAN_LI_WEI:
			case CarModelDefine.CAR_MODEL_NISSAN_BLUEBIRD:
			case CarModelDefine.CAR_MODEL_NISSAN_MORNAO:
			case CarModelDefine.CAR_MODEL_NISSAN_CIMA_L:
			case CarModelDefine.CAR_MODEL_NISSAN_CIMA_H:
			case CarModelDefine.CAR_MODEL_NISSAN_VENUCIA_T70:
			case CarModelDefine.CAR_MODEL_NISSAN_VENUCIA_T90:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_NissanSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//日产英菲尼迪QX50
			case CarModelDefine.CAR_MODEL_NISSAN_INIFINITI_QX50_L:
			case CarModelDefine.CAR_MODEL_NISSAN_INIFINITI_QX50_H:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return null;
					//return new RZC_Nissan_Infiniti_QX50_Manager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//丰田系列
			case CarModelDefine.CAR_MODEL_TOYOTA_RAV4:
			case CarModelDefine.CAR_MODEL_TOYOTA_VIOS:
			case CarModelDefine.CAR_MODEL_TOYOTA_REIZ:
			case CarModelDefine.CAR_MODEL_TOYOTA_OVERBEARING:
			case CarModelDefine.CAR_MODEL_TOYOTA_COROLLA:
			case CarModelDefine.CAR_MODEL_TOYOTA_HIGHLANDER:
			case CarModelDefine.CAR_MODEL_TOYOTA_CAMRY:
			case CarModelDefine.CAR_MODEL_TOYOTA_CROWN:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_ToyotaSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//本田系列
			case CarModelDefine.CAR_MODEL_HONDA_15CRV_L:
			case CarModelDefine.CAR_MODEL_HONDA_15CRV_H:
			case CarModelDefine.CAR_MODEL_HONDA_CROSSTOUR:
			case CarModelDefine.CAR_MODEL_HONDA_ACCORD_9L:
			case CarModelDefine.CAR_MODEL_HONDA_ACCORD_9H:
			case CarModelDefine.CAR_MODEL_HONDA_CRIDER:
			case CarModelDefine.CAR_MODEL_HONDA_JADE:
			case CarModelDefine.CAR_MODEL_HONDA_FIDO:
			case CarModelDefine.CAR_MODEL_HONDA_WISDOM:
			case CarModelDefine.CAR_MODEL_HONDA_CITY:
			case CarModelDefine.CAR_MODEL_HONDA_12CRV:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_HondaSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//通用系列
			case CarModelDefine.CAR_MODEL_GM_HIDEN:
			case CarModelDefine.CAR_MODEL_GM_CRUZE:
			case CarModelDefine.CAR_MODEL_GM_MALIBU:
			case CarModelDefine.CAR_MODEL_GM_16MALIBU_XL:
			case CarModelDefine.CAR_MODEL_GM_GL8:
			case CarModelDefine.CAR_MODEL_GM_AVEO:
			case CarModelDefine.CAR_MODEL_GM_REGAL:
			case CarModelDefine.CAR_MODEL_GM_ENCORE:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_GMSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//标志雪铁龙系
			case CarModelDefine.CAR_MODEL_PEUGEOT_16_308:
			case CarModelDefine.CAR_MODEL_PEUGEOT_14_408:
			case CarModelDefine.CAR_MODEL_PEUGEOT_ELYSEE:
			case CarModelDefine.CAR_MODEL_PEUGEOT_301:
			case CarModelDefine.CAR_MODEL_PEUGEOT_2008:
			case CarModelDefine.CAR_MODEL_PEUGEOT_3008:
			case CarModelDefine.CAR_MODEL_PEUGEOT_C4L:
			case CarModelDefine.CAR_MODEL_PEUGEOT_DS5LS:
			case CarModelDefine.CAR_MODEL_PEUGEOT_C3XL:
			case CarModelDefine.CAR_MODEL_PEUGEOT_SEGA:
			case CarModelDefine.CAR_MODEL_PEUGEOT_4008:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_PeugeotSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//韩系车型
			case CarModelDefine.CAR_MODEL_KOREA_IX35:
			case CarModelDefine.CAR_MODEL_KOREA_IX45:
			case CarModelDefine.CAR_MODEL_KOREA_SONATA8:
			case CarModelDefine.CAR_MODEL_KOREA_SONATA9:
			case CarModelDefine.CAR_MODEL_KOREA_SORRENTO:
			case CarModelDefine.CAR_MODEL_KOREA_KIA_K5:
			case CarModelDefine.CAR_MODEL_KOREA_KIA_KX5:
			case CarModelDefine.CAR_MODEL_KOREA_16MISTRA:
			case CarModelDefine.CAR_MODEL_KOREA_KIA_16ELANTRA:
			case CarModelDefine.CAR_MODEL_KOREA_KIA_16K3:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_KoreaSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//福特车型
			case CarModelDefine.CAR_MODEL_FORD_12_FUCOS:
			case CarModelDefine.CAR_MODEL_FORD_13_KUGA:
			case CarModelDefine.CAR_MODEL_FORD_13_ECO_SPORT:
			case CarModelDefine.CAR_MODEL_FORD_13_FIESTA:
			case CarModelDefine.CAR_MODEL_FORD_15_FUCOS:
			case CarModelDefine.CAR_MODEL_FORD_17_KUGA:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_FordSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//奔腾B50
			case CarModelDefine.CAR_MODEL_BESTURN_16_B50:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_BesturnSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			//广汽传祺车系
			case CarModelDefine.CAR_MODEL_TRUMPCH_GA3:
			case CarModelDefine.CAR_MODEL_TRUMPCH_GA3S:
			case CarModelDefine.CAR_MODEL_TRUMPCH_GA6:
			case CarModelDefine.CAR_MODEL_TRUMPCH_GS4:
			case CarModelDefine.CAR_MODEL_TRUMPCH_GS5:
			case CarModelDefine.CAR_MODEL_TRUMPCH_XINGLANG:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_TrumpchiSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			case CarModelDefine.CAR_MODEL_GEELY_EMGRAND:
			case CarModelDefine.CAR_MODEL_GEELY_EMGRAND_GS:
			case CarModelDefine.CAR_MODEL_GEELY_NL_3:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_GeelySeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			case CarModelDefine.CAR_MODEL_CHINA_MOTOR_V3:
			case CarModelDefine.CAR_MODEL_CHINA_MOTOR_H3:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_ChinaMotorManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			case CarModelDefine.CAR_MODEL_BAO_JUN_560_2015:
			case CarModelDefine.CAR_MODEL_BAO_JUN_730_2014:
			case CarModelDefine.CAR_MODEL_BAO_JUN_730_2017:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_BaoJunManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			case CarModelDefine.CAR_MODEL_JEEP_FREELIGHT_2015:
			case CarModelDefine.CAR_MODEL_JEEP_LIBERTY_2016:
			case CarModelDefine.CAR_MODEL_JEEP_COMPASS_2017:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					//暂不支持
					return null;
				//	return new RZC_JeepSeriesManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			case CarModelDefine.CAR_MODEL_GREAT_WALL_H2:
				if(canBoxType == CarModelDefine.CAN_BOX_RZC)
				{
					return new RZC_GreatWallManager(carModel);
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_SS)
				{
					return null;
				}
				else if(canBoxType == CarModelDefine.CAN_BOX_XP)
				{
					return null;
				}
				break;
			default:
				return new DefaultCarManager();
		}
		return null;
	}
	
}
