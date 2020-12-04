import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { Router } from "@angular/router";
export interface PeriodicElement {
    name: string;
    position: number;
    weight: number;
    symbol: string;
  }

  const ELEMENT_DATA: any[] = [
    {category: 'MAXO', curator: 'Leigh', date: '11/2/2020', time: '11:23pm'},
    {category: 'MAXO', curator: 'Peter', date: '11/2/2020', time: '11:26pm'},
    {category: 'PHENOPACKET', curator: 'Mike', date: '11/1/2020', time: '2:00am'},
    {category: 'MAXO', curator: 'Leigh', date: '11/1/2020', time: '9:00am'},
    {category: 'MAXO', curator: 'Leigh', date: '11/1/2020', time: '8:00am'},
    {category: 'PHENOPACKET', curator: 'Peter', date: '11/1/2020', time: '7:00am'},
    {category: 'HPO', curator: 'Leigh', date:'11/1/2020', time: '3:30am'},
    {category: 'PHENOPACKET', curator: 'Nicole', date: '11/1/2020', time: '4:00am'},
    {category: 'HPO', curator: 'Seb', date: '11/1/2020', time: '3:05pm'},
    {category: 'HPO', curator: 'Seb', date: '11/1/2020', time: '3:10pm'},
  ];

@Component({
  selector: 'app-portal-home',
  templateUrl: './portal-dashboard.component.html',
  styleUrls: ['./portal-dashboard.component.scss'],
  animations: [
    trigger('fadeIn', [transition('* => *', useAnimation(fadeIn))])
  ]
})
export class PortalDashboardComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;

  displayedColumns: string[] = ['category', 'curator', 'date', 'time', 'actions'];
  dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);
  user: any;
  pieData = [
    {
      "name": "Human Phenotype Ontology",
      "value": 40
    },
    {
      "name": "Medical Action Ontology",
      "value": 120
    },
    {
      "name": "Phenopackets",
      "value": 30
    }
  ];

  lineData = [
    {
      "name": "Annotations"
      ,
      "series": [
        {
          "name": "November 13",
          "value": 10
        },
        {
          "name": "November 14",
          "value": 5
        },
        {
          "name": "November 15",
          "value": 20
        },
        {
          "name": "November 16",
          "value": 9
        },
        {
          "name": "November 17",
          "value": 2
        },
        {
          "name": "November 18",
          "value": 0
        },
        {
          "name": "November 18",
          "value": 12
        }
      ]
    }
  ];
  constructor(public authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  searchSelect(data){
    console.log(data);
    if(data.type == 'disease'){
      this.router.navigate(['/portal/curate/' + data.id]);
    }
  }
}
