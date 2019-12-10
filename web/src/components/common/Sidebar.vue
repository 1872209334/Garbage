<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#2E4362"
            text-color="#bfcbd9" active-text-color="white" unique-opened router>

            <template v-for="item in menu">

                <template v-if="item.childMenus">

                    <el-submenu :index="item.menuUrl" :key="item.menuUrl">
                        <template slot="title">
                            <i :class="item.menuIcon"></i> &nbsp; <span slot="title">{{ item.menuName }}</span>
                        </template>
                        <el-menu-item v-for="(subItem,i) in item.childMenus" :key="i" :index="subItem.menuUrl">
                            {{ subItem.menuName }}
                        </el-menu-item>
                    </el-submenu>
                </template>

                <template v-else>
                    <el-menu-item :index="item.menuUrl" :key="item.menuUrl">
                        <i :class="item.menuIcon"></i> &nbsp;<span slot="title">{{ item.menuName }}</span>
                    </el-menu-item>
                </template>

            </template>
        </el-menu>

    </div>
</template>

<script>
    import bus from '../../common/bus';
    export default {
        data() {
            return {
                collapse: false,
                menu:[],
            }
        },
        methods:{

        },
        computed:{
            onRoutes(){
                let activeRoute = this.$route.path.replace('/','');
                let projectDetailArr = ['/deviceDetail','/mqttDeviceList','/mqttInformation','/centerControlInformation','/centerControlAlarm','/terminalInformation','/terminalAlarm'];
                if(projectDetailArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'projectDetail';
                    return activeRoute;
                }
                let controlGroupingArr = ['/editControlGrouping','/addControlGrouping'];
                if(controlGroupingArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'controlGrouping';
                    return activeRoute;
                }
                // let newProjectArr = ['/newProject'];
                // if(controlGroupingArr.indexOf(this.$route.path) !== -1){
                //     activeRoute = 'newProject';
                //     return activeRoute;
                // }
                let alarmLogArr = ['/alaLogDev'];
                if(alarmLogArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'alarmLog';
                    return activeRoute;
                }
                let faultLogArr = ['/fauLogDev'];
                if(faultLogArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'faultLog';
                    return activeRoute;
                }
                let offLogArr = ['/offLogDev'];
                if(offLogArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'off_LineLog';
                    return activeRoute;
                }
                let manageArr = ['/editManage'];
                if(manageArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'manage';
                    return activeRoute;
                }
                let controlPersonnelArr = ['/editControlPersonnel'];
                if(controlPersonnelArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'controlPersonnel';
                    return activeRoute;
                }
                let jurisdictionManagementArr = ['/editJurisdiction'];
                if(jurisdictionManagementArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'jurisdictionManagement';
                    return activeRoute;
                }
                let circuitDiagramArr = ['/upLoadDiagram'];
                if(circuitDiagramArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'circuitDiagram';
                    return activeRoute;
                }
                let inspectionReportArr = ['/upLoadReport'];
                if(inspectionReportArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'inspectionReport';
                    return activeRoute;
                }
                let developingProjectArr = ['/levelCenterControl','/levelTerminal'];
                if(developingProjectArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'developingProject';
                    return activeRoute;
                }
                let alarmHistoryArr = ['/historyLog'];
                if(alarmHistoryArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'alarmHistory';
                    return activeRoute;
                }
                let statisticalControlArr = ['/staticDetail'];
                if(statisticalControlArr.indexOf(this.$route.path) !== -1){
                    activeRoute = 'statisticalControl';
                    return activeRoute;
                }
                return activeRoute;
            }
        },
        created(){
            this.menu = JSON.parse(sessionStorage.getItem("fire_menu"));
        },

    }
</script>

<style scoped>
    .el-menu-item.is-active {
        background-color: #182538 !important;
        border-right: 6px solid #387EE8;
    }

    .sidebar{
        display: block;
        position: absolute;
        left: 0;
        top: 60px;
        bottom:0;
        overflow-y: scroll;
        z-index:4;
    }
    .sidebar::-webkit-scrollbar{
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse){
        width: 184px;
    }
    .sidebar > ul {
        height:100%;
    }
    .el-menu{
        border-right:0;
    }
    .el-menu li{
        font-size: 15px;
    }
    .el-submenu__title i{
        color: #A8B0BD !important;
    }
    .el-submenu__title span{
        font-size: 15px;
    }

    .el-menu-item i{
        color: #A8B0BD !important;
    }
    .el-menu-item span{
        letter-spacing: 1px;
        font-size: 15px;
    }
    .el-menu-item:hover{
        background-color: #182538 !important;
    }
    .el-submenu:hover{
        background-color: #182538 !important;
    }
    .el-menu-item[data-v-085de0bb]:hover{
        background-color: #203049 !important;
        color:#ffffff !important;
    }
    .myIcon-tubiao{
        color: #2d8cf0;
    }
    /*
    .el-menu-item[data-v-085de0bb]{
        background-color: #203049 !important;
    }
    .el-menu-item{
        background-color: red !important;
    }*/
    .el-menu:nth-child(n+2) > .el-menu-item{
         background: rgb(32,48,73) !important;
     }

    .el-menu:nth-child(n+2) > .is-active {
        background: rgb(24,37,56)  !important;
    }
</style>
