package org.monarchinitiative.poet.controller.annotation;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.exceptions.AuthenticationException;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.model.requests.PhenotypeRequest;
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

@CrossOrigin
@RestController
@RequestMapping(value = "${api.version}/annotation/phenotypes")
public class PhenotypeController {

    private AnnotationService annotationService;
    private UserService userService;

    public PhenotypeController(AnnotationService annotationService, UserService userService) {
        this.annotationService = annotationService;
        this.userService = userService;
    }

    /**
     * The endpoint to get phenotype annotations
     * @param diseaseId the diseaseId for the creation
     * @param sort the way to sort the response
     * @return a list of phenotype annotations
     */
    @JsonView(AnnotationViews.Simple.class)
    @GetMapping(value = {"/{diseaseId}", "/{diseaseId}/{publicationId}"})
    public List<PhenotypeAnnotation> getPhenotypeAnnotation(@PathVariable("diseaseId")  String diseaseId,
                                                            @RequestParam(defaultValue = "desc date") String sort){

       return this.annotationService.getPhenotypeAnnotationsByDisease(diseaseId, sort);
    }

    /**
     * The endpoint to create a phenotype annotation
     *
     * @param phenotypeRequest a json object in the form of {@link PhenotypeRequest}
     * @return a response entity either created or a server error if we failed to create the phenotype annotation.
     * @since 0.5.0
     */
    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> createPhenotypeAnnotation(@Valid @RequestBody PhenotypeRequest phenotypeRequest, Authentication authentication) {
        final User user = userService.getExistingUser(authentication);
        annotationService.createPhenotypeAnnotation(phenotypeRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * The endpoint to update a phenotype annotation
     *
     * @param phenotypeRequest a json object in the form of {@link PhenotypeRequest}
     * @return a response entity either created or a server error if we failed to create the maxo annotation.
     * @since 0.5.0
     */
    @PutMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> updatePhenotypeAnnotation(@Valid @RequestBody PhenotypeRequest phenotypeRequest,
                                                       @RequestParam(value = "review", defaultValue = "") String review,
                                                       Authentication authentication) {
        final User user = userService.getExistingUser(authentication);
        annotationService.updatePhenotypeAnnotation(phenotypeRequest, user, review);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * The endpoint to delete a phenotype annotation
     *
     * @param id the id to be deleted
     * @return a response entity ok if deleted or error if not.
     * @since 0.5.0
     */
    @DeleteMapping(value = "/{id}", headers = "Accept=application/json")
    public ResponseEntity<?> deletePhenotypeAnnotation(@PathVariable Long id, Authentication authentication) {
        final User user = userService.getExistingUser(authentication);
        if(annotationService.deletePhenotypeAnnotation(id, user)){
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
