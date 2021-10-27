import { TestBed } from '@angular/core/testing';

import { UtilityService } from './utility.service';
import { Annotation } from "../models/models";
import { MatDialogModule } from "@angular/material/dialog";
import {UserService} from "./user/user.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('UtilityService', () => {
  let service: UtilityService;
  let fakeAnnotations: Annotation[];
  let fakeOptions;
  let fakeUsers;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatDialogModule, HttpClientTestingModule],
      providers: [UserService]
    });
    service = TestBed.inject(UtilityService);
    fakeAnnotations = getFakeAnnotations();
    fakeOptions = getFakeOptions();
    fakeUsers = getFakeUsers();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should test reviewer comments', () => {
    expect(service.hasReviewerComments(fakeAnnotations[0])).toBeFalse();
    expect(service.hasReviewerComments(fakeAnnotations[1])).toBeTrue();
  });

  it('should test under review', () => {
    expect(service.isUnderReview(fakeAnnotations[0])).toBeFalse();
    expect(service.isUnderReview(fakeAnnotations[1])).toBeTrue();
  });

  it('should test is accepted', ()=> {
    expect(service.isAccepted(fakeAnnotations[0])).toBeFalse();
    expect(service.isAccepted(fakeAnnotations[1])).toBeFalse();
    expect(service.isAccepted(fakeAnnotations[2])).toBeTrue();
    expect(service.isAccepted(fakeAnnotations[3])).toBeFalse();
  });

  it('should test is official', ()=> {
    expect(service.isOfficial(fakeAnnotations[0])).toBeFalse();
    expect(service.isOfficial(fakeAnnotations[1])).toBeFalse();
    expect(service.isOfficial(fakeAnnotations[2])).toBeFalse();
    expect(service.isOfficial(fakeAnnotations[3])).toBeTrue();
  });

  it('should test is retired', ()=> {
    expect(service.isRetired(fakeAnnotations[0])).toBeFalse();
    expect(service.isRetired(fakeAnnotations[1])).toBeFalse();
    expect(service.isRetired(fakeAnnotations[2])).toBeFalse();
    expect(service.isRetired(fakeAnnotations[3])).toBeFalse();
    expect(service.isRetired(fakeAnnotations[4])).toBeTrue();
  });

  it('should test needs work', () => {
    expect(service.isNeedsWork(fakeAnnotations[0])).toBeTrue();
    expect(service.isNeedsWork(fakeAnnotations[1])).toBeFalse();
  });

  it('should return correct display maxo', () => {
    expect(service.displayMaxoFn(fakeOptions[0])).toEqual("Name1 OntologyId1");
    expect(service.displayMaxoFn(fakeOptions[1])).toEqual("Name2 OntologyId2");
    expect(service.displayMaxoFn(fakeOptions[2])).toEqual("Name3 OntologyId3");
    expect(service.displayMaxoFn(fakeOptions[3])).toEqual("");
  });

  it('should return correct display hpo', () => {
    expect(service.displayHpoFn(fakeOptions[0])).toEqual("Name1 Id1");
    expect(service.displayHpoFn(fakeOptions[1])).toEqual("Name2 Id2");
    expect(service.displayHpoFn(fakeOptions[2])).toEqual("Name3 Id3");
    expect(service.displayHpoFn(fakeOptions[3])).toEqual("");
  });

  it('should return correct disease source boolean', () => {
    expect(service.isDiseaseSource("OMIM:01931")).toBeTrue();
    expect(service.isDiseaseSource("PMID:01931")).toBeFalse();
    expect(service.isDiseaseSource("")).toBeFalse();
    expect(service.isDiseaseSource("PMID:009311")).toBeFalse();
  })

  it('it should return correct elevated actions boolean', () => {
    expect(service.showElevatedActions("POET_ADMIN", getFakeAnnotations()[1])).toBeTrue();
    expect(service.showElevatedActions("POET_ADMIN", getFakeAnnotations()[0])).toBeFalse();
    expect(service.showElevatedActions("POET_CURATOR", getFakeAnnotations()[1])).toBeFalse();
    expect(service.showElevatedActions("POET_CURATOR", getFakeAnnotations()[0])).toBeFalse();
  })

  it('it should test if they own annotation', () => {
    expect(service.ownsAnnotation(fakeUsers[0], fakeAnnotations[0].owner)).toBeTrue();
    expect(service.ownsAnnotation(fakeUsers[1], fakeAnnotations[1].owner)).toBeTrue();
    expect(service.ownsAnnotation(fakeUsers[1], fakeAnnotations[2].owner)).toBeTrue();
    expect(service.ownsAnnotation(fakeUsers[0], fakeAnnotations[3].owner)).toBeFalse();
    expect(service.ownsAnnotation(null, fakeAnnotations[4].owner)).toBeFalse();
    expect(service.ownsAnnotation(undefined, fakeAnnotations[4].owner)).toBeFalse();
    expect(service.ownsAnnotation({}, fakeAnnotations[4].owner)).toBeFalse();
  })



  function getFakeAnnotations(): Annotation[]
  {
   return [{

     id: "1",
     type: "phenotype",
     status: "NEEDS_WORK",
     reviewMessages: [],
     owner: {
       nickname: "Curator1",
       userRole: "Role"
     },
     annotationSource: {
       publication: {
         publicationId: "some fake publication id",
         publicationName: "some fake publication name",
         date: "2019-4-06",
         doi: "02392382",
         firstAuthor: "alex jones"
       },
       disease: {
         diseaseId: "OMIM:79384",
         diseaseName: "some disease name",
         equivalentId: "some id",
         description: "disease description"
       }
     }
   },
     {
       id: "2",
       type: "phenotype",
       status: "UNDER_REVIEW",
       owner: {
         nickname: "Curator2",
         userRole: "Role"
       },
       annotationSource: {
         publication: {
           publicationId: "some fake publication id",
           publicationName: "some fake publication name",
           date: "2019-4-06",
           doi: "02392382",
           firstAuthor: "alex jones"
         },
         disease: {
           diseaseId: "OMIM:79384",
           diseaseName: "some disease name",
           equivalentId: "some id",
           description: "disease description"
         }
       },
       reviewMessages: [{ reviewer: {
           nickname: "curatr",
           userRole: "Role"
         }, value: "the message"}],
     },
     {
       id: "3",
       type: "phenotype",
       status: "ACCEPTED",
       owner: {
         nickname: "Curator2",
         userRole: "Role"
       },
       annotationSource: {
         publication: {
           publicationId: "some fake publication id",
           publicationName: "some fake publication name",
           date: "2019-4-06",
           doi: "02392382",
           firstAuthor: "alex jones"
         },
         disease: {
           diseaseId: "OMIM:79384",
           diseaseName: "some disease name",
           equivalentId: "some id",
           description: "disease description"
         }
       },
       reviewMessages: [{ reviewer: {
           nickname: "curatr",
           userRole: "Role"
         }, value: "the message"}],
     },
     {
       id: "4",
       type: "phenotype",
       status: "OFFICIAL",
       owner: {
         nickname: "curatr",
         userRole: "Role"
       },
       annotationSource: {
         publication: {
           publicationId: "some fake publication id",
           publicationName: "some fake publication name",
           date: "2019-4-06",
           doi: "02392382",
           firstAuthor: "alex jones"
         },
         disease: {
           diseaseId: "OMIM:79384",
           diseaseName: "some disease name",
           equivalentId: "some id",
           description: "disease description"
         }
       },
       reviewMessages: [{ reviewer: {
           nickname: "curatr",
           userRole: "Role"
         }, value: "the message"}],
     },
     {
       id: "5",
       type: "phenotype",
       status: "RETIRED",
       owner: {
         nickname: "curatr",
         userRole: "Role"
       },
       annotationSource: {
         publication: {
           publicationId: "some fake publication id",
           publicationName: "some fake publication name",
           date: "2019-4-06",
           doi: "02392382",
           firstAuthor: "alex jones"
         },
         disease: {
           diseaseId: "OMIM:79384",
           diseaseName: "some disease name",
           equivalentId: "some id",
           description: "disease description"
         }
       },
       reviewMessages: [{ reviewer: {
           nickname: "curatr",
           userRole: "Role"
         }, value: "the message"}],
     }
   ];
  }

  function getFakeOptions()
  {
    return [{ name: "Name1", id: "Id1", ontologyId: "OntologyId1"},
      { name: "Name2", id: "Id2", ontologyId: "OntologyId2"},
      { name: "Name3", id: "Id3", ontologyId: "OntologyId3"},
      {}
    ];
  }

  function getFakeUsers(){
    return [
      {
        role: "ELEVATED_CURATOR",
        nickname: "Curator1"
      },
      {
        role: "CURATOR",
        nickname: "Curator2"
      },
      {
        role: "GUEST",
        nickname: "guest"
      }
    ]
  }
});
