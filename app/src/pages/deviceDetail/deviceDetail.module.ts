import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { DeviceDetailPage } from './deviceDetail';

@NgModule({
  declarations: [
    DeviceDetailPage
  ],
  imports: [
    IonicPageModule.forChild(DeviceDetailPage),
  ],
})
export class DeviceDetailPageModule {}
