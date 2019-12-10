<template>
    <div style="width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">

        <div class="pageBox" style="overflow-y: auto">
            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">报警管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">报警列表</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style=" height:50px; margin:5px 16px 0 16px;">

                <!--项目下拉框-->
                <div style="float: left; height:40px; width:135px;">
                    <div style="width:135px;">
                        <el-select v-model="projectSelectVal" placeholder="项目" @change="getCenterControl">
                            <!--<el-option-->
                                <!--label="不限"-->
                                <!--value="0">-->
                            <!--</el-option>-->
                            <el-option
                                v-for="item in projectSelect"
                                :key="item.siteCord"
                                :label="item.siteLabel"
                                :value="item.siteCord">
                            </el-option>
                        </el-select>
                    </div>
                </div>

                <!--中控下拉框-->
                <!--<div style="float: left; height:40px; width:135px; margin-left: 8px">-->
                    <!--<div style="width:135px;">-->
                        <!--<el-select v-model="centerControlVal" placeholder="中控">-->
                            <!--<el-option-->
                                <!--label="不限"-->
                                <!--value="0">-->
                            <!--</el-option>-->
                            <!--<el-option-->
                                <!--v-for="item in centerControlSel"-->
                                <!--:key="item.id"-->
                                <!--:label="item.efmLabel"-->
                                <!--:value="item.id">-->
                            <!--</el-option>-->
                        <!--</el-select>-->
                    <!--</div>-->
                <!--</div>-->

                <!--<div style="width:135px; float: left; height:40px;margin-left: 8px">-->
                    <!--<div style="width:135px;">-->
                        <!--<el-select v-model="equipmentTypeValue" placeholder="选择设备类型">-->
                            <!--<el-option-->
                                <!--v-for="item in equipmentType"-->
                                <!--:key="item.id"-->
                                <!--:label="item.name"-->
                                <!--:value="item.id">-->
                            <!--</el-option>-->
                        <!--</el-select>-->
                    <!--</div>-->
                <!--</div>-->

                <!--条件输入框-->
                <!--<div style="width:135px; float: left; height:40px;margin-left: 8px">-->
                    <!--&lt;!&ndash;编号输入框&ndash;&gt;-->
                    <!--<div style="width:135px;">-->
                        <!--<el-input v-model="inputBoxValue" placeholder="请输入关键字"></el-input>-->
                    <!--</div>-->
                <!--</div>-->

                <div style="float: left; height:40px;"><!-- width:56px;margin-left: 10px-->
                    <el-button class="v-1-0-0-Button" @click="common.page = 1;getAlarmLogBySiteId()">查询</el-button>
                    <el-button class="v-1-0-0-Button" style="margin-left: 5px;" @click="resetBtn">重置</el-button>
                </div>

                <!--<div style="float: left; height:40px; width:56px;margin-left: 10px">-->
                    <!--<el-button class="v-1-0-0-Button">高级搜索</el-button>-->
                <!--</div>-->

                <div style="float: right;">
                    <el-button class="v-1-0-0-Button" @click="exportAlarm">数据导出</el-button>
                </div>

            </div>

            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;
            border: 1px solid #F0F2F5;overflow: auto">
                <el-table ref="multipleTable" height="100%" :data="zipperAlarmLogData" tooltip-effect="dark" style="width: 100%"
                          :header-cell-style="tableStyle">
                    <el-table-column type="index" label="#" width="56"></el-table-column>

                    <el-table-column prop="siteName" label="项目" :formatter="formatProject"></el-table-column>

                    <el-table-column prop="deviceId" label="设备id" :formatter="formatNiname" show-overflow-tooltip></el-table-column>

                    <!--<el-table-column prop="serialNumber" :formatter="formatNumber" label="编号" width="60"></el-table-column>-->

                    <el-table-column prop="createTime" label="报警时间" :formatter="getLocalTime" show-overflow-tooltip></el-table-column>

                    <el-table-column prop="alarmType" label="报警类型" :formatter="formatAlarmType" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="isDeal" label="处理状态" :formatter="formatIsDeal" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="type" label="设备类型" :formatter="formatType" show-overflow-tooltip></el-table-column>

                    <el-table-column fixed="right" label="操作">
                        <template slot-scope="scope">
                            <!--<el-button @click="clearVoice(scope.row)" type="text" size="small">-->
                                <!--<span style="color: #387EE8;">消音</span>-->
                            <!--</el-button>-->

                            <el-button @click.native.prevent="alarmDetail(scope.row)" type="text" size="small">
                                <span style="color: #387EE8;">查看</span>
                            </el-button>

                            <el-button style="color: red" @click.native.prevent="deleteWarning(scope.row)" type="text" size="small">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>

            </div>

            <div style="float: right;margin-right: 13px;">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-size="common.pageSize"
                    :page-sizes="common.allSizes"
                    :current-page="common.page"
                    layout="total,sizes, prev, pager, next, jumper"
                    :total="common.total">
                </el-pagination>
            </div>

        </div>

    </div>
