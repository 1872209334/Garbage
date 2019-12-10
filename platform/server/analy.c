#include "analy.h"
#include "omcpa_fn.h"
#define CURL_STATICLIB
#include <sys/socket.h>
#include <sys/un.h>
#include <sys/resource.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <time.h>
#include <curl/curl.h>
#include <hiredis/hiredis.h>
#include <mysql/mysql.h>

/* 回复给上位机的相关异常编号  */
#define     MCPB_CMD_QUERY                0x02                        /*命令标识:查询                */
#define     MCPB_CMD_SET                0x03                        /*命令标识:设置                */
#define     MCPB_CMD_UPDATE                0x10                        /*命令标识:转换到软件升级模式*/
#define     MDPMB_CMD_SVERSW            0x11                        /*命令标识:切换监控软件版本    */
#define        MDPMB_LENGTH_MIN            17                            /* 命令包最小长度            */
#define         MCPB_RPT_MIN_LEN            50    
#define     OMCA_CMD_MAX                300 
#ifdef OMCA_CMD_DEF
#undef OMCA_CMD_DEF
#endif
#define OMCA_CMD_DEF(CMD_ID,DATATYPE,PDU_LEN,PNAME,FUNCB)  {CMD_ID,DATATYPE,PDU_LEN,PNAME,FUNCB}
struct _OMC_PARM{
    u16_t CmdId;
	u8_t datatype;
    u8_t PduLen;
    u8_t *pname;
    u8_t (*DoSpecial)(u16_t cmdflg, u16_t index,u8_t *pdu,u32_t site_code,u8_t device_code , struct message_inform *inform);
};

const struct _OMC_PARM OMCATable[OMCA_CMD_MAX] = {
    #include "omcpa.def"
};
#ifdef DEV_INFO_CMD
#undef DEV_INFO_CMD
#endif
#define DEV_INFO_CMD(INDEX,INFOLEN,PNAME)  {INDEX,INFOLEN,PNAME}
struct _DEV_INFO {
	u16_t index;
	u8_t InfoLen;
	u8_t pname[50];
};
const struct _DEV_INFO DevInfoTable[DEV_INFO_LEN_MAX] = {
	#include "devinfo.def"
};
//u8_t OMCA_MEM_BUF[OMCA_MEM_MAX_BUF_LEN];


/*
const struct _OMC_PARM OMCB_Table[OMCB_CMD_MAX] = {
#include "omcpb.def"
};*/

struct _OMC_DEVDESC
{
    u32_t SitNo;
    u8_t DevNo;
}OMCDevDesc;

void OMCDevAnaly(u8_t *buf, u16_t len)
{
	u32_t flag;
	u32_t id;
    //站点编号
    OMCDevDesc.SitNo = ((buf[6] << 24) & 0xFF000000) | ((buf[5] << 16) & 0x00FF0000) | ((buf[4] << 8) & 0x0000FF00) | buf[3];
    //设备编号
    OMCDevDesc.DevNo = buf[7];

}
u32_t OMCGetSitNo(void)
{
    return OMCDevDesc.SitNo;
}
u32_t OMCGetDevNo(void)
{
    
    return OMCDevDesc.DevNo;
}
u32_t getTime() {
	time_t now;
	now = time(NULL);
	u32_t t = time(&now);
	return t;
}

