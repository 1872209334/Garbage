<template>
    <div class="header">
        <div class="logo" style="width: 25px; float: left;height: 100%">
            <img :src="logoUrl">
        </div>
        <div style="height: 100%;line-height: 64px; width: 450px; float: left;">
            <p style="letter-spacing:2px;font-size: 1.5rem;margin-top: 1px;">
                {{projectTitle}}物联大数据管理平台
            </p>
        </div>
        <div>
            <div class="header-user-con" style="float: right;margin-right: 24px">
<!--                <div class="btn-fullscreen" style="position: relative;top: 3px;">-->
<!--                    <router-link target="_blank" :to="{path:'/largeSizePage'}">-->
<!--                        <el-tooltip effect="dark" :content="fullscreen?`取消大屏显示`:`大屏显示`" placement="bottom">-->
<!--                            <i class="okay myIcon-diannao"-->
<!--                               style="font-size: 22px; color: white;"></i>-->
<!--                        </el-tooltip>-->
<!--                    </router-link>-->
<!--                </div>-->
                <!--报警图标-->
                <!--<el-dropdown>-->
                      <span class="el-dropdown-link" style="margin-left: 30px;margin-top: 4px;">
                          <el-badge :value="alarmNum" :max="99" class="item">
                            <i class="okay myIcon-jingbao" style="color: #fff;font-size: 25px;cursor: pointer;" @click="goAlarm">
                            </i>
                        </el-badge>
                      </span>
                    <!--悬浮出现的下拉框-->
                    <!--<el-dropdown-menu slot="dropdown" style="padding: 0 !important;">-->
                        <!--<div style="width:320px; height:230px; overflow: auto;">-->
                            <!--<el-dropdown-item style="padding:0;margin-left: 15px">-->
                                <!--<div style="width:285px; height:90px;background-color: #fde9e8">-->
                                    <!--<div style="float: left;width:40px;height: 40px;">-->
                                        <!--<div style="width:30px;height: 30px;margin-left: 0;margin-top: 27px;">-->
                                            <!--<img src="../../resource/img/home_icon_alert.png" height="100%" width="100%">-->
                                        <!--</div>-->
                                    <!--</div>-->
                                    <!--<div style="float: left;width:200px;font-size: 15px;font-weight: 600;margin-top: 16px;text-align: left;">-->
                                        <!--有一条新的报警信息待<a style="color:#387EE8;">处理</a>-->
                                    <!--</div>-->
                                    <!--<div style="float: left;width:170px;font-size: 14px;margin-left: 20px;color: #73767a;">-->
                                        <!--2019.01.01&nbsp;22:00:00-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div style="width:280px; height:1px; background-color: #F0F2F5; margin: auto;">&nbsp;</div>-->
                            <!--</el-dropdown-item>-->
                            <!--<el-dropdown-item>-->
                                <!--<div style="width:285px; height:90px;">-->
                                    <!--<div style="float: left;width:40px;height: 40px;">-->
                                        <!--<div style="width:30px;height: 30px;margin-left: 0px;margin-top: 27px;">-->
                                            <!--<img src="../../resource/img/home_icon_alert.png" height="100%" width="100%">-->
                                        <!--</div>-->
                                    <!--</div>-->
                                    <!--<div style="float: left;width:200px;font-size: 15px;font-weight: 600;margin-top: 16px;text-align: left;">-->
                                        <!--有一条新的报警信息待<a style="color:#387EE8;">处理</a>-->
                                    <!--</div>-->
                                    <!--<div style="float: left;width:170px;font-size: 14px;margin-left: 20px;color: #73767a;">-->
                                        <!--2019.01.01&nbsp;22:00:00-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div style="width:280px; height:1px; background-color: #F0F2F5; margin: auto;">&nbsp;</div>-->
                            <!--</el-dropdown-item>-->
                            <!--<el-dropdown-item>-->
                                <!--<div style="width:285px; height:90px;">-->
                                    <!--<div style="float: left;width:40px;height: 40px;">-->
                                        <!--<div style="width:30px;height: 30px;margin-left: 0px;margin-top: 27px;">-->
                                            <!--<img src="../../resource/img/home_icon_alert.png" height="100%" width="100%">-->
                                        <!--</div>-->
                                    <!--</div>-->
                                    <!--<div style="float: left;width:200px;font-size: 15px;font-weight: 600;margin-top: 16px;text-align: left;">-->
                                        <!--有一条新的报警信息待<a style="color:#387EE8;">处理</a>-->
                                    <!--</div>-->
                                    <!--<div style="float: left;width:170px;font-size: 14px;margin-left: 20px;color: #73767a;">-->
                                        <!--2019.01.01&nbsp;22:00:00-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div style="width:280px; height:1px; background-color: #F0F2F5; margin: auto;">&nbsp;</div>-->
                            <!--</el-dropdown-item>-->
                        <!--</div>-->

                    <!--</el-dropdown-menu>-->
                <!--</el-dropdown>-->
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="hover" @command="handleCommand">
                    <span class="el-dropdown-link">
                        <i class="okay myIcon-yonghu" style="font-size: 25px"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>{{username}}</el-dropdown-item>
                        <!--<el-dropdown-item divided command="openSetPwdPanel">修改密码</el-dropdown-item>-->
                        <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>

        </div>
        <div>
            <!--修改密码的对话框-->
            <el-dialog title="修改密码" :visible.sync="changeDialog" width="30%" center :modal-append-to-body="pwdBox">
                <div>
                    <div style="width:240px; height:30%; margin:0 auto 0 auto;">
                        <table>
                            <tr>
                                <td>请输入手机号：</td>
                            </tr>

                            <tr>
                                <td colspan="2">
                                    <div style="width: 234px;">
                                        <el-input v-model="changePwd.mobile" placeholder="请输入手机号"></el-input>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td width="110px;">
                                    <div style="width: 140px;">
                                        <el-input v-model="changePwd.code" placeholder="请输入验证码"></el-input>
                                    </div>
                                </td>
                                <td width="110px" align="left">
                                    <el-button @click="getMyCode">获取验证码</el-button>
                                </td>
                            </tr>

                            <tr>
                                <td> <div style="width: 30px;">&nbsp;</div> </td>
                                <td></td>
                            </tr>

                            <tr>
                                <td> 新密码： </td>
                                <td> </td>
                            </tr>

                            <tr>
                                <td colspan="2">
                                    <div style="width: 234px;">
                                        <el-input v-model="changePwd.newPwd" type="password" placeholder="新密码"></el-input>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td><div style="width: 30px;">&nbsp;</div></td>
                            </tr>

                        </table>
                    </div>
                </div>

                <span slot="footer" class="dialog-footer">
                <el-button class="v-1-0-0-Button-Minor" @click="setPwdBack">取 消</el-button>
                <el-button class="v-1-0-0-Button" type="primary" @click="setMyPwd">确 定</el-button>
              </span>
            </el-dialog>
        </div>

    </div>
