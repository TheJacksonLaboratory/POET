import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCurationComponent } from './dialog-curation.component';

describe('DialogCurationComponent', () => {
  let component: DialogCurationComponent;
  let fixture: ComponentFixture<DialogCurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogCurationComponent ]
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
