import { Component, Inject } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { DOCUMENT } from "@angular/common";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'poet';
  portalActionItems =  [
    {title: 'Contact', route: '/portal/curate', icon: 'contact_support', private: false},
    {title: 'FAQ', route: '/portal/submissions', icon: 'view_list', private: false}
  ];

  constructor(public auth: AuthService, @Inject(DOCUMENT) public document: Document, public router: Router
  , private http: HttpClient) {
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
