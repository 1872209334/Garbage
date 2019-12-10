import {Component} from '@angular/core';
import {IonicPage, NavController} from 'ionic-angular';
import {AlertMsgService} from "../../app/service/alert.msg.service";

@IonicPage()
@Component({
  selector: 'page-brokenDetail',
  templateUrl: 'brokenDetail.html'
})
export class BrokenDetailPage {

  userInput = {
    handle: "",
    note: ""
  };

  title = '故障详情';

  constructor(
    public navCtrl: NavController,
    private alertMsgService: AlertMsgService
  ) {

  }

  goBack() {
    this.navCtrl.pop();
  }

  save() {
    if (this.userInput.handle == "") {
      this.alertMsgService.showMsg("请输入处理情况!");
    }
    if ((this.userInput.handle != "") && (this.userInput.note != "")) {
      this.goBack();
    }
  }
}
