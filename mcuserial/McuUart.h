
/***************************************************************************************************
 
 File name:		McuUart.h
 Author:		fanxueming
 Date:			2015.9.30
 Description:	stm8 head file

****************************************************************************************************
 Modified by:	
 Date:			
 Description:	
 
***************************************************************************************************/


#ifndef  __MCUUART_H__
#define  __MCUUART_H__

#include "mcu_type.h"

#ifdef __cplusplus									/* for C++ compiler */
extern "C" {
#endif

struct ProtocolPackT
{
	uint8 m_u8Type;
	uint8 m_u8Index;
	uint8 m_u8Device;
	uint8 m_u8Length;
	uint8 m_pu8Data[255];
	uint8 m_u8Checksum;
};


extern void McuUartInit(void);
extern uint32 McuUartGetTxWait(void);
extern void McuUartTxProcess(void);
extern bool McuUartRxAnalyze(uint8 u8Value);
extern bool McuUartGetPack(struct ProtocolPackT* ptPack);
extern bool McuUartPeekPack(struct ProtocolPackT* ptPack);
extern bool McuUartSendPack(uint8 u8Device, uint8* pu8Data, uint8 u8Length, bool bNeedAck);

#ifdef __cplusplus									/* for C++ compiler */
	}
#endif

#endif	/* __MCUUART_H__ */
