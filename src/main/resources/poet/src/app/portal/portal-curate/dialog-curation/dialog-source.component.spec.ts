import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogSourceComponent } from './dialog-source.component';
import { CurationService } from "../../../shared/services/curation/curation.service";
import { PubmedService } from "../../../shared/services/external/pubmed.service";
import { SharedModule } from "../../../shared/shared.module";
import { MatDialogRef } from "@angular/material/dialog";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

describe('DialogCurationComponent', () => {
  let component: DialogSourceComponent;
  let fixture: ComponentFixture<DialogSourceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule, HttpClientTestingModule, BrowserAnimationsModule],
      declarations: [ DialogSourceComponent ],
      providers: [CurationService, PubmedService,
        { provide: MatDialogRef,
          useValue: {} }]
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
