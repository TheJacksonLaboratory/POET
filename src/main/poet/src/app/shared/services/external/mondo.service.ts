import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { map, pluck} from "rxjs/operators";
import {Observable} from "rxjs";
import {MondoSearchResult} from "../../models/search-models";

@Injectable({
  providedIn: 'root'
})
export class MondoService {

  constructor(private httpClient: HttpClient) {
  }

  /**
   *
   * @param query
   */
  getDisease(query: string) {
    return this.httpClient.get(environment.MONDO_ENTITY_URL + query);
  }

  /**
   * Searches BIOLINK API with encoded prefix disease.
   * @param query
   * @param prefix
   */
  searchMondo(query: string, prefix: string): Observable<MondoSearchResult[]> {
    let parameters = new HttpParams().append("q", query);
    return this.httpClient.get(environment.MONDO_SEARCH_URL, {params: parameters}).pipe(
      pluck("terms"), map(
        (responses: any[]) => {
          return responses.map(response => {
            let mappedResponse: MondoSearchResult = {id: "", name: "", leaf: false, match: "", omim_id: ""};
            mappedResponse.name = response.name;
            mappedResponse.id = response.id;
            mappedResponse.omim_id = "";
            mappedResponse.leaf = false;
            mappedResponse.match = response.match;

            // Filter the disease results
            if(!mappedResponse.id.includes("OMIM")){
              const omim_index = response.xrefs.findIndex(element => element.includes("OMIM:"))
              if(omim_index != -1){
                mappedResponse.leaf = true;
                mappedResponse.omim_id = response.xrefs[omim_index];
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
