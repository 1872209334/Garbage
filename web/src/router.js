import Vue from 'vue';
import Router from 'vue-router';
import Login from './components/page/Login.vue';
import Login_Kys from './components/page/Login_Kys.vue';
import Home from './components/common/Home.vue';
import Main from './components/page/Main.vue';
import NotFound from './components/page/404.vue';
import PermissionDeny from './components/page/403.vue';
/*====================================================== 地图 ======================================================*/
import MapDetail from './components/page/MapDetail.vue';

/*==================================================== 统计管理 ====================================================*/
import StatisticalControl from './components/page/StatisticalControl.vue';
import StaticDetail from './components/page/StaticDeiail.vue'
/*====================================================  大屏页  ====================================================*/
import LargeSizePage from './components/page/LargeSizePage.vue';

/*==================================================== 报警列表 ====================================================*/
//报警历史
import AlarmHistory from './components/page/AlarmManagement/AlarmHistory.vue';
//报警日志
import AlarmLog from './components/page/AlarmManagement/AlarmLog.vue';
//离线记录
import Off_LineLog from './components/page/AlarmManagement/Off_LineLog.vue';
//故障日志
import FaultLog from './components/page/AlarmManagement/FaultLog.vue';
//故障日志查看设备
import FauLogDev from './components/page/AlarmManagement/FauLogDev.vue';
//离线日志详情
import OffLogDev from './components/page/AlarmManagement/OffLogDev.vue';

//报警日志查看设备
import AlaLogDev from './components/page/AlarmManagement/AlaLogDev.vue';
//处理历史详情
import HistoryLog from './components/page/AlarmManagement/HistoryLog.vue'

/*==================================================== 系统管理 ====================================================*/
//操作记录
import OperationLog from './components/page/SystemManagement/OperationLog.vue';
//线上项目升级
import DevelopingProject from './components/page/SystemManagement/DevelopingProject.vue';
//中控升级
import LevelCenterControl from './components/page/SystemManagement/LevelCenterControl.vue';
//终端升级
import LevelTerminal from './components/page/SystemManagement/LevelTerminal.vue';

/*==================================================== 项目管理 ====================================================*/
//新建项目
// import NewProject from './components/page/ProjectManagement/NewProject.vue';
//管控分组列表
import ControlGrouping from './components/page/ProjectManagement/ControlGrouping.vue';
//新建、修改管控分组
import EditControlGrouping from './components/page/ProjectManagement/EditControlGrouping.vue';
//新建管控分组
import AddControlGrouping from './components/page/ProjectManagement/AddControlGrouping.vue';
//中控、告警使能
import CenterControlAlarm from './components/page/ProjectManagement/CenterControlAlarm.vue';
//中控信息页
import CenterControlInformation from './components/page/ProjectManagement/CenterControlInformation.vue';
//终端设备告警使能
import TerminalAlarm from './components/page/ProjectManagement/TerminalAlarm.vue';
//终端设备信息
import TerminalInformation from './components/page/ProjectManagement/TerminalInformation.vue';
//终端设备页
import Project from './components/page/ProjectManagement/Project.vue';
import ProjectDetail from './components/page/ProjectManagement/ProjectDetail.vue';
import DeviceDetail from './components/page/ProjectManagement/DeviceDetail.vue';
import MqttDeviceList from './components/page/ProjectManagement/MqttDeviceList.vue';
import MqttInformation from './components/page/ProjectManagement/MqttInformation.vue';

/*==================================================== 用户管理 ====================================================*/
//管控人员
import ControlPersonnel from './components/page/UserManagement/ControlPersonnel.vue';
//管理员
import Manage from './components/page/UserManagement/Manage.vue';
//权限管理
import JurisdictionManagement from './components/page/UserManagement/JurisdictionManagement.vue';
//新增、修改管控人员
import EditControlPersonnel from './components/page/UserManagement/EditControlPersonnel.vue';
//新增、修改管理员
import EditManage from './components/page/UserManagement/EditManage.vue';
//新增、修改权限
import EditJurisdiction from './components/page/UserManagement/EditJurisdiction.vue';

