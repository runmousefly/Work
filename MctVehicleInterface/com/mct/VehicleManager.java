/*
 *    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
 *    Qualcomm Technologies Proprietary and Confidential.
 *
 */

package com.mct;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.qualcomm.qti.ivi.aidl.IVehicleService;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * VehicleManager provides set of APIs to be used by application developer to
 * query various data items related to the vehicle.
 */

public final class VehicleManager
{
	private static final String TAG = "MctVehicleManager";
	public static final String SERVICE_NAME = "com.mct.carservice";
	
	private Messenger mServiceRespMessenger = new Messenger(new ServiceRespHandler());
	private Vector mClientCallbackVector = new Vector();
    
	private static VehicleManager mManager = new VehicleManager();
	private static IVehicleService sService = null;
	
	

	public static VehicleManager getInstance()
	{
		return (getService() == null) ? null : mManager;
	}

	private static synchronized IVehicleService getService()
	{
		if (sService != null && sService.asBinder() != null
				&& sService.asBinder().isBinderAlive()
				&& sService.asBinder().pingBinder())
			return sService;

		IBinder b = ServiceManager.getService(SERVICE_NAME);
		sService = IVehicleService.Stub.asInterface(b);
		return sService;
	}
	
	/**
	 * VehicleManager constructor.
	 */
	private VehicleManager()
	{
	}
	
