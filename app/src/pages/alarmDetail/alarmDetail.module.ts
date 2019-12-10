import { NgModule } from '@angular/core';
import {IonicPageModule } from 'ionic-angular';
import { AlarmDetailPage } from './alarmDetail';


@NgModule({
  declarations: [
    AlarmDetailPage,
  ],
  imports: [
    IonicPageModule.forChild(AlarmDetailPage),
  ],
})
export class AlarmDetailPageModule {}
