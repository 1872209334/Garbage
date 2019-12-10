<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">

        <div class="pageBox">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">系统管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="color: #111111;font-size: 14px;color:#606266;">操作记录</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:50px; margin:5px 16px 0 16px;">
                <div style="display: inline-block;width: 140px;margin-right: 5px">
                    <el-input v-model="accountValue" placeholder="请输入账户"></el-input>
                </div>
                <div style="display: inline-block; height:40px;">
                    <el-date-picker
                        style="width: 150px !important;"
                        v-model="start_date"
                        type="date"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        placeholder="起始日期">
                    </el-date-picker>

                    &nbsp;<span style="font-size: 14px">至</span>&nbsp;

                    <el-date-picker
                        style="width: 150px !important;"
                        v-model="end_date"
                        type="date"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        placeholder="结束日期">
                    </el-date-picker>
                </div>


                <div style="display: inline-block; height:40px;margin-left: 5px;">
                    <el-button class="v-1-0-0-Button" @click="common.page=1;getLogList()">查询</el-button>
                </div>
            </div>

            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;
            border: 1px solid #F0F2F5;overflow: auto">
                <el-table ref="multipleTable" height="100%" :data="operaLog" :row-class-name="tableRowClassName" tooltip-effect="dark" style="width: 100%"
                          :header-cell-style="tableStyle" class="myTableClass">

                    <el-table-column prop="rowIndex" width="56" label="#" :formatter="formatIndex">
                    </el-table-column>

                    <el-table-column prop="username" label="管理员">
                    </el-table-column>

                    <el-table-column prop="createdAt" label="时间" show-overflow-tooltip>
                    </el-table-column>

                    <el-table-column prop="opration" label="内容" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column prop="ip" label="IP地址" show-overflow-tooltip>
                    </el-table-column>
                </el-table>

            </div>

            <div style="float: right;margin-right: 13px;">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-size="common.pageSize"
                    :page-sizes="common.allSizes"
                    :current-page="common.page"
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
        data() {
            return {
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
                //表格数据
                operaLog: [],
                start_date: "",
                end_date: "",
                accountValue:""
            };
        },
        created:function () {
            this.getLogList();
        },
        methods: {
            tableRowClassName(row){
                row.row.index = row.rowIndex;
            },
            formatIndex(row) {
                return row.rowIndex = this.common.pageSize * (this.common.page - 1)  + 1+ row.index;
            },
            deleteRow(index, rows) {
                rows.splice(index, 1);
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1;
                this.getLogList()
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getLogList();
            },
            getLogList(){
                let _loading = this.$commonFn.showLoading(2,'.main-box');
                let url = this.getLogListUrl;
                let data = {
                    isException:'',
                    username:this.accountValue,
                    opration:'',
                    controller:'',
                    method:'',
                    ip:'',
                    time1:this.start_date?this.start_date:"",
                    time2:this.end_date?this.end_date:"",
                    limit:this.common.pageSize,
                    page:this.common.page
                };
                this.apiPost(url,data).then((res)=>{
                    _loading.close();
                    if(res.status == 200){
                        this.operaLog = res.data.log_list;
                        this.common.total = res.data.total;
                    }else{
                        this.$commonFn.showTip(res.message,2)
                    }
                },(err)=>{
                    _loading.close();
                })
            }
        },
        mixins: [http]

    };
</script>

<style scoped>

</style>

