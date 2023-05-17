package org.monarchinitiative.poet.model.requests

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class DiseaseRequestSpec extends Specification {

    void "test treatment request constructor"() {
        given:
            def diseaseRequest = new DiseaseRequest(diseaseId, diseaseName, description, equivalentId)
            def diseaseRequest2 = diseaseRequest

        expect:
            diseaseRequest.getDiseaseName() == diseaseName
            diseaseRequest.getDiseaseId() == diseaseId
            diseaseRequest.getDescription() == description
            diseaseRequest.getEquivalentId() == equivalentId
            diseaseRequest.equals(diseaseRequest2)
            diseaseRequest.hashCode() == diseaseRequest.hashCode()

        where:
            diseaseId | equivalentId    |   diseaseName |   description
            "OMIM:029223"   | "MONDO:11111" | "very serious disease"    | "really big hands"
            "ORPHA:22301"   | "MONDO:23323" | "not so serious disease"  | "goofy smile"
    }
}
