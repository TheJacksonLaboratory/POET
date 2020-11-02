import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";

@Component({
  selector: 'app-portal',
  templateUrl: './portal.component.html',
  styleUrls: ['./portal.component.scss']
})
export class PortalComponent implements OnInit {

  @ViewChild('sidenav') sidenav: any;
  sideNavItems =  [
    {title: 'New Curation', route: '/portal/curate', icon: 'create', private: true},
    {title: 'Edit Submissions', route: '/portal/submissions', icon: 'view_list', private: true},
    {title: 'Curation Tutorial', route: '/portal/help', icon: 'help', private: false}
  ];
  isSideNavOpen = false;
  constructor(public auth: AuthService) { }

  ngOnInit(): void {
    // Want to open if not loading..
    this.auth.isLoading$.subscribe((loading) => {
      if(!loading){
        this.isSideNavOpen = true;
      }
    })

    this.auth.user$.subscribe((user) => {
      console.log(user);
    });
  }

  toggleSidenav(){
    this.isSideNavOpen = this.sidenav.opened;
  }
}
