package com.mct.coreservices;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;
import com.mct.serial.mirror.OBDSerial;
import com.mct.utils.ServiceHelper;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;

public class OBDManager extends MctVehicleManager implements OBDSerial.Listener
{
	public static String TAG = "OBDManager";
	public static final int PACKAGE_WRITE_BLOCK = 128;
	public static final int CONNECT_CHECK_TIMEOUT = 1000*30;//连接超时阈值,30s

	private CarService mService = null;
	private OBDSerial mSerial = null;
	private boolean mbWaitReturn = false;
	
	private int mStartupMode = -1;	// 0 Manual 1 Auto
	private int mDelayConn = -1;	//0 Off 1 On
	private int mRTStream = -1;		//0 Off 1 On

	private LinkedBlockingQueue<String> mlsReturn = new LinkedBlockingQueue<String>(); // 阻塞型队列，用于等待数据
	private SparseArray<Vector<Messenger>> mListeners = new SparseArray<Vector<Messenger>>(); // 监听器缓存器
	private ArrayList<BroadcastReceiver> mBroadcastReceives = new ArrayList<BroadcastReceiver>(); // 广播集

	private boolean mSerialConnect = false;
	private int mDeviceConnectState = VehiclePropertyConstants.CAN_DEVICE_CONNECTING;
	private long mLastReceiveDataTime = 0;
	private Timer mDeviceCheckTimer = null;
	
