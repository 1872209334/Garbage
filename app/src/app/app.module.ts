import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler, Config, NavController } from 'ionic-angular';
import { MyApp } from './app.component';
import { EventBus} from "./service/eventBus.service";
import { AlertMsgService} from './service/alert.msg.service';
import { MapPage } from '../pages/map/map';
import { ContactPage } from '../pages/contact/contact';
import { MessagePage } from '../pages/message/message';
import { HomePage } from '../pages/home/home';
import { DevicePage} from "../pages/device/device";
import { BrokenPage} from "../pages/broken/broken";
import { HistoryPage} from "../pages/history/history";
import { AlarmPage} from "../pages/alarm/alarm";
import { TabsPage } from '../pages/tabs/tabs';
import { LoginPage} from "../pages/login/login";
import { RegisterPage} from "../pages/login/register/register";
import { Register1Page} from "../pages/register1/register1";
import { Register2Page} from "../pages/register2/register2";
import { Register3Page} from "../pages/register3/register3";
import { Register4Page} from "../pages/register4/register4";
import { Register5Page} from "../pages/register5/register5";
import { ForgetPasswordPage} from "../pages/login/forget-password/forget-password";
import { ChangepasswordPage } from "../pages/changepassword/changepassword";
import { AlarmDetailPage} from "../pages/alarmDetail/alarmDetail";
import { DeviceDetailPage} from "../pages/deviceDetail/deviceDetail";
import { BrokenDetailPage} from "../pages/brokenDetail/brokenDetail";
import { HistoryDetailPage} from "../pages/historyDetail/historyDetail";
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { HttpClientModule} from "@angular/common/http";
import { HttpService} from './service/http.service';
import { PagesToggleService} from './service/pages.toggle.service';
import { ModalLeftAnimation} from './animation/modal.left.animation';
import { ModalRightAnimation} from './animation/modal.right.animation';
import { JPush } from '@jiguang-ionic/jpush';
import { JPushService} from "./service/jpush.service";
import { BaiduMapModule } from 'angular2-baidu-map'
import { BackButtonService} from "./service/backButton.service";
import { Camera} from "@ionic-native/camera";

import { Info1Page} from "../pages/info1/info1";
import { Info2Page} from "../pages/info2/info2";
import { Info3Page} from "../pages/info3/info3";

import { Projectdevice1Page} from "../pages/projectdevice1/projectdevice1";
import { Projectdevice2Page} from "../pages/projectdevice2/projectdevice2";
import { Projectdevice3Page} from "../pages/projectdevice3/projectdevice3";
import { Projectdevice4Page} from "../pages/projectdevice4/projectdevice4";
import { Projectdevice5Page} from "../pages/projectdevice5/projectdevice5";

import { SearchPage} from "../pages/search/search";

@NgModule({
  declarations: [
    MyApp, MapPage, ContactPage, HomePage, MessagePage, TabsPage,
    LoginPage, DevicePage,BrokenPage,HistoryPage,AlarmPage,RegisterPage,
    Register1Page,Register2Page,Register3Page,Register4Page,Register5Page,
    Info1Page,Info2Page,Info3Page,
    Projectdevice1Page,Projectdevice2Page,Projectdevice3Page,Projectdevice4Page,Projectdevice5Page,
    ForgetPasswordPage,ChangepasswordPage,AlarmDetailPage,HistoryDetailPage,
    BrokenDetailPage,DeviceDetailPage,SearchPage
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp,{
      iconMode:'ios',
      mode:'ios',
      tabsHideOnSubPages:'true',
      backButtonText: ""
    }),
    BaiduMapModule.forRoot({ ak: 'A0cee24ac69c19c83ae7b452e7f04234' }),
    HttpClientModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp, MapPage,ContactPage, MessagePage, HomePage, TabsPage,
    LoginPage, DevicePage,BrokenPage,HistoryPage,AlarmPage,RegisterPage,
    Register1Page,Register2Page,Register3Page,Register4Page,Register5Page,
    Info1Page,Info2Page,Info3Page,
    Projectdevice1Page,Projectdevice2Page,Projectdevice3Page,Projectdevice4Page,Projectdevice5Page,
    ForgetPasswordPage,ChangepasswordPage,AlarmDetailPage,HistoryDetailPage,
    BrokenDetailPage,DeviceDetailPage,SearchPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    HttpService,
    PagesToggleService,
    BackButtonService,
    EventBus,
    AlertMsgService,
    JPush,
    JPushService,
    Camera,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
  ]
})
export class AppModule {
  constructor(public config: Config) {
    this.setCustomTransitions();
  }

  private setCustomTransitions() {
    this.config.setTransition('modal-left-animation', ModalLeftAnimation);
    this.config.setTransition('modal-right-animation', ModalRightAnimation);
  }
}
