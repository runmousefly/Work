package com.mct.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.mct.common.MctCoreServicesProviderMetaData;

import java.util.Vector;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IInterface;
import android.util.Log;
import android.util.SparseArray;

public class ServiceHelper
{
	public static final String TAG = "ServiceHelper";
	public static final int WAIT_NOTIME = 0;
	public static final int WAIT_TICK = 1;
	public static final int WAIT_MINIMUM = 10;
	public static final int WAIT_MINIMUM2 = 50;
	public static final int WAIT_SHORT = 100;
	public static final int WAIT_ELAPSE = 300;
	public static final int WAIT_NORMAL = 1000;
	public static final int WAIT_MIDDLE = 3000;
	public static final int WAIT_LONG = 15000;
	public static final int WAIT_MAXIMUM = 30000;
	public static final int WAIT_INFINITE = -1;

	public static final int REPEAT_NONE = 0;
	public static final int REPEAT_ONCE = 1;
	public static final int REPEAT_NORMAL = 3;
	public static final int REPEAT_MIDDLE = 6;
	public static final int REPEAT_LONG = 10;
	public static final int REPEAT_MAXIMUM = 5;
	public static final int REPEAT_INFINITE = -1;

	public static final int DEFAULT_PACKAGE_LEN = 256;

	// 1MILE = 1.609344KM
	public static final float KM_PER_MILE = 1.6093f;

	// 1bar=1.02kg/cm2=102kpa=14.5PSI.
	public static final float BAR_PER_KPA = 0.0098f;
	public static final float BAR_PER_PSI = 0.0690f;

	public static final String BACK_LIGHT_CTRL_PATH = "/d/qpnp_pin/pm8916-mpp/4/master_en";

	private static final String DATABASE_DIR_PATH = "/data/data/com.mct.coreservices/databases/";

	public static final int POST_TYPE_NORMAL 					= 0;
	public static final int POST_TYPE_KEEP_QUEUE 			= 1;
	public static final int POST_TYPE_REPLACE_QUEUE 	= 2;
	
	public interface ServiceListener
	{
		public void onServiceStart(String serviceName);
	}

