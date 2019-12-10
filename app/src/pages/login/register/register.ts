import {Component, OnInit} from '@angular/core';
import {IonicPage, NavController} from 'ionic-angular';
import {AlertMsgService} from '../../../app/service/alert.msg.service';
import {ApiConfig} from '../../../app/app.api.config';
import {HttpService} from '../../../app/service/http.service';
import {PagesToggleService} from '../../../app/service/pages.toggle.service';
import { Register1Page} from "../../../pages/register1/register1";

/**
 * Generated class for the RegisterPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  templateUrl: 'register.html',
  providers: [AlertMsgService]
})

export class RegisterPage {
  title = '注册账号';
  userInput = {
    userName: "",
    phone: "",
    newPassword: "",
    confirmPassword: "",
    sex: ""
  };
  public user_type;
  private registerInput;
  private step = 1;
  private user;
  private getCodeText = "获取验证码";
  private time;
  private state;

  constructor(private alertMsgService: AlertMsgService,
              private httpService: HttpService,
              private navCtrl: NavController,
              private pagesToggleService: PagesToggleService) {
    this.user_type = 1;
  }

  ngOnInit() {
    this.user = {
      email: '',
      id: '',
      user_login: '',
      pwd1: '',
      pwd2: ''
    };
    //注册表单数据
    this.registerInput = {
      username: '',
      password:'',
      adminAccount: '',
      mobile: '',
      code: '',
    };
    this.state = false;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RegisterPage');

  }

  //注册
  register() {
    this.navCtrl.push(Register1Page, null);

    if (this.registerInput.username && this.registerInput.adminAccount && this.registerInput.mobile && this.registerInput.code) {
      let postData = {
        account: this.registerInput.username,
        password:this.registerInput.password,
        pid:this.registerInput.adminAccount,
        mobile:this.registerInput.mobile,
        ncode:this.registerInput.code,
      };
      this.httpService.httpPost(this.httpService.getRegisterUrl, postData, this.getRegisterSuccess);
    }
    else {
      this.alertMsgService.showMsg("请填写注册信息");
    }
  }

  //注册成功回调
  getRegisterSuccess = (response) => {
    console.log(response);
    this.alertMsgService.closeLoading();
    if (response.status == ApiConfig.successCode) {
      this.alertMsgService.showMsg("注册成功");
      this.navCtrl.pop();
    }
  };
  //去登陆
  goToLoginPage() {
    this.navCtrl.pop();
  }
  //返回
  goBack() {
    this.navCtrl.pop();
  }

  ionViewWillLeave() {
    clearInterval(this.time);
  }
  //获取验证码
  getVerifyCode() {
    let r = /^\+?[1-9][0-9]*$/;
    if (this.registerInput.mobile == "") {
      this.alertMsgService.showMsg("请输入手机号");
      return;
    }
    else if (!r.test(this.registerInput.mobile) || this.registerInput.mobile.length != 11) {
      this.alertMsgService.showMsg("请输入正确的手机格式");
      return;
    }
    let postData = {
      mobile: this.registerInput.mobile,
    };
    this.httpService.httpPost(this.httpService.getVerifyUrl, postData, this.getVerifySuccess);
  }
  //获取验证码回调
  getVerifySuccess = (response) => {
    console.log(response);
    this.alertMsgService.closeLoading();
    if (response.status == ApiConfig.successCode) {
      this.state = true;
      let second = 59;
      this.getCodeText = second + "秒...";
      this.time = setInterval(() => {
        this.getCodeText = second + "秒...";
        second--;
        if (second < 0) {
          this.getCodeText = "获取验证码";
          this.state = false;
          clearInterval(this.time);
        }
      }, 1000)
    }
  }
};




