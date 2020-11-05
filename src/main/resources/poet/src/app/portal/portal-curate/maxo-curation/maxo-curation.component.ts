import { Component, OnInit } from '@angular/core';
import { HpoService } from "../../../shared/services/hpo.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { HpoTerm, MaxoSearchResult, MaxoTerm } from "../../../shared/models/search-models";

@Component({
  selector: 'app-maxo-curation',
  templateUrl: './maxo-curation.component.html',
  styleUrls: ['./maxo-curation.component.scss']
})
export class MaxoCurationComponent implements OnInit {
  selectedMaxo: MaxoTerm;
  selectedHpo: HpoTerm;
  description: string;
  extension: string;
  maxoOptions: MaxoSearchResult[];
  hpoOptions: HpoTerm[];
  formControlGroup: FormGroup = new FormGroup({
    maxoFormControl: new FormControl('', Validators.required),
    hpoFormControl: new FormControl('', Validators.required),
    evidenceFormControl: new FormControl('', Validators.required),
    relationFormControl: new FormControl('', Validators.required),
    extensionFormControl: new FormControl('', Validators.required),
    descriptionFormControl: new FormControl('', Validators.required),
  });

  constructor(public hpoService: HpoService) {
  }

  ngOnInit(): void {
    this.formControlGroup.get("maxoFormControl").valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        // Make request to pubmed
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
        // Make request to pubmed
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

  submitForm() {
    //TODO: Do some funky stuff here
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
