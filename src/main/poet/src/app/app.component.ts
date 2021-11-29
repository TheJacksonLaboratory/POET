import { Component, Inject, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import { version } from '../../package.json';
import { UserService } from './shared/services/user/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title: string = 'poet';
  portalActionItems =  [
    {title: 'Contact', route: '/contact', icon: 'contact_support', private: false, requiresElevated: false},
    {title: 'FAQ', route: '/faq', icon: 'view_list', private: false, requiresElevated: false}
  ];
  isElevated: boolean = false;
  version: string = version + '.beta';

  constructor(public auth: AuthService, @Inject(DOCUMENT) public document: Document,
              public router: Router, public userService: UserService) {
  }

  ngOnInit(): void {
      this.auth.user$.subscribe((user) => {
        if (user) {
          this.userService.checkUser();
          this.isElevated = this.userService.isUserAdmin(user);
        }
      });
  }

  isHomePage(){
    return this.router.url === '/';
  }

  isPortalPage(){
    return this.router.url.includes('portal');
  }

  isDashboardPage(){
    return this.router.url.includes('dashboard');
  }

  isCuratePage(){
    return this.router.url.includes('curate');
  }

  loginWithRedirect(): void {
    const target = '/portal/dashboard';
    const redirect_uri = window.location.origin + target;
    this.auth.loginWithRedirect(  {
      redirect_uri: redirect_uri,
      appState: {target: target}
    });
  }
}
