package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.carmodels.RZC_NissanSeriesManager.SystemTimeSetReceiver;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRouter.VolumeCallback;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-宝骏车系车型协议管理
public class RZC_BaoJunManager extends MctVehicleManager
{
	private static String TAG = "RZC-BaoJun";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_BAO_JUN_730_2017;
	private int mPressedKeyValue = 0;
	private boolean mShowVehicleDoorInfo = true;
	private int[] mAirConditionParam = null;
	
	private boolean mSupportSyncTime = true;

	private Timer mSyncVehicleInfoTimer = null;
	private SystemTimeSetReceiver mSystemTimeSetReceiver = new SystemTimeSetReceiver();

	
	public RZC_BaoJunManager(int carModel)
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
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "10");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4}), false);
		
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_POWER_STATUS_PROPERTY, String.valueOf(VehiclePropertyConstants.RADAR_POWER_STATUS_POWER_ON_AND_SHOW), false);
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		if(mSystemTimeSetReceiver != null)
		{
			mService.unregisterReceiver(mSystemTimeSetReceiver);
		}
		stopSyncVehicleInfoTimer();
		mShowVehicleDoorInfo = false;
		mMcuManager = null;
		mCanMangaer = null;
		
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RZC_BaoJunProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_BaoJunProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_BaoJunProtocol.getProperityPermission(RZC_BaoJunProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_BaoJunProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_BaoJunProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_BaoJunProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							break;
						default:
							break;
					}
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					break;
				case VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY:
					if(mCarModel != CarModelDefine.CAR_MODEL_BAO_JUN_730_2017)
					{
						break;
					}
					HashMap<Integer, String> sourceInfoMap = VehicleManager.stringToHashMap(value);
					if (sourceInfoMap != null && sourceInfoMap.size() > 0)
					{
						int sourceType = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_MODE_PROPERTY));
						if (sourceType == -1)
						{
							Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
							break;
						}
						Log.i(TAG, "sync source media type:" + sourceType);
						// 更新收音信息
						if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_TUNER)
						{
							Log.i(TAG, "sync tuner type info");
							int band = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY));
							if (band >= VehiclePropertyConstants.RADIO_BAND_FM1 && band <= VehiclePropertyConstants.RADIO_BAND_FM3)
							{
								band = 0x00;
							}
							else if (band >= VehiclePropertyConstants.RADIO_BAND_AM1 && band <= VehiclePropertyConstants.RADIO_BAND_AM2)
							{
								band = 0x10;
							}
							else
							{
								Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
								break;
							}
							int freq = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE, new int[] {sourceType,band,freq&0xFF,(freq>>8)&0xFF,0x00,0x00,0x00}));
						}
						// 更新媒体格式信息
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB)
						{
							Log.i(TAG, "sync usb type info");
							int currentPlayTime = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY));
							int curTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY));
							int totalTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY));
							currentPlayTime = currentPlayTime < 0 ? 0 : currentPlayTime;
							int hour = currentPlayTime/3600;
							int minute = (currentPlayTime-hour*3600)/60;
							int second = currentPlayTime/60;
							curTrack = curTrack < 0 ? 0 : curTrack;
							totalTrack = totalTrack < 0 ? 0 : totalTrack;
							String strLine1 = curTrack+" / "+totalTrack;
							String strLine2 = String.format("%02d", hour)+":"+ String.format("%02d", minute)+":"+ String.format("%02d", second);
							int[] dataLine1 = ServiceHelper.combineArray(new int[]{sourceType,0x12,0x01,strLine1.length()}, ServiceHelper.byteArrayToIntArray(strLine1.getBytes()));
							int[] dataLine2 = ServiceHelper.combineArray(new int[]{sourceType,0x12,0x02,strLine2.length()}, ServiceHelper.byteArrayToIntArray(strLine2.getBytes()));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE,dataLine1));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE, dataLine2));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							Log.i(TAG, "sync other type info");
							String strLine1 = "Other";
							int[] dataLine1 = ServiceHelper.combineArray(new int[]{sourceType,0x12,0x01,strLine1.length()}, ServiceHelper.byteArrayToIntArray(strLine1.getBytes()));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE,dataLine1));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							Log.i(TAG, "sync aux type info");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE, new int[] {sourceType, 0x00,0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{
							Log.i(TAG, "sync off type");
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE, new int[] {sourceType, 0x00,0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
							return mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE, new int[] {sourceType, 0x00,0x00, 0x00, 0x00 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							Log.i(TAG, "sync bt music type info");
							String strLine1 = "A2DP";
							int[] dataLine1 = ServiceHelper.combineArray(new int[]{sourceType,0x12,0x01,strLine1.length()}, ServiceHelper.byteArrayToIntArray(strLine1.getBytes()));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE,dataLine1));						
						}
					}
					break;
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					if(mCarModel != CarModelDefine.CAR_MODEL_BAO_JUN_730_2017)
					{
						break;
					}
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
						int connStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY));
						String number = btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_NUMBER_PROPERTY);
						int callByte = 0x80;
						if(connStatus == 1)
						{
							//来电
							callByte = 0x81;
							if(callStatus == CarService.CALL_STATE_INCOMING)
							{
								callByte = 0x01;
							}
							//呼出
							else if(callStatus == CarService.CALL_STATE_DIAL || callStatus == CarService.CALL_STATE_DIALING)
							{
								callByte = 0x03;
							}
							//通话中
							else if (callStatus == CarService.CALL_STATE_COMMUNICATING)
							{
								callByte = 0x02;
							}
							else if(callStatus == CarService.CALL_STATE_WAITING || callStatus == CarService.CALL_STATE_HOLD || callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
							{
								callByte = 0x02;
							}
							//通话结束
							else if(callStatus == CarService.CALL_STATE_TERMINATED)
							{
								callByte = 0x00;
							}
							if(callByte != 0x00 && callByte != 0x81)
							{
								int[] dataLine1 = ServiceHelper.combineArray(new int[]{0x05,callByte,0x12,0x01,number.length()}, ServiceHelper.byteArrayToIntArray(number.getBytes()));
								mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE,dataLine1));
							}
							else if(callByte == 0x00)
							{
								mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE,new int[]{0x05,callByte,0x12,0x00,0x00}));
							}
							else if(callByte == 0x81)
							{
								Log.i(TAG, "bt connected!");
							}
						}
						else
						{
							Log.i(TAG, "bt disconnected!");
							//mMcuManager.postCanData(mCanMangaer.pack(RZC_BaoJunProtocol.HC_CMD_SOURCE,new int[]{0x05,callByte,0x12,0x00,0x00}));
						}
					}
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
			case RZC_BaoJunProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_BaoJunProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_BaoJunProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_BaoJunProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_BaoJunProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_BaoJunProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}

				break;
			case RZC_BaoJunProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
			case RZC_BaoJunProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length >= 1)
				{
					Log.i(TAG, "receive vehicle door info");
					int flStatus = ServiceHelper.getBit(param[0], 6);
					int frStatus = ServiceHelper.getBit(param[0], 7);
					int rlStatus = ServiceHelper.getBit(param[0], 4);
					int rrStatus = ServiceHelper.getBit(param[0], 5);
					int trunkStatus = ServiceHelper.getBit(param[0], 3);
					int hoodStatus = ServiceHelper.getBit(param[0], 2);

					String strHisrotyFlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY);
					String strHisrotyFrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY);
					String strHisrotyRlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY);
					String strHisrotyRrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY);
					String strHisrotyTrunkStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY);
					String strHoodStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY);

					//有变化才上报
					if (mShowVehicleDoorInfo || strHisrotyFlStatus == null || strHisrotyFrStatus == null || strHisrotyRlStatus == null || strHisrotyRrStatus == null || 
							strHisrotyTrunkStatus == null || strHoodStatus == null || Integer.valueOf(strHisrotyFlStatus) != flStatus || Integer.valueOf(strHisrotyFrStatus) != frStatus || 
							Integer.valueOf(strHisrotyRlStatus) != rlStatus || Integer.valueOf(strHisrotyRrStatus) != rrStatus || 
							Integer.valueOf(strHisrotyTrunkStatus) != trunkStatus || Integer.valueOf(strHoodStatus) != hoodStatus)
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
			case RZC_BaoJunProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
				if (param != null && param.length >= 3)
				{
					float angle = param[0];
					int dir = param[1];
					// 向左
					if (dir <= 0x10)
					{
						angle = -(param[0] + (0xFF + 1) * dir);
					}
					// 向右
					else if(dir >= 0xF0)
					{
						angle = (0xFF + 1) - param[0] + (0xFF - dir) * (0xFF + 1);
					}
					else 
					{
						Log.i(TAG, "owerflow angle range!!!");
						break;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_STEERING_WHEEL_POSN_SLIDE_PROPERTY, String.valueOf(angle), false);
				}
				break;
			// 协议版本号
			case RZC_BaoJunProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
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
	
	private void startSyncVehicleInfoTimer()
	{
		if (mSyncVehicleInfoTimer != null)
		{
			Log.w(TAG, "startSyncVehicleInfoTimer,timer is exist!");
			return;
		}
		Log.i(TAG, "startSyncVehicleInfoTimer");
		// 1分钟定时发送
		mSyncVehicleInfoTimer = new Timer();
		mSyncVehicleInfoTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				setPropValue(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
			}
		}, 3000, 60 * 1000);
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
}
