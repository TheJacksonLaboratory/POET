import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { Router } from "@angular/router";
import { CurationService } from "../../shared/services/curation/curation.service";
import { MatDialog } from "@angular/material/dialog";
import { environment } from "../../../environments/environment";

@Component({
  selector: 'app-portal-home',
  templateUrl: './portal-dashboard.component.html',
  styleUrls: ['./portal-dashboard.component.scss'],
  animations: [
    trigger('fadeIn', [transition('* => *', useAnimation(fadeIn))])
  ]
})
export class PortalDashboardComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;

  displayedColumns: string[] = ['category', 'curator', 'date', 'time', 'actions'];
  dataSource: any;
  user: any;
  pieData;
  lineData;
  userRole: any;
  recentActivity: any;
  lowValue: number = 0;
  highValue: number = 5;

  constructor(public authService: AuthService, private router: Router,
              public curationService: CurationService, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
      this.userRole = user[environment.AUDIENCE_ROLE];
    });

   /* this.curationService.getActivity(true).subscribe((userActivity) => {
      this.dataSource = new MatTableDataSource<any>(userActivity);
      this.lineData = this.graphUserActivity(userActivity);
      this.dataSource.paginator = this.paginator;
    });*/

    this.curationService.getGroupActivityFeed(true, 1).subscribe((recentActivity) => {
      this.recentActivity = recentActivity;
    });

    this.curationService.getUserContributions().subscribe((contributions) => {
      if (contributions.every(obj => obj.value === 0)) {
        this.pieData = [];
      } else {
        this.pieData = contributions;
      }
    });
  }


  navigateToAnnotation(element){
    const diseaseId = element.source.disease.diseaseId;
    this.router.navigate(['/portal/curate/' + diseaseId], {queryParams: {id:element.annotationId}});
  }

  /*
    Group by day
   */
  graphUserActivity(userActivity: any) {
    let dates = userActivity.map((activity) => activity.date);
    let counts = {};

    for (let i = 0; i < dates.length; i++) {
      let date = dates[i];
      counts[date] = counts[date] && counts[date].value ? {name: date, value: counts[date].value + 1} : {
        name: date,
        value: 1
      };
    }

    let graphSeries = Object.values(counts);

    return [{
      "name": "Annotations",
      "series": graphSeries
    }]
  }

  getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }
}
