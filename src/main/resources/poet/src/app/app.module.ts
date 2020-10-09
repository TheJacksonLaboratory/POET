import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from "./shared/shared.module";
import { HomeComponent } from './home/home.component';
import { PortalModule } from "./portal/portal.module";
import { FlexLayoutModule } from '@angular/flex-layout';
import { AuthConfig, AuthModule } from "@auth0/auth0-angular";
const config: AuthConfig = {
  domain: 'dev-poet.us.auth0.com',
  clientId: 'zAquxh5T1sPsoqBmSd8R7UasuFCcl9LY'
}
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    PortalModule,
    FlexLayoutModule,
    AuthModule.forRoot(config)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
