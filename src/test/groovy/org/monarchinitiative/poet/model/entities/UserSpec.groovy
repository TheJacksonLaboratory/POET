package org.monarchinitiative.poet.model.entities

import org.monarchinitiative.poet.model.enumeration.CurationRole
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId

@Unroll
@ActiveProfiles(value = "test")
class UserSpec extends Specification {

    void "test user constructor"(){
        given:
            def user = new User(authId, nickname, email, curationRole)
            def user2 = new User(authId, nickname, email,curationRole)
        expect:
            user.getAuthId() == authId
            user.getCurationRole() == curationRole
            user.getEmail() == email
            user.getNickname() == nickname
            user.equals(user2)
            user.hashCode() == user2.hashCode()
        where:
            authId  |   curationRole    |   email   |   nickname
            "auth|1039" |   CurationRole.POET_CURATOR |   "curator@gmail.com" | "the best curator"
            "auth|103920" |   CurationRole.POET_ADMIN |   "elevated. @gmail.com" | "even better curator"

    }

    void "test user set curation role"(){
        given:
            def user = new User(authId, nickname, email, curationRole)
            user.setCurationRole(expectedRole)
        expect:
            user.getCurationRole() == expectedRole

        where:
        authId  |   curationRole    |   email   |   nickname    |   expectedRole
        "auth|1039" |   CurationRole.POET_CURATOR |   "curator@gmail.com"    | "the best curator"    | CurationRole.POET_ADMIN
        "auth|103920" |   null                    |   "elevated. @gmail.com" | "even better curator" | CurationRole.POET_CURATOR
    }

    void "test user export name"(){
        given:
            def user = new User(authId, nickname, email, curationRole)
            user.setOrcid(inputOrcid)
        expect:
            //LocalDateTime dt = new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            user.getExportName() == expectedResult

        where:
        authId  |   curationRole    |   email   |   nickname    |   inputOrcid  | expectedResult
        "auth|1039" |   CurationRole.POET_CURATOR |   "curator@gmail.com"    | "the best curator"    | "0000-0002-3843-3472" | "ORCID:0000-0002-3843-3472"
        "auth|103920" |   null                    |   "elevated.@gmail.com" | "even better curator" | ""   | "elevated.@gmail.com"
    }
}
