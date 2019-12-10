<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">

        <div class="pageBox" style="overflow-y: auto">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">用户管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">管理员</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span v-show="routeParam.type == 0" style="font-size: 14px;color:#606266;">新增管理员</span>
                        <span v-show="routeParam.type != 0"style="font-size: 14px;color:#606266;">编辑管理员</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="width:95%; height:80%; margin-left: 15px; margin-top:1%;">

                <table style="font-size: 14px;">
                    <tr>
                        <td>
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
                                <el-input v-model="passwordInput" placeholder="请输入密码"></el-input>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><br>
                            权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            限</td>
                        <td><br>：</td>
                        <td><br>
                            <div style="width:240px;">
                                <el-select v-model="routeParam.roleId" placeholder="请选择权限" @change="clickRole">
                                    <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
                                    </el-option>
                                </el-select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="letter-spacing: 0.2px;"><br>
                            区&nbsp;&nbsp;域&nbsp;&nbsp;选&nbsp;&nbsp;择</td>
                        <td><br>：</td>
                        <td><br>
                            <div style="width:240px; float: left;">
                                <!--省级下拉框-->
                                <el-select v-model="routeParam.provinceId" placeholder="省" @change="clickProvince">
                                    <el-option v-show="userInfo.roleType == 0" label="不限" :value="0">
                                    </el-option>
                                    <el-option v-show="routeParam.roleId != 1" v-for="item in province" :key="item.provinceId" :label="item.provinceName" :value="item.provinceId">
                                    </el-option>
                                </el-select>
                            </div>
