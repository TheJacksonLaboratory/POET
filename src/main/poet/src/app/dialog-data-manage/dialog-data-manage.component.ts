import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogReviewComponent } from '../portal/portal-curate/dialog-review/dialog-review.component';
import { UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { environment } from '../../environments/environment';
import { CurationService } from '../shared/services/curation/curation.service';
import { ReleaseState } from '../shared/models/models';

@Component({
  selector: 'app-dialog-export',
  templateUrl: './dialog-data-manage.component.html',
  styleUrls: ['./dialog-data-manage.component.scss']
})
export class DialogDataManageComponent implements OnInit {

  title = 'Data Management';
  selectedOntology;
  selectedVersion;
  selectedAction = 'export';
  selectedReleaseKey;
  releaseState = ReleaseState.NOT_STARTED;
  releaseText = '';
  exportForm = new UntypedFormGroup({
    ontologyControl: new UntypedFormControl({value: '', disabled: false}, [Validators.required]),
    versionControl: new UntypedFormControl({value: '', disabled: false}),
    keyControl: new UntypedFormControl({value: '', disabled: false})
  });
  constructor(public dialogRef: MatDialogRef<DialogReviewComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any, private curationService: CurationService) {
  }

  ngOnInit(): void {
    this.exportForm.get('ontologyControl').valueChanges.subscribe(() => {
      this.exportForm.get('versionControl').reset();
    });
  }

  getDataActionText(){
    if (this.selectedAction === 'release'){
      return 'Release';
    }
    return 'Download';
  }

  doDataAction(){
    if (this.selectedAction === 'release'){
      this.releaseState = ReleaseState.IN_PROGRESS;
      this.title = 'Releasing data...';
      // make a call to release with key
      // if the key don't match we should raise an error
      this.curationService.releasePoetAnnotations(this.exportForm.get('keyControl').value).subscribe(() => {
        // return release stats?
        // create a table to store statistics of differences in release updates.
        this.releaseState = ReleaseState.DONE_SUCCESS;
        this.title = 'POET Released!';
        this.releaseText = 'A true poet. The release was successful.';
      }, (error) => {
        this.title = 'Error Releasing';
        if (error.status === 401){
          // key is not right
          this.releaseText = 'The key provided did not match the expected.';
        } else {
          // server is down
          // there was a massive error with the release method
          this.releaseText = 'There was an error releasing the data. Please contact support.';
          // TODO: Make this handling better;
        }
        this.releaseState = ReleaseState.DONE_ERROR;
      });
    } else {
      if (this.exportForm.get('versionControl').value === 'official'){
        if (this.exportForm.get('ontologyControl').value === 'hpo'){
          window.open(environment.HPO_ANNOTATIONS_URL);
        } else {
          // TODO: shouldn't get here
        }
      } else {
        window.open(`${environment.POET_API_EXPORT_URL}${this.exportForm.get('ontologyControl').value}?unstable=true`);
        this.dialogRef.close();
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

  releaseStarted() {
    return this.releaseState === ReleaseState.IN_PROGRESS;
  }

  showOptions() {
    return this.releaseState === ReleaseState.NOT_STARTED;
  }

  releaseSuccess(){
    return this.releaseState === ReleaseState.DONE_SUCCESS;
  }

  releaseError(){
    return this.releaseState === ReleaseState.DONE_ERROR;
  }

  releaseDone(){
    return this.releaseState === ReleaseState.DONE_SUCCESS || this.releaseState === ReleaseState.DONE_ERROR;
  }

  reset() {
    this.releaseState = ReleaseState.NOT_STARTED;
    this.releaseText = '';
    const previousOntologyValue = this.exportForm.get('ontologyControl').value;
    this.exportForm.reset();
    this.exportForm.get('ontologyControl').setValue(previousOntologyValue);
  }

  closing(){
    this.data.release = true;
    this.dialogRef.close(this.data);
  }
}
