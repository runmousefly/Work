package com.mct.coreservices;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;
import com.mct.serial.mirror.McuSerial;
import com.mct.utils.ServiceHelper;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;

public class MirrorMcuManager extends MctVehicleManager implements McuSerial.Listener
{
	public static String TAG = "MirrorMcuManager";
	
	public static final int  HDR_SIZE = 128;
	public static final int  PRODUCT_NAME_LEN = 32;
	public static final int  HDR_VERSION = 2;
	public static final int  HDR_MARK = 0x55AA55AA;

	/* header type */
	public static final int  HDR_TYPE_IMAGE = 0;/* image header */
	public static final int  HDR_TYPE_PACKAGE = 1; /* package header, include several images */

	/* image type */
	public static final int  IMAGE_TYPE_BOOT  = 0x424F4F54;   /* BOOT image */
	public static final int  IMAGE_TYPE_APPL  = 0x4150504C;   /* APPL image */
	
	private final static String MCU_ORIGINAL_VERSION 		= "B1600-000--V1.01";
	private final static String ACTION_MCT_WAKEUP_MODE 	= "com.mct.wakeup.mode";
	private final static String KEY_WAKEUP_MODE		 			= "wakeup_mode";//0 unknown 1 acc_on 2 gensor  4 modem
	
	
	private CarService mService = null;
	private McuSerial mSerial = null;
	private boolean mbWaitReturn = false;

	private LinkedBlockingQueue<int[]> mlsReturn = new LinkedBlockingQueue<int[]>(); // 阻塞型队列，用于等待数据

	private String mMcuUpgradePath = null;
	private boolean mSerialConnect = false;
	private int mMcuUpgradeState = VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN;

	private String mMcuVersion = null; // MCU软件版本
	private int mMcuType = 0; // MCU类型
	private int mMcuState = VehiclePropertyConstants.MCU_STATE_UNKNOWN;
	private String mMcuDescription = null; // MCU描述信息
	private int[] mBootCheckSum = new int[8]; // MCU BOOT CheckSum
	private int[] mAppCheckSum = new int[8]; // MCU APP CheckSum
	private int mMcuVoltage = 0; // 0-255
	private int mMcuSleepReason = 0; // 1: ACC OFF 2: 过高电压 4: 过低电压

