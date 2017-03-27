/*
 * Copyright (C) 2010 mAPPn.Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mct.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mct.utils.ServiceHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

/**
 * MctCoreServices Content Provider
 * 
 * @author terris
 * @date 2016-12-2
 * @since Version 0.1
 */
public class MctCoreServicesProvider extends ContentProvider
{

	private static final String TAG = "MctCoreServicesProvider";
	
	/** The database that lies underneath this content provider */
	private DatabaseHelper mOpenHelper = null;
	
	/** Current database version
	 * 	  V14	 增加明锐和17迈腾车型
	 * 	  V16 17迈腾车型增加空调控制
	 *   V17  增加通用昂科威
	 *	  V19 增加广汽传祺系列
	 *	  V20 增加标志车系
	 *   V21 增加福特车型
	 *   V22 增加福特SYNC字段
	 *   V23 增加吉利、中华车系
	 *   V24 增加启辰T70&T90
	 *   V25 去掉日产英菲尼迪QX50原车配置,待后续支持后再加入
	 *  */
	public static final int DB_VERSION = 25;//必须>=1
    
	/** domain */
	private static final String DB_HOST_NAME = "com.mct.coreservices";
	
	/** MIME type for the entire list */
	private static final String LIST_TYPE = "vnd.android.cursor.dir/";

	/** MIME type for an individual item */
	private static final String ITEM_TYPE = "vnd.android.cursor.item/";

	/** URI matcher used to recognize URIs sent by applications */
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	/** URI matcher constant for the URI of an search_history */
	private static final int URI_INDEX_CAN_VEHICLE_INFO 		= 1;
	private static final int URI_INDEX_APP_CONFIG 					= 2;
	private static final int URI_INDEX_CAN_VEHICLE_SETTING 	= 3;
	private static final int URI_INDEX_VEHICLE_FUNCTION 		= 4;

	static
	{
		sURIMatcher.addURI(DB_HOST_NAME, MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_INFO, URI_INDEX_CAN_VEHICLE_INFO);
		sURIMatcher.addURI(DB_HOST_NAME, MctCoreServicesProviderMetaData.TABLE_APP_CONFIG, URI_INDEX_APP_CONFIG);
		sURIMatcher.addURI(DB_HOST_NAME, MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_SETTING, URI_INDEX_CAN_VEHICLE_SETTING);
		sURIMatcher.addURI(DB_HOST_NAME, MctCoreServicesProviderMetaData.TABLE_VEHICLE_FUNCTION, URI_INDEX_VEHICLE_FUNCTION);
	}

	/**
	 * This class encapsulates a SQL where clause and its parameters. It makes
	 * it possible for shared methods (like
	 * {@link DownloadProvider#getWhereClause(Uri, String, String[], int)}) to
	 * return both pieces of information, and provides some utility logic to
	 * ease piece-by-piece construction of selections.
	 */
	public static class SqlSelection
	{
		public StringBuilder mWhereClause = new StringBuilder();
		public List<String> mParameters = new ArrayList<String>();

		public <T> void appendAndClause(String newClause, final T... parameters)
		{
			if (TextUtils.isEmpty(newClause))
			{
				return;
			}
			if (mWhereClause.length() != 0)
			{
				mWhereClause.append(" AND ");
			}
			mWhereClause.append("(");
			mWhereClause.append(newClause);
			mWhereClause.append(")");
			if (parameters != null)
			{
				for (Object parameter : parameters)
				{
					mParameters.add(parameter.toString());
				}
			}
		}
		
		public <T> void appendOrClause(String newClause, final T... parameters)
		{
			if (TextUtils.isEmpty(newClause))
			{
				return;
			}
			if (mWhereClause.length() != 0)
			{
				mWhereClause.append(" OR ");
			}
			mWhereClause.append("(");
			mWhereClause.append(newClause);
			mWhereClause.append(")");
			if (parameters != null)
			{
				for (Object parameter : parameters)
				{
					mParameters.add(parameter.toString());
				}
			}
		}
		

		public String getSelection()
		{
			return mWhereClause.toString();
		}

		public String[] getParameters()
		{
			String[] array = new String[mParameters.size()];
			return mParameters.toArray(array);
		}
	}

