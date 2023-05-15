import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { PortalCurateComponent } from './portal-curate.component';
import { RouterTestingModule } from "@angular/router/testing";
import { SharedModule } from "../../shared/shared.module";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { AuthConfig, AuthModule } from "@auth0/auth0-angular";
import {PortalDashboardComponent} from "../portal-dashboard/portal-dashboard.component";

describe('PortalCurateComponent', () => {
  let component: PortalCurateComponent;
  let fixture: ComponentFixture<PortalCurateComponent>;
  const authConfig: AuthConfig = {
    domain: "fake",
    clientId: "fake"
  };
  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([
        { path: 'portal/dashboard', component: PortalDashboardComponent }]), SharedModule, HttpClientTestingModule, NoopAnimationsModule,
        AuthModule.forRoot(authConfig)],
      declarations: [PortalCurateComponent]
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

  it('determine function should work', () => {
    expect(component.determineIdType("OMIM:2030203")).toEqual("disease");
    expect(component.determineIdType("PMID:20302043")).toEqual("publication");
    expect(component.determineIdType("")).toEqual(undefined);
    expect(component.determineIdType(null)).toEqual(undefined);
    expect(component.determineIdType(undefined)).toEqual(undefined);
  });

  it('generate url function should work', () => {

    expect(component.getExternalTermIdUrlFromId("OMIM:2030203")).toEqual("https://omim.org/entry/2030203");
    expect(component.getExternalTermIdUrlFromId("MONDO:019193")).toEqual("https://monarchinitiative.org/disease/MONDO:019193");
    expect(component.getExternalTermIdUrlFromId("ORPHA:019193")).toEqual("https://www.orpha.net/consor/cgi-bin/OC_Exp.php?Lng=EN&Expert=019193");
    expect(component.getExternalTermIdUrlFromId("")).toEqual("");
    expect(component.getExternalTermIdUrlFromId(null)).toEqual("");
    expect(component.getExternalTermIdUrlFromId(undefined)).toEqual("");
  });


  it('is term expected prefix', () => {

    expect(component.isTermIdExpected("OMIM:2030203", "OMIM")).toEqual(true);
    expect(component.isTermIdExpected("MONDO:019193", "MONDO")).toEqual(true);
    expect(component.isTermIdExpected("ORPHA:019193", "ORPHA")).toEqual(true);
    expect(component.isTermIdExpected("ORPHA:019193", "MONDO")).toEqual(false);
    expect(component.isTermIdExpected("", "")).toEqual(false);
    expect(component.isTermIdExpected(null, null)).toEqual(false);
    expect(component.isTermIdExpected(undefined, undefined)).toEqual(false);
  });
});
