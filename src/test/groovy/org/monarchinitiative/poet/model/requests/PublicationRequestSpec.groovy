package org.monarchinitiative.poet.model.requests

import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.model.entities.Publication
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class PublicationRequestSpec extends Specification {

    void "test publication request spec"(){
        given:
            def publicationRequest = new PublicationRequest(publication as Publication, disease as Disease)
            def publicationRequest2 = new PublicationRequest(publication as Publication, disease as Disease)
        expect:
            publicationRequest.getPublication() == publication
            publicationRequest.getDisease() == disease
            publicationRequest.equals(publicationRequest2)
            publicationRequest.hashCode() == publicationRequest2.hashCode()
        where:
            publication         | disease
            getPublication()    | getDisease()
    }

    def getPublication(){
        return new Publication("PMID:203932", "some publication name", "10/20/2003", "some author")
    }

    def getDisease(){
        return new Disease("MONDO:01930029", "Marfan Syndrome")
    }

}
