import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HpoService } from "../../../shared/services/external/hpo.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { debounceTime, distinctUntilChanged, finalize } from "rxjs/operators";
import { HpoTerm, MaxoSearchResult, MaxoTerm } from "../../../shared/models/search-models";
import { AnnotationSource, MaxoAnnotation } from "../../../shared/models/models";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { StateService } from "../../../shared/services/state/state.service";
import { forkJoin, Observable } from "rxjs";

@Component({
  selector: 'app-maxo-curation',
  templateUrl: './maxo-curation.component.html',
  styleUrls: ['./maxo-curation.component.scss']
})
export class MaxoCurationComponent implements OnInit {


  @Input('selectedSource') annotationSource: AnnotationSource;
  @Output('onAnnotationSuccess') onAnnotationSuccess: EventEmitter<boolean> = new EventEmitter<boolean>();
  selectedMaxo: MaxoTerm;
  selectedHpo: HpoTerm;
  maxoOptions: MaxoSearchResult[];
  hpoOptions: HpoTerm[];
  response: string;
  savingAnnotation: boolean = false;
  showMaxoForm: boolean = false;
  formControlGroup: FormGroup = new FormGroup({
    maxoFormControl: new FormControl('', Validators.required),
    hpoFormControl: new FormControl('', Validators.required),
    evidenceFormControl: new FormControl('', Validators.required),
    relationFormControl: new FormControl('', Validators.required),
    extensionFormControl: new FormControl(''), // Make a custom validator for chebi
    commentFormControl: new FormControl(''),
  });

  constructor(public hpoService: HpoService,
              public curationService: CurationService,
              public stateService: StateService) {
  }

  ngOnInit(): void {
    this.formControlGroup.get("maxoFormControl").valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length >= 3) {
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
        if (query && query.length > 3) {
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
    const maxoAnnotation = {
      maxoId: this.formControlGroup.get('maxoFormControl').value.ontologyId.toString(),
      maxoName: this.formControlGroup.get('maxoFormControl').value.name,
      hpoId: this.formControlGroup.get('hpoFormControl').value.id,
      hpoName: this.formControlGroup.get('hpoFormControl').value.name,
      evidence: this.formControlGroup.get('evidenceFormControl').value,
      relation: this.formControlGroup.get('relationFormControl').value,
      comment: this.formControlGroup.get('commentFormControl').value,
    }
    this.savingAnnotation = true;
    this.curationService.saveMaxoAnnotation(maxoAnnotation).pipe(
      finalize(() => this.savingAnnotation = false)
    ).subscribe(() => {
      // Show snacker with message then reset form
      this.stateService.triggerAnnotationSuccess(true);
    }, (err) => {
      this.response = "you fucked up!";
    });
  }

  resetMaxoForm() {
    this.formControlGroup.reset();
  }

  resetMaxoTermSelect() {
    this.formControlGroup.get("maxoFormControl").reset();
  }

  resetHpoTermSelect() {
    this.formControlGroup.get("hpoFormControl").reset();
  }

  displayMaxoFn(option) {
    return option && option.name ? `${option.name} ${option.ontologyId}` : '';
  }

  displayHpoFn(option) {
    return option && option.name ? `${option.name} ${option.id}` : '';
  }
}
