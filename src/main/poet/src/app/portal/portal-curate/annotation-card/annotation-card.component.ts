import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {
  Disease,
  TreatmentAnnotation,
  Publication,
  PhenotypeAnnotation, Annotation, Status,
} from '../../../shared/models/models';
import { StateService } from '../../../shared/services/state/state.service';
import { CurationService } from '../../../shared/services/curation/curation.service';
import { Observable } from 'rxjs';
import { transition, trigger, useAnimation } from '@angular/animations';
import { bounceInLeft } from 'ng-animate';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { ConfirmSheetComponent } from './confirm-sheet/confirm-sheet.component';
import { UtilityService } from '../../../shared/services/utility.service';
import { tap } from 'rxjs/operators';
import {UserService} from '../../../shared/services/user/user.service';
import { HpoService } from '../../../shared/services/external/hpo.service';

@Component({
  selector: 'poet-annotation-card',
  templateUrl: './annotation-card.component.html',
  styleUrls: ['./annotation-card.component.scss'],
  animations: [
    trigger('bounceInLeft', [transition('0 => 1', useAnimation(bounceInLeft, {
      params: {timing: .5}
    }))])
  ]
})
export class AnnotationCardComponent implements OnInit {

  @Output('openForm') openAnnotationForm: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Input() user: any;
  @Input() formOpen = false;
  @ViewChild('phenotypePaginator') phenotypePagination: MatPaginator;
  @ViewChild('treatmentPaginator') treatmentPagination: MatPaginator;
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
  lowValue = 0;
  highValue = 10;
  filteredAnnotationLength = { count: 0 };
  showAll = false;
  selectedSort = 'recent';
  loadingAnnotations = true;
  hpoToolTipText = '';


  constructor(public stateService: StateService, public curationService: CurationService, public utilityService: UtilityService,
              private _snackBar: MatSnackBar, private route: ActivatedRoute, private _bottomSheet: MatBottomSheet,
              private cdk: ChangeDetectorRef, public userService: UserService, public hpoService: HpoService) {
  }

  ngOnInit(): void {
    this.stateService.selectedDisease.subscribe((disease: Disease) => {
      if (disease != null) {
        this.disease = disease;
      }
    });

    this.stateService.selectedCategory.subscribe((category) => {
      this.category = category;
      this.annotationStatuses = [];
      if (this.category === 'phenotype') {
        this.phenotypeAnnotations = this.stateService.phenotypeAnnotations.pipe(
          tap(annotations => {
            if (this.category === 'phenotype'){
              this.configureAnnotationStatus(annotations);
            }
          })
        );
      } else if (this.category === 'treatment') {
        this.treatmentAnnotations = this.stateService.treatmentAnnotations.pipe(
          tap(annotations => {
            if (this.category === 'treatment'){
              this.configureAnnotationStatus(annotations);
            }
          }));
      }
    });

    this.stateService.selectedAnnotationMode.subscribe((mode) => {
      this.annotationMode = mode;
    });

    this.stateService.selectedPhenotypeAnnotation.subscribe((annotation) => {
      if (annotation) {
        this.activeAnnotation = annotation;
      } else {
        this.activeAnnotation = null;
      }
    });

    this.stateService.selectedTreatmentAnnotation.subscribe((annotation) => {
      if (annotation) {
        this.activeAnnotation = annotation;
      } else {
        this.activeAnnotation = null;
      }
    });
  }

  configureAnnotationStatus(annotations){
    if (annotations.length > 0){
      this.annotationStatuses = [];
      const statuses = annotations.map((annotation) => {
        return annotation.status;
      });
      this.annotationStatuses = [...new Set(statuses)].sort();
      this.selectedStatuses = this.annotationStatuses;
      this.cdk.detectChanges();
      this.loadingAnnotations = false;
    }
  }

  categoryToDisplay() {
    if (this.category === 'treatment') {
      return 'Treatments';
    } else if (this.category === 'phenotype') {
      return 'Phenotypes';
    }
  }

  openForm() {
    this.openAnnotationForm.emit(true);
  }

  closeForm() {
    this.openAnnotationForm.emit(false);
  }

  annotationAction(event: MouseEvent, annotation: any, action: any) {
    event.stopPropagation();
    if (action === 'delete') {
        this._bottomSheet.open(ConfirmSheetComponent, {
          restoreFocus: false,
          disableClose: true
        }).afterDismissed().subscribe(shouldDelete => {
          if (shouldDelete){
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
      if (this.category === 'treatment' && (annotation?.status !== Status.RETIRED.valueOf())) {
        this.stateService.setSelectedTreatmentAnnotation(annotation);
      } else if (this.category === 'phenotype' && (annotation?.status !== Status.RETIRED.valueOf())) {
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
    if (this.category === 'phenotype'){
      this.lowValue = 0;
      this.highValue = 10;
      this.phenotypePagination.pageIndex = 0;
    }
  }

  showEditAnnotation(annotation: Annotation) {
    return (this.userService.isUser(this.user) && this.utilityService.ownsAnnotation(this.user, annotation.owner) &&
      !this.utilityService.isNeedsWork(annotation) && !this.utilityService.isOfficial(annotation))
      || (this.userService.isRoleAdmin(this.user.role) && !this.utilityService.isUnderReview(annotation) &&
        !this.utilityService.isNeedsWork(annotation));
  }

  showDeleteAnnotation(annotation: Annotation){
    return (this.userService.isUser(this.user) && this.utilityService.ownsAnnotation(this.user, annotation.owner) &&
        !this.utilityService.isOfficial(annotation)) ||
      this.userService.isRoleAdmin(this.user.role);
  }

  showAnnotationNeedsReview(annotation: Annotation){
    return (this.userService.isRoleAdmin(this.user.role) && this.utilityService.isUnderReview(annotation));
  }

  showAnnotationNeedsWork(annotation: Annotation){
    return (this.userService.isUser(this.user) && this.utilityService.ownsAnnotation(this.user, annotation.owner)
      && this.utilityService.isNeedsWork(annotation));
  }

  showCreateTreatment(phenotype: PhenotypeAnnotation){
    return this.isUser() && (this.utilityService.isOfficial(phenotype) || this.utilityService.isAccepted(phenotype));
  }

  isUser(){
    return this.userService.isUser(this.user);
  }

  createTreatmentForPhenotype(phenotype: PhenotypeAnnotation){
    this.stateService.setTreatmentPhenotypeTarget({id: phenotype.hpoId, name: phenotype.hpoName});
    this.stateService.setSelectedAnnotationMode('create');
    this.openAnnotationForm.emit(true);
    this.stateService.setSelectedCategory('treatment');
  }

  getStatusTooltipText(status){
    if (status === 'ACCEPTED'){
      return 'Annotation is accepted.';
    } else if (status === 'UNDER_REVIEW'){
      return 'Annotation is under review.';
    } else if (status === 'OFFICIAL'){
      return 'Annotation is official.';
    } else if (status === 'NEEDS_WORK'){
      return 'Annotation needs work.';
    }
  }

  getTermDefinitionTooltipText(event, tooltip, hpoId: string){
    event.stopPropagation();
    this.hpoService.hpoTermDefinition(hpoId).subscribe(definition => {
      if (definition){
        tooltip.message = definition;
        tooltip.show();
      }
    });
  }

  hideDefinitionTooltip(tooltip){
    if (tooltip._isTooltipVisible()){
      tooltip.hide();
    }
  }

  isActive(annotation){
    return this.activeAnnotation === annotation;
  }
}
