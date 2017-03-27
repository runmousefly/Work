package com.mct.deviceservices;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mct.DeviceInterfaceProperties;
import com.mct.DevicePropertyConstants;
import com.mct.deviceservices.MctDeviceDataManager;
import com.mct.serial.ranging.RangingSerial;
import com.mct.utils.ServiceHelper;
import com.mct.utils.ThreadManager;

import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;

public class RangingManager extends MctDeviceDataManager implements RangingSerial.Listener
{
	public static String TAG = "RangingManager";
	
	public static final String RANGING_SERIAL_PORT 			= "/dev/ttysWK1";//转换IC模式下
	public static final String RANGING_ONLY_SERIAL_PORT 	= "/dev/ttyHS0";//硬切模式下串口号
	
	private DeviceService mService = null;
	private RangingSerial mSerial = null;
	private boolean mbWaitReturn = false;
	private LinkedBlockingQueue<int[]> mlsReturn = new LinkedBlockingQueue<int[]>(); // 阻塞型队列，用于等待数据
	private boolean mSerialConnect = false;
	
	@Override
	public boolean onInitManager(DeviceService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitMcuManager");
		mService = service;
		dispatchServiceState(DevicePropertyConstants.LASER_SERVICE_STATE_TURNING_ON);
		String serialPortName = RANGING_ONLY_SERIAL_PORT;
		if(mSerial == null)
		{
			mSerial = new RangingSerial();
		}
		mSerial.init(ServiceHelper.DEFAULT_PACKAGE_LEN, ServiceHelper.WAIT_SHORT, ServiceHelper.WAIT_SHORT, 1);
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
		dispatchServiceState(mSerialConnect?DevicePropertyConstants.LASER_SERVICE_STATE_ON:DevicePropertyConstants.LASER_SERVICE_STATE_OFF);
		return true;
	}

