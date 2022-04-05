import {AfterContentInit, Component, OnInit, ViewChild} from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { transition, trigger, useAnimation } from '@angular/animations';
import { fadeIn } from 'ng-animate';
import { CurationService } from '../../shared/services/curation/curation.service';
import { environment } from '../../../environments/environment';
import { UserService } from '../../shared/services/user/user.service';
import { Router } from '@angular/router';
import { StateService } from '../../shared/services/state/state.service';
import {distinctUntilChanged} from "rxjs/operators";

@Component({
  selector: 'app-portal-home',
  templateUrl: './portal-dashboard.component.html',
  styleUrls: ['./portal-dashboard.component.scss'],
  animations: [
    trigger('fadeIn', [transition('* => *', useAnimation(fadeIn))])
  ]
})
export class PortalDashboardComponent implements OnInit, AfterContentInit {

  @ViewChild('activityPaginator', {static: true}) activityPagintor: MatPaginator;
  @ViewChild('reviewsPaginator', {static: true}) reviewsPaginator: MatPaginator;
  @ViewChild('userAnnotationsPaginator', {static: true}) userAnnotationsPaginator: MatPaginator;

  displayedColumns: string[] = ['category', 'curator', 'date', 'time', 'actions'];
  user: any;
  pieData = [];
  userRole: any;
  recentUserActivity: any;
  diseaseActivity: any;
  paginators = {
    activityPaginator: {
      lowValue: 0,
      highValue: 5
    },
    reviewsPaginator: {
      lowValue: 0,
      highValue: 5
    },
    userAnnotationsPaginator: {
      lowValue: 0,
      highValue: 5
    }
  };
  reviews: any;
  userAnnotations: any;
  loading = true;
  loadingAnnotationsNeedingAction = true;

  constructor(public authService: AuthService, public curationService: CurationService,
              public userService: UserService, public stateService: StateService, public router: Router) {
  }

  ngOnInit(): void {
    this.authService.user$.pipe(distinctUntilChanged((prev, curr) => prev === curr))
      .subscribe((user) => {
      if (user) {
        this.user = user;
        this.userRole = user[environment.AUTH0_ROLE_CLAIM];
        if (this.userService.isRoleAdmin(this.userRole)){
          this.curationService.getAnnotationsNeedingReview().subscribe((annotations) => {
            this.reviews = annotations;
            this.loadingAnnotationsNeedingAction = false;
          });
        } else {
          this.curationService.getUserAnnotationsNeedingWork().subscribe((annotations) => {
            this.userAnnotations = annotations;
            this.loadingAnnotationsNeedingAction = false;
          });
        }
      }
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

  ngAfterContentInit() {
    this.getUserActivity(1);
  }

  getUserActivity(weeksBack: number) {
    this.curationService.getGroupActivityFeed(true, weeksBack,  null, null)
      .subscribe((recentActivity) => {
        this.recentUserActivity = recentActivity;
      }, (error) => {
        console.log(error);
      }, () => {
        this.loading = false;
      });
  }

  graphUserActivity(userActivity: any) {
    const dates = userActivity.map((activity) => activity.date);
    const counts = {};
    for (const date of dates) {
      counts[date] = counts[date] && counts[date].value ? {name: date, value: counts[date].value + 1} : {
        name: date,
        value: 1
      };
    }

    const graphSeries = Object.values(counts);

    return [{
      name: 'Annotations',
      series: graphSeries
    }];
  }

  getPaginatorData(name: string, event: PageEvent): PageEvent {
    this.paginators[name].lowValue = event.pageIndex * event.pageSize;
    this.paginators[name].highValue = this.paginators[name].lowValue  + event.pageSize;
    return event;
  }

  noContributions(){
    return this.pieData?.length === 0;
  }

  navigateToDisease(disease, category) {
    this.stateService.setSelectedCategory(category);
    const urlSegment = `/curate/${disease}`;
    this.router.navigateByUrl(urlSegment);
  }

  onDiseaseActivitySelect(data): void {
    this.router.navigate([`/portal/curate/${data.extra.id}`]);
  }
}
