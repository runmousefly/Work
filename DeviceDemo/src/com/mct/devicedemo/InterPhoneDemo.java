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
import android.graphics.Matrix;
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

public class InterPhoneDemo extends Activity implements View.OnClickListener,OnSeekBarChangeListener,OnItemSelectedListener

{
	public static String TAG = "InterPhoneDemo";
	public MctDeviceManager mDeviceManager = null;
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();
	public int mMute = 0;
	public int mKeyBeepStatus = 1;
	public int mGpsMixStatus = 1;
	public boolean mSeekBarEnable = false;
	public android.graphics.Point mPoint;
	
	int[] mSupportedPropIds = null;
	int[] mWritablePropIds = null;
	int[] mPropDataType = null;
	int[] mMcuProperties = new int[]{
			DeviceInterfaceProperties.DIM_INTERPHONE_CHANNEL_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_RECEIVE_VOLUME_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_SCAN_FREQ_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_DEVICE_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_SIGNAL_STRENGTH_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_CALLOUT_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_CALL_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_CALLING_CONTACTS_INFO_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_SEND_MESSAGE_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_ALARM_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_EXTERN_REQ_CHECK_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_EXTERN_CALL_PROMPT_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_EXTERN_REMOTE_MONITOR_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_EXTERN_REMOTE_KILL_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_EXTERN_ACTIVE_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_MIC_GAIN_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_POWER_MODE_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_COMN_FREQ_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_RELAY_MODE_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_SQUELCH_LEVEL_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_TONE_TYPE_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_TONE_FREQ_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_MONITOR_STATUS_PROPERTY	,
			DeviceInterfaceProperties.DIM_INTERPHONE_SENDING_POWER_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_CALL_CONTACT_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_ENCRYPTION_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_INIT_STATUS_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_CONTACTS_INFO_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_DEVICE_NUMBER_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_VERSION_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY,
			DeviceInterfaceProperties.DIM_INTERPHONE_REQ_CMD_PROPERTY
			};
	
	public Button mSetSignalBtn1 = null;
	public Button mSetSignalBtn2 = null;
	public Button mSetSignalBtn3 = null;
	public Button mReqStatusBtn = null;
	public Button mReqInitBtn = null;
	public Button mReqVersionBtn = null;
	public Button mVolumeUpBtn = null;
	public Button mVolumeDownBtn = null;
	public Button mMicGainUpBtn = null;
	public Button mMicGainDownBtn = null;
	public Button mCallOnBtn = null;
	public Button mCallOffBtn = null;
	public Button mReqLocalNumberBtn = null;
	public Button mSetConnFreqBtn1 = null;
	public Button mSetConnFreqBtn2 = null;
	public Button mSetConnFreqBtn3 = null;
	public Button mSetToneNoneBtn = null;
	public Button mSetToneABtn = null;
	public Button mSetToneDBtn = null;
	public Button mSetToneBtn1 = null;
	public Button mSetToneBtn2 = null;
	public Button mSetToneBtn3 = null;
	public Button mSetContactBtn1 = null;
	public Button mSetContactBtn2 = null;
	public Button mReqContacts = null;
	public Button mSetSendHighPowerBtn = null;
	public Button mSetSendLowPowerBtn = null;
	public Button mSavePowerOn = null;
	public Button mSavePowerOff = null;
	public Button mSquelchNormalBtn = null;
	public Button mSquelchAlwaysBtn = null;
	public Button mSquelchSteadyBtn = null;
	
	
	TextView mLogText = null;
	
	private int mReceiveVolume = 5;
	private int mMicGain = 0;
	
	public class VehicleDataNotification implements DeviceInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(final int propId,final String value)
		{
			// TODO Auto-generated method stub
			String logText = "PropId:"+propId+",value:"+value;
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
		setContentView(R.layout.activity_inter_phone_demo);
		
		mSetSignalBtn1 = (Button)this.findViewById(R.id.setSignal1);
		mSetSignalBtn1.setOnClickListener(this);
		
		mSetSignalBtn2 = (Button)this.findViewById(R.id.setSignal2);
		mSetSignalBtn2.setOnClickListener(this);
		
		mSetSignalBtn3 = (Button)this.findViewById(R.id.setSignal3);
		mSetSignalBtn3.setOnClickListener(this);
		
		mReqInitBtn = (Button)this.findViewById(R.id.QueryInitStatus);
		mReqInitBtn.setOnClickListener(this);
		
		mReqVersionBtn = (Button)this.findViewById(R.id.QueryVersion);
		mReqVersionBtn.setOnClickListener(this);
		
		mVolumeUpBtn = (Button)this.findViewById(R.id.VolumeUp);
		mVolumeUpBtn.setOnClickListener(this);
		
		mVolumeDownBtn = (Button)this.findViewById(R.id.VolumeDown);
		mVolumeDownBtn.setOnClickListener(this);
		
