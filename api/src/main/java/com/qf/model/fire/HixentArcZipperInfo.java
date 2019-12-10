package com.qf.model.fire;



import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * author zhangjun
 */
@Data
public class HixentArcZipperInfo implements Serializable {

	private int unid;//主键

	private String projectId;//所属项目id

	private String deviceId;//设备id

	private String finishedSum;//成品计数

	private String defectiveSum;//次品计数

	private String totalCycles;//总循环次数

	private String currentAngle;//当前角度

	private String currentSpeed;//当前速度

	private String speedSetting;//速度设定

	private String operationalMode;//运行模式，0为自动，1为手动

	private String isAlarm;//是否报警，0为正常，1为报警

	private String alarmType;// 报警类型，0为正常，1为停电，2为机器故障

	private String faultType;//故障类型，0为正常，1为运转故障，2为启动故障

	private String type;//连表查询的设备类型

	private String isDeal;//是否处理，0为未处理，1为处理

	private String siteName;//连表查询的设备所属项目名称

	private String siteId;//连表查询的设备所属项目id

	private Date createTime;//创建时间

	private Date updateTime;//更新时间

	private String createPeople;//创建人

	private String updatePeople;//更新人（处理人）



}
