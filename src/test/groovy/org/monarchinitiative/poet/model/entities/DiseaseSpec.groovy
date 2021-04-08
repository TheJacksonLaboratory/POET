package org.monarchinitiative.poet.model.entities

import org.monarchinitiative.poet.model.entities.Disease
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class DiseaseSpec extends Specification {

    void "test disease spec constructor"(){
        given:
            def disease = new Disease(diseaseId, diseaseName)
        expect:
            disease.getDiseaseName() == diseaseName
            disease.getDiseaseId() == diseaseId
        where:
            diseaseId   |  diseaseName
            "MONDO:0910392" | "Marfan Syndrome"
            "MONDO:0093021" | "Ovarian Cancer"
    }
}
