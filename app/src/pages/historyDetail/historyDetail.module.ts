import { NgModule } from '@angular/core';
import {IonicPageModule } from 'ionic-angular';
import { HistoryDetailPage } from './historyDetail';

@NgModule({
  declarations: [
    HistoryDetailPage,
  ],
  imports: [
    IonicPageModule.forChild(HistoryDetailPage),
  ],
})
export class HistoryDetailPageModule {}
