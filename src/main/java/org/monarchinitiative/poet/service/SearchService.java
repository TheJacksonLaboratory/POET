package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.search.SearchResponse;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SearchService {

    private PublicationRepository publicationRepository;
    private DiseaseRepository diseaseRepository;
    private AnnotationSourceRepository annotationSourceRepository;

    SearchService(PublicationRepository publicationRepository, DiseaseRepository diseaseRepository,
                  AnnotationSourceRepository annotationSourceRepository){
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
        this.annotationSourceRepository = annotationSourceRepository;
    }

    public SearchResponse searchPublicationAndDisease(String query){
        SearchResponse response = new SearchResponse();
        response.addDiseasesToResponse(diseaseRepository.findDiseaseByNameContainingIgnoreCaseOrIdentifierContainingIgnoreCase(query, query));
        response.addPublicationsToResponse(publicationRepository.findByIdentifierStartingWithOrNameContainingIgnoreCase(query, query));
        return response;
    }

    public List<AnnotationSource> searchAnnotationSource(String query, String type){
        if(type.equals("disease")){
            Disease disease = diseaseRepository.findDiseaseByIdentifier(query);
            if(disease != null){
                return annotationSourceRepository.findDistinctByDisease(disease);
            }
        } else if(type.equals("publication")){
            Publication publication = publicationRepository.findByIdentifier(query);
            if(publication != null) {
                return annotationSourceRepository.findDistinctByPublication(publication);
            }
        }
        return Collections.emptyList();
    }
}
