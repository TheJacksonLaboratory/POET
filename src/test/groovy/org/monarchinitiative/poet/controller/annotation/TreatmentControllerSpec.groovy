package org.monarchinitiative.poet.controller.annotation

import groovy.json.JsonBuilder
import org.monarchinitiative.poet.model.requests.TreatmentRequest
import org.monarchinitiative.poet.service.AnnotationService
import org.monarchinitiative.poet.service.UserService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll


@AutoConfigureMockMvc
@WebMvcTest(TreatmentController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class TreatmentControllerSpec extends Specification {

    @Autowired
    private MockMvc mvc

    @SpringBean
    private AnnotationService annotationService = Stub()

    @SpringBean
    private UserService userService = Stub()

    def setup() {
    }

    @Unroll
    def "when we test get treatment annotations"() {
        given:
        annotationService.getTreatmentAnnotationsByDisease(_) >> Collections.emptyList()


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/annotation/treatments/${inputDiseaseId}")).andExpect((ResultMatcher) expectedResponse);

        where:
        inputDiseaseId  | expectedResponse                                    | desc
        "OMIM:20391892" | MockMvcResultMatchers.status().isOk()               | "test get maxo with disease"
        "OMIM:20391891" | MockMvcResultMatchers.status().isOk()               | "test get maxo with disease"
        ""              | MockMvcResultMatchers.status().isMethodNotAllowed() | "test get maxo bad  uri params"
    }

    @Unroll
    def "when we test create treatment annotations they pass"() {
        given:
        annotationService.createTreatmentAnnotation(_,_) >> inputServiceResult

        expect: "an annotation state"
        def result = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/annotation/treatments/", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString()))
        result.andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                    | inputServiceResult | expectedResponse
        generateFakeTreatment(false) | null                | MockMvcResultMatchers.status().isCreated()
    }

    @Unroll
    def "when we test create treatment annotations they fail validation"() {
        given:
        annotationService.createTreatmentAnnotation(_,_) >> null

        expect:
        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/annotation/treatments/", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                    | expectedResponse
        generateFakeTreatment(true)  | MockMvcResultMatchers.status().isBadRequest()
    }

    def "when we test update treatment annotation they pass"() {
        given:
        annotationService.updateTreatmentAnnotation(_,_) >> null

        expect:
        mvc.perform(
                MockMvcRequestBuilders.put("/api/v1/annotation/treatments/?review=${inputReview}", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                 | inputReview    | expectedResponse
        generateFakeTreatment(false) | "approve"  | MockMvcResultMatchers.status().isCreated()
    }

    def "when we test update treatment annotation they fail"() {
        given:
        annotationService.updateTreatmentAnnotation(_,_) >> null

        expect:
        mvc.perform(
                MockMvcRequestBuilders.put("/api/v1/annotation/treatments/?review=${inputReview}", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                 | inputReview    | expectedResponse
        generateFakeTreatment(true) | "approve"  | MockMvcResultMatchers.status().isBadRequest()
    }

    def "when we test delete treatment annotation they are deleted"() {
        given:
        annotationService.deleteTreatmentAnnotation(_,_) >> serviceResponse

        expect:
        mvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/annotation/treatments/${treatmentId}")).andExpect((ResultMatcher) expectedResponse)

        where:
        treatmentId | serviceResponse      | expectedResponse
        1           | true                | MockMvcResultMatchers.status().isOk()
    }

    def "when we test delete treatment annotation they cant be found"() {
        given:
        annotationService.deleteTreatmentAnnotation(_,_) >> serviceResponse

        expect:
        mvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/annotation/treatments/${treatmentId}")).andExpect((ResultMatcher) expectedResponse)

        where:
        treatmentId | serviceResponse      | expectedResponse
        1           | false                | MockMvcResultMatchers.status().isInternalServerError()
    }

    private static generateFakeTreatment(incompleteTreatmentAnnotation) {
        if (incompleteTreatmentAnnotation) {
            return [comment    : "daf",
                    diseaseId  : "OMIM:154700",
                    diseaseName: "Marfan Syndrome",
                    evidence   : "IEA",
                    hpoId      : "HP:0002138"]
        } else {
            return new TreatmentRequest(null, "MAXO:0000004", "surgical procedure",
                    "Subarachnoid hemorrhage", "HP:0002138", "IEA", "daf", "PREVENTS", "CHEBI:1039000", "caffeine",
                    "PMID:31479590",
                    "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.",
                    "MONDO:1547000", "Marfan Syndrome", "")
        }
    }
}
