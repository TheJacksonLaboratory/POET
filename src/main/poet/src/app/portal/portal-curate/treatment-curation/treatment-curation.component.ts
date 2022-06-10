import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { HpoService } from '../../../shared/services/external/hpo.service';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {catchError, debounceTime, distinctUntilChanged, finalize, map, startWith, take} from 'rxjs/operators';
import { HpoTerm, MaxoSearchResult, MaxoTerm } from '../../../shared/models/search-models';
import { AnnotationSource, Publication, TreatmentAnnotation } from '../../../shared/models/models';
import { CurationService } from '../../../shared/services/curation/curation.service';
import { StateService } from '../../../shared/services/state/state.service';
import { MatDialog } from '@angular/material/dialog';
import { DialogSourceComponent } from '../dialog-source/dialog-source.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MonarchService } from '../../../shared/services/external/monarch.service';
import { DialogReviewComponent } from '../dialog-review/dialog-review.component';
import { UtilityService } from '../../../shared/services/utility.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'poet-treatment-curation',
  templateUrl: './treatment-curation.component.html',
  styleUrls: ['./treatment-curation.component.scss']
})
export class TreatmentCurationComponent implements OnInit {


  @Input('selectedSource') annotationSource: AnnotationSource;
  @Input('role') userRole: string;
  @Output('handleForm') handleFormEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();

  selectedAnnotation: TreatmentAnnotation;
  updating: boolean;
  selectedTreatment: MaxoTerm;
  selectedHpo: HpoTerm;
  maxoOptions: MaxoSearchResult[];
  hpoOptions: Observable<{ name: string; id: string }[]>;
  chebiOptions: any;
  selectedPublications: Publication[] = [];
  loadingHpoSuggestions = false;
  loadingMaxoSuggestions = false;
  loadingExtensionSuggestions = false;
  elevatedButtonText = {approve: {display: 'Approve', show: true}, deny: {display: 'Deny', show: true},
    changes: {display: 'Make changes', show: true}};
  elevatedChanges = false;
  title = 'Treatment';
  savingAnnotation = false;
  formControlGroup: FormGroup = new FormGroup({
    maxoFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    hpoFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    diseaseTreatmentControl:  new FormControl({value: '', disabled: false}),
    evidenceFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    relationFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    extensionFormControl: new FormControl({value: '', disabled: false}, this.extensionValidation()),
    commentFormControl: new FormControl({value: '', disabled: false}, Validators.maxLength(50)),
  });

  constructor(public hpoService: HpoService,
              public curationService: CurationService,
              public monarchService: MonarchService,
              public stateService: StateService,
              public utilityService: UtilityService,
              public dialog: MatDialog,
              private _snackBar: MatSnackBar, private cdf: ChangeDetectorRef) {
  }

