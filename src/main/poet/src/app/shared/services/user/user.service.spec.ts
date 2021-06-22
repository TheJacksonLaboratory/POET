import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should test elevated curator', () => {
    expect(service.isElevatedCurator("ELEVATED_CURATOR")).toBeTrue();
    expect(service.isElevatedCurator("CURATOR")).toBeFalse();
    expect(service.isElevatedCurator("")).toBeFalse();
    expect(service.isElevatedCurator(null)).toBeFalse();
  })
});
