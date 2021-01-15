import { Injectable, OnInit } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs";
import { AnnotationSource, Disease } from "../../models/models";

@Injectable({
  providedIn: 'root'
})
export class StateService implements OnInit {
  private selectedAnnotationSourceSubject: BehaviorSubject<AnnotationSource> = new BehaviorSubject<AnnotationSource>({
    publication: null,
    disease: null
  });

  private selectedOntologySubject: BehaviorSubject<string> = new BehaviorSubject<string>('maxo');
  private sourceAndOntologySelectedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private selectedDiseaseSubject: BehaviorSubject<Disease> = new BehaviorSubject<Disease>(null);
  private reloadAnnotationsSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private selectedTreatmentAnnotationSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private selectedPhenotypeAnnotationSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private selectedAnnotationModeSubject: BehaviorSubject<any> = new BehaviorSubject<any>("view");

  selectedAnnotationSource: Observable<AnnotationSource> = this.selectedAnnotationSourceSubject.asObservable();
  selectedOntology: Observable<string> = this.selectedOntologySubject.asObservable();
  sourceAndOntologySelected: Observable<boolean> = this.sourceAndOntologySelectedSubject.asObservable();
  selectedDisease: Observable<Disease> = this.selectedDiseaseSubject.asObservable();
  selectedTreatmentAnnotation: Observable<any> = this.selectedTreatmentAnnotationSubject.asObservable();
  selectedPhenotypeAnnotation: Observable<any> = this.selectedPhenotypeAnnotationSubject.asObservable();
  selectedAnnotationMode: Observable<any> = this.selectedAnnotationModeSubject.asObservable();
  triggerReloadAnnotations: Observable<any> = this.reloadAnnotationsSubject.asObservable();

  constructor() {

  }

  ngOnInit(){
  }

  setSelectedOntology(ontology: string): void {
    this.selectedOntologySubject.next(ontology);
    if(this.selectedAnnotationSourceSubject.getValue().publication != null &&
      this.selectedAnnotationSourceSubject.getValue().disease != null){
      this.sourceAndOntologySelectedSubject.next(true);
    }
  }

  getSelectedOntology() {
    return this.selectedOntologySubject.getValue();
  }

  setSelectedSource(annotationSource: AnnotationSource): void {
    this.selectedAnnotationSourceSubject.next(annotationSource);
    if(this.selectedOntologySubject.getValue() != ''){
      this.sourceAndOntologySelectedSubject.next(true);
    }
  }

  setSelectedDisease(disease: Disease){
    this.selectedDiseaseSubject.next(disease);
  }

  getSelectedSource() {
    return this.selectedAnnotationSourceSubject.getValue();
  }

  triggerAnnotationReload(reload: boolean){
    this.reloadAnnotationsSubject.next(reload);
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


}
