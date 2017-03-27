package com.mct.vehicle.demo;

import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.List;

import com.mct.VehicleInterfaceDataHandler;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.vehicle.demo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RadioActivity extends Activity implements View.OnClickListener,OnSeekBarChangeListener,OnItemSelectedListener

{
	public static String TAG = "RadioDemo";
	public VehicleManager mVehicleManager = null;
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();

	int[] mSupportedPropIds = null;
	int[] mWritablePropIds = null;
	int[] mPropDataType = null;
	int[] mRadioProperties = new int[]{VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_ST_KEY_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_ST_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_LOC_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_TP_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_TA_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_AF_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_PS_INFO_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_TYPE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_PTY_LIST_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_FREQ_LIST_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_FM_MAX_FREQ_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_FM_MIN_FREQ_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_FM_STEP_VALUE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_AM_MAX_FREQ_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_AM_MIN_FREQ_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_AM_STEP_VALUE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_STOP_SENS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY};

	public Button mRadioStatusBtn = null;
	public Button mCurBandBtn = null;
	public Button mCurFreqBtn = null;
	public Button mSterioStatusBtn = null;
	public Button mPwKeyBtn = null;
	public Button mStopSensBtn = null;
	public Button mSTStatusBtn = null;
	public Button mLOCStatusBtn = null;
	public Button mTPStatusBtn = null;
	public Button mTAStatusBtn = null;
	public Button mAFStatusBtn = null;
	
	public Button mSelectChannelBtn = null;
	
	public Button mRadioInitBtn = null;;
	public Button mRadioExitBtn = null;;
	public Button mBandBtn = null;;
	public Button mNPreBtn = null;
	public Button mPPreBtn = null;
	public Button mStepUpBtn = null;
	public Button mStepDownBtn = null;
	public Button mSearchUpBtn = null;
	public Button mSearchDownBtn = null;
	public Button mScanBtn = null;
	public Button mAutoSearchBtn = null;
	public Button mStopSearchBtn = null;
	
	public Button mFMAMBtn = null;
	public Button mFMBtn = null;
	public Button mAMBtn = null;
	public Button mCurRegionBtn = null;
	public Button mReqRegionBtn = null;
	public Button mReqRadioOnBtn = null;
	public Button mReqRadioOffBtn = null;
	
	public EditText mRadioStatusText = null;
	public EditText mCurBandText = null;
	public EditText mCurFreqText = null;
	public EditText mSterioStatusText = null;
	public EditText mPwKeyText = null;
	public EditText mStopSensText = null;
	public EditText mStStatusText = null;
	public EditText mLocStatusText = null;
	public EditText mTpStatusText = null;
	public EditText mTaStatusText = null;
	public EditText mAFStatusText = null;
	public EditText mSelectChannelText = null;
	public EditText mFreqListText = null;
	public EditText mCurRegionText = null;
	
	public EditText mFMMaxFreqText = null;
	public EditText mFMMinFreqText = null;
	public EditText mFMStepValueText = null;
	public EditText mAMMaxFreqText = null;
	public EditText mAMMinFreqText = null;
	public EditText mAMStepValueText = null; 

	public boolean mRadioEnable = false;
	
	private int mStopSens = 15;//15 - 25 - 40;
	
	public class VehicleDataNotification implements VehicleInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(int propId,String value)
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "onDataUpdate,PropId:"+propId+",value:"+value);
			switch(propId)
			{
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_STATE_PROPERTY:
					mRadioStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY:
					mCurBandText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY:
					mCurFreqText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY:
					mCurRegionText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_ST_KEY_STATUS_PROPERTY:
					mSterioStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_STOP_SENS_PROPERTY:
					mStopSensText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_ST_STATUS_PROPERTY:
					mStStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_LOC_STATUS_PROPERTY:
					mLocStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_TP_STATUS_PROPERTY:
					mTpStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_TA_STATUS_PROPERTY:
					mTaStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_AF_STATUS_PROPERTY:
					mAFStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_FM_MAX_FREQ_PROPERTY:
					mFMMaxFreqText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_FM_MIN_FREQ_PROPERTY:
					mFMMinFreqText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_FM_STEP_VALUE_PROPERTY:
					mFMStepValueText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_AM_MAX_FREQ_PROPERTY:
					mAMMaxFreqText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_AM_MIN_FREQ_PROPERTY:
					mAMMinFreqText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_AM_STEP_VALUE_PROPERTY:
					mAMStepValueText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_FREQ_LIST_PROPERTY:
					mFreqListText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY:
					mSelectChannelText.setText(value);
					break;
				default:
					break;
			}
		}
		
		@Override
		public void onError(boolean bCleanUpAndRestart)
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "onError,bCleanUpAndRestart:" + bCleanUpAndRestart);
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
		setContentView(R.layout.activity_radio);
		
		mRadioStatusBtn = (Button) findViewById(R.id.radioStatus);
		mCurBandBtn = (Button) findViewById(R.id.currentBand);	
		mCurFreqBtn = (Button) findViewById(R.id.currentFreq);
		mSterioStatusBtn = (Button) findViewById(R.id.sterioStatus);
		mPwKeyBtn = (Button)findViewById(R.id.pwKeyStatus);
		mStopSensBtn = (Button)findViewById(R.id.stopSens);
		mSTStatusBtn = (Button)findViewById(R.id.stStatus);
		mLOCStatusBtn = (Button)findViewById(R.id.locStatus);
		mTPStatusBtn = (Button)findViewById(R.id.tpStatus);
		mTAStatusBtn = (Button)findViewById(R.id.taStatus);
		mAFStatusBtn = (Button)findViewById(R.id.afStatus);
		
		mSelectChannelBtn = (Button)findViewById(R.id.selectChannel);
		mReqRadioOnBtn = (Button)findViewById(R.id.reqRadioOn);
		mReqRadioOffBtn = (Button)findViewById(R.id.reqRadioOff);
		
	
		mRadioInitBtn = (Button)findViewById(R.id.radioInit);
		mRadioExitBtn = (Button)findViewById(R.id.radioStop);
		mBandBtn = (Button)findViewById(R.id.band);
		mNPreBtn = (Button)findViewById(R.id.nPre);
		mPPreBtn = (Button)findViewById(R.id.pPre);
		mStepUpBtn = (Button)findViewById(R.id.stepUp);
		mStepDownBtn = (Button)findViewById(R.id.stepDown);
		mSearchUpBtn = (Button)findViewById(R.id.searchUp);
		mSearchDownBtn = (Button)findViewById(R.id.searchDown);
		mScanBtn = (Button)findViewById(R.id.scan);
		mAutoSearchBtn = (Button)findViewById(R.id.autoSearch);
		mStopSearchBtn = (Button)findViewById(R.id.stopSearch);
		
		mFMAMBtn = (Button)findViewById(R.id.fm_am);
		mFMBtn = (Button)findViewById(R.id.fm);
		mAMBtn = (Button)findViewById(R.id.am);
		mCurRegionBtn = (Button)findViewById(R.id.currentRegion);
		mReqRegionBtn = (Button)findViewById(R.id.reqRadioRegion);
				
		mRadioInitBtn.setOnClickListener(this);
		mCurBandBtn.setOnClickListener(this);
		mCurFreqBtn.setOnClickListener(this);
		mSterioStatusBtn.setOnClickListener(this);
		mPwKeyBtn.setOnClickListener(this);
		mStopSensBtn.setOnClickListener(this);
		mSTStatusBtn.setOnClickListener(this);
		mLOCStatusBtn.setOnClickListener(this);
		mTPStatusBtn.setOnClickListener(this);
		mTAStatusBtn.setOnClickListener(this);
		mAFStatusBtn.setOnClickListener(this);
		
		mSelectChannelBtn.setOnClickListener(this);
		mReqRadioOnBtn.setOnClickListener(this);
		mReqRadioOffBtn.setOnClickListener(this);

		mFMAMBtn.setOnClickListener(this);
		mFMBtn.setOnClickListener(this);
		mAMBtn.setOnClickListener(this);
		mCurRegionBtn.setOnClickListener(this);
		mReqRegionBtn.setOnClickListener(this);
		
		mRadioInitBtn.setOnClickListener(this);
		mRadioExitBtn.setOnClickListener(this);
		mBandBtn.setOnClickListener(this);
		mNPreBtn.setOnClickListener(this);
		mPPreBtn.setOnClickListener(this);
		mStepUpBtn.setOnClickListener(this);
		mStepDownBtn.setOnClickListener(this);
		mSearchUpBtn.setOnClickListener(this);
		mSearchDownBtn.setOnClickListener(this);
		mScanBtn.setOnClickListener(this);
		mAutoSearchBtn.setOnClickListener(this);
		mStopSearchBtn.setOnClickListener(this);
		
		
		mRadioStatusText = (EditText)findViewById(R.id.radioStatusText);
		mCurBandText = (EditText)findViewById(R.id.currentBandText);
		mCurFreqText = (EditText)findViewById(R.id.currentFreqText);
		mSterioStatusText = (EditText)findViewById(R.id.sterioStatusText);
		mPwKeyText = (EditText)findViewById(R.id.pwKeyStatusText);
		mStopSensText = (EditText)findViewById(R.id.stopSensText);
		mStStatusText = (EditText)findViewById(R.id.stText);
		mLocStatusText = (EditText)findViewById(R.id.locText);
		mTpStatusText = (EditText)findViewById(R.id.tpText);
		mTaStatusText = (EditText)findViewById(R.id.taText);
		mAFStatusText = (EditText)findViewById(R.id.afText);
		mSelectChannelText = (EditText)findViewById(R.id.selectChannelText);
		mFreqListText = (EditText)findViewById(R.id.freqListText);
		mCurRegionText = (EditText)findViewById(R.id.currentRegionText);
		
		mFMMaxFreqText = (EditText)findViewById(R.id.fmMaxFreqText);
		mFMMinFreqText = (EditText)findViewById(R.id.fmMinFreqText);
		mFMStepValueText = (EditText)findViewById(R.id.fmStepValueText);
		mAMMaxFreqText = (EditText)findViewById(R.id.amMaxFreqText);
		mAMMinFreqText = (EditText)findViewById(R.id.amMinFreqText);
		mAMStepValueText = (EditText)findViewById(R.id.amStepValueText);
		
		
		mVehicleManager = VehicleManager.getInstance();
		if(mVehicleManager == null)
		{
			Log.e(TAG, "get Vehcle Instace failed");
			return;
		}
		Log.i(TAG, "get Vehcle Instace success");
