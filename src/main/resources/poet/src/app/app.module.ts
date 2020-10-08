import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from "./shared/shared.module";
import { HomeComponent } from './home/home.component';
import { PortalModule } from "./portal/portal.module";
import { FlexLayoutModule } from '@angular/flex-layout';
import { AuthModule } from "@auth0/auth0-angular";

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
    FlexLayoutModule
    /*AuthModule.forRoot({
      domain: 'dev-poet.us.auth0.com',
      clientId: 'Y3zMbuJHFVDyeHR6RPt7mC6bw4jQGcgv'
    })*/
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
