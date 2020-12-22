package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.Publication
import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.monarchinitiative.poet.repository.PublicationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = [ServiceTestConfig.class])
class SearchServiceSpec extends Specification {
    @Autowired
    DiseaseRepository diseaseStub;

    @Autowired
    PublicationRepository publicationStub;

    @Autowired
    AnnotationSourceRepository annotationStub;

    @Autowired
    SearchService searchService


    void "test search publication and disease #desc"() {
        given:
        publicationStub.findByPublicationIdStartingWithOrPublicationNameContainingIgnoreCase(_, _) >> publicationResponse
        diseaseStub.findDiseaseByDiseaseNameContainingIgnoreCaseOrDiseaseIdContainingIgnoreCase(_, _) >> diseaseResponse
        def result = searchService.searchPublicationAndDisease(inputQuery)
        expect:
        result.size() == expectedSize

        where:
        inputQuery | publicationResponse    | diseaseResponse    | expectedSize | desc
        "ha"       | getFewPublications()   | getFewDiseases()   | 5            | "expect 5 response"
        "meh"      | getSinglePublication() | getFewDiseases()   | 4            | "expect 4 response"
        "pub"      | getSinglePublication() | getSingleDisease() | 2            | "expect 2 response"
        "dise"     | getEmptyList()         | getSingleDisease() | 1            | "expect 1 response"
        ""         | getEmptyList()         | getEmptyList()     | 0            | "expect 0 response"

    }

    def getEmptyList() {
        return []
    }

    def getFewPublications() {
        return [
                new Publication("PMID:00923232", "My first publication", "12/19/2020", "Test User"),
                new Publication("PMID:00923245", "My second publication", "12/20/2020", "Test User")
        ]
    }

    def getSinglePublication() {
        return [new Publication("PMID:00923232", "My first publication", "12/19/2020", "Test User")]
    }

    def getFewDiseases() {
        return [
                new Disease("OMIM:9820939", "Some bad disease"),
                new Disease("OMIM:8290392", "Some really bad disease"),
                new Disease("OMIM:2091938", "Some really really bad disease")
        ]
    }

    def getSingleDisease() {
        return [new Disease("OMIM:2091938", "Some really really bad disease")]
    }
}
