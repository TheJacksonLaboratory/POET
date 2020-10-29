import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonarchSearchComponent } from './monarch-search.component';
import { MonarchServiceService } from "../services/monarch-service.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { SharedModule } from "../shared.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

describe('MonarchSearchComponent', () => {
  let component: MonarchSearchComponent;
  let fixture: ComponentFixture<MonarchSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonarchSearchComponent ],
      providers: [ MonarchServiceService ],
      imports: [ HttpClientTestingModule, SharedModule, BrowserAnimationsModule ]
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
