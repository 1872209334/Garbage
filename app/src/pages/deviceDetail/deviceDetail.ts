import { Component } from '@angular/core';
import { IonicPage,NavController,LoadingController, NavParams } from 'ionic-angular';
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

@IonicPage()
@Component({
  selector: 'page-deviceDetail',
  templateUrl: 'deviceDetail.html'
})
export class DeviceDetailPage {
  public title = "设备详情";
  group = "";
  public deviceList = [];
  public isMore;
  public pageParam = {
    page: 1,
    isPull: false,
    page_size : 6,
  };
  public deviceInfo = {

  }
  constructor(
    public navCtrl: NavController,
    public httpService: HttpService,
    private navParams: NavParams
  ) {

  }
  ngOnInit() {
    let eid = this.navParams.get('eid');
    this.getDeviceInfo(eid);
    this.updateDeviceInfo(eid);
  }



  getDeviceInfo(eid){

    let postData1 = {
      eid:eid
    };
    this.httpService.httpPost(this.httpService.getTerminalInfoUrl, postData1, this.getDeviceInfoSuccess);

  }

  getDeviceInfoSuccess = (res) =>{
    if(res.status == ApiConfig.successCode){
      this.deviceInfo = res.data;
    }
  }

  updateDeviceInfo(eid) {
    let postData = {
      message: "mqttUpdateDeviceInfo",
      qos: 1,
      ID: eid,
      type: 1
    };
    this.httpService.httpPost(this.httpService.MqttDeviceInfoUrl, postData, this.updateDeviceInfoSuccess);
  }

  updateDeviceInfoSuccess = (res) =>{
    if(res.status == ApiConfig.successCode){

    }
  }


  goBack() {
    this.navCtrl.pop();
  }

}