  ngOnInit(): void {

    this.stateService.selectedAnnotationSource.subscribe(source => {
      if (source?.publication) {
        const id = source.publication.publicationId.split(':')[1];
        source.publication.url = `https://pubmed.ncbi.nlm.nih.gov/${id}/`;
        this.selectedPublications = [source.publication];
      }
    });

    this.stateService.selectedTreatmentAnnotation.subscribe((annotation) => {
      if (!annotation) {
        this.selectedPublications = [];
        this.resetTreatmentForm();
      } else {
        this.selectedAnnotation = annotation;
        if (this.selectedAnnotation.lastUpdatedDate.includes('T')){
          this.selectedAnnotation.lastUpdatedDate = new Date(this.selectedAnnotation.lastUpdatedDate + 'Z').toLocaleString();
        }
        this.setFormValues(annotation);
      }
    });

    this.stateService.selectedPhenotypeAnnotation.subscribe((annotation) => {
      if (annotation){
        const preselect = {id: annotation.hpoId, name: annotation.hpoName};
        this.selectedHpo = preselect;
        this.formControlGroup.get('hpoFormControl').setValue(preselect);
      }
    });

    this.stateService.selectedAnnotationMode.subscribe((mode) => {
      if (mode === 'view') {
        this.formControlGroup.disable();
        this.title = 'Treatment';
        return;
      } else if (mode === 'edit') {
        this.updating = true;
        this.formControlGroup.enable();
        this.title = 'Treatment';
      } else if (mode === 'create') {
        this.title = 'New Treatment';
        this.resetTreatmentForm();
      }
      this.formControlGroup.enable();
    });

    this.formControlGroup.get('diseaseTreatmentControl').valueChanges.subscribe(value => {
      if (value) {
        this.selectedHpo = {
          id: 'HP:0000118',
          name: 'Phenotypic abnormality'
        };
        this.formControlGroup.get('hpoFormControl').setValue(this.selectedHpo);
        this.formControlGroup.get('hpoFormControl').disable();
      } else if (this.formControlGroup.dirty){
          this.selectedHpo = {
            id: '',
            name: ''
          };
          this.formControlGroup.get('hpoFormControl').reset();
          this.formControlGroup.get('hpoFormControl').enable();
        }
    });

    this.formControlGroup.get('maxoFormControl').valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length >= 3 && !this.formControlGroup.disabled) {
          this.loadingMaxoSuggestions = true;
          this.maxoOptions = [];
          this.hpoService.searchMaxoTerms(query).pipe(
            finalize(() => this.loadingMaxoSuggestions = false)).subscribe((data) => {
            if (!data || data.length === 0) {
              this.formControlGroup.get('maxoFormControl').setErrors({notFound: true});
            }
            this.maxoOptions = data;
          }, (err) => {
            this.formControlGroup.get('maxoFormControl').setErrors({apiError: true});
          });
        }});

    this.formControlGroup.get('hpoFormControl').valueChanges
      .pipe(startWith(''), debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (!this.formControlGroup.disabled && !query?.hasOwnProperty('id')) {
          this.loadingHpoSuggestions = true;
          // Get phenotypes to display in select box for treatments.
          this.hpoOptions = this.stateService.phenotypeAnnotations.pipe(map(
            annotations => {
              return annotations.filter(annotation => {
                // do the filter here
                if (query === ''){
                  return annotation;
                } else if (query){
                  query = query.toLowerCase();
                  if (query.startsWith('hp:') && annotation.hpoId.toLowerCase().includes(query)){
                    return annotation;
                  } else if (annotation.hpoName.toLowerCase().includes(query)){
                    return annotation;
                  }
                }
              }).map(annotation => {
                return {
                  id: annotation.hpoId,
                  name: annotation.hpoName
                };
              });
            }
          ), take(1), finalize(() => {
            this.loadingHpoSuggestions = false;
            this.cdf.detectChanges();
          }), catchError((err) => {
            console.log(err);
            this.formControlGroup.get('hpoFormControl').setErrors({apiError: true});
            return [];
            }));
        }});

    this.formControlGroup.get('extensionFormControl').valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length > 3 && !this.formControlGroup.disabled) {
          this.loadingExtensionSuggestions = true;
          this.monarchService.searchMonarch(query, 'CHEBI').pipe(
            finalize(() => this.loadingExtensionSuggestions = false)
          ).subscribe((data) => {
            if (!data || data.length == 0) {
              this.formControlGroup.get('extensionFormControl').setErrors({notFound: true});
            }
            this.chebiOptions = data;
          }, (err) => {
            this.formControlGroup.get('extensionFormControl').setErrors({apiError: true});
          });
        }});
  }

  getFormTreatmentAnnotation(){
    const extension = this.formControlGroup.get('extensionFormControl').value;
    const annotationSource = this.stateService.getSelectedSource();
    return {
      id: this.selectedAnnotation && this.selectedAnnotation.id ? this.selectedAnnotation.id : null,
      maxoId: this.formControlGroup.get('maxoFormControl').value.ontologyId.toString(),
      maxoName: this.formControlGroup.get('maxoFormControl').value.name,
      hpoId: this.formControlGroup.get('hpoFormControl').value.id,
      hpoName: this.formControlGroup.get('hpoFormControl').value.name,
      evidence: this.formControlGroup.get('evidenceFormControl').value,
      relation: this.formControlGroup.get('relationFormControl').value,
      comment: this.formControlGroup.get('commentFormControl').value,
      extensionId: extension && extension.id ? extension.id : null,
      extensionLabel: extension && extension.label ? extension.label : null,
      message: '',
      publicationId: annotationSource.publication.publicationId,
      publicationName: annotationSource.publication.publicationName,
      diseaseId: annotationSource.disease.diseaseId,
      diseaseName: annotationSource.disease.diseaseName
    };
  }

  submitForm(): void {
    const treatmentAnnotation = this.getFormTreatmentAnnotation();
    this.savingAnnotation = true;
    if (this.updating) {
      this.curationService.updateAnnotation(treatmentAnnotation, 'treatment', '').subscribe(() => {
        this.onSuccessfulTreatment('Annotation Updated!', true);
      }, (err) => {
        this.onErrorTreatment(err.error);
      });
    } else {
      this.curationService.saveAnnotation(treatmentAnnotation, 'treatment').subscribe(() => {
        this.onSuccessfulTreatment('Annotation Saved!', false);
      }, (err) => {
        this.onErrorTreatment(err.error);
      });
    }
  }

  setFormValues(annotation: any) {
    this.formControlGroup.get('maxoFormControl').setValue({ontologyId: annotation.maxoId, name: annotation.maxoName});
    if (annotation.hpoId === 'HP:0000118'){
      this.formControlGroup.get('diseaseTreatmentControl').setValue(true);
    } else {
      this.formControlGroup.get('diseaseTreatmentControl').setValue(false);
    }
    this.formControlGroup.get('hpoFormControl').setValue({id: annotation.hpoId, name: annotation.hpoName});

    this.formControlGroup.get('evidenceFormControl').setValue(annotation.evidence);
    this.formControlGroup.get('relationFormControl').setValue(annotation.relation);
    this.formControlGroup.get('extensionFormControl').setValue({id: annotation.extensionId, label: annotation.extensionLabel});
    this.formControlGroup.get('commentFormControl').setValue(annotation.comment);
    this.stateService.setSelectedSource(annotation.annotationSource);
  }

  onSuccessfulTreatment(message: string, close: boolean) {
    this.savingAnnotation = false;
    this.stateService.triggerAnnotationReload(true, false);
    this.stateService.triggerAnnotationCountsReload(true);
    if (close){
      this.closeForm();
    } else {
      this.resetTreatmentForm();
    }
    this._snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'left'
    });
  }

  onErrorTreatment(err) {
    this.savingAnnotation = false;
    this._snackBar.open(err.details, 'Close', {
      horizontalPosition: 'left'
    });
  }

  selectPublication() {
    this.dialog.open(DialogSourceComponent, {
      minWidth: 300,
      data: {
        userRole: this.userRole
      }
    });
  }

  everythingValid() {
    return this.formControlGroup.valid && this.selectedPublications.length > 0 && !this.formControlGroup.disabled &&
      this.formControlGroup.dirty;
  }

  resetTreatmentForm() {
    this.selectedAnnotation = null;
    this.formControlGroup.reset();
    this.formControlGroup.get('evidenceFormControl').setValue('TAS');
  }

  remove(publication: Publication): void {
    this.formControlGroup.markAsDirty();
    const index = this.selectedPublications.indexOf(publication);
    if (index >= 0) {
      this.selectedPublications.splice(index, 1);
    }
  }

  closeForm() {
    this.stateService.setSelectedTreatmentAnnotation(null);
    this.elevatedButtonText = {approve: {display: 'Approve', show: true}, deny: {display: 'Deny', show: true}, changes: {display: 'Make changes', show: true}};
    this.elevatedChanges = false;
    this.handleFormEmitter.emit(false);
  }

  reviewAnnotation(action: string){
    if (action === 'approve'){
      const treatmentAnnotation = this.getFormTreatmentAnnotation();
      if (this.elevatedChanges){
        this.curationService.updateAnnotation(treatmentAnnotation, 'treatment', '').subscribe(() => {
          this.onSuccessfulTreatment('Treatment Annotation Approved!', true);
        }, (err) => {
          this.onErrorTreatment(err.error);
        });
      } else {
        this.curationService.updateAnnotation(treatmentAnnotation, 'treatment', 'approve').subscribe(() => {
          this.onSuccessfulTreatment('Treatment Annotation Approved!', true);
        }, (err) => {
          this.onErrorTreatment(err.error);
        });
      }
    } else if (action === 'deny') {
      this.dialog.open(DialogReviewComponent, {
        minWidth: 300,
        minHeight: 250,
        data: {
          title: 'Deny Treatment Annotation',
          approve: false
        }
      }).afterClosed().subscribe((data) => {
        if (data.confirmed) {
          const treatmentAnnotation = this.getFormTreatmentAnnotation();
          treatmentAnnotation.message = data.message;
          this.curationService.updateAnnotation(treatmentAnnotation, 'treatment', 'deny').subscribe(() => {
            this.onSuccessfulTreatment('Treatment Annotation Rejected!', true);
          }, (err) => {
            this.onErrorTreatment(err.error);
          });
        }
      });
    }
  }

  /*
   * Elevated curator making changes to an annotation under_review
   */
  toggleAnnotationChanges(shouldShow: boolean){
    if (shouldShow){
      this.elevatedChanges = true;
      this.formControlGroup.enable();
      this.elevatedButtonText.approve.display = 'Save & Accept';
      this.elevatedButtonText.changes.show = false;
    } else {
      this.elevatedChanges = false;
      this.formControlGroup.disable();
      this.elevatedButtonText.approve.display = 'Approve';
      this.elevatedButtonText.changes.show = true;
      this.setFormValues(this.selectedAnnotation);
    }
  }

  extensionValidation(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if ((control.value && typeof control.value === 'object') || (control.value === null || control.value === '')) {
        return null;
      } else {
        return {notValid: {value: 'Please select a valid chebi extension'}};
      }
    };
  }

  showFormControlElement(name: string){
    return this.formControlGroup.get(name).value !== null && !this.formControlGroup.disabled;
  }
}
