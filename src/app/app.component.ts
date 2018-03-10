import { Component, ViewChild } from '@angular/core';
import { Platform, Nav } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Auth } from 'aws-amplify';

import { TabsPage } from '../pages/tabs/tabs';
import { LoginPage } from '../pages/login/login';
import { IPage } from '../pages/i/i';

import { TabsControllerPage } from '../pages/tabs-controller/tabs-controller';

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) navCtrl: Nav;
    rootPage:any = null;

  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen) {
    let globalActions = function() {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    };

    // Manu: TODO - the rootPage should be IPage for iAPI Application
    platform.ready()
      .then(() => {
        Auth.currentAuthenticatedUser()
          .then(() => { this.rootPage = TabsControllerPage; })
          .catch(() => { this.rootPage = TabsControllerPage; })
          .then(() => globalActions());
      });
  }
}
