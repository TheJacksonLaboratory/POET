import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../environments/environment";
import { Observable } from "rxjs";
import { Disease, Publication } from "../models/models";

@Injectable({
  providedIn: 'root'
})
export class CurationService {

  constructor(private httpClient: HttpClient) { }

  // A search for articles or diseases
  searchAll(query: string){
    let parameters: HttpParams = new HttpParams().set('query', query);
    return this.httpClient.get(environment.POET_API_SEARCH_URL, {params: parameters});
  }

  getDisease(id: string): Observable<Disease>{
    return this.httpClient.get<Disease>(environment.POET_API_DISEASE_ENTITY_URL + id);
  }

  getDiseasePublications(id: string): Observable<Publication[]>{
    return this.httpClient.get<Publication[]>(environment.POET_API_DISEASE_ENTITY_URL + id + "/publications");
  }

  getPublication(id: string): Observable<Publication>{
    return this.httpClient.get<Publication>(environment.POET_API_PUBLICATION_ENTITY_URL + id);
  }

  getPublicationDiseases(id: string): Observable<Disease[]>{
    return this.httpClient.get<Disease[]>(environment.POET_API_PUBLICATION_ENTITY_URL + id + "/diseases");
  }


}
