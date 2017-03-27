package com.mct.utils;

import java.util.ArrayList;
import java.util.List;

import com.mct.common.CarModel;
import com.mct.common.MctCoreServicesProviderMetaData;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

public class DBUtils
{
	public static List<CarModel> queryAllSupportCarModels(Context context)
	{
		if(context == null)
		{
			return null;
		}
		Cursor cursor = context.getContentResolver().query(MctCoreServicesProviderMetaData.CAN_VEHICLE_INFO_CONTENT_URI, null, null, null, null);
		return formatCarModelsFromDb(cursor);
	}
	
	public static List<CarModel> queryByCarModelId(Context context,int carModelId)
	{
		if(context == null)
		{
			return null;
		}
		MctSqlSelection sqlSelection = new MctSqlSelection();
		sqlSelection.appendAndClause(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_ID+"=?", carModelId);
		Cursor cursor = context.getContentResolver().query(MctCoreServicesProviderMetaData.CAN_VEHICLE_INFO_CONTENT_URI, null, sqlSelection.getSelection(), sqlSelection.getParameters(), null);
		return formatCarModelsFromDb(cursor);
	}
	
	public static List<CarModel> querySupportFunctionByCarModel(Context context,int carModelId)
	{
		if(context == null)
		{
			return null;
		}
		MctSqlSelection sqlSelection = new MctSqlSelection();
		sqlSelection.appendAndClause(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_ID+"=?",carModelId);
		Cursor cursor = context.getContentResolver().query(MctCoreServicesProviderMetaData.CAN_VEHICLE_FUNCTION_URI, null, sqlSelection.getSelection(), sqlSelection.getParameters(), null);
		while(cursor != null && cursor.moveToNext())
		{
			int pid = cursor.getInt(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_ID));
			int vehicleInfoSupported = cursor.getInt(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_SUPPORT_VEHICLE_INFO));
			int vehicleSettingSupported = cursor.getInt(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_SUPPORT_VEHICLE_SETTING));
		}
		return null;
	}
	
	public static List<CarModel> formatCarModelsFromDb(Cursor cursor)
	{
		if(cursor == null)
		{
			return null;
		}
		List<CarModel> carModelList = new ArrayList<CarModel>();
		while(cursor.moveToNext())
		{
			CarModel carModel = new CarModel();
			carModel.setCarModelId(cursor.getInt(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_ID)));
			carModel.setCarSeriesId(cursor.getInt(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_SERIES_ID)));
			carModel.setCanBoxIds(cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_CAN_BOX_IDS)));
			carModel.setCarModelCHName(cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_CH_NAME)));
			carModel.setCarModelENName(cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_MODEL_EN_NAME)));
			carModel.setCarSeriesCHName(cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_SERIES_CH_NAME)));
			carModel.setCarSeriesENName(cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_SERIES_EN_NAME)));
			carModel.setCanBoxCHName(cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_CAN_BOX_CH_NAME)));
			carModel.setCanBoxENName(cursor.getString(cursor.getColumnIndex(MctCoreServicesProviderMetaData.COLUMN_VEHICLE_CAN_BOX_EN_NAME)));
			carModelList.add(carModel);
		}
		cursor.close();
		return carModelList;
	}
	
	public static class MctSqlSelection
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
}
