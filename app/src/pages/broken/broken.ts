import { Component } from '@angular/core';
import { IonicPage,NavController,LoadingController } from 'ionic-angular';
import {PagesToggleService} from "../../app/service/pages.toggle.service";
import {AlertMsgService} from "../../app/service/alert.msg.service";
import { BrokenDetailPage} from "../brokenDetail/brokenDetail";

@IonicPage()
@Component({
  selector: 'page-broken',
  templateUrl: 'broken.html'
})
export class BrokenPage {
  public title = "故障列表";
  public brokenList = [];
  public isMore;
  public pageParam = {
    page: 1,
    isPull: false,
    page_size : 6
  };
  constructor(
    public navCtrl: NavController,
    private pagesToggleService: PagesToggleService,
    private alertMsgService:AlertMsgService
  ) {

  }
  ngOnInit() {

  }

  clearAlarm(){
    this.alertMsgService.showMsg("消音发送成功");
  }

  goBack(){
    this.navCtrl.pop();
  }
  doRefresh(refresher){
    this.pageParam.page = 1;
    setTimeout(()=>{
      refresher.complete();
    },1000)
    // refresher.complete();
  };

  doInfinite=(e)=>{
    setTimeout(()=>{
      this.pageParam.page++;
    },1000)
    setTimeout(()=>{
      e.complete();
    },2000)
  };

  brokenDetail(){
    this.navCtrl.push(BrokenDetailPage,null);
    //this.pagesToggleService.goToPage("BrokenDetailPage", null, "LeftAndRight");
  }
}
