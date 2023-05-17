package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class ExportExceptionSpec extends Specification {
    void "test export exception failure message"(){
        given:
        def exception = new ExportException(ontology)

        expect:
        exception.getMessage() == expectedMessage

        where:
        ontology      | expectedMessage
        "maxo"        | "Could not generate export file for maxo"
        "hpo"         | "Could not generate export file for hpo"
    }
}
