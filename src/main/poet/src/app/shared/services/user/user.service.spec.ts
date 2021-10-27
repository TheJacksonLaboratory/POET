import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('UserService', () => {
  let service: UserService;
  let fakeUsers;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(UserService);
    fakeUsers = getFakeUsers();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should test elevated curator', () => {
    expect(service.isRoleAdmin("POET_ADMIN")).toBeTrue();
    expect(service.isRoleAdmin("POET_CURATOR")).toBeFalse();
    expect(service.isRoleAdmin("")).toBeFalse();
    expect(service.isRoleAdmin(null)).toBeFalse();
  })

  it('it should test if they are an admin', () => {
    expect(service.isUserAdmin(fakeUsers[0])).toBeTrue();
    expect(service.isUserAdmin(fakeUsers[1])).toBeFalse();
    expect(service.isUserAdmin(fakeUsers[2])).toBeFalse();
    expect(service.isUserAdmin({})).toBeFalse();
    expect(service.isUserAdmin(null)).toBeFalse();
    expect(service.isUserAdmin(undefined)).toBeFalse();
  })

  it('it should test if they are a user', () => {
    expect(service.isUser(fakeUsers[0])).toBeTrue();
    expect(service.isUser(fakeUsers[1])).toBeTrue();
    expect(service.isUser(fakeUsers[2])).toBeFalse();
    expect(service.isUser({})).toBeFalse();
    expect(service.isUser(null)).toBeFalse();
    expect(service.isUser(undefined)).toBeFalse();
  })

  function getFakeUsers(){
    return [
      {
        role: "POET_ADMIN",
        nickname: "Curator1"
      },
      {
        role: "POET_CURATOR",
        nickname: "Curator2"
      },
      {
        role: "GUEST",
        nickname: "guest"
      }
    ]
  }
});
