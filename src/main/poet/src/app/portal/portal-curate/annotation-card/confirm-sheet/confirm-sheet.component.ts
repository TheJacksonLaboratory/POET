import { Component } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';

@Component({
  selector: 'poet-confirm-sheet',
  templateUrl: './confirm-sheet.component.html',
})
export class ConfirmSheetComponent {

  constructor(private _bottomSheetRef: MatBottomSheetRef<ConfirmSheetComponent>) { }


  closeDialog(shouldAction: boolean): void {
    this._bottomSheetRef.dismiss(shouldAction);
  }

}
