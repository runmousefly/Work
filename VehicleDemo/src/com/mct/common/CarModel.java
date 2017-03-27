package com.mct.common;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;

public class CarModel
{
	private int mCarSeriesId 		= VehiclePropertyConstants.CAR_MODEL_NONE;
	private int mCarModelId 		= VehiclePropertyConstants.CAR_MODEL_NONE;
	private int[] mCanBoxIds 	= null;
	private String mCarModelCHName = null;
	private String mCarModelENName = null;
	private String mCarSeriesCHName = null;
	private String mCarSeriesENName = null;
	private String[] mCanBoxCHName 	= null;
	private String[] mCanBoxENName 	= null;

	
	//根据数据库查询到的值初始化CarModel
	public void InitCarModelFromDb(int carModelId,int carSeriesId,String canBoxIds,
			String carModelCHName,String carModelENName,String carSeriesCHName,String carSeriesENName,
			String canBoxCHName,String canBoxENName)
	{
		mCarSeriesId = carSeriesId;
		mCarModelId = carModelId;
		mCanBoxIds = VehicleManager.stringToIntArray(canBoxIds);
		mCarModelCHName = carModelCHName;
		mCarModelENName = carModelENName;
		mCarSeriesCHName = carSeriesCHName;
		mCarSeriesENName = carSeriesENName;
		mCanBoxCHName = VehicleManager.stringToStringArray(canBoxCHName);
		mCanBoxENName = VehicleManager.stringToStringArray(canBoxENName);
	}
	
	
	/**
	 * @return the mCarModelId
	 */
	public int getCarModelId()
	{
		return mCarModelId;
	}
	/**
	 * @param mCarModelId the mCarModelId to set
	 */
	public void setCarModelId(int carModelId)
	{
		this.mCarModelId = carModelId;
	}

	/**
	 * @return the mCanBoxId
	 */
	public int[] getCanBoxIds()
	{
		return mCanBoxIds;
	}
	/**
	 * @param mCanBoxId the mCanBoxId to set
	 */
	public void setCanBoxIds(String canBoxIds)
	{
		this.mCanBoxIds = VehicleManager.stringToIntArray(canBoxIds);
	}
	/**
	 * @return the mCarSeriesId
	 */
	public int getCarSeriesId()
	{
		return mCarSeriesId;
	}
	/**
	 * @param mCarSeriesId the mCarSeriesId to set
	 */
	public void setCarSeriesId(int carSeriesId)
	{
		this.mCarSeriesId = carSeriesId;
	}
	
	/**
	 * @return the mCarModelCHName
	 */
	public String getCarModelCHName()
	{
		return mCarModelCHName;
	}
	/**
	 * @param mCarModelCHName the mCarModelCHName to set
	 */
	public void setCarModelCHName(String carModelCHName)
	{
		this.mCarModelCHName = carModelCHName;
	}
	/**
	 * @return the mCarModelENName
	 */
	public String getCarModelENName()
	{
		return mCarModelENName;
	}
	/**
	 * @param mCarModelENName the mCarModelENName to set
	 */
	public void setCarModelENName(String carModelENName)
	{
		this.mCarModelENName = carModelENName;
	}


	public String getCarSeriesCHName()
	{
		return mCarSeriesCHName;
	}


	public void setCarSeriesCHName(String carSeriesCHName)
	{
		this.mCarSeriesCHName = carSeriesCHName;
	}


	public String getCarSeriesENName()
	{
		return mCarSeriesENName;
	}


	public void setCarSeriesENName(String carSeriesENName)
	{
		this.mCarSeriesENName = carSeriesENName;
	}


	public String[] getCanBoxCHName()
	{
		return mCanBoxCHName;
	}


	public void setCanBoxCHName(String canBoxCHName)
	{
		this.mCanBoxCHName = VehicleManager.stringToStringArray(canBoxCHName);
	}


	public String[] getCanBoxENName()
	{
		return mCanBoxCHName;
	}


	public void setCanBoxENName(String canBoxENName)
	{
		this.mCanBoxENName = VehicleManager.stringToStringArray(canBoxENName);
	}

	
}
