package org.monarchinitiative.poet.controller

import org.monarchinitiative.poet.service.AnnotationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification


@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration
class AnnotationControllerSpec extends Specification {

    @Autowired
    private MockMvc mvc

    @MockBean
    private AnnotationService annotationService

    def "when we test the test endpoint we should expect ok"() {
        expect: "Status is 200 for the response"
        mvc.perform(MockMvcRequestBuilders.get("/annotation/test")).andExpect(MockMvcResultMatchers.status().isOk())
    }
}
