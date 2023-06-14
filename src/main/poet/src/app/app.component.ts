import { Component, Inject, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import packageInfo from '../../package.json';
import { UserService } from './shared/services/user/user.service';
import { distinctUntilChanged, first } from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';
import {DialogProfileComponent} from './dialog-profile/dialog-profile.component';
import {MatSnackBar} from '@angular/material/snack-bar';
import { DialogDataManageComponent } from './dialog-data-manage/dialog-data-manage.component';
import { environment } from '../environments/environment';
import { StateService } from './shared/services/state/state.service';

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
  version: string = 'v' + packageInfo.version;

  constructor(public auth: AuthService, @Inject(DOCUMENT) public document: Document,
              public router: Router, public userService: UserService, public dialog: MatDialog,
              private _snackBar: MatSnackBar, public stateService: StateService) {
  }

  ngOnInit(): void {
      this.auth.user$.pipe(
        distinctUntilChanged((prev, curr) => prev === curr)).subscribe((user) => {
        if (user) {
          this.userService.checkUser();
          user.role = user[environment.AUTH0_ROLE_CLAIM];
          this.isElevated = this.userService.isRoleAdmin(user.role);
          this.userService.getUserDetails().pipe(first()).subscribe((user) => {
            if (user.orcid == null){
              this._snackBar.open("ORCID iD is missing from your profile.", "Add ORCID iD", {
                horizontalPosition: "left"
              }).onAction().pipe(first()).subscribe(() => {
                this.openProfile();
              });
            }
          })
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

  openProfile() {
    if(this.dialog.openDialogs.length === 0){
      this.dialog.open(DialogProfileComponent, {minWidth: 300, disableClose: false, hasBackdrop: true, data: {
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

  openDataManagement() {
    this.dialog.open(DialogDataManageComponent, {minWidth: 300, disableClose: false,
      hasBackdrop: true, data: {isElevated: this.isElevated}}).afterClosed().subscribe((data) => {
        if (data && data.release === true){
          this.stateService.triggerAnnotationReload(true, true);
        }
    });
  }
}
