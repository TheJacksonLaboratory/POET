package org.monarchinitiative.poet.controller.disease;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.exceptions.DiseaseNotFoundException;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.service.EntityService;
import org.monarchinitiative.poet.views.DiseaseViews;
import org.monarchinitiative.poet.views.PublicationViews;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class is an implementation of Spring's Rest Controller. It provides RESTful API's to get diseases
 * and associated objects
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@RestController
@RequestMapping(value = "entity/disease/")
public class DiseaseController {
    private final EntityService entityService;

    public DiseaseController(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * The endpoint to retrieve a disease annotation by disease id.
     *
     * @param id a url parameter that is an OMIM id
     * @return a disease object with fields annotated with DiseaseViews in the model.
     *
     * @throws DiseaseNotFoundException if a disease could not be found with the given id.
     * @since 0.5.0
     */
    @JsonView(DiseaseViews.Simple.class)
    @GetMapping(value = "/{id}", headers = "Accept=application/json")
    public Disease getDisease(@PathVariable(value="id") String id){
        Disease disease = this.entityService.getDisease(id);
        if(disease != null){
            return disease;
        } else {
            throw new DiseaseNotFoundException(id);
        }
    }

    /**
     * The endpoint to retrieve a disease annotation by disease id.
     *
     * @param id a url parameter that is an OMIM id
     * @return a list of publications annotated to the provided disease id
     * @since 0.5.0
     */
    @JsonView(PublicationViews.Simple.class)
    @GetMapping(value = "/{id}/publications", headers = "Accept=application/json")
    public List<Publication> getDiseasePublications(@PathVariable(value="id") String id){
        return this.entityService.getDiseasePublications(id);
    }
}