	/**
	 * Query supported property Ids on this platform.
	 * 
	 * @return array of supported properties else returns null on error. Please
	 *         note that this API is work in progress, and there might be few
	 *         changes going forward.
	 */
	public int[] getSupportedPropertyIds()
	{
		IVehicleService service = getService();
		try {
			return service.getSupportedSignalIds();
		} catch (RemoteException e) {
			Log.e(SERVICE_NAME, "getSupportedPropertyIds: RemoteException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query writable properties.
	 * 
	 * @return array of writable properties else returns null on error. Please
	 *         note that this API is work in progress, and there might be few
	 *         changes going forward.
	 */
	public int[] getWritablePropertyIds()
	{
		IVehicleService service = getService();
		try {
			return service.getWritableSignalIds();
		} catch (RemoteException e) {
			Log.e(SERVICE_NAME, "getWritablePropertyIds: RemoteException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query properties data type.
	 * 
	 * @param propertyIds
	 *            Identify properties to be queried.
	 * @return int[] where each element identifies the data type. Refer to
	 *         VehiclePropertyConstants.java for data type definitions. Property
	 *         value could be of any primitive data type. For each property
	 *         value retrieved via getProperties call, caller needs to
	 *         interpret/convert according to it's primitive data type. Please
	 *         note that this API is work in progress, and there might be few
	 *         changes going forward.
	 */
	public int[] getPropertiesDataType(int[] propertyIds)
	{
		IVehicleService service = getService();
		try {
			return service.getSignalsDataType(propertyIds);
		} catch (RemoteException e) {
			Log.e(SERVICE_NAME, "getPropertiesDataType: RemoteException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getPropertyDataType(int propertyId)
	{
		IVehicleService service = getService();
		try {
			return service.getSignalDataType(propertyId);
		} catch (RemoteException e) {
			Log.e(SERVICE_NAME, "getPropertyDataType: RemoteException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return VehiclePropertyConstants.DATA_TYPE_UNKNOWN;
	}
	/**
	 * Query properties identified by @param propertyIds.
	 * 
	 * @param propertyIds
	 *            Identify properties to be queried.
	 * @return Array of String pointing to value of properties. Although the
	 *         return value is String, the actual Property value could be any
	 *         primitive data type. Caller needs to identify data type before
	 *         interpreting/converting String value. Please see
	 *         VehiclePropertyConstants.java for data type definitions. Also,
	 *         refer to getPropertiesDataType API. Please note that this API is
	 *         work in progress, and there might be few changes going forward.
	 */
	public String[] getProperties(int[] propertyIds)
	{
		IVehicleService service = getService();
		try {
			return service.getSignals(propertyIds);
		} catch (RemoteException e) {
			Log.e(SERVICE_NAME, "getProperties: RemoteException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getProperty(int propertyId)
	{
		IVehicleService service = getService();
		try {
			return service.getSignal(propertyId);
		} catch (RemoteException e) {
			Log.e(SERVICE_NAME, "getProperty: RemoteException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Set value of properties.
	 * 
	 * @param propertyIds
	 *            Identify properties to be set.
	 * @param values
	 *            Point to values to be set for properties. A property could be
	 *            of any primitive data type. VehicleInterface will determine
	 *            the data type before setting values.
	 * @return true if no error occurs while setting the property else returns
	 *         false. Please note that this API is work in progress, and there
	 *         might be few changes going forward.
	 */
	public boolean setProperties(int[] propertyIds, String[] values)
	{
		IVehicleService service = getService();
		try {
			return service.setSignals(propertyIds,values);
		} catch (RemoteException e) 
		{
			Log.e(SERVICE_NAME, "setProperties: RemoteException", e);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean setProperty(int propertyId, String value)
	{
		IVehicleService service = getService();
		try {
			return service.setSignal(propertyId,value);
		} catch (RemoteException e) {
			Log.e(SERVICE_NAME, "setProperty: RemoteException", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	/**
	 * Registers a handler to be called from this interface when there is an
	 * update.
	 * 
	 * @param handler
	 *            Handle to the callback.
	 * @param rateMs
	 *            Frequency in milli-seconds for call-backs to be fired. This is
	 *            just a hint, the actual rate may be faster or slower.
	 * @return true if no error occurs while registering the callback else
	 *         returns false. Please note that this API is work in progress, and
	 *         there might be few changes going forward.
	 */
	public boolean registerHandler(int[] propIds, VehicleInterfaceDataHandler handler)
	{
		IVehicleService service = getService();
		try 
		{
			if (mClientCallbackVector != null && !mClientCallbackVector.contains(handler))
			{
				mClientCallbackVector.add(handler);
			}
			if(service != null)
			{
				return service.registerMessenger(propIds, mServiceRespMessenger,null,null);
			}
			
		} 
		catch (RemoteException e) 
		{
			Log.e(SERVICE_NAME, "registerMessenger: RemoteException", e);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Removes handler which was set successfully via registerHandler API.
	 * 
	 * @param handler
	 *            Handle to the callback.
	 * @return true if no error occurs while removing the handler else returns
	 *         false. Please note that this API is work in progress, and there
	 *         might be few changes going forward.
	 */
	public boolean removeHandler(int[] propIds, VehicleInterfaceDataHandler handler)
	{
		IVehicleService service = getService();
		try 
		{
			if (mClientCallbackVector != null && mClientCallbackVector.contains(handler))
			{
				mClientCallbackVector.remove(handler);
			}
			if(service != null)
			{
				return service.unregisterMessenger(propIds, mServiceRespMessenger);
			}
			
		} 
		catch (RemoteException e) 
		{
			Log.e(SERVICE_NAME, "unregisterMessenger: RemoteException", e);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Handler for service response. Please note that this class is work in
	 * progress, and there might be few changes going forward.
	 */
	private class ServiceRespHandler extends Handler
	{
		private static final String TAG = "ServiceRespHandler";
		@Override
		public void handleMessage(Message msg)
		{
			int propId = msg.what;
			String value = null;
			Bundle data = msg.getData();
			if(data != null && mClientCallbackVector!= null)
			{
				value = data.getString("Value");
				for (Object handler : mClientCallbackVector)
				{
					((VehicleInterfaceDataHandler) handler).onDataUpdate(propId,value);
				}
			}
		}
	}

	private static String COUSTOM_ARRAY_HEAD 	= "{";
	private static String COUSTOM_ARRAY_TAIL 	= "}";
	private static String COUSTOM_ARRAY_SEP 	= ",";
	public static String intArrayToString(int[] array)
	{
		if(array == null || array.length == 0)
		{
			return null;
		}
		String ret = COUSTOM_ARRAY_HEAD;
		for(int i=0;i<array.length;i++)
		{
			if(i == 0)
			{
				ret += array[i];
			}
			else
			{
				ret += (COUSTOM_ARRAY_SEP + array[i]);
			}
			
		}
		return ret;
	}
	
	public static int[] stringToIntArray(String value)
	{
		int[] retArray = null;
		if(value == null || value.length() < 2 
				|| !value.startsWith(COUSTOM_ARRAY_HEAD)
				|| !value.endsWith(COUSTOM_ARRAY_TAIL))
		{
			return null;
		}
		try
		{
			String validString = value.substring(1, value.length()-2);
			String[] subValues = validString.split(COUSTOM_ARRAY_SEP);
			retArray = new int[subValues.length];
			for(int i=0;i<subValues.length;i++)
			{
				retArray[i] = Integer.valueOf(subValues[i]);
			}
			return retArray;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public static String floatArrayToString(float[] array)
	{
		if(array == null || array.length == 0)
		{
			return null;
		}
		String ret = COUSTOM_ARRAY_HEAD;
		for(int i=0;i<array.length;i++)
		{
			if(i == 0)
			{
				ret += array[i];
			}
			else
			{
				ret += (COUSTOM_ARRAY_SEP + array[i]);
			}
			
		}
		return ret;
	}
	
	public static float[] stringToFloatArray(String value)
	{
		float[] retArray = null;
		if(value == null || value.length() < 2 
				|| !value.startsWith(COUSTOM_ARRAY_HEAD)
				|| !value.endsWith(COUSTOM_ARRAY_TAIL))
		{
			return null;
		}
		try
		{
			String validString = value.substring(1, value.length()-2);
			String[] subValues = validString.split(COUSTOM_ARRAY_SEP);
			retArray = new float[subValues.length];
			for(int i=0;i<subValues.length;i++)
			{
				retArray[i] = Float.valueOf(subValues[i]);
			}
			return retArray;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public static String stringArrayToString(String[] array)
	{
		if(array == null || array.length == 0)
		{
			return null;
		}
		String ret = COUSTOM_ARRAY_HEAD;
		for(int i=0;i<array.length;i++)
		{
			if(i == 0)
			{
				ret += array[i];
			}
			else
			{
				ret += (COUSTOM_ARRAY_SEP + array[i]);
			}
			
		}
		return ret;
	}
	
	public static String[] stringToStringArray(String value)
	{
		if(value == null || value.length() < 2 
				|| !value.startsWith(COUSTOM_ARRAY_HEAD)
				|| !value.endsWith(COUSTOM_ARRAY_TAIL))
		{
			return null;
		}
		try
		{
			String validString = value.substring(1, value.length()-2);
			String[] subValues = validString.split(COUSTOM_ARRAY_SEP);
			return subValues;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Notify the client about the fatal error and provide the hint to clean up
	 * and restart.
	 */
	private void notifyFatalError()
	{
		if (mClientCallbackVector != null)
		{
			for (Object handler : mClientCallbackVector)
			{
				((VehicleInterfaceDataHandler) handler).onError(true);
			}
		}
	}
};
