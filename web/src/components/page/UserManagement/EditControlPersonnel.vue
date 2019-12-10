<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto; ">

        <div class="pageBox">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">用户管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">管控人员</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span v-show="routeParam.type == 0" style="font-size: 14px;color:#606266;">新增管控人员</span>
                        <span v-show="routeParam.type != 0"style="font-size: 14px;color:#606266;">编辑管控人员</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:80%; margin-left: 16px; margin-top:1%;">

                <table>
                    <tr>
                        <td >
                            账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            号</td>
                        <td>：</td>
                        <td>
                            <div style="width:240px;">
                                <el-input v-model="routeParam.account" placeholder="请输入账号"></el-input>
                            </div>
                        </td>
                    </tr>
                    <tr v-show="routeParam.type == 0">
                        <td><br>
                            密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            码</td>
                        <td><br>：</td>
                        <td><br>
                            <div style="width:240px;">
                                <el-input v-model="userPassword" placeholder="请输入密码"></el-input>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><br>
                            手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            机</td>
                        <td><br>：</td>
                        <td><br>
                            <div style="width:240px;">
                                <el-input v-model="routeParam.mobile" placeholder="请输入手机" type="number" onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"></el-input>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td><br>
                            邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            箱</td>
                        <td><br>：</td>
                        <td><br>
                            <div style="width:240px;">
                                <el-input v-model="routeParam.email" placeholder="请输入邮箱，非必填"></el-input>
                            </div>
                        </td>
                    </tr>



                    <tr>
                        <td><br>
                            备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            注</td>
                        <td><br>：</td>
                        <td colspan="2"><br>
                            <div style="width:560px;">
                                <el-input
                                    type="textarea"
                                    :rows="3"
                                    placeholder="请输入备注信息..."
                                    v-model="routeParam.remark">
                                </el-input>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td></td><td></td>
                        <td><br>
                            <div style="float: left; height:40px; width:56px;">
                                <el-button class="v-1-0-0-Button" @click="saveUser">保存</el-button>
                            </div>
                            <div style="float: left; height:40px;width:56px; margin-left: 16px;">

                                <router-link to="/controlPersonnel">
                                    <el-button class="v-1-0-0-Button-Minor">返回</el-button>
                                </router-link>
                            </div>
                        </td>
                    </tr>
                </table>

            </div>

        </div>

    </div>
</template>

<script>
    import http from '../../../common/http';
    export default {
        name: "editControlPersonnel",
        data(){
            return{
                routeParam:{},
                userPassword:"",
            }
        },
        created:function () {
            this.routeParam = this.$route.params;

            console.log(this.routeParam)
        },
        methods:{
            saveUser(){
                if(this.routeParam.account == "" || this.routeParam.account == undefined){
                    this.$commonFn.showTip("账号不能为空",2);
                    return false;
                }
                if(this.routeParam.type == 0){
                    if(this.userPassword == "" || this.userPassword == undefined || this.userPassword.length < 3){
                        this.$commonFn.showTip("请正确输入密码格式",2);
                        return false;
                    }
                }

                if(this.routeParam.mobile == "" || this.routeParam.mobile == undefined){
                    this.$commonFn.showTip("手机号码不能为空",2);
                    return false;
                }
                let valid_mobile = /^1[3|4|5|7|8|9][0-9]\d{8}$/;
                if(!valid_mobile.test(this.routeParam.mobile)){
                    this.$commonFn.showTip("请正确填写手机格式",2);
                    return false;
                }
                if(this.routeParam.email){
                    let valid_mail =  /^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
                    if(!valid_mail.test(this.routeParam.email)){
                        this.$commonFn.showTip("请正确填写邮箱格式",2);
                        return false;
                    }
                }
                //添加管控人员
                if(this.routeParam.type == 0){
                    if(this.userPassword == ""){
                        this.$commonFn.showTip("密码不能为空",2);
                        return false;
                    }
                    let _loading = this.$commonFn.showLoading(2,'.main-box');
                    let url = this.addUserUrl;
                    let data = {
                        account:this.routeParam.account,
                        password:this.userPassword,
                        mobile:this.routeParam.mobile,
                        email:this.routeParam.email == undefined ? "":this.routeParam.email,
                        remark : this.routeParam.remark == undefined ? "":this.routeParam.remark,
                    };
                    this.apiPost(url, data).then((res) => {
                        _loading.close();
                        if(res.status == 200){
                            this.$commonFn.showTip("添加成功",1);
                            this.$router.push('/controlPersonnel');
                        }else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    }, (err) => {
                        _loading.close();
                        console.log(err);
                    });
                }
                //编辑管控人员
                else{
                    let _loading = this.$commonFn.showLoading(2,'.main-box');
                    let url = this.saveUserUrl;
                    let data = {
                        id:this.routeParam.id,
                        account:this.routeParam.account,
                        mobile:this.routeParam.mobile,
                        email:this.routeParam.email,
                        remark : this.routeParam.remark,
                    };
                    this.apiPost(url, data).then((res) => {
                        _loading.close();
                        if(res.status == 200){
                            this.$commonFn.showTip("修改成功",1);
                            this.$router.push('/controlPersonnel');
                        }else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    }, (err) => {
                        _loading.close();
                        console.log(err);
                    });
                }
            },
        },
        mixins: [http]
    }
</script>

<style>

    .v-1-0-0-Button{
        background-color: #387EE8;
        color: white;
    }
    .v-1-0-0-Button-Minor{
        background-color: #F0F8FF;
        color: #387EE8;
    }

</style>

