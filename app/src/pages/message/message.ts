import {Component} from '@angular/core';
import {NavController} from 'ionic-angular';
import {Info1Page} from "../info1/info1";
import {DevicePage} from "../device/device";
import {HistoryPage} from "../history/history";
import {AlarmPage} from "../alarm/alarm";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

@Component({
  selector: 'page-message',
  templateUrl: 'message.html'
})
export class MessagePage {

  title = '报警';
  efmAlarmCount = 0;
  equipCount0 = 0;
  equipCount1 = 0;

  constructor(
    public navCtrl: NavController,
    public httpService: HttpService,
  ) {

  }

  ionViewWillEnter() {
    this.getAlarmCount();
  }

  getAlarmCount(){
    let postData = {};
    this.httpService.httpPost(this.httpService.getAlarmCountUrl, postData, this.getAlarmCountSuccess);
  }

  getAlarmCountSuccess = (res) =>{
    if(res.status == ApiConfig.successCode){
      this.efmAlarmCount = res.data.deviceWarnCount;
      this.equipCount0 = res.data.arcWarnCount;
      this.equipCount1 = res.data.currentWarnCount;
    }
  };

  showInfo1() {
    this.navCtrl.push(Info1Page, null);
  }

  showDevice() {
    this.navCtrl.push(DevicePage, null);
  }

  goHistory() {
    this.navCtrl.push(HistoryPage, null);
  }

  goAlarm(type, title) {
    this.navCtrl.push(AlarmPage, {type: type, title: title});
  }


}
