//#include <sys/types.h>
//#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
//#include <errno.h>
//#include <unistd.h>
#include <stdlib.h>
#include <stdbool.h>
#include <termios.h>
#include <pthread.h>
//#include <utils/Log.h>

#include "SerialPort.h"

static int l_FileDescriptor = -1;
static pthread_t l_TRxThread;
static bool l_bTRxThreadStop = false;
static bool l_bTRxThreadRun = false;

static void
sleep_ms(int ms);
static void
Serial_Read_Thread(void);

bool
Serialport_Open(const char* pszPort)
{
    if (l_FileDescriptor >= 0)
    {
        LOGI("SERIAL_PORT: has opened.\n");
        return false;
    }

    l_bTRxThreadRun = false;
    l_bTRxThreadStop = false;
    int error = pthread_create(&l_TRxThread, null, (void*) Serial_Read_Thread, null);
    if (error != 0)
    {
        LOGE("SERIAL_PORT: create Serial_Read_Thread error\n");
        return false;
    }
    l_bTRxThreadRun = true;

    l_FileDescriptor = open(pszPort, O_RDWR | O_NOCTTY | O_NDELAY);
    //l_FileDescriptor = open(pszPort, O_RDWR);
    if (l_FileDescriptor < 0)
    {
        LOGI("SERIAL_PORT: open error.\n");
        l_bTRxThreadStop = true;
        while (l_bTRxThreadRun)
        {
            sleep_ms(5); //5ms
        }
        return false;
    }
    // configure port
    struct termios tio;
    if (tcgetattr(l_FileDescriptor, &tio))
    {
        memset(&tio, 0, sizeof(tio));
    }
    tio.c_cflag = B9600 | CS8 | CLOCAL | CREAD;
    tio.c_oflag &= ~OPOST;
    tio.c_iflag = IGNPAR;
    tio.c_lflag = 0;
    tio.c_cc[VTIME] = 0;
    //tio.c_cc[VMIN] = 1;
    tio.c_cc[VMIN] = 0;
    tcsetattr(l_FileDescriptor, TCSANOW, &tio);
    tcflush(l_FileDescriptor, TCIFLUSH);
    LOGI("SERIAL_PORT: opened. ==>%d.\n", l_FileDescriptor);

    return true;
}

bool
Serialport_Close(void)
{
    LOGI("SERIAL_PORT close begin");
    if (l_FileDescriptor < 0)
    {
        LOGI("SERIAL_PORT: has closed.\n");
        return false;
    }
    l_bTRxThreadStop = true;
    while (l_bTRxThreadRun)
    {
        sleep_ms(5); //5ms
    }
    LOGI("SERIAL_PORT: Read Thread Exit.\n");
    close(l_FileDescriptor);
    l_FileDescriptor = -1;
    LOGI("SERIAL_PORT: closed.\n");
    return true;
}

void
Serialport_Clear(void)
{
    if (l_FileDescriptor < 0)
    {
        return;
    }
    tcflush(l_FileDescriptor, TCIOFLUSH);
}

bool
Serialport_IsOpen(void)
{
    return ((l_FileDescriptor >= 0) ? true : false);
}

bool
Serialport_Write(uint8* pu8Buf, uint16 u16Len)
{
    LOGD("SERIAL_PORT, Serialport_Write data: ");
    int i = 0;
    for (i = 0; i < u16Len; i++)
    {
        LOGI(" %02X", pu8Buf[i]);
    }

    int len = -1;
    if (l_FileDescriptor < 0)
    {
        LOGD("SERIAL_PORT: Serialport_Write,l_FileDescriptor < 0");
        return false;
    }
    len = write(l_FileDescriptor, pu8Buf, u16Len);
    if (len < 0)
    {
        LOGD("SERIAL_PORT: Serialport_Write failed,len < 0");
        return false;
    }
    LOGD("SERIAL_PORT: Serialport_Write,length:%d", len);
    return true;
}

bool
Serialport_Read(uint8* pu8Buf, uint16* pu16ReadLen, uint16 u16Len)
{
    int len;
    if (l_FileDescriptor < 0)
    {
        LOGD("SERIAL_PORT: read data failed,serial is close");
        *pu16ReadLen = 0;
        return false;
    }
    len = read(l_FileDescriptor, pu8Buf, u16Len);
    if (len < 0)
    {
        //LOGD("SERIAL_PORT: read data null");
        return false;
    }
    *pu16ReadLen = (uint16) len;
    //LOGD("Serialport_Read,length:%d",len);
    return true;
}

static void
Serial_Read_Thread(void)
{
    int error;
    uint8 buf[200];
    uint16 len;
    uint16 i;
    LOGI("SERIAL_PORT:  Thread start\n");
    while (1)
    {
        if (l_bTRxThreadStop)
        {
            break;
        }
        //读取串口数据
        len = 0;
        Serialport_Read(buf, &len, 200);
        //返回给JAVA层
        if (len > 0)
        {
            LOGI("SERIAL_PORT: receive new data from obd,length:%d", len);
            onReceiveData(len, buf);
        }
        usleep(100 * 1000); //100ms读一次
    }
    LOGI("SERIAL_PORT:  Thread stop\n");
    l_bTRxThreadRun = false;
}

static void
sleep_ms(int ms)
{
    struct timeval timeout;
    timeout.tv_sec = 0;
    timeout.tv_usec = ms * 1000;
    select(0, NULL, NULL, NULL, &timeout);
}
