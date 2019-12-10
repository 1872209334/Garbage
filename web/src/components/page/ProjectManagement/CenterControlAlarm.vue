<template>
    <div style="height: 100%;">
        <div style="height: 100%; width:calc(100% - 185px); background-color: #F0F2F5; float: left;">
            <div  class="pageBox">

                <div class="myNavigation">
                    <el-breadcrumb separator="/" style="display: inline-block">
                        <el-breadcrumb-item>
                            <span style="font-size: 16px;color:#303313;">项目管理</span>
                        </el-breadcrumb-item>
                        <el-breadcrumb-item>
                            <span style="font-size: 14px;color:#606266;">中控告警使能设置</span>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                </div>

                <div style=" height:80%; margin:2% 16px auto 16px;">

                    <table style="font-size: 14px; ">
                        <tr>
                            <td class="msgTitle" style="letter-spacing: 1px; " >
                                温&nbsp;&nbsp;
                                度&nbsp;&nbsp;
                                上&nbsp;&nbsp;
                                限</td>
                            <td>：</td>
                            <td>
                                <div style="width:60%;">
                                    <el-input v-model="efmInfo.maxTemperature" placeholder="请输入"></el-input>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td width="115px;" class="msgTitle" style="letter-spacing: 3.8px;"><br>单位电弧上限</td>
                            <td><br>：</td>
                            <td><br>
                                <div style="width:60%;">
                                    <el-input v-model="efmInfo.perArc" placeholder="请输入"></el-input>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td class="msgTitle" style="letter-spacing: 1px;vertical-align: text-top; "><br>
                                告&nbsp;&nbsp;
                                警&nbsp;&nbsp;
                                选&nbsp;&nbsp;
                                择</td>
                            <td style="vertical-align: text-top; "><br>：</td>
                            <td colspan="2"><br>
                                <div style="width: 60%;height:100px;padding:0;overflow: auto; ">
                                    <el-checkbox-group v-model="efmAlarmEnableGroup">
                                        <div style="float:left; width:33%; height:30px" v-for="item in alarmEnableArr">
                                            <el-checkbox :label="item.protocolName"></el-checkbox>
                                        </div>
                                    </el-checkbox-group>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td></td><td></td>
                            <td><br>
                                <div style="float: left; height:40px; width:56px;">
                                    <el-button class="v-1-0-0-Button" @click="setAlarmEnable">保存</el-button>
                                </div>
                                <div style="float: left; height:40px; width:56px;margin-left: 16px;">
                                    <el-button class="v-1-0-0-Button" @click="queryAlarmEnable">查询</el-button>
                                </div>
                                <div style="float: left; height:40px;width:56px; margin-left: 16px;">
                                    <el-button class="v-1-0-0-Button-Minor" @click="goBack">返回</el-button>
                                </div>
                            </td>
                        </tr>
                    </table>

                </div>


            </div>
        </div>


    </div>
</template>

<script>
    import http from '../../../common/http';
    export default {
        data() {
            return {
                routeParam:{},
                efmInfo:{},
                efmAlarmEnableGroup:[],
                alarmEnableArr:[
                    {protocol:'udp_query_power_cut_enable',protocolName:'主电源切断',protocolValue:'power_cut_enable'},
                    {protocol:'udp_query_no_battery_enable',protocolName:'无电池',protocolValue:'no_battery_enable'},
                    {protocol:'udp_query_low_voltage_enable',protocolName:'蓄电池电压低',protocolValue:'low_voltage_enable'},
                    {protocol:'udp_query_printer_offline_enable',protocolName:'打印机脱机',protocolValue:'printer_offline_enable'},
                    {protocol:'udp_query_printer_out_paper_enable',protocolName:'打印机缺纸',protocolValue:'printer_out_paper_enable'},
                    {protocol:'udp_query_printer_communication_failure_enable',protocolName:'打印机通信故障',protocolValue:'printer_communication_failure_enable'},
                    {protocol:'udp_query_communication_offline_enable',protocolName:'通信板掉线',protocolValue:'communication_offline_enable'},
                    {protocol:'udp_query_device_offline_enable',protocolName:'设备掉线',protocolValue:'device_offline_enable'}
                ]
            };
        },
        created:function () {
            this.routeParam = this.$route.params;
            this.getEfmInfo();
        },
        methods: {
            getEfmInfo(){
                let url = this.getEfmInfoUrl;
                let data = {
                    deviceId: this.routeParam.id,
                };
                this.apiPost(url, data).then((res) => {
                    this.efmInfo = res.data.getdeviceInfo;
                    for(let i = 0;i < this.alarmEnableArr.length;i++){
                        //0为使能，1为屏蔽
                        if(!this.efmInfo[this.alarmEnableArr[i].protocolValue]){
                            this.efmAlarmEnableGroup.push(this.alarmEnableArr[i].protocolName);
                        }
                    }
                    console.log(this.efmAlarmEnableGroup);
                });
            },
            //告警设置
            setAlarmEnable(){
                let extraDataArr = [];
                for(let i in this.alarmEnableArr){
                    if(this.efmAlarmEnableGroup.indexOf(this.alarmEnableArr[i].protocolName) === -1){
                        extraDataArr.unshift('1');
                    }
                    else{
                        extraDataArr.unshift('0');
                    }
                }
                let extraDataStr = extraDataArr.join('');
                let extraData = parseInt(extraDataStr,2);
                extraData = extraData.toString(16);
                if(extraData.length < 2){
                    extraData = "0" + extraData;
                }
                let url = this.sendAdminSocketUrl;
                let data = {
                    siteCode:this.efmInfo.site_code,
                    deviceCode:this.efmInfo.device_code,
                    protocolName:'udp_set_efm_alarm_enable',
                    extraData:extraData + "000000"
                };
                this.apiPost(url,data).then((res) => {
                    if(res.status === 200){
                        this.$commonFn.showTip("设置成功",1);
                        let that = this;
                        setTimeout(function () {
                            that.queryAlarmEnable();
                        },2500);
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                });
            },
            //查询告警使能
            queryAlarmEnable(){
                let url = this.sendAdminSocketUrl;
                let data = {
                    siteCode:this.efmInfo.site_code,
                    deviceCode:this.efmInfo.device_code,
                    protocolName:'udp_query_efm_alarm_enable',
                    extraData:''
                };
                this.apiPost(url,data).then((res) => {
                    if(res.status === 200){
                        this.$commonFn.showTip("查询成功",1);
                        let that = this;
                        setTimeout(function () {
                            that.getEfmInfo();
                        },5000);
                    }else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                });
            },
            goBack(){
                this.$router.push(
                    {
                        name:'centerControlInformation',
                        params:{
                            id:this.routeParam.id,
                            efmName:this.routeParam.efmName
                        }
                    }
                );
            },
        },
        mixins: [http]
    };
</script>

<style scoped>
    .msgTitle{
        letter-spacing: 6px;
    }
</style>


