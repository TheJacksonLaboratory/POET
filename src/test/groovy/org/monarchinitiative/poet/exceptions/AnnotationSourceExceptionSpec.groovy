package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class AnnotationSourceExceptionSpec extends Specification {

    void "test exception message #desc"() {
        given:
        def exception = new AnnotationSourceException(publicationId, diseaseId)

        expect:
        exception.getMessage() == expectedMessage

        where:
        publicationId  | diseaseId      | expectedMessage
        "PMID:0929293" | "OMIM:2883903" | "Could not find a valid disease with id OMIM:2883903 or publication with id PMID:0929293."
        "PMID:2883903" | "OMIM:0929293" | "Could not find a valid disease with id OMIM:0929293 or publication with id PMID:2883903."
        "PMID:1111111" | "OMIM:2222222" | "Could not find a valid disease with id OMIM:2222222 or publication with id PMID:1111111."
    }
}
