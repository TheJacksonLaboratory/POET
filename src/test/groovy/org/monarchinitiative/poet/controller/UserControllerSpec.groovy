package org.monarchinitiative.poet.controller

import org.monarchinitiative.poet.model.entities.User
import org.monarchinitiative.poet.service.UserService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.core.Authentication
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class UserControllerSpec extends Specification {

    @SpringBean
    private UserService userService = Stub()

    @Autowired
    private MockMvc mvc


    @WithMockUser(value = "spring")
    @Unroll
    def "when we test check user"() {
        given:
        userService.saveOrUpdateUser(_ as Authentication) >> shouldSaveOrUpdate


        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user/check")).andExpect((ResultMatcher) expectedResponse)

        where:
        shouldSaveOrUpdate | expectedResponse
        true               | MockMvcResultMatchers.status().isOk()
        false              | MockMvcResultMatchers.status().isNotFound()
        false              | MockMvcResultMatchers.status().isNotFound()
    }

    @WithMockUser(value = "spring")
    @Unroll
    def "when we test update orcid"() {
        expect:
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/user").param("orcid", inputOrcid)).andExpect((ResultMatcher) expectedResponse)

        where:
        inputOrcid | expectedResponse
        "xxx"      | MockMvcResultMatchers.status().isOk()
        ""         | MockMvcResultMatchers.status().isOk()
        "zzz"      | MockMvcResultMatchers.status().isOk()
    }

    @WithMockUser(value = "spring")
    @Unroll
    def "when we test get user details"() {
        given:
        userService.getExistingUser(_ as Authentication) >> serviceResponse


        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user/")).andExpect((ResultMatcher) expectedResponse)

        where:
        serviceResponse | expectedResponse
        new User()      | MockMvcResultMatchers.status().isOk()

    }
}
