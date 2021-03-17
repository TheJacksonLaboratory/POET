package org.monarchinitiative.poet.controller.disease;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.exceptions.DiseaseNotFoundException;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.service.EntityService;
import org.monarchinitiative.poet.views.DiseaseViews;
import org.monarchinitiative.poet.views.PublicationViews;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is an implementation of Spring's Rest Controller. It provides RESTful API's to get diseases
 * and associated objects
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "${api.version}/entity/disease/")
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

    /**
     * The endpoint to retrieve a disease annotation by disease id.
     *
     * @return a disease object with fields annotated with DiseaseViews in the model.
     *
     * @throws DiseaseNotFoundException if a disease could not be found with the given id.
     * @since 0.5.0
     */
    @JsonView(DiseaseViews.Simple.class)
    @PutMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> newDisease(@RequestBody Disease disease){
        if(!this.entityService.saveNewDisease(disease)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
