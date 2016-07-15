/*
**
** Copyright 2008, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License"); 
** you may not use this file except in compliance with the License. 
** You may obtain a copy of the License at 
**
**     http://www.apache.org/licenses/LICENSE-2.0 
**
** Unless required by applicable law or agreed to in writing, software 
** distributed under the License is distributed on an "AS IS" BASIS, 
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
** See the License for the specific language governing permissions and 
** limitations under the License.
*/

#define LOG_TAG "mcu"

//#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
//#include <dirent.h>
//#include <errno.h>

#include <unistd.h>
#include <time.h>

//#include <pwd.h>
//#include <pthread.h>

#include <utils/Log.h>

#include <private/android_filesystem_config.h>

// Nicolas-open root right
#define _REALLY_INCLUDE_SYS__SYSTEM_PROPERTIES_H_
#include <sys/_system_properties.h>

#include "mcu.h"
#include "McuUart.h"
#include "McuSerialport.h"
#include "McuComm.h"

#define LOG_TAG "[MCT_MCU]"

#define IAP_CMD_TIMEOUT	10000

//iap sub cmd
#define CMD_IAP_START	0x00
#define CMD_IAP_EXIT	0x01
#define CMD_IAP_ERASE	0x02
#define CMD_IAP_WRITE	0x03
#define CMD_IAP_READ	0x04
#define CMD_IAP_BLANK	0x05
#define CMD_IAP_VERIFY	0x06
#define CMD_ACK_START	0x80
#define CMD_ACK_EXIT	0x81
#define CMD_ACK_ERASE	0x82
#define CMD_ACK_WRITE	0x83
#define CMD_ACK_READ	0x84
#define CMD_ACK_BLANK	0x85
#define CMD_ACK_VERIFY	0x86


#define MCU_USER_SPACE_SIZE 0x7800

typedef enum
{
	NO_ERROR,
	NOT_SUPPORT,
	ERASE_ERROR,
	ADDRESS_ERROR,
	WRITE_ERROR,
	READ_ERROR,
	NOT_BLANK,
}IapErrCode;

#define HDR_SIZE 128
#define PRODUCT_NAME_LEN  32
#define HDR_VERSION 1
#define HDR_MARK 0x55AA55AA

typedef struct {
	unsigned int mhdr_version;
	unsigned int mhdr_mark;
	unsigned int img_size;
	char product_name[PRODUCT_NAME_LEN];
	char resolved[HDR_SIZE - PRODUCT_NAME_LEN - sizeof(int) * 5];
	unsigned int img_checksum;
	unsigned int hdr_checksum;
}mhdr_type;

static bool l_bThreadRun = 0;
static struct ProtocolPackT l_Pack;
static char l_McuVersion[20];
static char l_McuType[8];
static char l_McuDesc[80];
static int l_BattVoltage;
static int l_SleepReason;

static mhdr_type mhdr;

uint32 GetTickCount(void)
{
	struct timespec ts;
	clock_gettime(CLOCK_MONOTONIC, &ts);
	return (uint32)(ts.tv_sec * 1000 + ts.tv_nsec / 1000000);
}

