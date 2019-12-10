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
                            <span style="color: #111111;font-size: 14px;color:#606266;">终端详情</span>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                </div>

                <div style="display: inline-block; height:40px;float: right;margin: 0 16px 6px 0;">
                    <el-button class="v-1-0-0-Button" icon="el-icon-setting" @click="checkTerminalAlarm" v-if="actionList.indexOf('setAlarmEnable')>-1">告警设置</el-button>
                </div>

                <div style="height:280px; margin: 24px 16px 1% 16px;">
                    <table style="width: 100%; font-size: 14px;">
                        <tr>
                            <td width="145px" style="background-color: #F6F7FB;">
                                项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                目 ：</td>
                            <td class="pricrTd">{{equipInfo.siteLabel}}</td>
                            <td width="145px" style="background-color: #F6F7FB;">
                                设&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                备 ：</td>
                            <td class="pricrTd">{{equipInfo.efmLabel}}</td>
                            <td width="145px" style="background-color: #F6F7FB;">

                            编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            号 ：</td>
                            <td class="pricrTd">{{equipInfo.serialNumber}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;">
                                备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                注 ：</td>
                            <td colspan="5" class="pricrTd" style="padding: 0;text-indent: 0">
                                <input type="text" v-model="equipInfo.equip_place" :disabled="editPlace" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;" />
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changePlace"> {{placeBtn}}</span>
                            </td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;">
                                类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                型 ：</td>
                            <td class="pricrTd">{{equipInfo.eType}}</td>
                            <td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">
                                灵&nbsp;&nbsp;&nbsp;敏&nbsp;&nbsp;&nbsp;度 ：</td>
                            <td class="pricrTd">{{equipInfo.sens}}</td>
                            <td style="background-color: #F6F7FB;">单位电弧次数：</td>
                            <td class="pricrTd">{{equipInfo.cnt}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                                时&nbsp;&nbsp;间&nbsp;&nbsp;范&nbsp;&nbsp;围 ：</td>
                            <td class="pricrTd">{{equipInfo.time}}</td>
                            <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                                温&nbsp;&nbsp;度&nbsp;&nbsp;上&nbsp;&nbsp;限 ：</td>
                            <td class="pricrTd">{{equipInfo.tempration}}</td>
                            <td style="background-color: #F6F7FB;">
                                状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                态 ：</td>
<!--                            <td  class="pricrTd">{{equipInfo.state}}</td>-->
                            <td  class="pricrTd">{{deviceState}}</td>

                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                                硬&nbsp;&nbsp;件&nbsp;&nbsp;版&nbsp;&nbsp;本 ：</td>
                            <td class="pricrTd">{{equipInfo.boardVersion}}</td>
                            <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                                软&nbsp;&nbsp;件&nbsp;&nbsp;版&nbsp;&nbsp;本 ：</td>
                            <td class="pricrTd">{{equipInfo.softVersion}}</td>
                            <td style="background-color: #F6F7FB;">
                                信&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                息 ：</td>
                            <td class="pricrTd">{{equipInfo.message}}</td>
                        </tr>

                        <tr>
                            <td style="background-color: #F6F7FB;">软件编辑日期：</td>
                            <td class="pricrTd">{{equipInfo.releaseData}}</td>
                            <td style="background-color: #F6F7FB;letter-spacing: 3px;padding-left: 3px;">产品序列号：</td>
                            <td class="pricrTd" colspan="3">{{equipInfo.serial}}</td>
                        </tr>

                    </table>
                </div>

                <div style="width:128px; height:34px; margin:14px auto auto auto;">

                    <!--<div style="width:56px; float: left; height:34px;">-->
                        <!--<el-button class="v-1-0-0-Button">查询</el-button>-->
                    <!--</div>-->

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

    let deviceState = '';
    export default {
        name: "terminalInformation",
        data(){
            return{
                routeParam:{},
                equipInfo:{},
                editPlace:true,
                placeBtn:"修改"
            }
        },
        created:function(){
            this.routeParam = this.$route.params;
            this.actionList = JSON.parse(sessionStorage.getItem('action'));
            this.getEquipInfo();
        },
        methods:{
            //设备状态转换
            formatState(){
                if(this.equipInfo.state == 0){
                    deviceState = "在线";
                }else if(this.equipInfo.state == 1){
                    deviceState = "离线";
                }
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
            changeParam(){
                let url = this.editEquipInfoUrl;
                let data = {
                    equipId:this.equipInfo.id,
                    equipPlace:this.equipInfo.equip_place
                };
                this.apiPost(url, data).then((res) => {
                    this.editPlace = true;
                    this.placeBtn = "修改";
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
                        this.equipInfo.serialNumber = parseInt(this.equipInfo.line_code) + 1 + "-" + this.equipInfo.addr;
                        let typeArr = ['拉链探测器一','拉链探测器二'];
                        this.equipInfo.eType = typeArr[this.equipInfo.type];
                        this.equipInfo.siteLabel = this.equipInfo.siteName?this.equipInfo.siteName:this.equipInfo.siteCode;
                        if(this.equipInfo.niNameDevice){
                            this.equipInfo.efmLabel = this.equipInfo.niNameDevice;
                        }
                        else if(this.equipInfo.specificatorDevice){
                            this.equipInfo.efmLabel = this.equipInfo.specificatorDevice;
                        }
                        else{
                            this.equipInfo.efmLabel = parseInt(this.equipInfo.deviceCode,16);
                        }
                        console.log(this.equipInfo);
                    }
                });
            },
            checkTerminalAlarm(){
                this.$router.push(
                    {
                        name:'terminalAlarm',
                        params:{
                            equipId:this.routeParam.equipId,
                            efmId:this.routeParam.efmId,
                            efmName:this.routeParam.efmName,
                            siteLabel:this.routeParam.efmName.siteLabel
                        }
                    }
                );
            },
            goBack(){
                this.$router.push(
                    {
                        name:'deviceDetail',
                        params:{
                            id:this.routeParam.efmId,
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
        width: 170px;
    }

</style>
