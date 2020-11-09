import { TestBed } from '@angular/core/testing';
import { MonarchService } from './monarch.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('MonarchServiceService', () => {
  let service: MonarchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(MonarchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
