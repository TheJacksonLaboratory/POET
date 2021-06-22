import { Injectable } from '@angular/core';
import { Annotation, Message } from "../models/models";
import { MatDialog } from "@angular/material/dialog";
import { DialogMessagesComponent } from "../../portal/portal-curate/dialog-messages/dialog-messages.component";

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  constructor(public dialog: MatDialog) { }

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

  openMessagesDialog(messages: Message[]){
    this.dialog.open(DialogMessagesComponent, {
      data: {
        messages: messages
      }
    });
  }
}
