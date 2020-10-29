import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PortalCurateComponent } from './portal-curate.component';
import {RouterTestingModule} from "@angular/router/testing";
import { SharedModule } from "../../shared/shared.module";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

describe('PortalCurateComponent', () => {
  let component: PortalCurateComponent;
  let fixture: ComponentFixture<PortalCurateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, SharedModule, HttpClientTestingModule, BrowserAnimationsModule],
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

  it('determine function should work', () => {
    expect(component.determineIdType("OMIM:2030203")).toEqual("disease");
    expect(component.determineIdType("PMID:20302043")).toEqual("publication");
    expect(component.determineIdType("")).toEqual(undefined);
    expect(component.determineIdType(null)).toEqual(undefined);
    expect(component.determineIdType(undefined)).toEqual(undefined);
  });
});
