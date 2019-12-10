<template>

    <div class="wrapper">
        <home-nav></home-nav>
        <home-menu-bar></home-menu-bar>
        <div class="main-box">
            <transition name="move" mode="out-in">
                <router-view></router-view>
            </transition>
        </div>
    </div>

</template>
<script>
    import homeNav from './Header.vue';
    import homeMenuBar from './Sidebar.vue';
    import bus from '../../common/bus';
    import http from '../../common/http';

    export default {
        inject: ['reload'],
        data() {
            return {
                userID: 'admin_',
                websocket: null,
                postValue: {},
                collapse: false,
                notificationArr: [],
                alarmNum: 0,
                faultNum: 0,
                offlineNum: 0,
                timer:{}
            }
        },
        components: {
            homeNav, homeMenuBar,
        },
        created() {
            //连接websocket
            this.connectWebSocket();
            bus.$on("closeWebsocket", () => {
                this.closeWebSocket();
            });
            let that = this;
            //自动重连websocket
            this.timer = setInterval(function () {
                if(that.websocket.readyState === that.websocket.CLOSED){
                    that.connectWebSocket();
                }
            },20000);
        },
        destroyed:function(){
            clearInterval(this.timer);
        },
        methods: {
            //建立WebSocket连接
            connectWebSocket() {
                let url = this.getWarningCountUrl;
                let data = {
                    warnIndex: 0
                };
                this.apiPost(url, data).then((res) => {

                }, (err) => {

                });
                let that = this;
                this.userID = JSON.parse(sessionStorage.getItem('user_info')).uid;
                console.log(this.userID);
                //建立webSocket连接
                //this.websocket = new WebSocket("ws://127.0.0.1:8082/webSocketService/ID=admin_" + this.userID);
                //this.websocket = new WebSocket("ws://47.111.77.3:8082/webSocketService/ID=admin_" + this.userID);
                this.websocket = new WebSocket("ws://129.211.104.67:8082/webSocketService/ID=admin_" + this.userID);
                //打开webSokcet连接时，回调该函数
                this.websocket.onopen = function () {
                    console.log("打开webSokcet连接");
                };

                this.websocket.onclose = function () {
                    console.log("关闭websocket连接");
                };

                //接收信息
                this.websocket.onmessage = function (message) {
                    console.log(message.data);

                    let messageJson = JSON.parse(message.data);
                    let msg = messageJson.msg;
                    //中控故障、离线处理
                    if (msg == "alarm") {
                        let deviceId = messageJson.deviceId;
                        let warnType = messageJson.warnType ? messageJson.warnType : "报警";
                        if (warnType == "中控离线") {
                            // for (let i = 0;i< that.$constant.notificationArr.length;i++) {
                            //     if(that.$constant.notificationArr[i].customClass == 'offlineTest'){
                            //         that.$constant.notificationArr[i].close();
                            //         that.$constant.notificationArr.splice(i,1);
                            //         break;
                            //     }
                            // }
                            //
                            // let url = that.getWarningCountUrl;
                            // let data = {
                            //     warnIndex: 2
                            // };
                            // that.apiPost(url, data).then((res) => {
                            //     let count = res.data;
                            //     let offlineBox = that.$notify({
                            //         title: '离线',
                            //         type: 'warning',
                            //         message: "您有" + count + "条离线信息未处理，请及时查看",
                            //         position: 'bottom-right',
                            //         duration: 0,
                            //         customClass: 'offlineTest',
                            //         onClick: function () {
                            //             localStorage.setItem('offlinePage', 0);
                            //             that.$router.push('/off_LineLog');
                            //             that.reload();
                            //         }
                            //     });
                            //     that.$constant.notificationArr.push(offlineBox);
                            //     //localStorage.setItem('notification',JSON.stringify(that.notificationArr));
                            //     //更新报警数
                            //     bus.$emit("updateAlarmNum");
                            //         localStorage.setItem('newWarn', 'offline');
                            // }, (err) => {
                            //
                            // });

                        }
                        else {
                            for (let i = 0;i< that.$constant.notificationArr.length;i++ ) {
                                if(that.$constant.notificationArr[i].customClass == 'faultTest'){
                                    that.$constant.notificationArr[i].close();
                                    that.$constant.notificationArr.splice(i,1);
                                    break;
                                }
                            }
                            let url = that.getWarningCountUrl;
                            let data = {
                                warnIndex: 1
                            };
                            that.apiPost(url, data).then((res) => {
                                let count = res.data;
                                let faultBox = that.$notify({
                                    title: '故障 ',
                                    type: 'warning',
                                    message: "您有" + count + "条故障信息未处理，请及时查看",
                                    position: 'bottom-right',
                                    duration: 0,
                                    customClass: 'faultTest',
                                    onClick: function () {
                                        localStorage.setItem('faultPage', 0);
                                        that.$router.push('/faultLog');
                                        that.reload();
                                    },
                                })
                                that.$constant.notificationArr.push(faultBox);
                                //更新报警数
                                bus.$emit("updateAlarmNum");
                                localStorage.setItem('newWarn', 'fault');

                            }, (err) => {

                            });
                        }
                    }
                    //终端报警、故障、离线
                    else if (msg == "equipAlarm") {
                        let warnType = messageJson.warnType ? messageJson.warnType : "报警";
                        let alarmArr = ['电弧预警',"A相电流报警","B相电流报警","C相电流报警","A相电压报警","B相电压报警",
                            "C相电压报警","0路温度报警","1路温度报警","2路温度报警","3路温度报警","4路温度报警","漏电报警","门禁报警"];
                        if (alarmArr.indexOf(warnType) != -1) {
                            for (let i = 0;i< that.$constant.notificationArr.length;i++ ) {
                                if(that.$constant.notificationArr[i].customClass == 'alertTest'){
                                    that.$constant.notificationArr[i].close();
                                    that.$constant.notificationArr.splice(i,1);
                                    break;
                                }
                            }
                            let url = that.getWarningCountUrl;
                            let data = {
                                warnIndex: 0
                            };
                            that.apiPost(url, data).then((res) => {
                                let count = res.data;
                                let alarmBox = that.$notify({
                                    title: '报警 ',
                                    type: 'warning',
                                    message: "您有" + count + "条报警信息未处理，请及时查看",
                                    position: 'bottom-right',
                                    duration: 0,
                                    customClass: 'alertTest',
                                    onClick: function () {
                                        that.$router.push('/alarmLog');
                                        that.reload();
                                    },
                                });
                                that.$constant.notificationArr.push(alarmBox);
                                console.log(that.notificationArr.length)
                                bus.$emit("updateAlarmNum");
                                localStorage.setItem('newWarn', 'alarm');
                            }, (err) => {

                            });
                        }
                        else if (warnType == "超温报警") {
                            for (let i = 0;i< that.$constant.notificationArr.length;i++ ) {
                                if(that.$constant.notificationArr[i].customClass == 'faultTest'){
                                    that.$constant.notificationArr[i].close();
                                    that.$constant.notificationArr.splice(i,1);
                                    break;
                                }
                            }

                            let url = that.getWarningCountUrl;
                            let data = {
                                warnIndex: 1
                            };
                            that.apiPost(url, data).then((res) => {
                                let count = res.data;
                                let faultBox = that.$notify({
                                    title: '故障 ',
                                    type: 'warning',
                                    message: "您有" + count + "条故障信息未处理，请及时查看",
                                    position: 'bottom-right',
                                    duration: 0,
                                    customClass: 'faultTest',
                                    onClick: function () {
                                        localStorage.setItem('faultPage', 1);
                                        that.$router.push('/faultLog');
                                        that.reload();
                                    },
                                });
                                that.$constant.notificationArr.push(faultBox);
                                bus.$emit("updateAlarmNum");
                                localStorage.setItem('newWarn', 'fault');
                            },(err) => {

                            });

                        }
                        // else {
                        //
                        //     for (let i = 0;i< that.$constant.notificationArr.length;i++ ) {
                        //         console.log(i)
                        //         if(that.$constant.notificationArr[i].customClass == 'offlineTest'){
                        //             console.log("cc")
                        //             that.$constant.notificationArr[i].close();
                        //             that.$constant.notificationArr.splice(i,1);
                        //             break;
                        //         }
                        //     }
                        //
                        //     let url = that.getWarningCountUrl;
                        //     let data = {
                        //         warnIndex: 2
                        //     };
                        //     that.apiPost(url, data).then((res) => {
                        //         let count = res.data;
                        //         let offlineBox = that.$notify({
                        //             title: '离线 ',
                        //             type: 'warning',
                        //             message: "您有" + count + "条离线信息未处理，请及时查看",
                        //             position: 'bottom-right',
                        //             duration: 0,
                        //             customClass: 'offlineTest',
                        //             onClick: function () {
                        //                 localStorage.setItem('offlinePage', 1);
                        //                 that.$router.push('/off_LineLog');
                        //                 that.reload();
                        //             },
                        //         });
                        //         that.$constant.notificationArr.push(offlineBox);
                        //         bus.$emit("updateAlarmNum");
                        //             localStorage.setItem('newWarn', 'offline');
                        //     },(err) => {
                        //
                        //     });
                        // }
                    }
                    else if(msg == "replySet"){
                        let efmId = messageJson.efmId;
                        let cmd = messageJson.cmd;
                        let efmId0 = localStorage.getItem('efmId');
                        let cmd0 = localStorage.getItem('cmd');
                        //消音
                        if(efmId == efmId0 && cmd == cmd0 && (cmd == 'udp_clear_voice' || cmd == 'udp_clear_broadcast_voice')){
                            that.$commonFn.showTip("消音成功", 1);
                            localStorage.removeItem("efmId");
                            localStorage.removeItem("cmd");
                            clearTimeout(that.$constant.setCmdTime);
                        }
                        //设置描述符
                        else if(efmId == efmId0 && cmd == cmd0 && cmd == 'udp_set_specificator'){
                            that.$commonFn.showTip("设置成功", 1);
                            localStorage.removeItem("efmId");
                            localStorage.removeItem("cmd");
                        }
                    }
                    //告警复位更新头部报警数
                    else if(msg == "deviceReset"){
                        bus.$emit("updateAlarmNum");
                    }
                }
            },
            //发送消息
            WebSocketsend(msg) {
                this.postValue.id = this.userID;
                this.postValue.message = msg;
                this.websocket.send(JSON.stringify(this.postValue));
            },
            //关闭连接
            closeWebSocket() {
                if (this.websocket != null) {
                    this.websocket.close();
                }
            },
        },
        mixins: [http]
    }
</script>
<style>
    .main-box {
        position: absolute;
        left: 184px;
        transition: left 0.35s linear 0s, width 0.4s linear 0s;
        -webkit-transition: left 0.35s linear 0s, width 0.4s linear 0s; /* Safari 和 Chrome */
        -moz-transition: left 0.35s linear 0s, width 0.4s linear 0s; /* Firefox 4 */
        -o-transition: left 0.35s linear 0s, width 0.4s linear 0s; /* Opera */
        width: calc(100% - 184px);
        height: calc(100% - 64px);
    }

    .BMap_stdMpCtrl {
        left: 20px !important;
    }
</style>
