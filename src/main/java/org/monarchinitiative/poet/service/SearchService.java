package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.search.SearchResponse;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private PublicationRepository publicationRepository;
    private DiseaseRepository diseaseRepository;

    SearchService(PublicationRepository publicationRepository, DiseaseRepository diseaseRepository){
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
    }

    public Publication findPublicationByPubMedId(Integer publicationId){
        return publicationRepository.findByPublicationIdentifier(publicationId);
    }

    public SearchResponse searchPublicationAndDisease(String query){
        SearchResponse response = new SearchResponse();
        response.addDiseasesToResponse(diseaseRepository.findDiseaseByDiseaseNameContainingIgnoreCaseOrDiseaseIdContainingIgnoreCase(query, query));
        response.addPublicationsToResponse(publicationRepository.findByPublicationIdentifierStartingWithOrPublicationNameContainingIgnoreCase(query, query));
        return response;
    }
}
