
#ifndef  __MCU_SERIAL_H__
#define  __MCU_SERIAL_H__

#include "jni.h"
#include "SerialPort.h"

#ifdef __cplusplus                                                                      /* for C++ compiler */
extern "C" {
#endif

extern void onReceiveData(int length,uint8 data[]);


#ifdef __cplusplus                                                                      /* for C++ compiler */
        }
#endif


#endif  /* __MCU_H__ */