static int count = 0;
void McuHelp(void);
void McuTest(void);
#if 1
int main(int argc, char **argv)
{
    int rc;
    char* str;
    int* value;
	if(argc <= 1)
	{
		#ifdef _MCU_TEST_
		McuTest();
		#else
		McuHelp();
		#endif
		return 0;
	}
	switch(argv[1][0])
	{
	case '-':
		switch(tolower(argv[1][1]))
		{
		case 'v':
			str = McuGetVersion();
			if(str == NULL)
			{
			    printf("McuGetVersion return error\n");
			}
			else
			{
			    printf("get: %s\n", str);
			}
			return 0;
		case 'b':
			value = McuGetBattvoltage();
			if(value == NULL)
			{
			    printf("McuGetBattvoltage return error\n");
			}
			else
			{
			    printf("get: %d\n", *value);
			}
			return 0;
		case 't':
			str = McuGetType();
			if(str == NULL)
			{
			    printf("McuGetType return error\n");
			}
			else
			{
			    printf("get: %s\n", str);
			}
			return 0;
		case 'd':
			str = McuGetDescription();
			if(str == NULL)
			{
			    printf("McuGetDescription return error\n");
			}
			else
			{
			    printf("get: %s\n", str);
			}
			return 0;
		case 's':
			value = McuGetSleepReason();
			if(value == NULL)
			{
			    printf("McuGetSleepReason return error\n");
			}
			else
			{
			    printf("get: %d\n", *value);
			}
			return 0;
		case 'r':
			rc = McuReset();
			if(rc < 0)
			{
			    printf("error code : %d\n", -rc);
			}
			else
			{
			    printf("get: OK\n");
			}
			return 0;
		case 'u':
			str = argv[2];
			
			rc = McuUpdate(str);
			if(rc < 0)
			{
			    printf("McuUpdate error code : %d\n", -rc);
			}
			else
			{
			    printf("McuUpdate: OK\n");
			}
			return 0;
		default:
			break;
		}
		break;
	default:
		break;
	}
	McuHelp();
	return 0;
}
#endif

void McuHelp(void)
{
	printf("MCU command and test program\n");
	#ifdef _MCU_TEST_
	printf("mcu               - open test program\n");
	#endif
	printf("mcu -v            - get MCU version\n");
	printf("mcu -b            - get MCU voltage\n");
	printf("mcu -t            - get MCU type\n");
	printf("mcu -d            - get MCU description\n");
	printf("mcu -s            - get last sleep reason\n");
	printf("mcu -r            - reset MCU\n");
	printf("mcu -u            - update MCU\n");
	printf("mcu *             - show help message\n");
}

static void sleep_ms(int ms)
{
	struct timeval timeout;
	timeout.tv_sec = 0;
	timeout.tv_usec = ms*1000;
	select( 0, NULL, NULL, NULL, &timeout );
}

void McuTest(void)
{
	char input_cmd[260];
	char ch;
	bool exit = false;
	bool open = false;
	printf("welcom to MCU test program\n");
	printf("e ==> exit\n");
	printf("o ==> open\n");
	printf("c ==> close\n");
	printf("a ==> pack type: AA\n");
	printf("b ==> pack type: BB\n");
	printf("m ==> pack type: Manaual\n");
	while(1)
	{
		if(exit)
		{
			break;
		}
		scanf("%c", &ch);
		switch(tolower(ch))
		{
		case 'o':
			printf("open\n");
			open = McuComm_Open();
			break;
		case 'c':
			printf("close\n");
			McuComm_Close();
			break;
		case 'e':
			printf("exit\n");
			if(open)
			{
				McuComm_Close();
			}
			exit = true;
			break;
		case 'a':
			printf("pack type: AA\n");
			McuComm_Type(ch);
			break;
		case 'b':
			printf("pack type: BB\n");
			McuComm_Type(ch);
			break;
		case 'm':
			printf("pack type: Manaual\n");
			McuComm_Type(ch);
			break;
		case '\n':
			break;
		case '\r':
			break;
		default:
			printf("%c not support\n", ch);
			break;
		}
	}
	printf("MCU test program exit\n");
}

void McuCopyString(char *tag, char *src, int len)
{
    int i;
    for(i=0; i<len; i++)
    {
        if(src[i] == '\0')
        {
            break;
        }
        tag[i] = src[i];
    }
    tag[i] = '\0';
}

