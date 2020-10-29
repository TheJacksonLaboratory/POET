import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PortalComponent } from './portal.component';
import { AuthConfig, AuthModule } from "@auth0/auth0-angular";
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('PortalComponent', () => {
  let component: PortalComponent;
  let fixture: ComponentFixture<PortalComponent>;
  const authConfig: AuthConfig  = {
    domain: "fake",
    clientId: "fake"
  };
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AuthModule.forRoot(authConfig)],
      declarations: [ PortalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PortalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
