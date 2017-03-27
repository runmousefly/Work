package com.mct.carmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CanManager;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.media.MediaRouter.VolumeCallback;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

//睿智诚-大众车系车型协议管理(不包括高尔夫7)
public class RZC_VolkswagenSeriesManager extends MctVehicleManager
{
	private static String TAG = "RZC-Volkswagen";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuManager = null;
	private CanManager mCanMangaer = null;
	private int mCarModel = CarModelDefine.CAR_MODEL_VOLKSWAGEN_MAGOTAN;
	private int mPressedKeyValue = 0;
	private String mSyncMediaInfo = null;
	private long mSyncMediaTick = 0;
	private boolean mShowVehicleDoorInfo = true;

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

	public RZC_VolkswagenSeriesManager(int carModel)
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
		return true;
	}

	private void initVehicleConfig()
	{
		// 初始化雷达距离范围
		//mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, "10");
		//{前左、前左中、前右中、前右、后左、后左中、后右中、后右、左前、左中前、左中后、左后、右前、右中前、右中后、右后}
		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_DIS_LEVEL_MAX_PROPERTY, 
					ServiceHelper.intArrayToString(new int[]{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10}), false);
		
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_ALL_KINDS_PROPERTY, "2");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_CONTROL_TYPE, "1");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MIN, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_VOLUME_MAX, "30");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MIN, "-9");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_SOUND_EFFECTS_MAX, "9");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MIN, "-9");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_BALANCE_VALUE_MAX, "9");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_MIN_PROPERTY, "0");
		mService.cachePropValueOnly(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_MAX_PROPERTY, "7");
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
		return RZC_VolkswagenSeriesProtocol.VEHICLE_CAN_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RZC_VolkswagenSeriesProtocol.VEHICLE_CAN_PROPERITIES.length; i++)
		{
			if (RZC_VolkswagenSeriesProtocol.getProperityPermission(RZC_VolkswagenSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]) >= RZC_VolkswagenSeriesProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RZC_VolkswagenSeriesProtocol.VEHICLE_CAN_PROPERITIES[i]);
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
		return RZC_VolkswagenSeriesProtocol.getProperityDataType(propId);
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
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x01 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_BASE_INFO }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_PARKING_STATUS_INFO }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_VEHICLE_INFO, 0x03 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_END:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SWITCH, new int[] { 0x00 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO:
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO,new
							// int[]{RZC_VolkswagenSeriesProtocol.CH_CMD_VEHICLE_SPEED_INFO}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_VEHICLE_INFO, 0x02 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO }));
							// 显示车外温度
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_VEHICLE_INFO, 0x02 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_RADAR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_FRONT_RADAR_INFO }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_REAR_RADAR_INFO }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_TPMS_INFO:
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_VEHICLE_INFO, 0x01 }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_STEERING_WHEEL_ANGLE:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE }));
							break;
						case VehiclePropertyConstants.CANBOX_CMD_REQ_VERSION_INFO:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { RZC_VolkswagenSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO }));
							break;
						// 雷达提示音
						case VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY:
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_RADAR, new int[] { 0x00, Integer.valueOf(value) }));
							break;
						default:
							break;
					}
				case VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_REQ_CONTROL_INFO, new int[] { Integer.valueOf(value) }));
					break;
				/** 功放控制音量 0-30 */
				case VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY:
					int amplifierVolume = (int) (Integer.valueOf(value) * 30.0 / 40.0);
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_AMPLIFIER, new int[] { 0x00, amplifierVolume }));
					break;
				case VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_AMPLIFIER, new int[] { 0x01, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_AMPLIFIER, new int[] { 0x02, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_AMPLIFIER, new int[] { 0x03, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_AMPLIFIER, new int[] { 0x04, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_AMPLIFIER, new int[] { 0x05, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_AMPLIFIER, new int[] { 0x06, Integer.valueOf(value) }));
					break;

				/** 导航信息显示 */
				case VehicleInterfaceProperties.VIM_NAVI_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> mapNaviInfo = VehicleManager.stringToHashMap(value);
					if (mapNaviInfo != null)
					{
						int displayEnable = Integer.valueOf(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_DISPLAY_ENABLE_PROPERTY));
						int displayType = Integer.valueOf(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_DISPLAY_TYPE_PROPERTY));
						int flagPointDisUnit = Integer.valueOf(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_FLAG_POINT_UNIT_PROPERTY));
						int destionDisUnit = Integer.valueOf(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_DESTION_DIS_UNIT_PROPERTY));
						int compassDir = Integer.valueOf(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_COMPASS_DIR_PROPERTY));
						int flagPointDis = Integer.valueOf(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_FLAG_POINT_DIS_PROPERTY));
						int destionPointDis = Integer.valueOf(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_DESTION_DIS_PROPERTY));
						int[] roadInfo = ServiceHelper.stringToIntArray(mapNaviInfo.get(VehicleInterfaceProperties.VIM_NAVI_INFO_ROAD_DIR_TYPE_PROPERTY));
						int[] param = new int[7];
						param[0] = displayEnable << 7 | displayEnable << 6 | flagPointDisUnit << 3 | destionDisUnit;
						param[1] = compassDir;
						param[2] = (flagPointDis & 0xFF);
						param[3] = (flagPointDis >> 8) & 0xFF;
						param[4] = (destionPointDis & 0xFF);
						param[5] = (destionPointDis >> 8) & 0xFF;
						mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_NAVI_INFO, ServiceHelper.combineArray(param, roadInfo)));
					}
					break;
				case VehicleInterfaceProperties.VIM_AUDIO_SOURCE_STATUS_PROPERTY:
					int[] sourceInfo = ServiceHelper.stringToIntArray(value);
					if (sourceInfo.length == 3)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x00, sourceInfo[0], sourceInfo[1] }));
						mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_ICON, new int[] { (sourceInfo[2] << 1) & 0xFF }));
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
								band = 0x10;
							}
							else
							{
								Log.e(TAG, "setPropValue Exception,propId:" + propId + ",value:" + value);
								break;
							}
							int freq = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_RADIO_CUR_FREQ_PROPERTY));
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE,new
							// int[]{0x01,0x01}));
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO,new
							// int[]{0,0,0,0,0,0}));
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_ICON,new
							// int[]{0x00}));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_RADIO, new int[] { band, freq & 0xFF, (freq >> 8) & 0xFF, 0 }));
						}
						// 更新媒体格式信息
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x08, 0x11 }));
							int currentPlayTime = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_PLAY_TIME_PROPERTY));

							int curTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_CUR_CHAPTER_PROPERTY));
							int totalTrack = ServiceHelper.stringToIntSafe(sourceInfoMap.get(VehicleInterfaceProperties.VIM_MCU_MEDIA_INFO_TOTAL_CHAPTER_PROPERTY));
							currentPlayTime = currentPlayTime < 0 ? 0 : currentPlayTime;
							curTrack = curTrack < 0 ? 0 : curTrack;
							totalTrack = totalTrack < 0 ? 0 : totalTrack;
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { totalTrack & 0xFF, (totalTrack >> 8) & 0xFF,curTrack & 0xFF, (curTrack >> 8) & 0xFF, currentPlayTime / 60, currentPlayTime % 60 }));
							//mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { curTrack & 0xFF, (curTrack >> 8) & 0xFF, totalTrack & 0xFF, (totalTrack >> 8) & 0xFF, currentPlayTime / 60, currentPlayTime % 60 }));
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO,new
							// int[]{curTrack&0xFF,(curTrack>>8)&0xFF,totalTrack&0xFF,(totalTrack>>8)&0xFF,0,0}));
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_ICON,new
							// int[]{0x00}));

						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OTHER)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x0C, 0x30 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_ICON,new
							// int[]{0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_AUX)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x07, 0x30 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
							// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_ICON,new
							// int[]{0x00}));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_OFF)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x0C, 0x30 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
							//mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x00, 0x30 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_PHONE)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x05, 0x40 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_NAVI)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x04, 0x30 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
						}
						else if (sourceType == VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_BT_MUSIC)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x0B, 0x30 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
						}
						// 非收音源要显示音量
						/*if (sourceType != VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_TUNER && sourceType != VehiclePropertyConstants.VEHICLE_MEDIA_TYPE_USB && mService != null)
						{
							setPropValue(VehicleInterfaceProperties.VIM_AUDIO_CONTROL_VOLUME_PROPERTY, mService.getCacheValue(VehicleInterfaceProperties.VIM_MCU_MEDIA_VOLUME_PROPERTY));
						}*/
					}
					break;
				case VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY:
					HashMap<Integer, String> btPhoneMapInfo = VehicleManager.stringToHashMap(value);
					if (btPhoneMapInfo != null || btPhoneMapInfo.size() > 0)
					{
						int callStatus = ServiceHelper.stringToIntSafe(btPhoneMapInfo.get(VehicleInterfaceProperties.VIM_MCU_BT_CALL_STATE_PROPERTY));
						/*
						//来电
						if(callStatus == CarService.CALL_STATE_INCOMING)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x05, 0x40 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_PHONE, new int[] {0x01}));	
						}
						//呼出
						else if(callStatus == CarService.CALL_STATE_DIAL || callStatus == CarService.CALL_STATE_DIALING)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x05, 0x40 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_PHONE, new int[] {0x02}));
						}
						//通话中
						else if (callStatus == CarService.CALL_STATE_COMMUNICATING || callStatus == CarService.CALL_STATE_WAITING || callStatus == CarService.CALL_STATE_HOLD || callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x05, 0x40 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
							//mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_PHONE, new int[] {0x03}));
						}
						//通话结束
						else if(callStatus == CarService.CALL_STATE_TERMINATED)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_PHONE, new int[] {0x00}));
						}
						else 
						{
							Log.i(TAG, "other call status:"+callStatus);
						//	mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x05, 0x40 }));
						//	mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
						}
						*/
						
						if (callStatus == CarService.CALL_STATE_INCOMING || callStatus == CarService.CALL_STATE_DIAL || callStatus == CarService.CALL_STATE_DIALING || callStatus == CarService.CALL_STATE_COMMUNICATING || callStatus == CarService.CALL_STATE_WAITING || callStatus == CarService.CALL_STATE_HOLD || callStatus == CarService.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD)
						{
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_SOURCE, new int[] { 0x05, 0x40 }));
							mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_MEDIA_INFO, new int[] { 0, 0, 0, 0, 0, 0 }));
						}
					}
					break;
				// 用volume代替
				// case
				// VehicleInterfaceProperties.VIM_AUDIO_CONTROL_MUTE_PROPERTY:
				// mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_VOLUME,new
				// int[]{0x80}));
				// break;
				case VehicleInterfaceProperties.VIM_AUDIO_CONTROL_VOLUME_PROPERTY:
					int controlVolume = Integer.valueOf(value);
					if (controlVolume == 0)
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_VOLUME, new int[] { 0x80 }));
					}
					else
					{
						mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_VOLUME, new int[] { controlVolume }));
					}
					if (mService != null && mMcuManager.getCurrentMediaMode() == VehiclePropertyConstants.MEDIA_MODE_TUNER)
					{
						mService.getMainHandler().removeCallbacks(mMediaSyncInfoRunnable);
						mService.getMainHandler().postDelayed(mMediaSyncInfoRunnable, 1000);
					}
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_RADAR, new int[] { 0x00, Integer.valueOf(value) }));
					break;
				case VehicleInterfaceProperties.VIM_VEHICLE_PARKING_MODE_PROPERTY:
					mMcuManager.postCanData(mCanMangaer.pack(RZC_VolkswagenSeriesProtocol.HC_CMD_RADAR, new int[] { 0x02, Integer.valueOf(value) }));
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
			case RZC_VolkswagenSeriesProtocol.CH_CMD_VEHICLE_SPEED_INFO:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive vehicle speed info:" + String.format("%.1f", ServiceHelper.MAKEWORD(param[0], param[1]) / 16.0f));
					// mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY,
					// String.valueOf(ServiceHelper.MAKEWORD(param[0],
					// param[1])/16.0f), true);
				}
				break;
			case RZC_VolkswagenSeriesProtocol.CH_CMD_STEERING_WHEEL_KEY:
				if (param != null && param.length == 2)
				{
					Log.i(TAG, "receive wheel key,Key:" + param[0] + ",status:" + param[1]);
					// 0 按键释放
					if (param[1] == 0 && mPressedKeyValue != RZC_VolkswagenSeriesProtocol.SWC_KEY_NONE)
					{
						// mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,
						// String.valueOf(RZC_VolkswagenSeriesProtocol.swcKeyToUserKey(mPressedKeyValue)),
						// true);
						mService.broadcastKeyEvent(RZC_VolkswagenSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_UP, 0);
						mPressedKeyValue = RZC_VolkswagenSeriesProtocol.SWC_KEY_NONE;
					}
					// 1 按键按下
					else if (param[1] == 1)
					{
						mPressedKeyValue = param[0];
						mService.broadcastKeyEvent(RZC_VolkswagenSeriesProtocol.swcKeyToUserKey(mPressedKeyValue), KeyEvent.ACTION_DOWN, 0);
					}
					// 2 按键持续按下
				}
				break;
			case RZC_VolkswagenSeriesProtocol.CH_CMD_AIR_CONDITIONING_INFO:
				if (param != null && param.length == 5)
				{
					Log.i(TAG, "receive air condition info");
					// 空调开关
					int acStatus = ServiceHelper.getBit(param[0], 7);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 7)), true);
					if (acStatus == 0)
					{
						Log.i(TAG, "AC is off");
						//mService.broadcastAirConditionInfo(0);
						mCanMangaer.showAirConditionEvent(false);
						break;
					}
					// 请求显示空调信息
					int showAcBar = ServiceHelper.getBit(param[1], 4);
					if (showAcBar == 0)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						break;
					}
					// 制冷模式
					int[] acMode = new int[5];
					acMode[0] = ServiceHelper.getBit(param[0], 6) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_OFF;
					acMode[1] = ServiceHelper.getBit(param[0], 3) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO_OFF;
					acMode[2] = ServiceHelper.getBit(param[0], 4) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AUTO2_ON : VehiclePropertyConstants.AC_COOL_MODE_AUTO2_OFF;
					acMode[3] = ServiceHelper.getBit(param[0], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_DUAL_ON : VehiclePropertyConstants.AC_COOL_MODE_DUAL_OFF;
					acMode[4] = ServiceHelper.getBit(param[4], 2) == 1 ? VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON : VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					// VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_ON:VehiclePropertyConstants.AC_COOL_MODE_AC_MAX_OFF;
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_COOL_MODE_PROPERTY, VehicleManager.intArrayToString(acMode), false);

					// 前窗除雾和后窗除雾
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_FRONT_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 1)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_DEFROST_REAR_WINDOW_PROPERTY, String.valueOf(ServiceHelper.getBit(param[0], 0)), false);

					// 循环模式
					int[] cycleMode = new int[1];
					if (ServiceHelper.getBit(param[4], 7) == 1)
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
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY, ServiceHelper.intArrayToString(new int[] { ServiceHelper.getBits(param[1], 0, 4) }), false);

					// 空调温度信息
					float[] interiorTemp = new float[2];
					// 驾驶员位温度(18-26)
					if (param[2] == 0x00)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[2] == 0x1F)
					{
						interiorTemp[0] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						interiorTemp[0] = (float) Math.max(RZC_VolkswagenSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_VolkswagenSeriesProtocol.AC_HIGHEST_TEMP, RZC_VolkswagenSeriesProtocol.AC_LOWEST_TEMP + (param[2] - 1) * 0.5));
					}

					// 副驾驶员位温度(18-26)
					if (param[3] == 0x00)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_LO;
					}
					else if (param[3] == 0x1F)
					{
						interiorTemp[1] = VehiclePropertyConstants.AC_TEMP_HI;
					}
					else
					{
						interiorTemp[1] = (float) Math.max(RZC_VolkswagenSeriesProtocol.AC_LOWEST_TEMP, Math.min(RZC_VolkswagenSeriesProtocol.AC_HIGHEST_TEMP, RZC_VolkswagenSeriesProtocol.AC_LOWEST_TEMP + (param[3] - 1) * 0.5));
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_AIR_CONDITIONING_TEMP_UNIT_PROPERTY, ServiceHelper.intArrayToString(new int[]{VehiclePropertyConstants.AC_TEMP_UNIT_C,VehiclePropertyConstants.AC_TEMP_UNIT_C}), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HVAC_FAN_TARGET_TEMP_PROPERTY, VehicleManager.floatArrayToString(interiorTemp), false);

					// 座椅加热信息
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_DRVNG_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 4, 2)), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_HEATER_PSNGR_SEAT_PROPERTY, String.valueOf(ServiceHelper.getBits(param[4], 0, 2)), false);

					if (showAcBar == 1)
					{
						//mService.broadcastAirConditionInfo(showAcBar);
						mCanMangaer.showAirConditionEvent(true);
					}
				}
				break;
			case RZC_VolkswagenSeriesProtocol.CH_CMD_REAR_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive rear radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_REAR_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}

				break;
			case RZC_VolkswagenSeriesProtocol.CH_CMD_FRONT_RADAR_INFO:
				if (param != null && param.length == 4)
				{
					Log.i(TAG, "receive front radar info");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_FRONT_RADAR_PROPERTY, VehicleManager.intArrayToString(param), false);
				}
				break;
			case RZC_VolkswagenSeriesProtocol.CH_CMD_BASE_INFO:
				if (param != null && param.length == 1)
				{
					// 0 非R档；1 R档
					int reverseState = ServiceHelper.getBit(param[0], 0);
					// 0 非P档；1 P档
					int brakeState = ServiceHelper.getBit(param[0], 1);
					int lightState = ServiceHelper.getBit(param[0], 2);
					Log.i(TAG, "receive base info(unused),reverseState:"+reverseState+",brakeState:"+brakeState+",headlightState:"+lightState);
					/*mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_REVERSE_STATUS_PROPERTY, String.valueOf(reverseState), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_BRAKE_STATUS_PROPERTY, String.valueOf(brakeState), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_HEAD_LIGHT_STATUS_PROPERTY, String.valueOf(lightState), false);
					*/
				}
				break;
			case RZC_VolkswagenSeriesProtocol.CH_CMD_VEHICLE_INFO:
				if (param != null && param.length >= 1)
				{
					int subCmd = param[0];
					switch (subCmd)
					{
						case 1:
						{
							if (param.length != 2)
							{
								break;
							}
							Log.i(TAG, "receive vehicle door info");
							int flStatus = ServiceHelper.getBit(param[1], 0);
							int frStatus = ServiceHelper.getBit(param[1], 1);
							int rlStatus = ServiceHelper.getBit(param[1], 2);
							int rrStatus = ServiceHelper.getBit(param[1], 3);
							int trunkStatus = ServiceHelper.getBit(param[1], 4);
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
							}
							
							String strAlertInfo = mService.getCacheValue(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY);
							int[] alertStatusFlags = null;
							if (strAlertInfo == null || strAlertInfo.length() == 0)
							{
								alertStatusFlags = new int[4];
							}
							alertStatusFlags = ServiceHelper.stringToIntArray(strAlertInfo);
							if (alertStatusFlags == null || alertStatusFlags.length != 4)
							{
								alertStatusFlags = new int[4];
							}
							alertStatusFlags[2] = ServiceHelper.getBit(param[1], 6) == 1 ? VehiclePropertyConstants.SECURITY_ALERT_LOW_CLEAN_FLUID_ON : VehiclePropertyConstants.SECURITY_ALERT_LOW_CLEAN_FLUID_OFF;
							alertStatusFlags[3] = ServiceHelper.getBit(param[1], 7) == 1 ? VehiclePropertyConstants.SECURITY_ALERT_SEAT_BELT_ON : VehiclePropertyConstants.SECURITY_ALERT_SEAT_BELT_OFF;
							mService.cachePropValue(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY, VehicleManager.intArrayToString(alertStatusFlags), false);
							// 手刹
							//mService.cachePropValue(VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY, String.valueOf(ServiceHelper.getBit(param[1], 5)), false);
						}
							break;
						case 2:
							if (param.length != 13)
							{
								break;
							}
							Log.i(TAG, "receive vehicle base info ");
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY, String.valueOf(param[1] * 256 + param[2]), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY, String.format("%.1f", (param[3] * 256 + param[4]) * 0.01), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY, String.format("%.1f", (param[5] * 256 + param[6]) * 0.01), false);
							// 补码显示（-50-77.5）
							float temp = 0.0f;
							if (ServiceHelper.getBit(param[7], 7) == 1)
							{
								temp = -ServiceHelper.MAKEWORD((~((param[8] - 1) & 0xFF) & 0xFF), (~(param[7] & 0xFF)) & 0xFF) * 0.1f;
							}
							// 正数
							else
							{
								temp = ServiceHelper.MAKEWORD(param[8], param[7]) * 0.1f;
							}
							temp = (float) Math.min(RZC_VolkswagenSeriesProtocol.EXTERN_HIGHEST_TEMP, Math.max(RZC_VolkswagenSeriesProtocol.EXTERN_LOWEST_TEMP, temp));
							mService.cachePropValue(VehicleInterfaceProperties.VIM_EXTERIOR_TEMP_PROPERTY, String.format("%.1f", temp), false);
							long odometer = param[9] * 65536 + param[10] * 256 + param[11];
							mService.cachePropValue(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY, String.valueOf(odometer), false);
							mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY, String.valueOf(param[12]), false);
							break;
						case 3:
						{
							if (param.length != 2)
							{
								break;
							}
							Log.i(TAG, "receive vehicle alert info");
							String strAlertInfo = mService.getCacheValue(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY);
							int[] alertStatusFlags = null;
							if (strAlertInfo == null || strAlertInfo.length() == 0)
							{
								alertStatusFlags = new int[4];
							}
							alertStatusFlags = ServiceHelper.stringToIntArray(strAlertInfo);
							if (alertStatusFlags == null || alertStatusFlags.length != 4)
							{
								alertStatusFlags = new int[4];
							}
							// 油量警告
							int fuelWarn = ServiceHelper.getBit(param[1], 7);
							alertStatusFlags[0] = (int) (fuelWarn == 1 ? VehiclePropertyConstants.SECURITY_ALERT_LOW_FUEL_ON : VehiclePropertyConstants.SECURITY_ALERT_LOW_FUEL_OFF);
							// 电池警告
							int batteryWarn = ServiceHelper.getBit(param[1], 6);
							alertStatusFlags[1] = (int) (batteryWarn == 1 ? VehiclePropertyConstants.SECURITY_ALERT_LOW_VOLTAGE_ON : VehiclePropertyConstants.SECURITY_ALERT_LOW_VOLTAGE_OFF);

							mService.cachePropValue(VehicleInterfaceProperties.VIM_SECURITY_ALERT_PROPERTY, VehicleManager.intArrayToString(alertStatusFlags), false);
						}
							break;
					}
				}
				break;
			// 方向盘角度
			case RZC_VolkswagenSeriesProtocol.CH_CMD_STEERING_WHEEL_ANGLE:
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
			case RZC_VolkswagenSeriesProtocol.CH_CMD_PARKING_STATUS_INFO:
				if (param != null && param.length == 2)
				{
					int opsStatus = ServiceHelper.getBit(param[0], 1);
					int radarTone = ServiceHelper.getBit(param[0], 0);
					// 倒车辅助状态
					String strCurParkAssistStatus = mService.getCacheValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY);
					if (strCurParkAssistStatus == null || Integer.valueOf(strCurParkAssistStatus) != opsStatus)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_PARKING_ASSIST_SYSTEM_STATUS_PROPERTY, String.valueOf(opsStatus), false);
						mService.broadcastRadarPreview(opsStatus);
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_RADAR_TONE_PROPERTY, String.valueOf(radarTone), false);
				}
				break;
			// 攻放信息
			case RZC_VolkswagenSeriesProtocol.CH_CMD_AMPLIFIER_STATUS_INFO:
				if (param != null && param.length == 8)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_AMPLIFIER_TYPE_PROPERTY, String.valueOf(param[0]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_VOLUME_LVL_PROPERTY, String.valueOf(param[1]), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BALANCE_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[2])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_FADE_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[3])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_BASS_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[4])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_ALTO_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[5])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_TREBLE_LVL_PROPERTY, String.valueOf(ServiceHelper.complementCodeToTrueCode(param[6])), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_SYNC_SPEED_VOLUME_PROPERTY, String.valueOf(param[7]), false);
				}
				break;
			// 协议版本号，验证暂不支持
			case RZC_VolkswagenSeriesProtocol.CH_CMD_PROTOCOL_VERSION_INFO:
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
}
