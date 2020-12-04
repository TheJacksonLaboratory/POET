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

@RestController
@RequestMapping(value = "entity/disease/")
public class DiseaseController {
    private final EntityService entityService;

    public DiseaseController(EntityService entityService) {
        this.entityService = entityService;
    }

    @JsonView(DiseaseViews.Simple.class)
    @GetMapping(value = "/{id}", headers = "Accept=application/json")
    public Disease getDiseaseWithAnnotations(@PathVariable(value="id") String id){
        Disease disease = this.entityService.getDisease(id);
        if(disease != null){
            return this.entityService.getDisease(id);
        } else {
            throw new DiseaseNotFoundException(id);
        }
    }

    @JsonView(PublicationViews.Simple.class)
    @GetMapping(value = "/{id}/publications", headers = "Accept=application/json")
    public List<Publication> getDiseasePublications(@PathVariable(value="id") String id){
        return this.entityService.getDiseasePublications(id);
    }
}
