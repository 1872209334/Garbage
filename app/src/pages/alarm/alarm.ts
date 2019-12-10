import {Component} from '@angular/core';
import {IonicPage, NavController, LoadingController, NavParams} from 'ionic-angular';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {AlarmDetailPage} from "../alarmDetail/alarmDetail";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

@IonicPage()
@Component({
  selector: 'page-alarm',
  templateUrl: 'alarm.html'
})
export class AlarmPage {
  public title = "报警列表";
  public warningList = [/*{
      eid:'111111111',
      siteCode:'111111111111',
      warningTime:'46535435',
      address:'111111111'
  }*/];
  public isMore;
  public total;
  public pageSum;
  public pageParam = {
    page: 1,
    isPull: false,
    page_size: 10
  };
  deviceList = [];
  group = "group1";
  public selectShow3 = false;
  public selectShow2 = false;
  public selectShow1 = false;
  public header1 = '全部';
  public header2 = '中控';
  public header3 = '全部';
  public headerItems1 = [
    {value: -2, name: '全部', selected: true},
    {value: -1, name: '正常', selected: false},
    {value: 0, name: '报警', selected: false},
    {value: 1, name: '故障', selected: false},
    {value: 2, name: '离线', selected: false},
  ];
  public headerItems2 = [
    {value: 1, name: '中控', selected: true},
    {value: 2, name: '电弧探测器', selected: false},
    {value: 3, name: '组合式探测器', selected: false},
  ];
  public headerItems3 = [
    /*{value:1,name:'全部',selected:true},
    {value:2,name:'项目一',selected:false},
    {value:3,name:'项目二项目二项目二',selected:false},*/
  ];
  public warmType;
  public equipType;
  public warnIndex = -2;
  public siteCode;


  constructor(
    public navCtrl: NavController,
    private pagesToggleService: PagesToggleService,
    private alertMsgService: AlertMsgService,
    public httpService: HttpService,
    public navParams: NavParams
  ) {
    this.title = navParams.get('title');
    this.warmType = navParams.get('type');
      this.getSiteList();
  }

    ionViewWillEnter() {

    }


  getNextWarningList() {
    let postData = {
      limit: this.pageParam.page_size,
      page: this.pageParam.page
    };
    this.httpService.httpPost(this.httpService.getWarningListUrl, postData, this.getNextWarningSuccess);
  };

  getNextWarningSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      for (let i = 0; i < response.data.warning_list.length; i++) {
        this.warningList.push(response.data.warning_list[i]);
      }
    }
  };

  timestampToTime(timestamp) {
    let date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    let D = date.getDate() + ' ';
    let h = date.getHours() + ':';
    let m = date.getMinutes() + ':';
    let s = date.getSeconds();
    return Y + M + D + h + m + s;
  };

  goBack() {
    this.navCtrl.pop();
  }

  doRefresh(refresher) {
    this.pageParam.page = 1;
    this.getDeviceList();
    setTimeout(() => {
      refresher.complete();
    }, 1000)
    refresher.complete();
  }

  doInfinite = (e) => {
    setTimeout(() => {
      this.pageParam.page += 1;
      this.getNextDeviceList();
    }, 1000)
    setTimeout(() => {
      e.complete();
    }, 2000)
  };

  alarmDetail(item) {
    item.warmType = this.warmType;
    this.navCtrl.push(AlarmDetailPage, item);
  }

  headerSelect1() {
    this.selectShow1 = this.selectShow1?false:true;
    this.selectShow2 = false;
    this.selectShow3 = false;
  }

  headerSelect2() {
    this.selectShow1 = false;
    this.selectShow2 = this.selectShow2?false:true;
    this.selectShow3 = false;
  }

  headerSelect3() {
    this.selectShow1 = false;
    this.selectShow2 = false;
    this.selectShow3 = this.selectShow3?false:true;
  }

  selectClose() {
    this.selectShow3 = false;
    this.selectShow1 = false;
    this.selectShow2 = false;
  }

  itemSelected1(item) {
    this.selectShow1 = false;
    for (let i in this.headerItems1) {
      this.headerItems1[i].selected = false;
    }
    item.selected = true;
    this.header1 = item.name;
    this.getDeviceList();
  }

  itemSelected2(item) {
    this.selectShow2 = false;
    for (let i in this.headerItems2) {
      this.headerItems2[i].selected = false;
    }
    item.selected = true;
    this.header2 = item.name;
  }

  itemSelected3(item) {
    this.selectShow3 = false;
    for (let i in this.headerItems3) {
      this.headerItems3[i].selected = false;
    }
    item.selected = true;
    this.header3 = item.siteName;
    this.getDeviceList()
  }

  //获取项目列表
  getSiteList() {
    let postData = {};
    this.httpService.httpPost(this.httpService.getSiteListUrl, postData, this.getSiteListSuccess);
  };

  getSiteListSuccess = (response) => {
    this.headerItems3 = response.data;
    if (response.status == ApiConfig.successCode && response.data.length > 0) {
      for (let item of response.data) {
        item.selected = false
      }
      this.header3 = response.data[0].siteName;
      response.data[0].selected = true;
      console.log(response)
      this.headerItems3 = response.data;
      for (let i in this.headerItems3){
        if(!this.headerItems3[i].siteName){
          this.headerItems3[i].siteName = this.headerItems3[i].siteCode
        }
      }
      this.getDeviceList();
    }

  };

  //获取设备列表
  getDeviceList() {
    this.equipType = this.navParams.get('type');
    this.warnIndex = -2;
    for (let i in this.headerItems1) {
      if (this.headerItems1[i].selected) {
        this.warnIndex = this.headerItems1[i].value
      }
    }
    for (let i in this.headerItems3) {
      if (this.headerItems3[i].selected) {
        this.siteCode = this.headerItems3[i].siteCode
      }
    }
    let postData = {
      siteCode: this.siteCode,
      equipType: this.equipType,
      warnIndex: this.warnIndex,
      currentPage: 1,
      pageSize: this.pageParam.page_size
    };
    this.httpService.httpPost(this.httpService.getDeviceWarnListUrl, postData, this.getDeviceListSuccess);
  };

  getDeviceListSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response)
      for (let item of response.data.deviceWarnList) {
        item.warnTime = this.timestampToTime(item.warnTime)
        item.deviceCode2 = Number(item.lineCode) + 1 + '-' + item.addr;
      }
      this.deviceList = response.data.deviceWarnList
    }

  };

  getNextDeviceList() {
    this.equipType = this.navParams.get('type');
    this.warnIndex = -2;
    let postData = {
      siteCode: this.siteCode,
      equipType: this.equipType,
      warnIndex: this.warnIndex,
      currentPage: this.pageParam.page,
      pageSize: this.pageParam.page_size
    };
    this.httpService.httpPost(this.httpService.getDeviceWarnListUrl, postData, this.getNextDeviceListSuccess);
  };

  getNextDeviceListSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.isMore = false;
      this.total = response.data.total;
      this.pageSum = Math.ceil(this.total / this.pageParam.page_size);
      for (let item of response.data) {
        item.warnTime = this.timestampToTime(item.warnTime);
        item.deviceCode2 = Number(item.lineCode) + 1 + '-' + item.addr;
        this.deviceList.push(item);
      }
      if (this.pageSum > this.pageParam.page) {
        this.isMore = true;
      }
      else {
        this.isMore = false;
      }
    }
  };


}
