package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.monarchinitiative.poet.repository.TreatmentAnnotationRepository
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
    TreatmentAnnotationRepository maxoAnnotationStub(){
        return mockFactory.Stub(TreatmentAnnotationRepository)
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
        EntityService entityService = new EntityService(diseaseStub(), publicationStub(), annotationSourceStub(),
                userStub())
        return entityService
    }

    @Bean
    SearchService searchService(){
        SearchService searchService = new SearchService(diseaseStub())
        return searchService
    }


    @Bean
    StatisticsService orderService() {
            StatisticsService statisticsService = new StatisticsService(userActivityStub());
            return statisticsService;
    }
}
