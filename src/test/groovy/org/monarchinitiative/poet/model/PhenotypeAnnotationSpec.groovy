package org.monarchinitiative.poet.model

import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class PhenotypeAnnotationSpec extends Specification {

    void "test phenotype annotation empty constructor"() {
        given:
        def phenotypeAnnotation = new PhenotypeAnnotation()
        expect:
        phenotypeAnnotation != null
    }

    void "test phenotype annotation constructor values"() {
        given:
        def phenotypeAnnotation = new PhenotypeAnnotation(hpoId, hpoName, onset, evidence, modifier, frequency,
                qualifier, description, sex)
        expect:
        phenotypeAnnotation.getHpoId() == hpoId
        phenotypeAnnotation.getHpoName() == hpoName
        phenotypeAnnotation.getDescription() == description
        phenotypeAnnotation.getSex() == sex
        phenotypeAnnotation.getQualifier() == qualifier
        phenotypeAnnotation.getFrequency() == frequency
        phenotypeAnnotation.getOnset() == onset
        phenotypeAnnotation.getModifier() == modifier
        phenotypeAnnotation.getEvidence() == evidence

        where:
        hpoId        | hpoName       | maxoId         | maxoName         | description        | evidence | sex      | qualifier | frequency | onset         | modifier
        "HP:0000919" | "some term"   | "MAXO:0092313" | "some treatment" | "this description" | "TAS"    | "MALE"   | "NOT"     | "10/20"   | "HP:009193"   | ""
        "HP:0000920" | "some term 2" | "MAXO:0092314" | "some treatment" | ""                 | "TAS"    | "FEMALE" |   ""      | "20%"     | ""            | "HP:0092039"
    }

}
