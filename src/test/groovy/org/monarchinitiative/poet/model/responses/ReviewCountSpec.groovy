package org.monarchinitiative.poet.model.responses

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class ReviewCountSpec extends Specification {

    void "test search response model as #desc"() {
        given:
        def reviewCountResponse = new ReviewCount(diseaseId, diseaseName, count, type)
        expect:
        reviewCountResponse.getDiseaseId() == diseaseId
        reviewCountResponse.getDiseaseName() == diseaseName
        reviewCountResponse.getCount() == count
        reviewCountResponse.getType() == type

        where:
        diseaseId       | diseaseName                | count | type        | desc
        "OMIM:10930495" | "a first terrible disease" | 60l   | "TREATMENT" | "a publication"
        "OMIM:90992932" | "a terrible disease"       | 0l    | "PHENOTYPE" | "a disease"
        ""              | ""                         | -60l  | "UNKNOWN"   | "all empties"
    }
}
