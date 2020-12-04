import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SourceCardComponent } from './source-card.component';
import { CurationService } from "../../../shared/services/curation/curation.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { SharedModule } from "../../../shared/shared.module";

describe('SelectionCardComponent', () => {
  let component: SourceCardComponent;
  let fixture: ComponentFixture<SourceCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SourceCardComponent ],
      providers: [ CurationService ],
      imports: [ HttpClientTestingModule, SharedModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SourceCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should know active type',() => {
    component.activeType = 'disease';
    expect(component.isActiveTypeDisease()).toBeTrue();
  })

  it('it should know opposite active type',() => {
    component.activeType = 'disease';
    expect(component.oppositeActiveType()).toEqual('publication');
    component.activeType = 'publication';
    expect(component.oppositeActiveType()).toEqual('disease');
  })
});
