import { Component,ViewChild } from '@angular/core';
import { Platform,Tabs} from 'ionic-angular';
import { MapPage } from '../map/map';

import { ContactPage } from '../contact/contact';
import { HomePage } from '../home/home';
import { MessagePage } from '../message/message';
import { BackButtonService} from "../../app/service/backButton.service";
import { Projectdevice1Page } from '../projectdevice1/projectdevice1';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = HomePage;
  //tab2Root = ContactPage;
  tab2Root = Projectdevice1Page;
  tab3Root = MessagePage;
  tab4Root = MapPage;
  //tab4Root = ContactPage;

  @ViewChild('myTabs') tabRef: Tabs;

  constructor(
    public backButtonService:BackButtonService,
    private platform:Platform
  ) {
    this.platform.ready().then(() => {
      // this.backButtonService.registerBackButtonAction(this.tabRef);
      this.backButtonService.registerBackButtonAction(null);
    });
  }
}
