import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { filter, map, pluck } from "rxjs/operators";
import { MaxoSearchResult, HpoMaxoSearchResult, HpoTerm, AnchorSearchResult } from "../../models/search-models";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HpoService {

  constructor(public httpClient: HttpClient) { }

  /**
   * Search HPO API for hpo terms
   * @param query
   */
  searchHPOTerms(query: string): Observable<HpoTerm[]> {
    let parameters: HttpParams = new HttpParams().set('q', query);
    return this.httpClient.get<any>(environment.HPO_API_HPO_SEARCH_URL, {params: parameters})
      .pipe(pluck('terms'));
  }

  /**
   * Search HPO API for maxo terms
   * @param query
   */
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
        }),
        map( (items: MaxoSearchResult[]) => {
          return items.filter((item: MaxoSearchResult) => !item.name.toLowerCase().includes("obsolete"));
        })
      );
  }

  /**
   * Search HPO API for descendants starting with HPO id.
   * @param query the term you want to search on, no query means return all
   * @param start where to start finding descendants from
   */
  searchDescendants(query: string, start: string): Observable<AnchorSearchResult[]> {
    let parameters: HttpParams = new HttpParams().set('q', query).set('s', start)
    return this.httpClient.get<any>(environment.HPO_API_HPO_SEARCH_DESCENDANTS_URL, {params: parameters});
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
