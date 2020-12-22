package org.monarchinitiative.poet.controller.annotation;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.exceptions.AnnotationSourceException;
import org.monarchinitiative.poet.model.requests.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.service.AnnotationService;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is an implementation of Spring's Rest Controller. It provides RESTful API's to both create and update
 * Medical Action Ontology annotations for a given disease and publication.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@RestController
@RequestMapping(value = "/annotation/maxo") // /annotation/<type>/<action>
public class MaxoController {

    private AnnotationService annotationService;

    public MaxoController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    /**
     * The endpoint to retrieve a maxo annotation by disease.
     *
     * @param diseaseId a url parameter that is an OMIM disease id
     * @param publicationId an optional url parameter that is a PubMed id to limit results only annotated
     *                      to this publication
     * @param sort an optional query parameter to sort the returned annotations <direction> <field>
     * @return the medical action ontology annotations
     * @throws AnnotationSourceException if required parameter entities could not be found in the database.
     * @since 0.5.0
     */
    @JsonView(AnnotationViews.Simple.class)
    @GetMapping(value = {"/{diseaseId}", "/{diseaseId}/{publicationId}"})
    public List<MaxoAnnotation> getMaxoAnnotation(@PathVariable("diseaseId")  String diseaseId,
                                                  @PathVariable(value = "publicationId", required = false) String publicationId,
                                                  @RequestParam(defaultValue = "desc date") String sort){

        final List<MaxoAnnotation> annotations = this.annotationService.getMaxoAnnotation(diseaseId, publicationId, sort);
        if(annotations != null){
            return annotations;
        } else {
            throw new AnnotationSourceException(publicationId, diseaseId);
        }
    }

    /**
     * The endpoint to retrieve a maxo annotation by disease.
     *
     * @param maxoRequest a json object in the form of {@link org.monarchinitiative.poet.model.MaxoRequest}
     * @return a response entity either created or a server error if we failed to create the maxo annotation.
     * TODO: Add better error handling here.
     * @since 0.5.0
     */
    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> createMaxoAnnotation(@RequestBody MaxoRequest maxoRequest, Authentication authentication) {
        if(annotationService.createMaxoAnnotation(maxoRequest, authentication)){
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/insert", headers = "Accept=application/json")
    public void insert(){
        annotationService.insertTestData();
    }
}
