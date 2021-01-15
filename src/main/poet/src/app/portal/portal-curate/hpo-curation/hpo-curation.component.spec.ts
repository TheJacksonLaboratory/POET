import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HpoCurationComponent } from './hpo-curation.component';
import { SharedModule } from "../../../shared/shared.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

describe('HpoCurationComponent', () => {
  let component: HpoCurationComponent;
  let fixture: ComponentFixture<HpoCurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule, NoopAnimationsModule],
      declarations: [HpoCurationComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HpoCurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
