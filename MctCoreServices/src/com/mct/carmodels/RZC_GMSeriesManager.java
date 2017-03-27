package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.util.ArrayUtils;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-通用车系车型协议管理
public class RZC_GMSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC-GM";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_GM_HIDEN;
	private boolean mShowVehicleDoorInfo = true;
	private int mPressedKeyValue = 0;
	private int[] mAirConditionParam = null;
	private boolean mSupportSyncTime = true;
	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();

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
	
	public RZC_GMSeriesManager(int carModel)
	{
		mCarModel = carModel;
	}

	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager,current carmodel:"+mCarModel);
		mService = service;
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
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "7");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7}), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		mShowVehicleDoorInfo = false;
		stopSyncVehicleInfoTimer();
		if(mSystemTimeSetReceiver != null && mService != null)
		{
			mService.unregisterReceiver(mSystemTimeSetReceiver);
		}
		if(mMediaSyncInfoRunnable != null && mService != null)
		{
			mService.getMainHandler().removeCallbacks(mMediaSyncInfoRunnable);
		}
		mMcuManager = null;
		mCanMangaer = null;
		return true;
	}
	
	public class SystemTimeSetReceiver extends BroadcastReceiver
	{
		public void onReceive(final Context context, final Intent intent)
		{
			Log.i(TAG, "receive broadcast message:"+intent.getAction());
			if(intent.getAction().equals("android.intent.action.TIME_SET") && mSupportSyncTime)
			{
				stopSyncVehicleInfoTimer();
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
				startSyncVehicleInfoTimer();
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
		// 60S定时发送
		mSyncVehicleInfoTimer = new Timer();
		mSyncVehicleInfoTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
			}
		}, 10*1000, 60*1000);
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

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_GMSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_GMSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_GMSeriesProtocol.getProperityPermission(RZC_GMSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_GMSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_GMSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_GMSeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO,0xFF}));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_BASE_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_FRONT_RADAR_INFO,0xFF }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_REAR_RADAR_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_BASE_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_SETTING:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_VEHICLE_SETTING_INFO,0xFF }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_VEHICLE_SETTING_INFO2,0xFF }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_GMSeriesProtocol.CH_CMD_EXTERN_VHICLE_SETTING_INFO,0xFF }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_RESET_ALL_SETTING:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_VEHICLE_CONTROL, new int[] { 0x80,0x01 }));
							break;
						default:
							break;
					}
					break;
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
					break;
				/*
				 * 空调控制
				 */
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_AUTO_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x00,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_AIR_QUALITY_SENSOR_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x01,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_TEMP_REGION_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x02,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_REAR_AUTO_DEFOGGING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x03,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_FRONT_AUTO_DEFOGGING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x04,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_SEAT_AUTO_HEATING_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x05,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_STARTUP_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x06,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_DOWN_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x07,Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_AIR_CONDITION_CONTROL_CMD_TOUCH_UP_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_AIR_CONDITION_CONTROL, new int[] { 0x07,0x00 }));
					break;
				//倒车雷达开关
				case VehicleInterfaceProperties.VIM_VEHICLE_RADAR_SWITCH_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_PARKING_RADAR_SET, new int[] {Integer.valueOf(value)  }));
					break;
				//OnStar指令
				case VehicleInterfaceProperties.VIM_ONSTAR_REQ_CMD_PROPERTY:
					int onStarCmd = ServiceHelper.stringToIntSafe(value);
					if(onStarCmd != -1)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_ONSTAR_SET, new int[] { onStarCmd }));
					}
					break;
				//OnStar拨号
				case VehicleInterfaceProperties.VIM_ONSTAR_PHONE_NUMBER_PROPERTY:
					int[] onStarCallNumberArray = phoneNumberToArray(value);
					if(onStarCallNumberArray != null)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_ONSTAR_CALL, onStarCallNumberArray));
					}
					break;
				//语言设置
				case VehicleInterfaceProperties.VIM_LANGUAGE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_LANGUAGE_SET, new int[] { Integer.valueOf(value) }));
					break;
				//警示音音量
				case VehicleInterfaceProperties.VIM_ALARM_VOLUME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_ALARM_VOLUME, new int[] { Integer.valueOf(value) }));
					break;
				default:
					int vehicleSettingCmd = RZC_GMSeriesProtocol.vehicleSettingPropIdToCmd(propId);
					//原车设置
					if(vehicleSettingCmd != -1)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_GMSeriesProtocol.HC_CMD_VEHICLE_CONTROL, new int[] { vehicleSettingCmd, Integer.valueOf(value) }));
					}
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
			case RZC_GMSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_GMSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_GMSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_GMSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] == 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_GMSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_PANEL_KEY:
				if(param != null && param.length == 0x02)
				{
					if (param != null && param.length >= 2)
					{
						Log.i(TAG, "receive panel key,Key:" + param[0] + ",status:" + param[1]);
						// 0 按键释放
						if (param[1] == 0 && mPressedKeyValue != RZC_GMSeriesProtocol.SWC_KEY_NONE)
						{
							if(mCarModel == CarModelDefine.CAR_MODEL_GM_GL8)
							{
								mService.broadcastKeyEvent(RZC_GMSeriesProtocol.gl8PanelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
							}
							else if(mCarModel == CarModelDefine.CAR_MODEL_GM_ENCORE)
							{
								mService.broadcastKeyEvent(RZC_GMSeriesProtocol.encorePanelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
							}
							else
							{
								mService.broadcastKeyEvent(RZC_GMSeriesProtocol.commonPanelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
							}
							mPressedKeyValue = RZC_GMSeriesProtocol.SWC_KEY_NONE;
						}
						// 1 按键按下
						else if (param[1] == 1)
						{
							mPressedKeyValue = param[0];
							if(mCarModel == CarModelDefine.CAR_MODEL_GM_GL8)
							{
								mService.broadcastKeyEvent(RZC_GMSeriesProtocol.gl8PanelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
							}
							else if(mCarModel == CarModelDefine.CAR_MODEL_GM_ENCORE)
							{
								mService.broadcastKeyEvent(RZC_GMSeriesProtocol.encorePanelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
							}
							else
							{
								mService.broadcastKeyEvent(RZC_GMSeriesProtocol.commonPanelKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
							}
						}
						// 2 按键持续按下
					}
					
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 0x06)
				{
					Log.i(TAG, "receive air condition info");
					// 空调开关
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					if (acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param))
					{
						Log.i(TAG, "filter no change air condition data");
						break;
					}
					mAirConditionParam = param;
					// 制冷模式
					int[] acMode = new int[2];
					if(ServiceHelper.getBit(param[6], 1) == 1)
					{
						acMode[0] = VehiclePropertyConstants.AC_COOL_MODE_AC_AUTO;
					}
					else
					{
						acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					}
					acMode[1] = ServiceHelper.getBit(param[1], 5) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 循环模式
					int[] cycleMode = new int[1];
					if (ServiceHelper.getBit(param[0], 3) == 1)
					{
						cycleMode[0] = VehiclePropertyConstants.AC_AUTO_IN_OUT_CYCLE_MODE;
					}
					else
					{
						cycleMode[0] = ServiceHelper.getBit(param[0], 5) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);
					
					// 风向
					int fanDir = ServiceHelper.getBits(param[1], 0, 4);
					switch (fanDir)
					{
						case RZC_GMSeriesProtocol.AC_FAN_DIR_AUTO:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF}), false);
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_FRONT_WINDOW:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF}), false);							
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_DOWN:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON}), false);							
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_DOWN_AND_CENTER:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON}), false);							
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_CENTER:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF}), false);							
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_CENTER_AND_UP:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF}), false);							
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_UP:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF}), false);							
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_UP_AND_DOWN:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON}), false);							
							break;
						case RZC_GMSeriesProtocol.AC_FAN_DIR_UP_DOWN_CENTER:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON}), false);							
							break;
						default:
							mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, ServiceHelper.intArrayToString(new int[]{
									VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF,VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF}), false);
							break;
					}
					// 风量大小
					int fanSpeed = ServiceHelper.getBits(param[0], 0, 3);
					if(ServiceHelper.getBit(param[6], 0) == 1)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, VehicleManager.intArrayToString(new int[] { 0xFF }), false);
					}
					//风量扩展位
					else 
					{
						if(ServiceHelper.getBit(param[1], 4) == 1)
						{
							fanSpeed += 8;
						}
						mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, VehicleManager.intArrayToString(new int[] { fanSpeed}), false);
					}
					
					//后座除霜
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 4)), false);
					
					// 空调温度信息
					// 左右温度
					float[] interiorTemp = new float[2];
					interiorTemp[0] = translateACTemp(param[2]);
					interiorTemp[1] = translateACTemp(param[3]);					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);
					
					// 座椅加热信息
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 4, 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_PSNGR_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 0, 4)), false);
					
					// 室外温度
					int externTemp = ServiceHelper.complementCodeToTrueCode(param[5]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY,String.valueOf(externTemp), false);

					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			//小灯信息
			case RZC_GMSeriesProtocol.CH_CMD_ILL_INFO:
				if(param != null && param.length >= 0x01)
				{
					Log.i(TAG, "receive ill info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
				}
				break;
			//车身中控设定信息
			case RZC_GMSeriesProtocol.CH_CMD_AIR_CONDITON_CONTROL_INFO:
				if(param != null && param.length >= 0x02)
				{
					Log.i(TAG, "receive air conditon control info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_FAN_SPEED_AUTO_MODE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 6, 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_AIR_QUALITY_SENSOR_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 4, 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_TEMP_REGION_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 2, 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_REAR_AUTO_DEFOGGING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_FRONT_AUTO_DEFOGGING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_SEAT_AUTO_HEATING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITION_STARTUP_MODE_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 0,2)), false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_VEHICLE_SETTING_INFO:
				if(param != null && param.length >= 0x02)
				{
					Log.i(TAG, "receive vehicle setting info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FIND_VEHICLE_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEADLAMPS_LIGHT_AUTO_OFF_TIME_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 5, 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_PREVENT_AUTO_LOCK_DOOR_OPEN_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SPEED_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_LOCK_BY_SHIFT_TO_P_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 1,2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DELAY_LOCK_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_UNLOCK_LIGHT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_LOCK_FEEDBACK_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 5,2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_KEY_AND_REMOTE_UNLOCK_MODE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_REAR_WINDOW_WIPEING_IN_REVERSE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_LOCK_AGAIN_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REMOTE_STARTUP_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
					//车身中控操作标识位
					//mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REMOTE_STARTUP_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 1)), false);
				}
				break;
			//倒车雷达开关信息
			case RZC_GMSeriesProtocol.CH_CMD_PARKING_RADAR_SWITCH_INFO:
				if(param != null && param.length  >= 1 )
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_SWITCH_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_ONSTAR_PHONE_INFO:
				if(param != null && param.length >= 0x0A)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ONSTAR_PHONE_NUMBER_PROPERTY, arrayToPhoneNumber(param), true);;
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_ONSTAR_STATUS_INFO:
				if(param != null && param.length >= 0x01)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ONSTAR_SYSTEM_STATUS_PROPERTY, String.valueOf(param[0]), false);;
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_VEHICLE_SETTING_INFO2:
				if(param != null && param.length >= 0x02)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LOCK_DOOR_MODE_NEAR_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_KEY_FORGOTTEN_PROMPT_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 6)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_PERSONAL_DRIVE_MODE_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_RELOCK_DOORS_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ADAS_FLANK_BLIND_WARNING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_DOOR_LOCK_WITH_PROPERTY, String.valueOf(ServiceHelper.getBits(param[0], 0,2)), false);
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AUTO_PREVENT_BUMP_PROPERTY, String.valueOf(ServiceHelper.getBits(param[1], 6,2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_STATUS_NOTIFICATION_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MW_AUTOMATIC_WIPING_IN_RAIN_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 4)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_REMOTE_CONTROL_WINDOW_PROPERTY,String.valueOf(ServiceHelper.getBit(param[1], 3)), false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_VEHICLE_SPPED_SIGNAL:
				if(param != null && param.length >= 0x02)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY, String.format("%.1f",(param[0]*256+param[1])/16.0), false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_VEHICLE_LANGUAGE:
				if(param != null && param.length >= 0x01)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_LANGUAGE_PROPERTY, String.valueOf(param[0]), false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_ALARM_VOLUME:
				if(param != null && param.length >= 0x01)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ALARM_VOLUME_PROPERTY, String.valueOf(param[0]), false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}

				break;
			case RZC_GMSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
				
			case RZC_GMSeriesProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 0x02)
				{
					Log.i(TAG, "receive base info");
					int flStatus = ServiceHelper.getBit(param[0], 6);
					int frStatus = ServiceHelper.getBit(param[0], 7);
					int rlStatus = ServiceHelper.getBit(param[0], 4);
					int rrStatus = ServiceHelper.getBit(param[0], 5);
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
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY, String.valueOf(hoodStatus), false);
					}
				}
				break;
			// 方向盘角度
			case RZC_GMSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length == 2)
				{
					float angle = 0;
					// 向左
					if (param[1] >= 128)
					{
						angle = -(256 * (255 - param[1]) + (256 - param[0]));
					}
					// 向右
					else
					{
						angle = 256 * param[1] + param[0];
					}
					angle = angle/16.0f;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			// 协议版本号
			case RZC_GMSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
				if (param != null && param.length > 0)
				{
					String version = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					Log.i(TAG, "receive protocol version info:" + version);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, version, true);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_ONSTAR_WIRELESS_ACCESS_POINT:
				if(param != null && param.length > 0)
				{
					String wirelessAccessPoint = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ONSTAR_WIRELESS_ACCESS_POINT_PROPERTY, wirelessAccessPoint, false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_ONSTAR_WIRELESS_PASSWORD:
				if(param != null && param.length > 0)
				{
					String wirelessAccessPassword = ServiceHelper.toString(param, 0, param.length, "UTF-8");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ONSTAR_WIRELESS_PASSWORD_PROPERTY, wirelessAccessPassword, false);
				}
				break;
			case RZC_GMSeriesProtocol.CH_CMD_EXTERN_VHICLE_SETTING_INFO:
				if(param != null && param.length > 0)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_RAMP_STARTUP_ASSIST_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), false);
				}
				break;
			default:
				break;
		}
	}
	
	private float translateACTemp(int protocolTempValue)
	{
		if (protocolTempValue <= 0x00)
		{
			return VehiclePropertyConstants.AC_TEMP_LO;
		}
		else if (protocolTempValue == 0x1E)
		{
			return VehiclePropertyConstants.AC_TEMP_HI;
		}
		else if(protocolTempValue == 0x1D)
		{
			return 16.0f;
		}
		else if(protocolTempValue == 0x1F)
		{
			return 16.5f;
		}
		else if(protocolTempValue == 0x20)
		{
			return 15.0f;
		}
		else if(protocolTempValue == 0x21)
		{
			return 15.5f;
		}
		else if(protocolTempValue == 0x22)
		{
			return 31.0f;
		}
		else if(protocolTempValue >= 0x01 && protocolTempValue <= 0x1C)
		{
			return (float) Math.max(RZC_GMSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_GMSeriesProtocol.AC_HIGHEST_TEMP, RZC_GMSeriesProtocol.AC_LOWEST_TEMP + (protocolTempValue-1) * 0.5));
		}
		else 
		{
			return VehiclePropertyConstants.AC_TEMP_HI;	
		}
	}
	
	private String arrayToPhoneNumber(int[] param)
	{
		if(param == null || param.length == 0)
		{
			return null;
		}
		String phoneNumber = new String();
		int index = 0;
		int digit = (param[0] >> 4) & 0xF;
		while(digit != 0xF && index < 19)
		{
			switch (digit)
			{
				case 0x0A:
					phoneNumber += "*"; 
					break;
				case 0x0B:
					phoneNumber += "#"; 
					break;
				default:
					phoneNumber += String.valueOf(digit);
					break;
			}
			index ++;
			if(index % 2 == 0)
			{
				digit = ((param[index/2] & 0xFF) >> 4) & 0x0F;
			}
			else
			{
				digit = (param[index/2] & 0x0F);
			}
		}
		return phoneNumber;
	}
	
	private int[] phoneNumberToArray(String phoneNumber)
	{
		if(phoneNumber == null || phoneNumber.length() == 0)
		{
			return null;
		}
		byte[] phoneNumberArray = new byte[10];
		for(int i=0;i<phoneNumberArray.length;i++)
		{
			phoneNumberArray[i] = (byte)0xFF;
		}
		for(int i=0;i<phoneNumber.length() && i < 20;i++)
		{
			byte value = 0xF;
			char digit = phoneNumber.charAt(i);
			if(digit >= '0' && digit <= '9' )
			{
				value = (byte)((Integer.valueOf(phoneNumber.substring(i, i))) & 0x0F);
			}
			else if(digit == '*')
			{
				value = 0x0A;
			}
			else if(digit == '#')
			{
				value = 0x0B;
			}
			else 
			{
				Log.e(TAG, "unvalid phone number:"+phoneNumber);
				return null;
			}
			//高位
			if(i%2 == 0)
			{
				phoneNumberArray[i/2] |= (value & 0xF0);
			}
			//低位
			else
			{
				phoneNumberArray[i/2] |= (value & 0x0F);
			}
		}
		return ServiceHelper.byteArrayToIntArray(phoneNumberArray);
	}
}