	public static boolean releaseService(Context context, BroadcastReceiver receiver)
	{
		try
		{
			context.unregisterReceiver(receiver);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public static <T> boolean addInterface(SparseArray<Vector<T>> mListeners, T listener, int event)
	{
		if (listener == null)
			return false;

		synchronized (mListeners)
		{
			removeInterface(mListeners, listener, event);
			Vector<T> ls = mListeners.get(event);
			if (ls == null)
			{
				ls = new Vector<T>();
				ls.add(listener);
				mListeners.put(event, ls);
			}
			else
			{
				if (!ls.contains(listener))
				{
					ls.add(listener);
				}

			}
		}

		return true;
	}

	public static <T> boolean removeInterface(SparseArray<Vector<T>> mListeners, T listener, int event)
	{
		if (listener == null)
			return false;

		synchronized (mListeners)
		{
			if (event == -1)
			{
				for (int i = 0; i < mListeners.size(); ++i)
				{
					Vector<T> mapHandler = (Vector<T>) mListeners.valueAt(i);
					for (int j = 0; j < mapHandler.size(); ++j)
					{
						if (mapHandler.get(j) == listener)
							mapHandler.remove(j--);
					}
				}
			}
			else
			{
				Vector<T> ls = (Vector<T>) mListeners.get(event);
				for (int j = 0; ls != null && j < ls.size(); ++j)
				{
					if (ls.get(j) == listener)
						ls.remove(j--);
				}
			}
		}

		return true;
	}

	public static <T extends IInterface> boolean addInterface(HashMap<String, Vector<T>> mListeners, T listener, String event)
	{
		if (listener == null || listener.asBinder() == null)
			return false;

		synchronized (mListeners)
		{
			removeInterface(mListeners, listener, event);
			Vector<T> ls = mListeners.get(event);
			if (ls == null)
			{
				ls = new Vector<T>();
				ls.add(listener);
				mListeners.put(event, ls);
			}
			else
			{
				ls.add(listener);
			}
		}

		return true;
	}

	public static <T extends IInterface> boolean removeInterface(HashMap<String, Vector<T>> mListeners, T listener, String event)
	{
		if (listener == null || listener.asBinder() == null)
			return false;

		synchronized (mListeners)
		{
			if (event == null)
			{
				Iterator<Entry<String, Vector<T>>> it = mListeners.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Vector<T>> entry = it.next();
					Vector<T> mapHandler = entry.getValue();
					for (int j = 0; j < mapHandler.size(); ++j)
					{
						if (mapHandler.get(j).asBinder().equals(listener.asBinder()))
							mapHandler.remove(j--);
					}
				}
			}
			else
			{
				Vector<T> ls = (Vector<T>) mListeners.get(event);
				for (int j = 0; ls != null && j < ls.size(); ++j)
				{
					if (ls.get(j).asBinder().equals(listener.asBinder()))
						ls.remove(j--);
				}
			}
		}

		return true;
	}

	public static boolean isServiceExist(Context context, String serviceName)
	{
		if (context == null) { return false; }
		Intent i = new Intent(serviceName);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfoList = pm.queryIntentServices(i, 0);
		if (resolveInfoList == null || resolveInfoList.size() == 0) { return false; }
		return true;
	}

	public static ComponentName getTopComponentName(Context context)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RecentTaskInfo> lsInfos = am.getRecentTasks(1, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
		ComponentName cn = (lsInfos == null || lsInfos.size() == 0 || lsInfos.get(0).baseIntent == null) ? null : lsInfos.get(0).baseIntent.getComponent();
		return cn;
	}

	public static String getCurrentTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());// 获取当前时间
		String strTime = formatter.format(date);
		return strTime;
	}

	public static String toTime(int year, int month, int day, int hour, int minute, int second)
	{
		return "" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}

	public static byte getByteFromInt(int val)
	{
		return (byte) (val & 0xFF);
	}

	public static int getIntFromBytes(byte[] val)
	{
		if (val.length != 4) { return -1; }
		int value = 0;
		for (int i = 0; i < 4; i++)
		{
			int shift = i * 8;
			value += (val[i] & 0x000000FF) << shift;
		}
		return value;
	}

	public static int getIntFromByte(byte val)
	{
		return (val & 0xFF);
	}

	public static int MAKELONG(int l, int h)
	{
		return ((l & 0xFFFF) | (h << 16));
	}

	public static int LOWORD(int l)
	{
		return (l & 0xFFFF);
	}

	public static int HIWORD(int l)
	{
		return ((l >> 16) & 0xFFFF);
	}

	public static int MAKEWORD(int l, int h)
	{
		return (((l & 0xFF) | (h << 8)) & 0xFFFF);
	}

	public static int LOBYTE(int w)
	{
		return (w & 0xFF);
	}

	public static int HIBYTE(int w)
	{
		return ((w >> 8) & 0xFF);
	}

	public static int getBit(int val, int pos)
	{
		return ((val >> pos) & 0x01);
	}

	public static int setBit(int val, int pos, boolean set)
	{
		return set ? (val | (0x01 << pos)) : (val & ~(0x01 << pos));
	}

	private static final int[] BITS_MASK = new int[] { 0x0, 0x1, 0x3, 0x7, 0xF, 0x1F, 0x3F, 0x7F, 0xFF, 0x1FF, 0x3FF, 0x7FF, 0xFFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF, 0x1FFFF, 0x3FFFF, 0x7FFFF, 0xFFFFF, 0x1FFFFF, 0x3FFFFF, 0x7FFFFF, 0xFFFFFF, 0x1FFFFFF, 0x3FFFFFF, 0x7FFFFFF, 0xFFFFFFF, 0x1FFFFFFF, 0x3FFFFFFF, 0x7FFFFFFF, 0xFFFFFFFF };

	public static int getBits(int val, int pos, int n)
	{
		return ((val >> pos) & BITS_MASK[n]);
	}

	public static int setBits(int val, int pos, int n, int set)
	{
		return (val & ~(getBits(val, pos, n) << pos)) | ((set & BITS_MASK[n]) << pos);
	}

	public static int arraycompare(int[] src, int srcPos, int[] dst, int dstPos, int length)
	{
		if (src == null && dst == null) { return 0; }
		if (src == null || dst == null) { return -1; }
		for (int i = 0; i < length; ++i)
		{
			if (src[srcPos + i] > dst[dstPos + i])
				return 1;
			else if (src[srcPos + i] < dst[dstPos + i])
				return -1;
		}
		return 0;
	}

	public static int arraycompare(byte[] src, int srcPos, byte[] dst, int dstPos, int length)
	{
		if (src == null && dst == null) { return 0; }
		if (src == null || dst == null) { return -1; }
		for (int i = 0; i < length; ++i)
		{
			if (src[srcPos + i] > dst[dstPos + i])
				return 1;
			else if (src[srcPos + i] < dst[dstPos + i])
				return -1;
		}
		return 0;
	}

	public static boolean arrayCopy(int[] src, int srcPos, int[] dst, int dstPos, int length)
	{
		if (src == null || dst == null || (src.length <= srcPos + length) || (dst.length <= dstPos + length)) { return false; }
		for (int i = 0; i < length; i++)
		{
			dst[dstPos + i] = src[srcPos + i];
		}
		return true;
	}

	public static boolean arrayCopy(byte[] src, int srcPos, byte[] dst, int dstPos, int length)
	{
		if (src == null || dst == null || (src.length <= srcPos + length) || (dst.length <= dstPos + length)) { return false; }
		for (int i = 0; i < length; i++)
		{
			dst[dstPos + i] = src[srcPos + i];
		}
		return true;
	}

	public static String toString(byte[] byData)
	{
		return toString(byData, 0, byData.length);
	}

	public static String toString(byte[] byData, int pos, int length)
	{
		length = (byData != null && (length < 0 || length > byData.length)) ? byData.length : length;
		if (byData == null || pos < 0 || pos >= length || length == 0)
			return "null";
		String str = new String();
		for (int i = pos; i < length; ++i)
			str += "0x" + Integer.toHexString(getIntFromByte(byData[i])) + ", ";
		return str;
	}

	public static String toString(int[] byData)
	{
		return toString(byData, 0, byData.length);
	}

	public static String toString(int[] byData, int pos, int length)
	{
		length = (byData != null && (length < 0 || length > byData.length)) ? byData.length : length;
		if (byData == null || pos < 0 || pos >= length || length == 0)
			return "null";
		String str = new String();
		for (int i = pos; i < length; ++i)
			str += "0x" + Integer.toHexString(byData[i]) + ", ";
		return str;
	}

	public static String toString(List<Byte> byData)
	{
		return toString(byData, 0, byData.size());
	}

	public static String toString(List<Byte> byData, int pos, int length)
	{
		length = (byData != null && (length < 0 || length > byData.size())) ? byData.size() : length;
		if (byData == null || pos < 0 || pos >= length || length == 0)
			return "null";
		String str = new String();
		for (int i = pos; i < length; ++i)
			str += "0x" + Integer.toHexString(getIntFromByte(byData.get(i))) + ", ";
		return str;
	}

	public static String toString(ByteBuffer byData)
	{
		int len = byData.capacity();
		if (byData == null || len == 0) { return null; }
		String strResult = new String();
		for (int i = 0; i < len; i++)
		{
			byte by = byData.get(i);
			char ch = (char) by;
			strResult += ch;
		}
		return strResult;
	}

	public static String toHexString(ByteBuffer byData)
	{
		return toString(byData, 0, byData.limit());
	}

	public static String toString(ByteBuffer byData, int pos, int length)
	{
		length = (byData != null && (length < 0 || length > byData.limit())) ? byData.limit() : length;
		if (byData == null || pos < 0 || pos >= length || length == 0)
			return "null";
		String str = new String();
		for (int i = pos; i < length; ++i)
			str += "0x" + Integer.toHexString(getIntFromByte(byData.get(i))) + ", ";
		return str;
	}

	public static String toString(byte[] param, int pos, int length, String charsetName)
	{
		length = (param != null && (length < 0 || length > param.length)) ? param.length : length;
		if (param == null || pos < 0 || pos >= length || length == 0)
			return null;
		try
		{
			int start = pos;
			int end = pos + length;
			for (int i = start; i < end; ++i)
			{
				if (param[i] == 0)
					return new String(param, start, i - start, charsetName);
			}

			return new String(param, start, end - start, charsetName);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String toString(int[] param, int pos, int length, String charsetName)
	{
		if (param == null || param.length == 0 || pos < 0 || length <= 0 || pos + length > param.length) { return null; }
		byte[] byParam = new byte[length];
		for (int i = 0; i < length; i++)
		{
			byParam[i] = getByteFromInt(param[i + pos]);
		}
		return toString(byParam, 0, length, charsetName);
	}

	public static String byteToBit(byte b)
	{
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	public static byte[] intArrayToByteArray(int[] intArray)
	{
		if (intArray == null || intArray.length == 0) { return null; }
		byte[] byteArray = new byte[intArray.length];
		for (int i = 0; i < intArray.length; i++)
		{
			byteArray[i] = (byte) (intArray[i] & 0xFF);
		}
		return byteArray;
	}

	public static int[] byteArrayToIntArray(byte[] byteArray)
	{
		if (byteArray == null || byteArray.length == 0) { return null; }
		int[] intArray = new int[byteArray.length];
		for (int i = 0; i < byteArray.length; i++)
		{
			intArray[i] = (byteArray[i] & 0xFF);
		}
		return intArray;
	}

	public static boolean compareIntArray(int[] first, int[] second)
	{
		if (first == null || second == null) { return false; }
		if (first.length != second.length) { return false; }
		for (int i = 0; i < first.length; i++)
		{
			if (first[i] != second[i]) { return false; }
		}
		return true;
	}

	public static boolean compareIntArray(int[] first, int[] second,int length)
	{
		if (first == null || second == null) { return false; }
		if (first.length >= length && second.length >= length) 
		{ 
			for (int i = 0; i < length; i++)
			{
				if (first[i] != second[i]) { return false; }
			}
			return true;
		}
		else if(first.length == second.length)
		{
			for (int i = 0; i < first.length; i++)
			{
				if (first[i] != second[i]) { return false; }
			}
			return true;
		}
		return false;
	}
	
	public static int[] combineArray(int[] first, int[] second)
	{
		if (first == null || first.length == 0)
		{
			return second;
		}
		else if (second == null || second.length == 0) { return first; }
		int[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static String readDeviceNode(String path)
	{
		String retValue = null;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			retValue = reader.readLine();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return retValue;
	}

	public static boolean writeDeviceNode(String path, String value)
	{
		try
		{
			BufferedWriter bufWriter = null;
			bufWriter = new BufferedWriter(new FileWriter(path));
			bufWriter.write(value);
			bufWriter.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean enableMirrorScreen(boolean enable)
	{
		if (enable) { return writeDeviceNode(BACK_LIGHT_CTRL_PATH, "1"); }
		return writeDeviceNode(BACK_LIGHT_CTRL_PATH, "0");
	}

	private static String COUSTOM_ARRAY_HEAD = "{";
	private static String COUSTOM_ARRAY_TAIL = "}";
	private static String COUSTOM_ARRAY_SEP = ",";
	private static String COUSTOM_ARRAY_SEP2 = ";";

	public static String intArrayToString(int[] array)
	{
		if (array == null || array.length == 0) { return null; }
		String ret = COUSTOM_ARRAY_HEAD;
		for (int i = 0; i < array.length; i++)
		{
			if (i == 0)
			{
				ret += array[i];
			}
			else
			{
				ret += (COUSTOM_ARRAY_SEP + array[i]);
			}

		}
		ret += COUSTOM_ARRAY_TAIL;
		return ret;
	}

	public static int[] stringToIntArray(String value)
	{
		int[] retArray = null;
		if (value == null || value.length() < 2 || !value.startsWith(COUSTOM_ARRAY_HEAD) || !value.endsWith(COUSTOM_ARRAY_TAIL)) { return null; }
		try
		{
			String validString = value.substring(1, value.length() - 1);
			String[] subValues = validString.split(COUSTOM_ARRAY_SEP);
			retArray = new int[subValues.length];
			for (int i = 0; i < subValues.length; i++)
			{
				retArray[i] = Integer.valueOf(subValues[i]);
			}
			return retArray;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static String floatArrayToString(float[] array)
	{
		if (array == null || array.length == 0) { return null; }
		String ret = COUSTOM_ARRAY_HEAD;
		for (int i = 0; i < array.length; i++)
		{
			if (i == 0)
			{
				ret += array[i];
			}
			else
			{
				ret += (COUSTOM_ARRAY_SEP + array[i]);
			}

		}
		ret += COUSTOM_ARRAY_TAIL;
		return ret;
	}
	
	public static String floatArrayToString(float[] array,int pointNumber)
	{
		try
		{
			String pointStr= "%."+pointNumber+"f";
			if (array == null || array.length == 0) { return null; }
			String ret = COUSTOM_ARRAY_HEAD;
			for (int i = 0; i < array.length; i++)
			{
				if (i == 0)
				{
					ret += String.format(pointStr, array[i]);
				}
				else
				{
					ret += (COUSTOM_ARRAY_SEP + String.format(pointStr, array[i]));
				}

			}
			ret += COUSTOM_ARRAY_TAIL;
			return ret;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			return floatArrayToString(array);
		}
	}

	public static float[] stringToFloatArray(String value)
	{
		float[] retArray = null;
		if (value == null || value.length() < 2 || !value.startsWith(COUSTOM_ARRAY_HEAD) || !value.endsWith(COUSTOM_ARRAY_TAIL)) { return null; }
		try
		{
			String validString = value.substring(1, value.length() - 1);
			String[] subValues = validString.split(COUSTOM_ARRAY_SEP);
			retArray = new float[subValues.length];
			for (int i = 0; i < subValues.length; i++)
			{
				retArray[i] = Float.valueOf(subValues[i]);
			}
			return retArray;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static String stringArrayToString(String[] array)
	{
		if (array == null || array.length == 0) { return null; }
		String ret = COUSTOM_ARRAY_HEAD;
		for (int i = 0; i < array.length; i++)
		{
			if (i == 0)
			{
				ret += array[i];
			}
			else
			{
				ret += (COUSTOM_ARRAY_SEP2 + array[i]);
			}

		}
		ret += COUSTOM_ARRAY_TAIL;
		return ret;
	}

	public static String[] stringToStringArray(String value)
	{
		if (value == null || value.length() < 2 || !value.startsWith(COUSTOM_ARRAY_HEAD) || !value.endsWith(COUSTOM_ARRAY_TAIL)) { return null; }
		try
		{
			String validString = value.substring(1, value.length() - 1);
			String[] subValues = validString.split(COUSTOM_ARRAY_SEP2,-1);
			return subValues;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * 返回当前程序版本名 
	 */  
	public static String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {   
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;  
	        if (versionName == null || versionName.length() <= 0) 
	        {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return versionName;  
	}  
	
	public static void execCommand(String command, boolean bWait) throws IOException
	{
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(command); // 这句话就是shell与高级语言间的调用
		// 实际上这样执行时启动了一个子进程,它没有父进程的控制台
		// 也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
		InputStream inputstream = proc.getInputStream();
		InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
		BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
		// read the ls output
		String line = "";
		StringBuilder sb = new StringBuilder(line);
		while ((line = bufferedreader.readLine()) != null)
		{
			sb.append(line);
			sb.append('\n');
		}
		// 使用exec执行不会等执行成功以后才返回,它会立即返回
		// 所以在某些情况下是很要命的(比如复制文件的时候)
		// 使用wairFor()可以等待命令执行完成以后才返回
		if (bWait)
		{
			try
			{
				if (proc.waitFor() != 0)
				{
					Log.i("ServiceHelper", "exit value = " + proc.exitValue());
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static int[] switchBigToLittleEndial(int[] data)
	{
		if (data == null || data.length == 0) { return null; }
		for (int i = 0; i < data.length; i++)
		{
			data[i] = (Integer.reverse(data[i]) >> 24) & 0xFF;
		}
		return data;
	}

	public static int switchBigToLittleEndial(int data)
	{
		return (Integer.reverse(data) >> 24) & 0xFF;
	}

	public static byte switchBigToLittleEndial(byte data)
	{
		return (byte) ((Integer.reverse(data) >> 24) & 0xFF);
	}

	public static int stringToIntSafe(String data)
	{
		try
		{
			if(data == null || data.length() == 0)
			{
				return -1;
			}
			return Integer.valueOf(data);
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}

	// 补码转原码
	public static int complementCodeToTrueCode(int param)
	{
		if (param >= 0xF0) { return -(~((param - 1) & 0xFF) & 0xFF); }
		return param;
	}

	// 原码转补码
	// public static int trueCodeToComplementCode(int param)
	// {
	// if(param >= 0)
	// {
	// return param;
	// }
	// return ((~(param&0xFF)) & 0x7F+1);
	// }

	public static int[] stringToUnicodeBytes(String strData, boolean bBigEndial)
	{
		if (strData == null || strData.length() == 0) { return null; }
		int[] unicodeParam = new int[strData.length() * 2];
		for (int i = 0; i < strData.length(); i++)
		{
			String hex = Integer.toHexString(strData.charAt(i));
			// 保证每个hex都是4位
			while (hex.length() < 4)
			{
				hex = "0" + hex;
			}
			if (bBigEndial)
			{
				unicodeParam[i * 2] = Integer.valueOf(hex.substring(2, 3), 16) * 16 + Integer.valueOf(hex.substring(3, 4), 16);
				unicodeParam[i * 2 + 1] = Integer.valueOf(hex.substring(0, 1), 16) * 16 + Integer.valueOf(hex.substring(1, 2), 16);
			}
			else
			{
				unicodeParam[i * 2] = Integer.valueOf(hex.substring(0, 1), 16) * 16 + Integer.valueOf(hex.substring(1, 2), 16);
				unicodeParam[i * 2 + 1] = Integer.valueOf(hex.substring(2, 3), 16) * 16 + Integer.valueOf(hex.substring(3, 4), 16);
			}

		}
		return unicodeParam;
	}

	// 初始化拷贝数据库文件
	public static boolean initDataBaseFile(Context context)
	{
		if (context == null) { return false; }
		File dbFile = new File(DATABASE_DIR_PATH + MctCoreServicesProviderMetaData.DB_NAME);
		if (!dbFile.exists())
		{
			File dbDir = new File(DATABASE_DIR_PATH);
			if (!dbDir.exists())
			{
				if (!dbDir.mkdir())
				{
					Log.e(TAG, "create database dir failed!");
					return false;
				}
			}
			try
			{
				// 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
				InputStream is = context.getAssets().open(MctCoreServicesProviderMetaData.DB_NAME);
				// 输出流
				OutputStream os = new FileOutputStream(DATABASE_DIR_PATH + MctCoreServicesProviderMetaData.DB_NAME);
				// 文件写入
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				// 关闭文件流
				os.flush();
				os.close();
				is.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	// 更新数据库文件
	public static boolean updateDataBaseFile(Context context)
	{
		if (context == null) { return false; }
		File dbFile = new File(DATABASE_DIR_PATH + MctCoreServicesProviderMetaData.DB_NAME);
		if (dbFile.exists())
		{
			if (!dbFile.delete())
			{
				Log.i(TAG, "delete old database file failed!");
			}
			File journalDbFile = new File(DATABASE_DIR_PATH + MctCoreServicesProviderMetaData.DB_NAME + "-journal");
			if (journalDbFile.exists())
			{
				journalDbFile.delete();
			}
			try
			{
				// 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
				InputStream is = context.getAssets().open(MctCoreServicesProviderMetaData.DB_NAME);
				// 输出流
				OutputStream os = new FileOutputStream(DATABASE_DIR_PATH + MctCoreServicesProviderMetaData.DB_NAME);
				// 文件写入
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				// 关闭文件流
				os.flush();
				os.close();
				is.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public static boolean writeToLocal(String path, String data)
	{
		try
		{
			if (path == null || path.length() == 0) { return false; }
			File file = new File(path);
			if (!file.exists())
			{
				if (!file.createNewFile())
				{
					Log.e(TAG, "create new file failed:" + path);
					return false;
				}
			}
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(data.getBytes("UTF-8"));
			outputStream.close();
			return true;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public static String readFromLocal(String path)
	{
		try
		{
			if (path == null || path.length() == 0) { return null; }
			File file = new File(path);
			if (file.exists() && file.canRead())
			{
				FileInputStream inputStream = new FileInputStream(file);
				byte[] buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				inputStream.close();
				String ret = new String(buffer, "UTF-8");
				return ret;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
