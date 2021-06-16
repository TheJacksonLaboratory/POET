package org.monarchinitiative.poet.controller.annotation;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.exceptions.AnnotationSourceException;
import org.monarchinitiative.poet.exceptions.AuthenticationException;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.model.requests.TreatmentRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.service.AnnotationService;
import org.monarchinitiative.poet.service.UserService;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * This class is an implementation of Spring's Rest Controller. It provides RESTful API's to both create and update
 * Medical Action Ontology annotations for a given disease and publication.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "${api.version}/annotation/treatments")
public class TreatmentController {

    private final AnnotationService annotationService;
    private final UserService userService;

    public TreatmentController(AnnotationService annotationService, UserService userService) {
        this.annotationService = annotationService;
        this.userService = userService;
    }

    /**
     * The endpoint to retrieve all maxo annotations by a user
     * @since 0.6.0
     */
    @JsonView(AnnotationViews.UserSpecific.class)
    @GetMapping(value = {"/"})
    public List<TreatmentAnnotation> getTreatmentAnnotationByUser(Authentication authentication){
        final User user = userService.getExistingUser(authentication);
        return this.annotationService.getTreatmentAnnotationByUser(user);
    }

    /**
     * The endpoint to retrieve a maxo annotation by disease.
     *
     * @param diseaseId a url parameter that is an OMIM disease id
     * @param sort an optional query parameter to sort the returned annotations <direction> <field>
     * @return the medical action ontology annotations
     * @throws AnnotationSourceException if required parameter entities could not be found in the database.
     * @since 0.5.0
     */
    @JsonView(AnnotationViews.Simple.class)
    @GetMapping(value = {"/{diseaseId}", "/{diseaseId}/{publicationId}"})
    public List<TreatmentAnnotation> getTreatmentAnnotation(@PathVariable("diseaseId")  String diseaseId,
                                                            @RequestParam(defaultValue = "desc date") String sort){

        return this.annotationService.getTreatmentAnnotationsByDisease(diseaseId, sort);
    }

    /**
     * The endpoint to retrieve treatment annotations that need review.
     *
     * @throws AuthenticationException if the user is not an elevated user.
     * @since 0.6.0
     */
    @JsonView(AnnotationViews.Simple.class)
    @GetMapping(value = {"/review"})
    public List<TreatmentAnnotation> getReviewableTreatmentAnnotations(Authentication authentication){
        final User user = userService.getExistingUser(authentication);
        if(user.getCurationRole().equals(CurationRole.ELEVATED_CURATOR)){
            final List<TreatmentAnnotation> annotations = this.annotationService.getReviewableTreatmentAnnotations();
            if(annotations != null){
                return annotations;
            } else {
                return Collections.emptyList();
            }
        } else {
            throw new AuthenticationException(user.getNickname());
        }
    }

    /**
     * The endpoint to create a maxo annotation
     *
     * @param treatmentRequest a json object in the form of {@link TreatmentRequest}
     * @return a response entity either created or a server error if we failed to create the maxo annotation.
     * TODO: Add better error handling here.
     * @since 0.5.0
     */
    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> createTreatmentAnnotation(@Valid @RequestBody TreatmentRequest treatmentRequest, Authentication authentication) {
        final User user = userService.getExistingUser(authentication);
        annotationService.createTreatmentAnnotation(treatmentRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * The endpoint to update a maxo annotation
     *
     * @param treatmentRequest a json object in the form of {@link TreatmentRequest}
     * @return a response entity either created or a server error if we failed to create the maxo annotation.
     * TODO: Add better error handling here.
     * @since 0.5.0
     */
    @PutMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> updateTreatmentAnnotation(@Valid @RequestBody TreatmentRequest treatmentRequest,
                                                       @RequestParam(value = "review", defaultValue = "") String review,
                                                       Authentication authentication) {
        final User user = userService.getExistingUser(authentication);
        annotationService.updateTreatmentAnnotation(treatmentRequest, user, review);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * The endpoint to retrieve a maxo annotation by disease.
     *
     * @param id the id to be deleted
     * @return a response entity ok if deleted or error if not.
     * TODO: Add better error handling here.
     * @since 0.5.0
     */
    @DeleteMapping(value = "/{id}", headers = "Accept=application/json")
    public ResponseEntity<?> deleteTreatmentAnnotation(@PathVariable Long id, Authentication authentication) {
        final User user = userService.getExistingUser(authentication);
        if(annotationService.deleteTreatmentAnnotation(id, user)){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/insert", headers = "Accept=application/json")
    public void insert(){
        annotationService.insertTestData();
    }
}
