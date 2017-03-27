package com.mct.vehicle.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mct.VehicleInterfaceDataHandler;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.vehicle.demo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.filterfw.core.FinalPort;
import android.filterfw.geometry.Point;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.os.Bundle;
import android.os.Handler;
import android.os.RecoverySystem;
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

public class McuDemoActivity extends Activity implements View.OnClickListener,OnSeekBarChangeListener,OnItemSelectedListener

{
	public static String TAG = "McuDemo";
	private final static String SHARE_PREFERNESS_NAME = TAG;
	private final static String KEY_MCU_UPGRADE_COUNT = "McuUpdateCount";
	private final static String KEY_BOOT_MODE = "0";
	public VehicleManager mVehicleManager = null;
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();
	public int mMute = 0;
	public int mKeyBeepStatus = 1;
	public int mGpsMixStatus = 1;
	public boolean mSeekBarEnable = false;
	public android.graphics.Point mPoint;
	private int mMcuUpgradeCount = 0;
	
	int[] mSupportedPropIds = null;
	int[] mWritablePropIds = null;
	int[] mPropDataType = null;
	int[] mMcuProperties = new int[]{VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY,VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY,VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY,VehicleInterfaceProperties.VIM_MCU_BT_PHONE_VOLUME_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_NAVIGATION_VOLUME_PROPERTY,VehicleInterfaceProperties.VIM_MCU_GPS_MIX_LEVEL_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_KEY_BEEP_SWITCH_PROPERTY,VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY,VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY,VehicleInterfaceProperties.VIM_MCU_GPS_MIX_ENABLE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_STATE_PROPERTY,VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_FUNC_ID_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY,VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY,
			VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_INFO_COLLECT_PROPERTY,VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_INFO_COLLECT_PROPERTY};
			

	public Button mReqMcuVersionBtn;
	public Button mReqMcuLastSleepReasonBtn;
	public Button mReqMcuWakeupModeBtn;
	public Button mReqMcuVoltageBtn;
	public Button mReqMcuUpdateBtn = null;
	public Button mReqMediaVolumeBtn = null;
	public Button mReqBtPhoneVolumeBtn = null;
	public Button mReqNavVolumeBtn = null;
	public Button mReqPresetModeBtn = null;
	
	public Button mBeginStudyBtn = null;
	public Button mExitStudyBtn = null;
	public Button mSaveStudyBtn = null;
	
	public Button mResetStudySettingBtn = null;
	public Button mClearStudySettingBtn = null;
	public Button mGetStudyInfoBtn = null;
	
	public EditText mStudyKeyEText = null;
	public Button mStudyKeyBtn = null;
	
	public EditText mMcuVersionEText = null;
	public EditText mMcuVoltageEText = null;
	public EditText mMcuSleepReasonEText = null;
	public EditText mMcuUpdateText = null;
	public EditText mMcuStateEText = null;
	public TextView mMediaVolumeText = null;
	public TextView mBtPhoneVolumeText = null;
	public TextView mNavVolumeText = null;
	

	public SeekBar mMediaVolumeSeekBar = null;
	public SeekBar mBtPhoneVolumeSeekBar = null;
	public SeekBar mNavVolumeSeekBar = null;
	public Spinner mPresetModeSpinner = null;
	private int mMediaVolume = 0;
	private int mBtPhoneVolume = 0;
	private int mNaviVolume = 0;
	private int mBacklightBrightness  = 0;
	private int mNaviMixLevel = 7;
	private int mNaviArmGain = 0;
	
	public Button mReqNaviMixLevelBtn = null;
	public TextView mNaviMixLevelText = null;
	public SeekBar mNaviMixLevelSeekBar = null;
	
	public Button mNaviArmGainBtn = null;
	public TextView mNaviArmGainText = null;
	public SeekBar mNaviArmGainSeekBar = null;
	
	public Button mReqGpsMixStatusBtn = null;
	public EditText mGpsMixStatusText = null;
	
	public Button mOpenSerialBtn = null;
	public Button mCloseSerialBtn = null;
	public Button mMcuResetBtn = null;
	
	public Button mTunerSourceBtn = null;
	public Button mAudioSourceBtn = null;
	public Button mVideoSourceBtn = null;
	
	public Button mAux1SourceBtn = null;
	public Button mAux2SourceBtn = null;
	public Button mDVRSourceBtn = null;
	
