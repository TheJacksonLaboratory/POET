import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MonarchService {

  constructor(private httpClient: HttpClient) {
  }

  // A search for articles or diseases
  searchDiseases(query: string) {
    let parameters: HttpParams = new HttpParams().set('prefix', 'MONDO');
    return this.httpClient.get(environment.MONARCH_SEARCH_URL + query, {params: parameters});
  }
}
