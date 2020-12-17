package org.monarchinitiative.poet.controller.publication;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.requests.PublicationRequest;
import org.monarchinitiative.poet.service.EntityService;
import org.monarchinitiative.poet.views.DiseaseViews;
import org.monarchinitiative.poet.views.PublicationViews;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "entity/publication")
public class PublicationController {
    private EntityService entityService;

    public PublicationController(EntityService entityService) {
        this.entityService = entityService;
    }

    @JsonView(PublicationViews.Simple.class)
    @GetMapping(value = "/{id}", headers = "Accept=application/json")
    public Publication getPublicationById(@PathVariable(value="id") String id){
        return entityService.getPublication(id);
    }

    @JsonView(DiseaseViews.Simple.class)
    @GetMapping(value = "/{id}/diseases", headers = "Accept=application/json")
    public List<Disease> getPublicationDiseases(@PathVariable(value="id") String id){
        return this.entityService.getPublicationDiseases(id);
    }

    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> savePublicationToDisease(@RequestBody PublicationRequest publicationRequest,
                                                      Authentication authentication){
        if(!this.entityService.savePublicationToDisease(publicationRequest, authentication)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
