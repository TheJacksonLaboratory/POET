import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogSourceComponent } from "./dialog-curation/dialog-source.component";
import { CurationService } from "../../shared/services/curation/curation.service";
import { AuthService } from "@auth0/auth0-angular";
import { transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { StateService } from "../../shared/services/state/state.service";
import { Disease, Publication } from "../../shared/models/models";
import { finalize } from "rxjs/operators";

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
  selectionId: string;
  selectedOntology: string;
  selectedDisease: Disease;
  selectedPublication: Publication;
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
    {value: 'hpo', display: 'Phenotypes', icon: 'assignment'},
    {value: 'maxo', display: 'Treatments', icon: 'healing'}
  ];

  constructor(private route: ActivatedRoute, public dialog: MatDialog,
              public curationService: CurationService, public stateService: StateService,
              public auth: AuthService, public router: Router) {
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
        } else if (this.determineIdType(id) == 'publication') {
          this.doingWork(true);
          this.curationService.getPublication(id).pipe(
            finalize(() => this.doingWork(false))
          ).subscribe((publication) =>
            this.selectedPublication = publication
          );
        }
        this.selectionType = this.determineIdType(id);
        this.selectionId = id;
      } else {
        // Open modal dialog to figure out what is the goal
        const dialogRef = this.dialog.open(DialogSourceComponent, {
          width: '500px',
          height: '500px'
        });

        dialogRef.afterClosed().subscribe(result => {
          // Get Selection Type
          if (result) {
            if (result.action === 'fetch') {
              if (result.disease) {
                this.selectionType = 'disease';
                this.selectionId = result.disease.id;
              } else if (result.publication) {
                this.selectionType = 'publication';
                this.selectionId = result.publication.id;
              }
            } else if (result.action === 'create') {
              // TODO: Have creating source an option.
            }
          }
        });
      }
    });

    this.stateService.selectedOntology.subscribe((ontology) => this.selectedOntology = ontology);
  }

  /**
   * Display Form
   */

  displayForm() {
    this.showForm = true;
    this.fxFlexAnnotations = "42";
    this.fxFlexAnnotationOffset = "5";
  }

  /**
   * Is the given id a disease or publication based on prefix.
   * @param id
   */
  determineIdType(id: string): string {
    if (id) {
      if (id.includes("OMIM")) {
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
}
