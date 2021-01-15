import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl } from "@angular/forms";
import { debounceTime, distinctUntilChanged, finalize, switchMap, tap } from "rxjs/operators";
import { MonarchService } from "../services/external/monarch.service";
import { MonarchSearchResult } from "../models/search-models";
import { of } from "rxjs/internal/observable/of";

@Component({
  selector: 'app-monarch-search',
  templateUrl: './monarch-search.component.html',
  styleUrls: ['./monarch-search.component.scss']
})
export class MonarchSearchComponent implements OnInit {


  @Output()
  onDiseaseSelect: EventEmitter<any> = new EventEmitter<any>();
  searchControl = new FormControl();
  filteredResponse: any;
  isLoading = false;
  errorMsg: string;

  constructor(private monarchService: MonarchService) {
  }

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => {
          this.errorMsg = "";
          this.filteredResponse = [];
          this.isLoading = true;
        }),
        switchMap((value) => {
            if (this.hasValidInput(value)) {
              return this.monarchService.getDisease(value)
                .pipe(
                  finalize(() => {
                    this.isLoading = false
                  })
                );
            } else {
              return of();
            }
          }
        ))
      .subscribe(data => {
        if (data['docs'].length <= 0) {
          this.errorMsg = data['Error'];
          this.filteredResponse = [];
        } else {
          this.errorMsg = "";
          this.filteredResponse = data['docs'];
        }
      });
  }

  selectDisease(monarchSearchResult: MonarchSearchResult) {
    console.log(monarchSearchResult);
    this.onDiseaseSelect.emit(monarchSearchResult);
  }

  displayFn(monarchSearchResult: MonarchSearchResult) {
    if (monarchSearchResult) {
      return monarchSearchResult.match;
    }
  }

  hasValidInput(val: string) {
    if (val && val.length >= 3) {
      return val;
    }
  }
}
