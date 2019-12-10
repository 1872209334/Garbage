<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto; ">

        <div class="pageBox" style="overflow-y: auto">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">报警管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">处理历史</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">处理历史详情</span>
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
                            中&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            控 ：</td>
                        <td class="pricrTd" width="200px">&nbsp;&nbsp;{{routeParam.niName}}</td>
                        <td width="155px" style="background-color: #F6F7FB;">
                            编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            号 ：</td>
                        <td class="pricrTd" width="200px">&nbsp;&nbsp;{{routeParam.serialNumber?routeParam.serialNumber:'无'}}</td>
                    </tr>

                    <tr>
                        <td style="background-color: #F6F7FB;">
                            状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            态 ：</td>
                        <td class="pricrTd" style="color: #71c647">&nbsp;&nbsp;已审核</td>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            处&nbsp;&nbsp;理&nbsp;&nbsp;人&nbsp;&nbsp;员 ：</td>
                        <td class="pricrTd">&nbsp;&nbsp;{{warnInfo.appUserAccount?warnInfo.appUserAccount:""}}</td>
                        <td style="background-color: #F6F7FB;">
                            电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            话 ：</td>
                        <td class="pricrTd">&nbsp;&nbsp;{{warnInfo.appUserPhone?warnInfo.appUserPhone:""}}</td>
                    </tr>
                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            报&nbsp;&nbsp;警&nbsp;&nbsp;原&nbsp;&nbsp;因 ：</td>
                        <td colspan="5" style="color: red;" align="left"  class="pricrTd">
                            &nbsp;&nbsp;{{routeParam.protocolNode}}
                        </td>
                    </tr>
                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            报&nbsp;&nbsp;警&nbsp;&nbsp;时&nbsp;&nbsp;间 ：</td>
                        <td colspan="5" class="pricrTd">&nbsp;&nbsp;{{warnInfo.warnTime?warnInfo.warnTime:""}}</td>
                    </tr>
                    <tr>
                        <td style="background-color: #F6F7FB;letter-spacing: 0.2px;">
                            处&nbsp;&nbsp;理&nbsp;&nbsp;时&nbsp;&nbsp;间 ：</td>
                        <td colspan="5" class="pricrTd">&nbsp;&nbsp;{{warnInfo.dealTime?warnInfo.dealTime:""}}</td>
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

            <div style="width:128px; height:36px; margin:14px auto 14px auto;">
                <div style="width:56px;float: left; height:36px; margin-left: 16px;">
                    <router-link to="/alarmHistory">
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
        name: "historyLog",
        data(){
            return{
                routeParam:{},
                warnInfo:{},
                imgList:[],
            }
        },
        created : function(){
            this.routeParam = this.$route.params;
            console.log(this.routeParam);
            this.getWarnInfo();
        },
        methods:{
            getWarnInfo(){
                let url = this.getWarnInfoUrl;
                let data = {
                    warnId:this.routeParam.id
                };
                this.apiPost(url, data).then((res) => {
                    console.log(res);
                    if (res.status === 200) {
                        if(res.data){
                            this.warnInfo = res.data;
                            if(this.warnInfo.dealTime){
                                this.warnInfo.dealTime = this.$commonFn.formatDate(this.warnInfo.dealTime);
                            }
                            if(this.warnInfo.warnTime){
                                this.warnInfo.warnTime = this.$commonFn.formatDate(this.warnInfo.warnTime);
                            }
                            if(this.warnInfo.imgUrl){
                                this.imgList = this.warnInfo.imgUrl.split(',');
                                console.log(this.imgList);
                            }
                        }
                        else{
                            this.warnInfo.appUserPhone = "";
                            this.warnInfo.appUserAccount = "";
                            this.warnInfo.dealRemark = "";
                            this.warnInfo.dealTime = "";
                        }
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
