import { Component, OnInit } from '@angular/core';
import { MatBottomSheetRef } from "@angular/material/bottom-sheet";

@Component({
  selector: 'app-delete-sheet',
  templateUrl: './delete-sheet.component.html',
})
export class DeleteSheetComponent implements OnInit {

  constructor(private _bottomSheetRef: MatBottomSheetRef<DeleteSheetComponent>) { }

  ngOnInit(): void {
  }

  closeDialog(shouldDelete: boolean): void {
    this._bottomSheetRef.dismiss(shouldDelete);
  }

}
