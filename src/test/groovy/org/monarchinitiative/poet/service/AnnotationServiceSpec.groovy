package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.model.entities.AnnotationSource
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.MaxoAnnotation
import org.monarchinitiative.poet.model.entities.Publication
import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.monarchinitiative.poet.repository.MaxoAnnotationRepository
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
    MaxoAnnotationRepository maxoAnnotationStub

    @Autowired
    UserActivityRepository userActivityStub

    @Autowired
    UserRepository userStub

    @Autowired
    AnnotationService annotationService

    void "test get maxo annotations #desc"() {
        given:
        diseaseStub.findDiseaseByDiseaseId(_ as String) >> diseaseResponse
        maxoAnnotationStub.findDistinctByAnnotationSourceAndStatusNot(_, _) >> maxoAnnotationResponse
        maxoAnnotationStub.findAllByAnnotationSourceDiseaseAndStatusNot(_, _) >> maxoAnnotationResponse
        publicationStub.findByPublicationId(_ as String) >> publicationResponse
        annotationStub.findByPublicationAndDisease(_ as Publication, _ as Disease) >> annotationSourceResponse
        def result = annotationService.getMaxoAnnotation(inputParameters[0], inputParameters[1], inputParameters[2])
        expect:
        result == expectedResponse

        where:
        inputParameters                               | diseaseResponse    | maxoAnnotationResponse | publicationResponse    | annotationSourceResponse    | expectedResponse     | desc
        ["OMIM:029983", "PMID: 0092303", "desc date"] | getSingleDisease() | getMaxoAnnotations()   | getSinglePublication() | getSingleAnnotationSource() | getMaxoAnnotations() | "a disease and publication id, with annotation source and returns maxo"
        ["OMIM:029983", "PMID: 0092303", "desc date"] | getSingleDisease() | []                     | getSinglePublication() | getSingleAnnotationSource() | []                   | "a disease and publication id, with annotation source and returns empty list"
        ["OMIM:029983", "PMID: 0092303", "desc date"] | getSingleDisease() | []                     | getSinglePublication() | null                        | null                 | "a disease and publication id, with no annotation source and returns null"
        ["OMIM:029983", null, "desc date"]            | getSingleDisease() | getMaxoAnnotations()   | getSinglePublication() | null                        | getMaxoAnnotations() | "a disease and no publication id, with no annotation source and returns annotations"
        ["OMIM:029983", null, "desc date"]            | getSingleDisease() | []                     | getSinglePublication() | null                        | []                   | "a disease and no publication id, with disease found and returns empty list"
        ["OMIM:029983", null, "desc date"]            | null               | []                     | getSinglePublication() | null                        | null                 | "a disease and no publication id, with no disease found and returns null"
    }

    def getMaxoAnnotations() {
        return [
                new MaxoAnnotation("MAXO:0919323", "Surgical Procedure", "arachnodayctly", "HP:0992334", "some evidence", "some comment", "some relation", "some extension"),
                new MaxoAnnotation("MAXO:8882029", "Therapeutic Procedure", "some hp name", "HP:0222222", "some evidence", "some comment", "some relation", "some extension")
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

}
