import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { BehaviorSubject, Observable } from "rxjs";
import { AnnotationSource, Disease, MaxoAnnotation, Publication } from "../../models/models";
import { StateService } from "../state/state.service";

@Injectable({
  providedIn: 'root'
})
export class CurationService {


  constructor(private httpClient: HttpClient, private stateService: StateService) { }

  /**
   * Search for publications and diseases by a query.
   * @param query
   */
  searchAll(query: string){
    let parameters: HttpParams = new HttpParams().set('query', query);
    return this.httpClient.get(environment.POET_API_SEARCH_URL, {params: parameters});
  }

  /***
   * Get a disease by OMIM id
   * @param id
   */
  getDisease(id: string): Observable<Disease>{
    return this.httpClient.get<Disease>(environment.POET_API_DISEASE_ENTITY_URL + id);
  }

  /**
   * Get a list of publications associated with a disease OMIM
   * @param id
   */
  getDiseasePublications(id: string): Observable<Publication[]>{
    return this.httpClient.get<Publication[]>(environment.POET_API_DISEASE_ENTITY_URL + id + "/publications");
  }

  /**
   * Get a publication by a pubmed id.
   * @param id
   */
  getPublication(id: string): Observable<Publication>{
    return this.httpClient.get<Publication>(environment.POET_API_PUBLICATION_ENTITY_URL + id);
  }

  /**
   * Get a list of diseases associated to a publication
   * @param id - ideally a PMID.
   */
  getPublicationDiseases(id: string): Observable<Disease[]>{
    return this.httpClient.get<Disease[]>(environment.POET_API_PUBLICATION_ENTITY_URL + id + "/diseases");
  }

  /**
   * Get a list of Maxo Annotations
   * @param publication
   * @param disease
   * @param ontology
   * @param sort
   */
  getMaxoAnnotations(disease: Disease, publication: Publication, sort: string): Observable<MaxoAnnotation[]>{
    const params = new HttpParams().set("sort", sort);
    if(publication != null){
      return this.httpClient.get<any>(environment.POET_API_MAXO_ANNOTATION + `${disease.diseaseId}/${publication.publicationId}`, {params: params});
    } else {
      return this.httpClient.get<any>(environment.POET_API_MAXO_ANNOTATION + disease.diseaseId, {params: params});
    }
  }

  /**
   * Save a maxo annotation to the database
   * @param annotation - a maxo annotation from the maxo form
   */
  saveMaxoAnnotation(annotation: any) {
    const annotationSource = this.stateService.getSelectedSource();
    annotation.publicationId = annotationSource.publication.publicationId;
    annotation.publicationName = annotationSource.publication.publicationName;
    annotation.diseaseId = annotationSource.disease.diseaseId;
    annotation.diseaseName = annotationSource.disease.diseaseName;
    return this.httpClient.post(environment.POET_API_MAXO_ANNOTATION, annotation);
  }
}
