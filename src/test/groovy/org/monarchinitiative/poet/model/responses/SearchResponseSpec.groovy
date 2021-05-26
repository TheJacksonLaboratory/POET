package org.monarchinitiative.poet.model.responses


import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(value = "test")
class SearchResponseSpec extends Specification {

    void "test search response model"() {
        given:
        def searchResponse = new SearchResponse(inputId, inputName, inputType)
        expect:
        searchResponse.getId() == inputId
        searchResponse.getName() == inputName
        searchResponse.getType() == inputType

        where:
        inputId         | inputName             | inputType     | desc
        "PMID:99202893" | "A great publication" | "publication" | "a publication"
        "OMIM:90992932" | "a terrible disease"  | "disease"     | "a disease"
        ""              | ""                    | ""            | "all empties"
        null            | null                  | null          | "all nulls"
    }
}
