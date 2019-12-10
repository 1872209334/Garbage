import {Component} from '@angular/core';
import {IonicPage, NavController, ActionSheetController, NavParams, AlertController} from 'ionic-angular';
import {AlertMsgService} from "../../app/service/alert.msg.service";
import {Camera, CameraOptions} from "@ionic-native/camera";
import {HttpService} from "../../app/service/http.service";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {ApiConfig} from "../../app/app.api.config";


@IonicPage()
@Component({
  selector: 'page-alarmDetail',
  templateUrl: 'alarmDetail.html',

})
export class AlarmDetailPage {
  item = {
    id: '',
    siteName: "",
    siteCode: "",
    lineCode: "0",
    deviceCode: '',
    addr: "",
    warnId: 0,
    warnName:""
  };
  userInput = {
    handle: "",
  };
  imgList = [

  ];
  //图片上传
  imageParam = {
    imageUrl: "",
    imageData: "",
    isImage: false,
    imageType: 0,
    isDefault: true
  };

  title = '报警详情';
  path = "";
  deal_state = "0";

  // @ts-ignore
  constructor(
    public navCtrl: NavController,
    private alertMsgService: AlertMsgService,
    public actionSheetCtrl: ActionSheetController,
    public camera: Camera,
    public httpService: HttpService,
    public navParams: NavParams,
    public alertCtrl: AlertController,
    private http: HttpClient,
  ) {

  }


  ionViewWillEnter() {
    this.item = this.navParams.data;
    console.log(this.item)
  }

  select_img(pic_index) {
    let actionSheet = this.actionSheetCtrl.create({
      buttons: [
        {
          text: '拍照',
          handler: () => {
            this.openCamera(1,pic_index);
          }
        }, {
          text: '从相册中选取',
          handler: () => {
            this.openCamera(2,pic_index);
          }
        }, {
          text: '取消',
          role: 'cancel',
          handler: () => {
          }
        }
      ]
    });
    actionSheet.present();
  }

  //打开相机或相册
  openCamera(index,pic_index) {
    //相机
    if (index == 1) {
      this.imageParam.imageType = this.camera.PictureSourceType.CAMERA;
    }
    //相册
    else {
      this.imageParam.imageType = this.camera.PictureSourceType.PHOTOLIBRARY;
    }
    const options: CameraOptions = {
      quality: 70,
      sourceType: this.imageParam.imageType,
      destinationType: this.camera.DestinationType.DATA_URL,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE,
      allowEdit: true,
      targetWidth: 100,
      targetHeight: 100
    };

    // 获取图片
    this.camera.getPicture(options).then((imageData) => {
      let img = "data:image/jpeg;base64," + imageData;
      if(pic_index == -1){
        this.imgList.push(img);
      }
      else{
        this.imgList.splice(pic_index,1, img)
      }

    }, (err) => {
    });
  }


  uploadImage() {
    let imgStr = this.imgList.join(',');
    let formData = new FormData();
    formData.append('imgFile', imgStr);
    formData.append('warnId', this.item.warnId + "");
    formData.append('dealId', "");
    let token = localStorage.getItem('token');
    let header = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    let options = {
      headers:header
    };
    this.http.post<HttpResponse<any>>(this.httpService.dealFeedbackUrl, formData, options).subscribe(this.uploadSuccess);
  };

  uploadSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.alertMsgService.showMsg("提交成功");
      this.navCtrl.pop();
    }
  }

  goBack() {
    this.navCtrl.pop();
  }

  save() {
    this.uploadImage();
  }
  //有线中控消音
  clearEfm() {
    let postData = {
      protocolName: 'udp_clear_voice',
      siteCode: this.item.siteCode,
      deviceCode: this.item.deviceCode,
      extraData: '0000'

    };

    this.httpService.httpPost(this.httpService.sendSocketMessageUrl, postData, this.xiaoyingSuccess);
  }

  //有线终端消音
  clearEquip() {
    let postData = {
      protocolName: 'udp_clear_voice',
      siteCode: this.item.siteCode,
      deviceCode: this.item.deviceCode,
      extraData: this.item.id.substring(this.item.id.length - 4)
    };
    if(this.item.warnName == '离线告警'){
      postData.extraData = "0000";
    }
    this.httpService.httpPost(this.httpService.sendSocketMessageUrl, postData, this.xiaoyingSuccess);
  }

  //无线终端
  clearMqtt() {
    let postData = {
      message: 'alarm_silencing',
      qos: 1,
      ID: '01' + this.item.id,
      type: 3,
      extraData: ''
    }

    this.httpService.httpPost(this.httpService.mqttRemoveWarningUrl, postData, this.xiaoyingSuccess);
  }

  xiaoyingSuccess = (response) => {
    if (response.status == ApiConfig.successCode) {
      this.alertMsgService.showMsg("消音成功");
    }
  }

}