//处理数据
u8_t analy_message(struct message_inform *inform, u8_t *buf, u16_t len)
{
    u8_t result;
	
    //1、命令包还原
    if (buf[0] == 0x7E) {
        len = HDP_ApaApcReback(buf, len);
    }
    

    //2、命令包下限长度校验
    if (len < 17) {
		printf("OMC_CMD_LENERR1:\n");
        return OMC_CMD_LENERR;
    }

    //3、命令包上限长度校验    
    if (((buf[1] == 0x01) && (len > OMC_APACMD_MAXLEN))) {
		printf("OMC_CMD_LENERR2:\n");
        return OMC_CMD_LENERR;
    }

    /*命令包接入层(AP层)协议相关的校验*/
    //1、AP层协议类型校验
    if (OMC_ApProtocolVef(buf) != OMC_DONE_OK) {
		printf("OMC_CMD_APPERR:\n");
        return OMC_CMD_APPERR;
    }

    //2、承载层协议类型校验
    if (buf[2] != 0x01) {
		printf("OMC_CMD_LDPERR:\n");
        return OMC_CMD_LDPERR;
    }
    //3、CRC校验
    if (HDP_CrcVef(buf, len) != OMC_DONE_OK) {
		printf("OMC_CMD_CRCERR:\n");
        return OMC_CMD_CRCERR;
    }

    /*远程升级数据检测*/
    //OMC_McpbDetect(buf, len);
    /*命令包访问层(VP层)协议相关的校验*/
    
    //4、VP层交互标志校验
    if ((buf[10] != 0x80) && (buf[10] != 0x00)) {
		printf("OMC_CMD_VPSFERR:\n");
        return OMC_CMD_VPSFERR;
    }
	
	//5、记忆地址信息
    OMCDevAnaly(buf, len);
	//该包为心跳数据，记录心跳包
	if (buf[14] == 0x41 && buf[15] == 0x01 && buf[17] == 0x07)
	{

		u32_t time = getTime();
		//心跳包写入redis
		redisContext *rconn = redisConnect("127.0.0.1", 6379); //redis server默认端口
		if (rconn->err) {
			printf("connection to redisServer error: %s\n", rconn->errstr);
			redisFree(rconn);
			return;
		}
		u8_t command[256];
		u8_t command1[256];
		u8_t command2[256];
		u8_t report_ip[16];
		u8_t report_port[16];
		sprintf(report_ip, "%s", inform->ip);
		sprintf(report_port, "%d", inform->port);
		sprintf(command, "set report_%08x%02x %d", OMCGetSitNo(), OMCGetDevNo(),time);
		sprintf(command1, "set ip_%08x%02x %s", OMCGetSitNo(), OMCGetDevNo(), report_ip);
		sprintf(command2, "set port_%08x%02x %s", OMCGetSitNo(), OMCGetDevNo(), report_port);
	
		//存时间
		redisReply *reply = redisCommand(rconn, command);
		if (NULL == reply)
		{
			printf("Execut command failure\n");
			redisFree(rconn);
			return;
		}
		if (!(reply->type == REDIS_REPLY_STATUS && (strcmp(reply->str, "OK") == 0 || strcmp(reply->str, "ok") == 0)))
		{
			printf("Failed to execute command[%s]\n", command);
			freeReplyObject(reply);
			redisFree(rconn);
			return;
		}
		freeReplyObject(reply);
		printf("Succeed to [%s]\n", command);

		//存ip
		redisReply *reply1 = redisCommand(rconn, command1);
		if (NULL == reply1)
		{
			printf("Execut command failure\n");
			redisFree(rconn);
			return;
		}
		if (!(reply1->type == REDIS_REPLY_STATUS && (strcmp(reply1->str, "OK") == 0 || strcmp(reply1->str, "ok") == 0)))
		{
			printf("Failed to execute command[%s]\n", command1);
			freeReplyObject(reply1);
			redisFree(rconn);
			return;
		}
		freeReplyObject(reply1);
		printf("Succeed to [%s]\n", command1);

		//存端口
		redisReply *reply2 = redisCommand(rconn, command2);
		if (NULL == reply2)
		{
			printf("Execut command failure\n");
			redisFree(rconn);
			return;
		}
		if (!(reply2->type == REDIS_REPLY_STATUS && (strcmp(reply2->str, "OK") == 0 || strcmp(reply2->str, "ok") == 0)))
		{
			printf("Failed to execute command[%s]\n", command2);
			freeReplyObject(reply2);
			redisFree(rconn);
			return;
		}
		freeReplyObject(reply2);
		printf("Succeed to [%s]\n", command2);

		//释放连接
		redisFree(rconn);

		/*if (reply->type != REDIS_REPLY_STRING)
		{
		printf("Failed to execute command[%s]\n", command);
		freeReplyObject(reply);
		redisFree(rconn);
		return;
		}
		printf("The value of 'foo' is %s\n", reply->str);
		freeReplyObject(reply);
		printf("Succeed to execute command[%s]\n", command);*/
	

		/* 存入mysql，分表
		MYSQL mysql;
		init_mysql(&mysql);
		u8_t efmId[32];
		sprintf(efmId, "%08x%02x", OMCGetSitNo(), OMCGetDevNo());
		u32_t time = getTime();
		u8_t sql[1024];
		u8_t sql2[256];
		u32_t flag;
		sprintf(sql, "CREATE TABLE IF NOT EXISTS hixent_efm_log_%s(id int(11) NOT NULL AUTO_INCREMENT,efm_id varchar(11) NOT NULL DEFAULT '',log_time int(11) NOT NULL DEFAULT '0', PRIMARY KEY (id));", efmId);
		flag = mysql_real_query(&mysql, sql, (u32_t)strlen(sql));
		if (flag)
		{
			printf("create failed!\n");
			return 0;
		}
		else
		{
			printf("create succeed\n");
		}
		sprintf(sql2, "insert into hixent_efm_log_%s(efm_id,log_time) values ('%s',%d);", efmId, efmId, time);
		mysql_insert(&mysql, sql2);
		mysql_close(&mysql);
		*/

		//数据单元解析成功
		buf[10] = 0x00; /*vp层交互标识*/
		buf[13] = 0x00; /*应答标识*/
		OMCPA_CmdReplyToOmc(buf, len, inform);

		//将ip、端口记录缓存
		return OMC_DONE_OK;
	}
    //3、监控控制层协议处理(CPID): MCP:A--0x01 MCP:B--0x02 
    switch (buf[11]) {
        //MCP:A
        case OMC_MCPA:
			
            OMC_McpaInput(buf,len, inform);
            result = OMC_DONE_OK;
            break;
        //MCP:B
        case OMC_MCPB:
            //OMC_MCPB_Input(buf, len);
            result = OMC_DONE_OK;
            break;
        default:
            result = OMC_CMD_CPIDERR;
            break;
    }
    return result;
}
/**************************************************************************************************
**  函数名称:  HDP_ApaApcReback
**  功能描述:  APA/APC数据包反转义
**  输入参数:  dest           -- 存放转义后的数据包
**  输出参数:  pcmd   -- 待还原数据包的首地址
cmdlen -- 待还原数据包的长度
**  返回参数:  数据包还原后的总长度
**************************************************************************************************/
u16_t HDP_ApaApcReback(u8_t *pcmd, u16_t cmdlen)
{
    u8_t prevdata;
    u16_t cnt, i;

    prevdata = 0;
    cnt = 1;
    for (i = 1; i < cmdlen; i++) {
        if (pcmd[i] != 0x5E) {
            if (pcmd[i] == 0x7E) {
                pcmd[cnt++] = 0x7E;
                return cnt;
            }
            if (prevdata == 0x5E) {
                pcmd[cnt++] = pcmd[i] + 1;
            }
            else {
                pcmd[cnt++] = pcmd[i];
            }
        }
        prevdata = pcmd[i];
    }
    return 0;
}
/**************************************************************************************************
*    函数名称:    OMC_ApProtocolVef
*    函数功能:    AP层协议类型校验
*    入口参数:    pcmd   -- 待校验数据包的首地址
*    出口参数:    OMC_DONE_FAIL -- 校验失败
*                OMC_DONE_OK     -- 校验成功
*    注意事项:   无
**************************************************************************************************/
u8_t OMC_ApProtocolVef(u8_t *pcmd)
{
    if (pcmd[0] == 0x7E) {
        //APA或APC协议类型
        if ((pcmd[1] == 0x01) || (pcmd[1] == 0x03)) {
            return OMC_DONE_OK;
        }
    }
    else if (pcmd[0] == 0x21) {
        //APB协议类型
        if (pcmd[1] == 0x02) {
            return OMC_DONE_OK;
        }
    }

    return OMC_DONE_FAIL;
}

/**************************************************************************************************
**  函数名称:  HDP_CrcVef
**  功能描述:  命令包CRC校验
**  输入参数:  pcmd   -- 待校验数据包的首地址
**  输出参数:  HDP_DONE_FAIL   -- 校验失败
**             HDP_DONE_OK     -- 校验成功
**  返回参数:  无
**************************************************************************************************/
u8_t HDP_CrcVef(u8_t *pcmd, u16_t cmdlen)
{
    u16_t crcval;
    u8_t crc[2];

    //重新计算CRC
    crcval = ustrcalcrc(pcmd + 1, cmdlen - 4);
    crc[0] = (u8_t)crcval;
    crc[1] = (u8_t)(crcval >> 8);

    //计算生成的CRC与通信包自带的CRC进行比较
    if ((crc[0] != pcmd[cmdlen - 3]) || (crc[1] != pcmd[cmdlen - 2])) {
        u8_t test1[256];
        u8_t test2[256];
        //sprintf(test1,"crc[0]=%02x,crc[0]:%02x",crc[0],pcmd[cmdlen - 3]);
        //printf("%s",test1);
		//sprintf(test2,"crc[1]=%02x,crc[1]:%02x",crc[1],pcmd[cmdlen - 2]);
        //printf("%s",test2);
		printf("OMC_CMD_CRCERR2:\n");
        return OMC_DONE_FAIL;
    }

    return OMC_DONE_OK;
}

