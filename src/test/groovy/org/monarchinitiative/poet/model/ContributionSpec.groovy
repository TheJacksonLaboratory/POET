package org.monarchinitiative.poet.model

import org.monarchinitiative.poet.model.response.Contribution
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class ContributionSpec extends Specification {

    def setup() {}

    void "test constructor for contribution #desc"() {
        given:
        def contribution = new Contribution(inputMaxoCount, inputHpoCount, inputPhenopacketCount);

        expect:
        contribution.getTreatment() == expectedCounts[0]
        contribution.getPhenotype() == expectedCounts[1]
        contribution.getPhenopackets() == expectedCounts[2]

        where:
        inputMaxoCount | inputHpoCount | inputPhenopacketCount | expectedCounts | desc
        0              | 1             | 19                    | [0, 1, 19]     | "normal counts"
        90             | 29            | 56                    | [90, 29, 56]   | "normal counts"
        99             | 299           | null                  | [99, 299, 0]   | "one null should return 0"
        null           | null          | null                  | [0, 0, 0]      | "all null should be 0s"
    }
}
