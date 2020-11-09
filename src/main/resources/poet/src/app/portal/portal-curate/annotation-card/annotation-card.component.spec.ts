import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnotationCardComponent } from './annotation-card.component';

describe('AnnotationCardComponent', () => {
  let component: AnnotationCardComponent;
  let fixture: ComponentFixture<AnnotationCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnnotationCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnnotationCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
