package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.repository.VersionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

@Unroll
@ActiveProfiles(value = "test")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = [ServiceTestConfig.class], initializers = ConfigFileApplicationContextInitializer.class)
class ExportServiceSpec extends Specification {

    @Autowired
    ExportService exportService

    @Autowired
    VersionRepository versionRepositoryStub

    void "test create release version"(){
        given:
        versionRepositoryStub.save(_) >> {arguments -> arguments[0]}

        expect:
        def versionGiven = exportService.createNewReleaseVersion();
        versionGiven.getVersion().getDayOfWeek() == LocalDateTime.now().getDayOfWeek()
    }
}
