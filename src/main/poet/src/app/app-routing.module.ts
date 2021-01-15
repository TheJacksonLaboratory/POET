import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./home/home.component";


const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'portal', loadChildren: () => import('./portal/portal.module').then(m => m.PortalModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
