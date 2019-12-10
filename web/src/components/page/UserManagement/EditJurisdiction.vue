<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto; ">

        <div class="pageBox" style="overflow-x: hidden">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size:16px;color:#303313; font-weight: 500;">用户管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="color: #111111;font-size: 14px;color:#606266;">权限管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span v-show="routeParam.type == 0" style="font-size: 14px;color:#606266;">新增权限</span>
                        <span v-show="routeParam.type != 0" style="font-size: 14px;color:#606266;">编辑权限</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="padding:5px;margin: 0 16px auto 11px;">
                <table style="border-collapse: collapse; font-size: 15px; font-weight: 300; ">
                    <tr>
                        <th width="60px;" align="right">权限名：</th>
                        <th width="170px">
                            <div style="width:160px;">
                                <el-input v-model="routeParam.roleName" placeholder="请输入"></el-input>
                            </div>
                        </th>
                        <th width="60px" align="right">备注：</th>
                        <th width="170px" align="left">
                            <div style="width:160px;">
                                <el-input v-model="routeParam.roleDesc" placeholder="请输入"></el-input>
                            </div>
                        </th>
                    </tr>
                </table>
            </div>

            <div style="margin:0 -10px auto -10px; ; height:72%; ">

                <div style=" width:95.5%; height:78%; margin:1% auto auto 2.3%; overflow: auto; border: 1px solid #d8dce6;">
                    <el-tree
                        :data="treeProps.permissionList"
                        :show-checkbox="treeProps.showCheckbox"
                        :node-key="treeProps.nodeKey"
                        ref="tree"
                        accordion
                        highlight-current
                        :default-checked-keys="treeProps.roleMenu"
                        :props="treeProps.defaultProps">
                    </el-tree>
                </div>

                <div style="width:26%; height:40px; margin:2% auto auto 2.3%;">
                    <div style="width:56px; float:left; height:40px; ">
                        <el-button class="v-1-0-0-Button" @click="saveRole">保存</el-button>
                    </div>
                    <div style="width:56px; float:left;  height:40px; margin-left: 16px; ">
                        <router-link to="/jurisdictionManagement">
                            <el-button class="v-1-0-0-Button-Minor">返回</el-button>
                        </router-link>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import bus from '../../../common/bus';
    import http from '../../../common/http';
    export default {
        name: "editJurisdiction",
        data(){
            return{
                routeParam:{},
                treeProps:{
                    permissionList: [],
                    nodeKey:'menuId',
                    roleMenu:[],
                    showCheckbox:true,
                    defaultProps: {
                        children: 'childMenus',
                        label: 'menuName'
                    },
                    menuIdChecked:""
                }
            }
        },
        created: function(){
            this.routeParam = this.$route.params;
            this.getAllRoleList();
        },

        methods :{
            getAllRoleList(){
                let url = this.getAllRoleUrl;
                let data = {
                    isPage:0
                };
                this.apiPost(url,data).then((res)=>{
                    console.log(res);
                    if(res.status == 200){
                        if(this.routeParam.menuIdList){
                            this.treeProps.roleMenu = this.routeParam.menuIdList.split(',');
                            console.log(this.treeProps.roleMenu)
                        }
                        this.treeProps.permissionList = JSON.parse(sessionStorage.getItem('fire_menu'));
                        console.log(this.treeProps.permissionList)
                        //let data = res.data.Permissions_list;
                        //this.treeProps.permissionList = this.$commonFn.toTree(data);
                    }
                },(err)=>{

                })
            },
            //添加/编辑角色
            saveRole(){
                if(this.routeParam.roleName == "" || this.routeParam.roleName == undefined){
                    this.$commonFn.showTip("名称不能为空",2);
                    return false;
                }
                let menuArr = [];
                let allCheckedNode = this.$refs.tree.getCheckedNodes();
                let halfCheckedNode = this.$refs.tree.getHalfCheckedNodes();
                for(let i = 0;i < allCheckedNode.length;i++){
                    menuArr.push(allCheckedNode[i].menuId);
                }
                for(let i = 0;i < halfCheckedNode.length;i++){
                    menuArr.push(halfCheckedNode[i].menuId);
                }
                this.menuIdChecked = menuArr.join(",");
                let _loading = this.$commonFn.showLoading(2,'.main');
                let url = this.saveRoleUrl;
                let data = {
                    roleId:this.routeParam.roleId,
                    name:this.routeParam.roleName,
                    menuIdList:this.menuIdChecked,
                    roleDesc:this.routeParam.roleDesc == undefined?'':this.routeParam.roleDesc
                };
                this.apiPost(url,data).then((res) => {
                    _loading.close();
                    if(res.status === 200){
                        if(this.routeParam.type == 0){
                            this.$commonFn.showTip('添加成功',1);
                        }
                        else{
                            this.$commonFn.showTip('编辑成功',1);
                        }
                        this.$router.push('/jurisdictionManagement');
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err)=>{
                    _loading.close();
                });
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

