<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto; ">

        <div class="pageBox" style="overflow-y: auto">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">报警管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">故障日志</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">终端离线信息</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="margin: 1% 16px auto 16px;">
                <table style="width: 100%; font-weight: 300;font-size: 14px;">
                    <tr>
                        <td width="155px" style="background-color: #F6F7FB;">
                            项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            目 ：</td>
                        <td class="pricrTd" width="200px">&nbsp;&nbsp;{{routeParam.siteName}}</td>
                        <td width="155px" style="background-color: #F6F7FB;">
                            所&nbsp;&nbsp;属&nbsp;&nbsp;项&nbsp;&nbsp;目 ：</td>
                        <td class="pricrTd" width="200px">&nbsp;&nbsp;{{routeParam.niName}}</td>
                        <td width="155px" style="background-color: #F6F7FB;">
                            终&nbsp;&nbsp;端&nbsp;&nbsp;编&nbsp;&nbsp;号 ：</td>
                        <td class="pricrTd" width="200px">&nbsp;&nbsp;{{routeParam.serialNumber}}</td>
                    </tr>

                    <tr>
                        <td style="background-color: #F6F7FB;">
                            成&nbsp;&nbsp;品&nbsp;&nbsp;计&nbsp;&nbsp;数  ：</td>
                        <td class="pricrTd">&nbsp;&nbsp;{{routeParam.tempration}}</td>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            次&nbsp;&nbsp;品&nbsp;&nbsp;计&nbsp;&nbsp;数 ：</td>
                        <td class="pricrTd">&nbsp;&nbsp;{{routeParam.defectiveSum}}</td>
                        <td style="background-color: #F6F7FB;">
                            循&nbsp;&nbsp;环&nbsp;&nbsp;次&nbsp;&nbsp;数 ：</td>
                        <td class="pricrTd">&nbsp;&nbsp;{{routeParam.totalCycles}}</td>
                    </tr>

                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            离&nbsp;&nbsp;线&nbsp;&nbsp;时&nbsp;&nbsp;间 ：</td>
                        <td colspan="5" class="pricrTd">&nbsp;&nbsp;{{routeParam.warnTime}}</td>
                    </tr>
                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            处&nbsp;&nbsp;理&nbsp;&nbsp;时&nbsp;&nbsp;间 ：</td>
                        <td colspan="5" class="pricrTd">&nbsp;&nbsp;{{routeParam.updateTime}}</td>
                    </tr>

                    <!--<tr>-->
                        <!--<td style="background-color: #F6F7FB;">-->
                            <!--位&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                            <!--置 ：</td>-->
                        <!--<td colspan="5" class="pricrTd">&nbsp;&nbsp;</td>-->
                    <!--</tr>-->

                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            是&nbsp;&nbsp;否&nbsp;&nbsp;确&nbsp;&nbsp;认 ：</td>
                        <td colspan="5" class="pricrTd" style="color: #71c647">
                            &nbsp;&nbsp;{{routeParam.isDeal}}
                        </td>
                    </tr>

                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            离&nbsp;&nbsp;线&nbsp;&nbsp;原&nbsp;&nbsp;因 ：</td>
                        <td colspan="5" style="color: red;" align="left"  class="pricrTd">
                            &nbsp;&nbsp;{{routeParam.protocolNode}}
                        </td>
                    </tr>

                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            现&nbsp;&nbsp;场&nbsp;&nbsp;图&nbsp;&nbsp;片 ：</td>
                        <td colspan="5" style="color: red;padding:12px 4px;" align="left"  class="pricrTd">
                            &nbsp;&nbsp;
                            <div v-show="warnInfo.imgUrl">
                                <img v-for="item in imgList" :src="item" style="width: 300px;height: 240px;margin-left: 5px">
                            </div>
                        </td>
                    </tr>
                </table>
            </div>

            <div style="width:128px; height:36px; margin:14px auto auto auto;">

                <div style="width:56px; float: left;height:36px;" v-show="warnInfo.dealTime">
                    <el-button class="v-1-0-0-Button" @click="auditAlarm">审核</el-button>
                </div>

                <div style="width:56px;float: left; height:36px; margin-left: 16px;">
                    <router-link to="/off_LineLog">
                        <el-button class="v-1-0-0-Button-Minor">返回</el-button>
                    </router-link>
                </div>

            </div>

        </div>

    </div>
</template>

<script>
    import http from '../../../common/http';
    import { MessageBox } from 'element-ui';
    export default {
        name: "offLogDev",

        data(){
            return{
                routeParam:{},
                warnInfo:{},
                imgList:[],
            }
        },
        created : function(){
            this.routeParam = this.$route.params;
            if(this.routeParam.isDeal == '0'){
                this.routeParam.isDeal = "未处理";
            }
            else{
                this.routeParam.isDeal = "已处理";
            }
            this.getWarnInfo();
        },
        methods:{
            getWarnInfo(){
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getWarnInfoUrl;
                let data = {
                    warnId:this.routeParam.id
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        if(res.data){
                            this.warnInfo = res.data;
                        }
                        else{
                            this.warnInfo.appUserPhone = "";
                            this.warnInfo.appUserAccount = "";
                            this.warnInfo.dealRemark = "";
                            this.warnInfo.dealTime = "";
                        }
                        if(this.warnInfo.dealTime){
                            this.warnInfo.dealTime = this.$commonFn.formatTime(this.warnInfo.dealTime);
                        }
                        if(this.warnInfo.imgUrl){
                            this.imgList = this.warnInfo.imgUrl.split(',');
                        }
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            auditAlarm(){
                MessageBox.confirm('是否通过该报警处理？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let url = this.auditAlarmUrl;
                    let data = {
                        id:this.warnInfo.id,
                    };
                    this.apiPost(url,data).then((res) => {
                        if(res.status === 200){
                            this.$commonFn.showTip("审核通过",1);
                            this.$router.push('/off_LineLog');
                        }
                        else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    },(err)=>{

                    });
                }).catch(() => {

                });

                // this.adminId = data.id;
                // this.$commonFn.showConfirm(this.deleteSuccess);
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
    }

</style>
