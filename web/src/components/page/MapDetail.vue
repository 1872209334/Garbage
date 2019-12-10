<template>
    <div>
        <div id="main_map" class='main_map' ref="mainMap"></div>
    </div>
</template>

<script>
    import http from '../../common/http';

    export default {
        name: "MapDetail",

        data() {
            return {
                userID: 'admin_',
                websocket: null,
                postValue: {},
                // tagsList: [],
                map: {},
                mapPoint: {},
                mapMarker: {},
                canvas: {},
                collapse: false,
                isShow: false,
                isTable: true,
                loc_Icon: {},
                loc_marker: {},
                loc_Con: {},
                loc_infowindow: {},
                loc_Icon1: {},
                loc_marker1: {},
                efmList:[],
                mqttData:[],
                mqttList:[],
                mqttDataMarker:[],
                mqttListMarker:[],
                infoBox:'',
                styleJson:[
                    {
                        "featureType": "road",
                        "elementType": "all",
                        "stylers": {
                            "lightness": 20
                        }
                    },
                    {
                        "featureType": "highway",
                        "elementType": "geometry",
                        "stylers": {
                            "color": "#f49935"
                        }
                    },
                    {
                        "featureType": "railway",
                        "elementType": "all",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "local",
                        "elementType": "labels",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "water",
                        "elementType": "all",
                        "stylers": {
                            "color": "#d1e5ff"
                        }
                    },
                    {
                        "featureType": "poi",
                        "elementType": "labels",
                        "stylers": {
                            "visibility": "off"
                        }
                    }
                ]
            }
        },
        created: function () {
            //获取地图和中控信息
            this.getMapInfo();
        },
        methods: {
            //生成地图
            createMap() {
                this.map = new BMap.Map(this.$refs.mainMap); //创建Map实例
                this.map.addControl(new BMap.MapTypeControl({//添加地图类型控件
                    mapTypes: [
                        BMAP_NORMAL_MAP,
                        BMAP_HYBRID_MAP
                    ]
                }));
                this.map.addControl(new BMap.NavigationControl({type: BMAP_ANCHOR_TOP_RIGHT}));
                //this.map.setCurrentCity("北京");              //设置地图显示的城市 此项是必须设置的
                this.map.enableScrollWheelZoom(true);        //开启鼠标滚轮缩放

                //设置地图样式
                this.map.setMapStyle({
                    styleJson: this.styleJson
                })
                let that = this;
                this.map.addEventListener("zoomend",function (e) {
                    let zoomNum = that.map.getZoom();
                    if (zoomNum >= 17){
                        for (let i = 0;i < that.mqttDataMarker.length;i++){
                            that.map.removeOverlay(that.mqttDataMarker[i]);
                        }
                        for (let i = 0;i < that.mqttListMarker.length;i++){
                            that.map.removeOverlay(that.mqttListMarker[i]);
                        }
                        that.createMqttEquip(that.mqttList);
                    }
                    else{
                        for (let i = 0;i < that.mqttDataMarker.length;i++){
                            that.map.removeOverlay(that.mqttDataMarker[i]);
                        }
                        for (let i = 0;i < that.mqttListMarker.length;i++){
                            that.map.removeOverlay(that.mqttListMarker[i]);
                        }
                        that.createMqttPoint(that.mqttData);
                    }
                })
            },
            createMqttEquip(list){
                this.mqttListMarker = [];
                //设置标注的图标
                this.icon_normal = new BMap.Icon('https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location.png', new BMap.Size(40, 40));
                this.icon_alarm = new BMap.Icon('https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location_red.png', new BMap.Size(40, 40));
                //设置标注的经纬度
                for (let i = 0; i < list.length; i++) {
                    let lng = list[i].lngBmap;
                    let lat = list[i].latBmap;
                    // if(lng && lat){
                    //     this.map.centerAndZoom(new BMap.Point(lng, lat), 18);
                    // }
                    let efmMarker;
                    if(list[i].warnNo){
                        efmMarker = new BMap.Marker(new BMap.Point(lng, lat), {icon: this.icon_alarm});
                    }
                    else{
                        efmMarker = new BMap.Marker(new BMap.Point(lng, lat), {icon: this.icon_normal});
                    }
                    this.mqttListMarker.push(efmMarker);
                    this.map.addOverlay(efmMarker);
                }
            },
            getMapInfo() {
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getMapInfoUrl;
                let data = {
                    'siteCode':sessionStorage.getItem('siteCode')
                };
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if (res.status === 200) {
                         //mapSign为0代表显示默认地图，为1代表显示项目地图
                        if(res.data.mapSign==0) {
                            let efmList = res.data.getDeviceList;
                            this.createPoint(efmList);
                            this.getMapData();
                            this.getMapWireless();
                        }else {
                            this.createDevicePoint(res);
                        }
					}
                    else {
                        this.$commonFn.showTip(res.message, 3);
                    }
 
                }, (err) => {
                    _loading.close();
                });
            },
            getMapData(){
                let url = this.mapDataUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.mqttData = res.data.mapData;
                        this.createMqttPoint(this.mqttData);
                    }
                    else {
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {
                });
            },
            getMapWireless(){
                let url = this.mapWirelessUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    if (res.status === 200) {
                        this.mqttList = res.data.wirelessEquiplist;

                    }
                    else {
                        this.$commonFn.showTip(res.message, 3);
                    }
                }, (err) => {
                });
            },
            createMqttPoint(list) {
                this.mqttDataMarker = [];
                //设置标注的图标
                this.icon_normal = new BMap.Icon('https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location.png', new BMap.Size(40, 40));
                this.icon_alarm = new BMap.Icon('https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location_red.png', new BMap.Size(40, 40));
                let boxStyle = {
                    boxStyle: {
                        background: 'rgb(56,125,299)',
                        width: "140px",
                        height: '135px',
                        borderRadius: '5px',
                        marginBottom: '20px'
                    },
                    closeIconMargin: "10px 10px 0 0",
                    enableAutoPan: true,
                    closeIconUrl: "https://lys-arc.oss-cn-hangzhou.aliyuncs.com/close.png",
                    alignBottom: false
                };
                let infoBox = '';
                //设置标注的经纬度
                for (let i = 0; i < list.length; i++) {
                    let lng = list[i].lngBmap;
                    let lat = list[i].latBmap;
                    // if(lng && lat){
                    //     this.map.centerAndZoom(new BMap.Point(lng, lat), 14);
                    // }
                    let efmMarker;
                    if(list[i].countData.alarmCount > 0){
                        efmMarker = new BMap.Marker(new BMap.Point(lng, lat), {icon: this.icon_alarm});
                    }
                    else{
                        efmMarker = new BMap.Marker(new BMap.Point(lng, lat), {icon: this.icon_normal});
                    }
                    this.mqttDataMarker.push(efmMarker);
                    this.map.addOverlay(efmMarker);
                    efmMarker.addEventListener("mouseover", function () {
                        if(list[i].siteName){
                            list[i].siteLabel = list[i].siteName;
                        }
                        else{
                            list[i].siteLabel = list[i].siteCode;
                        }
                        let loc_Con = '<div style="color: #fff;margin: 12px 16px;font-size: 14px;"><h4 style="margin-bottom: 8px">'+list[i].siteLabel+'无线</h4>' +
                            '<div style="color: #b9d2f6"><span style="display: inline-block;margin-bottom: 2px">设备数</span><span style="float: right;display: inline-block;padding-top: 2px;color:#fff;font-weight: bold">'+list[i].countData.equipCount+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">报警数</span><span style="color: #ffa0a0;float: right;font-weight: bold">'+list[i].countData.alarmCount+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">故障数</span><span style="color: #ffa0a0;float: right;font-weight: bold">'+list[i].countData.faultCount+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">离线数</span><span style="float: right;font-weight: bold">'+list[i].countData.offlineCount+'</span></div></div>';
                        if (infoBox){
                            infoBox.close();
                        }
                        infoBox = new BMapLib.InfoBox(this.map, loc_Con, boxStyle);
                        infoBox.open(efmMarker);
                    });
                }
            },
            createPoint(list) {
                //初始化
                this.createMap();
                //设置标注的图标
                this.icon_normal = new BMap.Icon('https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location.png', new BMap.Size(40, 40));
                this.icon_alarm = new BMap.Icon('https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location_red.png', new BMap.Size(40, 40));
                let boxStyle = {
                    boxStyle: {
                        background: 'rgb(56,125,299)',
                        width: "140px",
                        height: '135px',
                        borderRadius: '5px',
                        marginBottom: '20px'
                    },
                    closeIconMargin: "10px 10px 0 0",
                    enableAutoPan: true,
                    closeIconUrl: "https://lys-arc.oss-cn-hangzhou.aliyuncs.com/close.png",
                    alignBottom: false
                };
                let infoBox = '';
                //设置标注的经纬度
                for (let i = 0; i < list.length; i++) {
                    let lng = list[i].longitute;
                    let lat = list[i].latitude;
                    if(lng && lat){
                        this.map.centerAndZoom(new BMap.Point(lng, lat), 14);
                    }
                    let efmMarker;
                    if(list[i].equipAlarmCount > 0){
                        efmMarker = new BMap.Marker(new BMap.Point(lng, lat), {icon: this.icon_alarm});
                    }
                    else{
                        efmMarker = new BMap.Marker(new BMap.Point(lng, lat), {icon: this.icon_normal});
                    }
                    this.map.addOverlay(efmMarker);
                    efmMarker.addEventListener("mouseover", function () {
                        if(list[i].niName){
                            list[i].efmLabel = list[i].niName;
                        }
                        else if(list[i].specificator){
                            list[i].efmLabel = list[i].specificator;
                        }
                        else{
                            list[i].efmLabel = parseInt(list[i].device_code,16);
                        }
                        let loc_Con = '<div style="color: #fff;margin: 12px 16px;font-size: 14px;"><h4 style="margin-bottom: 8px">'+list[i].efmLabel+'</h4>' +
                            '<div style="color: #b9d2f6"><span style="display: inline-block;margin-bottom: 2px">设备数</span><span style="float: right;display: inline-block;padding-top: 2px;color:#fff;font-weight: bold">'+list[i].equipCount+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">报警数</span><span style="color: #ffa0a0;float: right;font-weight: bold">'+list[i].equipAlarmCount+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">故障数</span><span style="color: #ffa0a0;float: right;font-weight: bold">'+list[i].equipFaultCount+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">离线数</span><span style="float: right;font-weight: bold">'+list[i].equipOffLineCount+'</span></div></div>';
                        if (infoBox){
                            infoBox.close();
                        }
                        infoBox = new BMapLib.InfoBox(this.map, loc_Con, boxStyle);
                        infoBox.open(efmMarker);
                    });
                }
            },
            createDevicePoint(res) {
                //设置标注的图标
                this.icon_normal = new BMap.Icon('http://www.toysoho.com/download/img/map_icon_location.png', new BMap.Size(40, 40));
                let boxStyle = {
                    boxStyle: {
                        background: 'rgb(56,125,299)',
                        width: "200px",
                        height: '100px',
                        borderRadius: '5px',
                        marginBottom: '20px'
                    },
                    closeIconMargin: "10px 10px 0 0",
                    enableAutoPan: true,
                    closeIconUrl: "http://www.toysoho.com/download/img/close.png",
                    alignBottom: false
                };
                let latitude=res.data.latitude;
                let longitute=res.data.longitute;
                let mapNum=res.data.mapNum;

                // 百度地图API功能
                this.map = new BMap.Map(this.$refs.mainMap,{enableMapClick:false}); //创建Map实例
                // 百度地图API功能
                //let top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
                let top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
                this.map.addControl(top_left_control);
                //this.map.addControl(top_left_navigation);
                console.log("地图级别:"+mapNum);
                this.map.centerAndZoom(new BMap.Point(longitute,latitude),mapNum);  // 初始化地图,设置中心点坐标和地图级别
                //this.map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
                this.map.clearOverlays();
                this.map.disableDragging();//禁用拖动
                // 创建室内图实例
                let indoorManager = new BMapLib.IndoorManager(this.map);
                //设置标注的经纬度
                let list=res.data.getDeviceList;
                let infoBox = '';
                let typeArr = ['电弧探测器','组合式探测器','测温式探测器'];
                for (let i = 0; i < list.length; i++) {
                    let lng = list[i].lngBmap;
                    let lat = list[i].latBmap;
                    console.log("经度："+lng+";维度："+lat);
                    let efmMarker = new BMap.Marker(new BMap.Point(lng, lat),{icon: this.icon_normal});  // 创建标注
                    this.map.addOverlay(efmMarker); // 将标注添加到地图中
                    efmMarker.addEventListener("mouseover", function () {
                        let efmLabel ='无线终端';
                        let serialNumber=parseInt(list[i].line_code) + 1 + "-" + parseInt(list[i].addr,10);
                        let type = typeArr[list[i].type];

                        let loc_Con ='<div style="color: #fff;margin: 12px 16px;font-size: 14px;"><div style="color: #b9d2f6"><span style="display: inline-block;margin-bottom: 2px">设备名称：</span><span style="color: #ffa0a0;float: right;font-weight: bold">'+efmLabel+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">设备编号：</span><span style="color: #ffa0a0;float: right;font-weight: bold">'+serialNumber+'</span><br/>' +
                            '<span style="display: inline-block;margin-bottom: 2px">设备类型：</span><span style="color: #ffa0a0;float: right;font-weight: bold">'+type+'</span></div></div>';
                        if (infoBox){
                            infoBox.close();
                        }
                        infoBox = new BMapLib.InfoBox(this.map, loc_Con, boxStyle);
                        infoBox.open(efmMarker);
                    });
                }
                // indoorManager.hideBaseMap();
            }
        },
        mounted() {
            //this.createMap();
        },
        mixins: [http]
    }
</script>

<style scoped>

</style>
