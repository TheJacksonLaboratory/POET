import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { Observable } from "rxjs";
import {
  Disease,
  PhenotypeAnnotation,
  Publication, Status,
  TreatmentAnnotation,
  UserActivityResponse
} from "../../models/models";
import { StateService } from "../state/state.service";
import { map, shareReplay } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CurationService {


  constructor(private httpClient: HttpClient, private stateService: StateService) {
  }

  /**
   * Search for publications and diseases by a query.
   * @param query
   */
  searchAll(query: string) {
    let parameters: HttpParams = new HttpParams().set('query', query);
    return this.httpClient.get(environment.POET_API_SEARCH_URL, {params: parameters});
  }

  /***
   * Get a disease by OMIM id
   * @param id
   */
  getDisease(id: string): Observable<Disease> {
    return this.httpClient.get<any>(environment.POET_API_DISEASE_ENTITY_URL + id);
  }

  /***
   * Get a disease by OMIM id
   * @param diseaseRequest a monarch disease object
   */
  saveDisease(diseaseRequest: any): Observable<any> {
    return this.httpClient.put(environment.POET_API_DISEASE_ENTITY_URL, diseaseRequest);
  }

  /**
   * Get a list of publications associated with a disease OMIM
   * @param id
   */
  getDiseasePublications(id: string): Observable<Publication[]> {
    return this.httpClient.get<Publication[]>(environment.POET_API_DISEASE_ENTITY_URL + id + "/publications");
  }

  /**
   * Get a publication by a pubmed id.
   * @param id
   */
  getPublication(id: string): Observable<Publication> {
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
  getPublicationDiseases(id: string): Observable<Disease[]> {
    return this.httpClient.get<Disease[]>(environment.POET_API_PUBLICATION_ENTITY_URL + id + "/diseases");
  }

  /**
   * Get a list of Phenotype Annotations
   * @param publication
   * @param disease
   * @param sort
   * @param review
   */
  getPhenotypeAnnotations(disease: Disease, publication: Publication, sort: string, review: boolean): Observable<PhenotypeAnnotation[]> {
    let params;

    if(review) {
      return this.httpClient.get<any>(environment.POET_API_PHENOTYPES_ANNOTATION + 'review');
    } else {
      if (sort) {
        params = new HttpParams().set("sort", sort);
      }
      if (publication != null) {
        return this.httpClient.get<any>(environment.POET_API_PHENOTYPES_ANNOTATION + `${disease.diseaseId}/${publication.publicationId}`, {params: params});
      } else {
        return this.httpClient.get<any>(environment.POET_API_PHENOTYPES_ANNOTATION + disease.diseaseId, {params: params}).pipe(shareReplay());
      }
    }
  }

  getAnnotationsNeedingReview(): Observable<any> {
    return this.httpClient.get(environment.POET_API_STATISTICS_ANNOTATION_URL + 'review');
  }


  /**
   * Get a list of Treatment Annotations
   * @param publication
   * @param disease
   * @param publication
   * @param sort
   * @param review
   */
  getTreatmentAnnotations(disease: Disease, publication: Publication, sort: string, review: boolean): Observable<TreatmentAnnotation[]> {
    let params;

    if(review){
      return this.httpClient.get<any>(environment.POET_API_TREATMENTS_ANNOTATION + 'review');
    } else {
      if (sort) {
        params = new HttpParams().set("sort", sort);
      }

      if (publication != null) {
        return this.httpClient.get<any>(environment.POET_API_TREATMENTS_ANNOTATION + `${disease.diseaseId}/${publication.publicationId}`, {params: params});
      } else {
        return this.httpClient.get<any>(environment.POET_API_TREATMENTS_ANNOTATION + disease.diseaseId, {params: params}).pipe(shareReplay());
      }
    }
  }

  /**
   * Update an annotation to the database
   * @param annotation - an phenotype or treatment annotation
   * @param category - the selected category they are curating
   * @param review - whether this update is a review or not.
   */
  updateAnnotation(annotation: any, category: string, review: string) {
    const annotationSource = this.stateService.getSelectedSource();
    annotation.publicationId = annotationSource.publication.publicationId;
    annotation.publicationName = annotationSource.publication.publicationName;
    annotation.diseaseId = annotationSource.disease.diseaseId;
    annotation.diseaseName = annotationSource.disease.diseaseName;
    const params = new HttpParams().set("review", review);
    if(category === 'treatment'){
      return this.httpClient.put(environment.POET_API_TREATMENTS_ANNOTATION, annotation, {params: params});
    } else {
      return this.httpClient.put(environment.POET_API_PHENOTYPES_ANNOTATION, annotation, {params: params});
    }
  }

  /**
   * Save an annotation to the database
   * @param annotation - a phenotype or treatment annotation
   * @param category - the category we are curating
   */
  saveAnnotation(annotation: any, category: string) {
    const annotationSource = this.stateService.getSelectedSource();
    annotation.publicationId = annotationSource.publication.publicationId;
    annotation.publicationName = annotationSource.publication.publicationName;
    annotation.diseaseId = annotationSource.disease.diseaseId;
    annotation.diseaseName = annotationSource.disease.diseaseName;
    if(category ==='treatment'){
      return this.httpClient.post(environment.POET_API_TREATMENTS_ANNOTATION, annotation);
    } else {
      return this.httpClient.post(environment.POET_API_PHENOTYPES_ANNOTATION, annotation);
    }

  }

  /**
   * Save a maxo annotation to the database
   * @param id - an annotation id
   * @param category - the selected category
   */
  deleteAnnotation(id: string, category: string) {
    if(category === 'treatment'){
      return this.httpClient.delete(environment.POET_API_TREATMENTS_ANNOTATION + id);
    } else {
      return this.httpClient.delete(environment.POET_API_PHENOTYPES_ANNOTATION + id);
    }

  }

  /**
   * Get User Activity for curations
   * @param everyone
   */
  getUserActivity(everyone: boolean) {
    const params = new HttpParams().set("all", String(everyone));
    return this.httpClient.get<UserActivityResponse[]>(environment.POET_API_STATISTICS_ACTIVITY_URL, {params: params}).pipe(
      map((response: UserActivityResponse[]) => {
        return response.map((activity: UserActivityResponse) => {
          let newData = {
            source: null,
            category: null,
            curationAction: "",
            curator: null,
            date: null,
            time: null,
            annotationId: null
          };
          newData.source = activity.annotation["annotationSource"];
          newData.category = activity.annotation["annotationType"].toUpperCase();
          newData.curationAction = activity.curationAction;
          newData.curator = activity.owner["nickname"];
          newData.date = new Date(activity.localDateTime).toLocaleDateString();
          newData.time = new Date(activity.localDateTime).toLocaleTimeString();
          newData.annotationId = activity.annotation.id;
          return newData;
        })
      }));
  }

  /**
   * Get User Activity for curations
   * @param everyone - a boolean if we should get everyone or the current user
   * @param weeksBack - how many weeks of activity
   */
  getGroupActivityFeed(everyone: boolean, weeksBack: number) {
    const params = new HttpParams().set("all", String(everyone)).set("weeks", String(weeksBack));
    return this.httpClient.get<UserActivityResponse[]>(environment.POET_API_STATISTICS_ACTIVITY_URL, {params: params}).pipe(
      map((activityMap) => this.reduceToGroupedActivity(activityMap))
    );
  }

  /**
   * A function to reduce a list of activity records to a simplified grouping of activity.
   * Activity older than a day gets grouped by day, disease
   * Activity older than an hour gets grouped by day, disease, user, type
   * Activity under an hour gets grouped by day, disease, user, type, action
   * @param activityList
   */
  reduceToGroupedActivity(activityList) {
    let activities = [];
    let that = this;
    let dayMap = activityList.reduce(function (r, a) {
      let date = new Date(a.localDateTime);
      const dayDifference = that.daysFromNow(date);
      r[dayDifference] = r[dayDifference] || [];
      r[dayDifference].push(a);
      return r;
    }, Object.create(null));

    Object.keys(dayMap).forEach(daysFromNow => {
      dayMap[daysFromNow] = dayMap[daysFromNow].reduce((r, a) => {
        let disease = a.annotation.annotationSource.disease.diseaseId + "~" +
          a.annotation.annotationSource.disease.diseaseName;
        r[disease] = r[disease] || [];
        r[disease].push(a);
        return r;
      }, Object.create(null));

      Object.keys(dayMap[daysFromNow]).forEach(diseaseJoined => {
        const [diseaseId, diseaseName] = diseaseJoined.split("~");
        if (parseInt(daysFromNow) == 0) {
          // Today
          dayMap[daysFromNow][diseaseJoined] = dayMap[daysFromNow][diseaseJoined].reduce((r, a) => {
            r[a.owner.nickname] = r[a.owner.nickname] || [];
            r[a.owner.nickname].push(a);
            return r;
          }, Object.create(null));

          Object.keys(dayMap[daysFromNow][diseaseJoined]).forEach(user => {
            dayMap[daysFromNow][diseaseJoined][user] = dayMap[daysFromNow][diseaseJoined][user].reduce((r, a) => {
              r[a.annotation.annotationType] = r[a.annotation.annotationType] || [];
              r[a.annotation.annotationType].push(a);
              return r;
            }, Object.create(null));

            Object.keys(dayMap[daysFromNow][diseaseJoined][user]).forEach(type => {
              // Group by hours from now
              dayMap[daysFromNow][diseaseJoined][user][type] = dayMap[daysFromNow][diseaseJoined][user][type].reduce((r, a) => {
                // Group by hours from now
                let date = new Date(a.localDateTime);
                const hourDifference = that.hoursFromNow(date);
                r[hourDifference] = r[hourDifference] || [];
                r[hourDifference].push(a);
                return r;
              }, Object.create(null));

              Object.keys(dayMap[daysFromNow][diseaseJoined][user][type]).forEach(hour => {
                const hoursFromNow = parseInt(hour);
                if (hoursFromNow == 0) {
                  dayMap[daysFromNow][diseaseJoined][user][type][hour] = dayMap[daysFromNow][diseaseJoined][user][type][hour].reduce((r, a) => {
                    r[a.curationAction] = r[a.curationAction] || [];
                    r[a.curationAction].push(a);
                    return r;
                  }, Object.create(null));

                  Object.keys(dayMap[daysFromNow][diseaseJoined][user][type][hour]).forEach(action => {
                    const count = dayMap[daysFromNow][diseaseJoined][user][type][hour][action].length;
                    const mostRecent = this.getMostRecentDate(dayMap[daysFromNow][diseaseJoined][user][type][hour][action]);
                    const minutesFromNow = this.minutesFromNow(mostRecent);
                    const actionFriendly = this.actionLookup(action);
                    const annotationGrammar = this.annotationGrammar(count);
                    let view;
                    if (minutesFromNow == 0 || minutesFromNow == 1) {
                      view = `${user} ${actionFriendly} ${count} ${type} ${annotationGrammar} for ${diseaseName} just now.`;
                    } else {
                      view = `${user} ${actionFriendly} ${count} ${type} ${annotationGrammar} for ${diseaseName} ${minutesFromNow} minutes ago.`;
                    }
                    activities.push({
                      "view": view,
                      "diseaseId": diseaseId,
                      "date": mostRecent,
                    });
                  });

                } else if (hoursFromNow > 0) {
                  const count = dayMap[daysFromNow][diseaseJoined][user][type][hoursFromNow].length;
                  const mostRecent = this.getMostRecentDate(dayMap[daysFromNow][diseaseJoined][user][type][hoursFromNow]);
                  const annotationGrammar = this.annotationGrammar(count);
                  let view;
                  if (hoursFromNow == 1) {
                    view = `${user} modified ${count} ${type} ${annotationGrammar} for ${diseaseName} an hour ago.`;
                  } else {
                    view = `${user} modified ${count} ${type} ${annotationGrammar} for ${diseaseName} ${hoursFromNow} hours ago.`;
                  }
                  activities.push({
                    "view": view,
                    "diseaseId": diseaseId,
                    "date": mostRecent,
                  });
                }
              });

            });
          });

        } else if (parseInt(daysFromNow) > 0) {
          // Days Ago Up to a week
          const mostRecent = this.getMostRecentDate(dayMap[daysFromNow][diseaseJoined]);
          const count = dayMap[daysFromNow][diseaseJoined].length;
          const annotationGrammar = this.annotationGrammar(count);
          const userList = [...new Set(dayMap[daysFromNow][diseaseJoined].map(item => {
            return item.owner.nickname;
          }))].join(",");
          let view;
          if (parseInt(daysFromNow) == 1) {
            view = `${userList} modified ${count} ${annotationGrammar} for ${diseaseName} yesterday.`;
          } else {
            view = `${userList} modified ${count} ${annotationGrammar} for ${diseaseName} ${daysFromNow} days ago.`;
          }
          activities.push({
            "view": view,
            "diseaseId": diseaseId,
            "date": mostRecent,
          });
        }
      });
    });
    return activities.sort((a: any, b: any) => (b.date - a.date));
  }

  private getMostRecentDate(annotations) {
    let dates = annotations.map(a => {
      return new Date(a.localDateTime);
    });
    return new Date(Math.max(...dates));
  }

  private annotationGrammar(count) {
    return count > 1 ? "annotations" : "annotation";
  }

  private daysFromNow(dateToCheck: Date): number {
    const today = new Date();
    return Math.round((today.getTime() - dateToCheck.getTime()) / (24 * 3600 * 1000));
  }

  private hoursFromNow(dateToCheck): number {
    const now = new Date();
    return Math.round((now.getTime() - dateToCheck.getTime()) / (1000 * 60 * 60))
  }

  private minutesFromNow(dateToCheck): number {
    const now = new Date();
    return Math.round((now.getTime() - dateToCheck.getTime()) / (1000 * 60))
  }

  private actionLookup(action): string {
    const actionMap = {
      "CREATE": "created",
      "UPDATE": "updated",
      "DELETE": "deleted",
      "REVIEW": "reviewed"
    }
    return actionMap[action];
  }

  /**
   * Get User Activity for curations
   * @param everyone
   */
  getUserContributions() {
    return this.httpClient.get(environment.POET_API_STATISTICS_CONTRIBUTION_URL).pipe(
      map((contributions) => {
        return [{"value": contributions["treatment"], "name": "Medical Action Ontology"},
          {"value": contributions["phenotype"], "name": "Human Phenotype Ontology"},
          {"value": contributions["phenopackets"], "name": "PhenoPackets"}];
      })
    );
  }

  /**
   * Get Annotation Counts by disease id or all if none
   */
  getAnnotationCounts(diseaseId: string): any {
    return this.httpClient.get(environment.POET_API_STATISTICS_ANNOTATION_URL + diseaseId);
  }
}
