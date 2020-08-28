import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CurationService {

  constructor(private httpClient: HttpClient) { }

  // A search for articles or diseases
  searchAll(query: string){
    let parameters: HttpParams = new HttpParams();
    parameters.set('query', query);
    return this.httpClient.get(environment.POET_API_SEARCH_URL, {params: parameters});
  }
}