/*==================================================== 档案管理 ====================================================*/
//电路图
import CircuitDiagram from './components/page/RecordManagement/CircuitDiagram.vue';
//电路图上传
import UpLoadDiagram from './components/page/RecordManagement/UpLoadDiagram.vue';
//验收报告
import InspectionReport from './components/page/RecordManagement/InspectionReport.vue';
//报告上传
import UpLoadReport from './components/page/RecordManagement/UpLoadReport.vue';
import NewProject from "./components/page/ProjectManagement/NewProject";
import NewDevice from "./components/page/ProjectManagement/NewDevice";

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/',
            component: Home,
            meta: { title: '后台管理系统' },
            children:[
                {
                    path: '/main',
                    name:'main',
                    component: Main,
                    meta: { title: '首页' }
                },
                {
                    path: '/alaLogDev',
                    name:'alaLogDev',
                    component: AlaLogDev,
                    meta: { title: '报警日志设备详情' }
                },
                {
                    path: '/fauLogDev',
                    name:'fauLogDev',
                    component: FauLogDev,
                    meta: { title: '故障日志设备详情' }
                },
                {
                    path: '/offLogDev',
                    name:'offLogDev',
                    component: OffLogDev,
                    meta: { title: '离线日志设备详情' }
                },
                {
                    path: '/faultLog',
                    name:'faultLog',
                    component: FaultLog,
                    meta: { title: '故障日志' }
                },

                {
                    path: '/mapDetail',
                    name:'mapDetail',
                    component: MapDetail,
                    meta: { title: '详细地图' }
                },
                {
                    path: '/inspectionReport',
                    component: InspectionReport,
                    name:'inspectionReport',
                    meta: { title: '验收报告' }
                },
                {
                    path: '/circuitDiagram',
                    component: CircuitDiagram,
                    name:'circuitDiagram',
                    meta: { title: '电路图' }
                },
                {
                    path: '/upLoadDiagram',
                    component: UpLoadDiagram,
                    name:'upLoadDiagram',
                    meta: { title: '上传施工图' }
                },
                {
                    path: '/upLoadReport',
                    component: UpLoadReport,
                    name:'upLoadReport',
                    meta: { title: '上传报告' }
                },
                {
                    path: '/editJurisdiction',
                    component: EditJurisdiction,
                    name:'editJurisdiction',
                    meta: { title: '新增、修改权限' }
                },
                {
                    path: '/editManage',
                    component: EditManage,
                    name:'editManage',
                    meta: { title: '新增、修改管理员' }
                },
                {
                    path: '/editControlPersonnel',
                    component: EditControlPersonnel,
                    name:'editControlPersonnel',
                    meta: { title: '新增、修改管控人员' }
                },
                {
                    path: '/jurisdictionManagement',
                    component: JurisdictionManagement,
                    meta: { title: '权限管理' }
                },
                {
                    path: '/manage',
                    component: Manage,
                    meta: { title: '管理员' }
                },
                {
                    path: '/controlPersonnel',
                    component: ControlPersonnel,
                    meta: { title: '管控人员' }
                },
                {
                    path: '/project',
                    component: Project,
                    meta: { title: '终端设备页' },
                    children:[
                        {
                            path: '/projectDetail',
                            name:'projectDetail',
                            component: ProjectDetail,
                            meta: { title: '项目主页' }
                        },
                        {
                            path: '/deviceDetail',
                            name:'deviceDetail',
                            component: DeviceDetail,
                            meta: { title: '中控主页' }
                        },
                        {
                            path: '/terminalInformation',
                            component: TerminalInformation,
                            meta: { title: '终端设备信息' },
                            name:'terminalInformation',
                        },
                        {
                            path: '/centerControlInformation',
                            component: CenterControlInformation,
                            meta: { title: '中控信息页' },
                            name:'centerControlInformation'
                        },
                        {
                            path: '/centerControlAlarm',
                            component: CenterControlAlarm,
                            meta: { title: '中控、告警使能' },
                            name:'centerControlAlarm'
                        },
                        {
                            path: '/terminalAlarm',
                            component: TerminalAlarm,
                            meta: { title: '终端设备告警使能' },
                            name:'terminalAlarm'
                        },
                        {
                            path: '/mqttDeviceList',
                            component: MqttDeviceList,
                            meta: { title: '无线终端设备列表' },
                            name:'mqttDeviceList'
                        },
                        {
                            path: '/mqttInformation',
                            component: MqttInformation,
                            meta: { title: '无线终端设备详情' },
                            name:'mqttInformation'
                        },

                    ]
                },
                {
                    path: '/addControlGrouping',
                    component: AddControlGrouping,
                    name:'addControlGrouping',
                    meta: { title: '新建、修改管控分组' }
                },

                {
                    path: '/newProject',
                    component: NewProject,
                    name:'newProject',
                    meta: { title: '新建项目' }
                },
                {
                    path: '/newDevice',
                    component: NewDevice,
                    name:'newDevice',
                    meta: { title: '新建终端' }
                },

                {
                    path: '/editControlGrouping',
                    component: EditControlGrouping,
                    name:'editControlGrouping',
                    meta: { title: '新建、修改管控分组' }
                },
                {
                    path: '/controlGrouping',
                    component: ControlGrouping,
                    meta: { title: '管控分组列表' }
                },
                {
                    path: '/levelTerminal',
                    component: LevelTerminal,
                    meta: { title: '终端升级' }
                },
                {
                    path: '/levelCenterControl',
                    component: LevelCenterControl,
                    meta: { title: '中控升级' }
                },
                {
                    path: '/developingProject',
                    component: DevelopingProject,
                    meta: { title: '线上项目升级' }
                },
                {
                    path: '/operationLog',
                    component: OperationLog,
                    meta: { title: '操作记录' }
                },
                {
                    path: '/off_LineLog',
                    component: Off_LineLog,
                    meta: { title: '离线记录' }
                },
                {
                    path: '/alarmLog',
                    component: AlarmLog,
                    meta: { title: '报警日志' }
                },
                {
                    path: '/alarmHistory',
                    component: AlarmHistory,
                    meta: { title: '报警历史' }
                },
                {
                    path: '/historyLog',
                    name:'historyLog',
                    component: HistoryLog,
                    meta: { title: '报警历史详情' }
                },
                {
                    path: '/statisticalControl',
                    component: StatisticalControl,
                    meta: { title: '统计管理' }
                },
                {
                    path: '/staticDetail',
                    name:'staticDetail',
                    component: StaticDetail,
                    meta: { title: '统计详情' }
                },
            ]
        },
        {
            path: '/login',
            component: Login
        },
        {
            path: '/login_kys',
            component: Login_Kys
        },
        {
            path: '/404',
            component: NotFound
        },
        {
            path: '/403',
            component: PermissionDeny
        },
        {
            path: '*',
            redirect: '/404'
        },
        {
            path: '/largeSizePage',//大屏页
            component: LargeSizePage,
        },
    ]
})
