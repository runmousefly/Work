package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.ls.LSException;

import com.android.internal.util.ArrayUtils;
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
import android.os.TransactionTooLargeException;
import android.text.format.Time;
import android.util.FastImmutableArraySet;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-韩系车型协议管理
public class RZC_KoreaSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC_KoreaSeries";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_KOREA_KIA_K5;
	private int mPressedKeyValue = 0;
	private String mSyncMediaInfo = null;
	private long mSyncMediaTick = 0;
	private boolean mShowVehicleDoorInfo = true;
	private boolean mSupportSyncTime = true;
	
	private int[] mAirConditionParam = new int[4];
	
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
	
	public RZC_KoreaSeriesManager(int carModel)
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
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "3");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3}), false);
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE, "1");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MIN, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MAX, "35");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MIN, "-10");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MAX, "10");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MIN, "-7");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MAX, "7");
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_MIN_PROPERTY, "0");
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_MAX_PROPERTY, "7");
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
		return RZC_KoreaSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_KoreaSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_KoreaSeriesProtocol.getProperityPermission(RZC_KoreaSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_KoreaSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_KoreaSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_KoreaSeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_POWER, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_POWER, new int[] { 0x01 }));
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
						case VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME:
							int hourMode = 0x01;
							Time time = new Time();
							time.setToNow();
							String strTimeFormat = android.provider.Settings.System.getString(mService.getContentResolver(),
									android.provider.Settings.System.TIME_12_24);
							if(strTimeFormat != null && strTimeFormat.equals("12"))
							{
								hourMode = 0x00;
								if(time.hour > 12)
								{
									time.hour = time.hour-12;
								}
								else if(time.hour == 0)
								{
									time.hour = 12;
								}
							}
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_TIME_INFO, new int[] { time.minute, time.hour, hourMode }));
							break;
						default:
							break;
					}
					break;
				/** 功放控制音量 0-35 */
				case VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY:
					int amplifierVolume = (int) (Integer.valueOf(value) * 35.0 / 40.0);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_VOLUME_ADJUST, new int[] { amplifierVolume }));
					mService.cachePropValue(propId, value, false);
					break;
				case VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY:
					int fade = 0;
					String strFade = mService.getCacheValue(VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY);
					if(strFade != null)
					{
						fade = Integer.valueOf(strFade);
					}
					mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_AUDIO_BALANCE, new int[] { fade+7, Integer.valueOf(value)+7 }));
					mService.cachePropValue(propId, value, false);
					break;
				case VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY:
					int balance = 0;
					String strBalance = mService.getCacheValue(VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY);
					if(strBalance != null)
					{
						balance = Integer.valueOf(strBalance);
					}
					mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_AUDIO_BALANCE, new int[] { Integer.valueOf(value)+7,balance+7 }));
					mService.cachePropValue(propId, value, false);
					break;
				case VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY:
				{
					int alto = 0,treble = 0;
					String strAlto = mService.getCacheValue(VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY);
					String strTreble = mService.getCacheValue(VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY);
					if(strAlto != null)
					{
						alto = Integer.valueOf(strAlto);
					}
					if(strTreble != null)
					{
						treble = Integer.valueOf(strTreble);
					}
					mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_SOUND_EFFECTS, new int[] { Integer.valueOf(value)+10,alto+10,treble+10 }));
					mService.cachePropValue(propId, value, false);
				}
					break;
				case VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY:
				{
					int bass = 0,treble = 0;
					String strBass = mService.getCacheValue(VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY);
					String strTreble = mService.getCacheValue(VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY);
					if(strBass != null)
					{
						bass = Integer.valueOf(strBass);
					}
					if(strTreble != null)
					{
						treble = Integer.valueOf(strTreble);
					}
					mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_SOUND_EFFECTS, new int[] { bass+10,Integer.valueOf(value)+10,treble+10 }));
					mService.cachePropValue(propId, value, false);
				}
					break;
				case VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY:
				{
					int bass = 0,alto = 0;
					String strBass = mService.getCacheValue(VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY);
					String strAlto = mService.getCacheValue(VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY);
					if(strBass != null)
					{
						bass = Integer.valueOf(strBass);
					}
					if(strAlto != null)
					{
						alto = Integer.valueOf(strAlto);
					}
					mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_SOUND_EFFECTS, new int[] { bass+10,alto+10,Integer.valueOf(value)+10 }));
					mService.cachePropValue(propId, value, false);
				}
					break;
				case VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY:
					if (mSyncMediaInfo != null && mSyncMediaInfo.equals(value) && (SystemClock.uptimeMillis() - mSyncMediaTick) < 1000)
					{
						// filter post too fast
						Log.i(TAG, "media info sync too fast!");
						break;
					}
					mSyncMediaInfo = value;
					mSyncMediaTick = SystemClock.uptimeMillis();
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
							int band = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_BAND_PROPERTY));
							if (band >= VehiclePropertyConstants.RADIO_BAND_FM1 && band <= VehiclePropertyConstants.RADIO_BAND_FM3)
							{
								band = 0x00;
							}
							else if (band >= VehiclePropertyConstants.RADIO_BAND_AM1 && band <= VehiclePropertyConstants.RADIO_BAND_AM2)
							{
								band = 0x03;
							}
							else
							{
								Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
								break;
							}
							int freq = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));
							int freq_h = freq/100;
							int freq_l = freq%100;
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x02,band, freq/100, freq%100 }));
						}
						// 更新媒体格式信息
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB)
						{
							//mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x08, 0x11 }));
							int currentPlayTime = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY));

							int curTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY));
							int totalTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY));
							currentPlayTime = currentPlayTime < 0 ? 0 : currentPlayTime;
							int hour = currentPlayTime/3600;
							int minute = (currentPlayTime-hour*3600)/60;
							curTrack = curTrack < 0 ? 0 : curTrack;
							totalTrack = totalTrack < 0 ? 0 : totalTrack;
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x16,(curTrack >> 8) & 0xFF, (curTrack & 0xFF), hour,minute,currentPlayTime%60 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x83,0 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x12,0 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x80,0 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE)
						{
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x06,0 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x11,0}));
						}
					}
					break;
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
						int connectStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CON_STATE_PROPERTY));
						//来电
						if(callStatus == CarService.CALL_STATE_INCOMING)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x07,0x01}));
						}
						//呼出
						else if(callStatus == CarService.CALL_STATE_DIAL || callStatus == CarService.CALL_STATE_DIALING)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x07,0x03}));
						}
						//通话中
						else if (callStatus == CarService.CALL_STATE_COMMUNICATING || callStatus == CarService.CALL_STATE_WAITING || callStatus == CarService.CALL_STATE_HOLD || callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x07,0x02}));
						}
						//通话结束
						/*else if(callStatus == CarService.CALL_STATE_TERMINATED)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x07,0x06}));
						}*/
						//显示蓝牙连接状态
						else if((callStatus == CarService.CALL_STATE_TERMINATED || callStatus == CarService.CALL_STATE_IDLE) && connectStatus != -1)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_KoreaSeriesProtocol.HC_CMD_HOST_STATUS_INFO, new int[] { 0x0B,connectStatus == 1?0x04:0x05}));
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
			case RZC_KoreaSeriesProtocol.CH_CMD_EXTERN_TEMP_INFO:
				if (param != null && param.length > 0)
				{
					//华氏摄氏度
					int tempByte = param[0];
					if(param[0] == 0xFF && param.length > 1)
					{
						tempByte = param[1];
						mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_UNIT_PROPERTY, String.valueOf(VehiclePropertyConstants.AC_TEMP_UNIT_F), false);
					}
					else if(param[0] == 0xFF && param[1] == 0xFF)
					{
						tempByte = 0;
						mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_UNIT_PROPERTY, String.valueOf(VehiclePropertyConstants.AC_TEMP_UNIT_C), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_UNIT_PROPERTY, String.valueOf(VehiclePropertyConstants.AC_TEMP_UNIT_C), false);
					}
					int temp = ServiceHelper.getBits(tempByte, 0, 7);
					if(ServiceHelper.getBit(tempByte, 7) == 1)
					{
						temp = -temp;
					}
					Log.i(TAG, "receive extern temp info:"+temp );
					mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.format("%.1f", temp*1.0f), false);
				}
				break;
			case RZC_KoreaSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length >= 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					//旋钮操作
					if(param[0] == RZC_KoreaSeriesProtocol.SWC_KEY_SCROLL_UP ||
							param[0] == RZC_KoreaSeriesProtocol.SWC_KEY_SCROLL_DOWN ||
							param[0] == RZC_KoreaSeriesProtocol.PANEL_KEY_VOLUME_UP ||
							param[0] == RZC_KoreaSeriesProtocol.PANEL_KEY_VOLUME_DOWN ||
							param[0] == RZC_KoreaSeriesProtocol.PANEL_KEY_TUNER_UP ||
							param[0] == RZC_KoreaSeriesProtocol.PANEL_KEY_TUNER_DOWN)
					{
						//for(int i=0;i<param[1];i++)
						{
							mService.broadcastKeyEvent(RZC_KoreaSeriesProtocol.swcKeyToUserKey(param[0]), KeyEvent.ACTION_DOWN, param[1]);
							mService.broadcastKeyEvent(RZC_KoreaSeriesProtocol.swcKeyToUserKey(param[0]), KeyEvent.ACTION_UP,0);
						}
						mPressedKeyValue =  RZC_KoreaSeriesProtocol.SWC_KEY_NONE;
					}
					// 0 按键释放
					else if (param[1] == 0 && mPressedKeyValue != RZC_KoreaSeriesProtocol.SWC_KEY_NONE)
					{
						mService.broadcastKeyEvent(RZC_KoreaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_KoreaSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下（旋钮操作）
					else if (param[1] >= 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_KoreaSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_KoreaSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length >= 0x04)
				{
					Log.i(TAG, "receive air condition info");
					//空调开关
					int fanSpeed = param[2];
					if(fanSpeed == 0)
					{
						Log.i(TAG, "AC is off");
						System.arraycopy(param,0,mAirConditionParam, 0, 4);
						mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(0), true);
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					if(mAirConditionParam != null && ServiceHelper.compareIntArray(mAirConditionParam, param,4))
					{
						Log.i(TAG, "filter no change air condition data");
						break;
					}
					System.arraycopy(param,0,mAirConditionParam, 0, 4);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(1), true);
					
					//温度单位
					int leftTempUnit = 0,rightTempUnit = 0;
					if(param[0] >= 0 && param[0] <= 30)
					{
						leftTempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_C;					
					}
					else
					{
						leftTempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_F;
					}
					if(param[1] >= 0 && param[1] <= 30)
					{
						rightTempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_C;					
					}
					else
					{
						rightTempUnit = VehiclePropertyConstants.AC_TEMP_UNIT_F;
					}
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{leftTempUnit,rightTempUnit}), false);
					// 空调温度信息
					float[] interiorTemp = new float[2];
					// 驾驶员位温度
					if (param[0] == 0)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[0] == 30)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						if(leftTempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_C)
						{
							interiorTemp[0] = (float) Math.max(RZC_KoreaSeriesProtocol.AC_LOWEST_TEMP_C, Math.min(RZC_KoreaSeriesProtocol.AC_HIGHEST_TEMP_C, RZC_KoreaSeriesProtocol.AC_LOWEST_TEMP_C + (param[0] - 1) * 0.5));
						}
						else 
						{
							interiorTemp[0] = param[0];
						}
					}

					// 副驾驶员位温度(18-26)
					if (param[1] == 0)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[1] == 30)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						if(rightTempUnit == VehiclePropertyConstants.AC_TEMP_UNIT_C)
						{
							interiorTemp[1] = (float) Math.max(RZC_KoreaSeriesProtocol.AC_LOWEST_TEMP_C, Math.min(RZC_KoreaSeriesProtocol.AC_HIGHEST_TEMP_C, RZC_KoreaSeriesProtocol.AC_LOWEST_TEMP_C + (param[1] - 1) * 0.5));
						}
						else 
						{
							interiorTemp[1] = param[1];
						}
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);

					// 制冷模式
					int[] acMode = new int[3];
					acMode[0] = ServiceHelper.getBit(param[3], 0) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[3], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[3], 1) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 循环模式
					int[] cycleMode = new int[1];
					cycleMode[0] = ServiceHelper.getBit(param[3], 7) == 1 ? VehiclePropertyConstants.AC_INTERIOR_CYCLE_MODE : VehiclePropertyConstants.AC_EXTERIOR_CYCLE_MODE;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_CYCLE_MODE_PROPERTY, VehicleManager.intArrayToString(cycleMode), false);

					// 风向
					int[] windDirInfo = new int[3];
					windDirInfo[0] = ServiceHelper.getBit(param[3], 6) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_UP_OFF;
					windDirInfo[1] = ServiceHelper.getBit(param[3], 3) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_CENTER_OFF;
					windDirInfo[2] = ServiceHelper.getBit(param[3], 4) == 1 ? VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_ON : VehiclePropertyConstants.HVAC_FAN_DIRECTION_DOWN_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_DIRECTION_PROPERTY, VehicleManager.intArrayToString(windDirInfo), false);
					
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, VehicleManager.intArrayToString(new int[]{fanSpeed}), false);

					// 前窗除雾和后窗除雾
					if(fanSpeed == 8 && ServiceHelper.getBit(param[3], 6) == 1)
					{
						//Max Front
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 6)), false);
					}
					else
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, "0", false);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[3], 5)), false);

					
					mCanMangaer.showAirConditionEvent(true);
				}
				break;
			case RZC_KoreaSeriesProtocol.CH_CMD_RADAR_INFO:
				if (param != null && param.length >= 0x02)
				{
					Log.i(TAG, "receive rear radar info");
					int frontRadar[] = new int[4];
					int rearRadar[] = new int[4];
					frontRadar[0] =  ServiceHelper.getBits(param[0], 6, 2);
					frontRadar[1] =  ServiceHelper.getBits(param[0], 4, 2);
					frontRadar[2] =  ServiceHelper.getBits(param[0], 2, 2);
					frontRadar[3] =  ServiceHelper.getBits(param[0], 0, 2);
					rearRadar[0] =  ServiceHelper.getBits(param[1], 6, 2);
					rearRadar[1] =  ServiceHelper.getBits(param[1], 4, 2);
					rearRadar[2] =  ServiceHelper.getBits(param[1], 2, 2);
					rearRadar[3] =  ServiceHelper.getBits(param[1], 0, 2);
					for(int i=0;i<4;i++)
					{
						frontRadar[i] = tramslateRadarDistance(frontRadar[i]);
						rearRadar[i] = tramslateRadarDistance(rearRadar[i]);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(frontRadar), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(rearRadar), false);
				}

				break;
			case RZC_KoreaSeriesProtocol.CH_CMD_VEHICLE_DOOR_INFO:
				if (param != null && param.length > 0)
				{
					Log.i(TAG, "receive vehicle door info");
					int flStatus = ServiceHelper.getBit(param[0], 0);
					int frStatus = ServiceHelper.getBit(param[0], 1);
					int rlStatus = ServiceHelper.getBit(param[0], 2);
					int rrStatus = ServiceHelper.getBit(param[0], 3);
					int trunkStatus = ServiceHelper.getBit(param[0], 4);
					int hoodStatus = ServiceHelper.getBit(param[0], 5);

					String strHisrotyFlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY);
					String strHisrotyFrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY);
					String strHisrotyRlStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY);
					String strHisrotyRrStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY);
					String strHisrotyTrunkStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY);
					String strHisrotyHoodStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY);

					//有变化才上报
					if (mShowVehicleDoorInfo || strHisrotyFlStatus == null || strHisrotyFrStatus == null || strHisrotyRlStatus == null || strHisrotyRrStatus == null || 
							strHisrotyTrunkStatus == null || strHisrotyHoodStatus == null || Integer.valueOf(strHisrotyFlStatus) != flStatus || Integer.valueOf(strHisrotyFrStatus) != frStatus || 
							Integer.valueOf(strHisrotyRlStatus) != rlStatus || Integer.valueOf(strHisrotyRrStatus) != rrStatus || 
							Integer.valueOf(strHisrotyTrunkStatus) != trunkStatus || Integer.valueOf(strHisrotyHoodStatus) != hoodStatus)
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
			//2017款KX5的CLIMATE键按一次会上报两次，一次1,一次0,现只处理0值显示空调
			case RZC_KoreaSeriesProtocol.CH_CMD_AC_CLIMATE_STATUS:
				if (param != null && param.length > 0)
				{
					Log.i(TAG, "receive ac climate info");
					int acPower = ServiceHelper.stringToIntSafe(mService.getCacheValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY));
					if(acPower == 1 && param[0] == 0)
					{
						mCanMangaer.showAirConditionEvent(true);
					}
				}
				break;
			case RZC_KoreaSeriesProtocol.CH_CMD_BACKLIGHT_INFO:
				if (param != null && param.length > 0)
				{
					Log.i(TAG, "receive backlight info:"+param[0]);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BRIGHTNESS_LVL_PROPERTY, String.valueOf(param[0]),false);
				}
				break;
			// 协议版本号，验证暂不支持
			case RZC_KoreaSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
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
		switch (originDistance)
		{
			case 3:
				return 0x01;
			case 2:
				return 0x02;
			case 1:
				return 0x03;
			case 0:
				return 0x00;
			default:
				break;
		}
		return 0;
	}
}
