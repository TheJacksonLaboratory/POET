import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  AnnotationSource,
  Disease,
  TreatmentAnnotation,
  Publication,
  PhenotypeAnnotation, Status
} from "../../../shared/models/models";
import { StateService } from "../../../shared/services/state/state.service";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { Observable } from "rxjs";
import { finalize, tap } from "rxjs/operators";
import { transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ActivatedRoute, Router } from '@angular/router';
import { PageEvent } from "@angular/material/paginator";
import { MatBottomSheet } from "@angular/material/bottom-sheet";
import { ConfirmSheetComponent } from "./confirm-sheet/confirm-sheet.component";

@Component({
  selector: 'poet-annotation-card',
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
  @Input('user') user: any;
  @Input('formOpen') formOpen: boolean = false;
  disease: Disease;
  publication: Publication;
  category: string;
  treatmentAnnotations: Observable<TreatmentAnnotation[]>;
  phenotypeAnnotations: Observable<PhenotypeAnnotation[]>;
  annotationMode: any;
  triggerBounceIn: any;
  activeAnnotation: any;
  annotationStatuses: any[] = [];
  selectedStatuses: any[] = [];
  lowValue: number = 0;
  highValue: number = 10;
  showAll: boolean = false;
  selectedSort: string = 'recent';
  loadingAnnotations: boolean = false;


  constructor(public stateService: StateService, public curationService: CurationService,
              private _snackBar: MatSnackBar, private route: ActivatedRoute, private _bottomSheet: MatBottomSheet) {
  }

  ngOnInit(): void {

    this.stateService.selectedDisease.subscribe((disease: Disease) => {
      if (disease != null) {
        this.disease = disease;
        this.updateAnnotations({disease: disease, publication: null});
      }
    });

    this.stateService.selectedCategory.subscribe((category) => {
      this.category = category;
      this.updateAnnotations(null);
    });


    this.stateService.triggerReloadAnnotations.subscribe((reload) => {
      // TODO: Polling in the future.
      if (reload) {
        this.updateAnnotations(null);
      }
    });

    this.stateService.selectedAnnotationMode.subscribe((mode) => {
      this.annotationMode = mode;
    });

    this.stateService.selectedPhenotypeAnnotation.subscribe((annotation) => {
      if(!annotation){
        this.activeAnnotation = null;
      }
    });

    this.stateService.selectedTreatmentAnnotation.subscribe((annotation) => {
      if(!annotation){
        this.activeAnnotation = null;
      }
    });
  }

  categoryToDisplay() {
    if (this.category == 'treatment') {
      return "Treatments";
    } else if (this.category == 'phenotype') {
      return "Phenotypes";
    }
  }

  updateAnnotations(source: AnnotationSource) {
    if (source) {
      this.publication = source.publication;
      this.disease = source.disease;
    }
    if (this.category && this.disease) {
      if (this.annotationMode == 'edit') {
        this.activeAnnotation = null;
        this.annotationAction(null, 'create')
      }
      this.loadingAnnotations = true;
      if(this.category === 'treatment'){
        this.treatmentAnnotations = this.curationService.getTreatmentAnnotations(this.disease, this.publication, "").pipe(
          tap((annotations => {
          const statuses = annotations.map((annotation) => {
            return annotation.status;
          });
          this.annotationStatuses = [...new Set(statuses)].sort();
          this.selectedStatuses = this.annotationStatuses;
        })), finalize(() => this.loadingAnnotations = false));
      } else if(this.category === 'phenotype'){
        this.phenotypeAnnotations = this.curationService.getPhenotypeAnnotations(this.disease, this.publication, "").pipe(
          tap((annotations => {
            const statuses = annotations.map((annotation) => {
              return annotation.status;
            });
            this.annotationStatuses = [...new Set(statuses)].sort();
            this.selectedStatuses = this.annotationStatuses;
          })), finalize(() => this.loadingAnnotations = false));
      }
    }
  }

  openForm() {
    this.openAnnotationForm.emit(true);
  }

  closeForm() {
    this.openAnnotationForm.emit(false);
  }

  annotationAction(annotation: any, action: any) {
    if (action == 'delete') {
      if(this.category == 'treatment'){
        this._bottomSheet.open(ConfirmSheetComponent, {
          restoreFocus: false,
          disableClose: true
        }).afterDismissed().subscribe(shouldDelete => {
          if(shouldDelete){
            this.curationService.deleteAnnotation(annotation.id, 'treatment').subscribe(() => {
              this._snackBar.open('Annotation Deleted!', 'Close', {
                duration: 3000,
              });
              this.updateAnnotations(null);
              this.stateService.triggerAnnotationCountsReload(true);
              this.formOpen = false;
              this.closeForm();
            });
        }
        });
      } else {
        this._bottomSheet.open(ConfirmSheetComponent, {
          restoreFocus: false,
          disableClose: true
        }).afterDismissed().subscribe(shouldDelete => {
          if(shouldDelete){
            this.curationService.deleteAnnotation(annotation.id, this.category).subscribe(() => {
              this._snackBar.open('Annotation Deleted!', 'Close', {
                duration: 3000,
              });
              this.updateAnnotations(null);
              this.stateService.triggerAnnotationCountsReload(true);
              this.closeForm();
            });
          }
        });
      }
    } else {
      if (this.category == 'treatment') {
        this.stateService.setSelectedTreatmentAnnotation(annotation);
      } else {
        this.stateService.setSelectedPhenotypeAnnotation(annotation);
      }
      this.stateService.setSelectedAnnotationMode(action);
      this.formOpen = true;
      this.openForm();
    }
  }

  isUser(): boolean  {
    return this.user?.role != 'GUEST';
  }

  isElevatedCurator(): boolean  {
    return this.user?.role === 'ELEVATED_CURATOR';
  }

  isUnderReview(status: string): boolean {
    return status === "UNDER_REVIEW";
  }

  needsWork(status: string): boolean {
    return status === "NEEDS_WORK";
  }

  ownsAnnotation(annotationUser){
    return this.user?.nickname === annotationUser;
  }

  getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }
}
