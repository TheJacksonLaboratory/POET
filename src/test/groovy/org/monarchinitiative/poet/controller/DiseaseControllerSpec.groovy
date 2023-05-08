package org.monarchinitiative.poet.controller

import groovy.json.JsonBuilder
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.Publication
import org.monarchinitiative.poet.model.requests.DiseaseRequest
import org.monarchinitiative.poet.service.EntityService
import org.monarchinitiative.poet.service.StatisticsService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll

@AutoConfigureMockMvc
@WebMvcTest(DiseaseController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class DiseaseControllerSpec extends Specification {

    @Autowired
    private MockMvc mvc

    @SpringBean
    private EntityService entityService = Stub()

    @SpringBean
    private StatisticsService statisticsService = Stub()

    @Unroll
    def "when we test get disease #desc"() {
        given:
        entityService.getDisease(_) >> inputDisease


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/entity/disease/${inputDiseaseId}/")).andExpect((ResultMatcher) expectedResponse)

        where:
        inputDisease                                  | inputDiseaseId | expectedResponse                                    | desc
        new Disease("OMIM:154700", "Marfan Syndrome") | "OMIM:154700"  | MockMvcResultMatchers.status().isOk()               | "test get disease by id with pub"
        null                                          | "OMIM:0091920" | MockMvcResultMatchers.status().isNotFound()         | "test get disease by id find nothing"
        null                                          | ""             | MockMvcResultMatchers.status().isMethodNotAllowed() | "test get disease by nothing find nothing"
    }

    @Unroll
    def "when we test get disease publications #desc"() {
        given:
        entityService.getDiseasePublications(_ as String) >> inputPublications


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/entity/disease/${inputDiseaseId}/publications")).andExpect((ResultMatcher) expectedResponse)

        where:
        inputPublications                                   | inputDiseaseId | expectedResponse                      | desc
        [new Publication(
                "PMID:1337998",
                "A great novel about the poet application",
                "A great date on which it was published",
                "Michael A Gargano")]                       | "OMIM:154700"  | MockMvcResultMatchers.status().isOk() | "test get disease by id with pub"
        []                                                  | "OMIM:0091920" | MockMvcResultMatchers.status().isOk() | "test get disease by id find nothing"
        []                                                  | ""             | MockMvcResultMatchers.status().isOk() | "test get disease by nothing find nothing"
    }

    @Unroll
    def "when we test update disease #desc"() {
        given:
        entityService.updateDisease(_ as DiseaseRequest) >> shouldUpdate


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/entity/disease/", inputBody).contentType(MediaType.APPLICATION_JSON)
                .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)
        where:
        inputBody                              | shouldUpdate | expectedResponse                                       | desc
        diseaseRequestGenerator("OMIM:154700") | true         | MockMvcResultMatchers.status().isOk()                  | "test save new disease"
        diseaseRequestGenerator("ORPHA:02893") | false        | MockMvcResultMatchers.status().isInternalServerError() | "test save new disease and fails"
        ""                                     | false        | MockMvcResultMatchers.status().isBadRequest()          | "test save new disease bad request and fails"
    }

    @Unroll
    def "when we test new disease creation #desc"() {
        given:
        entityService.saveNewDisease(_ as Disease) >> shouldSave


        expect:
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/entity/disease/", inputBody).contentType(MediaType.APPLICATION_JSON)
                .content(new JsonBuilder(inputBody).toPrettyString())).andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                              | shouldSave | expectedResponse                                       | desc
        diseaseRequestGenerator("OMIM:154700") | true       | MockMvcResultMatchers.status().isOk()                  | "test save new disease"
        diseaseRequestGenerator("OMIM:154700") | false      | MockMvcResultMatchers.status().isInternalServerError() | "test save new disease and fails"
        ""                                     | false      | MockMvcResultMatchers.status().isBadRequest()          | "test save new disease bad request and fails"
    }


    def diseaseRequestGenerator(String id) {
        def diseases = ["OMIM:154700": new DiseaseRequest("OMIM:154700", "Marfan Syndrom", "A terrible disease", "MONDO:82933"),
                        "ORPHA:02893": new DiseaseRequest("ORPHA:02893", "second terrible disease", "fake descriptor", "MONDO:09293000")
        ]
        return diseases[id]
    }
}
