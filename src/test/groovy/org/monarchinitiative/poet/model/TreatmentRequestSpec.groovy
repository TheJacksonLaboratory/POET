package org.monarchinitiative.poet.model

import org.monarchinitiative.poet.model.requests.TreatmentRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class TreatmentRequestSpec extends Specification {

    def setup(){}

    void "test maxo request constructor"() {
        given:
        def treatmentRequest = new TreatmentRequest(null,
                maxoId, maxoName, hpoName, hpoId, evidence, comment, relation, extensionId, extensionLabel, publicationId, publicationName, diseaseId, diseaseName
        ) {}

        expect:
        treatmentRequest.getMaxoId() == maxoId
        treatmentRequest.getMaxoName() == maxoName
        treatmentRequest.getHpoName() == hpoName
        treatmentRequest.getHpoId() == hpoId
        treatmentRequest.getEvidence() == evidence
        treatmentRequest.getComment() == comment
        treatmentRequest.getRelation() == relation
        treatmentRequest.getExtensionId() == extensionId
        treatmentRequest.getExtensionLabel() == extensionLabel
        treatmentRequest.getPublicationId() == publicationId
        treatmentRequest.getPublicationName() == publicationName
        treatmentRequest.getDiseaseName() == diseaseName
        treatmentRequest.getDiseaseId() == diseaseId

        where:
        maxoId | maxoName | hpoName | hpoId | evidence | comment | relation | extensionId | extensionLabel | publicationId | publicationName | diseaseId | diseaseName
        "MAXO:9999001" | "surgical procedure" | "arachnodactyl" | "HP:0019193" | "fake evidence" | "some comment" | "some relation" | "some extension id" |  "some extension label" | "PMID:02923" | "the best publication" | "very serious disease" | "OMIM: 029223"
    }
}
