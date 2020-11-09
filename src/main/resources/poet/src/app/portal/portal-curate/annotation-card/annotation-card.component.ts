import { Component, OnInit } from '@angular/core';
import { Disease, MaxoAnnotation, Publication } from "../../../shared/models/models";
import { StateService } from "../../../shared/services/state/state.service";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { Observable } from "rxjs";
import { transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";

@Component({
  selector: 'app-annotation-card',
  templateUrl: './annotation-card.component.html',
  styleUrls: ['./annotation-card.component.scss'],
  animations: [
    trigger('bounceInLeft', [transition( "0 => 1" , useAnimation(bounceInLeft, {
      params: {timing: .5}
    }))])
  ]
})
export class AnnotationCardComponent implements OnInit {
  disease: Disease;
  publication: Publication;
  ontology: string;
  maxoAnnotations: Observable<MaxoAnnotation[]>;
  triggerBounceIn: any;
  constructor(public stateService: StateService, public curationService: CurationService) { }

  ngOnInit(): void {

    this.stateService.selectedOntology.subscribe((ontology) => {
      this.ontology = ontology;
    });

    this.stateService.selectedAnnotationSource.subscribe((source) => {
      this.publication = source.publication;
      this.disease = source.disease;
      // Could get funky here
      if(this.ontology){
        this.maxoAnnotations = this.curationService.getMaxoAnnotations(this.publication, this.disease,this.ontology, "");
      }
    });
  }

  onSelectedListItem(item: any) {
    // Send information to MaxoCard to display or edit.
  }

}
