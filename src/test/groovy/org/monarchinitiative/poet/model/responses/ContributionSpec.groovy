package org.monarchinitiative.poet.model.responses


import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class ContributionSpec extends Specification {

    def setup() {}

    void "test constructor for contribution #desc"() {
        given:
        def contribution = new Contribution(inputMaxoCount, inputHpoCount);

        expect:
        contribution.getTreatment() == expectedCounts[0]
        contribution.getPhenotype() == expectedCounts[1]

        where:
        inputMaxoCount | inputHpoCount | expectedCounts | desc
        0              | 1             | [0, 1]     | "normal counts"
        90             | 29            | [90, 29]   | "normal counts"
        99             | 299           | [99, 299]   | "one null should return 0"
    }
}
