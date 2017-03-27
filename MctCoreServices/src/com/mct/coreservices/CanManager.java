package com.mct.coreservices;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.carmodels.CarModelDefine;
import com.mct.carmodels.DefaultCarManager;
import com.mct.carmodels.RZC_HondaSeriesManager;
import com.mct.carmodels.RZC_NissanSeriesManager;
import com.mct.carmodels.RZC_Nissan_Infiniti_QX50_Manager;
import com.mct.carmodels.RZC_ToyotaSeriesManager;
import com.mct.carmodels.RZC_VolkswagenSeriesManager;
import com.mct.common.MctCoreServicesProvider;
import com.mct.common.MctCoreServicesProviderMetaData;
import com.mct.utils.ServiceHelper;

import android.R.integer;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

public class CanManager extends MctVehicleManager
{
	private static String TAG = "CanManager";
	private CarService mService = null;
	private CanBoxProtocol mCanBoxProtocol = null;
	private HeadUnitMcuManager mMcuMangaer = null;
	private MctVehicleManager mVehicleManager = null;
	private boolean mCanHandleReady = false;
	/*使用全局过滤器代替
	private long mSourceInfoTimer = 0;
	private long mNaviInfoTimer = 0;
	private String mPrevSourceInfo = null;
	private String mPrevNaviInfo = null;*/
	
	//初始睿智诚+大众迈腾
	private int mCanBoxType = CarModelDefine.CAN_BOX_NONE;
	private int mCarModel = CarModelDefine.CAR_MODEL_NONE;
	
	private final static int AIR_CONDITION_AUTO_HIDE_TIMEOUT = 8500;
	
	private final static String OEM_KEY_CODE 					= "keycode";
	private final static String OEM_KEY_ACTION 				= "keystatus";
	private final static String OEM_KEY_REPEAT_COUNT 	= "keyrepeatcount";
	private final static String ACTION_OEM_KEYEVENT 		= "com.mct.action.oem.keyevent";
	
	//vehicle info config file
	public static final String VEHICLE_INFO_CONFIG_DIR = "/svdata/config";
	public static final String VEHICLE_INFO_CONFIG_FILE_PATH = "/svdata/config/vehicle_info.conf";
	
	private static int[] EXCLUDED_PROP_IDS = new int[]{
			VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY, 
			VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY, 
			VehicleInterfaceProperties.VIM_VEHICLE_NAME_PROPERTY,
	};
	
