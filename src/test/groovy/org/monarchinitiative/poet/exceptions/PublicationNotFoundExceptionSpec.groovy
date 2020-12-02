package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class PublicationNotFoundExceptionSpec extends Specification {

    def setup() {}

    void "test publication not found exception message"() {
        given:
        def exception = new PublicationNotFoundException(publicationId)

        expect:
        exception.getMessage() == expectedMessage

        where:
        publicationId  | expectedMessage
        "PMID:0929293" | "Could not find publication: PMID:0929293"
        "PMID:2883903" | "Could not find publication: PMID:2883903"
        "PMID:1111111" | "Could not find publication: PMID:1111111"
    }
}
