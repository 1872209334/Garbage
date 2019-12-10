<template id="mqttDeviceList">
    <div style=" width:calc(100% - 185px); height:100%; background-color: #F0F2F5; overflow:auto;">
        <div class="pageBox" style="overflow-y: auto">
            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">项目管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">无线终端列表</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:50px; margin:5px 16px 0 16px;">

                <div style="width:135px; float: left; height:40px;">
                    <div style="width:135px;">
                        <el-select v-model="equipmentTypeValue" placeholder="选择设备类型">
                            <el-option
                            v-for="item in equipmentType"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                            </el-option>
                        </el-select>
                    </div>
                </div>

                <!--条件输入框-->
                <div style="width:135px; float: left; height:40px;margin-left: 8px">
                    <!--编号输入框-->
                    <div style="width:135px;">
                        <el-input v-model="inputBoxValue" placeholder="请输入关键字"></el-input>
                    </div>
                </div>

                <div style="float: left; height:40px; width:56px;margin-left: 10px">
                    <el-button class="v-1-0-0-Button" @click="common.page = 1;getMqttList()">查询</el-button>
                </div>
                <div style="float: right; height:40px;">
                    <!--<el-button class="v-1-0-1-Button" icon="el-icon-delete">删除</el-button>-->
                    <el-button class="v-1-0-0-Button" icon="el-icon-plus" @click="openCreatePanel">新建</el-button>
                </div>

            </div>

            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;
            border: 1px solid #F0F2F5;overflow: auto">
                <el-table ref="multipleTable" height="100%" :data="mqttDeviceData" tooltip-effect="dark" style="width: 100%"
                          :header-cell-style="tableStyle">
                    <el-table-column type="index" label="#" width="56"></el-table-column>

                    <el-table-column prop="id" label="编号" :formatter="formatNumber"></el-table-column>

                    <el-table-column prop="type" label="设备类型" :formatter="formatType"></el-table-column>

                    <el-table-column prop="message" label="描述符"  show-overflow-tooltip></el-table-column>

                    <!--<el-table-column prop="tem0" label="温度"  show-overflow-tooltip>-->
                        <!--<template slot-scope="scope">-->

                            <!--<div v-if="scope.row.type=='电弧探测器'">-->
                               <!--<span>{{scope.row.tem0}}</span>-->
                            <!--</div>-->
                            <!--<div v-if="scope.row.type=='组合式探测器'">-->
                                <!--<span>{{scope.row.tem0}}/{{scope.row.tem1}}/{{scope.row.tem2}}/{{scope.row.tem3}}/{{scope.row.tem4}}</span>-->
                            <!--</div>-->
                        <!--</template>-->
                    <!--</el-table-column>-->
                    <el-table-column fixed="right" label="操作">
                        <template slot-scope="scope">

                            <el-button @click.native.prevent="checkMqttTerminal(scope.row)" type="text" size="small">
                                <span style="color: #387EE8;" >查看</span>
                            </el-button>
                            <el-button @click.native.prevent="reboot(scope.row)" type="text" size="small">
                                <span style="color: red;">重启</span>
                            </el-button>
                            <el-button style="color: red" v-if="actionList.indexOf('deleteDevice')>-1" @click.native.prevent="delMqttTerminal(scope.row)" type="text" size="small">
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
        name: 'mqttDeviceList',
        data() {
            return {
                routeParam:{},
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
                equipmentTypeValue:null,
                equipmentType: [
                    {name: '不限', id: -1},
                    {name: '拉链探测器一', id: 0},
                    {name: '拉链探测器二', id: 1},
                    {name: '拉链探测器二', id: 2},
                    {name: '拉链探测器二', id: 3},
                ],
                mqttDeviceData:[],
                delMqttEquipId:0,
                inputBoxValue:"",
                multipleSelection: [],
            }
        },
        created: function () {
            this.routeParam = this.$route.params;
            this.equipmentTypeValue = this.routeParam.equipmentTypeValue >-1?this.routeParam.equipmentTypeValue:-1;
            this.inputBoxValue = this.routeParam.inputBoxValue?this.routeParam.inputBoxValue:'';
            this.common={
                total:0,            //总数据量
                pageSize:20,        //每页条数
                page:this.routeParam.page?this.routeParam.page:1,              //页码
                isPage:1,            //是否分页
                allSizes:[10,20,30], //每页条数筛选
                checkboxArr:[]       //多选框数组
            };
            console.log(this.routeParam);
            this.actionList = JSON.parse(sessionStorage.getItem('action'));
            this.getMqttList();
            bus.$on('mqttTerminalClick',(msg)=>{
                this.routeParam.siteId = msg.siteId;
                this.getMqttList();
            })
        },
        beforeDestroy () {
            bus.$off('mqttTerminalClick');
        },
        methods: {
            formatNumber: function (row) {
                // return row.serialNumber = row.device_code+"-"+(parseInt(row.lineCode) + 1) + "-" + parseInt(row.addr);
                return row.id;
            },
            formatType: function (row) {
                if(row.type == 0){
                    row.type = "拉链探测器一";
                }
                else if(row.type == 1){
                    row.type = "拉链探测器二";
                }
                else if(row.type == 2){
                    row.type = "拉链探测器三";
                }
                else if(row.type == 3){
                    row.type = "拉链探测器四";
                }
                return row.type;
            },
            //新增终端界面
            openCreatePanel() {
                // row.site_id = this.routeParam.siteId;
                // row.equipmentTypeValue = this.equipmentTypeValue;
                // row.inputBoxValue = this.inputBoxValue;
                // row.page = this.common.page;
                // row.type;
                // alert(this.mqttDeviceData[0].siteCode)
                this.$router.push(
                    {
                        name: 'newDevice',
                        params:{
                            siteCode:this.mqttDeviceData[0].siteCode,
                            type:0,
                            groupId:0,
                            place:"",
                            siteId:""
                        }
                    }
                );
            },
            getMqttList(){
                let url = this.getMqttListUrl;
                let data = {
                    siteId: this.routeParam.siteId,
                    type:this.equipmentTypeValue == null?-1:this.equipmentTypeValue,
                    currentPage:this.common.page,
                    pageSize:this.common.pageSize,
                    inquire:this.inputBoxValue
                };
                this.apiPost(url, data).then((res) => {
                    this.mqttDeviceData = res.data.pageList;
                    this.common.total = res.data.total;
                });
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
                this.getMqttList();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getMqttList();
            },
            reboot(row){
                this.$confirm('确定重启该设备吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let url = this.mqttIssuingInstructionsUrl;
                    let data = {
                        message: 'device_restart',
                        qos: 1,
                        ID: "01" + row.id,
                        type: 3,
                        extraData:"00"
                    };
                    console.log(data)
                    this.apiPost(url, data).then((res) => {
                        this.$commonFn.showTip("重启指令发送成功", 1);
                    }, (err) => {

                    });
                }).catch(() => {

                });
            },
            checkMqttTerminal(row){
                row.site_id = this.routeParam.siteId;
                row.equipmentTypeValue = this.equipmentTypeValue;
                row.inputBoxValue = this.inputBoxValue;
                row.page = this.common.page;
                row.type;
                this.$router.push({
                    name:'mqttInformation',
                    params:row,
                    // params:{
                    //     siteName:row.siteName,
                    //     id:row.siteId
                    // }
                });
            },
            delMqttTerminal(row){
                this.delMqttEquipId = row.id;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            deleteSuccess(){
                let url = this.deleteEquipUrl;
                let data = {
                    deviceIds:this.delMqttEquipId
                };
                this.apiPost(url, data).then((res) => {
                    if(res.status === 200){
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        this.getMqttList();
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {

                });
            },
        },
        mixins: [http]
    }
</script>

<style scoped>

</style>
