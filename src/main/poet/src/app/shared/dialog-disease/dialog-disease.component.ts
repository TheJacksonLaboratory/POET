import { Component, OnInit } from '@angular/core';
import { CurationService } from "../services/curation/curation.service";
import { MonarchService } from "../services/external/monarch.service";
import { FormControl } from "@angular/forms";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { HpoService } from "../services/external/hpo.service";
import { Router } from "@angular/router";
import { MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: 'app-dialog-disease',
  templateUrl: './dialog-disease.component.html',
  styleUrls: ['./dialog-disease.component.scss']
})
export class DialogDiseaseComponent implements OnInit {

  formControl = new FormControl('diseaseControl');
  diseaseOptions: any;
  selectedDisease: any;
  constructor(private curationService: CurationService, private hpoService: HpoService,
              private monarchService: MonarchService, private router: Router,
              public dialogRef: MatDialogRef<DialogDiseaseComponent>, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.formControl.valueChanges
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(query => {
        if (query && query.length >= 3) {
          this.monarchService.searchDisease(query).subscribe((data) => {
            if (!data) {
              this.formControl.setErrors({notFound: true});
            }
            this.diseaseOptions = data;
          }, (err) => {
            this.formControl.setErrors({notFound: true});
          });
        }
      });
  }

  saveNewDisease(){
    this.curationService.saveDisease(this.selectedDisease).subscribe(() => {
      this.router.navigate(['/portal/curate/' + this.selectedDisease.diseaseId]);
      this.dialogRef.close();
    }, () => {
      this._snackBar.open('Error Saving New Disease!', 'Close', {
        duration: 3000,
        horizontalPosition: "left"
      });
    })
  }

  selectDisease(disease){
    this.curationService.getDisease(disease.id).subscribe((data) => {
      // Disease Selected Already Exists, forward to page
      if(data){
        this.router.navigate(['/portal/curate/' + disease.diseaseId]);
        this.dialogRef.close();
      }
    });
    // Get disease information from monarch
    this.monarchService.getDisease(disease.id).subscribe((diseaseData: any) => {
      this.selectedDisease = {
        description: diseaseData.description,
        diseaseId: disease.id,
        diseaseName: disease.label.length > 0 ? disease.label[0] : disease.match
      };
    });
  }

  resetDiseaseControl(){
    this.formControl.reset();
    this.selectedDisease = null;
  }

  displayHpoFn(option: any) {
    return option && option.dbName ? `${option.dbName} ${option.diseaseId}` : '';
  }
}
