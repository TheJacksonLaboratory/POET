import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonarchSearchComponent } from './monarch-search.component';
import { MonarchService } from "../services/external/monarch.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { SharedModule } from "../shared.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

describe('MonarchSearchComponent', () => {
  let component: MonarchSearchComponent;
  let fixture: ComponentFixture<MonarchSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonarchSearchComponent ],
      providers: [ MonarchService ],
      imports: [ HttpClientTestingModule, SharedModule, NoopAnimationsModule ]
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
