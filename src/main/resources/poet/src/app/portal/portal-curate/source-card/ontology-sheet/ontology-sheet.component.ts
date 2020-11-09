import { Component } from '@angular/core';
import { MatBottomSheetRef } from "@angular/material/bottom-sheet";

@Component({
  selector: 'app-ontology-sheet',
  templateUrl: './ontology-sheet.component.html',
  styleUrls: ['./ontology-sheet.component.scss']
})
export class OntologySheet {

  constructor(private _bottomSheetRef: MatBottomSheetRef<OntologySheet>) {}

  saveOntologySelection(ontology: string): void {
    this._bottomSheetRef.dismiss(ontology);
  }
}
