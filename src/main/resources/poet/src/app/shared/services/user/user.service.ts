import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  checkUser(): void {
    this.http.get(environment.POET_API_CHECK_USER_URL).subscribe();
  }
}