/***************************************************************************************************
*    函数名称:    OMC_McpaInput
*    函数功能:    OMC-MCPA协议处理
*    入口参数:    pmsg -- 指向命包相关信息的指针
*    出口参数:    无
*    注意事项:   无
**************************************************************************************************/
u8_t OMC_McpaInput(u8_t *mcpaBuf, u16_t mcpaLen, struct message_inform *inform)
{
    u8_t result;

    //回复包
    if (mcpaBuf[9] < 0x80) {
        
        //1、应答标志校验
        if (mcpaBuf[13] != 0x00) {
			printf("OMC_CMD_CMDRFERR2：\n");
            return OMC_CMD_CMDRFERR;
        }

        //2、命令标识校验 
        if (((mcpaBuf[12] != OMC_CMD_QUERY) && (mcpaBuf[12] != OMC_CMD_SET))) {
			printf("OMC_CMD_CMDIDERR：\n");
            return OMC_CMD_CMDIDERR;
            
        }

        //3、命令包长度校验
        if (OMC_McpaLenVef(mcpaBuf, mcpaLen) != OMC_DONE_OK) {
			printf("OMC_CMD_LENERR3：\n");
            return OMC_CMD_LENERR;
        }
	
		//数据处理
        result = OMC_McpaPduAnaly(mcpaBuf, mcpaLen, inform);
	
        return OMC_DONE_OK;
    }
    //上报包
    else if (mcpaBuf[9] < 0x90) {
		//printf("pcmd1:%d\n", mcpaBuf[17]);
        //1、应答标志校验
        if (mcpaBuf[13] != 0xFF) {  
			printf("OMC_CMD_CMDRFERR1：\n");
            return OMC_CMD_CMDRFERR;
        }

        //2、命令包长度校验
        if (OMC_McpaLenVef(mcpaBuf, mcpaLen) != OMC_DONE_OK) {
			printf("OMC_CMD_LENERR4：\n");
            return OMC_CMD_LENERR;
        }
        
        //数据处理
        result = OMC_McpaPduAnaly(mcpaBuf, mcpaLen, inform);
		
        if(result == OMC_DONE_OK) {
            //数据单元解析成功
            mcpaBuf[10] = 0x00; /*vp层交互标识*/
            mcpaBuf[13] = 0x00; /*应答标识*/
			//printf("port:%s\n", inform->ip);
            OMCPA_CmdReplyToOmc(mcpaBuf, mcpaLen, inform);
        }
        
        return 0;
        
    }

    return OMC_DONE_FAIL;
}

