package org.monarchinitiative.poet.model.responses


import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class AnnotationCountSpec extends Specification {

    void "test annotation count constructor"(){
        given:
            def annotationCount = new AnnotationCount(inputPhenotypeCount, inputTreatmentCount)
        expect:
            annotationCount.getPhenotypeCount() == expectedPhenotypeCount
            annotationCount.getTreatmentCount() == expectedTreatmentCount
        where:
            inputPhenotypeCount     |   inputTreatmentCount | expectedPhenotypeCount    |   expectedTreatmentCount
            1                       |   1                   | 1                         |   1
            null                    |   10                  | 0                         |   10
            93                      |   null                | 93                        |   0

    }
}
