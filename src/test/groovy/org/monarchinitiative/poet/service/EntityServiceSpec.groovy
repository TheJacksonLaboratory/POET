package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.exceptions.DiseaseNotFoundException
import org.monarchinitiative.poet.exceptions.DiseaseNotFoundExceptionSpec
import org.monarchinitiative.poet.model.entities.AnnotationSource
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.Publication
import org.monarchinitiative.poet.model.requests.DiseaseRequest
import org.monarchinitiative.poet.model.requests.PublicationRequest
import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.monarchinitiative.poet.repository.PublicationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = [ServiceTestConfig.class], initializers = ConfigFileApplicationContextInitializer.class)
class EntityServiceSpec extends Specification {

    @Autowired
    DiseaseRepository diseaseStub

    @Autowired
    PublicationRepository publicationStub

    @Autowired
    AnnotationSourceRepository sourceRepositoryStub

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

    void "test update disease"() {
        given:
        diseaseStub.save(_) >> null
        expect:
        entityService.updateDisease(inputDiseaseRequest) == expectedResponse
        where:
        inputDiseaseRequest | expectedResponse
        getDiseaseRequest() | true

    }

    void "test save new disease"() {
        given:
        diseaseStub.findDiseaseByDiseaseId(_) >> diseaseResponse
        sourceRepositoryStub.findDistinctByDisease(_) >> sourceResponse
        expect:
        entityService.saveNewDisease(inputDisease) == expectedResponse
        where:
        inputDisease                     | diseaseResponse | sourceResponse | expectedResponse
        getDisease()                     | inputDisease    | null           | true
        new Disease(getDiseaseRequest()) | null            | null           | true

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
        "OMIM:2039382" | null                        | 0            | "a disease that could not be found"

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

    void "update publication"() {
        given:
        publicationStub.save(_) >> inputPublication
        expect:
        entityService.updatePublication(inputPublication)
        where:
        inputPublication | _
        getPublication() | _
    }

    void "test get all publication diseases"() {
        given:
        publicationStub.findAll() >> publicationResponse
        expect:
        entityService.getAllPublications() == publicationResponse
        where:
        publicationResponse                              | _
        [getPublication(), getPublicationWithDiseases()] | _
        []                                               | _
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
        "PMID:00923232" | null                         | 0            | "a publication that does not exist"
    }

    void "test get annotation source"() {
        given:
        publicationStub.findByPublicationId(_) >> publicationResponse
        publicationStub.save(_) >> true
        diseaseStub.findDiseaseByDiseaseId(_) >> diseaseResponse
        sourceRepositoryStub.findByPublicationAndDisease(_, _) >> expectedSource
        sourceRepositoryStub.save(_) >> expectedSource
        expect:
        entityService.getAnnotationSource(inputPublicationId, inputDiseaseId) == expectedSource
        where:
        inputPublicationId | inputDiseaseId | publicationResponse | diseaseResponse | expectedSource
        "PMID:039232"      | "OMIM:0392923" | getPublication()    | getDisease()    | new AnnotationSource(publicationResponse, diseaseResponse)
        ""                 | "OMIM:0392923" | null                | getDisease()    | null
        ""                 | ""             | null                | null            | null
        "PMID:039232"      | "OMIM:0392923" | getPublication()    | getDisease()    | null


    }

    void "test create annotation source"() {
        given:
        publicationStub.findByPublicationId(_) >> publicationResponse
        diseaseStub.findDiseaseByDiseaseId(_) >> diseaseResponse
        sourceRepositoryStub.findByPublicationAndDisease(_, _) >> sourceRepositoryResponse
        sourceRepositoryStub.save(_) >> expectedSource
        expect:
        entityService.createAnnotationSource(publicationRequest) == expectedSource
        where:
        publicationRequest      | publicationResponse | diseaseResponse | sourceRepositoryResponse | expectedSource
        getPublicationRequest() | getPublication()    | getDisease()    | null                     | new AnnotationSource(getPublication(), getDisease())
        getPublicationRequest() | null                | getDisease()    | null                     | new AnnotationSource(getPublication(), getDisease())
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

    def getPublicationRequest() {
        return new PublicationRequest(getPublication(), getDisease())
    }

    def getDiseaseRequest() {
        return new DiseaseRequest("OMIM:000293", "disease name", "desc", "MONDO:9032932")
    }
}
