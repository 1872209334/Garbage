<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">

        <div class="pageBox">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">用户管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">管理员</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:50px; margin:5px 16px 0 16px;">

                <div style="display: inline-block; height:40px;">
                    <!--编号输入框-->
                    <div style="width:160px;">
                        <el-input v-model="accountInput" placeholder="请输入账号"></el-input>
                    </div>
                </div>
                &nbsp;
                <div style="display: inline-block; height:40px;">
                    <el-button class="v-1-0-0-Button" @click="common.page = 1;getAdminList()">查询</el-button>
                    <el-button class="v-1-0-0-Button" style="margin-left: 5px;" @click="resetBtn">重置</el-button>
                </div>

                <div style="display: inline-block; height:40px;float: right;">
                    <el-button class="v-1-0-1-Button" icon="el-icon-delete" @click="batchDelete">批量删除</el-button>
                    <el-button class="v-1-0-0-Button" icon="el-icon-plus" @click="addAdmin()">新增管理员</el-button>
                </div>
            </div>
            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;border: 1px solid #F0F2F5;overflow: auto">
                <el-table ref="multipleTable" height="100%" :data="adminData" tooltip-effect="dark" style="width: 100%"
                          @selection-change="handleSelectionChange" :header-cell-style="tableStyle">
                    <el-table-column type="selection" width="55" :selectable="selectable"></el-table-column>
                    <el-table-column type="index" width="56" label="#"></el-table-column>
                    <el-table-column prop="account" label="登录账号" width="140"> </el-table-column>
                    <el-table-column prop="fire_role_name" label="权限"> </el-table-column>
                    <el-table-column prop="mobile" label="联系电话" width="150" show-overflow-tooltip> </el-table-column>
                    <el-table-column prop="created_at" label="创建时间" show-overflow-tooltip> </el-table-column>
                    <el-table-column prop="remark" label="备注" show-overflow-tooltip> </el-table-column>

                    <el-table-column fixed="right" label="操作" width="200" >
                        <template slot-scope="scope">
                            <el-button :disabled="scope.row.id == 1"
                                       @click.native.prevent="resetPwd(scope.row)"
                                       type="text"
                                       size="small">
                                <span style="color: #387EE8;">重置密码</span>
                            </el-button>
                            <el-button :disabled="scope.row.id == 1"
                                @click.native.prevent="editAdmin(scope.row)"
                                type="text"
                                size="small">
                                <span style="color: #387EE8;">编辑</span>
                            </el-button>
                            <el-button style="color: red" :disabled="scope.row.id == 1"
                                @click.native.prevent="deleteAdmin(scope.row)"
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
    export default {
        name: "ControlPersonnel",
        data(){
            return{
                tableStyle:{
                    'background-color':'#F6F7FB',
                    'font-weight':'400'
                },
                common:{
                    total:0,            //总数据量
                    pageSize:20,        //每页条数
                    page:1,              //页码
                    isPage:1,            //是否分页
                    allSizes:[10,20,30], //每页条数筛选
                    checkboxArr:[]       //多选框数组
                },
                accountInput:"",
                adminData:[],
                adminId:0,
            }
        },
        created:function () {
            this.getAdminList();
        },
        methods:{
            resetBtn(){
                this.accountInput = '';
                let url  = this.adminListUrl;
                let data = {
                    isPage:this.common.isPage,
                    page:this.common.page,
                    limit:this.common.pageSize,
                    order:'id asc',
                    name:this.accountInput
                };
                this.apiPost(url,data).then((res) => {
                    this.adminData = res.data.user_list;
                    this.common.total     = res.data.total;
                },(err)=>{
                });
            },
            getAdminList(){
                let url  = this.adminListUrl;
                let data = {
                    isPage:this.common.isPage,
                    page:this.common.page,
                    limit:this.common.pageSize,
                    order:'id asc',
                    name:this.accountInput
                };
                this.apiPost(url,data).then((res) => {
                    this.adminData = res.data.user_list;
                    this.common.total     = res.data.total;
                },(err)=>{
                });
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1;
                this.getAdminList();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getAdminList();
            },
            selectable(row){
                if(row.id == 1){
                    return false;
                }
                else{
                    return true;
                }
            },
            //新增管理员页面
            addAdmin(){
                this.$router.push(
                    {
                        name:'editManage',
                        params:{
                            type:0,
                            provinceId:"",
                            areaId:"",
                            remark:"",
                            roleId:""
                        }
                    }
                );
            },
            //重置管理员密码
            resetPwd(row){
                this.$confirm('确定重置此管理员的密码?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let url  = this.resetPwdUrl;
                    let data = {
                        id:row.id
                    };
                    this.apiPost(url,data).then((res) => {
                        if(res.status == 200){
                            this.$commonFn.showTip(res.message,1);
                        }
                    },(err)=>{

                    });
                }).catch(() => {

                });
            },
            //编辑管理员页面
            editAdmin(data){
                console.log(data);
                this.$router.push(
                    {
                        name:'editManage',
                        params:{
                            type:1,
                            id:data.id,
                            account:data.account,
                            mobile:data.mobile,
                            bid:data.bid,
                            provinceId:data.provinveId,
                            areaId:data.areaId,
                            remark:data.remark,
                            roleId:data.roleId
                        }
                    }
                );
            },
            deleteAdmin(data){
                this.adminId = data.id;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            deleteSuccess(){
                let url = this.deleteAdminUrl;
                let data = {
                    idStr:this.adminId
                };
                this.apiPost(url,data).then((res)=>{
                    if(res.status === 200){
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        this.getAdminList();
                    }else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                })
            },
            handleSelectionChange(val){
                this.common.checkboxArr = [];
                for (let i = 0;i < val.length;i++){
                    this.common.checkboxArr.push(val[i].id);
                }
            },
            //批量删除管理员
            batchDelete(){
                if(this.common.checkboxArr.length != 0){
                    if(this.common.checkboxArr.indexOf(1) > -1){
                        let index = this.common.checkboxArr.indexOf(1);
                        this.common.checkboxArr.splice(index,1);
                    }
                    let idStr = this.common.checkboxArr.join(",");
                    this.adminId = idStr;
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
