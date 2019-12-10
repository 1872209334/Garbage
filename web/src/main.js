import Vue from 'vue';
import App from './App';
import constant from './common/constant';
import router from './router';
import ElementUI from 'element-ui';
import commonFn from './common/global';
//图标
import './assets/iconfont/iconfont.css';
//import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
import './resource/css/theme-hixent/index.css';       // 汇讯主题
import "babel-polyfill";
import "./resource/icon/iconfont.css";
import PerfectScrollbar from 'perfect-scrollbar';
import "perfect-scrollbar/css/perfect-scrollbar.css";
import echarts from 'echarts';


import axios from 'axios';
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
Vue.prototype.$axios = axios;

axios.interceptors.request.use(
    config => {
        config.headers.Authorization = 'Bearer '+sessionStorage.getItem('token');
        return config
    },
    error => {
        return Promise.reject(error)
    }
);

Vue.prototype.$echarts = echarts;
Vue.use(ElementUI, { size: 'small' });
Vue.prototype.$commonFn = commonFn;
Vue.prototype.$constant = constant;

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    if(!sessionStorage.getItem('token') && to.path !== '/login'){
        if(to.path == '/login_kys') //泉州开元寺登录页
            next();
        else
            next('/login');
    }
    else if(to.meta.permission){
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        role === 'admin' ? next() : next('/403');
    }
    else{
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if(navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor'){
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        }else{
            next();
        }
    }
});

const el_scrollBar = (el) => {
    //在元素上加点私货，名字随便取，确保不会和已有属性重复即可，我取名叫做_ps_
    if (el._ps_ instanceof PerfectScrollbar) {
        el._ps_.update();
    } else {
        //el上挂一份属性
        el._ps_ = new PerfectScrollbar(el, { suppressScrollX: false });
    }
};
Vue.directive("scrollMask",{
    inserted(el, binding){
        const { arg } = binding;
        if(arg === "scrollTip"){
            el = el.querySelector(".el-table__body-wrapper");
            if(!el){
                return console.warn("未发现className为el-table__body-wrapper的dom");
            }
        }
        const rules = ["fixed", "absolute", "relative"];
        if (!rules.includes(window.getComputedStyle(el, null).position)) {
            console.error(`perfect-scrollbar所在的容器的position属性必须是以下之一：${rules.join("、")}`)
        }
        el_scrollBar(el);
    },
    componentUpdated(el, binding, vnode) {
        const { arg } = binding;
        if (arg === "scrollTip") {
            el = el.querySelector(".el-table__body-wrapper");
            if(!el){
                return console.warn("未发现className为el-table__body-wrapper的dom");
            }
        }
        vnode.context.$nextTick(
            () => {
                try {
                    el_scrollBar(el);
                } catch (error) {
                    console.error(error);
                }
            }
        )
    },
})

new Vue({
    router,
    render: h => h(App)
}).$mount('#app');
