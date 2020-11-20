import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PortalCurateComponent } from './portal-curate.component';

describe('PortalCurateComponent', () => {
  let component: PortalCurateComponent;
  let fixture: ComponentFixture<PortalCurateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PortalCurateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PortalCurateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