char* McuGetVersion(void)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		//printf("can't open mcu\n");
		ALOGE("can't open mcu\n");
		return NULL;
	}
	l_Pack.m_pu8Data[0] = 0x02;			// get
	l_Pack.m_pu8Data[1] = 0x03;			// cmd_version
	McuComm_Send(1, l_Pack.m_pu8Data, 2, false);
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > 1000)
		{
			ALOGE("timeout\n");
			rc = -EBUSY;
			break;
		}
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01)
				{
					if(l_Pack.m_u8Length > 2)
					{
						if(l_Pack.m_pu8Data[1] == 0x03)
						{
						    McuCopyString(l_McuVersion, &l_Pack.m_pu8Data[2], l_Pack.m_u8Length-2);
							ALOGD("%s\n", (char*)(&l_Pack.m_pu8Data[2]));
							break;
						}
					}
				}
			}
		}
	}
	McuComm_Close();
	if(rc < 0)
	{
	    return NULL;
	}
	return l_McuVersion;
}

int* McuGetBattvoltage(void)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		ALOGE("can't open mcu\n");
		return NULL;
	}
	l_Pack.m_pu8Data[0] = 0x02;			// get
	l_Pack.m_pu8Data[1] = 0x11;			// cmd_voltage
	McuComm_Send(1, l_Pack.m_pu8Data, 2, false);
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > 1000)
		{
			ALOGE("timeout\n");
			rc = -EBUSY;
			break;
		}
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01)
				{
					if(l_Pack.m_u8Length > 2)
					{
						if(l_Pack.m_pu8Data[1] == 0x11)
						{
						    l_BattVoltage = l_Pack.m_pu8Data[2];
							float f_vol = ((float)(l_Pack.m_pu8Data[2])) / 10;
							ALOGD("%0.1f\n", f_vol);
							break;
						}
					}
				}
			}
		}
	}
	McuComm_Close();
	if(rc < 0)
	{
	    return NULL;
	}
	return &l_BattVoltage;
}

char* McuGetType(void)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		ALOGE("can't open mcu\n");
		return NULL;
	}
	l_Pack.m_pu8Data[0] = 0x02;			// get
	l_Pack.m_pu8Data[1] = 0x04;			// cmd_mcu_info
	l_Pack.m_pu8Data[2] = 0x00;			// mcu_type
	McuComm_Send(1, l_Pack.m_pu8Data, 3, false);
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > 1000)
		{
			ALOGE("timeout\n");
			rc = -EBUSY;
			break;
		}
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01)
				{
					if(l_Pack.m_u8Length > 2)
					{
						if((l_Pack.m_pu8Data[1] == 0x04) && (l_Pack.m_pu8Data[2] == 0x00))
						{
						    McuCopyString(l_McuType, &l_Pack.m_pu8Data[3], l_Pack.m_u8Length-3);
							printf("%s\n", (char*)(&l_Pack.m_pu8Data[3]));
							break;
						}
					}
				}
			}
		}
	}
	McuComm_Close();
	if(rc < 0)
	{
	    return NULL;
	}
	return l_McuType;
}

char* McuGetDescription(void)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		ALOGE("can't open mcu\n");
		return NULL;
	}
	l_Pack.m_pu8Data[0] = 0x02;			// get
	l_Pack.m_pu8Data[1] = 0x04;			// cmd_mcu_info
	l_Pack.m_pu8Data[2] = 0x01;			// mcu_description
	McuComm_Send(1, l_Pack.m_pu8Data, 3, false);
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > 1000)
		{
			ALOGE("timeout\n");
			rc = -EBUSY;
			break;
		}
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01)
				{
					if(l_Pack.m_u8Length > 2)
					{
						if((l_Pack.m_pu8Data[1] == 0x04) && (l_Pack.m_pu8Data[2] == 0x01))
						{
						    McuCopyString(l_McuDesc, &l_Pack.m_pu8Data[3], l_Pack.m_u8Length-3);
							printf("%s\n", (char*)(&l_Pack.m_pu8Data[3]));
							break;
						}
					}
				}
			}
		}
	}
	McuComm_Close();
	if(rc < 0)
	{
	    return NULL;
	}
	return l_McuDesc;
}

