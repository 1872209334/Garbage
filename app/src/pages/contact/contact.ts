import { Component } from '@angular/core';
import { NavController,App } from 'ionic-angular';
import { LoginPage } from '../login/login';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {AlertMsgService} from "../../app/service/alert.msg.service";
import { ChangepasswordPage } from "../changepassword/changepassword";
import {HttpService} from "../../app/service/http.service";

@Component({
  selector: 'page-contact',
  templateUrl: 'contact.html'
})
export class ContactPage {

  constructor(
    public navCtrl: NavController,
    public app: App,
    public pagesToggleService : PagesToggleService,
    public alertMsgService  :AlertMsgService,
    public httpService: HttpService,
  ) {

  }

  private userInfo = {};
  ngOnInit(){
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    console.log(this.userInfo);
  }
  login_out(){
    this.alertMsgService.confirmAlert('', '确认退出账户吗',this.funCancle,this.funSuccess);
    // this.navCtrl.push(LoginPage, null, null, () => {
    //   this.app.getRootNav().setRoot(LoginPage);
    // })
  }

  //弹框取消
  funCancle(){

  }

  //注销
  funSuccess=()=>{
    this.httpService.httpGet(this.httpService.logoutUrl,this.logoutSuccess);
    localStorage.removeItem("token");
    localStorage.removeItem("userInfo");
    this.navCtrl.push(LoginPage, null, null, () => {
      this.app.getRootNav().setRoot(LoginPage);
    })
  };

  logoutSuccess=()=>{

  };

  changePwd(){
    this.navCtrl.push(ChangepasswordPage,null);
    //this.pagesToggleService.goToPage("ChangepasswordPage", null, "LeftAndRight");
  }
  checkVersion(){
    this.alertMsgService.showMsg('已是最新版本');
  }
}
