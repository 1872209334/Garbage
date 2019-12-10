/**
 * 页面切换服务
 *
 * */
import { Injectable } from '@angular/core';
import { ModalController } from "ionic-angular";

@Injectable()
export class PagesToggleService{
    constructor (
        private modalCtrl:ModalController
    ) {}

    goToPage(pageName,data = null,options = null){
        let myModal;
        if(options == "LeftAndRight"){
            //左右滑动
            options = {
                enterAnimation: 'modal-left-animation',
                leaveAnimation: 'modal-right-animation'
            }
            myModal = this.modalCtrl.create(pageName, data, options);
        }else{
            //上下滑动
            myModal = this.modalCtrl.create(pageName, data);
        }
        myModal.present();
    }
}
