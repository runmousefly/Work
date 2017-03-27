package com.mct.vehicle.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import com.mct.VehicleInterfaceDataHandler;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.vehicle.demo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OBDDemoActivity extends Activity implements View.OnClickListener

{
	public static String TAG = "OBDDemo";
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();
	// Serial mSerial = null;
	static Context gContext = null;
	public VehicleManager mVehicleManager = null;

	int[] mSupportedPropIds = null;
	int[] mWritablePropIds = null;
	int[] mPropDataType = null;
	int[] mDrivingStreamProperties = new int[]{VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY,
			VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY,VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY,
			VehicleInterfaceProperties.VIM_THROTTLE_POSN_PROPERTY,VehicleInterfaceProperties.VIM_ENGN_LOAD_PROPERTY,
			VehicleInterfaceProperties.VIM_COOLANT_TEMP_PROPERTY,VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY,
			VehicleInterfaceProperties.VIM_HUNDRED_KILOMETERS_AVG_FUEL_CONSUMPTION_PROPERTY,
			VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY,VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY,
			VehicleInterfaceProperties.VIM_TRIP_METER_1_FUEL_CONSUMPTION_PROPERTY,
			VehicleInterfaceProperties.VIM_TRIP_METER_2_FUEL_CONSUMPTION_PROPERTY,VehicleInterfaceProperties.VIM_MALFUNCTION_INDICATOR_PROPERTY,
			VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_UP_TIMES,VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_DOWN_TIMES};
	int[] mDrivingHabitsProperties = new int[]{VehicleInterfaceProperties.VIM_TOTAL_IGNITION_TIMES,
			VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_TOTAL_PROPERTY,VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_TOTAL_PROPERTY,
			VehicleInterfaceProperties.VIM_AVG_WARM_UP_TIME,VehicleInterfaceProperties.VIM_AVG_VECHILE_SPEED,
			VehicleInterfaceProperties.VIM_HISTORY_HIGHEST_VEHICLE_SPEED,VehicleInterfaceProperties.VIM_HISTORY_HIGHEST_ENGINE_SPEED,
			VehicleInterfaceProperties.VIM_TOTAL_TRIP_QUICK_SPEED_UP_TIMES,VehicleInterfaceProperties.VIM_TOTAL_TRIP_QUICK_SPEED_DOWN_TIMES};
	
	EditText mBatteryVoltageEText = null;
	EditText mEngineSpeedEText = null;
	EditText mVehicleSpeedEText = null;
	EditText mThrottlePosnEText = null;
	EditText mEngineLoadEText = null;
	EditText mCoolantTempEText = null;
	EditText mInstantFuelConsumeEText = null;
	EditText mAvgFuelConsumeEText = null;
	EditText mCurTravelOdometerEText = null;
	EditText mTotalOdometerEText = null;
	EditText mCurTravelFuelConsumeEText = null;
	EditText mTotallFuelConsumeEText = null;
	EditText mMalfunctionIndicatorEText = null;
	EditText mCurTravelSpeedUpTimesEText = null;
	EditText mCurTravelSpeedDownTimesEText = null;
	
	EditText mTotalIgnitionTimesEText = null;
	EditText mTotalDriveTimeEText = null;
	EditText mTotalIdleTimeEText = null;
	EditText mAvgWarmUpTime = null;
	EditText mAvgVehicleSpeed = null;
	EditText mHistoryHigestVehicleSpeedEText = null;
	EditText mHistoryHigestEngineSpeedEText = null;
	EditText mTotalSpeedUpTimesEText = null;
	EditText mTotalSpeedDownTimesEText = null;
	EditText mLeftFuelEText = null;
	
	EditText mAddedOdometerEText = null;//累计里程
	EditText mCalibrateOdometerEText = null;//累成校准值
	EditText mCurDriveTimeEText = null;//本次行驶时间
	EditText mCurIdleTimeEText = null;//本次怠速时间
	
	
	Button mOpenStreamBtn = null;
	Button mCloseStreamBtn = null;
	
	Button mReqHabitDataBtn = null;
	Button mReqLeftFuel = null;
	Button mCalbirateTotalOdometerBtn = null;//校准总里程
	Button mReqOdometerInfoBtn = null;//请求里程信息
	Button mReqMinusAddedOdometerBtn = null;//累计里程扣减
	Button mReqClearDataBtn = null;//请求清除OBD插头模块数据
	Button mReqTimeInfo = null;//请求时间信息
	
	Button mRestartBtn = null;
	Button mSleepBtn = null;
	
	HashMap<Integer, EditText> mWidgetMap = new HashMap<Integer, EditText>();
	
	
	public class VehicleDataNotification implements VehicleInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(int propId,String value)
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "onDataUpdate,PropId:"+propId+",value:"+value);
			if(propId != VehicleInterfaceProperties.VIM_CAN_DATA_STREAM_UPDATE_PROPERTY)
			{
				if(propId == VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS)
				{
					Log.i(TAG, "OBD Device Status Update:"+value);
					return;
				}
				onUpdateUi(propId,value);
			}
			else
			{
				int streamType = Integer.valueOf(value);
				switch(streamType)
				{
					case VehiclePropertyConstants.DATA_STREAM_TYPE_CAN_RT_STREAM:
						Log.i(TAG, "DATA_STREAM_TYPE_OBD_RT_STREAM Update");
						if(mVehicleManager != null)
						{
							String []values = mVehicleManager.getProperties(mDrivingStreamProperties);
							for(int i = 0; i < mDrivingStreamProperties.length; i++)
							{
								onUpdateUi(mDrivingStreamProperties[i], values[i]);
							}
						}
						break;
					case VehiclePropertyConstants.DATA_STREAM_TYPE_CAN_DRIVING_HABITS:
						Log.i(TAG, "DATA_STREAM_TYPE_OBD_DRIVING_HABITS Update");
						if(mVehicleManager != null)
						{
							String []values = mVehicleManager.getProperties(mDrivingHabitsProperties);
							for(int i = 0; i < mDrivingHabitsProperties.length; i++)
							{
								onUpdateUi(mDrivingHabitsProperties[i], values[i]);
							}
						}
						break;
					case VehiclePropertyConstants.DATA_STREAM_TYPE_CAN_THIS_TRIP:
						Log.i(TAG, "DATA_STREAM_TYPE_OBD_THIS_TRIP Update");
						break;
				}
			}
		}
		
		@Override
		public void onError(boolean bCleanUpAndRestart)
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "onError,bCleanUpAndRestart:" + bCleanUpAndRestart);
		}

		

	}

	private String toString(int[] array)
	{
		if (array == null || array.length == 0) { return null; }
		String str = new String();
		for (int i = 0; i < array.length; i++)
		{
			if (i > 0)
			{
				str += " ,";
			}
			str += array[i];
		}
		return str;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_obd);
		
		
		mBatteryVoltageEText = (EditText) findViewById(R.id.BatteryVoltage);
		mEngineSpeedEText = (EditText) findViewById(R.id.EngineSpeed);
		mVehicleSpeedEText = (EditText) findViewById(R.id.VehicleSpeed);
		mThrottlePosnEText = (EditText) findViewById(R.id.ThrotilePosn);
		mEngineLoadEText = (EditText) findViewById(R.id.EngineLoad);
		mCoolantTempEText = (EditText) findViewById(R.id.CoolantTemp);
		mInstantFuelConsumeEText = (EditText) findViewById(R.id.InstantFuelConsume);
		mAvgFuelConsumeEText = (EditText) findViewById(R.id.AVGFuelConsume);
		mCurTravelOdometerEText = (EditText) findViewById(R.id.CurTravelOdometer);
		mTotalOdometerEText = (EditText) findViewById(R.id.TotalOdometer);
		mCurTravelFuelConsumeEText = (EditText) findViewById(R.id.CurTravelFuelConsume);
		mTotallFuelConsumeEText = (EditText) findViewById(R.id.TotallFuelConsume);
		mMalfunctionIndicatorEText = (EditText) findViewById(R.id.MalfunctionIndicatorNo);
		mCurTravelSpeedUpTimesEText = (EditText) findViewById(R.id.CurTravelSpeedUpTimes);
		mCurTravelSpeedDownTimesEText = (EditText) findViewById(R.id.CurTravelSpeedDownTimes);
		
		mTotalIgnitionTimesEText = (EditText) findViewById(R.id.TotalIgnitionTimes);
		mTotalDriveTimeEText = (EditText) findViewById(R.id.TotalDriveTime);
		mTotalIdleTimeEText = (EditText) findViewById(R.id.TotalIdleTime);
		mAvgWarmUpTime = (EditText) findViewById(R.id.AvgWarmUpTime);
		mAvgVehicleSpeed = (EditText) findViewById(R.id.AvgVehicleSpeed);
		mHistoryHigestVehicleSpeedEText = (EditText) findViewById(R.id.HistoryHigestVehicleSpeed);
		mHistoryHigestEngineSpeedEText = (EditText) findViewById(R.id.HistoryHigestEngineSpeed);
		mTotalSpeedUpTimesEText = (EditText) findViewById(R.id.TotalSpeedUpTimes);
		mTotalSpeedDownTimesEText = (EditText) findViewById(R.id.TotalSpeedDownTimes);
		
		mLeftFuelEText = (EditText)findViewById(R.id.LeftFuel);
		mAddedOdometerEText = (EditText)findViewById(R.id.AddedOdoInfo);
		mCalibrateOdometerEText =(EditText)findViewById(R.id.CalibrateOdometerValue);
		
		mCurDriveTimeEText = (EditText)findViewById(R.id.CurDriveTime);
		mCurIdleTimeEText = (EditText)findViewById(R.id.CurIdleTime);
		
		mOpenStreamBtn = (Button) findViewById(R.id.OpenStream);
		mOpenStreamBtn.setOnClickListener(this);
		mCloseStreamBtn = (Button) findViewById(R.id.CloseStream);
		mCloseStreamBtn.setOnClickListener(this);
		
		mReqHabitDataBtn = (Button) findViewById(R.id.ReqHabitData);
		mReqHabitDataBtn.setOnClickListener(this);
		
		mReqLeftFuel = (Button)findViewById(R.id.ReqLeftFuel);
		mReqLeftFuel.setOnClickListener(this);
		
		mCalbirateTotalOdometerBtn = (Button)findViewById(R.id.CalibrageOdometer);
		mCalbirateTotalOdometerBtn.setOnClickListener(this);
		
		mReqOdometerInfoBtn = (Button)findViewById(R.id.ReqOdoInfo);
		mReqOdometerInfoBtn.setOnClickListener(this);
		
		mReqMinusAddedOdometerBtn = (Button)findViewById(R.id.MinusOdometer);
		mReqMinusAddedOdometerBtn.setOnClickListener(this);
		
		mReqTimeInfo = (Button)findViewById(R.id.ReqTimeInfo);
		mReqTimeInfo.setOnClickListener(this);
		
		mReqClearDataBtn = (Button)findViewById(R.id.ClearData);
		mReqClearDataBtn.setOnClickListener(this);
		
		mRestartBtn = (Button) findViewById(R.id.Restart);
		mRestartBtn.setOnClickListener(this);
		mSleepBtn = (Button) findViewById(R.id.Sleep);
		mSleepBtn.setOnClickListener(this);
		
		mWidgetMap.put(VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY, mBatteryVoltageEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY, mEngineSpeedEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY, mVehicleSpeedEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_THROTTLE_POSN_PROPERTY, mThrottlePosnEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_ENGN_LOAD_PROPERTY, mEngineLoadEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_COOLANT_TEMP_PROPERTY, mCoolantTempEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_ENGE_INSTANT_FUEL_CONSUMPTION_PROPERTY, mInstantFuelConsumeEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_HUNDRED_KILOMETERS_AVG_FUEL_CONSUMPTION_PROPERTY, mAvgFuelConsumeEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_TRIP_METER_1_MILEAGE_PROPERTY, mCurTravelOdometerEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY, mTotalOdometerEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_TRIP_METER_1_FUEL_CONSUMPTION_PROPERTY, mCurTravelFuelConsumeEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_TRIP_METER_2_FUEL_CONSUMPTION_PROPERTY, mTotallFuelConsumeEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_MALFUNCTION_INDICATOR_PROPERTY, mMalfunctionIndicatorEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_UP_TIMES, mCurTravelSpeedUpTimesEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_CUR_TRIP_QUICK_SPEED_DOWN_TIMES, mCurTravelSpeedDownTimesEText);
		
		mWidgetMap.put(VehicleInterfaceProperties.VIM_TOTAL_IGNITION_TIMES, mTotalIgnitionTimesEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_TOTAL_PROPERTY, mTotalDriveTimeEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_TOTAL_PROPERTY, mTotalIdleTimeEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_AVG_WARM_UP_TIME, mAvgWarmUpTime);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_AVG_VECHILE_SPEED, mAvgVehicleSpeed);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_HISTORY_HIGHEST_VEHICLE_SPEED, mHistoryHigestVehicleSpeedEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_HISTORY_HIGHEST_ENGINE_SPEED, mHistoryHigestEngineSpeedEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_TOTAL_TRIP_QUICK_SPEED_UP_TIMES, mTotalSpeedUpTimesEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_TOTAL_TRIP_QUICK_SPEED_DOWN_TIMES, mTotalSpeedDownTimesEText);

		mWidgetMap.put(VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY, mLeftFuelEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_TRIP_METER_2_MILEAGE_PROPERTY, mAddedOdometerEText);
		
		mWidgetMap.put(VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_CUR_PROPERTY, mCurDriveTimeEText);
		mWidgetMap.put(VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_CUR_PROPERTY, mCurIdleTimeEText);
		mVehicleManager = VehicleManager.getInstance();
		if(mVehicleManager == null)
		{
			Log.e(TAG, "get Vehcle Instace failed");
			return;
		}
		Log.i(TAG, "get Vehcle Instace success");
		mSupportedPropIds = mVehicleManager.getSupportedPropertyIds();
		mWritablePropIds = mVehicleManager.getWritablePropertyIds();
		mPropDataType = mVehicleManager.getPropertiesDataType(mSupportedPropIds);
		Log.i(TAG, "getSupportedPropIds:" + toString(mSupportedPropIds));
		Log.i(TAG, "getWritablePropIds:" + toString(mWritablePropIds));
		Log.i(TAG, "getPropDataType:" + toString(mPropDataType));
		
		
		String []values = mVehicleManager.getProperties(new int[]{VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY,
				VehicleInterfaceProperties.VIM_CAN_DELAY_CONNECT_STATUS_PROPERTY,VehicleInterfaceProperties.VIM_CAN_STARTUP_MODE_PROPERTY});
		if(values != null && values.length == 3)
		{
			Log.i(TAG, "VIM_OBD_RT_DATA_STREAM_STATUS_PROPERTY:" + values[0]);
			Log.i(TAG, "VIM_OBD_DELAY_CONNECT_STATUS_PROPERTY:" + values[1]);
			Log.i(TAG, "VIM_OBD_STARTUP_MODE_PROPERTY:" + values[2]);
		}
		
		mVehicleManager.registerHandler(new int[]{VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY,
				VehicleInterfaceProperties.VIM_TRIP_METER_2_MILEAGE_PROPERTY,
				VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_CUR_PROPERTY,
				VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_CUR_PROPERTY,
				VehicleInterfaceProperties.VIM_VEHICLE_DRIVING_TIME_TOTAL_PROPERTY,
				VehicleInterfaceProperties.VIM_VEHICLE_IDLE_TIME_TOTAL_PROPERTY,
				VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS}, mDataNotification);
		mVehicleManager.registerHandler(mDrivingStreamProperties, mDataNotification);
		mVehicleManager.registerHandler(new int[]{VehicleInterfaceProperties.VIM_CAN_DATA_STREAM_UPDATE_PROPERTY}, mDataNotification);
	
		Log.i(TAG, "OBD Connect status:"+mVehicleManager.getProperty(VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS));
		
	}

	@Override
	protected void onResume()
	{
		Log.i(TAG, "onResume");
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onDestroy()
	{
		Log.i(TAG, "onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mVehicleManager != null && mSupportedPropIds != null)
		{
			mVehicleManager.removeHandler(mDrivingStreamProperties, mDataNotification);
			mVehicleManager.removeHandler(new int[]{VehicleInterfaceProperties.VIM_CAN_DATA_STREAM_UPDATE_PROPERTY}, mDataNotification);
		}
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v == mOpenStreamBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY, "1");
			}
		}
		else if(v == mCloseStreamBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_RT_DATA_STREAM_STATUS_PROPERTY, "0");
			}
		}
		else if(v == mReqHabitDataBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_DRIV_HABIT_DATA));
			}
		}
		else if(v == mReqLeftFuel)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_FUEL_RMNG));
			}
		}
		else if(v == mRestartBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_HOT_RESTART));
			}
		}
		else if(v == mSleepBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_SLEEP));
			}
		}
		else if(v == mReqOdometerInfoBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_ODOMETER_INFO));
			}
		}
		else if(v == mReqMinusAddedOdometerBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_MINUS_ODOMETER));
			}
		}
		else if(v == mCalbirateTotalOdometerBtn)
		{
			if(mVehicleManager != null)
			{
				String totalOdometer = mCalibrateOdometerEText.getText().toString();
				if(totalOdometer == null || totalOdometer.length() == 0)
				{
					Log.i(TAG, "OBD Connect status:"+mVehicleManager.getProperty(VehicleInterfaceProperties.VIM_CAN_DEVICE_CONNECT_STATUS));
				}
				else
				{
					mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MEASUREMENT_DISTANCE_PROPERTY,totalOdometer );
				}

			}
		}
		else if(v == mReqTimeInfo)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_DRVING_TIME_INFO));
			}
		}
		else if(v == mReqClearDataBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CAN_CMD_REQ_CLEAR_DATA));
			}
		}
	}
	
	private void onUpdateUi(int propId,String value)
	{
		if(mWidgetMap.containsKey(propId))
		{
			mWidgetMap.get(propId).setText(value);
		}
	}

}
