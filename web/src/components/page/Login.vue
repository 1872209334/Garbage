<template>
    <div class="login-wrap">
        <div class="logo_box">
<!--            <img src="../../resource/img/login_logo_1_30.png">-->
        </div>
        <div style="letter-spacing: 2px;color: white; font-weight: 300; font-size: 25px; height: 35px;text-align:center;margin-top: 10%"> <!-- margin:11% auto auto 66.5%-->
            物联大数据管理平台
        </div>
<!--        <div style="letter-spacing: 2px;color: white; font-weight: 300; font-size: 25px; height: 35px; margin:11%  ;">-->
<!--            物联大数据管理平台-->
<!--        </div>-->
        <!--登录面板-->
        <div class="login-box" v-if="loginPanelDisplay">
            <br>
            <el-form :model="loginForm" ref="loginForm" label-width="0px" class="login-Form">
                <el-form-item prop="username">
                    <el-input class="login-input" @focus="hideErrmsg" placeholder="请输入用户名" v-model="loginForm.username">
                        <i slot="prefix" class="login-icon el-input__icon el-icon-ali-wodedamaijihuo"></i>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input class="login-input" @focus="hideErrmsg" type="password" placeholder="请输入密码" @keyup.enter.native="submitForm('loginForm')"
                        v-model="loginForm.password">
                        <i slot="prefix" class="login-icon el-input__icon el-icon-ali-lock-fill"></i>
                    </el-input>
                </el-form-item>
                <template>
                    <div class="join_formitem">
                        <div class = 'captcha'>
                            <el-input class="login-input" style="width: 150px" @focus="hideErrmsg"  placeholder="请输入验证码" @keyup.enter.native="submitForm('loginForm')"
                                      v-model="picLyanzhengma">
                            </el-input>
                            <input type="button"  @click="createCode"  class="verification" v-model="checkCode"/>
                        </div>
                    </div>
                </template>
                <div style="width: 100%;height: 25px;">
                    <i v-if="error_msg" class="el-icon-warning" style="color:#fb506c;font-size: 15px"></i>
                    <span v-if="error_msg" style="letter-spacing:0.5px;color:#fb506c;font-size: 12px;display: inline-block;margin-left: 5px">{{error_info}}</span>
                </div>
                <!--登录按钮-->
                <div class="login-btn">
                    <el-button class="v-1-0-0-Button" @click="submitForm('loginForm')" style="margin-left: 0;text-align: center" >登&nbsp;录</el-button>
                </div>
                <div class="losePwd">
                    <span style="margin-right: 0" @click="loginPanelDisplay = !loginPanelDisplay">忘记密码</span>
                </div>

            </el-form>
        </div>

        <!--忘记密码面板-->
        <div class="forget-pwd-box" v-if="!loginPanelDisplay">
            <br>
            <el-form ref="form" :model="forgetPwdForm" label-width="120px">
                <el-form-item>
                    <label slot="label">手&nbsp;&nbsp;&nbsp;&nbsp;机&nbsp;&nbsp;&nbsp;&nbsp;号：</label>
                    <el-input onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))" v-model="forgetPwdForm.mobile" placeholder="请输入" type="number"></el-input>
                </el-form-item>
                <el-form-item>
                    <label slot="label">验&nbsp;&nbsp;&nbsp;&nbsp;证&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
                    <el-input style="width: 55%" v-model="forgetPwdForm.code" placeholder="验证码" type="number" onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"></el-input>
                    <span style="letter-spacing: 0.5px;font-size: 12px;color:#387ee8;margin-left: 5px;cursor: pointer" @click="getVerifyCode">{{getCode}}</span>
                </el-form-item>
                <el-form-item>
                    <label slot="label" style="letter-spacing: 0.8px;">设置新密码：</label>
                    <el-input v-model="forgetPwdForm.newPwd" placeholder="请输入" type="password"></el-input>
                </el-form-item>

            </el-form>
            <div style="width: 100%;height: 40px;margin-top: 30px;">
                <div style="width:120px;display: inline-block;float: left">
                    <el-button class="v-1-0-0-Button" @click="forgetPwd" style="width: 120px;height: 40px;font-size: 14px;letter-spacing: 1px">
                        保存
                    </el-button>
                </div>
                <div style="width:120px;display: inline-block;float: right">
                    <el-button class="v-1-0-0-Button-Minor" @click="loginPanelDisplay = !loginPanelDisplay,forgetPwdForm.mobile='',forgetPwdForm.code='',forgetPwdForm.newPwd='',getCode='获取验证码'" style="width: 120px;height: 40px;font-size: 14px;letter-spacing: 1px">
                        返回
                    </el-button>
                </div>
            </div>
        </div>
        <el-dialog
            title="请选择要进入项目"
            :visible.sync="centerDialogVisible"
            width="30%"
            center>
            <span></span>
            <div style="width: 480px;height:50px;overflow: auto; ">
                <el-radio-group v-model="projectRadioList">
                    <div style="float:left; margin-right: 20px; height:33%" v-for="(item, index) in projectList">
                        <el-radio :value="item.siteCord" :label="item.siteCord">
                            {{item.siteName}}
                        </el-radio>
                    </div>
                </el-radio-group>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="getIntoSys">进入</el-button>
              </span>
        </el-dialog>
    </div>
