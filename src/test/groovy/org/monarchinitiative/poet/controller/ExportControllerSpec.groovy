package org.monarchinitiative.poet.controller

import org.monarchinitiative.poet.service.ExportService
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
@WebMvcTest(ExportController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class ExportControllerSpec extends Specification {

    @SpringBean
    private ExportService exportService = Stub()

    @Autowired
    private MockMvc mvc


    @Unroll
    def "when we test export ontology"() {
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/export/${ontologyInput}")
                .param("delim", inputDelim).param("unstable", inputUnstable)
        ).andExpect((ResultMatcher) expectedResponse)

        where:
        expectedResponse                      | ontologyInput | inputDelim | inputUnstable
        MockMvcResultMatchers.status().isOk() | "maxo"        | "csv"      | "false"
        MockMvcResultMatchers.status().isOk() | "hpo"         | "tsv"      | "false"
        MockMvcResultMatchers.status().isOk() | "mondo"       | "tsv"      | "false"
    }

    // release key is inherited in application-test.yml
    @Unroll
    def "when we test release ontology we fail"() {
        given:
        exportService.releaseAnnotations() >> { throw serviceWork }
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/export/release/")
                .param("key", inputKey)).andExpect((ResultMatcher) expectedResponse)

        where:
        expectedResponse                                       | inputKey  | serviceWork
        MockMvcResultMatchers.status().isUnauthorized()        | "tester"  | new Exception("something broke")
        MockMvcResultMatchers.status().isInternalServerError() | "release" | new Exception("something broke")

    }

    @Unroll
    def "when we test release ontology we pass"() {
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/export/release/")
                .param("key", inputKey)).andExpect((ResultMatcher) expectedResponse)

        where:
        expectedResponse                      | inputKey
        MockMvcResultMatchers.status().isOk() | "release"

    }
}