/*************************************************************************************************
*    函数名称:    MDPMB_McpaLenVef
*    函数功能:    MCPA命令包长度效验
*    入口参数:    pcmd   -- 待校验数据包的首地址
*                cmdlen -- 待校验命令包的总长度
*    出口参数:    MDPMB_DONE_FAIL  -- 校验失败
*                MDPMB_DONE_OK     -- 校验成功
*    注意事项:   无
**************************************************************************************************/
u8_t OMC_McpaLenVef(u8_t *pcmd, u16_t cmdlen)
{
    
    u16_t cnt, i;

    //这里没有越界(小于17的长度在先前校验是通不过的)
    cmdlen -= 17;
    cnt = 0;
    i = 0;

    while ((cnt < cmdlen) && (i < cmdlen)) {    //14+cnt < 14+cmdlen =>内存访问没有越界 
        cnt += pcmd[16 + cnt] + 3;
        i++;
    }
    if (cnt == cmdlen) {
        return OMC_DONE_OK;
    }

    return OMC_DONE_FAIL;
}
u8_t OMC_ReportAnaly(u16_t cmdid, u16_t index, u8_t *pdu, struct message_inform *inform) {
	u8_t *pseq;
	pseq = OMCATable[index].pname;
	MYSQL mysql;
	MYSQL_RES *res;
	MYSQL_ROW row;
	u8_t query[256];
	u8_t query2[1024];
	u8_t ulen;
	ulen = pdu[2];
	u8_t tempbuf[1024];
	u32_t time;
	u32_t flag;
	time = getTime();
	u32_t site_code = OMCGetSitNo();
	u8_t device_code = OMCGetDevNo();
	u8_t efmId[32];
	sprintf(efmId, "%08x%02x", site_code, device_code);
	u8_t busid;
	u8_t addrid;
	u16_t num;
	init_mysql(&mysql);
	//设备详情上报
	if (ulen == 252) 
	{
		/*
		u8_t clientIp[16];
		u8_t clientport[16];
		sprintf(clientIp, "%s", inform->ip);
		sprintf(clientport, "%d", inform->port);
		//判断站点是否存在
		sprintf(query, "select * from hixent_arc_site where site_code = '%08x'", site_code);
		flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
		if (flag) {
			printf("Query failed!\n");
			return 0;
		}
		res = mysql_store_result(&mysql);
		num = mysql_num_rows(res);
		if (num == 0) {
			sprintf(query, "insert into hixent_arc_site(site_code) values ('%08x');", site_code);
			mysql_insert(&mysql, query);
		}
		mysql_free_result(res);
		//判断中控是否存在
		sprintf(query, "select * from hixent_arc_efm_device where site_code = '%08x' and device_code = '%02x'", site_code, device_code);
		flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
		if (flag) {
			printf("Query failed!\n");
			return 0;
		}
		res = mysql_store_result(&mysql);
		num = mysql_num_rows(res);
		if (num == 0) {
			sprintf(query, "insert into hixent_arc_efm_device(id,site_code,device_code,device_ip,device_port) values ('%s','%08x','%02x','%s','%s');", efmId, site_code, device_code, clientIp, clientport);
			mysql_insert(&mysql, query);
		}
		mysql_free_result(res);
		*/

		//设备总线
		busid = pdu[3];
		u8_t String_u[128];
		u8_t String_u2[1024];
		u8_t String_u3[256];
		u8_t String_u4[256];
		u8_t String_u5[128];
	
		u16_t i;
		//设备地址
		addrid = pdu[4];
	
		u8_t equipment_id[32];
		sprintf(equipment_id, "%08x%02x%02x%02x", site_code, device_code, busid, addrid);
		sprintf(query, "select * from hixent_arc_equipment_info where id = '%s';", equipment_id);
		flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
		if (flag) 
		{
			printf("Query failed!\n");
			return 0;
		}
		res = mysql_store_result(&mysql);
		num = mysql_num_rows(res);
		if (num == 0) {
			sprintf(String_u3, "%s", DevInfoTable[0].pname);
			sprintf(String_u4, "%d", pdu[3]);
			for (i = 1; i < OS_COUNTOF(DevInfoTable); i++)
			{
				if (DevInfoTable[i].InfoLen > 0)
				{
					sprintf(String_u3, "%s,%s", String_u3, DevInfoTable[i].pname);
					if (DevInfoTable[i].InfoLen < 5)
					{
						
						if (DevInfoTable[i].InfoLen == 1)
						{
							u32_t test1 = 0;
							u16_t test2 = (DevInfoTable[i].index + 3);
							memcpy(&test1, pdu + test2, DevInfoTable[i].InfoLen);
							sprintf(String_u4, "%s,%d", String_u4, test1);
						}
						else if(DevInfoTable[i].InfoLen == 2)
						{
							u16_t test1 = 0;
							u16_t test2 = (DevInfoTable[i].index + 3);
							memcpy(&test1, pdu + test2, DevInfoTable[i].InfoLen);
							test1 = pdu[test2 + 1] | ((u16_t)(pdu[test2]) << 8);
							sprintf(String_u4, "%s,%d", String_u4, test1);
						}
					}
					else
					{
						u16_t test2 = (DevInfoTable[i].index + 3);
						memcpy(&String_u5, pdu + test2, DevInfoTable[i].InfoLen);
						u8_t m;
						u8_t message_str[256];
						sprintf(message_str, "%2x", String_u5[0]);
						printf("message_str:%s", message_str);
						for (m = 1; m < DevInfoTable[i].InfoLen; m++)
						{
							sprintf(message_str, "%s%02x", message_str, String_u5[m]);
						}
						sprintf(String_u4, "%s,'%s'", String_u4, message_str);
					}
				}
				else
				{
					break;
				}
			}
			sprintf(query2, "insert into hixent_arc_equipment_info(id,device_code,site_code,device_type,register_time,%s,efm_id) values ('%s','%02x','%08x','1',%d,%s,'%s');", String_u3, equipment_id, OMCGetDevNo(), OMCGetSitNo(), time, String_u4, efmId);
			printf("end2:%s", query2);
			mysql_insert(&mysql, query2);
		}
		else {
			memset(String_u2, 0, sizeof(String_u2));
			sprintf(String_u2, "%s = %d", DevInfoTable[0].pname, pdu[3]);
			for (i = 1; i < OS_COUNTOF(DevInfoTable); i++) {
				if (DevInfoTable[i].InfoLen > 0)
				{
					if (DevInfoTable[i].InfoLen < 5)
					{
						if (DevInfoTable[i].InfoLen == 1)
						{
							u32_t test1 = 0;
							u16_t test2 = (DevInfoTable[i].index + 3);
							memcpy(&test1, pdu + test2, DevInfoTable[i].InfoLen);
							sprintf(String_u2, "%s, %s = %d", String_u2, DevInfoTable[i].pname, test1);
						}
						else if (DevInfoTable[i].InfoLen == 2)
						{
							u16_t test1 = 0;
							u16_t test2 = (DevInfoTable[i].index + 3);
							memcpy(&test1, pdu + test2, DevInfoTable[i].InfoLen);
							test1 = pdu[test2+1] | ((u16_t)(pdu[test2]) << 8);
							sprintf(String_u2, "%s, %s = %d", String_u2, DevInfoTable[i].pname, test1);
						}
					}
					else
					{
						u16_t test2 = (DevInfoTable[i].index + 3);
						printf("test2:%d\n", DevInfoTable[i].InfoLen);
						memcpy(&String_u, pdu + test2, DevInfoTable[i].InfoLen);
						u8_t m;
						u8_t message_str[256];
						sprintf(message_str, "%2x", String_u[0]);
						printf("message_str:%s",message_str);
						for (m = 1; m < DevInfoTable[i].InfoLen; m++)
						{
							sprintf(message_str, "%s%02x", message_str, String_u[m]);
						}
						sprintf(String_u2, "%s, %s = '%s'", String_u2, DevInfoTable[i].pname, message_str);
						printf("message:%s\n", message_str);
					}

				}
				else
				{
					break;
				}
			}
			sprintf(query2, "update hixent_arc_equipment_info set %s where id = '%s';", String_u2, equipment_id);
			printf("end2:%s", query2);
			mysql_update(&mysql, query2);
		}
	}
	//告警或开站上报efm基本信息
	else if(cmdid != 0x0141) 
	{
		//efm6000主机告警上报,发送java服务端处理
		if (ulen == 4) {
			u8_t url[] = "http://127.0.0.1:8082/allGetApp/alarmWebsocket";
			u8_t postData[512];
			sprintf(postData, "code=%d&deviceId=%s", pdu[3], efmId);
			printf("alarmData:%s", postData);
			post_curl(url, postData);
		}
		//终端告警上报
		else if (ulen == 3 && pdu[5] == 1) {
			u8_t equipment_id[32];
			busid = pdu[3];
			addrid = pdu[4];
			sprintf(equipment_id, "%08x%02x%02x%02x", site_code, device_code, busid, addrid);
			sprintf(query, "select * from hixent_arc_equipment_info where id = '%s'", equipment_id);
			flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
			if (flag) 
			{
				printf("Query failed!\n");
				return 0;
			}
			else 
			{
				printf("[%s] Query succeed\n", query);
			}
			res = mysql_store_result(&mysql);
			num = mysql_num_rows(res);
			if (num == 0) {
				return 0;
			}
			u8_t warning_type[256];
			sprintf(query, "select protocol_name from hixent_arc_protocol where protocol_id = '%04x';", cmdid);
			printf("query:%s", query);
			flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
			if (flag) {
				printf("Query failed!\n");
				return 0;
			}
			res = mysql_store_result(&mysql);
			while (row = mysql_fetch_row(res)) {
				sprintf(warning_type, "%s", row[0]);
				printf("warning_type:%s", warning_type);
			}
			u8_t w_index;
			//终端离线
			if (cmdid == 0x0310) 
			{
				w_index = 2;
			}
			//终端预警
			else if (cmdid == 0x0320) 
			{
				w_index = 0;
			}
			//终端故障
			else if (cmdid == 0x0330) {
				w_index = 1;
			}	
			/*sprintf(query, "select * from hixent_arc_warning_list where warning_type = '%s' and etype = 2 and efm_id='%s' and eid='%s' and warning_index = %d", warning_type, efmId,equipment_id,w_index);
			flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
			if (flag) {
				printf("Query failed!\n");
				return 0;
			}
			res = mysql_store_result(&mysql);
			num = mysql_num_rows(res);
			if (num == 0) 
			{*/
			
				u8_t url[] = "http://127.0.0.1:8082/allGetApp/equipAlarm";
				u8_t postData[1024];
				sprintf(postData, "warning_type=%s&warning_time=%d&eid=%s&etype=2&efm_id=%s&warning_index=%d", warning_type, time,equipment_id, efmId, w_index);
				printf("postData:%s", postData);
				post_curl(url, postData);
			//}
		}
		//上报efm字符串基本信息
		else if (ulen == 20 || ulen == 40 || ulen == 45)
		{
			memset(tempbuf, 0, sizeof(tempbuf));
			memcpy(tempbuf, pdu + 3, ulen);
			u8_t m;
			u8_t message_str[256];
			sprintf(message_str, "%2x", tempbuf[0]);
			printf("message_str:%s", message_str);
			for (m = 1; m < ulen; m++)
			{
				sprintf(message_str, "%s%02x", message_str, tempbuf[m]);
			}
			sprintf(query, "update hixent_arc_efm_device set %s = '%s' where id = '%s';",pseq, message_str, efmId);
			printf("efmInfoReport:%s",query);
			mysql_update(&mysql, query);
		}
		//上报efm整数信息
		else if(ulen == 2)
		{
			if (cmdid == 0x0160)
			{
				u16_t value_u16;
				value_u16 = pdu[3] | ((u16_t)(pdu[4]) << 8);
				sprintf(query, "update hixent_arc_efm_device set %s = %d where id = '%s';", pseq, value_u16, efmId);
				mysql_update(&mysql, query);
			}
			else 
			{
				u16_t value_u16;
				value_u16 = pdu[4] | ((u16_t)(pdu[3]) << 8);
				sprintf(query, "update hixent_arc_efm_device set %s = %d where id = '%s';", pseq, value_u16, efmId);
				mysql_update(&mysql, query);
			}
		}
		else if (ulen == 1)
		{
			sprintf(query, "update hixent_arc_efm_device set %s = %d where id = '%s';",pseq, pdu[3], efmId);
			mysql_update(&mysql, query);
		}
	}
	mysql_close(&mysql);
}
u8_t OMC_QueryAnaly(u16_t cmdid,u16_t index,u8_t *pdu)
{
    u8_t *pseq;
    MYSQL mysql;
    MYSQL_RES *mysql_res;
    MYSQL_ROW row;    
    u8_t query[256];
    u8_t ulen;
	ulen = pdu[2];
	u8_t tempbuf[1024];
	u32_t site_code = OMCGetSitNo();
	u8_t device_code = OMCGetDevNo();
	//总线id
	u8_t busid;
	//地址id
	u8_t addrid;
	u8_t status;
	u8_t efmId[32];
	sprintf(efmId, "%08x%02x", site_code, device_code);
    init_mysql(&mysql);
    pseq = OMCATable[index].pname;
    if(ulen == 1){
		if (cmdid != 0x0102 && cmdid != 0x0200)
		{
			sprintf(query, "update hixent_arc_efm_device set %s = %d where id = '%s';",
				pseq, pdu[3], efmId);
		}
		//查询设备编号
		else if(cmdid == 0x0102)
		{
			sprintf(query, "update hixent_arc_efm_device set %s = '%02x' where id = '%s';",
				pseq, pdu[3], efmId);
		}
		
    }
	else if(ulen == 2)
	{
		if (cmdid == 0x0160) 
		{
			u16_t value_u16;
			value_u16 = pdu[3] | ((u16_t)(pdu[4]) << 8);
			sprintf(query, "update hixent_arc_efm_device set %s = %d where id = '%s';",
				pseq, value_u16, efmId);
		}
		else
		{
			u16_t value_u16;
			value_u16 = pdu[4] | ((u16_t)(pdu[3]) << 8);
			sprintf(query, "update hixent_arc_efm_device set %s = %d where id = '%s';",
				pseq, value_u16, efmId);
		}
		
    }
	else if (ulen == 4) 
	{
		u32_t value_u32;
		//站点编号
		if (OMCATable[index].datatype == 1)
		{
			value_u32 = ((pdu[6] << 24) & 0xFF000000) | ((pdu[5] << 16) & 0x00FF0000) | ((pdu[4] << 8) & 0x0000FF00) | pdu[3];
			sprintf(query, "update hixent_arc_efm_device set %s = '%08x' where id = '%s';",
				pseq, value_u32, efmId);
		}
		//查询EFM6000告警状态
		else if (OMCATable[index].datatype == 2)
		{
			sprintf(tempbuf, "%02x%02x%02x%02x", pdu[3], pdu[4], pdu[5], pdu[6]);
			sprintf(query, "update hixent_arc_efm_device set %s = '%s' where id = '%s';",
				pseq, tempbuf, efmId);
		}
		//EFM6000告警使能查询
		else if (OMCATable[index].datatype == 3)
		{
			u8_t url[] = "http://127.0.0.1:8082/allGetApp/enableDevice";
			u8_t postData[512];
			sprintf(postData, "code=%d&deviceId=%s", pdu[3], efmId);
			printf("alarmEnable:%s",postData);
			post_curl(url, postData);
			return;
		}
		//数字串:ip
		else
		{
			sprintf(tempbuf, "%d.%d.%d.%d", pdu[3], pdu[4], pdu[5], pdu[6]);
			sprintf(query, "update hixent_arc_efm_device set %s = '%s' where id = '%s';",
				pseq, tempbuf, efmId);
		}
	}
	else if (ulen == 7) 
	{
		sprintf(tempbuf, "%02x%02x-%02x-%02x %02x:%02x:%02x", pdu[3], pdu[4], pdu[5], pdu[6], pdu[7], pdu[8], pdu[9]);
		sprintf(query, "update hixent_arc_efm_device set %s = '%s' where id = '%s';",
			pseq, tempbuf, efmId);
	}
	else if (ulen == 20 || ulen == 40 || ulen == 45 || ulen == 50)
	{
		memset(tempbuf, 0, sizeof(tempbuf));
		memcpy(tempbuf, pdu + 3, ulen);
		printf("tempbuf:%s",tempbuf);
		u8_t m;
		u8_t message_str[256];
		sprintf(message_str, "%2x", tempbuf[0]);
		printf("message_str:%s", message_str);
		for (m = 1; m < ulen; m++)
		{
			sprintf(message_str, "%s%02x", message_str, tempbuf[m]);
		}
		sprintf(query, "update hixent_arc_efm_device set %s = '%s' where id = '%s';",
			pseq, message_str, efmId);
	}
	else{
		//其它终端处理
        if(OMCATable[index].datatype == 2){
			/*memset(tempbuf, 0, sizeof(tempbuf));
			memcpy(tempbuf, pdu + 3, ulen);
			printf("tempbuf:%s",tempbuf);*/
	
			if (ulen == 32) 
			{
				u8_t i;
				u8_t state[32];
				u8_t lineCode;
				memset(state, 0, sizeof(state));
				memcpy(state, pdu + 3, ulen);
				printf("len:%d\n",sizeof(state));
				u8_t d[256];
				sprintf(d, "%02x", state[0]);
				for (i = 1; i < sizeof(state); i++)
				{
					sprintf(d,"%s%02x",d,state[i]);
				}
				printf("d=%s",d);
				//sprintf(query, "update hixent_arc_efm_device set %s = '%s' where id = '%s';", pseq, d, efmId);
				if (cmdid == 0x0210 || cmdid == 0x0220 || cmdid == 0x0230 || cmdid == 0x0311 || cmdid == 0x0321 || cmdid == 0x0331)
				{
					lineCode = 0;
				}
				else if (cmdid == 0x0211 || cmdid == 0x0221 || cmdid == 0x0231 || cmdid == 0x0312 || cmdid == 0x0322 || cmdid == 0x0332)
				{
					lineCode = 1;
				}
				else if (cmdid == 0x0212 || cmdid == 0x0222 || cmdid == 0x0232 || cmdid == 0x0313 || cmdid == 0x0323 || cmdid == 0x0333)
				{
					lineCode = 2;
				}
				else if (cmdid == 0x0213 || cmdid == 0x0223 || cmdid == 0x0233 || cmdid == 0x0314 || cmdid == 0x0324 || cmdid == 0x0334)
				{
					lineCode = 3;
				}
				u8_t url[] = "http://127.0.0.1:8082/allGetApp/updateEquipAlarmInfo";
				u8_t postData[512];
				sprintf(postData, "code=%s&deviceId=%s&lineCode=%d&field=%s", d, efmId, lineCode,pseq);
				printf("query_32:%s", postData);
				post_curl(url, postData);
			}
			else if (ulen == 102) 
			{
				//设备总线
				busid = pdu[3];
				//设备地址
				addrid = pdu[4];
				//终端id
				u8_t equipment_id[32];
				sprintf(equipment_id, "%08x%02x%02x%02x", site_code, device_code, busid, addrid);
				memset(tempbuf, 0, sizeof(tempbuf));
				memcpy(tempbuf, pdu + 5, ulen - 2);
				printf("tempbuf:%s", tempbuf);
				sprintf(query, "update hixent_arc_equipment_info set %s = '%s' where id = '%s';",
					pseq, tempbuf, equipment_id);
			}
        }
    }

    printf("%s\n", query);
	mysql_update(&mysql, query);
	mysql_close(&mysql);
    
}
/*************************************************************************************************
*    函数名称:    OMC_McpaPduAnaly
*    函数功能:    dev-MCPA数据单元解析
*    入口参数:    pcmd   -- 完整命令包
*                cmdlen -- 命令包长度
*    出口参数:    MDPMB_DONE_OK              -- 命令数据单元解析成功
*                MDPMB_DONE_SPECIALCMD   -- 执行了特殊命令
*                MDPMB_CMD_DONECONDITION -- 命令被有条件执行(命令的某数据单元解析不成功)
*    注意事项:   无
**************************************************************************************************/
u8_t OMC_McpaPduAnaly(u8_t *pcmd, u16_t cmdlen,struct message_inform *inform)
{
    u16_t cnt;
    u16_t i;
    u16_t cmdid;
    u16_t cmdflg;
    u16_t pdulen;
    u8_t result;
	
	printf("OMC_McpaPduAnaly\n");
    //命令标识
    cmdflg = pcmd[12] & 0x00FF;
    //先认为命令被成功执行
    result = OMC_DONE_OK;

    //逐个执行监控命令标识    
    cmdlen -= 17;
    cnt = 0;

    while (cnt < cmdlen) { //1、先前已通过长度校验,这里不会无限循环 2、16+cnt < 16+cmdlen =>内存访问没越界
        //监控标识号
        cmdid = pcmd[14 + cnt] | ((u16_t)(pcmd[15 + cnt]) << 8);
		printf("cmdid=%d\n", cmdid);
        u8_t test[256];
        pdulen = pcmd[16 + cnt];
        for (i = 0; i<OS_COUNTOF(OMCATable); i++) {
            if (OMCATable[i].CmdId == cmdid) {
                printf("cmdflg=%d\n", cmdflg);
                if (OMCATable[i].PduLen != pdulen) {
					printf("OMC_DONE_FAIL:%x\n",cmdid);
                    return OMC_DONE_FAIL;
                }
                //回复查询指令包
                if(cmdflg == OMC_CMD_QUERY){
					printf("OMC_CMD_QUERY+:\n");
                    OMC_QueryAnaly(cmdid,i,pcmd + 14 + cnt);
                }
                //回复设置指令包
                else if(cmdflg == OMC_CMD_SET){
					printf("OMC_CMD_SET+:\n");
					u8_t url[] = "http://127.0.0.1:8082/allGetApp/replySetCmd";
					u8_t postData[512];
					u32_t site_code = OMCGetSitNo();
					u8_t device_code = OMCGetDevNo();
					u8_t efmId[32];
					sprintf(efmId, "%08x%02x", site_code, device_code);
					sprintf(postData, "cmdId=%04x&efmId=%s", cmdid, efmId);
					printf("setCmd:%s\n", postData);
					post_curl(url, postData);
                }
				else if (cmdflg == OMC_CMD_REPORT) {
					printf("OMC_CMD_REPORT+:\n");
					OMC_ReportAnaly(cmdid, i, pcmd + 14 + cnt,inform);
				}
                //其他处理
                if (OMCATable[i].DoSpecial) {
					printf("pcmd:%d\n", pcmd[3]);
                    result = OMCATable[i].DoSpecial(cmdflg,i,pcmd + 14 + cnt, OMCGetSitNo(), OMCGetDevNo(), inform);
                }
                break;
            }
            
        }
        //下一个监控数据单元
        cnt += pdulen + 3;
    }
    return result;
}
/**************************************************************************************************
**  函数名称:  HDP_ApaApcEscape
**  功能描述:  APA/APC数据包字符转义
**  输入参数:  dest           -- 存放转义后的数据包
**  输出参数:  dest           -- 存放转义后的数据包
**             destmaxlen    -- 能存放字符的最大长度
**             pcmd           -- 待转义数据包的首地址
**             cmdlen         -- 待转义数据包的长度
**  返回参数:  无
**************************************************************************************************/
u16_t HDP_ApaApcEscape(u8_t *dest, u16_t destmaxlen, u8_t *pcmd, u16_t cmdlen)
{
    u16_t cnt, i;

    if ((dest == (u8_t *)0) || (destmaxlen < 2) || (pcmd == (u8_t *)0) || (cmdlen < 2)) {
        return 0;
    }

    cnt = 0;
    dest[cnt++] = pcmd[0];
    for (i = 1; i < cmdlen - 1; i++) {
        if (cnt > destmaxlen - 2) {
            return 0;
        }
        if ((pcmd[i] == 0x7E) || (pcmd[i] == 0x5E)) {
            dest[cnt++] = 0x5E;
            dest[cnt++] = pcmd[i] - 1;
        }
        else {
            dest[cnt++] = pcmd[i];
        }
    }

    if (cnt > destmaxlen - 1) {
        return 0;
    }

    dest[cnt++] = pcmd[cmdlen - 1];

    return cnt;
}
/*************************************************************************************************
*    函数名称:    OMCPA_CmdUdpSend
*    函数功能:     
*    入口参数:    pmsg -- 指向命包相关信息的指针
*    出口参数:    无
*    注意事项:   无
**************************************************************************************************/
u8_t OMCPA_CmdUdpSend(u8_t *pcmd, u16_t cmdlen,struct message_inform *inform)
{
    printf("ip:%s\n",inform->ip);
    sendto(inform->sd, pcmd, cmdlen, 0, &inform->addr, sizeof(struct sockaddr_in));
}
/*************************************************************************************************
*    函数名称:    OMCPA_CmdReplyToOmc
*    函数功能:    OMC应答回去处理
*    入口参数:    pmsg -- 指向命包相关信息的指针
*    出口参数:    无
*    注意事项:   无
**************************************************************************************************/
u8_t OMCPA_CmdReplyToOmc(u8_t *pcmd, u16_t cmdlen,struct message_inform *inform)
{

    u8_t ptmp[1500];

    u16_t crcval;

    //计算生成CRC校验码
    crcval = ustrcalcrc(pcmd+1,cmdlen-4);

    //填充CRC
    pcmd[cmdlen-3] = (u8_t)crcval;
    pcmd[cmdlen-2] = (u8_t)(crcval >> 8);

	//printf("%s1\n", __func__);
    if(pcmd[0] == 0x7E) {
		//printf("%s2\n", __func__);
        //APA,APC命令包则进行转义处理
        cmdlen = HDP_ApaApcEscape(ptmp,OMCPA_PACKAGE_MAX_LEN,pcmd,cmdlen);
        printf("%d\n",cmdlen);
        OMCPA_CmdUdpSend(pcmd,cmdlen,inform);
    }
    
    return OMC_DONE_OK;
}

