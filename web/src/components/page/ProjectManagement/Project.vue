<template id="project">
    <div class="page_content" style="background-color: #F0F2F5;">
        <div class="deviceMenu">
            <el-tree
                :props="treeProps"
                :load="loadTree"
                :highlight-current="treeProps.highlight"
                accordion
                @node-click="projectNodeClick"
                lazy>
                <span class="custom-tree-node" slot-scope="{ node, data }" @mouseenter="showBtn(node, data)"
                      @mouseleave="hideBtn(node, data)">
                    <span style="letter-spacing: 0.5px;">{{data.treeLabel}}</span>
                    <i v-show="node.edit" style="color: #387EE8; font-size: 10px;font-style: normal;"
                       @click="() => editProjectName(node, data)">编辑</i>
                      <i v-show="node.edit" v-if="actionList.indexOf('delSite')>-1"
                         style="color: #f14e4e; font-size: 10px;font-style: normal;"
                         @click="() => delProject(node, data)">删除</i>
                    <i v-show="node.del" v-if="actionList.indexOf('deleteEfm')>-1"
                       style="color: #f14e4e; font-size: 10px;font-style: normal;" @click="() => delDevice(node, data)">删除</i>
                </span>
            </el-tree>
        </div>
        <el-dialog title="编辑项目" :visible.sync="editProjectForm" width="50%">
            <el-form :model="projectForm" ref="form">
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">项目编号</label>
                    <span style="margin-left: 5px">{{projectForm.siteCord}}</span>
                </el-form-item>
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">项目名称</label>
                    <el-input style="width: 60%;min-width: 240px" maxLength="10"
                              v-model="projectForm.siteName"></el-input>
                </el-form-item>
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">备注</label>
                    <el-input style="width: 60%;min-width: 240px" v-model="projectForm.sitePlace"></el-input>
                </el-form-item>
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">经度</label>
                    <el-input style="width: 60%;min-width: 240px" v-model="projectForm.longitute"></el-input>
                </el-form-item>
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">维度</label>
                    <el-input style="width: 60%;min-width: 240px" v-model="projectForm.latitude"></el-input>
                </el-form-item>
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">地图比例级别</label>
                    <el-input style="width: 60%;min-width: 240px" v-model="projectForm.mapNum" @keyup.native="regularMapNumInt"></el-input>
                    <span style="color:red;">(如:19=20米,18=50米...)</span>
                </el-form-item>
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">建筑类型</label>
                    <el-select style="width: 60%;min-width: 240px" v-model="projectForm.siteBuildId"
                               placeholder="请选择建筑类型">
                        <el-option v-for="item in buildTypeList" :key="item.id" :label="item.buildName"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item :label-width="projectForm.formLabelWidth">
                    <label slot="label" style="letter-spacing: 2px">项目LOGO</label>
                    <el-upload
                        :action="upload_url"
                        accept="image/jpeg,image/png,image/jpg"
                        :on-remove="handleRemove"
                        :file-list="fileList"
                        :auto-upload="isAuto"
                        :headers="headers"
                        :on-success="succ"
                        :on-error="err"
                        list-type="picture">
                        <el-button size="small" class="v-1-0-0-Button">点击选取</el-button>
                        <div slot="tip" class="el-upload__tip">LOGO只能上传jpeg/jpg/png格式，且不超过2M。</div>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editProjectForm = false">取 消</el-button>
                <el-button type="primary" @click="editProjectInfo">确 定</el-button>
            </div>
        </el-dialog>
        <transition name="move" mode="out-in" :key="$route.fullPath">
            <router-view></router-view>
        </transition>
    </div>

