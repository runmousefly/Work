package com.mct.carmodels;

import java.util.ArrayList;
import java.util.List;

import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CarService;
import com.mct.coreservices.MctVehicleManager;

import android.os.Bundle;
import android.util.Log;

//默认车型
public class DefaultCarManager extends MctVehicleManager
{
	private static String TAG = "DefaultCarManager";
	
	public boolean onInitManager(CarService service)
	{
		Log.i(TAG, "onInitManager");
		return true;
	}

	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		return true;
	}

	public int[] getSupportedPropertyIds()
	{
		Log.i(TAG, "getSupportedPropertyIds");
		return DefaultCarProtocol.VEHICLE_CAN_PROPERITIES;
	}

	public int[] getWritablePropertyIds()
	{
		Log.i(TAG, "getWritablePropertyIds");
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < DefaultCarProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (DefaultCarProtocol.getProperityPermission(DefaultCarProtocol.VEHICLE_CAN_PROPERITIES[i]) >= DefaultCarProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(DefaultCarProtocol.VEHICLE_CAN_PROPERITIES[i]);
			}
		}
		int[] retArray = new int[writableProps.size()];
		for (int i = 0; i < retArray.length; i++)
		{
			retArray[i] = writableProps.get(i);
		}
		return retArray;
	}

	public int getPropertyDataType(int propId)
	{
		Log.i(TAG, "getPropertyDataType,propId:"+propId);
		return DefaultCarProtocol.getProperityDataType(propId);
	}

	public boolean setPropValue(int propId, String value)
	{
		Log.i(TAG, "setPropValue,propId:"+propId+",value:"+value);
		return false;
	}

	public String getPropValue(int propId)
	{
		Log.i(TAG, "getPropValue,propId:"+propId);
		return null;
	}

	public void onLocalReceive(Bundle data)
	{
		Log.i(TAG, "onLocalReceive");
	}
}
