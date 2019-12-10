import axios from 'axios';
import router from '../router';
import bus from '../components/common/bus';
import { Notification } from 'element-ui';
//axios.defaults.withCredentials=true;//让ajax携带cookie
const apiMethods = {
    data:function(){
        return{
            loginUrl: this.$constant.apiUrl + "/get/token",                                     //登录接口
            getVerifyCodeUrl: this.$constant.apiUrl + "/get/passwordCode",                    //获取手机验证码接口
            forgetPasswordUrl:this.$constant.apiUrl + "/get/forgetPassword",                  //忘记密码修改密码接口
            adminListUrl:this.$constant.apiUrl+"/api/user/list/userList",                     //管理员列表接口
            saveAdminUrl:this.$constant.apiUrl+'/api/user/save/saveToUser',                  //新增管理员
            deleteAdminUrl:this.$constant.apiUrl+'/api/user/delete/deleteUser',              //删除管理员
            resetPwdUrl:this.$constant.apiUrl+"/api/user/save/resetPassWord",                //重置管理员密码
            getProvinceAndRoleUrl:this.$constant.apiUrl+'/api/user/save/getPrivinceAndRole',     //获取省份和角色
            getCityAndSiteUrl:this.$constant.apiUrl+'/api/user/save/getCityAndSite',         //获取省联动市区和项目
            getSiteByCity:this.$constant.apiUrl+'/api/user/save/getSiteByCity',              //新增管理员地区联动项目
            userListUrl:this.$constant.apiUrl+"/api/user/list/userAppList",                  //管控人员列表接口
            deleteUserUrl:this.$constant.apiUrl+'/api/user/delete/deleteAppUser',            //删除管控人员
            addUserUrl:this.$constant.apiUrl+"/api/user/save/addToAppUser",                  //新增管控人员
            saveUserUrl:this.$constant.apiUrl+"/api/user/save/saveToAppUser",                //编辑管控人员
            auditUserUrl:this.$constant.apiUrl+"/api/user/save/auditAppUser",                //审核管控人员
            getRoleListUrl:this.$constant.apiUrl+'/api/user/list/roleList',                  //获取角色列表
            getAllRoleUrl:this.$constant.apiUrl+'/api/user/list/permissionsList',            //获取所有权限
            saveRoleUrl:this.$constant.apiUrl+'/api/user/save/saveRole',                     //编辑角色
            deleteRoleUrl:this.$constant.apiUrl+"/api/user/delete/deleteRole",               //删除角色
            getSiteListUrl:this.$constant.apiUrl+'/api/user/list/getSiteList',               //获取项目列表
            getDeviceBySiteUrl:this.$constant.apiUrl+'/api/user/list/getDeviceBySite',       //获取项目的中控设备
            editSiteInfoUrl:this.$constant.apiUrl+'/api/equip/editSiteNiName',               //编辑项目信息
            uploadLogo:this.$constant.apiUrl+'/api/equip/uploadLogo',                       //上传项目LOGO
            delLogo:this.$constant.apiUrl+'/api/equip/delLogo',                             //删除项目LOGO
            deleteEfmUrl:this.$constant.apiUrl+'/api/equip/deleteEfm',                       //删除中控设备
            getEfmInfoUrl:this.$constant.apiUrl+'/api/equip/devicesInfo',                    //获取中控设备详情
            getEquipListUrl:this.$constant.apiUrl+'/api/equip/equipList',                    //获取中控下终端列表
            editEfmNameUrl:this.$constant.apiUrl+'/api/equip/editDeviceInfo',                //修改中控信息
            getEquipInfoUrl:this.$constant.apiUrl+'/api/equip/equipInfo',                      //获取终端详情
            getZipperInfoUrl:this.$constant.apiUrl+'/api/equip/selectAllZipperInfo',           //获取拉链终端详情
            deleteEquipUrl:this.$constant.apiUrl+'/api/equip/deleteDevice',                      //删除终端
            sendAdminSocketUrl:this.$constant.apiUrl + '/api/sendAdminSocket',               //管理员下发指令接口
            getAlarmLogUrl:this.$constant.apiUrl+'/api/alarm/alarmLog',                         //报警日志初始化
            getAlarmLogBySiteIdUrl:this.$constant.apiUrl+'/api/alarm/alarmLogBySiteId',                 //报警日志按项目id查询
            getFaultLogUrl:this.$constant.apiUrl+'/api/alarm/faultLog',                         //故障日志初始化
            getFaultLogBySiteIdUrl:this.$constant.apiUrl+'/api/alarm/faultLogBySiteId',             //故障日志按项目id查询
            getOffLineLogUrl:this.$constant.apiUrl+'/api/alarm/offLineLog',                         //离线日志初始化
            getOfflineLogBySiteIdUrl:this.$constant.apiUrl+'/api/alarm/offLineLogBySiteId',         //离线日志按项目id查询
            //getMapInfoUrl:this.$constant.apiUrl+'/api/index/mapSite',                         //地图信息
            getMapInfoUrl:this.$constant.apiUrl+'/api/map/mapSite',                         //地图信息(自动切换设备地图与中控地图)
            getControlGroupUrl:this.$constant.apiUrl+'/api/controlGroup/groupInfo',           //获取管控分组列表
            getAllGroupUrl:this.$constant.apiUrl+'/api/controlGroup/addGroupInitData',      //获取所有项目和管控人员
            deleteGroupUrl:this.$constant.apiUrl+'/api/controlGroup/delGroup',              //删除管控分组
            getGroupDeviceUrl:this.$constant.apiUrl+'/api/controlGroup/getDeviceAndEquipList',//获取管控分组和终端相关数据
            saveGroupUrl:this.$constant.apiUrl+'/api/controlGroup/saveGroup',                   //添加/编辑管控分组
            newProjectUrl:this.$constant.apiUrl+'/api/controlGroup/newProject',                 //新建项目
            newDeviceUrl:this.$constant.apiUrl+'/api/controlGroup/newDevice',                 //新建终端
            delWarningUrl:this.$constant.apiUrl+'/api/alarm/delWarn',                          //删除告警
            delZipperLogByIdUrl:this.$constant.apiUrl+'/api/alarm/deleteZipperLogById',                          //新的删除告警
            sendSocketMessage:this.$constant.apiUrl + '/api/user/list/sendSocketMessage',     //发送指令
            getAlarmCountUrl:this.$constant.apiUrl+'/api/index/headAlarmCount',               //获取头部数据
            getIndexUrl:this.$constant.apiUrl+'/api/index/default',                             //获取首页数据
            statisticDataUrl:this.$constant.apiUrl+'/api/statistics/default',                //统计管理初始化
            getProvinceIndexUrl:this.$constant.apiUrl+'/api/index/changeProvince',            //首页切换省获取数据
            getAreaIndexUrl:this.$constant.apiUrl+'/api/index/changeArea',                     //首页切换地区获取数据
            mqttIssuingInstructionsUrl:this.$constant.apiUrl + '/api/mqttIssuingInstructions',//mqtt下发指令接口
            setMqttIssuingInstructionsUrl:this.$constant.apiUrl + '/api/setMqttIssuingInstructions',//mqtt下发指令接口
            excelForWarnLogUrl:this.$constant.apiUrl+"/excelForWarnLog",                        //报警表导出excel
            excelForAlarmLogUrl:this.$constant.apiUrl+"/excelForWarnLogByNewParam",             //新报警日志表导出excel
            excelForFaultLogUrl:this.$constant.apiUrl+"/excelForFaultLogByNewParam",             //新故障日志表导出excel
            excelForOffLineLogUrl:this.$constant.apiUrl+"/excelForOffLineLogByNewParam",          //新离线日志表导出excel
            getGroupEquipUrl:this.$constant.apiUrl+"/api/controlGroup/selHadEquip",             //获取分组下的终端设备
            getFileUrl:this.$constant.apiUrl+"/api/file/getFolderList",                          //获取资料文件接口
            uploadUrl:this.$constant.apiUrl+'/api/user/common/img/upload',                     //上传文件
            deleteFileUrl:this.$constant.apiUrl+'/api/file/delFolder',                          //删除上传文件夹
            editFileUrl:this.$constant.apiUrl+'/api/user/common/img/editFolder',                //编辑上传文件
            getWarnInfoUrl:this.$constant.apiUrl+'/api/alarm/getWarnInfo',                      //报警查看
            auditAlarmUrl:this.$constant.apiUrl+'/api/alarm/auditDealFeedback',                //审核报警
            alarmHistoryUrl:this.$constant.apiUrl+'/api/alarm/alarmHistory',                      //处理历史
            deleteProjectUrl:this.$constant.apiUrl+'/api/equip/delSite',                          //删除项目
            editEfmInfoUrl:this.$constant.apiUrl+'/api/equip/editDeviceInfo',                    //修改中控信息
            editEquipInfoUrl:this.$constant.apiUrl+'/api/equip/editEquipInfo',                   //修改终端信息
            changeAreaStaticUrl:this.$constant.apiUrl+'/api/statistics/changeProvince',         //统计切换城市
            getMqttListUrl:this.$constant.apiUrl+'/api/mqttEquip/getMqttEquipList',               //获取无线终端设备列表
            deleteMqttListUrl:this.$constant.apiUrl+'/api/mqttEquip/deleteMqttEquip',               //删除无线终端
            getLogListUrl:this.$constant.apiUrl+'/api/user/list/logList',                           //获取操作日志
            getPhotoInfoUrl:this.$constant.apiUrl+'/api/file/getPhotoList',                           //获取图片详情
            deletePhotoUrl:this.$constant.apiUrl+'/api/file/img/delPhoto',                           //删除图片
            exportHistoryUrl:this.$constant.apiUrl+'/excelForWarnHistory',                           //导出历史
            deleteMqttDeviceUrl:this.$constant.apiUrl+'/api/mqttEquip/deleteMqttEquipBySiteCode',  //删除无线终端
            getWarningCountUrl:this.$constant.apiUrl+'/api/alarm/getWarnCountByWarnIndex',           //获取报警数量
            setDeviceParamsUrl:this.$constant.apiUrl+'/api/sendSocketMessageForStringtoHex',           //修改设备描述符
            mapWirelessUrl:this.$constant.apiUrl+'/api/index/mapWireless',                               //获取无线设备地图信息
            mapDataUrl:this.$constant.apiUrl+'/api/index/mapData',                                        //获取无线中控地图信息
            changePassword:this.$constant.apiUrl+'/api/user/save/changePassword',


            //  反向代理,'/baseUrl'指向'http://192.168.1.103:8080'
        }
    },
    methods: {
        apiGet(url) {
            return new Promise((resolve, reject) => {
                if(sessionStorage.getItem('token')){
                    let token = sessionStorage.getItem('token');
                    let otherConfig = {
                        headers:{
                            'Authorization': 'Bearer ' + token,
                        }
                    };
                    axios.get(url,otherConfig).then((response) => {
                        resolve(response.data)
                    },(err) => {
                        reject(err);
                        if(err.response.status === 401){
                            this.$commonFn.showTip('登录超时，请重新登录',3);
                            setTimeout(function () {
                                router.push('/login');
                            },2000);
                        }
                        else if(err.response.status === 403){
                            router.push('/403');
                        }
                        else{
                            this.$commonFn.showTip('网络繁忙，请稍后重试',3);
                        }
                    })
                }
                else{
                    axios.get(url).then((response) => {
                        resolve(response.data)
                    },(err) => {
                        this.$commonFn.showTip('网络繁忙，请稍后重试',3);
                        reject(err);
                    })
                }
            })
        },
        apiPost(url, data,responseType) {
            return new Promise((resolve, reject) => {
                if(sessionStorage.getItem('token')){
                    let token = sessionStorage.getItem('token');
                    let otherConfig = {
                        headers:{
                            'Authorization': 'Bearer ' + token,
                        },
                    };
                    //返回格式
                    if(responseType){
                        otherConfig.responseType = responseType;
                    }
                    data = this.getWwwFormData(data);
                    axios.post(url, data,otherConfig).then((response) => {
                        if(response.data.status == 10){
                            bus.$on("closeWebsocket",() => {
                                this.closeWebSocket();
                            });
                            if(response.data.message == '异地登录'){
                                this.$commonFn.showTip("您的账号已在其他地方登录，请及时确认",3);
                            }
                            else{
                                this.$commonFn.showTip("登录超时，请重新登录",3);
                            }
                            setTimeout(function () {
                                Notification.closeAll();
                                router.push('/login');
                            },2000);
                        }
                        else{
                            resolve(response.data);
                        }
                    },(err) => {
                        reject(err);
                        if(err.response.status === 401){
                            bus.$on("closeWebsocket",() => {
                                this.closeWebSocket();
                            });
                            this.$commonFn.showTip('登录超时，请重新登录',3);
                            setTimeout(function () {
                                Notification.closeAll();
                                router.push('/login');
                            },2000);
                        }
                        else if(err.response.status === 403){
                            router.push('/403');
                        }
                        else{
                            this.$commonFn.showTip('网络繁忙，请稍后重试',3);
                        }
                    })
                }
                else{
                    data = this.getWwwFormData(data);
                    axios.post(url, data).then((response) => {
                        resolve(response.data)
                    },(err) => {
                        this.$commonFn.showTip('网络繁忙，请稍后重试',3);
                        reject(err);
                    })
                }
            })
        },
        apiDelete(url, id) {
            return new Promise((resolve, reject) => {
                axios.delete(url + id).then((response) => {
                    resolve(response.data)
                }, (response) => {
                    this.$commonFn.showTip('网络繁忙，请稍后重试',3);
                    reject(response);
                    // _g.closeGlobalLoading()
                    // bus.$message({
                    //     message: '请求超时，请检查网络',
                    //     type: 'warning'
                    // })
                })
            })
        },
        apiPut(url, id, obj) {
            return new Promise((resolve, reject) => {
                axios.put(url + id, obj).then((response) => {
                    resolve(response.data)
                }, (response) => {
                    this.$commonFn.showTip('网络繁忙，请稍后重试',3);
                    reject(response);
                })
            })
        },
        //构造x-www-form-urlencoded格式的数据,否则服务端无法接收到数据
        getWwwFormData(data){
            let dataStr="";
            for(let key in data){
                dataStr += key + "=" + data[key] + "&";
            }
            dataStr = dataStr.substring(0,dataStr.length-1);
            return dataStr;
        }
    }
}

export default apiMethods
