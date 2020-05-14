import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PortalHomeComponent } from "./portal-home/portal-home.component";
import {PortalCurateComponent} from "./portal-curate/portal-curate.component";


const routes: Routes = [
  { path: '', component: PortalHomeComponent, children: [
      {path: 'curate', component: PortalCurateComponent}
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PortalRoutingModule {

}
