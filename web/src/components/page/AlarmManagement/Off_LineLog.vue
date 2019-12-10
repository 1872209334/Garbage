<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">
        <div class="pageBox" style="overflow-y: auto">
            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">报警管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">离线列表</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <!--<div>-->
                <!--<el-radio-group v-model="filters.deviceType" @change="changeDeviceType" style="padding-left: 16px;margin: 5px 0">-->
                    <!--<el-radio-button label="1">终端</el-radio-button>-->
                    <!--<el-radio-button label="0">中控</el-radio-button>-->
                <!--</el-radio-group>-->
            <!--</div>-->
            <div style=" height:50px; margin:5px 16px 0 16px;">
                <div style="float: left; height:40px; width:135px;">
                    <div style="width:135px;">
                        <el-select id="selectProject" v-model="projectSelectVal" placeholder="请选择项目" @change="getCenterControl">
                            <el-option
                                label="不限"
                                value="0">
                            </el-option>
                            <el-option
                                v-for="item in projectSelect"
                                :key="item.siteCord"
                                :label="item.siteLabel"
                                :value="item.siteCord">
                            </el-option>
                        </el-select>
                    </div>
                </div>

                <!--<div style="float: left; height:40px;margin-left: 8px">-->
                    <!--<div style="width:135px;">-->
                        <!--<el-select v-model="centerControlVal" placeholder="请选择中控">-->
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

                <!--<div style="width:135px; float: left; height:40px;margin-left: 8px">-->
                    <!--&lt;!&ndash;编号输入框&ndash;&gt;-->
                    <!--<div style="width:135px;">-->
                        <!--<el-input v-model="inputBoxValue" placeholder="请输入关键字"></el-input>-->
                    <!--</div>-->
                <!--</div>-->

                <div style="float: left; height:40px;"><!-- width:56px;margin-left: 10px-->
                    <el-button class="v-1-0-0-Button" @click="common.page = 1;getOfflineLogBySiteId()">查询</el-button>
                    <el-button class="v-1-0-0-Button" style="margin-left: 5px;" @click="resetBtn">重置</el-button>
                </div>

                <div style=" float: right; ">
                    <el-button class="v-1-0-0-Button" @click="exportAlarm">数据导出</el-button>
                </div>

            </div>

            <div style="height:calc(100% - 194px);margin: 0 16px auto 16px;border: 1px solid #F0F2F5;overflow: auto">
                <el-table
                    ref="multipleTable"
                    height="100%"
                    :data="offlineLogData"
                    tooltip-effect="dark"
                    style="width: 100%"
                    :header-cell-style="tableStyle">
                    <el-table-column type="index" width="56" label="#">
                    </el-table-column>
                    <el-table-column prop="siteName" :formatter="formatProject" label="项目">
                    </el-table-column>
                    <el-table-column prop="deviceId"  :formatter="formatNiname" label="设备id">
                    </el-table-column>

                    <!--<el-table-column prop="deviceName" :formatter="formatNiname" label="中控" show-overflow-tooltip>-->
                    <!--</el-table-column>-->

                    <!--<el-table-column prop="serialNumber" width="60" label="编号" v-if="filters.deviceType == '1'" :key="Math.random()" :formatter="formatNumber">-->
                    <!--</el-table-column>-->

                    <el-table-column prop="createTime" label="离线时间" :formatter="getLocalTime" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column prop="isDeal" label="处理状态" :formatter="formatIsDeal" show-overflow-tooltip></el-table-column>
                    <el-table-column label="设备类型" v-if="filters.deviceType == '1'" :key="Math.random()" :formatter="formatType"></el-table-column>

                    <el-table-column fixed="right" label="操作">
                        <template slot-scope="scope">
                            <!--<el-button @click="clearVoice(scope.row)" type="text" size="small"-->
                                       <!--v-if="filters.deviceType == '1'">-->
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

            <div style="float: right;margin-right: 16px;">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-size="common.pageSize"
                    :page-sizes="common.allSizes"
                    :current-page="common.page"
                    layout="total , sizes, prev, pager, next, jumper"
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
                inputBoxValue: "",
                //中控下拉框mdoel
                centerControlVal: null,
                //项目下拉框model
                projectSelectVal: '',
                //设备类型下拉框model
                equipmentTypeValue: null,
                //中控下拉框
                centerControlSel: [],
                //项目下拉框
                projectSelect: [],
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
                    checkboxArr: [] ,      //多选框数组
                    isAlarm:3  //类型，3为离线
                },
                offlineLogData: [],
                equipmentValue: -1,
                filters:{
                    deviceType:1,   //设备类型，0位中控，1为终端
                },
		        delWarningId: 0,
                warnIndex:2,
                start_date:"",
                end_date:"",
                alarmLogInformation1:[],
                input:"",
                province:[],
            };
        },
        created:function(){
            if(localStorage.getItem('offlinePage')){
                this.filters.deviceType = localStorage.getItem('offlinePage');
            }
            else{
                this.filters.deviceType = 1;
            }
            this.getOfflineLog();
            this.getProjectSel();
        },
        methods: {
            formatProject(row){
                if(row.siteName){
                    return row.siteName;
                }else{
                    return '';
                }

            },
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
            getCenterControl() {
                this.centerControlVal = null;
                let url = this.getDeviceBySiteUrl;
                let data = {
                    siteId: this.projectSelectVal
                };
                this.apiPost(url, data).then((res) => {
                    this.centerControlSel = res.data;
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
            formatNiname(row){
                return row.deviceId;
            },
            clearVoice(rows){
                let url = this.sendAdminSocketUrl;
                let data = {
                    protocolName: 'udp_clear_voice',
                    siteCode: rows.siteCode,
                    deviceCode: rows.deviceCode,
                    extraData: '0000'
                };
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
            },
            exportAlarm(){
                if(this.offlineLogData.length == 0){
                    this.$commonFn.showTip("无离线记录",2);
                    return;
                }
                let url = this.excelForOffLineLogUrl;
                let data = {
                    // siteId: this.projectSelectVal == ''?0:this.projectSelectVal,
                    projectId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // deviceId:this.centerControlVal == null?'':this.centerControlVal,
                    deviceId: this.projectSelectVal == ''?'':this.projectSelectVal,
                    currentPage:this.common.page,
                    pageSize:this.common.pageSize,
                    isAlarm:this.common.isAlarm,
                    // isDevice: this.filters.deviceType,
                    // siteId: (this.projectSelectVal == null?0:this.projectSelectVal),
                    // deviceId: (this.centerControlVal == null?0:this.centerControlVal),
                    // inquir:this.inputBoxValue == undefined?"":this.inputBoxValue,
                    // type:-1,
                    // warnIndex:this.warnIndex
                };
                this.apiPost(url,data,'blob').then((res) => {
                    this.excelForWarnLogSuccess(res);
                },(err)=>{

                });
            },
            //创建excel对象
            excelForWarnLogSuccess(res) {
                const link = document.createElement('a');
                let blob = new Blob([res], {type: 'application/vnd.ms-excel'});
                link.style.display = 'none';
                link.href = URL.createObjectURL(blob);
                link.setAttribute('download', '设备离线记录表.xlsx');
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            },
            resetBtn(){
                this.projectSelectVal = '';
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getOffLineLogUrl;
                let data = {
                    // isDevice:this.filters.deviceType,
                    // siteId:this.projectSelectVal == null?0:this.projectSelectVal,
                    // deviceId:this.centerControlVal == null?0:this.centerControlVal,
                    projectId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    deviceId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // deviceId:'',
                    // equipId:0,
                    // inquir:this.inputBoxValue,
                    currentPage:this.common.page,
                    pageSize:this.common.pageSize,
                    isAlarm:this.common.isAlarm
                    // type: this.equipmentValue,
                    // warnIndex:this.warnIndex
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        this.offlineLogData = res.data.zipperOffLineLog;
                        this.common.total = res.data.zipperOffLineLogCount;
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
                // this.projectSelectVal = "不限";
            },
            getOfflineLog(){
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getOffLineLogUrl;
                let data = {
                    // isDevice:this.filters.deviceType,
                    // siteId:this.projectSelectVal == null?0:this.projectSelectVal,
                    projectId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    deviceId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // deviceId:this.centerControlVal == null?'':this.centerControlVal,
                    // equipId:0,
                    // inquir:this.inputBoxValue,
                    currentPage:this.common.page,
                    pageSize:this.common.pageSize,
                    isAlarm:this.common.isAlarm,
                    // type: this.equipmentValue,
                    // warnIndex:this.warnIndex
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        this.offlineLogData = res.data.zipperOffLineLog;
                        this.common.total = res.data.zipperOffLineLogCount;
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            getOfflineLogBySiteId(){
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getOfflineLogBySiteIdUrl;
                let data = {
                    // isDevice:this.filters.deviceType,
                    // siteId:this.projectSelectVal == null?0:this.projectSelectVal,
                    // siteId: this.projectSelectVal == ''?0:this.projectSelectVal,
                    projectId:JSON.parse(sessionStorage.getItem("user_info")).bid =='0'?'':JSON.parse(sessionStorage.getItem("user_info")).bid,
                    // deviceId:this.centerControlVal == null?'':this.centerControlVal,
                    deviceId: this.projectSelectVal == ''?'':this.projectSelectVal,
                    // equipId:0,
                    // inquir:this.inputBoxValue,
                    currentPage:this.common.page,
                    pageSize:this.common.pageSize,
                    isAlarm:this.common.isAlarm,
                    // type: this.equipmentValue,
                    // warnIndex:this.warnIndex
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        this.offlineLogData = res.data.zipperOffLineLog;
                        this.common.total = res.data.zipperOffLineLogCount;
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            formatType:function(row){
                return row.type == 0 ? "拉链设备一":"拉链设备二";
            },
            formatNumber: function (row) {
                return row.serialNumber = parseInt(row.lineCode) + 1 + "-" + row.addr;
            },
            changeDeviceType(type){
                this.filters.deviceType = type;
                this.projectSelectVal = null;
                this.centerControlVal = null;
                this.equipmentTypeValue = -1;
                localStorage.setItem('offlinePage',type);
                this.getOfflineLog();
            },
            getLocalTime(row) {
                // return row.createTime = this.$commonFn.formatDate(row.createTime);
                return row.createTime;
            },
            formatIsDeal: function (row) {
                return row.isDeal == "0" ? "未处理" : "已处理";
            },
            //查看
            alarmDetail(rows) {
                // alert(rows.isDeal);
                this.$router.push(
                    {
                        name: 'offLogDev',
                        params:{
                            // id:rows.warning_no,
                            id:rows.unid,
                            // siteName:rows.siteName?rows.siteName:rows.site_code,
                            siteName:rows.siteName,
                            // niName:rows.niName?rows.niName:rows.device_code,
                            niName:rows.projectId,
                            serialNumber:rows.deviceId,
                            // tempration:rows.tempration,
                            tempration:rows.finishedSum,//成品计数
                            defectiveSum:rows.defectiveSum,//次品计数
                            totalCycles:rows.totalCycles,//循环次数
                            protocolNode:rows.protocolNode,
                            isDeal:rows.isDeal, //处理状态
                            // warnTime:rows.wtime
                            warnTime:rows.createTime, //离线时间
                            updateTime:rows.updateTime //处理时间
                        }
                    }
                );
            },
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
                        this.getOfflineLog();
                        let offlineCount = parseInt(localStorage.getItem('offlineCount'));
                        offlineCount -= 1;
                        localStorage.setItem('offlineCount',offlineCount);
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
                this.getOfflineLog();
            },
            handleCurrentChange(val) {
                this.common.page = val ;
                this.getOfflineLog();
            },
        },
        beforeDestroy() {
            localStorage.removeItem('offlinePage');
        },
        mixins: [http]
    };
</script>

<style scoped>

</style>

