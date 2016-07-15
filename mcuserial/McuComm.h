#ifndef __MCUCOMM_H__
#define __MCUCOMM_H__

#ifdef __cplusplus									/* for C++ compiler */
extern "C" {
#endif

extern bool McuComm_Open(void);
extern bool McuComm_Close(void);
extern void McuComm_Type(char cType);
extern void McuComm_Send(uint8 u8Device, uint8* pu8Data, uint8 u8Length, bool bNeedAck);


#ifdef __cplusplus									/* for C++ compiler */
	}
#endif

#endif
