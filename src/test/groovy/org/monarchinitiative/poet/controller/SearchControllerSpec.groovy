package org.monarchinitiative.poet.controller

import org.monarchinitiative.poet.model.response.SearchResponse
import org.monarchinitiative.poet.service.SearchService
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
@WebMvcTest(SearchController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class SearchControllerSpec extends Specification{
    @Autowired
    private MockMvc mvc

    @SpringBean
    private SearchService searchService = Stub()

    def setup() {
    }

    @Unroll
    def "when we test search annotations #desc"() {
        given:
        searchService.searchPublicationAndDisease(_) >> serviceResponse


        expect: "an annotation state"
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/search").param("query", searchTerm)).andExpect((ResultMatcher) expectedResponse);

        where:
        serviceResponse      | searchTerm      | expectedResponse                            | desc
        [new SearchResponse()] | "OMIM:00392928" | MockMvcResultMatchers.status().isOk() | "test search term with disease"
        [new SearchResponse()] | "PMID:20391892" | MockMvcResultMatchers.status().isOk()       | "test search term with publication"
        null                 | ""              | MockMvcResultMatchers.status().isOk()       | "test search term nothing"
    }
}
