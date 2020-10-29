import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCurationComponent } from './dialog-curation.component';
import { CurationService } from "../../../shared/services/curation.service";
import { PubmedService } from "../../../shared/services/pubmed.service";
import { SharedModule } from "../../../shared/shared.module";
import { MatDialogRef } from "@angular/material/dialog";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

describe('DialogCurationComponent', () => {
  let component: DialogCurationComponent;
  let fixture: ComponentFixture<DialogCurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule, HttpClientTestingModule, BrowserAnimationsModule],
      declarations: [ DialogCurationComponent ],
      providers: [CurationService, PubmedService,
        { provide: MatDialogRef,
          useValue: {} }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
