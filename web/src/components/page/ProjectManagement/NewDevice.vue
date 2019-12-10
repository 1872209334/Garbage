<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto; ">

        <div class="pageBox" style="overflow: auto">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <!--<el-breadcrumb-item>-->
                        <!--<span style="font-size: 16px;color:#303313;font-weight: 500;">项目管理</span>-->
                    <!--</el-breadcrumb-item>-->
                    <!--<el-breadcrumb-item>-->
                        <!--<span style="font-size: 14px;color:#606266;">管控分组</span>-->
                    <!--</el-breadcrumb-item>-->
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">新增终端</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:calc(100% - 53px); margin:16px 16px 0 17px">
                <el-form label-position="left" ref="form" label-width="160px">
                    <!--<el-form-item>-->
                        <!--<label slot="label" style="height:64px;display: inline-block;line-height: 64px;color:#000">管&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;控&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;员&nbsp;：</label>-->
                        <!--<div style="width: 440px;height:64px;overflow: auto; border: 1px solid #d8dce6; border-radius:4px;">-->
                            <!--<div style="display:inline-block;margin-left: 6px;"-->
                                 <!--v-for="item in appUserCheckedList">-->
                                <!--<el-tag type="success" size="small">{{item.account}}</el-tag>-->
                            <!--</div>-->

                            <!--<div style="display:inline-block;margin-left: 6px;"-->
                                 <!--@click="addUserDialog">-->
                                <!--<el-tag size="small " style="cursor: pointer;">＋新增人员</el-tag>-->
                            <!--</div>-->
                        <!--</div>-->
                    <!--</el-form-item>-->
                    <!--<el-form-item>-->
                        <!--<label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称&nbsp;：</label>-->
                        <!--<el-input v-model="routeParam.siteName" style="width: 442px;"></el-input>-->
                    <!--</el-form-item>-->
                    <!--<el-form-item>-->
                        <!--<label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;置&nbsp;：</label>-->
                        <!--<el-input v-model="routeParam.sitePlace" style="width: 442px;"></el-input>-->
                    <!--</el-form-item>-->
                    <el-form-item>
                        <label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">设&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;：</label>
                        <el-input v-model="equipCodeValue" style="width: 442px;"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">设&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型&nbsp;：</label>
                        <!--<el-input v-model="routeParam.sitePlace" style="width: 442px;"></el-input>-->
                        <el-select style="width: 152px" v-model="equipTypeValue" @change="clickEquipType" placeholder="设备类型">
                            <el-option label="不限" :value="-1">
                            </el-option>
                            <el-option v-for="item in equipmentType" :key="item.value"
                                       :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <!--<el-form-item>-->
                        <!--<label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">添&nbsp;加&nbsp;管&nbsp;控&nbsp;分&nbsp;组&nbsp;设&nbsp;备&nbsp;：</label>-->
                        <!--<el-select style="width: 152px" v-model="routeParam.siteId" placeholder="选择项目" @change="clickProject()">-->
                            <!--<el-option v-for="item in projectList" :key="item.siteId" :label="item.labelName"-->
                                       <!--:value="item.siteId">-->
                            <!--</el-option>-->
                        <!--</el-select>-->
                        <!--<el-select style="width: 152px" v-model="efmValue" @change="clickCenterControl" placeholder="选择中控">-->
                            <!--&lt;!&ndash;<el-option label="不限" value="">&ndash;&gt;-->
                            <!--&lt;!&ndash;</el-option>&ndash;&gt;-->
                            <!--<el-option v-for="item in efmList" :key="item.id" :label="item.name"-->
                                       <!--:value="item.id">-->
                            <!--</el-option>-->
                        <!--</el-select>-->
                        <!--<el-select style="width: 152px" v-model="equipTypeValue" @change="clickEquipType" placeholder="设备类型">-->
                            <!--<el-option label="不限" :value="-1">-->
                            <!--</el-option>-->
                            <!--<el-option v-for="item in equipmentType" :key="item.value"-->
                                       <!--:label="item.label" :value="item.value">-->
                            <!--</el-option>-->
                        <!--</el-select>-->
                    <!--</el-form-item>-->
                </el-form>
                <!--<div style="width: 280px; height: calc(100% - 280px);min-height:300px;margin-left:160px;float: left">-->
                    <!--<div style="border: 1px solid #d8dce6;border-radius:4px;height:calc(100% - 50px)">-->
                        <!--<el-table ref="multipleTable" height="100%" :data="equipList" tooltip-effect="dark"-->
                                  <!--style="width: 100%" :row-class-name="leftRowClassName"-->
                                  <!--@selection-change="handleSelectionChange1" :header-cell-style="tableStyle">-->
                            <!--<el-table-column type="selection" width="40"></el-table-column>-->
                            <!--<el-table-column type="rowIndex" width="56" :formatter="formatLeftIndex" label="#"></el-table-column>-->
                            <!--<el-table-column prop="serialNumber" :formatter="formatNumber" label="编号"></el-table-column>-->
                            <!--<el-table-column prop="message" label="信息"></el-table-column>-->
                        <!--</el-table>-->

                    <!--</div>-->
                    <!--<div style="height: 50px">-->
                        <!--<el-pagination-->
                            <!--@current-change="handleCurrentChange1"-->
                            <!--layout="total, prev,next"-->
                            <!--:total="common1.total">-->
                        <!--</el-pagination>-->
                    <!--</div>-->
                <!--</div>-->
                <!--<div style="width: 100px; height: calc(100% - 330px);float: left;position: relative">-->
                    <!--<el-button class="v-1-0-0-Button" icon="el-icon-arrow-right" style="position: absolute;top: 120px;left:20px" @click="moveIn"></el-button>-->
                    <!--<el-button class="v-1-0-0-Button-Minor" icon="el-icon-arrow-left" style="position: absolute;top: 170px;left:20px" @click="moveOut"></el-button>-->
                <!--</div>-->
                <!--<div style="width: 280px; height: calc(100% - 280px);min-height:300px;float: left">-->
                    <!--<div style="border: 1px solid #d8dce6;border-radius:4px;height:calc(100% - 50px)">-->
                        <!--<el-table ref="multipleTable" height="100%" :data="equipCheckedList" tooltip-effect="dark"-->
                                  <!--style="width: 100%" :row-class-name="rightRowClassName"-->
                                  <!--@selection-change="handleSelectionChange2" :header-cell-style="tableStyle">-->
                            <!--<el-table-column type="selection" width="40"></el-table-column>-->
                            <!--<el-table-column prop="rowIndex" :formatter="formatRightIndex" width="56" label="#"></el-table-column>-->
                            <!--<el-table-column prop="serialNumber" :formatter="formatNumber" label="编号"></el-table-column>-->
                            <!--<el-table-column prop="message" label="信息"></el-table-column>-->
                        <!--</el-table>-->

                    <!--</div>-->
                    <!--<div style="height: 50px">-->
                        <!--<el-pagination-->
                            <!--@current-change="handleCurrentChange2"-->
                            <!--layout="total, prev,next">-->
                        <!--</el-pagination>-->
                    <!--</div>-->
                <!--</div>-->
                <div style="width: 320px;margin-left:160px;margin-top:15px;">
                    <div style="height:40px; width:56px; margin-top: 5px; float: left;">
                        <el-button class="v-1-0-0-Button" @click="newPro">确定</el-button>
                    </div>
                    <div style="height:40px; width:56px; margin-top: 5px; margin-left: 16px; float: left;">
                        <el-button class="v-1-0-0-Button-Minor" @click="goBack">返回</el-button>
                    </div>
                </div>
            </div>

        </div>


        <el-dialog title="新增人员" :visible.sync="addAppUserDialog" width="35%" center>
            <div style="width: 90%; margin:0 auto;height:120px;overflow: auto;">

                <el-checkbox-group v-model="userCheckedArr">
                    <el-checkbox v-for="item in appUserList" :label="item.id" :value="item.id" :key="item.id">{{item.account}}</el-checkbox>
                </el-checkbox-group>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addAppUserDialog = false" style="margin-top: 15px;">取 消</el-button>
                <el-button type="primary" @click="addFinish">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>
