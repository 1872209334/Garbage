import { Component } from '@angular/core';
import { App, IonicPage, NavController, NavParams} from 'ionic-angular';
import { PagesToggleService}  from "../../app/service/pages.toggle.service";
import { AlertMsgService}     from "../../app/service/alert.msg.service";
import { HttpService}         from "../../app/service/http.service";
import { Info3Page}           from "../info3/info3";
import {LoginPage} from "../login/login";

/**
 * Generated class for the Info2Page page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-info2',
  templateUrl: 'info2.html',
})
export class Info2Page {

  //constructor(public navCtrl: NavController, public navParams: NavParams) {  }

  constructor(
    public navCtrl: NavController,
    public app: App,
    public pagesToggleService : PagesToggleService,
    public alertMsgService  :AlertMsgService,
    public httpService: HttpService,
  ) {

  }

  private userInfo = {};
  ngOnInit(){
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    console.log(this.userInfo);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Info1Page');
  }

  changeMobilePage(){
    this.navCtrl.push(Info3Page, null);
  }


}
