import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { UntypedFormControl } from "@angular/forms";
import { catchError, debounceTime, finalize } from "rxjs/operators";
import { tap } from "rxjs/internal/operators/tap";
import { switchMap } from "rxjs/internal/operators/switchMap";
import { CurationService } from "../services/curation/curation.service";
import { of } from "rxjs/internal/observable/of";
import { MondoSearchResult } from "../models/search-models";
import { Router } from "@angular/router";
import { MatAutocompleteTrigger } from "@angular/material/autocomplete";
import { MondoService } from "../services/external/mondo.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { forkJoin } from "rxjs";
import { UtilityService } from "../services/utility.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  @Input() role: string;
  @ViewChild(MatAutocompleteTrigger, {read: MatAutocompleteTrigger}) searchBar: MatAutocompleteTrigger;
  @ViewChild("search") searchInput: ElementRef<HTMLInputElement>;
  searchControl = new UntypedFormControl();
  diseaseOptions: MondoSearchResult[];
  isLoading = false;
  errorMsg: string;

  constructor(private curationService: CurationService, private monarchService: MondoService,
              public utilityService: UtilityService, private router: Router, private _snackBar: MatSnackBar) {
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
            let prefix = "MONDO";
            this.isLoading = true;
            if(value.startsWith("OMIM:")){
              prefix = "OMIM";
            }
            return this.monarchService.searchMondo(value, prefix)
              .pipe(
                finalize(() => {
                  this.isLoading = false
                })
              );
          } else {
            return of();
          }
        })).subscribe((data: MondoSearchResult[]) => {
            if (!data || data.length == 0) {
              this.searchControl.setErrors({notFound: true});
            }
            this.diseaseOptions = data;
          }, (err) => {
            this.searchControl.setErrors({notFound: true});
        });
  }

  resetSearchForm(){
    this.searchControl.reset();
    this.searchInput.nativeElement.blur();
    this.isLoading = false;
    this.diseaseOptions = [];
  }

  onSelection(monarchSearchResult: MondoSearchResult) {
    // Get the disease information first
    const monarchDiseaseHttp = this.monarchService.getDisease(monarchSearchResult.id).pipe(catchError(error => of(error)));
    const poetDiseaseHttp = this.curationService.getDisease(monarchSearchResult.omim_id).pipe(catchError(error => of(error)));

    forkJoin([monarchDiseaseHttp, poetDiseaseHttp]).subscribe(responseList => {
      const monarchDiseaseData = responseList[0];
      const poetResponse = responseList[1];
      const diseaseToSave = {
        description: monarchDiseaseData.description,
        diseaseId: monarchSearchResult.omim_id,
        diseaseName: monarchDiseaseData.label,
        equivalentId: monarchSearchResult.id
      };
      // isnt an error
      if(poetResponse && !poetResponse.error){
        // if response does not include equivalent id or description
        if(poetResponse.equivalentId == null || poetResponse.description == null || poetResponse.description == ""){
          this.curationService.updateDisease(diseaseToSave).subscribe(() => {
            this.resetSearchForm();
            this.router.navigate(['/portal/curate/' + poetResponse.diseaseId]);
          }, error =>    this._snackBar.open('Error Silently Updating Disease!', 'Close', {
            duration: 3000,
            horizontalPosition: "left"
          })); // TODO: fix error handling here
        } else {
          // Forward to disease page
          this.resetSearchForm();
          this.router.navigate(['/portal/curate/' + poetResponse.diseaseId]);
        }
      } else {
        // we insert into database
        this.curationService.saveDisease(diseaseToSave).subscribe(() => {
          this.resetSearchForm();
          this.router.navigate(['/portal/curate/' + diseaseToSave.diseaseId]);
        }, () => {
          this._snackBar.open('Error Saving New Disease!', 'Close', {
            duration: 3000,
            horizontalPosition: "left"
          });
        });
      }
    });
  }

  hasValidInput(val: string) {
    if (val && val.length >= 3) {
      return val;
    }
  }
}
