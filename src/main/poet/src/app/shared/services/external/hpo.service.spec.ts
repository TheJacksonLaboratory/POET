import { TestBed } from '@angular/core/testing';

import { HpoService } from './hpo.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('HpoService', () => {
  let service: HpoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ]
    });
    service = TestBed.inject(HpoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
