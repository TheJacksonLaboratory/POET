package org.monarchinitiative.poet.model.search;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

    private final List<SimpleSearchResponse> results = new ArrayList<>();

    public SearchResponse(){}

    public void addPublicationsToResponse(List<Publication> publications) {
        if(publications.size() > 0){
            for (Publication publication : publications) {
                results.add(new SimpleSearchResponse(publication.getPublicationName(),
                        publication.getPublicationIdentifier(), "publication"));
            }
        }
    }

    public void addDiseasesToResponse(List<Disease> diseases){
        if(diseases.size() > 0){
            for (Disease disease : diseases) {
                results.add(new SimpleSearchResponse(disease.getDiseaseName(), disease.getDiseaseId(), "disease"));
            }
        }
    }

    public List<SimpleSearchResponse> getResults() {
        return results;
    }
}
