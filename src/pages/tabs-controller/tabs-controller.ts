import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { IPage } from '../i/i';
import { ControlPage } from '../control/control';
import { LockerPage } from '../locker/locker';

@Component({
  selector: 'page-tabs-controller',
  templateUrl: 'tabs-controller.html'
})
export class TabsControllerPage {

  tab1Root: any = IPage;
  tab2Root: any = ControlPage;
  tab3Root: any = LockerPage;
  constructor(public navCtrl: NavController) {
  }
  
}
