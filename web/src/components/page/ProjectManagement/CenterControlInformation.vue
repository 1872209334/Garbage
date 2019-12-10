<template>

    <div style="height: 100%;">
        <div class="efm-info" style="height: 100%; width:calc(100% - 185px); background-color: #F0F2F5; float: left;">
            <div class="pageBox">
                <div class="myNavigation">
                    <el-breadcrumb separator="/" style="display: inline-block">
                        <el-breadcrumb-item>
                            <span style="font-size: 16px;color:#303313;">项目管理</span>
                        </el-breadcrumb-item>
                        <el-breadcrumb-item>
                            <span style="font-size: 14px;color:#606266;">中控详情</span>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                </div>

                <div style="display: inline-block; height:40px;float: right;margin: 0 16px 6px 0;">
                    <router-link v-if="actionList.indexOf('setAlarmEnable')>-1" :to="{name:'centerControlAlarm',params:{id:this.routeParam.id,efmName:this.routeParam.efmName}}">
                        <el-button class="v-1-0-0-Button" icon="el-icon-setting">告警设置</el-button>
                    </router-link>
                </div>

                <div style="height:420px; margin: 24px 16px 1% 16px;">
                    <table style="width: 100%; font-size: 14px;">

                        <tr>
                            <td width="145px" style="background-color: #F6F7FB;">
                                项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                目 ：</td>
                            <td class="pricrTd" width="170px">{{routeParam.siteLabel}}</td>
                            <td width="145px" style="background-color: #F6F7FB;" >
                                设 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                备 ：</td>
                            <td class="pricrTd" width="170px">{{routeParam.efmName}}</td>
                            <td width="145px" style="background-color: #F6F7FB;letter-spacing: 2.6px;">
                                &nbsp;设&nbsp;&nbsp;备&nbsp;&nbsp;号 ：</td>
                            <td class="pricrTd" width="170px">{{parseInt(efmInfo.device_code,16)}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;">
                                位&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                置 ：</td>
                            <td colspan="5" class="pricrTd" style="padding: 0;text-indent: 0">
                                <input type="text" v-model="efmInfo.device_place" :disabled="editPlace" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changePlace"> {{placeBtn}}</span></td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB; letter-spacing: 0.2px;">
                                型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                号 ：</td>
                            <td class="pricrTd">{{efmInfo.model}}</td>
                            <td style="background-color: #F6F7FB;">设&nbsp;&nbsp;备&nbsp;&nbsp;类&nbsp;&nbsp;别 ：</td>
                            <td class="pricrTd">{{efmInfo.category}}</td>
                            <td style="background-color: #F6F7FB;">短信服务号码：</td>
                            <td class="pricrTd">{{efmInfo.message_phone}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB; letter-spacing: 3.2px;">当前报警数：</td>
                            <td class="pricrTd">{{efm.equipAlarmCount}}</td>
                            <td style="background-color: #F6F7FB;">硬&nbsp;&nbsp;件&nbsp;&nbsp;信&nbsp;&nbsp;息 ：</td>
                            <td class="pricrTd"></td>
                            <td style="background-color: #F6F7FB;">软&nbsp;&nbsp;件&nbsp;&nbsp;信&nbsp;&nbsp;息 ：</td>
                            <td class="pricrTd">{{efmInfo.version}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;">发&nbsp;&nbsp;布&nbsp;&nbsp;日&nbsp;&nbsp;期 ：</td>
                            <td class="pricrTd">{{efmInfo.publish_date}}</td>
                            <td style="background-color: #F6F7FB;">设&nbsp;&nbsp;备&nbsp;&nbsp;总&nbsp;&nbsp;数 ：</td>
                            <td class="pricrTd">{{efmInfo.device_total}}</td>
                            <td style="background-color: #F6F7FB;">总线1设备数 ：</td>
                            <td class="pricrTd">{{efmInfo.device_bus1}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">总线2设备数 ：</td>
                            <td class="pricrTd">{{efmInfo.device_bus2}}</td>
                            <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">总线3设备数 ：</td>
                            <td class="pricrTd">{{efmInfo.device_bus3}}</td>
                            <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">总线4设备数 ：</td>
                            <td class="pricrTd">{{efmInfo.device_bus4}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;">通&nbsp;&nbsp;讯&nbsp;&nbsp;主&nbsp;&nbsp;板 ：</td>
                            <td class="pricrTd"></td>
                            <td style="background-color: #F6F7FB;letter-spacing: 1.5px;">生产序列号 ：</td>
                            <td  colspan="3" class="pricrTd">{{efmInfo.serial_number}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB; letter-spacing: 3.2px;">设备描述符：</td>
                            <td class="pricrTd">{{efmInfo.specificator}}</td>
                            <!--<td style="background-color: #F6F7FB;">监&nbsp;&nbsp;控&nbsp;&nbsp;主&nbsp;&nbsp;板 ：</td>-->
                            <!--<td class="pricrTd"></td>-->
                            <td width="145px" style="background-color: #F6F7FB;" >
                                经 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                度 ：</td>
                            <td class="pricrTd" style="padding: 0;text-indent: 0">
                                <input type="text" v-model="efmInfo.longitute == undefined?'':efmInfo.longitute" :disabled="editLon" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changeLon"> {{lonBtn}}</span>
                            </td>
                            <td width="145px" style="background-color: #F6F7FB;" >
                                纬 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                度 ：</td>

                            <td class="pricrTd" style="padding: 0;text-indent: 0">
                                <input type="text" v-model="efmInfo.latitude == undefined?'':efmInfo.latitude" :disabled="editLat" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changeLat"> {{latBtn}}</span>
                            </td>
                        </tr>
                        <tr>
                            <td style="background-color: #F6F7FB;">中控离线使能：</td>
                            <td class="pricrTd">
                                <el-switch v-model="offlineEnableValue" @change="setOfflineEnable">
                                </el-switch>
                            </td>
                            <td style="background-color: #F6F7FB;">中控离线时间：</td>
                            <td  class="pricrTd" style="padding: 0;text-indent: 0">
                                <input type="number" min="1" v-model="offlineTimeValue" :disabled="editOfflineTime" style="width: calc(100% - 75px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />
                                <span style="display: inline-block">(分钟)</span>
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changeOfflineTime"> {{offlineTimeBtn}}</span>
                            </td>
                        </tr>
                    </table>
                </div>

                <div style="width:128px; height:34px; margin:14px auto auto auto;">

<!--                    <div style="width:56px; float: left; height:34px;">-->
<!--                        <el-button class="v-1-0-0-Button" @click="getEfmParam">查询</el-button>-->
<!--                    </div>-->

                    <div style="width:56px; float: left; height:34px; margin-left: 16px; ">
                        <el-button class="v-1-0-0-Button-Minor" @click="goBack">返回</el-button>
                    </div>

                </div>

            </div>
        </div>
    </div>
</template>

<script>
    import http from '../../../common/http';
    export default {
        name: "CenterControlInformation",
        data(){
            return{
                routeParam:{},
                efm:{},
                efmInfo:{},
                editPlace:true,
                placeBtn:"修改",
                editLon:true,
                lonBtn:"修改",
                editLat:true,
                latBtn:"修改",
                offlineTimeBtn:"修改",
                editOfflineTime:true,
                offlineEnableValue:false,
                offlineTimeValue:0
            }
        },
        created:function () {
            this.routeParam = this.$route.params;
            this.actionList = JSON.parse(sessionStorage.getItem('action'));
            this.getEfmInfo();
        },
        methods:{
            getEfmInfo(){
                let url = this.getEfmInfoUrl;
                let data = {
                    deviceId: this.routeParam.id,
                };
                this.apiPost(url, data).then((res) => {
                    this.efm = res.data;
                    this.efmInfo = res.data.getdeviceInfo;
                    this.offlineEnableValue = !this.efmInfo.offlineEnable;
                    this.offlineTimeValue = this.efmInfo.offLineTime/60;
                });
            },
            changePlace(){
                if(this.editPlace == true){
                    this.editPlace = false;
                    this.placeBtn = "确定";
                }
                else{
                    this.changeParam();
                }
            },
            changeLon(){
                if(this.editLon == true){
                    this.editLon = false;
                    this.lonBtn = "确定";
                }
                else{
                    this.changeParam();
                }
            },
            //设置efm离线使能
            setOfflineEnable(){
                let _loading = this.$commonFn.showLoading(2,'.efm-info');
                let url = this.editEfmInfoUrl;
                let data = {
                    deviceId:this.efmInfo.id,
                    offlineEnable: Number(!this.offlineEnableValue)
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if(res.status === 200){
                        this.$commonFn.showTip("修改成功", 1);
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            changeOfflineTime(){
                if(this.editOfflineTime == true){
                    this.editOfflineTime = false;
                    this.offlineTimeBtn = "确定";
                }
                else{
                    this.setOfflineTime();
                }
            },
            changeLat(){
                if(this.editLat == true){
                    this.editLat = false;
                    this.latBtn = "确定";
                }
                else{
                    this.changeParam();
                }
            },
            changeParam(){
                let url = this.editEfmInfoUrl;
                let data = {
                    devicePlace:this.efmInfo.device_place,
                    longitute:this.efmInfo.longitute,
                    latitude:this.efmInfo.latitude,
                    deviceId:this.efmInfo.id
                };
                this.apiPost(url, data).then((res) => {
                    this.editPlace = true;
                    this.placeBtn = "修改";
                    this.editLon = true;
                    this.lonBtn = "修改";
                    this.editLat = true;
                    this.latBtn = "修改";
                });
            },
            setOfflineTime(){
                let url = this.editEfmInfoUrl;
                let data = {
                    deviceId:this.efmInfo.id,
                    offlineTime:this.offlineTimeValue * 60
                };
                this.apiPost(url, data).then((res) => {
                    this.editOfflineTime = true;
                    this.offlineTimeBtn = "修改";
                });
            },
            //查询中控参数
            getEfmParam(){
                let queryContentArr = [
                    "udp_query_model","udp_query_category","udp_query_message_phone",
                    "udp_query_device_total",
                ];
                let queryContent = queryContentArr.join(',');
                let url = this.sendAdminSocketUrl;
                let data = {
                    siteCode:this.efmInfo.site_code,
                    deviceCode:this.efmInfo.device_code,
                    protocolName:queryContent,
                    extraData:''
                };
                this.apiPost(url,data).then((res) => {
                    let that = this;
                    if(res.status === 200){
                        let queryContentArr2 = [
                            "udp_query_device_bus1",
                            "udp_query_device_bus2",
                            "udp_query_device_bus3",
                            "udp_query_device_bus4",
                            "udp_query_longitute",
                            "udp_query_latitude"
                        ];
                        let queryContent2 = queryContentArr2.join(',');
                        let data2 = {
                            siteCode:this.efmInfo.site_code,
                            deviceCode:this.efmInfo.device_code,
                            protocolName:queryContent2,
                            extraData:''
                        };
                        this.apiPost(url,data2).then((res) => {
                            let queryContentArr3 = [
                                "udp_query_serial_number",
                                "udp_query_version",
                                "udp_query_specificator",
                            ];
                            let queryContent3 = queryContentArr3.join(',');
                            let data3 = {
                                siteCode:this.efmInfo.site_code,
                                deviceCode:this.efmInfo.device_code,
                                protocolName:queryContent3,
                                extraData:''
                            };
                            this.apiPost(url,data3).then((res) => {
                                that.$commonFn.showTip("查询成功",1);
                            },(err)=>{

                            });
                        },(err)=>{

                        });

                        setTimeout(function () {
                            that.getEfmInfo();
                        },5000);
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                });
            },
            goBack(){
                this.$router.push(
                    {
                        name:'deviceDetail',
                        params:{
                            id:this.routeParam.id,
                            efmName:this.routeParam.efmName
                        }
                    }
                );
            }
        },
        mixins: [http]
    }

</script>

<style scoped>
    table{
        border: 1px solid rgb(216,220,228);
        border-collapse: collapse;
    }

    td{
        font-weight: 400;
        text-align: center;
        border: 1px solid rgb(216,220,228);
        height:38px;
    }

    .pricrTd{
        font-weight: 400 !important;
        text-align: left !important;
        text-indent: 10px;
    }


</style>
