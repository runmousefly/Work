package com.mct.coreservices;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehicleManager;
import com.mct.VehiclePropertyConstants;
import com.mct.utils.ServiceHelper;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

public class DVRManager
{
	private static String TAG = "DVRManager";
	
	private CarService mService = null;
	private HeadUnitMcuManager mMcuMangaer = null;
	private int mDVRType = VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_IR;
	private int mDVRDisplayAreaWidth 	= 1024;
	private int mDVRDisplayAreaHeight 	= 600;
	private int mDVROutputWidth 			= 255;
	private int mDVROutputHeight 			= 255;
	private int DVR_PROTOCOL_HEAD 	= 0x01;  
	
	
	public boolean onInitManager(CarService service)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onInitManager");
		mService = service;
		
		if(service != null)
		{
			mMcuMangaer = (HeadUnitMcuManager)service.getMcuManager();
			String strDvrType = service.loadSettings(CarService.SHARED_PREFERENCES_KEY_DVR_TYPE);
			if(strDvrType != null)
			{
				try
				{
					mDVRType = Integer.valueOf(strDvrType);
				} catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
					//默认值
					mDVRType = VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_IR;
				}
			}
			onUpdateDvrType(mDVRType);
		}
		if(mMcuMangaer == null)
		{
			return false;
		}
		return true;
	}

	public boolean onDeinitManager()
	{
		Log.i(TAG, "onDeinitManager");
		// TODO Auto-generated method stub
		mMcuMangaer = null;
		return true;
	}
	
	public boolean setPropValue(int propId, String value)
	{
		if(mMcuMangaer == null)
		{
			Log.e(TAG, "HeadUnitMcuManager is not ready!");
		}
		try
		{
			switch (propId)
			{
				//DVR显示区域
				case VehicleInterfaceProperties.VIM_MCU_DVR_DISPLAY_AREA_PROPERTYE:
					int[] area = VehicleManager.stringToIntArray(value);
					if(area != null && area.length == 2)
					{
						mDVRDisplayAreaWidth = area[0];
						mDVRDisplayAreaHeight = area[1];
						Log.i(TAG, "set display area,w:"+mDVRDisplayAreaWidth+",h:"+mDVRDisplayAreaHeight);
						return true;
					}
					break;
				//DVR触摸事件
				case VehicleInterfaceProperties.VIM_MCU_DVR_TOUCH_EVENT_PROPERTYE:
					int[] touchEvent = VehicleManager.stringToIntArray(value);
					if(touchEvent != null && touchEvent.length == 3)
					{
						//分辨率转化
						touchEvent[0] = (int)(((mDVROutputWidth*1.0)/mDVRDisplayAreaWidth)*touchEvent[0]);
						touchEvent[1] = (int)(((mDVROutputHeight*1.0)/mDVRDisplayAreaHeight)*touchEvent[1]);
						sendTouchPoint(touchEvent[0],touchEvent[1],touchEvent[2]);
						return true;
					}
					break;
				//DVR按键事件
				case VehicleInterfaceProperties.VIM_MCU_DVR_KEY_EVENT_PROPERTYE:
					sendKey(Integer.valueOf(value));
					break;
				case VehicleInterfaceProperties.VIM_MCU_DVR_TYPE_PROPERTYE:
					Log.i(TAG,"set dvr type:"+value);
					if(value != null && value.length() > 0)
					{
						onUpdateDvrType(Integer.valueOf(value));
					}
					break;
				default:
					break;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "setPropValue Exception,propId:"+propId+",value:"+value);
		}
		return false;
	}
	
	public void onReceiveData(int[] data)
	{
		
	}

	private void onUpdateDvrType(int newType)
	{
		if(mDVRType == newType)
		{
			Log.i(TAG, "no need to set dvr type again!");
			return;
		}
		int communicateType = DVRProtocol.DVR_DATA_VIA_IR;
		switch (newType)
		{
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_NO_TOUCH_PANEL_VIA_IR:
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_IR:
				communicateType = DVRProtocol.DVR_DATA_VIA_IR;
				mDVROutputWidth 	= 255;
				mDVROutputHeight 	= 255;
				break;
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_NO_TOUCH_PANEL_VIA_UART:
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_UART:
				communicateType = DVRProtocol.DVR_DATA_VIA_UART;
				mDVROutputWidth 	= 255;
				mDVROutputHeight 	= 255;
				break;
			case VehiclePropertyConstants.DVR_TYPE_CXJ_TOUCH_PANEL_VIA_IR:
				communicateType = DVRProtocol.DVR_DATA_VIA_IR;
				mDVROutputWidth 	= 255;
				mDVROutputHeight 	= 240;
				break;
			default:
				return;
		}
		mDVRType = newType;
		mMcuMangaer.postDVRType(communicateType);
		mService.saveSetting(CarService.SHARED_PREFERENCES_KEY_DVR_TYPE, String.valueOf(mDVRType));
	}
	
	public boolean sendKey(int key)
	{
		Log.i(TAG,"sendKey,key:"+key);
		if(mMcuMangaer == null)
		{
			return false;
		}
		int dvrKey = -1;
		switch(mDVRType)
		{
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_NO_TOUCH_PANEL_VIA_IR:
				dvrKey = DVRProtocol.userKeyToNoTouchIRKeyForMocar(key);
				if(dvrKey >= 0)
				{
					return mMcuMangaer.postDVRData(new int[]{0xFF,0x00,dvrKey});
				}
				break;
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_NO_TOUCH_PANEL_VIA_UART:
				dvrKey = DVRProtocol.userKeyToNoTouchUartKeyForMocar(key);
				if(dvrKey >= 0)
				{
					return mMcuMangaer.postDVRData(new int[]{0x01,0x88,0x00,0x01,dvrKey,0x03,0x00,0x00});
				}
				break;
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_IR:
				dvrKey = DVRProtocol.userKeyToTouchIRKeyForMocar(key);
				if(dvrKey > 0)
				{
					return mMcuMangaer.postDVRData(new int[]{0xEA,0x01,ServiceHelper.switchBigToLittleEndial(dvrKey)});
				}
				break;
			case VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_UART:
				dvrKey = DVRProtocol.userKeyToTouchUartKeyForMocar(key);
				if(dvrKey > 0)
				{
					return mMcuMangaer.postDVRData(new int[]{0x01,0x88,0x00,0x01,dvrKey,0x03,0x00,0x00});
				}
				break;
			case VehiclePropertyConstants.DVR_TYPE_CXJ_TOUCH_PANEL_VIA_IR:
				dvrKey = DVRProtocol.userKeyToTouchIRKeyForCXJ(key);
				if(dvrKey > 0)
				{
					return mMcuMangaer.postDVRData(new int[]{0x55,0xAA,dvrKey,~(dvrKey)});
				}
				break;
			default:
				break;
		}
		return false;
	}
	
	//0 释放 1 按下
	public boolean sendTouchPoint(int x,int y,int action)
	{
		Log.i(TAG,"sendTouchPoin,x:"+x+",y:"+y+",action:"+action);
		if(mMcuMangaer == null)
		{
			return false;
		}
		if(action == KeyEvent.ACTION_UP)
		{
			return true;
		}
		if(action == KeyEvent.ACTION_DOWN)
		{
			switch(mDVRType)
			{
				case VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_IR:
					return mMcuMangaer.postDVRData(new int[]{0x55,0xAA,ServiceHelper.switchBigToLittleEndial(x&0xFF),ServiceHelper.switchBigToLittleEndial(y&0xFF)});
				case VehiclePropertyConstants.DVR_TYPE_MOCAR_TOUCH_PANEL_VIA_UART:
					return mMcuMangaer.postDVRData(new int[]{0x01,0x88,0x02,0x02,x&0xFF,y&0xFF,0x03,0x00,0x00});
				case VehiclePropertyConstants.DVR_TYPE_CXJ_TOUCH_PANEL_VIA_IR:
					mMcuMangaer.postDVRData(new int[]{0xAA,0x55,x,y});
				default:
					break;
			}
		}
		return false;
	}
	
	public boolean syncDVRTime()
	{
		if(mMcuMangaer == null)
		{
			return false;
		}
		Time time = new Time("GMT+8");       
        time.setToNow();     
		mMcuMangaer.postDVRData(pack(DVRProtocol.HD_CMD_DATE, new int[]{time.year-2000,
				time.month,time.monthDay,time.weekDay}));
		mMcuMangaer.postDVRData(pack(DVRProtocol.HD_CMD_TIME, new int[]{time.hour,time.minute,24}));
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// 封包 
	private int[] pack(int cmd, int[] param)
	{
		int paramLen = (param == null) ? 0 : param.length;
		int[] data = new int[paramLen + 4];
		int checksum = 0x00;
		int count = 0;
		int i = 0;
		data[count++] = DVR_PROTOCOL_HEAD;
		checksum += DVR_PROTOCOL_HEAD;
		data[count++] = 0x88;
		checksum += (paramLen+1); 
		data[count++] = cmd;
		checksum += cmd; 
		for(i=0; i<paramLen; i++)
		{
			data[count++] = param[i];
			checksum += param[i]; 
		}
		data[count]  = (-checksum)&0xFF;
		return data;
	}

	// 解包
	public int[] unPack(int[] buffer)
	{
		return null;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
}
