package com.mct.tpms;

import java.io.File;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;
import com.mct.carmodels.CarModelDefine;
import com.mct.common.MctSetPropertyFilter;
import com.mct.coreservices.CanBoxProtocol;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.os.Bundle;
import android.util.Log;

public class TPMSManager extends MctVehicleManager
{
	private static String TAG = "TPMSManager";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuMangaer = null;
	private int mTPMSDeviceType = 0;
	private int mTPMSDeviceId = 0;
	private TPMSManager mTPMSDeviceManager = null;
	private boolean mSupportTPMSFunction = false;
	
	// tpms info config file
	public static final String TPMS_INFO_CONFIG_DIR = "/svdata/config";
	public static final String TPMS_INFO_CONFIG_FILE_PATH = "/svdata/config/tpms_info.conf";

	public TPMSManager(){  
    }
	
	@Override
	public boolean onInitManager(CarService service)
	{
		Log.i(TAG, "onInitManager");
		mService = service;
		mMcuMangaer = (HeadUnitMcuManager) mService.getMcuManager();
		if(!mSupportTPMSFunction)
		{
			return false;
		}
		// 根据TPMS类型与编号初始化协议
		int[] strTPMSSetting = ServiceHelper.stringToIntArray(ServiceHelper.readFromLocal(TPMS_INFO_CONFIG_FILE_PATH));
		if (strTPMSSetting != null && strTPMSSetting.length == 2)
		{
			mTPMSDeviceType = strTPMSSetting[0];
			mTPMSDeviceId = strTPMSSetting[1];
			Log.i(TAG, "Load TPMS Info,TPMSType:" + mTPMSDeviceType + ",TPMSId:" + mTPMSDeviceId);
		}
		else
		{
			Log.i(TAG, "read tpms info failed,and reset default!");
			File file = new File(TPMS_INFO_CONFIG_DIR);
			if (!file.exists())
			{
				file.mkdir();
			}
			ServiceHelper.writeToLocal(TPMS_INFO_CONFIG_FILE_PATH, ServiceHelper.intArrayToString(new int[] { mTPMSDeviceType, mTPMSDeviceId }));
		}
		if (updateTPMSDevice(mTPMSDeviceType, mTPMSDeviceId))
		{
			Log.i(TAG, "init tpms device success");
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
		if(mTPMSDeviceManager != null)
		{
			mTPMSDeviceManager.onDeinitManager();
			mTPMSDeviceManager = null;
		}
		return true;
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		if(!mSupportTPMSFunction)
		{
			return false;
		}
		//更新胎压模块设备
		if(propId == VehicleInterfaceProperties.VIM_MCU_TPMS_DEVICE_TYPE_PROPERTY)
		{
			int []deviceInfo = ServiceHelper.stringToIntArray(value);
			if(deviceInfo != null && deviceInfo[0] != mTPMSDeviceType && 
					deviceInfo[1] != mTPMSDeviceId &&
					deviceInfo[0] >= 0 && deviceInfo[1] >= 0)
			{
				return updateTPMSDevice(deviceInfo[0], deviceInfo[1]);
			}
		}
		if(mTPMSDeviceManager != null)
		{
			return mTPMSDeviceManager.setPropValue(propId, value);
		}
		return true;
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		return super.getPropValue(propId);
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return super.getSupportedPropertyIds();
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		return super.getWritablePropertyIds();
	}

	@Override
	public int getPropertyDataType(int propId)
	{
		// TODO Auto-generated method stub
		return super.getPropertyDataType(propId);
	}

	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		if(!mSupportTPMSFunction)
		{
			return;
		}
		if(mTPMSDeviceManager != null)
		{
			mTPMSDeviceManager.onLocalReceive(data);
		}
	}
	
	public void onReceiveData(int[] data)
	{
		if(!mSupportTPMSFunction)
		{
			return;
		}
		if(mTPMSDeviceManager != null)
		{
			mTPMSDeviceManager.onReceiveData(data);
		}
	}

	// 更新TPMS设备
	private boolean updateTPMSDevice(int tpmsType, int tpmsId)
	{
		if(!mSupportTPMSFunction)
		{
			return false;
		}
		// 根据车型初始化车型协议
		Log.i(TAG, "updateTPMSDevice,TPMSType:" + tpmsType + ",TPMSId:" + tpmsId);
		if (mService != null)
		{
			// update tpms
			if(mTPMSDeviceManager != null)
			{
				mTPMSDeviceManager.onDeinitManager();
				mTPMSDeviceManager = null;
			}
			if(mTPMSDeviceType == 0 && mTPMSDeviceId == 0)
			{
				mTPMSDeviceManager = new YAT_SERIAL_TPMSManager();
				mTPMSDeviceManager.onInitManager(mService);
				return true;
			}
			
			return false;
		}
		Log.e(TAG, "update car model failed");
		return false;
	}
}
