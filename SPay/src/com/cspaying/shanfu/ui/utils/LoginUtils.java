package com.cspaying.shanfu.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.PskKeyManager;
import cn.sharesdk.wechat.utils.i;

public class LoginUtils {

	/**
	 * 保存登录状态
	 * 
	 * @param context
	 * @param value
	 *            =1成功，value=2失败
	 * @return
	 */
	public static boolean setLoginFlag(Context context, int value) {
		boolean flag = false;
		if (context != null && value != 0) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			if(getRemenberpass(context) != 1)
			{
				setPass(context, "");
			}
			flag = sharedPreferences.edit().putInt("logginflag", value)
					.commit();
		}

		return flag;
	}

	/**
	 * 获取登录状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getLoginFlag(Context context) {
		boolean flag = false;
		if (context != null) {

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			int value = sharedPreferences.getInt("logginflag", 0);
			if (value == 1) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 保存登录账号
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setLoginName(Context context, String name) {
		boolean flag = false;
		if (context != null && name != null && !name.equals("")) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putString("logginname", name)
					.commit();
		}

		return flag;
	}

	/**
	 * 获取登录账号
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginName(Context context) {
		String flag = null;
		if (context != null) {

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.getString("logginname", "");
		}
		return flag;
	}

	/**
	 * 保存登录Token
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setLoginToken(Context context, String Token) {
		boolean flag = false;
		if (context != null && Token != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putString("logginToken", Token)
					.commit();
		}

		return flag;
	}

	/**
	 * 获取登录Cookie
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginToken(Context context) {
		String flag = null;
		if (context != null) {

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.getString("logginToken", "");
		}
		return flag;
	}

	/**
	 * 保存登录权限
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setLoginPemission(Context context, int permission) {
		boolean flag = false;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit()
					.putInt("logginpemission", permission).commit();
		}

		return flag;
	}

	/**
	 * 获取登录权限
	 * 
	 * @param context
	 * @return
	 */
	public static int getLoginPemission(Context context) {
		int flag = 0;
		if (context != null) {

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.getInt("logginpemission", 0);
		}
		return flag;
	}

	/**
	 * 保存商户号
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setMcId(Context context, String mcid) {
		boolean flag = false;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putString("logginmcid", mcid)
					.commit();
		}

		return flag;
	}

	/**
	 * 获取登录权限
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginMcid(Context context) {
		String flag = null;
		if (context != null) {

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.getString("logginmcid", "");
		}
		return flag;
	}

	/**
	 * 保存SignKey
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setSignKey(Context context, String sign) {
		boolean flag = false;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putString("SignKey", sign).commit();
		}
		return flag;
	}

	/**
	 * 获取SignKey
	 * 
	 * @param context
	 * @return
	 */
	public static String getSignKey(Context context) {
		String flag = null;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.getString("SignKey", "");
		}
		return flag;
	}
	
	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public static int getRemenberpass(Context context) {
		int ret = -1;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			ret = sharedPreferences.getInt("RemenberPass", -1);
		}
		return ret;
	}
	
	/**
	 * 保存是否记住密码
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setRemenberPass(Context context, int sign) {
		boolean flag = false;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putInt("RemenberPass", sign).commit();
		}
		return flag;
	}
	
	
	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public static String getpass(Context context) {
		String flag = "";
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.getString("Pass", "");
			
		}
		return flag;
	}
	
	/**
	 * 保存是否记住密码
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setPass(Context context, String pass) {
		boolean flag = false;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			String strHistoryPass = sharedPreferences.getString("Pass", "");
			if(strHistoryPass != null && strHistoryPass.equals(pass))
			{
				return true;
			}
			flag = sharedPreferences.edit().putString("Pass", pass).commit();
		}
		return flag;
	}
	
	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public static String getAccount(Context context) {
		String flag = "";
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.getString("Account", "");
			
		}
		return flag;
	}
	
	/**
	 * 保存是否记住密码
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setAccount(Context context, String account) {
		boolean flag = false;
		if (context != null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putString("Account", account).commit();
		}
		return flag;
	}
	
	/**
	 * 保存商户资料
	 * 
	 * @param context
	 * @return
	 */
	public static boolean setMerchantData(Context context, String merchdata,String loginName) {
		boolean flag = false;
		if(loginName == null)
		{
			loginName = getLoginMcid(context);
		}
		if (context != null && merchdata != null && !merchdata.equals("")
				&& loginName != null && !loginName.equals("")) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString("merchantDataId",loginName);
			editor.putString("merchantData", merchdata);
			flag = editor.commit();
		}

		return flag;
	}

	/**
	 * 获取本地商户资料
	 * 
	 * @param context
	 * @return
	 */
	public static String getMerchantData(Context context) {
		String flag = null;
		if (context != null) {

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"Loginshare", Context.MODE_PRIVATE);
			String merchDataId = sharedPreferences.getString("merchantDataId", "");
			if(merchDataId == null || merchDataId.length() == 0)
			{
				return null;
			}
			if(merchDataId.equals(getLoginMcid(context)))
			{
				return sharedPreferences.getString("merchantData", "");
			}
		}
		return null;
	}

}
