import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { Observable } from "rxjs";
import {Disease, TreatmentAnnotation, Publication, UserActivityResponse} from "../../models/models";
import { StateService } from "../state/state.service";
import { map, shareReplay } from "rxjs/operators";

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
    return this.httpClient.get<any>(environment.POET_API_DISEASE_ENTITY_URL + id);
  }

  /***
   * Get a disease by OMIM id
   * @param diseaseRequest a monarch disease object
   */
  saveDisease(diseaseRequest: any): Observable<any>{
    return this.httpClient.put(environment.POET_API_DISEASE_ENTITY_URL, diseaseRequest);
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
   * Save a publication to a disease.
   */
  savePublication(annotationSource: any) {
    return this.httpClient.post(environment.POET_API_PUBLICATION_ENTITY_URL, annotationSource)
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
  getTreatmentAnnotations(disease: Disease, publication: Publication, sort: string): Observable<TreatmentAnnotation[]>{
    let params;
    if(sort){
      params = new HttpParams().set("sort", sort);
    }
    if(publication != null){
      return this.httpClient.get<any>(environment.POET_API_MAXO_ANNOTATION + `${disease.diseaseId}/${publication.publicationId}`, {params: params});
    } else {
      return this.httpClient.get<any>(environment.POET_API_MAXO_ANNOTATION + disease.diseaseId, {params: params}).pipe(shareReplay());
    }
  }

  /**
   * Update a maxo annotation to the database
   * @param annotation - a maxo annotation from the maxo form
   */
  updateTreatmentAnnotation(annotation: any) {
    const annotationSource = this.stateService.getSelectedSource();
    annotation.publicationId = annotationSource.publication.publicationId;
    annotation.publicationName = annotationSource.publication.publicationName;
    annotation.diseaseId = annotationSource.disease.diseaseId;
    annotation.diseaseName = annotationSource.disease.diseaseName;
    return this.httpClient.put(environment.POET_API_MAXO_ANNOTATION, annotation);
  }

  /**
   * Save a maxo annotation to the database
   * @param annotation - a maxo annotation from the maxo form
   */
  saveTreatmentAnnotation(annotation: any) {
    const annotationSource = this.stateService.getSelectedSource();
    annotation.publicationId = annotationSource.publication.publicationId;
    annotation.publicationName = annotationSource.publication.publicationName;
    annotation.diseaseId = annotationSource.disease.diseaseId;
    annotation.diseaseName = annotationSource.disease.diseaseName;
    return this.httpClient.post(environment.POET_API_MAXO_ANNOTATION, annotation);
  }

  /**
   * Save a maxo annotation to the database
   * @param id - an annotation id
   */
  deleteTreatmentAnnotation(id: string) {
    return this.httpClient.delete(environment.POET_API_MAXO_ANNOTATION + id);
  }

  /**
   * Get User Activity for curations
   * @param everyone
   */
  getUserActivity(everyone: boolean){
    const params = new HttpParams().set("all", String(everyone));
    return this.httpClient.get<UserActivityResponse[]>(environment.POET_API_STATISTICS_USERACTIVITY_URL, {params: params}).pipe(
      map((response: UserActivityResponse[]) => {
        return response.map((activity: UserActivityResponse) => {
          let newData = {
            source: null,
            category: null,
            curationAction: null,
            curator: null,
            date: null,
            time: null,
            annotationId: null
          };
          newData.source = activity.annotation["annotationSource"];
          newData.category = activity.annotation["annotationType"].toUpperCase();
          newData.curationAction = activity.curationAction;
          newData.curator = activity.user["nickname"];
          newData.date = new Date(activity.localDateTime).toLocaleDateString();
          newData.time = new Date(activity.localDateTime).toLocaleTimeString();
          newData.annotationId = activity.annotation.id;
          return newData;
        })
      }));
  }

  /**
   * Get User Activity for curations
   * @param everyone
   */
  getUserContributions(){
    return this.httpClient.get(environment.POET_API_STATISTICS_CONTRIBUTION_URL).pipe(
      map((contributions) => {
        return [{"value": contributions["maxo"], "name": "Medical Action Ontology"},
          {"value": contributions["hpo"], "name": "Human Phenotype Ontology"},
          {"value": contributions["phenopackets"], "name": "PhenoPackets"}];
      })
    );
  }

  /**
   * Get Annotation Counts by disease id or all if none
   */
  getAnnotationCounts(diseaseId: string): any{
    return this.httpClient.get(environment.POET_API_STATISTICS_ANNOTATION_URL + diseaseId);
  }
}
