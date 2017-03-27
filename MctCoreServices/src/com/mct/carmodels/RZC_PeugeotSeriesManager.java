package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.carmodels.RZC_HondaSeriesManager.SystemTimeSetReceiver;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.R.bool;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRouter.VolumeCallback;
import android.net.UrlQuerySanitizer.ParameterValuePair;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-标致雪铁龙车型协议管理
public class RZC_PeugeotSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC_PeugeotSeries";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_PEUGEOT_14_408;
	private int mPressedKeyValue = 0;
	private String mSyncMediaInfo = null;
	private long mSyncMediaTick = 0;
	private boolean mShowVehicleDoorInfo = true;
	
	private boolean mSupportSyncTime = true;
	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();

	//临时数据缓存
	private int[] mAirConditionParam = null;
	private int[] mVehicleStatusParam = null;
	private int[] mCruiseSpeedParam = new int[]{30,30,30,30,30,30};
	private int[] mLimitSpeedParam = new int[]{30,30,30,30,30,30};
	private int[] mVehicleDisplayMode = new int[]{0,0};
	
	private Runnable mMediaSyncInfoRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			if (mService != null && mMcuManager != null)
			{
				if (mMcuManager.getCurrentMediaMode() == VehiclePropertyConstants.MEDIA_MODE_TUNER)
				{
					int sourceType = VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_TUNER;
					int curTrack = -1;
					int totalTrack = -1;
					int currentPlayTime = -1;
					int totalPlayTime = -1;
					int playState = -1;
					String strID3 = null;
					int band = Integer.valueOf(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY));
					int freq = Integer.valueOf(mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));;
					String mediaInfoParam = VehiclePropertyConstants.formatSourceInfoString(sourceType, curTrack, totalTrack, currentPlayTime, totalPlayTime, playState, strID3, band, freq);
					mCanMangaer.setPropValue(VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY, mediaInfoParam);
				}
			}
		}
	};
	
	public class SystemTimeSetReceiver extends BroadcastReceiver
	{
		public void onReceive(final Context context, final Intent intent)
		{
			Log.i(TAG, "receive broadcast message:"+intent.getAction());
			if(intent.getAction().equals("android.intent.action.TIME_SET") && mSupportSyncTime)
			{
				stopSyncVehicleInfoTimer();
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
			}
		}
	}
	
	private void startSyncVehicleInfoTimer()
	{
		if (mSyncVehicleInfoTimer != null)
		{
			Log.w(TAG, "startSyncVehicleInfoTimer,timer is exist!");
			return;
		}
		Log.i(TAG, "startSyncVehicleInfoTimer");
		// 延迟3S定时发送
		mSyncVehicleInfoTimer = new Timer();
		mSyncVehicleInfoTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
			}
		}, 3*1000);
	}

	// 停止源同步定时器
	private void stopSyncVehicleInfoTimer()
	{
		if (mSyncVehicleInfoTimer != null)
		{
			Log.i(TAG, "stopSyncVehicleInfoTimer");
			mSyncVehicleInfoTimer.cancel();
			mSyncVehicleInfoTimer.purge();
			mSyncVehicleInfoTimer = null;
		}
	}


	public RZC_PeugeotSeriesManager(int carModel)
	{
		mCarModel = carModel;
	}

	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager,current carmodel:"+mCarModel);
		mService = service;
		mShowVehicleDoorInfo = true;
		mMcuManager = (HeadUnitMcuManager) service.getMcuManager();
		mCanMangaer = (CanManager) service.getVehicleManager();
		initVehicleConfig();
		if(mSupportSyncTime)
		{
			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction("android.intent.action.TIME_SET");
			mService.registerReceiver(mSystemTimeSetReceiver, iFilter);
			startSyncVehicleInfoTimer();
		}
		return true;
	}

	private void initVehicleConfig()
	{
		// 初始化雷达距离范围
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "5");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5}), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		if(mMediaSyncInfoRunnable != null && mService != null)
		{
			mService.getMainHandler().removeCallbacks(mMediaSyncInfoRunnable);
		}
		mShowVehicleDoorInfo = false;
		mMcuManager = null;
		mCanMangaer = null;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_PeugeotSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_PeugeotSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_PeugeotSeriesProtocol.getProperityPermission(RZC_PeugeotSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_PeugeotSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_PeugeotSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
			}
		}
		int[] retArray = new int[writableProps.size()];
		for (int i = 0; i < retArray.length; i++)
		{
			retArray[i] = writableProps.get(i);
		}
		return retArray;
	}

	@Override
	public int getPropertyDataType(int propId)
	{
		// TODO Auto-generated method stub
		return RZC_PeugeotSeriesProtocol.getProperityDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		if (mMcuManager == null || mCanMangaer == null)
		{
			Log.e(TAG, "McuManager is not ready");
			return false;
		}
		try
		{
			switch (propId)
			{
				case VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY:
					int cmd = Integer.valueOf(value);
					switch (cmd)
					{
						case VehiclePropertyConstants.CANBOX_CMD_REQ_START:
						case VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME:
							Time time = new Time();
							time.setToNow();
							String strTimeFormat = android.provider.Settings.System.getString(mService.getContentResolver(),
									android.provider.Settings.System.TIME_12_24);
							if(strTimeFormat != null && strTimeFormat.equals("12"))
							{
								if(time.hour > 12)
								{
									time.hour = time.hour-12;
								}
								else if(time.hour == 0)
								{
									time.hour = 12;
								}
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_TIME, new int[] {Math.max(0, time.year-2000),time.month+1,time.monthDay,time.hour,time.minute }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_COMPUTE_INFO_PAGE0}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_COMPUTE_INFO_PAGE1}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_COMPUTE_INFO_PAGE2}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO}));
							//请求车外温度
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_EXTERN_TEMP}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_REVERSE_RADAR_INFO}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_VEHICLE_STATUS}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO }));
							break;
						//胎压标定
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_SET:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x10, 0x01}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_DIAGNOSTIC_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_PeugeotSeriesProtocol.CH_CMD_VEHICLE_DIAGNOSTIC_INFO }));
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
					break;				
				/*
				 * 灯光设置
				 */
				case VehicleInterfaceProperties.VIM_WELCOME_LIGHT_DELAY_TIME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x08, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x04, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_BACK_HOME_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x07, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_AMBIENT_LIGHT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x05, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MOTORWAY_LIGHT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x12, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_DAYTIME_RUNNING_LIGHT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x03, Integer.valueOf(value)}));
					break;
				/*
				 * 车锁设置
				 */
				case VehicleInterfaceProperties.VIM_UNLOCK_TRUNK_ONLY_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x13, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_DOOR_UNLOCK_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x0F, Integer.valueOf(value)}));
					break;
				/*
				 * 车身设置
				 */
				case VehicleInterfaceProperties.VIM_AUTO_START_STOP_FUNCTION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x0D, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_UNIT_TEMP_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x14, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_AUDIO_EFFECTS_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x09, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x01, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_BLIND_DETECTION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x0C, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_WELCOME_LIGHT_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x0E, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_COMMON_FUEL_CONSUMPTION_UNIT_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x0A, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_STATUS_AUTO_PARK_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x11, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x06, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_MW_REAR_WINDOW_WIPEING_IN_REVERSE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x02, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_COLOR_THEME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x15, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_ADAS_DRIVER_ALERT_SYSTEM_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x16, Integer.valueOf(value)}));
					break;
				case VehicleInterfaceProperties.VIM_RAMP_STARTUP_ASSIST_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_VEHICLE_PARAM_SETTING, new int[] { 0x17, Integer.valueOf(value)}));
					break;
				/*
				 * 设置巡航速度
				 */
				case VehicleInterfaceProperties.VIM_CRUISE_SPEED1_PROPERTY:
					mCruiseSpeedParam[0]= Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x50}, mCruiseSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_CRUISE_SPEED2_PROPERTY:
					mCruiseSpeedParam[1]= Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x50}, mCruiseSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_CRUISE_SPEED3_PROPERTY:
					mCruiseSpeedParam[2]= Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x50}, mCruiseSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_CRUISE_SPEED4_PROPERTY:
					mCruiseSpeedParam[3]= Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x50}, mCruiseSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_CRUISE_SPEED5_PROPERTY:
					mCruiseSpeedParam[4]= Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x50}, mCruiseSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_CRUISE_SPEED6_PROPERTY:
					mCruiseSpeedParam[5]= Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x50}, mCruiseSpeedParam)));
					break;
					/*
					 * 设置速度限值
					 */
				case VehicleInterfaceProperties.VIM_LIMIT_SPEED1_PROPERTY:
					mLimitSpeedParam[0] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x90}, mLimitSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_LIMIT_SPEED2_PROPERTY:
					mLimitSpeedParam[1] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x90}, mLimitSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_LIMIT_SPEED3_PROPERTY:
					mLimitSpeedParam[2] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x90}, mLimitSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_LIMIT_SPEED4_PROPERTY:
					mLimitSpeedParam[3] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x90}, mLimitSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_LIMIT_SPEED5_PROPERTY:
					mLimitSpeedParam[4] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x90}, mLimitSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_LIMIT_SPEED6_PROPERTY:
					mLimitSpeedParam[5] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, ServiceHelper.combineArray(new int[]{0x90}, mLimitSpeedParam)));
					break;
				case VehicleInterfaceProperties.VIM_RESET_SPEED_CONFIG_PROPERTY:
					int speedConfig = Integer.valueOf(value);
					if(speedConfig == 0)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, new int[]{0x60,0,0,0,0,0,0}));
					}
					else if(speedConfig == 1)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SET_SPEED_INFO, new int[]{0x0A,0,0,0,0,0,0}));
					}
					break;
				case VehicleInterfaceProperties.VIM_COUSTOM_LEFT_SCREEN_DISPLAY_MODE_PROPERTY:
					mVehicleDisplayMode[0] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SCREEN_DISPLAY, mVehicleDisplayMode));
					break;
				case VehicleInterfaceProperties.VIM_COUSTOM_RIGHT_SCREEN_DISPLAY_MODE_PROPERTY:
					mVehicleDisplayMode[1] = Integer.valueOf(value);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_SCREEN_DISPLAY, mVehicleDisplayMode));
					break;
				//空调OFF
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_DOWN_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AIR_CONDITION_CMD_POWER_OFF)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0C,0x01}));
					}
					break;
				//空调OFF
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_UP_PROPERTY:
					if(Integer.valueOf(value) == VehiclePropertyConstants.AIR_CONDITION_CMD_POWER_OFF)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0C,0x00}));
					}
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_PROPERTY:
					int airConditionCmd = Integer.valueOf(value);
					switch (airConditionCmd)
					{
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AUTO_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x01,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AUTO_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x01,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x02,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x02,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_MAX_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x03,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AC_MAX_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x03,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_DUAL_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0B,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_DUAL_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0B,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_LEFT_TEMP_UP:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x04,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_LEFT_TEMP_DOWN:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x04,0x02}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_RIGHT_TEMP_UP:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x05,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_RIGHT_TEMP_DOWN:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x05,0x02}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_CENTER_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x06,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_CENTER_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x06,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_UP_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x07,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_UP_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x07,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_DOWN_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x08,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_DIRECTION_DOWN_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x08,0x00}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_SPEED_UP:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0A,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_FAN_SPEED_DOWN:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0A,0x02}));
							break;	
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AQS_ON:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0D,0x01}));
							break;
						case VehiclePropertyConstants.AIR_CONDITION_CMD_AQS_OFF:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x0D,0x00}));
							break;
						default:
							break;
					}
					break;
				//风量强度
				case VehicleInterfaceProperties.VIM_HVAC_FAN_STRENGTH:
					int fanStrength = Integer.valueOf(value);
					fanStrength = Math.min(2, Math.max(0, fanStrength));
					mMcuManager.postCanData(mCanMangaer.pack(RZC_PeugeotSeriesProtocol.HC_CMD_AC_CONTROL, new int[]{0x09,fanStrength}));
					break;
				default:
					break;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
			return false;
		}
		return true;
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		if (data == null || mCanMangaer == null) { return; }
		int[] tmp = data.getIntArray(CarService.KEY_DATA);
		int[] buffer = mCanMangaer.unPack(tmp);
		if (buffer == null || buffer.length == 0) { return; }
		int[] param = new int[buffer.length - 1];
		System.arraycopy(buffer, 1, param, 0, param.length);
		onReceiveData(buffer[0], param);
	}

	private void onReceiveData(int cmd, int[] param)
	{
		if (mMcuManager == null)
		{
			Log.e(TAG, "Mcu Manager is not ready");
			return;
		}
		mMcuManager.returnCanAck(cmd);
		switch (cmd)
		{
			case RZC_PeugeotSeriesProtocol.CH_CMD_BACKLIGHT_INFO:
				if(param != null && param.length >= 0x03)
				{
					Log.i(TAG, "receive backlight info:"+param[2]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BRIGHTNESS_LVL_PROPERTY, String.valueOf(param[2]),false);
				}
				break;
			case RZC_PeugeotSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length >= 1)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0]);
					// 0 按键释放
					if (param[0] == 0 && mPressedKeyValue != RZC_PeugeotSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_PeugeotSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_PeugeotSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[0] > 0)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_PeugeotSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_PeugeotSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 0x07)
				{
					Log.i(TAG, "receive air condition info");
					// 空调开关
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), true);
					if (acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					
					//风量强度
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_STRENGTH, String.valueOf(ServiceHelper.getBits(param[6], 6, 2)), false);
					
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param,5))
					{
						Log.i(TAG, "filter no change air condition data");
						break;
					}
					mAirConditionParam = param;
					// 制冷模式
					int[] acMode = new int[4];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					acMode[3] = ServiceHelper.getBit(param[4], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 前窗除雾
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[4], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
					
					// 循环模式
					int[] cycleMode = new int[1];
					if (ServiceHelper.getBit(param[0], 4) == 1)
					{
						cycleMode[0] = VehiclePropertyConstants.AC_AUTO_IN_OUT_CYCLE_MODE;
					}
					else
					{
						cycleMode[0] = ServiceHelper.getBit(param[0], 5) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);

					// 风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = ServiceHelper.getBit(param[1], 7) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[1], 5) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					// 风量大小
					int fanSpeed = ServiceHelper.getBits(param[1], 0, 4)-1;
					fanSpeed = Math.min(8, Math.max(0, fanSpeed));
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[] { fanSpeed }), false);

					// 空调温度信息
					int tempUnit = ServiceHelper.getBit(param[4], 0);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{tempUnit,tempUnit}), false);
					float[] interiorTemp = new float[2];
					// 驾驶员位温度(18-26)
					if (param[2] == 0x00)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[2] == 0xFF)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						if(tempUnit == 1)
						{
							interiorTemp[0] = (float) Math.max(RZC_PeugeotSeriesProtocol.AC_LOWEST_TEMP_F, Math.min(RZC_PeugeotSeriesProtocol.AC_HIGHEST_TEMP_F, param[2] ));
						}
						else
						{
							interiorTemp[0] = (float) Math.max(RZC_PeugeotSeriesProtocol.AC_LOWEST_TEMP_C, Math.min(RZC_PeugeotSeriesProtocol.AC_HIGHEST_TEMP_C, RZC_PeugeotSeriesProtocol.AC_LOWEST_TEMP_C + (param[2] - 1) * 0.5));
						}
					}

					// 副驾驶员位温度(18-26)
					if (param[3] == 0x00)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[3] == 0xFF)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						if(tempUnit == 1)
						{
							interiorTemp[1] = (float) Math.max(RZC_PeugeotSeriesProtocol.AC_LOWEST_TEMP_F, Math.min(RZC_PeugeotSeriesProtocol.AC_HIGHEST_TEMP_F, param[3]));
						}
						else
						{
							interiorTemp[1] = (float) Math.max(RZC_PeugeotSeriesProtocol.AC_LOWEST_TEMP_C, Math.min(RZC_PeugeotSeriesProtocol.AC_HIGHEST_TEMP_C, RZC_PeugeotSeriesProtocol.AC_LOWEST_TEMP_C + (param[3] - 1) * 0.5));
						}
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);

					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_PeugeotSeriesProtocol.CH_CMD_REVERSE_RADAR_INFO:
				if (param != null && param.length >= 0x07)
				{
					Log.i(TAG, "receive reverse radar info");
					if(param[0] <= 1)
					{
						param[0] = VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_NOT_SHOW;
					}
					else if(param[0] == 3)
					{
						param[0] = VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_OFF;
					}
					else
					{
						param[0] = VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(param[0]), false);
					for(int i=1;i<= 6;i++)
					{
						param[i] = tramslateRadarDistance(param[i]);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(new int[]{param[1],param[2],param[2],param[3]}), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(new int[]{param[4],param[5],param[5],param[6]}), false);
				}

				break;
			case RZC_PeugeotSeriesProtocol.CH_CMD_FULL_RADAR_INFO:
				if (param != null && param.length >= 0x06)
				{
					Log.i(TAG, "receive full radar info");
					for(int i=0;i< 6;i++)
					{
						param[i] = tramslateRadarDistance(param[i]);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(new int[]{param[0],param[1],param[1],param[2]}), false);				}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(new int[]{param[3],param[4],param[4],param[5]}), false);
				break;
			//行车电脑信息Page1
			case RZC_PeugeotSeriesProtocol.CH_CMD_COMPUTE_INFO_PAGE0:
				if (param != null && param.length >= 0x09)
				{
					Log.i(TAG, "receive page0 info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY, String.format("%.1f", (float)Math.min(300.0, Math.max(0.0, ServiceHelper.MAKEWORD(param[1], param[0])*0.1f))), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_DRVNG_RANGE_PROPERTY, String.valueOf(Math.min(2000, Math.max(0, ServiceHelper.MAKEWORD(param[3], param[2])))), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DESTINATION_ODOMETER_PROPERTY, String.valueOf(Math.min(6000, Math.max(0, ServiceHelper.MAKEWORD(param[5], param[4])))), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_START_STOP_FUNCTION_TIMER_PROPERTY, String.valueOf(param[6]*3600+param[7]*60+param[8]), false);
				}
				break;
			//行车电脑信息Page2
			case RZC_PeugeotSeriesProtocol.CH_CMD_COMPUTE_INFO_PAGE1:
				if (param != null && param.length >= 0x06)
				{
					Log.i(TAG, "receive page1 info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_FUEL_CONSUMPTION_PROPERTY, String.format("%.1f", (float)Math.min(300.0, Math.max(0.0, ServiceHelper.MAKELONG(param[1], param[0])*0.1f))), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_AVG_SPEED_PROPERTY, String.valueOf(Math.min(250, Math.max(0, ServiceHelper.MAKELONG(param[3], param[2])))), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY, String.valueOf(Math.min(9999, Math.max(0, ServiceHelper.MAKELONG(param[5], param[4])))), false);
				}
				break;
			//行车电脑信息Page3
			case RZC_PeugeotSeriesProtocol.CH_CMD_COMPUTE_INFO_PAGE2:
				if (param != null && param.length >= 0x06)
				{
					Log.i(TAG, "receive page2 info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_2_FUEL_CONSUMPTION_PROPERTY, String.format("%.1f", (float)Math.min(300.0, Math.max(0.0, ServiceHelper.MAKELONG(param[1], param[0])*0.1f))), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_2_AVG_SPEED_PROPERTY, String.valueOf(Math.min(250, Math.max(0, ServiceHelper.MAKELONG(param[3], param[2])))), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_2_MILEAGE_PROPERTY, String.valueOf(Math.min(9999, Math.max(0, ServiceHelper.MAKELONG(param[5], param[4])))), false);
				}
				break;
			case RZC_PeugeotSeriesProtocol.CH_CMD_EXTERN_TEMP:
				if (param != null && param.length > 0)
				{
					Log.i(TAG, "receive extern temp info");
					int temp = ServiceHelper.getBits(param[0], 0, 7);
					if(ServiceHelper.getBit(param[0], 7) == 1)
					{
						temp = -temp;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.valueOf(temp), false);
				}
				break;
			case RZC_PeugeotSeriesProtocol.CH_CMD_VEHICLE_STATUS:
				if(param != null && param.length >= 0x06)
				{
					if(mVehicleStatusParam != null && ServiceHelper.compareIntArray(mVehicleStatusParam, param))
					{
						Log.i(TAG, "filter no change vehicle status info");
						break;
					}
					mVehicleStatusParam = param;
					
					//车门信息处理
					int flStatus = ServiceHelper.getBit(param[0], 7);
					int frStatus = ServiceHelper.getBit(param[0], 6);
					int rlStatus = ServiceHelper.getBit(param[0], 5);
					int rrStatus = ServiceHelper.getBit(param[0], 4);
					int trunkStatus = ServiceHelper.getBit(param[0], 3);
					int hoodStatus = 0;

					String strHisrotyFlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY);
					String strHisrotyFrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY);
					String strHisrotyRlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY);
					String strHisrotyRrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY);
					String strHisrotyTrunkStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY);

					//有变化才上报
					if (mShowVehicleDoorInfo || strHisrotyFlStatus == null || strHisrotyFrStatus == null || strHisrotyRlStatus == null || strHisrotyRrStatus == null || 
							strHisrotyTrunkStatus == null || Integer.valueOf(strHisrotyFlStatus) != flStatus || Integer.valueOf(strHisrotyFrStatus) != frStatus || 
							Integer.valueOf(strHisrotyRlStatus) != rlStatus || Integer.valueOf(strHisrotyRrStatus) != rrStatus || 
							Integer.valueOf(strHisrotyTrunkStatus) != trunkStatus)
					{
						mShowVehicleDoorInfo = false;
						mService.broadcastVehicleDoorInfo(flStatus, frStatus, rlStatus, rrStatus, trunkStatus, hoodStatus);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY, String.valueOf(flStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY, String.valueOf(frStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY, String.valueOf(rlStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY, String.valueOf(rrStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY, String.valueOf(trunkStatus), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY, String.valueOf(trunkStatus), false);
					}
					
					//其它车身状态
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_REAR_WINDOW_WIPEING_IN_REVERSE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STATUS_AUTO_PARK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_TEMP_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);
					//车门自锁状态
					//泊车辅助系统
					mService.cachePropValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
					
					//日间照明灯
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DAYTIME_RUNNING_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 5, 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_COMMON_UNIT_FUEL_CONSUMPTION_PROPERTY, String.valueOf(ServiceHelper.getBits(param[2], 3, 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MOTORWAY_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[2], 0)), false);

					//氛围照明
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AMBIENT_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[3], 5,3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_UNLOCK_TRUNK_ONLY_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 3)), false);
					//倒车状态(以MCU状态为准)
					//手刹(以MCU状态为准)
					//小灯(以MCU状态为准)
					
					//回家照明
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BACK_HOME_MODE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 6,2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_WELCOME_LIGHT_DELAY_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 4,2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_AUDIO_EFFECTS_MODE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 1,2)), false);
					
					//盲区探测
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_BLIND_DETECTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_START_STOP_FUNCTION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_WELCOME_LIGHT_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_UNLOCK_MODE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[5], 4)), false);
					
					//主题颜色 疲劳检测系统 牵引力控制系统
					if(param.length >= 0x07)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_COLOR_THEME_PROPERTY, String.valueOf(ServiceHelper.getBit(param[6], 7)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_DRIVER_ALERT_SYSTEM_PROPERTY, String.valueOf(ServiceHelper.getBit(param[6], 6)), false);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_RAMP_STARTUP_ASSIST_PROPERTY, String.valueOf(ServiceHelper.getBit(param[6], 5)), false);	
					}
				}
				break;
			//报警记录信息
			case RZC_PeugeotSeriesProtocol.CH_CMD_ALARM_RECORD_INFO:
				if(param != null && param.length > 1)
				{
					Log.i(TAG, "receive alarm info,size:"+param[0]);
					if(param[0] > 0)
					{
						int []recordInfoNos = new int[param[0]];
						System.arraycopy(param, 1, recordInfoNos, 0, param[0]);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_ALARM_RECORD_NO_PROPERTY, ServiceHelper.intArrayToString(recordInfoNos), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_ALARM_RECORD_NO_PROPERTY, ServiceHelper.intArrayToString(new int[]{}), false);
					}
				}
				break;
			//汽车功能状态信息
			case RZC_PeugeotSeriesProtocol.CH_CMD_VEHICLE_FUNC_INFO:
				if(param != null && param.length > 1)
				{
					Log.i(TAG, "receive vehicle status info,size:"+param[0]);
					if(param[0] > 0)
					{
						int []vehicleStatusInfoNos = new int[param[0]];
						System.arraycopy(param, 1, vehicleStatusInfoNos, 0, param[0]);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_CONDTION_NO_PROPERTY, ServiceHelper.intArrayToString(vehicleStatusInfoNos), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_CONDTION_NO_PROPERTY, ServiceHelper.intArrayToString(new int[]{}), false);
					}
				}
				break;
			//诊断信息
			case RZC_PeugeotSeriesProtocol.CH_CMD_VEHICLE_DIAGNOSTIC_INFO:
				if(param != null && param.length > 1)
				{
					Log.i(TAG, "receive vehicle diagnostic info,size:"+param[0]);
					if(param[0] > 0)
					{
						int []diagnosticInfoNos = new int[param[0]];
						System.arraycopy(param, 1, diagnosticInfoNos, 0, param[0]);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DIAGNOSTIC_INFO_NO_PROPERTY, ServiceHelper.intArrayToString(diagnosticInfoNos), true);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DIAGNOSTIC_INFO_NO_PROPERTY, ServiceHelper.intArrayToString(new int[]{}), false);
					}
				}
				break;
			//巡航速度与速度限值
			case RZC_PeugeotSeriesProtocol.CH_CMD_SPEED_INFO:
				if(param != null && param.length >= 12)
				{
					for(int i=0;i<6;i++)
					{
						mCruiseSpeedParam[i] = param[i];
						mLimitSpeedParam[i] = param[6+i];
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CRUISE_SPEED1_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CRUISE_SPEED2_PROPERTY, String.valueOf(param[1]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CRUISE_SPEED3_PROPERTY, String.valueOf(param[2]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CRUISE_SPEED4_PROPERTY, String.valueOf(param[3]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CRUISE_SPEED5_PROPERTY, String.valueOf(param[4]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CRUISE_SPEED6_PROPERTY, String.valueOf(param[5]), false);

					mService.cachePropValue(VehicleInterfaceProperties.VIM_LIMIT_SPEED1_PROPERTY, String.valueOf(param[6]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LIMIT_SPEED2_PROPERTY, String.valueOf(param[7]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LIMIT_SPEED3_PROPERTY, String.valueOf(param[8]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LIMIT_SPEED4_PROPERTY, String.valueOf(param[9]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LIMIT_SPEED5_PROPERTY, String.valueOf(param[10]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LIMIT_SPEED6_PROPERTY, String.valueOf(param[11]), false);
				}
				break;
			// 方向盘角度
			case RZC_PeugeotSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length == 2)
				{
					float angle = param[0];
					int dir = param[1];
					// 向左
					if (dir < 0x80)
					{
						angle = -(param[0] + (0xFF + 1) * dir);
					}
					// 向右
					else
					{
						angle = (0xFF + 1) - param[0] + (0xFF - dir) * (0xFF + 1);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			// 协议版本号，验证暂不支持
			case RZC_PeugeotSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if (param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					Log.i(TAG, "receive protocol version info:" + version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
				}
				break;
			default:
				break;
		}
	}
	
	private int tramslateRadarDistance(int originDistance)
	{
		//距离最近
		if(originDistance <= 0x00)
		{
			return 0x01;
		}
		//无障碍物
		else if(originDistance >= 0x05)
		{
			return 0x00;
		}
		else
		{
			return originDistance+1;
		}
	}
}
