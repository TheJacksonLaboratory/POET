package org.monarchinitiative.poet.controller

import groovy.json.JsonBuilder
import org.monarchinitiative.poet.model.entities.AnnotationSource
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.Publication
import org.monarchinitiative.poet.service.EntityService
import org.monarchinitiative.poet.service.UserService
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
@WebMvcTest(PublicationController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class PublicationControllerSpec extends Specification {
    @Autowired
    private MockMvc mvc

    @SpringBean
    private EntityService entityService = Stub()

    @SpringBean
    private UserService userService = Stub()

    def setup() {
    }

    @Unroll
    def "when we test get single publication"() {
        given:
        entityService.getPublication(_) >> returnedPublication


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/entity/publication/${inputPublicationId}"))
                .andExpect((ResultMatcher) expectedResponse)

        where:
        returnedPublication                  | inputPublicationId | expectedResponse                                    | desc
        publicationGenerator("PMID:1337998") | "PMID:1337998"     | MockMvcResultMatchers.status().isOk()               | "test get publication by id with pub"
        publicationGenerator("PMID:9999990") | "PMID:9999990"     | MockMvcResultMatchers.status().isOk()               | "test get disease by id find nothing"
        publicationGenerator("PMID:9999990") | "PMID:9999990"     | MockMvcResultMatchers.status().isOk()               | "test get disease by id find nothing"
        null                                 | ""                 | MockMvcResultMatchers.status().isMethodNotAllowed() | "test get disease by nothing should fail"
        ""                                   | ""                 | MockMvcResultMatchers.status().isMethodNotAllowed() | "test get disease by nothing should fail"
    }

    @Unroll
    def "when we test get publication diseases"() {
        given:
        entityService.getDiseasePublications(_ as String) >> returnedPublication


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/entity/publication/${inputPublicationId}/diseases"))
                .andExpect((ResultMatcher) expectedResponse)

        where:
        returnedPublication                           | inputPublicationId | expectedResponse                      | desc
        new Disease("OMIM:154700", "Marfan Syndrome") | "PMID:1337998"     | MockMvcResultMatchers.status().isOk() | "test get diseases for publication"
        new Disease("OMIM:154700", "Marfan Syndrome") | "PMID:9999990"     | MockMvcResultMatchers.status().isOk() | "test get diseases for publication 2"
        []                                            | "PMID:9999990"     | MockMvcResultMatchers.status().isOk() | "test get diseases for publication find nothing"
        []                                            | ""                 | MockMvcResultMatchers.status().isOk() | "test get disease by nothing should fail"
        []                                            | ""                 | MockMvcResultMatchers.status().isOk() | "test get disease by nothing should fail"
    }

    @Unroll
    def "when we save publication to disease"() {
        given:
        entityService.createAnnotationSource(_) >> new AnnotationSource()


        expect:
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/entity/publication/", inputBody).contentType(MediaType.APPLICATION_JSON)
                .content(new JsonBuilder(inputBody).toPrettyString()))
                .andExpect((ResultMatcher) expectedResponse)

        where:
        inputBody                            | expectedResponse                      | desc
        publicationGenerator("PMID:1337998") | MockMvcResultMatchers.status().isOk() | "test get publication by id with pub"

    }

    private static Publication publicationGenerator(publicationId) {
        def publications = [
                "PMID:1337998": new Publication(
                        "PMID:1337998",
                        "A great novel about the poet application",
                        "A great date on which it was published",
                        "Michael A Gargano"),
                "PMID:9999990": new Publication("PMID:9999990", "A great novel about the poet application",
                        "A great date on which it was published",
                        "Michael A Gargano"
                )
        ]
        return publications[publicationId]
    }


}
