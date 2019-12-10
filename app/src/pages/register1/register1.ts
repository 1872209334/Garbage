import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

import {AlertMsgService}    from '../../app/service/alert.msg.service';
import {ApiConfig}          from '../../app/app.api.config';
import {HttpService}        from '../../app/service/http.service';
import {PagesToggleService} from '../../app/service/pages.toggle.service';
import {Register2Page}      from "../register2/register2";

/**
 * Generated class for the Register1Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-register1',
  templateUrl: 'register1.html',
})

export class Register1Page {

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
    this.registerInput = {
      mobile: '',
    };
    this.state = false;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RegisterPage');

  }

  nextStep() {
    let r = /^1[3|4|5|7|8|9][0-9]\d{8}$/;;
    if (this.registerInput.mobile == "") {
      this.alertMsgService.showMsg("请输入手机号");
      return;
    }
    else if (!r.test(this.registerInput.mobile)) {
      this.alertMsgService.showMsg("请输入正确的手机格式");
      return;
    }
    this.getVerifyCode();
  }

  //获取验证码
  getVerifyCode() {
    let postData = {
      mobile: this.registerInput.mobile,
    };
    this.httpService.httpPost(this.httpService.forgetPasswordCodeUrl, postData, this.getVerifySuccess);
  }

  getVerifySuccess =(response)=>{
    if(response.status == ApiConfig.successCode){
      this.navCtrl.push(Register2Page, {mobile:this.registerInput.mobile});
    }
    else{
      this.alertMsgService.showMsg(response.message);
    }
  };

  //返回
  goBack() {
    this.navCtrl.pop();
  }

  test(){
    console.log("asd");
    this.registerInput.mobile = this.registerInput.mobile.replace(/[^\d]/g,'');
  }

}
