import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { CurationService } from "../../shared/services/curation/curation.service";
import { environment } from "../../../environments/environment";
import { UserService } from "../../shared/services/user/user.service";
import {Router} from "@angular/router";
import {StateService} from "../../shared/services/state/state.service";

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
  recentUserActivity: any;
  diseaseActivity: any;
  lowValue: number = 0;
  highValue: number = 5;
  reviews: any;
  userAnnotations: any;
  loading: boolean = true;
  loadingAnnotationsNeedingAction: boolean = true;

  constructor(public authService: AuthService, public curationService: CurationService,
              public userService: UserService, public stateService: StateService, public router: Router) {
  }

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
      this.userRole = user[environment.AUTH0_ROLE_CLAIM];
      if(this.userService.isRoleAdmin(this.userRole)){
        this.curationService.getAnnotationsNeedingReview().subscribe((annotations) => {
          this.reviews = annotations;
          this.loadingAnnotationsNeedingAction = false;
        });
      } else if(this.user) {
        this.curationService.getUserAnnotationsNeedingWork().subscribe((annotations)=>{
          this.userAnnotations = annotations;
          this.loadingAnnotationsNeedingAction = false;
        });
      }
    });

    this.curationService.getGroupActivityFeed(true, 1,  null, null)
      .subscribe((recentActivity) => {
      this.recentUserActivity = recentActivity;
    }, (error)=>{
      console.log(error);
    }, ()=> {
        this.loading = false;
    });

    this.curationService.getUserContributions().subscribe((contributions) => {
      if (contributions.every(obj => obj.value === 0)) {
        this.pieData = [];
      } else {
        this.pieData = contributions;
      }
    });

    this.curationService.getDiseaseActivity().subscribe(data => {
      this.diseaseActivity = data;
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

  navigateToDisease(disease, category) {
    this.stateService.setSelectedCategory(category);
    const urlSegment = `/curate/${disease}`;
    this.router.navigateByUrl(urlSegment);
  }

  onDiseaseActivitySelect(data): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }
}
