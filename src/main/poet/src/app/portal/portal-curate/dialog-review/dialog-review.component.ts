import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, Validators } from "@angular/forms";

@Component({
  selector: 'app-dialog-review',
  templateUrl: './dialog-review.component.html',
  styleUrls: ['./dialog-review.component.scss']
})
export class DialogReviewComponent implements OnInit {

  title: string;
  message: string;
  approve: boolean;
  denyMessageControl = new FormControl({value: '', disabled: false}, Validators.required);
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<DialogReviewComponent>) { }

  ngOnInit(): void {
    this.title = this.data.title;
    this.approve = this.data.approve;
  }

  closeDialog(action: string){
    if(action == "yes"){
      this.data.confirmed = true;
      this.data.message = this.denyMessageControl.value;
    } else {
      this.data.confirmed = false;
    }
    this.dialogRef.close(this.data);
  }
}