package com.mct.coreservices;

import com.mct.VehicleInterfaceProperties;
import com.mct.VehiclePropertyConstants;

import android.util.SparseIntArray;

public class DVRProtocol
{

	// TOUCH PANEL UART INTERFACE
	// 0x01 + 0x88 + 0x01 + 0x01 + data(key) + 0x03 + 0x00 + 0x00

	// TOUCH PANEL REMOTE
	// 0xEA01+key

	// NO TOUCH PANEL UART INTERFACE
	// 0x01 + 0x88 + 0x00 + 0x01 + data(key) + 0x03 + 0x00 + 0x00

	// NO TOUCH PANEL REMOTE
	// 0xFF00+key

	// TOUCH PANEL REMOTE INTERFACE VIA X/Y COORDINATE
	// 0x55aa+xy(0-255)

	// TOUCH PANEL UART INTERFACE VIA X/Y COORDINATE
	// 0x01 + 0x88 + 0x02 + 0x02 + data(x,y) + 0x03 + 0x00 + 0x00

	public static final int HD_CMD_REVERSED = 0xA0;
	public static final int HD_CMD_TOUCH_DOWN = 0xA1;
	public static final int HD_CMD_TOUCH_UP = 0xA2;
	public static final int HD_CMD_KEY = 0xA3;
	public static final int HD_CMD_TIME = 0xA4;
	public static final int HD_CMD_DATE = 0xA5;

	public static final int DVR_DATA_VIA_IR = 0;
	public static final int DVR_DATA_VIA_UART = 1;

	// NO TOUCH IR KEY
	public static final int DVR_KEY_NO_TOUCH_IR_UP = 0x04;
	public static final int DVR_KEY_NO_TOUCH_IR_DOWN = 0x05;
	public static final int DVR_KEY_NO_TOUCH_IR_LEFT = 0xC4;
	public static final int DVR_KEY_NO_TOUCH_IR_RIGHT = 0x4D;
	public static final int DVR_KEY_NO_TOUCH_IR_OK = 0x54;
	public static final int DVR_KEY_NO_TOUCH_IR_CANCEL = 0x1A;
	public static final int DVR_KEY_NO_TOUCH_IR_MENU = 0x1E;
	public static final int DVR_KEY_NO_TOUCH_IR_POWER = 0x10;
	public static final int DVR_KEY_NO_TOUCH_IR_SCAN = 0x01;
	public static final int DVR_KEY_NO_TOUCH_IR_MUTE = 0x06;
	public static final int DVR_KEY_NO_TOUCH_IR_RECALL = 0x16;

