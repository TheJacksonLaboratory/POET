import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DialogReviewComponent } from './dialog-review.component';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

describe('DialogReviewComponent', () => {
  let component: DialogReviewComponent;
  let fixture: ComponentFixture<DialogReviewComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogReviewComponent ],
      providers: [
        {
          provide: MatDialogRef,
          useValue: {}
        }, {
          provide: MAT_DIALOG_DATA,
          useValue: {}
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
