import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";
import {Projectdevice2Page} from "../projectdevice2/projectdevice2";

/**
 * Generated class for the SearchPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-search',
  templateUrl: 'search.html',
})
export class SearchPage {
  public isMore = false;
  public deviceText = "";
  public projectItem = [];
  public navParam = {
    siteCode: "",
    equipType: 0
  };
  public pageParam = {
    page: 1,
    isPull: false,
    page_size: 10
  };
  public total = 0;
  public pageSum = 0;

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public httpService: HttpService,
  ) {
  }

  ionViewDidLoad() {
    this.navParam = this.navParams.data;
    console.log(this.navParam)
  }

  getItems() {
    if (this.deviceText != "") {
      let postData = {
        siteCode: this.navParam.siteCode,
        equipType: this.navParam.equipType,
        inquire: this.deviceText,
        currentPage: this.pageParam.page,
        pageSize: this.pageParam.page_size
      };
      this.httpService.httpPost(this.httpService.findDeviceUrl, postData, this.findDeviceSuccess);
    }
  }

  findDeviceSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.projectItem = response.data.deviceList;
      this.total = response.data.total;
      for (let i in this.projectItem) {
        this.projectItem[i].deviceCode2 = parseInt(this.projectItem[i].lineCode) + 1 + "-" + this.projectItem[i].addr;
      }
    }
  };

  showDevice(item) {
    item.equipType = this.navParam.equipType;
    this.navCtrl.push(Projectdevice2Page, item);
  };

  doInfinite = (e) => {
    if (this.isMore) {
      this.pageParam.page += 1;
    }
    this.getNextItems()
    setTimeout(() => {

    }, 1000)
    setTimeout(() => {
      e.complete();
    }, 2000)
  }

  getNextItems() {
    if (this.deviceText != "") {
      let postData = {
        siteCode: this.navParam.siteCode,
        equipType: this.navParam.equipType,
        inquire: this.deviceText,
        currentPage: this.pageParam.page,
        pageSize: this.pageParam.page_size
      };
      this.httpService.httpPost(this.httpService.findDeviceUrl, postData, this.getNextItemsSuccess);
    }
  }

  getNextItemsSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.isMore = false;
      this.total = response.data.total;
      this.pageSum = Math.ceil(this.total / this.pageParam.page_size);
      for (let i in this.projectItem) {
        this.projectItem[i].deviceCode2 = parseInt(this.projectItem[i].lineCode) + 1 + "-" + this.projectItem[i].addr;
      }
      this.projectItem.push(response.data.deviceList);
      if (this.pageSum > this.pageParam.page) {
        this.isMore = true;
      }
      else {
        this.isMore = false;
      }
    }

  }
}
