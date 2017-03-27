package com.mct.devicedemo;

import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.List;

import com.mct.DeviceInterfaceDataHandler;
import com.mct.DeviceInterfaceProperties;
import com.mct.DevicePropertyConstants;
import com.mct.MctDeviceManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RangingDemo extends Activity implements View.OnClickListener,OnSeekBarChangeListener,OnItemSelectedListener

{
	public static String TAG = "RangingDemo";
	public MctDeviceManager mDeviceManager = null;
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();
	public boolean mSeekBarEnable = false;
	public android.graphics.Point mPoint;
	
	int[] mSupportedPropIds = null;
	int[] mWritablePropIds = null;
	int[] mPropDataType = null;
	int[] mMcuProperties = new int[]{DeviceInterfaceProperties.DIM_RANGING_DISTANCE_PROPERTY,
			DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_RANGING_TEMP_PROPERTY,
			DeviceInterfaceProperties.DIM_RANGING_VOLTAGE_PROPERTY,
			DeviceInterfaceProperties.DIM_RANGING_RET_ERROR_CODE_PROPERTY
			};
	
	public Button mOpenBtn = null;
	public Button mCloseBtn = null;
	public Button mEnableModuleBtn = null;
	public Button mDisableModuleBtn = null;
	public Button mEnableFillLightBtn = null;
	public Button mDisableFillLightBtn = null;
	public Button mReqRangingBtn = null;
	public Button mReqModuleInfoBtn= null;
	
	TextView mLogText = null;
	
	public class VehicleDataNotification implements DeviceInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(final int propId,final String value)
		{
			// TODO Auto-generated method stub
			String logText = "onDataUpdate,PropId:"+propId+",value:"+value;
			Log.i(TAG, logText);
			printUILog(logText);
		}
		
		@Override
		public void onError(boolean bCleanUpAndRestart)
		{
			// TODO Auto-generated method stub
			Log.e(TAG, "onError,bCleanUpAndRestart:" + bCleanUpAndRestart);
		}


	}

	private String toString(int[] array)
	{
		if (array == null || array.length == 0) { return null; }
		String str = new String();
		for (int i = 0; i < array.length; i++)
		{
			if (i > 0)
			{
				str += " ,";
			}
			str += array[i];
		}
		return str;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranging_demo);
		
		mOpenBtn = (Button)this.findViewById(R.id.open);
		mOpenBtn.setOnClickListener(this);
		
		mCloseBtn = (Button)this.findViewById(R.id.close);
		mCloseBtn.setOnClickListener(this);
		
		mEnableModuleBtn = (Button)this.findViewById(R.id.enableModule);
		mEnableModuleBtn.setOnClickListener(this);
		
		mDisableModuleBtn = (Button)this.findViewById(R.id.disableModule);
		mDisableModuleBtn.setOnClickListener(this);
		
		mEnableFillLightBtn = (Button)this.findViewById(R.id.enableFillLight);
		mEnableFillLightBtn.setOnClickListener(this);
		
		mDisableFillLightBtn = (Button)this.findViewById(R.id.disableFillLight);
		mDisableFillLightBtn.setOnClickListener(this);
		
		mReqRangingBtn = (Button)this.findViewById(R.id.reqRanging);
		mReqRangingBtn.setOnClickListener(this);
		
		mReqModuleInfoBtn = (Button)this.findViewById(R.id.reqModuleInfo);
		mReqModuleInfoBtn.setOnClickListener(this);
		
		
		mLogText = (TextView)this.findViewById(R.id.logView);
		mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		mDeviceManager = MctDeviceManager.getInstance();
		if(mDeviceManager == null)
		{
			Log.e(TAG, "get Device Instace failed");
			return;
		}
		Log.i(TAG, "get Device Instace success");
		mSupportedPropIds = mDeviceManager.getSupportedPropertyIds();
		mWritablePropIds = mDeviceManager.getWritablePropertyIds();
		mPropDataType = mDeviceManager.getPropertiesDataType(mSupportedPropIds);
		Log.i(TAG, "getSupportedPropIds:" + toString(mSupportedPropIds));
		Log.i(TAG, "getWritablePropIds:" + toString(mWritablePropIds));
		Log.i(TAG, "getPropDataType:" + toString(mPropDataType));
		
		mDeviceManager.registerHandler(mMcuProperties, mDataNotification);
	}

	@Override
	protected void onResume()
	{
		Log.i(TAG, "onResume");
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		Log.i(TAG, "onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mDeviceManager != null && mMcuProperties != null)
		{
			mDeviceManager.removeHandler(mMcuProperties, mDataNotification);
		}

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
		if(v == mOpenBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_OPEN_LASER));
			}
		}
		else if(v == mCloseBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_CLOSE_LASER));
			}
		}
		if(v == mEnableModuleBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_ENABLE_LASER_POWER));
			}
		}
		else if(v == mDisableModuleBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_DISABLE_LASER_POWER));
			}
		}
		//打开补光灯
		if(v == mEnableFillLightBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_HARDWARE_FUNCTON_SWITCH_PROPERTY, String.valueOf(DevicePropertyConstants.FUN_ID_FILLLIGHT_ON));
			}
		}
		//关闭补光灯
		else if(v == mDisableFillLightBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_HARDWARE_FUNCTON_SWITCH_PROPERTY, String.valueOf(DevicePropertyConstants.FUN_ID_FILLLIGHT_OFF));
			}
		}
		else if(v == mReqRangingBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_RANGING));
			}
		}
		else if(v == mReqModuleInfoBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_REQ_MODULE_INFO));
			}
		}
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
//		if(view == mPresetModeSpinner && mDeviceManager != null)
//		{
//			mDeviceManager.setProperty(VehicleInterfaceProperties.VIM_MCU_EQ_PRESET_MODE_PROPERTY, String.valueOf(position));
//		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		// TODO Auto-generated method stub
		if(!fromUser || mDeviceManager == null)
		{
			return;
		}
//		if(seekBar == mMediaVolumeSeekBar)
//		{
//			mMediaVolume = progress;
//			mDeviceManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY, String.valueOf(progress));
//			mMediaVolumeText.setText(String.valueOf(progress));
//		}
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
		
	}
	
	
	private void printUILog(String logText)
	{
		if(mLogText != null)
		{			
			mLogText.append("["+mLogText.getLineCount()+"] "+logText+"\n");
			int offset=mLogText.getLineCount()*mLogText.getLineHeight();
            if(offset>mLogText.getHeight())
            {
            	mLogText.scrollTo(0,offset-mLogText.getHeight());
            }
		}
	}

}
