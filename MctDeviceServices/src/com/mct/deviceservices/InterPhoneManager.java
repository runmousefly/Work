package com.mct.deviceservices;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mct.DeviceInterfaceDataHandler;
import com.mct.DeviceInterfaceProperties;
import com.mct.DevicePropertyConstants;
import com.mct.MctDeviceManager;
import com.mct.deviceservices.MctDeviceDataManager;
import com.mct.serial.interphone.*;
import com.mct.utils.ServiceHelper;
import com.mct.utils.ThreadManager;

import android.annotation.Nullable;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.os.BatteryStats.Uid.Pkg.Serv;
import android.print.PrintAttributes.Resolution;
import android.text.method.WordIterator;
import android.util.Log;

public class InterPhoneManager extends MctDeviceDataManager implements InterPhoneSerial.Listener
{
	public static String TAG = "InterPhoneManager";
	public static final String INTERPHONE_SERIAL_PORT 				= "/dev/ttysWK0";//转换IC模式下
	public static final String INTERPHONE_ONLY_SERIAL_PORT 	= "/dev/ttyHS0";//硬切模式下串口号
	
	private DeviceService mService = null;
	private InterPhoneSerial mSerial = null;
	private boolean mbWaitReturn = false;

	private LinkedBlockingQueue<int[]> mlsReturn = new LinkedBlockingQueue<int[]>(); // 阻塞型队列，用于等待数据
	private boolean mSerialConnect = false;
	
