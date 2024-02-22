import { TestBed } from '@angular/core/testing';

import { OntologyService } from './ontology.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('HpoService', () => {
  let service: OntologyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ]
    });
    service = TestBed.inject(OntologyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