	/**
	 * Creates and updated database on demand when opening it. Helper class to
	 * create database the first time the provider is initialized and upgrade it
	 * when a new version of the provider needs an updated version of the
	 * database.
	 */
	private final class DatabaseHelper extends SQLiteOpenHelper
	{
		private boolean mUpdate = false;
		public DatabaseHelper(final Context context)
		{
			super(context, MctCoreServicesProviderMetaData.DB_NAME, null, DB_VERSION);
			Log.i(TAG, "DatabaseHelper onCreate");
		
			SQLiteDatabase database = getWritableDatabase();
			if(database == null)
			{
				Log.i(TAG, "no database file");
				return;
			}
			Log.i(TAG, "create database helper instance,version:"+database.getVersion());
		}

		//数据库第一次创建时调用(不存在数据库文件时,系统会创建数据库文件，然后执行此回调要求创建表)
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			boolean bRet = ServiceHelper.updateDataBaseFile(getContext());
			Log.d(TAG,"create the new database,ret:"+bRet);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			oldVersion = getCurrentDBVersion(db);
			Log.d(TAG,"update the database.,oldVersion:"+oldVersion+",newVersion:"+newVersion);
			if(newVersion > oldVersion)
			{
				boolean bRet = ServiceHelper.updateDataBaseFile(getContext());
				if(bRet)
				{
					db.setVersion(newVersion);
					mUpdate = true;
					Log.i(TAG, "update database to version:"+newVersion);
				}
			}
			else
			{
				Log.d(TAG,"no need to update database");
			}
		}
		