		mCallOnBtn = (Button)this.findViewById(R.id.CallOn);
		mCallOnBtn.setOnClickListener(this);
		
		mCallOffBtn = (Button)this.findViewById(R.id.CallOff);
		mCallOffBtn.setOnClickListener(this);
		
		mReqStatusBtn = (Button)this.findViewById(R.id.QueryStatus);
		mReqStatusBtn.setOnClickListener(this);
		 
		mMicGainUpBtn = (Button)this.findViewById(R.id.MicGainUp);
		mMicGainUpBtn.setOnClickListener(this);
		
		mMicGainDownBtn = (Button)this.findViewById(R.id.MicGainDown);
		mMicGainDownBtn.setOnClickListener(this);
		
		mReqLocalNumberBtn = (Button)this.findViewById(R.id.QueryLocalNumber);
		mReqLocalNumberBtn.setOnClickListener(this);
		
		mSetConnFreqBtn1 = (Button)this.findViewById(R.id.SetFreq1);
		mSetConnFreqBtn1.setOnClickListener(this);
		
		mSetConnFreqBtn2 = (Button)this.findViewById(R.id.SetFreq2);
		mSetConnFreqBtn2.setOnClickListener(this);
		
		mSetConnFreqBtn3 = (Button)this.findViewById(R.id.SetFreq3);
		mSetConnFreqBtn3.setOnClickListener(this);
		
		mSetToneABtn = (Button)this.findViewById(R.id.Tone_A);
		mSetToneABtn.setOnClickListener(this);
		
		mSetToneNoneBtn = (Button)this.findViewById(R.id.Tone_None);
		mSetToneNoneBtn.setOnClickListener(this);
		
		mSetToneDBtn = (Button)this.findViewById(R.id.Tone_D);
		mSetToneDBtn.setOnClickListener(this);
		
		mSetToneBtn1 = (Button)this.findViewById(R.id.Tone_Freq1);
		mSetToneBtn1.setOnClickListener(this);
		
		mSetToneBtn2 = (Button)this.findViewById(R.id.Tone_Freq2);
		mSetToneBtn2.setOnClickListener(this);
		
		mSetToneBtn3 = (Button)this.findViewById(R.id.Tone_Freq3);
		mSetToneBtn3.setOnClickListener(this);
		
		mSetContactBtn1 = (Button)this.findViewById(R.id.SetContants1);
		mSetContactBtn1.setOnClickListener(this);
		
		mSetContactBtn2 = (Button)this.findViewById(R.id.SetContants2);
		mSetContactBtn2.setOnClickListener(this);
		
		mReqContacts = (Button)this.findViewById(R.id.QueryContacts);
		mReqContacts.setOnClickListener(this);
		
		mSetSendHighPowerBtn = (Button)this.findViewById(R.id.HightSendPower);
		mSetSendHighPowerBtn.setOnClickListener(this);
		
		mSetSendLowPowerBtn = (Button)this.findViewById(R.id.LowSendPower);
		mSetSendLowPowerBtn.setOnClickListener(this);
		
		mSavePowerOn = (Button)this.findViewById(R.id.SavePowerOn);
		mSavePowerOn.setOnClickListener(this);
		
		mSavePowerOff = (Button)this.findViewById(R.id.SavePowerOff);
		mSavePowerOff.setOnClickListener(this);
		
		mSquelchNormalBtn = (Button)this.findViewById(R.id.NormalSquelch);
		mSquelchNormalBtn.setOnClickListener(this);
		
		mSquelchAlwaysBtn = (Button)this.findViewById(R.id.SquelchAlways);
		mSquelchAlwaysBtn.setOnClickListener(this);
		
