package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.SearchResponse;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private PublicationRepository publicationRepository;
    private DiseaseRepository diseaseRepository;

    SearchService(PublicationRepository publicationRepository,
                  DiseaseRepository diseaseRepository){
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
    }

    public List<SearchResponse> searchPublicationAndDisease(String query){
        List<SearchResponse> responseList = new ArrayList<>();
        List<Publication> publications = publicationRepository.findByPublicationIdStartingWithOrPublicationNameContainingIgnoreCase(query, query);
        List<Disease> diseases =  diseaseRepository.findDiseaseByDiseaseNameContainingIgnoreCaseOrDiseaseIdContainingIgnoreCase(query, query);
        if(publications.size() > 0){
            for (Publication publication : publications) {
                responseList.add(new SearchResponse(publication.getPublicationId(),
                        publication.getPublicationName(), "publication"));
            }
        }

        if(diseases.size() > 0){
            for (Disease disease : diseases) {
                responseList.add(new SearchResponse(disease.getDiseaseId(),
                        disease.getDiseaseName(), "disease"));
            }
        }
        return responseList;
    }
}
