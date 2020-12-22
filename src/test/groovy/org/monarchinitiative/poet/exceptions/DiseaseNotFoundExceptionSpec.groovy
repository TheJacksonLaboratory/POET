package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class DiseaseNotFoundExceptionSpec extends Specification {

    def setup(){}

    void "test disease not found exception message"(){
        given:
        def exception = new DiseaseNotFoundException(diseaseId)

        expect:
        exception.getMessage() == expectedMessage

        where:
        diseaseId      | expectedMessage
        "OMIM:2883903" | "Could not find disease: OMIM:2883903"
        "OMIM:0929293" | "Could not find disease: OMIM:0929293"
        "OMIM:2222222" | "Could not find disease: OMIM:2222222"
    }
}
