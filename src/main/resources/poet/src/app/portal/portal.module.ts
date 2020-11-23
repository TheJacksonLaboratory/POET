import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PortalDashboardComponent } from './portal-home/portal-dashboard.component';
import { SharedModule } from "../shared/shared.module";
import { PortalRoutingModule } from "./portal-routing.module";
import { PortalCurateComponent } from './portal-curate/portal-curate.component';
import { RouterModule } from "@angular/router";
import { PortalComponent } from './portal/portal.component';
import { MaxoCurationComponent } from './portal-curate/maxo-curation/maxo-curation.component';
import { HpoCurationComponent } from './portal-curate/hpo-curation/hpo-curation.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { DialogCurationComponent } from './portal-curate/dialog-curation/dialog-curation.component';
import { HttpClientModule } from "@angular/common/http";
import { SourceCardComponent } from './portal-curate/source-card/source-card.component';
import { OntologySheet } from './portal-curate/source-card/ontology-sheet/ontology-sheet.component';
import { AnnotationCardComponent } from './portal-curate/annotation-card/annotation-card.component';


@NgModule({
  declarations: [PortalDashboardComponent, PortalCurateComponent, PortalComponent, MaxoCurationComponent,
    HpoCurationComponent, DialogCurationComponent, SourceCardComponent, OntologySheet, AnnotationCardComponent],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    HttpClientModule,
    PortalRoutingModule,
    FlexLayoutModule
  ],
  entryComponents: [DialogCurationComponent]
})
export class PortalModule { }