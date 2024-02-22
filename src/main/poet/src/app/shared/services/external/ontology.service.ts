import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { map, pluck, tap} from "rxjs/operators";
import { MaxoSearchResult, HpoMaxoSearchResult, HpoTerm, AnchorSearchResult } from "../../models/search-models";
import { BehaviorSubject, Observable, of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OntologyService {

  private hpoTermDefinitionSubject: BehaviorSubject<any> = new BehaviorSubject<any>('');

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
   * Search hpo for term definition
   */
  hpoTermDefinition(hpoId: string): Observable<any> {
    const lastTerm = this.hpoTermDefinitionSubject.getValue();
    if(lastTerm?.details?.id !== hpoId){
      return this.httpClient.get<any>(environment.HPO_API_TERM_URL + hpoId).pipe(
        tap(term => this.hpoTermDefinitionSubject.next(term)), map(term => term.details.definition));
    } else {
      return of(lastTerm.details.definition);
    }
  }

  /**
   * Search HPO API for maxo terms
   * @param query
   */
  searchMaxoTerms(query: string): Observable<MaxoSearchResult[]> {
    let parameters: HttpParams = new HttpParams().set('q', query);
    return this.httpClient.get<any>(environment.HPO_API_MAXO_SEARCH_URL, {params: parameters})
      .pipe(pluck('terms'));
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
}
