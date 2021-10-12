import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs";
import { AnnotationSource, Disease, PhenotypeAnnotation, TreatmentAnnotation } from "../../models/models";
import { CurationService } from "../curation/curation.service";

@Injectable({
  providedIn: 'root'
})
export class StateService {
  private selectedAnnotationSourceSubject: BehaviorSubject<AnnotationSource> = new BehaviorSubject<AnnotationSource>({
    publication: null,
    disease: null
  });

  private selectedCategorySubject: BehaviorSubject<string> = new BehaviorSubject<string>('phenotype');
  private sourceAndOntologySelectedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private selectedDiseaseSubject: BehaviorSubject<Disease> = new BehaviorSubject<Disease>(null);
  private reloadAnnotationCountsSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private selectedTreatmentAnnotationSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private selectedPhenotypeAnnotationSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private selectedAnnotationModeSubject: BehaviorSubject<any> = new BehaviorSubject<any>("view");
  private phenotypeAnnotationsSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]);
  private treatmentAnnotationsSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]);


  selectedAnnotationSource: Observable<AnnotationSource> = this.selectedAnnotationSourceSubject.asObservable();
  selectedCategory: Observable<string> = this.selectedCategorySubject.asObservable();
  sourceAndOntologySelected: Observable<boolean> = this.sourceAndOntologySelectedSubject.asObservable();
  selectedDisease: Observable<Disease> = this.selectedDiseaseSubject.asObservable();
  selectedTreatmentAnnotation: Observable<any> = this.selectedTreatmentAnnotationSubject.asObservable();
  selectedPhenotypeAnnotation: Observable<any> = this.selectedPhenotypeAnnotationSubject.asObservable();
  selectedAnnotationMode: Observable<any> = this.selectedAnnotationModeSubject.asObservable();
  triggerReloadAnnotationCounts: Observable<any> = this.reloadAnnotationCountsSubject.asObservable();
  phenotypeAnnotations: Observable<PhenotypeAnnotation[]> = this.phenotypeAnnotationsSubject.asObservable();
  treatmentAnnotations: Observable<TreatmentAnnotation[]> = this.treatmentAnnotationsSubject.asObservable();

  constructor(private curationService: CurationService) {}

  setSelectedCategory(ontology: string): void {
    this.selectedCategorySubject.next(ontology);
    if(this.selectedAnnotationSourceSubject.getValue().publication != null &&
      this.selectedAnnotationSourceSubject.getValue().disease != null){
      this.sourceAndOntologySelectedSubject.next(true);
    }
    this.triggerAnnotationReload(true, false);
  }

  getSelectedCategory() {
    return this.selectedCategorySubject.getValue();
  }

  setSelectedSource(annotationSource: AnnotationSource): void {
    this.selectedAnnotationSourceSubject.next(annotationSource);
    if(this.selectedCategorySubject.getValue() != ''){
      this.sourceAndOntologySelectedSubject.next(true);
    }
  }

  setSelectedDisease(disease: Disease){
    this.selectedDiseaseSubject.next(disease);
    if(disease != null){
      this.triggerAnnotationReload(true, true);
    }
  }

  getSelectedSource() {
    return this.selectedAnnotationSourceSubject.getValue();
  }

  triggerAnnotationReload(reload: boolean, all: boolean){
    const disease = this.selectedDiseaseSubject.getValue();
    if(reload && disease !== null){
      if(all || this.selectedCategorySubject.getValue() === 'phenotype'){
        this.curationService.getPhenotypeAnnotations(disease).subscribe((annotations) => {
            this.phenotypeAnnotationsSubject.next(annotations);
        });

      }

      if(all || this.selectedCategorySubject.getValue() === 'treatment'){
        this.curationService.getTreatmentAnnotations(disease).subscribe((annotations) => {
          this.treatmentAnnotationsSubject.next(annotations);
        })
      }
    }
  }

  triggerAnnotationCountsReload(reload: boolean){
    this.reloadAnnotationCountsSubject.next(reload);
  }

  setSelectedTreatmentAnnotation(annotation){
    this.selectedTreatmentAnnotationSubject.next(annotation);
  }

  setSelectedPhenotypeAnnotation(annotation){
    this.selectedPhenotypeAnnotationSubject.next(annotation);
  }

  setSelectedAnnotationMode(mode){
    this.selectedAnnotationModeSubject.next(mode)
  }

  isValidCategory(category: string){
    return category === 'phenotype' || category === 'treatment';
  }

}
