<template>
    <div style=" width:100%; height:100%; background-color: white; overflow: auto; ">

        <!--背景-->
        <div style="width:100%; height:100%; background-color: cyan; position: absolute; z-index: 9; float: left; ">
            <img src="../../resource/img/big-bg.jpg" width="100%" height="100%">
        </div>
        <!--头部-->
        <div style="width:100%; height:22%; position: absolute; z-index: 10;">
            <el-button class="v-1-0-0-Button" @click="backToControl"
                       style="position: absolute; margin-left: 1%; margin-top: 5px;">
                <i class="el-icon-back"> 返回系统</i>
            </el-button>
            <img src="../../resource/img/big-bg-header.png" width="100%" height="100%">
        </div>
        <!--logo-->
        <div style="width:26.5%; height:8%; position: absolute; z-index: 11; left: 36.7%; top: 0.4%; ">
            <div style="width:30px; height:35px; position: absolute; z-index: 12;top:10%;
          float: left;">
                <img src="../../resource/img/logo5.png" width="100%" height="100%">
            </div>

            <P style="position: absolute;line-height: 60px;letter-spacing:2px;font-size: 1.5rem;
            margin-left:12.5%;margin-top: -2px; color: white;">
                物联大数据管理平台
            </P>
        </div>

        <div style="color:rgba(195,225,238,0.5); font-weight: 500; width: 300px; height: 23px;
            float: left; position: absolute; top:6%; left:1%; z-index: 11;font-size: 16px; ">
            分布地图 <span style="font-size: 12px;font-weight: 500;">Distribution map</span>
        </div>

        <!--地图-->
        <div style="width:71.5%; height:88.1%;background-color: rgba(2,159,212,0.4); float: left; position: absolute; z-index: 9;
        margin-left: 1%; margin-top: 5.05%;border: 1px solid rgba(27,92,158,0.4);  ">

            <div style="color:rgb(170,170,170);  float:left; font-size: 17px; margin:0% auto auto 0%;
                     height:100%;width:100%;text-align: center; background-color: rgba(56,159,252,0.4); ">
                <div style="width:100%; height:100%;">
                    <div style="background-color: #000"></div>
                    <div id="largeSizeMap" style="width: 100%;height: 100%;"></div>
                </div>
            </div>

        </div>

        <div style="width:25%; height:32%; float: left; position: absolute; z-index: 10;
        margin-left: 2%; margin-top: 6%; ">

            <div style="color:rgb(170,170,170); width:calc(46% - 20px); float:left; font-size: 17px; margin:3% auto auto 3%;
                     height:40%;background-color: rgba(190,148,81,0.7);padding-left: 20px" class="item-first">
                <div style="font-size: 14px; margin-top:8%; height: 30px;color:white">项目数</div>
                <i class="okay" style="font-size: 20px;color: white;">{{countData.siteCount}}</i>
            </div>

            <div style="color:rgb(170,170,170); width:calc(46% - 20px); float:left; font-size: 17px; margin:3% auto auto 3%;
                     height:40%;background-color: rgba(35,121,193,0.7);padding-left: 20px " class="item-second">
                <div style="font-size: 14px; margin-top:8%; height: 30px;color:#ffffff;">设备数</div>
                <i class="okay" style="font-size: 20px;color: white;">{{countData.deviceCountOfEquip+countData.arcCountOfEquip+countData.currentCountOfEquip}}</i>
            </div>

            <div style="color:rgb(170,170,170); width:calc(46% - 20px); float:left; font-size: 17px; margin:3% auto auto 3%;
                     height:40%;background-color: rgba(31,52,67,0.7);padding-left: 20px " class="item-three">
                <div style="font-size: 14px; margin-top:8%; height: 30px;color:white">离线设备数</div>
                <i class="okay" style="font-size: 20px;color: white;">{{countData.deviceCountOfEquipOffLine+countData.arcCountOfEquipOffLine+countData.currentCountOfEquipOffLine}}</i>
            </div>

            <div style="color:rgb(170,170,170); width:calc(46% - 20px); float:left; font-size: 17px; margin:3% auto auto 3%;
                     height:40%;background-color: rgba(193,98,97,0.7);padding-left: 20px " class="item-four">
                <div style="font-size: 14px; margin-top:8%; height: 30px;color:white">报警数</div>
                <i class="okay" style="font-size: 20px;color: white;">{{countData.deviceCountOfEquipAlarm+countData.arcCountOfEquipAlarm+countData.currentCountOfEquipAlarm}}</i>
            </div>

        </div>

        <div style="width:26%; height:88%; float: right; position: absolute; z-index: 10;
        top:6.2%; right:1%;">

            <div style="color:rgba(195,225,238,0.5); font-weight: 500; width: 300px; height: 21px;z-index: 11;font-size: 16px;
            float: left; position: absolute; top:1%; left:3%;">
                设备区域分布 <span style="font-size: 12px;font-weight: 400;">Regional distribution</span>
            </div>

            <div style="color:rgb(170,170,170); width:94%; float:left; font-size: 17px; margin:8.3% auto auto 3%;
           height:29.4%;text-align: center; background-color: rgba(4,64,97,0.4);border: 1px solid rgba(27,92,158,0.4); ">
                <!--设备区域分布-->
                <div style="width:95%; height: 118%; margin-left: 5px; margin-top: -12%;">
                    <div id="equipmentArea" :style="{width: '100%', height: '100% '}">
                    </div>
                </div>
            </div>

            <div style="color:rgba(195,225,238,0.5); font-weight: 500; width: 300px; height: 21px;z-index: 11;font-size: 16px;
            float: left; position: absolute; top:36.5%; left:3%;">
                建筑类型 <span style="font-size: 12px;font-weight: 400;">Building Type</span>
            </div>

            <div style="color:rgba(195,225,238,0.5); width:94%; float:left; font-size: 16px; margin:9% auto auto 3%;
           height:29.4%;text-align: center; background-color: rgba(4,64,97,0.4);border: 1px solid rgba(27,92,158,0.4); ">
                <!--建筑类型柱状图-->
                <div style="width:95%; height:118%; margin-left: 5px; margin-top: -12%;">
                    <div id="architectureType" :style="{width: '100%', height: '100%'}"></div>
                </div>
            </div>

            <div style="color:rgba(195,225,238,0.5); font-weight: 500; width: 300px; height: 21px;z-index: 11;font-size: 16px;
            float: left; position: absolute; top:71.5%; left:3%;">
                设备状态 <span style="font-size: 12px;font-weight: 400;">Failure rate</span>
            </div>

            <div style="color:rgb(170,170,170); width:94%; float:left; font-size: 17px; margin:9% auto auto 3%;
           height:29.4%;text-align: center; background-color: rgba(4,64,97,0.4);border: 1px solid rgba(27,92,158,0.4); ">
                <!--设备状态-->
                <div style="width:110%; height:99%; float:left;margin-left:8%; margin-top:1.5%;">
                    <div id="stateRate" :style="{width: '100%', height: '100%'}"></div>
                </div>
            </div>

        </div>

    </div>