	@Override
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitOBDManager");
		mService = service;
		mSerial = new OBDSerial();
		mSerial.init(ServiceHelper.DEFAULT_PACKAGE_LEN, ServiceHelper.WAIT_ELAPSE, ServiceHelper.WAIT_ELAPSE, 1);
		mSerial.requestListener(this);
		mDeviceConnectState = VehiclePropertyConstants.CAN_DEVICE_CONNECTING;
		mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS, String.valueOf(mDeviceConnectState), false);
		Log.i(TAG, "begin to open serial");
		// 初始化串口数据收发配置
		for (int i = 0; i < ServiceHelper.REPEAT_MAXIMUM; ++i)
		{
			try
			{
				if (mSerial.open() != 0)
				{
					Log.i(TAG, "open obd serial success,and retry count:" + i);
					mSerialConnect = true;
					mDeviceCheckTimer = new Timer();
					mDeviceCheckTimer.schedule(new TimerTask()
					{
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							long curTime = SystemClock.uptimeMillis();
							if(curTime - mLastReceiveDataTime > CONNECT_CHECK_TIMEOUT)
							{
								//连接断开
								if(mDeviceConnectState == VehiclePropertyConstants.CAN_DEVICE_CONNECTED
										|| mDeviceConnectState == VehiclePropertyConstants.CAN_DEVICE_CONNECTING)
								{
									Log.i(TAG, "OBD Disconnected");
									mDeviceConnectState = VehiclePropertyConstants.CAN_DEVICE_DISCONNECTED;
									mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS, String.valueOf(mDeviceConnectState), true);
								}
							}
						}
					}, 5,1000*3);
					Thread.sleep(100);
					mLastReceiveDataTime = SystemClock.uptimeMillis();
					postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_RECEIVE_SETTING_STATUS));
					break;
				}
				else
				{
					Log.i(TAG, "open serial failed and retry");
					mSerial.close();
				}
				Thread.sleep(ServiceHelper.WAIT_ELAPSE);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		Log.i(TAG, "finish to open serial");

		return true;
	}

	@Override
	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		if(mDeviceCheckTimer != null)
		{
			mDeviceCheckTimer.cancel();
			mDeviceCheckTimer.purge();
			mDeviceCheckTimer = null;
			mDeviceConnectState = VehiclePropertyConstants.CAN_DEVICE_DISCONNECTED;
			mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS, String.valueOf(mDeviceConnectState), false);
		}
		if(mSerial != null && mSerialConnect)
		{
			if(mSerial.close() > 0)
			{
				mSerialConnect = false;
			}
			mSerial = null;
			mService = null;
		}
		return true;

	}

	@Override
	public int[] getSupportedPropertyIds()
	{
		// TODO Auto-generated method stub
		return OBDProtocol.VEHICLE_PROPERITIES;
	}

	@Override
	public int[] getWritablePropertyIds()
	{
		// TODO Auto-generated method stub
		List<Integer> writableProps = new ArrayList<Integer>();
		for (int i = 0; i < OBDProtocol.VEHICLE_PROPERITIES.length; i++)
		{
			if (OBDProtocol.getProperityPermission(OBDProtocol.VEHICLE_PROPERITIES[i]) >= OBDProtocol.PROPERITY_PERMISSON_SET)
			{
				writableProps.add(OBDProtocol.VEHICLE_PROPERITIES[i]);
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
		return OBDProtocol.getProperityDataType(propId);
	}
	
	@Override
	public void onReceive(ByteBuffer buffer)
	{
		if (buffer == null) { return; }
		//buffer可能包含多条数据,每条数据以\r\n结束
		String tempBuf = ServiceHelper.toString(buffer);
		Log.i(TAG, "OBD onReceive: " + tempBuf);
		String []data = tempBuf.split("\r\n");
		if (data == null || data.length == 0)
		{
			Log.e(TAG, "onReceive data is empty");
			return;
		}
		
		for(int i= 0;i<data.length;i++)
		{
			if(data[i] == null || data[i].length() < 4 || !data[i].startsWith("$"))
			{
				Log.e(TAG, "invalid obd data:"+data[i]);
				continue;
			}
			Message msg = mService.getMainHandler().obtainMessage();
			msg.what = CarService.MSG_RECEIVE_VEHICLE_DATA;
			Bundle bundle = new Bundle();
			bundle.putString(CarService.KEY_DATA, data[i]);
			msg.setData(bundle);
			mService.getMainHandler().sendMessage(msg);
		}
	}

	@Override
	public void onSend(ByteBuffer data)
	{
		Log.v(TAG, "OBD onSend: " + ServiceHelper.toString(data));
	}
	
	// 转到本地进程空间，避免后续数据处理出错
	@Override
	public void onLocalReceive(Bundle bundle)
	{
		//连接上
		mLastReceiveDataTime = SystemClock.uptimeMillis();
		if(mDeviceConnectState == VehiclePropertyConstants.CAN_DEVICE_CONNECTING ||
				mDeviceConnectState == VehiclePropertyConstants.CAN_DEVICE_DISCONNECTED)
		{
			Log.i(TAG, "OBD Connected");
			mDeviceConnectState = VehiclePropertyConstants.CAN_DEVICE_CONNECTED;
			mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS, String.valueOf(mDeviceConnectState), true);
		}		
				
		String data = bundle.getString(CarService.KEY_DATA);
		// 返回get的数据
		if (mbWaitReturn)
		{
			try
			{
				mlsReturn.put(data);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
		Log.i(TAG, "receive obd data:"+data);
		onReceiveData(data);
	}	

	@Override
	public boolean setPropValue(int propId, String value)
	{
//		if(!ArrayUtils.contains(OBDProtocol.VEHICLE_PROPERITIES, propId))
//		{
//			Log.e(TAG, "not support this property,ID:"+propId);
//			return false;
//		}
		int cmd = -1;
		switch (propId)
		{
			case VehicleInterfaceProperties.VIM_CAN_BAUD_RATE_PROPERTY:
				cmd = Integer.valueOf(value);
				switch(cmd)
				{
					case 0:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_BAUD_RATE_9600));
					case 1:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_BAUD_RATE_38400));
					case 2:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_BAUD_RATE_115200));
					default:
						break;
				}
				break;
			case VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY:
				cmd = Integer.valueOf(value);
				switch(cmd)
				{
					case VehiclePropertyConstants.CAN_CMD_REQ_ENGINE_LOAD:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_ENGINE_LOAD));
					case VehiclePropertyConstants.CAN_CMD_REQ_COOLANT_TEMP:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_COOLANT_TEMP));
					case VehiclePropertyConstants.CAN_CMD_REQ_FUEL_PRESSURE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_FUEL_PRESSURE));
					case VehiclePropertyConstants.CAN_CMD_REQ_INPIPE_PRESSURE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_INPIPE_PRESSURE));
					case VehiclePropertyConstants.CAN_CMD_REQ_ENGINE_SPEED:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_ENGINE_SPEED));
					case VehiclePropertyConstants.CAN_CMD_REQ_DRIVING_SPEED:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_DRIVING_SPEED));
					case VehiclePropertyConstants.CAN_CMD_REQ_IGNITION_ANGLE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_IGNITION_ANGLE));
					case VehiclePropertyConstants.CAN_CMD_REQ_INTAKE_TEMP:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_INTAKE_TEMP));
					case VehiclePropertyConstants.CAN_CMD_REQ_INTAKE_RATE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_INTAKE_FLOW_RATE));
					case VehiclePropertyConstants.CAN_CMD_REQ_THROTTLE_POSN:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_THROTTLE_POSN));
					case VehiclePropertyConstants.CAN_CMD_REQ_ENGINE_RUN_TIME:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_ENGINE_RUN_TIME));
					case VehiclePropertyConstants.CAN_CMD_REQ_VACUUM_OIL_RAIL_PRESSURE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_VACUUM_OIL_PRESSURE));
					case VehiclePropertyConstants.CAN_CMD_REQ_EGR_DEGREE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_EGR_OPEN_DEGREE));
					case VehiclePropertyConstants.CAN_CMD_REQ_EVA_DEGREE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_EVA_CLEAR_OPEN_DEGREE));
					case VehiclePropertyConstants.CAN_CMD_REQ_FUEL_RMNG:
						//测试数据，正式版需删除
						//mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY, "28.8", true);
						/////////////////////////////////////////////////////////////
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_REMAING_FUEL));
					case VehiclePropertyConstants.CAN_CMD_REQ_VEHICLE_VIN:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_VEHICLE_VIN_CODE));	
					case VehiclePropertyConstants.CAN_CMD_REQ_BATTERY_VOLTAGE:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_BATTERY_VOLTAGE));	
					case VehiclePropertyConstants.CAN_CMD_REQ_FUEL_CONSUM_INFO:
						postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_CUR_FUEL_CONSUMPTION));
						postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_FUEL_CONSN_HUND_KM));
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_FUEL_CONSUMPTION_INFO));
					case VehiclePropertyConstants.CAN_CMD_REQ_ODOMETER_INFO:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_ODOMETER_INFO));
						
					case VehiclePropertyConstants.CAN_CMD_REQ_DRVING_TIME_INFO:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_DRIVING_TIME_INFO));
					case VehiclePropertyConstants.CAN_CMD_REQ_MINUS_ODOMETER:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_MINUS_ODOMETER));
					case VehiclePropertyConstants.CAN_CMD_REQ_CLEAR_DTC:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_CLEAR_DTC_CODE));
					case VehiclePropertyConstants.CAN_CMD_REQ_DEVICE_INFO:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_DEVICE_INFO));
					case VehiclePropertyConstants.CAN_CMD_REQ_CLEAR_DATA:
						return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_CLEAR_CACHE_DATA));
					case VehiclePropertyConstants.CAN_CMD_REQ_DRIV_HABIT_DATA:
						return postData(OBDProtocol.AT_CMD_DRIVING_HABITS);
					case VehiclePropertyConstants.CAN_CMD_REQ_HOT_RESTART:
						return postData(OBDProtocol.AT_CMD_HOT_RESTART);
					case VehiclePropertyConstants.CAN_CMD_REQ_SLEEP:
						return postData(OBDProtocol.AT_CMD_SLEEP);
				}
				break;
			case VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY:
				if(Integer.valueOf(value) == 0 && mRTStream != 0)
				{
					return postData(OBDProtocol.addTail(OBDProtocol.AT_CMD_RT_DATA_STREAM_OFF));
				}
				else if(Integer.valueOf(value) == 1 && mRTStream != 1)
				{
					return postData(OBDProtocol.addTail(OBDProtocol.AT_CMD_RT_DATA_STREAM_ON));
				}
				break;
			case VehicleInterfaceProperties.VIM_CAN_STARTUP_MODE_PROPERTY:
				if(Integer.valueOf(value) == 0 && mStartupMode != 0)
				{
					return postData(OBDProtocol.addTail(OBDProtocol.AT_CMD_STARTUP_MANUAL));
				}
				else if(Integer.valueOf(value) == 1 && mStartupMode != 1)
				{
					return postData(OBDProtocol.addTail(OBDProtocol.AT_CMD_STARTUP_AUTO));
				}
				break;
			case VehicleInterfaceProperties.VIM_CAN_DELAY_CONNECT_STATUS_PROPERTY:
				if(Integer.valueOf(value) == 0 && mDelayConn  != 0)
				{
					return postData(OBDProtocol.addTail(OBDProtocol.AT_CMD_ECU_DELAY_CONN_ON));
				}
				else if(Integer.valueOf(value) == 1 && mDelayConn  != 1)
				{
					return postData(OBDProtocol.addTail(OBDProtocol.AT_CMD_ECU_DELAY_CONN_OFF));
				}
				break;
				//总里程校准
			case VehicleInterfaceProperties.VIM_MEASUREMENT_DISTANCE_PROPERTY:
				return postData(OBDProtocol.getATCmd(OBDProtocol.AT_CMD_NO_TOTAL_ODOMETER_CHECK, value));
			case VehicleInterfaceProperties.VIM_CRUISE_CONTROL_STATUS_PROPERTY:
				break;
			case VehicleInterfaceProperties.VIM_MALFUNCTION_INDICATOR_PROPERTY:
				break;
			case VehicleInterfaceProperties.VIM_HEAD_LIGHTS_PROPERTY:
				break;
			case VehicleInterfaceProperties.VIM_AUTOMATIC_HEAD_LIGHTS_PROPERTY:
				break;
			default:
				return false;
		}
		return true;
	}
	
	@Override
	public String getPropValue(int propId)
	{
		return null;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean postData(String data)
	{
		if(data == null || data.length() == 0)
		{
			return false;
		}
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
		byte[] dataArray = data.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(dataArray.length);
		buffer.put(dataArray);
		return mSerial.post(buffer);
	}

	//OBD暂不使用此情景方式
	private synchronized String sendData(String data,String expectedData,int timeout)
	{
		if(data == null || data.length() == 0)
		{
			return null;
		}
		String byReturn = null;
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
			Log.e(TAG, "OBD not connected, can't send request to it");
			return byReturn;
		}

		// 只需要封装Data区域即可
		byte[] dataArray = data.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(dataArray.length);
		buffer.put(dataArray);

		// send data, repeat 3 times if failed
		for (int i = 0; i < ServiceHelper.REPEAT_NORMAL; ++i)
		{
			if (mSerial.send(buffer))
				break;
		}

		if (timeout > 0)
		{
			byReturn = waitData(expectedData, timeout);
			if (byReturn == null)
			{
				Log.e(TAG, "writeData wait return timeout! " + "expecteData:" + expectedData + ", timeout=" + timeout);
			}
		}
		return byReturn;
	}

	private synchronized String waitData(String expectedData, int timeout)
	{
		if (mSerial == null || !mSerialConnect)
		{
			Log.e(TAG, "OBD Serial not connected, can't wait request to it");
			return null;
		}
		
		String byReturn = null;
		mbWaitReturn = true;
		try
		{
			int waitTime = timeout;
			while (!Thread.currentThread().isInterrupted() && (waitTime >= 0 || timeout == ServiceHelper.WAIT_INFINITE))
			{
				long tTick = SystemClock.uptimeMillis();
				byReturn = mlsReturn.poll(waitTime, TimeUnit.MILLISECONDS);
				if (byReturn != null && byReturn.length() > 0 && byReturn.contains(expectedData))
				{
					break;
				}
				else
				{
					byReturn = null;
					Log.w(TAG, "not expected result,expected data:"+expectedData);
				}
				waitTime -= (SystemClock.uptimeMillis() - tTick);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		mbWaitReturn = false;
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

	private void onReceiveData(String data)
	{
		// 驾驶实时数据
		if (data.startsWith(OBDProtocol.OBD_VEHICLE_RT_DATA_STREAM_HEAD))
		{
			String drivingData[] = data.split(",");
			if(drivingData == null || drivingData.length != 16)
			{
				Log.e(TAG,"unvalid driving data");
				return;
			}
			if(mService != null)
			{
				mRTStream = 1;
				mService.cachePropValue(VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY, drivingData[1], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY, drivingData[2], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY, drivingData[3], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_THROTTLE_POSN_PROPERTY, drivingData[4], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGN_LOAD_PROPERTY, drivingData[5], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_COOLANT_TEMP_PROPERTY, drivingData[6], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY, drivingData[7], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_HUNDRED_KILOMETERS_AVG_FUEL_CONSUMPTION_PROPERTY, drivingData[8], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY, drivingData[9], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY, drivingData[10], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_FUEL_CONSUMPTION_PROPERTY, drivingData[11], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_2_FUEL_CONSUMPTION_PROPERTY, drivingData[12], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_MALFUNCTION_INDICATOR_PROPERTY, drivingData[13], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_UP_TIMES, drivingData[14], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_DOWN_TIMES, drivingData[15], false);
				mService.dispatchData(VehicleInterfaceProperties.VIM_CAN_DATA_STREAM_UPDATE_PROPERTY, String.valueOf(VehiclePropertyConstants.DATA_STREAM_TYPE_CAN_RT_STREAM));
			}
		}
		// 驾驶习惯数据
		else if (data.startsWith(OBDProtocol.OBD_DRIVING_HABIT_HEAD))
		{
			String habitData[] = data.split(",");
			if(habitData == null || habitData.length != 10)
			{
				Log.e(TAG,"unvalid driving habit data");
				return;
			}
			if(mService != null)
			{
				mService.cachePropValue(VehicleInterfaceProperties.VIM_TOTAL_IGNITION_TIMES, habitData[1], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_TOTAL_PROPERTY, habitData[2], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_TOTAL_PROPERTY, habitData[3], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_WARM_UP_TIME, habitData[4], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_AVG_VECHILE_SPEED, habitData[5], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_HISTORY_HIGHEST_VEHICLE_SPEED, habitData[6], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_HISTORY_HIGHEST_ENGINE_SPEED, habitData[7], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_TOTAL_TRIP_QUICK_SPEED_UP_TIMES, habitData[8], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_TOTAL_TRIP_QUICK_SPEED_DOWN_TIMES, habitData[9], false);
				mService.dispatchData(VehicleInterfaceProperties.VIM_CAN_DATA_STREAM_UPDATE_PROPERTY, String.valueOf(VehiclePropertyConstants.DATA_STREAM_TYPE_CAN_DRIVING_HABITS));
			}
		}
		// 本次行程统计数据
		else if (data.startsWith(OBDProtocol.OBD_CUR_TRAVEL_DATA_HEAD))
		{
			String travelData[] = data.split(",");
			if(travelData == null || travelData.length != 11)
			{
				Log.e(TAG,"unvalid travel data");
				return;
			}
			if(mService != null)
			{
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_WARM_UP_TIME, travelData[1], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_CUR_PROPERTY, travelData[2], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_CUR_PROPERTY, travelData[3], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY, travelData[4], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_IDLING_FUEL_CONSUMPTION_PROPERTY, travelData[5], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_DRIVING_FUEL_CONSUMPTION_PROPERTY, travelData[6], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_HIGEST_ENGINE_SPEED, travelData[7], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_HIGEST_VECHILE_SPEED, travelData[8], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_UP_TIMES, travelData[9], false);
				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_DOWN_TIMES, travelData[10], false);
				mService.dispatchData(VehicleInterfaceProperties.VIM_CAN_DATA_STREAM_UPDATE_PROPERTY, String.valueOf(VehiclePropertyConstants.DATA_STREAM_TYPE_CAN_THIS_TRIP));
			}
		}
		else 
		{
			onHandleATCmdResult(data);
		}
	}
	
	private String onHandleATCmdResult(String data)
	{
		//$004=16.86
		String pattern = "\\$([0-9]+)=(.*)";
	    // 创建 Pattern 对象
	    Pattern r = Pattern.compile(pattern);
	    // 现在创建 matcher 对象
	    Matcher m = r.matcher(data);
	    String cmdNo = null;
	    String value = null;
	    //状态值返回
	    if (m.find( )) 
	    {
	    	cmdNo = m.group(1);
	    	value = m.group(2);
	    	if(cmdNo == null)
	    	{
	    		return null;
	    	}
	    	if(value != null && value.equals(OBDProtocol.ECU_NOT_SUPPORT))
	    	{
	    		Log.w(TAG, cmdNo+" not support!");
	    		//剩余油量不支持
	    		if(cmdNo.equals(OBDProtocol.AT_CMD_NO_REMAING_FUEL))
	    		{
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY, "-1", true);
	    		}
	    		return null;
	    	}
	    	if(cmdNo.equals(OBDProtocol.AT_CMD_NO_ENGINE_LOAD))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGN_LOAD_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_COOLANT_TEMP))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_COOLANT_TEMP_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_FUEL_PRESSURE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_FUEL_PRESSURE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_INPIPE_PRESSURE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_IN_PIPE_PRESSURE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_ENGINE_SPEED))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_DRIVING_SPEED))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_IGNITION_ANGLE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_IGNITION_ANGLE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_INTAKE_TEMP))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_INTAKE_TEMP_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_INTAKE_FLOW_RATE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_INTAKE_FLOW_RATE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_ENGINE_RUN_TIME))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_ENGN_RUN_TIME_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_VACUUM_OIL_PRESSURE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_VACUUM_OIL_RAIL_PRESSURE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_EGR_OPEN_DEGREE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_EGR_OPEN_DEGREE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_EVA_CLEAR_OPEN_DEGREE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_EVA_CLEAR_OPEN_DEGREE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_REMAING_FUEL))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_EVA_SYSTEM_PRESSURE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_EVA_SYSTEM_PRESSURE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_VEHICLE_VIN_CODE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_VIN_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_BATTERY_VOLTAGE))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_CUR_FUEL_CONSUMPTION))
	    	{
	    		String []curFuelConsum = value.split(",");
	    		if(curFuelConsum.length == 2)
	    		{
	    			//怠速瞬时油耗
	    			if(curFuelConsum[0].equals(0))
	    			{
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_IDLING_FUEL_CONSUMPTION_PROPERTY, curFuelConsum[1], true);
	    			}
	    			//行驶瞬时油耗
	    			else if(curFuelConsum[0].equals("1"))
	    			{
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CUR_TRIP_DRIVING_FUEL_CONSUMPTION_PROPERTY, curFuelConsum[1], true);
	    			}
	    		}
	    		
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_FUEL_CONSN_HUND_KM))
	    	{
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_HUNDRED_KILOMETERS_AVG_FUEL_CONSUMPTION_PROPERTY, value, true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_ODOMETER_INFO))
	    	{
	    		String []odometerInfo = value.split(",");
	    		if(odometerInfo.length == 3)
	    		{
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY, odometerInfo[0], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_2_MILEAGE_PROPERTY, odometerInfo[1], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY, odometerInfo[2], true);
	    		}
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_FUEL_CONSUMPTION_INFO))
	    	{
	    		String []fuelComsumInfo = value.split(",");
	    		if(fuelComsumInfo.length == 2)
	    		{
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_1_FUEL_CONSUMPTION_PROPERTY, fuelComsumInfo[0], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_TRIP_METER_2_FUEL_CONSUMPTION_PROPERTY, fuelComsumInfo[1], true);
	    		}
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_DRIVING_TIME_INFO))
	    	{
	    		String []drivTimeInfo = value.split(",");
	    		if(drivTimeInfo.length == 4)
	    		{
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_CUR_PROPERTY, drivTimeInfo[0], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_CUR_PROPERTY, drivTimeInfo[1], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_TOTAL_PROPERTY, drivTimeInfo[2], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_TOTAL_PROPERTY, drivTimeInfo[3], true);
	    		}
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_READ_DTC_CODE))
	    	{
	    		String []DTCStruct = value.split(",");
	    		if(DTCStruct.length !=2)
	    		{
	    			return null;
	    		}
	    		int DTCCodeNum = Integer.valueOf(DTCStruct[0]);
	    		String DTCCodes[] = DTCStruct[1].split("|");
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_MALFUNCTION_INDICATOR_PROPERTY, DTCStruct[1], true);
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_DEVICE_INFO))
	    	{
	    		String []deviceInfo = value.split(",");
	    		if(deviceInfo.length == 4)
	    		{
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_PROTOCOL_NAME_PROPERTY, deviceInfo[0], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_VEHICLE_MODULE_SN_PROPERTY, deviceInfo[1], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_HW_VERSION_PROPERTY, deviceInfo[2], true);
	    			mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY, deviceInfo[3], true);
	    		}
	    	}
	    	else if(cmdNo.equals(OBDProtocol.AT_CMD_NO_RECEIVE_SETTING_STATUS))
	    	{
	    		String []settingStatus = value.split(",");
	    		if(settingStatus.length >= 3)
	    		{
	    			if(settingStatus[0].equals("AUTO"))
	    			{
	    				mStartupMode = 1;
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_STARTUP_MODE_PROPERTY, String.valueOf(mStartupMode), false);
	    			}
	    			else if(settingStatus[0].equals("MANUAL"))
	    			{
	    				mStartupMode = 0;
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_STARTUP_MODE_PROPERTY, String.valueOf(mStartupMode), false);
	    			}
	    			if(settingStatus[1].equals("IMMEDLY"))
	    			{
	    				mDelayConn = 0;
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DELAY_CONNECT_STATUS_PROPERTY, String.valueOf(mDelayConn), false);
	    			}
	    			else if(settingStatus[1].equals("DELAY"))
	    			{
	    				mDelayConn = 1;
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DELAY_CONNECT_STATUS_PROPERTY, String.valueOf(mDelayConn), false);
	    			}
	    			if(settingStatus[2].equals("RON"))
	    			{
	    				mRTStream = 1;
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY, String.valueOf(mRTStream), false);
	    			}
	    			else if(settingStatus[2].equals("ROFF"))
	    			{
	    				mRTStream = 0;
	    				mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY, String.valueOf(mRTStream), false);
	    			}
	    		}
	    	}
	    	return value;
	    } 
	    //命令请求操作返回
	    else if(data.endsWith("+OK."))
	    {
	    	//$EST527,ATROFF+OK.
			//$EST527,ATRON+OK.
	    	if(data.contains(OBDProtocol.AT_CMD_RT_DATA_STREAM_ON))
	    	{
	    		mRTStream = 1;
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY, String.valueOf(mRTStream), false);
	    		Log.i(TAG, "RT Stream On");
	    	}
	    	else if(data.contains(OBDProtocol.AT_CMD_RT_DATA_STREAM_OFF))
	    	{
	    		mRTStream = 0;
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY, String.valueOf(mRTStream), false);
	    		Log.i(TAG, "RT Stream Off");
	    	}
	    	else if(data.contains(OBDProtocol.AT_CMD_ECU_DELAY_CONN_ON))
	    	{
	    		mDelayConn = 1;
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DELAY_CONNECT_STATUS_PROPERTY, String.valueOf(mDelayConn), false);
	    		Log.i(TAG, "Delay Connect On");
	    	}
	    	else if(data.contains(OBDProtocol.AT_CMD_ECU_DELAY_CONN_OFF))
	    	{
	    		mDelayConn = 0;
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_DELAY_CONNECT_STATUS_PROPERTY, String.valueOf(mDelayConn), false);
	    		Log.i(TAG, "Delay Connect Off");
	    	}
	    	else if(data.contains(OBDProtocol.AT_CMD_STARTUP_AUTO))
	    	{
	    		mStartupMode = 1;
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_STARTUP_MODE_PROPERTY, String.valueOf(mStartupMode), false);
	    		Log.i(TAG, "Switch to Startup Auto");
	    	}
	    	else if(data.contains(OBDProtocol.AT_CMD_STARTUP_MANUAL))
	    	{
	    		mStartupMode = 0;
	    		mService.cachePropValue(VehicleInterfaceProperties.VIM_CAN_STARTUP_MODE_PROPERTY, String.valueOf(mStartupMode), false);
	    		Log.i(TAG, "Switch to Startup Manual");
	    	}
	    }
	    return null;
	}

}
