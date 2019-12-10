import {Component} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';
import {Info1Page} from "../info1/info1";
import {DevicePage} from "../device/device";


import {Projectdevice2Page} from '../projectdevice2/projectdevice2';
import {Projectdevice3Page} from '../projectdevice3/projectdevice3';
import {PopoverController, ModalController} from 'ionic-angular';
import {SearchPage} from "../search/search";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

/**
 * Generated class for the Projectdevice1Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-projectdevice1',
  templateUrl: 'projectdevice1.html',
})
export class Projectdevice1Page {
  public pageParam = {
    page: 1,
    isPull: false,
    page_size: 6
  };
  public isMore = false;
  public userInfo = {};
  public header = '电弧探测器';
  public header1 = '全部';
  public header2 = '全部';
  public typeone;
  public typetwo;
  public accepted;
  public selectShow = false;
  public selectShow2 = false;
  public selectShow1 = false;
  public headerItems = [
    {value: -1, name: '中控', selected: false},
    {value: 0, name: '电弧探测器', selected: true},
    {value: 1, name: '组合式探测器', selected: false},
  ];
  public headerItems1 = [
    {value: -2, name: '全部', selected: true},
    {value: -1, name: '正常', selected: false},
    {value: 0, name: '报警', selected: false},
    {value: 1, name: '故障', selected: false},
    {value: 2, name: '离线', selected: false},
  ];
  public headerItems2 = [
    {value: -1, name: '全部', selected: true},
    {value: 1, name: '有线', selected: false},
    {value: 0, name: '无线', selected: false},
  ];
  siteInfo = {
    id: '',
    siteName: '项目名称',
    sitePlace: '地址',
    siteCode: ''
  };
  dealAlarmCount = 0;
  dealFaultCount = 0;
  siteName = '';
  equipType = -1;
  warnIndex = -2;
  isMqttEquip = -1;
  siteCode = "";
  total = 0;
  pageSum = 0;
  projectItem = [
    /* {
         addr: null,
         deviceCode: "01",
         id: "0e2b000201",
         isDevice: 0,
         lineCode: null,
         message: null,
         niName: null,
         node: null,
         place: null,
         siteCode:null,
         siteName: null,
         specificator: "华创园",
         warnIndex: "1",
     }*/
  ];

  constructor(
    public navCtrl: NavController,
    public popoverCtrl: PopoverController,
    public modal: ModalController,
    public httpService: HttpService,
    public navParams: NavParams
  ) {
    this.header = '电弧探测器'
  }

  ionViewWillEnter() {
    this.equipType = -1;
    this.warnIndex = -2;
    this.isMqttEquip = -1;
    this.getSiteList();
  }

  presentPopover(myEvent) {
    //this.selectShow2=true;
    this.userInfo = JSON.parse(sessionStorage.getItem('userInfo'));
    let data = {'foo': ''};
    let popover = this.popoverCtrl.create(Projectdevice3Page, data, {cssClass: 'custom-popover'});

    popover.onDidDismiss((data) => {
      if (data) {
        // this.saveItem(data);
        console.log("Data =>", data)
      }
      console.log("Data =>", data)
    });

    popover.present({ev: myEvent});

  }

  model() {
    this.pageParam.page = 1;
    let data = {'foo': ''};
    let model = this.modal.create(Projectdevice3Page);
    model.onDidDismiss(data => {
      console.log(data);
      if (data.data) {
        this.siteCode = data.data.siteCode
        this.getSiteInfo();
        this.getDealCount();
        this.getDeviceList()
        this.siteName = data.data.siteName
      }
    });
    model.present()
    this.isMore = false;
  }

  goSearch() {
    console.log(this.siteCode);
    console.log(this.equipType);
    this.navCtrl.push(SearchPage, {siteCode:this.siteCode,equipType:this.equipType});
  }

  ionViewDidLoad() {

  }


  showInfo1() {
    this.navCtrl.push(Info1Page, null);
  }

  showDevice(item) {
    item.siteName = this.siteName;
    item.equipType = this.equipType;
    this.navCtrl.push(Projectdevice2Page, item);
  }

  headerSelect() {
    this.selectShow = this.selectShow?false:true;
    this.selectShow1 = false;
    this.selectShow2 = false;
  }

  headerSelect1() {
    this.selectShow1 = this.selectShow1?false:true;
    this.selectShow2 = false;
  }

  headerSelect2() {
    this.selectShow1 = false;
    this.selectShow2 = this.selectShow2?false:true;
  }

  selectClose() {
    this.selectShow = false;
    this.selectShow1 = false;
    this.selectShow2 = false;
  }

  itemSelected(item) {
    this.selectShow = false;
    for (let i in this.headerItems) {
      this.headerItems[i].selected = false;
    }
    item.selected = true;
    this.header = item.name;
    this.getDeviceList();
    this.isMore = false;
  }

  itemSelected1(item) {
    this.selectShow1 = false;
    for (let i in this.headerItems1) {
      this.headerItems1[i].selected = false;
    }
    item.selected = true;
    this.header1 = item.name;
    this.getDeviceList();
    this.isMore = false;
  }

  itemSelected2(item) {
    this.selectShow2 = false;
    for (let i in this.headerItems2) {
      this.headerItems2[i].selected = false;
    }
    item.selected = true;
    this.header2 = item.name;
    this.getDeviceList();
    this.isMore = false;
  }

  projectChange() {
    this.selectShow2 = false;
  }

  //获取项目列表
  getSiteList() {
    let postData = {};
    this.httpService.httpPost(this.httpService.getSiteListUrl, postData, this.getSiteListSuccess);
  };

  getSiteListSuccess = (response) => {
    if (response.status == ApiConfig.successCode && response.data.length > 0) {
      console.log(response)
      this.siteInfo = response.data[0];
      if(!this.siteInfo.siteName){
        this.siteInfo.siteName = this.siteInfo.siteCode;
      }

      this.siteCode = response.data[0].siteCode;
      this.getDealCount();
      this.getDeviceList();
      this.siteName = response.data[0].siteName;
    }

  };

  //获取项目详情
  getSiteInfo() {
    let postData = {
      siteCode: this.siteCode
    };
    this.httpService.httpPost(this.httpService.getSiteInfoUrl, postData, this.getSiteInfoSuccess);
  };

  getSiteInfoSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response)
      this.siteInfo = response.data;
      for (let i in this.siteInfo){
        if(!this.siteInfo.siteName){
          this.siteInfo.siteName = this.siteInfo.siteCode;
        }
      }
    }

  };

  //获取已处理任务数
  getDealCount() {
    let postData = {
      siteCode: this.siteCode,
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

  //获取设备列表
  getDeviceList() {
    this.pageParam.page = 1;
    for (let i in this.headerItems) {
      if (this.headerItems[i].selected) {
        this.equipType = this.headerItems[i].value
      }
    }
    for (let i in this.headerItems1) {
      if (this.headerItems1[i].selected) {
        this.warnIndex = this.headerItems1[i].value
      }
    }
    for (let i in this.headerItems2) {
      if (this.headerItems2[i].selected) {
        this.isMqttEquip = this.headerItems2[i].value
      }
    }
    let postData = {
      siteCode: this.siteCode,
      equipType: this.equipType,
      warnIndex: this.warnIndex,
      isMqttEquip: this.isMqttEquip,
      currentPage: this.pageParam.page,
      pageSize: this.pageParam.page_size
    };
    this.httpService.httpPost(this.httpService.getDeviceListUrl, postData, this.getDeviceListSuccess);
  };

  getDeviceListSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.isMore = false;
      this.total = response.data.total;
      this.pageSum = Math.ceil(this.total / this.pageParam.page_size);
      for (let item of response.data.deviceList) {
        item.deviceCode2 = Number(item.lineCode) + 1 + '-' + item.addr;
        item.warnIndex = item.warnIndex ? item.warnIndex.split(',')[0] : -1
      }
      if (this.pageSum > this.pageParam.page) {
        this.isMore = true;
      }
      else {
        this.isMore = false;
      }
      this.projectItem = response.data.deviceList
    }
  };

  //获取设备列表
  getNextDeviceList() {
    let postData = {
      siteCode: this.siteCode,
      equipType: this.equipType,
      warnIndex: this.warnIndex,
      isMqttEquip: this.isMqttEquip,
      currentPage: this.pageParam.page,
      pageSize: this.pageParam.page_size
    };
    this.httpService.httpPost(this.httpService.getDeviceListUrl, postData, this.getNextDeviceListSuccess);
  };

  getNextDeviceListSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.isMore = false;
      this.total = response.data.total;
      this.pageSum = Math.ceil(this.total / this.pageParam.page_size);
      for (let item of response.data.deviceList) {
        item.deviceCode2 = Number(item.lineCode) + 1 + '-' + item.addr;
        item.warnIndex = item.warnIndex ? item.warnIndex.split(',')[0] : -1
        this.projectItem.push(item)
      }
      if (this.pageSum > this.pageParam.page) {
        this.isMore = true;
      }
      else {
        this.isMore = false;
      }
    }
  };

  doInfinite = (e) => {
    if (this.isMore) {
      this.pageParam.page += 1;
    }
    this.getNextDeviceList();
    setTimeout(() => {

    }, 1000)
    setTimeout(() => {
      e.complete();
    }, 2000)
  }

}
