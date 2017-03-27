package com.mct.tpms;

import java.nio.ByteBuffer;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;
import com.mct.coreservices.CarService;
import com.mct.coreservices.HeadUnitMcuManager;
import com.mct.coreservices.HeadUnitMcuProtocol;
import com.mct.coreservices.MctVehicleManager;
import com.mct.utils.ServiceHelper;

import android.os.Bundle;
import android.util.Log;

public class YAT_SERIAL_TPMSManager extends TPMSManager
{
	private static String TAG = "YAT_SERIAL_TPMSManager";
	private CarService mService = null;
	private HeadUnitMcuManager mMcuMangaer = null;
	private int mTPMSDeviceStatus = VehiclePropertyConstants.TPMS_DEVICE_STATE_NO;

	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager");
		mService = service;
		mMcuMangaer = (HeadUnitMcuManager) mService.getMcuManager();
		onInitConfig();
		return true;
	}

	private void onInitConfig()
	{
		// TODO Auto-generated method stub
		mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_DEVICE_STATE_PROPERTY, String.valueOf(mTPMSDeviceStatus), false);
		setPropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_CMD_REQ_DEVICE_STATE));
		
		mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_UNIT_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_PRESSURE_UNIT_BAR), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_UNIT_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_TEMP_UNIT_C), false);
		
		mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_ALARM_RANGE_PROPERTY,ServiceHelper.intArrayToString(new int[]{0,12,VehiclePropertyConstants.TPMS_PRESSURE_UNIT_BAR}), false);
		mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_ALARM_RANGE_PROPERTY, ServiceHelper.intArrayToString(new int[]{0,110,VehiclePropertyConstants.TPMS_TEMP_UNIT_C}), false);
	}
	
	@Override
	public boolean onDeinitManager()
	{
		// TODO Auto-generated method stub
		mMcuMangaer = null;
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return super.getSupportedPropertyIds();
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		return super.getWritablePropertyIds();
	}

	@Override
	public int getPropertyDataType(int propId)
	{
		// TODO Auto-generated method stub
		return super.getPropertyDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		if(mMcuMangaer == null || mService == null)
		{
			Log.e(TAG, "service is not ready!");
			return false;
		}
		if(mTPMSDeviceStatus == VehiclePropertyConstants.TPMS_DEVICE_STATE_NO)
		{
			Log.e(TAG, "the tpms device has not connect,ignore set!");
			return false;
		}
		switch (propId)
		{
			// 胎压单位
			case VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_UNIT_PROPERTY:

				break;
			// 温度单位
			case VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_UNIT_PROPERTY:

				break;
			// 胎压报警参数
			case VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_ALARM_PARAM_PROPERTY:
				int[] alarmParam = ServiceHelper.stringToIntArray(value);
				if (alarmParam != null && alarmParam.length == 3 && alarmParam[0] > 0 && alarmParam[0] < 3)
				{
					mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, alarmParam));
				}
				else
				{
					Log.w(TAG, "set VIM_MCU_TPMS_PRESSURE_ALARM_PARAM_PROPERTY,param error:" + value);
				}
				break;
			//温度报警参数设置
			case VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_ALARM_PARAM_PROPERTY:
				mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x06, Integer.valueOf(value) }));
				break;
			//删除轮胎ID
			case VehicleInterfaceProperties.VIM_MCU_TPMS_DELTE_TIRE_ID_PROPERTY:
				mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_DELETE_TIRE_DATA, new int[] { Integer.valueOf(value) }));
				break;
			//请求重启对码
			case VehicleInterfaceProperties.VIM_MCU_TPMS_REQ_AUTO_MATCH_CODE_PROPERTY:
			{
				int tirePos = ServiceHelper.stringToIntSafe(value);
				if (tirePos > 0 && tirePos <= VehiclePropertyConstants.TIRE_REAR_SPARE)
				{
					if (mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_STUDY_TIRE_DATA, new int[] { tirePos })))
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_AUTO_MATCH_CODE_STATE_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_MATCH_CODE_STATE_START), false);
					}
				}
			}
				break;
			// 手动配码
			case VehicleInterfaceProperties.VIM_MCU_TPMS_UPDATE_TIRE_CODE_PROPERTY:
			{
				int[] tireCode = ServiceHelper.stringToIntArray(value);
				if (tireCode != null && tireCode.length == 2)
				{
					mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA, new int[] { tireCode[0], (tireCode[1] >> 16) & 0xFF, (tireCode[1] >> 8) & 0xFF, tireCode[1] & 0xFF, 0xFF, 0xFF, 0xFF, 0xFF }));
				}
			}
				break;
			case VehicleInterfaceProperties.VIM_MCU_TPMS_REQ_COMMAND_PROPERTY:
				int subCmd = ServiceHelper.stringToIntSafe(value);
				switch (subCmd)
				{
					case VehiclePropertyConstants.TPMS_CMD_REQ_DEVICE_STATE:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_READY, null));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_TIRE_DATA_ALL:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA, new int[] { 0x00 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_TIRE_DATA_FRONT_RIGHT:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA, new int[] { 0x01 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_TIRE_DATA_FRONT_LEFT:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA, new int[] { 0x02 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_TIRE_DATA_REAR_RIGHT:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA, new int[] { 0x03 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_TIRE_DATA_REAR_LEFT:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA, new int[] { 0x04 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_TIRE_DATA_REAR_SPARE:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA, new int[] { 0x05 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_ALL:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x00 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_AXIS_1:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x01 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_AXIS_2:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x02 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_AXIS_3:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x03 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_AXIS_4:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x04 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_AXIS_DRAG:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x05 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_TEMP:
						mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM, new int[] { 0x06 }));
						break;
					case VehiclePropertyConstants.TPMS_CMD_REQ_RECEIVE_INIT_DATA:
						if (mMcuMangaer.postTPMSData(pack(YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_STUDY_TIRE_DATA, new int[] { 0x00 })))
						{
							mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_INIT_DATA_STATE_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_INIT_DATA_STATE_START), false);
						}
						break;
					default:
						break;
				}
				break;
		}
		return true;
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		return super.getPropValue(propId);
	}

	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		super.onLocalReceive(data);
	}
	
	@Override
	public void onReceiveData(int[] data)
	{
		if(mService == null)
		{
			Log.e(TAG, "car service instance has been destroyed!");
			return;
		}
		int[] param = unPack(data);
		if(param == null)
		{
			Log.w(TAG, "unvalid tpms data");
			return;
		}
		switch (param[0])
		{
			case YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_READY:
				mTPMSDeviceStatus = VehiclePropertyConstants.TPMS_DEVICE_STATE_READY;
				mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_DEVICE_STATE_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_DEVICE_STATE_READY), false);
				break;
			case YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_ALARM_PARAM:
				if(param[1] == 0x00 && param.length >= 13)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_ALARM_PARAM_PROPERTY, ServiceHelper.intArrayToString(new int[]{0x01,param[2],param[3]}), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_ALARM_PARAM_PROPERTY, ServiceHelper.intArrayToString(new int[]{0x02,param[4],param[5]}), false);				
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_ALARM_PARAM_PROPERTY, String.valueOf(param[12]-50), false);
				}
				else if(param[1] >= 0x01 && param[1] <= 0x05 && param.length >= 4)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_ALARM_PARAM_PROPERTY, ServiceHelper.intArrayToString(new int[]{param[1],param[2],param[3]}), false);
				}
				else if(param.length == 2)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_ALARM_PARAM_PROPERTY, String.valueOf(param[1]-50), false);
				}
				break;
			case YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_TIRE_DATA:
				if(param[1] >= VehiclePropertyConstants.TIRE_ALL && 
						param[1] <= VehiclePropertyConstants.TIRE_REAR_SPARE &&
						param.length >= 10)
				{
					int sensorBattery = ServiceHelper.getBit(param[9], 7);
					int repoter = ServiceHelper.getBit(param[9], 6);
					int pressureStatus = VehiclePropertyConstants.TPMS_PRESSURE_STATUS_NORMAL;
					if(ServiceHelper.getBit(param[9], 4) == 1)
					{
						pressureStatus = VehiclePropertyConstants.TPMS_PRESSURE_STATUS_HIGH;
					}
					else if(ServiceHelper.getBit(param[9], 3) == 1)
					{
						pressureStatus = VehiclePropertyConstants.TPMS_PRESSURE_STATUS_LOW;
					}
					int tempStatus = ServiceHelper.getBit(param[9], 2);
					int valveStatus = ServiceHelper.getBits(param[9], 0, 2);
					if(repoter == 1)
					{
						Log.e(TAG, "no tire data for too long time( > 1hour)");
						//return;
					}
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_TIRE_DATA_PROPERTY, ServiceHelper.intArrayToString(new int[]{
							param[2],(param[3] << 16)|(param[4] << 8)|(param[5]),(int)((((param[6] & 0x03)<<8)|param[7])*0.025*10),param[8]-50,
							sensorBattery,pressureStatus,tempStatus,valveStatus}), false);
				}
				break;
			case YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_DELETE_TIRE_DATA:
				if(param.length >= 2)
				{
					if(param[1] == 0xAA)
					{
						Log.i(TAG, "delete tire id success!");
					}
					else
					{
						Log.i(TAG, "delete tire id failed:"+param[1]);
					}
				}
				break;
			case YAT_SERIAL_TPMSProtocol.YAT_TPMS_CMD_STUDY_TIRE_DATA:
				if(param[1] == VehiclePropertyConstants.TIRE_ALL && param.length == 3)
				{
					Log.i(TAG, "no tire data");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_INIT_DATA_STATE_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_INIT_DATA_STATE_END), false);
				}
				else if(param[1] == 0xFF && param.length == 2)
				{
					Log.i(TAG, "req tire data timeout");
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_INIT_DATA_STATE_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_INIT_DATA_STATE_END), false);
				}
				else if(param[1] > VehiclePropertyConstants.TIRE_ALL &&
						param[1] <= VehiclePropertyConstants.TIRE_REAR_SPARE &&
						param.length == 10)
				{
					int sensorBattery = ServiceHelper.getBit(param[9], 7);
					int repoter = ServiceHelper.getBit(param[9], 6);
					int pressureStatus = VehiclePropertyConstants.TPMS_PRESSURE_STATUS_NORMAL;
					if(ServiceHelper.getBit(param[9], 4) == 1)
					{
						pressureStatus = VehiclePropertyConstants.TPMS_PRESSURE_STATUS_HIGH;
					}
					else if(ServiceHelper.getBit(param[9], 3) == 1)
					{
						pressureStatus = VehiclePropertyConstants.TPMS_PRESSURE_STATUS_LOW;
					}
					int tempStatus = ServiceHelper.getBit(param[9], 2);
					int valveStatus = ServiceHelper.getBits(param[9], 0, 2);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_TIRE_DATA_PROPERTY, ServiceHelper.intArrayToString(new int[]{
							param[2],(param[3] << 16)|(param[4] << 8)|(param[5]),(int)((((param[6] & 0x03)<<8)|param[7])*0.025*10),param[8]-50,
							sensorBattery,pressureStatus,tempStatus,valveStatus}), false);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_TPMS_AUTO_MATCH_CODE_STATE_PROPERTY, String.valueOf(VehiclePropertyConstants.TPMS_MATCH_CODE_STATE_END), false);
				}
				break;
				
			default:
				break;
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// 封包
	public int[] pack(int cmd, int[] param)
	{
		int paramLen = (param == null) ? 0 : param.length;// 子功能号+参数
		int[] data = new int[paramLen + 6];
		data[0] = ServiceHelper.getByteFromInt((0xAA & 0xFF));
		data[1] = ServiceHelper.getByteFromInt((0x41 & 0xFF));
		data[2] = ServiceHelper.getByteFromInt((0xA1 & 0xFF));
		data[3] = ServiceHelper.getByteFromInt((paramLen + 6) & 0xFF);
		data[4] = ServiceHelper.getByteFromInt(cmd & 0xFF);
		int i = 0;
		if (param != null)
		{
			for (i = 0; i < paramLen; ++i)
			{
				data[5 + i] = ServiceHelper.getByteFromInt((param[i] & 0xFF));
			}
		}
		// checksum
		int checksum = 0;
		for (i = 0; i < (data.length - 1); i++)
		{
			checksum += data[i];
			checksum = checksum & 0xFF;
		}
		data[i] = ServiceHelper.getByteFromInt(checksum & 0xFF);
		return data;
	}

	// 解包
	public int[] unPack(int[] buffer)
	{
		int[] byReturn = null;
		int pos = 0;
		int length = buffer.length;

		// sync
		while (length >= 6)
		{
			if (buffer[pos + 0] == 0xAA && 
					buffer[pos + 1] == 0xA1 && 
					buffer[pos + 2] == 0x41)
			{
				pos += 3;
				length -= 3;
				break;
			}
			++pos;
			--length;
		}

		if (length >= 3)
		{
			// len
			int frameLen = buffer[pos++];
			int len = frameLen - 5;//协议长度表示 帧头+数据+校验
			int cmd = buffer[pos++];
			length -= 2;
			if (len <= length)
			{
				// checksum
				int sum = (0xAA + 0xA1 + 0x41 + frameLen + cmd) & 0xFF;
				byReturn = new int[len];
				byReturn[0] = cmd;
				for (int i = 0; i < (len - 1); ++i)
				{
					int data = buffer[pos++];
					sum += data;
					sum = (sum & 0xFF);
					byReturn[1+i] = data;
					length--;
				}
				if (sum != buffer[pos++])
				{
					Log.e(TAG, "MCU onRecieve checksum error!");
					byReturn = null;
				}
				length--;
			}
			else
			{
				// need more bytes
				Log.w(TAG, "receive data is not compelte!");
			}
		}
		else
		{
			// need more bytes
			Log.w(TAG, "receive data(head) is not compelte!");
		}
		return byReturn;
	}
}
