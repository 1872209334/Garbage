/**************************************************************************************************
**                                                                                               **
**  文件名称:  md_func.c                                                                        **
**  版权所有:  CopyRight @ Fujian Guode Medical Technology Co.,Ltd 2015                          **
**  文件描述:  命令解析                                                                          **
**  ===========================================================================================  **
**  创建信息:  | 2015-4-30 | lwp | 创建本模块                                                    **
**  ===========================================================================================  **
**  修改信息:  单击此处添加....                                                                  **
**************************************************************************************************/

#ifndef __CMD_FUNC_H_
#define __CMD_FUNC_H_
//#include "app_lib.h"
//#include  "version.h" 
 
/*************************************************************************************************/
/*                           函数定义区域                                                        */
/*************************************************************************************************/

#define PduToCmd(pdu)  (pdu[1] | ((u8_t)(pdu[2]) << 8))
 


/*************************************************************************************************/
/*                           函数定义区域                                                        */
/*************************************************************************************************/
 

u8_t OMCA_DoCmd0x0141(u16_t cmdflg, u16_t index,u8_t *pdu,u32_t site_code,u8_t device_code, struct message_inform *inform){
	printf("%d\n", pdu[3]);
	//开站
	if (pdu[3] == 0x02) 
	{
		createSite(site_code, device_code, inform);
	}
	//登录上报
	else if (pdu[3] == 0x06) 
	{
		loginSite(site_code, device_code, inform);
	}
	else if (pdu[3] == 0x08)
	{
		voiceState(site_code, device_code);
	}
	else if (pdu[3] == 0x09)
	{
		alarmReset(site_code, device_code);
	}
		
	return OMC_DONE_OK;
}


#endif