	public static final SparseIntArray USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE = new SparseIntArray();
	static
	{
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_UP, DVR_KEY_NO_TOUCH_IR_UP);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_DOWN, DVR_KEY_NO_TOUCH_IR_DOWN);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_LEFT, DVR_KEY_NO_TOUCH_IR_LEFT);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_RIGHT, DVR_KEY_NO_TOUCH_IR_RIGHT);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_OK, DVR_KEY_NO_TOUCH_IR_OK);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_EXIT, DVR_KEY_NO_TOUCH_IR_CANCEL);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_MENU, DVR_KEY_NO_TOUCH_IR_MENU);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_POWER, DVR_KEY_NO_TOUCH_IR_POWER);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_PLAY_SCAN, DVR_KEY_NO_TOUCH_IR_SCAN);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_POWER, DVR_KEY_NO_TOUCH_IR_POWER);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_MUTE, DVR_KEY_NO_TOUCH_IR_MUTE);
		USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_CALL, DVR_KEY_NO_TOUCH_IR_RECALL);
	}

	public static int userKeyToNoTouchIRKeyForMocar(int userKey)
	{
		return (USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.indexOfKey(userKey) >= 0) ? USER_KEY_TO_NO_TOUCH_IR_KEY_TABLE.get(userKey) : -1;
	}

	// NO TOUCH UART KEY
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_UP = 0x0C;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_DOWN = 0x0D;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_LEFT = 0x0E;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_RIGHT = 0x0F;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_OK = 0x10;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_CANCEL = 0x18;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_MENU = 0x17;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_POWER = 0x0A;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_SCAN = 0x33;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_MUTE = 0x0B;
	public static final int MOCAR_DVR_KEY_NO_TOUCH_UART_RECALL = 0x1A;

	public static final SparseIntArray MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE = new SparseIntArray();
	static
	{
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_UP, MOCAR_DVR_KEY_NO_TOUCH_UART_UP);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_DOWN, MOCAR_DVR_KEY_NO_TOUCH_UART_DOWN);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_LEFT, MOCAR_DVR_KEY_NO_TOUCH_UART_LEFT);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_RIGHT, MOCAR_DVR_KEY_NO_TOUCH_UART_RIGHT);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_OK, MOCAR_DVR_KEY_NO_TOUCH_UART_OK);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_EXIT, MOCAR_DVR_KEY_NO_TOUCH_UART_CANCEL);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_MENU, MOCAR_DVR_KEY_NO_TOUCH_UART_MENU);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_POWER, MOCAR_DVR_KEY_NO_TOUCH_UART_POWER);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_PLAY_SCAN, MOCAR_DVR_KEY_NO_TOUCH_UART_SCAN);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_POWER, MOCAR_DVR_KEY_NO_TOUCH_UART_POWER);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_MUTE, MOCAR_DVR_KEY_NO_TOUCH_UART_MUTE);
		MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_CALL, MOCAR_DVR_KEY_NO_TOUCH_UART_RECALL);
	}

	public static int userKeyToNoTouchUartKeyForMocar(int userKey)
	{
		return (MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.indexOfKey(userKey) >= 0) ? MOCAR_USER_KEY_TO_NO_TOUCH_UART_KEY_TABLE.get(userKey) : -1;
	}

	// MOCAR TOUCH IR KEY
	public static final int MOCAR_DVR_KEY_TOUCH_IR_UP = 0x16;
	public static final int MOCAR_DVR_KEY_TOUCH_IR_DOWN = 0x0D;
	public static final int MOCAR_DVR_KEY_TOUCH_IR_LEFT = 0x05;
	public static final int MOCAR_DVR_KEY_TOUCH_IR_RIGHT = 0x19;
	public static final int MOCAR_DVR_KEY_TOUCH_IR_OK = 0x1C;
	public static final int MOCAR_DVR_KEY_TOUCH_IR_CANCEL = 0x18;
	public static final int MOCAR_DVR_KEY_TOUCH_IR_MUTE = 0x14;

	public static final SparseIntArray MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE = new SparseIntArray();
	static
	{
		MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_UP, MOCAR_DVR_KEY_TOUCH_IR_UP);
		MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_DOWN, MOCAR_DVR_KEY_TOUCH_IR_DOWN);
		MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_LEFT, MOCAR_DVR_KEY_TOUCH_IR_LEFT);
		MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_RIGHT, MOCAR_DVR_KEY_TOUCH_IR_RIGHT);
		MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_OK, MOCAR_DVR_KEY_TOUCH_IR_OK);
		MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_EXIT, MOCAR_DVR_KEY_TOUCH_IR_CANCEL);
		MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_MUTE, MOCAR_DVR_KEY_TOUCH_IR_MUTE);
	}

	public static int userKeyToTouchIRKeyForMocar(int userKey)
	{
		return (MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.indexOfKey(userKey) >= 0) ? MOCAR_USER_KEY_TO_TOUCH_IR_KEY_TABLE.get(userKey) : -1;
	}

	// MOCAR TOUCH UART KEY
	public static final int MOCAR_DVR_KEY_TOUCH_UART_UP = 0x03;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_DOWN = 0x04;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_LEFT = 0x01;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_RIGHT = 0x02;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_OK = 0x05;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_CANCEL = 0x08;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_MENU = 0x05;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_POWER = 0x10;
	public static final int MOCAR_DVR_KEY_TOUCH_UART_MUTE = 0x07;

	public static final SparseIntArray MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE = new SparseIntArray();
	static
	{
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_UP, MOCAR_DVR_KEY_TOUCH_UART_UP);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_DOWN, MOCAR_DVR_KEY_TOUCH_UART_DOWN);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_LEFT, MOCAR_DVR_KEY_TOUCH_UART_LEFT);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_RIGHT, MOCAR_DVR_KEY_TOUCH_UART_RIGHT);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_OK, MOCAR_DVR_KEY_TOUCH_UART_OK);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_EXIT, MOCAR_DVR_KEY_TOUCH_UART_CANCEL);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_MENU, MOCAR_DVR_KEY_TOUCH_UART_MENU);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_POWER, MOCAR_DVR_KEY_TOUCH_UART_POWER);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_POWER, MOCAR_DVR_KEY_TOUCH_UART_POWER);
		MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_MUTE, MOCAR_DVR_KEY_TOUCH_UART_MUTE);
	}

	public static int userKeyToTouchUartKeyForMocar(int userKey)
	{
		return (MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.indexOfKey(userKey) >= 0) ? MOCAR_USER_KEY_TO_TOUCH_UART_KEY_TABLE.get(userKey) : -1;
	}

	// CXJ TOUCH UART KEY
	public static final int CXJ_DVR_KEY_TOUCH_IR_UP 	= 0x04;
	public static final int CXJ_DVR_KEY_TOUCH_IR_DOWN = 0x0e;
	public static final int CXJ_DVR_KEY_TOUCH_IR_LEFT = 0x4d;
	public static final int CXJ_DVR_KEY_TOUCH_IR_RIGHT = 0x05;
	public static final int CXJ_DVR_KEY_TOUCH_IR_OK 	= 0x54;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM0 = 0x12;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM1 = 0x09;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM2 = 0x1d;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM3 = 0x1f;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM4 = 0x0d;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM5 = 0x19;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM6 = 0x1b;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM7 = 0x11;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM8 = 0x15;
	public static final int CXJ_DVR_KEY_TOUCH_IR_NUM9 = 0x17;
	

	public static final SparseIntArray CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE = new SparseIntArray();
	static
	{
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_UP, CXJ_DVR_KEY_TOUCH_IR_UP);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_DOWN, CXJ_DVR_KEY_TOUCH_IR_DOWN);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_LEFT, CXJ_DVR_KEY_TOUCH_IR_LEFT);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_RIGHT, CXJ_DVR_KEY_TOUCH_IR_RIGHT);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_OK, CXJ_DVR_KEY_TOUCH_IR_OK);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM0, CXJ_DVR_KEY_TOUCH_IR_NUM0);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM1, CXJ_DVR_KEY_TOUCH_IR_NUM1);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM2, CXJ_DVR_KEY_TOUCH_IR_NUM2);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM3, CXJ_DVR_KEY_TOUCH_IR_NUM3);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM4, CXJ_DVR_KEY_TOUCH_IR_NUM4);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM5, CXJ_DVR_KEY_TOUCH_IR_NUM5);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM6, CXJ_DVR_KEY_TOUCH_IR_NUM6);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM7, CXJ_DVR_KEY_TOUCH_IR_NUM7);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM8, CXJ_DVR_KEY_TOUCH_IR_NUM8);
		CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.put(VehiclePropertyConstants.USER_KEY_NUM9, CXJ_DVR_KEY_TOUCH_IR_NUM9);
	
	}

	public static int userKeyToTouchIRKeyForCXJ(int userKey)
	{
		return (CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.indexOfKey(userKey) >= 0) ? CXJ_USER_KEY_TO_TOUCH_IR_KEY_TABLE.get(userKey) : -1;
	}
}
