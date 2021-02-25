package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.response.SearchResponse;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.springframework.stereotype.Service;

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

    SearchService(DiseaseRepository diseaseRepository){
        this.diseaseRepository = diseaseRepository;
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
}
