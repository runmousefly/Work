
#ifndef  __MCU_H__
#define  __MCU_H__

#include "mcu_type.h"

#define MCU_DEBUG(x)
//#define MCU_DEBUG(x)	x

#ifdef __cplusplus									/* for C++ compiler */
extern "C" {
#endif

extern uint32 GetTickCount(void);


#ifdef __cplusplus									/* for C++ compiler */
	}
#endif

#include "McuExtern.h"

#endif	/* __MCU_H__ */
