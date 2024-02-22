import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TreatmentCurationComponent } from './treatment-curation.component';
import { OntologyService } from "../../../shared/services/external/ontology.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { SharedModule } from "../../../shared/shared.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { MondoService } from "../../../shared/services/external/mondo.service";

describe('MaxoCurationComponent', () => {
  let component: TreatmentCurationComponent;
  let fixture: ComponentFixture<TreatmentCurationComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [TreatmentCurationComponent],
      providers: [OntologyService, MondoService],
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