int* McuGetSleepReason(void)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		ALOGE("can't open mcu\n");
		return NULL;
	}
	l_Pack.m_pu8Data[0] = 0x02;			// get
	l_Pack.m_pu8Data[1] = 0x05;			// cmd_sys_status
	l_Pack.m_pu8Data[2] = 0x00;			// sleep_reason
	McuComm_Send(1, l_Pack.m_pu8Data, 3, false);
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > 1000)
		{
			ALOGE("timeout\n");
			rc = -EBUSY;
			break;
		}
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01)
				{
					if(l_Pack.m_u8Length > 2)
					{
						if((l_Pack.m_pu8Data[1] == 0x05) && (l_Pack.m_pu8Data[2] == 0x00))
						{
						    l_SleepReason = l_Pack.m_pu8Data[3];
							printf("%d\n", l_Pack.m_pu8Data[3]);
							break;
						}
					}
				}
			}
		}
	}
	McuComm_Close();
	if(rc < 0)
	{
	    return NULL;
	}
	return &l_SleepReason;
}

int McuReset(void)
{
	int i;
	if(!McuComm_Open())
	{
		ALOGE("can't open mcu\n");
		return -ENODEV;
	}
	l_Pack.m_pu8Data[0] = 0x03;			// msg
	l_Pack.m_pu8Data[1] = 0x00;			// cmd_reset
	l_Pack.m_pu8Data[2] = 0x19;
	l_Pack.m_pu8Data[3] = 0x82;
	l_Pack.m_pu8Data[4] = 0x06;
	l_Pack.m_pu8Data[5] = 0x12;
	for(i=0; i<5; i++)
	{
		McuComm_Send(1, l_Pack.m_pu8Data, 6, false);
		sleep_ms(200);
	}
	McuComm_Close();
	return 0;
}


static void show_iap_err_log(unsigned char log)
{
	switch(log)
	{
		case NOT_SUPPORT:printf("NOT_SUPPORT\n");break;
		case ERASE_ERROR:printf("ERASE_ERROR\n");break;
		case ADDRESS_ERROR:printf("ADDRESS_ERROR\n");break;
		case WRITE_ERROR:printf("WRITE_ERROR\n");break;
		case READ_ERROR:printf("READ_ERROR\n");break;
		case NOT_BLANK:printf("NOT_BLANK\n");break;
		default:break;
	}
}


static int iap_start(void)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		printf("can't open mcu\n");
		return -1;
	}
	
	l_Pack.m_pu8Data[0] = 0x01;			// set
	l_Pack.m_pu8Data[1] = 0x0F;			// cmd_iap
	l_Pack.m_pu8Data[2] = CMD_IAP_START;
	McuComm_Send(1, l_Pack.m_pu8Data, 5, false);
	
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > IAP_CMD_TIMEOUT)
		{
			printf("timeout\n");
			rc = -1;
			break;
		}
		
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01 && l_Pack.m_pu8Data[2] == CMD_ACK_START)
				{
					if(l_Pack.m_pu8Data[3] == NO_ERROR)//NO ERROR
					{
						rc = 0;
					}
					else
					{
						show_iap_err_log(l_Pack.m_pu8Data[3]);
						rc = -1;
					}
					break;
				}
			}
		}
	}
	McuComm_Close();
	return rc;
}




static int iap_erase(int* pMcuProAddBuf) 
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		printf("can't open mcu\n");
		return -1;
	}
	
	l_Pack.m_pu8Data[0] = 0x01;			// set
	l_Pack.m_pu8Data[1] = 0x0F;			// cmd_iap
	l_Pack.m_pu8Data[2] = CMD_IAP_ERASE;
	McuComm_Send(1, l_Pack.m_pu8Data, 3, false);
	
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > IAP_CMD_TIMEOUT)
		{
			printf("timeout\n");
			rc = -1;
			break;
		}
		
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01 && l_Pack.m_pu8Data[2] == CMD_ACK_ERASE)
				{
					if(l_Pack.m_pu8Data[3] == NO_ERROR)//NO ERROR
					{
						*pMcuProAddBuf = l_Pack.m_pu8Data[4] << 24 | l_Pack.m_pu8Data[5] << 16 | l_Pack.m_pu8Data[6] << 8 | l_Pack.m_pu8Data[7];
						//memcpy(pMcuProAddBuf, &l_Pack.m_pu8Data[4], 4);
						rc = 0;
					}
					else
					{
						show_iap_err_log(l_Pack.m_pu8Data[3]);
						rc = -1;
					}
					break;
				}
			}
		}
	}
	McuComm_Close();
	return rc;
}

