import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl } from "@angular/forms";
import { debounceTime, finalize } from "rxjs/operators";
import { tap } from "rxjs/internal/operators/tap";
import { switchMap } from "rxjs/internal/operators/switchMap";
import { CurationService } from "../services/curation/curation.service";
import { of } from "rxjs/internal/observable/of";
import { SearchResult } from "../models/search-models";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  @Output()
  onSearchSelect: EventEmitter<any> = new EventEmitter<any>();
  searchControl = new FormControl();
  filteredResponse: any;
  isLoading = false;
  errorMsg: string;

  constructor(private curationService: CurationService) {
  }

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(500),
        tap(() => {
          this.errorMsg = "";
          this.filteredResponse = [];
        }),
        switchMap(value => {
          if (this.hasValidInput(value)) {
            this.isLoading = true;
            return this.curationService.searchAll(value)
              .pipe(
                finalize(() => {
                  this.isLoading = false
                }),
              );
          } else {
            return of();
          }
        })
      ).subscribe(data => {
      if (data) {
        this.errorMsg = ""
        this.filteredResponse = data;
      }
    }, ()=> {}, ()=> {
        this.isLoading = false;
    });
  }

  resetSearchForm(){
    this.searchControl.reset();
    this.isLoading = false;
    this.filteredResponse = [];
  }

  onSelection(result) {
    this.onSearchSelect.emit(result);
  }

  hasValidInput(val: string) {
    if (val && val.length >= 3) {
      return val;
    }
  }

  displayFn(searchResult: SearchResult) {
    if (searchResult) {
      return searchResult.name;
    }
  }
}
