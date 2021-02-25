import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PubmedService {

  constructor(private httpClient: HttpClient) { }

  // Find a publication by pubmed id
  findPublication(id: string) {
    let parameters: HttpParams = new HttpParams().set('db', 'pubmed').set('id', id).set('retmode', 'json');
    return this.httpClient.get(environment.PUBMED_SUMMARY_URL, {params: parameters}).pipe(
      map((data) => data['result'][id])
    );
  }
}
