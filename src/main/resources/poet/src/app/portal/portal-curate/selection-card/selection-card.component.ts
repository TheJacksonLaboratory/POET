import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { CurationService } from "../../../shared/services/curation.service";
import { Disease, Publication } from "../../../shared/models/models";
import { Observable } from "rxjs";
import { finalize, tap } from "rxjs/operators";
import { AuthService } from "@auth0/auth0-angular";

@Component({
  selector: 'app-selection-card',
  templateUrl: './selection-card.component.html',
  styleUrls: ['./selection-card.component.scss']
})
export class SelectionCardComponent implements OnChanges {

  @Input('activeType') activeType: string;
  @Input('activeId') id: string;
  @Output('working') workEvent: EventEmitter<boolean>  = new EventEmitter<boolean>();
  @Output('sourceSelection') sourceSelection: EventEmitter<any> = new EventEmitter<any>();

  selectedDisease: Disease;
  selectedPublication: Publication;
  annotatedDiseases$: Observable<Disease[]>;
  annotatedPublications$: Observable<Publication[]>;
  selectedSource: any;
  constructor(public curationService: CurationService) { }

  ngOnChanges(): void {
    if(this.isActiveTypeDisease()){
      this.workEvent.emit(true);
      this.curationService.getDisease(this.id).pipe(
        finalize(() => this.workEvent.emit(false))
      ).subscribe((disease) => this.selectedDisease = disease);
      this.annotatedPublications$= this.curationService.getDiseasePublications(this.id);
    } else if(this.isActiveTypePublication()) {
      this.workEvent.emit(true);
      this.curationService.getPublication(this.id).pipe(
          finalize(() => this.workEvent.emit(false))
        ).subscribe((publication) => this.selectedPublication = publication);
      this.annotatedDiseases$ = this.curationService.getPublicationDiseases(this.id);
    }
  }

  /**
   * Return the opposite of the current active type, disease or publication
   */
  oppositeActiveType(){
    return this.activeType === 'disease' ? 'publication': 'disease';
  }

  /**
   * When selecting an item from annotated list. Completes the selection and emits to parent portal-curate.
   * @param item
   */
  onSelectedListItem(item: any){
    if(this.isActiveTypeDisease()){
      this.selectedSource = {
          'disease': this.selectedDisease,
          'publication': item
        };
    } else {
      this.selectedSource = {
        'disease': item,
        'publication': this.selectedPublication
      };
    }
    this.sourceSelection.emit(this.selectedSource);
  }

  /**
   * Is the current active type a disease?
   */
  isActiveTypeDisease(){
    return this.activeType === 'disease';
  }

  /**
   * Is the current active type a publication?
   */
  isActiveTypePublication(){
    return this.activeType === 'publication';
  }
}
