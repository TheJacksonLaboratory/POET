package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.model.entities.AnnotationSource
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation
import org.monarchinitiative.poet.model.entities.TreatmentAnnotation
import org.monarchinitiative.poet.model.entities.Publication
import org.monarchinitiative.poet.model.entities.User
import org.monarchinitiative.poet.model.entities.UserActivity
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus
import org.monarchinitiative.poet.model.enumeration.CurationAction
import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.monarchinitiative.poet.repository.PhenotypeAnnotationRepository
import org.monarchinitiative.poet.repository.TreatmentAnnotationRepository
import org.monarchinitiative.poet.repository.PublicationRepository
import org.monarchinitiative.poet.repository.UserActivityRepository
import org.monarchinitiative.poet.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = [ServiceTestConfig.class])
class AnnotationServiceSpec extends Specification {

    @Autowired
    PublicationRepository publicationStub

    @Autowired
    DiseaseRepository diseaseStub

    @Autowired
    AnnotationSourceRepository annotationStub

    @Autowired
    TreatmentAnnotationRepository treatmentAnnotationStub

    @Autowired
    PhenotypeAnnotationRepository phenotypeAnnotationStub

    @Autowired
    UserActivityRepository userActivityStub

    @Autowired
    UserRepository userStub

    @Autowired
    AnnotationService annotationService

    void "test get treatment annotations #desc"() {
        given:
        diseaseStub.findDiseaseByDiseaseId(_ as String) >> diseaseResponse
        treatmentAnnotationStub.findDistinctByAnnotationSourceAndStatusNot(_ as AnnotationSource, _ as AnnotationStatus) >> treatmentAnnotationResponse
        treatmentAnnotationStub.findAllByAnnotationSourceDiseaseAndStatusNot(_ as Disease, _ as AnnotationStatus) >> treatmentAnnotationResponse
        publicationStub.findByPublicationId(_ as String) >> publicationResponse
        annotationStub.findByPublicationAndDisease(_ as Publication, _ as Disease) >> annotationSourceResponse
        userActivityStub.getMostRecentDateForAnnotationActivity(_ as Long) >> userActivityResponse
        def result = annotationService.getTreatmentAnnotations(inputParameters[0], inputParameters[1], inputParameters[2])
        expect:
        result == expectedResponse

        where:
        inputParameters                               | diseaseResponse    | treatmentAnnotationResponse | publicationResponse    | annotationSourceResponse    | userActivityResponse | expectedResponse          | desc
        ["OMIM:029983", "PMID: 0092303", "desc date"] | getSingleDisease() | getTreatmentAnnotations()   | getSinglePublication() | getSingleAnnotationSource() | getUserActivity()    | getTreatmentAnnotations() | "a disease and publication id, with annotation source and returns maxo"
        ["OMIM:029983", "PMID: 0092303", "desc date"] | getSingleDisease() | []                          | getSinglePublication() | getSingleAnnotationSource() | getUserActivity()    | []                        | "a disease and publication id, with annotation source and returns empty list"
        ["OMIM:029983", "PMID: 0092303", "desc date"] | getSingleDisease() | []                          | getSinglePublication() | null                        | getUserActivity()    | null                      | "a disease and publication id, with no annotation source and returns null"
        ["OMIM:029983", null, "desc date"]            | getSingleDisease() | getTreatmentAnnotations()   | getSinglePublication() | null                        | getUserActivity()    | getTreatmentAnnotations() | "a disease and no publication id, with no annotation source and returns annotations"
        ["OMIM:029983", null, "desc date"]            | getSingleDisease() | []                          | getSinglePublication() | null                        | getUserActivity()    | []                        | "a disease and no publication id, with disease found and returns empty list"
        ["OMIM:029983", null, "desc date"]            | null               | []                          | getSinglePublication() | null                        | getUserActivity()    | null                      | "a disease and no publication id, with no disease found and returns null"
    }

    def getTreatmentAnnotations() {
        return [
                new TreatmentAnnotation("MAXO:0919323", "Surgical Procedure", "arachnodayctly", "HP:0992334", "some evidence", "some comment", "some relation", "some extension id", "some extension label"),
                new TreatmentAnnotation("MAXO:8882029", "Therapeutic Procedure", "some hp name", "HP:0222222", "some evidence", "some comment", "some relation", "some extension id", "some extension label")
        ]
    }

    def getSingleDisease() {
        return new Disease("OMIM:9820939", "Some bad disease")
    }

    def getSinglePublication() {
        return new Publication("PMID:00923232", "My first publication", "12/19/2020", "Test User", [])
    }

    def getSingleAnnotationSource() {
        return new AnnotationSource(getSinglePublication(), getSingleDisease())
    }

    def getUserActivity() {
        return new UserActivity(new User(), CurationAction.CREATE, new TreatmentAnnotation(), new TreatmentAnnotation())
    }

    void "test get phenotype annotations #desc"(){
        given:
        diseaseStub.findDiseaseByDiseaseId(_ as String) >> diseaseResponse
        phenotypeAnnotationStub.findAllByAnnotationSourceDiseaseAndStatusNot(_ as Disease, _ as AnnotationStatus) >> phenotypeAnnotationResponse
        annotationStub.findByPublicationAndDisease(_ as Publication, _ as Disease) >> annotationSourceResponse
        userActivityStub.getMostRecentDateForAnnotationActivity(_ as Long) >> userActivityResponse
        def result = annotationService.getPhenotypeAnnotations(inputParameters[0], inputParameters[2])
        expect:
        result == expectedResponse

        where:
        inputParameters                               | diseaseResponse    | phenotypeAnnotationResponse | annotationSourceResponse    | userActivityResponse | expectedResponse          | desc
        ["OMIM:029983", "desc date"]                  | getSingleDisease() | getPhenotypeAnnotations()   | getSingleAnnotationSource() | getUserActivity()    | getPhenotypeAnnotations() | "a disease with annotation source and returns phenotypes"
        ["OMIM:029983", "desc date"]                  | getSingleDisease() | []                          | getSingleAnnotationSource() | getUserActivity()    | []                        | "a disease with annotation source and returns empty list"
        ["OMIM:029983", "desc date"]                  | null               | []                          | null                        | getUserActivity()    | null                      | "a disease with no annotation source and returns null"
        ["OMIM:029983", "desc date"]                  | getSingleDisease() | getPhenotypeAnnotations()   | null                        | getUserActivity()    | getPhenotypeAnnotations() | "a disease with no annotation source and returns annotations"
        ["OMIM:029983", "desc date"]                  | getSingleDisease() | []                          | null                        | getUserActivity()    | []                        | "a disease with disease found and returns empty list"
        ["OMIM:029983", "desc date"]                  | null               | []                          | null                        | getUserActivity()    | null                      | "a disease and no publication id, with no disease found and returns null"
    }

    def getPhenotypeAnnotations(){
        return [
                new PhenotypeAnnotation("HP:0011192", "some fake hpo name", "HP:001939", "TAS", "HP:0092931", "10/20", "NOT", "some description", "" ),
                new PhenotypeAnnotation("HP:0011192", "some fake hpo name 2", "HP:001938", "TAS", "", "1/20", "", "some description", "MALE" ),
        ]
    }
}
