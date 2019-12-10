/**
 * 弹窗提示封装
 *
 * */
import {Injectable} from '@angular/core';
import {ToastController, LoadingController, AlertController} from "ionic-angular";

@Injectable()
// ToastService
export class AlertMsgService {
  constructor(
    private toastCtrl: ToastController,
    private alertCtrl: AlertController,
    private loadingCtrl: LoadingController
  ) {
  }

  public loading;
  showMsg(msg: string, position = "bottom", time = 2000) {
    let toast = this.toastCtrl.create({
      message: msg,
      position: position,
      duration: time,
      cssClass: 'toast'
    });
    toast.present();
  }

  basicAlert(title: string, msg: string, buttonsText = "确认") {
    let alert = this.alertCtrl.create({
      title: title,
      subTitle: msg,
      buttons: [buttonsText]
    });
    alert.present();
  }

  confirmAlert(title: string, msg: string, funLeft, funRight, buttonsLeft = "取消", buttonsRight = "确认") {
    let alert = this.alertCtrl.create({
      title: title,
      message: msg,
      buttons: [
        {
          text: buttonsLeft,
          role: 'cancel',
          handler: funLeft
        },
        {
          text: buttonsRight,
          handler: funRight,
          cssClass:'confirmYes'
        }
      ]
    });
    alert.present();
  }

  submitForm(title: string, subTitle: string, placeholder: string, funLeft, funRight, buttonsLeft = "取消", buttonsRight = "确认") {
    let alert = this.alertCtrl.create({
      title: title,
      subTitle: subTitle,
      inputs: [
        {
          name: 'value',
          placeholder: placeholder
        }
      ],
      buttons: [
        {
          text: buttonsLeft,
          role: 'cancel',
          handler: funLeft
        },
        {
          text: buttonsRight,
          handler: funRight
        }
      ]
    });
    alert.present();
  }
  //显示加载效果
  showLoading() {
    this.loading = this.loadingCtrl.create({
      spinner: 'hide',
      content: "<div class='loading'></div>",
      dismissOnPageChange: true, // 是否在切换页面之后关闭loading框
      showBackdrop: true  //是否显示遮罩层
    });
    this.loading.present();
  }
  //取消加载效果
  closeLoading(){
    if(this.loading){
      this.loading.dismiss();
    }
  }
}
