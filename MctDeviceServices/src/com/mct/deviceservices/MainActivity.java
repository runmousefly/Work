package com.mct.deviceservices;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity

{
	public static String TAG = "CoreServices-MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
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

	}

}
