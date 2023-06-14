package org.monarchinitiative.poet.exceptions

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class AuthenticationExceptionSpec extends Specification {

    void "test authentication failure message"(){
        given:
        def exception = new AuthenticationException(username)

        expect:
        exception.getMessage() == expectedMessage

        where:
        username      | expectedMessage
        "gargam"      | "User gargam is not authorized to perform this action."
        "robinp"      | "User robinp is not authorized to perform this action."
        "biox"        | "User biox is not authorized to perform this action."
    }

    void "test authentication malformed"(){
        given:
        def exception = new AuthenticationException(malformed)

        expect:
        exception.getMessage() == expectedMessage

        where:
        malformed      | expectedMessage
        false          | "Authentication or authentication claims are malformed."
    }
}
