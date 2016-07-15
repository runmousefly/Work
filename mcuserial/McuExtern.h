#ifndef __MCU_EXTERN_H__
#define __MCU_EXTERN_H__

#include  <android/log.h>

#define LOG_TAG "[MCU_SERIAL_JNI]"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
// 定义debug信息
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
// 定义error信息
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#ifdef __cplusplus									/* for C++ compiler */
extern "C" {
#endif

  typedef unsigned char         uint8;
  typedef signed char             int8;
  typedef unsigned short  uint16;
  typedef signed short    int16;
  typedef unsigned long   uint32;
  typedef signed long             int32;

  extern bool McuComm_Open(void);
  extern bool McuComm_Close(void);
  extern void McuComm_Type(char cType);
  extern void McuComm_Send(uint8 u8Device, uint8* pu8Data, uint8 u8Length, bool bNeedAck);

#ifdef __cplusplus									/* for C++ compiler */
	}
#endif

#endif
