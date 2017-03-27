package com.mct.vehicle.demo;

import java.lang.annotation.Target;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mct.VehicleInterfaceDataHandler;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.common.CarModel;
import com.mct.utils.DBUtils;
import com.mct.vehicle.demo.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Matrix;
import android.media.MiniThumbFile;
import android.media.MediaFile.MediaFileType;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView.EGLWindowSurfaceFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CanActivity extends Activity implements View.OnClickListener,OnItemSelectedListener

{
	public static String TAG = "CanDemo";
	public VehicleManager mVehicleManager = null;
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();

	int[] mSupportedPropIds = null;
	int[] mWritablePropIds = null;
	int[] mPropDataType = null;
	int[] mCanProperties = new int[] { 
			VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY, 
			VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY,
			VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_COOLANT_TEMP_PROPERTY,
			VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY,
			VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY,
			VehicleInterfaceProperties.VIM_SEAT_BELT_STATUS_DRIVER_PROPERTY,
			VehicleInterfaceProperties.VIM_SEAT_BELT_STATUS_PSNGR_PROPERTY,
			VehicleInterfaceProperties.VIM_SEAT_BELT_STATUS_REAR_LEFT_PROPERTY,
			VehicleInterfaceProperties.VIM_SEAT_BELT_STATUS_REAR_RIGHT_PROPERTY,
			VehicleInterfaceProperties.VIM_SEAT_BELT_STATUS_REAR_CENTER_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_PSNGR_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_LEFT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_REAR_RIGHT_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_TRUNK_PROPERTY,
			VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_HOOD_PROPERTY,
			VehicleInterfaceProperties.VIM_WASHER_FLUID_LVL_PROPERTY,
			VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY,
			VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY,
			VehicleInterfaceProperties.VIM_HVAC_FAN_SPEED_PROPERTY
	};	
	
	private int mDriverSeatBeltStatus = 1;
	private int mDriverDoorOpenStatus = 1;
	private int mWashFuelLVL = 30;
	private int mParkingBrakesStatus = 1;

	public Button mStartBtn = null;
	public Button mStopBtn = null;
	public Button mReqACInfoBtn = null;
	public Button mReqVehicleInfoBtn = null;
	
	public Button mSyncMediaInfoBtn = null;
	public Button mSyncBtPhoneInfoBtn = null;
	public Button mSyncTimeBtn = null;
	public Button mReqCommonCmdBtn = null;

	public Spinner mCanBoxTypeSpinner = null;
	public Spinner mCarModelSpinner = null;
	public EditText mProtocolVersionText = null;
	public EditText mTotalOdometerText = null;
	public EditText mEngineSpeedText = null;
	public EditText mVehicleSpeedText = null;
	public EditText mLeftFuelText = null;
	public EditText mCoolantTempText = null;
	public EditText mBatteryVoltageText = null;
	
	public EditText mDriverSeatBeltStatusText = null;
	public EditText mDriverDoorOpenStatusText = null;
	
	public EditText mWashFuelLVLStatusText = null;
	public EditText mParkingBrakesStatusText = null;
	
	public EditText mMediaInfoText = null;
	public EditText mBtPhoneInfoText = null;
	public EditText mComonInfoText = null;
	
	TextView mLogText = null;
	
	private String[] mCanBoxTypeArray = new String[]{"睿智诚","尚摄","欣普"};
	private ArrayAdapter<String> mCanBoxTypeAdapter = null;
	
	private String[] mCarModelArray = new String[]{"大众途锐","丰田锐志",
			"日系/英菲尼迪QX50低配","日系/英菲尼迪QX50高配","日系其它"};
	private ArrayAdapter<String> mCarModelAdapter = null;
	
	public class VehicleDataNotification implements VehicleInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(int propId,String value)
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "[CAN]onDataUpdate,PropId:"+propId+",value:"+value);
			switch(propId)
			{
				case VehicleInterfaceProperties.VIM_ENGINE_SPEED_PROPERTY:
					mEngineSpeedText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_SPEEDO_METER_PROPERTY:
					mVehicleSpeedText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_ODOMETER_PROPERTY:
					mTotalOdometerText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_RMNG_FUEL_LVL_PROPERTY:
					mLeftFuelText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_COOLANT_TEMP_PROPERTY:
					mCoolantTempText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_BATTERY_VOLTAGE_PROPERTY:
					mBatteryVoltageText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_SEAT_BELT_STATUS_DRIVER_PROPERTY:
					mDriverSeatBeltStatusText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_DOOR_OPEN_STATUS_DRIVER_PROPERTY:
					mDriverDoorOpenStatusText.setText(value);
					 break;
				case VehicleInterfaceProperties.VIM_WASHER_FLUID_LVL_PROPERTY:
					mWashFuelLVLStatusText.setText(value);
					 break;
				case VehicleInterfaceProperties.VIM_PARKING_BRAKES_PROPERTY:
					mParkingBrakesStatusText.setText(value);
					 break;
				case VehicleInterfaceProperties.VIM_CAN_SW_VERSION_PROPERTY:
					mProtocolVersionText.setText(value);
					break;
				case VehicleInterfaceProperties.VIM_MCU_USER_KEY_PROPERTY:
					printUILog("User Key:"+value);
					break;
				default:
					Log.i(TAG,"propId:"+propId+",value:"+value);
					printUILog("propId:"+propId+",value:"+value);
					break;
			}
		}
		
		@Override
		public void onError(boolean bCleanUpAndRestart)
		{
			// TODO Auto-generated method stub
			Log.i(TAG, "onError,bCleanUpAndRestart:" + bCleanUpAndRestart);
		}

		

	}

	private void printUILog(String logText)
	{
		if(mLogText != null)
		{			
			mLogText.append("["+mLogText.getLineCount()+"] "+logText+"\n");
			int offset=mLogText.getLineCount()*mLogText.getLineHeight();
            if(offset>mLogText.getHeight())
            {
            	mLogText.scrollTo(0,offset-mLogText.getHeight());
            }
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
		setContentView(R.layout.activity_can);
		
		mStartBtn = (Button) findViewById(R.id.start);
		mStopBtn = (Button) findViewById(R.id.stop);	
		mReqACInfoBtn = (Button)findViewById(R.id.reqACInfo);
		mReqVehicleInfoBtn = (Button)findViewById(R.id.reqVehicleInfo);
		
		mSyncMediaInfoBtn = (Button)findViewById(R.id.syncMediaInfoBtn);
		mSyncBtPhoneInfoBtn = (Button)findViewById(R.id.syncBtPhoneInfoBtn);
		mSyncTimeBtn = (Button) findViewById(R.id.reqSyncTimeBtn);
		mReqCommonCmdBtn = (Button) findViewById(R.id.reqCommonBtn);
		
		mStartBtn.setOnClickListener(this);
		mStopBtn.setOnClickListener(this);
		mReqACInfoBtn.setOnClickListener(this);
		mReqVehicleInfoBtn.setOnClickListener(this);
		mSyncMediaInfoBtn.setOnClickListener(this);
		mSyncBtPhoneInfoBtn.setOnClickListener(this);
		mSyncTimeBtn.setOnClickListener(this);
		mReqCommonCmdBtn.setOnClickListener(this);
		
		 mCanBoxTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mCanBoxTypeArray);    
		 mCanBoxTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		 mCarModelAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mCarModelArray);    
		 mCarModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	        
		 mCanBoxTypeSpinner = (Spinner)findViewById(R.id.CanBoxTypeSpinner);
		 mCanBoxTypeSpinner.setAdapter(mCanBoxTypeAdapter);
		mCanBoxTypeSpinner.setOnItemSelectedListener(this);
			
		mCarModelSpinner  = (Spinner)findViewById(R.id.CarModelSpinner);
		mCarModelSpinner.setAdapter(mCarModelAdapter);
		mCarModelSpinner.setOnItemSelectedListener(this);
		
		mProtocolVersionText = (EditText)findViewById(R.id.ProtocolVersion);
		mTotalOdometerText = (EditText)findViewById(R.id.TotalOdometer);
		mVehicleSpeedText = (EditText)findViewById(R.id.VehicleSpeed);
		mEngineSpeedText = (EditText)findViewById(R.id.EngineSpeed);
		mLeftFuelText = (EditText)findViewById(R.id.LeftFuel);
		mCoolantTempText = (EditText)findViewById(R.id.CoolantTemp);
		mBatteryVoltageText = (EditText)findViewById(R.id.BatteryVoltage);
		
		mDriverSeatBeltStatusText = (EditText)findViewById(R.id.DriverSeatBelt);
		mDriverDoorOpenStatusText = (EditText)findViewById(R.id.DriverDoor);
		mWashFuelLVLStatusText = (EditText)findViewById(R.id.WashFuelLVI);
		mParkingBrakesStatusText = (EditText)findViewById(R.id.ParkingBrakes);
		
		mMediaInfoText = (EditText)findViewById(R.id.MediaInfoText);
		mBtPhoneInfoText = (EditText)findViewById(R.id.BtPhoneInfoText);
		mComonInfoText = (EditText)findViewById(R.id.CommonInfoText);
		
		mLogText = (TextView)this.findViewById(R.id.logView);
		mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());

		mVehicleManager = VehicleManager.getInstance();
		if(mVehicleManager == null)
		{
			Log.e(TAG, "get Vehcle Instace failed");
			return;
		}
		Log.i(TAG, "get Vehcle Instace success");
		mVehicleManager.registerHandler(mCanProperties, mDataNotification);
