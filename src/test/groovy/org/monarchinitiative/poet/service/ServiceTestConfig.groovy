package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.monarchinitiative.poet.repository.MessageRepository
import org.monarchinitiative.poet.repository.PhenotypeAnnotationRepository
import org.monarchinitiative.poet.repository.TreatmentAnnotationRepository
import org.monarchinitiative.poet.repository.PublicationRepository
import org.monarchinitiative.poet.repository.UserActivityRepository
import org.monarchinitiative.poet.repository.UserRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.ws.client.core.WebServiceTemplate
import spock.mock.DetachedMockFactory

@TestConfiguration
class ServiceTestConfig {
    def mockFactory = new DetachedMockFactory()

    @Bean
    UserActivityRepository userActivityStub(){
        return mockFactory.Stub(UserActivityRepository)
    }

    @Bean
    UserRepository userRepositoryStub(){
        return mockFactory.Stub(UserRepository)
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
    TreatmentAnnotationRepository treatmentAnnotationStub(){
        return mockFactory.Stub(TreatmentAnnotationRepository)
    }

    @Bean
    PhenotypeAnnotationRepository phenotypeAnnotationStub(){
        return mockFactory.Stub(PhenotypeAnnotationRepository)
    }

    @Bean
    MessageRepository messageRepositoryStub() {
        return mockFactory.Stub(MessageRepository)
    }
    @Bean
    EntityService entityService(){
        EntityService entityService = new EntityService(diseaseStub(), publicationStub(), annotationSourceStub())
        return entityService
    }

    @Bean
    AnnotationService annotationService(){
        AnnotationService annotationService = new AnnotationService(diseaseStub(),
                treatmentAnnotationStub(), userActivityStub(), phenotypeAnnotationStub(),
                messageRepositoryStub(), entityService())
        return annotationService
    }

    @Bean
    WebServiceTemplate webServiceTemplateStub(){
        return mockFactory.Stub(WebServiceTemplate)
    }

    @Bean
    SearchService searchService(){
        SearchService searchService = new SearchService(diseaseStub(), webServiceTemplateStub())
        return searchService
    }


    @Bean
    StatisticsService statisticService() {
            StatisticsService statisticsService = new StatisticsService(userActivityStub(), treatmentAnnotationStub(), diseaseStub(),
                    phenotypeAnnotationStub());
            return statisticsService;
    }
}
