import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { ViewController } from 'ionic-angular';
import {HttpService} from "../../app/service/http.service";
import {ApiConfig} from "../../app/app.api.config";
/**
 * Generated class for the Projectdevice3Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-projectdevice3',
  templateUrl: 'projectdevice3.html',
})
export class Projectdevice3Page {
    public siteItem=[];
  constructor(public navCtrl: NavController,
              public viewCtrl: ViewController,
              public httpService: HttpService,
              public navParams: NavParams) {

  }

    ionViewWillEnter(){
        this.getSiteList();
    }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Projectdevice3Page');
  }

  close(val){
    this.dismiss(val);
  }

  dismiss(val) {
      // You can send information back to the previous page if you need to
      let data = { "data":val};
      this.viewCtrl.dismiss(data);
  }

    //获取未处理任务数
    getSiteList(){
        let postData = {};
        this.httpService.httpPost(this.httpService.getSiteListUrl, postData, this.getSiteListSuccess);
    };

    getSiteListSuccess = (response) =>{
        if(response.status == ApiConfig.successCode){
            this.siteItem=response.data;
            for (let i in this.siteItem){
              if(!this.siteItem[i].siteName){
                this.siteItem[i].siteName = this.siteItem[i].siteCode
              }
            }
          console.log(this.siteItem)
        }

    };

}
