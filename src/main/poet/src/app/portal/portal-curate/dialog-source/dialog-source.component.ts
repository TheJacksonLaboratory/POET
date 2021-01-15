import { Component, Inject, OnInit } from '@angular/core';
import { ErrorStateMatcher } from "@angular/material/core";
import { FormControl, FormGroupDirective, NgForm, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { PubmedService } from "../../../shared/services/external/pubmed.service";
import { StateService } from "../../../shared/services/state/state.service";
import { Publication } from "../../../shared/models/models";
import { Observable } from "rxjs";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: 'app-dialog-curation',
  templateUrl: './dialog-source.component.html',
  styleUrls: ['./dialog-source.component.scss']
})
export class DialogSourceComponent implements OnInit {

  annotationSourceControl = new FormControl('', [
    Validators.required]);
  selectedOntology: string;
  selectedPublication: any;
  selectedDisease: any = {diseaseName: "", diseaseId: ""};
  selectedType: string;
  annotatedPublications$: Observable<Publication[]>;
  newPublication: boolean = false;
  searchingPubMed: boolean = false;
  matcher = new DialogErrorStateMatcher();
  newPublicationChecks: any;

  constructor(public dialogRef: MatDialogRef<DialogSourceComponent>,
              private curationService: CurationService, private pubmedService: PubmedService,
              private stateService: StateService,
              @Inject(MAT_DIALOG_DATA) public data: any, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.stateService.selectedDisease.subscribe((disease) => {
      if (disease) {
        this.selectedDisease = disease;
        this.newPublicationChecks = [
          {
            name: 'I have reviewed that the above publication information matches my publication.',
            completed: false,
            color: 'primary'
          },
          {name: this.getSecondAffirmation(), completed: false, color: 'primary'}
        ];
        this.annotatedPublications$ = this.curationService.getDiseasePublications(disease.diseaseId);
      }
    });
    this.stateService.selectedOntology.subscribe((ontology) => {
      this.selectedOntology = ontology;
    })
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
    this.stateService.setSelectedSource({
      disease: this.selectedDisease,
      publication: this.selectedPublication
    });
    this.dialogRef.close();
  }

  selectDisease(disease: any) {
    this.selectedDisease = disease;
  }

  resetForm() {
    this.annotationSourceControl.reset();
  }

  saveNewPublication() {
    this.curationService.savePublication(
      {
        disease: this.selectedDisease,
        publication: {
          "publicationId": "PMID:" + this.selectedPublication.uid,
          "publicationName": this.selectedPublication.title,
          "date": this.selectedPublication.pubdate,
          "firstAuthor": this.selectedPublication.sortfirstauthor
        }
      }
    ).subscribe(() => {
      this.annotatedPublications$ = this.curationService.getDiseasePublications(this.selectedDisease.diseaseId);
      this.newPublication = false;
    }, error => {
      const message = this.getErrorMessage(error);
      this._snackBar.open(message, 'Close', {
        duration: 5000,
      });
    });
  }

  getErrorMessage(error: any) {
    if (error.error.message) {
      return "[ERROR]: " + error.error.message;
    } else {
      return "[ERROR]: " + error.message;
    }
  }

  isElevatedCurator() {
    return this.data.userRole == "ELEVATED_CURATOR";
  }

  dialogRequirementsMet() {
    return this.selectedPublication != null;
  }

  selectPublication(publication: any) {
    if (publication == "new") {
      this.selectedPublication = null;
      this.newPublication = true;
    } else {
      this.selectedPublication = publication;
    }
  }

  allTasksCompleted() {
    return this.newPublicationChecks.every(t => t.completed);
  }

  getSecondAffirmation() {
    if (this.selectedOntology == 'maxo') {
      return 'I have affirmed that this publication describes medical actions for ' + this.selectedDisease.diseaseName;
    }
    return 'I have affirmed that this publication describes phenotypes for ' + this.selectedDisease.diseaseName;
  }

}

/** Error when invalid control is dirty, touched, or submitted. */
export class DialogErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
