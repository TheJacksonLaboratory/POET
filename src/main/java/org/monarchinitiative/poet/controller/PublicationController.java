package org.monarchinitiative.poet.controller;

import com.fasterxml.jackson.annotation.JsonView;
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

@RestController
@RequestMapping(value = "entity/publication/")
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
}
