import {Component, OnInit} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';
import {HttpService} from "../../../app/service/http.service";
import {ApiConfig} from "../../../app/app.api.config";
import {AlertMsgService} from "../../../app/service/alert.msg.service";
import {PagesToggleService} from "../../../app/service/pages.toggle.service";

@IonicPage()
@Component({
  templateUrl: 'forget-password.html'
})
export class ForgetPasswordPage {
  private step = 1;
  private forgetInput;
  private user;
  private getCodeText = "获取验证码";
  private time;
  private title = "忘记密码";
  private state;

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              public httpService: HttpService,
              public alertMsgService: AlertMsgService,
              public pagesToggleService: PagesToggleService) {
  }

  ngOnInit() {
    this.forgetInput = {
      phone: '',
      code: '',
      newPwd: ''
    };
    this.user = {
      email: '',
      id: '',
      phone: '',
      pwd1: '',
      pwd2: ''
    };
    this.state = false;
  }


  //获取验证码
  getVerifyCode() {
    let r = /^\+?[1-9][0-9]*$/;
    if (this.forgetInput.phone == "") {
      this.alertMsgService.showMsg("请输入手机号");
      return;
    }
    else if (!r.test(this.forgetInput.phone) || this.forgetInput.phone.length != 11) {
      this.alertMsgService.showMsg("请输入正确的手机格式");
      return;
    }
    let postData = {
      mobile: this.forgetInput.phone,
    };
    this.httpService.httpPost(this.httpService.getPasswordCodeUrl, postData, this.getPasswordCodeSuccess);
  }

  //获取验证码回调
  getPasswordCodeSuccess = (response) => {
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
    else{
      this.alertMsgService.showMsg(response.message);
    }
  };

  //返回
  goBack() {
    this.navCtrl.pop();
  }

  ionViewWillLeave() {
    clearInterval(this.time);
  }

  //修改密码
  changePwd() {
    if(this.forgetInput.phone && this.forgetInput.code && this.forgetInput.newPwd){
      let postData = {
        mobile: this.forgetInput.phone,
        ncode: this.forgetInput.code,
        password: this.forgetInput.newPwd
      };
      this.httpService.httpPost(this.httpService.setPasswordUrl, postData, this.forgetPasswordSuccess);
    }
    else{
      this.alertMsgService.showMsg("请正确填写信息");
    }
  }

  forgetPasswordSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.alertMsgService.showMsg("修改密码成功");
      this.navCtrl.pop();
    }
    else{
      this.alertMsgService.showMsg(response.message);
    }
  };

}
