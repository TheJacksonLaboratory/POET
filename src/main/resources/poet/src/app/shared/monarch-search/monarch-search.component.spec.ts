import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonarchSearchComponent } from './monarch-search.component';

describe('MonarchSearchComponent', () => {
  let component: MonarchSearchComponent;
  let fixture: ComponentFixture<MonarchSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonarchSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonarchSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