u8_t sendAlarm(u8_t devNo) 
{
	//app推送
    u8_t url[] = "http://127.0.0.1:8082/allGetApp/sendJpushMessage";
    u8_t postData[512];
    u8_t type[8] = "1";
    u8_t title[64] = "莱茵斯科技";
    u8_t content[256];
	sprintf(content, "您的设备'%2x'处于报警中",devNo);
    u8_t key[256] = "user";
    sprintf(postData, "type=%s&title=%s&content=%s&key=%s", type,title,content,key);
    post_curl(url,postData);
	
}
//开站
u8_t createSite(u32_t siteNo, u8_t devNo,struct message_inform *inform) 
{
    u8_t clientIp[16];
    u8_t clientport[16];
	u8_t efmId[256];
	sprintf(efmId,"%08x%02x", siteNo, devNo);
    sprintf(clientIp,"%s",inform->ip);
    sprintf(clientport,"%d",inform->port);
    u16_t num;
    MYSQL mysql;
    MYSQL_RES *res;
    MYSQL_ROW row;    
    u8_t query[256];
    u32_t flag, t;
    init_mysql(&mysql);
	//判断站点是否存在
	sprintf(query, "select * from hixent_arc_site where site_code = '%08x'", siteNo);
	flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
	if (flag) {
		printf("Query failed!\n");
		return 0;
	}
	res = mysql_store_result(&mysql);
	num = mysql_num_rows(res);
	if (num == 0) {
		sprintf(query, "insert into hixent_arc_site(site_code) values ('%08x');", siteNo);
		//printf("query:%s\n", query);
		mysql_insert(&mysql, query);
	}
	mysql_free_result(res);
	//判断中控是否存在
    sprintf(query, "select * from hixent_arc_efm_device where site_code = '%08x' and device_code = '%02x'", siteNo, devNo);
    flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
    if (flag) {
        printf("Query failed!\n");
        return 0;
    }
    else {
        //printf("[%s] Query succeed\n", query);
    }
    res = mysql_store_result(&mysql);
    num = mysql_num_rows(res);
    if (num == 0) {
        sprintf(query, "insert into hixent_arc_efm_device(id,site_code,device_code,device_ip,device_port) values ('%s','%08x','%02x','%s','%s');",efmId,siteNo,devNo,clientIp, clientport);
        //printf("query:%s\n",query);
        mysql_insert(&mysql,query);
    }
    else{
        sprintf(query, "update hixent_arc_efm_device set device_ip = '%s',device_port = '%s' where site_code = '%08x' and device_code = '%02x';", clientIp, clientport, siteNo, devNo );
        //printf("query:%s\n", query);
        mysql_update(&mysql, query);
    }
    mysql_free_result(res);
    mysql_close(&mysql);
    return 0;
}
//登录上报
u8_t loginSite(u32_t siteNo, u8_t devNo, struct message_inform *inform)
{
	u8_t clientIp[16];
	u8_t clientport[16];
	u8_t efmId[256];
	sprintf(efmId, "%08x%02x", siteNo, devNo);
	sprintf(clientIp, "%s", inform->ip);
	sprintf(clientport, "%d", inform->port);
	u16_t num;
	MYSQL mysql;
	MYSQL_RES *res;
	MYSQL_ROW row;
	u8_t query[512];
	u32_t flag, t;
	init_mysql(&mysql);
	sprintf(query, "select * from hixent_arc_efm_device where site_code = '%08x' and device_code = '%02x'", siteNo, devNo);
	flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
	if (flag) {
		printf("Query failed!\n");
		return 0;
	}
	res = mysql_store_result(&mysql);
	num = mysql_num_rows(res);
	if (num != 0) {
		sprintf(query, "update hixent_arc_efm_device set device_ip = '%s',device_port = '%s' where site_code = '%08x' and device_code = '%02x';", clientIp, clientport, siteNo, devNo);
		mysql_update(&mysql, query);
	}
	mysql_free_result(res);
	mysql_close(&mysql);
	return 0;
}
//消音状态更新
u8_t voiceState(u32_t siteNo, u8_t devNo)
{
	
}
//告警复位
u8_t alarmReset(u32_t siteNo, u8_t devNo)
{
	u8_t clientIp[16];
	u8_t clientport[16];
	u8_t efmId[256];
	sprintf(efmId, "%08x%02x", siteNo, devNo);
	u8_t url[] = "http://127.0.0.1:8082/allGetApp/alarmReset";
	u8_t postData[512];
	sprintf(postData, "deviceId=%s", efmId);
	printf("postData:%s", postData);
	post_curl(url, postData);
}

