import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PortalDashboardComponent } from './portal-dashboard.component';

describe('PortalDashboardComponent', () => {
  let component: PortalDashboardComponent;
  let fixture: ComponentFixture<PortalDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PortalDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PortalDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should group by activity date', () => {
    let testData = [
      {"date": "November 1"},
      {"date": "November 1"},
      {"date": "November 3"},
      {"date": "November 4"},
      {"date": "November 4"},
      {"date": "November 4"}
    ];
    let expectedData = [{
      "name": "Annotations",
      "series": [{"name": "November 1", "value": 2},
        {"name": "November 3", "value": 1},
        {"name": "November 1", "value": 3}]}];
    expect(component.graphUserActivity(testData)).toEqual(expectedData);
  })
});
