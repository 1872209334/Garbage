import {Component} from '@angular/core';
import {Platform} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';
import {LoginPage} from "../pages/login/login";
import {TabsPage} from "../pages/tabs/tabs";

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  rootPage: any;

  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen) {
    platform.ready().then(() => {
      if (localStorage.getItem("userInfo")) {
        this.rootPage = TabsPage;
      }
      else {
        this.rootPage = LoginPage;
      }
      statusBar.styleLightContent();
      setTimeout(() => {
        splashScreen.hide();
      }, 2000);
    });
  }
}