	private int mIAPEraseAddress = 0;
	private int mRetBootCheckSum = 0;
	private int mRetAppCheckSum = 0;
	
	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitMcuManager");
		mService = service;
		mSerial = new McuSerial();
		mSerial.init(ServiceHelper.DEFAULT_PACKAGE_LEN, ServiceHelper.WAIT_SHORT, ServiceHelper.WAIT_SHORT, McuSerial.DEVICE_TYPE_MCU_MAIN);
		mSerial.requestListener(this);
		Log.i(TAG, "begin to open serial");
		// 初始化串口数据收发配置
		for (int i = 0; i < ServiceHelper.REPEAT_MAXIMUM; ++i)
		{
			try
			{
				if (mSerial.open() != 0)
				{
					Log.i(TAG, "open serial success,and retry count:" + i);
					mSerialConnect = true;
					Thread.sleep(100);
					// ARM启动完成
					if(!sendArmReady())
					{
						Log.e(TAG, "sendArmReady Faied");
					}
					//reqMcuState();
					//获取失败(低mcu版本)或者IBOOT模式
					if(mMcuVersion == null || mMcuVersion.equals("B1600-000--V1.06") || getMcuState() <= 0)
					{
						handleExceptionInWakeup();
					}
					break;
				}
				else
				{
					Log.i(TAG, "open serial failed and retry!");
					mSerial.close();
				}
				Thread.sleep(ServiceHelper.WAIT_ELAPSE);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if(!mSerialConnect)
		{
			Log.e(TAG, "open serial error!");
			handleExceptionInWakeup();
		}
		Log.i(TAG, "finish to open serial");
		return true;
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		if(!mService.isWakeupInit())
		{
			//关屏
			Log.i(TAG, "Close Screen BackLight");
			ServiceHelper.enableMirrorScreen(false);
		}
		if(mSerial != null)
		{
			//打印休眠原因以及MCU电压
			int sleepReason = getMcuLastSleepReason();
			int voltage = getMcuVoltage();
			Log.w(TAG, "McuSleepReason:"+sleepReason+",McuVoltage:"+voltage);
			if(mSerial.close() > 0)
			{
				mSerialConnect = false;
			}
			mSerial = null;
			mService = null;
			mMcuState = VehiclePropertyConstants.MCU_STATE_UNKNOWN;
		}
		return true;

	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return MirrorMcuProtocol.MIRROR_MCU_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < MirrorMcuProtocol.MIRROR_MCU_PROPERITIES.length; i++)
		{
			if (MirrorMcuProtocol.getProperityPermission(MirrorMcuProtocol.MIRROR_MCU_PROPERITIES[i]) >= MirrorMcuProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(MirrorMcuProtocol.MIRROR_MCU_PROPERITIES[i]);
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
		return MirrorMcuProtocol.getProperityDataType(propId);
	}
	
	// 转到本地进程空间，避免后续数据处理出错
	@Override
	public void onLocalReceive(Bundle bundle)
	{
		if(bundle == null)
		{
			return;
		}
		int []data = bundle.getIntArray(CarService.KEY_DATA);
		// 返回get的数据
		if (mbWaitReturn)
		{
			Log.i(TAG, "notify wait data:"+ServiceHelper.toString(data));
			try
			{
				mlsReturn.put(data);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
		int type = data[0];
		int cmd = data[1];
		// notify类型消息才会主动上报
		//if (type == 0x05)
		{
			int[] param = new int[data.length - 2];
			System.arraycopy(data, 2, param, 0, param.length);
			onReceiveData(type, cmd, param);
		}
	}		

		@Override
		public boolean setPropValue(int propId, String value)
		{
//			if(!ArrayUtils.contains(MirrorMcuProtocol.MIRROR_MCU_PROPERITIES, propId))
//			{
//				Log.e(TAG, "not support this property,ID:"+propId);
//				return false;
//			}
			switch (propId)
			{
				case VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY:
					mMcuUpgradePath = value;
					break;
				case VehicleInterfaceProperties.VIM_MCU_REQ_COMMAND_PROPERTY:
					if (Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_RESET)
					{
						reqMCUReset();
					}
					else if (Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_UPGRADE)
					{
						if (mMcuUpgradePath == null || mMcuUpgradePath.length() == 0)
						{
							Log.e(TAG, "mcu upgrade path is empty");
							break;
						}
						if (mMcuUpgradeState != VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN )
						{
							Log.e(TAG, "mcu is in update");
							break;
						}
						// 开始MCU升级
						if (!mcuUpdate(mMcuUpgradePath))
						{
							onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "check upgrade file failed");
						}
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_VERSION)
					{
						return reqMcuVersion();
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_LAST_SLEEP_REASON)
					{
						return reqMcuLastSleepReason();
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_STATE)
					{
						return reqMcuState();
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_VOLTAGE)
					{
						return reqMcuVoltage();
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_OPEN_SERIAL)
					{
						Log.i(TAG, "req to open serial");
						if (!mSerialConnect)
						{
							if (mSerial.open() != 0)
							{
								mSerialConnect = true;
								break;
							}
						}
						else
						{
							sendArmReady();
						}
					}
					else if(Integer.valueOf(value) == VehiclePropertyConstants.MCU_CMD_REQ_CLOSE_SERIAL)
					{
						Log.i(TAG, "req to close serial");
						if (mSerialConnect)
						{
							if(mSerial.close() > 0)
							{
								mSerialConnect = false;
							}
						}
					}
					break;
				default:
					return false;
			}
			return true;
		}

		@Override
		public String getPropValue(int propId)
		{
			switch (propId)
			{
				case VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY:
					return mMcuVersion;
				case VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY:
					return String.valueOf(mMcuVoltage);
				case VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY:
					return String.valueOf(mMcuSleepReason);
				case VehicleInterfaceProperties.VIM_MCU_UPGRADE_PATH_PROPERTY:
					return mMcuUpgradePath;
				case VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY:
					return String.valueOf(mMcuUpgradeState);
				default:
					break;
			}
			return null;
		}
		
	@Override
	public void onReceive(ByteBuffer buffer)
	{
		Log.i(TAG, "MCU onReceive: " + ServiceHelper.toHexString(buffer));
		if (buffer == null) { return; }
		int[] data = new int[buffer.position()];
		for (int i = 0; i < buffer.position(); i++)
		{
			data[i] = (buffer.get(i) & 0xFF);
		}
		if (data == null || data.length == 0)
		{
			Log.e(TAG, "onReceive data is empty");
			return;
		}
		else if (data.length < 2)
		{
			Log.e(TAG, "onReceive data is not incomplete");
			return;
		}
		Message msg = mService.getMainHandler().obtainMessage();
		msg.what = CarService.MSG_RECEIVE_MCU_DATA;
		msg.arg1 = data.length;
		msg.obj = data;
		Bundle bundle = new Bundle();
		bundle.putIntArray(CarService.KEY_DATA, data);
		msg.setData(bundle);
		mService.getMainHandler().sendMessage(msg);
	}

	@Override
	public void onSend(ByteBuffer data)
	{
		Log.v(TAG, "MCU onSend: " + ServiceHelper.toHexString(data));
	}
	
	private int getMcuLastSleepReason()
	{
		int[] retData = sendData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_SYS_STATUS, 0x00, new int[] { 0 }, true, ServiceHelper.WAIT_NORMAL);
		if (retData != null)
		{
			onHandleSysStatus(retData);
			return mMcuSleepReason;
		}
		return -1;
	}
	
	
	
	private int getMcuState()
	{
		if(mMcuState == VehiclePropertyConstants.MCU_STATE_BOOT)
		{
			return mMcuState;
		}
		int[] retData = sendData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_SYS_STATUS, 0x01, new int[] { 1 }, true, ServiceHelper.WAIT_MIDDLE);
		if (retData != null)
		{
			onHandleSysStatus(retData);
			return mMcuState;
		}
		return -1;
	}

	private int getMcuVoltage()
	{
		int[] retData = sendData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_VOLTAGE, -1, null, true, ServiceHelper.WAIT_NORMAL);
		if (retData != null)
		{
			onHandleMcuVoltage(retData);
			return mMcuVoltage;
		}
		return -1;
	}
	

