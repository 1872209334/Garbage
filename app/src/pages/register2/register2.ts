import {Component} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {HttpService} from "../../app/service/http.service";
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {ApiConfig} from "../../app/app.api.config";
import {Register3Page} from "../register3/register3";

/**
 * Generated class for the Register2Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-register2',
  templateUrl: 'register2.html',
})

export class Register2Page {

  title = '注册账号';
  getCodeText = "获取验证码";

  public user_type;
  private userCode1: "";
  private userCode2: "";
  private userCode3: "";
  private userCode4: "";

  private userInput;
  private registerInput;

  private user;
  private time;
  private state = false;
  public mobile;
  public code;
  constructor(
    private alertMsgService: AlertMsgService,
    private httpService: HttpService,
    private navCtrl: NavController,
    public navParams: NavParams
  ) {
    this.user_type = 1;
  }

  //constructor(public navCtrl: NavController, public navParams: NavParams) {
  //}

  ionViewWillEnter(){
    this.mobile = this.navParams.data.mobile;
    this.updateTime();
  }

  ngOnInit() {
    this.userInput = {
      userCode1: '',
      userCode2: '',
      userCode3: '',
      userCode4: ''
    };

    this.user = {
      email: '',
      id: '',
      user_login: '',
      pwd1: '',
      pwd2: ''
    };


  }

  //返回
  goBack() {
    this.navCtrl.pop();
  }

  updateTime(){
    this.state = true;
    let second = 59;
    this.getCodeText = second + "秒后可重新发送";
    this.time = setInterval(() => {
      this.getCodeText = second + "秒后可重新发送";
      second--;
      if (second < 0) {
        this.getCodeText = "发送验证码";
        this.state = false;
        clearInterval(this.time);
      }
    }, 1000);
  }



  focusInput(input,val) {
    console.log(input)
    if(val.length == 1){
      input.setFocus();
    }

  }

  verify() {
    if(this.userCode1 && this.userCode2 && this.userCode3 && this.userCode4){
      this.code = this.userCode1 + this.userCode2 + this.userCode3 + this.userCode4;
      let postData = {
        mobile: this.mobile,
        ncode:this.code
      };
      this.httpService.httpPost(this.httpService.verifyCodeUrl, postData, this.getVerifySuccess);

    }
    else{
      this.alertMsgService.showMsg("请正确输入验证码格式");
    }
  }

  getVerifySuccess = (response)=>{
    if(response.status == ApiConfig.successCode){
      this.navCtrl.push(Register3Page, {mobile:this.mobile,code:this.code});
    }
    else{
      this.alertMsgService.showMsg(response.message);
    }
  }

  //重新发送验证码
  resend(){
    let postData = {
      mobile: this.mobile,
    };
    this.httpService.httpPost(this.httpService.forgetPasswordCodeUrl, postData, this.forgetPasswordSuccess);
  }

  forgetPasswordSuccess =(response)=>{
    if(response.status == ApiConfig.successCode){
      this.updateTime();
    }
    else{
      this.alertMsgService.showMsg(response.message);
    }
  }

}
