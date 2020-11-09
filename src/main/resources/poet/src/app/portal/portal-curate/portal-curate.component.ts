import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogCurationComponent } from "./dialog-curation/dialog-curation.component";
import { CurationService } from "../../shared/services/curation/curation.service";
import { AuthService } from "@auth0/auth0-angular";
import { transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";
import { StateService } from "../../shared/services/state/state.service";

@Component({
  selector: 'app-portal-curate',
  templateUrl: './portal-curate.component.html',
  styleUrls: ['./portal-curate.component.scss'],
  animations: [
    trigger('bounceInLeft', [transition('* => *', useAnimation(bounceInLeft, {
      params: {timing: 1}
    }))]),
  ]
})
export class PortalCurateComponent implements OnInit {

  currentStep: string = "selection";
  selectionType: string;
  selectionId: string;
  selectedOntology: string;
  showLoader: boolean = false;
  fxLayout: string = "column";
  fxLayoutAlign: string = "space-around center";
  fxFlexAnnotations: string;
  fxFlexForm: string;
  sourceAndOntologySelected: boolean = false;

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
    this.stateService.sourceAndOntologySelected.subscribe((selected) => {
      if(selected){
        this.fxLayout = "row";
        this.fxLayoutAlign = "start stretch";
        this.fxFlexAnnotations = "50";
        this.fxFlexForm = "50";
        this.sourceAndOntologySelected = selected;
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
    return this.sourceAndOntologySelected && this.selectedOntology === 'maxo';
  }

  shouldShowHpoCard() {
    return this.sourceAndOntologySelected && this.selectedOntology === 'hpo';
  }
}
