import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogMessagesComponent } from './dialog-messages.component';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

describe('DialogMessagesComponent', () => {
  let component: DialogMessagesComponent;
  let fixture: ComponentFixture<DialogMessagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogMessagesComponent ],
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
    fixture = TestBed.createComponent(DialogMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
