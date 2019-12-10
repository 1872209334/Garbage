import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { BrokenPage } from './broken';

@NgModule({
  declarations: [
    BrokenPage
  ],
  imports: [
    IonicPageModule.forChild(BrokenPage),
  ],
})
export class BrokenPageModule {}
