import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TreatmentCurationComponent } from './treatment-curation.component';
import { HpoService } from "../../../shared/services/external/hpo.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { SharedModule } from "../../../shared/shared.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { MonarchService } from "../../../shared/services/external/monarch.service";

describe('MaxoCurationComponent', () => {
  let component: TreatmentCurationComponent;
  let fixture: ComponentFixture<TreatmentCurationComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [TreatmentCurationComponent],
      providers: [HpoService, MonarchService],
      imports: [HttpClientTestingModule, SharedModule, NoopAnimationsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TreatmentCurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
