package org.monarchinitiative.poet.model.entities

import org.monarchinitiative.poet.model.enumeration.AnnotationStatus
import org.monarchinitiative.poet.model.enumeration.Relation
import org.monarchinitiative.poet.model.requests.TreatmentRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class TreatmentAnnotationSpec extends Specification {
    def setup() {

    }

    void "test treatment annotation empty constructor"() {
        given:
            def treatmentAnnotation = new TreatmentAnnotation()
        expect:
            treatmentAnnotation != null
    }

    void "test treatment annotation constructor values"() {
        given:
            def treatmentAnnotation = new TreatmentAnnotation(maxoId, maxoName, hpoName, hpoId, evidence, comment,
                    relation, extensionId, extensionLabel)
        expect:
            treatmentAnnotation.getHpoId() == hpoId
            treatmentAnnotation.getHpoName() == hpoName
            treatmentAnnotation.getMaxoId() == maxoId
            treatmentAnnotation.getMaxoName() == maxoName
            treatmentAnnotation.getComment() == comment
            treatmentAnnotation.getExtensionLabel() == extensionLabel
            treatmentAnnotation.getExtensionId() == extensionId
            treatmentAnnotation.getRelation().toString() == relation
            treatmentAnnotation.getEvidence() == evidence

        where:
        hpoId        | hpoName       | maxoId         | maxoName         | comment        | extensionId      | extensionLabel | relation   | evidence
        "HP:0000919" | "some term"   | "MAXO:0092313" | "some treatment" | "this comment" | "CHEBI:00919392" | "beta-blocker" | "PREVENTS" | "TAS"
        "HP:0000920" | "some term 2" | "MAXO:0092314" | "some treatment" | ""             | ""               | ""             | "PREVENTS" | "TAS"
    }

    void "test treatment annotation constructor phenotype request"(){
        given:
            def treatmentAnnotation = new TreatmentAnnotation(treatmentRequest, annotationSource, annotationStatus, new User())
        expect:
            treatmentAnnotation.getHpoId() == treatmentRequest.getHpoId()
            treatmentAnnotation.getHpoName() == treatmentRequest.getHpoName()
            treatmentAnnotation.getMaxoId() == treatmentRequest.getMaxoId()
            treatmentAnnotation.getMaxoName() == treatmentRequest.getMaxoName()
            treatmentAnnotation.getComment() == treatmentRequest.getComment()
            treatmentAnnotation.getExtensionLabel() == treatmentRequest.getExtensionLabel()
            treatmentAnnotation.getExtensionId() == treatmentRequest.getExtensionId()
            treatmentAnnotation.getRelation().toString() == treatmentRequest.getRelation()
            treatmentAnnotation.getEvidence() == treatmentRequest.getEvidence()
            treatmentAnnotation.getStatus() == annotationStatus
        where:
        treatmentRequest            | annotationSource          | annotationStatus
        getTreatmentRequest(0)      | getAnnotationSource(0, 0) | AnnotationStatus.ACCEPTED
        getTreatmentRequest(1)      | getAnnotationSource(0, 0) | AnnotationStatus.NEEDS_WORK
        getTreatmentRequest(2)      | getAnnotationSource(1, 1) | AnnotationStatus.UNDER_REVIEW
    }

    def getTreatmentRequest(int i){
        return [
            new TreatmentRequest(null, "MAXO:0000004", "surgical procedure",
                "Subarachnoid hemorrhage", "HP:0002138", "IEA", "daf", "PREVENTS", "CHEBI:1039000", "caffeine",
                "PMID:31479590",
                "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.",
                "MONDO:1547000", "Marfan Syndrome", ""),
            new TreatmentRequest(null, "MAXO:0000004", "surgical procedure",
                "Subarachnoid hemorrhage", "HP:0002138", "TAS", "", "TREATS", "", "",
                "PMID:31479590",
                "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.",
                "MONDO:1547000", "Marfan Syndrome", "some message"),
         new TreatmentRequest(null, "MAXO:0000006", "surgical analysis",
                "arachnoid hemorrhage", "HP:0002240", "TAS", "", "PREVENTS", "CHEBI:1039000", "beta blocker",
                "PMID:49304995",
                "some important publication name",
                "MONDO:1547001", "Marfan Syndrome type 2", "")

        ][i]
    }

    def getAnnotationSource(int i, int k){
        return [
            new AnnotationSource(getPublication(i) as Publication, getDisease(k) as Disease)
        ][i]
    }

    def getPublication(int i){
        return [
                new Publication("PMID:31479590", "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.", "date", "Robinson P"),
                new Publication("PMID:49304995", "some important publication name", "some date", "Gargano M")
        ][i]
    }

   def getDisease(int i) {
        return [
                new Disease("MONDO:1547000", "Marfan Syndrome"),
                new Disease("MONDO:1547001", "Marfan Syndrome type 2")
        ][i]
    }
}
