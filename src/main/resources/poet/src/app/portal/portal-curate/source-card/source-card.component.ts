import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { CurationService } from "../../../shared/services/curation/curation.service";
import { Disease, Publication } from "../../../shared/models/models";
import { Observable } from "rxjs";
import { finalize } from "rxjs/operators";
import { OntologySheet } from "./ontology-sheet/ontology-sheet.component";
import { MatBottomSheet } from "@angular/material/bottom-sheet";
import { animate, query, stagger, style, transition, trigger, useAnimation } from "@angular/animations";
import { bounceInLeft } from "ng-animate";
import { StateService } from "../../../shared/services/state/state.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-source-selection-card',
  templateUrl: './source-card.component.html',
  styleUrls: ['./source-card.component.scss']
})
export class SourceCardComponent implements OnChanges, OnInit {

  @Input('activeType') activeType: string;
  @Input('activeId') id: string;
  @Output('working') workEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

  selectedDisease: Disease;
  selectedPublication: Publication;
  annotatedDiseases$: Observable<Disease[]>;
  annotatedPublications$: Observable<Publication[]>;

  constructor(public curationService: CurationService,
              private ontologySheet: MatBottomSheet, private stateService: StateService, private route: Router) {

  }

  ngOnInit() {
  }

  ngOnChanges(): void {
    if (this.isActiveTypeDisease()) {
      this.workEvent.emit(true);
      this.curationService.getDisease(this.id).pipe(
        finalize(() => this.workEvent.emit(false))
      ).subscribe((disease) => {
        this.selectedDisease = disease
        this.stateService.setSelectedDisease(disease);
      }, (error) => {
          this.route.navigate(['portal/dashboard'], {state: {error:true, message: error.text }});
      });
      this.annotatedPublications$ = this.curationService.getDiseasePublications(this.id);
    } else if (this.isActiveTypePublication()) {
      this.workEvent.emit(true);
      this.curationService.getPublication(this.id).pipe(
        finalize(() => this.workEvent.emit(false))
      ).subscribe((publication) =>
        this.selectedPublication = publication
      );
      this.annotatedDiseases$ = this.curationService.getPublicationDiseases(this.id);
    }
  }

  /**
   * Return the opposite of the current active type, disease or publication
   */
  oppositeActiveType() {
    return this.activeType === 'disease' ? 'publication' : 'disease';
  }

  /**
   * When selecting an item from annotated list. Completes the selection and emits to parent portal-curate.
   * @param item
   */
  onSelectedListItem(item: any) {
    if (!this.stateService.getSelectedOntology()) {
      this.openBottomSheet();
    }
    this.saveSource(item);
  }

  saveSource(item: any) {
    let selectedSource;
    if (this.isActiveTypeDisease()) {
      selectedSource = {
        'disease': this.selectedDisease,
        'publication': item,
      };
    } else {
      selectedSource = {
        'disease': item,
        'publication': this.selectedPublication,
      };
    }
    this.stateService.setSelectedSource(selectedSource);
  }

  /**
   * Is the current active type a disease?
   */
  isActiveTypeDisease() {
    return this.activeType === 'disease';
  }

  /**
   * Is the current active type a publication?
   */
  isActiveTypePublication() {
    return this.activeType === 'publication';
  }

  openBottomSheet(): void {
    const ontologySheetRef = this.ontologySheet.open(OntologySheet);
    // subscribe to observable that emit event when bottom sheet closes
    ontologySheetRef.afterDismissed().subscribe((ontology) => {
      // Selected an ontology.
      this.stateService.setSelectedOntology(ontology);
    });
  }
}
