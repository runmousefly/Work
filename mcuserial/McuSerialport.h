
#ifndef  __MCUSERIALPORT_H__
#define  __MCUSERIALPORT_H__

#include "mcu_type.h"

#ifdef __cplusplus									/* for C++ compiler */
extern "C" {
#endif

extern void McuSerialport_Open(const char* pszPort);
extern void McuSerialport_Close(void);
extern void McuSerialport_Clear(void);
extern bool McuSerialport_IsOpen(void);
extern bool McuSerialport_Write(uint8* pu8Buf, uint16 u16Len);
extern bool McuSerialport_Read(uint8* pu8Buf, uint16* pu16Written, uint16 u16Len);

#ifdef __cplusplus									/* for C++ compiler */
	}
#endif

#endif	/* __MCUSERIALPORT_H__ */