static int iap_write(unsigned long addr, unsigned char *buffer, unsigned int len)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		printf("can't open mcu\n");
		return -1;
	}
	l_Pack.m_pu8Data[0] = 0x01;			// set
	l_Pack.m_pu8Data[1] = 0x0F;			// cmd_iap
	l_Pack.m_pu8Data[2] = CMD_IAP_WRITE;
	l_Pack.m_pu8Data[3] = (addr >> 24) & 0xFF;
	l_Pack.m_pu8Data[4] = (addr >> 16) & 0xFF;
	l_Pack.m_pu8Data[5] = (addr >> 8) & 0xFF;
	l_Pack.m_pu8Data[6] = addr & 0xFF;
	memcpy(&l_Pack.m_pu8Data[7], buffer, len);
	McuComm_Send(1, l_Pack.m_pu8Data, 7+len, false);
	
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > IAP_CMD_TIMEOUT)
		{
			printf("timeout\n");
			rc = -1;
			break;
		}
		
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01 && l_Pack.m_pu8Data[2] == CMD_ACK_WRITE)
				{
					if(l_Pack.m_pu8Data[3] == NO_ERROR)//NO ERROR
					{
						rc = 0;
					}
					else
					{
						show_iap_err_log(l_Pack.m_pu8Data[3]);
						rc = -1;
					}
					break;
				}
			}
		}
	}
	McuComm_Close();
	return rc;
}

static int iap_read(unsigned long addr, unsigned char *buffer, unsigned int len)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		printf("can't open mcu\n");
		return -1;
	}
	
	l_Pack.m_pu8Data[0] = 0x02;			// get
	l_Pack.m_pu8Data[1] = 0x0F;			// cmd_iap
	l_Pack.m_pu8Data[2] = CMD_IAP_READ;
	l_Pack.m_pu8Data[3] = (addr >> 24) & 0xFF;
	l_Pack.m_pu8Data[4] = (addr >> 16) & 0xFF;
	l_Pack.m_pu8Data[5] = (addr >> 8) & 0xFF;
	l_Pack.m_pu8Data[6] = addr & 0xFF;
	
	l_Pack.m_pu8Data[7] = 0;
	l_Pack.m_pu8Data[8] = 0;
	l_Pack.m_pu8Data[9] = (len >> 8) & 0xFF;
	l_Pack.m_pu8Data[10] = len & 0xFF;
	
	McuComm_Send(1, l_Pack.m_pu8Data, 11, false);
	
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > IAP_CMD_TIMEOUT)
		{
			printf("timeout\n");
			rc = -1;
			break;
		}
		
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01 && l_Pack.m_pu8Data[2] == CMD_ACK_READ)
				{
					if(l_Pack.m_pu8Data[3] == NO_ERROR)//NO ERROR
					{
						rc = 0;
						memcpy(buffer, &l_Pack.m_pu8Data[8], len);
					}
					else
					{
						show_iap_err_log(l_Pack.m_pu8Data[3]);
						rc = -1;
					}
					break;
				}
			}
		}
	}
	McuComm_Close();
	return rc;
}

