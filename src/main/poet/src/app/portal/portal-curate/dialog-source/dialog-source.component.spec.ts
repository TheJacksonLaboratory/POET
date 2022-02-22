import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DialogSourceComponent } from './dialog-source.component';
import { CurationService } from "../../../shared/services/curation/curation.service";
import { PubmedService } from "../../../shared/services/external/pubmed.service";
import { SharedModule } from "../../../shared/shared.module";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

describe('DialogSourceComponent', () => {
  let component: DialogSourceComponent;
  let fixture: ComponentFixture<DialogSourceComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule, HttpClientTestingModule, NoopAnimationsModule],
      declarations: [DialogSourceComponent],
      providers: [CurationService, PubmedService,
        {
          provide: MatDialogRef,
          useValue: {}
        }, {
          provide: MAT_DIALOG_DATA,
          useValue: {}
        }]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogSourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