	private String getMcuVersion()
	{
		if (mMcuVersion == null)
		{
			int[] retData = sendData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_VERSION, -1, null, false, ServiceHelper.WAIT_MIDDLE);
			if (retData != null)
			{
				onHandleMcuVersion(retData);
				return mMcuVersion;
			}
		}
		Log.i(TAG, "getMcuVersion return cache value");
		return mMcuVersion;
	}
	
	private boolean reqMcuVersion()
	{
		return postData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_VERSION, null, true);
	}

	private boolean reqMcuLastSleepReason()
	{
		return postData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_SYS_STATUS, new int[] { 0 }, true);
	}
	
	private boolean reqMcuState()
	{
		return postData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_SYS_STATUS, new int[] { 1 }, false);
	}

	private boolean reqMcuVoltage()
	{
		return postData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_VOLTAGE, null, true);
	}

	private boolean sendIAPCmd(int subCmd, int returnAck, int[] data)
	{
		int[] retData = null;
		retData = sendData(MirrorMcuProtocol.MCU_CMD_TYPE_SET, MirrorMcuProtocol.CMD_IAP, returnAck, data, false, ServiceHelper.WAIT_LONG);

		if (retData == null || retData.length < 2) { return false; }
		if (retData[0] != returnAck || retData[1] != MirrorMcuProtocol.MCU_IAP_NO_ERROR) 
		{ 
			Log.i(TAG, "expected ack:"+returnAck+",actual return ack:"+retData[0]+",and return code:"+retData[1]);
			return false; 
		}
		// erase
		if (subCmd == MirrorMcuProtocol.SUB_CMD_IAP_ERASE)
		{
			if (retData.length < 6) { return false; }
			mIAPEraseAddress =retData[2] << 24 | retData[3] << 16 | retData[4] << 8 | retData[5];
		}
		else if (subCmd == MirrorMcuProtocol.SUB_CMD_IAP_VERTIFY)
		{
			if (retData.length < 10) { return false; };
			mRetBootCheckSum = retData[2] << 24 | retData[3] << 16 | retData[4] << 8 | retData[5];
			mRetAppCheckSum = retData[6] << 24 | retData[7] << 16 | retData[8] << 8 | retData[9];
		}
		return true;
	}
	
	private int[] iap_read(int readAddr,int len)
	{
		//read data
		int[] readParamData = new int[9];
		readParamData[0] = MirrorMcuProtocol.SUB_CMD_IAP_READ;
		readParamData[1] = (readAddr >> 24) & 0xFF;
		readParamData[2] = (readAddr >> 16) & 0xFF;
		readParamData[3] = (readAddr >> 8) & 0xFF;
		readParamData[4] = readAddr & 0xFF;
		readParamData[5] = 0;
		readParamData[6] = 0;
		readParamData[7] = (len >> 8) & 0xFF;
		readParamData[8] = len & 0xFF;
		int [] result = null;
		int []retData = sendData(MirrorMcuProtocol.MCU_CMD_TYPE_GET, MirrorMcuProtocol.CMD_IAP, MirrorMcuProtocol.MCU_IAP_ACK_READ, readParamData, false, ServiceHelper.WAIT_MIDDLE);
		//Log.i(TAG,"IAP Read Data length: "+(retData == null ? 0 : retData.length)+",excepted length:"+(len+6));
		//0 subCmd ,1 errorCode,2-5 Address
		if(retData != null && retData.length >= len+6)
		{
			if(retData[0] != MirrorMcuProtocol.MCU_IAP_ACK_READ || retData[1] != MirrorMcuProtocol.MCU_IAP_NO_ERROR)
			{
				Log.e(TAG, "iap read error code:"+retData[0]);
				return null;
			}
			result = new int[len];
			System.arraycopy(retData, 6, result, 0, len);
		}
		return result;
	}

	private boolean sendArmReady()
	{
		if(mMcuVersion != null && mMcuVersion.length() > 0)
		{
			Log.i(TAG, "post arm ready");
			return postData(MirrorMcuProtocol.MCU_CMD_TYPE_MSG, MirrorMcuProtocol.CMD_READY, new int[] { 0x52, 0x44 }, false);
		}
		Log.i(TAG, "send arm ready");
		int[] retData = sendData(MirrorMcuProtocol.MCU_CMD_TYPE_MSG, MirrorMcuProtocol.CMD_READY, new int[] { 0x52, 0x44 }, MirrorMcuProtocol.CMD_VERSION,-1,false,ServiceHelper.WAIT_MIDDLE);
		if (retData != null)
		{
			onHandleMcuVersion(retData);
			return true;
		}
		return false;
	}

	private boolean reqMCUReset()
	{
		if (postData(MirrorMcuProtocol.MCU_CMD_TYPE_MSG, MirrorMcuProtocol.CMD_RESET, new int[] { 0x19, 0x82, 0x06, 0x12 }, false))
		{
			return true;
		}
		return false;
	}
	
	private List<McuUpgradeData> checkUpdateFile(String path)
	{
		// 升级文件校验
		byte[] byFile = null;
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(path);
			byFile = new byte[is.available()];	
			if (is.read(byFile, 0, byFile.length) < byFile.length)
			{
				Log.w(TAG, "update mcu read file failed: " + path);
				is.close();
				return null;
			}
			if(is != null)
			{
				is.close();
				is = null;
			}
			List<McuUpgradeData> updateDataFrames = new ArrayList<MirrorMcuManager.McuUpgradeData>();
			int pos = 0;
			//解包头
			McuUpgradeData packageHead = new McuUpgradeData();
			if(packageHead.initFrameData(byFile, pos))
			{
				pos += HDR_SIZE;
				boolean checkRet = true;
				//解数据帧
				while(pos < byFile.length)
				{
					McuUpgradeData frameData = new McuUpgradeData();
					if(!frameData.initFrameData(byFile, pos))
					{
						checkRet = false;
						break;
					}
					updateDataFrames.add(frameData);
					pos += frameData.frame_size+HDR_SIZE;
				}
				//包确认正确
				if(checkRet)
				{
					int checksum = 0;
					for(int i=0;i<updateDataFrames.size();i++)
					{
						checksum += updateDataFrames.get(i).frame_check_sum;
					}
					if(checksum != mUpdateImgCheckSum)
					{
						Log.e(TAG, "package checksum is error!");
						return null;
					}
				}
			}
			return updateDataFrames;
			
		} catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				if (is != null)
					is.close();
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}		
		return null;
	}
	
	private boolean mcuUpdate(final String path)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				
				onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_INIT, null);
				List<McuUpgradeData> updateData = checkUpdateFile(path);
				if(updateData == null || updateData.size() == 0)
				{
					Log.i(TAG, "check update file failed!");
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "check update file failed");
					return;
				}
				Log.i(TAG, "check update file success!");
				
				// TODO Auto-generated method stub
				if(mMcuVersion == null)
				{
					//获取Mcu版本号
					mMcuVersion = getMcuVersion();
				}
				// v1.01 doesn't support init command
				if(mMcuState == VehiclePropertyConstants.MCU_STATE_BOOT || (mMcuVersion != null && !mMcuVersion.equals(MCU_ORIGINAL_VERSION)))
				{
					// 1. iap init
					if (!sendIAPCmd(MirrorMcuProtocol.SUB_CMD_IAP_INIT, MirrorMcuProtocol.MCU_IAP_ACK_INIT, new int[] { MirrorMcuProtocol.SUB_CMD_IAP_INIT, 0 }))
					{
//						if(mMcuState != VehiclePropertyConstants.MCU_STATE_BOOT)
//						{
//							onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "IAP init failed");
//							return;
//						}
//						else
						{
							Log.w(TAG, "IAP init failed");
						}
					}
				}
				
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// 2. iap_start
				onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_START, null);
				if (!sendIAPCmd(MirrorMcuProtocol.SUB_CMD_IAP_START, MirrorMcuProtocol.MCU_IAP_ACK_START, new int[] { MirrorMcuProtocol.SUB_CMD_IAP_START,0}))
				{
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "IAP start failed");
					return;
				}
				
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// 3. iap_erase
				onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_ERASE, null);
				if (!sendIAPCmd(MirrorMcuProtocol.SUB_CMD_IAP_ERASE, MirrorMcuProtocol.MCU_IAP_ACK_ERASE, new int[] { MirrorMcuProtocol.SUB_CMD_IAP_ERASE }))
				{
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "IAP erase failed");
					return;
				}
				

				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// 4. write data
				onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_WRITE, null);
				float progress = 0;
				int sentSize = 0;
				for(int i = 0; i < updateData.size(); i++ )
				{
					int nPacketCount = updateData.get(i).frame_size / HDR_SIZE + (updateData.get(i).frame_size % HDR_SIZE == 0 ? 0 : 1);
					// 分包
					int write_addr = updateData.get(i).frame_entry;
					if(write_addr < mIAPEraseAddress)
					{
						Log.w(TAG,"unvalid frame entry address:"+write_addr+",and continue next frame");
						mUpdateImgDataSize -= updateData.get(i).frame_size;
						continue;
					}
					byte []FrameData = updateData.get(i).data;
					for (int count = 0; count < nPacketCount; count++)
					{
						int len = HDR_SIZE;
						if (count == nPacketCount - 1)
						{
							len = updateData.get(i).frame_size - count * HDR_SIZE;
						}
						int[] data = new int[len + 5];
						int []writeData = new int[len];
						data[0] = MirrorMcuProtocol.SUB_CMD_IAP_WRITE;
						data[1] = (write_addr >> 24) & 0xFF;
						data[2] = (write_addr >> 16) & 0xFF;
						data[3] = (write_addr >> 8) & 0xFF;
						data[4] = write_addr & 0xFF;
						for (int index = 0; index < len; index++)
						{
							data[5 + index] = FrameData[index+count*HDR_SIZE] & 0xFF;
							writeData[index] = FrameData[index+count*HDR_SIZE] & 0xFF;
						}
						if (!sendIAPCmd(MirrorMcuProtocol.SUB_CMD_IAP_WRITE, MirrorMcuProtocol.MCU_IAP_ACK_WRITE, data))
						{
							onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "IAP write failed");
							return;
						}
						
						int []readData = iap_read(write_addr,len);
						if(readData == null || readData.length != len || !ServiceHelper.compareIntArray(readData,writeData))
						{
							Log.e(TAG, "write and read is not the same");
							onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "IAP write failed");
							return;
						}
						
						sentSize += len;
						float curProgress = (float)(Math.round(sentSize*1.0f/mUpdateImgDataSize*1000))/1000;
						if(curProgress != progress)
						{
							progress = curProgress;
							if(mService != null)
							{
								mService.dispatchData(VehicleInterfaceProperties.VIM_MCU_UPGRADE_PROGRESS_PROPERTY, String.valueOf(progress));
							}
						}
						write_addr += len;
					}

				}
				
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// 5. Vertify
				onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_VERTIFY, null);
				if (!sendIAPCmd(MirrorMcuProtocol.SUB_CMD_IAP_VERTIFY, MirrorMcuProtocol.MCU_IAP_ACK_VERTIFY, new int[] { MirrorMcuProtocol.SUB_CMD_IAP_VERTIFY }))
				{
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "IAP vertify failed");
					return;
				}
				

				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// 比较CheckSum
