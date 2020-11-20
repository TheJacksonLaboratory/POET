import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HpoCurationComponent } from './hpo-curation.component';

describe('HpoCurationComponent', () => {
  let component: HpoCurationComponent;
  let fixture: ComponentFixture<HpoCurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HpoCurationComponent ]
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
