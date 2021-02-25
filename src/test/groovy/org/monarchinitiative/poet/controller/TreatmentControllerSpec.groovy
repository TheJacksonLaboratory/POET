package org.monarchinitiative.poet.controller

import groovy.json.JsonBuilder
import org.monarchinitiative.poet.controller.annotation.TreatmentController
import org.monarchinitiative.poet.service.AnnotationService
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

    def setup(){
    }

    @Unroll
    def "when we test get maxo annotations"(){
        given:
        annotationService.getTreatmentAnnotations(_,_,_) >> Collections.emptyList()


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/annotation/maxo/${inputDiseaseId}/${inputPublicationId}")).andExpect((ResultMatcher) expectedResponse);

        where:
        inputDiseaseId | inputPublicationId | inputSort | expectedResponse                               | desc
        "OMIM:20391892" | "PMID:20391892"   | ""        | MockMvcResultMatchers.status().isOk()        | "test get maxo with pub"
        "OMIM:20391891" | ""                | ""        | MockMvcResultMatchers.status().isOk()         | "test get maxo with pub"
        ""              | ""                | ""        | MockMvcResultMatchers.status().isMethodNotAllowed()    | "test get maxo bad  uri params"
    }

    @Unroll
    def "when we test create maxo annotations"(){
        given:
        annotationService.createTreatmentAnnotation(_,_) >> inputServiceResult

        expect: "an annotation state"
        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/annotation/maxo/", inputBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(inputBody).toPrettyString())
        ).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                         | inputServiceResult    |   expectedResponse
        generateFakeMaxo(false)           | true                  |   MockMvcResultMatchers.status().isCreated()
        generateFakeMaxo(true)            | false                 |   MockMvcResultMatchers.status().isInternalServerError()
        [:]                               | false                 |   MockMvcResultMatchers.status().isInternalServerError()

    }

    private static generateFakeMaxo(incompleteMaxoAnnotation) {
        if(incompleteMaxoAnnotation){
            return [comment: "daf",
                    diseaseId: "OMIM:154700",
                    diseaseName: "Marfan Syndrome",
                    evidence: "IEA",
                    hpoId: "HP:0002138"]
        } else {
            return [comment: "daf",
                        diseaseId: "OMIM:154700",
                        diseaseName: "Marfan Syndrome",
                        evidence: "IEA",
                        hpoId: "HP:0002138",
                        hpoName: "Subarachnoid hemorrhage",
                        maxoId: "MAXO:0000004",
                        maxoName: "surgical procedure",
                        publicationId: "PMID:31479590",
                        publicationName: "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.",
                        relation: "PREVENTS"]
        }
    }
}
