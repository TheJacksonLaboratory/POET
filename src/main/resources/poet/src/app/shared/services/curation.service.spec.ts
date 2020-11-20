import { TestBed } from '@angular/core/testing';

import { CurationService } from './curation.service';

describe('CurationService', () => {
  let service: CurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
