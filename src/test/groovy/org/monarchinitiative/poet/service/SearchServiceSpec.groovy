package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
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
    AnnotationSourceRepository annotationStub;

    @Autowired
    SearchService searchService


    void "test search publication and disease #desc"() {
        given:
        diseaseStub.findDiseaseByDiseaseNameContainingIgnoreCaseOrDiseaseIdContainingIgnoreCase(_ as String, _ as String) >> diseaseResponse
        def result = searchService.searchDisease(inputQuery)
        expect:
        result.size() == expectedSize

        where:
        inputQuery | diseaseResponse        | expectedSize | desc
        "really"   | getFewDiseases()[1..2] | 2            | "expect 2 response"
        "dise"     | getFewDiseases()       | 3            | "expect 3 response"
        "bad"      | getSingleDisease()     | 1            | "expect 2 response"
        "dise"     | getSingleDisease()     | 1            | "expect 1 response"
        ""         | getEmptyList()         | 0            | "expect 0 response"

    }

    def getEmptyList() {
        return []
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
