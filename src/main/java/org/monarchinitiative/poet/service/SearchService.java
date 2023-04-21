package org.monarchinitiative.poet.service;

import org.monarchinitiative.model.responses.chebi.*;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.responses.SearchResponse;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A spring service component created to provide business logic and functionality to search the database
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@Service
public class SearchService {

    private DiseaseRepository diseaseRepository;

    private WebServiceTemplate webServiceTemplate;

    SearchService(DiseaseRepository diseaseRepository, WebServiceTemplate webServiceTemplate){
        this.diseaseRepository = diseaseRepository;
        this.webServiceTemplate = webServiceTemplate;
    }

    /**
     * A function to search the disease and publication repositories for any records.
     *
     * @param query a string query to search for.
     *
     * @return a list of search response objects or an empty list
     * @since 0.5.0
     */
    public List<SearchResponse> searchDisease(String query){
        List<SearchResponse> responseList = new ArrayList<>();
        List<Disease> diseases =  diseaseRepository.findDiseaseByDiseaseNameContainingIgnoreCaseOrDiseaseIdContainingIgnoreCase(query, query);

        if(diseases.size() > 0){
            for (Disease disease : diseases) {
                responseList.add(new SearchResponse(disease.getDiseaseId(),
                        disease.getDiseaseName(), "disease"));
            }
        }
        return responseList;
    }

    /**
     * A function to call the chebi wdsl web service to search for a chbi
     * @param query the search string
     * @return a list of lite chebi lite elements or empty list
     */
    public List<LiteEntity> searchChebi(String query){
        GetLiteEntity liteEntity = new GetLiteEntity();
        liteEntity.setSearch(query);
        liteEntity.setStars(StarsCategory.ALL);
        liteEntity.setSearchCategory(SearchCategory.ALL);
        liteEntity.setMaximumResults(10);
        JAXBElement<GetLiteEntity> request = new ObjectFactory().createGetLiteEntity(liteEntity);

        JAXBElement<GetLiteEntityResponse> response =  (JAXBElement<GetLiteEntityResponse>) webServiceTemplate.marshalSendAndReceive(
                "http://www.ebi.ac.uk:80/webservices/chebi/2.0/webservice", request);


        return response.getValue().getReturn().getListElement();
    }


}
