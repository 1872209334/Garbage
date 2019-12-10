import { Component } from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-historyDetail',
  templateUrl: 'historyDetail.html',
})
export class HistoryDetailPage {

  title = '处理详情';
  navParam = {} as any;
  fileList = [];
  constructor(
    public navCtrl: NavController,
    public navParams: NavParams
  ) {

  }

  ionViewWillEnter() {
    this.navParam = this.navParams.data;
    if(this.navParam.filePath){
      this.fileList = this.navParam.filePath.split(',');
    }
    console.log(this.navParam)
  }
  goBack() {
    this.navCtrl.pop();
  }

}
