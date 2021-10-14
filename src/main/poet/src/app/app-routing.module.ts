import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import {ContactComponent} from "./contact/contact.component";
import {FaqComponent} from "./faq/faq.component";


const routes: Routes = [
  { path: '', component: HomeComponent,  pathMatch: 'full'},
  { path: 'contact', component: ContactComponent},
  { path: 'faq', component: FaqComponent },
  { path: 'portal', loadChildren: () => import('./portal/portal.module').then(m => m.PortalModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
