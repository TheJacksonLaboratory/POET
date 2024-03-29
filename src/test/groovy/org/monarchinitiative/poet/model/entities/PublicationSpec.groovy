package org.monarchinitiative.poet.model.entities

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class PublicationSpec extends Specification {

    void "test publication spec constructor"(){
        given:
            def publication = new Publication(publicationId, publicationName, date, firstAuthor)
            def publication2 = new Publication(publicationId, publicationName, date, firstAuthor)
        expect:
            publication.getPublicationName() == publicationName
            publication.getPublicationId()  ==  publicationId
            publication.getDate() == date
            publication.getFirstAuthor() == firstAuthor
            publication.equals(publication2)
            publication.hashCode() == publication2.hashCode()

        where:
            publicationName |   publicationId   |   date    |   firstAuthor
            "Publication1"  |   "PMID:09399101" |   ""      |   "first author 1"
            "Publication2"  |   "PMID:09399103" |   "10-20-21"      |   "first author 2"
    }
}
