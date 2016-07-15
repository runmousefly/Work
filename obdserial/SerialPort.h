
#ifndef  __MCUSERIALPORT_H__
#define  __MCUSERIALPORT_H__

#include "serial_type.h"

#ifdef __cplusplus									/* for C++ compiler */
extern "C" {
#endif

extern bool Serialport_Open(const char* pszPort);
extern bool Serialport_Close(void);
extern void Serialport_Clear(void);
extern bool Serialport_IsOpen(void);
extern bool Serialport_Write(uint8* pu8Buf, uint16 u16Len);
extern bool Serialport_Read(uint8* pu8Buf, uint16* pu16Written, uint16 u16Len);

#ifdef __cplusplus									/* for C++ compiler */
	}
#endif

#endif	/* __MCUSERIALPORT_H__ */
