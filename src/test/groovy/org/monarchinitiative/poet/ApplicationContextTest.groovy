package org.monarchinitiative.poet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification


@SpringBootTest
class ApplicationContextTest extends Specification {

    @Autowired
    ApplicationContext context

    def "when context is loaded then all expected beans are created"() {
        expect: "the annotation controller is created"
        context != null
    }
}
