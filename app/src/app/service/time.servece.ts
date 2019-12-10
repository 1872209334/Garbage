/*时间格式化*/
import {Injectable} from '@angular/core';
import {ToastController} from "ionic-angular";

@Injectable()
export class TimeService {
    constructor(private toastCtrl: ToastController) {
    }

    time(timeStamp) {
        let date = new Date();
        date.setTime(timeStamp * 1000);
        let y = date.getFullYear();
        let  m = date.getMonth() + 1;
        let d = date.getDate();
        let h = date.getHours();
        let minute = date.getMinutes();
        let second = date.getSeconds();
        return y + '/' + m + '/' + d;
    }
}