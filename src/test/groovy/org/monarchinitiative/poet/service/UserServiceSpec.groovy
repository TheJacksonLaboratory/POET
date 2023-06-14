package org.monarchinitiative.poet.service

import org.monarchinitiative.poet.exceptions.AuthenticationException
import org.monarchinitiative.poet.exceptions.UserModificationException
import org.monarchinitiative.poet.model.entities.User
import org.monarchinitiative.poet.model.enumeration.CurationRole
import org.monarchinitiative.poet.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification
import java.time.Instant

@AutoConfigureMockMvc
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = [ServiceTestConfig.class], initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles(value = "test")
class UserServiceSpec extends Specification {

    @Autowired
    UserRepository userRepositoryStub

    @Autowired
    UserService userService

    def setup() {
    }

    void "test get existing user"() {
        given:
        userRepositoryStub.findDistinctByAuthId(_) >> existingUser
        userRepositoryStub.save(_) >> null
        def user = userService.getExistingUser(authenticationJwt)
        expect:
        user.getAuthId() == expected.getAuthId()
        user.getEmail() == expected.getEmail()
        user.getCurationRole() == expected.getCurationRole()
        user.getNickname() == expected.getNickname()
        where:
        authenticationJwt                                        | existingUser                                                                       | expected
        new JwtAuthenticationConverter().convert(fakeJwt(false)) | null                                                                               | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_CURATOR)
        new JwtAuthenticationConverter().convert(fakeJwt(false)) | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_CURATOR) | existingUser
        new JwtAuthenticationConverter().convert(fakeJwt(true))  | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_ADMIN)   | existingUser
    }

    void "test get hpo user"() {
        given:
        def expected = new User("auth:00000", "hpoteam", "dr.sebastian.koehler@gmail.com", CurationRole.POET_ADMIN)
        userRepositoryStub.save(_) >> expected
        def user = userService.getHumanPhenotypeOntologyUser()

        expect:
        user.getAuthId() == expected.getAuthId()
        user.getEmail() == expected.getEmail()
        user.getCurationRole() == expected.getCurationRole()
        user.getNickname() == expected.getNickname()
    }

    void "test save or update user"() {
        given:
        userRepositoryStub.findDistinctByAuthId(_) >> existingUser
        userRepositoryStub.save(_) >> null
        expect:
        userService.saveOrUpdateUser(authenticationJwt) == expectedResponse
        where:
        authenticationJwt                                        | existingUser                                                                       | expectedResponse
        new JwtAuthenticationConverter().convert(fakeJwt(false)) | null                                                                               | true
        new JwtAuthenticationConverter().convert(fakeJwt(true))  | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_CURATOR) | true
        new JwtAuthenticationConverter().convert(fakeJwt(true))  | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_ADMIN)   | true

    }

    void "test get user from authentication should return user"() {
        expect:
        def user = userService.getUserFromAuthentication(inputAuthentication)
        user.getAuthId() == expectedResponse.getAuthId()
        user.getEmail() == expectedResponse.getEmail()
        user.getCurationRole() == expectedResponse.getCurationRole()
        user.getNickname() == expectedResponse.getNickname()
        where:
        inputAuthentication                                      | expectedResponse
        new JwtAuthenticationConverter().convert(fakeJwt(false)) | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_CURATOR)
        new JwtAuthenticationConverter().convert(fakeJwt(true))  | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_ADMIN)
    }

    void "test get user from authentication should throw error"() {
        when:
        userService.getUserFromAuthentication(inputAuthentication)
        then:
        def e = thrown(AuthenticationException)
        e.message == expectedResponse.message
        where:
        inputAuthentication                                   | expectedResponse
        null                                                  | new AuthenticationException(true)
        new JwtAuthenticationConverter().convert(brokenJwt()) | new AuthenticationException(true)
    }

    void "test set user orcid id does not throw"() {
        when:
        userRepositoryStub.findDistinctByAuthId(_) >> existingUser
        userRepositoryStub.save(_) >> null
        userRepositoryStub.existsUserByOrcid(_) >> exists
        def user = userService.setUserOrcId(authenticationJwt, "00293832-002932-2")
        then:
        notThrown(UserModificationException)
        where:
        authenticationJwt                                       | exists | existingUser
        new JwtAuthenticationConverter().convert(fakeJwt(true)) | false  | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_ADMIN)
    }

    void "test set user orcid id throws"() {
        when:
        userRepositoryStub.findDistinctByAuthId(_) >> existingUser
        userRepositoryStub.save(_) >> null
        userRepositoryStub.existsUserByOrcid(_) >> exists
        def user = userService.setUserOrcId(authenticationJwt, "00293832-002932-2")
        then:
        thrown(UserModificationException)
        where:
        authenticationJwt                                       | exists | existingUser
        new JwtAuthenticationConverter().convert(fakeJwt(true)) | true   | new User("sms|12345678", "test-nickname", "test-email", CurationRole.POET_ADMIN)
    }


    def brokenJwt() {
        return new Jwt("poet-api-audience",
                Instant.now(),
                Instant.now().plusSeconds(30),
                Map.of("alg", "none"),
                Map.of(
                        "sub", "sms|12345678",
                        "https://poet.jax.org/nickname", "",
                        "https://poet.jax.org/email", "test-email",
                        "permissions", []
                )
        )
    }


    def fakeJwt(hasAdmin) {
        def permissions = []
        if (hasAdmin) {
            permissions.push("poet:admin")
        }
        return new Jwt("poet-api-audience",
                Instant.now(),
                Instant.now().plusSeconds(30),
                Map.of("alg", "none"),
                Map.of(
                        "sub", "sms|12345678",
                        "https://poet.jax.org/nickname", "test-nickname",
                        "https://poet.jax.org/email", "test-email",
                        "permissions", permissions
                )
        )
    }

}
