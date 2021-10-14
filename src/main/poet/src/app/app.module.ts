import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from "./shared/shared.module";
import { HomeComponent } from './home/home.component';
import { PortalModule } from "./portal/portal.module";
import { FlexLayoutModule } from '@angular/flex-layout';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthHttpInterceptor } from '@auth0/auth0-angular';
import { AuthConfig, AuthModule } from "@auth0/auth0-angular";
import { environment as env} from "../environments/environment";
import { MAT_DIALOG_DEFAULT_OPTIONS } from "@angular/material/dialog";
import { ContactComponent } from './contact/contact.component';
import { FaqComponent } from './faq/faq.component';
const config: AuthConfig = {
  ...env.auth,
  httpInterceptor: {
    ...env.httpInterceptor,
  }
}
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ContactComponent,
    FaqComponent
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
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true },
    {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {panelClass: 'mat-dialog-override'}}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