	public Button mBtPhoneSourceBtn = null;
	public Button mBtMusicSourceBtn = null;
	public Button mNavigationSourceBtn = null;
	
	public Button mReqBacklightBrightnessBtn = null;
	public SeekBar mBacklightBrightnessSeekBar = null;
	public TextView mBacklightBrightnessText = null;
	
	public Button mTTSOnBtn = null;
	public Button mTTSOffBtn = null;
	public Button mMuteBtn = null;;
	
	public Button mScreenOn = null;
	public Button mScreenOff = null;
	public Button mScreenAuto = null;
	public Button mReqKeyBeepStatusBtn = null;
	public Button mPlayKeyBeepBtn = null;
	public Button mKeyBeepSwitchBtn = null;
	
	public Button mReqAudioBalanceInfoBtn = null;
	public Button mReqEQBassInfoBtn = null;
	public Button mReqGPSInfoBtn = null;
	
	public Button mGetCacheValueBtn = null;
	public EditText mPropIdEditText = null;
	public EditText mPropValueEditText = null;
	
	public Handler mHandler;
	public Context mContext = null;
	public int mMcuUpgradeState = VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN;
	public float mMcuUpgradeProgress = 0;
	    
	private String[] mPresetModeArray = new String[]{"0.特效关","1.古典","2.流行","3.俱乐部","4.现场","5.用户",
			"6.爵士","7.轻柔","8.摇滚","9.迪斯科"};
	private ArrayAdapter<String> mPresetModeAdapter = null;
	
