<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto; ">

        <div style=" width:96%; height:calc(100% - 40px); background-color: white; margin: 20px auto 20px auto;overflow:auto; ">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">档案管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">施工图</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="height:50px; margin:5px 16px 0 16px;">
                <div style="display: inline-block; height:40px;float: right;">
                    <el-button class="v-1-0-0-Button" icon="el-icon-upload2" @click="uploadPage()">上传</el-button>
                </div>
            </div>
            <div style="height:calc(100% - 152px);margin: 0 16px auto 16px;overflow: auto">
                <el-row class="picBox" :gutter="30" style="margin: 0 !important;">
                    <el-col :span="6" v-for="(item,index) in fileList" :key="item.id">
                        <div class="picBox2" @click="openImg(item)" @mousemove="itemMove(index)" @mouseout="itemOut(index)">
                            <img :src="item.firstPic" />
                            <div class="pic_info" style="text-align: center">
                                <div style="width: 100%;height: 50%">
                                    <span style="font-weight: bold;position: relative;top: 3px;">{{item.folderName}}</span>
                                </div>
                                <div style="width: 100%;height: 50%">
                                    <span style="display: inline-block;float: right">{{item.utime}}</span>
                                </div>
                            </div>
                            <i class="el-icon-delete deleteFileIcon" :class="{active:index==isActive}" @click.stop="deleteFile(item)" ></i>
                        </div>
                    </el-col>
                </el-row>
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
        name: "circuitDiagram",
        data() {
            return {
                common: {
                    total: 0,            //总数据量
                    pageSize: 12,        //每页条数
                    page: 1,              //页码
                    isPage: 1,            //是否分页
                    allSizes: [8, 12, 20], //每页条数筛选
                    checkboxArr: []       //多选框数组
                },
                filters:{
                    type:0,
                    siteId:0,
                    startTime:"",
                    endTime:"",
                },
                fileList:[],
                // isMoveIn:false,
                deleteFileId:0,
                isActive:false,
            }
        },
        created:function(){
            this.getFile();
        },
        methods: {
            getFile(){
                let url = this.getFileUrl;
                let data = {
                    currentPage:this.common.page,
                    pageSize:this.common.pageSize,
                    type:this.filters.type,
                    siteId:this.filters.siteId,
                    startTime:this.filters.startTime,
                    endTime:this.filters.endTime,
                };
                this.apiPost(url,data).then((res) => {
                    if(res.status === 200){
                        this.fileList = res.data.photoList;
                        this.common.total = res.data.total;
                        for(let i in this.fileList){
                            this.fileList[i].utime = this.$commonFn.formatDate(this.fileList[i].uploadTime,'Y-M-D');
                            if(this.fileList[i].gFilePath){
                                let picArr = this.fileList[i].gFilePath.split(',');
                                this.fileList[i].firstPic = picArr[0];
                            }
                        }
                    }
                },(err)=>{

                });
            },
            itemMove(index){
                this.isActive = index;
            },
            itemOut(){
                this.isActive = null;
            },
            openImg(obj) {
                this.$router.push(
                    {
                        name: 'upLoadDiagram',
                        params: {
                            type: 1,
                            id:obj.folderId,
                            folderName:obj.folderName,
                            filePath:obj.gFilePath,
                            siteId:obj.siteId,
                            uploader:obj.uploader,
                        }
                    }
                );
            },
            uploadPage() {
                this.$router.push(
                    {
                        name: 'upLoadDiagram',
                        params: {
                            type: 0
                        }
                    }
                );
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1;
                this.getFile();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getFile();
            },
            deleteFile(obj){
                this.deleteFileId = obj.folderId;
                this.$commonFn.showConfirm(this.deleteSuccess);
            },
            deleteSuccess(){
                let url = this.deleteFileUrl;
                let data = {
                    folderId:this.deleteFileId
                };
                this.apiPost(url,data).then((res) => {
                    if(res.status === 200){
                        this.$commonFn.showTip("删除成功",1);
                        this.common.page = 1;
                        this.getFile();
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                },(err)=>{

                });
            }
        },
        mixins: [http]
    }
</script>

<style>
    .picBox{
        height: 100%;
    }
    .picBox .el-col{
        margin-top: 10px;
    }
    @media screen and (max-width: 1920px) {
        .picBox .el-col{
            height: 300px;
        }
        .pic_info div span{
            font-size: 17px;
        }
    }
    @media screen and (max-width: 1600px) {
        .picBox .el-col{
            height: 250px;
        }
        .pic_info div span{
            font-size: 15px;
        }
    }
    @media screen and (max-width: 1440px) {
        .picBox .el-col{
            height: 200px;
        }
        .pic_info div span{
            font-size: 13px;
        }
    }


    .picBox .el-col .picBox2{
        height: calc(100% - 18px);
        border: 1px solid #f5eeee;
        padding: 8px;
        box-shadow: 1px 1px 2px #cccccc;
        cursor: pointer;
    }
    .picBox .el-col .picBox2{
        position: relative;
    }
    .picBox .el-col .picBox2:hover{
        position: relative;
        top: -2px;
        left: -2px;
        box-shadow: 4px 4px 5px #cccccc !important;
    }
    .picBox .el-col img{
        width: 100%;
        height: 80%
    }
    .picBox .el-col .picBox2 .pic_info{
        width: 100%;
        height: 20%;
    }
    .deleteFileIcon{
        position: absolute;
        right: 2px;
        top: 2px;
        color:red;
        cursor: pointer;
        display: none;
    }
    .active{
        display: inline-block;
    }
</style>

