package org.monarchinitiative.poet.model.entities

import org.monarchinitiative.poet.model.enumeration.CurationRole
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class UserSpec extends Specification {

    void "test user constructor"(){
        given:
            def user = new User(authId, nickname, email, orcId, curationRole)
            def user2 = new User(authId, nickname, email, orcId, curationRole)
        expect:
            user.getAuthId() == authId
            user.getCurationRole() == curationRole
            user.getEmail() == email
            user.getNickname() == nickname
            user.getOrcId() == orcId
            user.equals(user2)
            user.hashCode() == user2.hashCode()
        where:
            authId  |   curationRole    |   email   |   nickname    |   orcId
            "auth|1039" |   CurationRole.POET_CURATOR |   "curator@gmail.com" | "the best curator" | "orcid:029323"
            "auth|103920" |   CurationRole.POET_ADMIN |   "elevated. @gmail.com" | "even better curator" | "orcid:029344"

    }

    void "test user set curation role"(){
        given:
            def user = new User(authId, nickname, email, orcId, curationRole)
            user.setCurationRole(expectedRole)
        expect:
            user.getCurationRole() == expectedRole

        where:
        authId  |   curationRole    |   email   |   nickname    |   orcId   |  expectedRole
        "auth|1039" |   CurationRole.POET_CURATOR |   "curator@gmail.com"    | "the best curator"    | "orcid:029323" | CurationRole.POET_ADMIN
        "auth|103920" |   null                    |   "elevated. @gmail.com" | "even better curator" | "orcid:029344" | CurationRole.POET_CURATOR
    }
}
