package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.repository.UserActivityRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.mock.DetachedMockFactory

@Configuration
class StatisticsServiceConfig {
    def mockFactory = new DetachedMockFactory()

    @Bean
    UserActivityRepository userActivityStub(){
        return mockFactory.Stub(UserActivityRepository)
    }

    @Bean
    StatisticsService orderService() {
            StatisticsService statisticsService = new StatisticsService(userActivityStub());
            return statisticsService;
    }
}
