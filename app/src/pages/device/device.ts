import {Component} from '@angular/core';
import {IonicPage, NavController, LoadingController} from 'ionic-angular';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {DeviceDetailPage} from "../deviceDetail/deviceDetail";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";

@IonicPage()
@Component({
  selector: 'page-device',
  templateUrl: 'device.html'
})
export class DevicePage {
  public title = "设备列表";
  public project = [];
  public projectId = 0;
  public projectAddress = "";
  public deviceList = [];
  public isMore;
  public pageParam = {
    page: 1,
    isPull: false,
    page_size: 6
  };

  constructor(
    public navCtrl: NavController,
    private pagesToggleService: PagesToggleService,
    private alertMsgService: AlertMsgService,
    public httpService: HttpService
  ) {

  }

  ngOnInit() {
    this.getProjectList();
  }

  //获取虚拟分组
  getProjectList() {
    let postData = {

    };
    this.httpService.httpPost(this.httpService.getProjectListUrl, postData, this.getProjectListSuccess);
  };

  getProjectListSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      if(response.data.project_type_list.length > 0){
        this.project = response.data.project_type_list;
        this.projectId = this.project[0].id;
        this.projectAddress = this.project[0].address;
        this.getProjectDevice();
      }
      else{
        this.alertMsgService.showMsg("没有分组权限");
        this.navCtrl.pop();
      }
    }
  };

  //获取虚拟分组下的设备
  getProjectDevice() {
    let postData = {
      pid: this.projectId,
      limit: this.pageParam.page_size,
      page: this.pageParam.page
    };
    this.httpService.httpPost(this.httpService.getProjectDeviceUrl, postData, this.getProjectDeviceSuccess);
  };

  getProjectDeviceSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.deviceList = response.data.device_list;
    }

  };



  doRefresh(refresher) {
    this.pageParam.page = 1;
    let postData = {
      pid: this.projectId,
      limit: this.pageParam.page_size,
      page: this.pageParam.page
    };
    this.httpService.httpPost(this.httpService.getProjectDeviceUrl, postData, this.getProjectDeviceSuccess, 0);
    setTimeout(() => {
      refresher.complete();
    }, 1000)
    // refresher.complete();
  }

  goBack() {
    this.navCtrl.pop();
  }

  //设备详情页
  deviceDetail(eid) {
    let param = {
      eid:eid
    }
    this.navCtrl.push(DeviceDetailPage, param);
    //this.pagesToggleService.goToPage("DeviceDetailPage", null, "LeftAndRight");
  }

  doInfinite = (e) => {
    setTimeout(() => {
      this.pageParam.page++;
      let postData = {
        pid: this.projectId,
        limit: this.pageParam.page_size,
        page: this.pageParam.page
      };
      this.httpService.httpPost(this.httpService.getProjectDeviceUrl, postData, this.getProjectNextDeviceSuccess, 0);
    }, 1000);
    setTimeout(() => {
      e.complete();
    }, 2000)
  };

  getProjectNextDeviceSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      for(let i = 0;i<response.data.device_list.length;i++){
        this.deviceList.push(response.data.device_list[i]);
      }
    }

  };

}
