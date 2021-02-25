import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnotationCardComponent } from './annotation-card.component';
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { MatSnackBar } from "@angular/material/snack-bar";
import { SharedModule } from "../../../shared/shared.module";
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('AnnotationCardComponent', () => {
  let component: AnnotationCardComponent;
  let fixture: ComponentFixture<AnnotationCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AnnotationCardComponent],
      imports: [HttpClientTestingModule, NoopAnimationsModule, SharedModule],
      providers: [
        {
          provide: MatSnackBar,
          useValue: {}
        },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of()
          }
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnnotationCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
