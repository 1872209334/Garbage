import {Component} from '@angular/core';
import {IonicPage, NavController, LoadingController} from 'ionic-angular';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {HistoryDetailPage} from "../historyDetail/historyDetail";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

@IonicPage()
@Component({
  selector: 'page-history',
  templateUrl: 'history.html'
})
export class HistoryPage {
  public title = "历史记录";
  public historyList = [];
  public isMore;
  public pageParam = {
    page: 1,
    isPull: false,
    page_size: 6
  };
  public selectShow3 = false;
  public selectShow2 = false;
  public selectShow1 = false;
  public header1 = '全部';
  public header2 = '中控';
  public header3 = '无项目';
  public headerItems1 = [
    {value: -2, name: '全部', selected: true},
    {value: 0, name: '报警', selected: false},
    {value: 1, name: '故障', selected: false},
    {value: 2, name: '离线', selected: false},
  ];
  public headerItems2 = [
    {value: -1, name: '中控', selected: true},
    {value: 0, name: '电弧探测器', selected: false},
    {value: 1, name: '组合式探测器', selected: false},
  ];
  public headerItems3 = [];
  public siteCode = "";
  public equipType = -1;
  public warnIndex = -2;
  public startTime = "";
  public endTime = "";
  public total = 0;
  public pageSum = 0;



  constructor(
    public navCtrl: NavController,
    private pagesToggleService: PagesToggleService,
    public httpService: HttpService,
  ) {

  }

  ionViewWillEnter() {
    this.getSiteList();
  }

  getSiteList() {
    let postData = {};
    this.httpService.httpPost(this.httpService.getSiteListUrl, postData, this.getSiteListSuccess);
  }

  getSiteListSuccess = (response) => {
    if (response.status == ApiConfig.successCode && response.data.length > 0) {
      for (let item of response.data) {
        item.selected = false
      }
      response.data[0].selected = true;
      this.header3 = response.data[0].siteName;
      this.headerItems3 = response.data;
      for (let i in this.headerItems3){
        if(!this.headerItems3[i].siteName){
          this.headerItems3[i].siteName = this.headerItems3[i].siteCode
        }
      }
      this.getHistory();
    }

  };

  getHistory() {
    for (let item of this.headerItems3) {
      if (item.selected) {
        this.siteCode = item.siteCode;
      }
    }
    for (let item of this.headerItems2) {
      if (item.selected) {
        this.equipType = item.value;
      }
    }
    for (let item of this.headerItems1) {
      if (item.selected) {
        this.warnIndex = item.value;
      }
    }
    let postData = {
      siteCode: this.siteCode,
      equipType: this.equipType,
      warnIndex: this.warnIndex,
      currentPage: this.pageParam.page,
      pageSize: this.pageParam.page_size,
      startTime: this.startTime,
      endTime: this.endTime
    };
    this.httpService.httpPost(this.httpService.warnHistoryUrl, postData, this.getwarnHistorySuccess);
  }

  getwarnHistorySuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response.data.deviceWarnList)
      if(response.data.deviceWarnList){
        for (let item of response.data.deviceWarnList) {
          item.dealTime = this.timestampToTime(item.dealTime)
        }
      }
      this.historyList = response.data.deviceWarnList;
    }
  }

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

  doRefresh(refresher) {
    this.pageParam.page = 1;
    this.getHistory();
    setTimeout(() => {
      refresher.complete();
    }, 1000)
  }

  goBack() {
    this.navCtrl.pop();
  }

  doInfinite = (e) => {
    if (this.isMore) {
      this.pageParam.page += 1;
    }
    setTimeout(() => {
      this.getNextHistory();
    }, 1000)
    setTimeout(() => {
      e.complete();
    }, 2000)
  }

  historyDetail(row) {
    this.navCtrl.push(HistoryDetailPage, row);
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
    this.getHistory();
  }

  itemSelected2(item) {
    console.log(item)
    this.selectShow2 = false;
    for (let i in this.headerItems2) {
      this.headerItems2[i].selected = false;
    }
    item.selected = true;
    this.header2 = item.name;
    this.equipType = item.value;
    this.getHistory();
  }

  itemSelected3(item) {
    this.selectShow3 = false;
    for (let i in this.headerItems3) {
      this.headerItems3[i].selected = false;
    }
    item.selected = true;
    this.header3 = item.siteName;
    this.getHistory();
  }

  getNextHistory(){
    for (let item of this.headerItems3) {
      if (item.selected) {
        this.siteCode = item.siteCode;
      }
    }
    for (let item of this.headerItems2) {
      if (item.selected) {
        this.equipType = item.value;
      }
    }
    for (let item of this.headerItems1) {
      if (item.selected) {
        this.warnIndex = item.value;
      }
    }
    let postData = {
      siteCode: this.siteCode,
      equipType: this.equipType,
      warnIndex: this.warnIndex,
      currentPage: this.pageParam.page,
      pageSize: this.pageParam.page_size,
      startTime: this.startTime,
      endTime: this.endTime
    };
    this.httpService.httpPost(this.httpService.warnHistoryUrl, postData, this.getNextHistorySuccess);
  }
  getNextHistorySuccess =(response)=>{
    if(response.status == ApiConfig.successCode){
      this.isMore = false;
      this.total = response.data.total;
      this.pageSum = Math.ceil(this.total / this.pageParam.page_size);
      if(response.data.deviceWarnList){
        for (let item of response.data.deviceWarnList) {
          item.dealTime = this.timestampToTime(item.dealTime)
        }
      }
      this.historyList.push(response.data.deviceWarnList);
      if (this.pageSum > this.pageParam.page) {
        this.isMore = true;
      }
      else {
        this.isMore = false;
      }
    }
  }
  search(){
    console.log(this.startTime);
    console.log(this.endTime);
    if((this.startTime && this.endTime) || (this.startTime == "" && this.endTime == "")){
      this.getHistory();
    }
  }
}
