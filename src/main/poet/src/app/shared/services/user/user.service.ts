import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { User } from "../../models/models";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  checkUser(): void {
    this.http.get(environment.POET_API_CHECK_USER_URL).subscribe();
  }

  isElevatedCurator(userRole: string): boolean {
    return userRole === 'ELEVATED_CURATOR';
  }

}
