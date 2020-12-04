package org.monarchinitiative.poet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


@SpringBootTest
@ContextConfiguration
class ApplicationContextTest extends Specification {

    @Autowired
    ApplicationContext context

    def "when context is loaded then all expected beans are created"() {
        expect: "the application context is created"
        context != null
    }
}