	public class VehicleDataNotification implements VehicleInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(final int propId,final String value)
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "onDataUpdate,PropId:"+propId+",value:"+value);
			switch(propId)
			{
				//菜单信息集
				case VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_INFO_COLLECT_PROPERTY:
					HashMap<Integer,String> syncMenuInfo = VehicleManager.stringToHashMap(value);
					if(syncMenuInfo != null)
					{
						//菜单图标
						String iconValue = syncMenuInfo.get(VehicleInterfaceProperties.VIM_FORD_SYNC_CUR_MENU_ICON_PROPERTY);
						//其它属性均如此解析出值
					}
					break;
					
				//菜单项信息集
				case VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_INFO_COLLECT_PROPERTY:
					HashMap<Integer,String> syncMenuItemInfo = VehicleManager.stringToHashMap(value);
					if(syncMenuItemInfo != null)
					{
						String lineId = syncMenuItemInfo.get(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_ID_PROPERTY);
						String lineContext = syncMenuItemInfo.get(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_CONTEXT_PROPERTY);
						Log.i(TAG, "receive menu item info,lineId:"+lineId+",lineContext:"+lineContext);
						mPropValueEditText.setText(lineContext);
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY:
					if(mMcuVersionEText != null)
					{
						mMcuVersionEText.setText(value);
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY:
					if(mMcuSleepReasonEText != null)
					{
						mMcuSleepReasonEText.setText(value);
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY:
					if(mMcuVoltageEText != null)
					{
						mMcuVoltageEText.setText(value);
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY:
					if(mMcuStateEText != null)
					{
						mMcuStateEText.setText(value);
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_GPS_MIX_ENABLE_PROPERTY:
					if(mGpsMixStatusText != null)
					{
						mGpsMixStatus = Integer.valueOf(value);
						mGpsMixStatusText.setText(value);
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_GPS_MIX_LEVEL_PROPERTY:
					if(mNaviMixLevelSeekBar != null && mNaviMixLevel != Integer.valueOf(value) && mSeekBarEnable)
					{
						mNaviMixLevel = Integer.valueOf(value);
						mNaviMixLevelSeekBar.setProgress(mNaviMixLevel);
						mNaviMixLevelText.setText(value);
						mSeekBarEnable = false;
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY:
					if(mMediaVolumeSeekBar != null && mMediaVolume != Integer.valueOf(value) && mSeekBarEnable)
					{
						mMediaVolume = Integer.valueOf(value);
						mMediaVolumeSeekBar.setProgress(mMediaVolume);
						mMediaVolumeText.setText(value);
						mSeekBarEnable = false;
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_BT_PHONE_VOLUME_PROPERTY:
					if(mBtPhoneVolumeSeekBar != null && mBtPhoneVolume != Integer.valueOf(value) && mSeekBarEnable)
					{
						mBtPhoneVolume = Integer.valueOf(value);
						mBtPhoneVolumeSeekBar.setProgress(mBtPhoneVolume);
						mBtPhoneVolumeText.setText(value);
						mSeekBarEnable = false;
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_NAVIGATION_VOLUME_PROPERTY:
					if(mNavVolumeSeekBar != null && mNaviVolume != Integer.valueOf(value) && mSeekBarEnable)
					{
						mNaviVolume = Integer.valueOf(value);
						mNavVolumeSeekBar.setProgress(mNaviVolume);
						mNavVolumeText.setText(value);
						mSeekBarEnable = false;
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY:
					if(mBacklightBrightnessSeekBar != null && mBacklightBrightness != Integer.valueOf(value) && mSeekBarEnable)
					{
						mBacklightBrightness = Integer.valueOf(value);
						mBacklightBrightnessSeekBar.setProgress(mBacklightBrightness);
						mBacklightBrightnessText.setText(value);
						mSeekBarEnable = false;
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY:
					Log.i(TAG, "receive backlight status:"+value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_KEY_BEEP_SWITCH_PROPERTY:
					mKeyBeepStatus = Integer.valueOf(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY:
					mMute = Integer.valueOf(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY:
					mMcuUpgradeState = Integer.valueOf(value);
					switch(mMcuUpgradeState)
					{
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN:
							mMcuUpdateText.setText("");
							break;
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_INIT:
							mMcuUpdateText.setText(R.string.McuUpgradeInit);
							break;
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_START:
							mMcuUpdateText.setText(R.string.McuUpgradeStart);
							break;
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_ERASE:
							mMcuUpdateText.setText(R.string.McuUpgradeErase);
							break;
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_WRITE:
							mMcuUpdateText.setText(R.string.McuUpgradeWriting);
							break;
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_VERTIFY:
							mMcuUpdateText.setText(R.string.McuUpgradeVertify);
							break;
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_SUCCESS:
							mMcuUpdateText.setText(R.string.McuUpgradeSuccess);
							if(mVehicleManager != null)
							{
								mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_RESET));
							}
							break;
						case VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED:
							saveSettings(KEY_MCU_UPGRADE_COUNT, 0);
							mMcuUpdateText.setText(R.string.McuUpgradeFailed);
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY:
					mMcuUpdateText.setText("MCU数据写入进度:"+value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_STATE_PROPERTY:
					Log.i(TAG, "SteerStudy Study State:"+value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_STEER_STUDIED_KEY_FUNC_ID_PROPERTY:
					Log.i(TAG, "Steer Key:"+value+" has studied");
					break;
				case VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY:
					Log.i(TAG, "User Key:"+value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_RADIO_AUDIO_STATUS_PROPERTY:
					Log.i(TAG, "receive radio audio status:"+value);
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
		setContentView(R.layout.activity_mcu);
		
		mContext = getApplicationContext();
		mHandler = new Handler(getMainLooper());
		mReqMcuVersionBtn = (Button)findViewById(R.id.mcuVersionBtn);
		mMcuVersionEText = (EditText)findViewById(R.id.mcuVersionText);
		mReqMcuVersionBtn.setOnClickListener(this);
		
		mReqMcuVoltageBtn = (Button)findViewById(R.id.mcuVoltageBtn);
		mMcuVoltageEText = (EditText)findViewById(R.id.mcuVoltageText);
		mReqMcuVoltageBtn.setOnClickListener(this);
		
		mReqMcuLastSleepReasonBtn = (Button)findViewById(R.id.mcuSleepReasonBtn);
		mMcuSleepReasonEText = (EditText)findViewById(R.id.mcuSleepReasonText);
		mReqMcuLastSleepReasonBtn.setOnClickListener(this);
		
		mReqMcuWakeupModeBtn = (Button)findViewById(R.id.mcuWakeModeBtn);
		mMcuStateEText = (EditText)findViewById(R.id.mcuWakeModeText);
		mReqMcuWakeupModeBtn.setOnClickListener(this);
		
		
		mReqMcuUpdateBtn = (Button)findViewById(R.id.mcuUpdateBtn);
		mMcuUpdateText = (EditText)findViewById(R.id.mcuUpdateText);
		mReqMcuUpdateBtn.setOnClickListener(this);
		
		mReqNaviMixLevelBtn = (Button)findViewById(R.id.naviMixLevelBtn);
		mNaviMixLevelText = (TextView)findViewById(R.id.naviMixLevelText);
		mNaviMixLevelSeekBar = (SeekBar)findViewById(R.id.naviMixLevelBar);
		mReqNaviMixLevelBtn.setOnClickListener(this);
		mNaviMixLevelSeekBar.setOnSeekBarChangeListener(this);
		
		mNaviArmGainBtn = (Button)findViewById(R.id.naviArmGainBtn);
		mNaviArmGainText = (TextView)findViewById(R.id.naviArmGainText);
		mNaviArmGainSeekBar = (SeekBar)findViewById(R.id.naviArmGainBar);
		mNaviArmGainBtn.setOnClickListener(this);
		mNaviArmGainSeekBar.setOnSeekBarChangeListener(this);
		
		mReqGpsMixStatusBtn = (Button)findViewById(R.id.naviMixStatus);
		mGpsMixStatusText = (EditText)findViewById(R.id.naviMixStatusText);
		mReqGpsMixStatusBtn.setOnClickListener(this);
		
		mReqMediaVolumeBtn = (Button)findViewById(R.id.mediaVolumeBtn);
		mMediaVolumeText = (TextView)findViewById(R.id.mediaVolumeText);
		mMediaVolumeSeekBar = (SeekBar)findViewById(R.id.mediaVolumeBar);
		mReqMediaVolumeBtn.setOnClickListener(this);
		mMediaVolumeSeekBar.setOnSeekBarChangeListener(this);
		
		mReqBtPhoneVolumeBtn = (Button)findViewById(R.id.btPhoneVolumeBtn);
		mBtPhoneVolumeText = (TextView)findViewById(R.id.btPhoneVolumeText);
		mBtPhoneVolumeSeekBar = (SeekBar)findViewById(R.id.btPhoneVolumeBar);
		mReqBtPhoneVolumeBtn.setOnClickListener(this);
		mBtPhoneVolumeSeekBar.setOnSeekBarChangeListener(this);
		
		mReqNavVolumeBtn = (Button)findViewById(R.id.navVolumeBtn);
		mNavVolumeText = (TextView)findViewById(R.id.navVolumeText);
		mNavVolumeSeekBar = (SeekBar)findViewById(R.id.navVolumeBar);
		mReqNavVolumeBtn.setOnClickListener(this);
		mNavVolumeSeekBar.setOnSeekBarChangeListener(this);
		
		mReqBacklightBrightnessBtn = (Button)findViewById(R.id. backlightBrightnessBtn);
		mBacklightBrightnessText = (TextView)findViewById(R.id.backlightBrightnessText);
		mBacklightBrightnessSeekBar = (SeekBar)findViewById(R.id.backlightBrightnessBar);
		mReqBacklightBrightnessBtn.setOnClickListener(this);
		mBacklightBrightnessSeekBar.setOnSeekBarChangeListener(this);
		
        mPresetModeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mPresetModeArray);    
        mPresetModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		mReqPresetModeBtn = (Button)findViewById(R.id.presetModeBtn);
		mPresetModeSpinner = (Spinner)findViewById(R.id.presetModeSpinner);
		mReqPresetModeBtn.setOnClickListener(this);
		mPresetModeSpinner.setAdapter(mPresetModeAdapter);
		mPresetModeSpinner.setOnItemSelectedListener(this);
		
		mScreenOn = (Button)findViewById(R.id.screenOn);
		mScreenOn.setOnClickListener(this);
		
		mScreenOff = (Button)findViewById(R.id.screenOff);
		mScreenOff.setOnClickListener(this);
		
		mScreenAuto = (Button)findViewById(R.id.screenAuto);
		mScreenAuto.setOnClickListener(this);
		
		
		mOpenSerialBtn = (Button)findViewById(R.id.openSerialBtn);
		mOpenSerialBtn.setOnClickListener(this);
		
		mCloseSerialBtn = (Button)findViewById(R.id.closeSerialBtn);
		mCloseSerialBtn.setOnClickListener(this);
		
		mMcuResetBtn = (Button)findViewById(R.id.mcuResetBtn);
		mMcuResetBtn.setOnClickListener(this);
		
		mTunerSourceBtn = (Button)findViewById(R.id.tunerBtn);
		mTunerSourceBtn.setOnClickListener(this);
		mAudioSourceBtn = (Button)findViewById(R.id.audioSourceBtn);
		mAudioSourceBtn.setOnClickListener(this);
		mVideoSourceBtn = (Button)findViewById(R.id.videoSourceBtn);
		mVideoSourceBtn.setOnClickListener(this);
		
		mAux1SourceBtn = (Button)findViewById(R.id.auxBtn1);
		mAux1SourceBtn.setOnClickListener(this);
		mAux2SourceBtn = (Button)findViewById(R.id.auxBtn2);
		mAux2SourceBtn.setOnClickListener(this);
		mDVRSourceBtn = (Button)findViewById(R.id.dvrBtn);
		mDVRSourceBtn.setOnClickListener(this);
		
		mBtPhoneSourceBtn = (Button)findViewById(R.id.btPhoneBtn);
		mBtPhoneSourceBtn.setOnClickListener(this);
		mBtMusicSourceBtn = (Button)findViewById(R.id.btMusicBtn);
		mBtMusicSourceBtn.setOnClickListener(this);
		mNavigationSourceBtn = (Button)findViewById(R.id.naviBtn);
		mNavigationSourceBtn.setOnClickListener(this);
		
		mTTSOnBtn = (Button)findViewById(R.id.ttsOnBtn);
		mTTSOnBtn.setOnClickListener(this);
		
		mTTSOffBtn = (Button)findViewById(R.id.ttsOffBtn);
		mTTSOffBtn.setOnClickListener(this);
		
		mMuteBtn = (Button)findViewById(R.id.muteBtn);
		mMuteBtn.setOnClickListener(this);
		
		mReqKeyBeepStatusBtn = (Button)findViewById(R.id.reqKeyBeepStatus);
		mReqKeyBeepStatusBtn.setOnClickListener(this);
		
		mPlayKeyBeepBtn = (Button)findViewById(R.id.playKeyBeep);
		mPlayKeyBeepBtn.setOnClickListener(this);
		
		mKeyBeepSwitchBtn = (Button)findViewById(R.id.keyBeepSwitch);
		mKeyBeepSwitchBtn.setOnClickListener(this);
		
		mReqAudioBalanceInfoBtn = (Button)findViewById(R.id.reqAudioBalanceInfo);
		mReqAudioBalanceInfoBtn.setOnClickListener(this);
		
		mReqEQBassInfoBtn = (Button)findViewById(R.id.reqEQBassInfo);
		mReqEQBassInfoBtn.setOnClickListener(this);
		
		mReqGPSInfoBtn = (Button)findViewById(R.id.reqGPSInfo);
		mReqGPSInfoBtn.setOnClickListener(this);
		
		mBeginStudyBtn = (Button)findViewById(R.id.beginStudy);
		mBeginStudyBtn.setOnClickListener(this);
		
		mExitStudyBtn = (Button)findViewById(R.id.exitStudy);
		mExitStudyBtn.setOnClickListener(this);
		
		mSaveStudyBtn = (Button)findViewById(R.id.saveStudy);
		mSaveStudyBtn.setOnClickListener(this);
		
		mResetStudySettingBtn = (Button)findViewById(R.id.resetStudySetting);
		mResetStudySettingBtn.setOnClickListener(this);
		
		mClearStudySettingBtn = (Button)findViewById(R.id.clearStudy);
		mClearStudySettingBtn.setOnClickListener(this);
		
		mGetStudyInfoBtn = (Button)findViewById(R.id.studyInfo);
		mGetStudyInfoBtn.setOnClickListener(this);
		
		mStudyKeyEText = (EditText)findViewById(R.id.studyKey);
		mStudyKeyBtn = (Button)findViewById(R.id.studyKeyBtn);
		mStudyKeyBtn.setOnClickListener(this);
		
		mGetCacheValueBtn = (Button)findViewById(R.id.getCacheValueBtn);
		mPropIdEditText = (EditText)findViewById(R.id.propIdText);
		mPropValueEditText = (EditText)findViewById(R.id.propValueText);
		mGetCacheValueBtn.setOnClickListener(this);

		mVehicleManager = VehicleManager.getInstance();
		if(mVehicleManager == null)
		{
			Log.e(TAG, "get Vehcle Instace failed");
			return;
		}
		Log.i(TAG, "get Vehcle Instace success");
		mSupportedPropIds = mVehicleManager.getSupportedPropertyIds();
		mWritablePropIds = mVehicleManager.getWritablePropertyIds();
		mPropDataType = mVehicleManager.getPropertiesDataType(mSupportedPropIds);
		Log.i(TAG, "getSupportedPropIds:" + toString(mSupportedPropIds));
		Log.i(TAG, "getWritablePropIds:" + toString(mWritablePropIds));
		Log.i(TAG, "getPropDataType:" + toString(mPropDataType));
		
		mVehicleManager.registerHandler(mMcuProperties, mDataNotification);
		
		/*mMcuUpgradeCount = loadSettings(KEY_MCU_UPGRADE_COUNT);
		if(mMcuUpgradeCount < 0)
		{
			mMcuUpgradeCount = 0;
		}
		
		mMcuVoltageEText.setText("mcu auto upgrade test count:"+mMcuUpgradeCount);*/

		if(mHandler != null)
		{
			mHandler.postDelayed(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_VERSION));
				}
			}, 1000);
		}
	}

	private int loadSettings(String key)
	{
		return getSharedPreferences(SHARE_PREFERNESS_NAME, Context.MODE_PRIVATE).getInt(key, -1);
	}
	
	public boolean saveSettings(String key,int value)
	{
		Log.i(TAG, "save settings,key:"+key+",value:"+value);
		SharedPreferences.Editor editor = getSharedPreferences(SHARE_PREFERNESS_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	
	private Runnable mcuUpdateRunable = new Runnable()
	{
		
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			
			//前一次是正常模式升级
			if(loadSettings(KEY_BOOT_MODE) == 1)
			{
				try
				{
					saveSettings(KEY_BOOT_MODE, 0);
					while(true)
					{
						RecoverySystem.installPackage(mContext, new File("/cache/update.zip"));
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else 
			{
				if(mVehicleManager != null)
				{
					mMcuUpgradeCount ++;
					saveSettings(KEY_MCU_UPGRADE_COUNT, mMcuUpgradeCount);
					saveSettings(KEY_BOOT_MODE, 1);
					mVehicleManager.setProperties(new int[]{VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY,VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY}, 
							new String[]{"/cache/mcuUpdate.mce",String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_UPGRADE)});
				}
			}
		}
	};
	
	@Override
	protected void onResume()
	{
		Log.i(TAG, "onResume");
		// TODO Auto-generated method stub
		super.onResume();
		if(mHandler != null)
		{
			//mHandler.postDelayed(mcuUpdateRunable, 5000);
		}
	}

	@Override
	protected void onDestroy()
	{
		Log.i(TAG, "onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mVehicleManager != null && mMcuProperties != null)
		{
			mVehicleManager.removeHandler(mMcuProperties, mDataNotification);
		}

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(mHandler != null)
		{
			mHandler.removeCallbacks(mcuUpdateRunable);
		}
		if(v == mReqMcuVersionBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_VERSION));
			}
		}
		else if(v == mReqMcuVoltageBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_VOLTAGE));
			}
			
		}
		else if(v == mReqMcuLastSleepReasonBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_LAST_SLEEP_REASON));
			}
		}
		else if(v == mReqMcuWakeupModeBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_STATE));
			}
		}
		else if(v == mReqMcuUpdateBtn)
		{
			String strText = mMcuUpdateText.getText().toString();
			if (mVehicleManager != null)
			{
				if(!strText.equals("jidou"))
				{
					mVehicleManager.setProperties(new int[]{VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY,VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY}, 
							new String[]{"/cache/mcuUpdate.mce",String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_UPGRADE)});
				}
				else
				{
					new AlertDialog.Builder(McuDemoActivity.this).setTitle("极豆MCU升级文件确认")
					.setMessage("请确认待烧录的机器硬件环境和MCU升级文件均属于极豆项目!!!")
					.setPositiveButton("确认", new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface arg0, int arg1)
								{
									// TODO Auto-generated method stub
									mVehicleManager.setProperties(new int[]{VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY,VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY}, 
											new String[]{"/cache/mcu_jidou.img",String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_UPGRADE)});
									mMcuUpdateText.setText("");
									//finish();
								}
							})
					.setNegativeButton("取消", new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface arg0, int arg1)
								{
									// TODO Auto-generated method stub
									mMcuUpdateText.setText("");
									//finish();
								}
						
							}
					).show();
				}
			}
		}
		else if(v == mScreenOn)
		{
			if (mVehicleManager != null)
			{
				boolean bRet = mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.SCREEN_BACKLIGHT_ON));
			}
		}
		else if(v == mScreenOff)
		{
			if (mVehicleManager != null)
			{
				boolean bRet = mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.SCREEN_BACKLIGHT_OFF));
			}
		}
		else if(v == mScreenAuto)
		{
			if (mVehicleManager != null)
			{
				boolean bRet = mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.SCREEN_BACKLIGHT_AUTO));
			}
		}
		else if(v == mOpenSerialBtn)
		{
			if (mVehicleManager != null)
			{
				boolean bRet = mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_OPEN_SERIAL));
			}
		}
		else if(v == mCloseSerialBtn)
		{
			if (mVehicleManager != null)
			{
				boolean bRet = mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY , String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_CLOSE_SERIAL));
			}
		}
		else if(v == mMcuResetBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_RESET));
			}
			
		}
		else if(v == mTunerSourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_ARM_AUDIO_CHANNEL_PROPERTY, String.valueOf("0"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("0"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf("0"));
				
			}
		}
		else if(v == mAudioSourceBtn)
		{
			if(mVehicleManager != null)
			{
				
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("2"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf("2"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_ARM_AUDIO_CHANNEL_PROPERTY, String.valueOf("1"));
			}
		}
		else if(v == mVideoSourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf("3"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("3"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_ARM_AUDIO_CHANNEL_PROPERTY, String.valueOf("1"));
			}
		}
		
		else if(v == mAux1SourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf(VehiclePropertyConstants.MEDIA_MODE_AUX1));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf(VehiclePropertyConstants.UI_MODE_AUX1));
			}
		}
		else if(v == mAux2SourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf(VehiclePropertyConstants.MEDIA_MODE_AUX2));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf(VehiclePropertyConstants.UI_MODE_AUX2));
			}
		}
		else if(v == mDVRSourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf("11"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("11"));
			}
		}
		else if(v == mBtPhoneSourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf("5"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("5"));
			}
		}
		else if(v == mBtMusicSourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY, String.valueOf("10"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("10"));
			}
		}
		else if(v == mNavigationSourceBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_UI_MODE_PROPERTY, String.valueOf("6"));
			}
		}
		else if(v == mTTSOnBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_ARM_AUDIO_CHANNEL_PROPERTY, String.valueOf("0"));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_NAV_AUDIO_CHANNEL_PROPERTY, String.valueOf(VehiclePropertyConstants.TTS_ON));
			}
		}
		else if(v == mTTSOffBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_NAV_AUDIO_CHANNEL_PROPERTY, String.valueOf(VehiclePropertyConstants.TTS_OFF));
			}
		}
		else if(v ==mMuteBtn)
		{
			if(mVehicleManager != null)
			{
				//0 unmute   1 mute
				mMute = (mMute == 0 ? 1 : 0);
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MUTE_STATUS_PROPERTY, String.valueOf(mMute));
			}
		}
		else if(v == mReqMediaVolumeBtn)
		{
			if(mVehicleManager != null)
			{
				mSeekBarEnable = true;
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_MEDIA_VOLUME_INFO));
			}
		}
		else if(v == mReqBtPhoneVolumeBtn)
		{
			if(mVehicleManager != null)
			{
				mSeekBarEnable = true;
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_BT_VOLUME_INFO));
			}
		}
		else if(v == mReqNavVolumeBtn)
		{
			if(mVehicleManager != null)
			{
				mSeekBarEnable = true;
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_NAVI_VOLUME_INFO));
			}
		}
		else if(v == mReqBacklightBrightnessBtn)
		{
			if(mVehicleManager != null)
			{
				mSeekBarEnable = true;
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_BACKLIGHT_BRIGHTNESS));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_BACKLIGHT_STATUS));
			}
		}
		else if(v == mReqKeyBeepStatusBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_KEY_BEPP_STATUS));
			}
			
		}
		else if(v == mPlayKeyBeepBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_PLAY_KEY_SPEAKER));
			}
		}
		else if(v == mKeyBeepSwitchBtn)
		{
			if(mVehicleManager != null)
			{
				mKeyBeepStatus = (mKeyBeepStatus == 0 ? 1: 0);
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_KEY_BEEP_SWITCH_PROPERTY, String.valueOf(mKeyBeepStatus));
			}
		}
		else if(v == mReqAudioBalanceInfoBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_AUDIO_BALANCE_INFO));
			}
		}
		else if(v == mReqEQBassInfoBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_EQ_BASS_INFO));
			}
		}
		else if(v == mReqGPSInfoBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_GPS_MONITOR_STATUS));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_GPS_MIX_STATUS));
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_GPS_MIX_LEVEL));
			}
		}
		else if(v == mReqNaviMixLevelBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_GPS_MIX_LEVEL));
			}
		}
		else if(v == mReqGpsMixStatusBtn)
		{
			if(mVehicleManager != null)
			{
				mGpsMixStatus = (mGpsMixStatus == 0 ? 1:0);
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_GPS_MIX_ENABLE_PROPERTY, String.valueOf(mGpsMixStatus));
			}
		}
		else if(v == mBeginStudyBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_ENTER_STEER_STUDY));
		}
		else if(v == mExitStudyBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.MCU_CMD_REQ_EXIT_STEER_STUDY));
		}
		else if(v == mSaveStudyBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.STEER_STUDY_ACTION_SAVE));
			}
		}
		else if(v == mResetStudySettingBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.STEER_STUDY_ACTION_RESET));
			}
		}
		else if(v == mClearStudySettingBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.STEER_STUDY_ACTION_CLEAR));
			}
		}
		else if(v == mGetStudyInfoBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_ACTION_PROPERTY, String.valueOf(VehiclePropertyConstants.STEER_STUDY_ACTION_INFO));
			}
		}
		else if(v == mStudyKeyBtn)
		{
			String strKeyValue = mStudyKeyEText.getText().toString();
			if(strKeyValue == null || strKeyValue.length()  == 0)
			{
				return;
			}
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_STEER_STUDY_KEY_PROPERTY, strKeyValue);
			}
		}
		else if(v == mGetCacheValueBtn)
		{
			try
			{
				int propId = Integer.valueOf(mPropIdEditText.getText().toString());
				if(mVehicleManager != null)
				{
					String value = mVehicleManager.getProperty(propId);
					if(value == null)
					{
						value = "null";
					}
					if(propId == VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_CONTEXT_CACHE_PROPERTY ||
							propId == VehicleInterfaceProperties.VIM_FORD_SYNC_DIALOG_CONTEXT_CACHE_PROPERTY)
					{
						String[] items = VehicleManager.stringToStringArray(value);
						for(int i=0;items != null && i < items.length;i++)
						{
							HashMap<Integer, String> itemMap = VehicleManager.stringToHashMap(items[i]);
							if(itemMap != null)
							{
								mPropValueEditText.setText(itemMap.get(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_ID_PROPERTY)+
										":"+itemMap.get(VehicleInterfaceProperties.VIM_FORD_SYNC_MENU_ITEM_LINE_CONTEXT_PROPERTY)+"\n");
							}
						}
					}
					else
					{
						mPropValueEditText.setText(value);
					}
				}
			} catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		//设置默认增益
		else if(v == mNaviArmGainBtn)
		{
		}
		
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		if(view == mPresetModeSpinner && mVehicleManager != null)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_EQ_PRESET_MODE_PROPERTY, String.valueOf(position));
		}
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
		if(seekBar == mMediaVolumeSeekBar)
		{
			mMediaVolume = progress;
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY, String.valueOf(progress));
			mMediaVolumeText.setText(String.valueOf(progress));
		}
		else if(seekBar == mBtPhoneVolumeSeekBar) 
		{
			mBtPhoneVolume = progress;
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_BT_PHONE_VOLUME_PROPERTY, String.valueOf(progress));
			mBtPhoneVolumeText.setText(String.valueOf(progress));
		}
		else if(seekBar == mNavVolumeSeekBar)
		{
			mNaviVolume = progress;
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_NAVIGATION_VOLUME_PROPERTY, String.valueOf(progress));
			mNavVolumeText.setText(String.valueOf(progress));
		}
		else if(seekBar == mBacklightBrightnessSeekBar)
		{
			mBacklightBrightness = progress;
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_BACKLIGHT_BRIGHTNESS_PROPERTY, String.valueOf(progress));
			mBacklightBrightnessText.setText(String.valueOf(progress));
		}
		else if(seekBar == mNaviMixLevelSeekBar)
		{
			mNaviMixLevel = progress;
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_GPS_MIX_LEVEL_PROPERTY, String.valueOf(progress));
			mNaviMixLevelText.setText(String.valueOf(progress));
		}
		else if(seekBar == mNaviArmGainSeekBar)
		{
			mNaviArmGain = progress;
			AudioSystem.setMasterVolume(progress);
			mNaviArmGainText.setText(String.valueOf(progress));
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
