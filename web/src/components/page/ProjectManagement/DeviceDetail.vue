<template id="deviceDetail">
    <div style="height: 100%;">
        <!--数据页面-->
        <div class="efm-box" style="overflow-y:auto;height: 100%; width:calc(100% - 185px); background-color: #F0F2F5; float: left;">
            <div style="height:36.5%;margin: 20px 24px;background-color: #fff">
                <div style="height:100%;margin: 16px 20px;background-color: #fff">
                    <div style="height: 25px;width:100%;padding-top: 12px;">
                        <div style="height: 25px;width:100%;padding-top: 0;">
                            <el-breadcrumb separator="/" style="display: inline-block">
                                <el-breadcrumb-item>
                                    <span style="font-size: 16px;color:#303313;">项目管理</span>
                                </el-breadcrumb-item>
                                <el-breadcrumb-item>
                                    <span style="font-size: 14px;color:#606266;">中控信息</span>
                                </el-breadcrumb-item>
                            </el-breadcrumb>
                        </div>
                    </div>
                    <div style="width: 100%;height: 16%; margin-top: 1%; margin-left: -2px;">
                        <div style="margin-top: 0; float: left;">
                            <i class="okay myIcon-xiangmu" style="font-size: 23px;color: orange;"></i>
                            <span style="font-size: 20px;font-weight: 500;">中控：</span>
                            <span>
                                <i v-show="efmInfo.isOnline == 1" class="el-icon-success" style="color: limegreen;position: relative;top: -1px;"></i>
                                <i v-show="efmInfo.isOnline == 0" class="el-icon-warning" style="color: lightgray;position: relative;top: -1px"></i>
                            </span>&nbsp;
                            <span style="font-size: 20px;font-weight: 500;">{{routeParam.efmName}}</span>
                            <span style="font-size: 10px;font-weight: 300;color: #2d8cf0; cursor: pointer;"
                                  @click="centerDialogVisible = true">
                        <i class="okay myIcon-xiugai" style="font-size: 15px" @click="editNameBox = true;efmNameInput = routeParam.efmName"></i>
                        </span>
                        </div>
                        <div style="float: right;position: relative;left: 2px;">
                            <el-button class="v-1-0-0-Button" @click="checkEfm">查看</el-button>
                        </div>

                    </div>

                    <div style=" height: 52%; margin: 10px 0; ">
                        <table class="gridtable" style="border-collapse: collapse;font-weight: 400 !important;
                        font-size: 14px;" width="100%" height="80%">
                            <tr >
                                <td class="nameTd">位&nbsp;&nbsp;&nbsp;&nbsp;置：</td>
                                <td class="priceTd">
                                    {{efmDetail.device_place}}
                                </td>
                                <td class="nameTd">设 备 数：</td>
                                <td class="priceTd">
                                    {{efmInfo.equipCount}}
                                </td>
                                <td align="left" class="nameTd">报 警 数：</td>
                                <td align="left" class="priceTd">
                                    {{efmInfo.equipAlarmCount}}
                                </td>
                            </tr>
                            <tr>
                                <td class="nameTd">预留电话：</td>
                                <td class="priceTd">
                                    {{efmDetail.tel}}
                                </td>
                                <td align="left" class="nameTd">离 线 数：</td>
                                <td align="left" class="priceTd">{{efmInfo.equipOffLineCount}}</td>
                                <td align="left" class="nameTd">故 障 数：</td>
                                <td align="left" class="priceTd">{{efmInfo.equipFaultCount}}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>

            <div style="height:calc(100% - (36.5% + 60px)) ;margin: 20px 24px;background-color: #fff;min-height:500px;">
                <div style="padding-top: 12px;height: calc(100% - 10px)">
                    <div style="width:100%;background:#fff; height: 100%;overflow: hidden;">
                        <el-radio-group v-model="equipType" @change="changeEquipType" style="padding-left: 16px">
<!--                            <el-radio-button label="0">电弧探测器</el-radio-button>-->
<!--                            <el-radio-button label="1">组合式探测器</el-radio-button>-->
                            <el-radio-button label="0">终端信息</el-radio-button>
                        </el-radio-group>
                        <div style="margin-top: 10px;padding-left: 16px;padding-right: 16px">
                            <el-input id="searchKeys" v-model="equipInput" placeholder="请输入编号" style="width: 200px;"></el-input>
                            <el-button class="v-1-0-0-Button" style="margin-left: 5px;" @click="getArcEquip">搜索</el-button>
<!--                            <button class="primary" style="margin-left: 5px;" id="resetBtn">重置</button>-->
                            <el-button class="v-1-0-0-Button" style="margin-left: 5px;" @click="resetBtn">重置</el-button>
                            <el-button class="v-1-0-1-Button" v-if="actionList.indexOf('deleteDevice')>-1" icon="el-icon-delete" @click="batchDelete" style="float: right">批量删除</el-button>
