import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-dialog-messages',
  templateUrl: './dialog-messages.component.html',
  styleUrls: ['./dialog-messages.component.scss']
})
export class DialogMessagesComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<DialogMessagesComponent>) { }

  ngOnInit(): void {
  }

}
