import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from "rxjs/internal/observable/of";
import { CurationService } from '../../shared/services/curation/curation.service';
import { AuthService } from '@auth0/auth0-angular';
import { transition, trigger, useAnimation } from '@angular/animations';
import { fadeIn } from 'ng-animate';
import { StateService } from '../../shared/services/state/state.service';
import { Disease } from '../../shared/models/models';
import { catchError, finalize, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-portal-curate',
  templateUrl: './portal-curate.component.html',
  styleUrls: ['./portal-curate.component.scss'],
  animations: [
    trigger('fadeIn', [transition('* => *', useAnimation(fadeIn))])
  ]
})
export class PortalCurateComponent implements OnInit {

  selectionType: string;
  selectedCategory: string;
  selectedDisease: Disease;
  showLoader = false;
  fxLayout = 'row';
  fxLayoutAlign = 'start stretch';
  fxFlexAnnotations = '60';
  fxFlexAnnotationOffset = '20';
  fxFlexForm = '45';
  fxFlexFormOffset = '2.5';
  sourceAndOntologySelected = false;
  showForm = false;
  annotationItems = [
    {value: 'phenotype', display: 'Phenotypes', icon: 'assignment', disabled: false, count: 0, reason: ''},
    {value: 'treatment', display: 'Treatments', icon: 'medication', disabled: false, count: 0, reason: ''}
  ];
  user: any;

  constructor(private route: ActivatedRoute, public curationService: CurationService, public stateService: StateService,
              public authService: AuthService, public router: Router) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = params.id;
      if (id) {
        if (this.determineIdType(id) === 'disease') {
          this.doingWork(true);
          this.curationService.getDisease(id).pipe(
            finalize(() => this.doingWork(false))
          ).subscribe((disease) => {
            this.selectedDisease = disease;
            this.stateService.setSelectedDisease(disease);
            this.stateService.triggerAnnotationCountsReload(true);
          }, (error) => {
            this.router.navigate(['/portal/dashboard'], {state: {error: true, message: error.text}});
          });
        }
      } else {
        this.router.navigate(['/portal/dashboard']);
      }
    });

    this.stateService.triggerReloadAnnotationCounts.subscribe((reload) => {
      if (reload && this.selectedDisease){
        this.curationService.getAnnotationCounts(this.selectedDisease.diseaseId).subscribe((counts) => {
          this.updateAnnotationCount(counts);
        });
      }
    });

    this.stateService.selectedCategory.subscribe((category) => this.selectedCategory = category);

    this.authService.user$.subscribe((user) => {
      if (user){
        user.role = user[environment.AUTH0_ROLE_CLAIM];
        this.user = user;
      }
    });


  }

  handleForm(value: boolean) {
    if (value) {
      this.showForm = true;
      this.fxFlexAnnotations = '45';
      this.fxFlexAnnotationOffset = '2.5';
    } else {
      this.showForm = false;
      this.fxFlexAnnotations = '50';
      this.fxFlexAnnotationOffset = '25';
    }
  }

  /**
   * Is the given id a disease or publication based on prefix.
   * @param id
   */
  determineIdType(id: string): string {
    if (id) {
      if (id.includes('OMIM') || id.includes('MONDO')) {
        return 'disease';
      } else if (id.includes('PMID')) {
        return 'publication';
      }
    }
  }

  /**
   * Set loader based on work happening in child components or this component.
   * @param working
   */
  doingWork(working: boolean) {
    this.showLoader = working;
  }

  updateAnnotationCount(counts){
    this.annotationItems.forEach((item) => {
      if (item.value === 'phenotype'){
        item.count = counts.phenotypeCount;
      } else if (item.value === 'treatment') {
        item.count = counts.treatmentCount;
      }
    });
  }

  shouldShowTreatmentCard() {
    return this.selectedDisease && this.selectedCategory === 'treatment' && this.showForm;
  }

  shouldShowPhenotypeCard() {
    return this.selectedDisease && this.selectedCategory === 'phenotype' && this.showForm;
  }

  changeCategory(ontology: string) {
    if (this.selectedCategory !== ontology){
      this.stateService.setSelectedCategory(ontology);
      this.handleForm(false);
    }
  }

  navigateToPage(disease) {
    if (disease) {
      this.router.navigate(['/portal/curate/' + disease.id]);
    }
  }

  /*
      Mapping ontology terms to there respective URLS.
      OMIM:1234 -> https://omim.org/entry/1234
   */
  getExternalTermIdUrlFromId(termId?: string) {
    if(!termId){
      return '';
    }
    const sourceParts = termId.split(':');
    if (this.isTermIdExpected(termId, "OMIM")) {
      return `https://omim.org/entry/${sourceParts[1]}`;
    } else if (this.isTermIdExpected(termId, "ORPHA")) {
      return `https://www.orpha.net/consor/cgi-bin/OC_Exp.php?Lng=EN&Expert=${sourceParts[1]}`
    } else if (this.isTermIdExpected(termId, "MONDO")){
      return `https://monarchinitiative.org/disease/${termId}`;
    } else if(this.isTermIdExpected(termId, "PMID")){
      return `https://www.ncbi.nlm.nih.gov/pubmed/${sourceParts[1]}`;
    }
  }
  /*
      Checking if the term prefix matches what we are looking for.
      OMIM:1234 matches? OMIM -> true
   */
  isTermIdExpected(termId: string, expected: string) {
    return termId != "" && termId != null && expected != "" && expected != null
      ? termId.toUpperCase().includes(expected) : false;
  }
}
