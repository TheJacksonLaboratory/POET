import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectionCardComponent } from './selection-card.component';
import { CurationService } from "../../../shared/services/curation.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { SharedModule } from "../../../shared/shared.module";

describe('SelectionCardComponent', () => {
  let component: SelectionCardComponent;
  let fixture: ComponentFixture<SelectionCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectionCardComponent ],
      providers: [ CurationService ],
      imports: [ HttpClientTestingModule, SharedModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectionCardComponent);
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
