import {Component} from '@angular/core';
import {App, NavController, Platform} from "ionic-angular";

import {TabsPage} from "../tabs/tabs";
import {LoadingController} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {BackButtonService} from "../../app/service/backButton.service";
//import {RegisterPage} from "./register/register";
import { Register1Page} from "../../pages/register1/register1";
import {ForgetPasswordPage} from "./forget-password/forget-password";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

import { JPush } from "@jiguang-ionic/jpush";
import { JPushService } from "../../app/service/jpush.service";

@Component({
  templateUrl: 'login.html',
})


export class LoginPage {
  userInput = {
    userName: "",
    userPwd: ""
  };
  loginInput;
  loading;
  initCard = {
    position: 'absolute',
    top: '28rem',
    padding: '3rem 2rem 4rem 2rem',
    'border-radius': '12px'
  };

  constructor(
    public app: App,
    public navCtrl: NavController,
    public loadingCtrl: LoadingController,
    private statusBar: StatusBar,
    public pagesToggleService: PagesToggleService,
    private alertMsgService: AlertMsgService,
    private platform: Platform,
    private backButtonService: BackButtonService,
    public httpService: HttpService,
    public jpush:JPush,
    public jpushService: JPushService,
  ) {
    this.statusBar.styleLightContent();
    this.loading = this.loadingCtrl.create({
      spinner: 'hide',
      content: "<div class='loading'></div>",
      dismissOnPageChange: true, // 是否在切换页面之后关闭loading框
      showBackdrop: true  //是否显示遮罩层
    });
    this.platform.ready().then(() => {
      this.backButtonService.registerBackButtonAction(null);
    });
  }

  ionViewDidLoad() {

  }

  ngOnInit() {
    this.loginInput = {
      account: '',
      password: '',
      messgae:''
    }
  }


  resetPwd() {
    this.navCtrl.push(Register1Page, null);
  }

  doLogin() {
    if(this.loginInput.account == ''){
      this.alertMsgService.showMsg("用户名不能为空");
      return;
    }
    if(this.loginInput.password == ''){
      this.alertMsgService.showMsg("密码不能为空");
      return;
    }
    let postData = {
      account: this.loginInput.account,
      password: this.loginInput.password
    };
console.log("aaf")
    this.httpService.httpPost(this.httpService.loginUrl, postData, this.loginSuccess);
    //alert(this.loginInput.account+this.loginInput.password);
  }

  loginSuccess = (response) => {
    this.alertMsgService.closeLoading();
    if (response.status == ApiConfig.successCode) {
        //保存用户信息
        localStorage.setItem("token",response.data.token);
      localStorage.setItem("userInfo",JSON.stringify(response.data.userinfo));
      this.navCtrl.push(TabsPage, null, null, () => {
        //建立webSocket连接
        let wsId = response.data.userinfo.uid;
        let websocket = new WebSocket("ws://192.168.1.103:8082/webSocketService/ID=app_"+wsId);
        //打开webSokcet连接时，回调该函数
        websocket.onopen = function () {
          console.log("onpen");
        };
        //关闭webSocket连接时，回调该函数
        websocket.onclose = function () {
          //关闭连接
          console.log("onclose");
        };
        //接收信息
        websocket.onmessage = function (msg) {
          console.log(msg);
        };
        //极光推送插件初始化
        this.jpush.init();
        this.jpush.setDebugMode(true);
        /*消息推送配置**/
        //监听初始化
        this.jpushService.initPush();
        this.jpushService.setAlias(response.data.userinfo.username);
        this.app.getRootNav().setRoot(TabsPage);
      })
    }
    else{
      this.alertMsgService.showMsg("用户名或密码错误");
    }
  };

}
