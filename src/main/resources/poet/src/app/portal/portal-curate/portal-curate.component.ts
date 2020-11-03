import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { DialogCurationComponent } from "./dialog-curation/dialog-curation.component";
import { CurationService } from "../../shared/services/curation.service";
import { AuthService } from "@auth0/auth0-angular";
import { transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";

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
  sourceSelected: boolean = false;
  selectedOntology: string;
  showLoader: boolean = false;
  fxLayout: string = "column";
  fxLayoutAlign: string = "space-around center";
  fxFlexAnnotations: string;
  fxFlexForm: string;
  shouldAnimateMove: boolean = false;

  constructor(private route: ActivatedRoute, public dialog: MatDialog,
              public curationService: CurationService, public auth: AuthService) {
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
                this.selectedOntology = result.ontology;
              } else if (result.publication) {
                this.selectionType = 'publication';
                this.selectionId = result.publication.id;
                this.selectedOntology = result.ontology;
              }
            } else if (result.action === 'create') {
              // TODO: Have creating source an option.
            }
          }
        });
      }
    });
  }

  /**
   * On Source Selection move card to display 2.
   * @param source
   */
  onSourceSelection(source: any) {
    this.sourceSelected = true;
    this.selectedOntology = source.ontology;
    this.fxLayout = "row";
    this.fxLayoutAlign = "start stretch";
    this.fxFlexAnnotations = "50";
    this.fxFlexForm = "50";
    this.shouldAnimateMove = true;
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
}
