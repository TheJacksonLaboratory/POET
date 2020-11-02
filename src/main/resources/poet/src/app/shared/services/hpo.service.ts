import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class HpoService {

  constructor(public httpClient: HttpClient) { }


  // Seach HPO Terms
  searchHPOTerms(query: string){
    let parameters: HttpParams = new HttpParams().set('q', query);
    return this.httpClient.get(environment.HPO_API_HPO_SEARCH_URL, {params: parameters});
  }

  // Search MAXO Terms
  searchMaxoTerms(query: string){
    let parameters: HttpParams = new HttpParams().set('q', query);
    return this.httpClient.get(environment.HPO_API_MAXO_SEARCH_URL, {params: parameters});
  }
}
