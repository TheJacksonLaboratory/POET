import {Component, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-portal',
  templateUrl: './portal.component.html',
  styleUrls: ['./portal.component.scss']
})
export class PortalComponent implements OnInit {

  @ViewChild('sidenav') sidenav: any;
  sideNavItems =  [
    {title: 'New Curation', route: '/portal/curate/new', icon: 'create'},
    {title: 'Edit Submissions', route: '/portal/submissions', icon: 'view_list'},
    {title: 'Curation Tutorial', route: '/portal/help', icon: 'help' }
  ];
  isSideNavOpen = true;
  constructor() { }

  ngOnInit(): void {
  }

  toggleSidenav(){
    this.isSideNavOpen = this.sidenav.opened;
  }
}
