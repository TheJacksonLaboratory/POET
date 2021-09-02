import { Injectable } from '@angular/core';
import { Annotation, Message } from "../models/models";
import { MatDialog } from "@angular/material/dialog";
import { DialogMessagesComponent } from "../../portal/portal-curate/dialog-messages/dialog-messages.component";
import { UserService } from './user/user.service';
import {MonarchSearchResult} from "../models/search-models";

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  constructor(public dialog: MatDialog, public userService: UserService) { }

  hasReviewerComments(annotation: Annotation){
    return  annotation && annotation.reviewMessages.length > 0;
  }

  isUnderReview(annotation: Annotation): boolean {
      return annotation && annotation.status === "UNDER_REVIEW";
  }

  isNeedsWork(annotation: Annotation): boolean {
    return annotation && annotation.status === "NEEDS_WORK";
  }

  displayMaxoFn(option) {
    return option && option.name ? `${option.name} ${option.ontologyId}` : '';
  }

  displayHpoFn(option) {
    return option && option.name ? `${option.name} ${option.id}` : '';
  }

  displayFrequency(option){
    if(option && option.hasOwnProperty("id")){
      return `${option.name} ${option.id}`;
    } else if(option && option.hasOwnProperty("ontologyId")) {
      return `${option.name} ${option.ontologyId}`;
    } else {
      return option;
    }
  }

  displayMonarchSearchFn(monarchSearchResult: MonarchSearchResult) {
      return  monarchSearchResult ? monarchSearchResult.label : "";
  }

  displayChebiFn(monarchSearchResult: MonarchSearchResult) {
    return monarchSearchResult && monarchSearchResult.label ? `${monarchSearchResult.id} - ${monarchSearchResult.label}` : '';
  }

  isDiseaseSource(publicationId: string){
    return publicationId.includes("OMIM");
  }

  openMessagesDialog(messages: Message[]){
    this.dialog.open(DialogMessagesComponent, {
      data: {
        messages: messages
      }
    });
  }

  showElevatedActions(userRole: string, selectedAnnotation: Annotation){
    return this.isUnderReview(selectedAnnotation) && this.userService.isElevatedCurator(userRole);
  }
}
