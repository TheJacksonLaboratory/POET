package org.monarchinitiative.poet.service

import org.monarchinitiative.model.responses.chebi.GetLiteEntity
import org.monarchinitiative.model.responses.chebi.GetLiteEntityResponse
import org.monarchinitiative.model.responses.chebi.LiteEntity
import org.monarchinitiative.model.responses.chebi.LiteEntityList
import org.monarchinitiative.poet.model.entities.Disease
import org.monarchinitiative.poet.repository.AnnotationSourceRepository
import org.monarchinitiative.poet.repository.DiseaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.ws.client.core.WebServiceTemplate
import spock.lang.Specification
import spock.lang.Unroll

import javax.xml.bind.JAXBElement
import javax.xml.namespace.QName

@Unroll
@ActiveProfiles(value = "test")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = [ServiceTestConfig.class],  initializers = ConfigFileApplicationContextInitializer.class )
class SearchServiceSpec extends Specification {
    @Autowired
    DiseaseRepository diseaseStub;

    @Autowired
    WebServiceTemplate webServiceTemplateStub;

    @Autowired
    SearchService searchService


    void "test search publication and disease #desc"() {
        given:
        diseaseStub.findDiseaseByDiseaseNameContainingIgnoreCaseOrDiseaseIdContainingIgnoreCase(_ as String, _ as String) >> diseaseResponse
        def result = searchService.searchDisease(inputQuery)
        expect:
        result.size() == expectedSize

        where:
        inputQuery | diseaseResponse        | expectedSize | desc
        "really"   | getFewDiseases()[1..2] | 2            | "expect 2 response"
        "dise"     | getFewDiseases()       | 3            | "expect 3 response"
        "bad"      | getSingleDisease()     | 1            | "expect 2 response"
        "dise"     | getSingleDisease()     | 1            | "expect 1 response"
        ""         | getEmptyList()         | 0            | "expect 0 response"

    }

    def getEmptyList() {
        return []
    }

    def getFewDiseases() {
        return [
                new Disease("OMIM:9820939", "Some bad disease"),
                new Disease("OMIM:8290392", "Some really bad disease"),
                new Disease("OMIM:2091938", "Some really really bad disease")
        ]
    }

    def getSingleDisease() {
        return [new Disease("OMIM:2091938", "Some really really bad disease")]
    }


    void "test search chebi #desc"(){
        given:
            webServiceTemplateStub.marshalSendAndReceive(_ as String, _ as JAXBElement) >> fakeChebiResponse
            def result = searchService.searchChebi(inputQuery)
        expect:
            result == chebiList
            result.size() == chebiList.size()
        where:
        inputQuery | chebiList | fakeChebiResponse
        "Aky"      | [createChebiEntity("CHEBI:85154", "Akynzeo")] | getChebiResponse(chebiList)
        "Amlo"     | [createChebiEntity("CHEBI:2668", "Amlodipine"), createChebiEntity("CHEBI:2669", "amlodipine benzenesulfonate")] | getChebiResponse(chebiList)

    }

    def createChebiElement(LiteEntityList entityList){
        GetLiteEntityResponse response = new GetLiteEntityResponse()
        response.setReturn(entityList)
        return new JAXBElement(
                new QName(GetLiteEntityResponse.class.getSimpleName()), GetLiteEntity.class, response)
    }

    def createChebiEntity(chebiId, chebiAscii){
        def entity = new LiteEntity()
        entity.setChebiId(chebiId)
        entity.setChebiAsciiName(chebiAscii)
        return entity
    }

    def getChebiResponse(chebiEntityList) {
        def chebi = new LiteEntity()
        LiteEntityList liteEntityList = new LiteEntityList();
        chebiEntityList.each { it ->
            liteEntityList.listElement.add(it);
        }
        return createChebiElement(liteEntityList);
    }
}