static int iap_verify(int *app_checksum, int *boot_check_sum)
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		printf("can't open mcu\n");
		return -1;
	}
	
	l_Pack.m_pu8Data[0] = 0x02;			// get
	l_Pack.m_pu8Data[1] = 0x0F;			// cmd_iap
	l_Pack.m_pu8Data[2] = CMD_IAP_VERIFY;
	McuComm_Send(1, l_Pack.m_pu8Data, 3, false);
	
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > IAP_CMD_TIMEOUT)
		{
			printf("timeout\n");
			rc = -1;
			break;
		}
		
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01 && l_Pack.m_pu8Data[2] == CMD_ACK_VERIFY)
				{
					if(l_Pack.m_pu8Data[3] == NO_ERROR)//NO ERROR
					{
						//memcpy(boot_check_sum, &l_Pack.m_pu8Data[4], 4);
						//memcpy(app_checksum, &l_Pack.m_pu8Data[8], 4);
						*boot_check_sum = l_Pack.m_pu8Data[4] << 24 | l_Pack.m_pu8Data[5] << 16 | l_Pack.m_pu8Data[6] << 8 | l_Pack.m_pu8Data[7];
						*app_checksum = l_Pack.m_pu8Data[8] << 24 | l_Pack.m_pu8Data[9] << 16 | l_Pack.m_pu8Data[10] << 8 | l_Pack.m_pu8Data[11];
						rc = 0;
					}
					else
					{
						show_iap_err_log(l_Pack.m_pu8Data[3]);
						rc = -1;
					}
					break;
				}
			}
		}
	}
	McuComm_Close();
	return rc;
}

static int iap_exit()
{
	int rc;
	uint32 timeout;
	if(!McuComm_Open())
	{
		printf("can't open mcu\n");
		return -1;
	}
	
	l_Pack.m_pu8Data[0] = 0x01;			// set
	l_Pack.m_pu8Data[1] = 0x0F;			// cmd_iap
	l_Pack.m_pu8Data[2] = CMD_IAP_EXIT;
	McuComm_Send(1, l_Pack.m_pu8Data, 3, false);
	
	timeout = GetTickCount();
	rc = 0;
	while(1)
	{
		sleep_ms(10);
		if((GetTickCount()-timeout) > IAP_CMD_TIMEOUT)
		{
			printf("timeout\n");
			rc = -1;
			break;
		}
		
		if(McuUartGetPack(&l_Pack))
		{
			if((l_Pack.m_u8Type == 0xAA) || (l_Pack.m_u8Type == 0xBB))
			{
				if(l_Pack.m_u8Device == 0x01 && l_Pack.m_pu8Data[2] == CMD_ACK_EXIT)
				{
					if(l_Pack.m_pu8Data[3] == NO_ERROR)//NO ERROR
					{
						rc = 0;
					}
					else
					{
						show_iap_err_log(l_Pack.m_pu8Data[3]);
						rc = -1;
					}
					break;
				}
			}
		}
	}
	McuComm_Close();
	return rc;
}

int checkUpdateFile(char *update_file) {
	FILE *fp;
	unsigned int img_checksum = 0, hdr_checksum = 0;
	int index;
	int total_size = 0, count;
	char r_buf[128];
	
	memset((void *)&mhdr, 0, sizeof(mhdr_type));
	
	if(update_file == NULL) {
		printf("update file is null\n");
		return -1;
	}
	
	fp = fopen(update_file, "rb");
	if(fp == NULL) {
		printf("can't not open file %s\n", update_file);
		return -1;
	}
	
	count = fread(&mhdr, 1, sizeof(mhdr_type), fp);
	if(count != sizeof(mhdr_type)) {
		printf("update file read failed \n");
		fclose(fp);
		return -1;
	}
	
	// check header
	if(mhdr.mhdr_mark != HDR_MARK) {
		printf("mark not match\n");
		fclose(fp);
		return -1;
	}
	
	for(index = 0; index < (sizeof(mhdr_type) - sizeof(int)); index++) {
		hdr_checksum += (unsigned int)((unsigned char *)&mhdr)[index];
	}
	
	if(mhdr.hdr_checksum != hdr_checksum) {
		printf("hdr checksum not match\n");
		fclose(fp);
		return -1;
	}
	
	while(feof(fp) == 0) {
		count = fread(r_buf, 1, 128, fp);
		
		for(index = 0; index < count; index ++) {		
			img_checksum += (unsigned int)r_buf[index];
		}
		
		total_size += count;
	}
	
	if(total_size != mhdr.img_size) {
		printf("file size not match\n");
		fclose(fp);
		return -1;
	}
	
	if(img_checksum != mhdr.img_checksum){
		printf("image checksum not match, mhdr img checksum %x, img checksum %x\n", mhdr.img_checksum, img_checksum);
		fclose(fp);
		return -1;
	}
	
	fclose(fp);
	
	return 0;
}

