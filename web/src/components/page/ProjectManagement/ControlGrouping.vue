<template>
    <div style="width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">
        <div class="pageBox">
            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">项目管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">管控分组</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:50px; margin:5px 16px 0 16px;">
                <div style="display: inline-block; height:40px;width: 136px">
                    <el-select v-model="projectSelectVal" placeholder="项目">
                        <el-option label="不限" value="0"></el-option>
                        <el-option
                            v-for="item in projectSelect"
                            :key="item.siteId"
                            :label="item.siteLabel"
                            :value="item.siteId">
                        </el-option>
                    </el-select>
                </div>

                <!--<div style="display: inline-block; height:40px;">-->
                    <!--<el-select v-model="appUserVal" placeholder="管控人员">-->
                        <!--<el-option label="不限" value="0"></el-option>-->
                        <!--<el-option-->
                            <!--v-for="item in appUserList"-->
                            <!--:key="item.id"-->
                            <!--:label="item.account"-->
                            <!--:value="item.id">-->
                        <!--</el-option>-->
                    <!--</el-select>-->
                <!--</div>-->
                &nbsp;
                <div style="display: inline-block; height:40px; width: 65px;">
                    <el-button class="v-1-0-0-Button" @click="common.page = 1;getControlGroupingList()">查询</el-button>
                </div>

                <!--<div style="display: inline-block; height:40px;">-->
                <!--<el-button class="v-1-0-0-Button">高级搜索</el-button>-->
                <!--</div>-->

                <div style="float: right; height:40px;">
                    <!--<el-button class="v-1-0-1-Button" icon="el-icon-delete">删除</el-button>-->
                    <el-button class="v-1-0-0-Button" icon="el-icon-plus" @click="openCreatePanel">新建</el-button>
                </div>
            </div>
            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;border: 1px solid #F0F2F5;overflow: auto">
                <el-table ref="multipleTable" height="100%" :data="controlGroupingList" tooltip-effect="dark" style="width: 100%"
                          @selection-change="handleSelectionChange" :header-cell-style="tableStyle">


                    <el-table-column type="index" label="#" width="56">
                    </el-table-column>

                    <el-table-column prop="siteLabel" :formatter="formatSite" label="项目" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column prop="groupPlace" label="分组" show-overflow-tooltip>
                    </el-table-column>

                    <el-table-column prop="appUsers" label="管控人员" show-overflow-tooltip>
                    </el-table-column>

                    <el-table-column prop="mobiles" label="电话" show-overflow-tooltip>
                    </el-table-column>

                    <el-table-column prop="cnEquip" label="设备数" show-overflow-tooltip width="80">
                    </el-table-column>

                    <el-table-column fixed="right" label="操作">
                        <template slot-scope="scope">
                            <el-button @click.native.prevent="editGroup(scope.row)" type="text"
                                       size="small">
                                <span style="color: #387EE8;">编辑</span>
                            </el-button>
                            <el-button style="color: red"
                                       @click.native.prevent="deleteGroup(scope.row)"
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
                    layout="total , sizes, prev, pager, next, jumper"
                    :total="common.total">
                </el-pagination>
            </div>

        </div>

    </div>
</template>

<script>
    import http from '../../../common/http';
    export default {
        data() {
            return {
                tableStyle: {
                    'background-color': '#F6F7FB',
                    'font-weight': '400'
                },
                common: {
                    total: 0,            //总数据量
                    pageSize: 20,        //每页条数
                    page: 1,              //页码
                    isPage: 1,            //是否分页
                    allSizes: [10, 20, 30], //每页条数筛选
                    checkboxArr: []       //多选框数组
                },
                projectSelectVal:null,
                projectSelect: [],
                appUserVal:'',
                appUserList:[],
                controlGroupingList: [],
                groupDelId:0,
                //当前选中的分组成员数组
                nowControlPersonal: [],
                multipleSelection: [],
            };
        },

        created: function () {
            this.getControlGroupingList();
            this.getProjectSelect();
        },

        methods: {
            formatSite(row){
                if(row.siteName){
                    return row.siteLabel = row.siteName;
                }
                else{
                    return row.siteLabel = row.siteCode
                }
            },
            //获取管控分组列表
            getControlGroupingList() {
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getControlGroupUrl;
                let data = {
                    uid: this.appUserVal,
                    siteId: this.projectSelectVal == null?0:this.projectSelectVal,
                    currentPage: this.common.page,
                    pageSize: this.common.pageSize
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                        console.log(res);
                        this.common.total = res.data.totalNum;
                        this.controlGroupingList = res.data.items;
                    }
                    else {
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            //获取项目下拉框
            getProjectSelect() {
                let url = this.getAllGroupUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.projectSelect = res.data.getsiteList;
                        for(let i in this.projectSelect){
                            if(this.projectSelect[i].siteName){
                                this.projectSelect[i].siteLabel = this.projectSelect[i].siteName;
                            }
                            else{
                                this.projectSelect[i].siteLabel = this.projectSelect[i].siteCord;
                            }
                        }
                        this.appUserList = res.data.appUserList;
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {

                });
            },
            //编辑分组
            editGroup(row){
                this.$router.push(
                    {
                        name: 'editControlGrouping',
                        params:{
                            type:1,
                            groupId:row.id,
                            place:row.groupPlace,
                            siteId: row.siteId,
                            hid:row.hid
                        }
                    }
                );
            },
            //删除分组
            deleteGroup(row){
                this.groupDelId = row.id;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            deleteSuccess(){
                let url = this.deleteGroupUrl;
                let data = {
                    id:this.groupDelId
                };
                this.apiPost(url,data).then((res)=>{
                    if(res.status === 200){
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        this.getControlGroupingList();
                    }else{
                        this.$commonFn.showTip(res.message,2);
                    }
                },(err)=>{

                })
            },
            //编辑
            editRow(index, rows) {
                this.nowControlPersonal = rows[index].appUsers.split(",");
                //路由跳转 携带参数
                this.$router.push({path: 'editControlGrouping', query: {nowControlPersonal: this.nowControlPersonal}});
                //界面删除当前行
                /*rows.splice(index, 1);*/
            },
            //打开新建管控分组界面
            openCreatePanel() {
                this.$router.push(
                    {
                        name: 'addControlGrouping',
                        params:{
                            type:0,
                            groupId:0,
                            place:"",
                            siteId:""
                        }
                    }
                );
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            deleteRow(index, rows) {

            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.getControlGroupingList();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getControlGroupingList();
            }
        },
        mixins: [http]
    };
</script>

<style>
    .v-1-0-0-Button-Minor {
        background-color: #F0F8FF;
        color: #387EE8;
    }

</style>

