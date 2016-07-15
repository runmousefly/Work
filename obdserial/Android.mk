LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := libobdserial

LOCAL_SRC_FILES:= SerialPort.c SerialJNI.cpp

LOCAL_STATIC_LIBRARIES := libc

LOCAL_SHARED_LIBRARIES := \
	liblog

LOCAL_MODULE_TAGS := optional

LOCAL_LDLIBS:=-L$(SYSROOT)/usr/lib -llog

include $(BUILD_SHARED_LIBRARY)
