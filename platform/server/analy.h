/*命令解析返回值*/
#define OMC_DONE_OK				0x00
#define OMC_DONE_FAIL				0xFF
#define OMC_CMD_LENERR			0x01	//命令包长度有误
#define OMC_CMD_APPERR			0x02   	//AP层协议型类有误
#define OMC_CMD_LDPERR			0x03	//承载层协议类型有误
#define OMC_CMD_CRCERR			0x04	//命令包CRC校验出错
#define OMC_CMD_ADDERR			0x05	//命令包地址校验不通过(即非本设备的命令包)
#define OMC_CMD_VPSFERR			0x06	//VP层交互标志校验出错
#define OMC_CMD_CPIDERR			0x07	//CPID校验出错
#define OMC_CMD_CMDIDERR 			0x08	//命令标识校验出错	
#define OMC_CMD_CMDRFERR			0x09	//命令包应答标志有误
#define OMC_CMD_DONECONDITION		0x7E	//命令被有条件执行
#define OMC_DONE_SPECIALCMD		0x80    //执行了特殊命令
#define OMC_DEVNO_ERR				0xFF			 
#define OMC_SITNO_ERR				0xFE

#define OMC_MCPA					0x01	/*监控控制层协议标识:MCPA协议	*/
#define OMC_MCPB					0x02	/*监控控制层协议标识:MCPB协议	*/

#define OMC_CMD_REPORT			0x01	/*命令标识:主动上报				*/
#define OMC_CMD_QUERY				0x02	/*命令标识:查询					*/
#define OMC_CMD_SET				0x03	/*命令标识:设置					*/
#define OMC_CMD_UPDATE			0x10	/*命令标识:转换到软件升级模式	*/
#define OMC_CMD_SVERSW			0x11	/*命令标识:切换监控软件版本		*/
#define OMC_CMD_ADMIN				0x8000  /*命令标识:管理员命令(存于高8位而低8位(必须为0)用于上述的其它命令标识)*/
#define OMC_IsAdminCmd(pcmd)		((pcmd[14] == 0x03)&&(pcmd[15] == 0xC3)&&(pcmd[16] == 0x01))/*管理员命令*/

#define OMC_CMDID_UNRGNZ			0x10	/*监控数据标识无法识别					*/
#define OMC_SETVAL_OUTOFRANGE		0x20 	/*监控数据的设置值超出范围				*/
#define OMC_IDANDDA_NOMATCH		0x30	/*监控数据标识与监控数据的值不符合要求	*/
#define OMC_IDANDLEN_NOMATCH  	0x40	/*监控数据标识与监控数据长度不匹配		*/
#define OMC_DETECTVAL_TOOLOW		0x50	/*监控数据的检测值低于工作范围			*/
#define OMC_DETECTVAL_TOOHIGH		0x60	/*监控数据的检测值高于工作范围			*/
#define OMC_VAL_CANNOTDETECT		0x70	/*监控数据无法检测						*/
#define OMC_OTHER_UNLISTERR		0x90	/*未列出的其它错误						*/
#define OMC_ErrCmdDeal(pdu,errflg)  {pdu[2] &= 0x0F; pdu[2] |= errflg;}  /*监控对像出错处理*/

#define OMC_APACMD_MAXLEN			1500		/*APA命令包的最大长度(转义前/反转义后(包括头和尾))				*/
#define OMC_APBCMD_MAXLEN			71	  	/*APB命令包的最大长度(ACSII拆分前/反拆分后的长度(包括头和尾))	*/ 
#define OMC_APCCMD_MAXLEN			1502  	/*APC命令包的最大长度(转义前/反转义后(包括头和尾))				*/

#define DEV_INFO_LEN_MAX            200

#ifndef __ANALY_H_
#define __ANALY_H_

#define OMCPA_PACKAGE_MAX_LEN   1024
#define OMCA_MEM_MAX_BUF_LEN    1500


#define OS_COUNTOF(a) (sizeof(a) / sizeof(a[0]))


#include "thread.h"

typedef unsigned int   u32_t;
typedef unsigned short u16_t;
typedef unsigned char  u8_t;
#if 0
typedef enum {

#ifdef OMCA_CMD_DEF
#undef OMCA_CMD_DEF
#endif
#define OMCA_CMD_DEF(CMD_ID,PDU_LEN,PNAME,FUNCB)  {CMD_ID,PDU_LEN,PNAME,FUNCB}
	#include "omcpa.def"
	OMCA_CMD_MAX
}OMCA_CMD_ID_E;
#endif
 
 

 
#include <sys/socket.h>
#include <sys/un.h>
#include <sys/resource.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>

struct message_inform {
	int sd;
	char ip[16];
	u16_t port;
    struct sockaddr_in addr;
};


u32_t OMCGetSitNo(void);
u32_t OMCGetDevNo(void);
u8_t OMCGetEfmNo(void);
u8_t analy_message(struct message_inform *inform, u8_t *buf, u16_t size);
u16_t HDP_ApaApcReback(u8_t *pcmd, u16_t cmdlen);
u8_t OMC_ApProtocolVef(u8_t *pcmd);
u8_t HDP_CrcVef(u8_t *pcmd, u16_t cmdlen);
u8_t OMC_AddrVef(u8_t *pcmd);
u8_t OMC_McpaInput(u8_t *buf, u16_t len, struct message_inform *inform);
u8_t OMC_McpaLenVef(u8_t *pcmd, u16_t cmdlen);
u8_t OMCPA_CmdReplyToOmc(u8_t *buf, u16_t len, struct message_inform *inform);
u8_t OMC_McpaPduAnaly(u8_t *pcmd, u16_t cmdlen, struct message_inform *inform);
u16_t HDP_ApaApcEscape(u8_t *dest, u16_t destmaxlen, u8_t *pcmd, u16_t cmdlen);
u8_t sendAlarm(u8_t device_code);
u8_t createSite(u32_t site_code, u8_t device_code, struct message_inform *inform);
u8_t loginSite(u32_t site_code, u8_t device_code, struct message_inform *inform);
u8_t voiceState(u32_t site_code, u8_t device_code);
u8_t alarmReset(u32_t site_code, u8_t device_code);
u8_t OMC_MCPB_Input(u8_t *buf, u16_t len);
u16_t ustrcalcrc(u8_t *pStr, u32_t len);
u8_t OMCPA_CmdToDev(u8_t *buf, u16_t len, struct message_inform *inform);
void post_curl(u8_t *url,u8_t *postData);
void init_mysql(u32_t *mysql);
void mysql_insert(u32_t *mysql , u8_t *query);
u8_t OMCPA_CmdReplyToOmc(u8_t *pcmd, u16_t cmdlen,struct message_inform *inform);
#endif