	@Override
	public boolean onInitManager(DeviceService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitMcuManager");
		mService = service;
		dispatchServiceState(DevicePropertyConstants.INTERPHONE_SERVICE_STATE_TURNING_ON);
		String serialPortName = INTERPHONE_ONLY_SERIAL_PORT;
		if(mSerial == null)
		{
			mSerial = new InterPhoneSerial();
		}
		mSerial.init(ServiceHelper.DEFAULT_PACKAGE_LEN, ServiceHelper.WAIT_ELAPSE, ServiceHelper.WAIT_ELAPSE, 1);
		mSerial.requestListener(this);
		mSerialConnect = false;
		Log.i(TAG, "begin to open serial");		
		// 初始化串口数据收发配置
		for (int i = 0; i < ServiceHelper.REPEAT_MAXIMUM; ++i)
		{
			try
			{
				if (mSerial.open(serialPortName) != 0)
				{
					Log.i(TAG, "open serial success,and retry count:" + i);
					mSerialConnect = true;
					break;
				}
				else
				{
					mSerial.close();
				}
				Thread.sleep(ServiceHelper.WAIT_ELAPSE);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		Log.i(TAG, "finish to open serial");
		dispatchServiceState(mSerialConnect?DevicePropertyConstants.INTERPHONE_SERVICE_STATE_ON:DevicePropertyConstants.INTERPHONE_SERVICE_STATE_OFF);
		return mSerialConnect;
	}

	@Override
	public boolean onDeinitManager()
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onDeinitManager");
		dispatchServiceState(DevicePropertyConstants.INTERPHONE_SERVICE_STATE_TURNING_OFF);
		if (mSerial != null && mSerialConnect)
		{
			if(mSerial.close() > 0)
			{
				mSerialConnect = false;
			}
			//mSerial = null;
		}
		Log.i(TAG, "onDeinitManager end");
		dispatchServiceState(DevicePropertyConstants.INTERPHONE_SERVICE_STATE_OFF);
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return InterPhoneProtocol.INTERPHONE_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < InterPhoneProtocol.INTERPHONE_PROPERITIES.length; i++)
		{
			if (InterPhoneProtocol.getProperityPermission(InterPhoneProtocol.INTERPHONE_PROPERITIES[i]) >= InterPhoneProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(InterPhoneProtocol.INTERPHONE_PROPERITIES[i]);
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
		return InterPhoneProtocol.getProperityDataType(propId);
	}

	@Override
	public boolean setPropValue(int propId, String value)
	{
		// TODO Auto-generated method stub
		if(value == null || value.length() == 0)
		{
			Log.e(TAG, "value is invalid!");
			return false;
		}
		try
		{
			switch(propId)
			{
				case DeviceInterfaceProperties.DIM_INTERPHONE_CHANNEL_PROPERTY:
					int channel = Integer.valueOf(value);
					if(channel >= 1 && channel <= 16)
					{
						return postData(InterPhoneProtocol.CMD_CHANNEL_SWITCH, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{channel});
					}
					break;
				case DeviceInterfaceProperties.DIM_INTERPHONE_SCAN_FREQ_STATUS_PROPERTY:
					int scanSwitch = Integer.valueOf(value) == DevicePropertyConstants.STATUS_OFF?0xFF:0x01;
					return postData(InterPhoneProtocol.CMD_SCAN_FREQ_SWITCH, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{scanSwitch});
				case DeviceInterfaceProperties.DIM_INTERPHONE_RECEIVE_VOLUME_PROPERTY:
					int volume = Integer.valueOf(value);
					if(volume >= 1 && volume <= 9)
					{
						return postData(InterPhoneProtocol.CMD_SET_VOLUME, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{volume});
					}
					break;
				case DeviceInterfaceProperties.DIM_INTERPHONE_CALLOUT_PROPERTY:
					int[] callInfo = ServiceHelper.stringToIntArray(value);
					if(callInfo.length != 3)
					{
						Log.e(TAG, "setPropValue Exception,propId:"+propId+",value:"+value);
						return false;
					}
					callInfo[0] = (callInfo[0] == DevicePropertyConstants.STATUS_OFF ? 0xFF : 0x01);
					int callNumber0 = callInfo[2] & 0xFF;
					int callNumber1 = (callInfo[2] >> 8) & 0xFF;
					int callNumber2 = (callInfo[2] >> 16) & 0xFF;
					return postData(InterPhoneProtocol.CMD_CALL_STATUS, InterPhoneProtocol.DATA_WRITE, callInfo[0], new int[]{callInfo[1],callNumber2,callNumber1,callNumber0});
				case DeviceInterfaceProperties.DIM_INTERPHONE_ALARM_STATUS_PROPERTY:
					int alarmSwitch = Integer.valueOf(value) == DevicePropertyConstants.STATUS_OFF?0xFF:0x01;
					return postData(InterPhoneProtocol.CMD_ALARM_STATUS, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{alarmSwitch});
				case DeviceInterfaceProperties.DIM_INTERPHONE_MIC_GAIN_PROPERTY:
					int micGain = Integer.valueOf(value);
					micGain = Math.min(0x0F, Math.max(0x00, micGain));
					return postData(InterPhoneProtocol.CMD_SET_MIC, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{micGain});
				case DeviceInterfaceProperties.DIM_INTERPHONE_POWER_MODE_PROPERTY:
					int powerSaveSwitch = Integer.valueOf(value) == DevicePropertyConstants.STATUS_OFF?0xFF:0x01;
					return postData(InterPhoneProtocol.CMD_SET_POWER, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{powerSaveSwitch});
				case DeviceInterfaceProperties.DIM_INTERPHONE_SENDING_POWER_PROPERTY:
					int sendPower = Integer.valueOf(value) == DevicePropertyConstants.STATUS_OFF?0xFF:0x01;
					return postData(InterPhoneProtocol.CMD_SEND_POWER, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{sendPower});
				case DeviceInterfaceProperties.DIM_INTERPHONE_SQUELCH_LEVEL_PROPERTY:
					int squelchLevel = Integer.valueOf(value);
					return postData(InterPhoneProtocol.CMD_SQUELCH_LEVEL, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{squelchLevel});				
				case DeviceInterfaceProperties.DIM_INTERPHONE_MONITOR_STATUS_PROPERTY:
					int monitorSwitch = Integer.valueOf(value) == DevicePropertyConstants.STATUS_OFF?0xFF:0x01;
					return postData(InterPhoneProtocol.CMD_MONITOR_SWITCH, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{monitorSwitch});
				//设置通讯频率
				case DeviceInterfaceProperties.DIM_INTERPHONE_COMN_FREQ_PROPERTY:
					int[] comnFreq = MctDeviceManager.stringToIntArray(value);
					int rxTxFreq[] = new int[8];
					rxTxFreq[0] = ((comnFreq[0] >> 24) & 0xFF);
					rxTxFreq[1] = ((comnFreq[0] >> 16) & 0xFF);
					rxTxFreq[2] = ((comnFreq[0] >> 8) & 0xFF);
					rxTxFreq[3] = (comnFreq[0] & 0xFF);
					rxTxFreq[4] = ((comnFreq[1] >> 24) & 0xFF);
					rxTxFreq[5] = ((comnFreq[1] >> 16) & 0xFF);
					rxTxFreq[6] = ((comnFreq[1] >> 8) & 0xFF);
					rxTxFreq[7] = (comnFreq[1] & 0xFF);
					return postData(InterPhoneProtocol.CMD_SET_FREQ, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, rxTxFreq);
				case DeviceInterfaceProperties.DIM_INTERPHONE_CALL_CONTACT_PROPERTY:
					int[] callContactInfo = MctDeviceManager.stringToIntArray(value);
					if(callContactInfo != null && callContactInfo.length == 2)
					{
						int []callContactParam = new int[4];
						callContactParam[0] = callContactInfo[0];
						callContactParam[1] = (callContactInfo[1] >> 16) & 0xFF;
						callContactParam[2] = (callContactInfo[1] >> 8) & 0xFF;
						callContactParam[3] = callContactInfo[1] & 0xFF;
						return postData(InterPhoneProtocol.CMD_SET_CONTACTS, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, callContactParam);
					}
					break;
				case DeviceInterfaceProperties.DIM_INTERPHONE_TONE_TYPE_PROPERTY:
					int[] toneTypes = MctDeviceManager.stringToIntArray(value);
					return postData(InterPhoneProtocol.CMD_TONE_TYPE, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, toneTypes);
				case DeviceInterfaceProperties.DIM_INTERPHONE_TONE_FREQ_PROPERTY:
					int[] toneFreqs = MctDeviceManager.stringToIntArray(value);
					return postData(InterPhoneProtocol.CMD_TONE_FREQ, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, toneFreqs);
				
				case DeviceInterfaceProperties.DIM_INTERPHONE_REQ_CMD_PROPERTY:
					int subCmd = Integer.valueOf(value);
					switch (subCmd)
					{
						case DevicePropertyConstants.CMD_QUERY_INIT_STATUS:
							postData(InterPhoneProtocol.CMD_INIT_STATUS, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_QUERY_VERSION:
							postData(InterPhoneProtocol.CMD_QUERY_VERSION, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_QUERY_SIGNAL_STRENGTH:
							postData(InterPhoneProtocol.CMD_QUERY_SIGNAL_STRENGTH, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_QUERY_SCAN_FREQ_STATUS:
							postData(InterPhoneProtocol.CMD_QUERY_SCAN_STATUS, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_QUERY_LOCAL_NUMBER:
							postData(InterPhoneProtocol.CMD_QUERY_DEVICE_NUMBER, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_QUERY_MODULE_STATUS:
							postData(InterPhoneProtocol.CMD_QUERY_MODULE_STATUS, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_QUERY_ENCRYPTION_STATUS:
							postData(InterPhoneProtocol.CMD_QUERY_ENCRYPTION_STATUS, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_QUERY_CONTACTS:
							postData(InterPhoneProtocol.CMD_QUERY_CONTACTS, InterPhoneProtocol.DATA_WRITE, InterPhoneProtocol.DATA_WRITE, new int[]{0x01});
							break;
						case DevicePropertyConstants.CMD_ENABLE_POWER_ON:
							{
								if(mService != null)
								{
									mService.enableInterPhoneModule(true);
								}
							}
							break;
						case DevicePropertyConstants.CMD_ENABLE_POWER_OFF:
							{
								if(mService != null)
								{
									mService.enableInterPhoneModule(false);
								}
							}
						default:
							break;
					}
					break;
				default:
					break;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "setPropValue Exception,propId:"+propId+",value:"+value);
			return false;
		}
		return true;
	}

	@Override
	public String getPropValue(int propId)
	{
		// TODO Auto-generated method stub
		return super.getPropValue(propId);
	}

	// 此时获取的数据时Cmd+RW+SR+Data
	@Override
	public void onLocalReceive(Bundle data)
	{
		// TODO Auto-generated method stub
		if (data == null) 
		{
			Log.e(TAG, "onLocalData is null");
			return; 
		}
		int[] buffer = data.getIntArray(DeviceService.KEY_DATA);
		int[] param = new int[buffer.length - 3];
		System.arraycopy(buffer, 3, param, 0, param.length);
		onReceiveData(buffer[0],buffer[1],buffer[2],param);
	}

	private int getCheckSum(byte[] param)
	{
		if(param == null || param.length == 0)
		{
			return 0;
		}
		int sum=0;
		int len = param.length;
		int index = 0;
		while(len > 1)
		{
			sum += 0xFFFF & (param[index++]<<8 | param[index++]);
			len-=2;
		}
		if (len > 0)
		{
			sum += (0xFF & param[index++])<<8;
		}
		int tmp = sum >> 16;
		while (tmp > 0)
		{
			sum = (sum & 0xFFFF)+(sum >> 16);
			tmp = sum >> 16;
		}
		return (sum ^ 0xFFFF);
	}
	
	
	@Override
	public  void onReceive(ByteBuffer buffer)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "MCU onReceive: " + ServiceHelper.toHexString(buffer)+",position:"+buffer.position());
		if (buffer == null) 
		{ 
			return; 
		}
		buffer.position(0);
		ByteBuffer data = ByteBuffer.allocate(buffer.capacity());
		data.put(buffer);

		while (data.position() >= 9)
		{
			int[] frameBuffer = unPack(data);
			if (frameBuffer == null || frameBuffer.length < 1)
			{
				Log.i(TAG, "data is null or too short,content:"+ServiceHelper.toString(buffer));
				continue;
			}
			
			// 返回get的数据
			//Log.w(TAG, "wait data:"+mbWaitReturn);
			if (mbWaitReturn)
			{
				try
				{
					mlsReturn.put(frameBuffer);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Message msg = mService.getMainHandler().obtainMessage();
			Bundle bundle = new Bundle();
			msg.arg1 = frameBuffer.length;
			msg.what = DeviceService.MSG_RECEIVE_INTERPHONE_DATA;
			bundle.putIntArray(DeviceService.KEY_DATA, frameBuffer);
			msg.setData(bundle);
			mService.getMainHandler().sendMessage(msg);
		}
	}

	@Override
	public void onSend(ByteBuffer data)
	{
		// TODO Auto-generated method stub
		Log.v(TAG, "MCU onSend: " + ServiceHelper.toHexString(data));
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// 封包
	public ByteBuffer pack(int cmd,int rw,int sr, int[] param)
	{
		int paramLen = (param == null) ? 0 : param.length;
		byte[] data = new byte[paramLen + 9];

		data[0] = ServiceHelper.getByteFromInt(InterPhoneProtocol.PROTOCOL_FORMAT_HEAD);
		data[1] = ServiceHelper.getByteFromInt(cmd);
		data[2] = ServiceHelper.getByteFromInt(rw);
		data[3] = ServiceHelper.getByteFromInt(sr);
		//checkSum
		data[4] = 0;
		data[5] = 0;
		//Len
		data[6] = ServiceHelper.getByteFromInt((paramLen>>8) & 0xFF);
		data[7] = ServiceHelper.getByteFromInt((paramLen & 0xFF));
		
		int i = 0;
		if (param != null)
		{
			for (i = 0; i < paramLen; ++i)
			{
				data[8 + i] = ServiceHelper.getByteFromInt((param[i] & 0xFF));
			}
		}
		data[8 + i] = ServiceHelper.getByteFromInt(InterPhoneProtocol.PROTOCOL_FORMAT_TAIL);

		int checksum = getCheckSum(data);
		data[4] = 0x00;//ServiceHelper.getByteFromInt((checksum>>8) & 0xFF);
		data[5] = 0x00;//ServiceHelper.getByteFromInt((checksum & 0xFF));
		
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.position(data.length);
		return buffer;
	}

	// 解包
	public int[] unPack(ByteBuffer buffer)
	{
		int[] byReturn = null;
		int pos = 0;
		int length = buffer.position();

		// sync
		while (length >= 1)
		{
			if (ServiceHelper.getIntFromByte(buffer.get(pos)) == InterPhoneProtocol.PROTOCOL_FORMAT_HEAD)
			{
				pos ++;
				length --;
				break;
			}
			++pos;
			--length;
		}

		if (length >= 6)
		{
			// len
			int cmd = ServiceHelper.getIntFromByte(buffer.get(pos++));
			int rw = ServiceHelper.getIntFromByte(buffer.get(pos++));
			int sr = ServiceHelper.getIntFromByte(buffer.get(pos++));
			
			int checksum = ServiceHelper.MAKEWORD(ServiceHelper.getIntFromByte(buffer.get(pos+1)), ServiceHelper.getIntFromByte(buffer.get(pos)));
			pos += 2;
			int len = ServiceHelper.MAKEWORD(ServiceHelper.getIntFromByte(buffer.get(pos+1)), ServiceHelper.getIntFromByte(buffer.get(pos)));
			pos += 2;
			length -= 7;
			if (len <= (length-1))
			{
				byReturn = new int[len+3];
				byReturn[0] = cmd;
				byReturn[1] = rw;
				byReturn[2] = sr;
				for (int i = 0; i < len; ++i)
				{
					byReturn[3+i] = ServiceHelper.getIntFromByte(buffer.get(pos++));
				}
				int tail = ServiceHelper.getIntFromByte(buffer.get(pos));
				length -= (len+1);
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
		if(pos > 0)
		{
			if(length == 0)
			{
				buffer.clear();
			}
			else
			{
				byte[] tmp = new byte[length];
				buffer.position(pos);
				buffer.get(tmp, 0, length);
				buffer.clear();
				buffer.put(tmp);
			}
		}
		return byReturn;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean postData(int cmd,int rw,int sr, int[] param)
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

		ByteBuffer buffer = pack(cmd,rw,sr,param);
		return mSerial.post(buffer);
	}

	private synchronized int[] sendData(int cmd, int rw,int sr,int[] param, int expectedCmd,int[] expectedData, int timeout)
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

		// 只需要封装Data区域即可
		ByteBuffer buffer = pack(cmd,rw,sr,param);

		// send data, repeat 3 times if failed
		for (int i = 0; i < ServiceHelper.REPEAT_NORMAL; ++i)
		{
			if(timeout > 0)
			{
				mbWaitReturn = true;
			}
			if (mSerial.send(buffer))
			{
				break;
			}
//			else
//			{
//				mbWaitReturn = false;
//			}
		}

		if (timeout > 0)
		{
			byReturn = waitData(expectedCmd, expectedData, timeout);
			if (byReturn == null)
			{
				Log.e(TAG, "writeData wait return timeout! " + "cmd=0x" + Integer.toHexString(expectedCmd) + ", subcmd=0x" +ServiceHelper.toString(expectedData) + ", timeout=" + timeout);
			}
		}
		return byReturn;
	}

	private synchronized int[] waitData(int cmd, int[] data, int timeout)
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
				if (retArray != null && retArray.length >= (1+data.length) && retArray[0] == cmd && 
						(data == null || ServiceHelper.compareIntArray(Arrays.copyOfRange(retArray, 1,1+data.length), data)))
				{
					byReturn = new int[retArray.length - 1];
					System.arraycopy(retArray, 1, byReturn, 0, byReturn.length);
					break;
				}
				else
				{
					byReturn = null;
					Log.w(TAG, "not expected result,expected cmd:" + cmd + " and data:" + ServiceHelper.toString(data));
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

	private void onReceiveData(int cmd,int rw,int sr, int[] param)
	{
		switch (cmd)
		{
			case InterPhoneProtocol.CMD_CHANNEL_SWITCH:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_CHANNEL_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_SET_VOLUME:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_RECEIVE_VOLUME_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_INIT_STATUS:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_INIT_STATUS_PROPERTY, String.valueOf(sr), true);
				break;
			case InterPhoneProtocol.CMD_QUERY_VERSION:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_VERSION_PROPERTY,sr}), true);
				if(param != null && param.length > 0)
				{
					Log.i(TAG, "receive version info");
					mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_VERSION_PROPERTY, ServiceHelper.toString(param, 0, param.length, "UTF-8"), true);
				}
				break;
			case InterPhoneProtocol.CMD_QUERY_DEVICE_NUMBER:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_DEVICE_NUMBER_PROPERTY,sr}), true);
				if(param != null && param.length >= 3)
				{
					int deviceNumber = ((param[0] >> 16) | (param[1] >> 8) | param[2]);
					Log.i(TAG, "receive device number:"+deviceNumber);
					mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_DEVICE_NUMBER_PROPERTY,String.valueOf(deviceNumber) , true);
				}
				break;
			case InterPhoneProtocol.CMD_QUERY_SIGNAL_STRENGTH:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_SIGNAL_STRENGTH_PROPERTY,sr}), true);
				if(param != null && param.length >= 1)
				{
					Log.i(TAG, "receive signal strength:"+param[0]);
					mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_SIGNAL_STRENGTH_PROPERTY,String.valueOf(param[0]) , true);
				}
				break;
			case InterPhoneProtocol.CMD_QUERY_CONTACTS:
				break;
			case InterPhoneProtocol.CMD_CALL_STATUS:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_CALL_STATUS_PROPERTY, String.valueOf(sr), true);
				break;
			case InterPhoneProtocol.CMD_SQUELCH_LEVEL:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_SQUELCH_LEVEL_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_SET_FREQ:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_COMN_FREQ_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_QUERY_MODULE_STATUS:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_DEVICE_STATUS_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_TONE_TYPE:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_TONE_TYPE_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_TONE_FREQ:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_TONE_FREQ_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_SET_MIC:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_MIC_GAIN_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_SEND_POWER:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_SENDING_POWER_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_SET_POWER:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_POWER_MODE_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_SET_CONTACTS:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_CALL_CONTACT_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_SCAN_FREQ_SWITCH:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_SCAN_FREQ_STATUS_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_ALARM_STATUS:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_ALARM_STATUS_PROPERTY,sr}), true);
				break;
			case InterPhoneProtocol.CMD_MONITOR_SWITCH:
				mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_RET_ERROR_CODE_PROPERTY, MctDeviceManager.intArrayToString(new int[]{DeviceInterfaceProperties.DIM_INTERPHONE_MONITOR_STATUS_PROPERTY,sr}), true);
				break;
			default:
				break;
		}
	}
	
	public void dispatchServiceState(int state)
	{
		Log.i(TAG,"dispatchServiceState:"+state);
		if(mService != null)
		{
			mServiceState = state;
			mService.cachePropValue(DeviceInterfaceProperties.DIM_INTERPHONE_SERVICE_STATUS_PROPERTY, String.valueOf(state), false);
		}
	}
}