</template>

<script>
    import http from '../../common/http';
    export default {
        data: function () {
            return {
                error_msg:false,
                loginPanelDisplay:true,
                myPhoneNumber:"",
                myCode:"",
                newPwd:"",
                loginForm: {
                    username: '',
                    password: ''
                },
                forgetPwdForm:{
                    mobile:"",
                    code:"",
                    newPwd:""
                },
                getCode:"获取验证码",
                codeTime:"",
                checkCode : "",
                picLyanzhengma : "",
                error_info:'',
                centerDialogVisible: false,
                projectRadioList:[],
                projectList:[]
            }
        },
        methods: {
            hideErrmsg(){
                this.error_msg = false;
            },

            //登录
            submitForm() {
                if (this.loginForm.username && this.loginForm.password && this.picLyanzhengma) {
                    //let _loading = this.$commonFn.showLoading(1);
                    if(this.checkCode.toLowerCase() == this.picLyanzhengma.toLowerCase()){
                        let url = this.loginUrl;
                        let data = {
                            account: this.loginForm.username,
                            password: this.loginForm.password
                        };
                        this.apiPost(url,data).then((res) => {
                            this.loginSuccess(res);
                            //_loading.close();
                        },(err)=>{
                        });
                    }else{
                        this.error_msg=true;
                        this.error_info='验证码错误！';
                        this.createCode();
                    }
                } else {
                    //return false;
                    this.error_msg=true;
                    this.error_info='信息不完整！';
                    this.createCode();
                }
            },
            loginSuccess(res){
                if(res.status === 200){
                    sessionStorage.setItem("token",res.data.token);
                    sessionStorage.setItem("user_info",JSON.stringify(res.data.userinfo));
                    sessionStorage.setItem('buildType',JSON.stringify(res.data.buildList));
                    let m3 = res.data.thirdPermissions;
                    let actionArr = [];
                    for (let i in m3){
                        if(m3[i].menuUrl){
                            actionArr.push(m3[i].menuUrl);
                        }

                    }
                    sessionStorage.setItem("action",JSON.stringify(actionArr));
                    let menuList = res.data.fire_menu;
                    sessionStorage.setItem('fire_menu',JSON.stringify(menuList));
                    sessionStorage.setItem("loginUrl","/login");
                    //验证如果是区域用户，绑定多个用户
                    let siteList=res.data.siteList;
                    if(siteList && siteList.length>0){
                        this.projectList=siteList;
                        //默认选中第一个
                        this.projectRadioList=siteList[0].siteCord;
                        if(siteList.length==1){
                            this.getIntoSys();
                        }else{
                            this.centerDialogVisible=true;
                        }
                    }else{
                        this.$router.push({
                            name: 'main'
                        });
                    }
                }else{
                    this.error_msg = true;
                    this.error_info = '用户名或密码错误';
                    this.loginForm.password = "";
                    this.createCode();
                }
            },
            getIntoSys(){
                let siteCode=this.projectRadioList;
                let isPass=false;
                for(let i=0;i<this.projectList.length;i++){
                     if(siteCode==this.projectList[i].siteCord){
                         isPass=true;
                         sessionStorage.setItem("siteCode",this.projectList[i].siteCord==null?"":this.projectList[i].siteCord);
                         sessionStorage.setItem("siteName",this.projectList[i].siteName==null?"":this.projectList[i].siteName);
                         sessionStorage.setItem("logoFileUrl",this.projectList[i].logoFileUrl==null?"":this.projectList[i].logoFileUrl);
                         //console.log("站点编码："+this.projectList[i].siteCord+"；URL："+this.projectList[i].logoFileUrl);
                         break;
                     }
                }
                if(isPass){
                    this.$router.push({
                        name: 'main'
                    });
                }
            },
            getVerifyCode(){
                let url = this.getVerifyCodeUrl;
                let data = {
                    mobile: this.forgetPwdForm.mobile,
                };
                this.apiPost(url,data).then((res) => {
                    if(res.status === 200){
                        let second = 59;
                        this.getCode = second + "秒...";
                        this.codeTime = setInterval(() => {
                            this.getCode = second + "秒...";
                            second--;
                            if (second < 0) {
                                this.getCode = "获取验证码";
                                clearInterval(this.codeTime);
                            }
                        }, 1000);
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                });
            },
            forgetPwd(){
                let url = this.forgetPasswordUrl;
                let data = {
                    mobile: this.forgetPwdForm.mobile,
                    ncode:this.forgetPwdForm.code,
                    password:this.forgetPwdForm.newPwd
                };
                this.apiPost(url,data).then((res) => {
                    if(res.status === 200){
                        this.$commonFn.showTip("设置成功",1);
                        this.loginPanelDisplay = true;
                        this.forgetPwdForm.code = '';
                        this.forgetPwdForm.newPwd = '';
                        this.forgetPwdForm.code='';
                        this.getCode="获取验证码"
                    }
                    else{
                        this.forgetPwdForm.code = '';
                        this.forgetPwdForm.newPwd = '';
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                });
            },
            // 图片验证码
            createCode(){
                //先清空验证码的输入
                this.code = "";
                this.checkCode = "";
                this.picLyanzhengma = "";
                //验证码的长度
                var codeLength = 4;
                //随机数
                var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
                for(var i = 0; i < codeLength; i++) {
                    //取得随机数的索引（0~35）
                    var index = Math.floor(Math.random()*36);
                    //根据索引取得随机数加到code上
                    this.code += random[index];
                }
                //把code值赋给验证码
                this.checkCode = this.code;
            }
        },
        destroyed:function () {
            clearInterval(this.codeTime);
        },
        created:function(){
            this.createCode();
        },
        mixins: [http]
    }
</script>
<style scoped>
    .login-wrap {
        position: absolute;
        width: 100%;
        height: 100%;
        background-image: url("../../resource/img/login_bg_1_30.jpg");
        /*background-color: #533fff;*/
        background-position: center;
        background-repeat: no-repeat;
        -webkit-background-size: cover;
        -moz-background-size: cover;
        -o-background-size: cover;
        background-size: cover;
    }
    .login-box {
        height:265px;
        width:279px;
        /*margin-left: 66.5%;*/
        /*margin-top: 15px;*/
        margin: 15px auto;
        padding: 25px 22px;
        border-radius: 4px;
        background: white;
        box-shadow: 5px 5px 2px #387EE8;
    }
    .forget-pwd-box{
        height:245px;
        width:279px;
        margin-left: 66.5%;
        margin-top: 15px;
        padding: 25px 22px;
        border-radius: 4px;
        background: white;
        box-shadow: 5px 5px 2px #387EE8;
    }
    .forget-pwd-box .el-form-item__label{
        font-size: 17px !important;
    }
    .login-input i{
        line-height: 38px !important;
    }
    .login-icon{
        color:#cbcbcb !important;
    }
    .login-btn button {
        width: 100%;
        height: 35px;
        font-size: 15px;
        margin-left: 0 !important;
        text-align: center;
        margin-top: 15px;
    }
    .login-box .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
        margin-bottom: 15px !important;
    }

    .el-form-item--small .el-form-item__error {
        padding-top: 5px !important;
    }

    .login-input input{
        height:38px !important;
    }

    .losePwd{
        cursor: pointer;
        float: right;
        margin-top: 12px;
        color:#387ee8;
        font-size: 12px;
        font-weight: bold;
        letter-spacing: 0.5px;
    }
    .logo_box{
        position: absolute;
        top: 5%;
        left: 5%;
    }

    /*验证码*/
    .yanzhengma_input{
        font-family: 'Exo 2', sans-serif;
        border: 1px solid #fff;
        color: #fff;
        outline: none;
        border-radius: 12px;
        letter-spacing: 1px;
        font-size: 17px;
        font-weight: normal;
        background-color: rgba(82, 56, 76, 0.15);
        padding: 5px 0 5px 10px;
        margin-left: 30px;
        height: 30px;
        margin-top: 25px;
        border: 1px solid #e6e6e6;
    }
    .verification{
        border-radius: 3px;
        width:100px;
        letter-spacing:5px;
        float: right;
        height: 40px;
        border: none;
    }
    .captcha{
        height: 50px;
        text-align: justify;
    }
</style>


