import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AnnotationSource, Disease, MaxoAnnotation, Publication } from "../../../shared/models/models";
import { StateService } from "../../../shared/services/state/state.service";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { Observable } from "rxjs";
import { animate, query, stagger, style, transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: 'app-annotation-card',
  templateUrl: './annotation-card.component.html',
  styleUrls: ['./annotation-card.component.scss'],
  animations: [
    trigger('bounceInLeft', [transition("0 => 1", useAnimation(bounceInLeft, {
      params: {timing: .5}
    }))])
  ]
})
export class AnnotationCardComponent implements OnInit {

  @Output('openForm') openAnnotationForm: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Input('role') userRole : string;
  disease: Disease;
  publication: Publication;
  ontology: string;
  maxoAnnotations: Observable<MaxoAnnotation[]>;
  annotationMode: any;
  triggerBounceIn: any;
  activeIndex: any;

  constructor(public stateService: StateService, public curationService: CurationService, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {

    this.stateService.selectedOntology.subscribe((ontology) => {
      this.ontology = ontology;
    });

    this.stateService.selectedDisease.subscribe((disease: Disease) => {
      if (this.ontology === 'maxo' && disease != null) {
        this.disease = disease;
        this.maxoAnnotations = this.curationService.getMaxoAnnotations(this.disease, null, "")
      }
    });

    this.stateService.triggerReloadAnnotations.subscribe((reload) => {
      // Reload if submission is successful for now.
      // TODO: Polling in the future.
      if (reload) {
        this.updateAnnotations(null);
      }
    });

    this.stateService.selectedAnnotationMode.subscribe((mode) => {
      this.annotationMode = mode;
    })
  }

  ontologyToDisplay() {
    if (this.ontology == 'maxo') {
      return "Medical Actions";
    } else if (this.ontology == 'hpo') {
      return "Phenotypes";
    }
  }

  updateAnnotations(source: AnnotationSource) {
    if (source) {
      this.publication = source.publication;
      this.disease = source.disease;
    }
    // Could get funky here
    if (this.ontology && this.disease) {
      if(this.annotationMode == 'edit'){
        this.activeIndex = -1;
        this.annotationAction(null, 'create', -1)
      }
      this.maxoAnnotations = this.curationService.getMaxoAnnotations(this.disease, this.publication, "");
    }
  }

  openForm() {
    this.openAnnotationForm.emit(true);
  }

  annotationAction(annotation: any, action: any, index: any) {
    this.activeIndex = index;
    if(action == 'delete'){
      this.curationService.deleteMaxoAnnotation(annotation.id).subscribe(() => {
        this._snackBar.open('Annotation Deleted!', 'Close', {
          duration: 3000,
        });
        this.updateAnnotations(null);
      });
    } else {
      if (this.ontology == 'maxo' && action != 'delete') {
        this.stateService.setSelectedTreatmentAnnotation(annotation);
      } else {
        this.stateService.setSelectedPhenotypeAnnotation(annotation);
      }
      this.stateService.setSelectedAnnotationMode(action);
      this.openForm();
    }
  }

  isElevatedCurator(){
    return this.userRole === 'ELEVATED_CURATOR';
  }
}
