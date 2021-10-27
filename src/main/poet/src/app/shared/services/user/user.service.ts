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

  isUserAdmin(user): boolean {
    return user?.role === environment.AUTH0_ADMIN_ROLE;
  }

  isRoleAdmin(userRole): boolean {
    return userRole === environment.AUTH0_ADMIN_ROLE;
  }

  isUser(user): boolean  {
    if(user == null){
      return false;
    }
    return user && user.role !== 'GUEST' && Object.keys(user).length !== 0;
  }
}
