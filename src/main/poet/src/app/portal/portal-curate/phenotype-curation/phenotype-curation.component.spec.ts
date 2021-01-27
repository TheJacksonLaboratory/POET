import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhenotypeCurationComponent } from './phenotype-curation.component';
import { SharedModule } from "../../../shared/shared.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

describe('HpoCurationComponent', () => {
  let component: PhenotypeCurationComponent;
  let fixture: ComponentFixture<PhenotypeCurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule, NoopAnimationsModule],
      declarations: [PhenotypeCurationComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhenotypeCurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