</template>
<script>
    import http from '../../../common/http';
    import bus from '../../../common/bus';

    export default {
        name: 'project',
        data() {
            return {
                siteList: [],
                buildList: [],
                treeProps: {
                    label: 'treeLabel',
                    isLeaf: 'leaf',
                    highlight: true,
                    node: {},
                    resolve: {},
                    isFirst: true,
                },
                editProjectForm: false,
                projectForm: {
                    siteId: 0,
                    siteCord: "",
                    siteName: "",
                    sitePlace: "",
                    siteBuildId: "",
                    longitute: "",
                    latitude: "",
                    mapNum: "",
                    formLabelWidth: '120px'
                },
                buildTypeList: [],
                actionList: [],
                upload_url:"",
                isAuto:true,
                fileList:[],
                headers: {
                    Authorization: 'Bearer '+ sessionStorage.getItem('token')
                }
            }
        },
        created: function () {
            this.buildTypeList = JSON.parse(sessionStorage.getItem('buildType'));
            this.actionList = JSON.parse(sessionStorage.getItem('action'));
            bus.$on('initTree', () => {
                let url = this.getSiteListUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                        this.siteList = res.data.getsiteList;
                        for (let i = 0; i < this.siteList.length; i++) {
                            if (this.siteList[i].siteName) {
                                this.siteList[i].treeLabel = this.siteList[i].siteName;
                            }
                            else {
                                this.siteList[i].treeLabel = this.siteList[i].siteCord;
                            }
                            console.log(this.siteList[i].treeLabel.length)
                            this.siteList[i].isDevice = false;
                        }
                        this.treeProps.node.childNodes = [];
                        this.treeProps.resolve(this.siteList);
                    },
                    (err) => {

                    });
            })
        },
        computed: {
        },
        beforeDestroy() {
            bus.$off('initTree');
        },
        methods: {
            handleRemove(file, fileList) {
                if(this.projectForm.siteId){
                    let url = this.delLogo;
                    let data = {
                        siteId:this.projectForm.siteId
                    };
                    this.apiPost(url,data).then((res) => {
                        if(res.status === 200){
                            this.$commonFn.showTip("删除成功",1);
                        }
                        else{
                            this.$commonFn.showTip(res.message, 3);
                        }
                    },(err)=>{
                    });
                }
            },
            err:function(res, file, fileList){
                this.$commonFn.showTip("上传Logo失败", 3);
            },
            succ:function(response, file, fileList){
                this.fileList=[];
                this.fileList.push(file);
                this.init();
            },
            init:function(){//刷新站点列表
                let url = this.getSiteListUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    console.log(res);
                    this.siteList = res.data.getsiteList;
                    for (let i = 0; i < this.siteList.length; i++) {
                        if (this.siteList[i].siteName) {
                            this.siteList[i].treeLabel = this.siteList[i].siteName;
                        }
                        else {
                            this.siteList[i].treeLabel = this.siteList[i].siteCord;
                        }
                        this.siteList[i].isDevice = false;
                    }
                    this.treeProps.node.childNodes = [];
                    this.treeProps.resolve(this.siteList);
                },
                (err) => {

                });
            },
            regularMapNumInt:function(){
                this.projectForm.mapNum=this.projectForm.mapNum.replace(/[^\d]/g,'');
            },
            loadTree(node, resolve) {
                //判断初次生成保存参数
                if (this.treeProps.isFirst) {
                    this.treeProps.node = node;
                    this.treeProps.resolve = resolve;
                    this.treeProps.isFirst = false;
                }
                let treeNode = node;
                if (treeNode.level === 0) {
                    //this.siteList = this.getProjectList();
                    let url = this.getSiteListUrl;
                    let data = {};
                    this.apiPost(url, data).then((res) => {
                            if (res.data) {
                                this.siteList = res.data.getsiteList;
                                for (let i = 0; i < this.siteList.length; i++) {
                                    //若存在项目备注则显示，不存在则显示项目编号
                                    if (this.siteList[i].siteName) {
                                        this.siteList[i].treeLabel = this.siteList[i].siteName;
                                    }
                                    else {
                                        this.siteList[i].treeLabel = this.siteList[i].siteCord;
                                    }
                                    this.siteList[i].isDevice = false;
                                }

                            }
                            else {
                                console.log(this.siteList);
                                this.siteList = [];
                            }
                            return resolve(this.siteList);
                        },
                        (err) => {
                        });
                }
                else if (treeNode.level === 1) {
                    let url = this.getDeviceBySiteUrl;
                    let data = {
                        siteId: treeNode.data.siteId
                    };
                    this.apiPost(url, data).then((res) => {
                            console.log(res);
                            for (let i = 0; i < res.data.length; i++) {
                                if (res.data[i].niName) {
                                    res.data[i].treeLabel = res.data[i].niName;
                                }
                                else if (res.data[i].specificator) {
                                    res.data[i].treeLabel = res.data[i].specificator;
                                }
                                else {
                                    res.data[i].treeLabel = parseInt(res.data[i].device_code, 16);
                                }
                                res.data[i].leaf = true;
                                res.data[i].isDevice = true;
                            }
                            return resolve(res.data);
                        },
                        (err) => {

                        });
                }
            },
            showBtn(node) {
                if (node.data.isDevice) {
                    this.$set(node, 'del', true);
                }
                else {
                    this.$set(node, 'edit', true);
                }
            },
            hideBtn(node) {
                if (node.data.isDevice) {
                    this.$set(node, 'del', false);
                }
                else {
                    this.$set(node, 'edit', false);
                }
            },
            //弹出修改页面
            editProjectName(node, data) {
                this.projectForm.siteId = data.siteId;
                this.projectForm.siteCord = data.siteCord;
                this.projectForm.siteName = data.siteName;
                this.projectForm.sitePlace = data.sitePlace;
                this.projectForm.siteBuildId = data.siteBuildId;
                this.projectForm.longitute = data.longitute;
                this.projectForm.latitude = data.latitude;
                this.projectForm.mapNum = data.mapNum;
                this.editProjectForm = true;
                //初始文件列表
                if(data.logoFileName!='' && data.logoFileName!=null
                    && data.logoFileUrl!='' && data.logoFileUrl!=null){
                    let files={
                        'name':data.logoFileName,
                        'url':data.logoFileUrl
                    }
                    this.fileList=[];
                    this.fileList.push(files);
                }
                //初始上传路径
                let url=this.uploadLogo
                this.upload_url=url+"?siteId="+data.siteId;
            },
            //删除项目
            delProject(node, nodeData) {
                this.$confirm('此操作将删除该项目以及所属设备, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let url = this.deleteProjectUrl;
                    let data = {
                        siteCode: nodeData.siteCord
                    };
                    this.apiPost(url, data).then((res) => {
                        if (res.status === 200) {
                            let url = this.getSiteListUrl;
                            let data = {};
                            this.apiPost(url, data).then((res) => {
                                    if (res.data) {
                                        this.siteList = res.data.getsiteList;
                                        for (let i = 0; i < this.siteList.length; i++) {
                                            if (this.siteList[i].siteName) {
                                                this.siteList[i].treeLabel = this.siteList[i].siteName;
                                            }
                                            else {
                                                this.siteList[i].treeLabel = this.siteList[i].siteCord;
                                            }
                                            this.siteList[i].isDevice = false;
                                        }
                                        this.$commonFn.showTip("删除成功", 1);
                                        this.treeProps.node.childNodes = [];
                                        this.treeProps.resolve(this.siteList);
                                        this.$router.push(
                                            {
                                                name: 'projectDetail',
                                            }
                                        );
                                    }
                                    else {
                                        this.siteList = [];
                                        this.treeProps.resolve(this.siteList);
                                    }
                                },
                                (err) => {

                                });
                        }
                        else {
                            this.$commonFn.showTip(res.data.message, 3);
                        }
                    })
                }).catch(() => {

                });
            },
            //修改项目信息
            editProjectInfo() {
                let url = this.editSiteInfoUrl;
                if (!this.projectForm.siteName) {
                    this.$commonFn.showTip("项目名不可为空", 2);
                    return false
                }
                if (!this.projectForm.longitute) {
                    this.$commonFn.showTip("经度不可为空", 2);
                    return false
                }
                if (!this.projectForm.latitude) {
                    this.$commonFn.showTip("维度不可为空", 2);
                    return false
                }
                if (!this.projectForm.siteBuildId) {
                    this.$commonFn.showTip("建筑类型不可为空", 2);
                    return false
                }
                if (!this.projectForm.mapNum) {
                    this.$commonFn.showTip("地图比例级别不可为空", 2);
                    return false
                }

                let data = {
                    'siteId':this.projectForm.siteId,
                    'siteNiName':this.projectForm.siteName,
                    'siteRemark':this.projectForm.sitePlace,
                    'siteBuildId':parseInt(this.projectForm.siteBuildId),
                    'longitute':this.projectForm.longitute,
                    'latitude':this.projectForm.latitude,
                    'mapNum':parseInt(this.projectForm.mapNum)
                };
                this.apiPost(url,data).then((res) => {
                    if (res.status === 200) {
                        let url = this.getSiteListUrl;
                        let data = {};
                        this.apiPost(url, data).then((res) => {
                                console.log(res);
                                this.siteList = res.data.getsiteList;
                                for (let i = 0; i < this.siteList.length; i++) {
                                    if (this.siteList[i].siteName) {
                                        this.siteList[i].treeLabel = this.siteList[i].siteName;
                                    }
                                    else {
                                        this.siteList[i].treeLabel = this.siteList[i].siteCord;
                                    }
                                    this.siteList[i].isDevice = false;
                                }
                                this.$commonFn.showTip("编辑成功", 1);
                                this.treeProps.node.childNodes = [];
                                this.treeProps.resolve(this.siteList);
                        },
                        (err) => {

                        });
                    }else {
                        this.$commonFn.showTip(res.message, 3);
                    }
                    this.editProjectForm = false;
                });
            },
            //删除中控
            delDevice(node, nodeData) {
                console.log(nodeData);
                this.$confirm('此操作将删除该中控设备, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (nodeData.niName != "无线终端") {
                        let url = this.deleteEfmUrl;
                        let data = {
                            efmid: nodeData.id
                        };
                        this.apiPost(url, data).then((res) => {
                            if (res.status === 200) {
                                let url = this.getSiteListUrl;
                                let data = {};
                                this.apiPost(url, data).then((res) => {
                                        this.siteList = res.data.getsiteList;
                                        for (let i = 0; i < this.siteList.length; i++) {
                                            if (this.siteList[i].siteName) {
                                                this.siteList[i].treeLabel = this.siteList[i].siteName;
                                            }
                                            else {
                                                this.siteList[i].treeLabel = this.siteList[i].siteCord;
                                            }
                                            this.siteList[i].isDevice = false;
                                        }
                                        this.$commonFn.showTip("删除成功", 1);
                                        this.treeProps.node.childNodes = [];
                                        this.treeProps.resolve(this.siteList);
                                        this.$router.push(
                                            {
                                                name: 'projectDetail',
                                            }
                                        );
                                    },
                                    (err) => {

                                    });
                            }
                            else {
                                this.$commonFn.showTip(res.message, 3);
                            }
                        })
                    }
                    else {
                        let site_code = localStorage.getItem("siteCode");
                        let url = this.deleteMqttDeviceUrl;
                        let data = {
                            siteCode: site_code
                        };
                        this.apiPost(url, data).then((res) => {
                            if (res.status === 200) {
                                let url = this.getSiteListUrl;
                                let data = {};
                                this.apiPost(url, data).then((res) => {
                                        this.siteList = res.data.getsiteList;
                                        for (let i = 0; i < this.siteList.length; i++) {
                                            if (this.siteList[i].siteName) {
                                                this.siteList[i].treeLabel = this.siteList[i].siteName;
                                            }
                                            else {
                                                this.siteList[i].treeLabel = this.siteList[i].siteCord;
                                            }
                                            this.siteList[i].isDevice = false;
                                        }
                                        this.$commonFn.showTip("删除成功", 1);
                                        this.treeProps.node.childNodes = [];
                                        this.treeProps.resolve(this.siteList);
                                        this.$router.push(
                                            {
                                                name: 'projectDetail',
                                            }
                                        );
                                    },
                                    (err) => {

                                    });
                            }
                            else {
                                this.$commonFn.showTip(res.message, 3);
                            }
                        })
                    }
                }).catch(() => {

                });
            },
            //点击树节点
            projectNodeClick(data) {
                if (data.isDevice == true) {
                    if (data.treeLabel == "无线终端") {
                        let site_id = localStorage.getItem("siteId");
                        console.log(site_id);
                        bus.$emit('mqttTerminalClick',{
                            siteId:site_id 
                        });
                        this.$router.push(
                            {
                                name: 'mqttDeviceList',
                                params: {
                                    siteId: site_id
                                }
                            }
                        );
                    }
                    else {
                        console.log(data);
                        bus.$emit('efmClickEfmClick', {
                            id: data.id,
                            efmName: data.treeLabel
                        });
                        this.$router.push(
                            {
                                name: 'deviceDetail',
                                params: {
                                    id: data.id,
                                    efmName: data.treeLabel
                                }
                            }
                        );
                    }

                }
                else {
                    console.log(data);
                    localStorage.setItem('siteId', data.siteId);
                    localStorage.setItem('siteCode', data.siteCord);
                }

            },
        },
        mixins: [http]
    }
</script>

<style scoped>
    .disabled .el-upload--picture-card {
        display: none;
    }
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
        overflow-y: auto !important;
        background: white;
        width: 185px;
        height: 100%;
        float: left;
    }

    .el-tree .el-tree-node {
        height: 40px !important;
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

    .el-tree-node__content {
        padding-top: 10px !important;
        padding-bottom: 10px !important;
    }

    .upload-demo .el-upload-list {
        height: 110px;
        /* width: 400px; */
        overflow-y: auto;
        border: 1px solid #f0f0f0;
        border-radius: 5px;
    }
</style>
