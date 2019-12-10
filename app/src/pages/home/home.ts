import {Component} from '@angular/core';
import {IonicPage, NavController} from 'ionic-angular';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {DevicePage} from "../device/device";
import {HistoryPage} from "../history/history";
import {AlarmPage} from "../alarm/alarm";
import {BrokenPage} from "../broken/broken";
import {Info1Page} from "../info1/info1";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})

export class HomePage {
  // public title = "报警列表";
  public alarmList = [];
  public isMore;
  public pageParam = {
    page: 1,
    isPull: false,
    page_size: 6
  };
  public unDealCount = 0;
  public dealAlarmCount = 0;
  public dealFaultCount = 0;
  public homeInfo = {
    alarmOfArcEquipAlarm: 1,
    alarmOfCurrentEquipAlarm: 0,
    arcOfEquipCount: 0,
    currentOfEquipCount: 0,
    deviceCount: 0,
    faultCountOfAlarm: 0,
    faultOfArcEquipAlarm: 0,
    faultOfCurrentEquipAlarm: 0,
    offlineCountOfAlarm: 0,
    offlineOfArcEquipAlarm: 0,
    offlineOfCurrentEquipAlarm: 0,
    siteCount: 0,
  };


  constructor(
    public navCtrl: NavController,
    private pagesToggleService: PagesToggleService,
    public httpService: HttpService,
  ) {

  }

  ngOnInit() {

  }

  ionViewWillEnter() {
    this.getunDealCount();
    this.getDealCount();
    this.getDefault();
  }

  showInfo1() {
    this.navCtrl.push(Info1Page, null);
  }

  showDevice() {
    this.navCtrl.push(DevicePage, null);
  }

  showHistory() {
    this.navCtrl.push(HistoryPage, null);
  }

  showAlarm() {
    this.navCtrl.push(AlarmPage, null);
  }

  showBroken() {
    this.navCtrl.push(BrokenPage, null);
  }

  //获取未处理任务数
  getunDealCount() {
    let postData = {
      siteCode: '',
    };
    this.httpService.httpPost(this.httpService.getunDealCountUrl, postData, this.getunDealCountSuccess);
  };

  getunDealCountSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response)
      this.unDealCount = response.data;
    }

  };

  //获取已处理任务数
  getDealCount() {
    let postData = {
      siteCode: '',
    };
    this.httpService.httpPost(this.httpService.getDealCountUrl, postData, this.getDealCountSuccess);
  };

  getDealCountSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response)
      this.dealAlarmCount = response.data.dealAlarmCount;
      this.dealFaultCount = response.data.dealFaultCount;
    }

  };

  //获取设备信息
  getDefault() {
    let postData = {};
    this.httpService.httpPost(this.httpService.getDefaultUrl, postData, this.getDefaultSuccess);
  };

  getDefaultSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response)
      this.homeInfo = response.data;
    }

  };


}
