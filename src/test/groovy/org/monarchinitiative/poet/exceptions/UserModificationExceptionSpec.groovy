package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class UserModificationExceptionSpec extends Specification {
    void "test export exception failure message"(){
        given:
        def exception = new UserModificationException(orcid)

        expect:
        exception.getMessage() == expectedMessage

        where:
        orcid      | expectedMessage
        "000-1103-32291"        | "Attemping to assign user 000-1103-32291, someone already has this orcid. Please contact us."
        "mike"                  | "Attemping to assign user mike, someone already has this orcid. Please contact us."
    }
}
