package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class DuplicationAnnotationExceptionSpec extends Specification {

    void "test duplication annotation exception message"(){
        given:
        def exception = new DuplicateAnnotationException(inputType, inputDiseaseName)

        expect:
        exception.getMessage() == expectedMessage

        where:
        inputType | inputDiseaseName      | expectedMessage
        "treatment"| "some disease 1" | "Duplicate treatment annotation already exists for some disease 1."
        "phenotype"| "some disease 2" | "Duplicate phenotype annotation already exists for some disease 2."
        "treatment"| "some disease 3" | "Duplicate treatment annotation already exists for some disease 3."
    }
}
