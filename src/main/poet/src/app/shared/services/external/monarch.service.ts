import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import {filter, map, pluck} from "rxjs/operators";
import {Observable} from "rxjs";
import {MonarchSearchResult} from "../../models/search-models";

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
  searchMonarch(query: string, prefix: string): Observable<MonarchSearchResult[]> {
    let parameters = new HttpParams().append("prefix", prefix);
    if(prefix === "OMIM"){
      parameters = parameters.append("include_eqs", "true").append("exclude_groups", "true");
    }
    return this.httpClient.get(environment.MONARCH_SEARCH_URL + query, {params: parameters}).pipe(
      pluck("docs"), map(
        (responses: any[]) => {
          return responses.map(response => {
            let mappedResponse: MonarchSearchResult = {id: "", label: "", leaf: false, match: "", omim_id: ""};
            mappedResponse.label = response.label[0];
            mappedResponse.id = response.id;
            mappedResponse.omim_id = "";
            mappedResponse.leaf = false;
            mappedResponse.match = response.match;
            if(!mappedResponse.id.includes("OMIM")){
              const omim_index = response.equivalent_ids.findIndex(element => element.includes("OMIM:"))
              if(omim_index != -1){
                mappedResponse.leaf = true;
                mappedResponse.omim_id = response.equivalent_ids[omim_index];
                return mappedResponse;
              }
            } else {
              return mappedResponse;
            }
          }).filter(this.notEmpty);
        }
      ));
  }

   notEmpty<TValue>(value: TValue | null | undefined): value is TValue {
    return value !== null && value !== undefined;
  }

}