//		mSupportedPropIds = mVehicleManager.getSupportedPropertyIds();
//		mWritablePropIds = mVehicleManager.getWritablePropertyIds();
//		mPropDataType = mVehicleManager.getPropertiesDataType(mSupportedPropIds);
//		Log.i(TAG, "getSupportedPropIds:" + toString(mSupportedPropIds));
//		Log.i(TAG, "getWritablePropIds:" + toString(mWritablePropIds));
//		Log.i(TAG, "getPropDataType:" + toString(mPropDataType));
		
		//测试数据库调用
		//List<CarModel> carModels = DBUtils.querySupportFunctionByCarModel(this, 0);
		//Log.i(TAG, carModels.toString());
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
		if (mVehicleManager != null && mCanProperties != null && mCanProperties.length > 0)
		{
			mVehicleManager.removeHandler(mCanProperties, mDataNotification);
		}

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
		if(v == mStartBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_START));
			}
			
		}
		else if(v == mStopBtn)
		{
			if (mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_END));
			}
			
		}
		else if(v == mReqACInfoBtn)
		{			
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_AIR_CONDITION_INFO));
		}
		else if(v == mReqVehicleInfoBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_VEHICLE_INFO));
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_DOOR_INFO));
		}
		else if(v == mSyncBtPhoneInfoBtn)
		{
			int btConnStatus = 1;
			int callState = 1;
			String phoneNumber = mBtPhoneInfoText.getText().toString();
			String btPhoneParam = VehiclePropertyConstants.formatBtPhoneInfoString(btConnStatus, callState, phoneNumber);
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_SYNC_BT_PHONE_INFO_COLLECT_PROPERTY, btPhoneParam);
			Log.getStackTraceString(new Throwable());
		}
		else if(v == mSyncMediaInfoBtn)
		{
			int sourceType = Integer.valueOf(mMediaInfoText.getText().toString());
			int curTrack = 1;
			int totalTrack = 100;
			int currentPlayTime = 23000;
			int totalPlayTime = 25000;
			int playState = 0;
			String strID3 = VehicleManager.stringArrayToString(new String[]{"title:sky","artist:liudehua","album:1982"});
			int band = 0;
			int freq = 8750;
			String mediaInfoParam = VehiclePropertyConstants.formatSourceInfoString(sourceType, curTrack, totalTrack,currentPlayTime,totalPlayTime,playState,strID3,band,freq);
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_SYNC_SOURCE_INFO_COLLECT_PROPERTY, mediaInfoParam);
		}
		else if(v == mSyncTimeBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COMMAND_PROPERTY, String.valueOf(VehiclePropertyConstants.CANBOX_CMD_REQ_SYNC_SYS_TIME));
		}
		else if(v == mReqCommonCmdBtn)
		{
			if(mComonInfoText != null && mVehicleManager != null)
			{
				String cmd = mComonInfoText.getText().toString();
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_CAN_REQ_COUSTOM_COMMAND_PROPERTY, cmd);
			}
			
		}
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		 if(parent == mCarModelSpinner && mVehicleManager != null)
		{
			//更新CarModel
			int carModel = VehiclePropertyConstants.CAR_MODEL_VOLKSWAGEN_MAGOTAN;
			switch(position)
			{
				case 0:
					carModel = VehiclePropertyConstants.CAR_MODEL_VOLKSWAGEN_MAGOTAN;
					break;
				case 1:
					carModel = VehiclePropertyConstants.CAR_MODEL_TOYOTA_REIZ;
					break;
				case 2:
					carModel = VehiclePropertyConstants.CAR_MODEL_NISSAN_INIFINITI_QX50_L;
					break;
				case 3:
					carModel = VehiclePropertyConstants.CAR_MODEL_NISSAN_INIFINITI_QX50_H;
					break;
				case 4:
					carModel = VehiclePropertyConstants.CAR_MODEL_NISSAN_X_TRAIL;
					break;
				default:
					return;
			}
			int canBoxType = mCanBoxTypeSpinner.getSelectedItemPosition();
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_VEHICLE_MODEL_PROPERTY, VehicleManager.intArrayToString(new int[]{canBoxType,carModel}));
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		// TODO Auto-generated method stub
		
	}
}