</template>

<script>
    import http from '../../../common/http';
    import bus from '../../../common/bus';
    export default {

        data() {
            return {
                //搜索条件输入框model
                inputBoxValue:"",
                //中控下拉框mdoel
                centerControlVal:null,
                //项目下拉框model
                projectSelectVal:'',
                //设备类型下拉框model
                equipmentTypeValue: null,
                //中控下拉框
                centerControlSel:[],
                //项目下拉框
                projectSelect:[],
                //设备类型下拉框
                equipmentType: [
                    {name: '不限', id: -1},
                    {name: '电弧探测器', id: 0},
                    {name: '组合式探测器', id: 1},
                ],
                tableStyle: {
                    'background-color': '#F6F7FB',
                    'font-weight': '400'
                },
                common: {
                    total: 0,            //总数据量
                    pageSize: 20,        //每页条数
                    page: 1,              //页码
                    isPage: 1,            //是否分页
                    allSizes: [10, 20, 30], //每页条数筛选
                    checkboxArr: []   ,    //多选框数组
                    isAlarm:1   //报警
                },
                zipperAlarmLogData: [],
                filters:{
                    deviceType:1,
                },
                delWarningId:0,
                warnIndex:0,
                start_date: "",
                end_date: "",
                checkEquipmentType: true,
                checkShowEquip: false,
                checkShowCenter: true,
            };
        },

        created: function () {
            this.getAlarmLog();
            this.getProjectSel();
        },

        methods: {
            //获取项目下拉框
            getProjectSel(){
                let url = this.getAllGroupUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.projectSelect = res.data.getsiteList;
                        for(let i in this.projectSelect){
                            if(this.projectSelect[i].siteName){
                                this.projectSelect[i].siteLabel = this.projectSelect[i].siteName;
                            }
                            else{
                                this.projectSelect[i].siteLabel = this.projectSelect[i].siteCord;
                            }
                        }
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {

                });
            },
            //通过项目获取中控
            getCenterControl(){
                this.centerControlVal = null;
                let url = this.getDeviceBySiteUrl;
                let data = {
                    siteId : this.projectSelectVal
                };
                this.apiPost(url, data).then((res) => {
                    this.centerControlSel =  res.data;
                    for (let i in this.centerControlSel){
                        if(this.centerControlSel[i].niName){
                            this.centerControlSel[i].efmLabel = this.centerControlSel[i].niName;
                        }
                        else if(this.centerControlSel[i].specificator){
                            this.centerControlSel[i].efmLabel = this.centerControlSel[i].specificator;
                        }
                        else{
                            this.centerControlSel[i].efmLabel = parseInt(this.centerControlSel[i].device_code,16);
                        }
                    }
                }, (err) => {

                });
            },
            resetBtn(){
                this.projectSelectVal = '';
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getAlarmLogUrl;
                let data = {
                    // isDevice: this.filters.deviceType,//1->终端,0->中控
                    // siteId: this.projectSelectVal == null?0:this.projectSelectVal,
                    // deviceId: this.centerControlVal == null?0:this.centerControlVal,
                    // equipId: 0,
                    // inquir: this.inputBoxValue,
                    projectId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    deviceId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    currentPage: this.common.page,
                    pageSize: this.common.pageSize,
                    isAlarm:this.common.isAlarm,
                    // type: this.equipmentTypeValue == null?-1:this.equipmentTypeValue,
                    // warnIndex: this.warnIndex
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        this.zipperAlarmLogData = res.data.zipperAlarmLog;
                        this.common.total = res.data.zipperAlarmLogCount;
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
                // this.projectSelectVal = "0";
            },
            getAlarmLog() {
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getAlarmLogUrl;
                // alert(JSON.parse(sessionStorage.getItem("user_info")).bid)
                let data = {
                    // isDevice: this.filters.deviceType,//1->终端,0->中控
                    // projectId: this.projectSelectVal == null?'':this.projectSelectVal,
                    projectId: JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // deviceId: this.projectSelectVal == ''?'':this.projectSelectVal,
                    deviceId: JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // equipId: 0,
                    // inquir: this.inputBoxValue,
                    currentPage: this.common.page,
                    pageSize: this.common.pageSize,
                    isAlarm:this.common.isAlarm,
                    // type: this.equipmentTypeValue == null?-1:this.equipmentTypeValue,
                    // warnIndex: this.warnIndex
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        this.zipperAlarmLogData = res.data.zipperAlarmLog;
                        this.common.total = res.data.zipperAlarmLogCount;
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            getAlarmLogBySiteId() {
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getAlarmLogBySiteIdUrl;
                let data = {
                    // isDevice: this.filters.deviceType,//1->终端,0->中控
                    // siteId: this.projectSelectVal == ''?0:this.projectSelectVal,
                    projectId: JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // deviceId: this.centerControlVal == null?'':this.centerControlVal,
                    deviceId: this.projectSelectVal == ''?'':this.projectSelectVal,
                    // equipId: 0,
                    // inquir: this.inputBoxValue,
                    currentPage: this.common.page,
                    pageSize: this.common.pageSize,
                    isAlarm:this.common.isAlarm,
                    // type: this.equipmentTypeValue == null?-1:this.equipmentTypeValue,
                    // warnIndex: this.warnIndex
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        this.zipperAlarmLogData = res.data.zipperAlarmLog;
                        this.common.total = res.data.zipperAlarmLogCount;
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            formatProject(row){
                if(row.siteName){
                    return row.siteName;
                }else{
                    return '';
                }

            },
            // formatNiname(row){
            //     if(row.niName){
            //         return row.deviceName = row.niName;
            //     }
            //     else if(row.specificator){
            //         return row.deviceName = row.specificator;
            //     }
            //     else{
            //         return row.deviceName = parseInt(row.device_code,16);
            //     }
            // },
            formatNiname(row){
                return row.deviceId;
            },
            formatNumber: function (row, column) {
                return row.serialNumber = parseInt(row.lineCode) + 1 + "-" + row.addr;
            },
            formatType: function (row, column) {
                return row.type == 0 ? "拉链探测器一" : "拉链探测器二";
            },
            formatIsDeal: function (row) {
                // return row.isDeal == 0 ? "未处理" : "待审核";
                return row.isDeal == 0 ? "未处理" : "已处理";
            },
            //时间戳转时间
            getLocalTime(row) {
                // return row.wtime = this.$commonFn.formatDate(row.warning_time);
                return row.createTime ;
            },
            formatAlarmType(row){
                if(row.alarmType == 1){
                    return "停电";
                }else if(row.alarmType == 2){
                    return "机器故障";
                }
            },
            //导出报警
                exportAlarm() {
                if(this.zipperAlarmLogData.length == 0){
                    this.$commonFn.showTip("无报警记录",2);
                    return;
                }
                // let url = this.excelForWarnLogUrl;
                let url = this.excelForAlarmLogUrl;
                let data = {
                    // siteId: this.projectSelectVal == ''?0:this.projectSelectVal,
                    projectId: JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // deviceId: this.centerControlVal == null?'':this.centerControlVal,
                    deviceId: this.projectSelectVal == ''?'':this.projectSelectVal,
                    // equipId: 0,
                    // inquir: this.inputBoxValue,
                    currentPage: this.common.page,
                    pageSize: this.common.pageSize,
                    isAlarm:this.common.isAlarm,
                    // isDevice: this.filters.deviceType,
                    // siteId: (this.projectSelectVal == null?0:this.projectSelectVal),
                    // deviceId: (this.centerControlVal == null?"":this.centerControlVal),
                    // inquir: this.inputBoxValue == undefined?"":this.inputBoxValue,
                    // type: -1,
                    // warnIndex:this.warnIndex
                };
                this.apiPost(url, data, 'blob').then((res) => {
                    this.excelForWarnLogSuccess(res);
                }, (err) => {

                });
            },
            //创建excel对象
            excelForWarnLogSuccess(res) {
                const link = document.createElement('a');
                let blob = new Blob([res], {type: 'application/vnd.ms-excel'});
                link.style.display = 'none';
                link.href = URL.createObjectURL(blob);
                let num = '';
                for (let i = 0; i < 10; i++) {
                    num += Math.ceil(Math.random() * 10)
                }
                link.setAttribute('download', '设备报警记录表.xlsx');
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            },
            //查看
            alarmDetail(rows) {
                console.log(rows);
                this.$router.push(
                    {
                        name: 'alaLogDev',
                        params:{
                            // id:rows.warning_no,
                            id:rows.unid,
                            // siteName:rows.siteName?rows.siteName:rows.site_code,
                            siteName:rows.siteName,
                            // niName:rows.niName?rows.niName:rows.device_code,
                            // niName:rows.niName?rows.niName:rows.projectId,
                            niName:rows.projectId,
                            // serialNumber:rows.serialNumber,
                            serialNumber:rows.deviceId,
                            // tempration:rows.tempration,
                            tempration:rows.finishedSum,
                            defectiveSum:rows.defectiveSum,//次品计数
                            totalCycles:rows.totalCycles,//总循环次数
                            // protocolNode:rows.protocolNode,
                            protocolNode:rows.alarmType=='1'?'停电':'机器故障',//报警原因（类型）
                            isDeal:rows.isDeal,
                            // warnTime:rows.wtime
                            warnTime:rows.createTime, //报警时间
                            updateTime:rows.updateTime //处理时间
                        }
                    }
                );
            },

            clearVoice(rows) {
                if (rows.etype == 2) {
                    let url = this.sendAdminSocketUrl;
                    let data = {
                        protocolName: 'udp_clear_voice',
                        siteCode: rows.siteCode,
                        deviceCode: rows.deviceCode,
                        extraData: rows.eid.substring(rows.eid.length - 4)
                    };
                    console.log(data)
                    this.apiPost(url, data).then((res) => {
                        if(res.status === 200){
                            localStorage.setItem('cmd',"udp_clear_voice");
                            localStorage.setItem('efmId',rows.siteCode+rows.deviceCode);
                            let that = this;
                            this.$constant.setCmdTime = setTimeout(function () {
                                that.apiPost(url, data).then((res) => {
                                    if(res.status === 200){
                                        let self = that;
                                        that.$constant.setCmdTime = setTimeout(function () {
                                            localStorage.removeItem('cmd');
                                            localStorage.removeItem('efmId');
                                            that.$commonFn.showTip("消息超时", 2);
                                        },4000)
                                    }
                                    else{
                                        that.$commonFn.showTip(res.message,3);
                                    }
                                }, (err) => {
                                    console.log(err);
                                });
                            },4000)
                        }
                        else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    }, (err) => {
                        console.log(err);
                    });
                }
                else if (rows.etype == 0) {
                    let url = this.mqttIssuingInstructionsUrl;
                    let data = {
                        message: 'alarm_silencing',
                        qos: 1,
                        ID: "01"+rows.eid,
                        type: 3,
                        extraData:""
                    };
                    this.apiPost(url, data).then((res) => {
                        this.$commonFn.showTip("消音发送成功", 1);
                    }, (err) => {
                        console.log(err);
                    });

                }
            },

            //删除报警
            deleteWarning(row){
                this.delWarningId = row.unid;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            deleteSuccess(){
                let url = this.delZipperLogByIdUrl;
                let data = {
                    unid: this.delWarningId
                };
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        bus.$emit("updateAlarmNum");
                        this.getAlarmLog();
                        let alarmCount = parseInt(localStorage.getItem('alarmCount'));
                        alarmCount -= 1;
                        localStorage.setItem('alarmCount',alarmCount);
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {

                });
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1 ;
                this.getAlarmLog();
            },
            handleCurrentChange(val) {
                this.common.page = val ;
                this.getAlarmLog();
            }
        },
        mixins: [http]
    };
</script>

<style scoped>


</style>

