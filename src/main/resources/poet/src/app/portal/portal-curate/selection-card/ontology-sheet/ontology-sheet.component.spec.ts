import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OntologySheetComponent } from './ontology-sheet.component';

describe('OntologySheetComponent', () => {
  let component: OntologySheetComponent;
  let fixture: ComponentFixture<OntologySheetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OntologySheetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OntologySheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
