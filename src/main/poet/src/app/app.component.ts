import {Component, Inject, OnInit} from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { DOCUMENT } from "@angular/common";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import {environment} from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'poet';
  portalActionItems =  [
    {title: 'Contact', route: '/contact', icon: 'contact_support', private: false, requiresElevated: false},
    {title: 'FAQ', route: '/faq', icon: 'view_list', private: false, requiresElevated: false}
  ];
  isElevated: boolean = false;
  userRole: "GUEST"

  constructor(public auth: AuthService, @Inject(DOCUMENT) public document: Document, public router: Router
  , private http: HttpClient) {
  }

  ngOnInit(): void {
      this.auth.user$.subscribe((user) => {
        this.userRole = user[environment.AUDIENCE_ROLE];
        this.isElevated = user[environment.AUDIENCE_ROLE] == "ELEVATED_CURATOR";
      });
    }

  isHomePage(){
    return this.router.url === '/';
  }

  isPortalPage(){
    return this.router.url.includes("portal");
  }

  isDashboardPage(){
    return this.router.url.includes('dashboard');
  }

  isCuratePage(){
    return this.router.url.includes('curate');
  }

  testApi(){
    this.http.get("http://localhost:8080/api/v1/private/").subscribe(
      response => console.log(response)
    );
  }

  loginWithRedirect(): void {
    const target = '/portal/dashboard'
    const redirect_uri = window.location.origin + target;
    this.auth.loginWithRedirect(  {
      redirect_uri: redirect_uri,
      appState: {target: target}
    });
  }
}
