/*
*    Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
*    Qualcomm Technologies Proprietary and Confidential.
*
*/
package com.qualcomm.qti.ivi.aidl;

import android.os.Messenger;

interface IVehicleService 
{
    /**
    *    Returns array of supported property Ids on this platform.
    *    @return array of integers, each identifying the property supported on this platform.
    *    Please note that this API is work in progress and there can be few changes in
    *    subsequent revisions of this file.
    */
    int[] getSupportedSignalIds();

    /**
    *    Returns array of writable property Ids available on this platform.
    *    @return array of integers, each identifying the writable
    *    property supported on this platform.
    *    Array returned from this API is in general the sub-set of properties
    *    returned via getSupportedPropertyIds API.
    *    Please note that this API is work in progress and there can be few changes in
    *    subsequent revisions of this file.
    */
    int[] getWritableSignalIds();

	/**
    *    Retrieve data types for properties idetified by @param propIds.
    *    Refer to VehiclePropertyConstants to interpret @return int[].
    */
    int[] getSignalsDataType(in int[] propIds);
    
    int getSignalDataType(int propId);


	/**
    *    Set values associated with properties specified via @param  propIds.
    *    @param propIds Point to properties to be set.
    *    @param propValues Point to values to be set.
    *    @return true if successful in setting property values else returns false.
    *    Please note that this API is work in progress and there can be few changes in
    *    subsequent revisions of this file.
    */
    boolean setSignals(in int[] propIds, in String[] propValues);
    
    boolean setSignal(int propertyId, String value);
    
    /**
    *    Return values associated with properties specified via @param  propIds.
    *    @param propIds Identify properties being queried.
    *    @return values for properties identified via @param  propIds.
    *    Returns null if an error occurs while retrieving values.
    *    Please note that this API is work in progress and there can be few changes in
    *    subsequent revisions of this file.
    */
    String[] getSignals(in int[] propIds);
    
    String getSignal(int propertyId);
    
    
    String getUserValue(int param);
    
    /**
    *    Registers a messenger with properties.
    *    @param propIds Identify properties to be associated with the messenger.
    *    @param msngr is the handle to the messenger.
    *    @param rateMs specify the frequency for call-backs in milli-seconds.
    *    rateMs is just a hint, the actual rate might be slower of faster than this.
    *    @return true if successful in registering the messenger else returns false.
    *    Please note that this API is work in progress and there can be few changes in
    *    subsequent revisions of this file.
    */
    
    boolean registerMessenger(in int[] propIds, in Messenger paramMessenger, in int[] notifyTypes, in int[] rateMs);

    /**
    *    Unregisteres the messenger registered earlier via successful call to registerMessenger.
    *    @param propIds Identify properties to be associated with the messenger.
    *    @param msngr is the handle to the Messenger.
    *    @return true if successful in removing the messenger else returns false.
    *    Please note that this API is work in progress and there can be few changes in
    *    subsequent revisions of this file.
    */
    boolean unregisterMessenger(in int[] propIds, in Messenger msngr);

}
