import { TestBed } from '@angular/core/testing';

import { MonarchServiceService } from './monarch-service.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('MonarchServiceService', () => {
  let service: MonarchServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(MonarchServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
