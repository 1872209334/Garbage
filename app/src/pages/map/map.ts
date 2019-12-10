//import { Component } from '@angular/core';
//import { NavController } from 'ionic-angular';
import {Component, ViewChild, ElementRef} from '@angular/core';
import {App, NavController} from "ionic-angular";
import {TabsPage} from "../tabs/tabs";
import {LoadingController} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';

import {ControlAnchor, NavigationControlType} from 'angular2-baidu-map';
import {Animation, MapOptions, BMarker, BMapInstance, MarkerOptions, Point} from 'angular2-baidu-map'
import {NavigationControlOptions, ScaleControlOptions, BNavigationControl} from 'angular2-baidu-map'
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {DevicePage} from "../device/device";
import {Info1Page} from "../info1/info1";
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";


declare var BMap;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html'
})

export class MapPage {
  title = '地图';
  public opts: {};
  public markers = [];
  public controlOpts: NavigationControlOptions;
  public scaleOpts: ScaleControlOptions;
  public mapBoxShow = true;
  public mapList = [];
  public projectInfo = {
    countData:{},
    id:0,
    place:"",
    siteCode:"",
    siteName:"",
    latitude:"",
    longitute:""
  };
  public icon_normal = "https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location.png";
  public icon_alarm = "https://lys-arc.oss-cn-hangzhou.aliyuncs.com/map_icon_location_red.png";
  public item = {
    msg1:1,
    msg2:1,
    msg3:1,
    msg4:1,
  };

  constructor(
    public navCtrl: NavController,
    public httpService: HttpService,
  ) {

    this.opts = {
      centerAndZoom: {
        // lat: 0,
        // lng: 0,
        // zoom: 15
      }
    };

    this.controlOpts = {
      anchor: ControlAnchor.BMAP_ANCHOR_TOP_LEFT,
      type: NavigationControlType.BMAP_NAVIGATION_CONTROL_LARGE
    };

    this.scaleOpts = {
      anchor: ControlAnchor.BMAP_ANCHOR_BOTTOM_LEFT
    };

  }
  private userInfo = {
    adminMobile:""
  };

  ionViewWillEnter() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    this.getMapDetail();
  }

  //获取未处理任务数
  getMapDetail() {
    let postData = {};
    this.httpService.httpPost(this.httpService.mapDetailUrl, postData, this.getMapDetailSuccess);
  };

  getMapDetailSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      console.log(response);
      this.mapList = response.data;
      if(this.mapList.length > 0){
        for (let i in this.mapList) {
          if(!this.mapList[i].latitude){
            this.mapList[i].latitude = this.mapList[i].latBmap;
          }
          if(!this.mapList[i].longitute){
            this.mapList[i].longitute = this.mapList[i].lngBmap;
          }
          if(!this.mapList[i].siteName){
            this.mapList[i].siteName = this.mapList[i].siteCode;
          }
          if (this.mapList[i].warnNo) {
            let config = {
              options: {
                icon: {
                  imageUrl: this.icon_alarm,
                  size: {
                    height: 35,
                    width: 35
                  },
                  imageSize: {
                    height: 35,
                    width: 35
                  }
                }
              },
              point: {
                lat: this.mapList[i].latitude,
                lng: this.mapList[i].longitute
              },
              info:{
                id:this.mapList[i].id,
                place:this.mapList[i].place,
                siteName:this.mapList[i].siteName,
                siteCode:this.mapList[i].siteCode,
                countData:this.mapList[i].countData
              }
            };
            this.markers.push(config);
          }
          else {
            let config = {
              options: {
                icon: {
                  imageUrl: this.icon_normal,
                  size: {
                    height: 35,
                    width: 35
                  },
                  imageSize: {
                    height: 35,
                    width: 35
                  }
                }
              },
              point: {
                lat: this.mapList[i].latitude,
                lng: this.mapList[i].longitute
              }
            };
            this.markers.push(config);
          }
          if (this.mapList[i].longitute && this.mapList[i].latitude) {
            this.opts = {
              centerAndZoom: {
                lat: this.mapList[i].latitude,
                lng: this.mapList[i].longitute,
                zoom: 15
              }
            };
            this.projectInfo = this.mapList[i];
          }

        }
      }
      else{
        this.opts = {
          centerAndZoom: {
            lat: 118.305452,
            lng: 26.257854,
            zoom: 15
          }
        };
      }

    }
  };

  public setAnimation(marker: BMarker): void {
    marker.setAnimation(Animation.BMAP_ANIMATION_BOUNCE)
  }

  showWindow(pointInfo, markers){
    this.projectInfo = markers.info;
    this.opts = {
      centerAndZoom: {
        lat: pointInfo.marker.point.lat,
        lng: pointInfo.marker.point.lng,
        zoom: 15
      }
    };
  }


  public controlLoaded(control: BNavigationControl): void {
    console.log('control loaded', control)
  }

  showInfo1() {
    this.navCtrl.push(Info1Page, null);
  }

  tel(){
    let tel_str = "tel:" + this.userInfo.adminMobile;
    console.log(tel_str);
    document.location.href = tel_str;
  }


}
