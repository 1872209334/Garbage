<template>
    <div style=" width:100%; height:100%; background-color: #F0F2F5; overflow:auto;">
        <div style="width:96%; height:calc(100% - 40px);margin: 20px auto 20px auto;">
            <!--故障率饼图-->
            <div style="width:32%; height:48%; float:left; background-color: white; margin-right: 1.8%;">
                <div style="width:100%; height: 96%;padding: 2% ;position: relative">
                    <!--<el-button class="v-1-0-0-Button" style=" position: absolute;right: 10px;top: 15px;z-index: 2" @click="exportAlarm">导出</el-button>-->
                    <div id="stateRate" class="stateRate" style="margin-left: 0; width: 100%;height: 100%"></div>
                </div>
            </div>

            <!--安全测评饼图-->
            <div style="width:32%; height:48%; margin-right: 1.8%; background-color: white; float:left;">
                <div style="width:96%; height: 100%;padding: 2%;position: relative">
                    <!--<el-button class="v-1-0-0-Button" style=" position: absolute;right: 30px;top: 16px;z-index: 2" @click="exportAlarm">导出</el-button>-->
                    <div id="safeScore" style="width: 100%;height: 100%"></div>
                </div>
            </div>

            <!--建筑类型柱状图-->
            <div style="width:32%; height:48%; background-color: white; float: left;">
                <div style="width:90%; height: 92%;padding: 2%;position: relative">
                    <!--<el-button class="v-1-0-0-Button" style="position: absolute;right: 0px;top: 10px;z-index: 2" @click="exportAlarm">导出</el-button>-->
                    <div id="architectureType" style="width: 100%;height: 100%"></div>
                </div>
            </div>

            <!--设备区域分布柱状图div-->
            <div v-if="userInfo.roleType != 2" style=" width:99.5%; height:48%;margin:2% auto 0 auto; background-color: white; float: left;position: relative">
                <!--<el-button class="v-1-0-0-Button" style="position: absolute;right: 50px;top: 16px;z-index: 2" @click="exportAlarm">导出</el-button>-->
                <div style="width:200px; margin:2% auto auto 32px; float: left; z-index: 15; font-size: 18px; font-weight: 600;">
                    区域分布
                </div>

                <!--设备区域分布下拉框div-->
                <div v-if="userInfo.roleType == 0" style="width:200px;position: absolute;right: 120px; z-index: 2;top: 16px;">
                    <!--省份下拉框-->
                    <el-select v-model="provinceValue" placeholder="省份"  @change="changeProvince">

                        <el-option label="不限" :value="0">
                        </el-option>

                        <el-option v-for="item in provinceList" :key="item.provinceId" :label="item.provinceName" :value="item.provinceId">
                        </el-option>
                    </el-select>
                </div>

                <div style="width:100%; height:80%;">
                    <div id="equipmentArea" :style="{width: '100%', height: '100% '}">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import http from '../../common/http';
    export default {
        name: 'statisticalControl',
        data () {
            return {
                userInfo: {},
                provinceValue:"",
                value: '',
                //设备状态相关
                stateRate:{},
                stateData:{},
                stateOption:{},
                //安全评分相关
                safeScore:{},
                safeData:0,
                safeOption:{},
                //建筑类型相关
                architectureType:{},
                architectureTypeData:[],
                architectureOption:{},
                //建筑类型名字
                buildName:[],
                //建筑类型数量
                equipCount:[],
                //区域分布相关
                equipmentArea:{},
                equipmentAreaData: [],
                equipmentAreaOption:{},
                //地区分布名字
                areaName:[],
                //地区分布数值
                areaCount:[],
                safeRate:0,
                provinceList:[],
            }
        },
        created: function () {
            this.userInfo = JSON.parse(sessionStorage.getItem('user_info'));
            this.init();
        },
        mounted(){
            this.drawLine();
        },

        methods: {
            init(){
                let url = this.statisticDataUrl;
                let data = {};
                this.apiPost(url, data).then((res) => {
                    this.provinceList = res.data.provinceList;
                    //离线数
                    this.stateData.equipCountOffLine = res.data.statisticsData.equipCountOffLine;
                    //报警数
                    this.stateData.equipCountAlarm = res.data.statisticsData.equipCountAlarm;
                    //故障数
                    this.stateData.equipCountFault = res.data.statisticsData.equipCountFault;
                    //设备数
                    this.stateData.deviceCount = res.data.statisticsData.equipCount;
                    this.safeData = (((this.stateData.deviceCount - this.stateData.equipCountAlarm)/(this.stateData.deviceCount)).toFixed(2))*100;
                    if(this.stateData.deviceCount == 0){
                        this.safeData = 100;
                    }
                    //按照建筑类型分类的数据
                    this.architectureTypeData = res.data.statisticsData.buildCount;
                    for(let i = 0 ; i< this.architectureTypeData.length ; i++){
                        this.buildName.push(this.architectureTypeData[i].buildName);
                        this.equipCount.push(this.architectureTypeData[i].equipCount);
                    }
                    if(this.userInfo.roleType == 0){
                        this.equipmentAreaData = res.data.statisticsData.provinceCount;
                        let otherCount = 0;
                        for(let i = 0 ; i< this.equipmentAreaData.length ; i++){
                            // this.areaName.push(this.equipmentAreaData[i].provinceName);
                            // if(!this.equipmentAreaData[i].provinceCount && this.equipmentAreaData[i].provinceCount != 0){
                            //     this.equipmentAreaData[i].provinceCount = 0;
                            // }
                            // this.areaCount.push(this.equipmentAreaData[i].provinceCount)
                            if (i < 11) {
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
                                if (i == 11) {
                                    this.areaName.push("其它");
                                    this.areaCount.push(otherCount);
                                }
                            }
                        }

                    }
                    else{
                        this.equipmentAreaData = res.data.statisticsData.areaCount;
                        let otherCount = 0;
                        for(let i = 0 ; i< this.equipmentAreaData.length ; i++){
                            if (i < 11) {
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
                                if (i == 11) {
                                    this.areaName.push("其它");
                                    this.areaCount.push(otherCount);
                                }
                            }
                        }
                    }

                    this.stateOption = {
                        title : {
                            text: '设备状态',
                            left:'20px',
                            x:'left',
                            subtext:'实时数据',

                        },

                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)"
                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2
                                },
                                myTool1: {
                                    show: true,
                                    title: '数据详情',
                                    icon: 'path://M432.45,595.444c0,2.177-4.661,6.82-11.305,6.82c-6.475,0-11.306-4.567-11.306-6.82s4.852-6.812,11.306-6.812C427.841,588.632,432.452,593.191,432.45,595.444L432.45,595.444z M421.155,589.876c-3.009,0-5.448,2.495-5.448,5.572s2.439,5.572,5.448,5.572c3.01,0,5.449-2.495,5.449-5.572C426.604,592.371,424.165,589.876,421.155,589.876L421.155,589.876z M421.146,591.891c-1.916,0-3.47,1.589-3.47,3.549c0,1.959,1.554,3.548,3.47,3.548s3.469-1.589,3.469-3.548C424.614,593.479,423.062,591.891,421.146,591.891L421.146,591.891zM421.146,591.891',
                                    onclick:()=>{
                                        this.$router.push('/staticDetail')
                                    }
                                },
                            },
                            right:'40px',
                        },
                        color:[ '#F56C6C','#909399','#3BB57D',],
                        legend: {
                            left:"70%",
                            top:"35%",
                            orient: 'vertical',
                            x: 'left',
                            data:['报警率','离线率','故障率']
                        },
                        series: [
                            {
                                name:'设备状态',
                                type:'pie',
                                center:['40%', '50%'],
                                radius: ['55%', '70%'],
                                avoidLabelOverlap: false,
                                label: {
                                    normal: {
                                        show: false,
                                        position: 'center'
                                    },
                                    emphasis: {
                                        show: true,
                                        textStyle: {
                                            fontSize: '18',
                                            fontWeight: 'bold'
                                        }
                                    }
                                },
                                labelLine: {
                                    normal: {
                                        show: false
                                    }
                                },
                                data:[
                                    {value:this.stateData.equipCountAlarm, name:'报警率'},
                                    {value:this.stateData.equipCountOffLine, name:'离线率'},
                                    {value:this.stateData.equipCountFault, name:'故障率'},
                                ],

                            }
                        ]
                    };
                    this.safeData = (((this.stateData.deviceCount - this.stateData.equipCountAlarm)/this.stateData.deviceCount)*100).toFixed(2);
                    if(this.stateData.deviceCount == 0){
                        this.safeData = 100;
                    }
                    this.safeOption = {
                        title : {
                            left:"26px",
                            text: '安全测评',
                            x:'center',
                            subtext:'实时数据',

                        },
                        tooltip : {
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2
                                },
                                myTool1: {
                                    show: true,
                                    title: '数据详情',
                                    icon: 'path://M432.45,595.444c0,2.177-4.661,6.82-11.305,6.82c-6.475,0-11.306-4.567-11.306-6.82s4.852-6.812,11.306-6.812C427.841,588.632,432.452,593.191,432.45,595.444L432.45,595.444z M421.155,589.876c-3.009,0-5.448,2.495-5.448,5.572s2.439,5.572,5.448,5.572c3.01,0,5.449-2.495,5.449-5.572C426.604,592.371,424.165,589.876,421.155,589.876L421.155,589.876z M421.146,591.891c-1.916,0-3.47,1.589-3.47,3.549c0,1.959,1.554,3.548,3.47,3.548s3.469-1.589,3.469-3.548C424.614,593.479,423.062,591.891,421.146,591.891L421.146,591.891zM421.146,591.891',
                                    onclick:()=>{
                                        this.$router.push('/staticDetail')
                                    }
                                },
                            },
                            right:'25px',
                        },
                        series: [
                            {
                                textStyle: {
                                    color: '#fff',
                                    size:'3',
                                },
                                name: '测评结果',
                                type: 'gauge',
                                detail: {formatter:'{value}%'},
                                data: [{value:this.safeData,}],
                                axisLine: {            // 坐标轴线
                                    lineStyle: {       // 属性lineStyle控制线条样式
                                        color: [[this.safeData/100, '#538fea'],[1, '#f0f2f5']]
                                    }
                                },
                                axisTick: {            // 坐标轴小标记
                                    show: false
                                },
                                splitLine: {           // 分隔线
                                    show: false
                                },
                                pointer:{
                                    width:3,
                                },
                                center: ["50%", "56%"],
                            },
                        ],

                    };
                    this.architectureOption = {
                        title: { text: '建筑类型',x:'center' ,left:"25px",top:"0"},
                        tooltip: {},
                        xAxis: {
                            data: this.buildName,
                            axisLine:{
                                lineStyle:{
                                    color:'#d9d9d9'
                                }
                            },
                            axisLabel:{
                                color:'#6b6c70',
                                interval:0,
                                rotate:30
                            },
                        },
                        yAxis: {
                            axisLine:{
                                lineStyle:{
                                    color:'#ffffff'
                                }
                            },
                            axisLabel:{
                                color:'#6b6c70'
                            }
                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2
                                }
                            },
                            right:'10px'
                        },
                        series: [{
                            name: '数量',
                            type: 'bar',
                            data: this.equipCount,
                            itemStyle: {
                                normal: {
                                    // 随机显示
                                    //color:function(d){return "#"+Math.floor(Math.random()*(256*256*256-1)).toString(16);}

                                    // 定制显示（按顺序）
                                    color: function(params) {
                                        var colorList = ['#7ed0ff','#fcba6d','#3aa1ff','#f08198','#3ec7bf', '#B74AE5','#0AAF9F','#E89589','#16A085','#4A235A','#C39BD3 ','#F9E79F','#BA4A00','#ECF0F1','#616A6B','#EAF2F8','#4A235A','#3498DB' ];
                                        return colorList[params.dataIndex]
                                    }
                                },
                            },

                        }],
                        grid: {
                            left: '8%',
                            right: 0,
                            bottom: 0,
                            top:'20%',
                            containLabel: true,
                        },
                    };

                    this.equipmentAreaOption = {
                        color: ['#3398DB'],
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {
                                type : 'shadow'
                            }
                        },
                        grid: {
                            left: '4%',
                            right:"4%",
                            bottom: "4%",
                            top:'10%',
                            containLabel: true,

                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2,
                                }
                            },
                            top:-4,
                            right:40
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data :this.areaName ,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine:{
                                    lineStyle:{
                                        color:'#d9d9d9'
                                    }
                                },
                                axisLabel:{
                                    color:'#6b6c70',
                                    interval:0,
                                    rotate:30
                                },

                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine:{
                                    lineStyle:{
                                        color:'#ffffff'
                                    }
                                },
                                axisLabel:{
                                    color:'#6b6c70'
                                }
                            }
                        ],
                        series : [
                            {
                                name:'数量',
                                type:'bar',
                                barWidth: '60%',
                                data:this.areaCount,
                                itemStyle: {
                                    normal: {
                                        color:'#387ee8'
                                    },
                                },

                            }
                        ]
                    };

                    this.stateRate.setOption(this.stateOption);
                    this.safeScore.setOption(this.safeOption);
                    this.architectureType.setOption(this.architectureOption);
                    if(this.userInfo.roleType != 2){
                        this.equipmentArea.setOption(this.equipmentAreaOption);
                    }

                }, (err) => {

                });
            },
            changeProvince(){
                let url = this.changeAreaStaticUrl;
                let data = {
                    provinceId:this.provinceValue
                };
                this.apiPost(url,data).then((res) => {
                    console.log(res);
                    //离线数
                    this.stateData.equipCountOffLine = res.data.statisticsData.equipCountOffLine;
                    //报警数
                    this.stateData.equipCountAlarm = res.data.statisticsData.equipCountAlarm;
                    //故障数
                    this.stateData.equipCountFault = res.data.statisticsData.equipCountFault;
                    //设备数
                    this.stateData.deviceCount = res.data.statisticsData.equipCount;
                    this.stateOption = {
                        title : {
                            text: '设备状态',
                            left:"60px",
                            style:"margin-left:10px;",
                            x:'center'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)"
                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2
                                }
                            }
                        },
                        color:[ '#F56C6C','#909399','#3BB57D',],
                        legend: {
                            left:"80%",
                            top:"35%",
                            orient: 'vertical',
                            x: 'left',
                            data:['报警率','离线率','故障率']
                        },
                        series: [
                            {
                                name:'设备状态',
                                type:'pie',
                                radius: ['55%', '70%'],
                                avoidLabelOverlap: false,
                                label: {
                                    normal: {
                                        show: false,
                                        position: 'center'
                                    },
                                    emphasis: {
                                        show: true,
                                        textStyle: {
                                            fontSize: '18',
                                            fontWeight: 'bold'
                                        }
                                    }
                                },
                                labelLine: {
                                    normal: {
                                        show: false
                                    }
                                },
                                data:[
                                    {value:this.stateData.equipCountAlarm, name:'报警率'},
                                    {value:this.stateData.equipCountOffLine, name:'离线率'},
                                    {value:this.stateData.equipCountFault, name:'故障率'},
                                ],

                            }
                        ]
                    };
                    this.safeData = (((this.stateData.deviceCount - this.stateData.equipCountAlarm)/this.stateData.deviceCount)*100).toFixed(2);
                    console.log(this.safeData)
                    if(this.stateData.deviceCount == 0){
                        this.safeData = 100;
                    }
                    this.safeOption = {
                        title : {
                            left:"26px",
                            text: '安全测评',
                            x:'center'
                        },
                        tooltip : {
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2
                                }
                            },
                            right:26
                        },
                        series: [
                            {
                                textStyle: {
                                    color: '#fff',
                                    size:'3',
                                },
                                name: '测评结果',
                                type: 'gauge',
                                detail: {formatter:'{value}%'},
                                data: [{value:this.safeData,}],
                                axisLine: {            // 坐标轴线
                                    lineStyle: {       // 属性lineStyle控制线条样式
                                        color: [[this.safeData/100, '#538fea'],[1, '#f0f2f5']]
                                    }
                                },
                                axisTick: {            // 坐标轴小标记
                                    show: false
                                },
                                splitLine: {           // 分隔线
                                    show: false
                                },

                            },

                        ]
                    };
                    //按照建筑类型分类的数据
                    this.architectureTypeData = res.data.statisticsData.buildCount;
                    this.buildName = [];
                    this.equipCount = [];
                    for(let i = 0 ; i< this.architectureTypeData.length ; i++){
                        this.buildName.push(this.architectureTypeData[i].buildName);
                        this.equipCount.push(this.architectureTypeData[i].equipCount);
                    }
                    this.architectureOption = {
                        title: { text: '建筑类型',x:'center' ,left:"0px",top:"-5px"},
                        tooltip: {},
                        xAxis: {
                            data: this.buildName,
                            axisLine:{
                                lineStyle:{
                                    color:'#d9d9d9'
                                }
                            },
                            axisLabel:{
                                color:'#6b6c70',
                                interval:0,
                                rotate:30
                            },
                        },
                        yAxis: {
                            axisLine:{
                                lineStyle:{
                                    color:'#ffffff'
                                }
                            },
                            axisLabel:{
                                color:'#6b6c70'
                            }
                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2
                                }
                            }
                        },
                        series: [{
                            name: '数量',
                            type: 'bar',
                            data: this.equipCount,
                            itemStyle: {
                                normal: {
                                    // 随机显示
                                    //color:function(d){return "#"+Math.floor(Math.random()*(256*256*256-1)).toString(16);}

                                    // 定制显示（按顺序）
                                    color: function(params) {
                                        var colorList = ['#7ed0ff','#fcba6d','#3aa1ff','#f08198','#3ec7bf', '#B74AE5','#0AAF9F','#E89589','#16A085','#4A235A','#C39BD3 ','#F9E79F','#BA4A00','#ECF0F1','#616A6B','#EAF2F8','#4A235A','#3498DB' ];
                                        return colorList[params.dataIndex]
                                    }
                                },
                            },

                        }],
                        grid: {
                            left: 0,
                            right: 0,
                            bottom: 0,
                            top:'14%',
                            containLabel: true
                        },
                    };
                    this.areaName = [];
                    this.areaCount = [];
                    let otherCount = 0;
                    if(this.provinceValue == 0){
                        this.equipmentAreaData = res.data.statisticsData.provinceCount;
                        for(let i = 0 ; i< this.equipmentAreaData.length ; i++){
                            if (i < 11) {
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
                                if (i == 11) {
                                    this.areaName.push("其它");
                                    this.areaCount.push(otherCount);
                                }
                            }
                            // this.areaName.push(this.equipmentAreaData[i].provinceName);
                            // if(!this.equipmentAreaData[i].provinceCount && this.equipmentAreaData[i].provinceCount != 0){
                            //     this.equipmentAreaData[i].provinceCount = 0;
                            // }
                            // this.areaCount.push(this.equipmentAreaData[i].provinceCount)
                        }
                    }
                    else{
                        this.equipmentAreaData = res.data.statisticsData.areaCount;
                        for(let i = 0 ; i< this.equipmentAreaData.length ; i++){
                            if (i < 11) {
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
                                if (i == 11) {
                                    this.areaName.push("其它");
                                    this.areaCount.push(otherCount);
                                }
                            }
                            // this.areaName.push(this.equipmentAreaData[i].areaName);
                            // if(!this.equipmentAreaData[i].areaCount && this.equipmentAreaData[i].areaCount != 0){
                            //     this.equipmentAreaData[i].areaCount = 0;
                            // }
                            // this.areaCount.push(this.equipmentAreaData[i].areaCount)
                        }
                    }

                    this.equipmentAreaOption = {
                        color: ['#3398DB'],
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {
                                type : 'shadow'
                            }
                        },
                        grid: {
                            left: '4%',
                            right:"4%",
                            bottom: "4%",
                            top:'10%',
                            containLabel: true,

                        },
                        toolbox:{
                            show: true,
                            feature: {
                                saveAsImage: {
                                    show:true,
                                    excludeComponents :['toolbox'],
                                    pixelRatio: 2,
                                }
                            },
                            top:-4,
                            right:40
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data :this.areaName ,
                                axisTick: {
                                    alignWithLabel: true
                                },
                                axisLine:{
                                    lineStyle:{
                                        color:'#d9d9d9'
                                    }
                                },
                                axisLabel:{
                                    color:'#6b6c70',
                                    interval:0,
                                    rotate:30
                                },

                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisLine:{
                                    lineStyle:{
                                        color:'#ffffff'
                                    }
                                },
                                axisLabel:{
                                    color:'#6b6c70'
                                }
                            }
                        ],
                        series : [
                            {
                                name:'数量',
                                type:'bar',
                                barWidth: '60%',
                                data:this.areaCount,
                                itemStyle: {
                                    normal: {
                                        color:'#387ee8'
                                    },
                                },

                            }
                        ]
                    };
                    this.stateRate.setOption(this.stateOption);
                    this.safeScore.setOption(this.safeOption);
                    this.architectureType.setOption(this.architectureOption);
                    this.equipmentArea.setOption(this.equipmentAreaOption);
                },(err)=>{

                });
            },
            drawLine(){
                // 基于准备好的dom，初始化echarts实例
                this.stateRate = this.$echarts.init(document.getElementById('stateRate'));
                this.safeScore = this.$echarts.init(document.getElementById('safeScore'));
                this.architectureType = this.$echarts.init(document.getElementById('architectureType'));
                if(this.userInfo.roleType != 2){
                    this.equipmentArea = this.$echarts.init(document.getElementById('equipmentArea'));
                }
            },
            exportAlarm(){
                let url = this.excelForWarnLogUrl;
                let data = {
                    isDevice: 0,
                    siteId: 0,
                    deviceId: '',
                    inquir:'',
                    type:-1
                };
                this.apiPost(url,data,'blob').then((res) => {
                    this.excelForWarnLogSuccess(res);
                },(err)=>{

                });
            },
            //创建excel对象
            excelForWarnLogSuccess(res){
                const link = document.createElement('a');
                let blob = new Blob([res],{type: 'application/vnd.ms-excel'});
                link.style.display = 'none';
                link.href = URL.createObjectURL(blob);
                let num = '';
                for(let i=0;i < 10;i++){
                    num += Math.ceil(Math.random() * 10)
                }
                link.setAttribute('download', '图表.xlsx');
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            },
        },

        mixins: [http]
    }

</script>

<style scoped>
    .stateRate div{
        width: 100% !important;
    }

</style>
