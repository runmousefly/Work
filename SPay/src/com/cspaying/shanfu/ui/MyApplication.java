package com.cspaying.shanfu.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;


public class MyApplication extends Application {
	private static Context mContext;
	private static MyApplication mApplication;
	public static ExecutorService threadPool;
	public static Handler mHandler = new Handler(Looper.getMainLooper());
	private static String imei,strVersion;
	private List<Activity> mList = new LinkedList<Activity>();

	public static MyApplication getInstance() {
		if (null == mApplication) {
			mApplication = new MyApplication();
		}
		return mApplication; 
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		mContext = getApplicationContext();
		/** 反汇编难度不大. 写在C层...依然不是好的解决方案 反编译之后可以看到加密密钥 有密钥后 加密形同虚设 */
		// 解决方案:
		// ① .体积较小的数据直接使用非对称加密,客户端握有公钥用于加密,服务器握有私钥用于解密;只要服务器安全,数据便安全
		// ②.体积较大的数据;客户端生成随机密钥(对称加密),用随机密钥加密信息,
		// 用非对称加密手段加密随机生成的密钥, 将随机密钥和加密内容一起发送给服务器,
		// 由于客户端只拥有非对称加密的公钥,无法解密,服务器持有私钥
		// InitKeysData.initSignificantData();
		threadPool = Executors.newSingleThreadExecutor();
		imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();
		
		HashMap<String, String> hashMap = null;
		try {
			strVersion = getPackageManager()
					.getPackageInfo("com.cspaying.shanfu", 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * add Activity
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		   if (activity != null) {
			   mList.add(activity);
		     }
			
	  }
	
	
	/**
	 * 退出所有Activity
	 */
	public void exit() {

		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	/**
	 * @return 返回全局Context
	 */
	public static Context getContext() {
		return mContext;
	}

	public static String getImei() {
		return imei;
	}

	public static String getStrVersion() {
		return strVersion;
	}
	
}
