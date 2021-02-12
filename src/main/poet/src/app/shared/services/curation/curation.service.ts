import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { Observable } from "rxjs";
import {Disease, MaxoAnnotation, Publication, UserActivityResponse} from "../../models/models";
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
  getMaxoAnnotations(disease: Disease, publication: Publication, sort: string): Observable<MaxoAnnotation[]>{
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
  updateMaxoAnnotation(annotation: any) {
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
  saveMaxoAnnotation(annotation: any) {
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
  deleteMaxoAnnotation(id: string) {
    return this.httpClient.delete(environment.POET_API_MAXO_ANNOTATION + id);
  }

  /**
   * Get User Activity for curations
   * @param everyone
   */
  getActivity(everyone: boolean){
    const params = new HttpParams().set("all", String(everyone));
    return this.httpClient.get<UserActivityResponse[]>(environment.POET_API_STATISTICS_ACTIVITY_URL, {params: params}).pipe(
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
   * @param everyone - a boolean if we should get everyone or the current user
   * @param weeksBack - how many weeks of activity
   */
  getGroupActivityFeed(everyone: boolean, weeksBack: number){
    const params = new HttpParams().set("all", String(everyone)).set("weeks", String(weeksBack));
    return this.httpClient.get<UserActivityResponse[]>(environment.POET_API_STATISTICS_ACTIVITY_URL, {params: params}).pipe(
      map((activityMap) => this.reduceToGroupedActivity(activityMap))
    );
  }

  /**
   * A function to reduce a list of activity records to a simplified grouping of activity.
   * Activity older than a day gets grouped by user and disease
   * Activity older than an hour gets grouped by user, disease, type
   * Activity under an hour gets grouped by user, disease, type, action
   * @param activityList
   */
  reduceToGroupedActivity(activityList){
    let activities = [];
    let dayMap = activityList.reduce(function (r, a) {
      let date = new Date(a.localDateTime).toLocaleDateString();
      r[date] = r[date] || [];
      r[date].push(a);
      return r;
    }, Object.create(null));

    Object.keys(dayMap).forEach(date => {
     let isSameDay =  this.isSameDay(new Date(date));
      // Reduce down and create activities for day, user, and disease
      dayMap[date] = dayMap[date].reduce(function (r, a) {
        r[a.user.nickname] = r[a.user.nickname] || [];
        r[a.user.nickname].push(a);
        return r;
      }, Object.create(null));

      Object.keys(dayMap[date]).forEach(user => {
        // On a user
        dayMap[date][user] = dayMap[date][user].reduce(function (r, a) {
          let disease = a.annotation.annotationSource.disease.diseaseId + "~" +
            a.annotation.annotationSource.disease.diseaseName;
            r[disease] = r[disease] || [];
            r[disease].push(a);
          return r;
        }, Object.create(null));
          Object.keys(dayMap[date][user]).forEach(diseaseJoined => {
            let [diseaseId, diseaseName] = diseaseJoined.split("~");
            if(isSameDay){
              let currentHour = new Date().getHours();
              const roundToHour = this.roundToHour;
              dayMap[date][user][diseaseJoined] = dayMap[date][user][diseaseJoined].reduce(function (r, a) {
                let hourOfDay = roundToHour(new Date(a.localDateTime));
                r[hourOfDay] = r[hourOfDay] || [];
                r[hourOfDay].push(a);
                return r;
              }, Object.create(null));
              Object.keys(dayMap[date][user][diseaseJoined]).forEach(hour => {
                dayMap[date][user][diseaseJoined][hour] = dayMap[date][user][diseaseJoined][hour].reduce(function (r, a) {
                  r[a.annotation.annotationType] = r[a.annotation.annotationType] || [];
                  r[a.annotation.annotationType].push(a);
                  return r;
                }, Object.create(null));
                Object.keys(dayMap[date][user][diseaseJoined][hour]).forEach(type => {
                  if(parseInt(hour) == currentHour){
                    dayMap[date][user][diseaseJoined][hour][type] = dayMap[date][user][diseaseJoined][hour][type].reduce(function (r, a) {
                        r[a.curationAction] = r[a.curationAction] || [];
                        r[a.curationAction].push(a);
                        return r;
                      }, Object.create(null));
                    Object.keys(dayMap[date][user][diseaseJoined][hour][type]).forEach(action => {
                      const count = dayMap[date][user][diseaseJoined][hour][type][action].length;
                      const mostRecent = this.getMostRecentDate(dayMap[date][user][diseaseJoined][hour][type][action]);
                      const minutesFromNow = this.minutesFromNow(mostRecent);
                      const actionFriendly = this.actionLookup(action);
                      const annotationGrammar = this.annotationGrammar(count);
                      let view;
                      if (minutesFromNow == 0) {
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
                  } else {
                    const count = dayMap[date][user][diseaseJoined][hour][type].length;
                    const mostRecent = this.getMostRecentDate(dayMap[date][user][diseaseJoined][hour][type]);
                    const hoursFromNow = this.hoursFromNow(mostRecent);
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
            } else {
              const mostRecent = this.getMostRecentDate(dayMap[date][user][diseaseJoined]);
              const count = dayMap[date][user][diseaseJoined].length;
              const daysFrom = this.daysFromDate(mostRecent);
              const annotationGrammar = this.annotationGrammar(count);
              let view;
              if (daysFrom == 0) {
                view = `${user} modified ${count} ${annotationGrammar} for ${diseaseName} today.`;
              } else if(daysFrom == 1){
                view = `${user} modified ${count} ${annotationGrammar} for ${diseaseName} yesterday.`;
              } else {
                view = `${user} modified ${count} ${annotationGrammar} for ${diseaseName} ${daysFrom} days ago.`;
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
    return activities.sort((a:any, b:any) => (b.date - a.date));
  }

  private getMostRecentDate(annotations){
    let dates = annotations.map(a => {
      return new Date(a.localDateTime);
    });
    return new Date(Math.max(...dates));
  }

  private annotationGrammar(count){
    return count > 1 ? "annotations" : "annotation";
  }
  private isSameDay(dateToCheck): boolean {
    const today = new Date();
    return (dateToCheck.getDate() === today.getDate()
      && dateToCheck.getMonth() === today.getMonth()
      && dateToCheck.getFullYear() === today.getFullYear())
  }

  private roundToHour(dateToRound): number {
    let m = 60 * 60 * 1000; // milliseconds in an hour
    return new Date(Math.round(dateToRound.getTime() / m ) * m).getHours();
  }
  private daysFromDate(dateToCheck): number {
    const today = new Date();
    return Math.round((today.getTime() - dateToCheck.getTime())/(1000*60*60*24))
  }

  private hoursFromNow(dateToCheck): number {
    const now = new Date();
    return Math.round((now.getTime() - dateToCheck.getTime())/(1000*60*60))
  }

  private minutesFromNow(dateToCheck): number {
    const now = new Date();
    return Math.round((now.getTime() - dateToCheck.getTime())/(1000*60))
  }

  private actionLookup(action): string {
    const actionMap = {
      "CREATE": "created",
      "UPDATE": "updated",
      "DELETE": "deleted"
    }
    return actionMap[action];
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
