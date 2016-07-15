LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES += com/qualcomm/qti/ivi/aidl/IVehicleService.aidl
LOCAL_SRC_FILES += com/mct/VehicleInterfaceDataHandler.java
LOCAL_SRC_FILES += com/mct/VehicleInterfaceProperties.java
LOCAL_SRC_FILES += com/mct/VehicleManager.java
LOCAL_SRC_FILES += com/mct/VehiclePropertyConstants.java

LOCAL_CERTIFICATE := platform
LOCAL_MODULE := MctVehicleInterface

LOCAL_MODULE_PATH := $(TARGET_OUT_JAVA_LIBRARIES)

include $(BUILD_JAVA_LIBRARY)
