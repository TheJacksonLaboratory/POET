import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PortalDashboardComponent } from './portal-dashboard.component';
import {AuthConfig, AuthModule} from "@auth0/auth0-angular";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {Router} from "@angular/router";
import {RouterTestingModule} from "@angular/router/testing";

describe('PortalDashboardComponent', () => {
  let component: PortalDashboardComponent;
  let fixture: ComponentFixture<PortalDashboardComponent>;
  const authConfig: AuthConfig  = {
    domain: "fake",
    clientId: "fake"
  };
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PortalDashboardComponent ],
      imports: [AuthModule.forRoot(authConfig), RouterTestingModule, HttpClientTestingModule]
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
      "series": [
          {"name": "November 1", "value": 2},
          {"name": "November 3", "value": 1},
          {"name": "November 4", "value": 3}
        ]
    }];
    expect(component.graphUserActivity(testData)).toEqual(expectedData);
  })
});