		private int getCurrentDBVersion(SQLiteDatabase db)
		{
			int retVersionNo = -1;
			Cursor cursor = db.query(MctCoreServicesProviderMetaData.TABLE_APP_CONFIG, null, "propId=? ", new String[]{"0"}, null, null, null);
			while(cursor != null && cursor.moveToNext())
			{
				String strVersionNo = cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_PROP_VALUE));
				retVersionNo = ServiceHelper.stringToIntSafe(strVersionNo);
			}
			cursor.close();
			return retVersionNo;
		}

		/*
		 * 创建原车信息表
		 */
		private void createCanVehicleInfoTable(SQLiteDatabase db)
		{
			try
			{
				//INTEGER PRIMARY KEY AUTOINCREMENT
				db.execSQL("CREATE TABLE IF NOT EXISTS " + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_INFO + "(" + 
						MctCoreServicesProviderMetaData.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_ID + " INTEGER, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_SERIES_ID + " INTEGER, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PLATFORM_ID + " INTEGER, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_CAN_BOX_IDS + " TEXT, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_CH_NAME + " TEXT, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_EN_NAME + " TEXT, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_SERIES_CH_NAME + " TEXT, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_SERIES_EN_NAME + " TEXT, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_CAN_BOX_CH_NAME + " TEXT, " +
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_CAN_BOX_EN_NAME + " TEXT);");
				
				Log.d(TAG,"create " + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_INFO + " table in database");
				
			} catch (SQLException ex)
			{
				Log.d(TAG,"couldn't create " + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_INFO + " table in mct_data database,exception:"+ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
		}
		
		/*
		 * 创建AppConfig缓存表
		 */
		private void createAppConfigTable(SQLiteDatabase db)
		{
			try
			{
				db.execSQL("CREATE TABLE IF NOT EXISTS " + MctCoreServicesProviderMetaData.TABLE_APP_CONFIG + "(" + MctCoreServicesProviderMetaData.COLUMN_ID + 
						" INTEGER PRIMARY KEY AUTOINCREMENT, " + 
						MctCoreServicesProviderMetaData.COLUMN_PROP_ID + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_PROP_VALUE + " TEXT);");
				
				Log.d(TAG,"create " + MctCoreServicesProviderMetaData.TABLE_APP_CONFIG + " table in database");
			} catch (SQLException ex)
			{
				Log.d(TAG,"couldn't create " + MctCoreServicesProviderMetaData.TABLE_APP_CONFIG + " table in market database,exception:"+ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
		}
		
		/*
		 * 创建CanVehicleSetting缓存表
		 */
		private void createCanVehicleSettingTable(SQLiteDatabase db)
		{
			try
			{
				db.execSQL("CREATE TABLE IF NOT EXISTS " + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_SETTING + "(" + MctCoreServicesProviderMetaData.COLUMN_ID + 
						" INTEGER PRIMARY KEY AUTOINCREMENT, " + 
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PLATFORM_ID + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_GROUP_ID + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_GROUP_NAME + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_GROUP_NAME2 + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_ID + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_NAME + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_NAME2 + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_TYPE + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_VALUE + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_VALUE2  + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_CMD  + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_CONTENT  + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_PROP_CONTENT2  + " TEXT, "+
						MctCoreServicesProviderMetaData.COLUMN_DEPEND_BY_CAR_PROP_ID  + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_DEPEND_BY_CAR_PROP_CMD  + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_PROGRESS_STEP_VALUE+" INTEGER);");
				
				Log.d(TAG,"create " + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_SETTING + " table in database");
			} catch (SQLException ex)
			{
				Log.d(TAG,"couldn't create " + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_SETTING + " table in market database,exception:"+ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
		}
		
		/*
		 * 创建VehicleFunction缓存表
		 */
		private void createVehicleFunctionTable(SQLiteDatabase db)
		{
			try
			{
				db.execSQL("CREATE TABLE IF NOT EXISTS " + MctCoreServicesProviderMetaData.TABLE_VEHICLE_FUNCTION + "(" + MctCoreServicesProviderMetaData.COLUMN_ID + 
						" INTEGER PRIMARY KEY AUTOINCREMENT, " + 
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_ID + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_VEHICLE_INFO + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_VEHICLE_INFO_VIEW_ID + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_VEHICLE_SETTING + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_AIR_CONDITION + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_AMPLIFIER + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_PARKING_RADAR + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_TPMS + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_AVM + " INTEGER, "+
						MctCoreServicesProviderMetaData.COLUMN_SUPPORT_REVERSE_TRACE + " INTEGER);");

				Log.d(TAG,"create " + MctCoreServicesProviderMetaData.TABLE_VEHICLE_FUNCTION + " table in database");
			} catch (SQLException ex)
			{
				Log.d(TAG,"couldn't create " + MctCoreServicesProviderMetaData.TABLE_VEHICLE_FUNCTION + " table in market database,exception:"+ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
		}
		
		public boolean isHasUpdated()
		{
			return mUpdate;
		}
	}

	@Override
	public boolean onCreate()
	{
		Log.i(TAG, "onCreate:");
		
		ServiceHelper.initDataBaseFile(getContext());
		try
		{
			mOpenHelper = new DatabaseHelper(getContext());
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			//新版本升旧版本异常
			ServiceHelper.updateDataBaseFile(getContext());
			if(mOpenHelper != null)
			{
				try
				{
					mOpenHelper.close();
				} catch (Exception subException)
				{
					// TODO: handle exception
					subException.printStackTrace();
				}
				mOpenHelper = null;
			}
			mOpenHelper = new DatabaseHelper(getContext());
		}
		if(mOpenHelper.isHasUpdated())
		{
			Log.i(TAG, "recreate database helper!");
			mOpenHelper.close();
			mOpenHelper = null;
			mOpenHelper = new DatabaseHelper(getContext());
		}
		return true;
	}

	/*
	 * 删除数据库
	 */
	private boolean deleteDataBase()
	{
		return getContext().deleteDatabase(MctCoreServicesProviderMetaData.DB_NAME);
	}
	

	@Override
	public String getType(Uri uri)
	{

		int match = sURIMatcher.match(uri);
		switch (match)
		{
		case URI_INDEX_CAN_VEHICLE_INFO:
			return LIST_TYPE + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_INFO;
		case URI_INDEX_APP_CONFIG:
			return LIST_TYPE + MctCoreServicesProviderMetaData.TABLE_APP_CONFIG;
		case URI_INDEX_CAN_VEHICLE_SETTING:
			return LIST_TYPE + MctCoreServicesProviderMetaData.TABLE_CAN_VEHICLE_SETTING;
		case URI_INDEX_VEHICLE_FUNCTION:
			return LIST_TYPE + MctCoreServicesProviderMetaData.TABLE_VEHICLE_FUNCTION;
		default:
			break;
		}
		return null;
	}

	
	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		int match = sURIMatcher.match(uri);
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
		final String table = getTableFromUri(uri);
		long rowID = db.insert(table, null, values);
		db.close();
		if (rowID == -1)
		{
			Log.d(TAG,"couldn't insert into " + table + " database");
			return null;
		}
		
		Uri inserResult = ContentUris.withAppendedId(uri, rowID);
		notifyContentChanged(uri, match);
		return inserResult;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs)
	{

		int match = sURIMatcher.match(uri);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final String table = getTableFromUri(uri);
		//SqlSelection selection = getWhereClause(uri, where, whereArgs);
		//int count = db.delete(table, selection.getSelection(), selection.getParameters());
		int count = db.delete(table, where, whereArgs);
		db.close();
		if (count == 0)
		{
			Log.e(TAG,"couldn't delete URI " + uri);
			return count;
		}
		notifyContentChanged(uri, match);
		return count;
	}

	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		int match = sURIMatcher.match(uri);
		if (match == -1)
		{
			Log.e(TAG,"updating unknown URI: " + uri);
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		final String table = getTableFromUri(uri);
		int nRet = db.update(table, values, selection, selectionArgs);
		db.close();
		return nRet;
	}

	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		int match = sURIMatcher.match(uri);
		if (match == -1)
		{
			Log.d(TAG,"querying unknown URI: " + uri);
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		//SqlSelection fullSelection = getWhereClause(uri, selection, selectionArgs);
		final String table = getTableFromUri(uri);
		//Cursor ret = db.query(table, projection, fullSelection.getSelection(), fullSelection.getParameters(), null, null, sortOrder);
		Cursor ret = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
		return ret;
	}

	/**
	 * Notify of a change through both URIs
	 */
	private void notifyContentChanged(final Uri uri, int uriMatch)
	{
		getContext().getContentResolver().notifyChange(uri, null);
	}

	/**
	 * 从URI中获取表名
	 * 
	 * @param uri
	 *            目标Uri
	 * @return 操作目标的表名
	 */
	private static String getTableFromUri(final Uri uri)
	{
		return uri.getPathSegments().get(0);
	}

	/**
	 * 获取SQL条件的工具方法
	 * 
	 * @param uri
	 *            Content URI
	 * @param where
	 *            条件
	 * @param whereArgs
	 *            参数
	 * @param uriMatch
	 *            类型
	 * @return 合成的SqlSelection对象
	 */
	private static SqlSelection getWhereClause(final Uri uri, final String where, final String[] whereArgs)
	{
		SqlSelection selection = new SqlSelection();
		selection.appendAndClause(where, whereArgs);
		return selection;
	}

	/**
	 * 打印 【查询SQL】 详细信息
	 */
	private static void logVerboseQueryInfo(String[] projection, final String selection, final String[] selectionArgs, final String sort, SQLiteDatabase db)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("starting query, database is ");
		if (db != null)
		{
			sb.append("not ");
		}
		sb.append("null; ");
		if (projection == null)
		{
			sb.append("projection is null; ");
		}
		else if (projection.length == 0)
		{
			sb.append("projection is empty; ");
		}
		else
		{
			for (int i = 0; i < projection.length; ++i)
			{
				sb.append("projection[");
				sb.append(i);
				sb.append("] is ");
				sb.append(projection[i]);
				sb.append("; ");
			}
		}
		sb.append("selection is ");
		sb.append(selection);
		sb.append("; ");
		if (selectionArgs == null)
		{
			sb.append("selectionArgs is null; ");
		}
		else if (selectionArgs.length == 0)
		{
			sb.append("selectionArgs is empty; ");
		}
		else
		{
			for (int i = 0; i < selectionArgs.length; ++i)
			{
				sb.append("selectionArgs[");
				sb.append(i);
				sb.append("] is ");
				sb.append(selectionArgs[i]);
				sb.append("; ");
			}
		}
		sb.append("sort is ");
		sb.append(sort);
		sb.append(".");
		Log.d(TAG,sb.toString());
	}

}
