<template id="deviceList">
    <div class="page_content" style="background-color: #F0F2F5;" :class="{'content-box':!is_show}">
        <div class="deviceMenu">
            <el-tree :data="treedata" @node-click="handleNodeClick" class="deviceMenu" node-key="id"
                     :expand-on-click-node="true" :highlight-current="true" accordion>
            <span class="custom-tree-node" slot-scope="{ node, data }" @mouseenter="mouseenter(node, data)"
                  @mouseleave="mouseleave(node, data)">
            <span>{{ node.label}}</span>
            <!--<span>-->
            <!--<i v-show="node.edit" style="color: #387EE8; font-size: 10px;font-style: normal;"-->
               <!--@click="() => projectDetail(node, data)">-->
            <!--编辑-->
            <!--</i>-->
            <!--</span>-->
            <span>
            <i v-show="node.del" style="color: #f14e4e; font-size: 10px;font-style: normal;"
               @click="() => deleteEfm(node, data)">
            删除
            </i>
            </span>
            </span>
            </el-tree>
        </div>
        <transition name="move" mode="out-in">
            <router-view></router-view>
        </transition>
    </div>

</template>
<script>
    import http from '../../../common/http';
    import bus from '../../../common/bus';
    export default {
        name: 'deviceList',
        data() {
            return {
                newProjectName: "",
                siteList: [],
                deviceList: [],
                //用于修改站点名对话框的布尔值
                centerDialogVisible: false,
                //项目名称
                siteId: 11,
                equipType: "badArc",
                twoTableShow: false,
                oneTableShow: true,
                functionList: [],
                treedata: [],
                total:0,
                page: 1,
                pageSize: 10,
                editNameBox:false,
                is_show: false,
                is_site: true,
                is_device: false,
                editSite: {
                    site_code: "",
                    site_name: "",
                    site_place: "",
                    site_build_id: "",
                    site_id: 0
                },
                deviceChecked: 0,
                deviceTitle:"",
                type: 0,
                inquire: "",
                devInfo:{},
                buildList: [],
                show_list: false,
                deviceData: [],
                equipList: [],
                multipleSelection: [],
                site: {},
                value: '',
                efmId: '',
                nameInput:""
            }
        },
        created: function () {
            this.getSiteList();
        },
        methods: {
            checkTerminal(){
                this.$router.push('/terminalInformation');
            },
            getSiteList() {
                let url = this.getSiteListUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                        this.siteList = res.data.getsiteList;
                        this.deviceList = res.data.getDeviceList;
                        this.buildList = res.data.sizeInfo.buildList;
                        console.log(this.siteList);
                        console.log(this.deviceList);
                        let site_arr = [];//站点数组
                        let data_list = [];//设备列表分类数组
                        for (let j in this.siteList) {
                            if(this.siteList[j].siteName){
                                this.siteList[j].label = this.siteList[j].siteName
                            }
                            else{
                                this.siteList[j].label = this.siteList[j].siteCord
                            }
                            data_list.push({
                                label:  this.siteList[j].label,
                                siteCode: this.siteList[j].siteCord,
                                sitePlace: this.siteList[j].sitePlace,
                                siteBuildId: this.siteList[j].siteBuildId,
                                siteId: this.siteList[j].siteId,
                                siteBuildName:this.siteList[j].buildName,
                                children: [],
                                is_del: false
                            });
                            for (let k in this.deviceList) {
                                if (this.deviceList[k].site_code == this.siteList[j].siteCord) {
                                    //备注
                                    if(this.deviceList[k].niName){
                                        this.deviceList[k].label = this.deviceList[k].niName;
                                    }
                                    //描述符
                                    else if (this.deviceList[k].specificator) {
                                        this.deviceList[k].label = this.deviceList[k].specificator;
                                    }
                                    //设备编号
                                    else {
                                        this.deviceList[k].label = this.deviceList[k].device_code;
                                    }
                                    this.deviceList[k].efmId = this.deviceList[k].id;
                                    this.deviceList[k].is_del = true;
                                    data_list[j].children.push(this.deviceList[k])
                                }
                            }
                        }
                        console.log(data_list);
                        this.treedata = data_list;
                        this.editSite.site_code = data_list[0].siteCode;
                        this.editSite.site_name = data_list[0].label;
                        this.editSite.site_place = data_list[0].sitePlace;
                        this.editSite.site_build_id = data_list[0].siteBuildId;
                        this.editSite.site_id = data_list[0].siteId;
                        console.log(this.editSite.site_id);
                    },
                    (err) => {
                    }
                );
            },
            //修改项目名
            editSiteInfo() {
                let url = this.editSiteInfoUrl;
                let data = {
                    siteNiName: this.editSite.site_name,
                    siteId: this.editSite.site_id,
                    siteRemark: this.editSite.site_place,
                    siteBuildId: this.editSite.site_build_id,
                };
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.getSiteList();
                        this.$commonFn.showTip("修改成功", 1);
                    }
                });
            },
            formatNumber: function (row, column) {
                return row.serialNumber = parseInt(row.line_code) + 1 + "-" + row.addr;
            },
            formatState:function(row, column){
                return row.is_alarm == 0 ? "正常":"报警";
            },
            mouseenter(node) {
                if (node.data.is_del) {
                    this.$set(node, 'del', true);
                }
                else {
                    this.$set(node, 'edit', true);
                }
            },
            mouseleave(node) {
                if (node.data.is_del) {
                    this.$set(node, 'del', false);
                }
                else {
                    this.$set(node, 'edit', false);
                }
            },
            editDeviceName(){
                let url = this.editDeviceNameUrl;
                let data = {
                    deviceId: this.deviceChecked,
                    niName:this.nameInput
                };
                this.apiPost(url, data).then((res) => {
                    if(res.status === 200){
                        this.getSiteList();
                        this.editNameBox = false;
                        this.$commonFn.showTip("修改成功", 1);

                    }
                });
            },
            //项目详情查看
            projectDetail(node, data) {
                console.log(data);
                this.is_site = true;
                this.is_device = false;
                this.editSite.site_code = data.siteCode;
                this.editSite.site_name = data.label;
                this.editSite.site_place = data.sitePlace;
                this.editSite.site_build_id = data.siteBuildId;
                this.editSite.site_id = data.siteId;
            },
            //切换终端类型
            changeEquipType(type) {
                if(type == "badArc"){
                    this.type = 0;
                    this.getArcEquip();
                }
                else if(type == 'restCurrent'){
                    this.type = 1;
                    this.getArcEquip();
                }
            },
            /*删除主机的方法*/
            deleteEfm(node, data) {
                /*对话框*/
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log(data.efmId)
                    let url = this.testDeleteItem;
                    let datas = {
                        efmid: data.efmId
                    };
                    this.apiPost(url, datas).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        //清除这一行的数据
                        this.getSiteList();
                        // const parent = node.parent;
                        // const children = parent.data.children || parent.data;
                        // const index = children.findIndex(d => d.id === data.id);
                        // children.splice(index, 1);
                    })
                }).catch(() => {

                });
            },
            // 修改table tr行的背景色
            tableRowStyle({row, rowIndex}) {
                return 'background-color: #F7F6Fd'
            },
            // 修改table header的背景色
            tableHeaderColor({row, column, rowIndex, columnIndex}) {
                if (rowIndex === 0) {
                    return 'background-color: #F7F6Fd;color: #B6B5C2;font-weight: 500;'
                }
            },
            handleNodeClick(data) {

                this.deviceData = data;
                //点击中控
                if (data.is_del) {
                    this.$router.push(
                        {
                            path:'/deviceDetail'
                        }
                    );
                    // this.deviceChecked = this.deviceData.id;
                    // if(this.deviceData.niName){
                    //     this.deviceTitle = this.deviceData.niName;
                    // }
                    // else if(this.deviceData.specificator){
                    //     this.deviceTitle = this.deviceData.specificator;
                    // }
                    // else if(this.deviceData.device_code){
                    //     this.deviceTitle = this.deviceData.device_code;
                    // }
                    // this.getArcEquip();
                    // this.getDevInfo();
                }
                else{
                    console.log(data)
                    let param = {
                        site_code:data.siteCode,
                        site_name:data.label,
                        site_place:data.sitePlace,
                        site_build_id:data.siteBuildId,
                        site_id:data.siteId,
                        site_build_name:data.siteBuildName
                    }
                    bus.$emit("changeSite",param);
                    this.$router.push(
                        {
                            name:'projectDetail'
                        }
                    );
                    this.editSite.site_code = data.siteCode;
                    this.editSite.site_name = data.label;
                    this.editSite.site_place = data.sitePlace;
                    this.editSite.site_build_id = data.siteBuildId;
                    this.editSite.site_id = data.siteId;
                }
            },
            deleteItem() {
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            deleteSuccess() {
                console.log("aa")
            },
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(function (row) {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            callbackFunction(result) {
                alert(result + "aaa");
            },
            handleSizeChange(val) {
                this.pageSize = val;
                this.currentPage = 1;
            },
            handleCurrentChange(val) {
                this.currentPage = val;
            },
            getArcEquip() {
                let url = this.getEquipListUrl;
                let data = {
                    deviceId: this.deviceChecked,
                    pagesize: this.pageSize,
                    currentPage: this.page,
                    type: this.type,
                    inquire: this.inquire
                };
                this.apiPost(url, data).then((res) => {
                    this.equipList = res.data.equipList;
                    this.total = res.data.equipListCount;
                });
            },
            getDevInfo(){
                let url = this.getEfmInfoUrl;
                let data = {
                    deviceId: this.deviceChecked,
                };
                this.apiPost(url, data).then((res) => {
                    this.devInfo = res.data;
                    console.log(res);
                });
            }
        },
        mixins: [http]
    }
</script>

<style scoped>
    table.gridtable {
        padding: 8px;
        font-family: verdana, arial, sans-serif;
        font-size: 11px;
        color: #333333;
        border-width: 1px;
        /* border-color: #666666;*/
        border-color: rgb(154, 200, 228);
        border-collapse: collapse;
    }

    table.gridtable th:nth-child(odd) {
        border-width: 1px;
        padding: 8px;
        height: 18px;
        border-style: solid;
        /*  border-color: #666666;*/
        /* border-color: rgb(154,200,228);*/
        border-color: rgb(216, 220, 228);
        background-color: #F6F7FB;
        /* background-color: #dadee9;*/
        text-align: center;
    }

    table.gridtable th:nth-child(even) {
        border-width: 1px;
        height: 18px;
        padding: 8px;
        border-style: solid;
        /*  border-color: #666666;*/
        /* border-color: rgb(154,200,228);*/
        border-color: rgb(216, 220, 228);
        background-color: white;
        /* background-color: #dadee9;*/
        text-align: left;
    }

    .nameTd {
        text-align: center;
        padding: 8px;
        border-style: solid;
        border-color: gray;
        background-color: #F6F7FB;
    }

    .priceTd {
        padding: 8px;
        border-style: solid;
        border-color: gray;
        background-color: white;
    }

    .login-box {
        position: absolute;
        left: 50%;
        top: 60%;
        /*width:298px;*/
        /*height:288px;*/
        margin: -146px 0 0 -163px;
        padding: 30px;
        border-radius: 5px;
        background: rgba(5, 5, 38, 0.2);
        border: 1px solid rgba(5, 5, 38, 0.3);
    }



    .el-table th {
        text-align: center;
    }

    .el-table tbody tr td:first-child {
        text-align: center;
    }

    .el-pagination {
        padding: 15px 5px;
    }

    .table-scroll-mod {
        position: relative;
        height: 65px;
        overflow: hidden;
    }



    .gridcontainer[data-v-cc10f246] {
        width: 0;
        background: rgb(16, 42, 93);
    }

    .deviceMenu {
        background: white;
        width: 185px;
        height: 100%;
        float: left;
    }
    .el-tree .el-tree-node{
        height:40px !important;
    }
    .el-tree .el-tree-node:hover {
        background: red !important;
    }

    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 16px;
        padding-right: 8px;
    }

    .mySpan {
        cursor: pointer;
        color: cornflowerblue;
    }

    hr {
        background-color: #F0F2F5;
        height: 1px;
        border: none;
    }

    .v-1-0-0-Button {
        background-color: #387EE8;
        color: white;
    }

    .v-1-0-0-Button-Minor {
        background-color: #F0F8FF;
        color: #387EE8;
    }
    .el-tree-node__content{
        padding-top: 10px !important;
        padding-bottom: 10px !important;
    }
</style>
