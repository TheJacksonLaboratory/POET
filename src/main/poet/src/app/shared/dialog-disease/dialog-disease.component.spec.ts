import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogDiseaseComponent } from './dialog-disease.component';
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { RouterTestingModule } from "@angular/router/testing";
import { MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { SharedModule } from "../shared.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

describe('DialogDiseaseComponent', () => {
  let component: DialogDiseaseComponent;
  let fixture: ComponentFixture<DialogDiseaseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule, RouterTestingModule, SharedModule, NoopAnimationsModule ],
      declarations: [ DialogDiseaseComponent ],
      providers: [{
        provide: MatDialogRef,
        useValue: {}
        },
        {
          provide: MatSnackBar,
          useValue: {}
        }]
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
