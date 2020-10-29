import { TestBed } from '@angular/core/testing';

import { CurationService } from './curation.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('CurationService', () => {
  let service: CurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(CurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
