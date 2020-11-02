import { Component, OnInit } from '@angular/core';
import { HpoService } from "../../../shared/services/hpo.service";
import { FormControl } from "@angular/forms";
import { debounceTime, distinctUntilChanged, finalize } from "rxjs/operators";

@Component({
  selector: 'app-maxo-curation',
  templateUrl: './maxo-curation.component.html',
  styleUrls: ['./maxo-curation.component.scss']
})
export class MaxoCurationComponent implements OnInit {

  maxoFormControl = new FormControl();
  hpoFormControl = new FormControl();
  selectedMaxo: any;
  selectedHpo: any;
  constructor(public hpoService: HpoService) { }

  ngOnInit(): void {
    this.maxoFormControl.valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        // Make request to pubmed
        if(query && query.length > 3){
          this.hpoService.searchMaxoTerms(query).subscribe((data) => {
            if(!data){
              this.maxoFormControl.setErrors({notFound: true});
            }
            this.selectedMaxo = data;
          }, (err) => {
            this.maxoFormControl.setErrors({notFound: true});
          });
        }
      });

    this.hpoFormControl.valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        // Make request to pubmed
        if(query && query.length > 3){
          this.hpoService.searchHPOTerms(query).subscribe((data) => {
            if(!data){
              this.hpoFormControl.setErrors({notFound: true});
            }
            this.selectedHpo = data;
          }, (err) => {
            this.hpoFormControl.setErrors({notFound: true});
          });
        }
      });
  }

  resetMaxoForm() {
    this.maxoFormControl.reset();
  }

  resetHpoForm() {
    this.maxoFormControl.reset();
  }

}
