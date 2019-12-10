import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {HttpService} from "../../app/service/http.service";
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {ApiConfig} from "../../app/app.api.config";
import {Register4Page} from "../register4/register4";
import {TabsPage} from "../tabs/tabs";
import {LoginPage} from "../login/login";

/**
 * Generated class for the Register3Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-register3',
  templateUrl: 'register3.html',
})

export class Register3Page {

  userInput = {
    userName: "",
    phone: "",
    userCode:"",
  };

  public user_type;
  private registerInput;
  private user;
  private time;
  private state;
  private mobile;
  private code

  constructor(
    private alertMsgService: AlertMsgService,
    private httpService: HttpService,
    private navCtrl: NavController,
    public navParams: NavParams

  ) {
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
      password:'',
    };
    this.state = false;
  }

  ionViewDidLoad() {
    this.mobile = this.navParams.data.mobile;
    this.code = this.navParams.data.code;
  }

  setPassword(){
    if(this.registerInput.password.length < 3){
      this.alertMsgService.showMsg("密码长度太短");
      this.registerInput.password = "";
      return;
    }
    let postData = {
      mobile: this.mobile,
      ncode:this.code,
      password:this.registerInput.password
    };
    this.httpService.httpPost(this.httpService.forgetPasswordChangeUrl, postData, this.changePasswordSuccess);
  }

  changePasswordSuccess =(response)=>{
    if(response.status == ApiConfig.successCode) {
      this.alertMsgService.showMsg("设置新密码成功");
      this.navCtrl.push(LoginPage, null);
    }
    else{
      this.alertMsgService.showMsg(response.message);
    }
  };

  //注册成功回调
  getRegisterSuccess = (response) => {
    console.log(response);
    this.alertMsgService.closeLoading();
    if (response.status == ApiConfig.successCode) {
      this.alertMsgService.showMsg("注册成功");
      this.navCtrl.pop();
    }
  };

  //返回
  goBack() {
    this.navCtrl.pop();
  }

  ionViewWillLeave() {
    clearInterval(this.time);
  }

}