		mSquelchSteadyBtn = (Button)this.findViewById(R.id.SquelchSteady);
		mSquelchSteadyBtn.setOnClickListener(this);

		
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
		if(v == mSetSignalBtn1)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_CHANNEL_PROPERTY, "1");
			}
		}
		else if(v == mSetSignalBtn2)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_CHANNEL_PROPERTY, "2");
			}
		}
		else if(v == mSetSignalBtn3)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_CHANNEL_PROPERTY, "10");
			}
		}
		else if(v == mReqInitBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_QUERY_INIT_STATUS));
			}
		}
		else if(v == mReqVersionBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_REQ_CMD_PROPERTY, String.valueOf(DevicePropertyConstants.CMD_QUERY_VERSION));
			}
		}
		else if(v == mVolumeUpBtn)
		{
			if (mDeviceManager != null)
			{
				mReceiveVolume++;
				mReceiveVolume = Math.min(9, Math.max(1,mReceiveVolume));
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_RECEIVE_VOLUME_PROPERTY, String.valueOf(mReceiveVolume));
			}
		}
		else if(v == mVolumeDownBtn)
		{
			if (mDeviceManager != null)
			{
				mReceiveVolume--;
				mReceiveVolume = Math.min(9, Math.max(1,mReceiveVolume));
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_RECEIVE_VOLUME_PROPERTY, String.valueOf(mReceiveVolume));
			}
		}
		else if(v == mCallOnBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_CALLOUT_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{0x01,DevicePropertyConstants.CALL_TYPE_BROADCAST,0}));
			}
		}
		else if(v == mCallOffBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_CALLOUT_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{0x00,DevicePropertyConstants.CALL_TYPE_BROADCAST,0}));
			}
		}
		else if(v== mReqStatusBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_REQ_CMD_PROPERTY, 
						String.valueOf(DevicePropertyConstants.CMD_QUERY_MODULE_STATUS));
			}
		}
		else if(v == mMicGainUpBtn)
		{
			if (mDeviceManager != null)
			{
				mMicGain++;
				mMicGain = Math.min(16, Math.max(0,mMicGain));
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_MIC_GAIN_PROPERTY, 
						String.valueOf(mMicGain));
			}
		}
		else if(v == mMicGainDownBtn)
		{
			if (mDeviceManager != null)
			{
				mMicGain--;
				mMicGain = Math.min(16, Math.max(0,mMicGain));
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_MIC_GAIN_PROPERTY, 
						String.valueOf(mMicGain));
			}
		}
		else if(v== mReqLocalNumberBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_REQ_CMD_PROPERTY, 
						String.valueOf(DevicePropertyConstants.CMD_QUERY_LOCAL_NUMBER));
			}
		}
		else if(v == mSetConnFreqBtn1)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_COMN_FREQ_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{415750000,415750000}));
			}
		}
		else if(v == mSetConnFreqBtn2)
		{
			
		}
		else if(v == mSetConnFreqBtn3)
		{
			
		}
		else if(v == mSetToneNoneBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_TONE_TYPE_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{DevicePropertyConstants.TONE_TYPE_NONE,DevicePropertyConstants.TONE_TYPE_NONE}));
			}
		}
		else if(v == mSetToneABtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_TONE_TYPE_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{DevicePropertyConstants.TONE_TYPE_ANALOG,DevicePropertyConstants.TONE_TYPE_ANALOG}));
			}
		}
		else if(v == mSetToneDBtn)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_TONE_TYPE_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{DevicePropertyConstants.TONE_TYPE_DIGIT,DevicePropertyConstants.TONE_TYPE_DIGIT}));
			}
		}
		else if(v == mSetToneBtn1)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_TONE_FREQ_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{1,2}));
			}
		}
		else if(v == mSetToneBtn2)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_TONE_FREQ_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{3,4}));
			}
		}
		else if(v == mSetToneBtn3)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_TONE_FREQ_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{5,6}));
			}
		}
		else if(v == mSetContactBtn1)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_CALL_CONTACT_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{DevicePropertyConstants.CALL_TYPE_PERSONAL,1}));
			}
		}
		else if(v == mSetContactBtn2)
		{
			if (mDeviceManager != null)
			{
				mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_CALL_CONTACT_PROPERTY, 
						MctDeviceManager.intArrayToString(new int[]{DevicePropertyConstants.CALL_TYPE_GROUP,10}));
			}
		}
		else if(v == mReqContacts)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_REQ_CMD_PROPERTY, 
					String.valueOf(DevicePropertyConstants.CMD_QUERY_CONTACTS));
		}
		else if(v == mSetSendHighPowerBtn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_SENDING_POWER_PROPERTY, 
					String.valueOf(DevicePropertyConstants.SEND_POWER_HIGH));
		}
		else if(v == mSetSendLowPowerBtn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_SENDING_POWER_PROPERTY, 
					String.valueOf(DevicePropertyConstants.SEND_POWER_LOWER));
		}
		else if(v == mSavePowerOn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_POWER_MODE_PROPERTY, 
					String.valueOf(DevicePropertyConstants.SAVE_POWER_ON));
		}
		else if(v == mSavePowerOff)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_SENDING_POWER_PROPERTY, 
					String.valueOf(DevicePropertyConstants.SAVE_POWER_OFF));
		}
		else if(v == mSquelchNormalBtn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_SQUELCH_LEVEL_PROPERTY, 
					String.valueOf(DevicePropertyConstants.SQUELCH_LEVEL_NORMAL));
		}
		else if(v == mSquelchAlwaysBtn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_SQUELCH_LEVEL_PROPERTY, 
					String.valueOf(DevicePropertyConstants.SQUELCH_LEVEL_ALWAYS));
		}
		else if(v == mSquelchSteadyBtn)
		{
			mDeviceManager.setProperty(DeviceInterfaceProperties.DIM_INTERPHONE_SQUELCH_LEVEL_PROPERTY, 
					String.valueOf(DevicePropertyConstants.SQUELCH_LEVEL_STEADY));
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