//				if (mRetAppCheckSum != mUpdateFileCheckSum)
//				{
//					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "upgrade file checksum:" + mUpdateFileCheckSum + ",but mcu return checksum:" + mRetAppCheckSum);
//					return;
//				}
				// 6. Exit
				onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_SUCCESS, null);
				if (!sendIAPCmd(MirrorMcuProtocol.SUB_CMD_IAP_EXIT, MirrorMcuProtocol.MCU_IAP_ACK_EXIT, new int[] { MirrorMcuProtocol.SUB_CMD_IAP_EXIT }))
				{
					onMcuUpgradeStateChange(VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED, "IAP exit failed");
					return;
				}
				
				try
				{
					Thread.sleep(3000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// MCU Reset
				reqMCUReset();
			}
		}).start();
		return true;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean postData(int cmdType, int cmd, int[] param, boolean bNeedAck)
	{
		try
		{
			if (!mSerialConnect)
			{
				Log.w(TAG, "PostData failed,Serial is close");
				return false;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		ByteBuffer buffer = pack(cmdType, cmd, param, bNeedAck);
		return mSerial.post(buffer);
	}

	private synchronized int[] sendData(int cmdType, int cmd, int subcmd, int[] data, boolean bNeedAck, int timeout)
	{
		int[] byReturn = null;
		try
		{
			if (!mSerialConnect)
			{
				Log.w(TAG, "SendData failed,Serial is close");
				return null;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (mSerial == null)
		{
			Log.e(TAG, "MCU not connected, can't send request to it");
			return byReturn;
		}
		if (data != null && data.length > 253)
		{
			Log.e(TAG, "Param too long: " + data.length);
			return byReturn;
		}

		// 只需要封装Data区域即可
		ByteBuffer buffer = pack(cmdType, cmd, data, bNeedAck);

		// send data, repeat 3 times if failed
		for (int i = 0; i < ServiceHelper.REPEAT_NORMAL; ++i)
		{
			mbWaitReturn = true;
			if (mSerial.send(buffer))
			{
				break;
			}
			else
			{
				mbWaitReturn = false;
			}
		}

		if (timeout > 0)
		{
			byReturn = waitData(cmd, subcmd, timeout);
			if (byReturn == null)
			{
				Log.e(TAG, "writeData wait return timeout! " + "cmd=0x" + Integer.toHexString(cmd) + ", subcmd=0x" + Integer.toHexString(subcmd) + ", timeout=" + timeout);
			}
		}
		return byReturn;
	}
	
	//针对发送cmd与期待接收的cmd不同的情况(如Ready)
	private synchronized int[] sendData(int cmdType, int cmd, int[] data, int retCmd,int retSubCmd,boolean bNeedAck, int timeout)
	{
		int[] byReturn = null;
		try
		{
			if (!mSerialConnect)
			{
				Log.w(TAG, "SendData failed,Serial is close");
				return null;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (mSerial == null)
		{
			Log.e(TAG, "MCU not connected, can't send request to it");
			return byReturn;
		}
		if (data != null && data.length > 253)
		{
			Log.e(TAG, "Param too long: " + data.length);
			return byReturn;
		}

		// 只需要封装Data区域即可
		ByteBuffer buffer = pack(cmdType, cmd, data, bNeedAck);

		// send data, repeat 3 times if failed
		for (int i = 0; i < ServiceHelper.REPEAT_NORMAL; ++i)
		{
			mbWaitReturn = true;
			if (mSerial.send(buffer))
			{
				break;
			}
			else
			{
				mbWaitReturn = false;
			}
		}

		if (timeout > 0)
		{
			byReturn = waitData(retCmd, retSubCmd, timeout);
			if (byReturn == null)
			{
				Log.e(TAG, "writeData wait return timeout! " + "retCmd=0x" + Integer.toHexString(retCmd) + ", retSubCmd=0x" + Integer.toHexString(retSubCmd) + ", timeout=" + timeout);
			}
		}
		return byReturn;
	}

	private synchronized int[] waitData(int cmd, int subcmd, int timeout)
	{
		if (mSerial == null || !mSerialConnect)
		{
			Log.e(TAG, "MCU not connected, can't wait request to it");
			mbWaitReturn = false;
			return null;
		}
		if (cmd < 0)
		{
			mbWaitReturn = false;
			return null;
		}

		int[] byReturn = null;
		try
		{
			int waitTime = timeout;
			while (!Thread.currentThread().isInterrupted() && (waitTime >= 0 || timeout == ServiceHelper.WAIT_INFINITE))
			{
				long tTick = SystemClock.uptimeMillis();
				int[] retArray = mlsReturn.poll(waitTime, TimeUnit.MILLISECONDS);
				if (retArray != null && retArray.length > 2 && retArray[1] == cmd && (retArray[2] == subcmd || subcmd == -1))
				{
					byReturn = new int[retArray.length - 2];
					System.arraycopy(retArray, 2, byReturn, 0, byReturn.length);
					break;
				}
				else
				{
					byReturn = null;
					Log.w(TAG, "not expected result,expected cmd:" + cmd + " and subcmd:" + subcmd);
				}
				waitTime -= (SystemClock.uptimeMillis() - tTick);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		mbWaitReturn = false;
		mlsReturn.clear();

		return byReturn;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	private int mPackageSequenceNo = 0;

	public ByteBuffer pack(int cmdType, int cmd, int[] data, boolean bNeedAck)
	{
		int dataLen = (data == null) ? 0 : data.length;
		byte[] buffer = new byte[dataLen + 3];

		int bufferPos = 0;
		// 第一位标记为是否需要Ack
		buffer[bufferPos++] = ServiceHelper.getByteFromInt(bNeedAck ? 1 : 0);
		buffer[bufferPos++] = ServiceHelper.getByteFromInt(cmdType);
		buffer[bufferPos++] = ServiceHelper.getByteFromInt(cmd);
		for (int i = 0; i < dataLen; i++)
		{
			buffer[bufferPos++] = ServiceHelper.getByteFromInt(data[i]);
		}
		ByteBuffer retbuffer = ByteBuffer.wrap(buffer);
		retbuffer.position(bufferPos);
		return retbuffer;
	}

	// 处理MCU Version
	private void onHandleMcuVersion(int[] param)
	{
		if (param.length < 16) 
		{ 
			//非有效数据长度的Boot模式数据
			if(param.length >= 5)
			{
				String head = ServiceHelper.toString(param, 0, 5, "UTF-8");
				if (head.equals("IBOOT"))
				{
					if(mService != null)
					{
						mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, "BootMode",true);
					}
					Log.i(TAG, "receive mcu version in boot mode");
				}
			}
			Log.e(TAG, "unvalid mcu version data");
			return;
		}
		String head = ServiceHelper.toString(param, 0, 5, "UTF-8");
		// Boot模式解析
		if (head.equals("IBOOT"))
		{
			mMcuState = VehiclePropertyConstants.MCU_STATE_BOOT;
			mMcuType = ServiceHelper.MAKELONG(param[6], param[5]);
			mBootCheckSum = Arrays.copyOfRange(param, 8, 15);
			if(mService != null)
			{
				mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, "BootMode",true);
			}
			Log.i(TAG, "receive mcu version in boot mode");
		}
		// Normal模式解析
		else
		{
			mMcuVersion = ServiceHelper.toString(param, 0, 16, "UTF-8");
			if(mService != null)
			{
				mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_VERSION_PROPERTY, mMcuVersion,true);
			}
			
			Log.i(TAG, "receive mcu version in normal mode");
		}
	}

	private void onHandleMcuInfo(int[] param)
	{
		if (param.length < 2) { return; }
		int infoId = param[0];
		switch (infoId)
		{
			// MCU类型
			case 0:
				mMcuType = param[1];
				break;
			// MCU description
			case 1:
				mMcuDescription = ServiceHelper.toString(param, 2, param.length - 2);
				break;
			// MCU CheckSum
			case 2:
				mAppCheckSum = Arrays.copyOfRange(param, 1, 8);
				break;
			// Boot CheckSum
			case 3:
				mBootCheckSum = Arrays.copyOfRange(param, 1, 8);
				break;
		}
	}

	private void onHandleSysStatus(int[] param)
	{
		if (param.length < 2) { return; }
		int sysId = param[0];
		switch (sysId)
		{
			// 休眠原因
			case 0:
				mMcuSleepReason = param[1];
				Log.i(TAG, "receive mcu sleep reason : "+mMcuSleepReason);
				if(mService != null)
				{
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_LAST_SLEEP_REASON_PROPERTY, String.valueOf(mMcuSleepReason),true);
				}
				break;
			case 1:
				Log.i(TAG, "receive wakeup mode : "+param[1]);
				if(mService != null)
				{
					//避免出现0上传给上层误以为是IBOOT
					int nMcuState = (param[1] < 1 ? 1: param[1]);
					Intent wakeupByGensorIntent = new Intent(ACTION_MCT_WAKEUP_MODE);
					mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_STATUS_PROPERTY, String.valueOf(nMcuState),true);
					if(mMcuState != nMcuState)
					{
						mMcuState = nMcuState;
						if(mMcuState == VehiclePropertyConstants.MCU_STATE_WAKEUP_GSENSOR)
						{
							//发送Gensor唤醒广播
							Log.i(TAG, "send gensor wake up brocast message,time:"+System.currentTimeMillis());
							wakeupByGensorIntent.putExtra(KEY_WAKEUP_MODE, mMcuState);
						}
						else if(mMcuState == VehiclePropertyConstants.MCU_STATE_WAKEUP_ACCON ||
								mMcuState == VehiclePropertyConstants.MCU_STATE_WAKEUP_MODEM)
						{
							wakeupByGensorIntent.putExtra(KEY_WAKEUP_MODE, mMcuState);
							//开屏
							Log.i(TAG, "Open Screen BackLight");
							ServiceHelper.enableMirrorScreen(true);
						}
						else 
						{
							wakeupByGensorIntent.putExtra(KEY_WAKEUP_MODE, 0);
							Log.e(TAG, "unhandle mcu state:"+mMcuState);
						}
						Log.i(TAG, "send com.mct.wakeup.mode broadcast message"+",value:"+wakeupByGensorIntent.getIntExtra(KEY_WAKEUP_MODE, -1));
						mService.sendBroadcastAsUser(wakeupByGensorIntent, UserHandle.CURRENT);
					}
				}
				break;
			default:
				break;
		}
	}
	
	//针对低版本的mcu,不支持wakeupmode协议以及首次拿不到wakemode状态值的版本处理
	private void handleExceptionInWakeup()
	{
		//开屏
		Log.i(TAG, "handleExceptionInWakeup,and open screen");
		ServiceHelper.enableMirrorScreen(true);
		//发送唤醒广播
		Intent wakeupByGensorIntent = new Intent(ACTION_MCT_WAKEUP_MODE);
		wakeupByGensorIntent.putExtra(KEY_WAKEUP_MODE, 0);
		mService.sendBroadcastAsUser(wakeupByGensorIntent, UserHandle.CURRENT);
	}

	private void onHandleMcuVoltage(int[] param)
	{
		if (param.length < 1) { return; }
		mMcuVoltage = param[0];
		Log.i(TAG, "receive mcu voltage : "+mMcuVoltage);
		if(mService != null)
		{
			mService.cachePropValue(VehicleInterfaceProperties.VIM_MCU_VOLTAGE_PROPERTY, String.valueOf(mMcuVoltage),true);
		}
		
	}

	private void onMcuUpgradeStateChange(int state, String failedReason)
	{
		if (mMcuUpgradeState != state)
		{
			mMcuUpgradeState = state;
			if(mService != null)
			{
				mService.dispatchData(VehicleInterfaceProperties.VIM_MCU_UPGRADE_STATE_PROPERTY, String.valueOf(mMcuUpgradeState));
			}
			
			if (state == VehiclePropertyConstants.MCU_UPGRADE_STATE_FAILED)
			{
				Log.e(TAG, "catch the mcu upgrade failed:" + failedReason);
				// reqMCUReset();
				mMcuUpgradeState = VehiclePropertyConstants.MCU_UPGRADE_STATE_UNKNOWN;
			}
		}
	}

	private void onReceiveData(int type, int cmd, int[] param)
	{
		switch (cmd)
		{
			// MCU或者Boot版本以及MCU所处的模式
			case MirrorMcuProtocol.CMD_VERSION:
				onHandleMcuVersion(param);
				break;
			case MirrorMcuProtocol.CMD_MCU_INFO:
				onHandleMcuInfo(param);
				break;
			case MirrorMcuProtocol.CMD_SYS_STATUS:
				onHandleSysStatus(param);
				break;
			case MirrorMcuProtocol.CMD_VOLTAGE:
				onHandleMcuVoltage(param);
				break;
			// MCU升级状态
			case MirrorMcuProtocol.CMD_IAP:
				break;
			default:
				break;
		}
	}
	
	
	public int mUpdateImgCheckSum = 0;
	public int mUpdateImgDataSize = 0;
	private class McuUpgradeData
	{
		int frame_entry = 0;
		int frame_size = 0;
		int frame_check_sum = 0;
		byte[] data = null;
		//提取一帧数据
		public boolean initFrameData(byte[] array,int pos)
		{
			if(!checkHdr(array, pos))
			{
				Log.e(TAG, "check Hdr failed!");
				return false;
			}
			if(pos == 0)
			{
				mUpdateImgDataSize = 0;
				data = null;
				return true;
			}
			if(frame_size > 0)
			{
				mUpdateImgDataSize += frame_size;
				data = new byte[frame_size];
				data = Arrays.copyOfRange(array, pos+HDR_SIZE,pos+HDR_SIZE+frame_size);
				return true;
			}
			return false;
		}
		
		public boolean checkHdr(byte[] array,int pos)
		{
			// TODO Auto-generated constructor stub
			if(array.length < HDR_SIZE)
			{
				return false;
			}
			int offset = pos;
			byte[] hdr_version_bytes = new byte[4];
			byte[] hdr_mark_bytes = new byte[4];
			byte[] img_Size_bytes = new byte[4];
			byte[] product_name_bytes = new byte[32];
			byte[] img_type_bytes = new byte[4];
			byte[] img_entry_bytes = new byte[4];
			byte[] img_checksum_bytes = new byte[4];
			byte[] hdr_type_bytes = new byte[4];
			byte[] hdr_checksum_bytes = new byte[4];
			
			hdr_version_bytes = Arrays.copyOfRange(array, offset, offset+4);
			offset+=4;
			hdr_mark_bytes = Arrays.copyOfRange(array, offset, offset+4);
			offset+=4;
			img_Size_bytes = Arrays.copyOfRange(array, offset, offset+4);
			offset+=4;
			product_name_bytes = Arrays.copyOfRange(array, offset, offset+32);
			offset+=32;
			img_type_bytes = Arrays.copyOfRange(array, offset, offset+4);
			offset+=4;
			img_entry_bytes = Arrays.copyOfRange(array, offset, offset+4);
			offset+=4+64;
			img_checksum_bytes = Arrays.copyOfRange(array, offset, offset+4);
			offset+=4;
			hdr_type_bytes = Arrays.copyOfRange(array, offset,offset+4);
			offset+=4;
			hdr_checksum_bytes = Arrays.copyOfRange(array, offset, offset+4);
			offset+=4;
			
			int hdr_version = ServiceHelper.getIntFromBytes(hdr_version_bytes);
			int hdr_mark = ServiceHelper.getIntFromBytes(hdr_mark_bytes);
			frame_size = ServiceHelper.getIntFromBytes(img_Size_bytes);
			String product_name = ServiceHelper.toString(product_name_bytes, 0, product_name_bytes.length, "UTF-8");
			int img_type = ServiceHelper.getIntFromBytes(img_type_bytes);
			frame_entry = ServiceHelper.getIntFromBytes(img_entry_bytes);
			int img_check_sum = ServiceHelper.getIntFromBytes(img_checksum_bytes);
			int hdr_type = ServiceHelper.getIntFromBytes(hdr_type_bytes);
			frame_check_sum = ServiceHelper.getIntFromBytes(hdr_checksum_bytes);
			
			// check version
		    if (hdr_version < HDR_VERSION)
		    {
		    	Log.e(TAG, "mce version"+hdr_version+" less than required version "+HDR_VERSION);
		    	return false;
		    }

		    // check header
		    if (hdr_mark != HDR_MARK)
		    {
		    	Log.e(TAG, "mark not match");
		        return false;
		    }

		    // hdr check sum
		    int head_check_sum = 0;
		    for (int index = 0; index < (HDR_SIZE - 4); index++)
		    {
		    	head_check_sum += (array[pos+index] & 0xFF);
		    }

		    if (frame_check_sum != head_check_sum)
		    {
		        Log.e(TAG,"hdr checksum not match, orignal "+frame_check_sum+", actual "+head_check_sum);
		        return false;
		    }
		    
		    if(pos == 0)
		    {
		    	mUpdateImgCheckSum = img_check_sum;
		    	//mUpdateImgDataSize = frame_size;
		    	 if (hdr_type != HDR_TYPE_PACKAGE)
		    	 {
		    		 Log.e(TAG,"hdr type is not package\n");
		    	      return false;
		    	 }
		    }
		    else
		    {
		    	if(hdr_type != HDR_TYPE_IMAGE)
		    	{
		    		Log.e(TAG, "hdr type is not image");
		    		return false;
		    	}
		    	
		    	//计算合并Data后的checksum，用于帧数据解析完成后的汇总对比
		    	int checkSum = frame_check_sum ;
		    	checkSum += frame_check_sum >> 24 & 0xFF;
		    	checkSum += frame_check_sum >> 16 & 0xFF;
				checkSum += frame_check_sum >> 8 & 0xFF;
				checkSum += frame_check_sum & 0xFF;
		        for (int i = 0; i < frame_size; i++)
		        {
		           checkSum += (array[offset] & 0xFF);
		           offset ++;
		        }
		    	frame_check_sum = checkSum;
		    }
			return true;
		}
		
	}

}
