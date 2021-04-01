import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { filter, pluck } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class MonarchService {

  constructor(private httpClient: HttpClient) {
  }

  // Get the disease from monarch
  getDisease(query: string) {
    return this.httpClient.get(environment.MONARCH_ENTITY_URL + query);
  }

  /**
   * Searches BIOLINK API with encoded prefix disease.
   * @param query
   * @param prefix
   */
  searchMonarch(query: string, prefix: string) {
    let parameters = new HttpParams().append("prefix", prefix);
    return this.httpClient.get(environment.MONARCH_SEARCH_URL + query, {params: parameters}).pipe(
      pluck("docs")
    );
  }
}
