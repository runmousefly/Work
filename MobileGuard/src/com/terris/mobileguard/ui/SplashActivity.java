package com.terris.mobileguard.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OptionalDataException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.MemoryHandler;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.terris.mobileguard.R;
import com.terris.mobileguard.utils.StreamUtils;
import com.terris.mobileguard.utils.UIUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Matrix;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.storage.StorageManager;
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
	private TextView tv_download_progress = null;
	
	private final static String TAG = "SplashActivity";
	
	public static final String KEY_ERRCODE 			= "ErrorCode";
	public static final String KEY_OLD_VERSION 	= "OldVersion";
	public static final String KEY_NEW_VERSION 	= "NewVersion";
	public static final String KEY_UPGRADE_URL 	= "UpgradeUrl";
	public static final String KEY_APP_DESC 			= "AppDesc";
	
	public static final int MSG_UPGRADE_APP 			= 0;
	public static final int MSG_LOAD_MAINMENU 		= 1;
	
	//消息处理器
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what)
			{
				case MSG_UPGRADE_APP:
					pb_splash_progress.setVisibility(View.GONE);
					int oldVersion = msg.getData().getInt(KEY_OLD_VERSION);
					int newVersion = msg.getData().getInt(KEY_NEW_VERSION);
					String appDesc = msg.getData().getString(KEY_APP_DESC);
					final String downloadUrl = msg.getData().getString(KEY_UPGRADE_URL);
					AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
					builder.setTitle(getResources().getString(R.string.title_upgrade_dialog));
					builder.setMessage("当前版本:"+oldVersion+",可用新版本:"+newVersion+"\n新版本描述:"+appDesc);
					//builder.setCancelable(false);//只能点击按钮取消，返回键无法取消
					builder.setOnCancelListener(new DialogInterface.OnCancelListener()
					{
						
						@Override
						public void onCancel(DialogInterface dialog)
						{
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what = MSG_LOAD_MAINMENU;
							Bundle bundle = new Bundle();
							bundle.putInt(KEY_ERRCODE, 0);
							msg.setData(bundle);
							mHandler.sendMessage(msg);
						}
					});
					builder.setNegativeButton("下次再说",new DialogInterface.OnClickListener()
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
					
					builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							upgradeApp(downloadUrl);
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
		tv_download_progress = (TextView)findViewById(R.id.tv_download_progress);
		tv_download_progress.setVisibility(View.INVISIBLE);
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
	
	//检测新版本
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
							//服务器访问失败
							errorCode = 1000;
							UIUtils.showToast(SplashActivity.this, "网络错误，错误码:1000");
						}
					}
					
				} catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorCode = 1001;
					UIUtils.showToast(SplashActivity.this, "网络错误，错误码:1001");
				} catch (NotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorCode = 1002;
					UIUtils.showToast(SplashActivity.this, "网络错误，错误码:1002");
				}
				catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
					errorCode = 1003;
					UIUtils.showToast(SplashActivity.this, "网络错误，错误码:1003");
				}
				catch(JSONException e){
					e.printStackTrace();
					errorCode = 1004;
					UIUtils.showToast(SplashActivity.this, "服务器信息解析失败，错误码:1004");
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
	
	
	private void upgradeApp(String url)
	{
		final String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobileguard_temp.apk";
		HttpUtils httpUtils = new HttpUtils();
		HttpHandler handler = httpUtils.download(url,downloadPath, new RequestCallBack<File>()
		{
			
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo)
			{
				// TODO Auto-generated megetApplicationContext()
				installApk(downloadPath);
			}
			
			@Override
			public void onFailure(HttpException httpException, String arg1)
			{
				// TODO Auto-generated method stub
				UIUtils.showToast(SplashActivity.this, "更新失败，错误码:"+1005);
				Message msg = new Message();
				msg.what = MSG_LOAD_MAINMENU;
				Bundle bundle = new Bundle();
				bundle.putInt(KEY_ERRCODE, 1005);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				finish();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading)
			{
				// TODO Auto-generated method stub
				Log.i(TAG,"download,progress:"+String.format("%.3f", current*1.0/total));
				tv_download_progress.setVisibility(View.VISIBLE);
				tv_download_progress.setText(current+"/"+total);
			}
		});
	}
	
	public boolean installApk(String strFilePath)
	{
		File file = new File(strFilePath);
		if (file.exists())
		{
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			startActivity(intent);
			finish();
			return true;
		}
		// install_fail_file_not_exist
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}