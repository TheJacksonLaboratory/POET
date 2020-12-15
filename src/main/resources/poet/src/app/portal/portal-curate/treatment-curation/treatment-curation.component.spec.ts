import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TreatmentCurationComponent } from './treatment-curation.component';
import { HpoService } from "../../../shared/services/external/hpo.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { SharedModule } from "../../../shared/shared.module";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

describe('MaxoCurationComponent', () => {
  let component: TreatmentCurationComponent;
  let fixture: ComponentFixture<TreatmentCurationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TreatmentCurationComponent ],
      providers: [ HpoService ],
      imports: [ HttpClientTestingModule, SharedModule, NoopAnimationsModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TreatmentCurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('test maxo display fn', () => {
    let testData = [{
      name: "test",
      ontologyId: "123"
    },{
      name: "",
      ontologyId: "123"
    }, null]
    expect(component.displayMaxoFn(testData[0])).toEqual("test 123");
    expect(component.displayMaxoFn(testData[1])).toEqual("");
    expect(component.displayMaxoFn(testData[2])).toEqual("");
  });

  it('test hpo display fn', () =>{
    let testData = [{
      name: "test",
      id: "123"
    },{
      name: "",
      id: "123"
    }, null]
    expect(component.displayHpoFn(testData[0])).toEqual("test 123");
    expect(component.displayHpoFn(testData[1])).toEqual("");
    expect(component.displayHpoFn(testData[2])).toEqual("");
  });
});