/**************************************************************************************************
Function      : OMC_MCPB_Input()
Description   : 数据入口
Input         : 无
Output        : 无
Return        : 操作结果
Others        : 其它说明
**************************************************************************************************/
#if 0
u8_t OMC_MCPB_Input(u8_t *buf, u16_t len)
{
    u8_t *mcpbBuf;
    u16_t mcpbLen;
    u8_t result;
    u16_t cnt;
    u16_t cmdid;
    u16_t pdulen;
    u8_t  cmdflg;
    u8_t  index;


    /* 1， 基本的校验 */

    
    // 2、数据包长度校验
    if ((buf == 0) || (len == 0)) {
        //app_log(LG_ERR, "MDPUN: -D- pCmd len err \r\n");
        return OMC_DONE_FAIL;
    }
    // 3、命令体长度校验
    if (HDP_McpbLenVef(buf, len) != OMC_DONE_OK) {
        //app_log(LG_ERR, "MDPUN: -D- pCmd Crc err \r\n");
        return OMC_DONE_FAIL;
    }

    /* 2 , 读取命令标识 */

    // 1 ，命令标识
    mcpbBuf = buf;
    cmdflg = mcpbBuf[12];
    // 2 ，不是查询设置命令  直接返回
    if ((cmdflg != MCPB_CMD_SET) && (cmdflg != MCPB_CMD_QUERY)) {
        return OMC_DONE_FAIL;
    }
    mcpbLen = len;
    // 3 ，逐个执行监控命令标识    
    mcpbLen -= 17;
    cnt = 0;

    /* 3 , 数据体解析处理  */
    mcpbBuf[10] = 0x00;
    mcpbBuf[13] = 0x00;


    //1、先前已通过长度校验,这里不会无限循环 2、16+cnt < 16+cmdlen =>内存访问没越界
    while (cnt < mcpbLen) {
        // 监控标识号 以及数据长度
        cmdid = mcpbBuf[16 + cnt] | ((u16_t)(mcpbBuf[17 + cnt]) << 8);
        pdulen = ((mcpbBuf[14 + cnt] & 0x00FF) | (mcpbBuf[15 + cnt] << 8));
        //远程升级应答，以及数据块处理函数
        if (cmdid == 0x0303) {
            //return MCPB_FileBlackCmd(pmsg);
        }

        for (index = 0; index < OMCB_CMD_MAX; index++) {
            // MCPB 信息处理函数
            if (OMCB_Table[index].CmdId == cmdid) {
                if (OMCB_Table[index].DoSpecial != NULL) {
                    OMCB_Table[index].DoSpecial(cmdflg, index, mcpbBuf + 14 + cnt);
                }
                break;
            }
        }
        //下一个监控数据单元
        cnt += pdulen;
    }

    /* 4， 控制命令的回复函数*/
    OMC_CmdReplyToOmc(mcpbBuf, mcpbLen);


    return OMC_DONE_OK;
}
#endif
/**************************************************************************************************
**  函数名称:  ustrcalcrc
**  功能描述:  计算CRC
**  输入参数:  pSrc    -- 指向源字符串的首地址
**             len     --    待计算校验值字符串的长度
**  输出参数:  crcval  -- 计算出的CRC值
**  返回参数:  无
**************************************************************************************************/
u16_t ustrcalcrc(u8_t *pStr,u32_t len)
{    

    u32_t UstrCrcTable[16] =
     {    
        0x0000,0x1021,0x2042,0x3063,0x4084,0x50A5,0x60C6,0x70E7,
        0x8108,0x9129,0xA14A,0xB16B,0xC18C,0xD1AD,0xE1CE,0xF1EF
     };
    u16_t crc;
    u8_t temp;

    crc = 0;
    while(len--) {
        // 暂存CRC的高4位
        temp = ((u8_t)(crc >> 8)) >> 4;
        // 取CRC的低12位                 
        crc <<= 4;                            
        
        // CRC高4位和本字节的高4位相加后查表计算CRC,再加前次CRC的余数
        crc ^= UstrCrcTable[temp^(*pStr >> 4)];                

        // 暂存CRC的高4位
        temp = ((u8_t)(crc >> 8)) >> 4;
        // 取CRC的低12位                 
        crc <<= 4;                                
 
        // CRC高4位和本字节的低4位相加后查表计算CRC,再加前次CRC的余数
        crc ^= UstrCrcTable[temp^(*pStr & 0x0f)]; 
 
        pStr++;
    }

    return crc;
} 
/**************************************************************************************************
**  函数名称:  post_curl
**  功能描述:  发送curl
**  输入参数:  url      -- 发送地址
**          postData     --    post发送数据
**  输出参数:  无
**  返回参数:  无
**************************************************************************************************/
void post_curl(u8_t *url,u8_t *postData){
    CURL *curl;
    CURLcode res;
    curl = curl_easy_init();
    if(curl)
    {
        curl_easy_setopt(curl, CURLOPT_URL,url);
        curl_easy_setopt(curl, CURLOPT_POST, 1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, postData);
        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);
    }
}

void init_mysql(u32_t *mysql){
	char server[] = "127.0.0.1";
    char user[] = "root";
    //char password[] = "root";
	char password[] = "hixentdata123";
    char database[] = "hixent_arc_system";
    mysql_init(mysql);
    if (!mysql_real_connect(mysql, server, user, password, database, 3306, NULL, 0)){
        printf("Failed to connect to Mysql!\n");
        return 0;
    }
}

void mysql_insert(u32_t *mysql , u8_t *query){
    u32_t flag;
    flag = mysql_real_query(mysql, query, (u32_t)strlen(query));
    if (flag) {
        printf("Insert data failure!\n");
        return 0;
    }
    else {
        //printf("Insert data success!\n");
    }
	return;
}

void mysql_update(u32_t *mysql , u8_t *query){
    u32_t flag;
    flag = mysql_real_query(mysql, query, (u32_t)strlen(query));
    if (flag) {
        printf("update data failure!\n");
        return 0;
    }
    else {
        //printf("update data success!\n");
    }
}


    




