import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogSourceComponent } from "./dialog-source/dialog-source.component";
import { CurationService } from "../../shared/services/curation/curation.service";
import { AuthService } from "@auth0/auth0-angular";
import { transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { StateService } from "../../shared/services/state/state.service";
import { Disease, Publication } from "../../shared/models/models";
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
  selectedOntology: string;
  selectedDisease: Disease;
  showLoader: boolean = false;
  fxLayout: string = "row";
  fxLayoutAlign: string = "start stretch";
  fxFlexAnnotations: string = "50";
  fxFlexForm: string = "42";
  fxFlexFormOffset: string = "5"
  fxFlexAnnotationOffset: string = "25";
  sourceAndOntologySelected: boolean = false;
  showForm: boolean = false;
  annotationItems = [
    {value: 'hpo', display: 'Phenotypes', icon: 'assignment', disabled: true},
    {value: 'maxo', display: 'Treatments', icon: 'healing', disabled: false}
  ];
  userRole: string = 'GUEST';

  constructor(private route: ActivatedRoute, public dialog: MatDialog,
              public curationService: CurationService, public stateService: StateService,
              public authService: AuthService, public router: Router) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let id = params['id'];
      if (id) {
        if (this.determineIdType(id) == 'disease') {
          this.doingWork(true);
          this.curationService.getDisease(id).pipe(
            finalize(() => this.doingWork(false))
          ).subscribe((disease) => {
            this.selectedDisease = disease
            this.stateService.setSelectedDisease(disease);
          }, (error) => {
            this.router.navigate(['portal/dashboard'], {state: {error: true, message: error.text}});
          });
        }
      } else {
        this.router.navigate(['/portal/dashboard']);
      }
    });

    this.stateService.selectedOntology.subscribe((ontology) => this.selectedOntology = ontology);

    this.authService.user$.subscribe((user) => {
      this.userRole = user[environment.AUDIENCE_ROLE];
    })
  }

  /**
   * Display Form
   */

  handleForm(value: boolean) {
    if(value){
      this.showForm = true;
      this.fxFlexAnnotations = "42";
      this.fxFlexAnnotationOffset = "5";
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

  shouldShowMaxoCard() {
    return this.selectedDisease && this.selectedOntology === 'maxo' && this.showForm;
  }

  shouldShowHpoCard() {
    return this.selectedDisease && this.selectedOntology === 'hpo' && this.showForm;
  }

  changeOntology(ontology: string) {
    this.stateService.setSelectedOntology(ontology);
  }

  navigateToPage(disease){
    if(disease){
      this.router.navigate(['portal/curate/' + disease.id]);
    }
  }
}
