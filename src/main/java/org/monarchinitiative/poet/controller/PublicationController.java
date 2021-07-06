package org.monarchinitiative.poet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.requests.PublicationRequest;
import org.monarchinitiative.poet.service.EntityService;
import org.monarchinitiative.poet.service.UserService;
import org.monarchinitiative.poet.views.DiseaseViews;
import org.monarchinitiative.poet.views.PublicationViews;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is an implementation of Spring's Rest Controller. It provides RESTful API's to get publications
 * and associated objects.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "${api.version}/entity/publication")
public class PublicationController {
    private EntityService entityService;
    private UserService userService;

    public PublicationController(EntityService entityService,  UserService userService) {
        this.entityService = entityService;
        this.userService = userService;
    }

    /**
     * The endpoint to retrieve a publication annotation by publication id.
     *
     * @param id a url parameter that is an PubMed id
     * @return a publication object with fields annotated with PublicationViews in the model.
     * @since 0.5.0
     */
    @JsonView(PublicationViews.Simple.class)
    @GetMapping(value = "/{id}", headers = "Accept=application/json")
    public Publication getPublicationById(@PathVariable(value="id") String id){
        return entityService.getPublication(id);
    }

    /**
     * The endpoint to retrieve a list of disease objects annotated to a publication by publication id.
     *
     * @param id a url parameter that is an PubMed id
     * @return a list of disease objects annotated to a publication
     * @since 0.5.0
     */
    @JsonView(DiseaseViews.Simple.class)
    @GetMapping(value = "/{id}/diseases", headers = "Accept=application/json")
    public List<Disease> getPublicationDiseases(@PathVariable(value="id") String id){
        return this.entityService.getPublicationDiseases(id);
    }

    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> savePublicationToDisease(@RequestBody PublicationRequest publicationRequest,
                                                      Authentication authentication) {
        // Just ensure they are a user
        userService.getExistingUser(authentication);
        // Create the annotation source but do nothing with the response
        this.entityService.createAnnotationSource(publicationRequest);
        return ResponseEntity.ok().build();
    }
}
