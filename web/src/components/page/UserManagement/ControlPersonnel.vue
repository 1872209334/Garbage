<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">
        <div class="pageBox">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">用户管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="color: #111111;font-size: 14px;color:#606266;">管控人员</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:50px; margin:5px 16px 0 16px;">

                <div style="display: inline-block; height:40px;">
                    <!--编号输入框-->
                    <div style="width:160px;">
                        <el-input v-model="filters.accountInput" placeholder="请输入账号"></el-input>
                    </div>
                </div>
                &nbsp;

                <div style="display: inline-block; height:40px;"><!-- width:65px;-->
                    <el-button class="v-1-0-0-Button" @click="getUserList">查询</el-button>
                    <el-button class="v-1-0-0-Button" style="margin-left: 5px;" @click="resetBtn">重置</el-button>
                </div>

                    <!--<div style="display: inline-block; height:40px;">-->
                        <!--<el-button class="v-1-0-0-Button">高级搜索</el-button>-->
                    <!--</div>-->

                <div style="display: inline-block; height:40px;float: right;">
                    <el-button class="v-1-0-1-Button" icon="el-icon-delete" @click="batchDelete">删除管控人员</el-button>
                    <el-button class="v-1-0-0-Button" icon="el-icon-plus" @click="addUser()">新增管控人员</el-button>
                </div>
            </div>
            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;border: 1px solid #F0F2F5;overflow: auto">
                <el-table ref="multipleTable" height="100%" :data="userData" tooltip-effect="dark" style="width: 100%"
                          @selection-change="handleSelectionChange" :header-cell-style="tableStyle">
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column type="index" width="56" label="#">
                    </el-table-column>
                    <el-table-column prop="account" label="账号" width="120"></el-table-column>
                    <el-table-column prop="created_at" label="注册时间" width="180" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="mobile" label="联系方式" width="120"></el-table-column>
                    <el-table-column prop="email" label="邮箱" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
                    <el-table-column fixed="right" label="操作" width="120">
                        <template slot-scope="scope">
                            <el-button
                                @click.native.prevent="editUser(scope.row)"
                                type="text"
                                size="small">
                                <span style="color: #387EE8;">编辑</span>
                            </el-button>
                            <el-button style="color: red"
                                       @click.native.prevent="deleteUser(scope.row)"
                                       type="text"
                                       size="small">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div style="float: right;margin-right: 13px;">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-size="common.pageSize"
                    :page-sizes="common.allSizes"
                    layout="total,sizes, prev, pager, next, jumper"
                    :total="common.total">
                </el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
    import http from '../../../common/http';
    import { MessageBox } from 'element-ui';
    export default {
        name: "ControlPersonnel",
        data() {
            return {
                tableStyle:{
                    'background-color':'#F6F7FB',
                    'font-weight':'400'
                },
                userData:[],
                userId:0,
                common:{
                    total:0,            //总数据量
                    pageSize:20,        //每页条数
                    page:1,              //页码
                    isPage:1,            //是否分页
                    allSizes:[10,20,30], //每页条数筛选
                    checkboxArr:[]       //多选框数组
                },
                filters:{
                    accountInput:"",
                    state: null,
                }
            }
        },
        created:function () {
            //获取管控人员列表
            this.getUserList();
        },
        methods:{
            addUser(){
                this.$router.push(
                    {
                        name:'editControlPersonnel',
                        params:{
                            type:0,
                        }
                    }
                );
            },
            auditUser(data){
                this.userId = data.id;
                MessageBox.confirm('是否通过该用户的申请？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let url = this.auditUserUrl;
                    let data = {
                        appUserId:this.userId,
                    };
                    this.apiPost(url,data).then((res) => {
                        if(res.status === 200){
                            this.getUserList();
                        }
                        else{
                            this.$commonFn.showTip(res.message,3);
                        }
                    },(err)=>{

                    });
                }).catch(() => {

                });
            },
            //弹出审核确认框
            showAudit(){

            },
            auditSuccess(){

            },
            deleteUser(data){
                this.userId = data.id;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            resetBtn(){
                this.filters.accountInput = '';
                let url  = this.userListUrl;
                let data = {
                    page:this.common.page,
                    limit:this.common.pageSize,
                    name:this.filters.accountInput,
                    statek:this.filters.state
                };
                if(this.filters.state == null){
                    data.statek = -1;
                }
                this.apiPost(url,data).then((res) => {
                    this.userData = res.data.appUserList;
                    this.common.total = res.data.total;
                },(err)=>{
                });
            },
            getUserList(){
                let url  = this.userListUrl;
                let data = {
                    page:this.common.page,
                    limit:this.common.pageSize,
                    name:this.filters.accountInput,
                    statek:this.filters.state
                };
                if(this.filters.state == null){
                    data.statek = -1;
                }
                this.apiPost(url,data).then((res) => {
                    this.userData = res.data.appUserList;
                    this.common.total = res.data.total;
                },(err)=>{
                });
            },
            deleteSuccess(){
                let url = this.deleteUserUrl;
                let data = {
                    idStr:this.userId
                };
                this.apiPost(url,data).then((res)=>{
                    if(res.status === 200){
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        this.getUserList();
                    }else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                })
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1;
                this.getUserList();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getUserList();
            },
            handleSelectionChange(val){
                this.common.checkboxArr = [];
                for (let i = 0;i < val.length;i++){
                    this.common.checkboxArr.push(val[i].id);
                }
            },
            editUser(data){
                this.$router.push(
                    {
                        name:'editControlPersonnel',
                        params:{
                            type:1,
                            id:data.id,
                            account:data.account,
                            mobile:data.mobile,
                            email:data.email,
                            remark:data.remark,
                        }
                    }
                );
            },
            //批量删除管控人员
            batchDelete(){
                if(this.common.checkboxArr.length != 0){
                    let idStr = this.common.checkboxArr.join(",");
                    this.userId = idStr;
                    this.$commonFn.showConfirm(this.deleteSuccess);
                }
                else{
                    this.$commonFn.showTip("请选择删除数据",2);
                }
            }
        },
        mixins: [http]
    }

</script>

<style scoped>

</style>
