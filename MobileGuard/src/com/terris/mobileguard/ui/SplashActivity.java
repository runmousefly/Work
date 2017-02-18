package com.terris.mobileguard.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OptionalDataException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.MemoryHandler;

import org.json.JSONException;
import org.json.JSONObject;

import com.terris.mobileguard.R;
import com.terris.mobileguard.utils.StreamUtils;
import com.terris.mobileguard.utils.UIUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Matrix;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity
{
	private Context mContext  = null;
	private int mCurrentVersonNo = -1;
	
	private TextView tv_splash_version = null;
	private ProgressBar pb_splash_progress = null;
	
	private final static String TAG = "SplashActivity";
	
	public static final String KEY_ERRCODE 			= "ErrorCode";
	public static final String KEY_OLD_VERSION 	= "OldVersion";
	public static final String KEY_NEW_VERSION 	= "NewVersion";
	public static final String KEY_UPGRADE_URL 	= "UpgradeUrl";
	public static final String KEY_APP_DESC 			= "AppDesc";
	
	public static final int MSG_UPGRADE_APP 			= 0;
	public static final int MSG_LOAD_MAINMENU 		= 1;
	
	//ÏûÏ¢´¦ÀíÆ÷
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what)
			{
				case MSG_UPGRADE_APP:
					pb_splash_progress.setVisibility(View.GONE);
					int oldVersion = msg.getData().getInt(KEY_OLD_VERSION);
					int newVersion = msg.getData().getInt(KEY_NEW_VERSION);
					String appDesc = msg.getData().getString(KEY_APP_DESC);
					String downloadUrl = msg.getData().getString(KEY_UPGRADE_URL);
					AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
					builder.setTitle(getResources().getString(R.string.title_upgrade_dialog));
					builder.setMessage("µ±Ç°°æ±¾:"+oldVersion+",¿ÉÓÃÐÂ°æ±¾:"+newVersion+"\nÐÂ°æ±¾ÃèÊö:"+appDesc);
					builder.setNegativeButton("ÏÂ´ÎÔÙËµ",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
							startActivity(intent);
							finish();
						}
					});
					
					builder.setPositiveButton("Á¢¼´¸üÐÂ", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							
						}
					});
					builder.show();
					break;
				case MSG_LOAD_MAINMENU:
					pb_splash_progress.setVisibility(View.GONE);
					Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
					break;
				default:
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mContext = this.getApplicationContext();
		initView();
		updateView();
		checkAppVersion();
	}

	private void initView()
	{
		tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
		pb_splash_progress = (ProgressBar)findViewById(R.id.pb_splash_progress);
		pb_splash_progress.setVisibility(View.VISIBLE);
	}
	
	private void updateView()
	{
		if(tv_splash_version != null && mContext != null)
		{
			try
			{
				PackageManager pm = mContext.getPackageManager();  
		        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);  
		        mCurrentVersonNo = pi.versionCode;
		        tv_splash_version.append(pi.versionName);
			} catch (NameNotFoundException e)
			{
				// TODO: handle exception
				e.printStackTrace();
			} 
		}
	}
	
	//¼ì²âÐÂ°æ±¾
	private void checkAppVersion()
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				int errorCode = 0;
				long startTime = 0;
				try
				{
					startTime = SystemClock.uptimeMillis();
					URL url = new URL(getResources().getString(R.string.app_info_url));
					HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
					if(httpURLConnection != null)
					{
						httpURLConnection.setRequestMethod("GET");
						httpURLConnection.setConnectTimeout(5000);
						int retCode = httpURLConnection.getResponseCode();
						if(retCode == 200)
						{
							InputStream appInfoStream = httpURLConnection.getInputStream();
							String strAppInfoJson = StreamUtils.readStream(appInfoStream);
							if(TextUtils.isEmpty(strAppInfoJson))
							{
								Log.e(TAG,"get app info null from server!");
								errorCode = 1000;
							}
							else
							{
								JSONObject jsonObject = new JSONObject(strAppInfoJson);
								String downloadUrl = jsonObject.getString("downloadurl");
								int version = jsonObject.getInt("version");
								String desc = jsonObject.getString("desc");
								Log.i(TAG,"get server info,version:"+version+",downloadUrl:"+downloadUrl+",desc:"+desc);
								if(mCurrentVersonNo > 0 && version > mCurrentVersonNo)
								{
									Message msg = new Message();
									msg.what = MSG_UPGRADE_APP;
									Bundle bundle = new Bundle();
									bundle.putInt(KEY_OLD_VERSION, mCurrentVersonNo);
									bundle.putInt(KEY_NEW_VERSION, version);
									bundle.putString(KEY_UPGRADE_URL, downloadUrl);
									bundle.putString(KEY_APP_DESC, desc);
									msg.setData(bundle);
									long waitTime = 2000 - (SystemClock.uptimeMillis() - startTime);
									waitTime = Math.min(2000, Math.max(0, waitTime));
									mHandler.sendMessageDelayed(msg, waitTime);
									return;
								}
								else
								{
									errorCode = 0;
								}
							}
						}
						else
						{
							//·þÎñÆ÷·ÃÎÊÊ§°Ü
							errorCode = 1000;
							UIUtils.showToast(SplashActivity.this, "ÍøÂç´íÎó£¬´íÎóÂë:1000");
						}
					}
					
				} catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorCode = 1001;
					UIUtils.showToast(SplashActivity.this, "ÍøÂç´íÎó£¬´íÎóÂë:1001");
				} catch (NotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorCode = 1002;
					UIUtils.showToast(SplashActivity.this, "ÍøÂç´íÎó£¬´íÎóÂë:1002");
				}
				catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
					errorCode = 1003;
					UIUtils.showToast(SplashActivity.this, "ÍøÂç´íÎó£¬´íÎóÂë:1003");
				}
				catch(JSONException e){
					e.printStackTrace();
					errorCode = 1004;
					UIUtils.showToast(SplashActivity.this, "·þÎñÆ÷ÐÅÏ¢½âÎöÊ§°Ü£¬´íÎóÂë:1004");
				}
				if(errorCode >= 0)
				{
					Message msg = new Message();
					msg.what = MSG_LOAD_MAINMENU;
					Bundle bundle = new Bundle();
					bundle.putInt(KEY_ERRCODE, errorCode);
					msg.setData(bundle);
					long waitTime = 2000 - (SystemClock.uptimeMillis() - startTime);
					waitTime = Math.min(2000, Math.max(0, waitTime));
					mHandler.sendMessageDelayed(msg, waitTime);
				}
			}
		}).start();
	}
	
}