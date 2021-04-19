import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl } from "@angular/forms";
import { debounceTime, finalize } from "rxjs/operators";
import { tap } from "rxjs/internal/operators/tap";
import { switchMap } from "rxjs/internal/operators/switchMap";
import { CurationService } from "../services/curation/curation.service";
import { of } from "rxjs/internal/observable/of";
import { MonarchSearchResult } from "../models/search-models";
import { Router } from "@angular/router";
import { MatAutocompleteTrigger } from "@angular/material/autocomplete";
import { MonarchService } from "../services/external/monarch.service";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  @Input() role: string;
  @ViewChild(MatAutocompleteTrigger, {read: MatAutocompleteTrigger}) searchBar: MatAutocompleteTrigger;
  searchControl = new FormControl();
  diseaseOptions: any;
  isLoading = false;
  errorMsg: string;

  constructor(private curationService: CurationService, private monarchService: MonarchService,
              private router: Router, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(500),
        tap(() => {
          this.errorMsg = "";
          this.diseaseOptions = [];
        }),
        switchMap(value => {
          if (this.hasValidInput(value)) {
            this.isLoading = true;
            return this.monarchService.searchMonarch(value, "MONDO")
              .pipe(
                finalize(() => {
                  this.isLoading = false
                }),
              );
          } else {
            return of();
          }
        })).subscribe(data => {
            if (!data) {
              this.searchControl.setErrors({notFound: true});
            }
            this.diseaseOptions = data;
          }, (err) => {
            this.searchControl.setErrors({notFound: true});
        });
  }

  resetSearchForm(){
    this.searchControl.reset();
    this.isLoading = false;
    this.diseaseOptions = [];
  }

  onSelection(monarchSearchResult: MonarchSearchResult) {
    this.curationService.getDisease(monarchSearchResult.id).subscribe( result => {
      this.router.navigate(['/portal/curate/' + result.diseaseId]);
      }, error => {
       if(error.status == 404){
         this.monarchService.getDisease(monarchSearchResult.id).subscribe((diseaseData: any) => {
           const diseaseToSave = {
             description: diseaseData.description,
             diseaseId: diseaseData.id,
             diseaseName: diseaseData.label
           };
           this.curationService.saveDisease(diseaseToSave).subscribe(() => {
             this.router.navigate(['/portal/curate/' + diseaseToSave.diseaseId]);
           }, () => {
             this._snackBar.open('Error Saving New Disease!', 'Close', {
               duration: 3000,
               horizontalPosition: "left"
             });
           })
         });
       }
    });
  }

  hasValidInput(val: string) {
    if (val && val.length >= 3) {
      return val;
    }
  }

  displayFn(monarchSearchResult: MonarchSearchResult) {
    if (monarchSearchResult) {
      return monarchSearchResult.label[0];
    }
  }
}
