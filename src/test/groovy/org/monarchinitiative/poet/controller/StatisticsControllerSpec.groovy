package org.monarchinitiative.poet.controller

import org.monarchinitiative.poet.model.entities.Annotation
import org.monarchinitiative.poet.model.entities.AnnotationSource
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.Publication
import org.monarchinitiative.poet.model.entities.User
import org.monarchinitiative.poet.model.entities.UserActivity
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus
import org.monarchinitiative.poet.model.enumeration.CurationAction
import org.monarchinitiative.poet.model.enumeration.CurationRole
import org.monarchinitiative.poet.model.responses.AnnotationCount
import org.monarchinitiative.poet.model.responses.Contribution
import org.monarchinitiative.poet.model.responses.ReviewCount
import org.monarchinitiative.poet.service.StatisticsService
import org.monarchinitiative.poet.service.UserService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll
import org.monarchinitiative.poet.model.enumeration.Category
import org.springframework.http.MediaType

@AutoConfigureMockMvc
@WebMvcTest(StatisticsController.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
class StatisticsControllerSpec extends Specification {

    @SpringBean
    UserService userService = Stub()

    @SpringBean
    StatisticsService statisticsService = Stub()

    @Autowired
    private MockMvc mvc

    @Unroll
    def "test get user activity"(){
        given:
        statisticsService.getUserActivity(_, _, _, _) >> serviceResponse
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/activity")
                .param("all", inputAll).param("weeks", inputWeeks).param("offset", inputOffset).param("limit", inputLimit))
                .andExpect((ResultMatcher) expectedResponseStatus)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].owner.nickname").value(expectedResponseBody[0]))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].curationAction").value(expectedResponseBody[1]))
        where:
        serviceResponse     |   inputAll    | inputWeeks    | inputOffset   | inputLimit | expectedResponseStatus | expectedResponseBody
        userActivityPage()  |   "true"      | "1"           | "0"           | "2"        | MockMvcResultMatchers.status().isOk() | ["gary", "ACCEPTED"]
    }

    @Unroll
    def "test get disease activity"(){
        given:
        statisticsService.getUserActivity(_, _, _, _) >> serviceResponse
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/activity/disease")
        .param("all", inputAll).param("weeks", inputWeeks).param("offset", inputOffset).param("limit", inputLimit))
                .andExpect((ResultMatcher) expectedResponseStatus)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].diseaseId").value(expectedResponseBody[0]))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].diseaseCount").value(expectedResponseBody[1]))
        where:
        serviceResponse     |   inputAll    | inputWeeks    | inputOffset   | inputLimit | expectedResponseStatus | expectedResponseBody
        userActivityPage()  |   "true"      | "1"           | "0"           | "2"        | MockMvcResultMatchers.status().isOk() | ["OMIM:302023", 1l]

    }

    def userActivityPage() {
        def activity = userActivity()
        return new PageImpl<>(List.of(activity), Pageable.unpaged(), 0);
    }

    def userActivity(){
        def owner = new User("auth|00193", "gary", "gary@gmail.com", CurationRole.POET_CURATOR)
        def reviewer = new User()
        def curationAction = CurationAction.CREATE
        def annotationSource = new AnnotationSource(new Publication("PMID:00919433",
                "some paper name", "", "Gargano"), new Disease("OMIM:302023", "terrible disease"))
        def annotation = new Annotation(annotationSource, AnnotationStatus.ACCEPTED)
        return new UserActivity(owner, curationAction, annotation, null)
    }

    @Unroll
    def "test get current user contribution statistic"(){
        given:
        statisticsService.summarizeUserContributions(_) >> serviceResponse
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/contributions"))
                .andExpect((ResultMatcher) expectedResponseStatus)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.treatment").value(expectedResponseBody[0]))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.phenotype").value(expectedResponseBody[1]))
        where:
        serviceResponse                     | expectedResponseStatus |                expectedResponseBody
        contribution(1 as int,2 as int)       |           MockMvcResultMatchers.status().isOk() | [1,2]
        contribution(-10 as int, 1 as int) |MockMvcResultMatchers.status().isOk() | [0,1]
    }

    def contribution(t, p){
        return new Contribution(t, p)
    }

    @Unroll
    def "test get annotation statistics for a disease"(){
        given:
        statisticsService.summarizeAnnotations(_) >> serviceResponse
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/annotation/${inputDisease}"))
                .andExpect((ResultMatcher) expectedResponseStatus)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.phenotypeCount").value(expectedResponseBody[0]))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.treatmentCount").value(expectedResponseBody[1]))
        where:
        serviceResponse                     | inputDisease              | expectedResponseStatus |                expectedResponseBody
        annotationCount(1,2)        | "MONDO:019113"            |           MockMvcResultMatchers.status().isOk() | [1,2]
        annotationCount(null, null) | "OMIM:1039999"            |MockMvcResultMatchers.status().isOk() | [0,0]
    }

    def annotationCount(pc, tc){
        return new AnnotationCount(pc,tc)
    }

    @Unroll
    def "test get annotations statistics needing review"(){
        given:
        statisticsService.summarizeAnnotationNeedReview() >> statistics
        expect:
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/annotation/review"))
                .andExpect((ResultMatcher) expectedResponseStatus)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].count").value(60l))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[1].count").value(5l))
        where:
        statistics              | expectedResponseStatus
        generateStatistics()    | MockMvcResultMatchers.status().isOk()
    }

    @Unroll
    def "test get annotations that need work"(){
        given:
            statisticsService.summarizeAnnotationNeedWork(_) >> statistics
        expect:
            mvc.perform(MockMvcRequestBuilders.get("/api/v1/statistics/annotation/work"))
                    .andExpect((ResultMatcher) expectedResponseStatus)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("\$[0].count").value(60l))
                    .andExpect(MockMvcResultMatchers.jsonPath("\$[1].count").value(5l))
        where:
        statistics              | expectedResponseStatus
        generateStatistics()    | MockMvcResultMatchers.status().isOk()
    }

    def generateStatistics(){
        return [
                new ReviewCount("OMIM:02903", "terrible disease", 60l, Category.TREATMENT),
                new ReviewCount("OMIM:04093", "terrible disease 2", 5l, Category.PHENOTYPE),
                new ReviewCount("MONDO:333225", "terrible disease 14", 300l, Category.UNKNOWN)
        ]
    }
}
