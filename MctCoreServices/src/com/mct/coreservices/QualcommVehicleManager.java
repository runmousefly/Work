package com.mct.coreservices;

import java.util.List;

import com.android.internal.util.ArrayUtils;
import com.mct.VehiclePropertyConstants;
import com.qualcomm.qti.ivi.aidl.IVehicleService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class QualcommVehicleManager extends MctVehicleManager
{
	private static String TAG = "QualcommVehicleManager";
	public static final String QUALCOMM_SERVICE_NAME = "com.qualcomm.qti.ivi.VehicleService";
	private CarService mService = null;
	private IVehicleService mQualcommService = null;
	private Messenger mQualcommMessenger = null;
	private boolean mBind = false;
	private int[] mSupportSignalIds = null;
	
	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager");
		mService = service;
		return bindService(mService.getApplicationContext(), QUALCOMM_SERVICE_NAME);
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		//高通服务在休眠状态下不需要解绑，避免快速ACC操作时的异步过程
		//unbindService(mService.getApplicationContext());
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		if(mSupportSignalIds != null)
		{
			return mSupportSignalIds;
		}
		if(mQualcommService != null)
		{
			try
			{
				mSupportSignalIds =  mQualcommService.getSupportedSignalIds();
				return mSupportSignalIds;
			} catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		if(mQualcommService != null)
		{
			try
			{
				return mQualcommService.getWritableSignalIds();
			} catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int getPropertyDataType(int propId)
	{
		// TODO Auto-generated method stub
		if(mQualcommService != null)
		{
			try
			{
				return mQualcommService.getSignalDataType(propId);
			} catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return VehiclePropertyConstants.DATA_TYPE_UNKNOWN;
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		if(mSupportSignalIds == null)
		{
			mSupportSignalIds = getSupportedPropertyIds();
			if(mSupportSignalIds != null && !ArrayUtils.contains(mSupportSignalIds, propId))
			{
				Log.e(TAG, "not support this property,ID:"+propId);
				return false;
			}
		}
		if(mQualcommService != null)
		{
			try
			{
				return mQualcommService.setSignal(propId, value);
			} catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		if(mQualcommService != null)
		{
			try
			{
				return mQualcommService.getSignal(propId);
			} catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		super.onLocalReceive(data);
	}

	private boolean bindService(Context context, String serviceName)
	{
		if (context == null) { return false; }
		Intent i = new Intent(serviceName);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfoList = pm.queryIntentServices(i, 0);
		if (resolveInfoList == null || resolveInfoList.size() == 0)
		{
			Log.e(TAG, "Error querying IntentService for "+serviceName);
			return false;
		}
		else
		{
			ResolveInfo resolveInfoNode = resolveInfoList.get(0);
			ComponentName component = new ComponentName(resolveInfoNode.serviceInfo.packageName, resolveInfoNode.serviceInfo.name);
			i.setComponent(component);
			context.bindService(i, mConnection, Context.BIND_AUTO_CREATE);
		}
		return true;
	}

	private boolean unbindService(Context context)
	{
		if (context == null) { return false; }
		if (mConnection != null && mBind)
		{
			if(mQualcommService != null && mQualcommMessenger != null)
			{
				try
				{
					mQualcommService.unregisterMessenger(mQualcommService.getSupportedSignalIds(), mQualcommMessenger);
				} catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			context.unbindService(mConnection);
		}
		return true;
	}
	
	private ServiceConnection mConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			// TODO Auto-generated method stub
			Log.v(TAG, "onServiceConnected");
			mBind = true;
			mQualcommService = IVehicleService.Stub.asInterface(service);
			if(mQualcommService != null)
			{
				try
				{
					mQualcommService.registerMessenger(mQualcommService.getSupportedSignalIds(), mQualcommMessenger, new int[]{0}, new int[]{0});
				} catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void onServiceDisconnected(ComponentName className)
		{
			Log.v(TAG, "onServiceDisconnected");
			mQualcommService = null;
			mBind = false;
		}
	};
	
	private class ServiceRespHandler extends Handler
	{
		//要看qualcomm service传上来的msg具体数据再确定实现方式
		@Override
		public void handleMessage(Message msg)
		{
			if(mQualcommService != null && mService != null)
			{
				//QualcommVehicleManager.mService.dispatchData(VehicleInterfaceProperties.VIM_MIN, value);
			}
		}
	}
	
}
