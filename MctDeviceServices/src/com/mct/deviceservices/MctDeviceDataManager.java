package com.mct.deviceservices;

import com.mct.DevicePropertyConstants;

import android.os.Bundle;

public abstract class MctDeviceDataManager
{
	protected int mServiceState = -1;
	public boolean onInitManager(DeviceService service)
	{
		return false;
	}

	public boolean onDeinitManager()
	{
		return false;
	}

	public int[] getSupportedPropertyIds()
	{
		return null;
	}

	public int[] getWritablePropertyIds()
	{
		return null;
	}

	public int getPropertyDataType(int propId)
	{
		return DevicePropertyConstants.DATA_TYPE_UNKNOWN;
	}

	public boolean setPropValue(int propId, String value)
	{
		return false;
	}

	public String getPropValue(int propId)
	{
		return null;
	}

	public void onLocalReceive(Bundle data)
	{
	}
	
	public int getServiceState()
	{
		return mServiceState;
	}
}
