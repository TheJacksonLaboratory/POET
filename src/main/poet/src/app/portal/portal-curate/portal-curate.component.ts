import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { CurationService } from "../../shared/services/curation/curation.service";
import { AuthService } from "@auth0/auth0-angular";
import { transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { StateService } from "../../shared/services/state/state.service";
import { Disease } from "../../shared/models/models";
import { finalize } from "rxjs/operators";
import { environment } from "../../../environments/environment";

@Component({
  selector: 'app-portal-curate',
  templateUrl: './portal-curate.component.html',
  styleUrls: ['./portal-curate.component.scss'],
  animations: [
    trigger('fadeIn', [transition('* => *', useAnimation(fadeIn))])
  ]
})
export class PortalCurateComponent implements OnInit {

  selectionType: string;
  selectedCategory: string;
  selectedDisease: Disease;
  showLoader: boolean = false;
  fxLayout: string = "row";
  fxLayoutAlign: string = "start stretch";
  fxFlexAnnotations: string = "60";
  fxFlexAnnotationOffset: string = "20";
  fxFlexForm: string = "45";
  fxFlexFormOffset: string = "2.5"
  sourceAndOntologySelected: boolean = false;
  showForm: boolean = false;
  annotationItems = [
    {value: 'phenotype', display: 'Phenotypes', icon: 'assignment', disabled: false, count: 0, reason:""},
    {value: 'treatment', display: 'Treatments', icon: 'healing', disabled: false, count: 0, reason: ""}
  ];
  user: any;

  constructor(private route: ActivatedRoute, public curationService: CurationService, public stateService: StateService,
              public authService: AuthService, public router: Router) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let id = params['id'];
      let type = params['type'];
      if (id) {
        if (this.determineIdType(id) == 'disease') {
          this.doingWork(true);
          this.curationService.getDisease(id).pipe(
            finalize(() => this.doingWork(false))
          ).subscribe((disease) => {
            this.selectedDisease = disease
            this.stateService.setSelectedDisease(disease);
            this.stateService.triggerAnnotationCountsReload(true);
          }, (error) => {
            this.router.navigate(['/portal/dashboard'], {state: {error: true, message: error.text}});
          });
        }
      } else {
        this.router.navigate(['/portal/dashboard']);
      }
      if(type && this.stateService.isValidCategory(type)) {
        this.stateService.setSelectedCategory(type);
      }
    });

    this.stateService.selectedCategory.subscribe((category) => this.selectedCategory = category);

    this.authService.user$.subscribe((user) => {
      if(!user){
        user = {nickname: 'GUEST', role: 'GUEST'};
      } else {
        user.role = user[environment.AUDIENCE_ROLE];
      }
      this.user = user;
    });

    this.stateService.triggerReloadAnnotationCounts.subscribe((reload) => {
      if(reload){
        this.getAnnotationCount();
      }
    });
  }

  handleForm(value: boolean) {
    if (value) {
      this.showForm = true;
      this.fxFlexAnnotations = "45";
      this.fxFlexAnnotationOffset = "2.5";
    } else {
      this.showForm = false;
      this.fxFlexAnnotations = "50";
      this.fxFlexAnnotationOffset = "25";
    }
  }

  /**
   * Is the given id a disease or publication based on prefix.
   * @param id
   */
  determineIdType(id: string): string {
    if (id) {
      if (id.includes("OMIM") || id.includes("MONDO")) {
        return "disease";
      } else if (id.includes("PMID")) {
        return "publication";
      }
    }
  }

  /**
   * Set loader based on work happening in child components or this component.
   * @param working
   */
  doingWork(working: boolean) {
    this.showLoader = working;
  }

  getAnnotationCount(){
      this.curationService.getAnnotationCounts(this.selectedDisease.diseaseId).subscribe((counts) => {
        this.annotationItems.forEach((item) => {
          if(item.value == 'phenotype'){
            item.count = counts.phenotypeCount;
          } else if(item.value == 'treatment') {
           item.count = counts.treatmentCount;
          }
        });
      });
  }

  shouldShowTreatmentCard() {
    return this.selectedDisease && this.selectedCategory === 'treatment' && this.showForm;
  }

  shouldShowPhenotypeCard() {
    return this.selectedDisease && this.selectedCategory === 'phenotype' && this.showForm;
  }

  changeCategory(ontology: string) {
    this.stateService.setSelectedCategory(ontology);
    this.handleForm(false);
  }

  navigateToPage(disease) {
    if (disease) {
      this.router.navigate(['/portal/curate/' + disease.id]);
    }
  }
}
