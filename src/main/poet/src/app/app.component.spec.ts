import { TestBed, waitForAsync } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { AuthConfig, AuthModule, AuthService } from "@auth0/auth0-angular";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { SharedModule } from "./shared/shared.module";

describe('AppComponent', () => {
  beforeEach(waitForAsync(() => {
    const authConfig: AuthConfig  = {
      domain: "fake",
      clientId: "fake"
    };
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        AuthModule.forRoot(authConfig),
        HttpClientTestingModule,
        NoopAnimationsModule,
        SharedModule
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
