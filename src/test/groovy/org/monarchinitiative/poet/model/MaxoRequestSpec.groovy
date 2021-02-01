package org.monarchinitiative.poet.model

import org.monarchinitiative.poet.model.requests.MaxoRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class MaxoRequestSpec extends Specification {

    def setup(){}

    void "test maxo request constructor"() {
        given:
        def maxoRequest = new MaxoRequest(null,
                maxoId, maxoName, hpoName, hpoId, evidence, comment, relation, extension, publicationId, publicationName, diseaseId, diseaseName
        ) {}

        expect:
        maxoRequest.getMaxoId() == maxoId
        maxoRequest.getMaxoName() == maxoName
        maxoRequest.getHpoName() == hpoName
        maxoRequest.getHpoId() == hpoId
        maxoRequest.getEvidence() == evidence
        maxoRequest.getComment() == comment
        maxoRequest.getRelation() == relation
        maxoRequest.getExtension() == extension
        maxoRequest.getPublicationId() == publicationId
        maxoRequest.getPublicationName() == publicationName
        maxoRequest.getDiseaseName() == diseaseName
        maxoRequest.getDiseaseId() == diseaseId

        where:
        maxoId | maxoName | hpoName | hpoId | evidence | comment | relation | extension | publicationId | publicationName | diseaseId | diseaseName
        "MAXO:9999001" | "surgical procedure" | "arachnodactyl" | "HP:0019193" | "fake evidence" | "some comment" | "some relation" | "some extension" | "PMID:02923" | "the best publication" | "very serious disease" | "OMIM: 029223"
    }
}
