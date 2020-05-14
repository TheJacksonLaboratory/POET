import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PortalHomeComponent } from './portal-home/portal-home.component';
import { SharedModule } from "../shared/shared.module";
import { PortalRoutingModule } from "./portal-routing.module";
import { PortalCurateComponent } from './portal-curate/portal-curate.component';
import {RouterModule} from "@angular/router";



@NgModule({
  declarations: [PortalHomeComponent, PortalCurateComponent],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    PortalRoutingModule
  ]
})
export class PortalModule { }
