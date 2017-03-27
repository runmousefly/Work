/*
 *    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
 *    Qualcomm Technologies Proprietary and Confidential.
 *
 */

package com.mct;

import android.R.integer;

/**
 * Interface contains callbacks to be called to notify change in data or when an
 * error occurs. Please note that this interface is work in progress, and there
 * might be few changes going forward.
 */
public interface DeviceInterfaceDataHandler
{
	/**
	 * Called by VehicleInterfaceData when there is a change in interface's
	 * data.
	 */
	public void onDataUpdate(int propId,String value);

	/**
	 * Called by VehicleInterfaceData to notify when an error occurs.
	 * 
	 * @param bCleanUpAndRestart
	 *            provides a hint to the caller to take the appropriate action.
	 * @param bCleanUpAndRestart
	 *            = true indicates a fatal error and expects caller to clean up
	 *            and restart.
	 */
	public void onError(boolean bCleanUpAndRestart);
}
