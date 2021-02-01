package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.model.entities.Annotation
import org.monarchinitiative.poet.model.entities.User
import org.monarchinitiative.poet.model.entities.UserActivity
import org.monarchinitiative.poet.model.enumeration.CurationAction
import org.monarchinitiative.poet.repository.UserActivityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


@Unroll
@ActiveProfiles(value = "test")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = [ServiceTestConfig.class])
class StatisticsServiceSpec extends Specification {

    @Autowired
    UserActivityRepository userActivityStub

    @Autowired
    StatisticsService statisticsService

    @Shared
    Authentication authentication = Stub(Authentication.class)

    void "test that user activities #desc"() {
        given:
        userActivityStub.findAll() >> repositoryResponse
        userActivityStub.findUserActivityByUserAuthId(_) >> repositoryResponse
        authentication.getName() >> "fakename"
        def result = statisticsService.getUserActivity(all, inputAuthentication)

        expect:
        result.size() == expectedResultSize

        where:
        all   | inputAuthentication | repositoryResponse        | expectedResultSize | desc
        true  | authentication      | getFakeUserActivity()     | 3                  | "all user activity no activity"
        true  | authentication      | getEmptyList()            | 0                  | "all user activity and has nothing"
        false | authentication      | getSpecificUserActivity() | 1                  | "current user and has one"
        false | authentication      | getEmptyList()            | 0                  | "not valid (even though impossible) and has none"
        false | authentication      | getEmptyList()            | 0                  | "not valid (even though impossible) and has none"

    }

    void "test that user contribution #desc"() {
        given:
        userActivityStub.countAllByAnnotation_AnnotationTypeAndUserAuthId(_, _) >> repositoryResponse
        def result = statisticsService.summarizeUserContributions(inputAuthentication)
        expect:
        result.getMaxo() == repositoryResponse
        result.getHpo() == 0
        result.getPhenopackets() == 0;
        where:
        inputAuthentication | repositoryResponse
        authentication      | 20
        authentication      | 0
        authentication      | 1092
        authentication      | 1337
    }

    def getFakeUserActivity() {
        return [new UserActivity(
                new User(),
                CurationAction.CREATE,
                new Annotation(),
                new Annotation()
        ), new UserActivity(
                new User(),
                CurationAction.DELETE,
                new Annotation(), new Annotation()), new UserActivity(
                new User(),
                CurationAction.UPDATE,
                new Annotation(), new Annotation())
        ]
    }

    def getSpecificUserActivity() {
        return [new UserActivity(
                new User(),
                CurationAction.CREATE,
                new Annotation(), new Annotation())
        ]
    }

    def getEmptyList() {
        return Collections.emptyList();
    }
}
