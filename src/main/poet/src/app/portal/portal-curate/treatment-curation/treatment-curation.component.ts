import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HpoService } from "../../../shared/services/external/hpo.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { HpoTerm, MaxoSearchResult, MaxoTerm } from "../../../shared/models/search-models";
import { AnnotationSource, Publication, TreatmentAnnotation } from "../../../shared/models/models";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { StateService } from "../../../shared/services/state/state.service";
import { MatDialog } from "@angular/material/dialog";
import { DialogSourceComponent } from "../dialog-source/dialog-source.component";
import { MatSnackBar } from "@angular/material/snack-bar";

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
  hpoOptions: HpoTerm[];
  selectedPublications: Publication[] = [];

  savingAnnotation: boolean = false;
  formControlGroup: FormGroup = new FormGroup({
    maxoFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    hpoFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    evidenceFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    relationFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    extensionIdFormControl: new FormControl({value: '', disabled: false}, Validators.pattern("^CHEBI:[0-9]{5}$")),
    extensionLabelFormControl: new FormControl({value: '', disabled: false}),
    commentFormControl: new FormControl({value: '', disabled: false}),
  });

  constructor(public hpoService: HpoService,
              public curationService: CurationService,
              public stateService: StateService,
              public dialog: MatDialog,
              private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {

    this.stateService.selectedAnnotationSource.subscribe(source => {
      if (source?.publication) {
        const id = source.publication.publicationId.split(":")[1];
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
        this.setFormValues(annotation);
      }
    });

    this.stateService.selectedAnnotationMode.subscribe((mode) => {
      if (mode == 'view') {
        this.formControlGroup.disable();
      } else if (mode == 'edit') {
        this.updating = true;
        this.formControlGroup.enable();
      } else {
        this.formControlGroup.enable();
      }
    });

    this.formControlGroup.get("maxoFormControl").valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length >= 3 && !this.formControlGroup.disabled) {
          this.hpoService.searchMaxoTerms(query).subscribe((data) => {
            if (!data) {
              this.formControlGroup.get("maxoFormControl").setErrors({notFound: true});
            }
            this.maxoOptions = data;
          }, (err) => {
            this.formControlGroup.get("maxoFormControl").setErrors({notFound: true});
          });
        }
      });

    this.formControlGroup.get("hpoFormControl").valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length > 3 && !this.formControlGroup.disabled) {
          this.hpoService.searchHPOTerms(query).subscribe((data) => {
            if (!data) {
              this.formControlGroup.get("hpoFormControl").setErrors({notFound: true});
            }
            this.hpoOptions = data;
          }, (err) => {
            this.formControlGroup.get("hpoFormControl").setErrors({notFound: true});
          });
        }
      });
  }

  submitForm(): void {
    const treatmentAnnotation = {
      id: this.selectedAnnotation && this.selectedAnnotation.id ? this.selectedAnnotation.id : null,
      maxoId: this.formControlGroup.get('maxoFormControl').value.ontologyId.toString(),
      maxoName: this.formControlGroup.get('maxoFormControl').value.name,
      hpoId: this.formControlGroup.get('hpoFormControl').value.id,
      hpoName: this.formControlGroup.get('hpoFormControl').value.name,
      evidence: this.formControlGroup.get('evidenceFormControl').value,
      relation: this.formControlGroup.get('relationFormControl').value,
      comment: this.formControlGroup.get('commentFormControl').value,
      extensionId: this.formControlGroup.get('extensionIdFormControl').value,
      extensionLabel: this.formControlGroup.get('extensionLabelFormControl').value
    }
    this.savingAnnotation = true;
    if (this.updating) {
      this.curationService.updateAnnotation(treatmentAnnotation, 'treatment').subscribe(() => {
        this.onSuccessfulTreatment('Annotation Updated!')
      }, (err) => {
        this.onErrorTreatmentSave();
      });
    } else {
      this.curationService.saveAnnotation(treatmentAnnotation, 'treatment').subscribe(() => {
        this.onSuccessfulTreatment('Annotation Saved!')
      }, (err) => {
        this.onErrorTreatmentSave();
      });
    }
  }

  setFormValues(annotation: any) {
    this.formControlGroup.get('maxoFormControl').setValue({ontologyId: annotation.maxoId, name: annotation.maxoName});
    this.formControlGroup.get('hpoFormControl').setValue({id: annotation.hpoId, name: annotation.hpoName});
    this.formControlGroup.get('evidenceFormControl').setValue(annotation.evidenceType);
    this.formControlGroup.get('relationFormControl').setValue(annotation.relation);
    this.formControlGroup.get('extensionIdFormControl').setValue(annotation.extensionId);
    this.formControlGroup.get('extensionLabelFormControl').setValue(annotation.extensionLabel);
    this.formControlGroup.get('commentFormControl').setValue(annotation.comment);
    this.stateService.setSelectedSource(annotation.annotationSource);

  }

  onSuccessfulTreatment(message: string) {
    this.savingAnnotation = false;
    this.stateService.triggerAnnotationReload(true);
    this.stateService.triggerAnnotationCountsReload(true);
    this.resetTreatmentForm();
    this._snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: "left"
    });
  }

  onErrorTreatmentSave() {
    this.savingAnnotation = false;
    this._snackBar.open('Error Saving Annotation!', 'Close', {
      duration: 3000,
      horizontalPosition: "left"
    });
  }

  selectPublication() {
    this.dialog.open(DialogSourceComponent, {
      minWidth: 300,
      data: {
        "userRole": this.userRole
      }
    });
  }

  everythingValid() {
    return this.formControlGroup.valid && this.selectedPublications.length > 0 && !this.formControlGroup.disabled;
  }

  resetTreatmentForm() {
    this.formControlGroup.reset();
  }

  displayMaxoFn(option) {
    return option && option.name ? `${option.name} ${option.ontologyId}` : '';
  }

  displayHpoFn(option) {
    return option && option.name ? `${option.name} ${option.id}` : '';
  }

  remove(publication: Publication): void {
    const index = this.selectedPublications.indexOf(publication);

    if (index >= 0) {
      this.selectedPublications.splice(index, 1);
    }
  }

  closeForm() {
    this.stateService.setSelectedTreatmentAnnotation(null);
    this.handleFormEmitter.emit(false);
  }
}
