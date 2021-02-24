import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl } from "@angular/forms";
import { debounceTime, finalize } from "rxjs/operators";
import { tap } from "rxjs/internal/operators/tap";
import { switchMap } from "rxjs/internal/operators/switchMap";
import { CurationService } from "../services/curation/curation.service";
import { of } from "rxjs/internal/observable/of";
import { SearchResult } from "../models/search-models";
import { DialogDiseaseComponent } from "../dialog-disease/dialog-disease.component";
import { MatDialog } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { MatAutocompleteTrigger } from "@angular/material/autocomplete";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  @Input() role: string;
  @ViewChild(MatAutocompleteTrigger, {read: MatAutocompleteTrigger}) searchBar: MatAutocompleteTrigger;
  searchControl = new FormControl();
  filteredResponse: any;
  isLoading = false;
  errorMsg: string;

  constructor(private curationService: CurationService, private router: Router, public dialog: MatDialog) {
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
      ).subscribe((data: any[]) => {
        if(this.isElevatedCurator()){
          this.filteredResponse = [{name: "Create a New Disease", type: "new" }];
        }
        if (data && data.length > 0) {
          this.errorMsg = ""
          this.filteredResponse.push.apply(this.filteredResponse, data); // flatten arrays
        }
    }, ()=> {
        this.isLoading = false;
    });
  }

  showCreate(){
    if(!this.searchBar.panelOpen && !this.searchControl.value && this.isElevatedCurator()){
      this.filteredResponse = [{name: "Create a New Disease", type: "new" }];
      this.searchBar.openPanel();
    }
  }

  resetSearchForm(){
    this.searchControl.reset();
    this.isLoading = false;
    this.filteredResponse = [];
  }

  onSelection(result) {
    if(result.type == 'disease'){
      this.router.navigate(['/portal/curate/' + result.id]);
    } else if(result.type == 'new'){
      // open new disease dialog
      this.dialog.open(DialogDiseaseComponent, {
        minWidth: 500,
        maxWidth: 600
      })
    }
  }

  hasValidInput(val: string) {
    if (val && val.length >= 3) {
      return val;
    }
  }

  displayFn(searchResult: SearchResult) {
    if (searchResult && searchResult.name != "Create a New Disease") {
      return searchResult.name;
    }
  }

  isElevatedCurator(){
    return this.role === 'ELEVATED_CURATOR';
  }
}
