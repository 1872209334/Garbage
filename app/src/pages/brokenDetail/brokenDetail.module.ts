import { NgModule } from '@angular/core';
import {IonicPageModule } from 'ionic-angular';
import { BrokenDetailPage } from './brokenDetail';

@NgModule({
  declarations: [
    BrokenDetailPage,
  ],
  imports: [
    IonicPageModule.forChild(BrokenDetailPage),
  ],
})
export class BrokenDetailPageModule {}
