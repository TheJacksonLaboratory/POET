package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class AnnotationSourceExceptionSpec extends Specification {

    void "test exception message not found"() {
        given:
        def exception = AnnotationSourceException.bothNotFound(publicationId, diseaseId)

        expect:
        exception.getMessage() == expectedMessage

        where:
        publicationId  | diseaseId      | expectedMessage
        "PMID:0929293" | "OMIM:2883903" | "Could not find a valid disease with id OMIM:2883903 or publication with id PMID:0929293."
        "PMID:2883903" | "OMIM:0929293" | "Could not find a valid disease with id OMIM:0929293 or publication with id PMID:2883903."
        "PMID:1111111" | "OMIM:2222222" | "Could not find a valid disease with id OMIM:2222222 or publication with id PMID:1111111."
    }


    void "test exception message already exists"() {
        given:
        def exception = AnnotationSourceException.exists(publicationId, diseaseId)

        expect:
        exception.getMessage() == expectedMessage

        where:
        publicationId  | diseaseId      | expectedMessage
        "PMID:0929293" | "OMIM:2883903" | "Annotation source exists for disease OMIM:2883903 and publication PMID:0929293."
        "PMID:2883903" | "OMIM:0929293" | "Annotation source exists for disease OMIM:0929293 and publication PMID:2883903."
        "PMID:1111111" | "OMIM:2222222" | "Annotation source exists for disease OMIM:2222222 and publication PMID:1111111."
    }

    void "test exception message for disease only"() {
        given:
        def exception = AnnotationSourceException.diseaseNotFound(diseaseId)

        expect:
        exception.getMessage() == expectedMessage

        where:
        diseaseId      | expectedMessage
        "OMIM:2883903" | "Could not find a valid disease with id OMIM:2883903."
        "OMIM:0929293" | "Could not find a valid disease with id OMIM:0929293."
        "OMIM:2222222" | "Could not find a valid disease with id OMIM:2222222."
    }

    void "test exception message for disease and exists #desc"() {
        given:
        def exception = AnnotationSourceException.diseaseNotFound(diseaseId)

        expect:
        exception.getMessage() == expectedMessage

        where:
        diseaseId      | expectedMessage
        "OMIM:2883903" | "Could not find a valid disease with id OMIM:2883903."
        "OMIM:0929293" | "Could not find a valid disease with id OMIM:0929293."
        "OMIM:2222222" | "Could not find a valid disease with id OMIM:2222222."
    }
}
