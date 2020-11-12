import { Component, OnInit } from '@angular/core';
import { Disease, MaxoAnnotation, Publication } from "../../../shared/models/models";
import { StateService } from "../../../shared/services/state/state.service";
import { CurationService } from "../../../shared/services/curation/curation.service";
import { Observable } from "rxjs";
import { animate, query, stagger, style, transition, trigger, useAnimation } from "@angular/animations";
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

    this.stateService.selectedDisease.subscribe((disease: Disease) => {
      if(this.ontology === 'maxo' && disease != null){
        this.disease = disease;
        this.maxoAnnotations = this.curationService.getMaxoAnnotations(this.disease, null, "")
      }
    })

    this.stateService.selectedAnnotationSource.subscribe((source) => {
      this.publication = source.publication;
      this.disease = source.disease;
      // Could get funky here
      if(this.ontology && this.disease){
        this.maxoAnnotations = this.curationService.getMaxoAnnotations(this.disease, this.publication,"");
      }
    });
  }

  onSelectedListItem(item: any) {
    // Send information to MaxoCard to display or edit.
  }

  ontologyToDisplay(){
    if(this.ontology == 'maxo'){
      return "Medical Actions";
    } else if(this.ontology == 'hpo'){
      return "Phenotypes";
    }
  }

}
