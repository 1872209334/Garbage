<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto; ">

        <div class="pageBox">

            <div class="myNavigation">
                <el-breadcrumb separator="/" style="display: inline-block">
                    <el-breadcrumb-item>
                        <span style="font-size: 16px;color:#303313;">档案管理</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span style="font-size: 14px;color:#606266;">验收报告</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>
                        <span v-show="routeParam.type == 0" style="font-size: 14px;color:#606266;">新增验收报告</span>
                        <span v-show="routeParam.type != 0" style="font-size: 14px;color:#606266;">编辑验收报告</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div style="margin: 16px 20px 0 16px;height: calc(100% - 53px);overflow-y: auto">
                <el-form label-position="left" ref="form" label-width="100px">
                    <el-form-item>
                        <label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</label>
                        <el-input style="width: 215px;" v-model="drawingInput" placeholder="文件名称"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;目：</label>
                        <el-select v-model="projectSelectVal" placeholder="请选择">
                            <el-option v-for="item in projectSelect" :key="item.siteId" :label="item.siteLabel" :value="item.siteId"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-show="routeParam.type == 1">
                        <label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">用&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;户：</label>
                        <div style="margin-left: 5px">{{routeParam.uploader}}</div>
                    </el-form-item>
                    <el-form-item style="width: 500px;">
                        <label slot="label" style="height:32px;display: inline-block;line-height: 32px;color:#000">图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片：</label>
                        <el-upload
                            ref="upload"
                            class="upload-demo"
                            :action="upload_url"
                            accept="image/jpeg,image/gif,image/png,image/jpg"
                            :on-preview="handlePreview"
                            :on-remove="removeImg"
                            multiple
                            :file-list="fileList"
                            :before-upload="beforeUpload"
                            :auto-upload="isAuto"
                            :headers="headers"
                            list-type="picture">
                            <el-button size="small" class="v-1-0-0-Button">点击选取</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2M</div>
                        </el-upload>
                    </el-form-item>
                </el-form>
                <div style="width: 320px;margin-left:100px;float: left">
                    <div style="height:40px; width:56px; margin-top: 5px; float: left;">
                        <el-button class="v-1-0-0-Button" @click="submitUpload">提交</el-button>
                    </div>
                    <div style="height:40px; width:56px; margin-top: 5px; margin-left: 16px; float: left;">
                        <el-button class="v-1-0-0-Button-Minor" @click="goBack">返回</el-button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import http from '../../../common/http';

    export default {
        name:"upLoadReport",
        data(){
            return {
                upload_url:'aaa',
                headers: {
                    Authorization: 'Bearer '+ sessionStorage.getItem('token')
                },
                fileList: [],
                fileData:{},
                projectSelect:[],
                projectSelectVal:null,
                drawingInput:"",
                fileType:1,
                isAuto:false,
                uploadForm: new FormData()
            }
        },
        created:function () {
            this.fileData = new FormData();
            this.routeParam = this.$route.params;
            if(this.routeParam.type == 1){
                this.getPhotoInfo();
                this.projectSelectVal = this.routeParam.siteId;
                this.drawingInput = this.routeParam.folderName;
            }
            this.getProject();

        },
        methods:{
            //获取项目下拉框
            getProject(){
                let url = this.getAllGroupUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    console.log(res);
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
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {

                });
            },
            getPhotoInfo(){
                let url = this.getPhotoInfoUrl;
                let data = {
                    folderId:this.routeParam.id
                };
                this.apiPost(url, data).then((res) => {
                    console.log(res);
                    if (res.status === 200) {
                        let fileArr = res.data.photoList;
                        for (let i in fileArr){
                            this.fileList.push({
                                imgId:fileArr[i].id,
                                url:fileArr[i].filePath
                            })
                        }
                    }
                    else{
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {

                });
            },
            //点击上传的操作
            beforeUpload(file){
                this.uploadForm.append('imgFile', file);
                return false;
            },
            submitUpload() {
                if(this.routeParam.type == 0){
                    let url = this.uploadUrl;
                    this.uploadForm.append('siteId',this.projectSelectVal);
                    this.uploadForm.append('type',this.fileType);
                    this.uploadForm.append('folderName',this.drawingInput);
                    this.$axios.post(url, this.uploadForm).then((res) => {
                        if(res.data.status === 200){
                            this.$commonFn.showTip("上传成功",1);
                            this.$router.push({
                                name:'inspectionReport'
                            });
                        }
                        else{
                            this.$commonFn.showTip(res.data.message, 3);
                        }
                    }, (err) => {
                        //_loading.close();
                        this.$commonFn.showTip("网络繁忙，请稍后重试", 3);
                    });
                    this.$refs.upload.submit();
                }
                else{
                    let url = this.editFileUrl;
                    console.log(this.uploadForm)
                    this.uploadForm.append('siteId',this.projectSelectVal);
                    this.uploadForm.append('folderId',this.routeParam.id);
                    this.uploadForm.append('folderName',this.drawingInput);
                    console.log(this.uploadForm)
                    this.$axios.post(url, this.uploadForm).then((res) => {
                        console.log(res.status)
                        //_loading.close();
                        if(res.data.status == 200){
                            this.$commonFn.showTip("编辑成功",1);
                            this.$router.push({
                                name:'inspectionReport'
                            });
                        }
                        else{
                            this.$commonFn.showTip(res.data.message, 3);
                        }
                    }, (err) => {
                        //_loading.close();
                        this.$commonFn.showTip("网络繁忙，请稍后重试", 3);
                    });
                    this.$refs.upload.submit();
                }
            },
            goBack(){
                this.$router.push({
                    name:'inspectionReport'
                });
            },
            handleSuccessWare(response,file,fileList){
                this.$commonFn.showTip("上传成功",1);
                this.$router.push({
                    name:'inspectionReport'
                });
            },
            removeImg(file, fileList) {
                console.log(file, fileList);
                if(this.routeParam.type == 1 && file.imgId){
                    let url = this.deletePhotoUrl;
                    let data = {
                        imgId:file.imgId
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
            handlePreview(file) {
                console.log("handlePreview");
                console.log(file);
            },
        },
        mixins: [http]
    }
</script>

<style>
    .upload-container {
        margin-bottom: 20px;
    }
    .upload-demo .el-upload-list{
        height: 280px;
        overflow-y: auto;
        border: 1px solid #f0f0f0;
        border-radius: 5px;
    }

</style>