<!--                            <el-button class="v-1-0-0-Button" @click="alertClear" style="float: right">广播消音</el-button>-->
                        </div>

                        <!--中控信息设备信息列表-->
                        <div style="height:calc(100% - 146px);margin: 15px 16px 0 16px;border: 1px solid #F0F2F5;overflow: auto">
                            <el-table ref="multipleTable" height="100%" :data="equipList"
                                      tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" :header-cell-style="tableStyle">

                                <el-table-column type="selection" width="48">
                                </el-table-column>

                                <el-table-column type="index" width="56" label="#">
                                </el-table-column>

                                <el-table-column prop="serialNumber" :formatter="formatNumber" label="编号">
                                </el-table-column>

                                <el-table-column prop="message" label="信息" show-overflow-tooltip>
                                </el-table-column>

                                <el-table-column prop="tempration" label="温度" v-if="equipType == '1'" :key="Math.random()">
                                </el-table-column>

                                <el-table-column prop="broken_num" label="电压/电流" v-if="equipType == '1'" :key="Math.random()">
                                </el-table-column>

                                <el-table-column prop="is_alarm" :formatter="formatState" label="正常/告警"></el-table-column>
                                <el-table-column fixed="right" label="操作">
                                    <template slot-scope="scope">
                                        <el-button
                                            @click.native.prevent="checkTerminal(scope.row)"
                                            type="text"
                                            size="small">
                                            <span style="color: #387EE8;">查看</span>
                                        </el-button>
                                        <el-button style="color: red;"
                                                   v-if="actionList.indexOf('deleteDevice')>-1"
                                            @click.native.prevent="delTerminal(scope.row)"
                                            type="text"
                                            size="small">
                                            <span>删除</span>
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
                                layout="total,sizes, prev, pager, next, jumper"
                                :total="common.total">
                            </el-pagination>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <el-dialog :visible.sync="editNameBox" width="26%">
            <div style=" width:100%;text-align: center; font-size: 20px; margin: -10px auto auto auto;">
                修改中控名称
            </div>

            <div style=" width:55%; height:30%;margin: 20px auto auto auto;">
                <el-input v-model="efmNameInput" maxLength="6" placeholder="请输入中控名称"></el-input>
            </div>

            <div style="width:135px;margin: 20px auto auto auto; ">
                <el-button class="v-1-0-0-Button-Minor" style="width: 56px;" @click="editNameBox = false">
                    取 消</el-button>
                <el-button class="v-1-0-0-Button" style="width: 56px;margin-left: 16px;" @click="editEfmName">
                    确 定</el-button>
            </div>

        </el-dialog>
    </div>

