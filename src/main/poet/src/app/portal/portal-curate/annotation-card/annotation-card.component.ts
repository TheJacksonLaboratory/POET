import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AnnotationSource, Disease, MaxoAnnotation, Publication } from "../../../shared/models/models";
import { StateService } from "../../../shared/services/state/state.service";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { Observable } from "rxjs";
import { tap } from "rxjs/operators";
import { transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Route } from '@angular/compiler/src/core';
import { ActivatedRoute, Router } from '@angular/router';

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
  @Input('role') userRole: string;
  disease: Disease;
  publication: Publication;
  ontology: string;
  maxoAnnotations: Observable<MaxoAnnotation[]>;
  annotationMode: any;
  triggerBounceIn: any;
  activeIndex: any;
  annotationStatuses: any[] = [];
  selectedStatuses: any[] = [];

  constructor(public stateService: StateService, public curationService: CurationService, private _snackBar: MatSnackBar, private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    const openAnnotation = 
    this.stateService.selectedOntology.subscribe((ontology) => {
      this.ontology = ontology;
    });

    this.stateService.selectedDisease.subscribe((disease: Disease) => {
      if (this.ontology === 'maxo' && disease != null) {
        this.disease = disease;
        this.updateAnnotations({disease: disease, publication: null});
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
      if (this.annotationMode == 'edit') {
        this.activeIndex = -1;
        this.annotationAction(null, 'create', -1)
      }
      this.maxoAnnotations = this.curationService.getMaxoAnnotations(this.disease, this.publication, "").pipe(
        tap((annotations => {
        annotations.forEach((annotation) => {
          this.annotationStatuses.push(annotation.status);
        });
        this.annotationStatuses = [...new Set(this.annotationStatuses)].sort();
        this.selectedStatuses = this.annotationStatuses;
        const id = parseInt(this.route.snapshot.queryParams.id);
        if(id){
          const index = annotations.map((item) => item["id"]).indexOf(id);
          const annotation = annotations[index];
          this.annotationAction(annotation,  'view', index)
        }
      })), tap(() => {

      }));
    }
  }

  openForm() {
    this.openAnnotationForm.emit(true);
  }

  closeForm() {
    this.openAnnotationForm.emit(false);
  }

  annotationAction(annotation: any, action: any, index: any) {
    this.activeIndex = index;
    if (action == 'delete') {
      this.curationService.deleteMaxoAnnotation(annotation.id).subscribe(() => {
        this._snackBar.open('Annotation Deleted!', 'Close', {
          duration: 3000,
        });
        this.updateAnnotations(null);
        this.closeForm();
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

  isUser() {
    return this.userRole != 'GUEST';
  }

  isElevatedCurator() {
    return this.userRole === 'ELEVATED_CURATOR';
  }
}