</template>
<script>
    import http from '../../common/http';
    import bus from '../../common/bus';
    import { Notification } from 'element-ui';
    export default {
        data() {
            return {
                changeDialog: false,
                //用于显示报警条数
                alarmNum:0,
                pwdBox: false,
                fullscreen: false,
                changePwd:{
                    mobile:"",
                    code:"",
                    newPwd:""
                },
                notificationArr:[],
                projectTitle:"",
                logoUrl:require("../../resource/img/logo5.png")
            }
        },
        created: function () {
            //获取报警数
            this.getAlarmCount();
            //实时更新报警数
            bus.$on("updateAlarmNum",() => {
                this.getAlarmCount();
            });
            //更新项目名称与logo
            if(sessionStorage.getItem('siteName')!=""){
                this.projectTitle="";
            }
            if(sessionStorage.getItem('logoFileUrl')!="" &&
                sessionStorage.getItem('logoFileUrl')!=null){
                this.logoUrl=sessionStorage.getItem('logoFileUrl');
            }
            console.log("llllll:"+this.logoUrl);
        },
        beforeDestroy () {
            bus.$off('updateAlarmNum');
        },
        computed: {
            username() {
                let username = JSON.parse(sessionStorage.getItem('user_info')).username;
                return username ? username : this.name;
            },
            onRoutes() {
                let activeRoute = this.$route.path.replace('/', '');
                let equipmentInspectionArr = ['/main', '/fireExtinguisher', '/fireHydrant', '/waterPump', '/fireWaterTank', '/EIWordOrder', '/EIDataOrder', '/EIDeviceUpdate'];
                if (equipmentInspectionArr.indexOf(this.$route.path) !== -1) {
                    activeRoute = 'equipmentInspection';
                }
                return activeRoute;
            }
        },
        methods: {

            getAlarmCount(){
                let url  = this.getAlarmCountUrl;
                let data = {};
                this.apiPost(url,data).then((res) => {
                    this.alarmNum = res.data;
                },(err)=>{

                });
            },
            //确定修改密码的方法
            setMyPwd(){
                alert("确定修改密码");
            },
            //修改密码界面点击返回
            setPwdBack(){
                this.changeDialog = false;
            },
            //获取验证码的方法
            getMyCode(){
                alert("获取验证码的方法");
            },
            //跳转到警报页面
            goAlarm(){
                let warn = localStorage.getItem('newWarn');
                if(warn == 'alarm'){
                    this.$router.push('/alarmLog');
                }
                else if(warn == 'fault'){
                    this.$router.push('/faultLog');
                }
                else if(warn == 'offline'){
                    this.$router.push('/faultLog');
                }
                else{
                    this.$router.push('/alarmLog');
                }
            },
            // 用户名下拉菜单选择事件
            handleCommand(command) {
                //退出登录
                if (command == 'loginout') {
                    bus.$emit("closeWebsocket");
                    sessionStorage.removeItem('user_info');
                    sessionStorage.removeItem('token');
                    sessionStorage.removeItem('fire_menu');
                    sessionStorage.removeItem('provinceId');
                    sessionStorage.removeItem('areaId');
                    sessionStorage.removeItem('buildType');
                    sessionStorage.removeItem('action');
                    sessionStorage.removeItem('siteCode');
                    sessionStorage.removeItem('siteName');
                    sessionStorage.removeItem('logoFileUrl');
                    Notification.closeAll();
                    this.$router.push(sessionStorage.getItem("loginUrl"));
                }
                else if (command == 'openSetPwdPanel') {
                    this.changeDialog = true;
                }
            },
            getSystemMenu(system_id) {
                bus.$emit('getSystemMenu', system_id);
            },
            // 全屏事件
            handleFullScreen() {
                let element = document.documentElement;
                if (this.fullscreen) {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                } else {
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.msRequestFullscreen) {
                        // IE11
                        element.msRequestFullscreen();
                    }
                }
                this.fullscreen = !this.fullscreen;
            }
        },
        mixins: [http]
    }