</template>
<!--<script>-->
<!--    $("#resetBtn").on('click', function(){-->
<!--        $("#searchKeys").val('');-->
<!--        // $(".search-type>.search-type-item:first-child").click();-->
<!--        // remote.getTemplateDataApi();-->
<!--    });-->
<!--</script>-->
<script>
    import http from '../../../common/http';
    import bus from '../../../common/bus';
    export default {
        name: 'deviceDetail',
        data() {
            return {
                routeParam:{},
                efmInfo:{},
                efmDetail:{},
                tableStyle:{
                    'background-color':'#F6F7FB',
                    'font-weight':'400'
                },
                common:{
                    total:0,            //总数据量
                    pageSize:20,        //每页条数
                    page:1,              //页码
                    isPage:1,            //是否分页
                    allSizes:[10,20,30], //每页条数筛选
                    checkboxArr:[]       //多选框数组
                },
                equipInput:"",
                equipType:0,
                editNameBox:false,
                efmNameInput:"",
                delEquipId:0,          //删除的终端id
                centerDialogVisible: false,//修改中控名对话框
                is_site: true,
                is_device: false,

                editSite: {
                    site_code: "",
                    site_name: "",
                    site_place: "",
                    site_build_id: "",
                    site_id: 0
                },
                equipList: [], //终端列表
                multipleSelection: [],
            }
        },
        created: function () {
            this.routeParam = this.$route.params;
            this.actionList = JSON.parse(sessionStorage.getItem('action'));
            this.getEfmInfo();
            this.getArcEquip();
            bus.$on('efmClickEfmClick',(msg)=>{
                this.routeParam.id = msg.id;
                this.routeParam.efmName = msg.efmName;
                this.getEfmInfo();
                this.getArcEquip();
            })
        },
        beforeDestroy () {
            bus.$off('efmClickEfmClick');
        },
        methods: {
            getEfmInfo(){
                let url = this.getEfmInfoUrl;
                let data = {
                    deviceId: this.routeParam.id,
                };
                this.apiPost(url, data).then((res) => {
                    this.efmInfo = res.data;
                    this.efmDetail = this.efmInfo.getdeviceInfo;
                    console.log(res);
                });
            },
            resetBtn(){
                // $("#searchKeys").val('');
                this.equipInput = '';
                let _loading = this.$commonFn.showLoading(2,'.efm-box');
                let url = this.getEquipListUrl;
                let data = {
                    deviceId: this.routeParam.id,
                    pagesize: this.common.pageSize,
                    currentPage: this.common.page,
                    type: this.equipType,
                    inquire: this.equipInput
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if(res.status === 200){
                        this.equipList = res.data.equipList;
                        this.common.total = res.data.equipListCount;
                        // for (let i in this.equipList){
                        //     //临时数据
                        //     this.equipList[i].tempration = this.equipList[i].tempration+"/"+ this.equipList[i].tempration+"/"+this.equipList[i].tempration
                        //     this.equipList[i].broken_num = "220V/1.0A";
                        // }
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            getArcEquip() {
                let _loading = this.$commonFn.showLoading(2,'.efm-box');
                let url = this.getEquipListUrl;
                let data = {
                    deviceId: this.routeParam.id,
                    pagesize: this.common.pageSize,
                    currentPage: this.common.page,
                    type: this.equipType,
                    inquire: this.equipInput
                };
                if(this.equipInput.indexOf("-") > 0){
                    data.parameterType = 1;
                    let arr = this.equipInput.split('-');
                    let arr1 = parseInt(arr[0]) - 1;
                    data.inquire = arr1 + '-' + arr[1];
                }
                else{
                    data.parameterType = 0;
                }
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if(res.status === 200){
                        this.equipList = res.data.equipList;
                        this.common.total = res.data.equipListCount;
                        for (let i in this.equipList){
                            //临时数据
                            this.equipList[i].tempration = this.equipList[i].tempration+"/"+ this.equipList[i].tempration+"/"+this.equipList[i].tempration
                            this.equipList[i].broken_num = "220V/1.0A";
                        }
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            //修改中控名称
            editEfmName(){
                let url = this.editEfmNameUrl;
                let data = {
                    deviceId: this.routeParam.id,
                    niName:this.efmNameInput
                };
                this.apiPost(url, data).then((res) => {
                    if(res.status === 200){
                        this.editNameBox = false;
                        this.routeParam.efmName = this.efmNameInput;
                        this.$commonFn.showTip("修改成功", 1);
                        bus.$emit('initTree');
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                });
                // let url = this.setDeviceParamsUrl;
                // let data = {
                //     siteCode: this.routeParam.id.substring(0,8),
                //     deviceCode:this.routeParam.id.substring(8,10),
                //     protocolName:"udp_set_specificator",
                //     extraData:this.efmNameInput
                // };
                // this.apiPost(url, data).then((res) => {
                    // if(res.status === 200){
                    //     localStorage.setItem('cmd',"udp_set_specificator");
                    //     localStorage.setItem('efmId',this.routeParam.id.substring(0,8) + this.routeParam.id.substring(8,10));
                    //     let that = this;
                    //     setTimeout(function () {
                    //         localStorage.removeItem('cmd');
                    //         localStorage.removeItem('efmId');
                    //         //that.$commonFn.showTip("消息超时", 2);
                    //     },5000)
                    // }
                    // else{
                    //     this.$commonFn.showTip(res.message,3);
                    // }
                // });
            },
            alertClear(){
                let url = this.sendAdminSocketUrl;
                let data = {
                    protocolName: 'udp_clear_broadcast_voice',
                    siteCode: this.efmDetail.site_code,
                    deviceCode: this.efmDetail.device_code,
                    extraData: ""
                };
                this.apiPost(url, data).then((res) => {
                    if(res.status === 200){
                        localStorage.setItem('cmd',"udp_clear_broadcast_voice");
                        localStorage.setItem('efmId',this.efmDetail.site_code+this.efmDetail.device_code);
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
            checkEfm(){
                this.$router.push({
                    name:'centerControlInformation',
                    params:{
                        id:this.routeParam.id,
                        efmName:this.routeParam.efmName,
                        siteLabel:this.efmInfo.getdeviceInfo.siteName?this.efmInfo.getdeviceInfo.siteName:this.efmInfo.getdeviceInfo.site_code
                    }
                });
            },
            checkTerminal(data){
                console.log(data);
                this.$router.push({
                    name:'terminalInformation',
                    params:{
                        equipId:data.id,
                        efmId:this.routeParam.id,
                        efmName:this.routeParam.efmName,
                        siteLabel:this.efmInfo.getdeviceInfo.siteName?this.efmInfo.getdeviceInfo.siteName:this.efmInfo.getdeviceInfo.site_code
                    }
                });
            },
            delTerminal(row){
                this.delEquipId = row.id;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            deleteSuccess(){
                let url = this.deleteEquipUrl;
                let data = {
                    deviceIds:this.delEquipId
                };
                this.apiPost(url, data).then((res) => {
                    if(res.status === 200){
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        this.getArcEquip();
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {

                });
            },
            batchDelete(){
                if(this.common.checkboxArr.length != 0){
                    let idStr = this.common.checkboxArr.join(",");
                    this.delEquipId = idStr;
                    this.$commonFn.showConfirm(this.deleteSuccess);
                }
                else{
                    this.$commonFn.showTip("请选择删除数据",2);
                }
            },
            formatNumber: function (row, column) {
                return row.serialNumber = parseInt(row.line_code) + 1 + "-" + row.addr;
            },
            formatState:function(row, column){
                return row.is_alarm == 0 ? "正常":"告警";
            },

            //项目详情查看
            projectDetail(node, data) {
                console.log(data);
                this.is_site = true;
                this.is_device = false;
                this.editSite.site_code = data.siteCode;
                this.editSite.site_name = data.label;
                this.editSite.site_place = data.sitePlace;
                this.editSite.site_build_id = data.siteBuildId;
                this.editSite.site_id = data.siteId;
            },
            //切换终端类型
            changeEquipType(type) {
                this.equipType = type;
                this.getArcEquip();
            },
            handleSelectionChange(val) {
                this.common.checkboxArr = [];
                for (let i = 0;i < val.length;i++){
                    this.common.checkboxArr.push(val[i].id);
                }
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1;
                this.getArcEquip();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getArcEquip();
            },

            // handleScroll () {
            //     let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            //     console.log(scrollTop)
            // },
        },
        // mounted () {
        //     window.addEventListener('scroll', this.handleScroll,true)
        // },
        mixins: [http]
    }
</script>

<style scoped>
    table.gridtable {
        padding: 8px;
        font-family: verdana, arial, sans-serif;
        font-size: 11px;
        color: #333333;
        border-width: 1px;
        /* border-color: #666666;*/
        border-color: #d8dce6 !important;
        border-collapse: collapse;
    }

    table.gridtable th:nth-child(odd) {
        border-width: 1px;
        padding: 8px;
        height: 18px;
        border-style: solid;
        /*  border-color: #666666;*/
        /* border-color: rgb(154,200,228);*/
        border-color: #d8dce6;
        background-color: #F6F7FB;
        /* background-color: #dadee9;*/
        text-align: center;
    }

    table.gridtable th:nth-child(even) {
        border-width: 1px;
        height: 18px;
        padding: 0px;
        border-style: solid;
        /*  border-color: #666666;*/
        /* border-color: rgb(154,200,228);*/
        border-color: #d8dce6;
        background-color: white;
        /* background-color: #dadee9;*/
        text-align: left;
    }

    .nameTd {
        letter-spacing: 3px;
        text-align: center;
        padding: 8px;
        border-style: solid;
        border-color: #d8dce6;
        background-color: #F6F7FB;
        width: 130px;
    }

    .priceTd {
        padding: 8px;
        height: 20px;
        border-style: solid;
        border-color: #d8dce6;
        background-color: white;
        width: 160px;
        text-indent: 10px;
    }

    .login-box {
        position: absolute;
        left: 50%;
        top: 60%;
        /*width:298px;*/
        /*height:288px;*/
        margin: -146px 0 0 -163px;
        padding: 30px;
        border-radius: 5px;
        background: rgba(5, 5, 38, 0.2);
        border: 1px solid rgba(5, 5, 38, 0.3);
    }

    .gridcontainer {
        display: grid;
        grid-template-columns: 10rem 2px auto;
        grid-template-rows: auto;
        background: #325AA2;
    }

    .el-table th {
        text-align: center;
    }

    .el-table tbody tr td:first-child {
        text-align: center;
    }

    .el-pagination {
        padding: 15px 5px;
    }

    .table-scroll-mod {
        position: relative;
        height: 65px;
        overflow: hidden;
    }



    .gridcontainer[data-v-cc10f246] {
        width: 0;
        background: rgb(16, 42, 93);
    }

    .deviceMenu {
        background: white;
        width: 185px;
        height: 100%;
        float: left;
        overflow-y: auto !important;
    }

    .el-tree .el-tree-node:hover {
        background: red !important;
    }

    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 16px;
        padding-right: 8px;
    }

    .mySpan {
        cursor: pointer;
        color: cornflowerblue;
    }

    hr {
        background-color: #F0F2F5;
        height: 1px;
        border: none;
    }

    .v-1-0-0-Button {
        background-color: #387EE8;
        color: white;
    }

    .v-1-0-0-Button-Minor {
        background-color: #F0F8FF;
        color: #387EE8;
    }
    .el-tabs__content{
        height: calc(100% - 108px) !important;
    }
    .el-tabs{
        height: 100% !important;
    }
</style>
