import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogDiseaseComponent } from './dialog-disease.component';

describe('DialogDiseaseComponent', () => {
  let component: DialogDiseaseComponent;
  let fixture: ComponentFixture<DialogDiseaseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogDiseaseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogDiseaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
