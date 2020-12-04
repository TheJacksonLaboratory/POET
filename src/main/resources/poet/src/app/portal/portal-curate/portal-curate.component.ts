import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogCurationComponent } from "./dialog-curation/dialog-curation.component";
import { CurationService } from "../../shared/services/curation/curation.service";
import { AuthService } from "@auth0/auth0-angular";
import { animate, query, stagger, style, transition, trigger, useAnimation } from "@angular/animations";
import { fadeIn } from "ng-animate";
import { StateService } from "../../shared/services/state/state.service";
import { Disease } from "../../shared/models/models";

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
  showLoader: boolean = false;
  fxLayout: string = "row";
  fxLayoutAlign: string = "start stretch";
  fxFlexAnnotations: string = "40";
  fxFlexForm: string = "33";
  fxFlexOffset: string = "10";
  sourceAndOntologySelected: boolean = false;
  showForm: boolean = false;
  annotationItems = [
    { value: 'hpo', display: 'Phenotypes', icon: 'assignment'},
    { value: 'maxo', display: 'Medical Actions', icon:  'healing' }
  ];

  constructor(private route: ActivatedRoute, public dialog: MatDialog,
              public curationService: CurationService, public stateService: StateService,
              public auth: AuthService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      let id = params['id'];
      if (id) {
        this.selectionType = this.determineIdType(id);
        this.selectionId = id;
      } else {
        // Open modal dialog to figure out what is the goal
        const dialogRef = this.dialog.open(DialogCurationComponent, {
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

    this.stateService.selectedOntology.subscribe( (ontology) => this.selectedOntology = ontology);

    this.stateService.selectedDisease.subscribe((disease) => {
      this.selectedDisease = disease;
    });

    this.stateService.sourceAndOntologySelected.subscribe((source) => {
      if(source){
        this.showForm = true;
        this.fxFlexAnnotations = "33";
        this.fxFlexOffset = "0";
      }
    });
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

  shouldShowSourceSelection () {
    return this.selectionType && this.selectionId && !this.sourceAndOntologySelected;
  }

  shouldShowMaxoCard() {
    return this.selectedDisease && this.selectedOntology === 'maxo' && this.showForm;
  }

  shouldShowHpoCard() {
    return this.selectedDisease && this.selectedOntology === 'hpo' && this.showForm;
  }

  changeOntology(ontology: string){
    this.stateService.setSelectedOntology(ontology);
  }
}
