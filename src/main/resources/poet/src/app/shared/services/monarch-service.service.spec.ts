import { TestBed } from '@angular/core/testing';

import { MonarchServiceService } from './monarch-service.service';

describe('MonarchServiceService', () => {
  let service: MonarchServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonarchServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