	private Runnable mHideAirConditionRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			if(mService != null)
			{
				mService.broadcastAirConditionInfo(0);
			}
		}
	};
	
	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager");
		mService = service;
		//初始化支持的CAN盒厂家以及车型
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SUPPORTED_CAN_BOX_MODEL_PROPERTY, ServiceHelper.intArrayToString(CanBoxProtocol.getSupportedCanBoxs()));
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SUPPORTED_VEHICLE_MODELS_PROPERTY, ServiceHelper.stringArrayToString(CanBoxProtocol.getAllSupportedCarModels()));
		
		mMcuMangaer = (HeadUnitMcuManager)mService.getMcuManager();
		//根据CanBox类型初始化CAN解析协议
		int []strCarModelSetting = ServiceHelper.stringToIntArray(ServiceHelper.readFromLocal(VEHICLE_INFO_CONFIG_FILE_PATH));
		if(strCarModelSetting != null && strCarModelSetting.length == 2)
		{
			mCanBoxType = strCarModelSetting[0];
			mCarModel = strCarModelSetting[1];
			Log.i(TAG, "Load CarModel Info,CanBoxType:"+mCanBoxType+",CarModel:"+mCarModel);
		}
		else
		{
			Log.i(TAG, "read vehicele info failed,and reset default!");
			File file = new File(VEHICLE_INFO_CONFIG_DIR);
			if(!file.exists())
			{
				file.mkdir();
			}
			ServiceHelper.writeToLocal(VEHICLE_INFO_CONFIG_FILE_PATH, ServiceHelper.intArrayToString(new int[]{mCanBoxType,mCarModel}));
		}
		if(updateCanBoxAndCarModel(mCanBoxType, mCarModel))
		{
			Log.i(TAG, "init canbox and carmodel success");
			mCanHandleReady = true;
			mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY,ServiceHelper.intArrayToString(new int[]{mCanBoxType,mCarModel}),false);
			setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_START));
			setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO));
			setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO));
		}
		else
		{
			Log.e(TAG, "init canbox and car model failed");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		if(mVehicleManager != null)
		{
			setOffSource();
			if(mService != null)
			{
				mService.getMainHandler().removeCallbacks(mHideAirConditionRunnable);
			}
			mVehicleManager.setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_END));
			mVehicleManager.onDeinitManager();
			mVehicleManager = null;
		}
		mMcuMangaer = null;
		mCanHandleReady = false;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		if(mVehicleManager == null || !mCanHandleReady)
		{
			return null;
		}
		return mVehicleManager.getSupportedPropertyIds();
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		if(mVehicleManager == null || !mCanHandleReady)
		{
			return null;
		}
		return mVehicleManager.getWritablePropertyIds();
	}

	@Override
	public int getPropertyDataType(int propId)
	{
		// TODO Auto-generated method stub
		if(mVehicleManager == null || !mCanHandleReady)
		{
			return VehiclePropertyConstants.DATA_TYPE_UNKNOWN;
		}
		return mVehicleManager.getPropertyDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "setPropValue,propId:"+propId+",value:"+value);
		if(mVehicleManager == null || !mCanHandleReady)
		{
			return false;
		}
		try
		{
			switch(propId)
			{
				//更新can盒类型与车型
				case VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY:
					int []array = VehicleManager.stringToIntArray(value);
					if(array == null || array.length != 2)
					{
						Log.i(TAG, "unvalid param,prop:"+propId+",value:"+value);
						return false;
					}
					if(mCanBoxProtocol != null && CanBoxProtocol.isSupportedCarModel(array[0],array[1]))
					{
						if(updateCanBoxAndCarModel(array[0],array[1]))
						{
							//清除前一个车型的缓存数据
							mService.clearHistoryCache(CarService.VEHICLE_PROPERTY_ID_MIN,CarService.VEHICLE_PROPERTY_ID_MAX,
									EXCLUDED_PROP_IDS);
							if(ServiceHelper.writeToLocal(VEHICLE_INFO_CONFIG_FILE_PATH, ServiceHelper.intArrayToString(array)))
							{
								Log.i(TAG, "write vehicle info success,canbox:"+array[0]+".carmodel:"+array[1]);
							}
							mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY,ServiceHelper.intArrayToString(array),false);
							return true;
						}
					}
					return false;	
				//过滤频繁调用
				/*case VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY:
					long curSourceTime = SystemClock.uptimeMillis();
					Log.i(TAG, "sync source info:"+value+",time:"+curSourceTime);
					if(curSourceTime - mSourceInfoTimer < 1000)
					{
						Log.i(TAG, "sync source too fast!!!");
						return false;
					}
					mSourceInfoTimer = curSourceTime;
					break;
				//过滤频繁调用
				case VehicleInterfaceProperties.VIM_NAVI_INFO_COLLECT_PROPERTY:
					long curNaviTime = SystemClock.uptimeMillis();
					Log.i(TAG, "sync navi info:"+value+",time:"+curNaviTime);
					if(curNaviTime - mNaviInfoTimer < 1000)
					{
						Log.i(TAG, "sync navi info too fast!!!");
						return false;
					}
					mNaviInfoTimer = curNaviTime;
					break;*/
				default:
					break;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "setProp exception,propId"+propId+",value:"+value);
			return false;
		}
		return mVehicleManager.setPropValue(propId, value);
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		if(mVehicleManager == null || !mCanHandleReady)
		{
			return null;
		}
		return mVehicleManager.getPropValue(propId);
	}

	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		if (data == null || mVehicleManager == null || !mCanHandleReady) { return; }
		mVehicleManager.onLocalReceive(data);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//更新can盒和车型
	private boolean updateCanBoxAndCarModel(int canBoxType,int carModel)
	{
		//根据车型初始化车型协议
		Log.i(TAG, "updateCanBoxAndCarMode,CanBoxType:"+canBoxType+",carModel:"+carModel);
		if(mService != null)
		{
			mCanHandleReady = false;
			
			//update can box
			if(mCanBoxProtocol != null)
			{
				mCanBoxProtocol.updateCanBoxType(canBoxType,carModel);
			}
			else
			{
				mCanBoxProtocol = new CanBoxProtocol(canBoxType,carModel);
			}
			if(canBoxType != CarModelDefine.CAN_BOX_NONE &&
					carModel != CarModelDefine.CAR_MODEL_NONE &&
					!mMcuMangaer.onInitCanProtocol(mCanBoxProtocol))
			{
				Log.e(TAG, "update can box failed!");
				mCanHandleReady = true;
				return false;
			}
			
			//update car model
			MctVehicleManager vehicleManager = mCanBoxProtocol.createVehicleManager(canBoxType, carModel);
			if(vehicleManager == null)
			{
				Log.e(TAG, "not support this carmodel,CanBox:"+canBoxType+",CarModel:"+carModel);
				mCanHandleReady = true;
				return false;
			}
			mVehicleManager = null;
			mVehicleManager = vehicleManager;
			if(!mVehicleManager.onInitManager(mService))
			{
				Log.e(TAG, "update car model failed!");
				mCanHandleReady = true;
				return false;
			}
			mCanHandleReady = true;
			return true;
		}
		Log.e(TAG, "update car model failed");
		return false;
	}
	// 封包
	public int[] pack(int cmd, int[] param)
	{
		if(!mCanHandleReady)
		{
			Log.w(TAG, "can handle not ready");
			return null;
		}
		if(!mCanBoxProtocol.isValidCanBoxProtocol())
		{
			Log.e(TAG, "unvalid canbox type");
			return null;
		}
		int paramLen = (param == null) ? 0 : param.length;
		int protocolLen = paramLen;
		int[] data = new int[paramLen + 2+mCanBoxProtocol.getHeadLength()+mCanBoxProtocol.getCheckSumLength()];//Cmd+Len+Checksum+Head
		long checksum = 0x00;
		int count = 0;
		int i = 0;
		//Head
		int[] canHead = mCanBoxProtocol.getHeadFlag();
		for(i=0;i<mCanBoxProtocol.getHeadLength();i++)
		{
			data[count] = canHead[i];
			count ++;
		}
		if(mCanBoxProtocol.getDataLengthMode() == 0x01)
		{
			protocolLen =  paramLen + 2+mCanBoxProtocol.getCheckSumLength();
		}
		//Length or Cmd
		if(mCanBoxProtocol.getLengthFlag() == 0x01)
		{
			data[count] = cmd;
			data[count+1] =  protocolLen;
		}
		else
		{
			data[count] =  protocolLen;
			data[count+1] = cmd;
		}
		
		//Data
		count += 2;
		if (param != null)
		{
			for (i = 0; i < paramLen; ++i)
			{
				data[count] = (param[i] & 0xFF);
				checksum += (param[i] & 0xFF);
				if(mCanBoxProtocol.getCheckSumLength() == 0x02)
				{
					checksum = checksum & 0xFFFF;
				}
				else
				{
					checksum = checksum & 0xFF;
				}
				count ++;
			}
		}
		
		// checksum
		if(mCanBoxProtocol.getChecksumAddLengthFlag() == 0x01)
		{
			checksum += protocolLen;
			if(mCanBoxProtocol.getCheckSumLength() == 0x02)
			{
				checksum = checksum & 0xFFFF;
			}
			else
			{
				checksum = checksum & 0xFF;
			}
		}
		if(mCanBoxProtocol.getChecksumAddCmdFlag() == 0x01)
		{
			checksum += cmd;
			if(mCanBoxProtocol.getCheckSumLength() == 0x02)
			{
				checksum = checksum & 0xFFFF;
			}
			else
			{
				checksum = checksum & 0xFF;
			}
		}
		if(mCanBoxProtocol.getChecksumModeFlag() == 0x01)
		{
			//^0xFF
			checksum = checksum ^ 0xFF;
		}
		else
		{
			//&0xFF
			if(mCanBoxProtocol.getCheckSumLength() == 0x02)
			{
				checksum = checksum & 0xFFFF;
			}
			else
			{
				checksum = checksum & 0xFF;
			}
		}
		if(mCanBoxProtocol.getChecksumExternFlag() == 0x01)
		{
			checksum --;
		}
		if(mCanBoxProtocol.getCheckSumLength() == 0x02)
		{
			data[count++] = (int)((checksum >> 8) & 0xFF);//高位
			data[count] = (int)(checksum & 0xFF);//低位
		}
		else
		{
			data[count] = (int)(checksum & 0xFF);
		}
		return data;
	}

	// 解包
	public int[] unPack(int[] buffer)
	{
		if(!mCanHandleReady)
		{
			Log.w(TAG, "can handle not ready");
			return null;
		}
		if(!mCanBoxProtocol.isValidCanBoxProtocol())
		{
			Log.e(TAG, "unvalid canbox type");
			return null;
		}
		
		int[] byReturn = null;
		int pos = 0;
		int length = buffer.length;

		// sync
		int headLength = mCanBoxProtocol.getHeadLength();
		int[]canHead = mCanBoxProtocol.getHeadFlag();
		while (length >= headLength)
		{
			if(ServiceHelper.arraycompare(canHead, 0, buffer, pos + 0, headLength) == 0)
			{
				pos += headLength;
				length -= headLength;
				break;
			}
			++pos;
			--length;
		}

		if (length >= 3)
		{

			int cmd = 0;
			int dataLen = 0;
			if(mCanBoxProtocol.getLengthFlag() == 1)
			{
				cmd = buffer[pos];
				dataLen = buffer[pos+1];
			}
			else
			{
				dataLen = buffer[pos];
				cmd = buffer[pos+1];
			}
			pos += 2;
			length -= 2;
			if(mCanBoxProtocol.getDataLengthMode() == 1)
			{
				dataLen = dataLen - 2 - mCanBoxProtocol.getCheckSumLength();
			}
			if (dataLen <= length)
			{
				//+cmd
				byReturn = new int[dataLen+1];
				byReturn[0] = cmd;
				for (int i = 0; i < dataLen; ++i)
				{
					int data = buffer[pos+i];
					byReturn[i+1] = data;
				}
				pos += (dataLen+mCanBoxProtocol.getCheckSumLength());//+checksum
				length -= (dataLen+mCanBoxProtocol.getCheckSumLength());//+checksum
			}
			else
			{
				// need more bytes
				Log.w(TAG, "receive data is not compelte!");
			}
		}
		else
		{
			// need more bytes
			Log.w(TAG, "receive data(head) is not compelte!");
		}
		if(pos > 0)
		{
			if(length > 0)
			{
				Log.w(TAG, "left unvalid data");
			}
		}
		return byReturn;
	}
		
	//////////////////////////////////////////////////////////////////////////////////////////////////

	public void setOffSource()
	{
		if(mVehicleManager != null)
		{
			int sourceType = VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF;
			int curTrack = -1;
			int totalTrack = -1;
			int currentPlayTime = -1;
			int totalPlayTime = -1;
			int playState = -1;
			String strID3 = null;
			int band = -1;
			int freq = -1;
			String mediaInfoParam = VehiclePropertyConstants.formatSourceInfoString(sourceType, curTrack, totalTrack,currentPlayTime,totalPlayTime,playState,strID3,band,freq);
			setPropValue(VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY, mediaInfoParam);
		}
	}
	
	public void showAirConditionEvent(boolean bShow)
	{
		if(bShow && mService != null)
		{
			mService.broadcastAirConditionInfo(1);
			mService.getMainHandler().removeCallbacks(mHideAirConditionRunnable);
			mService.getMainHandler().postDelayed(mHideAirConditionRunnable, AIR_CONDITION_AUTO_HIDE_TIMEOUT);
		}
		else
		{
			mService.getMainHandler().removeCallbacks(mHideAirConditionRunnable);
			mService.broadcastAirConditionInfo(0);
		}
	}
}
