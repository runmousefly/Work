package com.mct.coreservices;

import com.mct.VehiclePropertyConstants;

import android.os.Bundle;

public abstract class MctVehicleManager
{
	public boolean onInitManager(CarService service)
	{
		return true;
	}

	public boolean onDeinitManager()
	{
		return true;
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
		return VehiclePropertyConstants.DATA_TYPE_UNKNOWN;
	}

	public boolean setPropValue(int propId, String value)
	{
		return true;
	}

	public String getPropValue(int propId)
	{
		return null;
	}

	public void onLocalReceive(Bundle data)
	{
	}
}