</script>
<style scoped>
    .header {
        position: relative;
        box-sizing: border-box;
        width: 100%;
        height: 64px;
        font-size: 22px;
        color: #fff;
        z-index: 5;
        background-color: #202B3D;
        min-width: 680px;
    }

    .collapse-btn {
        float: left;
        padding: 0 21px;
        cursor: pointer;
        line-height: 70px;
    }

    .header .logo {
        float: left;
        width: 260px;
        height: 60px;
    }

    .logo img {
        padding: 12px 24px;
        height: 40px;
    }

    .header-right {
        float: right;
        padding-right: 20px;
    }

    .header-user-con {
        display: flex;
        height: 64px;
        align-items: center;
    }

    .header-user-con .el-badge .el-badge__content.is-fixed {
        border: 0 !important;
    }

    .header-user-con-mini {
        display: none;
        height: 60px;
        align-items: center;
    }

    @media screen and (max-width: 800px) {
        .header-user-con {
            display: none;
        }
    }

    @media screen and (max-width: 800px) {
        .header-user-con-mini {
            display: flex;
        }
    }

    .btn-bell, .btn-fullscreen, .user-name {
        position: relative;
        margin-left: 30px;
        cursor: pointer;
    }

    /*.btn-bell-badge {*/
        /*position: absolute;*/
        /*right: 0;*/
        /*top: -2px;*/
        /*width: 8px;*/
        /*height: 8px;*/
        /*border-radius: 4px;*/
        /*background: #f56c6c;*/
        /*color: #fff;*/
    /*}*/

    .btn-bell .el-icon-bell {
        color: #fff;
    }

    .el-dropdown-link {
        color: #fff;
        cursor: pointer;
    }

    .el-dropdown-menu__item {
        text-align: center;
    }

    .app .el-notification.right{
        background-color: cyan;
    }

</style>
