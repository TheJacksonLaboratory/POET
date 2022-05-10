import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  checkUser(): void {
    this.http.get(environment.POET_API_CHECK_USER_URL).subscribe();
  }

  isRoleAdmin(userRole): boolean {
    return userRole === environment.AUTH0_ADMIN_ROLE;
  }

  isUser(user): boolean {
    if (user == null) {
      return false;
    }
    return user && user.role !== 'GUEST' && Object.keys(user).length !== 0;
  }

  getUserDetails(): Observable<any> {
    return this.http.get(environment.POET_API_USER_URL);
  }

  setOrcid(orcid) {
    const params = new HttpParams().set('orcid', orcid);
    return this.http.post(environment.POET_API_USER_URL, {}, {params});
  }
}
