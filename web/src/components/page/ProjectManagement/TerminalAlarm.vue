<template>
    <div style="height: 100%;">
        <div style="height: 100%; width:calc(100% - 185px); background-color: #F0F2F5; float: left;">
            <div class="pageBox">

                <div class="myNavigation">
                    <el-breadcrumb separator="/" style="display: inline-block">
                        <el-breadcrumb-item>
                            <span style="font-size: 16px;color:#303313;">项目管理</span>
                        </el-breadcrumb-item>
                        <el-breadcrumb-item>
                            <span style="color: #111111;font-size: 14px;color:#606266;">终端告警设置</span>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                </div>

                <div style="width:95%; height:80%; margin:3% 16px;">

                    <table>
                        <tr>
                            <td class="msgTitle" style="letter-spacing: 1px; " >
                                温&nbsp;&nbsp;
                                度&nbsp;&nbsp;
                                上&nbsp;&nbsp;
                                限</td>
                            <td>：</td>
                            <td>
                                <div style="width:290px;">
                                    <el-input  v-model="equipInfo.temLimit"></el-input>
                                </div>
                            </td>
                            <td class="pricrTd" style="padding: 0;text-indent: 0">
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changeTem"> {{temBtn}}</span>
                            </td>
                        </tr>

                        <tr>
                            <td width="115px;" class="msgTitle" style="letter-spacing: 3.8px;"><br>单位电弧上限</td>
                            <td><br>：</td>
                            <td><br>
                                <div style="width:290px;" >
                                    <el-input  placeholder="请输入"></el-input>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td class="msgTitle" style="letter-spacing: 1px;"><br>
                                告&nbsp;&nbsp;
                                警&nbsp;&nbsp;
                                选&nbsp;&nbsp;
                                择</td>
                            <td><br>：</td>
                            <td colspan="2"><br>
                                <!--<div style="width: 300px;height:15px;padding:10px;overflow: auto;">-->

                                    <!--<div style="float:left; width:33%; height:33%">-->
                                        <!--<el-checkbox>过温告警</el-checkbox>-->
                                    <!--</div>-->
                                    <!--<div style="float:left; width:33%; height:33%">-->
                                        <!--<el-checkbox>电弧告警</el-checkbox>-->
                                    <!--</div>-->
                                    <!--<div style="float:left; width:33%; height:33%">-->
                                        <!--<el-checkbox>离线告警</el-checkbox>-->
                                    <!--</div>-->
                                <!--</div>-->
                                <div style="height:120px;padding:0;overflow: auto; ">
                                    <el-checkbox-group v-model="equipAlarmEnableGroup">
                                        <div style="float:left; width:33%; height:30px" v-for="item in alarmEnableArr">
                                            <el-checkbox :label="item.protocolName"></el-checkbox>
                                        </div>
                                    </el-checkbox-group>
                                    <el-checkbox-group v-model="arcEquipAlarmEnableGroup" v-if="equipInfo.type == 0">
                                        <div style="float:left; width:33%; height:30px" v-for="item in arcAlarmEnableArr">
                                            <el-checkbox :label="item.protocolName"></el-checkbox>
                                        </div>
                                    </el-checkbox-group>
                                    <el-checkbox-group v-model="combineEquipAlarmEnableGroup1" v-if="equipInfo.type != 0">
                                        <div style="float:left; width:33%; height:30px" v-for="item in combineAlarmEnableArr1">
                                            <el-checkbox :label="item.protocolName"></el-checkbox>
                                        </div>
                                    </el-checkbox-group>
                                    <el-checkbox-group v-model="combineEquipAlarmEnableGroup2" v-if="equipInfo.type != 0">
                                        <div style="float:left; width:33%; height:30px" v-for="item in combineAlarmEnableArr2">
                                            <el-checkbox :label="item.protocolName"></el-checkbox>
                                        </div>
                                    </el-checkbox-group>
                                    <el-checkbox-group v-model="combineEquipAlarmEnableGroup3" v-if="equipInfo.type != 0">
                                        <div style="float:left; width:33%; height:30px" v-for="item in combineAlarmEnableArr3">
                                            <el-checkbox :label="item.protocolName"></el-checkbox>
                                        </div>
                                    </el-checkbox-group>
                                    <el-checkbox-group v-model="combineEquipAlarmEnableGroup4" v-if="equipInfo.type != 0">
                                        <div style="float:left; width:33%; height:30px" v-for="item in combineAlarmEnableArr4">
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
                                    <el-button class="v-1-0-0-Button" @click="getAlarmEnable">查询</el-button>
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
        name: "terminalAlarm",
        data(){
            return{
                temBtn:"修改",
                editTem:true,
                routeParam:{},
                equipInfo:{},
                equipAlarmEnableGroup:[],
                alarmEnableArr:[
                    {protocol:'udp_query_fire_enable',protocolName:'电弧预警',protocolValue:'arcAlarmEn'},
                    {protocol:'udp_query_offline_enable',protocolName:'离线报警',protocolValue:'alive_en'},
                ],
                arcEquipAlarmEnableGroup:[],
                arcAlarmEnableArr:[
                    {protocol:'udp_query_temperature_enable',protocolName:'超温报警',protocolValue:'tempAlarmEn'},
                ],
                combineEquipAlarmEnableGroup1:[],
                combineAlarmEnableArr1:[
                    {protocol:'udp_query_power_cut_enable',protocolName:'A相电流报警使能',protocolValue:'acurrentWarningEnable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'B相电流报警使能',protocolValue:'bcurrentWarningEnable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'C相电流报警使能',protocolValue:'ccurrentWarningEnable'},
                ],
                combineEquipAlarmEnableGroup2:[],
                combineAlarmEnableArr2:[
                    {protocol:'udp_query_power_cut_enable',protocolName:'A相电压报警使能',protocolValue:'avoltageWarningEnable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'B相电压报警使能',protocolValue:'bvoltageWarningEnable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'C相电压报警使能',protocolValue:'cvoltageWarningEnable'},

                ],
                combineEquipAlarmEnableGroup3:[],
                combineAlarmEnableArr3:[
                    {protocol:'udp_query_power_cut_enable',protocolName:'0路温度报警使能',protocolValue:'temperatureWarning0Enable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'1路温度报警使能',protocolValue:'temperatureWarning1Enable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'2路温度报警使能',protocolValue:'temperatureWarning2Enable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'3路温度报警使能',protocolValue:'temperatureWarning3Enable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'4路温度报警使能',protocolValue:'temperatureWarning4Enable'},
                ],
                combineEquipAlarmEnableGroup4:[],
                combineAlarmEnableArr4:[
                    {protocol:'udp_query_power_cut_enable',protocolName:'漏电报警使能',protocolValue:'leakageWarningEnable'},
                    {protocol:'udp_query_power_cut_enable',protocolName:'门禁报警使能',protocolValue:'entranceWarningEnable'},
                ]
            }
        },
        created:function(){
            this.routeParam = this.$route.params;
            this.getEquipInfo();
        },
        methods:{
            changeTem(){
                let url = this.sendAdminSocketUrl;
                let data = {
                    siteCode:this.equipInfo.site_code,
                    deviceCode:this.equipInfo.device_code,
                    protocolName:'udp_set_tem_limit',
                    extraData:"sdf"
                };
                this.apiPost(url,data).then((res) => {
                    this.editTem = true;
                    this.temBtn = "修改";
                    if(res.status === 200){
                        this.$commonFn.showTip("设置成功",1);
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

            getEquipInfo(){
                let url = this.getEquipInfoUrl;
                let data = {
                    equipId:this.routeParam.equipId
                };
                this.apiPost(url, data).then((res) => {
                    if(res.status === 200){
                        this.equipInfo = res.data;
                        for(let i = 0;i < this.alarmEnableArr.length;i++){
                            //0为使能，1为屏蔽
                            if(!parseInt(this.equipInfo[this.alarmEnableArr[i].protocolValue])){
                                this.equipAlarmEnableGroup.push(this.alarmEnableArr[i].protocolName);
                            }
                        }
                        for(let i = 0;i < this.arcAlarmEnableArr.length;i++){
                            //0为使能，1为屏蔽
                            if(!parseInt(this.equipInfo[this.arcAlarmEnableArr[i].protocolValue])){
                                this.arcEquipAlarmEnableGroup.push(this.arcAlarmEnableArr[i].protocolName);
                            }
                        }
                        for(let i = 0;i < this.combineAlarmEnableArr1.length;i++){
                            //0为使能，1为屏蔽
                            if(!parseInt(this.equipInfo[this.combineAlarmEnableArr1[i].protocolValue])){
                                this.combineEquipAlarmEnableGroup1.push(this.combineAlarmEnableArr1[i].protocolName);
                            }
                        }
                        for(let i = 0;i < this.combineAlarmEnableArr2.length;i++){
                            //0为使能，1为屏蔽
                            if(!parseInt(this.equipInfo[this.combineAlarmEnableArr2[i].protocolValue])){
                                this.combineEquipAlarmEnableGroup2.push(this.combineAlarmEnableArr2[i].protocolName);
                            }
                        }
                        for(let i = 0;i < this.combineAlarmEnableArr3.length;i++){
                            //0为使能，1为屏蔽
                            if(!parseInt(this.equipInfo[this.combineAlarmEnableArr3[i].protocolValue])){
                                this.combineEquipAlarmEnableGroup3.push(this.combineAlarmEnableArr3[i].protocolName);
                            }
                        }
                        for(let i = 0;i < this.combineAlarmEnableArr4.length;i++){
                            //0为使能，1为屏蔽
                            if(!parseInt(this.equipInfo[this.combineAlarmEnableArr4[i].protocolValue])){
                                this.combineEquipAlarmEnableGroup4.push(this.combineAlarmEnableArr4[i].protocolName);
                            }
                        }
                    }
                });
            },
            setAlarmEnable(){
                //电弧探测器
                if(this.equipInfo.type == 0){
                    let extraData = "";
                    for(let i in this.alarmEnableArr){
                        if(this.equipAlarmEnableGroup.indexOf(this.alarmEnableArr[i].protocolName) === -1){
                            extraData = extraData + "01";
                        }
                        else{
                            extraData = extraData + "00";
                        }
                    }
                    for(let i in this.arcAlarmEnableArr){
                        if(this.arcEquipAlarmEnableGroup.indexOf(this.arcAlarmEnableArr[i].protocolName) === -1){
                            extraData = extraData + "01";
                        }
                        else{
                            extraData = extraData + "00";
                        }
                    }
                    let url = this.sendAdminSocketUrl;
                    let data = {
                        siteCode:this.equipInfo.site_code,
                        deviceCode:this.equipInfo.device_code,
                        protocolName:'udp_set_fire_enable,udp_set_offline_enable,udp_set_temperature_enable',
                        extraData:this.equipInfo.id.substring(10,14)+extraData.substring(0,2)+","+this.equipInfo.id.substring(10,14)+extraData.substring(2,4)+","+this.equipInfo.id.substring(10,14)+extraData.substring(4,6)
                    };
                    this.apiPost(url,data).then((res) => {
                        if(res.status === 200){
                            this.$commonFn.showTip("设置成功",1);
                            let that = this;
                            setTimeout(function () {
                                that.getEfmInfo();
                            },5000);
                        }else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    },(err)=>{

                    });
                }
                //组合式探测器
                else{
                    let extraData = "";
                    let extraData1 = "";
                    let extraData2 = "";
                    let extraData3 = "";
                    let extraData4 = "";
                    for(let i in this.alarmEnableArr){
                        if(this.equipAlarmEnableGroup.indexOf(this.alarmEnableArr[i].protocolName) === -1){
                            extraData = extraData + "01";
                        }
                        else{
                            extraData = extraData + "00";
                        }
                    }
                    //电流
                    for(let i in this.combineAlarmEnableArr1){
                        if(this.combineEquipAlarmEnableGroup1.indexOf(this.combineAlarmEnableArr1[i].protocolName) === -1){
                            extraData1 = "1"+extraData1 ;
                        }
                        else{
                            extraData1 = "0"+extraData1;
                        }

                    }

                    let extraDataInt1 = parseInt(extraData1,2);
                    extraData1 = extraDataInt1.toString(16);
                    if(extraData1.length < 2){
                        extraData1 = "0" + extraData1;
                    }
                    //电压
                    for(let i in this.combineAlarmEnableArr2){
                        if(this.combineEquipAlarmEnableGroup2.indexOf(this.combineAlarmEnableArr2[i].protocolName) === -1){
                            extraData2 = "1" +extraData2  ;
                        }
                        else{
                            extraData2 = "0" +extraData2 ;
                        }

                    }

                    let extraDataInt2 = parseInt(extraData2,2);
                    extraData2 = extraDataInt2.toString(16);
                    if(extraData2.length < 2){
                        extraData2 = "0" + extraData2;
                    }
                    //温度
                    for(let i in this.combineAlarmEnableArr3){
                        if(this.combineEquipAlarmEnableGroup3.indexOf(this.combineAlarmEnableArr3[i].protocolName) === -1){
                            extraData3 ="1"+ extraData3 ;
                        }
                        else{
                            extraData3 ="0"+extraData3 ;
                        }


                    }
                    let extraDataInt3 = parseInt(extraData3,2);
                    extraData3 = extraDataInt3.toString(16);
                    if(extraData3.length < 2){
                        extraData3 = "0" + extraData3;
                    }
                    //其他
                    for(let i in this.combineAlarmEnableArr4){
                        if(this.combineEquipAlarmEnableGroup4.indexOf(this.combineAlarmEnableArr4[i].protocolName) === -1){
                            extraData4 = "1"+extraData4 ;
                        }
                        else{
                            extraData4 = "0" +extraData4 ;
                        }


                    }
                    let extraDataInt4 = parseInt(extraData4,2);
                    extraData4 = extraDataInt4.toString(16);
                    if(extraData4.length < 2){
                        extraData4 = "0" + extraData4;
                    }
                    extraData=extraData+extraData1+extraData2+extraData3+extraData4;
                    let url = this.sendAdminSocketUrl;
                    let data = {
                        siteCode:this.equipInfo.site_code,
                        deviceCode:this.equipInfo.device_code,
                        protocolName:'udp_set_fire_enable,udp_set_offline_enable,udp_set_current_enable,udp_set_voltage_enable,udp_set_tem_enable,udp_set_other_enable',
                        extraData:this.equipInfo.id.substring(10,14)+extraData.substring(0,2)+","+this.equipInfo.id.substring(10,14)+extraData.substring(2,4)+","+
                            this.equipInfo.id.substring(10,14)+extraData.substring(4,6)+","+this.equipInfo.id.substring(10,14)+extraData.substring(6,8)+","+
                            this.equipInfo.id.substring(10,14)+extraData.substring(8,10)+","+this.equipInfo.id.substring(10,14)+extraData.substring(10,12)
                    };

                    this.apiPost(url,data).then((res) => {
                        if(res.status === 200){
                            this.$commonFn.showTip("设置成功",1);
                            let that = this;
                            setTimeout(function () {
                                that.getEfmInfo();
                            },5000);
                        }else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    },(err)=>{

                    });
                }
            },
            getAlarmEnable(){
                //电弧探测器
                if(this.equipInfo.type == 0){
                    let url = this.sendAdminSocketUrl;
                    let data = {
                        siteCode:this.equipInfo.site_code,
                        deviceCode:this.equipInfo.device_code,
                        protocolName:'udp_query_fire_enable,udp_query_offline_enable,udp_query_temperature_enable',
                        extraData:this.equipInfo.id.substring(10,14)+"00,"+this.equipInfo.id.substring(10,14)+"00,"+this.equipInfo.id.substring(10,14)+"00"
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
                }
                //组合式探测器
                else{
                    let extraData = "";
                    let url = this.sendAdminSocketUrl;
                    let data = {
                        siteCode:this.equipInfo.site_code,
                        deviceCode:this.equipInfo.device_code,
                        protocolName:'udp_query_fire_enable,udp_query_offline_enable,udp_query_current_enable,udp_query_voltage_enable,udp_query_tem_enable,udp_query_other_enable',
                        extraData:this.equipInfo.id.substring(10,14)+"00"+","+this.equipInfo.id.substring(10,14)+"00"+","+
                            this.equipInfo.id.substring(10,14)+"00"+","+this.equipInfo.id.substring(10,14)+"00"+","+
                            this.equipInfo.id.substring(10,14)+"00"+","+this.equipInfo.id.substring(10,14)+"00"
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
                }
            },
            goBack(){
                this.$router.push(
                    {
                        name:'terminalInformation',
                        params:{
                            equipId:this.routeParam.equipId,
                            efmId:this.routeParam.efmId,
                            efmName:this.routeParam.efmName,
                            siteLabel:this.routeParam.efmName.siteLabel
                        }
                    }
                );
            },
        },
        mixins: [http]
    }
</script>

<style scoped>
    .msgTitle{
        font-size: 14px;
    }
    .pricrTd{
        font-weight: 400 !important;
        text-align: left !important;
        text-indent: 10px;
    }
</style>
