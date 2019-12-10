import { Loading } from 'element-ui';
import { Message } from 'element-ui';
import { Notification } from 'element-ui';
import { MessageBox } from 'element-ui';
const commonFn = {
    //路由加密
    encodeQuery:function(queryObj){
        let queryStr = JSON.stringify(queryObj);
        queryStr = encodeURIComponent(queryStr);
        queryStr = "hello" + queryStr + "world";
        queryStr = encodeURIComponent(queryStr);
        return queryStr;
    },
    //路由解密
    decodeQuery:function(str){
        let queryStr = decodeURIComponent(str);
        queryStr = queryStr.replace("hello","").replace("world","");
        queryStr = decodeURIComponent(queryStr);
        queryStr = JSON.parse(queryStr);
        return queryStr;
    },
    //保存cookie
    setCookie: function (cname, cvalue, exdays) {
        let d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        let expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    },
    //获取cookie
    getCookie: function (cname) {
        let arr;
        let reg = new RegExp("(^| )" + cname + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)){
            return (arr[2]);
        }
        else{
            return null;
        }
    },
    //删除cookie
    delCookie: function (name) {
        let exp = new Date();
        exp.setTime(exp.getTime() - 1);
        let cval = this.getCookie(name);
        if (cval != null){
            document.cookie = name + "=" + cval + ";expires=" + exp.toUTCString();
        }
    },
    //显示loading页
    showLoading(type,selector){
        let _loading;
        //全屏loading
        if(type === 1){
            _loading = Loading.service({
                text:'加载中...',
                spinner: 'el-icon-loading',
                background:'rgba(255,255,255,0.5)',
            });
        }
        //选择器loading
        else{
            _loading = Loading.service({
                target:document.querySelector(selector),
                fullscreen:false,
                spinner: 'el-icon-loading',
                text:'加载中...',
                background:'rgba(255,255,255,0.5)',
            });
        }
        return _loading;
    },
    //弹出消息提示
    showTip(msg,type,showClose){
        //成功消息提示
        if(type === 1){
            Message.success({
                message: msg,
                center: true,
                duration:2000,
                showClose:showClose
            })
        }
        //警告消息提示
        else if(type === 2){
            Message.warning({
                message: msg,
                center: true,
                duration:2000,
                showClose:showClose
            })
        }
        //错误消息提示
        else if(type === 3){
            Message.error({
                message: msg,
                center: true,
                duration:2000,
                showClose:showClose
            })
        }
        //消息提示
        else{
            Message.info({
                message: msg,
                center: true,
                duration:2000,
                showClose:showClose
            })
        }
    },
    //弹出通知栏
    showNotification(msg,type,showClose,successCallback){
        //成功通知提示
        if(type === 1){
            Notification.success({
                message: msg,
                center: true,
                showClose:showClose,
                position: 'bottom-right',
                duration : 0
            })
        }
        //警告通知提示
        else if(type === 2){
            Notification.warning({
                message: msg,
                center: true,
                showClose:showClose,
                position: 'bottom-right',
                duration : 0,
                customClass:'handType',
                onClick:successCallback
            })
        }
        //错误通知提示
        else if(type === 3){
            Notification.error({
                message: msg,
                center: true,
                showClose:showClose,
                position: 'bottom-right',
                duration : 0
            })
        }
        //通知提示
        else{
            Notification.info({
                message: msg,
                center: true,
                showClose:showClose,
                position: 'bottom-right',
                duration : 0
            })
        }
    },
    closeNotification(){
        Notification.closeAll();
    },
    //弹出确认框
    showConfirm(fun){
        MessageBox.confirm('此操作将永久删除该数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then((resolve) => {
            fun();
            //resolve(response);
        }).catch(() => {

        });
    },
    //获取当前时间
    getLocalTime(){
        let date = new Date();
        let seperator1 = "-";
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        let currentDate = year + seperator1 + month + seperator1 + strDate;
        return currentDate;
    },
    formatDate(timeStamp, type = 'Y-M-D H:I:S', auto = false) {
        let time = (timeStamp + '').length === 10 ? new Date(parseInt(timeStamp) * 1000) : new Date(parseInt(timeStamp));
        let _year = time.getFullYear();
        let _month = (time.getMonth() + 1) < 10 ? '0' + (time.getMonth() + 1) : (time.getMonth() + 1);
        let _date = time.getDate() < 10 ? '0' + time.getDate() : time.getDate();
        let _hours = time.getHours() < 10 ? '0' + time.getHours() : time.getHours();
        let _minutes = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
        let _secconds = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
        let formatTime = '';
        let distinctTime = new Date().getTime() - time.getTime();

        if (auto) {
            if (distinctTime <= (1 * 60 * 1000)) {
                // console.log('一分钟以内，以秒数计算');
                let _s = Math.floor((distinctTime / 1000) % 60);
                formatTime = _s + '秒前';
            } else if (distinctTime <= (1 * 3600 * 1000)) {
                // console.log('一小时以内,以分钟计算');
                let _m = Math.floor((distinctTime / (60 * 1000)) % 60);
                formatTime = _m + '分钟前';
            } else if (distinctTime <= (24 * 3600 * 1000)) {
                // console.log('一天以内，以小时计算');
                let _h = Math.floor((distinctTime / (60 * 60 * 1000)) % 24);
                formatTime = _h + '小时前';
            } else if (distinctTime <= (30 * 24 * 3600 * 1000)) {
                let _d = Math.floor((distinctTime / (24 * 60 * 60 * 1000)) % 30);
                formatTime = _d + '天前';
                // console.log('30天以内,以天数计算');
            } else {
                // 30天以外只显示年月日
                formatTime = _year + '-' + _month + '-' + _date;
            }
        } else {

            switch (type) {
                case 'Y-M-D H:I:S':
                    formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours + ':' + _minutes + ':' + _secconds;
                    break;
                case 'Y-M-D H:I:S zh':
                    formatTime = _year + '年' + _month + '月' + _date + '日  ' + _hours + ':' + _minutes + ':' + _secconds;
                    break;
                case 'Y-M-D H:I':
                    formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours + ':' + _minutes;
                    break;
                case 'Y-M-D H':
                    formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours;
                    break;
                case 'Y-M-D':
                    formatTime = _year + '-' + _month + '-' + _date;
                    break;
                case 'Y-M-D zh':
                    formatTime = _year + '年' + _month + '月' + _date + '日';
                    break;
                case 'Y-M':
                    formatTime = _year + '-' + _month;
                    break;
                case 'Y':
                    formatTime = _year;
                    break;
                case 'M':
                    formatTime = _month;
                    break;
                case 'D':
                    formatTime = _date;
                    break;
                case 'H':
                    formatTime = _hours;
                    break;
                case 'I':
                    formatTime = _minutes;
                    break;
                case 'S':
                    formatTime = _secconds;
                    break;
                default:
                    formatTime = _year + '-' + _month + '-' + _date + ' ' + _hours + ':' + _minutes + ':' + _secconds;
                    break;
            }
        }
        // 返回格式化的日期字符串
        return formatTime;
    },
    formatTime(timeInt) {
        return new Date(parseInt(timeInt) * 1000).toLocaleString().replace(/:\d{1,2}$/, ' ');
    },
}

export default commonFn
