package org.monarchinitiative.poet.model.entities

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class DiseaseSpec extends Specification {

    void "test disease spec constructor"(){
        given:
            def disease = new Disease(diseaseId, diseaseName)
            def disease2 = new Disease(diseaseId, diseaseName)
        expect:
            disease.getDiseaseName() == diseaseName
            disease.getDiseaseId() == diseaseId
            disease.equals(disease2)
            disease.hashCode() == disease2.hashCode()

        where:
            diseaseId   |  diseaseName
            "MONDO:0910392" | "Marfan Syndrome"
            "MONDO:0093021" | "Ovarian Cancer"
    }

    void "test disease export id method"(){
        given:
        def disease = new Disease(diseaseId, diseaseName)
        disease.setEquivalentId(equivalentId)
        expect:
        disease.getExportDiseaseId() == expected

        where:
        diseaseId   |  diseaseName  | equivalentId  | expected
        "OMIM:0910392" | "Marfan Syndrome" | ""     | diseaseId
        "OMIM:0910393" | "Marfan Syndrome" | null     | diseaseId
        "OMIM:0093021" | "Ovarian Cancer" | "MONDO:001913" | equivalentId
    }
}
