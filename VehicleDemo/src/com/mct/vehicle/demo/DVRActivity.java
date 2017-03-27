package com.mct.vehicle.demo;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DVRActivity extends Activity implements View.OnClickListener
{
	public final static String TAG = "DVRDemo";
	
	public DVRActivity gInstance = null;
	public Context mContext = null;
	public VehicleManager mVehicleManager = null;
	
	public Button mDVRTypeBtn = null;
	public EditText mDVRTypeText = null;
	public Button mUpBtn = null;
	public Button mDownBtn = null;
	public Button mLeftBtn = null;
	public Button mRightBtn = null;
	public Button mOkBtn = null;
	public Button mCancelBtn = null;
	
	TextView mLogText = null;
	Handler mHandler = null;
	
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
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dvr);
		gInstance = this;
		mContext = getApplicationContext();
		
		mVehicleManager = VehicleManager.getInstance();
		if(mVehicleManager == null)
		{
			Log.e(TAG, "get Vehcle Instace failed");
			return;
		}
		Log.i(TAG, "get Vehcle Instace success");
		
		mDVRTypeBtn = (Button)this.findViewById(R.id.dvrTypeBtn);
		mDVRTypeBtn.setOnClickListener(this);
		
		mDVRTypeText = (EditText)this.findViewById(R.id.dvrTypeText);

		mUpBtn = (Button)this.findViewById(R.id.upBtn);
		mUpBtn.setOnClickListener(this);
		
		mDownBtn = (Button)this.findViewById(R.id.downBtn);
		mDownBtn.setOnClickListener(this);
		
		mLeftBtn = (Button)this.findViewById(R.id.leftBtn);
		mLeftBtn.setOnClickListener(this);
		
		mRightBtn = (Button)this.findViewById(R.id.rightBtn);
		mRightBtn.setOnClickListener(this);
		
		mOkBtn = (Button)this.findViewById(R.id.okBtn);
		mOkBtn.setOnClickListener(this);
		
		mCancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		mCancelBtn.setOnClickListener(this);

		mLogText = (TextView)this.findViewById(R.id.logView);
		mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) { return true; }
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		if(mVehicleManager == null)
		{
			return;
		}
		if(v == mDVRTypeBtn && mDVRTypeText != null)
		{
			String strType = mDVRTypeText.getText().toString();
			try
			{
				if(strType != null && Integer.valueOf(strType) >= 0 && Integer.valueOf(strType) < 4)
				{
					mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_DVR_TYPE_PROPERTYE, strType);
				}
			} catch (Exception e)
			{
				// TODO: handle exceptione
				e.printStackTrace();
			}
		}
		if(v == mUpBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE, String.valueOf(VehiclePropertyConstants.USER_KEY_UP));
		}
		else if(v == mDownBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE, String.valueOf(VehiclePropertyConstants.USER_KEY_DOWN));
    	}
		else if(v == mLeftBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE, String.valueOf(VehiclePropertyConstants.USER_KEY_LEFT));
		}
		else if(v == mRightBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE, String.valueOf(VehiclePropertyConstants.USER_KEY_RIGHT));
		}
		else if(v == mOkBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE, String.valueOf(VehiclePropertyConstants.USER_KEY_OK));
		}
		else if(v == mCancelBtn)
		{
			mVehicleManager.setProperty(VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE, String.valueOf(VehiclePropertyConstants.USER_KEY_EXIT));
		}
	}
}
