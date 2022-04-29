import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogReviewComponent } from '../portal/portal-curate/dialog-review/dialog-review.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { environment } from '../../environments/environment';
import { CurationService } from '../shared/services/curation/curation.service';

@Component({
  selector: 'app-dialog-export',
  templateUrl: './dialog-export.component.html',
  styleUrls: ['./dialog-export.component.scss']
})
export class DialogExportComponent implements OnInit {

  selectedOntology;
  selectedVersion;
  selectedAction = 'export';
  selectedReleaseKey;

  exportForm = new FormGroup({
    ontologyControl: new FormControl({value: '', disabled: false}, [Validators.required]),
    versionControl: new FormControl({value: '', disabled: false}),
    keyControl: new FormControl({value: '', disabled: false})
  });
  constructor(public dialogRef: MatDialogRef<DialogReviewComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any, private curationService: CurationService) {
  }

  ngOnInit(): void {

  }

  getDataActionText(){
    if (this.selectedAction === 'release'){
      return 'Release';
    }
    return 'Download';
  }

  doDataAction(){
    if (this.selectedAction === 'release'){
      // make a call to release with key
      // if the key don't match we should raise an error
      this.curationService.exportPoetAnnotations(this.exportForm.get('keyControl').value).subscribe(() => {
        // return release stats?
        // create a table to store statistics of differences in release updates.
      }, (error) => {
        // key is not right
        // server is down
        // there was a massive error with the release method
      });
    } else {
      if (this.selectedVersion === 'official'){
        // TODO: put the purl here for respective ontology
      } else {
        window.open(environment.POET_API_EXPORT_URL + this.exportForm.get('ontologyControl').value);
      }
    }
  }

  disableDialogAction(){
    if (this.selectedAction === 'export' && this.exportForm.get('ontologyControl').value !== '' &&
      this.exportForm.get('versionControl').value !== ''){
      return false;
    } else if (this.selectedAction === 'release' && this.exportForm.get('ontologyControl').value !== '' &&
      this.exportForm.get('keyControl').value !== ''){
      return false;
    }
    return true;
  }
}
