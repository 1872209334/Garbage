import { Component } from '@angular/core';
import {App, IonicPage, NavController, NavParams} from 'ionic-angular';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

/**
 * Generated class for the Info3Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-info3',
  templateUrl: 'info3.html',
})
export class Info3Page {

//  constructor(public navCtrl: NavController, public navParams: NavParams) {  }

  constructor(
    public navCtrl: NavController,
    public app: App,
    public pagesToggleService : PagesToggleService,
    public alertMsgService  :AlertMsgService,
    public httpService: HttpService,
  ) {

  }

  private userInfo = {};
  private changeMobileInput= {
    phone: '',
    code:'',
  };
  getCodeText = "发送验证码";
  time;
  isEdit = false;
  ionViewDidLoad() {
  }

  changeMobile(){
    if(this.isEdit == true){
      let postData = {
        newMobile: this.changeMobileInput.phone,
        ncode:this.changeMobileInput.code
      };
      this.httpService.httpPost(this.httpService.changeMobileUrl, postData, this.changeUserMobileSuccess);
    }


  }
  changeUserMobileSuccess =(response)=>{
    if (response.status == ApiConfig.successCode) {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      userInfo.mobile = this.changeMobileInput.phone;
      localStorage.setItem("userInfo",JSON.stringify(userInfo));
      this.alertMsgService.showMsg("修改成功");
      this.navCtrl.pop();
    }
    else{
      this.alertMsgService.showMsg(response.message);
      this.changeMobileInput.phone = "";
      this.changeMobileInput.code = "";
    }
  };
  changeMobileCode(){
    let r = /^\+?[1-9][0-9]*$/;
    if (this.changeMobileInput.phone == "") {
      this.alertMsgService.showMsg("请输入手机号");
      return;
    }
    else if (!r.test(this.changeMobileInput.phone) || this.changeMobileInput.phone.length != 11) {
      this.alertMsgService.showMsg("请输入正确的手机格式");
      return;
    }
    let postData = {
      mobile: this.changeMobileInput.phone,
    };
    this.httpService.httpPost(this.httpService.changeMobileCodeUrl, postData, this.changeMobileCodeSuccess);
  }

  changeMobileCodeSuccess =(response)=>{
    if (response.status == ApiConfig.successCode) {
      let second = 59;
      this.getCodeText = second + "秒...";
      this.time = setInterval(() => {
        this.getCodeText = second + "秒...";
        second--;
        if (second < 0) {
          this.getCodeText = "发送验证码";
          clearInterval(this.time);
        }
      }, 1000)
    }
    else{
      this.alertMsgService.showMsg(response.message);
    }
  }
  changeState(){
    if(this.changeMobileInput.phone && this.changeMobileInput.code){
      this.isEdit = true;
    }
    else{
      this.isEdit = false;
    }
  }


}