<!--<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>-->
    <!--<script type="text/javascript">-->

    <!--var map = new BMap.Map("adress");-->
    <!--var localSearch = new BMap.LocalSearch(map);-->
    <!--localSearch.enableAutoViewport(); //允许自动调节窗体大小-->
    <!--function searchByStationName() {-->
        <!--var adress = $('#adress').val(); //获取输入框输入的地址-->
        <!--localSearch.setSearchCompleteCallback(function (searchResult) {-->
            <!--var poi = searchResult.getPoi(0);-->
            <!--document.getElementById("result_").value = poi.point.lng + "," + poi.point.lat; //获取经度和纬度，将结果显示在文本框中-->
            <!--document.getElementById("lng").value = poi.point.lng;-->
            <!--document.getElementById("lat").value = poi.point.lat;-->
            <!--map.centerAndZoom(poi.point, 13);-->
        <!--});-->
        <!--localSearch.search(adress);-->
    <!--}-->
    <!--</script>-->

<script>
    import http from '../../../common/http';

    export default {

        data() {
            return {
                tableStyle: {
                    'background-color': '#F6F7FB',
                    'font-weight': '400'
                },
                common1: {
                    total: 0,            //总数据量
                    pageSize: 20,        //每页条数
                    page: 1,              //页码
                    isPage: 1,            //是否分页
                    allSizes: [10, 20, 30], //每页条数筛选
                    checkboxArr: []       //多选框数组
                },
                common2: {
                    total: 0,            //总数据量
                    pageSize: 20,        //每页条数
                    page: 1,              //页码
                    isPage: 1,            //是否分页
                    allSizes: [10, 20, 30], //每页条数筛选
                    checkboxArr: []       //多选框数组
                },
                //管控人员列表
                appUserList: [],
                //已选中的管控人员
                appUserCheckedList:[],
                projectList: [],
                efmList: [],
                efmValue: null,
                //分组名称
                place: "",
                //终端类型
                equipTypeValue: -1,
                equipCodeValue: "",
                userCheckedArr: [],
                equipList:[],
                //已选中或已拥有的终端，右框显示
                equipCheckedList:[],
                //添加人员窗口
                addAppUserDialog: false,
                multipleSelectionLeft:[],
                multipleSelectionRight:[],
                //设备类型数组
                equipmentType: [
                    {
                        label: "拉链探测器一",
                        value: "0"
                    },
                    {
                        label: "拉链探测器二",
                        value: "1"
                    },
                ],
                min:[]
            };
        },
        created: function () {
            this.routeParam = this.$route.params;
            this.getProjectAndAppUser();
        },
        methods: {
            //生成时把索引插入数据
            rightRowClassName(row){
                row.row.index = row.rowIndex;
            },
            leftRowClassName(row){
                row.row.index = row.rowIndex;
            },
            formatLeftIndex(row) {
                return row.rowIndex = this.common1.pageSize * (this.common1.page - 1)  + 1+ row.index;
            },
            formatRightIndex(row) {
                return row.rowIndex = this.common2.pageSize * (this.common2.page - 1)  + 1+ row.index;
            },
            //获取项目和管控人员
            getProjectAndAppUser() {
                let url = this.getAllGroupUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.appUserList = res.data.appUserList;
                        this.projectList = res.data.getsiteList;
                        //编辑分组管控人员状态改变
                        for (let i in this.userCheckedArr){
                            this.userCheckedArr[i] = parseInt(this.userCheckedArr[i]);
                        }
                        for (let i = 0;i < this.appUserList.length;i++){
                            if(this.userCheckedArr.indexOf(this.appUserList[i].id) != -1){
                                this.appUserCheckedList.push(this.appUserList[i]);
                            }
                        }
                        for (let i in this.projectList){
                            if(this.projectList[i].siteName){
                                this.projectList[i].labelName = this.projectList[i].siteName;
                            }
                            else{
                                this.projectList[i].labelName = this.projectList[i].siteCord
                            }
                        }
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {

                });
            },
            getGroupEquip(){
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getGroupEquipUrl;
                let data = {
                    id:this.routeParam.groupId,
                    page:0,
                    limit:0
                };
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        _loading.close();
                        this.equipCheckedList = res.data.items;
                        console.log(res);
                    }
                    else {
                        _loading.close();
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {
                    _loading.close();
                });
            },
            //打开管控人员窗口
            addUserDialog() {
                // if(this.appUserList.length == 0){
                //     this.$commonFn.showTip("请先创建管控人员", 2);
                //     return;
                // }
                this.addAppUserDialog = true;
            },
            //点击项目下拉框
            clickProject() {
                let url = this.getGroupDeviceUrl;
                this.efmValue = null;
                this.equipTypeValue = -1;
                this.equipCheckedList = [];
                let data = {
                    page: this.common1.page,
                    limit: this.common1.pageSize,
                    siteId: this.routeParam.siteId,
                    deviceId: this.efmValue,
                    type: this.equipTypeValue
                };
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.efmList = res.data.deviceBySiteId;
                        this.equipList = res.data.equipList;
                        this.common1.total = res.data.equipListCount;
                        for (let i = 0; i < this.efmList.length; i++) {
                            if (this.efmList[i].niName) {
                                this.efmList[i].name = this.efmList[i].niName;
                            }
                            else if (this.efmList[i].specificator) {
                                this.efmList[i].name = this.efmList[i].specificator;
                            }
                            else {
                                this.efmList[i].name = parseInt(this.efmList[i].device_code,16);
                            }
                        }
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {

                });
            },
            getEquipList(){
                let url = this.getGroupDeviceUrl;
                let data = {
                    page: this.common1.page,
                    limit: this.common1.pageSize,
                    siteId: this.routeParam.siteId,
                    deviceId: this.efmValue,
                    type: this.equipTypeValue
                };
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.equipList = res.data.equipList;
                        this.common1.total = res.data.equipListCount;
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {

                });
            },
            //点击选择中控
            clickCenterControl() {
                // if(!this.routeParam.siteId){
                //     this.$commonFn.showTip("请先选择项目",2);
                //     this.efmValue = "";
                //     return;
                // }
                console.log(this.efmValue)
                if(this.efmValue !== '0'){
                    this.getEquipList();
                }
                else{
                    this.getMqttList();
                }
            },
            getMqttList(){
                let url = this.getMqttListUrl;
                let data = {
                    siteId: this.routeParam.siteId,
                    type:this.equipTypeValue == null?-1:this.equipTypeValue,
                    currentPage: this.common1.page,
                    pageSize: this.common1.pageSize,
                    checkgroup:1
                };
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.equipList = res.data.pageList;
                        this.common1.total = res.data.total;
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                });
            },
            //选择类型
            clickEquipType(){
                // if(!this.routeParam.siteId){
                //     this.$commonFn.showTip("请先选择项目",2);
                //     this.equipTypeValue = "";
                //     return;
                // }
                this.getEquipList();
            },
            formatNumber: function (row) {
                return row.serialNumber = parseInt(row.line_code) + 1 + "-" + row.addr;
            },
            // formatEfmName: function (row) {
            //     if(row.niNameDevice){
            //         row.efmName = row.niNameDevice;
            //     }
            //     else if(row.specificatorDevice){
            //         row.efmName = row.specificatorDevice;
            //     }
            //     else{
            //         row.efmName = row.device_code
            //     }
            //     return row.efmName;
            // },
            //确定添加人员
            addFinish() {
                this.appUserCheckedList = [];
                for (let i = 0;i < this.appUserList.length;i++){
                    if(this.userCheckedArr.indexOf(this.appUserList[i].id) != -1){
                        this.appUserCheckedList.push(this.appUserList[i]);
                    }
                }
                console.log(this.userCheckedArr);
                this.addAppUserDialog = false;
            },
            //确定新增项目
            newPro(){

                // let appUserArr = [];
                // let equipArr = [];
                // for(let i = 0;i < this.appUserCheckedList.length;i++){
                //     appUserArr.push(this.appUserCheckedList[i].id);
                // }
                // for(let i = 0;i < this.equipCheckedList.length;i++){
                //     equipArr.push(this.equipCheckedList[i].id);
                // }
                // let appUserStr = appUserArr.join(',');
                // let equipList = equipArr.join(',');
                //新增终端
                if(this.equipTypeValue === "-1" || this.equipCodeValue === "" || this.equipCodeValue.length != 6){
                    alert("请选择设备类型和填写设备编码");
                    return "";
                }else {
                let _loading = this.$commonFn.showLoading(2,'.main-box');
                let url = this.newDeviceUrl;
                let data = {
                    // id: this.routeParam.groupId,
                    // place:this.routeParam.place,
                    // siteId: this.routeParam.siteId,
                    // equipList: equipList,
                    // appUserList: appUserStr
                    siteCode:this.routeParam.siteCode,
                    deviceType:this.equipTypeValue,
                    equipCode:this.equipCodeValue,
                };
                // alert(this.routeParam.siteCode)

                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if(res.status === 200){
                        this.$commonFn.showTip("添加成功",1);
                        this.$router.push('/main');
                    }
                    else{
                        this.$commonFn.showTip(res.message,3);
                    }
                }, (err) => {
                    _loading.close();
                });
                }
            },
            //返回管控页面
            backToControl() {
                this.$router.push(
                    {
                        path: 'mqttDeviceList'
                    }
                );
            },
            goBack(){
                // alert("11111")
                this.$router.push(
                    {
                        name:'mqttDeviceList',
                        params:{
                            siteId:this.routeParam.site_id,
                            equipmentTypeValue:this.routeParam.equipmentTypeValue,
                            inputBoxValue:this.routeParam.inputBoxValue,
                            page:this.routeParam.page

                        }
                    }
                );
            },

            handleCurrentChange1(val) {
                this.common1.page = val;
                if(this.efmValue !== '0'){
                    this.getEquipList();
                }
                else{
                    this.getMqttList();
                }
            },
            handleCurrentChange2(val) {
                this.common2.page = val;
            },
            handleSelectionChange1(val) {
                console.log(val)
                this.multipleSelectionLeft = val;
            },
            handleSelectionChange2(val) {
                this.multipleSelectionRight = val;
            },
            //选中
            moveIn(){
                //排序
                let min;
                for(let i = 0; i < this.multipleSelectionLeft.length; i++){
                    for(let  j = i; j < this.multipleSelectionLeft.length; j++){
                        if(this.multipleSelectionLeft[i].index > this.multipleSelectionLeft[j].index){
                            min = this.multipleSelectionLeft[j];
                            this.multipleSelectionLeft[j]=this.multipleSelectionLeft[i];
                            this.multipleSelectionLeft[i]=min;
                        }
                    }
                }
                for (let i = 0;i < this.multipleSelectionLeft.length;i++){
                    if(this.equipCheckedList.indexOf(this.multipleSelectionLeft[i]) == -1){
                        //console.log(this.equipList)
                        console.log(this.multipleSelectionLeft)
                        console.log(this.multipleSelectionLeft[i].index)
                        this.equipCheckedList.push(this.multipleSelectionLeft[i]);
                        this.equipList.splice(this.multipleSelectionLeft[i].index - i,1);
                        //console.log(this.equipList)
                    }
                }
            },
            //取消选中
            moveOut(){
                //排序
                let min;
                for(let i = 0; i < this.multipleSelectionRight.length; i++){
                    for(let  j = i; j < this.multipleSelectionRight.length; j++){
                        if(this.multipleSelectionRight[i].index > this.multipleSelectionRight[j].index){
                            min = this.multipleSelectionRight[j];
                            this.multipleSelectionRight[j]=this.multipleSelectionRight[i];
                            this.multipleSelectionRight[i]=min;
                        }
                    }
                }
                for (let i = 0;i < this.multipleSelectionRight.length;i++){
                    this.equipCheckedList.splice(this.multipleSelectionRight[i].index - i,1);
                    this.equipList.unshift(this.multipleSelectionRight[i]);
                }
            }
        },
        mixins: [http]
    };
</script>

<style scoped>

</style>
