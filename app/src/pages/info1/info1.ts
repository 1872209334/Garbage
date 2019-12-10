import { Component } from '@angular/core';
import {App, IonicPage, NavController, NavParams} from 'ionic-angular';
import {PagesToggleService}       from "../../app/service/pages.toggle.service";
import {AlertMsgService}          from "../../app/service/alert.msg.service";
import {HttpService}              from "../../app/service/http.service";
import {Info2Page}                from "../info2/info2";
import {LoginPage} from "../login/login";
import {ForgetPasswordPage} from "../login/forget-password/forget-password";

/**
 * Generated class for the Info1Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-info1',
  templateUrl: 'info1.html',
})

export class Info1Page {

  //constructor(public navCtrl: NavController, public navParams: NavParams) {  }
  constructor(
    public navCtrl: NavController,
    public app: App,
    public pagesToggleService : PagesToggleService,
    public alertMsgService  :AlertMsgService,
    public httpService: HttpService,
  ) {

  }

  private userInfo = {
    adminMobile:""
  };
  private registerInput= {
    username: '',
    password:'',
    adminAccount: '',
    mobile: '',
    code: '',
  };
  ngOnInit(){
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    console.log(this.userInfo);
  }
  ionViewDidLoad() {

  }

  showInfo1(){
    this.navCtrl.push(Info2Page, null);
  }

  resetPwd() {
    this.navCtrl.push(ForgetPasswordPage, null);
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

  tel(){
    let tel_str = "tel:" + this.userInfo.adminMobile;
    document.location.href = tel_str;
  }

}
