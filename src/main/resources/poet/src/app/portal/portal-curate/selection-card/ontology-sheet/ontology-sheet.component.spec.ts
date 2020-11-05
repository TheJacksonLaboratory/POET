import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OntologySheet } from './ontology-sheet.component';
import { SharedModule } from "../../../../shared/shared.module";
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from "@angular/material/bottom-sheet";

describe('OntologySheetComponent', () => {
  let component: OntologySheet;
  let fixture: ComponentFixture<OntologySheet>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OntologySheet ],
      imports: [ SharedModule ],
      providers: [
        { provide: MatBottomSheetRef, useValue: {} },
        { provide: MAT_BOTTOM_SHEET_DATA, useValue: {} }
        ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OntologySheet);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
