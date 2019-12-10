/**
 * 页面切换服务
 *
 * */
import { Injectable } from '@angular/core';
import {ToastController} from "ionic-angular";

@Injectable()
export class ToastService{
    constructor (
        private toastCtrl:ToastController
    ) {}

    showMsg(msg:string,position="middle",time=2000){
        let toast = this.toastCtrl.create({
            message: msg,
            position:position,
            duration: time
        });
        toast.present();
    }
}