int McuUpdate(char *update_file) {
	int ret;
	FILE *fp;
	char w_buf[128], r_buf[128];
	int index = 0, cound = 0, total_size = 0, erase_addr = 0;
	int org_checksum = 0, app_checksum = 0, boot_checksum = 0;
	
	if(update_file == NULL) {
		printf("update file is null\n");
		return -1;
	}
	
	ret = checkUpdateFile(update_file);
	if(ret < 0){
		return ret;
	}
	
	fp = fopen(update_file, "rb");
	if(fp == NULL) {
		printf("can't not open update file %s\n", update_file);
		return -1;
	}
	
	// not to download hdr
	count = fread(&mhdr, 1, sizeof(mhdr_type), fp);
	if(count != sizeof(mhdr_type)) {
		printf("update file read failed \n");
		fclose(fp);
		return -1;
	}
	
	// stage 1: start updating
	printf("stage 1: start updating mcu");
	ret = iap_start();
	if(ret < 0)
	{
		printf("mcu update start failed\n");
		fclose(fp);
		return ret;
	}
	
	// stage 2: erase 
	printf("stage 2: erase mcu");
	ret = iap_erase(&erase_addr);
	if(ret < 0)
	{
		printf("mcu erase failed\n");
		fclose(fp);
		return ret;
	}
	printf("erase start address %x\n", erase_addr);
	
	// stage 3: writting
	while(feof(fp) == 0) {
		count = fread(w_buf, 1, 128, fp);
		printf("read %d bytes from update file\n", count);
		
		for(index = 0; index < count; index ++) {
			if (index % 16 == 0)
				printf("\n");
				
			printf("0x%2x ", w_buf[index]);
			
			org_checksum += (unsigned int)w_buf[index];
		}
		
		printf("\n");
		
		// try to write mcu
		ret = iap_write(erase_addr, w_buf, count);
		if(ret < 0){
			printf("write mcu failed\n");
			fclose(fp);
		  return ret;
		}
		
		ret = iap_read(erase_addr, r_buf, count);
		if(ret < 0){
			printf("read mcu failed\n");
			fclose(fp);
		  return ret;
		}
		
		for(index = 0; index < count; index++)
		{
			if(w_buf[index] != r_buf[index]) {
				printf("write wrong data\n");
				fclose(fp);
		    return -1;
			}
		}
		
		total_size += count;
		erase_addr += count;
	}
	printf("update file total size %x\n", total_size);
	org_checksum += (unsigned long)0xFF * (MCU_USER_SPACE_SIZE - total_size);
	printf("update file check sum %x\n", org_checksum);
	
	
	// stage 4: verify
	ret = iap_verify(&app_checksum, &boot_checksum);
	if(ret < 0)
	{
		printf("get verify failed\n");
		fclose(fp);
		return ret;
	}
	printf("got app checksum %x, boot checksum %x\n", app_checksum, boot_checksum);
	if(org_checksum != app_checksum) {
		printf("mcu update verify failed\n");
		fclose(fp);
		return -1;
	}
	
	// stage 5: exit
	ret = iap_exit();
	if(ret < 0)
	{
		printf("mcu exit failed\n");
		fclose(fp);
		return ret;
	}
	
	printf("mcu update successfully!\n ");
	
	printf("\n mcu Reset!\n ");
	McuReset();
	return 0;
}
