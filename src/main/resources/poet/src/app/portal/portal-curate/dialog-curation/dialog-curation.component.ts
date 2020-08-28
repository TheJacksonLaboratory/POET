import { Component, OnInit } from '@angular/core';
import {ErrorStateMatcher} from "@angular/material/core";
import {FormControl, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-dialog-curation',
  templateUrl: './dialog-curation.component.html',
  styleUrls: ['./dialog-curation.component.scss']
})
export class DialogCurationComponent implements OnInit {

  annotationSourceControl = new FormControl('', [
    Validators.required]);
  curationOptions: any[] = [
    { value:  'hpo', view: 'Human Phenotype Ontology (HPO)' },
    { value:  'maxo', view: 'Medical Action Ontology (MAxO)' }
  ];
  selectedOntology: string;
  selectedPMID: "";
  newCuration: boolean = false;
  matcher = new DialogErrorStateMatcher();

  constructor(public dialogRef: MatDialogRef<DialogCurationComponent>) { }

  ngOnInit(): void {}

  closeDialog() {
    this.dialogRef.close({'ontology': this.selectedOntology, 'doi': this.selectedPMID});
  }

}

/** Error when invalid control is dirty, touched, or submitted. */
export class DialogErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