<!--                            <div style="width:240px; float: left; margin-left: 10px;">-->
<!--                                <el-select v-model="routeParam.areaId" placeholder="市" @change="clickCity">-->
<!--                                    <el-option v-show="userInfo.roleType != 2" label="不限" :value="0" >-->
<!--                                    </el-option>-->
<!--                                    <el-option v-show="routeParam.roleId == 25" v-for="item in city" :key="item.areaId" :label="item.areaName" :value="item.areaId">-->
<!--                                    </el-option>-->
<!--                                </el-select>-->
<!--                            </div>-->
                        </td>
                        <td><br>

                        </td>
                    </tr>

                    <tr>
                        <td><br>
                            手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            机</td>
                        <td><br>：</td>
                        <td><br>
                            <div style="width:240px;">
                                <el-input v-model="routeParam.mobile" placeholder="请输入手机号" type="number" onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"></el-input>
                            </div>
                        </td>
                    </tr>
                    <tr v-if="routeParam.provinceId == 0 && routeParam.areaId == 0">
                        <td><br>
                            项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            目</td>
                        <td><br>：</td>
                        <td colspan="2"><br>
                            <div style="width: 480px;height:80px;overflow: auto;line-height: 80px;text-indent: 10px">
                               <span>拥有全部项目权限</span>
                            </div>
                        </td>
                    </tr>
                    <tr  v-if="routeParam.provinceId != 0 || routeParam.areaId != 0">
                        <td><br>
                            项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            目</td>
                        <td><br>：</td>
                        <td colspan="2"><br>
                            <div style="width: 480px;height:80px;overflow: auto; ">
                                <el-checkbox-group v-model="projectCheckList">
                                <div style="float:left; margin-right: 20px; height:33%" v-for="item in projectList">
                                    <el-checkbox v-if="routeParam.type == 1" :value="item.siteCord" :label="item.siteCord" :checked="routeParam.bid.split(',').indexOf(item.siteCord) > -1">
                                        {{item.siteLabel}}
                                    </el-checkbox>
                                    <el-checkbox v-if="routeParam.type == 0" :value="item.siteCord" :label="item.siteCord">
                                        {{item.siteLabel}}
                                    </el-checkbox>
                                </div>
                                </el-checkbox-group>
                                <span v-if="projectList.length == 0" style="position: relative;top: 30px;">该地区暂无项目</span>
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
                                    class="myTextArea"
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
                            <div style="float: left; height:40px; width:56px">
                                <el-button class="v-1-0-0-Button" @click="saveAdmin">保存</el-button>
                            </div>
                            <div style="float: left; height:40px;width:56px; margin-left: 16px;">
                                <router-link to="/manage">
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
    import bus from '../../../common/bus';
    export default {
        data() {
            return {
                routeParam:{},
                projectCheckList:[],
                passwordInput:"",
                provinceValue:0,
                cityValue:0,
                province:[],
                city:[],
                projectList:[],
                roleList:"",
                roleValue:"",
                bidList:[],
            };
        },
        created:function(){
            //获取省列表
            this.getProvince();
            console.log(this.$route);
            this.routeParam = this.$route.params;
            this.userInfo = JSON.parse(sessionStorage.getItem('user_info'));
            console.log(this.routeParam)
        },
        methods: {
            getProvince(){
                let _loading = this.$commonFn.showLoading(2,'.main-box');
                let data = {
                    roleId:this.$route.params.roleId
                };
                let url = this.getProvinceAndRoleUrl;
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    this.province = res.data.prinvince;
                    this.roleList = res.data.role;
                    //编辑页面触发更新省和地区
                    if(this.routeParam.type != 0){
                        this.updateArea();
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            //保存管理员
            saveAdmin(){
                console.log(this.routeParam.provinceId)
                console.log(this.routeParam.areaId)
                if(this.routeParam.account == "" || this.routeParam.account == undefined){
                    this.$commonFn.showTip("账号不能为空",2);
                    return false;
                }
                if(this.routeParam.type == 0){
                    if(this.passwordInput == "" || this.passwordInput == undefined || this.passwordInput.length < 3){
                        this.$commonFn.showTip("请正确输入密码格式",2);
                        return false;
                    }
                }

                if(this.routeParam.provinceId === "" || this.routeParam.areaId === ""){
                    this.$commonFn.showTip("请选择管理员区域",2);
                    return false;
                }
                if(this.routeParam.provinceId === "" || this.routeParam.provinceId == null){
                    this.$commonFn.showTip("请选择管理员区域",2);
                    return false;
                }
                if(this.routeParam.areaId === "" || this.routeParam.areaId == null){
                    this.$commonFn.showTip("请选择管理员区域",2);
                    return false;
                }
                let valid_mobile = /^1[3|4|5|7|8|9][0-9]\d{8}$/;
                if(!valid_mobile.test(this.routeParam.mobile)){
                    this.$commonFn.showTip("请正确填写手机格式",2);
                    return false;
                }
                if(this.routeParam.roleId == "" || this.routeParam.roleId == undefined){
                    this.$commonFn.showTip("请选择管理员权限",2);
                    return false;
                }
                let bid;
                if(this.routeParam.provinceId == 0 && this.routeParam.areaId == 0){
                    bid = 0;
                }
                else{
                    bid = this.projectCheckList.join(",");
                }
                //添加管理员
                if(this.routeParam.type == 0){
                    if(this.passwordInput == ""){
                        this.$commonFn.showTip("密码不能为空",2);
                        return false;
                    }
                    let _loading = this.$commonFn.showLoading(2,'.main-box');
                    let url = this.saveAdminUrl;
                    let data = {
                        id:0,
                        account:this.routeParam.account,
                        password:this.passwordInput,
                        password2:this.passwordInput,
                        mobile:this.routeParam.mobile,
                        fireRole: this.routeParam.roleId,
                        bid:bid,
                        remark : this.routeParam.remark == null?"":this.routeParam.remark,
                        provinceId : this.routeParam.provinceId,
                        areaId: this.routeParam.areaId,
                    };
                    console.log(data);
                    this.apiPost(url, data).then((res) => {
                        _loading.close();
                        if(res.status === 200){
                            this.$commonFn.showTip("添加成功",1);
                            this.$router.push('/manage');
                        }else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    }, (err) => {
                        _loading.close();
                        console.log(err);
                    });
                }
                //编辑管理员
                else{
                    let _loading = this.$commonFn.showLoading(2,'.main-box');
                    let url = this.saveAdminUrl;
                    let data = {
                        id:this.routeParam.id,
                        account:this.routeParam.account,
                        mobile:this.routeParam.mobile,
                        fireRole: this.routeParam.roleId,
                        bid:bid,
                        remark : this.routeParam.remark == null?"":this.routeParam.remark,
                        provinceId : this.routeParam.provinceId,
                        areaId: this.routeParam.areaId,
                    };
                    console.log(data);
                    this.apiPost(url, data).then((res) => {
                        _loading.close();
                        console.log(res);
                        if(res.status == 200){
                            this.$commonFn.showTip("修改成功",1);
                            this.$router.push('/manage');
                        }else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    }, (err) => {
                        _loading.close();
                        console.log(err);
                    });
                }
            },
            //触发更新地区
            updateArea(){
                let data = {
                    privinceId : this.routeParam.provinceId
                };
                let url = this.getCityAndSiteUrl;
                this.apiPost(url, data).then((res) => {
                    this.city = res.data.city;
                    let data = {
                        privinceId : this.routeParam.provinceId,
                        areaId: this.routeParam.areaId,
                    };
                    let url = this.getSiteByCity;
                    this.apiPost(url, data).then((res) => {
                        this.projectList = res.data.site;
                        for (let i in this.projectList){
                            if(this.projectList[i].siteName){
                                this.projectList[i].siteLabel = this.projectList[i].siteName;
                            }
                            else{
                                this.projectList[i].siteLabel = this.projectList[i].siteCord;
                            }
                        }
                    }, (err) => {

                    });
                }, (err) => {

                });

            },
            clickRole(){
                console.log("b");
                this.routeParam.provinceId = null;
                this.routeParam.areaId = null;
            },
            //点击省下拉框
            clickProvince(){
                if(this.routeParam.provinceId != 0){
                    //this.routeParam.roleId = null;
                    let data = {
                        privinceId : this.routeParam.provinceId
                    };
                    let url = this.getCityAndSiteUrl;
                    this.apiPost(url, data).then((res) => {
                        this.routeParam.areaId = 0;
                        this.city = res.data.city;
                        this.projectList = res.data.site;
                        for (let i in this.projectList){
                            if(this.projectList[i].siteName){
                                this.projectList[i].siteLabel = this.projectList[i].siteName;
                            }
                            else{
                                this.projectList[i].siteLabel = this.projectList[i].siteCord;
                            }
                        }
                    }, (err) => {

                    });
                }
                else{
                    this.routeParam.areaId = 0;
                    this.city = [];
                    this.routeParam.roleId = 1;
                }
            },

            //点击市下拉框
            clickCity(){
                let data = {
                    privinceId : this.routeParam.provinceId,
                    areaId: this.routeParam.areaId,
                };
                let url = this.getSiteByCity;
                this.apiPost(url, data).then((res) => {
                    this.projectList = res.data.site;
                    for (let i in this.projectList){
                        if(this.projectList[i].siteName){
                            this.projectList[i].siteLabel = this.projectList[i].siteName;
                        }
                        else{
                            this.projectList[i].siteLabel = this.projectList[i].siteCord;
                        }
                    }
                }, (err) => {

                });
            }
        },
        mixins: [http]
    };
</script>

<style scoped>

</style>


