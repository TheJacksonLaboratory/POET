import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PortalDashboardComponent } from './portal-home/portal-dashboard.component';
import { SharedModule } from "../shared/shared.module";
import { PortalRoutingModule } from "./portal-routing.module";
import { PortalCurateComponent } from './portal-curate/portal-curate.component';
import { RouterModule } from "@angular/router";
import { PortalComponent } from './portal/portal.component';



@NgModule({
  declarations: [PortalDashboardComponent, PortalCurateComponent, PortalComponent],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    PortalRoutingModule
  ]
})
export class PortalModule { }
