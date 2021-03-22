import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { HpoService } from "../../../shared/services/external/hpo.service";
import { AbstractControl, FormControl, FormGroup, ValidatorFn, Validators } from "@angular/forms";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { AnchorSearchResult, HpoTerm } from "../../../shared/models/search-models";
import { AnnotationSource, Publication } from "../../../shared/models/models";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { StateService } from "../../../shared/services/state/state.service";
import { MatDialog } from "@angular/material/dialog";
import { DialogSourceComponent } from "../dialog-source/dialog-source.component";
import { MatSnackBar } from "@angular/material/snack-bar";
import { COMMA, ENTER } from "@angular/cdk/keycodes";
import { MatAutocompleteSelectedEvent } from "@angular/material/autocomplete";

@Component({
  selector: 'poet-phenotype-curation',
  templateUrl: './phenotype-curation.component.html',
  styleUrls: ['./phenotype-curation.component.scss']
})
export class PhenotypeCurationComponent implements OnInit {

  @Input('selectedSource') annotationSource: AnnotationSource;
  @Input('role') userRole: string;
  @Output('onAnnotationSuccess') onAnnotationSuccess: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output('handleForm') handleFormEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();
  @ViewChild('modifierInput') modifierInput: ElementRef<HTMLInputElement>;
  selectedAnnotation: any;
  updating: boolean;
  selectedHpo: HpoTerm;
  hpoOptions: HpoTerm[];
  modifierOptions: AnchorSearchResult[];
  onsetOptions: AnchorSearchResult[];
  selectedPublications: Publication[] = [];
  selectedModifiers: string[] = [];
  selectedOnset: any;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];

  savingAnnotation: boolean = false;
  formControlGroup: FormGroup = new FormGroup({
    hpoFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    onsetFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    modifierFormControl: new FormControl({value: '', disabled: false}),
    evidenceFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    negationFormControl: new FormControl({value: '', disabled: false}, Validators.required),
    sexFormControl: new FormControl({value: '', disabled: false}),
    frequencyFormControl: new FormControl({value: '', disabled: false}, this.nGreaterThanM()),
    extensionFormControl: new FormControl({value: '', disabled: false}),
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
        this.selectedPublications = [source.publication];
      }
    });

    this.stateService.selectedPhenotypeAnnotation.subscribe((annotation) => {
      if (!annotation) {
        this.selectedPublications = [];
        this.resetPhenotypeForm();
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

    this.formControlGroup.get("modifierFormControl").valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length > 3 && !this.formControlGroup.disabled) {
          this.hpoService.searchDescendants(query, 'HP:0012823').subscribe((data) => {
            if (!data) {
              this.formControlGroup.get("hpoFormControl").setErrors({notFound: true});
            }
            this.modifierOptions = data;
          }, (err) => {
            this.formControlGroup.get("hpoFormControl").setErrors({notFound: true});
          });
        }
      });

    this.formControlGroup.get("onsetFormControl").valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length > 3 && !this.formControlGroup.disabled) {
          this.hpoService.searchDescendants(query, 'HP:0003674').subscribe((data) => {
            if (!data) {
              this.formControlGroup.get("ageOnsetFormControl").setErrors({notFound: true});
            }
            this.onsetOptions = data;
          }, (err) => {
            this.formControlGroup.get("ageOnsetFormControl").setErrors({notFound: true});
          });
        }
      });
  }

  submitForm(): void {
    const phenotypeAnnotation = {
      id: this.selectedAnnotation && this.selectedAnnotation.id ? this.selectedAnnotation.id : null,
      hpoId: this.formControlGroup.get('hpoFormControl').value.id,
      hpoName: this.formControlGroup.get('hpoFormControl').value.name,
      evidence: this.formControlGroup.get('evidenceFormControl').value,
      relation: this.formControlGroup.get('relationFormControl').value,
      comment: this.formControlGroup.get('commentFormControl').value,
      extension: this.formControlGroup.get('extensionFormControl').value
    }
    this.savingAnnotation = true;
    if (this.updating) {
      this.curationService.updateAnnotation(phenotypeAnnotation, 'phenotype').subscribe(() => {
        this.onSuccessfulPhenotype('Annotation Updated!')
      }, (err) => {
        this.onErrorPhenotypeSave();
      });
    } else {
      this.curationService.updateAnnotation(phenotypeAnnotation, 'phenotype').subscribe(() => {
        this.onSuccessfulPhenotype('Annotation Saved!')
      }, (err) => {
        this.onErrorPhenotypeSave();
      });
    }
  }

  setFormValues(annotation: any) {
    this.formControlGroup.get('hpoFormControl').setValue({id: annotation.hpoId, name: annotation.hpoName});
    this.formControlGroup.get('evidenceFormControl').setValue(annotation.evidenceType);
    this.formControlGroup.get('relationFormControl').setValue(annotation.relation);
    this.formControlGroup.get('extensionFormControl').setValue(annotation.extension);
    this.formControlGroup.get('commentFormControl').setValue(annotation.comment);
    this.stateService.setSelectedSource(annotation.annotationSource);

  }

  onSuccessfulPhenotype(message: string) {
    this.savingAnnotation = false;
    this.stateService.triggerAnnotationReload(true);
    this.resetPhenotypeForm();
    this._snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: "left"
    });
  }

  onErrorPhenotypeSave() {
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

  resetPhenotypeForm() {
    this.formControlGroup.reset();
  }

  displayMaxoFn(option) {
    return option && option.name ? `${option.name} ${option.ontologyId}` : '';
  }

  displayHpoFn(option) {
    return option && option.name ? `${option.name} ${option.id}` : '';
  }

  removePublication(publication: Publication): void {
    const index = this.selectedPublications.indexOf(publication);

    if (index >= 0) {
      this.selectedPublications.splice(index, 1);
    }
  }

  closeForm() {
    this.handleFormEmitter.emit(false);
  }

  nGreaterThanM(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if(control.value){
        if(control.value && control.value.includes("/") && new RegExp("\\d+\\/\\d+").test(control.value)){
          const nums = control.value.split("/");
          return nums[0].trim() > nums[1].trim() ? {notValid: {value: "M Greater than M"}} : null;
        } else {
          return {notValid: {value: "M Greater than M"}};
        }
      } else {
        return null;
      }
    };
  }

  removeModifiers(modifier: string): void {
    const index = this.selectedModifiers.indexOf(modifier);

    if (index >= 0) {
      this.selectedModifiers.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.selectedModifiers.push(event.option.viewValue);
    this.modifierInput.nativeElement.value = '';
    this.formControlGroup.get("modifierFormControl").setValue(null);
  }
}
