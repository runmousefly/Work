package com.mct.vehicle.demo;

import java.io.FileWriter;

import com.mct.VehicleInterfaceDataHandler;
import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.vehicle.demo.CanActivity.VehicleDataNotification;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TPMSActivity extends Activity implements View.OnClickListener
{

	public final static String TAG = "TPMSDemo";
	
	public TPMSActivity gInstance = null;
	public Context mContext = null;
	public VehicleManager mVehicleManager = null;
	
	public Button mUpdateTire1Id = null;
	public EditText mTire1IdText = null;
	public Button mUpdateTire2Id = null;
	public EditText mTire2IdText = null;
	public Button mReadyBtn = null;
	public Button mReqTireDataBtn = null;
	public Button mReqAlarmParamBtn = null;
	
	TextView mLogText = null;
	Handler mHandler = null;
	
	public VehicleDataNotification mDataNotification = new VehicleDataNotification();
	
	int[] mTpmsProperties = new int[] { 
			VehicleInterfaceProperties.VIM_MCU_TPMS_DEVICE_STATE_PROPERTY, 
			VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_UNIT_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_UNIT_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_ALARM_RANGE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_ALARM_RANGE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_PRESSURE_ALARM_PARAM_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_TEMP_ALARM_PARAM_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_TIRE_DATA_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_DELTE_TIRE_ID_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_INIT_DATA_STATE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_AUTO_MATCH_CODE_STATE_PROPERTY,
			VehicleInterfaceProperties.VIM_MCU_TPMS_UPDATE_TIRE_CODE_PROPERTY
	};	
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tpms);
		
		gInstance = this;
		mContext = getApplicationContext();
		
		mVehicleManager = VehicleManager.getInstance();
		if(mVehicleManager == null)
		{
			Log.e(TAG, "get Vehcle Instace failed");
			return;
		}
		Log.i(TAG, "get Vehcle Instace success");
		
		mReadyBtn = (Button)this.findViewById(R.id.ready);
		mReadyBtn.setOnClickListener(this);
		
		mReqAlarmParamBtn = (Button)this.findViewById(R.id.reqAlarmParam);
		mReqAlarmParamBtn.setOnClickListener(this);
		
		mReqTireDataBtn = (Button)this.findViewById(R.id.reqTireData);
		mReqTireDataBtn.setOnClickListener(this);
		
		mUpdateTire1Id = (Button)this.findViewById(R.id.updateTireId1);
		mUpdateTire1Id.setOnClickListener(this);
		mTire1IdText = (EditText)this.findViewById(R.id.TireIdText1);
		
		mUpdateTire2Id = (Button)this.findViewById(R.id.updateTireId2);
		mUpdateTire2Id.setOnClickListener(this);
		mTire2IdText = (EditText)this.findViewById(R.id.TireIdText2);
		
		mLogText = (TextView)this.findViewById(R.id.logView);
		mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		if(mVehicleManager != null)
		{
			mVehicleManager.registerHandler(mTpmsProperties, mDataNotification);
		}
	}


	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		if(mVehicleManager != null)
		{
			mVehicleManager.removeHandler(mTpmsProperties, mDataNotification);
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View view)
	{
		// TODO Auto-generated method stub
		if(view == mReadyBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_TPMS_REQ_COMMAND_PROPERTY,  String.valueOf(VehiclePropertyConstants.TPMS_CMD_REQ_DEVICE_STATE));
			}
		}
		else if(view == mReqAlarmParamBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_TPMS_REQ_COMMAND_PROPERTY,  String.valueOf(VehiclePropertyConstants.TPMS_CMD_REQ_ALARM_PARAM_ALL));
			}
		}
		else if(view == mReqTireDataBtn)
		{
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_TPMS_REQ_COMMAND_PROPERTY,  String.valueOf(VehiclePropertyConstants.TPMS_CMD_REQ_TIRE_DATA_ALL));
			}
		}
		else if(view == mUpdateTire1Id && mTire1IdText != null)
		{
			String trid1IdText = mTire1IdText.getText().toString();
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_TPMS_UPDATE_TIRE_CODE_PROPERTY,  VehicleManager.intArrayToString(new int[]{0x01,0x112233}));
			}
		}
		else if(view == mUpdateTire2Id && mTire2IdText != null)
		{
			String tird2IdText = mTire2IdText.getText().toString();
			if(mVehicleManager != null)
			{
				mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_TPMS_UPDATE_TIRE_CODE_PROPERTY,  VehicleManager.intArrayToString(new int[]{0x01,0x556677}));
			}
		}
	}
	
	public class VehicleDataNotification implements VehicleInterfaceDataHandler
	{

		@Override
		public void onDataUpdate(int propId,String value)
		{
			// TODO Auto-generated method stub
			printUILog("propId:"+propId+",value:"+value);
		}

		@Override
		public void onError(boolean arg0)
		{
			// TODO Auto-generated method stub
			
		}
	}
}
