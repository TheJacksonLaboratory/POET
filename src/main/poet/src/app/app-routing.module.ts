import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import {ResourcesComponent} from "./resources/resources/resources.component";


const routes: Routes = [
  { path: '', component: HomeComponent,  pathMatch: 'full'},
  { path: 'resources', component: ResourcesComponent },
  { path: 'resources/faq', component: ResourcesComponent},
  { path: 'resources/contact', component: ResourcesComponent},
  { path: 'portal', loadChildren: () => import('./portal/portal.module').then(m => m.PortalModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
