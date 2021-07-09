import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { CurationService } from "../../shared/services/curation/curation.service";
import { environment } from "../../../environments/environment";
import { Status } from "../../shared/models/models";

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
  user: any;
  pieData = [];
  userRole: any;
  recentActivity: any;
  lowValue: number = 0;
  highValue: number = 5;
  reviews: any;
  userAnnotations: any;

  constructor(public authService: AuthService, public curationService: CurationService) {
  }

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
      this.userRole = user[environment.AUDIENCE_ROLE];
      this.curationService.getUserAnnotationsNeedingWork().subscribe((annotations)=>{
        this.userAnnotations = annotations;
      });
      if(this.isElevatedCurator()){
        this.curationService.getAnnotationsNeedingReview().subscribe((annotations) => {
          this.reviews = annotations;
        });
      }
    });

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

  noContributions(){
    return this.pieData?.length == 0;
  }

  isElevatedCurator(){
    return this.userRole == "ELEVATED_CURATOR";
  }
}
