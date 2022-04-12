import { Component, Inject, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import packageInfo from '../../package.json';
import { UserService } from './shared/services/user/user.service';
import {distinctUntilChanged} from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';
import {DialogProfileComponent} from './dialog-profile/dialog-profile.component';
import {MatSnackBar} from '@angular/material/snack-bar';

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
  version: string = packageInfo.version + '.beta';

  constructor(public auth: AuthService, @Inject(DOCUMENT) public document: Document,
              public router: Router, public userService: UserService, public dialog: MatDialog,
              private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
      this.auth.user$.pipe(
        distinctUntilChanged((prev, curr) => prev === curr)).subscribe((user) => {
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
    const redirectUri = window.location.origin + target;
    this.auth.loginWithRedirect(  {
      redirect_uri: redirectUri,
      appState: {target}
    });
  }

  openProfile(){
    this.dialog.open(DialogProfileComponent, {minWidth: 300, data: {
      orcid: ''
      }}).afterClosed().subscribe((id) => {
        if (id) {
          this.userService.setOrcid(id).subscribe(() => {
            this._snackBar.open('Successfully updated profile!', 'close', {
              duration: 3000,
              horizontalPosition: 'left'
            });
          }, (err) => {
            this._snackBar.open(err?.error?.message, 'close', {
              horizontalPosition: 'left'
            });
          });
        }
      }
    );
  }
}
