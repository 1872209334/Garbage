<template>
    <!--底色-->
    <div style=" width:100%; height:100%; background-color:#F0F2F5; overflow:auto;">

        <!--嵌套欢迎信息的div-->
        <div style="height:96px; background-color: white; margin: 16px 24px ;">
            <div style="float: left;padding-top: 20px;margin-left: 30px">
                <span style="width:100%; font-size: 16px;;">
                    {{userInfo.username}}，你好！
                </span>
                <br>
                <span style="color:rgb(170,170,170);width:100%;font-size: 14px;display:inline-block;margin-top: 12px">
                    欢迎回来继续使用监控管理平台
                </span>

            </div>
            <!--<img src="../../resource/img/home_img.png" style="float: right;width: 368px;height:93px;"/>-->
        </div>

        <!--嵌套导航信息的div-->
        <div style="height: 570px; background-color: white; margin:0 24px 12px 24px;">
            <!--省市联动下拉框-->
            <div style="padding-left: 24px; padding-top: 20px;">
                <table v-if="userInfo.roleType == 0">
                    <tr>
                        <td>
                            <!--省级下拉框-->
                            <el-select v-model="provinceValue" @change="changeProvince">
                                <el-option :label="defaultProvinceLabel" :value="defaultProvinceValue">
                                    不限
                                </el-option>
                                <el-option v-for="item in provinceList" :key="item.provinceId" :label="item.provinceName"
                                           :value="item.provinceId">
                                </el-option>
                            </el-select>
                        </td>
                        <td>
                            &nbsp;&nbsp;
                        </td>
                        <td>
                            <!--市级下拉框-->
                            <el-select v-model="cityValue" @change="changeArea">
                                <el-option :label="defaultCityLabel" :value="defaultCityValue">
                                    不限
                                </el-option>
                                <el-option v-for="item in cityList" :key="item.areaId" :label="item.areaName"
                                           :value="item.areaId">
                                </el-option>
                            </el-select>
                        </td>
                        <!--<div style="height:50px; margin:5px 16px 0 16px;">-->
                            <!--<div style="float: right; height:40px;">-->
                                <!--&lt;!&ndash;<el-button class="v-1-0-1-Button" icon="el-icon-delete">删除</el-button>&ndash;&gt;-->
                                <!--<el-button class="v-1-0-0-Button" icon="el-icon-plus" @click="openCreatePanel">新建项目</el-button>-->
                            <!--</div>-->
                        <!--</div>-->
                    </tr>

                </table>

            </div>
            <div style="margin-top: 25px;padding-left: 2.5%;padding-right: 2.5%;width: 95%;">
                <!--项目数-->
                <div class="main_count1">
                    <div style="width: 100%; height:70px;line-height: 70px;">
                        <!--<i class="okay myIcon-xiangmu" style="color: #F5A50B;"></i>-->
                        <div style="float: left; width: 30px;height: 30px; margin-top: 8px;">
                            <img src="../../resource/img/main_project.png" height="100%" width="100%">
                        </div>
                        <span class="count_title">项目数</span>
                        <span class="count_number" style="color: #F5A50B;">{{countData.siteCount}}</span>
                    </div>
                </div>

                <!--设备数-->
                <div class="main_count">
                    <div style="width: 100%; height:30%;line-height: 70px;">
                        <div style="float: left; width: 30px;height: 30px; margin-top: 8px;">
                            <img src="../../resource/img/main_equipment.png" height="100%" width="100%">
                        </div>
                        <span class="count_title">总设备数</span><!--countData.deviceCountOfEquip+-->
                        <span class="count_number" style="color: #387EE8">{{countData.arcCountOfEquip+countData.currentCountOfEquip}}</span>
                    </div>

                    <div style="width: 100%;height: 50%;margin-top: 10%; color:#777E8C;">
                        <!--<div style="height:30px;width: 100%;line-height: 30px;">-->
                            <!--<i style="width: 35px;height:20px;"></i>-->
                            <!--<span class="count_title">中控</span>-->
                            <!--<span style="float: right;margin-right: 30px;">{{countData.deviceCountOfEquip}}</span>-->
                        <!--</div>-->

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备一</span>
                            <span style="float: right;margin-right: 30px;">{{countData.arcCountOfEquip}}</span>
                        </div>

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备二</span>
                            <span style="float: right;margin-right: 30px;">{{countData.currentCountOfEquip}}</span>
                        </div>
                    </div>
                </div>

                <!--离线设备-->
                <div class="main_count">
                    <div style="width: 100%; height:30%;line-height: 70px;">
                        <div style="float: left; width: 30px;height: 30px; margin-top: 8px;">
                            <img src="../../resource/img/main_offline.png" height="100%" width="100%">
                        </div>
                        <span class="count_title">总离线数</span><!--countData.deviceCountOfEquipOffLine+-->
                        <span class="count_number" style="color: #909399">{{countData.arcCountOfEquipOffLine+countData.currentCountOfEquipOffLine}}</span>
                    </div>

                    <div style="width: 100%;height: 50%;margin-top: 10%;color:#777E8C;">
                        <!--<div style="height:30px;width: 100%;line-height: 30px;">-->
                            <!--<i style="width: 35px;height:20px;"></i>-->
                            <!--<span class="count_title">中控</span>-->
                            <!--<span style="float: right;margin-right: 30px;">{{countData.deviceCountOfEquipOffLine}}</span>-->
                        <!--</div>-->

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备一</span>
                            <span style="float: right;margin-right: 30px;">{{countData.arcCountOfEquipOffLine}}</span>
                        </div>

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备二</span>
                            <span style="float: right;margin-right: 30px;">{{countData.currentCountOfEquipOffLine}}</span>
                        </div>
                    </div>
                </div>

                <!--报警数-->
                <div class="main_count" style="border-right: 0">
                    <div style="width: 100%; height:30%;line-height: 70px; color:#777E8C;">
                        <div style="float: left; width: 30px;height: 30px; margin-top: 8px;">
                            <img src="../../resource/img/home_icon_alert.png" height="100%" width="100%">
                        </div>
                        <span class="count_title">总报警数</span><!--countData.deviceCountOfEquipAlarm+-->
                        <span class="count_number" style="color:#F56C6C">{{countData.arcCountOfEquipAlarm+countData.currentCountOfEquipAlarm}}</span>
                    </div>

                    <div style="width: 100%;height: 50%;margin-top: 10%;color:#777E8C;">
                        <!--<div style="height:30px;width: 100%;line-height: 30px;">-->
                            <!--<i style="width: 35px;height:20px;"></i>-->
                            <!--<span class="count_title">中控</span>-->
                            <!--<span style="float: right;margin-right: 30px;">{{countData.deviceCountOfEquipAlarm}}</span>-->
                        <!--</div>-->

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备一</span>
                            <span style="float: right;margin-right: 30px;">{{countData.arcCountOfEquipAlarm}}</span>
                        </div>

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备二</span>
                            <span style="float: right;margin-right: 30px;">{{countData.currentCountOfEquipAlarm}}</span>
                        </div>
                    </div>
                </div>

                <div style="background-color: #dcdfe6; float: left; width:100%; height:1px;margin-top: 10px;">
                    &nbsp;
                </div>

                <!--故障设备-->
                <div class="main_count1" style="border: none;margin-top: 10px;">
                    <div style="width: 100%; height:70px;line-height: 70px;">
                        <div style="float: left; width: 30px;height: 30px; margin-top: 8px;">
                            <img src="../../resource/img/fault_1_30.png" height="100%" width="100%">
                        </div>
                        <span class="count_title">总故障数</span><!--countData.deviceCountOfEquipFault+-->
                        <span class="count_number" style="color:#7b97c1">{{countData.arcCountOfEquipFault+countData.currentCountOfEquipFault}}</span>
                    </div>

                    <div style="width: 100%;height: 50%;margin-top: 10%;color:#777E8C;">
                        <!--<div style="height:30px;width: 100%;line-height: 30px;">-->
                            <!--<i style="width: 35px;height:20px;"></i>-->
                            <!--<span class="count_title">中控</span>-->
                            <!--<span style="float: right;margin-right: 30px;">{{countData.deviceCountOfEquipFault}}</span>-->
                        <!--</div>-->

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备一</span>
                            <span style="float: right;margin-right: 30px;">{{countData.arcCountOfEquipFault}}</span>
                        </div>

                        <div style="height:30px;width: 100%;line-height: 30px;display:inline-block;margin-top: 3%">
                            <i style="width: 35px;height:20px;"></i>
                            <span class="count_title">拉链设备二</span>
                            <span style="float: right;margin-right: 30px;">{{countData.currentCountOfEquipFault}}</span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import http from '../../common/http';
    export default {

        data() {
            return {
                userInfo: {},
                provinceList:[],
                cityList:[],
                defaultProvinceLabel: "不限",
                defaultProvinceValue: 0,
                provinceValue: 0,//省级下拉框的model
                defaultCityLabel: "不限",
                defaultCityValue: 0,
                cityValue: 0,//市级下拉框的model
                countData:{},
            }
        },
        created: function () {
            this.userInfo = JSON.parse(sessionStorage.getItem('user_info'));
            this.getProvinceList();
        },

        methods: {
            getProvinceList(){
                let _loading = this.$commonFn.showLoading(2,'.main-box');
                let url = this.getIndexUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    this.provinceList = res.data.provinceList;
                    this.countData = res.data.countData;
                    localStorage.setItem("alarmCount",this.countData.deviceCountOfEquipAlarm+this.countData.arcCountOfEquipAlarm+this.countData.currentCountOfEquipAlarm);
                    localStorage.setItem("faultCount",this.countData.deviceCountOfEquipFault+this.countData.arcCountOfEquipFault+this.countData.currentCountOfEquipFault);
                    localStorage.setItem("offlineCount",this.countData.deviceCountOfEquipOffLine+this.countData.arcCountOfEquipOffLine+this.countData.currentCountOfEquipOffLine);
                    if(sessionStorage.getItem('provinceId')){
                        this.provinceValue = parseInt(sessionStorage.getItem('provinceId'));
                        let url = this.getProvinceIndexUrl;
                        let data = {
                            provinceId: this.provinceValue
                        };
                        this.apiPost(url, data).then((res) => {
                            this.cityValue = 0;
                            this.cityList = res.data.cityList;
                            this.countData = res.data.countData;
                            if(sessionStorage.getItem('areaId')){
                                this.cityValue = parseInt(sessionStorage.getItem('areaId'));
                                let url = this.getAreaIndexUrl;
                                let data = {
                                    provinceId: this.provinceValue,
                                    areaId:this.cityValue
                                };
                                this.apiPost(url, data).then((res) => {
                                    this.countData = res.data.countData;
                                });
                            }
                        });
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            //打开新建项目界面
            openCreatePanel() {
                this.$router.push(
                    {
                        name: 'newProject',
                        params:{
                            type:0,
                            groupId:0,
                            place:"",
                            siteId:""
                        }
                    }
                );
            },
            changeProvince(){
                let url = this.getProvinceIndexUrl;
                let data = {
                    provinceId: this.provinceValue
                };
                this.apiPost(url, data).then((res) => {
                    this.cityValue = 0;
                    this.cityList = res.data.cityList;
                    this.countData = res.data.countData;
                    sessionStorage.setItem('provinceId',this.provinceValue);
                    sessionStorage.setItem('areaId',this.cityValue);
                });
            },
            changeArea(){
                let url = this.getAreaIndexUrl;
                let data = {
                    provinceId: this.provinceValue,
                    areaId:this.cityValue
                };
                this.apiPost(url, data).then((res) => {
                    this.countData = res.data.countData;
                    sessionStorage.setItem('areaId',this.cityValue);
                });
            }
        },
        mixins: [http]
    }

</script>

<style scoped>
    .main_count {
        width: 20.8%;
        border-right: 1px solid #dcdfe6;
        height: 220px;
        float: left;
        padding-left: 2%;
        padding-right: 2%;
    }
    .main_count1 {
        width: 20.8%;
        border-right: 1px solid #dcdfe6;
        height: 220px;
        float: left;
        padding-left: 0;
        padding-right: 2%;
    }
    .main_count i{
        font-size: 35px;
        float: left
    }
    .main_count1 i{
        font-size: 35px;
        float: left
    }
    .count_title{
        /*width:90px;*/
        color:#777E8C;
        font-size: 17px;
        float: left;
        margin-left: 8px;
    }
    .count_number{
        font-size: 30px;
        float: right;
        margin-right: 20px;
    }
</style>


