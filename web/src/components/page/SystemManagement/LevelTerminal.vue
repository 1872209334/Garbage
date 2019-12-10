<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">

        <div class="pageBox">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">系统管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="color: #111111;font-size: 14px;color:#606266;">线上升级</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:50px; margin:5px 16px 0 16px;">

                <div style=" float: left; height:40px;width:6%;">
                    <router-link to="levelCenterControl">
                        <el-button class="v-1-0-0-Button-Minor">返回</el-button>
                    </router-link>
                </div>

                <div style="margin-top: 2px; float: left; ">
                    <span style="font-size: 20px;font-weight: 500;">中控：</span>
                    <span style="font-size: 20px;font-weight: 500;">{{centerControlName}}</span>
                </div>

                <div style="display: inline-block; height:40px;margin-left: 10px;">
                    <!--编号输入框-->
                    <div style="width:160px;">
                        <el-input v-model="roleInput" placeholder="请输入关键字"></el-input>
                    </div>
                </div>
                &nbsp;
                <div style="display: inline-block; height:40px;">
                    <el-button class="v-1-0-0-Button">搜索</el-button>
                </div>

                <div style="float:right; height:40px;">
                    <el-button class="v-1-0-0-Button">升级</el-button>
                </div>

            </div>

            <div style=" height:calc(100% - 152px);background-color:#F0F2F5; overflow:auto; margin: 0 16px auto 16px;
            border: 1px solid #F0F2F5; ">
                <el-table ref="multipleTable" :data="tableData3" tooltip-effect="dark" style="width: 100%"
                          @selection-change="handleSelectionChange" header-cell-style="background-color:#F6F7FB;font-weight:400">

                    <el-table-column
                        type="selection"
                        width="55">
                    </el-table-column>

                    <el-table-column type="index" width="56" label="#">
                    </el-table-column>

                    <el-table-column
                        label="项目"
                        width="120">
                        <template slot-scope="scope">{{ scope.row.date }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="name"
                        label="硬件版本"
                        width="120">
                    </el-table-column>
                    <el-table-column
                        prop="address"
                        label="软件版本"
                        show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column
                        prop="address"
                        label="发布时间"
                        show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column
                        prop="address"
                        label="产品序列号"
                        show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column
                        fixed="right"
                        label="操作"
                        width="200">
                        <template slot-scope="scope">
                            <el-button
                                @click.native.prevent="deleteRow(scope.$index, tableData4)"
                                type="text"
                                size="small">
                                <span style="color: #387EE8;">终端</span>
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>

            </div>

            <div style="float: right;margin-right: 13px;">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-size="pageSize"
                    layout="total,sizes, prev, pager, next, jumper"
                    :total="total">
                </el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
    export default {

        /*方法体*/
        methods: {
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            deleteRow(index, rows) {
                rows.splice(index, 1);
            },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
            }
        },

        data() {
            return {
                //中控名
                centerControlName:"有线中控01",
                //分页数据
                currentPage1: 5,
                //表格数据
                tableData3: [],
                multipleSelection: [],
                //日期选择器快捷选项
                pickerOptions2: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                value5: ''
            };
        },

    };
</script>

<style scoped>

    .v-1-0-0-Button{
        background-color: #387EE8;
        color: white;
    }

</style>


