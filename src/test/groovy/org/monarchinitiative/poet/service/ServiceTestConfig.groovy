package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.monarchinitiative.poet.repository.MaxoAnnotationRepository
import org.monarchinitiative.poet.repository.PublicationRepository
import org.monarchinitiative.poet.repository.UserActivityRepository
import org.monarchinitiative.poet.repository.UserRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory

@TestConfiguration
class ServiceTestConfig {
    def mockFactory = new DetachedMockFactory()

    @Bean
    UserActivityRepository userActivityStub(){
        return mockFactory.Stub(UserActivityRepository)
    }

    @Bean
    AnnotationSourceRepository annotationSourceStub(){
        return mockFactory.Stub(AnnotationSourceRepository)
    }

    @Bean
    PublicationRepository publicationStub(){
        return mockFactory.Stub(PublicationRepository)
    }

    @Bean
    DiseaseRepository diseaseStub(){
        return mockFactory.Stub(DiseaseRepository)
    }

    @Bean
    UserRepository userStub(){
        return mockFactory.Stub(UserRepository)
    }

    @Bean
    MaxoAnnotationRepository maxoAnnotationStub(){
        return mockFactory.Stub(MaxoAnnotationRepository)
    }

    @Bean
    AnnotationService annotationService(){
        AnnotationService annotationService = new AnnotationService(
                publicationStub(), diseaseStub(), annotationSourceStub(),
                maxoAnnotationStub(), userStub(), userActivityStub()
        )
        return annotationService
    }

    @Bean
    EntityService entityService(){
        EntityService entityService = new EntityService(diseaseStub(), publicationStub())
        return entityService
    }

    @Bean
    SearchService searchService(){
        SearchService searchService = new SearchService(publicationStub(), diseaseStub())
        return searchService
    }


    @Bean
    StatisticsService orderService() {
            StatisticsService statisticsService = new StatisticsService(userActivityStub());
            return statisticsService;
    }
}
