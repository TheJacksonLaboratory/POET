package org.monarchinitiative.poet.controller.annotation

import groovy.json.JsonBuilder
import org.monarchinitiative.poet.model.requests.PhenotypeRequest
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
@WebMvcTest(PhenotypeController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class PhenotypeControllerSpec extends Specification {

    @Autowired
    private MockMvc mvc

    @SpringBean
    private AnnotationService annotationService = Stub()

    @SpringBean
    private UserService userService = Stub()

    def setup() {
    }

    @Unroll
    def "when we test get phenotype annotations"() {
        given:
        annotationService.getPhenotypeAnnotationsByDisease(_, _) >> Collections.emptyList()


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/annotation/phenotypes/${inputDiseaseId}/")).andExpect((ResultMatcher) expectedResponse);

        where:
        inputDiseaseId  | inputSort | expectedResponse                                    | desc
        "OMIM:20391892" | ""        | MockMvcResultMatchers.status().isOk()               | "test get phenotype with disease"
        "OMIM:20391891" | ""        | MockMvcResultMatchers.status().isOk()               | "test get phenotype with pub"
        ""              | ""        | MockMvcResultMatchers.status().isMethodNotAllowed() | "test get phenotype bad uri params"
    }

    @Unroll
    def "when we test create phenotype annotations they pass"() {
        given:
        annotationService.createTreatmentAnnotation(_,_) >> inputServiceResult

        expect: "an annotation state"
        def result = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/annotation/phenotypes/", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString()))
        result.andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                    | inputServiceResult | expectedResponse
        generateFakePhenotype(false) | null | MockMvcResultMatchers.status().isCreated()
    }

    @Unroll
    def "when we test create treatment annotations they fail validation"() {
        given:
        annotationService.createPhenotypeAnnotation(_,_) >> null

        expect:
        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/annotation/phenotypes/", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                    | expectedResponse
        generateFakePhenotype(true)  | MockMvcResultMatchers.status().isBadRequest()
    }


    def "when we test update phenotype annotation they pass"() {
        given:
        annotationService.updatePhenotypeAnnotation(_,_) >> null

        expect:
        mvc.perform(
                MockMvcRequestBuilders.put("/api/v1/annotation/phenotypes/?review=${inputReview}", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                 | inputReview    | expectedResponse
        generateFakePhenotype(false) | "approve"  | MockMvcResultMatchers.status().isCreated()
    }

    def "when we test update phenotype annotation they fail"() {
        given:
        annotationService.updatePhenotypeAnnotation(_,_) >> null

        expect:
        mvc.perform(
                MockMvcRequestBuilders.put("/api/v1/annotation/phenotypes/?review=${inputReview}", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                 | inputReview    | expectedResponse
        generateFakePhenotype(true) | "approve"  | MockMvcResultMatchers.status().isBadRequest()
    }

    def "when we test delete phenotype annotation they are deleted"() {
        given:
        annotationService.deletePhenotypeAnnotation(_,_) >> serviceResponse

        expect:
        mvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/annotation/phenotypes/${phenotypeId}")).andExpect((ResultMatcher) expectedResponse)

        where:
        phenotypeId | serviceResponse      | expectedResponse
        1           | true                | MockMvcResultMatchers.status().isOk()
    }

    def "when we test delete phenotype annotation they cant be found"() {
        given:
        annotationService.deletePhenotypeAnnotation(_,_) >> serviceResponse

        expect:
        mvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/annotation/phenotypes/${phenotypeId}")).andExpect((ResultMatcher) expectedResponse)

        where:
        phenotypeId | serviceResponse      | expectedResponse
        1           | false                | MockMvcResultMatchers.status().isInternalServerError()
    }

    private static generateFakePhenotype(incompleteTreatmentAnnotation) {
        if (incompleteTreatmentAnnotation) {
            return  new PhenotypeRequest(null, "fake hpo name", "HP:02138",
                    "TAS", "some description", "HP:19", "PMID:31479590",
                    "fake publication name", "MONDO:154", "mondo disease name",
                    "",
                    "",
                    "", "M", "")
        } else {
            return new PhenotypeRequest(null, "fake hpo name", "HP:0002138",
                    "TAS", "some description", "HP:0002139", "PMID:31479590",
                    "fake publication name", "MONDO:1547000", "mondo disease name",
                    "",
                    "",
                    "", "MALE", "")
        }
    }
}
