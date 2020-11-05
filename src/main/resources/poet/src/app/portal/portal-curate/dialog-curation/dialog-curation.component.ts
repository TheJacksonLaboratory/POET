import { Component, OnInit } from '@angular/core';
import { ErrorStateMatcher } from "@angular/material/core";
import { FormControl, FormGroupDirective, NgForm, Validators } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { SearchResult } from "../../../shared/models/search-models";
import { CurationService } from "../../../shared/services/curation.service";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { PubmedService } from "../../../shared/services/pubmed.service";

@Component({
  selector: 'app-dialog-curation',
  templateUrl: './dialog-curation.component.html',
  styleUrls: ['./dialog-curation.component.scss']
})
export class DialogCurationComponent implements OnInit {

  annotationSourceControl = new FormControl('', [
    Validators.required]);
  curationOptions: any[] = [
    {value: 'hpo', view: 'Human Phenotype Ontology (HPO)'},
    {value: 'maxo', view: 'Medical Action Ontology (MAxO)'}
  ];
  selectedOntology: string;
  selectedPublication: any;
  selectedDisease: any;
  newCuration: boolean = false;
  searchingPubMed: boolean = false;
  selectedExisting: boolean = false;
  matcher = new DialogErrorStateMatcher();

  constructor(public dialogRef: MatDialogRef<DialogCurationComponent>,
              private curationService: CurationService, private pubmedService: PubmedService) {
  }

  ngOnInit(): void {
    this.annotationSourceControl.valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(id => {
        // Make request to pubmed
        if (id) {
          this.searchingPubMed = true;
          this.pubmedService.findPublication(id).subscribe((data) => {
            if (!data) {
              this.annotationSourceControl.setErrors({notFound: true});
            }
            this.selectedPublication = data;
          }, (err) => {
            this.annotationSourceControl.setErrors({notFound: true});
          }).add(() => {
            this.searchingPubMed = false;
          });
        }
      })
  }

  closeDialog() {
    if (!this.selectedExisting) {
      this.dialogRef.close(
        {
          'ontology': this.selectedOntology,
          'publication': this.selectedPublication,
          'disease': this.selectedDisease,
          'action': 'create'
        });
    } else {
      this.dialogRef.close(
        {
          'ontology': this.selectedOntology,
          'publication': this.selectedPublication,
          'disease': this.selectedDisease,
          'action': 'fetch'
        });
    }
  }

  selectExisting(searchResult: SearchResult) {
    this.selectedExisting = true;
    if (searchResult.type == 'disease') {
      this.selectedDisease = searchResult;
    } else if (searchResult.type == 'publication') {
      this.selectedPublication = searchResult;
    }
  }

  selectDisease(disease: any) {
    this.selectedDisease = disease;
  }

  resetForm() {
    this.annotationSourceControl.reset();
  }

  shouldShowOntologySelection() {
    return (this.selectedDisease && this.selectedPublication) || this.selectedExisting;
  }

  dialogRequirementsMet() {
    return (this.annotationSourceControl.valid && this.selectedDisease && this.selectedPublication) || this.selectedExisting;
  }

}

/** Error when invalid control is dirty, touched, or submitted. */
export class DialogErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
