import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MaxoCurationComponent } from './maxo-curation.component';

describe('MaxoCurationComponent', () => {
  let component: MaxoCurationComponent;
  let fixture: ComponentFixture<MaxoCurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MaxoCurationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MaxoCurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
