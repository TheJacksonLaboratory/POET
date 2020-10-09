import { Component, Inject, OnInit } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { DOCUMENT } from "@angular/common";
import { Router } from "@angular/router";
import { combineLatest } from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'poet';

  constructor(public auth: AuthService, @Inject(DOCUMENT) public document: Document, public router: Router) {
  }

  isHomePage(){
    return this.router.url === '/';
  }

  loginWithRedirect(): void {
    const target = 'portal/dashboard'
    const redirect_uri = window.location.href + target;
    console.log(redirect_uri);
    this.auth.loginWithRedirect(  {
      redirect_uri: redirect_uri,
      appState: {target: "/" + target}
    });

  }
}
