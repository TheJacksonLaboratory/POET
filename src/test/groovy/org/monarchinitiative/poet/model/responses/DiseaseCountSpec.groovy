package org.monarchinitiative.poet.model.responses

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class DiseaseCountSpec extends Specification {

    void "test search response model as #desc"() {
        given:
        def diseaseCountResponse = new DiseaseCount(diseaseId, diseaseName, activityCount)
        expect:
        diseaseCountResponse.getDiseaseId() == diseaseId
        diseaseCountResponse.getDiseaseName() == diseaseName
        diseaseCountResponse.getDiseaseCount() == activityCount

        where:
        diseaseId       | diseaseName         | activityCount     | desc
        "OMIM:10930495" | "a first terrible disease" | 60 | "a publication"
        "OMIM:90992932" | "a terrible disease"  | 0    | "a disease"
        ""              | ""                    | -60            | "all empties"
    }
}
