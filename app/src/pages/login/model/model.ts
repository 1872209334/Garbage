import {Component} from '@angular/core';
import {IonicPage, NavController, NavParams, App} from 'ionic-angular';
import {HttpService} from '../../../app/service/http.service';
import {ApiConfig} from '../../../app/app.api.config';
import {TabsPage} from "../../tabs/tabs";


/**
 * Generated class for the ModelPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
    selector: 'page-model',
    templateUrl: 'model.html',
})
export class ModelPage {

    constructor(public app: App,
                public httpService: HttpService,
                public navCtrl: NavController,
                public navParams: NavParams) {
    }

    ionViewDidLoad() {
        console.log('ionViewDidLoad ModelPage');
    }

    doLogin = () => {
        // console.log(this.navParams);
        // console.log(this.navParams.data.newPassword);
        // console.log(this.navParams.data.userName);

        let postData = {
            userName: this.navParams.data.phone,
            Password: this.navParams.data.newPassword
        }
       // this.httpService.httpPost(ApiConfig.mobileUrl + 'login', postData, this.loginComplete);

    }


    loginComplete = (data) => {
        if (data.error == ApiConfig.successCode) {
            localStorage.setItem("userInfo", JSON.stringify(data.data));
            this.navCtrl.push(TabsPage, null, null, () => {
                this.app.getRootNav().setRoot(TabsPage);
            })

        }
    }

}