</template>

<script>
    import http from '../../common/http';

    export default {
        name: "largeSizePage",
        data() {
            return {
                countData: {},
                //设备状态相关
                stateRate: {},
                stateData: {},
                stateOption: {},
                //建筑类型相关
                architectureType: {},
                architectureTypeData: [],
                architectureOption: {},
                //建筑类型名字
                buildName: [],
                //建筑类型数量
                equipCount: [],
                //地区分布名字
                areaName: [],
                //地区分布数值
                areaCount: [],
                largeSizeMap: {},
                largeSizeMapData: {},
                largeSizeMapOption: {},
                siteColor: 'red',
                location: [],
                geoCoordMap: {
                    '上海': [121.487899, 31.249162],
                    '云南': [101.592952, 24.864213],
                    '内蒙古': [114.415868, 43.468238],
                    '北京': [116.395645, 39.929986],
                    '台湾': [121.973871, 24.086957],
                    '吉林': [126.262876, 43.678846],
                    '四川': [102.89916, 30.367481],
                    '天津': [117.210813, 39.14393],
                    '宁夏': [106.155481, 37.321323],
                    '安徽': [117.216005, 31.859252],
                    '山东': [118.527663, 36.09929],
                    '山西': [112.515496, 37.866566],
                    '广东': [113.394818, 23.408004],
                    '广西': [108.924274, 23.552255],
                    '新疆': [85.614899, 42.127001],
                    '江苏': [119.368489, 33.013797],
                    '江西': [115.676082, 27.757258],
                    '河北': [115.661434, 38.61384],
                    '河南': [113.486804, 34.157184],
                    '浙江': [119.957202, 29.159494],
                    '海南': [109.733755, 19.180501],
                    '湖北': [112.410562, 31.209316],
                    '湖南': [111.720664, 27.695864],
                    '澳门': [113.557519, 22.204118],
                    '甘肃': [102.457625, 38.103267],
                    '福建': [117.984943, 26.050118],
                    '西藏': [89.137982, 31.367315],
                    '贵州': [106.734996, 26.902826],
                    '辽宁': [122.753592, 41.6216],
                    '重庆': [106.530635, 29.544606],
                    '陕西': [109.503789, 35.860026],
                    '青海': [96.202544, 35.499761],
                    '香港': [114.186124, 22.293586],
                    '黑龙江': [128.047414, 47.356592],
                    '台湾': [121.300212, 23.9],
                },

            }
        },
        created: function () {
            this.userInfo = JSON.parse(sessionStorage.getItem('user_info'));
            this.getMain();
        },
        mounted() {
            this.init();
            this.drawLine();
        },

        methods: {
            getMain() {
                let _loading = this.$commonFn.showLoading(2, '.main-box');
                let url = this.getIndexUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    this.provinceList = res.data.provinceList;
                    this.countData = res.data.countData;
                }, (err) => {
                    _loading.close();
                });
            },
            init() {
                let url = this.statisticDataUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    //离线数
                    this.stateData.equipCountOffLine = res.data.statisticsData.equipCountOffLine;
                    //报警数
                    this.stateData.equipCountAlarm = res.data.statisticsData.equipCountAlarm;
                    //故障数
                    this.stateData.equipCountFault = res.data.statisticsData.equipCountFault;
                    //设备数
                    this.stateData.deviceCount = res.data.statisticsData.equipCount;
                    this.safeData = ((this.stateData.equipCountAlarm / (this.stateData.deviceCount)).toFixed(2)) * 100;
                    //按照建筑类型分类的数据
                    this.architectureTypeData = res.data.statisticsData.buildCount;
                    for (let i = 0; i < this.architectureTypeData.length; i++) {
                        this.buildName.push(this.architectureTypeData[i].buildName);
                        this.equipCount.push(this.architectureTypeData[i].equipCount);
                    }

                    this.largeSizeMapData = res.data.statisticsData.provinceCount;
                    let myGeo = new BMap.Geocoder();
                    for (let i = 0; i < this.largeSizeMapData.length; i++) {
                        let formatName = {
                            "宁夏回族自治区": "宁夏",
                            "内蒙古自治区": "内蒙古",
                            "广西壮族自治区": "广西",
                            "西藏自治区": "西藏",
                            "内蒙古自治区": "内蒙古",
                            "新疆维吾尔自治区": "新疆",
                            "香港特别行政区": "香港",
                            "澳门特别行政区": "澳门",
                            "黑龙江": "黑龙江",
                            "台湾": "台湾",
                        };
                        if (formatName[this.largeSizeMapData[i].provinceName]) {
                            this.largeSizeMapData[i].provinceName = formatName[this.largeSizeMapData[i].provinceName];
                        }
                        else{
                            console.log(this.largeSizeMapData[i].provinceName.length)
                            this.largeSizeMapData[i].provinceName = this.largeSizeMapData[i].provinceName.substring(0,(this.largeSizeMapData[i].provinceName.length)-1);
                        }
                        this.location.push({
                            name: this.largeSizeMapData[i].provinceName,
                            value: this.largeSizeMapData[i].provinceCount + 1
                        });
                    }
                    //地区分配数据
                    let otherCount = 0;
                    if (this.userInfo.roleType == 0) {
                        this.equipmentAreaData = res.data.statisticsData.provinceCount;
                    }
                    else {
                        this.equipmentAreaData = res.data.statisticsData.areaCount;
                    }
                    for (let i = 0; i < this.equipmentAreaData.length; i++) {
                        if (this.userInfo.roleType == 0) {
                            if (i < 5) {
                                let formatName = {
                                    "宁夏回族自治区": "宁夏",
                                    "内蒙古自治区": "内蒙古",
                                    "广西壮族自治区": "广西",
                                    "西藏自治区": "西藏",
                                    "内蒙古自治区": "内蒙古",
                                    "新疆维吾尔自治区": "新疆",
                                    "香港特别行政区": "香港",
                                    "澳门特别行政区": "澳门",
                                    "黑龙江": "黑龙江",
                                    "台湾": "台湾",
                                };
                                if (formatName[this.equipmentAreaData[i].provinceName]) {
                                    this.equipmentAreaData[i].provinceName = formatName[this.equipmentAreaData[i].provinceName];
                                }

                                this.areaName.push(this.equipmentAreaData[i].provinceName);
                                if (!this.equipmentAreaData[i].provinceCount && this.equipmentAreaData[i].provinceCount != 0) {
                                    this.equipmentAreaData[i].provinceCount = 0;
                                }
                                this.areaCount.push(this.equipmentAreaData[i].provinceCount)
                            }
                            else {
                                if (!this.equipmentAreaData[i].provinceCount && this.equipmentAreaData[i].provinceCount != 0) {
                                    this.equipmentAreaData[i].provinceCount = 0;
                                }
                                otherCount = otherCount + parseInt(this.equipmentAreaData[i].provinceCount);
                                if (i == 5) {
                                    this.areaName.push("其它");
                                }
                            }
                        }
                        else {
                            if (i < 5) {
                                this.areaName.push(this.equipmentAreaData[i].areaName);
                                if (!this.equipmentAreaData[i].areaCount && this.equipmentAreaData[i].areaCount != 0) {
                                    this.equipmentAreaData[i].areaCount = 0;
                                }
                                this.areaCount.push(this.equipmentAreaData[i].areaCount)
                            }
                            else {
                                if (!this.equipmentAreaData[i].areaCount && this.equipmentAreaData[i].areaCount != 0) {
                                    this.equipmentAreaData[i].areaCount = 0;
                                }
                                otherCount = otherCount + parseInt(this.equipmentAreaData[i].areaCount);
                                if (i == 5) {
                                    this.areaName.push("其它");
                                }
                            }
                        }

                        this.areaCount.push(otherCount);
                    }
                    this.stateOption = {
                        textStyle: {
                            color: '#fff',
                            size: '3',
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)"
                        },
                        legend: {
                            textStyle: {
                                color: '#fff',
                                size: '3',
                            },
                            orient: 'vertical',
                            x: 'left',
                            data: ['报警率', '离线率', '故障率']
                        },
                        series: [
                            {
                                name: '设备状态',
                                type: 'pie',
                                radius: ['50%', '70%'],
                                avoidLabelOverlap: false,
                                label: {
                                    normal: {
                                        show: false,
                                        position: 'center'
                                    },
                                    emphasis: {
                                        show: true,
                                        textStyle: {
                                            fontSize: '15',
                                            fontWeight: 'bold'
                                        }
                                    }
                                },
                                labelLine: {
                                    normal: {
                                        show: false
                                    }
                                },
                                data: [
                                    {value: this.stateData.equipCountAlarm, name: '报警率'},
                                    {value: this.stateData.equipCountOffLine, name: '离线率'},
                                    {value: this.stateData.equipCountFault, name: '故障率'}
                                ]
                            }
                        ]
                    };
                    this.stateRate.setOption(this.stateOption);
                    this.architectureOption = {
                        textStyle: {
                            color: '#fff',
                            size: '3',
                        },
                        color: ['skyblue'],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {              // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: this.buildName,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLabel: {
                                    color: '#fff',
                                    interval: 0,

                                },
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value'
                            }
                        ],
                        series: [
                            {
                                name: '数量',
                                type: 'bar',
                                barWidth: '60%',
                                data: this.equipCount,
                                itemStyle: {
                                    normal: {
                                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                            offset: 0,
                                            color: 'rgba(52,224,240,1)'
                                        }, {
                                            offset: 1,
                                            color: 'rgba(52,224,240,0)'
                                        }]),
                                        shadowColor: 'rgba(0, 0, 0, 0.4)',
                                        shadowBlur: 20
                                    }
                                }
                            }
                        ]
                    };
                    /*建筑类型*/
                    this.architectureType.setOption(this.architectureOption);
                    this.equipmentAreaOption = {
                        textStyle: {
                            color: '#fff'
                        },
                        color: ['rgba(218,211,17,0.8)'],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {              // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: this.areaName,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLabel: {
                                    color: '#fff',
                                    interval: 0,

                                },
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value'
                            }
                        ],
                        series: [
                            {
                                name: '数量',
                                type: 'bar',
                                barWidth: '60%',
                                data: this.areaCount,
                                itemStyle: {
                                    normal: {
                                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                            offset: 0,
                                            color: 'rgba(214,208,18,1)'
                                        }, {
                                            offset: 1,
                                            color: 'rgba(214,208,18,0)'
                                        }]),
                                        shadowColor: 'rgba(0, 0, 0, 0.4)',
                                        shadowBlur: 20
                                    }
                                }

                            }
                        ]
                    };
                    /*设备区域分布*/
                    this.equipmentArea.setOption(this.equipmentAreaOption);
                    this.largeSizeMapOption = {
                        tooltip: {
                            trigger: 'item',
                            formatter: function (params) {
                                let count = params.data.value[2] - 1;
                                let info = '<p style="font-size:14px">' + params.name + '</p><span style="font-size:14px">' + params.seriesName + ':' + count + '</span>'

                                return info;
                            },

                        },
                        legend:{

                            y: 'top',
                            x:'right',
                            data:['111111111'],
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        bmap: {
                            center: [99.114129, 36.550339],
                            zoom: 5,
                            roam: true,
                            mapStyle: {
                                styleJson:
                                    [
                                        {
                                            "featureType": "land",
                                            "elementType": "all",
                                            "stylers": {
                                                "color": "#065d8aff"
                                            }
                                        },
                                        {
                                            "featureType": "boundary",
                                            "elementType": "all",
                                            "stylers": {
                                                "color": "#6ba5e7ff"
                                            }
                                        },
                                        {
                                            "featureType": "city",
                                            "elementType": "labels",
                                            "stylers": {
                                                "color": "#ffffffff",
                                                "hue": "#000000",
                                                "weight": "0.1",
                                                "lightness": 31,
                                                "saturation": -100
                                            }
                                        },
                                        {
                                            "featureType": "highway",
                                            "elementType": "all",
                                            "stylers": {
                                                "visibility": "off"
                                            }
                                        },
                                        {
                                            "featureType": "continent",
                                            "elementType": "all",
                                            "stylers": {
                                                "visibility": "off"
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
                                            "featureType": "continent",
                                            "elementType": "all",
                                            "stylers": {
                                                "visibility": "off"
                                            }
                                        },
                                        {
                                            "featureType": "water",
                                            "elementType": "all",
                                            "stylers": {
                                                "color": "#074a7bff"
                                            }
                                        },
                                        {
                                            "featureType": "local",
                                            "elementType": "all",
                                            "stylers": {
                                                "visibility": "off"
                                            }
                                        },
                                        {
                                            "featureType": "arterial",
                                            "elementType": "all",
                                            "stylers": {
                                                "visibility": "off"
                                            }
                                        },
                                        {
                                            "featureType": "highway",
                                            "elementType": "all",
                                            "stylers": {
                                                "visibility": "off"
                                            }
                                        },
                                        {
                                            "featureType": "city",
                                            "elementType": "all",
                                            "stylers": {
                                                "visibility": "off"
                                            }
                                        }
                                    ]
                            }
                        },
                        series: [
                            {
                                name: '数量',
                                type: 'scatter',
                                mapType: 'china',
                                coordinateSystem: 'bmap',
                                data: this.convertData(this.location),
                                symbolSize: function (val) {
                                    console.log(val)
                                    let a = val[2];
                                    if(a < 20 && a > 1){
                                        a = 20;
                                    }
                                    return a/2;
                                },
                                showEffectOn: 'render',
                                label: {
                                    normal: {
                                        formatter: '{b}',
                                        position: 'right',
                                        show: true,
                                        color:'#fff'
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: '#83ab47',
                                        shadowBlur: 4,
                                        shadowColor: '#83ab47',

                                    }
                                },

                            },
                        ],

                    };

                    this.largeSizeMap.setOption(this.largeSizeMapOption);
                }, (err) => {

                });
            },
            backToControl() {
                this.$router.push('/main');
            },
            convertData(msg) {
                var res = [];
                for (var i = 0; i < msg.length; i++) {
                    var geoCoord = this.geoCoordMap[msg[i].name];
                    if (geoCoord) {
                        res.push({
                            name: msg[i].name,
                            value: geoCoord.concat(msg[i].value)
                        });
                    }
                }
                return res;
            },
            drawLine() {
                this.largeSizeMap = echarts.init(document.getElementById('largeSizeMap'));
                this.stateRate = this.$echarts.init(document.getElementById('stateRate'));
                this.architectureType = this.$echarts.init(document.getElementById('architectureType'));
                this.equipmentArea = this.$echarts.init(document.getElementById('equipmentArea'));
            }
        },
        mixins: [http]
    }
</script>

<style scoped>
    .item-first {
        background: linear-gradient(to right, rgba(235, 178, 81, 0.8), rgba(236, 135, 61, 0.8)); /* 标准的语法 */
    }

    .item-second {
        background: linear-gradient(to right, rgba(87, 148, 195, 0.8), rgba(14, 132, 226, 0.8)); /* 标准的语法 */
    }

    .item-three {
        background: linear-gradient(to right, rgba(47, 69, 81, 0.8), rgba(35, 48, 55, 0.8)); /* 标准的语法 */
    }

    .item-four {
        background: linear-gradient(to right, rgba(246, 122, 109, 0.8), rgba(235, 96, 81, 0.8)); /* 标准的语法 */
    }
    .anchorBL{
        display:none !important;
    }
</style>