	@Override
	public boolean onDeinitManager()
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onDeinitManager");
		dispatchServiceState(DevicePropertyConstants.LASER_SERVICE_STATE_TURNING_OFF);
		if (mSerial != null && mSerialConnect)
		{
			if(mSerial.close() > 0)
			{
				mSerialConnect = false;
			}
			//mSerial = null;
		}
		Log.i(TAG, "onDeinitManager end");
		dispatchServiceState(DevicePropertyConstants.LASER_SERVICE_STATE_OFF);
		return true;
	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return RangingProtocol.RANGING_DEVICE_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < RangingProtocol.RANGING_DEVICE_PROPERITIES.length; i++)
		{
			if (RangingProtocol.getProperityPermission(RangingProtocol.RANGING_DEVICE_PROPERITIES[i]) >= RangingProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(RangingProtocol.RANGING_DEVICE_PROPERITIES[i]);
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
		return RangingProtocol.getProperityDataType(propId);
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
				case DeviceInterfaceProperties.DIM_RANGING_REQ_CMD_PROPERTY:
					int subCmd = Integer.valueOf(value);
					if(subCmd == DevicePropertyConstants.CMD_OPEN_LASER && mService != null)
					{
						return postData(RangingProtocol.CMD_OPEN_LASER, null);
					}
					else if(subCmd == DevicePropertyConstants.CMD_CLOSE_LASER && mService != null)
					{
						return postData(RangingProtocol.CMD_CLOSE_LASER, null);
					}
					else if(subCmd == DevicePropertyConstants.CMD_RANGING)
					{
						return postData(RangingProtocol.CMD_RANGING_DISTANCE, null);
					}
					else if(subCmd == DevicePropertyConstants.CMD_REQ_MODULE_INFO)
					{
						return postData(RangingProtocol.CMD_QUERY_MODULE_INFO, null);
					}
					else if(subCmd == DevicePropertyConstants.CMD_ENABLE_LASER_POWER && mService != null)
					{
						mService.enableRangingModule(true);
					}
					else if(subCmd == DevicePropertyConstants.CMD_DISABLE_LASER_POWER && mService != null)
					{
						mService.enableRangingModule(false);
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
		//ServiceHelper.toString(data.array(), 0, data.position(), "UTF-8");
		String strCmd = data.getString(DeviceService.KEY_DATA);
		onReceiveData(strCmd);
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
		String tempBuf = ServiceHelper.toString(buffer);
		String []data = tempBuf.split("\r\n");
		if (data == null || data.length == 0)
		{
			Log.e(TAG, "onReceive data is empty");
			return;
		}
		
		for(int i= 0;i<data.length;i++)
		{
			Message msg = mService.getMainHandler().obtainMessage();
			Bundle bundle = new Bundle();
			msg.arg1 = buffer.position();
			msg.what = DeviceService.MSG_RECEIVE_RANGING_DATA;
			bundle.putString(DeviceService.KEY_DATA, data[i]);
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
	public ByteBuffer pack(int cmd, int[] param)
	{
		int paramLen = (param == null) ? 0 : param.length;
		byte[] data = new byte[paramLen + 1];
		data[0] = ServiceHelper.getByteFromInt(cmd & 0xFF);
		int i = 0;
		if (param != null)
		{
			for (i = 0; i < paramLen; ++i)
			{
				data[1 + i] = ServiceHelper.getByteFromInt((param[i] & 0xFF));
			}
		}
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
		while (length >= 2)
		{
			if (ServiceHelper.getIntFromByte(buffer.get(pos + 0)) == 0xAA && ServiceHelper.getIntFromByte(buffer.get(pos + 1)) == 0x55)
			{
				pos += 2;
				length -= 2;
				break;
			}
			++pos;
			--length;
		}

		if (length >= 4)
		{
			// len
			int len_h = ServiceHelper.getIntFromByte(buffer.get(pos));
			int len_l = ServiceHelper.getIntFromByte(buffer.get(pos+1));
			pos += 2;
			length -= 2;
			int len = ServiceHelper.MAKEWORD(len_l, len_h);
			if (len <= length)
			{
				// checksum
				int sum = (len_h + len_l) & 0xFF;
				byReturn = new int[len - 1];
				for (int i = 0; i < (len - 1); ++i)
				{
					int data = ServiceHelper.getIntFromByte(buffer.get(pos+i));
					sum += data;
					sum = (sum & 0xFF);
					byReturn[i] = data;
				}
				pos += (len-1);
				length -= (len-1);
				sum = ((sum^0xFF)+1) & 0xFF;
				if (sum != ServiceHelper.getIntFromByte(buffer.get(pos)))
				{
					Log.e(TAG, "MCU onRecieve checksum error!");
					byReturn = null;
				}
				pos ++;
				length --;
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
	
	private boolean postData(int cmd, int[] param)
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

		ByteBuffer buffer = pack(cmd,param);
		return mSerial.post(buffer);
	}

	private synchronized int[] sendData(int cmd, int[] param, int expectedCmd,int[] expectedData, int timeout)
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
		ByteBuffer buffer = pack(cmd,param);

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


	private void onReceiveData(String strCmd)
	{
		if(strCmd.startsWith("O,"))
		{
			if(strCmd.contains("OK"))
			{
				Log.i(TAG, "open laser success!");
				mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY, String.valueOf(DevicePropertyConstants.RANGING_LASER_ON), true);
			}
			else
			{
				Log.i(TAG, "open laser failed!");
				//mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY, String.valueOf(DevicePropertyConstants.RANGING_LASER_OFF), true);
			}
		}
		else if(strCmd.startsWith("C,"))
		{
			if(strCmd.contains("OK"))
			{
				Log.i(TAG, "close laser success!");
				mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY, String.valueOf(DevicePropertyConstants.RANGING_LASER_OFF), true);
			}
			else
			{
				Log.i(TAG, "close laser failed!");
				//mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_LASER_STATUS_PROPERTY, String.valueOf(DevicePropertyConstants.RANGING_LASER_OFF), true);
			}
		}
		else if(strCmd.startsWith("D:"))
		{
			Log.i(TAG, "receive distance data:"+strCmd);
			String strDistance = null;
			try
			{
				if(strCmd.startsWith("D:Er"))
				{
					strDistance = strCmd.substring(4).split(",")[0].trim();
					if(strDistance != null && strDistance.length() > 1)
					{
						strDistance = strDistance.substring(0, strDistance.length()-1);
					}
					else
					{
						Log.i(TAG, "unpack ranging data error:1");
						strDistance = null;
					}
					mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_RET_ERROR_CODE_PROPERTY, strDistance, true);
				}
				else
				{
					strDistance = strCmd.substring(2).split(",")[0].trim();
					if(strDistance != null && strDistance.length() > 1)
					{
						strDistance = strDistance.substring(0, strDistance.length()-1);
						mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_DISTANCE_PROPERTY, strDistance, true);
					}
					else
					{
						Log.i(TAG, "unpack ranging data error:1");
						strDistance = null;
					}
					
				}
			} catch (Exception e)
			{
				// TODO: handle exception
				Log.e(TAG, "unpack ranging data error:2");
			}
		}
		else if(strCmd.startsWith("S:"))
		{
			Log.i(TAG, "receive module info:"+strCmd);
			try
			{
				String tempAndVoltage[] = strCmd.substring(2).split(",");
				if(tempAndVoltage.length == 2)
				{
					tempAndVoltage[0] = tempAndVoltage[0].trim();
					tempAndVoltage[1] = tempAndVoltage[1].trim();
					if(tempAndVoltage[0].length() > 2 && tempAndVoltage[1].length() > 1)
					{
						tempAndVoltage[0] = tempAndVoltage[0].substring(0, tempAndVoltage[0].length()-2);
						tempAndVoltage[1] = tempAndVoltage[1].substring(0, tempAndVoltage[1].length()-1);
						mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_TEMP_PROPERTY, tempAndVoltage[0].trim(), true);
						mService.cachePropValue(DeviceInterfaceProperties.DIM_RANGING_VOLTAGE_PROPERTY, tempAndVoltage[1].trim(), true);
					}	
				}
			} catch (Exception e)
			{
				// TODO: handle exception
				Log.e(TAG, "unpack module info data error!");
			}
		}
	}
	
	public void dispatchServiceState(int state)
	{
		Log.i(TAG,"dispatchServiceState:"+state);
		if(mService != null)
		{
			mServiceState = state;
			mService.cachePropValue(DeviceInterfaceProperties.DIM_LASER_SERVICE_STATUS_PROPERTY, String.valueOf(state), false);
		}
	}

}