//		mSupportedPropIds = mVehicleManager.getSupportedPropertyIds();
//		mWritablePropIds = mVehicleManager.getWritablePropertyIds();
//		mPropDataType = mVehicleManager.getPropertiesDataType(mSupportedPropIds);
//		Log.i(TAG, "getSupportedPropIds:" + toString(mSupportedPropIds));
//		Log.i(TAG, "getWritablePropIds:" + toString(mWritablePropIds));
//		Log.i(TAG, "getPropDataType:" + toString(mPropDataType));
		
		mVehicleManager.registerHandler(mRadioProperties, mDataNotification);
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
		if (mVehicleManager != null && mRadioProperties != null)
		{
			mVehicleManager.removeHandler(mRadioProperties, mDataNotification);
		}

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v == mRadioInitBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_ARM_AUDIO_CHANNEL_PROPERTY, String.valueOf("0"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("0"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf("0"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_INIT));
			}
		}
		else if(v == mRadioExitBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_EXIT));
			}
			
		}
		else if(v == mBandBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_BAND));
			}
		}
		else if(v == mFMAMBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_FM_AM));
			}
		}
		else if(v == mFMBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_FM));
			}
		}
		else if(v == mAMBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_AM));
			}
		}
		//设置收音区域
		else if(v == mCurRegionBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_REGION_PROPERTY,mCurRegionText.getText().toString());
			}
		}
		else if(v == mNPreBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_NEXT_PRE_CHANNEL));
			}
		}
		else if(v == mPPreBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_PRIO_PRE_CHANNEL));
			}
		}
		else if(v == mScanBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_SCAN));
			}
		}
		else if(v == mStepUpBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_STEP_UP));
			}
		}
		else if(v == mStepDownBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_STEP_DOWN));
			}
		}
		else if(v == mSearchUpBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_SEARCH_UP));
			}
			
		}
		else if(v == mSearchDownBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_SEARCH_DOWN));
			}
		}
		else if(v == mAutoSearchBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_AUTO_SEARCH));
			}
		}
		else if(v == mStopSearchBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_STOP_SEARCH));
			}
		}
		else if(v == mReqRegionBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_REQ_REGION));
			}
		}
		else if(v == mSterioStatusBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_SWITCH_STERIO));
			}
		}
		else if(v == mReqRadioOnBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_ENABLE_AUDIO));
			}
		}
		else if(v == mReqRadioOffBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_HANDLE_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.RADIO_CMD_DISABLE_AUDIO));
			}
		}
		else if(v == mSelectChannelBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_FOCUS_FREQ_INDEX_PROPERTY, mSelectChannelText.getText().toString());
			}
		}
		else if(v == mCurBandBtn)
		{
			if (mVehicleManager != null)
			{
				try
				{
					int band = Integer.valueOf(mCurBandText.getText().toString());
					if(band < 0 || band > 4)
					{
						return;
					}
					mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY,mCurBandText.getText().toString());
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				
			}
		}
		else if(v == mCurFreqBtn)
		{
			if (mVehicleManager != null)
			{
				try
				{
					int band = Integer.valueOf(mCurBandText.getText().toString());
					int freq = Integer.valueOf(mCurFreqText.getText().toString());
					setBandAndFreq(band, freq);
				} catch (NumberFormatException e)
				{
					// TODO: handle exception
				}
			}
		}
		else if(v == mStopSensBtn)
		{
			if (mVehicleManager != null)
			{
				try
				{
					if(mStopSens == 15)
					{
						mStopSens = 25;
					}
					else if(mStopSens == 25)
					{
						mStopSens = 40;
					}
					else if(mStopSens == 40)
					{
						mStopSens = 15;
					}
					mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_STOP_SENS_PROPERTY,String.valueOf(mStopSens));
				} catch (NumberFormatException e)
				{
					// TODO: handle exception
				}
			}
		}
	}

	//设置当前波段下的频率
	private void setCurrentFreq(int freq)
	{
		// TODO Auto-generated method stub
		setBandAndFreq(-1,freq);
	}
	
	//设置指定波段下的指定频率
	private void setBandAndFreq(int band,int freq)
	{
		if(band < 0 || band > 4)
		{
			band = -1;
		}
		mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY,VehicleManager.intArrayToString(new int[]{band,freq}));
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		
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
		if(!fromUser || mVehicleManager == null)
		{
			return;
		}
		
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


}
