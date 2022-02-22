import { TestBed } from '@angular/core/testing';

import { StateService } from './state.service';
import { CurationService } from "../curation/curation.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('StateService', () => {
  let service: StateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CurationService]
    });
    service = TestBed.inject(StateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
