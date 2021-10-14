import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {
  Disease,
  TreatmentAnnotation,
  Publication,
  PhenotypeAnnotation, Annotation,
} from "../../../shared/models/models";
import { StateService } from "../../../shared/services/state/state.service";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { Observable } from "rxjs";
import { transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ActivatedRoute } from '@angular/router';
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatBottomSheet } from "@angular/material/bottom-sheet";
import { ConfirmSheetComponent } from "./confirm-sheet/confirm-sheet.component";
import { UtilityService } from "../../../shared/services/utility.service";
import { tap } from "rxjs/operators";

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
  @ViewChild('phenotypePaginator') phenotypePagination: MatPaginator;
  @ViewChild('treatmentPaginator') treatmentPagination:MatPaginator;
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
  filteredAnnotationLength = { count: 0 };
  showAll: boolean = false;
  selectedSort: string = 'recent';
  loadingAnnotations: boolean = false;


  constructor(public stateService: StateService, public curationService: CurationService, public utilityService: UtilityService,
              private _snackBar: MatSnackBar, private route: ActivatedRoute, private _bottomSheet: MatBottomSheet,
              private cdk: ChangeDetectorRef) {
  }

  ngOnInit(): void {

    this.phenotypeAnnotations = this.stateService.phenotypeAnnotations.pipe(
      tap(annotations => {
        if(this.category == "phenotype" && annotations.length > 0){
          this.annotationStatuses = [];
          const statuses = annotations.map((annotation) => {
            return annotation.status;
          });
          this.annotationStatuses = [...new Set(statuses)].sort();
          this.selectedStatuses = this.annotationStatuses;
          this.cdk.detectChanges();
        }
      })
    );

    this.treatmentAnnotations = this.stateService.treatmentAnnotations.pipe(
      tap(annotations => {
        if(this.category == "treatment" && annotations.length > 0){
          this.annotationStatuses = [];
          const statuses = annotations.map((annotation) => {
            return annotation.status;
          });
          this.annotationStatuses = [...new Set(statuses)].sort();
          this.selectedStatuses = this.annotationStatuses;
          this.cdk.detectChanges();
        }
      }));

    this.stateService.selectedDisease.subscribe((disease: Disease) => {
      if (disease != null) {
        this.disease = disease;
      }
    });

    this.stateService.selectedCategory.subscribe((category) => {
      this.category = category;
      this.annotationStatuses = [];
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

  openForm() {
    this.openAnnotationForm.emit(true);
  }

  closeForm() {
    this.openAnnotationForm.emit(false);
  }

  annotationAction(annotation: any, action: any) {
    if (action == 'delete') {
        this._bottomSheet.open(ConfirmSheetComponent, {
          restoreFocus: false,
          disableClose: true
        }).afterDismissed().subscribe(shouldDelete => {
          if(shouldDelete){
            this.curationService.deleteAnnotation(annotation.id, this.category).subscribe(() => {
              this._snackBar.open('Annotation Deleted!', 'Close', {
                duration: 3000,
              });
              this.annotationStatuses = [];
              this.stateService.triggerAnnotationReload(true, false);
              this.stateService.triggerAnnotationCountsReload(true);
              this.formOpen = false;
              this.closeForm();
            });
          }
        });
    } else {
      if (this.category === 'treatment') {
        this.stateService.setSelectedTreatmentAnnotation(annotation);
      } else {
        this.stateService.setSelectedPhenotypeAnnotation(annotation);
      }
      this.stateService.setSelectedAnnotationMode(action);
      this.formOpen = true;
      this.openForm();
    }
  }

  getPaginatorData(event: PageEvent): PageEvent {
    this.lowValue = event.pageIndex * event.pageSize;
    this.highValue = this.lowValue + event.pageSize;
    return event;
  }

  resetPaginator(){
    if(this.category == 'phenotype'){
      this.lowValue = 0;
      this.highValue = 10;
      this.phenotypePagination.pageIndex = 0;
    }
  }

  showBugReport(annotation: Annotation){
    return (this.utilityService.isOfficial(annotation) && !this.utilityService.isElevatedCurator(this.user));
  }

  showCreateAnnotation(annotation: Annotation) {
    return (this.utilityService.isUser(this.user) && this.utilityService.ownsAnnotation(this.user, annotation.owner))
      || (this.utilityService.isElevatedCurator(this.user) &&
      !this.utilityService.isUnderReview(annotation) && !this.utilityService.isNeedsWork(annotation));
  }

  showDeleteAnnotation(annotation: Annotation){
    return this.utilityService.isUser(this.user) && this.utilityService.ownsAnnotation(this.user, annotation.owner) ||
      this.utilityService.isElevatedCurator(this.user);
  }

  showAnnotationNeedsReview(annotation: Annotation){
    return (this.utilityService.isElevatedCurator(this.user) && this.utilityService.isUnderReview(annotation));
  }

  showCreateTreatment(phenotype: PhenotypeAnnotation){
    return this.isUser() && (this.utilityService.isOfficial(phenotype) || this.utilityService.isAccepted(phenotype));
  }

  isUser(){
    return this.utilityService.isUser(this.user);
  }

  createTreatmentForPhenotype(phenotype: PhenotypeAnnotation){
    this.stateService.setSelectedPhenotypeAnnotation(phenotype);
    this.stateService.setSelectedAnnotationMode("edit");
    this.openAnnotationForm.emit(true);
    this.stateService.setSelectedCategory('treatment');
  }
}
