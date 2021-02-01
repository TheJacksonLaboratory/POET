package org.monarchinitiative.poet.controller

import org.monarchinitiative.poet.controller.disease.DiseaseController
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.service.EntityService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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

    def setup(){
    }

    @Unroll
    def "when we test get maxo annotations #desc"(){
        given:
        entityService.getDisease(_) >> inputDisease


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/entity/disease/${inputDiseaseId}/")).andExpect((ResultMatcher) expectedResponse);

        where:
        inputDisease                                  | inputDiseaseId   | expectedResponse                                | desc
        new Disease("OMIM:154700", "Marfan Syndrome") | "OMIM:154700"    | MockMvcResultMatchers.status().isOk()           | "test get disease by id with pub"
        null                                          | "OMIM:0091920"   | MockMvcResultMatchers.status().isNotFound()     | "test get disease by id find nothing"
        null                                            | ""               | MockMvcResultMatchers.status().isMethodNotAllowed()     | "test get disease by nothing find nothing"
    }




}
