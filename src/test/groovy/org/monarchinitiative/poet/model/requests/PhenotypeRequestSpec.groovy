package org.monarchinitiative.poet.model.requests

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class PhenotypeRequestSpec extends Specification {

    def setup(){}

    void "test phenotype request constructor"() {
        given:
            def phenotypeRequest = new PhenotypeRequest(null, hpoName, hpoId, evidence, description, onset, publicationId,
                    publicationName, diseaseId, diseaseName, modifiers, frequency, qualifier, sex, "")
            def phenotypeRequest2 = new PhenotypeRequest(null, hpoName, hpoId, evidence, description, onset, publicationId,
                    publicationName, diseaseId, diseaseName, modifiers, frequency, qualifier, sex, "")

        expect:
            phenotypeRequest.getHpoName() == hpoName
            phenotypeRequest.getHpoId() == hpoId
            phenotypeRequest.getEvidence() == evidence
            phenotypeRequest.getDescription() == description
            phenotypeRequest.getOnset() == onset
            phenotypeRequest.getPublicationId() == publicationId
            phenotypeRequest.getPublicationName() == publicationName
            phenotypeRequest.getDiseaseId() == diseaseId
            phenotypeRequest.getDiseaseName() == diseaseName
            phenotypeRequest.getModifiers() == modifiers
            phenotypeRequest.getFrequency() == frequency
            phenotypeRequest.getQualifier() == qualifier
            phenotypeRequest.getSex() == sex
            phenotypeRequest.equals(phenotypeRequest2)
            phenotypeRequest.hashCode() == phenotypeRequest2.hashCode()

        where:
        maxoId | maxoName | hpoName | hpoId | evidence | description | onset | publicationId | publicationName | diseaseId | diseaseName | modifiers | frequency | qualifier | sex
        "MAXO:9999001" | "surgical procedure" | "arachnodactyl" | "HP:0019193" | "fake evidence" | "some comment" | "some relation" | "PMID:02923" | "the best publication" | "very serious disease" | "OMIM: 029223" | "" | "10/20" | "NOT" | "MALE"
    }
}
