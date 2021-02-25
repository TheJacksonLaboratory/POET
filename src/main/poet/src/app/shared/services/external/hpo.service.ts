import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { filter, map, pluck } from "rxjs/operators";
import { MaxoSearchResult, HpoMaxoSearchResult, HpoTerm } from "../../models/search-models";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HpoService {

  constructor(public httpClient: HttpClient) { }

  // Seach HPO Terms
  searchHPOTerms(query: string): Observable<HpoTerm[]> {
    let parameters: HttpParams = new HttpParams().set('q', query);
    return this.httpClient.get<any>(environment.HPO_API_HPO_SEARCH_URL, {params: parameters})
      .pipe(pluck('terms'));
  }

  // Search MAXO Terms
  searchMaxoTerms(query: string): Observable<MaxoSearchResult[]> {
    let parameters: HttpParams = new HttpParams().set('q', query);
    return this.httpClient.get<any>(environment.HPO_API_MAXO_SEARCH_URL, {params: parameters})
      .pipe(pluck('terms'),
      map((maxoResults: HpoMaxoSearchResult[]) => {
        return maxoResults.map((maxoResult) => {
          return {
            ...maxoResult.term,
            synonym: maxoResult.synonym,
            synonymMatched: maxoResult.synonymMatched
          };
        });
      }));
  }

  searchDiseases(query: string): any {
    let parameters: HttpParams = new HttpParams().set('q', query);
    return this.httpClient.get<any>(environment.HPO_API_HPO_SEARCH_URL, {params: parameters})
      .pipe(pluck('diseases'),
        map((items: any) => {
          return items.filter((item) => {
           return item.db == "OMIM";
          });
        }));
  }
}
