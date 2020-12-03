package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.model.entities.AnnotationSource
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.Publication
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
class EntityServiceSpec extends Specification {

    @Autowired
    DiseaseRepository diseaseStub

    @Autowired
    PublicationRepository publicationStub

    @Autowired
    EntityService entityService


    void "test get disease #desc"() {
        given:
        diseaseStub.findDiseaseByDiseaseId(_) >> diseaseResponse
        def result = entityService.getDisease(inputId)
        expect:
        result.getDiseaseId() == diseaseResponse.diseaseId
        result.getDiseaseName() == diseaseResponse.diseaseName

        where:
        inputId        | diseaseResponse | desc
        "OMIM:2039382" | getDisease()    | "a regular disease fetch"
    }


    void "test get disease publications #desc"() {
        given:
        diseaseStub.findDiseaseByDiseaseId(_) >> diseaseResponse
        def result = entityService.getDiseasePublications(inputId)

        expect:
        result.size() == expectedSize

        where:
        inputId        | diseaseResponse             | expectedSize | desc
        "OMIM:2039382" | getDiseaseWithPublication() | 1            | "a disease with 1 publication"
        "OMIM:2039382" | getDisease()                | 0            | "a disease with 0 publications"

    }

    void "test get publication"() {
        given:
        publicationStub.findByPublicationId(_) >> publicationResponse
        def result = entityService.getPublication(inputId)
        expect:
        result.getPublicationId() == publicationResponse.publicationId
        result.getPublicationName() == publicationResponse.publicationName

        where:
        inputId         | publicationResponse | desc
        "PMID:00923232" | getPublication()    | "a regular publication fetch"
    }

    void "test get publication diseases #desc"() {
        given:
        publicationStub.findByPublicationId(_) >> publicationResponse
        def result = entityService.getPublicationDiseases(inputId)

        expect:
        result.size() == expectedSize

        where:
        inputId         | publicationResponse          | expectedSize | desc
        "PMID:00923232" | getPublicationWithDiseases() | 1            | "a publication with 1 disease"
        "PMID:00923232" | getPublication()             | 0            | "a publication with 0 diseases"
    }

    def getPublication() {
        new Publication("PMID:00923232", "My first publication", "12/19/2020", "Test User", [])
    }

    def getPublicationWithDiseases() {
        def temp = new Publication("PMID:00923232", "My first publication", "12/19/2020", "Test User")
        def annotationSource = new AnnotationSource(temp, getDisease())
        return new Publication("PMID:00923232", "My first publication", "12/19/2020", "Test User", [annotationSource])
    }

    def getDiseaseWithPublication() {
        def temp = new Disease("OMIM:2039382", "a terrible disease")
        def annotationSource = new AnnotationSource(getPublication(), temp)
        return new Disease("OMIM:2039382", "a terrible disease", [annotationSource])
    }

    def getDisease() {
        return new Disease("OMIM:2039382", "a terrible disease", [])
    }
}
