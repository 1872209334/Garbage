import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams,App } from 'ionic-angular';
import { PagesToggleService } from "../../app/service/pages.toggle.service";
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {LoginPage} from "../login/login";

/**
 * Generated class for the ChangepasswordPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-changepassword',
  templateUrl: 'changepassword.html',
})
export class ChangepasswordPage {

  title = "修改密码";
  userInput = {
    userName: "",
    phone: "",
    oldPassword:"",
    newPassword: "",
    confirmPassword: "",
    sex: ""
  };

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    private alertMsgService: AlertMsgService,
    public app: App,
  ) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ChangepasswordPage');
  }

  changePwd(){
      if(this.userInput.newPassword != this.userInput.confirmPassword ) {
        this.alertMsgService.showMsg("两次输入密码不同");
      }
      else {
        this.alertMsgService.showMsg("修改成功");
        this.navCtrl.push(LoginPage, null, null, () => {
          this.app.getRootNav().setRoot(LoginPage);
        })
      }
  }

  goBack() {
    this.navCtrl.pop();
  }

}
