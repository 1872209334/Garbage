<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">
        <div class="pageBox">
            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">用户管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="color: #111111;font-size: 14px;color:#606266;">权限管理</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div style=" height:50px; margin:5px 16px 0 16px;">
                <div style="display: inline-block; height:40px;">
                    <!--编号输入框-->
                    <div style="width:160px;">
                        <el-input v-model="roleInput" placeholder="请输入角色名称"></el-input>
                    </div>
                </div>
                &nbsp;
                <div style="display: inline-block; height:40px;">
                    <el-button class="v-1-0-0-Button" @click="common.page = 1;getRoleList()">查询</el-button>
                    <el-button class="v-1-0-0-Button" style="margin-left: 5px;" @click="resetBtn">重置</el-button>
                </div>
                <!--<div style="display: inline-block; height:40px;float: right;">-->
                    <!--<el-button class="v-1-0-0-Button" icon="el-icon-plus" @click="addRole()">新增权限</el-button>-->
                <!--</div>-->
            </div>

            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;border: 1px solid #F0F2F5;overflow: auto">
                <el-table ref="multipleTable" height="100%" :data="roleData" class="nowPageTable" tooltip-effect="dark" style="width: 100%"
                          @selection-change="handleSelectionChange" :header-cell-style="tableStyle">

                    <el-table-column type="index" width="56" label="#">
                    </el-table-column>
                    <el-table-column prop="roleName"label="权限名"width="200"> </el-table-column>
                    <el-table-column prop="roleDesc" label="描述" show-overflow-tooltip> </el-table-column>
                    <el-table-column fixed="right" label="操作" width="230" >
                        <template slot-scope="scope">
                            <el-button :disabled="scope.row.roleId == 1"
                                @click.native.prevent="editRole(scope.row)"
                                type="text"
                                size="small">
                                <span style="color: #387EE8;">编辑</span>
                            </el-button>
                            <el-button style="color: red" :disabled="true"
                                @click.native.prevent="deleteRole(scope.row)"
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
        name: "JurisdictionManagement",
        data(){
            return{
                tableStyle:{
                    'background-color':'#F6F7FB',
                    'font-weight':'400'
                },
                roleData:[],
                roleInput:"",
                roleId:0,
                allMenuId:[],
                common:{
                    total:0,            //总数据量
                    pageSize:20,        //每页条数
                    page:1,              //页码
                    isPage:1,            //是否分页
                    allSizes:[10,20,30], //每页条数筛选
                    checkboxArr:[]       //多选框数组
                },
            }
        },
        created:function () {
            this.getRoleList();
        },
        methods:{
            resetBtn(){
                this.roleInput = '';
                let url  = this.getRoleListUrl;
                let data = {
                    isPage:this.common.isPage,
                    page:this.common.page,
                    limit:this.common.pageSize,
                    name:this.roleInput
                };
                this.apiPost(url,data).then((res) => {
                    this.roleData = res.data.role_list;
                    this.common.total = res.data.total;
                },(err)=>{
                });
            },
            getRoleList(){
                let url  = this.getRoleListUrl;
                let data = {
                    isPage:this.common.isPage,
                    page:this.common.page,
                    limit:this.common.pageSize,
                    name:this.roleInput
                };
                this.apiPost(url,data).then((res) => {
                    this.roleData = res.data.role_list;
                    this.common.total = res.data.total;
                },(err)=>{
                });
            },
            addRole(){
                this.$router.push(
                    {
                        name:'editJurisdiction',
                        params:{
                            type:0,
                            roleId:0,
                        }
                    }
                );
            },
            deleteRole(data){
                this.roleId = data.roleId;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            //删除角色
            deleteSuccess(){
                let url = this.deleteRoleUrl;
                let data = {
                    idStr:this.roleId
                };
                this.apiPost(url,data).then((res)=>{
                    if(res.status === 200){
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        this.getRoleList();
                    }else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{

                })
            },
            //编辑角色
            editRole(data){
                let menuArr = data.menuIdList.split(",");
                let menuHeader = JSON.parse(sessionStorage.getItem("fire_menu"));
                this.checkChildMenu(menuHeader);
                let childMenuIdList  = [];
                for(let i = 0;i < menuArr.length;i++){
                    if(this.allMenuId.indexOf(parseInt(menuArr[i])) !== -1){
                        childMenuIdList.push(menuArr[i]);
                    }
                }
                childMenuIdList = childMenuIdList.join(",");
                this.$router.push(
                    {
                        name:'editJurisdiction',
                        params:{
                            type:1,
                            roleId:data.roleId,
                            roleType:data.role_type,
                            roleName:data.roleName,
                            roleDesc:data.roleDesc,
                            menuIdList:childMenuIdList
                        }
                    }
                );
            },
            selectable(row){
                if(row.roleId == 1){
                    return false;
                }
                else{
                    return true;
                }
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1;
                this.getRoleList();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getRoleList();
            },
            handleSelectionChange(){
                this.common.checkboxArr = [];
                for (let i = 0;i < val.length;i++){
                    this.common.checkboxArr.push(val[i].id);
                }
            },
            //递归子菜单
            checkChildMenu(menuArr){
                for(let i = 0;i < menuArr.length;i++){
                    if(menuArr[i].childMenus){
                        this.checkChildMenu(menuArr[i].childMenus);
                    }
                    else{
                        this.allMenuId.push(menuArr[i].menuId);
                    }
                }
            },
        },
        mixins: [http]
    }

</script>

<style scoped>

</style>
