import { TestBed } from '@angular/core/testing';
import { MondoService } from './mondo.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('MonarchServiceService', () => {
  let service: MondoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(MondoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
