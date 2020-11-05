import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { FormControl } from "@angular/forms";
import { debounceTime, finalize } from "rxjs/operators";
import { tap } from "rxjs/internal/operators/tap";
import { switchMap } from "rxjs/internal/operators/switchMap";
import { CurationService } from "../services/curation.service";
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

  constructor(private curationService: CurationService) { }

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(500),
        tap(() => {
          this.errorMsg = "";
          this.filteredResponse = [];
          this.isLoading = true;
        }),
        switchMap(value => {
          if(this.hasValidInput(value)){
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
        if (data['results'] == undefined) {
          this.errorMsg = data['Error'];
          this.filteredResponse = [];
        } else {
          this.errorMsg = "";
          this.filteredResponse = data['results'];
        }
      });
  }

  onSelection(result){
    this.onSearchSelect.emit(result);
  }

  hasValidInput(val: string){
    if(val && val.length >= 3){
      return val;
    }
  }

  displayFn(searchResult: SearchResult){
    if(searchResult){
      return searchResult.name;
    }
  }
}
