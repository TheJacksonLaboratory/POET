package security

import org.monarchinitiative.poet.security.AudienceValidator
import org.springframework.security.oauth2.jwt.Jwt
import spock.lang.Specification
import java.time.Instant

class AudienceValidatorSpec extends Specification {

    void "test oauth validator result success"(){
        given:
            def token = new Jwt(inputToken,
                    Instant.now(),
                    Instant.now().plusSeconds(30),
                    Map.of("alg", "none"),
                    inputClaims)
            def validator = new AudienceValidator(inputAudience)
            def authResult = validator.validate(token)
        expect:
            !authResult.hasErrors()
        where:
        inputToken  |   inputAudience | inputClaims
        "token"     |   "my-special-role" | getClaims(inputAudience)
    }

    void "test oauth validator result failure"(){
        given:
        def token = new Jwt(inputToken,
                Instant.now(),
                Instant.now().plusSeconds(30),
                Map.of("alg", "none"),
                inputClaims)
        def validator = new AudienceValidator(inputAudience)
        def authResult = validator.validate(token)
        expect:
        authResult.hasErrors()
        where:
        inputToken  |   inputAudience | inputClaims
        "token"     |   "my-special-role" | getClaims("bad-audience")
    }



    def getClaims(inputAudience) {
        // This is a place to add general and maybe custom claims which should be available after parsing token in the live system
        return Map.of(
                "sub", "sms|12345678",
                    "aud", inputAudience
        );
    }
}
