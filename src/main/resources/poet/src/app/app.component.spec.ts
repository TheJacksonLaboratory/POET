import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { AuthConfig, AuthModule, AuthService } from "@auth0/auth0-angular";
import { HttpClient } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('AppComponent', () => {
  beforeEach(async(() => {
    const authConfig: AuthConfig  = {
      domain: "fake",
      clientId: "fake"
    };
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        AuthModule.forRoot(authConfig),
        HttpClientTestingModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [AuthService]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
