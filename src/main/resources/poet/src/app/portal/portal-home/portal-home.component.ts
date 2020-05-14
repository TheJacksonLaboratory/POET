import {Component, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-portal-home',
  templateUrl: './portal-home.component.html',
  styleUrls: ['./portal-home.component.scss']
})
export class PortalHomeComponent implements OnInit {

  @ViewChild('sidenav') sidenav: any;
  sideNavItems =  [
    {title: 'New Curation', route: '/portal/curate', icon: 'create'},
    {title: 'Edit Submissions', route: '/portal/submissions', icon: 'view_list'},
    {title: 'Curation Tutorial', route: '/portal/help', icon: 'help' }
    ];
  isSideNavOpen = true;
  constructor() { }

  ngOnInit(): void {
  }

  toggleSidenav(){
    console.log(this.sidenav.opened);
    this.isSideNavOpen = this.sidenav.opened;
  }

}
