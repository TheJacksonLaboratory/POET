package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.SearchResponse;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<AnnotationSource> searchAnnotationSource(String query, String type){
        if(type.equals("disease")){
            Disease disease = diseaseRepository.findDiseaseByDiseaseId(query);
            if(disease != null){
                return annotationSourceRepository.findDistinctByDisease(disease);
            }
        } else if(type.equals("publication")){
            Publication publication = publicationRepository.findByPublicationId(query);
            if(publication != null) {
                return annotationSourceRepository.findDistinctByPublication(publication);
            }
        }
        return Collections.emptyList();
    }
}
