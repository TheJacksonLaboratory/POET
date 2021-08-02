import { Component, OnInit } from '@angular/core';
import { MatBottomSheetRef } from "@angular/material/bottom-sheet";

@Component({
  selector: 'poet-confirm-sheet',
  templateUrl: './confirm-sheet.component.html',
})
export class ConfirmSheetComponent implements OnInit {

  constructor(private _bottomSheetRef: MatBottomSheetRef<ConfirmSheetComponent>) { }

  ngOnInit(): void {
  }

  closeDialog(shouldAction: boolean): void {
    this._bottomSheetRef.dismiss(shouldAction);
  }

}
