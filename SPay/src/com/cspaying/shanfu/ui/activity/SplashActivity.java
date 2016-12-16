package com.cspaying.shanfu.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cspaying.shanfu.R;
import com.cspaying.shanfu.ui.MyApplication;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_main);
		MyApplication.getInstance().addActivity(this);
	}
}
