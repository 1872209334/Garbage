import {Component} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

/**
 * Generated class for the Projectdevice2Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-projectdevice2',
  templateUrl: 'projectdevice2.html',
})
export class Projectdevice2Page {
  public item = {
    equipType: 0,
    id:""
  };
  public info = {
    node:""
  };

  constructor(
    public navCtrl: NavController,
    public httpService: HttpService,
    public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Projectdevice2Page');
  }

  ionViewWillEnter() {
    this.item = this.navParams.data;
    console.log(this.navParams);
    this.getDeviceInfo();
  }

  //获取设备详情
  getDeviceInfo() {
    let postData = {
      equipType: this.item.equipType,
      id: this.item.id
    };
    this.httpService.httpPost(this.httpService.getDeviceInfoUrl, postData, this.getDeviceInfoSuccess);
  };

  getDeviceInfoSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response)
      this.info = response.data;
    }
  };

}
