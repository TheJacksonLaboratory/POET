import { TestBed } from '@angular/core/testing';

import { PubmedService } from './pubmed.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('PubmedService', () => {
  let service: PubmedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(PubmedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
